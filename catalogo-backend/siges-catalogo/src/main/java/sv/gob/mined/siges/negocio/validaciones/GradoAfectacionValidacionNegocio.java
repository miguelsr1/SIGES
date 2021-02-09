/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgGradoAfectacion;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class GradoAfectacionValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param gaf SgGradoAfectacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgGradoAfectacion gaf) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (gaf==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(gaf.getGafCodigo())) {
                ge.addError("gafCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (gaf.getGafCodigo().length() > 45){
                ge.addError("gafCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(gaf.getGafNombre())) {
                ge.addError("gafNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (gaf.getGafNombre().length() > 255){
                ge.addError("gafNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgGradoAfectacion
     * @param c2 SgGradoAfectacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgGradoAfectacion c1, SgGradoAfectacion c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getGafCodigo(), c2.getGafCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getGafNombre(), c2.getGafNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getGafHabilitado(), c2.getGafHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
