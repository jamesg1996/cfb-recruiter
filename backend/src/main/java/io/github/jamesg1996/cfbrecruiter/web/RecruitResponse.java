package io.github.jamesg1996.cfbrecruiter.web;

import io.github.jamesg1996.cfbrecruiter.domain.Position;

public record RecruitResponse(long id, String name,
                              Integer year, Integer pipelineGrade,
                              Position position, Integer nationalRanking
) {
}
