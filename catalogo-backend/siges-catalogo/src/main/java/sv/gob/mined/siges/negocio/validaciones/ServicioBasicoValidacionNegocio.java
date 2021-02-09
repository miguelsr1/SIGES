/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgServicioBasico;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class ServicioBasicoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param sba SgServicioBasico
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgServicioBasico sba) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (sba==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(sba.getSbaCodigo())) {
                ge.addError("sbaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (sba.getSbaCodigo().length() > 45){
                ge.addError("sbaCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(sba.getSbaNombre())) {
                ge.addError("sbaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (sba.getSbaNombre().length() > 255){
                ge.addError("sbaNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgServicioBasico
     * @param c2 SgServicioBasico
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgServicioBasico c1, SgServicioBasico c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getSbaCodigo(), c2.getSbaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getSbaNombre(), c2.getSbaNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getSbaHabilitado(), c2.getSbaHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
