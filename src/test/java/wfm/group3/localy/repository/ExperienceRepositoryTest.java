package wfm.group3.localy.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExperienceRepositoryTest {

    @Autowired
    ExperienceRepository experienceRepository;

    @Test
    public void testFindAll(){
        System.out.println(experienceRepository.findAll());
    }

    @Test
    public void testFindExperiencesByNoReservationYet() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = "2019-06-10";
        LocalDate date = LocalDate.parse(dateStr, formatter);
        System.out.println(experienceRepository.findExperiencesByNoReservationYet(date));
    }
}
