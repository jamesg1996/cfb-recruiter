package io.github.jamesg1996.cfbrecruiter.web;

import jakarta.validation.constraints.NotBlank;

public record CreateRecruitRequest(@NotBlank String name) {
}