package dania.app.web.integrationTests;

import dania.app.web.integrationTests.ServiceTests.CreateCalendarDTOTest;
import dania.app.web.integrationTests.config.TestDatabaseConfig;
import dania.app.web.integrationTests.config.TestWebAppConfig;
import dania.app.web.service.CalendarService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDatabaseConfig.class, TestWebAppConfig.class})
@WebAppConfiguration
@TestPropertySource(TestConstants.TEST_PROPERTY_SOURCE)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CalendarControllerIT {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Resource
    private CalendarService calendarService;
    @Autowired
    private CreateCalendarDTOTest createCalendarDTOTest;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        calendarService.create(createCalendarDTOTest.getCalendarDTO());
    }

    @Test
    public void getMapCalendarByQuarterYear_expectCalendarByQuarterYearToBeReturnedSuccessfully() throws Exception {
        // given
        // when
        String requestResultString = mockMvc
                .perform(MockMvcRequestBuilders.get(Constants.CALENDAR_CONTROLLER + Constants.GET_MAP_CALENDAR_BY_QUARTER_YEAR))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        JSONArray firstQuarterOf2019 = (new JSONObject(requestResultString)).getJSONArray(TestConstants.YEAR_2019_AND_QUARTER_1);
        // then
        Assert.assertEquals(createCalendarDTOTest.getCalendarDTO().getQuarter(), firstQuarterOf2019.getJSONObject(0).get(TestConstants.QUARTER));
    }

}
