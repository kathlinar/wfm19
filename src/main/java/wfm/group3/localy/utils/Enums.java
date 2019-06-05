package wfm.group3.localy.utils;

import java.util.Random;

public class Enums {

    public enum ExperienceType {
        SIGHTSEEING, NIGHTLIFE, GAMES, MUSEUM, SPECIAL, CONCERT, SPORTS;

        public static ExperienceType getRandom(){
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

    public enum PersonType {
        CUSTOMER, GUIDE
    }

    public enum ReservationStatus {
        PENDING, CANCELLED, CONFIRMED, OVERLAP, GROUP_FULL, CONFIRMED_AND_ATTENDED
    }

    public enum MailPurpose {
        USER_CONFIRMATION, USER_CANCELLATION, GUIDE_CANCELLATION, OVERLAP, GROUP_FULL, GUIDE_CONFIRMATION
    }
}
