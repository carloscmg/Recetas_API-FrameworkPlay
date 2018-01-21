package validators;

import javax.validation.ConstraintValidator;

import play.data.validation.Constraints;
import play.libs.F;
import play.libs.F.Tuple;

public class LowerCaseValidator 
		extends Constraints.Validator<String> 
		implements ConstraintValidator<LowerCase, String>{

	@Override
	public boolean isValid(String cadena) {
		String palabra = cadena.toLowerCase();
		if(palabra.equals(cadena)) {
			return true;
		}else {
		return false;
		}
	}

	@Override
	public Tuple<String, Object[]> getErrorMessageKey() {
		return new F.Tuple<String, Object[]>(
				"La palabra no es minuscula",
				new Object[]{""});
	}

	@Override
	public void initialize(LowerCase constraintAnnotation) {
		// TODO Auto-generated method stub
		
	} 

}