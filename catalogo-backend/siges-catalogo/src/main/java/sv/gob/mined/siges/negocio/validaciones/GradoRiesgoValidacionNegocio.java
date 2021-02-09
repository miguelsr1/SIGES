/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgGradoRiesgo;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class GradoRiesgoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param gre SgGradoRiesgo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgGradoRiesgo gre) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (gre==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(gre.getGreCodigo())) {
                ge.addError("greCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (gre.getGreCodigo().length() > 45){
                ge.addError("greCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(gre.getGreNombre())) {
                ge.addError("greNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (gre.getGreNombre().length() > 255){
                ge.addError("greNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgGradoRiesgo
     * @param c2 SgGradoRiesgo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgGradoRiesgo c1, SgGradoRiesgo c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getGreCodigo(), c2.getGreCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getGreNombre(), c2.getGreNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getGreHabilitado(), c2.getGreHabilitado());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getGreRiesgoAmbiental(), c2.getGreRiesgoAmbiental());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getGreRiesgoSocial(), c2.getGreRiesgoSocial());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
