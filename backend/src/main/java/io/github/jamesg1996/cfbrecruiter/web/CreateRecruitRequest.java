package io.github.jamesg1996.cfbrecruiter.web;

import io.github.jamesg1996.cfbrecruiter.domain.Position;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRecruitRequest( @NotBlank String name, 
                                    @NotNull Integer year,
                                    @NotNull @Min(1) @Max(5) Integer pipelineGrade,
                                    @NotNull Position position,
                                    Integer nationalRanking
){}