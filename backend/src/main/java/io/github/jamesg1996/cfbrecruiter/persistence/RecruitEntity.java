package io.github.jamesg1996.cfbrecruiter.persistence;

import java.util.Map;

import io.github.jamesg1996.cfbrecruiter.domain.MotivationStatus;
import io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recruit")
public class RecruitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    String name;
    
    @ElementCollection
    @CollectionTable(name = "recruit_motivation", joinColumns = @JoinColumn(name = "recruit_id"))
    @MapKeyColumn(name = "category")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Map<MotivationCategory, MotivationStatus> motivationStatuses;

    protected RecruitEntity() {}
    public RecruitEntity(String name, Map<MotivationCategory, MotivationStatus> motivationStatuses){
        this.name = name;
        this.motivationStatuses = motivationStatuses;
    }
    public Long getId(){
        return id;
    }
}