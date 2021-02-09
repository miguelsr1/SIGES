/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgItemLiquidacion;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 * @author Sofis Solutions
 */
public class ItemLiquidacionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ili SgItemLiquidacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgItemLiquidacion ili) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ili == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (ili.getIliCodigo()==null) {
                ge.addError("iliCodigo", Errores.ERROR_CODIGO_VACIO);
            } 
            if (StringUtils.isBlank(ili.getIliNombre())) {
                ge.addError("iliNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ili.getIliNombre().length() > 255) {
                ge.addError("iliNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Compara dos elementos del tipo. Indica si los elementos contienen la
     * misma información.
     *
     * @param c1 SgItemLiquidacion
     * @param c2 SgItemLiquidacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgItemLiquidacion c1, SgItemLiquidacion c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {
                respuesta = respuesta && StringUtils.equals(c1.getIliNombre(), c2.getIliNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getIliHabilitado(), c2.getIliHabilitado());
            }
        } else {
            respuesta = c2 == null;
        }
        return respuesta;
    }
}
