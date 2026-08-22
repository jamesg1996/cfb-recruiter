package io.github.jamesg1996.cfbrecruiter.persistence;

import io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "pitch")
public class PitchEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String name;

    @ElementCollection
    @CollectionTable(name = "pitch_motivation", joinColumns = @JoinColumn(name = "pitch_id"))
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    Set<MotivationCategory> motivations;

    protected PitchEntity() {}
}
