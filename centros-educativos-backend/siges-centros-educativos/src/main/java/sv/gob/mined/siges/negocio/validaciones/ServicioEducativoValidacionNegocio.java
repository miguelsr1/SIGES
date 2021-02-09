/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.dto.SgRegistroServiciosEducativos;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgServicioEducativo;

public class ServicioEducativoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgServicioEducativo entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getSduGrado() == null) {
                ge.addError("sduGrado", Errores.ERROR_GRADO_VACIO);
            }
            if (entity.getSduSede() == null) {
                ge.addError("sduSede", Errores.ERROR_SEDE_VACIO);
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
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRegistroServiciosEducativos entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getListadoServicios() == null) {
                ge.addError("listadoServicios", Errores.ERROR_DATO_VACIO);
            }
            if (entity.getSedePk() == null) {
                ge.addError("sduSede", Errores.ERROR_SEDE_VACIO);
            }

            if (StringUtils.isBlank(entity.getNumTramite())) {
                ge.addError("numTramite", Errores.ERROR_NUMERO_TRAMITE_VACIO);
            }

            if (entity.getListadoServicios() != null) {
                for (List<String> servicio : entity.getListadoServicios()) {

                    if (servicio.size() < 8) {
                        ge.addError("listadoServicios", Errores.ERROR_TAMANIO_ARRAY_SERVICIO_MENOR_8);
                    }

                    if (StringUtils.isBlank(servicio.get(0))) {
                        ge.addError("nivelPk", Errores.ERROR_NIVEL_VACIO);
                    }
                    if (StringUtils.isBlank(servicio.get(1))) {
                        ge.addError("cicloPk", Errores.ERROR_CICLO_VACIO);
                    }
                    if (StringUtils.isBlank(servicio.get(2))) {
                        ge.addError("modalidadEducativaPk", Errores.ERROR_MODALIDAD_EDUCATIVA_VACIO);
                    }
                    if (StringUtils.isBlank(servicio.get(3))) {
                        ge.addError("modalidadAtencionPk", Errores.ERROR_MODALIDAD_ATENCION_VACIO);
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
