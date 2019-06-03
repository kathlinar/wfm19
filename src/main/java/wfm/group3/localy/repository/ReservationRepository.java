package wfm.group3.localy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wfm.group3.localy.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT res FROM Reservation res  WHERE res.personId=(:pID)")
    List<Reservation> findReservationsByPersonId(@Param("pID") Long personId);

    @Query("SELECT res FROM Reservation res WHERE  res.reservationId=(:rID)")
    Reservation findReservationId(@Param("rID") Long reservationId);

    List<Reservation> findByReservationDate(LocalDate reservationDate);

    Reservation findFirstByExperienceIdAndReservationDate(Long experienceId, LocalDate date);

    @Query("SELECT count(res.experienceId) FROM Reservation res WHERE res.experienceId=(:eID) AND res.reservationDate=(:date) AND res.status='CONFIRMED' group by res.reservationDate,res.experienceId")
    Long getGroupSizeOfReservation(@Param("eID") Long experienceId, @Param("date") LocalDate date);

}
