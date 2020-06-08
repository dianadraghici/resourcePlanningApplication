package dania.app.web.service;

import dania.app.web.controllers.dto.CalendarDTO;
import dania.app.web.controllers.dto.MemberPositionDTO;
import dania.app.web.controllers.dto.ProjectDTO;
import dania.app.web.controllers.dto.ProjectPositionDTO;
import dania.app.web.mapper.ProjectMapper;
import dania.app.web.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class ProjectService implements ServiceManager<ProjectDTO, Integer> {

    private ProjectRepository projectRepository;
    private ProjectMapper projectMapper;
    private ProjectPositionService projectPositionService;
    private MemberPositionService memberPositionService;
    private ParameterService parameterService;
    private CalendarService calendarService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          ProjectPositionService projectPositionService,
                          MemberPositionService memberPositionService,
                          ProjectMapper projectMapper,
                          ParameterService parameterService,
                          CalendarService calendarService) {
        this.projectRepository = projectRepository;
        this.projectPositionService = projectPositionService;
        this.memberPositionService = memberPositionService;
        this.projectMapper = projectMapper;
        this.parameterService = parameterService;
        this.calendarService = calendarService;
    }

    @Override
    public ProjectDTO create(ProjectDTO projectDTO) {
        return projectMapper.projectEntityToDTO(
                projectRepository.saveAndFlush(
                        projectMapper.projectDTOToEntity(projectDTO)));
    }

    @Override
    public ProjectDTO delete(Integer id) {
        ProjectDTO projectDTO = findById(id);
        if (projectDTO != null) {
            projectRepository.delete(
                    projectMapper.projectDTOToEntity(projectDTO));
        }
        return projectDTO;
    }

    @Override
    public List<ProjectDTO> findAll() {
        return projectRepository.findAll().stream()
                .map(projectMapper::projectEntityToDTO)
                .map(this::setPercentAndStatusDescriptionConditionally)
                .collect(toList());
    }

    /**
     * @param pageable - the pageable obj which will be used to perform the pagination.
     * @return a Pair containing first the retrieved page requested and secondly the total size of elements.
     * @see org.springframework.data.domain.Pageable
     */
    public Pair<List<ProjectDTO>, Long> findAllWith(Pageable pageable) {
        List<ProjectDTO> projectDTOList = projectRepository.findAll(pageable).getContent().stream()
                .map(projectMapper::projectEntityToDTO)
                .map(this::setPercentAndStatusDescriptionConditionally)
                .collect(toList());
        return Pair.of(projectDTOList, projectRepository.count());
    }

    @Override
    public ProjectDTO findById(Integer id) {
        return projectMapper.projectEntityToDTO(projectRepository.findOneById(id));
    }

    public ProjectDTO findByProjectCode(String code) {
        return projectMapper.projectEntityToDTO(projectRepository.findByProjectCode(code));
    }

    @Override
    public ProjectDTO update(ProjectDTO projectDTO) {
        return projectMapper.projectEntityToDTO(
                projectRepository.saveAndFlush(
                        projectMapper.projectDTOToEntity(projectDTO)));
    }

    public ProjectDTO updateProjectById(ProjectDTO project, Integer id) {
        ProjectDTO found = findById(id);

        CalendarDTO projectStartDate = project.getStartDateCalendarDTO();
        CalendarDTO projectEndDate = project.getEndDateCalendarDTO();

        found.setProjectCode(project.getProjectCode());
        found.setProjectName(project.getProjectName());
        found.setStatusId(project.getStatusId());
        found.setPercentId(project.getPercentId());
        found.setStartDateCalendarDTO(projectStartDate);
        found.setEndDateCalendarDTO(projectEndDate);

        //Update all project positions and their respective member
        //positions to fit the project's new start and end dates
        List<ProjectPositionDTO> projectPositions = projectPositionService.findProjectPositionByIdProject(found.getId());
        for (ProjectPositionDTO pos : projectPositions) {
            // if Project StartDate is after ProjectPosition StartDate -> ProjectPosition StartDate = Project StartDate
            if (project.getStartDateCalendarDTO().getBop().compareTo(pos.getStartDateCalendarDTO().getBop()) >= 0) {
                pos.setStartDateCalendarDTO(projectStartDate);
            }
            // if Project EndDate is before ProjectPosition EndDate -> ProjectPosition EndDate = Project EndDate
            if (project.getEndDateCalendarDTO().getEop().compareTo(pos.getEndDateCalendarDTO().getEop()) <= 0) {
                pos.setEndDateCalendarDTO(projectEndDate);
            }
            //calendarService.update(pos.getStartDateCalendarDTO());
            //calendarService.update(pos.getEndDateCalendarDTO());
            projectPositionService.update(pos);

            List<MemberPositionDTO> memberPositions = memberPositionService.findMemberPositionByIdProjectPosition(pos.getId());
            for (MemberPositionDTO memberPos : memberPositions) {
                // if ProjectPosition StartDate is after MemberPosition StartDate ->  MemberPosition StartDate = ProjectPosition StartDate
                if (pos.getStartDateCalendarDTO().getBop().compareTo(memberPos.getStartDateCalendarDTO().getBop()) >= 0) {
                    memberPos.setStartDateCalendarDTO(pos.getStartDateCalendarDTO());
                }
                // if ProjectPosition EndDate is before MemberPosition EndDate ->  MemberPosition EndDate = ProjectPosition EndDate
                if (pos.getEndDateCalendarDTO().getEop().compareTo(memberPos.getEndDateCalendarDTO().getEop()) <= 0) {
                    memberPos.setEndDateCalendarDTO(pos.getEndDateCalendarDTO());
                }
                //calendarService.update(memberPos.getStartDateCalendarDTO());
                //calendarService.update(memberPos.getEndDateCalendarDTO());
                memberPositionService.update(memberPos);
            }
        }
        //calendarService.update(found.getStartDateCalendarDTO());
        //calendarService.update(found.getEndDateCalendarDTO());
        return projectMapper.projectEntityToDTO(
                projectRepository.saveAndFlush(
                        projectMapper.projectDTOToEntity(found)));
    }

    /**
     * @param projectDTO - the projectDTO on which the description will be set if condition true
     * @return the given projectDTO
     */
    private ProjectDTO setPercentAndStatusDescriptionConditionally(ProjectDTO projectDTO) {
        Optional.ofNullable(projectDTO)
                .filter(dto -> dto.getPercentId() >= 0)
                .ifPresent(dto -> dto.setPercentDescription(parameterService.findByIdPercent(dto.getPercentId()).getDescription()));
        Optional.ofNullable(projectDTO)
                .filter(dto -> dto.getStatusId() >= 0)
                .ifPresent(dto -> dto.setStatusDescription(parameterService.findByIdStatus(dto.getStatusId()).getDescription()));
        return projectDTO;
    }
}
