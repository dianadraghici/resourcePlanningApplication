package dania.app.web.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametersDTO {

    private String type;
    private Integer id;
    private String description;

}

