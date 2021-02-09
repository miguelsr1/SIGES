/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoInasistenciaPersonal;

/**
 *
 * @author Sofis Solutions
 */
public class MotivoInasistenciaPersonalValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mip SgMotivoInasistenciaPersonal
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMotivoInasistenciaPersonal mip) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mip==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(mip.getMipCodigo())) {
                ge.addError("mipCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mip.getMipCodigo().length() > 45){
                ge.addError("mipCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(mip.getMipNombre())) {
                ge.addError("mipNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mip.getMipNombre().length() > 255){
                ge.addError("mipNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
