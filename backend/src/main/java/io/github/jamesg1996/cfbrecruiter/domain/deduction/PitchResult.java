package io.github.jamesg1996.cfbrecruiter.domain.deduction;

import io.github.jamesg1996.cfbrecruiter.domain.Pitch;

public record PitchResult(Pitch pitch, PitchStatus status) {
    public PitchResult {
        if (pitch == null) {
            throw new IllegalArgumentException("Pitch cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Pitch status cannot be null");
        }
    }
    
}
