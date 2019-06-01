package wfm.group3.localy.services;

import org.springframework.beans.factory.annotation.Autowired;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.entity.Person;
import wfm.group3.localy.entity.Reservation;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.PersonRepository;
import wfm.group3.localy.repository.ReservationRepository;
import wfm.group3.localy.utils.Enums;

import java.util.*;
import java.util.logging.Logger;

public class RecommenderService {

    private final Logger LOGGER = Logger.getLogger(RecommenderService.class.getName());

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public void getRecommendation(Person person, Vector<Experience> experiences) {
        System.out.println("do stuff here");

    }

    private Map<Enums.ExperienceType, MutableInt> calculateTypeOccurances(Vector<Reservation> reservations) {
        Map<Enums.ExperienceType, MutableInt> resultMap = new HashMap<>();
        for(Reservation reservation : reservations){
            if(!reservation.getAttended()){
                continue;
            }
            Experience experience = experienceRepository.getOne(reservation.getExperienceId());
            if(experience == null){
                LOGGER.info("Experience not available anymore");
                continue;
            }
            MutableInt value = resultMap.get(experience.getType());
            if(value == null){
                resultMap.put(experience.getType(), new MutableInt());
            }else{
                value.increment();
            }
        }
        return resultMap;
    }

    class MutableInt{
        int value = 1;

        void increment(){
            ++value;
        }
        int get() {
            return value;
        }
    }
}
