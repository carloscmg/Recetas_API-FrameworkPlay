package validators;

import javax.validation.ConstraintValidator;

import play.data.validation.Constraints;
import play.libs.F;
import play.libs.F.Tuple;

public class DifficultyValidator 
		extends Constraints.Validator<String> 
		implements ConstraintValidator<Difficulty, String>{

	@Override
	public boolean isValid(String cadena) {
		String palabra = cadena.toLowerCase();
		if(palabra.equals("alta")) {
			return true;
		}else if(palabra.equals("media"))  {
			return true;
		}else if(palabra.equals("baja")) {
			return true;
		}else {
		return false;
		}
	}

	@Override
	public Tuple<String, Object[]> getErrorMessageKey() {
		return new F.Tuple<String, Object[]>(
				"Dificultad mal asignada",
				new Object[]{""});
	}

	@Override
	public void initialize(Difficulty constraintAnnotation) {
		// TODO Auto-generated method stub
		
	} 

}
