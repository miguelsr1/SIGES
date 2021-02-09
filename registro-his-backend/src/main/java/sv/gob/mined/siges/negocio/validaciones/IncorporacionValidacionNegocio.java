/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import java.time.Year;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgIncorporacion;
import sv.gob.mined.siges.utils.ValidationUtils;

/**
 *
 * @author Sofis Solutions
 */
public class IncorporacionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param inc SgIncorporacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgIncorporacion inc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (inc == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (StringUtils.isBlank(inc.getIncDui())
                    && StringUtils.isBlank(inc.getIncCarneResidente())
                    && StringUtils.isBlank(inc.getIncPasaporte())) {
                ge.addError(Errores.ERROR_IDENTIFICACIONES_VACIO);
            }else{
                if(!StringUtils.isBlank(inc.getIncDui()) && !ValidationUtils.validarDUI(inc.getIncDui())){
                    ge.addError("perDui", Errores.ERROR_DUI_INCORRECTO);
                }            
            }

            if (StringUtils.isBlank(inc.getIncPrimerNombre())) {
                ge.addError("incPrimerNombre", Errores.ERROR_PRIMER_NOMBRE_VACIO);
            } else if (inc.getIncPrimerNombre().length() > 100) {
                ge.addError("incPrimerNombre", Errores.ERROR_PRIMER_NOMBRE_100);
            }

            if (StringUtils.isBlank(inc.getIncPrimerApellido())) {
                ge.addError("incPrimerApellido", Errores.ERROR_PRIMER_APELLIDO_VACIO);
            } else if (inc.getIncPrimerApellido().length() > 100) {
                ge.addError("incPrimerApellido", Errores.ERROR_PRIMER_APELLIDO_100);
            }

            if (!StringUtils.isBlank(inc.getIncSegundoNombre()) && inc.getIncSegundoNombre().length() > 100) {
                ge.addError("incSegundoNombre", Errores.ERROR_SEGUNDO_NOMBRE_100);
            }

            if (!StringUtils.isBlank(inc.getIncTercerNombre()) && inc.getIncTercerNombre().length() > 100) {
                ge.addError("incTercerNombre", Errores.ERROR_TERCER_NOMBRE_100);
            }

            if (!StringUtils.isBlank(inc.getIncSegundoApellido()) && inc.getIncSegundoApellido().length() > 100) {
                ge.addError("incSegundoApellido", Errores.ERROR_SEGUNDO_APELLIDO_100);
            }

            if (!StringUtils.isBlank(inc.getIncTercerApellido()) && inc.getIncTercerApellido().length() > 100) {
                ge.addError("incTercerApellido", Errores.ERROR_TERCER_APELLIDO_100);
            }

            if (!StringUtils.isBlank(inc.getIncPasaporte()) && inc.getIncPasaportePaisEmisor() == null) {
                ge.addError("incPasaportePaisEmisor", Errores.ERROR_PAIS_EMISOR_VACIO);
            }

            if (inc.getIncFechaNacimiento() == null) {
                ge.addError("incFechaNacimiento", Errores.ERROR_FECHA_NACIMIENTO_VACIO);
            } else if (inc.getIncFechaNacimiento().compareTo(LocalDate.now()) > 0) {
                ge.addError("incFechaNacimiento", Errores.ERROR_FECHA_NACIMIENTO_MAYOR_ACTUAL);
            }
            if (inc.getIncNacionalidad() == null) {
                ge.addError("incNacionalidad", Errores.ERROR_NACIONALIDAD_VACIA);
            }
            if (inc.getIncSexo() == null) {
                ge.addError("incSexo", Errores.ERROR_SEXO_VACIO);
            }
            if (inc.getIncEstadoCivil() == null) {
                ge.addError("incEstadoCivil", Errores.ERROR_ESTADO_CIVIL_VACIO);
            }
            
            if (StringUtils.isBlank(inc.getIncNombreSede())){
                 ge.addError("incNombreSede", Errores.ERROR_NOMBRE_SEDE_VACIO);
            } else if (inc.getIncNombreSede().length() > 500) {
                ge.addError("incNombreSede", Errores.ERROR_LARGO_NOMBRE_SEDE_500);
            }
            
            if (StringUtils.isBlank(inc.getIncUltimoGradoEstudio())){
                 ge.addError("incUltimoGradoEstudio", Errores.ERROR_GRADO_ESTUDIO_VACIO);
            } else if (inc.getIncUltimoGradoEstudio().length() > 250) {
                ge.addError("incUltimoGradoEstudio", Errores.ERROR_LARGO_GRADO_ESTUDIO_250);
            }
            
            if (StringUtils.isBlank(inc.getIncCiudad())){
                 ge.addError("incCiudad", Errores.ERROR_CIUDAD_VACIA);
            } else if (inc.getIncCiudad().length() > 250) {
                ge.addError("incCiudad", Errores.ERROR_LARGO_CIUDAD_250);
            }
            
            if (inc.getIncAnioEstudio() == null){
                ge.addError("incAnioEstudio", Errores.ERROR_ANIO_ESTUDIO_VACIO);
            }else if (inc.getIncAnioEstudio() > Year.now().getValue()){
                ge.addError("incAnioEstudio", Errores.ERROR_ANIO_ESTUDIO_MAYOR_ACTUAL);
            }
            
            if (inc.getIncFechaAprobacion() == null){
                ge.addError("incFechaAprobacion", Errores.ERROR_FECHA_APROBACION_VACIA);
            } else if (inc.getIncFechaAprobacion().compareTo(LocalDate.now()) > 0) {
                ge.addError("incFechaAprobacion", Errores.ERROR_FECHA_APROBACION_MAYOR_ACTUAL);
            }
            
            if (StringUtils.isBlank(inc.getIncNumeroTramite())){
                ge.addError("incNumeroTramite", Errores.ERROR_NUMERO_TRAMITE_VACIO);
            } else if (inc.getIncNumeroTramite().length() > 250) {
                ge.addError("incNumeroTramite", Errores.ERROR_LARGO_NUMERO_TRAMITE_250);
            }
            
            if (StringUtils.isBlank(inc.getIncNumeroResolucion())){
                ge.addError("incNumeroResolucion", Errores.ERROR_NUMERO_RESOLUCION_VACIO);
            } else if (inc.getIncNumeroResolucion().length() > 250) {
                ge.addError("incNumeroResolucion", Errores.ERROR_LARGO_NUMERO_RESOLUCION_250);
            }
            
            

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
