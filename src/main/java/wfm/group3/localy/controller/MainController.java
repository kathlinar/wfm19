package wfm.group3.localy.controller;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.authorization.*;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.entity.ExperienceFrontend;
import wfm.group3.localy.entity.Person;
import wfm.group3.localy.repository.PersonRepository;
import wfm.group3.localy.utils.Enums;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;

@Controller
public class MainController {

    private Map<String, ProcessInstance> customerInstances = new HashMap<>();

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TaskService taskService;

    private Map<String, List<Experience>> lastRecommendedExperiences = new HashMap<>();

    @EventListener
    private void processPostDeploy(PostDeployEvent event) {
        System.out.println(event);
        //runtimeService.startProcessInstanceByKey("loanApproval");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public ResponseEntity createNewUser(@RequestBody Map<String, Object> payload){

        Person person = new Person();
        person.setFirstName(payload.get("firstName").toString());
        person.setLastName(payload.get("lastName").toString());
        person.setEmail(payload.get("mail").toString());
        person.setPassword(payload.get("password").toString());
        person.setType(Enums.PersonType.CUSTOMER);
        if( this.personRepository.findByEmail(person.getEmail()) == null){
            Person p = this.personRepository.saveAndFlush(person);
            //createCamundaUser(p);
            return new ResponseEntity(HttpStatus.OK);
        }else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody Map<String,Object> payload) {
        Person person = this.personRepository.findByEmail(payload.get("email").toString());
        if( person != null &&  person.getPassword().equals(payload.get("password").toString())) {

            if (!this.customerInstances.containsKey(person.getEmail()))
                this.customerInstances.put(person.getEmail(), this.runtimeService.createProcessInstanceByKey("Customer")
                    .setVariables(payload)
                    .execute());

            List<Task> tasks = this.taskService.createTaskQuery()
                    .processDefinitionId(this.customerInstances.get(person.getEmail()).getProcessDefinitionId()).list();

            this.taskService.complete(tasks.get(0).getId());


            return new ResponseEntity(HttpStatus.OK);
        }else
            return new ResponseEntity(HttpStatus.FAILED_DEPENDENCY);
    }


    @RequestMapping(value = "/selectDate", method = RequestMethod.POST)
    public ResponseEntity selectDate(@RequestBody Map<String ,Object> payload){
        List<Task> tasks = this.taskService.createTaskQuery()
                .processDefinitionId(this.customerInstances.get(payload.get("email").toString()).getProcessDefinitionId()).list();


        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String text = payload.get("date").toString().substring(0,payload.get("date").toString().indexOf("."));
        LocalDate localDate = LocalDate.parse(text,formatter);

        this.runtimeService.setVariable(this.customerInstances.get(payload.get("email").toString()).getId(),"date",Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

        this.taskService.complete(tasks.get(0).getId());

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/makeReservation", method = RequestMethod.POST)
    public ResponseEntity makeReservation(@RequestBody Map<String ,Object> payload){
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionId(this.customerInstances.get(payload.get("email").toString()).getProcessDefinitionId()).list();

        Experience[] experiences = (Experience[]) runtimeService.getVariable(this.customerInstances.get(payload.get("email")).getId(),"allExperiences");
        System.out.println(experiences[0]);
        taskService.complete(tasks.get(0).getId());
        return new ResponseEntity(HttpStatus.OK);
    }
    @RequestMapping(value = "/getExperiences/{email}", method = RequestMethod.GET)
    public ResponseEntity<List<ExperienceFrontend>> getExperiences(@PathVariable("email") String email){
        if(!this.lastRecommendedExperiences.isEmpty()){
            List<ExperienceFrontend> result = new ArrayList<>();
            for(Experience experience : this.lastRecommendedExperiences.get(email)){
                result.add(new ExperienceFrontend(experience));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.LOCKED);
    }

    public void getRecommendedExperiences(List<Experience> experiences, String email){
        this.lastRecommendedExperiences.put(email,experiences);
    }

    @RequestMapping(value = "/postExperiences", method = RequestMethod.POST)
    public void postRecommendedExperiences(@RequestBody Map<String ,Object> payload){
        this.lastRecommendedExperiences.put(payload.get("email").toString(),(List<Experience>)payload.get("experiences"));
    }

    private void createCamundaUser(Person p) {
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

    }

}
