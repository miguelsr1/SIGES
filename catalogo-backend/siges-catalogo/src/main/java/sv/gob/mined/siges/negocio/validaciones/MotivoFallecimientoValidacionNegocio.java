/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoFallecimiento;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class MotivoFallecimientoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mfa SgMotivoFallecimiento
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMotivoFallecimiento mfa) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mfa==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(mfa.getMfaCodigo())) {
                ge.addError("mfaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mfa.getMfaCodigo().length() > 45){
                ge.addError("mfaCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(mfa.getMfaNombre())) {
                ge.addError("mfaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mfa.getMfaNombre().length() > 255){
                ge.addError("mfaNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgMotivoFallecimiento
     * @param c2 SgMotivoFallecimiento
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMotivoFallecimiento c1, SgMotivoFallecimiento c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMfaCodigo(), c2.getMfaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMfaNombre(), c2.getMfaNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMfaHabilitado(), c2.getMfaHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
