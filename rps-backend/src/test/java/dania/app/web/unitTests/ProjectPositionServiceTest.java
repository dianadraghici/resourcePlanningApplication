package dania.app.web.unitTests;

import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.controllers.dto.ProjectDTO;
import dania.app.web.controllers.dto.ProjectPositionDTO;
import dania.app.web.entities.ProjectPositionEntity;
import dania.app.web.mapper.ProjectPositionMapper;
import dania.app.web.repository.ProjectPositionRepository;
import dania.app.web.service.ParameterService;
import dania.app.web.service.ProjectPositionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ProjectPositionServiceTest {

    @Mock
    private ProjectPositionRepository projectPositionRepository;
    @Mock
    private ProjectPositionMapper projectPositionMapper;
    @MockBean
    private ParameterService parameterService;

    private ProjectPositionService projectPositionService;
    private ProjectPositionEntity projectPositionEntityExpected;
    private ProjectPositionDTO projectPositionDTOExpected;
    private ParametersDTO parametersDTOExpected;

    @Before
    public void setupProjectPositionService() {
        //given
        projectPositionDTOExpected = new ProjectPositionDTO();
        projectPositionDTOExpected.setId(12);
        projectPositionDTOExpected.setNumberPositions(2);
        projectPositionDTOExpected.setPercentId(1);
        projectPositionDTOExpected.setPositionId(2);

        projectPositionEntityExpected = new ProjectPositionEntity();
        projectPositionEntityExpected.setId(13);
        projectPositionEntityExpected.setNumberPositions(2);
        projectPositionEntityExpected.setPercentId(1);
        projectPositionEntityExpected.setPositionId(2);

        when(projectPositionMapper.projectPositionEntityToDTO(any())).thenReturn(projectPositionDTOExpected);
        when(projectPositionMapper.projectPositionDTOToEntity(any())).thenReturn(projectPositionEntityExpected);
        when(projectPositionMapper.listProjectPositionDTOToEntity(any())).thenReturn(Collections.singletonList(projectPositionEntityExpected));
        when(projectPositionMapper.listProjectPositionEntityToDTO(any())).thenReturn(Collections.singletonList(projectPositionDTOExpected));

        parametersDTOExpected = new ParametersDTO();
        projectPositionService = new ProjectPositionService(projectPositionRepository, projectPositionMapper, parameterService);
    }

    @Test
    public void createProjectPositionServiceTest() {
        //given
        when(projectPositionRepository.saveAndFlush(any())).thenReturn(projectPositionEntityExpected);
        when(parameterService.findByIdPosition(any())).thenReturn(parametersDTOExpected);
        when(parameterService.findByIdPercent(any())).thenReturn(parametersDTOExpected);

        //when
        ProjectPositionDTO projectPositionDTO = projectPositionService.create(projectPositionDTOExpected);

        //then
        assertEquals(projectPositionDTOExpected.getNumberPositions(), projectPositionDTO.getNumberPositions());
        verify(projectPositionRepository).saveAndFlush(projectPositionEntityExpected);
    }

    @Test
    public void deleteProjectPositionServiceTest() {
        //DELETE METHOD should be modified, the return type of that method should be boolean : to do

        //given
        when(parameterService.findByIdPosition(any())).thenReturn(parametersDTOExpected);
        when(parameterService.findByIdPercent(any())).thenReturn(parametersDTOExpected);
        setupForFindByIdProjectPositionService();

        //when
        ProjectPositionDTO projectPositionDTO = projectPositionService.delete(12);

        //then
        assertEquals(projectPositionDTOExpected.getNumberPositions(), projectPositionDTO.getNumberPositions());
        verify(projectPositionRepository, times(1)).findOneById(anyInt());
    }

    @Test
    public void findAllProjectPositionServiceTest() {
        //given
        when(projectPositionRepository.findAll()).thenReturn(Collections.singletonList(projectPositionEntityExpected));

        //when
        List<ProjectPositionDTO> all = projectPositionService.findAll();

        //then
        assertEquals(all.size(), 1);
        assertEquals(projectPositionDTOExpected.getNumberPositions(), all.get(0).getNumberPositions());
        verify(projectPositionRepository).findAll();
    }

    @Test
    public void findOneByIdProjectPositionServiceTest() {
        //given
        when(parameterService.findByIdPosition(any())).thenReturn(parametersDTOExpected);
        when(parameterService.findByIdPercent(any())).thenReturn(parametersDTOExpected);
        setupForFindByIdProjectPositionService();

        //when
        ProjectPositionDTO byId = projectPositionService.findById(13);

        //then
        assertEquals(projectPositionDTOExpected.getId(), byId.getId());
        verify(projectPositionRepository).findOneById(any());
    }

    @Test
    public void updateProjectPositionServiceTest() {
        //given
        when(parameterService.findByIdPosition(any())).thenReturn(parametersDTOExpected);
        when(parameterService.findByIdPercent(any())).thenReturn(parametersDTOExpected);
        when(projectPositionRepository.saveAndFlush(any())).thenReturn(projectPositionEntityExpected);

        //when
        projectPositionEntityExpected.setNumberPositions(4);
        projectPositionDTOExpected.setNumberPositions(4);
        ProjectPositionDTO projectPositionDTO = projectPositionService.update(projectPositionDTOExpected);

        //then
        assertEquals(projectPositionDTOExpected.getNumberPositions(), projectPositionDTO.getNumberPositions());
        assertEquals(projectPositionDTO.getNumberPositions(), Integer.valueOf(4));
        verify(projectPositionRepository).saveAndFlush(projectPositionEntityExpected);
        verify(projectPositionMapper).projectPositionEntityToDTO(projectPositionEntityExpected);
    }

    @Test
    public void findProjectPositionByIdProjectTest() {
        //given
        when(projectPositionRepository.findProjectPositionEntityByProjectEntity_Id(anyInt())).thenReturn(Collections.singletonList(projectPositionEntityExpected));
        when(parameterService.findByIdPercent(any())).thenReturn(parametersDTOExpected);
        when(parameterService.findByIdPosition(any())).thenReturn(parametersDTOExpected);

        //when
        List<ProjectPositionDTO> listProjectPositionDTO = projectPositionService.findProjectPositionByIdProject(anyInt());
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(33);
        projectPositionDTOExpected.setProjectDTO(projectDTO);

        //then
        assertEquals(projectPositionDTOExpected.getProjectDTO().getId(), listProjectPositionDTO.get(0).getProjectDTO().getId());
        assertEquals(projectPositionDTOExpected.getNumberPositions(), listProjectPositionDTO.get(0).getNumberPositions());
        verify(projectPositionRepository).findProjectPositionEntityByProjectEntity_Id(anyInt());
    }

    private void setupForFindByIdProjectPositionService() {
        when(projectPositionRepository.findOneById(anyInt())).thenReturn(projectPositionEntityExpected);
    }
}
