/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInfTipoManejoDesechos;

/**
 *
 * @author Sofis Solutions
 */
public class InfTipoManejoDesechosValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tmd SgInfTipoManejoDesechos
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInfTipoManejoDesechos tmd) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tmd==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tmd.getTmdCodigo())) {
                ge.addError("tmdCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tmd.getTmdCodigo().length() > 45){
                ge.addError("tmdCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tmd.getTmdNombre())) {
                ge.addError("tmdNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tmd.getTmdNombre().length() > 255){
                ge.addError("tmdNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
