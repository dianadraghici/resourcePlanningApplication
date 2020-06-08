package dania.app.web.integrationTests;

import dania.app.web.controllers.dto.*;

import dania.app.web.integrationTests.ServiceTests.CreateCalendarDTOTest;
import dania.app.web.integrationTests.ServiceTests.CreateMemberDTOTest;
import dania.app.web.integrationTests.ServiceTests.CreateParameterDTOTest;
import dania.app.web.integrationTests.config.TestDatabaseConfig;
import dania.app.web.integrationTests.config.TestWebAppConfig;
import dania.app.web.service.MemberPositionService;
import dania.app.web.service.MemberService;
import dania.app.web.service.ProjectService;
import dania.app.web.service.ProjectPositionService;
import dania.app.web.service.ParameterService;
import dania.app.web.service.CalendarService;
import dania.app.web.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import dania.app.web.integrationTests.config.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
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
import javax.annotation.Resource;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDatabaseConfig.class, TestWebAppConfig.class})
@WebAppConfiguration
@TestPropertySource(TestConstants.TEST_PROPERTY_SOURCE)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class MemberPositionControllerIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private CreateCalendarDTOTest createCalendarDTOTest;
    @Autowired
    private CreateMemberDTOTest createMemberDTOTest;
    @Autowired
    private CreateParameterDTOTest createParameterDTOTest;
    @Resource
    private MemberService memberService;
    @Resource
    private ProjectService projectService;
    @Resource
    private MemberPositionService memberPositionService;
    @Resource
    private ProjectPositionService projectPositionService;
    @Resource
    private ParameterService parameterService;
    @Resource
    private CalendarService calendarService;
    private MockMvc mockMvc;
    private MemberDTO memberDTOExpected;
    private MemberPositionDTO memberPositionDTOExpected;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        parameterService.create(createParameterDTOTest.getParameterDto(TestConstants.TWO_INTEGER, TestConstants.TWENTY, TestConstants.PERCENT));
        parameterService.create(createParameterDTOTest.getParameterDto(TestConstants.ONE_INTEGER, TestConstants.TEN, TestConstants.PERCENT));
        parameterService.create(createParameterDTOTest.getParameterDto(TestConstants.ONE_INTEGER, TestConstants.IN_NEGOTIATION, TestConstants.STATUS));
        parameterService.create(createParameterDTOTest.getParameterDto(TestConstants.TWO_INTEGER, TestConstants.JUNIOR_JAVA_DEVELOPER, TestConstants.POSITION));
        parameterService.create(createParameterDTOTest.getParameterDto(TestConstants.ONE_INTEGER, TestConstants.DATA, TestConstants.TECH));

        calendarService.create(createCalendarDTOTest.getCalendarDTO());
        calendarService.create(createCalendarDTOTest.getCalendarWithIdTwo());

        memberDTOExpected = createMemberDTOTest.getMemberDTO(TestConstants.ONE_INTEGER);
        memberService.create(memberDTOExpected);
        memberService.create(createMemberDTOTest.getMemberDTO(TestConstants.TWO_INTEGER));

        projectService.create(getProjectDTO());
        projectPositionService.create(getProjectPositionDTO());

        memberPositionDTOExpected = getMemberPositionDTO();
        memberPositionService.create(memberPositionDTOExpected);
    }

    @Test
    public void findProjectPositionByMemberId_givenOneProjectPositionOneMemberPosition_expectProjectPositionToBeReturnedSuccessfully() throws Exception{
        //given
        //when
        String contentAsString = findMemberPositionWithIdOne().getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(contentAsString);
        JSONObject memberPositionJsonObject = jsonArray.getJSONObject(0);
        JSONObject memberJsonObject  = memberPositionJsonObject.getJSONObject(TestConstants.MEMBER_DTO);
        JSONObject projectPositionJsonObject  = memberPositionJsonObject.getJSONObject(TestConstants.PROJECT_POSITION_DTO);

        //then
        assertNotNull(memberPositionJsonObject);
        assertEquals(7, memberPositionJsonObject.length());
        assertResponseMemberDTO(memberDTOExpected, memberJsonObject);
        assertResponseProjectPositionDTO(getProjectPositionDTO(), projectPositionJsonObject);
        assertResponseMemberPositionDTO(getMemberPositionDTO(), memberPositionJsonObject);
    }

    @Test
    public void create_givenOneMemberPosition_expectCreateMemberPosition() throws Exception{
        //given

        String contentAsString = findMemberPositionWithIdOne().getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(contentAsString);
        JSONObject memberPositionJsonObject = jsonArray.getJSONObject(0);
        assertNotNull(memberPositionJsonObject);

        //when
        MemberPositionDTO memberPositionDTOToBeCreated = getMemberPositionDTO();
        memberPositionDTOToBeCreated.setMemberDTO(createMemberDTOTest.getMemberDTO(TestConstants.TWO_INTEGER));
        memberPositionDTOToBeCreated.setId(TestConstants.TWO_INTEGER);

        String s = OBJECT_MAPPER.writeValueAsString(memberPositionDTOToBeCreated);

        MvcResult mvcResultToBeCreated = mockMvc
                .perform(MockMvcRequestBuilders.post(Constants.MEMBER_POSITION_CONTROLLER, memberPositionDTOToBeCreated)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(TestConstants.CHARACTER_ENCODING_UTF_8)
                    .content(s))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResultAfterCreateMemberPosition = mockMvc
                .perform(MockMvcRequestBuilders.get(Constants.MEMBER_POSITION_CONTROLLER + Constants.FIND_BY_ID_MEMBER_POSITION, TestConstants.TWO_INTEGER))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertNotNull(mvcResultToBeCreated);
        assertNotNull(mvcResultAfterCreateMemberPosition);

        contentAsString = mvcResultAfterCreateMemberPosition.getResponse().getContentAsString();
        jsonArray = new JSONArray(contentAsString);
        memberPositionJsonObject = jsonArray.getJSONObject(0);
        assertNotNull(memberPositionJsonObject);

        JSONObject memberJsonObject  = memberPositionJsonObject.getJSONObject(TestConstants.MEMBER_DTO);
        assertResponseMemberDTO(memberDTOExpected, memberJsonObject);
    }

    @Test
    public void update_givenOneMemberPosition_expectUpdatedMemberPosition() throws Exception{
        //given
        memberPositionDTOExpected.setPercentId(TestConstants.TWO_INTEGER);
        memberPositionDTOExpected.setPercentDescription(TestConstants.TWENTY);
        memberPositionDTOExpected.setStartDateCalendarDTO(createCalendarDTOTest.getCalendarWithIdTwo());

        //when
        MvcResult mvcResultToBeUpdated = mockMvc
                .perform(put(Constants.MEMBER_POSITION_CONTROLLER + Constants.UPDATE_MEMBER_POSITION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(memberPositionDTOExpected)))
                .andExpect(status().isOk())
                .andReturn();
        assertNotNull(mvcResultToBeUpdated);

        //then
        MvcResult mvcResult = findMemberPositionWithIdOne();
        assertNotNull(mvcResult);

        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(contentAsString);
        JSONObject memberPositionJsonObject = jsonArray.getJSONObject(0);
        String  percentDescription  = memberPositionJsonObject.getString(TestConstants.PERCENT_DESCRIPTION);
        String startDateForFiscalYear = memberPositionJsonObject.getJSONObject(TestConstants.START_DATE_CALENDAR_DTO).getString(TestConstants.FISCAL_YEAR);
        assertEquals(memberPositionDTOExpected.getPercentDescription(), percentDescription);
        assertEquals(memberPositionDTOExpected.getStartDateCalendarDTO().getFiscalYear().toString(), startDateForFiscalYear);
    }

    @Test
    public void delete_givenOneMemberPosition_expectMemberPositionDeleted() throws Exception {
        //given
        MvcResult result = findMemberPositionWithIdOne();
        String contentBeforeDelete = result.getResponse().getContentAsString();
        assertNotNull(contentBeforeDelete);

        //when
        MvcResult mvcResultToBeDeleted = mockMvc
                .perform(MockMvcRequestBuilders.delete(Constants.MEMBER_POSITION_CONTROLLER + Constants.DELETE_MEMBER_POSITION, TestConstants.ONE_INTEGER))
                .andExpect(status().isOk())
                .andReturn();

        //then
        String contentAfterDelete = mvcResultToBeDeleted.getResponse().getContentAsString();
        String contentFindMemberWithIdOne = findMemberPositionWithIdOne().getResponse().getContentAsString();
        Assert.assertEquals(contentAfterDelete+ TestConstants.EMPTY_ARRAY, contentFindMemberWithIdOne);
    }

    private MvcResult findMemberPositionWithIdOne() throws Exception{
        return  mockMvc
                .perform(MockMvcRequestBuilders.get(Constants.MEMBER_POSITION_CONTROLLER + Constants.FIND_BY_ID_MEMBER_POSITION, TestConstants.ONE_INTEGER))
                .andExpect(status().isOk())
                .andReturn();
    }

    private void assertResponseMemberPositionDTO(MemberPositionDTO expected, JSONObject actual)throws Exception {
        JSONObject memberJsonObject  = actual.getJSONObject(TestConstants.MEMBER_DTO);
        JSONObject projectPositionJsonObject  = actual.getJSONObject(TestConstants.PROJECT_POSITION_DTO);
        JSONObject startDateCalendarJsonObject  = actual.getJSONObject(TestConstants.START_DATE_CALENDAR_DTO);
        JSONObject endDateCalendarJsonObject  = actual.getJSONObject(TestConstants.END_DATE_CALENDAR_DTO);

        assertEquals(expected.getPercentId(), actual.get(TestConstants.PERCENT_ID));
        assertEquals(expected.getId(), actual.get(TestConstants.ID));
        assertEquals(expected.getPercentDescription(), actual.get(TestConstants.PERCENT_DESCRIPTION));
        assertResponseCalendarDTO(expected.getStartDateCalendarDTO(), startDateCalendarJsonObject);
        assertResponseCalendarDTO(expected.getEndDateCalendarDTO(), endDateCalendarJsonObject);
        assertResponseProjectPositionDTO(expected.getProjectPositionDTO(), projectPositionJsonObject);
        assertResponseMemberDTO(expected.getMemberDTO(), memberJsonObject);
    }

    private void assertResponseMemberDTO(MemberDTO expected, JSONObject actual)throws Exception {
        assertEquals(expected.getStaffNumber(), actual.get(TestConstants.STAFF_NUMBER));
        assertEquals(expected.getTechnologyDescription(), actual.get(TestConstants.TECHNOLOGY_DESCRIPTION));
        assertEquals(expected.getTechnologyId(), actual.get(TestConstants.TECHNOLOGY_ID));
        assertEquals(expected.getFirstName(), actual.get(TestConstants.FIRSTNAME));
        assertEquals(expected.getComment(), actual.get(TestConstants.COMMENT));
        int flag = (int)actual.get(TestConstants.FLAG);
        assertEquals(expected.getFlag(), Byte.valueOf((byte) flag));
        assertEquals(expected.getLastName(), actual.get(TestConstants.LASTNAME));
    }

    private void assertResponseProjectPositionDTO(ProjectPositionDTO expected, JSONObject actual) throws Exception{
        JSONObject projectJsonObject  = actual.getJSONObject(TestConstants.PROJECT_DTO);
        JSONObject startDateCalendarJsonObject  = actual.getJSONObject(TestConstants.START_DATE_CALENDAR_DTO);
        JSONObject endDateCalendarJsonObject  = actual.getJSONObject(TestConstants.END_DATE_CALENDAR_DTO);
        assertEquals(expected.getId(), actual.get(TestConstants.ID));
        assertEquals(expected.getNumberPositions(), actual.get(TestConstants.MEMBER_POSITIONS));
        assertEquals(expected.getPercentId(), actual.get(TestConstants.PERCENT_ID));
        assertEquals(expected.getPositionId(), actual.get(TestConstants.POSITION_ID));
        assertEquals(expected.getPositionDescription(), actual.get(TestConstants.POSITION_DESCRIPTION));
        assertResponseProjectDTO(expected.getProjectDTO(),projectJsonObject);
        assertResponseCalendarDTO(expected.getStartDateCalendarDTO(), startDateCalendarJsonObject);
        assertResponseCalendarDTO(expected.getEndDateCalendarDTO(), endDateCalendarJsonObject);
    }

    private void assertResponseProjectDTO(ProjectDTO expected, JSONObject actual)throws Exception{
        JSONObject startDateCalendarJsonObject  = actual.getJSONObject(TestConstants.START_DATE_CALENDAR_DTO);
        JSONObject endDateCalendarJsonObject  = actual.getJSONObject(TestConstants.END_DATE_CALENDAR_DTO);
        assertEquals(expected.getId(), actual.get(TestConstants.ID));
        assertEquals(expected.getStatusId(), actual.get(TestConstants.STATUS_ID));
        assertEquals(expected.getPercentId(), actual.get(TestConstants.PERCENT_ID));
        assertEquals(expected.getProjectCode(), actual.get(TestConstants.PROJECT_CODE));
        assertEquals(expected.getProjectName(), actual.get(TestConstants.PROJECT_NAME));
        assertEquals(expected.getPercentDescription(), actual.get(TestConstants.PERCENT_DESCRIPTION));
        assertEquals(expected.getStatusDescription(), actual.get(TestConstants.STATUS_DESCRIPTION));
        assertResponseCalendarDTO(expected.getStartDateCalendarDTO(), startDateCalendarJsonObject);
        assertResponseCalendarDTO(expected.getEndDateCalendarDTO(), endDateCalendarJsonObject);
    }

    private void assertResponseCalendarDTO(CalendarDTO expected, JSONObject actual) throws Exception{
        assertEquals(expected.getBop().toString(), actual.get(TestConstants.BOP));
        assertEquals(expected.getEop().toString(), actual.get(TestConstants.EOP));
        assertEquals(expected.getQuarter(), actual.get(TestConstants.QUARTER));
        assertEquals(expected.getId(), actual.get(TestConstants.ID));
        assertEquals(expected.getFiscalYear(), actual.get(TestConstants.FISCAL_YEAR));
        assertEquals(expected.getWeek(), actual.get(TestConstants.WEEK));
        assertEquals(expected.getPeriod(), actual.get(TestConstants.PERIOD));
    }

    private ProjectPositionDTO getProjectPositionDTO(){
        ProjectPositionDTO projectPositionDTO = new ProjectPositionDTO();
        projectPositionDTO.setId(TestConstants.ONE_INTEGER);
        projectPositionDTO.setProjectDTO(getProjectDTO());
        projectPositionDTO.setNumberPositions(TestConstants.TWO_INTEGER);
        projectPositionDTO.setPercentId(TestConstants.ONE_INTEGER);
        projectPositionDTO.setPositionDescription(TestConstants.JUNIOR_JAVA_DEVELOPER);
        projectPositionDTO.setPositionId(TestConstants.TWO_INTEGER);
        projectPositionDTO.setPercentDescription(TestConstants.TEN);
        projectPositionDTO.setEndDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        projectPositionDTO.setStartDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        return projectPositionDTO;
    }

    private ProjectDTO getProjectDTO(){
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectName(TestConstants.PROJECT_NAME);
        projectDTO.setProjectCode(TestConstants.PROJECT_CODE);
        projectDTO.setId(TestConstants.ONE_INTEGER);
        projectDTO.setPercentId(TestConstants.ONE_INTEGER);
        projectDTO.setStatusId(TestConstants.ONE_INTEGER);
        projectDTO.setStatusDescription(TestConstants.IN_NEGOTIATION);
        projectDTO.setPercentDescription(TestConstants.TEN);
        projectDTO.setStartDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        projectDTO.setEndDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        return projectDTO;
    }

    private MemberPositionDTO getMemberPositionDTO(){
        MemberPositionDTO memberPositionDTO = new MemberPositionDTO();
        memberPositionDTO.setId(TestConstants.ONE_INTEGER);
        memberPositionDTO.setMemberDTO(createMemberDTOTest.getMemberDTO(TestConstants.ONE_INTEGER));
        memberPositionDTO.setPercentId(TestConstants.ONE_INTEGER);
        memberPositionDTO.setProjectPositionDTO(getProjectPositionDTO());
        memberPositionDTO.setEndDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        memberPositionDTO.setStartDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        memberPositionDTO.setPercentDescription(TestConstants.TEN);
        return memberPositionDTO;
    }
}
