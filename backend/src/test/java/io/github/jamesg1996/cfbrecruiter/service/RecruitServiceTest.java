package io.github.jamesg1996.cfbrecruiter.service;

import static io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory.ACADEMIC_PRESTIGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
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
import io.github.jamesg1996.cfbrecruiter.persistence.RecruitRepository;
import static io.github.jamesg1996.cfbrecruiter.domain.Position.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainersConfiguration.class)
public class RecruitServiceTest {
    @Autowired
    RecruitRepository recruitRepository;
    @Autowired
    RecruitService recruitService;
    @Autowired
    DeductionService deductionService;

    @Test
    void createRecruit(){
        long recruitId = recruitService.createRecruit("Test Recruit", 2026, 3, HB, 1035);
        recruitService.confirm(recruitId, ACADEMIC_PRESTIGE);

        List<PitchResult> pitches = deductionService.evaluate(recruitId);

        Set<String> survivors = pitches.stream()
            .filter(r -> r.status() != PitchStatus.ELIMINATED)
            .map(r -> r.pitch().name())
            .collect(Collectors.toSet());
        assertEquals(Set.of("College Experience", "Student Of The Game"), survivors);
    }

    @Test
    void resetRecruit(){
        long recruitId = recruitService.createRecruit("Test Recruit", 2025, 4,QB,12234);
        recruitService.confirm(recruitId, ACADEMIC_PRESTIGE);
        List<PitchResult> pitches = deductionService.evaluate(recruitId);
        Set<String> beforeReset = pitches.stream()
            .filter(r -> r.status() != PitchStatus.ELIMINATED)
            .map(r -> r.pitch().name())
            .collect(Collectors.toSet());
        
        recruitService.reset(recruitId, ACADEMIC_PRESTIGE);
        List<PitchResult> resetPitches = deductionService.evaluate(recruitId);
        Set<String> afterReset = resetPitches.stream()
            .filter(r -> r.status() != PitchStatus.ELIMINATED)
            .map(r -> r.pitch().name())
            .collect(Collectors.toSet());

        assertEquals(2, beforeReset.size());
        assertEquals(20, afterReset.size());
    }

    @Test
    void toggleOnUnknownRecruitThrows() {
        assertThrows(RecruitNotFoundException.class,
            () -> recruitService.confirm(999999L, ACADEMIC_PRESTIGE));
    }
}
