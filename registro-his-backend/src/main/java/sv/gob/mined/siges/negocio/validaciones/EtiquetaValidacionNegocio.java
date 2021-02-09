/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRhEtiqueta;
import sv.gob.mined.siges.utils.ValidationUtils;

/**
 *
 * @author Sofis Solutions
 */
public class EtiquetaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgRhEtiqueta
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRhEtiqueta entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getEtiPagina() == null) {
                ge.addError(Errores.ERROR_PAGINA_VACIO);
            } else {
                try {
                    PaginaValidacionNegocio.validarPaginaDeEtiqueta(entity.getEtiPagina());
                } catch (BusinessException be) {
                    ge.getErrores().addAll(be.getErrores());
                }

            }
            
            if(!StringUtils.isBlank(entity.getEtiDui()) && !ValidationUtils.validarDUI(entity.getEtiDui())){
                    ge.addError("perDui", Errores.ERROR_DUI_INCORRECTO);
                }  

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
