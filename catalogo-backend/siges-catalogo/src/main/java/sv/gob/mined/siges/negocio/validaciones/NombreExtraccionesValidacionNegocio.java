/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEstNombreExtraccion;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class NombreExtraccionesValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param nex SgNombreExtracciones
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEstNombreExtraccion nex) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (nex==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(nex.getNexCodigo())) {
                ge.addError("nexCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (nex.getNexCodigo().length() > 45){
                ge.addError("nexCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(nex.getNexNombre())) {
                ge.addError("nexNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (nex.getNexNombre().length() > 255){
                ge.addError("nexNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgNombreExtracciones
     * @param c2 SgNombreExtracciones
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgEstNombreExtraccion c1, SgEstNombreExtraccion c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getNexCodigo(), c2.getNexCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getNexNombre(), c2.getNexNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getNexHabilitado(), c2.getNexHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
