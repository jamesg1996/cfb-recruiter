package io.github.jamesg1996.cfbrecruiter.domain;

import java.util.Set;

public record Pitch(String name, Set<MotivationCategory> motivationCategories) {
    public Pitch {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Pitch name cannot be null or blank");
        }
        if (motivationCategories == null || motivationCategories.isEmpty()) {
            throw new IllegalArgumentException("Motivation categories cannot be null or empty");
        }
        motivationCategories = Set.copyOf(motivationCategories);
        if (motivationCategories.size() != 3) {
            throw new IllegalArgumentException("Motivation categories must contain exactly 3 items");
        }
    }
}
