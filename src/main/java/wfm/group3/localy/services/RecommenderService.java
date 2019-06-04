package wfm.group3.localy.services;

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
import java.util.*;
import java.util.logging.Logger;

@Component
public class RecommenderService {

    private final Logger LOGGER = Logger.getLogger(RecommenderService.class.getName());

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public Experience[] getRecommendation(Person person, Experience[] experiences, LocalDate date) {
        List<Experience> topExperiences = experienceRepository.findTopExperiences(date);
        if(person != null && experiences != null){
            LOGGER.info("do stuff here with " + person.getEmail() + " and " + experiences.length + " experiences.");
            List<Reservation> personReservations = reservationRepository.findReservationsByPersonId(person.getId());
            LOGGER.info("Found " + personReservations.size() + " reservations.");
            Map<Enums.ExperienceType, MutableInt> typeOccurances = calculateTypeOccurences(personReservations);
            Enums.ExperienceType favoriteType = Enums.ExperienceType.getRandom();
            int favoriteCount = 0;
            for(Map.Entry<Enums.ExperienceType, MutableInt> entry : typeOccurances.entrySet()){
                LOGGER.info("Found " + entry.getValue().get() + " entries for type " + entry.getKey().name());
                if(entry.getValue().get() > favoriteCount){
                    favoriteCount = entry.getValue().get();
                    favoriteType = entry.getKey();
                }
            }
            experiences = sortByPreferences(experiences, topExperiences, favoriteType);
        }else{
            LOGGER.info("Do nothing with person " + person + " or null array.");
        }

        return experiences;
    }

    private Map<Enums.ExperienceType, MutableInt> calculateTypeOccurences(List<Reservation> reservations) {
        Map<Enums.ExperienceType, MutableInt> resultMap = new HashMap<>();
        for(Reservation reservation : reservations){
            System.out.println(reservation);
            if(reservation == null){
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

    private Experience[] sortByPreferences(Experience[] experiences, List<Experience> topExperiences, Enums.ExperienceType type) {
        List<Experience> resultList = new ArrayList<>();
        int added = 0;
        for(Experience exp : topExperiences){
            if(exp.getType() == type){
                exp.setRecommended(true);
                LOGGER.info("Recommending: " + exp);
                resultList.add(exp);
                added++;
            }
            if(added > 1){
                break;
            }
        }
        for(Experience exp : experiences){
            if(resultList.contains(exp)){
                LOGGER.info("Experience already in the list.");
            }else{
                resultList.add(exp);
            }
        }
        return resultList.toArray(new Experience[]{});
    }
}
