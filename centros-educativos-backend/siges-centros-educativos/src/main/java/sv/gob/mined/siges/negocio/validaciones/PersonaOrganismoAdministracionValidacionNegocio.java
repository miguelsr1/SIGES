/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.utils.ValidationUtils;

/**
 *
 * @author Sofis Solutions
 */
public class PersonaOrganismoAdministracionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param poa SgPersonaOrganismoAdministracion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPersonaOrganismoAdministracion poa) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (poa == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (BooleanUtils.isTrue(poa.getPoaPrerregistro())) {
                if (poa.getPoaPersona() == null || poa.getPoaPersona().getPerPk() == null) {
                    ge.addError("poaPersona", Errores.ERROR_PERSONA_VACIA);
                }
            } else {
                if (StringUtils.isBlank(poa.getPoaNombre())) {
                    ge.addError("poaNombre", Errores.ERROR_NOMBRE_VACIO);
                } else if (poa.getPoaNombre().length() > 255) {
                    ge.addError("poaNombre", Errores.ERROR_LARGO_NOMBRE_255);
                }

                if (StringUtils.isBlank(poa.getPoaDui())) {
                    ge.addError("poaDui", Errores.ERROR_DUI_VACIO);
                }
                if (poa.getPoaDui() != null && !ValidationUtils.validarDUI(poa.getPoaDui())) {
                    ge.addError("poaDui", Errores.ERROR_DUI_INCORRECTO);
                }
                if (StringUtils.isBlank(poa.getPoaOcupacion())) {
                    ge.addError("poaOcupacion", Errores.ERROR_OCUPACION_VACIO);
                }
                if (StringUtils.isBlank(poa.getPoaDomicilio())) {
                    ge.addError("poaDomicilio", Errores.ERROR_DOMICILIO_VACIO);
                }
                if (poa.getPoaSexo() == null) {
                    ge.addError("poaSexo", Errores.ERROR_SEXO_VACIO);
                }
                
            }
            if(poa.getPoaFechaExpedicionDUI()!=null ){
                    if(poa.getPoaFechaExpedicionDUI().isAfter(LocalDate.now())){
                        ge.addError("poaSexo", Errores.ERROR_FECHA_FECHA_EXPEDICION_DUI_MAYOR_HOY);
                    }
                    
                }
            if (poa.getPoaCargo() == null) {
                ge.addError("poaNombre", Errores.ERROR_CARGO_OAE_VACIO);
            }
            if (poa.getPoaDesde() != null && poa.getPoaHasta() != null && poa.getPoaDesde().isAfter(poa.getPoaHasta())) {
                ge.addError("poaDesde", Errores.ERROR_FECHA_DESDE_MAYOR_HASTA);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
