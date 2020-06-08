package dania.app.web.unitTests;

import dania.app.web.controllers.dto.*;
import dania.app.web.entities.*;
import dania.app.web.mapper.MemberPositionMapper;
import dania.app.web.mapper.ParametersMapper;
import dania.app.web.repository.MemberPositionRepository;
import dania.app.web.repository.ParameterRepository;
import dania.app.web.service.MemberPositionService;
import dania.app.web.service.ParameterService;
import dania.app.web.controllers.dto.*;
import dania.app.web.entities.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MemberPositionServiceTest {

    private MemberPositionDTO memberPositionDTOExpected;
    private MemberPositionEntity memberPositionEntityExpected;
    private MemberPositionService memberPositionService;
    private ParametersDTO parametersDTOExpected;
    private ParametersEntity parametersEntityExpected;

    @Mock
    private ParameterRepository parameterRepository;
    @Mock
    private MemberPositionRepository memberPositionRepository;
    @Mock
    private MemberPositionMapper memberPositionMapper;
    @Mock
    private ParametersMapper parametersMapper;
    @MockBean
    private ParameterService parameterService;

    @Before
    public void setupMemberPositionServiceTest(){
        MemberDTO memberDTO;
        ProjectPositionDTO projectPositionDTO;
        ProjectDTO projectDTO;
        ProjectPositionEntity projectPositionEntity;

        //given
        memberDTO = new MemberDTO();
        memberDTO.setFirstName("Name to be tested");
        memberDTO.setTechnologyId(1);
        memberDTO.setTechnologyDescription("description found");
        memberDTO.setId(50);

        projectPositionDTO = new ProjectPositionDTO();
        projectDTO = new ProjectDTO();
        projectDTO.setStatusId(12);
        projectDTO.setPercentId(33);
        projectPositionDTO.setProjectDTO(projectDTO);
        projectPositionDTO.setPositionId(1);
        projectPositionDTO.setId(331);
        projectPositionDTO.setPositionDescription("PositionDescription project DTO");
        projectPositionDTO.setPercentId(10);
        projectPositionDTO.setPercentDescription("PercentDescription project DTO");


        memberPositionDTOExpected = new MemberPositionDTO();
        memberPositionDTOExpected.setId(50);
        memberPositionDTOExpected.setMemberDTO(memberDTO);
        memberPositionDTOExpected.setProjectPositionDTO(projectPositionDTO);
        memberPositionDTOExpected.setPercentId(4);

        memberPositionEntityExpected = new MemberPositionEntity();
        projectPositionEntity = new ProjectPositionEntity();
        memberPositionEntityExpected.setProjectPositionEntity(projectPositionEntity);
        memberPositionEntityExpected.setId(10);
        memberPositionEntityExpected.setPercentId(1);
        memberPositionEntityExpected.getProjectPositionEntity().setId(331);


        when(memberPositionMapper.memberPositionDTOToEntity(any())).thenReturn(memberPositionEntityExpected);
        when(memberPositionMapper.memberPositionEntityToDTO(any())).thenReturn(memberPositionDTOExpected);
        when(memberPositionMapper.listMemberPositionEntityToDTO(any())).thenReturn(Collections.singletonList(memberPositionDTOExpected));
        when(memberPositionMapper.listMemberPositionDTOToEntity(any())).thenReturn(Collections.singletonList(memberPositionEntityExpected));

        parametersDTOExpected = new ParametersDTO();
        parametersEntityExpected = new ParametersEntity();
        memberPositionService = new MemberPositionService(memberPositionRepository, memberPositionMapper, parameterService);
    }

    @Test
    public void createMemberPositionServiceTest(){
        //given
        when(memberPositionRepository.saveAndFlush(any())).thenReturn(memberPositionEntityExpected);

        //when
        MemberPositionDTO memberPositionDTO = memberPositionService.create(memberPositionDTOExpected);

        //then
        assertEquals(memberPositionDTOExpected.getId(), memberPositionDTO.getId());
        assertEquals(memberPositionDTOExpected.getMemberDTO().getFirstName(), memberPositionDTO.getMemberDTO().getFirstName());
        verify(memberPositionRepository).saveAndFlush(memberPositionEntityExpected);
        verify(memberPositionMapper).memberPositionDTOToEntity(memberPositionDTO);
        verify(memberPositionMapper).memberPositionEntityToDTO(memberPositionEntityExpected);
    }
    @Test
    public void findByIdMemberPositionServiceTest() {
        //given
        when(parametersMapper.parameterEntityToDTO(any())).thenReturn(parametersDTOExpected);
        setupForFindById();

        //when
        MemberPositionDTO byId = memberPositionService.findById(13);

        //then
        assertEquals(memberPositionDTOExpected.getId(), byId.getId());
        verify(memberPositionRepository).findMemberPositionEntityById(anyInt());
    }

    @Test
    public void deleteMemberPositionServiceTest(){
        // DELETE method from MemberPositionService should be refactor in the future
        //given
        setupForFindById();
        when(memberPositionService.findById(anyInt())).thenReturn(memberPositionDTOExpected);

        //when
        MemberPositionDTO memberPositionDTO = memberPositionService.delete(10);

        //then
        assertEquals(memberPositionDTOExpected.getId(), memberPositionDTO.getId());
        verify(memberPositionRepository).delete(memberPositionEntityExpected);
        verify(memberPositionRepository, times(2)).findMemberPositionEntityById(anyInt());
    }

    @Test
    public void updateMemberPositionServiceTest(){
        //given
        memberPositionDTOExpected.setPercentId(15);
        memberPositionEntityExpected.setPercentId(15);
        when(memberPositionRepository.saveAndFlush(any())).thenReturn(memberPositionEntityExpected);

        //when
        MemberPositionDTO memberPositionDTO = memberPositionService.update(memberPositionDTOExpected);

        //then
        assertEquals(memberPositionDTOExpected.getId(), memberPositionDTO.getId());
        assertEquals(memberPositionDTOExpected.getPercentId(),Integer.valueOf(15));
        verify(memberPositionRepository).saveAndFlush(memberPositionEntityExpected);
    }

     @Test
    public void findAllMemberPositionServiceTest(){
         //given
         setupForParameterDTO();
         when(memberPositionRepository.findAll()).thenReturn(Collections.singletonList(memberPositionEntityExpected));

         //when
         List<MemberPositionDTO> memberPositionDTOList = memberPositionService.findAll();

         //then
         assertEquals(parametersDTOExpected.getDescription(), memberPositionDTOList.get(0).getMemberDTO().getTechnologyDescription());
         assertEquals(parametersDTOExpected.getDescription(), memberPositionDTOList.get(0).getPercentDescription());
         assertEquals(parametersDTOExpected.getDescription(), memberPositionDTOList.get(0).getProjectPositionDTO().getProjectDTO().getStatusDescription());
         assertEquals(parametersDTOExpected.getDescription(), memberPositionDTOList.get(0).getProjectPositionDTO().getProjectDTO().getPercentDescription());
         assertEquals(memberPositionDTOList.size(),1);
         verify(memberPositionRepository).findAll();
         verifyMethodsForParameterService();
    }

    @Test
    public void findMemberPositionByIdMember(){
        //given
        setupForParameterDTO();
        when(memberPositionRepository.findMemberPositionEntityByMemberEntity_Id(anyInt())).thenReturn(Collections.singletonList(memberPositionEntityExpected));

        //when
        List<MemberPositionDTO> memberPositionDTOList = memberPositionService.findMemberPositionByIdMember(1);

        //then
        assertEquals(memberPositionDTOList.size(),1);
        assertEquals(memberPositionDTOList.get(0).getMemberDTO().getTechnologyDescription(), parametersDTOExpected.getDescription());
        assertEquals(Integer.valueOf(50), memberPositionDTOList.get(0).getMemberDTO().getId());
        verify(memberPositionRepository).findMemberPositionEntityByMemberEntity_Id(anyInt());
        verifyMethodsForParameterService();
    }

    @Test
    public void findMemberPositionByIdProjectPosition(){
        //given
        setupForParameterDTO();
        when(memberPositionRepository.findMemberPositionEntityByProjectPositionEntity_Id(anyInt())).thenReturn(Collections.singletonList(memberPositionEntityExpected));

        //when
        List<MemberPositionDTO> memberPositionDTOList = memberPositionService.findMemberPositionByIdMember(1);

        //then
        assertEquals(memberPositionDTOExpected.getId(), memberPositionDTOList.get(0).getId());
        assertEquals(memberPositionDTOList.size(),1);
        assertEquals(memberPositionDTOList.get(0).getMemberDTO().getTechnologyDescription(), parametersDTOExpected.getDescription());
        assertEquals(memberPositionDTOList.get(0).getProjectPositionDTO().getId(), memberPositionDTOExpected.getProjectPositionDTO().getId());
        verifyMethodsForParameterService();
    }

    private void verifyMethodsForParameterService(){
        verify(parameterService, times(3)).findByIdPercent(anyInt());
        verify(parameterService).findByIdTechnology(anyInt());
        verify(parameterService).findByIdStatus(anyInt());
        verify(parameterService).findByIdPosition(anyInt());
    }

    private void setupForFindById(){
        when(memberPositionRepository.findMemberPositionEntityById(anyInt())).thenReturn(memberPositionEntityExpected);
    }

    private void setupForParameterDTO(){
        parametersDTOExpected.setDescription("description to be found");
        when(parameterRepository.findParametersEntityByIdAndType(anyInt(), anyString())).thenReturn(parametersEntityExpected);
        when(parameterRepository.findParametersEntityByType(anyString())).thenReturn(Collections.singletonList(parametersEntityExpected));
        when(parameterService.findByIdTechnology(anyInt())).thenReturn(parametersDTOExpected);
        when(parameterService.findByIdPosition(anyInt())).thenReturn(parametersDTOExpected);
        when(parameterService.findByIdStatus(anyInt())).thenReturn(parametersDTOExpected);
        when(parameterService.findByIdPercent(anyInt())).thenReturn(parametersDTOExpected);
    }
}
