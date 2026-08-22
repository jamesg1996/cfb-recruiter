package io.github.jamesg1996.cfbrecruiter.domain.deduction;

import io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory;
import io.github.jamesg1996.cfbrecruiter.domain.Pitch;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeductionEngineTest {
    Pitch collegeExperience = new Pitch("College Experience", Set.of(MotivationCategory.ACADEMIC_PRESTIGE, MotivationCategory.CAMPUS_LIFESTYLE, MotivationCategory.STADIUM_ATMOSPHERE));
    Pitch studentOfTheGame  = new Pitch("Student Of The Game", Set.of(MotivationCategory.ACADEMIC_PRESTIGE, MotivationCategory.COACH_PRESTIGE, MotivationCategory.PROXIMITY_TO_HOME));
    Pitch theClutch         = new Pitch("The Clutch", Set.of(MotivationCategory.COACH_STABILITY, MotivationCategory.PLAYING_STYLE, MotivationCategory.PLAYING_TIME));
    List<Pitch> pitches = List.of(collegeExperience, studentOfTheGame, theClutch);

    @Test
    public void test_oneSurvivorByConfirms() {
        DeductionEngine engine = new DeductionEngine();
        ScoutingState state = new ScoutingState(Set.of(MotivationCategory.ACADEMIC_PRESTIGE, MotivationCategory.CAMPUS_LIFESTYLE), Set.of());
        List<PitchResult> results = engine.evaluate(pitches, state);
        assertEquals(PitchStatus.CONFIRMED, results.get(0).status());
        assertEquals(PitchStatus.ELIMINATED, results.get(1).status());
        assertEquals(PitchStatus.ELIMINATED, results.get(2).status());
    }

    @Test
    public void test_oneSurvivorByEliminations() {
        DeductionEngine engine = new DeductionEngine();
        ScoutingState state = new ScoutingState(Set.of(MotivationCategory.ACADEMIC_PRESTIGE), Set.of(MotivationCategory.CAMPUS_LIFESTYLE));
        List<PitchResult> results = engine.evaluate(pitches, state);
        assertEquals(PitchStatus.ELIMINATED, results.get(0).status());
        assertEquals(PitchStatus.CONFIRMED, results.get(1).status());
        assertEquals(PitchStatus.ELIMINATED, results.get(2).status());
    }

    @Test
    public void test_twoSurvivors() {
        DeductionEngine engine = new DeductionEngine();
        ScoutingState state = new ScoutingState(Set.of(MotivationCategory.ACADEMIC_PRESTIGE), Set.of(MotivationCategory.PLAYING_TIME));
        List<PitchResult> results = engine.evaluate(pitches, state);
        assertEquals(PitchStatus.POSSIBLE, results.get(0).status());
        assertEquals(PitchStatus.POSSIBLE, results.get(1).status());
        assertEquals(PitchStatus.ELIMINATED, results.get(2).status());
    }

    @Test
    public void test_noSurvivors() {
        DeductionEngine engine = new DeductionEngine();
        ScoutingState state = new ScoutingState(Set.of(MotivationCategory.ACADEMIC_PRESTIGE, MotivationCategory.COACH_STABILITY), Set.of());
        List<PitchResult> results = engine.evaluate(pitches, state);
        assertEquals(PitchStatus.ELIMINATED, results.get(0).status());
        assertEquals(PitchStatus.ELIMINATED, results.get(1).status());
        assertEquals(PitchStatus.ELIMINATED, results.get(2).status());
    }

    @Test
    public void test_freshRecruit() {
        DeductionEngine engine = new DeductionEngine();
        ScoutingState state = new ScoutingState(Set.of(), Set.of());
        List<PitchResult> results = engine.evaluate(pitches, state);
        assertEquals(PitchStatus.POSSIBLE, results.get(0).status());
        assertEquals(PitchStatus.POSSIBLE, results.get(1).status());
        assertEquals(PitchStatus.POSSIBLE, results.get(2).status());
    }
}
