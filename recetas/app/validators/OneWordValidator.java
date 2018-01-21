package validators;

import javax.validation.ConstraintValidator;

import play.data.validation.Constraints;
import play.libs.F;
import play.libs.F.Tuple;

public class OneWordValidator 
		extends Constraints.Validator<String> 
		implements ConstraintValidator<OneWord, String>{

	@Override
	public boolean isValid(String palabra) {
		return !palabra.contains(" ");
	}

	@Override
	public Tuple<String, Object[]> getErrorMessageKey() {
		return new F.Tuple<String, Object[]>(
				"Debe de contener solo una palabra",
				new Object[]{""});
	}

	@Override
	public void initialize(OneWord constraintAnnotation) {
		// TODO Auto-generated method stub
		
	} 

}
