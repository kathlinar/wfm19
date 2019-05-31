package wfm.group3.localy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wfm.group3.localy.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
