/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRangoFecha;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class RangoFechaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rfe SgRangoFecha
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRangoFecha rfe) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rfe==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (StringUtils.isBlank(rfe.getRfeCodigo())){
                ge.addError("rfeCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (rfe.getRfeCodigo().length() > 4){
                ge.addError("rfeCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (rfe.getRfeFechaDesde() == null){
                ge.addError("rfeFechaDesde", Errores.ERROR_FECHA_DESDE_VACIO);
            }
            
            if (rfe.getRfeFechaHasta() == null){
                ge.addError("rfeFechaHasta", Errores.ERROR_FECHA_HASTA_VACIO);
            }
            if(rfe.getRfeFechaDesde()!=null && rfe.getRfeFechaHasta()!=null){
                if (rfe.getRfeFechaDesde().isAfter(rfe.getRfeFechaHasta())){
                    ge.addError("rfeFechaDesde", Errores.ERROR_FECHA_DESDE_MAYOR_HASTA);
                }
            }
            
            if(rfe.getRfePeriodoCalificacion()!=null && rfe.getRfeFechaDesde() != null && rfe.getRfeFechaHasta() != null){
                if((rfe.getRfePeriodoCalificacion().getPcaCalendario().getCalFechaInicio()!=null)&&(rfe.getRfePeriodoCalificacion().getPcaCalendario().getCalFechaFin()!=null)){
                    if(rfe.getRfeFechaDesde().isBefore(rfe.getRfePeriodoCalificacion().getPcaCalendario().getCalFechaInicio())
                            || (rfe.getRfeFechaDesde().isAfter(rfe.getRfePeriodoCalificacion().getPcaCalendario().getCalFechaFin()))){
                        ge.addError("rfeFechaDesde", Errores.ERROR_RANGO_FECHA_FUERA_DE_RANGO_CALENDARIO);
                    }else{
                        if(rfe.getRfeFechaHasta().isBefore(rfe.getRfePeriodoCalificacion().getPcaCalendario().getCalFechaInicio())
                            || (rfe.getRfeFechaHasta().isAfter(rfe.getRfePeriodoCalificacion().getPcaCalendario().getCalFechaFin()))){
                        ge.addError("rfeFechaHasta", Errores.ERROR_RANGO_FECHA_FUERA_DE_RANGO_CALENDARIO);
                    }
                    }
                }
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
     * @param c1 SgRangoFecha
     * @param c2 SgRangoFecha
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgRangoFecha c1, SgRangoFecha c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getRfeCodigo(), c2.getRfeCodigo());
           //     respuesta = respuesta && StringUtils.equals(c1.getRfeNombre(), c2.getRfeNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getRfeHabilitado(), c2.getRfeHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
