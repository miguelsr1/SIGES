/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudOAE;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSolDeshabilitarPerJur;

public class DesSolicitudPersonaJurValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSolDeshabilitarPerJur entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(StringUtils.isEmpty(entity.getDpjMotivo())){
                ge.addError("dpjMotivo", Errores.ERROR_MOTIVO_VACIO);
            }
            if(entity.getDpjActa() == null){
                ge.addError("dpjArchivo", Errores.ERROR_ARCHIVO_VACIO);
            }
            if(entity.getDpjEstado().equals(EnumEstadoSolicitudOAE.FINALIZADA)){
                if(entity.getDpjAprobar()){
                    if(entity.getDpjFechaAcuerdo() == null){
                        ge.addError("dpjFechaAcuerdo", Errores.ERROR_FECHA_ACUERDO);
                    }
                    if(StringUtils.isBlank(entity.getDpjNumeroAcuerdo())){
                        ge.addError("dpjNumeroAcuerdo", Errores.ERROR_NUMERO_ACUERDO);
                    }
                    if(entity.getDpjAcuerdo() == null){
                        ge.addError("dpjAcuerdo", Errores.ERROR_ARCHIVO_ACUERDO);
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
