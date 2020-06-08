package dania.app.web.controllers;

import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.service.ParameterService;
import dania.app.web.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(Constants.PARAMETER_CONTROLLER)
public class ParameterController {

    private ParameterService parameterService;

    @Autowired
    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @GetMapping(path = Constants.GET_STATUS_PARAMETERS)
    List<ParametersDTO> getStatusParameters() {
        return new ArrayList<>(parameterService.findStatusParameters());
    }

    @GetMapping(path = Constants.GET_PERCENT_PARAMETERS)
    List<ParametersDTO> getPercentParameters() {
        return new ArrayList<>(parameterService.findPercentParameters());
    }

    @GetMapping(path = Constants.GET_TECHNOLOGY_PARAMETERS)
    List<ParametersDTO> getTechnologyParameters() {
        return new ArrayList<>(parameterService.findTechnologyParameters());
    }

    @GetMapping(path = Constants.GET_POSITION_PARAMETERS)
    List<ParametersDTO> getPositionParameters() {
        return new ArrayList<>(parameterService.findPositionParameters());
    }
}
