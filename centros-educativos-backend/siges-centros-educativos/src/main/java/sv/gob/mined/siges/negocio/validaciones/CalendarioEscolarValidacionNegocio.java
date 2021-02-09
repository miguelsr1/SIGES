/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPeriodoCalendario;

/**
 *
 * @author Sofis Solutions
 */
public class CalendarioEscolarValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ces SgCalendarioEscolar
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPeriodoCalendario ces) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ces==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
           if (ces.getCesFechaDesde()==null) {
                ge.addError("cesFechaDesde", Errores.ERROR_FECHA_DESDE_VACIO);
            } 
           if (ces.getCesFechaHasta()==null) {
                ge.addError("cesFechaHasta", Errores.ERROR_FECHA_HASTA_VACIO);
            } 
           if (ces.getCesTipo()==null) {
                ge.addError("cesTipo", Errores.ERROR_TIPO_VACIO);
            } 
           if (ces.getCesNivel()==null) {
                ge.addError("cesNivel", Errores.ERROR_NIVEL_VACIO);
            } 
           if (ces.getCesModalidadAtencion()==null) {
                ge.addError("cesModalidadAtencion", Errores.ERROR_MODALIDAD_ATENCION_VACIO);
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
     * @param c1 SgCalendarioEscolar
     * @param c2 SgCalendarioEscolar
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgPeriodoCalendario c1, SgPeriodoCalendario c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
           /*     respuesta = StringUtils.equals(c1.getCesCodigo(), c2.getCesCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getCesNombre(), c2.getCesNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCesHabilitado(), c2.getCesHabilitado());*/
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
