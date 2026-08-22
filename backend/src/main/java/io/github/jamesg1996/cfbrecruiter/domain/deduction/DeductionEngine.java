package io.github.jamesg1996.cfbrecruiter.domain.deduction;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import io.github.jamesg1996.cfbrecruiter.domain.Pitch;

public class DeductionEngine {
    public List<PitchResult> evaluate(List<Pitch> pitches, ScoutingState state) {
        Set<Pitch> survivors = new HashSet<>();
        List<PitchResult> results = new ArrayList<>();
        for(Pitch pitch : pitches) {
            if (pitch.motivationCategories().containsAll(state.confirmed()) && Collections.disjoint(pitch.motivationCategories(), state.ruledOut())) {
                survivors.add(pitch);
            }
        }
        for(Pitch pitch : pitches) {
            if (survivors.contains(pitch)) {
                if (survivors.size() == 1) {
                    results.add(new PitchResult(pitch, PitchStatus.CONFIRMED));
                } else {
                    results.add(new PitchResult(pitch, PitchStatus.POSSIBLE));
                }
            } else {
                results.add(new PitchResult(pitch, PitchStatus.ELIMINATED));
            }
            
        }
        return List.copyOf(results);
    }
}
