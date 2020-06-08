package dania.app.web.integrationTests;

import dania.app.web.controllers.dto.MemberPositionViewDTO;
import dania.app.web.integrationTests.ServiceTests.CreateCalendarDTOTest;
import dania.app.web.integrationTests.config.TestDatabaseConfig;
import dania.app.web.integrationTests.config.TestWebAppConfig;
import dania.app.web.service.CalendarService;
import dania.app.web.service.MemberPositionViewService;
import dania.app.web.utils.Constants;
import dania.app.web.integrationTests.config.*;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDatabaseConfig.class, TestWebAppConfig.class})
@WebAppConfiguration
@TestPropertySource(TestConstants.TEST_PROPERTY_SOURCE)
@EnableMBeanExport(registration=RegistrationPolicy.IGNORE_EXISTING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ScheduleMemberControllerIT {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Resource
    private CalendarService calendarService;
    @Resource
    private MemberPositionViewService memberPositionViewService;
    @Autowired
    private CreateCalendarDTOTest createCalendarDTOTest;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        calendarService.create(createCalendarDTOTest.getCalendarDTO());
        memberPositionViewService.create(getMemberPositionViewDTO());
    }

    @Test
    public void getMapPositionsByMember_expectMapPositionsByMemberToBeReturnedSuccessfully() throws Exception {
        // given
        // when
        MvcResult mvcResult = getMvcResult(Constants.GET_MAP_POSITIONS_BY_MEMBER);

        // then
        JSONArray jsonArray = getJSONArray(mvcResult, TestConstants.FIRSTNAME_LASTNAME);

        Assert.assertEquals(getMemberPositionViewDTO().getFirstName(), jsonArray.getJSONObject(0).get(TestConstants.FIRSTNAME));
        Assert.assertEquals(getMemberPositionViewDTO().getLastName(), jsonArray.getJSONObject(0).get(TestConstants.LASTNAME));
        Assert.assertEquals(getMemberPositionViewDTO().getPercentMember(), jsonArray.getJSONObject(0).get(TestConstants.PERCENT_MEMBER));
        Assert.assertEquals(getMemberPositionViewDTO().getProjectName(), jsonArray.getJSONObject(0).get(TestConstants.PROJECT_NAME));
    }

    private MvcResult getMvcResult(String constant) throws Exception{
        return mockMvc
                .perform(get(Constants.SCHEDULE_MEMBER_CONTROLLER + constant))
                .andExpect(status().isOk())
                .andReturn();
    }

    private JSONArray getJSONArray(MvcResult mvcResult, String constant) throws Exception {
        return (new JSONObject(mvcResult.getResponse().getContentAsString())).getJSONArray(constant);
    }

    private MemberPositionViewDTO getMemberPositionViewDTO() {
        MemberPositionViewDTO memberPositionViewDTO = new MemberPositionViewDTO();
        memberPositionViewDTO.setId(TestConstants.ONE);
        memberPositionViewDTO.setFirstName(TestConstants.FIRSTNAME);
        memberPositionViewDTO.setLastName(TestConstants.LASTNAME);
        memberPositionViewDTO.setIdCalendarStartDateFk(TestConstants.FY19_P03_W11);
        memberPositionViewDTO.setIdCalendarEndDateFk(TestConstants.FY19_P12_W52);
        memberPositionViewDTO.setIdProjectPositionFk(8);
        memberPositionViewDTO.setPercentPosition(TestConstants.ONE_HUNDRED);
        memberPositionViewDTO.setPercentMember(TestConstants.ONE_HUNDRED);
        memberPositionViewDTO.setPercentProject(TestConstants.EIGHTY);
        memberPositionViewDTO.setStatusProject(TestConstants.IN_NEGOTIATION);
        memberPositionViewDTO.setProjectName(TestConstants.THALES_JAVA_23_11);
        return memberPositionViewDTO ;
    }

}
