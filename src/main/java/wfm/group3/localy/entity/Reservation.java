package wfm.group3.localy.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@IdClass(Reservation.IdClass.class)
public class Reservation implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "res_generator")
    @SequenceGenerator(name="reservation_generator", sequenceName = "res_seq", initialValue = 4)
    private Long reservationId;

    @Id
    private Long personId;

    @Id
    private Long experienceId;

    @Column
    private LocalDateTime reservationDate;

    @Column
    private Boolean attended;

    @Column
    private Boolean deleted;

    @Column
    private String feedback;

    @Column
    private String processDefinitionId;

    static class IdClass implements Serializable {
        private Long reservationId;
        private Long personId;
        private Long experienceId;

        public Long getReservationId() {
            return reservationId;
        }

        public void setReservationId(Long reservationId) {
            this.reservationId = reservationId;
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
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
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

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
