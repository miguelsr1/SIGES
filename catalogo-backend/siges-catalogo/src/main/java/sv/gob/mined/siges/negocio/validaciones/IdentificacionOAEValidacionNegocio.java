/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgIdentificacionOAE;

/**
 *
 * @author Sofis Solutions
 */
public class IdentificacionOAEValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ioa SgIdentificacionOAE
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgIdentificacionOAE ioa) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ioa==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(ioa.getIoaCodigo())) {
                ge.addError("ioaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ioa.getIoaCodigo().length() > 45){
                ge.addError("ioaCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(ioa.getIoaNombre())) {
                ge.addError("ioaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ioa.getIoaNombre().length() > 255){
                ge.addError("ioaNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

  
}
