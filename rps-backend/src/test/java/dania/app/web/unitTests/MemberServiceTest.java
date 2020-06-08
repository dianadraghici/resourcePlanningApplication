package dania.app.web.unitTests;

import dania.app.web.controllers.dto.MemberDTO;
import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.entities.CalendarEntity;
import dania.app.web.entities.MemberEntity;
import dania.app.web.entities.MemberPositionEntity;
import dania.app.web.mapper.MemberMapper;
import dania.app.web.mapper.ParametersMapper;
import dania.app.web.repository.CalendarRepository;
import dania.app.web.repository.MemberPositionRepository;
import dania.app.web.repository.MemberRepository;
import dania.app.web.service.MemberService;
import dania.app.web.service.ParameterService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    private MemberEntity memberEntityExpected;
    @MockBean
    private ParameterService parameterService;
    private ParametersDTO parametersDTOExpected;
    @Mock
    private ParametersMapper parametersMapper;
    @Mock
    private Pageable pageable;
    @Mock
    private Page<MemberEntity> pageMemberEntity;
    @Mock
    private CalendarRepository calendarRepository;
    @Mock
    private MemberMapper memberMapper;
    @MockBean
    private MemberPositionRepository memberPositionRepository;
    @Mock
    private CalendarEntity calendarEntity;
    @Mock
    private MemberEntity memberEntityMock;

    private MemberPositionEntity memberPositionEntity;
    private MemberService memberService;
    private MemberDTO memberDTOExpected;

    @Before
    public void setup() {

        memberDTOExpected = new MemberDTO();
        memberDTOExpected.setFirstName("ion");
        memberDTOExpected.setId(60);
        memberDTOExpected.setTechnologyId(100);

        memberEntityExpected = new MemberEntity();
        memberEntityExpected.setFirstName("gigel");
        memberEntityExpected.setTechnologyId(3);

        memberPositionEntity = new MemberPositionEntity();

        when(memberMapper.memberEntityToDTO(any())).thenReturn(memberDTOExpected);
        when(memberMapper.memberDTOToEntity(any())).thenReturn(memberEntityExpected);
        when(memberMapper.listMemberDTOToEntity(any())).thenReturn(Collections.singletonList(memberEntityExpected));
        when(memberMapper.listMemberEntityToDTO(any())).thenReturn(Collections.singletonList(memberDTOExpected));

        parametersDTOExpected = new ParametersDTO();
        memberService = new MemberService(memberRepository, memberMapper, parameterService, memberPositionRepository, calendarRepository);
    }

    @Test
    public void createTest() {
        when(memberRepository.saveAndFlush(any())).thenReturn(memberEntityExpected);
        MemberDTO memberDTO = memberService.create(memberDTOExpected);

        assertFirstNameFromMemberDTO(memberDTO);
        verify(memberRepository).saveAndFlush(memberEntityExpected);
    }

    @Test
    public void deleteTest() {
        setUpForFindById();
        when(memberService.findById(anyInt())).thenReturn(memberDTOExpected);
        MemberDTO memberDTO = memberService.delete(15);

        assertFirstNameFromMemberDTO(memberDTO);
        verify(memberRepository, times(2)).findById(anyInt());
    }

    @Test
    public void findAllTest() {
        setUpForParameterDTO();
        when(parametersMapper.parameterEntityToDTO(any())).thenReturn(parametersDTOExpected);
        when(memberRepository.findAll()).thenReturn(Collections.singletonList(memberEntityExpected));
        List<MemberDTO> all = memberService.findAll();

        assertEquals(parametersDTOExpected.getDescription(), all.get(0).getTechnologyDescription());
        verify(memberRepository).findAll();
    }

    @Test
    public void findAllWithTest() {
        setUpForFindTestMethods();
        when(memberRepository.findAll(pageable)).thenReturn(pageMemberEntity);
        Pair<List<MemberDTO>, Long> allWith = memberService.findAllWith(pageable);

        assertEquals(memberDTOExpected.getTechnologyId(), allWith.getFirst().get(0).getTechnologyId());
        verify(memberRepository).findAll(pageable);
    }

    @Test
    public void findAllByTest() {
        byte byteNr = 2;
        setUpForFindTestMethods();
        when(memberRepository.findAllByFlagEquals(anyByte(), any())).thenReturn(pageMemberEntity);
        Pair<List<MemberDTO>, Long> allBy = memberService.findAllBy(byteNr, pageable);

        assertEquals(memberDTOExpected.getTechnologyId(), allBy.getFirst().get(0).getTechnologyId());
        verify(memberRepository).findAllByFlagEquals(anyByte(), any());
    }

    @Test
    public void findByIdTest() {
        setUpForFindById();
        MemberDTO byId = memberService.findById(13);

        assertFirstNameFromMemberDTO(byId);
        verify(memberRepository).findById(anyInt());
    }

    @Test
    public void findByStaffNumberTest() {
        when(memberRepository.findByStaffNumber(anyString())).thenReturn(memberEntityExpected);
        MemberDTO byId = memberService.findByStaffNumber("");

        assertFirstNameFromMemberDTO(byId);
        verify(memberRepository).findByStaffNumber(anyString());
    }

    @Test
    public void deactivateMemberTest() {
        calendarEntity.setPeriod(34);
        setUpForFindById();
        Optional<CalendarEntity> firstByEopGreaterThanEqual = Optional.of(calendarEntity);
        List<MemberPositionEntity> list = new ArrayList<>();
        list.add(memberPositionEntity);
        when(calendarRepository.findFirstByEopGreaterThanEqual(any())).thenReturn(firstByEopGreaterThanEqual);
        when(memberPositionRepository.findMemberPositionEntityByMemberEntity_Id(anyInt())).thenReturn(list);
        MemberDTO memberDTO = memberService.deactivateMember(4);

        assertFirstNameFromMemberDTO(memberDTO);
        verify(calendarRepository).findFirstByEopGreaterThanEqual(any());
        verify(memberRepository, times(2)).findById(anyInt());
        verify(memberRepository).save(any());
        verify(memberPositionRepository).findMemberPositionEntityByMemberEntity_Id(anyInt());
        verify(memberPositionRepository).save(any());
    }

    private void setUpForFindTestMethods() {
        setUpForParameterDTO();
        List<MemberEntity> content = pageMemberEntity.getContent();
        content.add(memberEntityExpected);
        when(pageMemberEntity.getContent()).thenReturn(content);
        when(memberEntityMock.getTechnologyId()).thenReturn(10);
    }

    private void setUpForParameterDTO() {
        parametersDTOExpected.setDescription("description find");
        when(parameterService.findByIdTechnology(anyInt())).thenReturn(parametersDTOExpected);
    }

    private void assertFirstNameFromMemberDTO(MemberDTO memberDTO) {
        assertEquals(memberDTOExpected.getFirstName(), memberDTO.getFirstName());
    }

    private void setUpForFindById() {
        when(memberRepository.findById(anyInt())).thenReturn(memberEntityExpected);
    }
}
