/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelSustitucionMiembroOAE;

/**
 *
 * @author Sofis Solutions
 */
public class RelSustitucionMiembroOAEValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rsm SgRelSustitucionMiembroOAE
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelSustitucionMiembroOAE rsm) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rsm==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (rsm.getRsmMiembroaSustituirFk()==null) {
                ge.addError("rsmCodigo", Errores.ERROR_MIEMBRO_SUSTITUIR_VACIO);
            }else{
                if(BooleanUtils.isNotTrue(rsm.getRsmMiembroaSustituirFk().getPoaHabilitado())){
                    ge.addError("rsmCodigo", Errores.ERROR_MIEMBRO_A_SUSTITUIR_NO_HABILITADO);
                }               
            }
            if (rsm.getRsmMiembroSustituyenteFk()==null) {
                ge.addError("rsmCodigo", Errores.ERROR_MIEMBRO_SUSTITUYENTE_VACIO);
            }else{
                 if(BooleanUtils.isTrue(rsm.getRsmMiembroSustituyenteFk().getPoaHabilitado())){
                    ge.addError("rsmCodigo", Errores.ERROR_MIEMBRO_SUSTITUYENTE_HABILITADO);
                }
            }
            if(rsm.getRsmMiembroSustituyenteFk()!=null && rsm.getRsmMiembroaSustituirFk()!=null){
                if(BooleanUtils.isTrue(rsm.getRsmMiembroSustituyenteFk().getPoaPrerregistro()) && BooleanUtils.isTrue(rsm.getRsmMiembroaSustituirFk().getPoaPrerregistro())){
                    if(rsm.getRsmMiembroSustituyenteFk().getPoaPersona()!=null && rsm.getRsmMiembroaSustituirFk().getPoaPersona()!=null){
                        if(rsm.getRsmMiembroSustituyenteFk().getPoaPersona().equals(rsm.getRsmMiembroaSustituirFk().getPoaPersona())){
                            ge.addError("rsmCodigo", Errores.ERROR_MIEMBRO_SUSTITUYENTE_NO_PUEDE_SER_EL_MISMO);
                        }
                    }
                }
                if(BooleanUtils.isFalse(rsm.getRsmMiembroSustituyenteFk().getPoaPrerregistro()) && BooleanUtils.isFalse(rsm.getRsmMiembroaSustituirFk().getPoaPrerregistro())){
                    if(rsm.getRsmMiembroSustituyenteFk().getPoaDui()!=null && rsm.getRsmMiembroaSustituirFk().getPoaDui()!=null){
                        if(rsm.getRsmMiembroSustituyenteFk().getPoaDui().equals(rsm.getRsmMiembroaSustituirFk().getPoaDui())){
                            ge.addError("rsmCodigo", Errores.ERROR_MIEMBRO_SUSTITUYENTE_NO_PUEDE_SER_EL_MISMO);
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

}
