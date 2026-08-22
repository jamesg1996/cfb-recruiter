package io.github.jamesg1996.cfbrecruiter.persistence;

import io.github.jamesg1996.cfbrecruiter.domain.Pitch;

public class PitchMapper {
    public static Pitch toDomain(PitchEntity entity) {
        return new Pitch(entity.name, entity.motivations);
    }
    private PitchMapper() {}
}
