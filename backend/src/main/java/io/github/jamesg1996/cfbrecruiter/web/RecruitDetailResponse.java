package io.github.jamesg1996.cfbrecruiter.web;

import java.util.Map;

import io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory;
import io.github.jamesg1996.cfbrecruiter.domain.MotivationStatus;

public record RecruitDetailResponse(long id, String name, Map<MotivationCategory,MotivationStatus> motivations) {
    
}
