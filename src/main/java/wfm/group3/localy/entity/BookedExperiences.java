package wfm.group3.localy.entity;

import wfm.group3.localy.utils.Enums;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookedExperiences {

    private String name;

    private String description;

    private Enums.ExperienceType type;

    private BigDecimal price;

    private String address;

    private LocalTime duration;

    private LocalDateTime reservationDate;

    public BookedExperiences(Experience experience, Reservation reservation) {
        this.name = experience.getName();
        this.description = experience.getDescription();
        this.type = experience.getType();
        this.price = experience.getPrice();
        this.address = experience.getLocation().getCity() +", " + experience.getLocation().getStreet();
        this.duration = experience.getDuration();

        this.reservationDate = reservation.getReservationDate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Enums.ExperienceType getType() {
        return type;
    }

    public void setType(Enums.ExperienceType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }
}
