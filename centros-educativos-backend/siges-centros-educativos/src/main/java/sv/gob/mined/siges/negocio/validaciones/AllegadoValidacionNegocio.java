/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConfiguracionBean;
import sv.gob.mined.siges.persistencia.entidades.SgAllegado;

public class AllegadoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAllegado
     * @param emailPattern
     * @param configuracionBean
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAllegado entity, String emailPattern, ConfiguracionBean configuracionBean) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (BooleanUtils.isTrue(entity.getAllEsFamiliar())) {
                if (entity.getAllTipoParentesco() == null) {
                    ge.addError("famTipoParentesco", Errores.ERROR_TIPO_PARENTESCO_VACIO);
                }
            }

            try {
                DireccionValidacionNegocio.validar(entity.getAllPersona().getPerDireccion());
            } catch (BusinessException be) {
                ge.getErrores().addAll(be.getErrores());
            }

            if (entity.getAllPersona().getPerPk() != null && entity.getAllPersonaReferenciada() != null && entity.getAllPersonaReferenciada().getPerPk() != null) {
                if (entity.getAllPersona().equals(entity.getAllPersonaReferenciada())) {
                    if (entity.getAllReferente()) {
                        ge.addError("famTipoParentesco", Errores.ERROR_ESTUDIANTE_PROPIO_REFERENTE);
                    } else {
                        ge.addError("famTipoParentesco", Errores.ERROR_ESTUDIANTE_PROPIO_FAMILIAR);
                    }
                }
            }

            try {
                PersonaValidacionNegocio.validar(entity.getAllPersona(), emailPattern);
            } catch (BusinessException be) {
                ge.getErrores().addAll(be.getErrores());
            }

            if (ge.getErrores().size() > 0) {
                throw ge;
            }

            Long maximaEdadPermitidaPersona = Long.parseLong(configuracionBean.obtenerPorCodigo(Constantes.CONFIG_MAXIMA_EDAD_PERMITIDA_ALTA_PERSONA).getConValor());
            if (maximaEdadPermitidaPersona > 0 && entity.getAllPersona().getPerFechaNacimiento() != null && ChronoUnit.YEARS.between(entity.getAllPersona().getPerFechaNacimiento(), LocalDate.now()) > maximaEdadPermitidaPersona) {
                ge.addError(maximaEdadPermitidaPersona.toString(), Errores.ERROR_EDAD_PERSONA_SUPERA_MAXIMO_PERMITIDO, new Object[]{maximaEdadPermitidaPersona});
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
