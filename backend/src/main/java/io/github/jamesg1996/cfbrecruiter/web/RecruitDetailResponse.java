package io.github.jamesg1996.cfbrecruiter.web;

import java.util.Map;

import io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory;
import io.github.jamesg1996.cfbrecruiter.domain.MotivationStatus;
import io.github.jamesg1996.cfbrecruiter.domain.Position;

public record RecruitDetailResponse(long id, String name, 
                                    Integer year, Integer pipelineGrade,
                                    Position position, Integer nationalRanking,                                
                                    Map<MotivationCategory,MotivationStatus> motivations) {
    
}
