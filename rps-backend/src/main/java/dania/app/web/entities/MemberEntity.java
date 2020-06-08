package dania.app.web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "staffNumber", "lastName", "firstName", "flag", "technologyId"})
@Entity
@Table(name = "members", schema = "planification", catalog = "")
@DynamicUpdate
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String staffNumber;
    private String lastName;
    private String firstName;
    private Byte flag;
    private Integer technologyId;
    private String comment;

}
