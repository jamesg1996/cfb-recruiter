package io.github.jamesg1996.cfbrecruiter.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;


import io.github.jamesg1996.cfbrecruiter.TestContainersConfiguration;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Import(TestContainersConfiguration.class)
public class RecruitControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createReturns201WithId() throws Exception {
        mockMvc.perform(post("/recruits")
            .contentType(APPLICATION_JSON)
            .content("{\"name\":\"Test Recruit\",\"year\":2026,\"pipelineGrade\":3,\"position\":\"HB\"}"))
        .andExpect(status().isCreated());
    }

    @Test
    void updateRecruitMotivation() throws Exception{
        String body = mockMvc.perform(post("/recruits")
            .contentType(APPLICATION_JSON).content("{\"name\":\"Test Recruit\",\"year\":2026,\"pipelineGrade\":3,\"position\":\"HB\"}"))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        long id = Long.parseLong(body.trim());

        mockMvc.perform(put("/recruits/" + id + "/motivations/ACADEMIC_PRESTIGE")
            .contentType(APPLICATION_JSON)
            .content("{\"status\":\"CONFIRMED\"}"))
            .andExpect(status().isNoContent());
    }

    @Test
    void evaluateRecruit() throws Exception{
        String body = mockMvc.perform(post("/recruits")
            .contentType(APPLICATION_JSON).content("{\"name\":\"Test Recruit\",\"year\":2026,\"pipelineGrade\":3,\"position\":\"HB\"}"))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        long id = Long.parseLong(body.trim());

        mockMvc.perform(get("/recruits/" + id + "/evaluation"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(20));
    }

    @Test
    void unknownRecruit() throws Exception{
        mockMvc.perform(get("/recruits/999999/evaluation"))
            .andExpect(status().isNotFound());   
    }

    @Test
    void blankRecruit() throws Exception{
        mockMvc.perform(post("/recruits")
        .contentType(APPLICATION_JSON)
        .content("{\"name\":\"\"}"))
        .andExpect(status().isBadRequest());
    }

    @Test
    void getAllRecruits() throws Exception{
        mockMvc.perform(get("/recruits"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray()); 
    }

    @Test
    void getRecruitById() throws Exception{
        String body = mockMvc.perform(post("/recruits")
            .contentType(APPLICATION_JSON).content("{\"name\":\"Test Recruit\",\"year\":2026,\"pipelineGrade\":3,\"position\":\"HB\"}"))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        long id = Long.parseLong(body.trim());
        mockMvc.perform(get("/recruits/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Recruit"))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.motivations").exists());

    }

    @Test
    void getRecruitByIdUnknownRecruit() throws Exception{
        mockMvc.perform(get("/recruits/999999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteRecruitById() throws Exception{
        String body = mockMvc.perform(post("/recruits")
            .contentType(APPLICATION_JSON).content("{\"name\":\"Test Recruit\",\"year\":2026,\"pipelineGrade\":3,\"position\":\"HB\"}"))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        long id = Long.parseLong(body.trim());

        mockMvc.perform(delete("/recruits/" + id))
        .andExpect(status().isNoContent());
        mockMvc.perform(get("/recruits/" + id))
        .andExpect(status().isNotFound());
    }

    @Test
    void deleteRecruitByIdUnknownRecruit() throws Exception{
        mockMvc.perform(delete("/recruits/99999"))
        .andExpect(status().isNotFound());
    }
}
