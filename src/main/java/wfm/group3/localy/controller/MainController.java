package wfm.group3.localy.controller;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.authorization.*;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.entity.Person;
import wfm.group3.localy.repository.PersonRepository;
import wfm.group3.localy.utils.Enums;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;

@Controller
public class MainController {

    private ProcessInstance customerInstance;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TaskService taskService;

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
            createCamundaUser(p);
            return new ResponseEntity(HttpStatus.OK);
        }else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody Map<String,Object> payload) {

       this.customerInstance = runtimeService.createProcessInstanceByKey("Customer")
               .setVariables(payload)
               .execute();

       List<Task> tasks = taskService.createTaskQuery()
               .processDefinitionId(this.customerInstance.getProcessDefinitionId()).list();

        taskService.complete(tasks.get(0).getId());


       return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(value = "/selectDate", method = RequestMethod.POST)
    public ResponseEntity selectDate(@RequestBody Map<String ,Object> payload){
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionId(this.customerInstance.getProcessDefinitionId()).list();


        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String text = payload.get("date").toString().substring(0,payload.get("date").toString().indexOf("."));
        LocalDate localDate = LocalDate.parse(text,formatter);
        //DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        runtimeService.setVariable(this.customerInstance.getId(),"date",Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        taskService.complete(tasks.get(0).getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/makeReservation", method = RequestMethod.POST)
    public ResponseEntity makeReservation(@RequestBody Map<String ,Object> payload){
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionId(this.customerInstance.getProcessDefinitionId()).list();

        Experience[] experiences = (Experience[]) runtimeService.getVariable(this.customerInstance.getId(),"allExperiences");
        System.out.println(experiences[0]);
        taskService.complete(tasks.get(0).getId());
        return new ResponseEntity(HttpStatus.OK);
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
