/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacion;
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

public class CalificacionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param eca SgCalificacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCalificacion eca) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (eca == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (StringUtils.isBlank(eca.getCalCodigo())) {
                ge.addError("calCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (eca.getCalCodigo().length() > 45) {
                ge.addError("calCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }

            if (StringUtils.isBlank(eca.getCalValor())) {
                ge.addError("calValor", Errores.ERROR_VALOR_VACIO);
            } else if (eca.getCalValor().length() > 25) {
                ge.addError("calValor", Errores.ERROR_LARGO_VALOR_25);
            }
            
            
            if (eca.getCalValorEnCertificado() != null && eca.getCalValorEnCertificado().length() > 25) {
                ge.addError("calValorEnCertificado", Errores.ERROR_LARGO_VALOR_25);
            }

            if (eca.getCalOrden() == null) {
                ge.addError("calOrden", Errores.ERROR_ORDEN_VACIO);
            } else if (eca.getCalOrden() < 1) {
                ge.addError("calOrden", Errores.ERROR_ORDEN_NEGATICO);
            }

            if (eca.getCalEscala() == null) {
                ge.addError("calEscala", Errores.ERROR_ESCALA_VACIO);
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
     * @param c1 SgCalificacion
     * @param c2 SgCalificacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgCalificacion c1, SgCalificacion c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {
                respuesta = StringUtils.equals(c1.getCalCodigo(), c2.getCalCodigo());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCalHabilitado(), c2.getCalHabilitado());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getCalOrden(), c2.getCalOrden());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getCalEscala(), c2.getCalEscala());
                respuesta = respuesta && StringUtils.equals(c1.getCalValor(), c2.getCalValor());
                respuesta = respuesta && StringUtils.equals(c1.getCalValorEnCertificado(), c2.getCalValorEnCertificado());
                
            }
        } else {
            respuesta = c2 == null;
        }
        return respuesta;
    }
}
