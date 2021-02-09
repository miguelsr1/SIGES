/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumCalculoNotaInstitucional;
import sv.gob.mined.siges.enumerados.EnumTipoFormula;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado; 

public class ComponentePlanGradoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgComponentePlanCpgdo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgComponentePlanGrado entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getCpgComponentePlanEstudio()== null){
                ge.addError("cpgComponentePlanEstudio", Errores.ERROR_COMPONENTE_PLAN_ESTUDIO_VACIO);
            }
            if (entity.getCpgEscalaCalificacion() == null){
                ge.addError("cpgEscalaCalificacion", Errores.ERROR_ESCALA_CALIFICACION_VACIO);
            }
            if (StringUtils.isBlank(entity.getCpgNombrePublicable())){
                ge.addError("cpgNombrePublicable", Errores.ERROR_NOMBRE_VACIO);
            }
            if (entity.getCpgCalculoNotaInstitucional() == null){
                ge.addError("cpgCalculoNotaInstitucional", Errores.ERROR_CALCULO_NOTA_INSTITUCIONAL_VACIO);
            }else if(entity.getCpgCalculoNotaInstitucional().equals(EnumCalculoNotaInstitucional.PROM)){
                if (entity.getCpgFuncionRedondeo() == null){
                    ge.addError("cpgFuncionRedondeo", Errores.ERROR_FUNCION_REDONDEO_VACIO);
                }
            }
            if (entity.getCpgPlanEstudio() == null){
                ge.addError("cpgPlanEstudio", Errores.ERROR_PLAN_ESTUDIO_VACIO);
            }
            if(entity.getCpgFormulaHabilitacionPP()!=null){
                if(EnumTipoFormula.PRUEBA_EXTRAORDINARIA_PARAM.equals(entity.getCpgFormulaHabilitacionPP().getFomTipoFormula())){
                    if(entity.getCpgParametroFormulaPruebaPP()==null){
                        ge.addError("cpgPlanEstudio", Errores.ERROR_PARAMETRO_VACIO);
                    }
                }                
            }
            if(entity.getCpgFormulaHabilitacionPS()!=null){
                if(EnumTipoFormula.PRUEBA_EXTRAORDINARIA_PARAM.equals(entity.getCpgFormulaHabilitacionPS().getFomTipoFormula())){
                    if(entity.getCpgParametroFormulaPruebaPS()==null){
                        ge.addError("cpgPlanEstudio", Errores.ERROR_PARAMETRO_VACIO);
                    }
                }                
            }
            if(entity.getCpgFormulaHabilitacionSP()!=null){
                if(EnumTipoFormula.PRUEBA_EXTRAORDINARIA_PARAM.equals(entity.getCpgFormulaHabilitacionSP().getFomTipoFormula())){
                    if(entity.getCpgParametroFormulaPruebaSP()==null){
                        ge.addError("cpgPlanEstudio", Errores.ERROR_PARAMETRO_VACIO);
                    }
                }                
            }
            if(entity.getCpgFormulaHabilitacionSS()!=null){
                if(EnumTipoFormula.PRUEBA_EXTRAORDINARIA_PARAM.equals(entity.getCpgFormulaHabilitacionSS().getFomTipoFormula())){
                    if(entity.getCpgParametroFormulaPruebaSS()==null){
                        ge.addError("cpgPlanEstudio", Errores.ERROR_PARAMETRO_VACIO);
                    }
                }                
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
