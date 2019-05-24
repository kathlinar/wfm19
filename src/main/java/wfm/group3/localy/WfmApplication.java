package wfm.group3.localy;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableProcessApplication
public class WfmApplication {

    public static void main(String... args) {
        SpringApplication.run(WfmApplication.class, args);
    }

    @Autowired
    private RuntimeService runtimeService;

    @EventListener
    private void processPostDeploy(PostDeployEvent event) {
        //runtimeService.startProcessInstanceByKey("loanApproval");
    }
}