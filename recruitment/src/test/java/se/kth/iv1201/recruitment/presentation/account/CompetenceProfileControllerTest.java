package se.kth.iv1201.recruitment.presentation.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Tests for {@link CompetenceProfileController}.
 *
 * Tests if controller correctly populates the model with the expected number of rows
 * when adding date ranges and experiences.
 */

@SpringBootTest
public class CompetenceProfileControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        this.mockMvc = null;
    }

    @Test
    void populateModelWithRow() throws Exception {
        // Fake a browser, send GET request, to /competenceProfile
        MvcResult res = mockMvc.perform(get("/competenceProfile"))
                .andExpect(status().isOk())
                .andReturn();

        // Give me the object that the controller sent to the view (What will be rendered)
        Object obj = res.getModelAndView().getModel().get("competenceProfile");
        assertThat(obj).isInstanceOf(CompetenceProfile.class);
        CompetenceProfile form = (CompetenceProfile) obj;
        assertThat(form.getDateRanges()).withFailMessage("Expected 1 date range, but found " + form.getDateRanges().size()).hasSize(1);
        assertThat(form.getExperiences()).withFailMessage("Expected 1 experience, but found " + form.getExperiences().size()).hasSize(1);

        // when getting "/competenceProfile", the controller adds the rows for date ranges and experiences, so we expect 1 of each.
        // Checks that the model contains a "competenceProfile" object, 
        // and that its an instance of CompetenceProfile class. 
        // Then checks that the initial size of date ranges and experiences is 1, as expected.
    }

    @Test
    void addOneDateRange() throws Exception {
        MvcResult res = mockMvc.perform(post("/competence").param("addDateRow", "true").with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        Object obj = res.getModelAndView().getModel().get("competenceProfile");
        assertThat(obj).isInstanceOf(CompetenceProfile.class);
        CompetenceProfile form = (CompetenceProfile) obj;
        assertThat(form.getDateRanges()).withFailMessage("Expected 1 date range, but found " + form.getDateRanges().size()).hasSize(1);
    }

    @Test
    void addOneExperience() throws Exception {
        MvcResult res = mockMvc.perform(post("/competence").param("addExperienceRow", "true").with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        // same as "model.addAttribute("competenceProfile", form);"
        Object obj = res.getModelAndView().getModel().get("competenceProfile");
        assertThat(obj).isInstanceOf(CompetenceProfile.class);
        CompetenceProfile form = (CompetenceProfile) obj;
        assertThat(form.getExperiences()).withFailMessage("Expected 1 experience, but found " + form.getExperiences().size()).hasSize(1);
    }

    @Test
    void returnSuccessView() throws Exception {
        mockMvc.perform(post("/competence").param("submitted", "true").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getModelAndView().getViewName()).isEqualTo("competence_success"));
                // Pass a function that takes 'result' as input and runs this assertion.
            }
}
