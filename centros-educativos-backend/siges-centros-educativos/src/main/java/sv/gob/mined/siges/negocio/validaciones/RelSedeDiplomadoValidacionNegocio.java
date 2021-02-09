/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.dto.SgRegistroDiplomadosSede;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelSedeDiplomado;

/**
 *
 * @author Sofis Solutions
 */
public class RelSedeDiplomadoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rsd SgRelSedeDiplomado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelSedeDiplomado rsd) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rsd==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (rsd.getRsdSedeFk()==null) {
                ge.addError("rsdSede", Errores.ERROR_SEDE_VACIO);
            } 
            if (rsd.getRsdDiplomadoFk()==null) {
                ge.addError("rsdDiplomado", Errores.ERROR_DIPLOMADO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rsd SgRelSedeDiplomado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRegistroDiplomadosSede rsd) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rsd==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (rsd.getSedePk() == null) {
                ge.addError("sedePk", Errores.ERROR_SEDE_VACIO);
            } 
            if (rsd.getDiplomadosPk() == null || rsd.getDiplomadosPk().isEmpty()) {
                ge.addError("diplomadosPk", Errores.ERROR_DIPLOMADO_VACIO);
            }
            if (StringUtils.isBlank(rsd.getNumTramite())) {
                ge.addError("numTramite", Errores.ERROR_NUMERO_TRAMITE_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    

  
}
