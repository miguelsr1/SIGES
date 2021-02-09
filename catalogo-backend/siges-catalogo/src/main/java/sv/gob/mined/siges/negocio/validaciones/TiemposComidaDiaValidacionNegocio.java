/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTiemposComidaDia;

/**
 *
 * @author Sofis Solutions
 */
public class TiemposComidaDiaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tcd SgTiemposComidaDia
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTiemposComidaDia tcd) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tcd==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tcd.getTcdCodigo())) {
                ge.addError("tcdCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tcd.getTcdCodigo().length() > 45){
                ge.addError("tcdCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tcd.getTcdNombre())) {
                ge.addError("tcdNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tcd.getTcdNombre().length() > 255){
                ge.addError("tcdNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
