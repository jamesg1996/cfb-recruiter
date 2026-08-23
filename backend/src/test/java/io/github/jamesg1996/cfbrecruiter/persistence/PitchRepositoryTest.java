package io.github.jamesg1996.cfbrecruiter.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import io.github.jamesg1996.cfbrecruiter.domain.Pitch;
import java.util.Set;

import io.github.jamesg1996.cfbrecruiter.TestContainersConfiguration;
import io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainersConfiguration.class)
public class PitchRepositoryTest {
    @Autowired 
    PitchRepository repository;

    @Test
    void findAll(){
        List<PitchEntity> pitches = repository.findAll();
        assertEquals(20, pitches.size());
    }

    @Test
    void checkPitches(){
        List<Pitch> pitches = repository.findAll().stream().map(PitchMapper::toDomain).toList();
        assertEquals(20, pitches.size());
    }

    @Test
    void checkSinglePitch(){
        Pitch pitch = repository.findByName("College Experience").map(PitchMapper::toDomain).orElseThrow();

        assertEquals(pitch.motivationCategories(), Set.of(MotivationCategory.ACADEMIC_PRESTIGE, MotivationCategory.CAMPUS_LIFESTYLE, MotivationCategory.STADIUM_ATMOSPHERE));

    }

}
