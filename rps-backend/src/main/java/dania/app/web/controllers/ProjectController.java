package dania.app.web.controllers;

import dania.app.web.controllers.dto.ProjectDTO;
import dania.app.web.errorHandler.BadRequestException;
import dania.app.web.service.ProjectService;
import dania.app.web.utils.Constants;
import dania.app.web.utils.PageParams;
import dania.app.web.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping({Constants.PROJECT_CONTROLLER})
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDTO> getProjectList() {
        return projectService.findAll();
    }

    /**
     * @param page - the page number to retrieve.
     * @param size - the maxim size or length of elements to be on the given page.
     * @param sortBy - the sort criteria to be apply on the query made.
     * @param direction - the direction [ASC/DESC] to be apply on the query made.
     * @return a Pair containing first the retrieved page requested and secondly the total size of elements.
     */
    @GetMapping(value = Constants.FIND_MEMBER_PAGINATED, params = {"page", "size", "sortBy"})
    public Pair<List<ProjectDTO>, Long> findProjectsPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String sortBy, @RequestParam String direction) {
        final PageParams params = Cleaner.processGivenPageParams(new PageParams(page, size, sortBy, Sort.Direction.fromString(direction)), ProjectDTO.class);
        return projectService.findAllWith(PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getSortBy())));
    }

    /**
     * Search a project by a given project code
     * @param code - project code.
     * @return the project with given code
     */
    @GetMapping(path = Constants.FIND_PROJECT_BY_CODE)
    public ProjectDTO getProjectByCode(@PathVariable String code) {
        return projectService.findByProjectCode(code);
    }

    @PostMapping
    public ProjectDTO create(@RequestBody @Valid ProjectDTO projectDTO) {
        return projectService.create(projectDTO);
    }

    @PutMapping(path = Constants.UPDATE_PROJECT_BY_ID)
    public ProjectDTO updateProjectById(@RequestBody @Valid ProjectDTO projectDTO, @PathVariable Integer id) {
        if (projectDTO.getId() == null || projectDTO.getId() <= 0) {
            throw new BadRequestException("Project - Project Id must be a positive number!");
        }
        return projectService.updateProjectById(projectDTO, id);
    }

    @DeleteMapping(path = Constants.DELETE_PROJECT)
    public void delete(@PathVariable Integer id) {
        projectService.delete(id);
    }

}