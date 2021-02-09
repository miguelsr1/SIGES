/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.enumerados.EnumTipoUnidadResponsable;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleUnidadResp;

/**
 *
 * @author Sofis Solutions
 */
public class RelInmuebleUnidadRespValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param riu SgRelInmuebleUnidadResp
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelInmuebleUnidadResp riu) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (riu==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(riu.getRiuTipoUnidadResp()==null){
                ge.addError("riuCodigo", Errores.ERROR_TIPO_UNIDAD_RESPONSABLE_VACIO);
            }else{
                if(EnumTipoUnidadResponsable.SEDE.equals(riu.getRiuTipoUnidadResp()) && riu.getRiuSedeFk()==null){
                    ge.addError("riuCodigo", Errores.ERROR_SEDE_VACIO);
                }
                if(EnumTipoUnidadResponsable.UNIDAD_ADMINISTRATIVA.equals(riu.getRiuTipoUnidadResp()) && riu.getRiuAfUnidadAdministrativa()==null){
                    ge.addError("riuCodigo", Errores.ERROR_UNIDAD_ADMINISTRATIVA_VACIO);
                }
            }           
            
        
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
