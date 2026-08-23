package io.github.jamesg1996.cfbrecruiter.service;

public class RecruitNotFoundException extends RuntimeException {
    public RecruitNotFoundException(long recruitId){
        super("Recruit not found: " + recruitId);
    }
}
