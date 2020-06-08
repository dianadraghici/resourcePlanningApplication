package dania.app.web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"type", "id", "description"})
@Entity
@Table(name = "parameters", schema = "planification")
@IdClass(ParametersEntityPK.class)
public class ParametersEntity {

    @Id
    private String type;
    @Id
    private Integer id;
    private String description;

}
