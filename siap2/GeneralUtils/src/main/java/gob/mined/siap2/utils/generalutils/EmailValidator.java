/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.utils.generalutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Esta clase provee métodos para trabajar con correos electrónicos.
 * @author Sofis Solutions
 */
public class EmailValidator {
    private Pattern pattern;
	private Matcher matcher;
 
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
 
        
	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
 
	/**
	 * Valida si una cadena de caracteres es un correo electrónico válido.
	 * 
	 * @param hex
	 *            cadena a validar.
	 * @return true si hex tiene formato de correo electrónico
         *         false en caso contrario
	 */
	public boolean validate(final String hex) {
 
		matcher = pattern.matcher(hex);
		return matcher.matches();
 
	}
}
