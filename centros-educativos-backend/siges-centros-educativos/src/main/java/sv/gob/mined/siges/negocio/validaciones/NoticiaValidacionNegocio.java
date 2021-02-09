/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgNoticia;

public class NoticiaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgNoticia
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgNoticia entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(entity.getNotCodigo())){
                ge.addError("notCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getNotCodigo().length() > 4){
                ge.addError("notCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            if (StringUtils.isBlank(entity.getNotTitulo())){
                ge.addError("notTitulo", Errores.ERROR_TITULO_VACIO);
            } else if (entity.getNotTitulo().length() > 255){
                ge.addError("notTitulo", Errores.ERROR_TITULO_255);
            }
            if (StringUtils.isBlank(entity.getNotContenido())){
                ge.addError("notContenido", Errores.ERROR_CONTENIDO_VACIO);
            } else if (entity.getNotContenido().length() > 5000){
                ge.addError("notContenido", Errores.ERROR_LARGO_CONTENIDO_5000);
            }
            if (entity.getNotFechaDesde() == null){
                ge.addError("notFechaDesde", Errores.ERROR_FECHA_DESDE_VACIO);
            } 
            if (entity.getNotFechaHasta() == null){
                ge.addError("notFechaHasta", Errores.ERROR_FECHA_HASTA_VACIO);
            }
            if (entity.getNotOrden() == null){
                ge.addError("notOrden", Errores.ERROR_ORDEN_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
