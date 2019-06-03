package wfm.group3.localy.services;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.entity.Person;
import wfm.group3.localy.entity.Reservation;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.PersonRepository;
import wfm.group3.localy.repository.ReservationRepository;
import wfm.group3.localy.utils.Enums;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    public List<Experience> retrieveExperiences(LocalDate date) {
        return experienceRepository.findExperiencesByNoReservationYet(date);
    }

    public long makeReservation(Long experienceId, String email, LocalDate date, String processInstanceId) {
        return makeReservation(experienceId, email, date, processInstanceId, Enums.ReservationStatus.PENDING);
    }

    public long makeReservation(Long experienceId, String email, LocalDate date, String processInstanceId, Enums.ReservationStatus status) {
        Long id = null;
        Optional<Experience> experience = this.experienceRepository.findById(experienceId);
        Person person = this.personRepository.findByEmail(email);
        LOGGER.info(experience.toString());
        LOGGER.info(person.toString());
        if (experience.isPresent() && person != null) {
            id = this.makeReservation(experience.get(), person, date, processInstanceId, status);
        }
        return id;
    }

    private Long makeReservation(Experience experience, Person person, LocalDate date, String processInstanceId, Enums.ReservationStatus status) {
        LOGGER.info(experience.toString());
        LOGGER.info(person.toString());
        LOGGER.info(date.toString());
        LOGGER.info(processInstanceId);
        LOGGER.info(status.name());
        Reservation reservation = new Reservation();
        reservation.setPersonId(person.getId());
        reservation.setExperienceId(experience.getId());
        reservation.setReservationDate(date);
        reservation.setProcessInstanceId(processInstanceId);
        reservation.setStatus(status);

        reservation = this.reservationRepository.saveAndFlush(reservation);
        return reservation.getReservationId();
    }

    public boolean isGroupFull(long experienceId, LocalDate date) {
        boolean full = false;

        Reservation reservation = this.reservationRepository.findFirstByExperienceIdAndReservationDateAndStatus(experienceId, date, Enums.ReservationStatus.CONFIRMED);

        if (reservation != null) {
            Optional<Experience> experienceOptional = this.experienceRepository.findById(experienceId);
            if (experienceOptional.isPresent()) {
                if (this.reservationRepository.getGroupSizeOfReservation(experienceId, date) == experienceOptional.get().getMaxGroupSize()) {
                    full = true;
                }
            }
        }

        return full;
    }
}
