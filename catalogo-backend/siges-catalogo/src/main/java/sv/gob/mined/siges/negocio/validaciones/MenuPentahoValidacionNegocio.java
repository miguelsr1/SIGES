/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMenuPentaho;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class MenuPentahoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param fom SgMenuPentaho
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMenuPentaho pen) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pen == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(pen.getMpeNombre())) {
                ge.addError("mpeNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (pen.getMpeNombre().length() > 255) {
                ge.addError("mpeNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if (StringUtils.isBlank(pen.getMpeRuta())) {
                ge.addError("mpeRuta", Errores.ERROR_PENTAHO_RUTA_VACIO);
            } else if (pen.getMpeRuta().length() > 255) {
                ge.addError("mpeRuta", Errores.ERROR_PENTAHO_OPERACION_255);
            }
            if (pen.getMpeTipo() == null) {
                ge.addError("mpeTipo", Errores.ERROR_PENTAHO_TIPO_COMPONENTE);
            }
            if (pen.getMpeOperacionFk() == null){
                ge.addError("mpeOperacionFk", Errores.ERROR_PENTAHO_OPERACION);
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
     * @param c1 SgMenuPentaho
     * @param c2 SgMenuPentaho
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMenuPentaho c1, SgMenuPentaho c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {
                respuesta =  StringUtils.equals(c1.getMpeNombre(), c2.getMpeNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMpeHabilitado(), c2.getMpeHabilitado());
                respuesta = respuesta && StringUtils.equals(c1.getMpeRuta(), c2.getMpeRuta());
                respuesta = respuesta && c1.getMpeTipo().equals(c2.getMpeTipo());
            }
        } else {
            respuesta = c2 == null;
        }
        return respuesta;
    }
}
