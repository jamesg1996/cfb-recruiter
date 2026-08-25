package io.github.jamesg1996.cfbrecruiter.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory;
import io.github.jamesg1996.cfbrecruiter.persistence.RecruitEntity;
import io.github.jamesg1996.cfbrecruiter.persistence.RecruitRepository;
import io.github.jamesg1996.cfbrecruiter.web.RecruitResponse;

@Transactional
@Service
public class RecruitService {
    private final RecruitRepository recruitRepository;
    
    public RecruitService(RecruitRepository recruitRepository){
        this.recruitRepository = recruitRepository;
    }

    public RecruitResponse getRecruitById(long id){
        RecruitEntity recruit = load(id);
        RecruitResponse recruitResponse = new RecruitResponse(id, recruit.getName());
        return recruitResponse;
    }

    public List<RecruitResponse> listRecruits(){
        return recruitRepository.findAll().stream()
            .map(e -> new RecruitResponse(e.getId(), e.getName()))
            .toList();
    }

    public long createRecruit(String name){
        RecruitEntity recruit = new RecruitEntity(name);
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
}
