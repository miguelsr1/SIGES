/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAreaAsignaturaModulo;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class AreaAsignaturaModuloValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param aream SgAreaAsignaturaModulo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAreaAsignaturaModulo aream) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (aream==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(aream.getAamCodigo())) {
                ge.addError("areamCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (aream.getAamCodigo().length() > 45){
                ge.addError("areamCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(aream.getAamNombre())) {
                ge.addError("areamNombre",Errores.ERROR_NOMBRE_VACIO);
            } else if (aream.getAamNombre().length() > 255){
                ge.addError("areamNombre",Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgAreaAsignaturaModulo
     * @param c2 SgAreaAsignaturaModulo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAreaAsignaturaModulo c1, SgAreaAsignaturaModulo c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getAamCodigo(), c2.getAamCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getAamNombre(), c2.getAamNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getAamHabilitado(), c2.getAamHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
