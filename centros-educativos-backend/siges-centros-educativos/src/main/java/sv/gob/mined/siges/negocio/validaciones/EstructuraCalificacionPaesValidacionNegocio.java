/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEstructuraCalificacionPaes;

/**
 *
 * @author Sofis Solutions
 */
public class EstructuraCalificacionPaesValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ecp SgEstructuraCalificacionPaes
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEstructuraCalificacionPaes ecp) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ecp==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(ecp.getEcpNie())) {
                ge.addError("ecpCodigo", Errores.ERROR_NIE_VACIO);
            } 
            if (StringUtils.isBlank(ecp.getEcpCodigoCpe())) {
                ge.addError("ecpNombre", Errores.ERROR_CODIGO_EXTERNO_VACIO);
            } 
             if (StringUtils.isBlank(ecp.getEcpCalificacion())) {
                ge.addError("ecpNombre", Errores.ERROR_CALIFICACION_VACIO);
            } 
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

   
}
