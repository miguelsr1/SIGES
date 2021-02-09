/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSustitucionMiembroOAE;

/**
 *
 * @author Sofis Solutions
 */
public class SustitucionMiembroOAEValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param smo SgSustitucionMiembroOAE
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSustitucionMiembroOAE smo) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (smo==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (smo.getSmoOaeFk()==null) {
                ge.addError(Errores.ERROR_OAE_VACIO);
            } 
            if(smo.getSmoEstado()==null){
                ge.addError(Errores.ERROR_ESTADO_VACIO);
            }     
            if(smo.getSmoRelSustitucionMiembroOAE()==null || smo.getSmoRelSustitucionMiembroOAE().isEmpty()){
                ge.addError(Errores.ERROR_MIEMBROS_A_SUSTITUIR_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    public static boolean validarRechazo(SgSustitucionMiembroOAE smo) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (smo==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(smo.getSmoMotivoRechazo())) {
                ge.addError(Errores.ERROR_MOTIVO_RECHAZO_VACIO);
            } 
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    public static boolean validarAprobacion(SgSustitucionMiembroOAE smo) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (smo==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(smo.getSmoNumeroResolucion())) {
                ge.addError(Errores.ERROR_NUMERO_RESOLUCION_VACIO);
            } 
            if(smo.getSmoFechaResolucion()==null){
                ge.addError(Errores.ERROR_FECHA_RESOLUCION_VACIO);
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }


}
