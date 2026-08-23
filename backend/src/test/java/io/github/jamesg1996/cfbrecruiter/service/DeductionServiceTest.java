package io.github.jamesg1996.cfbrecruiter.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


import io.github.jamesg1996.cfbrecruiter.TestContainersConfiguration;
import io.github.jamesg1996.cfbrecruiter.domain.deduction.PitchResult;
import io.github.jamesg1996.cfbrecruiter.domain.deduction.PitchStatus;
import io.github.jamesg1996.cfbrecruiter.persistence.RecruitEntity;
import io.github.jamesg1996.cfbrecruiter.persistence.RecruitRepository;
import static io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory.*;
import static io.github.jamesg1996.cfbrecruiter.domain.MotivationStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainersConfiguration.class)
public class DeductionServiceTest{

    @Autowired 
    RecruitRepository recruitRepository;
    @Autowired
    DeductionService deductionService;

    @Test 
    void happyPath(){
        RecruitEntity recruit = new RecruitEntity("Test Recruit", Map.of(ACADEMIC_PRESTIGE, CONFIRMED));
        RecruitEntity saved = recruitRepository.save(recruit);

        List<PitchResult> results = deductionService.evaluate(saved.getId());
        Set<String> survivors = results.stream()
            .filter(r -> r.status() != PitchStatus.ELIMINATED)
            .map(r -> r.pitch().name())
            .collect(Collectors.toSet());
        assertEquals(Set.of("College Experience", "Student Of The Game"), survivors);
    }

    @Test
    void unknownRecruitThrows() {
        assertThrows(RecruitNotFoundException.class, () -> deductionService.evaluate(999999L));
    }

}