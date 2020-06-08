package dania.app.web.unitTests;

import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.entities.ParametersEntity;
import dania.app.web.mapper.ParametersMapper;
import dania.app.web.repository.ParameterRepository;
import dania.app.web.service.ParameterService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ParameterServiceTest {

    @Mock
    private ParameterRepository parameterRepository;

    @Mock
    private ParametersMapper parametersMapper;

    private ParametersEntity parameterEntityExpected;
    private ParameterService parameterService;
    private ParametersDTO parametersDTOExpected;

    @Before
    public void setupParameterService() {
        //given
        parameterEntityExpected = new ParametersEntity();
        parameterEntityExpected.setDescription("descriptionForParameterEntityExpected");

        parametersDTOExpected = new ParametersDTO();
        parametersDTOExpected.setId(30);
        parametersDTOExpected.setDescription("descriptionForParametersDTO");

        when(parametersMapper.parameterEntityToDTO(any())).thenReturn(parametersDTOExpected);
        when(parametersMapper.parameterDTOToEntity(any())).thenReturn(parameterEntityExpected);
        when(parametersMapper.listParamatersDTOToEntity(any())).thenReturn(Collections.singletonList(parameterEntityExpected));
        when(parametersMapper.listParamatersEntityToDTO(any())).thenReturn(Collections.singletonList(parametersDTOExpected));

        parameterService = new ParameterService(parameterRepository, parametersMapper);
    }

    @Test
    public void createParameterServiceTest() {
        //given
        when(parameterRepository.saveAndFlush(any())).thenReturn(parameterEntityExpected);

        //when
        ParametersDTO parametersDTO = parameterService.create(parametersDTOExpected);

        //then
        assertDescriptionFromParametersDTOFromParameterService(parametersDTO);
        verify(parameterRepository).saveAndFlush(parameterEntityExpected);
    }

    @Test
    public void deleteParameterServiceTest() {
        //given
        setUpForFindByIdParameterService();
        when(parameterService.findById(anyInt())).thenReturn(parametersDTOExpected);

        //when
        ParametersDTO parametersDTO = parameterService.delete(15);

        //then
        assertDescriptionFromParametersDTOFromParameterService(parametersDTO);
        verify(parameterRepository, times(2)).findById(anyInt());
    }

    @Test
    public void findAllParameterServiceTest() {
        //given
        when(parameterRepository.findAll()).thenReturn(Collections.singletonList(parameterEntityExpected));

        //when
        List<ParametersDTO> all = parameterService.findAll();

        //then
        assertEquals(parametersDTOExpected.getDescription(), all.get(0).getDescription());
        verify(parameterRepository).findAll();
    }

    @Test
    public void findByIdParameterServiceTest() {
        //given
        setUpForFindByIdParameterService();

        //when
        ParametersDTO byId = parameterService.findById(13);

        //then
        assertDescriptionFromParametersDTOFromParameterService(byId);
        verify(parameterRepository).findById(anyInt());
    }

    @Test
    public void updateParameterServiceTest() {
        //given
        when(parameterRepository.saveAndFlush(any())).thenReturn(parameterEntityExpected);

        //when
        ParametersDTO parametersDTO = parameterService.create(parametersDTOExpected);

        //then
        assertEquals(parametersDTOExpected.getId(), parametersDTO.getId());
        verify(parameterRepository).saveAndFlush(parameterEntityExpected);
        verify(parametersMapper).parameterEntityToDTO(parameterEntityExpected);
    }


    @Test
    public void findStatusParametersFromParameterServiceTest() {
        //given
        setUpForFindParametersFromParameterService();

        //when
        List<ParametersDTO> statusParameters = parameterService.findStatusParameters();

        //then
        assertEquals(parametersDTOExpected.getDescription(), statusParameters.get(0).getDescription());
        verify(parameterRepository).findParametersEntityByType(anyString());
    }

    @Test
    public void findPercentParametersFromParameterServiceTest(){
        //given
        setUpForFindParametersFromParameterService();

        //when
        List<ParametersDTO> percentParameters = parameterService.findPercentParameters();

        //then
        assertEquals(parametersDTOExpected.getDescription(), percentParameters.get(0).getDescription());
        verify(parameterRepository).findParametersEntityByType(anyString());
    }

    @Test
    public void findTechnologyParametersFromParameterServiceTest() {
        //given
        setUpForFindParametersFromParameterService();

        //when
        List<ParametersDTO> technologyParameters = parameterService.findTechnologyParameters();

        //then
        assertEquals(parametersDTOExpected.getDescription(), technologyParameters.get(0).getDescription());
        verify(parameterRepository).findParametersEntityByType(anyString());
    }

    @Test
    public void findPositionParametersFromParameterServiceTest() {
        //given
        setUpForFindParametersFromParameterService();

        //when
        List<ParametersDTO> positionParameters = parameterService.findPositionParameters();

        //then
        assertEquals(parametersDTOExpected.getDescription(), positionParameters.get(0).getDescription());
        verify(parameterRepository).findParametersEntityByType(anyString());
    }

    @Test
    public void findByIdPositionParameterServiceTest() {
        //given
        setUpForFindByIdParametersFromParameterService();

        //when
        ParametersDTO idPositionParameters = parameterService.findByIdPosition(5);

        //then
        assertDescriptionFromParametersDTOFromParameterService(idPositionParameters);
        verify(parameterRepository).findParametersEntityByIdAndType(anyInt(), anyString());
    }

    @Test
    public void findByIdPercentParameterServiceTest() {
        //given
        setUpForFindByIdParametersFromParameterService();

        //when
        ParametersDTO idPercentParameters = parameterService.findByIdPercent(5);

        //then
        assertDescriptionFromParametersDTOFromParameterService(idPercentParameters);
        verify(parameterRepository).findParametersEntityByIdAndType(anyInt(), anyString());
    }

    @Test
    public void findByIdStatusParameterServiceTest() {
        //given
        setUpForFindByIdParametersFromParameterService();

        //when
        ParametersDTO idStatusParameters = parameterService.findByIdStatus(5);

        //then
        assertDescriptionFromParametersDTOFromParameterService(idStatusParameters);
        verify(parameterRepository).findParametersEntityByIdAndType(anyInt(), anyString());
    }

    @Test
    public void findByIdTechnologyParameterServiceTest() {
        //given
        setUpForFindByIdParametersFromParameterService();

        //when
        ParametersDTO idTechnologyParameters = parameterService.findByIdTechnology(5);

        //then
        assertDescriptionFromParametersDTOFromParameterService(idTechnologyParameters);
        verify(parameterRepository).findParametersEntityByIdAndType(anyInt(), anyString());
    }

    private void assertDescriptionFromParametersDTOFromParameterService(ParametersDTO parametersDTO) {
        assertEquals(parametersDTOExpected.getDescription(), parametersDTO.getDescription());
    }

    private void setUpForFindByIdParameterService() {
        when(parameterRepository.findById(anyInt())).thenReturn(parameterEntityExpected);
    }

    private void setUpForFindParametersFromParameterService() {
        when(parameterRepository.findParametersEntityByType(anyString())).thenReturn(Collections.singletonList(parameterEntityExpected));
    }

    private void setUpForFindByIdParametersFromParameterService() {
        when(parameterRepository.findParametersEntityByIdAndType(anyInt(), anyString())).thenReturn(parameterEntityExpected);
    }
}
