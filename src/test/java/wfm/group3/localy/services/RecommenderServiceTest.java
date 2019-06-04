package wfm.group3.localy.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.entity.Person;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.PersonRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecommenderServiceTest {

    @Autowired
    private RecommenderService recommenderService;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testGetRecommendation(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = "2019-08-10";
        LocalDate date = LocalDate.parse(dateStr, formatter);
        List<Experience> allExperiences = experienceRepository.findExperiencesByNoReservationYet(date);
        Person person = personRepository.getOne(3L);
        Experience[] experiences = recommenderService.getRecommendation(person, allExperiences.toArray(new Experience[]{}),date);
        for(Experience exp : experiences){
            System.out.println(exp);
        }
    }
}
