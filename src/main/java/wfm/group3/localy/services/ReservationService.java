package wfm.group3.localy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.PersonRepository;
import wfm.group3.localy.repository.ReservationRepository;

import java.util.List;
import java.util.logging.Logger;

@Component
public class ReservationService {

    private final Logger LOGGER = Logger.getLogger(ReservationService.class.getName());

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    public List<Experience> retrieveExperiences() {
        return experienceRepository.findAll();
    }

}
