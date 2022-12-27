package com.my.location.core.location.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    @Transactional(readOnly = true, propagation = Propagation.NEVER)
    Optional<LocationEntity> findByStreetName(String name);

}
