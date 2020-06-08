package dania.app.web.validations;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = StatusValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusConstraints {

    String message();

    String field();

    String fieldMatch();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        StatusConstraints[] value();
    }

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
