package wfm.group3.localy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wfm.group3.localy.entity.Reservation;
import wfm.group3.localy.utils.Enums;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT res FROM Reservation res  WHERE res.personId=(:pID)")
    List<Reservation> findReservationsByPersonId(@Param("pID") Long personId);

    @Query("SELECT res FROM Reservation res WHERE  res.reservationId=(:rID)")
    Reservation findReservationId(@Param("rID") Long reservationId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Reservation res SET res.attended=:attended WHERE res.reservationId=(:rID)")
    int updateAttended(@Param("rID") Long reservationId, @Param("attended") boolean attended);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Reservation res SET res.feedback=:feedback WHERE res.reservationId=(:rID)")
    int setFeedback(@Param("rID") Long reservationId, @Param("feedback") String feedback);


    List<Reservation> findByReservationDate(LocalDate reservationDate);

    Reservation findFirstByExperienceIdAndReservationDateAndStatus(Long experienceId, LocalDate date, Enums.ReservationStatus status);

    @Query("SELECT count(res.experienceId) FROM Reservation res WHERE res.experienceId=(:eID) AND res.reservationDate=(:date) AND res.status='CONFIRMED' group by res.reservationDate,res.experienceId")
    Long getGroupSizeOfReservation(@Param("eID") Long experienceId, @Param("date") LocalDate date);

}
