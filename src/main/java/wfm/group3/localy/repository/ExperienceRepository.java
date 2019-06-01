package wfm.group3.localy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.entity.Reservation;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

}
