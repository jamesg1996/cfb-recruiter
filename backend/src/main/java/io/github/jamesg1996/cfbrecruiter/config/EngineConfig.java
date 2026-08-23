package io.github.jamesg1996.cfbrecruiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.jamesg1996.cfbrecruiter.domain.deduction.DeductionEngine;

@Configuration
public class EngineConfig {
    @Bean
    DeductionEngine deductionEngine(){ return new DeductionEngine();}
}
