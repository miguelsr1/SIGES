/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgUsuario;

public class UsuarioValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgUsuario
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgUsuario entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(entity.getUsuCodigo())) {
                ge.addError("usuCodigo", Errores.ERROR_CODIGO_VACIO);
            } else {
                if (entity.getUsuCodigo().length() > 45) {
                    ge.addError("usuCodigo",Errores.ERROR_LARGO_CODIGO_45);
                }
                String codigo = entity.getUsuCodigo();
                String patron = "^\\w+[\\-\\_\\w]*$";
                Pattern pattern = Pattern.compile(patron);
                Matcher matcher = pattern.matcher(codigo);
                if(!matcher.matches()){
                    ge.addError("usuCodigo",Errores.ERROR_CODIGO_CARACTERES_INVALIDOS);
                }
            }
            if(!StringUtils.isBlank(entity.getUsuEmail())){
                Pattern pattern = Pattern.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +"[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$");
                Matcher matcher = pattern.matcher(entity.getUsuEmail());              
                if(!matcher.matches()){
                    ge.addError(Errores.ERROR_MAIL_INCORRECTO);
                }
            }
            if (StringUtils.isBlank(entity.getUsuNombre())) {
                ge.addError("usuNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getUsuNombre().length() > 255) {
                ge.addError(Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
