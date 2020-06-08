package dania.app.web.integrationTests;

import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.controllers.dto.ProjectPositionDTO;
import dania.app.web.integrationTests.ServiceTests.CreateCalendarDTOTest;
import dania.app.web.integrationTests.ServiceTests.CreateParameterDTOTest;
import dania.app.web.integrationTests.ServiceTests.CreateProjectDTOTest;
import dania.app.web.integrationTests.config.TestDatabaseConfig;
import dania.app.web.integrationTests.config.TestWebAppConfig;
import dania.app.web.service.CalendarService;
import dania.app.web.service.ParameterService;
import dania.app.web.service.ProjectPositionService;
import dania.app.web.service.ProjectService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dania.app.web.integrationTests.config.*;
import dania.app.web.utils.Constants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableMBeanExport;
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

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDatabaseConfig.class, TestWebAppConfig.class})
@WebAppConfiguration
@TestPropertySource(TestConstants.TEST_PROPERTY_SOURCE)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ScheduleProjectControllerIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private CreateParameterDTOTest createParameterDTOTest;
    private MockMvc mockMvc;
    @Resource
    private CalendarService calendarService;
    @Resource
    private ParameterService parameterService;
    @Resource
    private ProjectPositionService projectPositionService;
    @Resource
    private ProjectService projectService;
    @Autowired
    private CreateCalendarDTOTest createCalendarDTOTest;
    @Autowired
    private CreateProjectDTOTest createProjectDTOTest;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        calendarService.create(createCalendarDTOTest.getCalendarDTO());
        parameterService.create(createParameterDTOTest.getParameterDto(TestConstants.TEN_INTEGER, TestConstants.POSITION_DEVOPS, TestConstants.POSITION));
        parameterService.create(createParameterDTOTest.getParameterDto(TestConstants.THREE_INTEGER, TestConstants.THIRTY, TestConstants.PERCENT));
        projectService.create(createProjectDTOTest.getProjectDto());
        projectPositionService.create(getProjectPositionDto());
    }

    @Test
    public void getPositionParameters_expectParameterToBeReturnedSuccessfully() throws Exception {
        // given
        // when
        MvcResult mvcResult = getMvcResult(Constants.GET_POSITION_PARAMETERS);

        // then
        assertNotNull(mvcResult);
        List<ParametersDTO> result = getResponse(mvcResult);
        assertEquals(1, result.size());

        ParametersDTO actual = result.get(0);
        assertResponseDto(createParameterDTOTest.getParameterDto(TestConstants.TEN_INTEGER, TestConstants.POSITION_DEVOPS, TestConstants.POSITION), actual);
    }

    @Test
    public void findProjectPositionByProjectId_givenOneProjectPosition_expectedProjectToBeReturnedSuccessfully() throws Exception {
        //given
        //when
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(Constants.SCHEDULE_PROJECT_CONTROLLER + Constants.FIND_BY_ID_PROJECT_POSITION, 1))
                .andExpect(status().isOk())
                .andReturn();
        //then
        JSONArray jsonArray = getJSONArray(mvcResult, TestConstants.POSITION_DEVOPS);

        Assert.assertEquals(getProjectPositionDto().getPositionDescription(), jsonArray.getJSONObject(0).getString(TestConstants.POSITION_DESCRIPTION));
        Assert.assertEquals(getProjectPositionDto().getProjectName(), jsonArray.getJSONObject(0).getString(TestConstants.PROJECT_NAME));
        Assert.assertEquals(getProjectPositionDto().getPercentId(), jsonArray.getJSONObject(0).get(TestConstants.PERCENT_ID));
    }

    @Test
    public void getMapPositionsByProject_expectPositionsToBeReturnedSuccessfully() throws Exception {
        // given
        // when
        MvcResult mvcResult = getMvcResult(Constants.GET_MAP_POSITIONS_BY_PROJECT);

        // then
        JSONArray jsonArray = getJSONArray(mvcResult, TestConstants.PROJECT_NAME);

        Assert.assertEquals(getProjectPositionDto().getProjectName(), jsonArray.getJSONObject(0).get(TestConstants.PROJECT_NAME));
    }

    private MvcResult getMvcResult(String constant) throws Exception {
        return mockMvc
                .perform(get(Constants.SCHEDULE_PROJECT_CONTROLLER + constant))
                .andExpect(status().isOk())
                .andReturn();
    }

    private JSONArray getJSONArray(MvcResult mvcResult, String constant) throws Exception {
        return (new JSONObject(mvcResult.getResponse().getContentAsString())).getJSONArray(constant);
    }

    private void assertResponseDto(ParametersDTO expected, ParametersDTO actual) {
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getType(), actual.getType());
    }

    private List<ParametersDTO> getResponse(MvcResult mvcResult) throws IOException {
        List<ParametersDTO> tResponse = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<ParametersDTO>>() {
        });
        assertNotNull(tResponse);
        return tResponse;
    }

    private ProjectPositionDTO getProjectPositionDto() {
        ProjectPositionDTO projectPositionDto = new ProjectPositionDTO();
        projectPositionDto.setId(1);
        projectPositionDto.setNumberPositions(2);
        projectPositionDto.setProjectDTO(createProjectDTOTest.getProjectDto());
        projectPositionDto.setStartDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        projectPositionDto.setEndDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());

        projectPositionDto.setPositionId(10);
        projectPositionDto.setPositionDescription(TestConstants.POSITION_DEVOPS);
        projectPositionDto.setPercentId(3);
        projectPositionDto.setPercentDescription(TestConstants.THIRTY);
        return projectPositionDto;
    }
}
