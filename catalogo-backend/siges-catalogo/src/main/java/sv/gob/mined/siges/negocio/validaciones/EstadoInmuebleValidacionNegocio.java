/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEstadoInmueble;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class EstadoInmuebleValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ein SgEstadoInmueble
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEstadoInmueble ein) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ein==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(ein.getEinCodigo())) {
                ge.addError("einCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ein.getEinCodigo().length() > 45){
                ge.addError("einCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(ein.getEinNombre())) {
                ge.addError("einNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ein.getEinNombre().length() > 255){
                ge.addError("einNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgEstadoInmueble
     * @param c2 SgEstadoInmueble
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgEstadoInmueble c1, SgEstadoInmueble c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEinCodigo(), c2.getEinCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEinNombre(), c2.getEinNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEinHabilitado(), c2.getEinHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
