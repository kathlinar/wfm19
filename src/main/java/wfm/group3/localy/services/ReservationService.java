package wfm.group3.localy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.entity.Person;
import wfm.group3.localy.entity.Reservation;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.PersonRepository;
import wfm.group3.localy.repository.ReservationRepository;
import wfm.group3.localy.utils.JavaMailUtil;

import java.time.LocalDateTime;
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


    public void makeReservation(Experience experience, Person person, LocalDateTime date, String processDefinitionId) {
        Reservation reservation = new Reservation();
        reservation.setPersonId(person.getId());
        reservation.setExperienceId(experience.getId());
        reservation.setReservationDate(date);
        reservation.setProcessDefinitionId(processDefinitionId);

        reservationRepository.save(reservation);

        String detail = experience.getName() + ", on " + date;

        try {
            JavaMailUtil.sendMail(person.getEmail(),detail,"User Confirmation");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
