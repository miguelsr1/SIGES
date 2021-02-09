/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.TipoPersonalSedeEducativa;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConfiguracionBean;
import sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativa;

public class PersonalSedeEducativoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgPersonalSedeEducativa
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPersonalSedeEducativa entity, String emailPattern, ConfiguracionBean configuracionBean, Set<TipoPersonalSedeEducativa> tiposDelPersonal) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            //Validar las identificaciones obligatorias
            if (entity.getPsePk() == null) {

                if (BooleanUtils.isTrue(entity.getPsePuedeAplicarPlaza())) {

                    if (StringUtils.isBlank(entity.getPsePersona().getPerNit())) {
                        ge.addError(Errores.ERROR_NIT_VACIO);
                    }

                    if (!entity.getPsePersona().getPerIngresoIdentParaNipPersonalSedeAlfabetizador()) {
                        ge.addError(Errores.ERROR_IDENTIFICACIONES_OBLIGATORIA_NIP_ALFABETIZADOR_VACIA);
                    }
                    
                } else {

                    if (entity.getPseCrearConDatoContratacion() == null) {
                        ge.addError(Errores.ERROR_DATO_CONTRATACION_VACIO);
                        throw ge;
                    }

                    TipoPersonalSedeEducativa tipo = entity.getPseCrearConDatoContratacion().getDcoTipo();

                    if (!TipoPersonalSedeEducativa.ALF.equals(tipo)) {
                        if (StringUtils.isBlank(entity.getPsePersona().getPerNit())) {
                            ge.addError(Errores.ERROR_NIT_VACIO);
                        }
                    }

                    //Validar las identificaciones obligatorias para generar el NIP
                    if (!TipoPersonalSedeEducativa.ALF.equals(tipo)) {
                        if (!entity.getPsePersona().getPerIngresoIdentParaNipPersonalSede()) {
                            ge.addError(Errores.ERROR_IDENTIFICACIONES_OBLIGATORIA_NIP_VACIA);
                        }
                    } else {
                        if (!entity.getPsePersona().getPerIngresoIdentParaNipPersonalSedeAlfabetizador()) {
                            ge.addError(Errores.ERROR_IDENTIFICACIONES_OBLIGATORIA_NIP_ALFABETIZADOR_VACIA);
                        }
                    }

                }
            } else {
            
                if (BooleanUtils.isTrue(entity.getPsePuedeAplicarPlaza())) {

                    if (StringUtils.isBlank(entity.getPsePersona().getPerNit())) {
                        ge.addError(Errores.ERROR_NIT_VACIO);
                    }

                    if (!entity.getPsePersona().getPerIngresoIdentParaNipPersonalSedeAlfabetizador()) {
                        ge.addError(Errores.ERROR_IDENTIFICACIONES_OBLIGATORIA_NIP_ALFABETIZADOR_VACIA);
                    }
                    
                } else {


                    if (tiposDelPersonal.contains(TipoPersonalSedeEducativa.ADM) || tiposDelPersonal.contains(TipoPersonalSedeEducativa.ATPI)
                            || tiposDelPersonal.contains(TipoPersonalSedeEducativa.DOC) || tiposDelPersonal.contains(TipoPersonalSedeEducativa.DOF)){
                    
                        if (StringUtils.isBlank(entity.getPsePersona().getPerNit())) {
                            ge.addError(Errores.ERROR_NIT_VACIO);
                        }
                    
                        if (!entity.getPsePersona().getPerIngresoIdentParaNipPersonalSede()) {
                            ge.addError(Errores.ERROR_IDENTIFICACIONES_OBLIGATORIA_NIP_VACIA);
                        }
                        
                    } else {
                        
                        if (!entity.getPsePersona().getPerIngresoIdentParaNipPersonalSedeAlfabetizador()) {
                            ge.addError(Errores.ERROR_IDENTIFICACIONES_OBLIGATORIA_NIP_ALFABETIZADOR_VACIA);
                        }
                    
                    }
                }
                    
            }

            try {
                PersonaValidacionNegocio.validar(entity.getPsePersona(), emailPattern);
            } catch (BusinessException be) {
                ge.getErrores().addAll(be.getErrores());
            }

            try {
                DireccionValidacionNegocio.validar(entity.getPsePersona().getPerDireccion());
            } catch (BusinessException be) {
                ge.getErrores().addAll(be.getErrores());
            }

            if (ge.getErrores().size() > 0) {
                throw ge;
            }

            Long maximaEdadPermitidaPersonal = Long.parseLong(configuracionBean.obtenerPorCodigo(Constantes.CONFIG_MAXIMA_EDAD_PERMITIDA_ALTA_PERSONA).getConValor());
            if (maximaEdadPermitidaPersonal > 0 && entity.getPsePersona().getPerFechaNacimiento() != null && ChronoUnit.YEARS.between(entity.getPsePersona().getPerFechaNacimiento(), LocalDate.now()) > maximaEdadPermitidaPersonal) {
                ge.addError(maximaEdadPermitidaPersonal.toString(), Errores.ERROR_EDAD_PERSONA_SUPERA_MAXIMO_PERMITIDO, new Object[]{maximaEdadPermitidaPersonal});
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
