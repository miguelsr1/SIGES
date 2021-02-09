/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgNotificacion;

public class NotificacionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgNotificacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgNotificacion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getNfsTipo()==null){
                ge.addError("nfsTipo", Errores.ERROR_TIPO_NOTIFICACION_VACIO);
            }else{
                switch(entity.getNfsTipo()){
                    case CENTRO:
                        if(entity.getNfsSede()==null){
                            ge.addError("nfsSede", Errores.ERROR_SEDE_VACIO);
                        }
                        break;
                    case ESTUDIANTE:
                        if(entity.getNfsEstudiante()==null){
                            ge.addError("nfsEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
                        }
                        break;
                    case SECCION:
                        if(entity.getNfsSeccion()==null){
                            ge.addError("nfsSeccion", Errores.ERROR_SECCION_VACIO);
                        }
                        break;
                }
            }
            
            if (StringUtils.isBlank(entity.getNfsNotificacion())){
                ge.addError("nfsNotificacion", Errores.ERROR_NOTIFICACION_VACIO);
            } else if (entity.getNfsNotificacion().length() > 400){
                ge.addError("nfsNotificacion", Errores.ERROR_LARGO_NOTIFICACION_400);
            }
            if (StringUtils.isBlank(entity.getNfsTextoBreve())){
                ge.addError("nfsTextoBreve", Errores.ERROR_TEXTO_BREVE_VACIO);
            } else if (entity.getNfsTextoBreve().length() > 5000){
                ge.addError("nfsTextoBreve", Errores.ERROR_LARGO_TEXTO_BREVE_100);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
