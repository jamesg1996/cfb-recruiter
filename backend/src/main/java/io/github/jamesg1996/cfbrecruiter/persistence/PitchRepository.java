package io.github.jamesg1996.cfbrecruiter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PitchRepository extends JpaRepository<PitchEntity, Long> {
    Optional<PitchEntity> findByName(String name);
}
