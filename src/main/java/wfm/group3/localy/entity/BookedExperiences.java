package wfm.group3.localy.entity;

import wfm.group3.localy.utils.Enums;
import wfm.group3.localy.utils.Utils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookedExperiences {

    private Long id;

    private Long experienceId;

    private String name;

    private String description;

    private Enums.ExperienceType type;

    private BigDecimal price;

    private String address;

    private String durationString;

    private Duration duration;

    private LocalTime startTime;

    private LocalDate reservationDate;

    private boolean attended;

    private Enums.ReservationStatus status;

    private String feedback;

    public BookedExperiences(Experience experience, Reservation reservation) {
        this.id = reservation.getReservationId();
        this.experienceId = experience.getId();
        this.name = experience.getName();
        this.description = experience.getDescription();
        this.type = experience.getType();
        this.price = experience.getPrice();
        this.address = experience.getLocation().getCity() + ", " + experience.getLocation().getStreet();
        this.duration = experience.getDuration();
        this.durationString = Utils.formatDuration(experience.getDuration());
        this.startTime = experience.getStartTime();

        this.attended = reservation.getAttended();
        this.reservationDate = reservation.getReservationDate();
        this.status = reservation.getStatus();
        this.feedback = reservation.getFeedback();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public Enums.ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(Enums.ReservationStatus status) {
        this.status = status;
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

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
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

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
