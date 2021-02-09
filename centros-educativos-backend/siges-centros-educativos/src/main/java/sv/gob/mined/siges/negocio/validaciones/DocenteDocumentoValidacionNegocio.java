/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDocenteDocumento;
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

/**
 *
 * @author Sofis Solutions
 */
public class DocenteDocumentoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDocenteDocumento entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getDdoPersonal()==null){
                ge.addError(Errores.ERROR_PERSONAL_VACIO);
            }
            if (entity.getDdoTipoDocumento()==null){
                ge.addError(Errores.ERROR_TIPO_DOCUMENTO_VACIO);
            }
            if (entity.getDdoArchivo()==null){
                ge.addError(Errores.ERROR_ARCHIVO_VACIO);
            }
            if (!StringUtils.isBlank(entity.getDdoDescripcion()) && entity.getDdoDescripcion().length()>255){
                ge.addError(Errores.ERROR_LARGO_DESCRIPCION_255);
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
     * @param c1 SgDocenteDocumento
     * @param c2 SgDocenteDocumento
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgDocenteDocumento c1, SgDocenteDocumento c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = ObjectUtils.sonIguales(c1.getDdoArchivo(), c2.getDdoArchivo());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getDdoTipoDocumento(), c2.getDdoTipoDocumento());  
                respuesta = respuesta && StringUtils.equals(c1.getDdoDescripcion(), c2.getDdoDescripcion());  
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getDdoValidado(), c2.getDdoValidado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    


}
