package dania.app.web.service;

import dania.app.web.controllers.dto.ProjectPositionDTO;
import dania.app.web.entities.ProjectPositionEntity;
import dania.app.web.mapper.ProjectPositionMapper;
import dania.app.web.repository.ProjectPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectPositionService implements ServiceManager<ProjectPositionDTO, Integer> {

    private final ProjectPositionRepository projectPositionRepository;
    private final ProjectPositionMapper projectPositionMapper;
    private final ParameterService parameterService;

    @Autowired
    public ProjectPositionService(ProjectPositionRepository projectPositionRepository,
                                  ProjectPositionMapper projectPositionMapper,
                                  ParameterService parameterService) {
        this.projectPositionRepository = projectPositionRepository;
        this.projectPositionMapper = projectPositionMapper;
        this.parameterService = parameterService;
    }

    @Override
    public ProjectPositionDTO create(ProjectPositionDTO projectPositionDTO) {
        return this.projectPositionEntityToDTO(projectPositionRepository.saveAndFlush(
                projectPositionMapper.projectPositionDTOToEntity(projectPositionDTO)));
    }

    @Override
    public ProjectPositionDTO delete(Integer id) {
        ProjectPositionDTO projectPositionDTO = findById(id);
        if (projectPositionDTO != null) {
            projectPositionRepository.delete(projectPositionMapper.projectPositionDTOToEntity(projectPositionDTO));
        }
        return projectPositionDTO;
    }

    @Override
    public List<ProjectPositionDTO> findAll() {

        return projectPositionMapper.listProjectPositionEntityToDTO(projectPositionRepository.findAll());
    }

    @Override
    public ProjectPositionDTO findById(Integer id) {

        return this.projectPositionEntityToDTO(projectPositionRepository.findOneById(id));
    }

    @Override
    public ProjectPositionDTO update(ProjectPositionDTO projectPositionEntity) {
        return this.projectPositionEntityToDTO(projectPositionRepository.saveAndFlush(
                projectPositionMapper.projectPositionDTOToEntity(projectPositionEntity)));
    }

    public List<ProjectPositionDTO> findProjectPositionByIdProject(int id) {
        return getProjectPositionDTOSList(id);
    }

    private ProjectPositionDTO projectPositionEntityToDTO(ProjectPositionEntity projectPositionEntity) {
        ProjectPositionDTO projectPositionDTO = projectPositionMapper.projectPositionEntityToDTO(projectPositionEntity);

        projectPositionDTO.setPositionDescription(parameterService.findByIdPosition(projectPositionEntity
                .getPositionId()).getDescription());
        projectPositionDTO.setPercentDescription(parameterService.findByIdPercent(projectPositionEntity
                .getPercentId()).getDescription());

        return projectPositionDTO;
    }

    private List<ProjectPositionDTO> getProjectPositionDTOSList(int id) {
        List<ProjectPositionDTO> projectPositionDTOList = projectPositionMapper.listProjectPositionEntityToDTO(
                projectPositionRepository.findProjectPositionEntityByProjectEntity_Id(id));

        for (ProjectPositionDTO projectPositionDTO : projectPositionDTOList) {
            if (projectPositionDTO.getPercentId() >= 0) {
                projectPositionDTO.setPercentDescription(parameterService.findByIdPercent(projectPositionDTO.getPercentId()).getDescription());
            }
            if (projectPositionDTO.getPositionId() >= 0) {
                projectPositionDTO.setPositionDescription(parameterService.findByIdPosition(projectPositionDTO.getPositionId()).getDescription());
            }
        }
        return projectPositionDTOList;
    }
}
