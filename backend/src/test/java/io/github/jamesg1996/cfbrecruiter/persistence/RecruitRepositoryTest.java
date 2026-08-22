package io.github.jamesg1996.cfbrecruiter.persistence;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import io.github.jamesg1996.cfbrecruiter.domain.deduction.ScoutingState;

import org.testcontainers.junit.jupiter.Container;
import static io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory.*;
import static io.github.jamesg1996.cfbrecruiter.domain.MotivationStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecruitRepositoryTest {
    @Autowired
    RecruitRepository recruitRepository;
    
    @Autowired
    TestEntityManager em;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17");

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
