/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDefinicionTitulo;

/**
 *
 * @author Sofis Solutions
 */
public class DefinicionTituloValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param dti SgDefinicionTitulo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDefinicionTitulo dti) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (dti==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(dti.getDtiCodigo())) {
                ge.addError("dtiCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (dti.getDtiCodigo().length() > 45){
                ge.addError("dtiCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(dti.getDtiNombre())) {
                ge.addError("dtiNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (dti.getDtiNombre().length() > 255){
                ge.addError("dtiNombre",Errores.ERROR_LARGO_NOMBRE_255);
            }
            if(dti.getDtiPlantilla()==null){
                ge.addError("dtiPlantilla", Errores.ERROR_PLANTILLA_VACIO);
            }
            if(dti.getDtiFechaDesde()==null){
                ge.addError("dtiFechaDesde", Errores.ERROR_FECHA_DESDE_VACIO);
            }
            if(dti.getDtiFechaHasta()==null){
                ge.addError("dtiFechaHasta", Errores.ERROR_FECHA_HASTA_VACIO);
            }
            if((dti.getDtiFechaHasta()!=null)&&(dti.getDtiFechaDesde()!=null)){
                if(dti.getDtiFechaDesde().isAfter(dti.getDtiFechaHasta())){
                    ge.addError("ditDesde", Errores.ERROR_FECHA_DESDE_MAYOR_FECHA_HASTA);
                }
            }
            if (StringUtils.isBlank(dti.getDtiNombreCertificado())) {
                ge.addError("dtiNombre", Errores.ERROR_NOMBRE_CERTIFICADO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    


  
   
}
