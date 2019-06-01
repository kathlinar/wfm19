package wfm.group3.localy.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExperienceRepositoryTest {

    @Autowired
    ExperienceRepository experienceRepository;

    @Test
    public void testFindAll(){
        System.out.println(experienceRepository.findAll());
    }
}
