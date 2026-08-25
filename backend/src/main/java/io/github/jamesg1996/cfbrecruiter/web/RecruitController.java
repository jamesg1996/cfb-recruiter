package io.github.jamesg1996.cfbrecruiter.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.jamesg1996.cfbrecruiter.domain.MotivationCategory;
import io.github.jamesg1996.cfbrecruiter.domain.deduction.PitchResult;
import io.github.jamesg1996.cfbrecruiter.persistence.RecruitEntity;
import io.github.jamesg1996.cfbrecruiter.service.DeductionService;
import io.github.jamesg1996.cfbrecruiter.service.RecruitService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/recruits")
public class RecruitController {
    private final DeductionService deductionService;
    private final RecruitService recruitService;

    public RecruitController(DeductionService deductionService, RecruitService recruitService){
        this.deductionService = deductionService;
        this.recruitService = recruitService;
    }


    @GetMapping("/{id}/evaluation")
    List<PitchResult> evaluate(@PathVariable long id){
        return deductionService.evaluate(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Long create(@Valid @RequestBody CreateRecruitRequest request){
        return recruitService.createRecruit(request.name());
    }

    @PutMapping("/{id}/motivations/{category}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updatedMotivation( @PathVariable long id,
                            @PathVariable MotivationCategory category,
                            @RequestBody MotivationUpdate update){
        switch(update.status()){
            case CONFIRMED -> recruitService.confirm(id, category);
            case RULED_OUT -> recruitService.ruledOut(id,category);
            case UNKNOWN   -> recruitService.reset(id, category);
        }
    }
    
    @GetMapping("/{id}")
    RecruitResponse getRecruitById(@PathVariable long id){
        return recruitService.getRecruitById(id);
    }

    @GetMapping
    List<RecruitResponse> getRecruits(){
        return recruitService.listRecruits();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecruit(@PathVariable long id){
        recruitService.deleteRecruit(id);
    }
                            
}
