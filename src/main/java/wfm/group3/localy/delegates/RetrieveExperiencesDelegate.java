package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.controller.MainController;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.services.ReservationService;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

@Component("retrieveExperiencesDelegate")
public class RetrieveExperiencesDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(RetrieveExperiencesDelegate.class.getName());

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MainController mainController;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Retrieving experiences");
        List<Experience> experiences = this.reservationService.retrieveExperiences();
        Experience[] arr = new Experience[experiences.size()];
        experiences.toArray(arr);
        LOGGER.info("Found " + experiences.size() +  " experiences.");
        this.mainController.getRecommendedExperiences(experiences,delegateExecution.getVariable("email").toString());
/*
        URL url = new URL("http://localhost:8081/postExperiences");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject outputField = new JSONObject();
        outputField.put("email", delegateExecution.getVariable("email").toString());
        outputField.put("experiences", experiences);

        OutputStream os = conn.getOutputStream();
        os.write(outputField.toString().getBytes());
        os.flush();*/

        delegateExecution.setVariable("Experiences", arr);
    }
}
