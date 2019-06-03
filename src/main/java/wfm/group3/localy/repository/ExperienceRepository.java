package wfm.group3.localy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wfm.group3.localy.entity.Experience;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Query("SELECT exp FROM Experience exp WHERE exp.maxGroupSize > (SELECT count(res.experienceId) FROM Reservation res WHERE res.experienceId=exp.id AND res.reservationDate=(:date) AND res.status='CONFIRMED')")
    List<Experience> findExperiencesByNoReservationYet(@Param("date") LocalDate date);

}
