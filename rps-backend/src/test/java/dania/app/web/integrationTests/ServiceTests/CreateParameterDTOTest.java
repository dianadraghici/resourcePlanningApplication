package dania.app.web.integrationTests.ServiceTests;

import dania.app.web.controllers.dto.ParametersDTO;
import org.springframework.stereotype.Service;

@Service
public class CreateParameterDTOTest {

    public ParametersDTO getParameterDto(Integer id, String description, String type) {
        ParametersDTO parametersDto = new ParametersDTO();
        parametersDto.setId(id);
        parametersDto.setDescription(description);
        parametersDto.setType(type);
        return parametersDto;
    }
}
