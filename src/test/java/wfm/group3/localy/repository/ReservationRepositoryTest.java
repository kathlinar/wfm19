package wfm.group3.localy.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wfm.group3.localy.entity.Reservation;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Before
    public void initialize(){
        Reservation res1 = new Reservation();
        res1.setPersonId(1L);
        res1.setExperienceId(1L);
        res1.setAttended(false);
        res1.setDeleted(false);
        res1.setFeedback("Test feedback");
        res1.setReservationDate(LocalDateTime.now());

        Reservation res2 = new Reservation();
        res2.setPersonId(1L);
        res2.setExperienceId(2L);
        res2.setAttended(true);
        res2.setDeleted(false);
        res2.setFeedback("Test feedback");
        res2.setReservationDate(LocalDateTime.now());

        reservationRepository.save(res1);
        reservationRepository.save(res2);
    }

    @Test
    public void testFindReservationsByPersonId(){
        assertEquals(reservationRepository.findReservationsByPersonId(1L).size(),2);
    }
}