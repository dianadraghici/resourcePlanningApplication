package dania.app.web.service;

import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.mapper.ParametersMapper;
import dania.app.web.repository.ParameterRepository;
import dania.app.web.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParameterService implements ServiceManager<ParametersDTO, Integer> {

    private final ParameterRepository parameterRepository;
    private final ParametersMapper parametersMapper;

    @Autowired
    public ParameterService(ParameterRepository parameterRepository, ParametersMapper parametersMapper) {
        this.parameterRepository = parameterRepository;
        this.parametersMapper = parametersMapper;
    }

    @Override
    public ParametersDTO create(ParametersDTO parametersDTO) {
        return parametersMapper.parameterEntityToDTO(parameterRepository.saveAndFlush(
                parametersMapper.parameterDTOToEntity(parametersDTO)));
    }

    @Override
    public ParametersDTO delete(Integer id) {
        ParametersDTO parametersDTO = findById(id);
        if (parametersDTO != null) {
            parameterRepository.delete(parametersMapper.parameterDTOToEntity(parametersDTO));
        }
        return parametersDTO;
    }

    @Override
    public List<ParametersDTO> findAll() {
        return parametersMapper.listParamatersEntityToDTO(parameterRepository.findAll());
    }

    @Override
    public ParametersDTO findById(Integer id) {
        return parametersMapper.parameterEntityToDTO(parameterRepository.findById(id));
    }

    @Override
    public ParametersDTO update(ParametersDTO parametersDTO) {
        return parametersMapper.parameterEntityToDTO(parameterRepository.saveAndFlush(
                parametersMapper.parameterDTOToEntity(parametersDTO)));
    }

    public List<ParametersDTO> findStatusParameters() {
        return parametersMapper.listParamatersEntityToDTO(parameterRepository.findParametersEntityByType(
                Constants.STATUS_PARAMETERS_TYPE));
    }

    public List<ParametersDTO> findPercentParameters() {
        return parametersMapper.listParamatersEntityToDTO(parameterRepository.findParametersEntityByType(
                Constants.PERCENT_PARAMETERS_TYPE));
    }

    public List<ParametersDTO> findTechnologyParameters() {
        return parametersMapper.listParamatersEntityToDTO(parameterRepository.findParametersEntityByType(
                Constants.TECHNOLOGY_PARAMETERS_TYPE));
    }

    public List<ParametersDTO> findPositionParameters() {
        return parametersMapper.listParamatersEntityToDTO(parameterRepository.findParametersEntityByType(
                Constants.POSITION_PARAMETERS_TYPE));
    }

    public ParametersDTO findByIdPosition(Integer id) {
        return parametersMapper.parameterEntityToDTO(parameterRepository.findParametersEntityByIdAndType(
                id, Constants.POSITION_PARAMETERS_TYPE));
    }

    public ParametersDTO findByIdPercent(Integer id) {
        return parametersMapper.parameterEntityToDTO(parameterRepository.findParametersEntityByIdAndType(
                id, Constants.PERCENT_PARAMETERS_TYPE));
    }

    public ParametersDTO findByIdStatus(Integer id) {
        return parametersMapper.parameterEntityToDTO(parameterRepository.findParametersEntityByIdAndType(
                id, Constants.STATUS_PARAMETERS_TYPE));
    }

    public ParametersDTO findByIdTechnology(Integer id) {
        return parametersMapper.parameterEntityToDTO(parameterRepository.findParametersEntityByIdAndType(
                id, Constants.TECHNOLOGY_PARAMETERS_TYPE));
    }
}


