package validators;

import javax.validation.ConstraintValidator;

import play.data.validation.Constraints;
import play.libs.F;
import play.libs.F.Tuple;

public class TypeOfKitchenValidator 
		extends Constraints.Validator<String> 
		implements ConstraintValidator<TypeOfKitchen, String>{

	@Override
	public boolean isValid(String cadena) {
		String palabra = cadena.toLowerCase();
		if(palabra.equals("tradicional")) {
			return true;
		}else if(palabra.equals("internacional"))  {
			return true;
		}else if(palabra.equals("asiatica")) {
			return true;
		}else if(palabra.equals("italiana")) {
			return true;
		}else if(palabra.equals("tailandesa")) {
			return true;
		}else if(palabra.equals("mediterranea")) {
			return true;
		}else if(palabra.equals("griega")) {
			return true;
		}else {
		return false;
		}
	}

	@Override
	public Tuple<String, Object[]> getErrorMessageKey() {
		return new F.Tuple<String, Object[]>(
				"Tipo de cocina mal asignado",
				new Object[]{""});
	}

	@Override
	public void initialize(TypeOfKitchen constraintAnnotation) {
		// TODO Auto-generated method stub
		
	} 

}