/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTransferenciaACed;

/**
 * Validación de las reglas de negocio de las transferencias a los centros
 * educativos.
 *
 * @author Sofis Solutions
 */
public class TransferenciaACedValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tac SgTransferenciaACed
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTransferenciaACed tac) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tac == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

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
     * @param c1 SgTransferenciaACed
     * @param c2 SgTransferenciaACed
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTransferenciaACed c1, SgTransferenciaACed c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {
                respuesta = c1.getTacMontoAutorizado().equals(c2.getTacMontoAutorizado());
                respuesta = respuesta && c1.getTacMontoSolicitado().equals(c2.getTacMontoSolicitado());
            }
        } else {
            respuesta = c2 == null;
        }
        return respuesta;
    }
}
