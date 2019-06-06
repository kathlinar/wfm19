package wfm.group3.localy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.BookedExperiences;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.entity.Person;
import wfm.group3.localy.entity.Reservation;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.PersonRepository;
import wfm.group3.localy.repository.ReservationRepository;
import wfm.group3.localy.utils.Enums;
import wfm.group3.localy.utils.PBKDF2Hasher;
import wfm.group3.localy.utils.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class PersonService {

    private final Logger LOGGER = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PBKDF2Hasher pbkdf2Hasher;

    public boolean isLoginValid(Person p, String password) {
        boolean valid = false;
        if (p != null) {
            valid = pbkdf2Hasher.checkPassword(password, p.getPassword());
        }
        return valid;
    }

    public String hash(String password) {
        return pbkdf2Hasher.hash(password);
    }

    public boolean hasOverlap(String mail, LocalDate date, long experienceId) {
        boolean overlap = false;
        Person p = this.personRepository.findByEmail(mail);

        if (p != null) {
            List<BookedExperiences> bookedExperiences = this.getBookedExperiences(p, date);
            for (BookedExperiences be : bookedExperiences) {
                if (be.getStatus().equals(Enums.ReservationStatus.CONFIRMED)
                        || be.getStatus().equals(Enums.ReservationStatus.CONFIRMED_AND_ATTENDED)
                        || be.getStatus().equals(Enums.ReservationStatus.PENDING)) {
                    Optional<Experience> toReserveOptional = this.experienceRepository.findById(experienceId);
                    LocalDateTime experienceStartAt = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), be.getStartTime().getHour(), be.getStartTime().getMinute());
                    LocalDateTime experienceEndsAt = experienceStartAt.plus(be.getDuration());

                    if (toReserveOptional.isPresent()) {
                        LocalDateTime toReserveExperienceStartsAt = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), toReserveOptional.get().getStartTime().getHour(), toReserveOptional.get().getStartTime().getMinute());
                        LocalDateTime toReserveExperienceEndsAt = toReserveExperienceStartsAt.plus(toReserveOptional.get().getDuration());
                        overlap = Utils.max(experienceStartAt, toReserveExperienceStartsAt).isBefore(Utils.min(experienceEndsAt, toReserveExperienceEndsAt));
                        if (overlap) {
                            LOGGER.info("overlap found for " + toReserveOptional.get() + " at " + experienceEndsAt);
                        }
                    }
                }
            }
        }

        return overlap;
    }

    public List<BookedExperiences> getBookedExperiences(Person p) {
        List<BookedExperiences> result = new ArrayList<>();

        if (p != null) {
            for (Reservation reservation : this.reservationRepository.findReservationsByPersonId(p.getId())) {
                result.add(new BookedExperiences(this.experienceRepository.getOne(reservation.getExperienceId()), reservation));
            }
        }
        return result;
    }

    public List<BookedExperiences> getBookedExperiences(Person p, LocalDate date) {
        List<BookedExperiences> result = new ArrayList<>();

        if (p != null) {
            for (Reservation reservation : this.reservationRepository.findByReservationDateAndPersonId(date, p.getId())) {
                result.add(new BookedExperiences(this.experienceRepository.getOne(reservation.getExperienceId()), reservation));
            }
        }
        return result;
    }

}
