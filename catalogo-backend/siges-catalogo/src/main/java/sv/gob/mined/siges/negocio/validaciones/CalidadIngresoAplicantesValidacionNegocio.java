/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCalidadIngresoAplicantes;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class CalidadIngresoAplicantesValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cia SgCalidadIngresoAplicantes
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCalidadIngresoAplicantes cia) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cia==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(cia.getCiaCodigo())) {
                ge.addError("ciaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (cia.getCiaCodigo().length() > 45){
                ge.addError("ciaCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(cia.getCiaNombre())) {
                ge.addError("ciaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (cia.getCiaNombre().length() > 255){
                ge.addError("ciaNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgCalidadIngresoAplicantes
     * @param c2 SgCalidadIngresoAplicantes
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgCalidadIngresoAplicantes c1, SgCalidadIngresoAplicantes c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getCiaCodigo(), c2.getCiaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getCiaNombre(), c2.getCiaNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCiaHabilitado(), c2.getCiaHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
