/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConfiguracionBean;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;

public class EstudianteValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgEstudiante
     * @param emailPattern String
     * @param configuracionBean ConfiguracionBean
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    
   
    public static boolean validar(SgEstudiante entity, String emailPattern, ConfiguracionBean configuracionBean) throws BusinessException {

        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (!StringUtils.isBlank(entity.getEstFacRiesgo())) {
                if (entity.getEstFacRiesgo().length() > 255) {
                    ge.addError("opcCodigo", Errores.ERROR_LARGO_FACTOR_RIESGO_255);
                }
            }

            if (entity.getEstPersona().getPerNacionalidad() != null && StringUtils.equalsIgnoreCase(entity.getEstPersona().getPerNacionalidad().getNacCodigo(), Constantes.CODIGO_NACIONALIDAD_EL_SALVADOR)
                    && (BooleanUtils.isTrue(entity.getEstPersona().getPerPartidaNacimientoPresenta()) || entity.getEstPersona().getPerPartidaNacimiento() != null)) {
                if (entity.getEstPersona().getPerPartidaNacimiento() == null) {
                    ge.addError("perPartidaNacimiento", Errores.ERROR_PARTIDA_NACIMIENTO_NUMERO_VACIO);
                }
                if (StringUtils.isBlank(entity.getEstPersona().getPerPartidaNacimientoFolio())) {
                    ge.addError("perPartidaNacimientoFolio", Errores.ERROR_PARTIDA_NACIMIENTO_FOLIO_VACIO);
                }

                if (entity.getEstPersona().getPerPartidaNacimientoAnio() == null) {
                    ge.addError("perPartidaNacimientoAnio", Errores.ERROR_PARTIDA_NACIMIENTO_ANIO_VACIO);
                } else if (entity.getEstPersona().getPerPartidaNacimientoAnio() > LocalDate.now().getYear()) {
                    ge.addError("perPartidaNacimientoAnio", Errores.ERROR_PARTIDA_NACIMIENTO_ANIO_MAYOR_ACTUAL);
                }
                if (!StringUtils.isBlank(entity.getEstPersona().getPerPartidaNacimientoComplemento())) {
                    if (entity.getEstPersona().getPerPartidaNacimientoComplemento().length() > 10) {
                        ge.addError("perPartidaNacimientoComplemento", Errores.ERROR_PARTIDA_NACIMIENTO_COMPLEMENTO_10);
                    }
                }
                if (entity.getEstPersona().getPerPartidaDepartamento() == null) {
                    ge.addError("perPartidaDepartamento", Errores.ERROR_DEPARTAMENTO_PARTIDA_VACIO);
                }

                if (entity.getEstPersona().getPerPartidaMunicipio() == null) {
                    ge.addError("perPartidaMunicipio", Errores.ERROR_MUNICIPIO_PARTIDA_VACIO);
                }

            }

            try {
                EstudianteValidacionNegocio.validarServicioSocial(entity);
            } catch (BusinessException be) {
                ge.getErrores().addAll(be.getErrores());
            }

            try {
                DireccionValidacionNegocio.validar(entity.getEstPersona().getPerDireccion());
            } catch (BusinessException be) {
                ge.getErrores().addAll(be.getErrores());
            }
            try {
                PersonaValidacionNegocio.validar(entity.getEstPersona(), emailPattern);
            } catch (BusinessException be) {
                ge.getErrores().addAll(be.getErrores());
            }
            
            if (BooleanUtils.isTrue(entity.getEstPersona().getPerTrabaja())) {
                if (entity.getEstPersona().getPerTipoTrabajo() == null) {
                    ge.addError("perTipoTrabajo", Errores.ERROR_TIPO_TRABAJO_VACIO);
                }
                if (!StringUtils.isBlank(entity.getEstPersona().getPerLugarTrabajo()) && entity.getEstPersona().getPerLugarTrabajo().length() > 255) {
                    ge.addError("perLugarTrabajo", Errores.ERROR_LUGAR_TRABAJO_255);
                }
                if (!StringUtils.isBlank(entity.getEstPersona().getPerJornadaLaboral()) && entity.getEstPersona().getPerJornadaLaboral().length() > 20) {
                    ge.addError("perJornadaLaboral", Errores.ERROR_JORNADA_LABORAL_20);
                }
                if (entity.getEstPersona().getPerSalario() == null) {
                    ge.addError("perSalario", Errores.ERROR_SALARIO_VACIO);
                }
            }
          if(BooleanUtils.isNotTrue(entity.getEstEsDeTramite())){
            if(entity.getEstPersona().getPerTrabaja() == null){
                ge.addError("perTrabaja", Errores.ERROR_TRABAJA_VACIO);
            }
            
            if(entity.getEstDependenciaEconomica() == null){
                ge.addError("estDependenciaEconomica", Errores.ERROR_DEPENDENCIA_ECONOMICA_VACIO);
            }
            
            if (BooleanUtils.isTrue(entity.getEstPersona().getPerTieneHijos())) {
                if (entity.getEstPersona().getPerCantidadHijos() == null) {
                    ge.addError("perCantidadHijos", Errores.ERROR_CANTIDAD_HIJOS_NULO);
                }
            }          
            if(entity.getEstPersona().getPerRetornada() == null){
                ge.addError("perRetornada", Errores.ERROR_RETORNADA_VACIO);
            }
         }   
            
            if (ge.getErrores().size() > 0) {
                throw ge;
            }

            Long maximaEdadPermitidaEstudiante = Long.parseLong(configuracionBean.obtenerPorCodigo(Constantes.CONFIG_MAXIMA_EDAD_PERMITIDA_ALTA_ESTUDIANTES).getConValor());
            if (maximaEdadPermitidaEstudiante > 0 && entity.getEstPersona().getPerFechaNacimiento() != null && ChronoUnit.YEARS.between(entity.getEstPersona().getPerFechaNacimiento(), LocalDate.now()) > maximaEdadPermitidaEstudiante) {

                ge.addError(maximaEdadPermitidaEstudiante.toString(), Errores.ERROR_EDAD_ESTUDIANTE_SUPERA_MAXIMO_PERMITIDO, new String[]{maximaEdadPermitidaEstudiante.toString()});
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    public static boolean validarServicioSocial(SgEstudiante entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (BooleanUtils.isTrue(entity.getEstRealizoServicioSocial())) {
            if (entity.getEstCantidadHorasServicioSocial() == null) {
                ge.addError("estCantidadHorasServicioSocial", Errores.ERROR_CANTIDAD_HORAS_SERVICIO_SOCIAL_VACIO);
            } else {
                if (entity.getEstCantidadHorasServicioSocial() <= 0) {
                    ge.addError("estCantidadHorasServicioSocial", Errores.ERROR_CANTIDAD_HORAS_SERVICIO_SOCIAL_MENOR_CERO);
                } else if (entity.getEstCantidadHorasServicioSocial() > 200) {
                    ge.addError("estCantidadHorasServicioSocial", Errores.ERROR_CANTIDAD_HORAS_SERVICIO_SOCIAL_MAXIMO);
                }
            }
            if (entity.getEstFechaServicioSocial() == null) {
                ge.addError("estFechaServicioSocial", Errores.ERROR_FECHA_SERVICIO_SOCIAL_VACIO);
            } else {
                if (entity.getEstFechaServicioSocial().compareTo(LocalDate.now()) > 0) {
                    ge.addError("estFechaServicioSocial", Errores.ERROR_FECHA_SERVICIO_SOCIAL_MAYOR_HOY);
                }
            }
            if (StringUtils.isBlank(entity.getEstDescripcionServicioSocial())) {
                ge.addError("estDescripcionServicioSocial", Errores.ERROR_DESCRIPCION_SERVICIO_SOCIAL_VACIO);
            } else {
                if (entity.getEstDescripcionServicioSocial().length() > 1000) {
                    ge.addError("estDescripcionServicioSocial", Errores.ERROR_DESCRIPCION_SERVICIO_SOCIAL_MAYOR_MIL);
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
