/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgFuenteAbastecimientoAgua;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class FuenteAbastecimientoAguaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param faa SgFuenteAbastecimientoAgua
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgFuenteAbastecimientoAgua faa) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (faa==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(faa.getFaaCodigo())) {
                ge.addError("faaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (faa.getFaaCodigo().length() > 45){
                ge.addError("faaCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(faa.getFaaNombre())) {
                ge.addError("faaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (faa.getFaaNombre().length() > 255){
                ge.addError("faaNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgFuenteAbastecimientoAgua
     * @param c2 SgFuenteAbastecimientoAgua
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgFuenteAbastecimientoAgua c1, SgFuenteAbastecimientoAgua c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getFaaCodigo(), c2.getFaaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getFaaNombre(), c2.getFaaNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getFaaHabilitado(), c2.getFaaHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
