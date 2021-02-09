/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCargoOAE;

/**
 *
 * @author Sofis Solutions
 */
public class CargoOAEValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param coa SgCargoOAE
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCargoOAE coa) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (coa==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(coa.getCoaCodigo())) {
                ge.addError("coaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (coa.getCoaCodigo().length() > 45){
                ge.addError("coaCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(coa.getCoaNombre())) {
                ge.addError("coaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (coa.getCoaNombre().length() > 255){
                ge.addError("coaNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if (StringUtils.isBlank(coa.getCoaNombreMasculino())) {
                ge.addError("coaNombreMasculino", Errores.ERROR_NOMBRE_VACIO);
            } else if (coa.getCoaNombreMasculino().length() > 255){
                ge.addError("coaNombreMasculino", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(coa.getCoaOrden()==null){
                ge.addError("coaOrden", Errores.ERROR_ORDEN_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
