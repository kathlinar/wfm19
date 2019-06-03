package wfm.group3.localy.utils;

public class Enums {

    public enum ExperienceType {
        SIGHTSEEING, NIGHTLIFE, GAMES, MUSEUM, SPECIAL, CONCERT, SPORTS
    }

    public enum PersonType {
        CUSTOMER, GUIDE
    }

    public enum ReservationStatus {
        PENDING, CANCELLED, CONFIRMED, OVERLAP, GROUP_FULL
    }

    public enum MailPurpose {
        USER_CONFIRMATION, USER_CANCELLATION, GUIDE_CANCELLATION, OVERLAP, GROUP_FULL
    }
}
