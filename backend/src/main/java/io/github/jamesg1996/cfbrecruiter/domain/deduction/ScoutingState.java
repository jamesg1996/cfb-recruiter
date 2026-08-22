package io.github.jamesg1996.cfbrecruiter.domain.deduction;

import io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory;

import java.util.Collections;
import java.util.Set;

public record ScoutingState(Set<MotivationCategory> confirmed, Set<MotivationCategory> ruledOut) {
    public ScoutingState {
        if (confirmed == null || ruledOut == null) {
            throw new IllegalArgumentException("Confirmed and ruled out sets cannot be null");
        }
        if (!Collections.disjoint(confirmed, ruledOut)) {
            throw new IllegalArgumentException("A category cannot be both confirmed and ruled out");
        }
        confirmed = Set.copyOf(confirmed);
        ruledOut = Set.copyOf(ruledOut);
    }
}
