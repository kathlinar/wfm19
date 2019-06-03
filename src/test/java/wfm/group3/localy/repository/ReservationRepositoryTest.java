package wfm.group3.localy.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wfm.group3.localy.entity.Reservation;
import wfm.group3.localy.utils.Enums;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Before
    public void initialize() {
        Reservation res1 = new Reservation();
        res1.setPersonId(1L);
        res1.setExperienceId(1L);
        res1.setAttended(false);
        res1.setStatus(Enums.ReservationStatus.CANCELLED);
        res1.setFeedback("Test feedback");
        res1.setReservationDate(LocalDate.now());

        Reservation res2 = new Reservation();
        res2.setPersonId(1L);
        res2.setExperienceId(2L);
        res2.setAttended(true);
        res1.setStatus(Enums.ReservationStatus.CANCELLED);
        res2.setFeedback("Test feedback");
        res2.setReservationDate(LocalDate.now());

        //reservationRepository.save(res1);
        //reservationRepository.save(res2);
    }

    @Test
    public void testFindReservationsByPersonId() {
        assertEquals(reservationRepository.findReservationsByPersonId(1L).size(), 2);
    }

    @Test
    public void testGetGroupSizeOfReservation() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = "2019-06-10";
        LocalDate date = LocalDate.parse(dateStr, formatter);
        System.out.println(reservationRepository.getGroupSizeOfReservation(10L, date));
    }
}
