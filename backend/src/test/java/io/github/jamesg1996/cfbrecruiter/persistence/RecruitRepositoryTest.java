package io.github.jamesg1996.cfbrecruiter.persistence;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import io.github.jamesg1996.cfbrecruiter.TestContainersConfiguration;
import io.github.jamesg1996.cfbrecruiter.domain.deduction.ScoutingState;

import static io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory.*;
import static io.github.jamesg1996.cfbrecruiter.domain.MotivationStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainersConfiguration.class)
public class RecruitRepositoryTest {
    @Autowired
    RecruitRepository recruitRepository;
    
    @Autowired
    TestEntityManager em;

    @Test
    void recruitMotivationsRoundTripToScoutingState(){
        RecruitEntity recruit = new RecruitEntity();
        recruit.name = "Test Recruit";
        recruit.motivationStatuses = Map.of(ACADEMIC_PRESTIGE, CONFIRMED, CAMPUS_LIFESTYLE, RULED_OUT);
        
        em.persistAndFlush(recruit);
        em.clear();
        RecruitEntity reloaded = recruitRepository.findById(recruit.id)
            .orElseThrow();
        
        ScoutingState state = RecruitMapper.toScoutingState(reloaded);
        assertEquals(Set.of(ACADEMIC_PRESTIGE), state.confirmed());
        assertEquals(Set.of(CAMPUS_LIFESTYLE), state.ruledOut());
    }
}
