package wfm.group3.localy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Person;
import wfm.group3.localy.repository.PersonRepository;
import wfm.group3.localy.utils.PBKDF2Hasher;

import java.util.logging.Logger;

@Component
public class PersonService {

    private final Logger LOGGER = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PBKDF2Hasher pbkdf2Hasher;

    public boolean isLoginValid(Person p, String password) {
        boolean valid = false;
        if (p != null) {
            LOGGER.info("hashed " + password + " is " + hash(password));
            valid = pbkdf2Hasher.checkPassword(password, p.getPassword());
        }
        return valid;
    }

    public String hash(String password) {
        return pbkdf2Hasher.hash(password);
    }

}
