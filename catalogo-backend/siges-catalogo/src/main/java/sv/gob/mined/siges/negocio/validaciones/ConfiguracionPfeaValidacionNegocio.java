/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgConfiguracionPfea;


/**
 *
 * @author Sofis Solutions
 */
public class ConfiguracionPfeaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param con SgConfiguracionPfea
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgConfiguracionPfea con) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (con==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(con.getConCodigo())) {
                ge.addError(Errores.ERROR_CODIGO_VACIO);
            } else if (con.getConCodigo().length() > 45){
                ge.addError(Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(con.getConNombre())) {
                ge.addError(Errores.ERROR_NOMBRE_VACIO);
            } else if (con.getConNombre().length() > 255){
                ge.addError(Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
   
}
