package dania.app.web.integrationTests;

import dania.app.web.controllers.dto.MemberDTO;
import dania.app.web.entities.CalendarEntity;
import dania.app.web.integrationTests.ServiceTests.CreateMemberDTOTest;
import dania.app.web.integrationTests.ServiceTests.CreateParameterDTOTest;
import dania.app.web.integrationTests.config.TestDatabaseConfig;
import dania.app.web.integrationTests.config.TestWebAppConfig;
import dania.app.web.repository.CalendarRepository;
import dania.app.web.service.MemberService;
import dania.app.web.service.ParameterService;
import dania.app.web.utils.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dania.app.web.integrationTests.config.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDatabaseConfig.class, TestWebAppConfig.class})
@WebAppConfiguration
@TestPropertySource(TestConstants.TEST_PROPERTY_SOURCE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberControllerIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private MemberDTO expected;
    @MockBean
    private CalendarRepository calendarRepository;
    @Mock
    private CalendarEntity calendarEntity;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Resource
    private MemberService memberService;
    @Resource
    private ParameterService parameterService;
    @Autowired
    private CreateParameterDTOTest createParameterDTOTest;
    @Autowired
    private CreateMemberDTOTest createMemberDTOTest;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        expected = createMemberDTOTest.getMemberDTO(TestConstants.ONE_INTEGER);
        parameterService.create(createParameterDTOTest.getParameterDto(TestConstants.ONE_INTEGER, TestConstants.DATA, TestConstants.TECH));
        memberService.create(expected);
        calendarEntity.setPeriod(55);
    }

    @Test
    public void findAllMembers_givenOneMember_expectMemberToBeReturnedSuccessfully() throws Exception {
        // given
        // when
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(Constants.MEMBER_CONTROLLER))
                .andExpect(status().isOk())
                .andReturn();
        // then
        assertNotNull(mvcResult);
        List<MemberDTO> result = getResponse(mvcResult);
        assertEquals(1, result.size());
        MemberDTO actual = result.get(0);
        assertResponseDto(expected, actual);
    }

    @Test
    public void findMembersPage_givenOneMember_expectMemberToBeReturnedSuccessfully() throws Exception {
        // given
        // when
        MvcResult mvcResult = mockMvc
                .perform(get(Constants.MEMBER_CONTROLLER + Constants.FIND_MEMBER_PAGINATED)
                        .param(TestConstants.PAGE, TestConstants.ZERO)
                        .param(TestConstants.SIZE, TestConstants.FIFTY)
                        .param(TestConstants.SORT_BY, TestConstants.ID)
                        .param(TestConstants.DIRECTION, TestConstants.ASCENDENT))
                .andExpect(status().isOk())
                .andReturn();
        // then
        assertNotNull(mvcResult);
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        JSONArray jsonArray = jsonObject.getJSONArray(TestConstants.FIRST);
        assertEquals(expected.getFirstName(), jsonArray.getJSONObject(0).getString(TestConstants.FIRSTNAME));
    }

    @Test
    public void findMembersPageByFlag_givenOneMember_expectMemberToBeReturnedSuccessfully() throws Exception {
        // given
        // when
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(Constants.MEMBER_CONTROLLER + Constants.FIND_MEMBER_BY_FLAG_PAGINATED, TestConstants.ONE_INTEGER)
                        .param(TestConstants.PAGE, TestConstants.ZERO)
                        .param(TestConstants.SIZE, TestConstants.FIFTY)
                        .param(TestConstants.SORT_BY, TestConstants.ID)
                        .param(TestConstants.DIRECTION, TestConstants.ASCENDENT))
                .andExpect(status().isOk())
                .andReturn();
        // then
        assertNotNull(mvcResult);
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        JSONArray jsonArray = jsonObject.getJSONArray(TestConstants.FIRST);
        assertEquals(expected.getFirstName(), jsonArray.getJSONObject(0).getString(TestConstants.FIRSTNAME));
    }

    @Test
    public void update_givenOneMember_expectUpdatedMember() throws Exception {
        // given
        expected.setFirstName("Alex");
        String s = OBJECT_MAPPER.writeValueAsString(expected);
        // when
        mockMvc
                .perform(put(Constants.MEMBER_CONTROLLER + Constants.UPDATE_MEMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(TestConstants.CHARACTER_ENCODING_UTF_8)
                        .content(s))
                .andExpect(status().isOk())
                .andReturn();
        // then
        MvcResult mvcResult = mockMvc
                .perform(get(Constants.MEMBER_CONTROLLER))
                .andExpect(status().isOk())
                .andReturn();
        assertNotNull(mvcResult);
        List<MemberDTO> result = getResponse(mvcResult);
        assertEquals(1, result.size());
        MemberDTO actual = result.get(0);
        assertResponseDto(expected, actual);
    }

    @Test
    public void create_givenOneMember_expectCreatedMember() throws Exception {
        // given
        List<MemberDTO> result;
        MvcResult mvcResultBeforeCreatedMember = mockMvc
                .perform(get(Constants.MEMBER_CONTROLLER))
                .andExpect(status().isOk())
                .andReturn();
        result = getResponse(mvcResultBeforeCreatedMember);
        assertEquals(1, result.size());
        // when

        expected.setId(90);
        expected.setFirstName("John");
        expected.setLastName("Doe");

        String s = OBJECT_MAPPER.writeValueAsString(expected);
        MvcResult mvcResult = mockMvc
                .perform(post(Constants.MEMBER_CONTROLLER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(TestConstants.CHARACTER_ENCODING_UTF_8)
                        .content(s))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResultAfterCreatedMember = mockMvc
                .perform(get(Constants.MEMBER_CONTROLLER))
                .andExpect(status().isOk())
                .andReturn();
        result = getResponse(mvcResultAfterCreatedMember);
        assertNotNull(mvcResult);
        assertEquals(2, result.size());
        assertEquals("John", result.get(1).getFirstName());
        // then
    }

    @Test
    public void delete_givenMember_expectEmptyTable() throws Exception {
        List<MemberDTO> result;
        MvcResult mvcResultBeforeCreatedMember = mockMvc
                .perform(get(Constants.MEMBER_CONTROLLER))
                .andExpect(status().isOk())
                .andReturn();

        result = getResponse(mvcResultBeforeCreatedMember);
        assertEquals(1, result.size());

        String s = OBJECT_MAPPER.writeValueAsString(expected);
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.delete(Constants.MEMBER_CONTROLLER + Constants.DELETE_MEMBER, TestConstants.ONE_INTEGER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(TestConstants.CHARACTER_ENCODING_UTF_8)
                        .content(s))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResultAfterCreatedMember = mockMvc
                .perform(get(Constants.MEMBER_CONTROLLER))
                .andExpect(status().isOk())
                .andReturn();
        result = getResponse(mvcResultAfterCreatedMember);
        assertNotNull(mvcResult);
        assertEquals(0, result.size());
    }

    @Test
    public void deactivate_givenOneMember_expectMemberDeactivated() throws Exception {
        // given
        when(calendarRepository.findFirstByEopGreaterThanEqual(any())).thenReturn(Optional.of(calendarEntity));
        // when
        mockMvc
                .perform(put(Constants.MEMBER_CONTROLLER + Constants.DEACTIVATE_MEMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(TestConstants.CHARACTER_ENCODING_UTF_8)
                        .content(TestConstants.ONE))
                .andExpect(status().isOk())
                .andReturn();
        // then
        MvcResult mvcResult = mockMvc
                .perform(get(Constants.MEMBER_CONTROLLER))
                .andExpect(status().isOk())
                .andReturn();
        assertNotNull(mvcResult);
        List<MemberDTO> result = getResponse(mvcResult);
        assertEquals(1, result.size());
        MemberDTO actual = result.get(0);
        assertThat(actual, is(not(expected)));
    }

    @Test
    public void getMemberByStaffNumber_givenOneMember_expectMemberToBeReturnedSuccessfully() throws Exception {
        // given
        // when
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(Constants.MEMBER_CONTROLLER + Constants.FIND_MEMBER_BY_STAFF_NUMBER, TestConstants.STAFF_NUMBER))
                .andExpect(status().isOk())
                .andReturn();
        // then
        assertNotNull(mvcResult);

        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);

        assertEquals(expected.getStaffNumber(), jsonObject.getString(TestConstants.STAFF_NUMBER));
    }

    @Test
    public void getActiveMembers_givenOneMember_expectMemberToBeReturnedSuccessfully() throws Exception {
        // given
        // when
        MvcResult mvcResult = mockMvc
                .perform(get(Constants.MEMBER_CONTROLLER + Constants.GET_ACTIVE_MEMBERS))
                .andExpect(status().isOk())
                .andReturn();
        // then
        assertNotNull(mvcResult);
        List<MemberDTO> result = getResponse(mvcResult);
        assertEquals(1, result.size());
        MemberDTO actual = result.get(0);
        assertResponseDto(expected, actual);
    }

    @Test
    public void getInactiveMembers_creatingOneInactiveMember_expectMemberToBeReturnedSuccessfully() throws Exception {
        // given
        expected.setFlag((byte) 0);
        expected.setFirstName(TestConstants.INACTIVE);
        String s = OBJECT_MAPPER.writeValueAsString(expected);
        mockMvc
                .perform(post(Constants.MEMBER_CONTROLLER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(TestConstants.CHARACTER_ENCODING_UTF_8)
                        .content(s))
                .andExpect(status().isOk())
                .andReturn();
        // when
        MvcResult mvcResult = mockMvc
                .perform(get(Constants.MEMBER_CONTROLLER + Constants.GET_INACTIVE_MEMBERS))
                .andExpect(status().isOk())
                .andReturn();
        // then
        assertNotNull(mvcResult);
        List<MemberDTO> result = getResponse(mvcResult);
        assertEquals(1, result.size());
        MemberDTO actual = result.get(0);
        assertResponseDto(expected, actual);
    }

    private void assertResponseDto(MemberDTO expected, MemberDTO actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getComment(), actual.getComment());
        assertEquals(expected.getFlag(), actual.getFlag());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getStaffNumber(), actual.getStaffNumber());
        assertEquals(expected.getTechnologyDescription(), actual.getTechnologyDescription());
        assertEquals(expected.getTechnologyId(), actual.getTechnologyId());
    }

    private List<MemberDTO> getResponse(MvcResult mvcResult) throws IOException {
        List<MemberDTO> tResponse = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<MemberDTO>>() {
        });
        assertNotNull(tResponse);
        return tResponse;
    }
}
