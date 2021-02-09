/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEstIndicador;

/**
 *
 * @author Sofis Solutions
 */
public class IndicadorValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ind SgIndicador
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEstIndicador ind) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ind==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(ind.getIndCodigo())) {
                ge.addError("indCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ind.getIndCodigo().length() > 45){
                ge.addError("indCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(ind.getIndNombre())) {
                ge.addError("indNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ind.getIndNombre().length() > 1000){
                ge.addError("indNombre",Errores.ERROR_LARGO_NOMBRE_1000);
            }
            

            if (StringUtils.isNotBlank(ind.getIndDefinicion()) && ind.getIndDefinicion().length() > 1000){
                ge.addError("indDefinicion",Errores.ERROR_LARGO_DEFINICION_1000);
            }
            
            if (StringUtils.isNotBlank(ind.getIndMetodoCalculo()) && ind.getIndMetodoCalculo().length() > 1000){
                ge.addError("indMetodoCalculo",Errores.ERROR_LARGO_METODO_CALCULO_1000);
            }
            
            if (StringUtils.isNotBlank(ind.getIndDescripcion()) && ind.getIndDescripcion().length() > 1000){
                ge.addError("indDescripcion",Errores.ERROR_LARGO_DESCRIPCION_1000);
            }
            
            if (StringUtils.isNotBlank(ind.getIndFuente()) && ind.getIndFuente().length() > 1000){
                ge.addError("indFuente",Errores.ERROR_LARGO_FUENTE_1000);
            }
            
            if (StringUtils.isNotBlank(ind.getIndObservaciones()) && ind.getIndObservaciones().length() > 1000){
                ge.addError("indObservaciones",Errores.ERROR_LARGO_OBSERVACIONES_1000);
            }
            
            if (ind.getIndTipoResultado() == null){
                ge.addError("indTipoResultado", Errores.ERROR_TIPO_RESULTADO_VACIO);
            }
          
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }


}
