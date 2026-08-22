package io.github.jamesg1996.cfbrecruiter.persistence;

import java.util.HashSet;
import java.util.Set;

import io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory;
import io.github.jamesg1996.cfbrecruiter.domain.MotivationStatus;
import io.github.jamesg1996.cfbrecruiter.domain.deduction.ScoutingState;

public class RecruitMapper {
    public static ScoutingState toScoutingState(RecruitEntity entity){
        Set<MotivationCategory> confirmed = new HashSet<>();
        Set<MotivationCategory> ruledOut = new HashSet<>();
        for (var entry : entity.motivationStatuses.entrySet()) {
            if (entry.getValue() == MotivationStatus.CONFIRMED) {
                confirmed.add(entry.getKey());
            } else if (entry.getValue() == MotivationStatus.RULED_OUT) {
                ruledOut.add(entry.getKey());
            }
        }
        return new ScoutingState(confirmed, ruledOut);
    }
    private RecruitMapper() {}
}
