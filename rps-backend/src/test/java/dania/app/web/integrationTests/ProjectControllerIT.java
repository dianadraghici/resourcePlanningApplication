package dania.app.web.integrationTests;

import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.controllers.dto.ProjectDTO;
import dania.app.web.integrationTests.ServiceTests.CreateCalendarDTOTest;
import dania.app.web.integrationTests.ServiceTests.CreateProjectDTOTest;
import dania.app.web.integrationTests.config.TestDatabaseConfig;
import dania.app.web.integrationTests.config.TestWebAppConfig;
import dania.app.web.service.CalendarService;
import dania.app.web.service.ParameterService;
import dania.app.web.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import dania.app.web.integrationTests.config.*;
import dania.app.web.utils.Constants;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.http.MediaType;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDatabaseConfig.class, TestWebAppConfig.class})
@WebAppConfiguration
@TestPropertySource(TestConstants.TEST_PROPERTY_SOURCE)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProjectControllerIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private CreateCalendarDTOTest createCalendarDTOTest;

    @Autowired
    private CreateProjectDTOTest createProjectDTOTest;

    private MockMvc mockMvc;

    private ProjectDTO projectExpected;


    @Before
    public void setupProjectControllerIT() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        parameterService.create(getPercentParameterDto());
        parameterService.create(getStatusParameterDto());
        calendarService.create(createCalendarDTOTest.getCalendarDTO());

        projectExpected = createProjectDTOTest.getProjectDto();
        projectService.create(createProjectDTOTest.getProjectDto());
    }

    @Test
    public void getProjectList_givenOneProject_expectProjectToBeReturnedSuccessfully() throws Exception {
        //given
        getMvcResult();

        //when
        //then
        assertEquals(1, getJsonArray().length());
    }

    @Test
    public void findProjectsPage_givenOneProject_expectProjectToBeReturnedSuccessfully() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(Constants.PROJECT_CONTROLLER + Constants.FIND_MEMBER_PAGINATED)
                        .param("page", "0")
                        .param("size", "50")
                        .param("sortBy", "id")
                        .param("direction", "ASC"))
                .andExpect(status().isOk())
                .andReturn();
        //then
        checkNameCodeFromProject();
    }

    @Test///
    public void getProjectByCode_givenOneProject_expectProjectToBeReturnedSuccessfully() throws Exception{
        //given
        //when
       mockMvc.perform(MockMvcRequestBuilders.get(Constants.PROJECT_CONTROLLER + Constants.FIND_PROJECT_BY_CODE, TestConstants.PROJECT_CODE))
                .andExpect(status().isOk())
                .andReturn();
        //then
        checkNameCodeFromProject();
    }

    @Test
    public void createProject_givenOneProject_expectCreatedProject() throws  Exception{
//        given
        getNewProject();

        //when
        mockMvc.perform(MockMvcRequestBuilders.post(Constants.PROJECT_CONTROLLER )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(getNewProject())))
                .andExpect(status().isOk())
                .andReturn();
        //then
        assertEquals(2, getJsonArray().length());
    }

    @Test
    public  void updateProjectById_givenOneProject_expectUpdatedProject() throws  Exception{
        // given
        projectExpected.setProjectName("New name");
        projectExpected.setProjectCode("new code");

        // when
        mockMvc.perform(MockMvcRequestBuilders.put(Constants.PROJECT_CONTROLLER + Constants.UPDATE_PROJECT_BY_ID, TestConstants.ONE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(projectExpected)))
                .andExpect(status().isOk())
                .andReturn();
        // then
        checkNameCodeFromProject();
    }

    @Test
    public void deleteProject_givenOneProject_expectEmptyTable() throws  Exception{
        // given
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete(Constants.PROJECT_CONTROLLER + Constants.DELETE_PROJECT, TestConstants.ONE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(0, getJsonArray().length());
    }

    private JSONArray getJsonArray() throws Exception {
        return new JSONArray(getMvcResult().getResponse().getContentAsString());
    }

    private void checkNameCodeFromProject() throws Exception {
        assertEquals(projectExpected.getProjectCode(), getJsonArray().getJSONObject(0).getString(TestConstants.PROJECT_CODE));
        assertEquals(projectExpected.getProjectName(), getJsonArray().getJSONObject(0).getString(TestConstants.PROJECT_NAME));
    }

    private MvcResult getMvcResult() throws Exception{
         return mockMvc .perform(MockMvcRequestBuilders.get(Constants.PROJECT_CONTROLLER))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    private ProjectDTO getNewProject(){
        ProjectDTO expected = new ProjectDTO();
        expected.setStartDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        expected.setEndDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        expected.setId(2);
        expected.setProjectName("new name");
        expected.setProjectCode(TestConstants.PROJECT_CODE);
        expected.setStatusId(1);
        expected.setPercentId(5);
        expected.setPercentDescription(TestConstants.FIFTY);
        expected.setStatusDescription(TestConstants.IN_NEGOTIATION);
        return expected;
    }

    private ParametersDTO getPercentParameterDto() {
        ParametersDTO parametersDTO = new ParametersDTO();
        parametersDTO.setId(5);
        parametersDTO.setDescription(TestConstants.FIFTY);
        parametersDTO.setType(TestConstants.PERCENT);
        return parametersDTO;
    }

    private ParametersDTO getStatusParameterDto() {
        ParametersDTO parametersDTO = new ParametersDTO();
        parametersDTO.setId(1);
        parametersDTO.setDescription(TestConstants.IN_NEGOTIATION);
        parametersDTO.setType(TestConstants.STATUS);
        return parametersDTO;
    }
}