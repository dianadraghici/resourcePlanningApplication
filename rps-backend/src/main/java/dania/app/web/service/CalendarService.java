package dania.app.web.service;

import dania.app.web.controllers.dto.CalendarDTO;
import dania.app.web.mapper.CalendarMapper;
import dania.app.web.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService implements ServiceManager<CalendarDTO, String> {

    private final CalendarRepository calendarRepository;
    private final CalendarMapper calendarMapper;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository,
                           CalendarMapper calendarMapper) {
        this.calendarRepository = calendarRepository;
        this.calendarMapper = calendarMapper;
    }

    @Override
    public CalendarDTO create(CalendarDTO calendarDTO) {
        return calendarMapper.calendarEntityToDTO(calendarRepository.saveAndFlush(
                calendarMapper.calendarDTOToEntity(calendarDTO)));
    }

    @Override
    public CalendarDTO delete(String id) {
        CalendarDTO calendarDTO = findById(id);
        if (calendarDTO != null) {
            calendarRepository.delete(calendarMapper.calendarDTOToEntity(calendarDTO));
        }
        return calendarDTO;
    }

    @Override
    public List<CalendarDTO> findAll() {
        return calendarMapper.listCalendarEntityToDTO(calendarRepository.findAll());
    }

    @Override
    public CalendarDTO findById(String id) {
        return calendarMapper.calendarEntityToDTO(calendarRepository.findCalendarEntityById(id));
    }

    @Override
    public CalendarDTO update(CalendarDTO calendarDTO) {
        return calendarMapper.calendarEntityToDTO(calendarRepository.saveAndFlush(
                calendarMapper.calendarDTOToEntity(calendarDTO)));
    }
}
