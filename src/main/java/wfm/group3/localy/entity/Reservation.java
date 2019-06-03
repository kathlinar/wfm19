package wfm.group3.localy.entity;

import wfm.group3.localy.utils.Enums;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_generator")
    @SequenceGenerator(name = "reservation_generator", sequenceName = "res_seq", initialValue = 4)
    private Long reservationId;

    @Column
    private Long personId;

    @Column
    private Long experienceId;

    @Column
    private LocalDate reservationDate;

    @Column
    private Boolean attended;

    @Column
    private String feedback;

    @Column
    private String processInstanceId;

    @Column
    @Enumerated(EnumType.STRING)
    private Enums.ReservationStatus status;

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Enums.ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(Enums.ReservationStatus status) {
        this.status = status;
    }

    public Long getReservationId() {
        return reservationId;
    }
}
