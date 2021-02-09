/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoProveedor;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class TipoProveedorValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tpr SgTipoProveedor
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoProveedor tpr) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tpr==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tpr.getTprCodigo())) {
                ge.addError("tprCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tpr.getTprCodigo().length() > 45){
                ge.addError("tprCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tpr.getTprNombre())) {
                ge.addError("tprNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tpr.getTprNombre().length() > 255){
                ge.addError("tprNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Compara dos elementos del tipo.
     * Indica si los elementos contienen la misma información.
     *
     * @param c1 SgTipoProveedor
     * @param c2 SgTipoProveedor
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoProveedor c1, SgTipoProveedor c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTprCodigo(), c2.getTprCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTprNombre(), c2.getTprNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTprHabilitado(), c2.getTprHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
