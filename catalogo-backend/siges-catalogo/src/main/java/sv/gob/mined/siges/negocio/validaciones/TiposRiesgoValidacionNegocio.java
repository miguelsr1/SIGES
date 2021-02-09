/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTiposRiesgo;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class TiposRiesgoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tri SgTiposRiesgo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTiposRiesgo tri) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tri==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tri.getTriCodigo())) {
                ge.addError("triCodigo",Errores.ERROR_CODIGO_VACIO);
            } else if (tri.getTriCodigo().length() > 45){
                ge.addError("triCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tri.getTriNombre())) {
                ge.addError("triNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tri.getTriNombre().length() > 255){
                ge.addError("triNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTiposRiesgo
     * @param c2 SgTiposRiesgo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTiposRiesgo c1, SgTiposRiesgo c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTriCodigo(), c2.getTriCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTriNombre(), c2.getTriNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTriHabilitado(), c2.getTriHabilitado());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTriRiesgoAmbiental(), c2.getTriRiesgoAmbiental());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTriRiesgoSocial(), c2.getTriRiesgoSocial());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}