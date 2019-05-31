package wfm.group3.localy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wfm.group3.localy.entity.Experience;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}
