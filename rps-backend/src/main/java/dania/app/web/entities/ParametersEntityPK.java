package dania.app.web.entities;

import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(of = {"type", "id"})
public class ParametersEntityPK implements Serializable {

    @Id
    private String type;
    @Id
    private Integer id;

}
