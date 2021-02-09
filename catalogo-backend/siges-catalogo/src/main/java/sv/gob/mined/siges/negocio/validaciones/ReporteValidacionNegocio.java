/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgReporte;
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

/**
 *
 * @author Sofis Solutions
 */
public class ReporteValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rep SgReporte
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgReporte rep) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rep==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(rep.getRepCodigo())) {
                ge.addError("repCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (rep.getRepCodigo().length() > 45){
                ge.addError("repCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(rep.getRepNombre())) {
                ge.addError("repNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (rep.getRepNombre().length() > 255){
                ge.addError("repNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if (rep.getRepPlantilla() == null){
                ge.addError("repPlantilla", Errores.ERROR_PLANTILLA_VACIO);
            }
            if (rep.getRepDescripcion() != null && rep.getRepDescripcion().length() > 1000){
                ge.addError("repDescripcion", Errores.ERROR_LARGO_DESCRIPCION_1000);
            }
            if (StringUtils.isBlank(rep.getRepOperacionAsociada())){
                ge.addError("repOperacionAsociada", Errores.ERROR_OPERACION_VACIA);
            } else if (rep.getRepOperacionAsociada().length() > 100){
                ge.addError("repOperacionAsociada", Errores.ERROR_LARGO_OPERACION_100);
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
     * @param c1 SgReporte
     * @param c2 SgReporte
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgReporte c1, SgReporte c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getRepCodigo(), c2.getRepCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getRepNombre(), c2.getRepNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getRepHabilitado(), c2.getRepHabilitado());
                respuesta = respuesta && StringUtils.equals(c1.getRepDescripcion(), c2.getRepDescripcion());
                respuesta = respuesta && StringUtils.equals(c1.getRepOperacionAsociada(), c2.getRepOperacionAsociada());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getRepPlantilla(), c2.getRepPlantilla());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
