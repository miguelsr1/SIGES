/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgConsultaGuardada;

/**
 *
 * @author Sofis Solutions
 */
public class ConsultaGuardadaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cgr SgConsultaGuardada
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgConsultaGuardada cgr) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cgr==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
          
            if (StringUtils.isBlank(cgr.getCgrNombre())) {
                ge.addError("cgrNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (cgr.getCgrNombre().length() > 255){
                ge.addError("cgrNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
