/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;


import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumTipoPeriodoSeccion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPeriodoCalificacion;


/**
 *
 * @author Sofis Solutions
 */
public class PeriodoCalificacionValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param pca SgPeriodoCalificacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPeriodoCalificacion pca) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pca==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
           
            if (pca.getPcaCalendario() == null){
                ge.addError(Errores.ERROR_CALENDARIO_VACIO);
            }
            
            if (pca.getPcaModalidad() == null){
                ge.addError("pcaModalidadEducativa", Errores.ERROR_MODALIDAD_EDUCATIVA_VACIO);
            }
            
            if (pca.getPcaModalidadAtencion() == null){
                ge.addError("pcaModalidadAtencion", Errores.ERROR_MODALIDAD_ATENCION_VACIO);
            }
            if(pca.getPcaNumero()==null){
                ge.addError("pcaNumero", Errores.ERROR_CANTIDAD_VACIO);
            }else{
                if(pca.getPcaNumero() > 6){
                    ge.addError("pcaNumero", Errores.ERROR_CANTIDAD_MAYOR_LIMITE);
                }
            }
            if(StringUtils.isBlank(pca.getPcaNombre())){
                ge.addError("pcaNombre", Errores.ERROR_NOMBRE_VACIO);
            }
            if(!EnumTipoPeriodoSeccion.ANUAL.equals(pca.getPcaTipoPeriodo()) && pca.getPcaNumeroPeriodo()==null){
                ge.addError("secNumeroPeriodo", Errores.ERROR_NUMERO_PERIODO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
