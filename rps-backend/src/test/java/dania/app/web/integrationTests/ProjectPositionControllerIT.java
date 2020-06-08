package dania.app.web.integrationTests;

import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.controllers.dto.ProjectDTO;
import dania.app.web.controllers.dto.ProjectPositionDTO;
import dania.app.web.integrationTests.ServiceTests.CreateCalendarDTOTest;
import dania.app.web.integrationTests.ServiceTests.CreateProjectDTOTest;
import dania.app.web.integrationTests.config.TestDatabaseConfig;
import dania.app.web.integrationTests.config.TestWebAppConfig;
import dania.app.web.service.CalendarService;
import dania.app.web.service.ParameterService;
import dania.app.web.service.ProjectPositionService;
import dania.app.web.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import dania.app.web.integrationTests.config.*;
import dania.app.web.utils.Constants;
import org.json.JSONArray;
import org.json.JSONObject;
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

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDatabaseConfig.class, TestWebAppConfig.class})
@WebAppConfiguration
@TestPropertySource(TestConstants.TEST_PROPERTY_SOURCE)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProjectPositionControllerIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ProjectPositionService projectPositionService;

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

    private ProjectPositionDTO expected;

    private ProjectDTO projectDTO;

    @Before
    public void setupProjectPositionControllerIT(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        calendarService.create(createCalendarDTOTest.getCalendarDTO());
        parameterService.create(getPositionParameterDto());
        parameterService.create(getPercentParameterDto());

        projectDTO =createProjectDTOTest.getProjectDto();
        projectService.create(projectDTO);

        expected = getProjectPositionDto();
        projectPositionService.create(expected);
    }

    @Test
    public void findProjectPositionByProjectId_givenOneProjectPosition_expectedProjectToBeReturnedSuccessfully() throws  Exception{
        assertResponseForJsonArray();
    }

    @Test//
    public void createProjectPosition_givenOneProjectPosition_expectedCreatedProjectPosition() throws  Exception{
        //given
        expected.setId(2);
        expected.setNumberPositions(4);
        expected.setPositionDescription("Junior Java Developer");

        //when
        mockMvc.perform(MockMvcRequestBuilders.post(Constants.PROJECT_POSITION_CONTROLLER, expected)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(expected)))
                .andExpect(status().isOk())
                .andReturn();
        //then
        assertEquals(2, getJsonObject().getJSONArray(TestConstants.POSITION_DEVOPS).length());
    }

    @Test
    public  void updateProjectPositionById_givenOneProjectPosition_expectUpdatedProject() throws Exception{
        //given
        expected.setId(1);
        expected.setNumberPositions(5);

        //when
        mockMvc.perform(MockMvcRequestBuilders.put(Constants.PROJECT_POSITION_CONTROLLER + Constants.UPDATE_PROJECT_POSITION, expected)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(expected)))
                .andExpect(status().isOk())
                .andReturn();
        //then
        assertResponseForJsonArray();
    }


    @Test
    public void deleteProjectPosition_givenOneProjectPosition_expectEmptyTable() throws  Exception{
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete(Constants.PROJECT_POSITION_CONTROLLER + Constants.DELETE_PROJECT_POSITION, TestConstants.ONE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        assertEquals(0, getJsonObject().length());
    }

    private void  assertResponseForJsonArray() throws Exception{
        JSONArray jsonArray = getJsonObject().getJSONArray(TestConstants.POSITION_DEVOPS);
        assertEquals(expected.getId(), jsonArray.getJSONObject(0).get("id"));
        assertEquals(expected.getNumberPositions(), jsonArray.getJSONObject(0).get(TestConstants.NUMBER_POSITIONS));
        assertEquals(expected.getPositionDescription(), jsonArray.getJSONObject(0).getString(TestConstants.POSITION_DESCRIPTION));
        assertEquals(expected.getProjectName(), jsonArray.getJSONObject(0).getString(TestConstants.PROJECT_NAME));
        assertEquals(expected.getPercentId(), jsonArray.getJSONObject(0).get(TestConstants.PERCENT_ID));
    }

    private JSONObject getJsonObject() throws Exception {
        return new JSONObject(getMvcResult().getResponse().getContentAsString());
    }

    private MvcResult getMvcResult() throws Exception{
        return mockMvc .perform(MockMvcRequestBuilders.get(Constants.PROJECT_POSITION_CONTROLLER + Constants.FIND_BY_ID_PROJECT_POSITION, TestConstants.ONE))
                .andExpect(status().isOk())
                .andReturn();
    }

    private ProjectPositionDTO getProjectPositionDto(){
        ProjectPositionDTO projectPositionDto = new ProjectPositionDTO();
        projectPositionDto.setId(1);
        projectPositionDto.setNumberPositions(2);
        projectPositionDto.setProjectDTO(projectDTO);
        projectPositionDto.setStartDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        projectPositionDto.setEndDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        projectPositionDto.setPositionId(10);
        projectPositionDto.setPositionDescription(TestConstants.POSITION_DEVOPS);
        projectPositionDto.setPercentId(3);
        projectPositionDto.setPercentDescription(TestConstants.THIRTY);
        return projectPositionDto;
    }

    private ParametersDTO getPositionParameterDto() {
        ParametersDTO parametersDto = new ParametersDTO();
        parametersDto.setId(10);
        parametersDto.setDescription(TestConstants.POSITION_DEVOPS);
        parametersDto.setType(TestConstants.POSITION);
        return parametersDto;
    }

    private ParametersDTO getPercentParameterDto() {
        ParametersDTO parametersDTO = new ParametersDTO();
        parametersDTO.setId(3);
        parametersDTO.setDescription(TestConstants.THIRTY);
        parametersDTO.setType(TestConstants.PERCENT);
        return parametersDTO;
    }
}
