package dania.app.web.unitTests;

import dania.app.web.entities.CalendarEntity;
import dania.app.web.controllers.dto.CalendarDTO;
import dania.app.web.mapper.CalendarMapper;
import dania.app.web.repository.CalendarRepository;
import dania.app.web.service.CalendarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CalendarServiceTest {

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private CalendarMapper calendarMapper;

    private CalendarDTO calendarDTOExpected;
    private CalendarEntity calendarEntityExpected;
    private CalendarService calendarService;

    @Before
    public void setupCalendarServiceTest() {
        calendarService = new CalendarService(calendarRepository, calendarMapper);

        calendarDTOExpected = new CalendarDTO();
        calendarDTOExpected.setQuarter("trei");
        calendarDTOExpected.setId("3");

        calendarEntityExpected = new CalendarEntity();
        calendarEntityExpected.setQuarter("doi");
        calendarEntityExpected.setId("2");

        when(calendarMapper.calendarEntityToDTO(any())).thenReturn(calendarDTOExpected);
        when(calendarMapper.calendarDTOToEntity(any())).thenReturn(calendarEntityExpected);
    }

    @Test
    public void createCalendarServiceTest() {
        //given
        when(calendarRepository.saveAndFlush(any())).thenReturn(calendarEntityExpected);

        //when
        CalendarDTO calendarDTO = calendarService.create(calendarDTOExpected);

        //then
        assertEquals(calendarDTOExpected.getQuarter(), calendarDTO.getQuarter());
        assertEquals(calendarDTOExpected.getId(), calendarDTO.getId());
        verify(calendarMapper).calendarEntityToDTO(calendarEntityExpected);
        verify(calendarRepository).saveAndFlush(calendarEntityExpected);
        verify(calendarMapper).calendarDTOToEntity(any());
    }

    @Test
    public void deleteCalendarServiceTest(){
        //given
        when(calendarService.findById(any())).thenReturn(calendarDTOExpected);

        //when
        CalendarDTO calendarDTO = calendarService.delete("1");

        //then
        assertEquals(calendarDTOExpected.getQuarter(), calendarDTO.getQuarter());
        assertEquals(calendarDTOExpected.getId(), calendarDTO.getId());
        verify(calendarRepository).delete(calendarEntityExpected);
        verify(calendarMapper).calendarDTOToEntity(any());
    }

    @Test
    public void findAllCalendarServiceTest(){
        //given
        List<CalendarDTO> calendarDTOExpectedList = new ArrayList<>();
        calendarDTOExpectedList.add(calendarDTOExpected);

        List<CalendarEntity> calendarEntityExpectedList = new ArrayList<>();
        calendarEntityExpectedList.add(calendarEntityExpected);

        when(calendarMapper.listCalendarEntityToDTO(any())).thenReturn(calendarDTOExpectedList);
        when(calendarRepository.findAll()).thenReturn(calendarEntityExpectedList);

        //when
        List<CalendarDTO> calendarList = calendarService.findAll();
        CalendarDTO calendarFromResultList = calendarList.get(0);

        //then
        assertEquals(calendarList.size(), calendarDTOExpectedList.size());
        assertEquals(calendarFromResultList.getId(), calendarDTOExpected.getId());
        assertEquals(calendarFromResultList.getQuarter(), calendarDTOExpected.getQuarter());
        verify(calendarMapper).listCalendarEntityToDTO(any());
        verify(calendarRepository).findAll();
    }

    @Test
    public void findByIdCalendarServiceTest(){
        //given
        when(calendarRepository.findCalendarEntityById(any())).thenReturn(calendarEntityExpected);

        //when
        CalendarDTO calendarDTO = calendarService.findById(anyString());

        //then
        assertEquals(calendarDTOExpected.getId(), calendarDTO.getId());
        verify(calendarMapper).calendarEntityToDTO(calendarEntityExpected);
        verify(calendarRepository).findCalendarEntityById(anyString());

    }

    @Test
    public void updateCalendarServiceTest(){
        //given
        when(calendarRepository.saveAndFlush(any())).thenReturn(calendarEntityExpected);

        //when
        CalendarDTO calendarDTO = calendarService.update(calendarDTOExpected);

        //then
        assertEquals(calendarDTOExpected.getQuarter(), calendarDTO.getQuarter());
        verify(calendarMapper).calendarEntityToDTO(calendarEntityExpected);
        verify(calendarMapper).calendarDTOToEntity(calendarDTO);
        verify(calendarRepository).saveAndFlush(calendarEntityExpected);
    }
}
