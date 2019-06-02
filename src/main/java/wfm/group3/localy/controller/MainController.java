package wfm.group3.localy.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wfm.group3.localy.entity.*;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.PersonRepository;
import wfm.group3.localy.repository.ReservationRepository;
import wfm.group3.localy.services.PersonService;
import wfm.group3.localy.services.ReservationService;
import wfm.group3.localy.utils.Enums;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class MainController {

    private Map<String, List<ProcessInstance>> customerInstances = new HashMap<>();

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private TaskService taskService;

    private Map<String, List<Experience>> lastRecommendedExperiences = new HashMap<>();

    private Map<String, LocalDateTime> lastSelectedDate = new HashMap<>();


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public ResponseEntity start(@RequestBody Map<String, Object> payload) {
        Person person = this.personRepository.findByEmail(payload.get("email").toString());
        if (person != null) {
            if (this.customerInstances.containsKey(person.getEmail())) {
                this.customerInstances.get(person.getEmail()).add(this.runtimeService.createProcessInstanceByKey("Customer")
                        .setVariables(payload)
                        .setVariable("loggedIn", true)
                        .execute());
                this.lastRecommendedExperiences.remove(payload.get("email").toString());
                this.lastSelectedDate.remove(payload.get("email").toString());
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.FAILED_DEPENDENCY);
    }

    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public ResponseEntity createNewUser(@RequestBody Map<String, Object> payload) {

        Person person = new Person();
        person.setFirstName(payload.get("firstName").toString());
        person.setLastName(payload.get("lastName").toString());
        person.setEmail(payload.get("mail").toString());
        person.setPassword(this.personService.hash(payload.get("password").toString()));
        person.setType(Enums.PersonType.CUSTOMER);
        if (this.personRepository.findByEmail(person.getEmail()) == null) {
            this.personRepository.saveAndFlush(person);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody Map<String, Object> payload) {
        Person person = this.personRepository.findByEmail(payload.get("email").toString());

        if (this.personService.isLoginValid(person, payload.get("password").toString())) {
            if (!this.customerInstances.containsKey(person.getEmail())) {
                List<ProcessInstance> list = new ArrayList();
                list.add(this.runtimeService.createProcessInstanceByKey("Customer")
                        .setVariables(payload)
                        .setVariable("loggedIn", false)
                        .execute());
                this.customerInstances.put(person.getEmail(), list);
            }
            List<Task> tasks = this.taskService.createTaskQuery()
                    .processDefinitionId(this.customerInstances.get(person.getEmail()).get(this.customerInstances.get(person.getEmail()).size() - 1).getProcessDefinitionId()).list();

            if (tasks.get(0).getName().equals("Log in with mail"))
                this.taskService.complete(tasks.get(0).getId());

            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.FAILED_DEPENDENCY);
    }


    @RequestMapping(value = "/selectDate", method = RequestMethod.POST)
    public ResponseEntity selectDate(@RequestBody Map<String, Object> payload) {
        int posNewestInstance = this.customerInstances.get(payload.get("email").toString()).size() - 1;
        List<Task> tasks = this.taskService.createTaskQuery()
                .processDefinitionId(this.customerInstances.get(payload.get("email").toString()).get(posNewestInstance).getProcessDefinitionId()).list();


        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String text = payload.get("date").toString().substring(0,payload.get("date").toString().indexOf("."));
        LocalDate localDate = LocalDate.parse(text,formatter);

        this.lastSelectedDate.put(payload.get("email").toString(),LocalDateTime.parse(text,formatter));
        this.runtimeService.setVariable(this.customerInstances.get(payload.get("email").toString()).get(posNewestInstance).getId(),"date",Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

        if(tasks.get(0).getName().equals("Select Date"))
            this.taskService.complete(tasks.get(0).getId());
        else{
            this.runtimeService.setVariable(this.customerInstances.get(payload.get("email").toString()).get(posNewestInstance).getId(),"searchCancelled",true);
            this.taskService.complete(tasks.get(0).getId());

            tasks = this.taskService.createTaskQuery()
                    .processDefinitionId(this.customerInstances.get(payload.get("email").toString()).get(posNewestInstance).getProcessDefinitionId()).list();

            this.runtimeService.setVariable(this.customerInstances.get(payload.get("email").toString()).get(posNewestInstance).getId(),"date",Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

            if(tasks.get(0).getName().equals("Select Date"))
                this.taskService.complete(tasks.get(0).getId());

        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/cancelReservation", method = RequestMethod.POST)
    public ResponseEntity cancelReservation(@RequestBody Map<String, Object> payload){

        this.runtimeService.createMessageCorrelation("InitUserCancellation").correlate();

        String processDefId = this.reservationRepository
                .findByReservationDate(LocalDateTime.parse(payload.get("date").toString().replace("T"," "),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .get(0)
                .getProcessDefinitionId();

        for(ProcessInstance processInstance : this.customerInstances.get(payload.get("email").toString())){
            if(processInstance.getProcessDefinitionId().equals(processDefId)){
                this.customerInstances.get(payload.get("email").toString()).remove(processInstance);
            }
        }

        //TODO set status of reservation to canceled

        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(value = "/attendExperience", method = RequestMethod.POST)
    public ResponseEntity attendExperience(@RequestBody Map<String, Object> payload){

        String processDefId = this.reservationRepository
                .findByReservationDate(LocalDateTime.parse(payload.get("date").toString().replace("T"," "),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .get(0)
                .getProcessDefinitionId();


        //TODO set status of reservation to attended

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/makeReservation", method = RequestMethod.POST)
    public ResponseEntity makeReservation(@RequestBody Map<String, Object> payload) {

        Person person = this.personRepository.findByEmail(payload.get("email").toString());
        int posNewestInstance = this.customerInstances.get(person.getEmail()).size() - 1;

        Experience experience = this.experienceRepository.getOne(Long.parseLong(payload.get("id").toString()));

        this.reservationService.makeReservation(experience, person, this.lastSelectedDate.get(payload.get("email").toString()), this.customerInstances.get(person.getEmail()).get(posNewestInstance).getProcessDefinitionId());

        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionId(this.customerInstances.get(person.getEmail()).get(posNewestInstance).getProcessDefinitionId()).list();

        this.runtimeService.setVariable(this.customerInstances.get(person.getEmail()).get(posNewestInstance).getId(), "searchCancelled", false);

        if (tasks.get(0).getName().equals("Choose desired experiences"))
            this.taskService.complete(tasks.get(0).getId());

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/getExperiences/{email}", method = RequestMethod.GET)
    public ResponseEntity<List<ExperienceFrontend>> getExperiences(@PathVariable("email") String email) {
        if (!this.lastRecommendedExperiences.isEmpty()) {
            List<ExperienceFrontend> result = new ArrayList<>();
            for (Experience experience : this.lastRecommendedExperiences.get(email)) {
                result.add(new ExperienceFrontend(experience));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.LOCKED);
    }

    public void getRecommendedExperiences(List<Experience> experiences, String email) {
        this.lastRecommendedExperiences.put(email, experiences);
    }

    @RequestMapping(value = "/getBookedExperiences/{email}", method = RequestMethod.GET)
    public ResponseEntity<List<BookedExperiences>> getBookedExperiences(@PathVariable("email") String email) {
        Person person = this.personRepository.findByEmail(email);
        if (person != null) {
            List<BookedExperiences> result = new ArrayList<>();
            for (Reservation reservation : this.reservationRepository.findReservationsByPersonId(person.getId())) {
                result.add(new BookedExperiences(this.experienceRepository.getOne(reservation.getExperienceId()), reservation));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    /*private void createCamundaUser(Person p) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        AuthorizationService authorizationService = processEngine.getAuthorizationService();
        IdentityService identityService = processEngine.getIdentityService();

        User user = identityService.newUser(p.getId().toString());
        user.setFirstName(p.getFirstName());
        user.setLastName(p.getLastName());
        user.setPassword(p.getPassword());
        user.setEmail(p.getEmail());
        identityService.saveUser(user);

        if(identityService.createGroupQuery().groupId("customers").count() == 0) {
            Group customersGroup = identityService.newGroup("customers");
            customersGroup.setName("customers");
            identityService.saveGroup(customersGroup);
        }

        Authorization auth = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
        auth.setGroupId("customers");
        auth.setResource(Resources.GROUP);
        auth.setResourceId("*");

        auth.addPermission(Permissions.READ);
        authorizationService.saveAuthorization(auth);


        Authorization auth1 = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
        auth1.setGroupId("customers");
        auth1.setResourceId("*");
        auth1.setResource(Resources.PROCESS_INSTANCE);
        auth1.addPermission(Permissions.ALL);
        authorizationService.saveAuthorization(auth1);


        // create group
        identityService.createMembership(p.getId().toString(), "customers");

    }*/

}
