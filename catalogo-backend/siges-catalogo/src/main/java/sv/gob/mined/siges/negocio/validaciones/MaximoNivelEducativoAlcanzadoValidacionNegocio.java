/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMaximoNivelEducativoAlcanzado;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class MaximoNivelEducativoAlcanzadoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mne SgMaximoNivelEducativoAlcanzado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMaximoNivelEducativoAlcanzado mne) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mne==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(mne.getMneCodigo())) {
                ge.addError("mneCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mne.getMneCodigo().length() > 45){
                ge.addError("mneCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(mne.getMneNombre())) {
                ge.addError("mneNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mne.getMneNombre().length() > 255){
                ge.addError("mneNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgMaximoNivelEducativoAlcanzado
     * @param c2 SgMaximoNivelEducativoAlcanzado
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMaximoNivelEducativoAlcanzado c1, SgMaximoNivelEducativoAlcanzado c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMneCodigo(), c2.getMneCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMneNombre(), c2.getMneNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMneHabilitado(), c2.getMneHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
