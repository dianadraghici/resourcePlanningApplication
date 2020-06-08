package dania.app.web.controllers;


import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.controllers.dto.ProjectPositionDTO;
import dania.app.web.service.ParameterService;
import dania.app.web.service.ProjectPositionService;
import dania.app.web.utils.Constants;
import dania.app.web.utils.Cleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@RestController
@RequestMapping({Constants.SCHEDULE_PROJECT_CONTROLLER})
public class ScheduleProjectController {

    private final ParameterService parameterService;
    private final ProjectPositionService projectPositionService;

    @Autowired
    public ScheduleProjectController(ParameterService parameterService, ProjectPositionService projectPositionService) {
        this.parameterService = parameterService;
        this.projectPositionService = projectPositionService;
    }

    @GetMapping(path = Constants.GET_POSITION_PARAMETERS)
    public List<ParametersDTO> getPositionParameters() {
        return parameterService.findPositionParameters();
    }

    @GetMapping(path = Constants.FIND_BY_ID_PROJECT_POSITION)
    public Map<String, List<ProjectPositionDTO>> findProjectPositionByProjectId(@PathVariable Integer id) {
        return projectPositionService.findProjectPositionByIdProject(id).stream()
                .collect(groupingBy(ProjectPositionDTO::getPositionDescription));
    }

    @GetMapping
    @RequestMapping({Constants.GET_MAP_POSITIONS_BY_PROJECT})
    public Map<String, List<ProjectPositionDTO>> getMapPositionsByProject() {
        return Cleaner.getSortedMap(projectPositionService.findAll().stream()
                .collect(groupingBy(ProjectPositionDTO::getProjectName)));
    }

}
