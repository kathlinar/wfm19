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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
public class MainController {

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

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

    private Map<String, List<ProcessInstance>> customerInstances = new HashMap<>();

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
            } else {
                String processInstanceId = this.processIdHelper(person.getEmail());
                List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstanceId).list();
                if (!tasks.isEmpty() && tasks.get(0).getName().equals("Select Date")) {
                    this.runtimeService.deleteProcessInstance(processInstanceId, "");
                    this.customerInstances.get(person.getEmail()).remove(this.customerInstances.get(person.getEmail()).get(this.customerInstances.get(person.getEmail()).size() - 1));
                }
                this.customerInstances.get(person.getEmail()).add(this.runtimeService.createProcessInstanceByKey("Customer")
                        .setVariables(payload)
                        .setVariable("loggedIn", false)
                        .execute());
            }
            List<Task> tasks = this.taskService.createTaskQuery()
                    .processInstanceId(this.customerInstances.get(person.getEmail()).get(this.customerInstances.get(person.getEmail()).size() - 1).getId()).list();

            if (tasks.get(0).getName().equals("Log in with mail"))
                this.taskService.complete(tasks.get(0).getId());

            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.FAILED_DEPENDENCY);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity logout(@RequestBody Map<String, Object> payload) {
        int posNewestInstance = this.customerInstances.get(payload.get("email").toString()).size() - 1;
        this.runtimeService.deleteProcessInstance(this.customerInstances.get(payload.get("email").toString()).get(posNewestInstance).getId(), "");
        this.customerInstances.get(payload.get("email").toString()).remove(this.customerInstances.get(payload.get("email").toString()).get(posNewestInstance));

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/selectDate", method = RequestMethod.POST)
    public ResponseEntity selectDate(@RequestBody Map<String, Object> payload) {
        int posNewestInstance = this.customerInstances.get(payload.get("email").toString()).size() - 1;
        ProcessInstance processInstance = this.customerInstances.get(payload.get("email").toString()).get(posNewestInstance);

        List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        String text = payload.get("date").toString().substring(0, payload.get("date").toString().indexOf("."));
        LocalDateTime localDate = LocalDateTime.parse(text);

        this.lastSelectedDate.put(payload.get("email").toString(), localDate);
        this.runtimeService.setVariable(processInstance.getId(), "date", formatter.format(localDate));

        if (tasks.get(0).getName().equals("Select Date"))
            this.taskService.complete(tasks.get(0).getId());
        else {
            this.runtimeService.setVariable(processInstance.getId(), "searchCancelled", true);
            this.taskService.complete(tasks.get(0).getId());

            tasks = this.taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
            this.runtimeService.setVariable(processInstance.getId(), "date", formatter.format(localDate));

            if (tasks.get(0).getName().equals("Select Date"))
                this.taskService.complete(tasks.get(0).getId());

        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/cancelReservation", method = RequestMethod.POST)
    public ResponseEntity cancelReservation(@RequestBody Map<String, Object> payload) {


        String processInstanceId = this.reservationRepository
                .findByReservationDate(LocalDate.parse(payload.get("date").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .get(0)
                .getProcessInstanceId();

        // this.runtimeService.setVariable(processDefId,"canceled",true);
        System.out.println(payload.get("reservationId"));

        for (ProcessInstance processInstance : this.customerInstances.get(payload.get("email").toString())) {
            if (processInstance.getId().equals(processInstanceId)) {
                this.customerInstances.get(payload.get("email").toString()).remove(processInstance);
            }
        }

        this.runtimeService.createMessageCorrelation("InitUserCancellation")
                .setVariable("reservationId", payload.get("reservationId").toString())
                .processInstanceId(processInstanceId)
                .correlate();


        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/checkLoginState/{email}", method = RequestMethod.GET)
    public ResponseEntity checkLogin(@PathVariable("email") String email) {
        if (!email.isEmpty()) {
            if (this.customerInstances.containsKey(email) && this.customerInstances.get(email).size() > 0) {
                List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(this.customerInstances.get(email).get(this.customerInstances.get(email).size() - 1).getId()).list();

                if (tasks.size() == 1) {
                    if (!tasks.get(0).getCreateTime().before(Date.from(Instant.now().minus(1, ChronoUnit.HOURS)))) {
                        return new ResponseEntity(HttpStatus.OK);
                    } else {
                        this.runtimeService.deleteProcessInstance(this.customerInstances.get(email).get(this.customerInstances.get(email).size() - 1).getId(), "");
                        this.customerInstances.get(email).remove(this.customerInstances.get(email).get(this.customerInstances.get(email).size() - 1));
                    }
                }
            }

        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/attendExperience", method = RequestMethod.POST)
    public ResponseEntity attendExperience(@RequestBody Map<String, Object> payload) {

        String email = payload.get("email").toString();


        Optional<Reservation> reservationOptional = this.reservationRepository.findById(Long.parseLong(payload.get("reservationId").toString()));

        if (reservationOptional.isPresent()) {
            String processInstanceId = reservationOptional.get().getProcessInstanceId();
            this.reservationRepository.updateAttended(Long.parseLong(payload.get("reservationId").toString()), true);
            this.reservationRepository.updateStatus(Long.parseLong(payload.get("reservationId").toString()), Enums.ReservationStatus.CONFIRMED_AND_ATTENDED);


            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();

            this.runtimeService.setVariable(processInstanceId, "email", email);
            this.runtimeService.setVariable(processInstanceId, "reservationId", payload.get("reservationId").toString());
            this.runtimeService.setVariable(processInstanceId, "processInstanceId", processInstanceId);

            if (tasks.get(0).getName().equals("Attend Event/Experience"))
                this.taskService.complete(tasks.get(0).getId());

            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }
    /*@RequestMapping(value = "/checkAttended", method = RequestMethod.POST)
    public ResponseEntity <List<Boolean>> checkAttended(@RequestBody Map<String, Object> payload) {
        String email = payload.get("email").toString();
        Person pers = this.personRepository.findByEmail(email);
        List <Reservation> list = reservationRepository.findReservationsByPersonId(Long.parseLong(pers.getId().toString()));
        List<Boolean> result = new ArrayList<>();
        for (Reservation res: list) {
            result.add(res.getAttended());
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }*/

    @RequestMapping(value = "/sendFeedback", method = RequestMethod.POST)
    public ResponseEntity sendFeedback(@RequestBody Map<String, Object> payload) {
        String email = payload.get("email").toString();

        Optional<Reservation> reservationOptional = this.reservationRepository.findById(Long.parseLong(payload.get("reservationId").toString()));
        if (reservationOptional.isPresent()) {
            this.reservationRepository.setFeedback(reservationOptional.get().getReservationId(), payload.get("feedback").toString());
            /*Person pers = this.personRepository.findByEmail(email);
            List<Reservation> list = reservationRepository.findReservationsByPersonId(Long.parseLong(pers.getId().toString()));
            for (Reservation res : list) {
                System.out.println(res.getReservationId() + ": " + res.getFeedback());
            }

            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
            if (tasks.get(0).getName().equals("Feedback about experience"))
                this.taskService.complete(tasks.get(0).getId());*/

            this.runtimeService.createMessageCorrelation("UserFeedback")
                    .correlate();

            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/makeReservation", method = RequestMethod.POST)
    public ResponseEntity makeReservation(@RequestBody Map<String, Object> payload) {
        String email = payload.get("email").toString();
        int posNewestInstance = this.customerInstances.get(email).size() - 1;
        ProcessInstance processInstance = this.customerInstances.get(email).get(posNewestInstance);

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        this.runtimeService.setVariable(processInstance.getId(), "searchCancelled", false);
        this.runtimeService.setVariable(processInstance.getId(), "email", email);
        this.runtimeService.setVariable(processInstance.getId(), "date", formatter.format(this.lastSelectedDate.get(email)));
        this.runtimeService.setVariable(processInstance.getId(), "processInstanceId", processInstance.getId());
        this.runtimeService.setVariable(processInstance.getId(), "experienceToReserve", payload.get("id").toString());

        System.out.println("RESERVATION: " + tasks.get(0).getName());

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
            List<BookedExperiences> result = this.personService.getBookedExperiences(person);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private String processIdHelper(String email) {
        int posNewestInstance = this.customerInstances.get(email).size() - 1;
        return this.customerInstances.get(email).get(posNewestInstance).getId();
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
