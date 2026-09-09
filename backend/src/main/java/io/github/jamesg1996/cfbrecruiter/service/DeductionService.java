package io.github.jamesg1996.cfbrecruiter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jamesg1996.cfbrecruiter.domain.Pitch;
import io.github.jamesg1996.cfbrecruiter.domain.deduction.DeductionEngine;
import io.github.jamesg1996.cfbrecruiter.domain.deduction.PitchResult;
import io.github.jamesg1996.cfbrecruiter.domain.deduction.PitchStatus;
import io.github.jamesg1996.cfbrecruiter.domain.deduction.ScoutingState;
import io.github.jamesg1996.cfbrecruiter.persistence.PitchMapper;
import io.github.jamesg1996.cfbrecruiter.persistence.PitchRepository;
import io.github.jamesg1996.cfbrecruiter.persistence.RecruitEntity;
import io.github.jamesg1996.cfbrecruiter.persistence.RecruitMapper;
import io.github.jamesg1996.cfbrecruiter.persistence.RecruitRepository;

@Service
public class DeductionService {
    private final PitchRepository pitchRepository;
    private final RecruitRepository recruitRepository;
    private final DeductionEngine deductionEngine;

    @Autowired
    public DeductionService(PitchRepository pitchRepository, RecruitRepository recruitRepository, DeductionEngine deductionEngine){
        this.pitchRepository = pitchRepository;
        this.recruitRepository = recruitRepository;
        this.deductionEngine = deductionEngine;
    }

    @Transactional(readOnly = true)
    public List<PitchResult> evaluate(long recruitId){
        List<Pitch> pitches = pitchRepository.findAll().stream().map(PitchMapper::toDomain).toList();
        RecruitEntity recruit = recruitRepository.findById(recruitId).orElseThrow(() -> new RecruitNotFoundException(recruitId));
        ScoutingState state = RecruitMapper.toScoutingState(recruit);
        return deductionEngine.evaluate(pitches, state);        
    }

    @Transactional 
    public List<Pitch> loadPitches(){
        return pitchRepository.findAll().stream().map(PitchMapper::toDomain).toList();
    }

    public Optional<String> confirmedPitchName(RecruitEntity recruit, List<Pitch> pitches){
        ScoutingState state = RecruitMapper.toScoutingState(recruit);
        return deductionEngine.evaluate(pitches, state).stream()
            .filter(r -> r.status() == PitchStatus.CONFIRMED)
            .map(r -> r.pitch().name())
            .findFirst();
    }
}
