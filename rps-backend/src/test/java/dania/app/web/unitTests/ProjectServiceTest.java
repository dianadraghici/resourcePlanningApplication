package dania.app.web.unitTests;

import dania.app.web.controllers.dto.*;
import dania.app.web.entities.ProjectEntity;
import dania.app.web.mapper.ProjectMapper;
import dania.app.web.repository.ProjectPositionRepository;
import dania.app.web.repository.ProjectRepository;
import dania.app.web.service.*;
import dania.app.web.controllers.dto.*;
import dania.app.web.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
public class ProjectServiceTest {

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ParameterService parameterService;

    @Mock
    private Pageable pageable;

    @Mock
    private Page<ProjectEntity> projectEntityPage;

    @Mock
    private ProjectPositionService projectPositionService;

    @Mock
    private ProjectPositionRepository projectPositionRepository;

    @Mock
    private MemberPositionService memberPositionService;

    private ProjectService projectService;
    private ProjectEntity projectEntityExpected;
    private ProjectDTO projectDTOExpected;
    private ProjectPositionDTO projectPositionDTOExpected;
    private MemberPositionDTO memberPositionDTO;
    private CalendarService calendarService;


    @Before
    public void setupForProjectServiceTest() {
        projectService = new ProjectService(projectRepository, projectPositionService, memberPositionService, projectMapper, parameterService, calendarService);

        LocalDate dateBop = LocalDate.of(2019, 3, 21);
        LocalDate dateEop = LocalDate.of(2019, 11, 30);

        CalendarDTO calendarDTO = new CalendarDTO();
        calendarDTO.setBop(dateBop);
        calendarDTO.setEop(dateEop);

        projectDTOExpected = new ProjectDTO();
        projectDTOExpected.setId(123);
        projectDTOExpected.setPercentId(20);
        projectDTOExpected.setStatusId(11);
        projectDTOExpected.setStartDateCalendarDTO(calendarDTO);
        projectDTOExpected.setEndDateCalendarDTO(calendarDTO);

        projectPositionDTOExpected = new ProjectPositionDTO();
        projectPositionDTOExpected.setId(123);
        projectPositionDTOExpected.setPercentId(20);
        projectPositionDTOExpected.setProjectDTO(projectDTOExpected);
        projectPositionDTOExpected.setEndDateCalendarDTO(calendarDTO);
        projectPositionDTOExpected.setStartDateCalendarDTO(calendarDTO);

        memberPositionDTO = new MemberPositionDTO();
        memberPositionDTO.setEndDateCalendarDTO(calendarDTO);
        memberPositionDTO.setStartDateCalendarDTO(calendarDTO);

        projectEntityExpected = new ProjectEntity();
        projectEntityExpected.setPercentId(20);

        when(projectMapper.projectEntityToDTO(any())).thenReturn(projectDTOExpected);
        when(projectMapper.projectDTOToEntity(any())).thenReturn(projectEntityExpected);

        when(projectMapper.listProjectDTOToEntity(any())).thenReturn(Collections.singletonList(projectEntityExpected));
        when(projectMapper.listProjectEntityToDTO(any())).thenReturn(Collections.singletonList(projectDTOExpected));
    }

    @Test
    public void createProjectServiceTest() {
        //given
        when(projectRepository.saveAndFlush(any())).thenReturn(projectEntityExpected);

        //when
        ProjectDTO projectDTO = projectService.create(projectDTOExpected);

        //then
        assertEquals(projectDTOExpected.getId(), projectDTO.getId());
        verify(projectRepository).saveAndFlush(projectEntityExpected);
    }

    @Test
    public void deleteProjectServiceTest() {
        //given
        when(projectRepository.findOneById(anyInt())).thenReturn(projectEntityExpected);
        when(projectService.findById(anyInt())).thenReturn(projectDTOExpected);

        //when
        ProjectDTO projectDTO = projectService.delete(anyInt());

        //then
        assertEquals(projectDTOExpected.getId(), projectDTO.getId());
        verify(projectRepository, times(2)).findOneById(anyInt());
    }

    @Test
    public void findAllProjectServiceTest() {
        //given
        setUpForParameterProjectServiceTest();
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(projectEntityExpected));

        //when
        List<ProjectDTO> all = projectService.findAll();

        //then
        assertEquals(Collections.singleton(projectEntityExpected).size(), all.size());
        assertEquals(projectEntityExpected.getPercentId(), all.get(0).getPercentId());
        verify(projectRepository).findAll();
    }

    @Test
    public void findAllWithProjectServiceTest() {
        //given
        setUpForParameterProjectServiceTest();
        List<ProjectEntity> pageContent = projectEntityPage.getContent();
        pageContent.add(projectEntityExpected);
        when(projectEntityPage.getContent()).thenReturn(pageContent);
        when(projectRepository.findAll(pageable)).thenReturn(projectEntityPage);

        //when
        Pair<List<ProjectDTO>, Long> allWith = projectService.findAllWith(pageable);

        //then
        assertEquals(Collections.singleton(projectDTOExpected).size(), allWith.getFirst().size());
        assertEquals(projectDTOExpected.getPercentId(), allWith.getFirst().get(0).getPercentId());
        verify(projectRepository).findAll(pageable);
    }

    @Test
    public void findByIdProjectServiceTest() {
        //given
        when(projectRepository.findOneById(anyInt())).thenReturn(projectEntityExpected);
        when(projectService.findById(anyInt())).thenReturn(projectDTOExpected);

        //when
        ProjectDTO projectDTO = projectService.findById(anyInt());

        //then
        assertEquals(projectDTOExpected.getId(), projectDTO.getId());
        verify(projectRepository, times(2)).findOneById(anyInt());
    }

    @Test
    public void findByProjectCodeProjectServiceTest() {
        //given
        when(projectRepository.findByProjectCode(anyString())).thenReturn(projectEntityExpected);

        //then
        ProjectDTO projectDTO = projectService.findByProjectCode(anyString());

        //then
        assertEquals(projectDTOExpected.getProjectCode(), projectDTO.getProjectCode());
        verify(projectRepository).findByProjectCode(anyString());
    }

    @Test
    public void updateProjectServiceTest() {
        //given
        when(projectRepository.saveAndFlush(any())).thenReturn(projectEntityExpected);

        //when
        ProjectDTO projectDTO = projectService.update(projectDTOExpected);

        //then
        assertEquals(projectDTOExpected.getProjectCode(), projectDTO.getProjectCode());
        verify(projectRepository).saveAndFlush(projectEntityExpected);
    }

    @Test
    public void updateProjectByIdProjectServiceTest() {
        //given
        setUpForParameterProjectServiceTest();
        when(projectRepository.findOneById(anyInt())).thenReturn(projectEntityExpected);
        when(projectPositionRepository.saveAndFlush(any())).thenReturn(projectEntityExpected);

        when(projectPositionService.findProjectPositionByIdProject(anyInt())).thenReturn(Collections.singletonList(projectPositionDTOExpected));
        List<ProjectPositionDTO> projectPositionDTOList = projectPositionService.findProjectPositionByIdProject(123);
        when(projectPositionService.update(any())).thenReturn(projectPositionDTOExpected);

        when(memberPositionService.findMemberPositionByIdProjectPosition(anyInt())).thenReturn(Collections.singletonList(memberPositionDTO));
        List<MemberPositionDTO> memberPositionDTOList = memberPositionService.findMemberPositionByIdProjectPosition(123);
        when(memberPositionService.update(any())).thenReturn(memberPositionDTO);

        //when
        MemberPositionDTO memberDTO = memberPositionService.update(memberPositionDTO);
        ProjectPositionDTO positionDTO = projectPositionService.update(projectPositionDTOExpected);
        ProjectDTO projectDTO = projectService.updateProjectById(projectDTOExpected, 123);

        //then
        assertEquals(projectPositionDTOList.size(), Collections.singletonList(projectPositionDTOExpected).size());
        assertEquals(memberPositionDTOList.size(), Collections.singleton(memberPositionDTO).size());
        assertEquals(projectPositionDTOExpected.getEndDateCalendarDTO(), positionDTO.getEndDateCalendarDTO());
        assertEquals(memberPositionDTO.getStartDateCalendarDTO(), memberDTO.getStartDateCalendarDTO());
        assertEquals(projectDTOExpected.getEndDateCalendarDTO(), projectDTO.getEndDateCalendarDTO());
        verify(projectRepository,times(1)).findOneById(anyInt());
        verify(projectRepository).saveAndFlush(any());
    }

    private void setUpForParameterProjectServiceTest(){
        //given
        ParametersDTO parameterDtoExpected = new ParametersDTO();
        parameterDtoExpected.setDescription("Parameter description");
        when(parameterService.findByIdPercent(any())).thenReturn(parameterDtoExpected);
        when(parameterService.findByIdStatus(any())).thenReturn(parameterDtoExpected);
    }

}