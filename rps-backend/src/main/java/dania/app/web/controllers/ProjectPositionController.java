package dania.app.web.controllers;

import dania.app.web.controllers.dto.ProjectPositionDTO;
import dania.app.web.errorHandler.BadRequestException;
import dania.app.web.service.ProjectPositionService;
import dania.app.web.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping({Constants.PROJECT_POSITION_CONTROLLER})
@Validated
public class ProjectPositionController {

    private final ProjectPositionService projectPositionService;

    @Autowired
    public ProjectPositionController(ProjectPositionService projectPositionService) {
        this.projectPositionService = projectPositionService;
    }

    @GetMapping(path = Constants.FIND_BY_ID_PROJECT_POSITION)
    public Map<String, List<ProjectPositionDTO>> findProjectPositionByProjectId(@PathVariable Integer id) {
        return projectPositionService.findProjectPositionByIdProject(id)
                .stream()
                .collect(Collectors.groupingBy(ProjectPositionDTO::getPositionDescription));
    }

    @PostMapping
    public ProjectPositionDTO create(@RequestBody @Valid ProjectPositionDTO projectPositionDTO) {
        return projectPositionService.create(projectPositionDTO);
    }

    @PutMapping(path = Constants.UPDATE_PROJECT_POSITION)
    public void update(@RequestBody @Valid ProjectPositionDTO projectPositionDTO) {
        if (projectPositionDTO.getId() == null || projectPositionDTO.getId() <= 0) {
            throw new BadRequestException("Project Position - Project Position Id must be a positive number!");
        }
        projectPositionService.update(projectPositionDTO);
    }

    /**
     * Delete Mapping for deleting a ProjectPositionEntity
     */
    @DeleteMapping(path = {Constants.DELETE_PROJECT_POSITION})
    public void delete(@PathVariable int id) {
        projectPositionService.delete(id);
    }
}