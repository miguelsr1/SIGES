/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEstadoProcesoLegalizacion;

/**
 *
 * @author Sofis Solutions
 */
public class EstadoProcesoLegalizacionValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param epl SgEstadoProcesoLegalizacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEstadoProcesoLegalizacion epl) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (epl==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(epl.getEplCodigo())) {
                ge.addError("eplCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (epl.getEplCodigo().length() > 45){
                ge.addError("eplCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(epl.getEplNombre())) {
                ge.addError("eplNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (epl.getEplNombre().length() > 255){
                ge.addError("eplNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
