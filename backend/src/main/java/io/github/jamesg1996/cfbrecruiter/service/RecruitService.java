package io.github.jamesg1996.cfbrecruiter.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory;
import io.github.jamesg1996.cfbrecruiter.domain.Pitch;
import io.github.jamesg1996.cfbrecruiter.domain.Position;
import io.github.jamesg1996.cfbrecruiter.persistence.RecruitEntity;
import io.github.jamesg1996.cfbrecruiter.persistence.RecruitRepository;
import io.github.jamesg1996.cfbrecruiter.web.RecruitDetailResponse;
import io.github.jamesg1996.cfbrecruiter.web.RecruitResponse;

@Transactional
@Service
public class RecruitService {
    private final RecruitRepository recruitRepository;
    private final DeductionService deductionService;
    
    public RecruitService(RecruitRepository recruitRepository, DeductionService deductionService){
        this.recruitRepository = recruitRepository;
        this.deductionService = deductionService;
    }
    
    @Transactional(readOnly = true)
    public RecruitDetailResponse getRecruitById(long id){
        RecruitEntity recruit = load(id);
        return new RecruitDetailResponse(id, recruit.getName(), recruit.getYear(), recruit.getPipelineGrade(), recruit.getPosition(), recruit.getNationalRanking(), 
        Map.copyOf(recruit.getMotivationStatuses()));
    }

    public long createRecruit(String name, Integer year, Integer pipelineGrade, Position position, Integer nationalRanking){
        RecruitEntity recruit = new RecruitEntity(name,year,pipelineGrade,position,nationalRanking);
        long id = recruitRepository.save(recruit).getId();
        return id;
    }

    public void deleteRecruit(long id){
        RecruitEntity recruit = load(id);
        recruitRepository.delete(recruit);
    }

    private RecruitEntity load(long recruitId) {
        return recruitRepository.findById(recruitId)
            .orElseThrow(() -> new RecruitNotFoundException(recruitId));
    }

    public void confirm(long recruitId, MotivationCategory category){
        load(recruitId).confirm(category);
    }

    public void ruledOut(long recruitId, MotivationCategory category){
        load(recruitId).ruledOut(category);
    }

    public void reset(long recruitId, MotivationCategory category){
        load(recruitId).reset(category);
    }

    @Transactional(readOnly = true)
    public List<RecruitResponse> listRecruits(){
        List<Pitch> pitches = deductionService.loadPitches();
        return recruitRepository.findAll().stream()
            .map(e -> new RecruitResponse(e.getId(), e.getName(), e.getYear(),
                 e.getPipelineGrade(), e.getPosition(), e.getNationalRanking(),
                 deductionService.confirmedPitchName(e, pitches).orElse(null)))
            .toList();
    
    }
}
