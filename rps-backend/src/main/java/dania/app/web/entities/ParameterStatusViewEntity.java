package dania.app.web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"type", "id"})
@Entity
@Table(name = "parameter_status_view", schema = "planification", catalog = "")
public class ParameterStatusViewEntity implements Serializable {
    @Id
    private String type;
    @Id
    private Integer id;
    private String description;

}
