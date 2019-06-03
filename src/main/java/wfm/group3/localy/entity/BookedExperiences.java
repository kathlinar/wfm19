package wfm.group3.localy.entity;

import wfm.group3.localy.utils.Enums;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookedExperiences {


    private Long experienceId;

    private String name;

    private String description;

    private Enums.ExperienceType type;

    private BigDecimal price;

    private String address;

    private Duration duration;

    private LocalTime startTime;

    private LocalDate reservationDate;

    public BookedExperiences(Experience experience, Reservation reservation) {
        this.experienceId = experience.getId();
        this.name = experience.getName();
        this.description = experience.getDescription();
        this.type = experience.getType();
        this.price = experience.getPrice();
        this.address = experience.getLocation().getCity() + ", " + experience.getLocation().getStreet();
        this.duration = experience.getDuration();
        this.startTime = experience.getStartTime();

        this.reservationDate = reservation.getReservationDate();
    }


    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }
}
