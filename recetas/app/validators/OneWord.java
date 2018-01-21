package validators;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OneWordValidator.class)
public @interface OneWord {
	 String message() default "invalid_word:only one word";
	 Class<?>[] groups() default {};
	 Class<? extends Payload>[] payload() default {};
}
