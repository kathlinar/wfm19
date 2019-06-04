package wfm.group3.localy.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wfm.group3.localy.entity.Experience;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        String dateStr = "2019-08-10";
        LocalDate date = LocalDate.parse(dateStr, formatter);
        List<Experience> experienceList = experienceRepository.findExperiencesByNoReservationYet(date);
        for(Experience exp : experienceList){
            System.out.println(exp);
        }
    }

    @Test
    public void testFindTopTenExperiences() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = "2019-08-10";
        LocalDate date = LocalDate.parse(dateStr, formatter);
        List<Experience> experienceList = experienceRepository.findTopExperiences(date);
        for(Experience exp : experienceList){
            System.out.println(exp);
        }
    }
}
