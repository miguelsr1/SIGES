/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.siap2.SgAreaInversion;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 * Validaciòn de las reglas de negocio de las áreas de inversión
 *
 * @author Sofis Solutions
 */
public class AreaInversionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param adi SgAreaInversion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAreaInversion adi) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (adi == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(adi.getAdiCodigo())) {
                ge.addError("adiCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (adi.getAdiCodigo().length() > 45) {
                ge.addError("adiCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(adi.getAdiNombre())) {
                ge.addError("adiNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (adi.getAdiNombre().length() > 255) {
                ge.addError("adiNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgAreaInversion
     * @param c2 SgAreaInversion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAreaInversion c1, SgAreaInversion c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {
                respuesta = StringUtils.equals(c1.getAdiCodigo(), c2.getAdiCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getAdiNombre(), c2.getAdiNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getAdiHabilitado(), c2.getAdiHabilitado());
            }
        } else {
            respuesta = c2 == null;
        }
        return respuesta;
    }
}
