/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAsociacion;
import sv.gob.mined.siges.utils.ValidationUtils;

/**
 *
 * @author Sofis Solutions
 */
public class AsociacionValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param aso SgAsociacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAsociacion aso) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (aso==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(aso.getAsoCodigo())) {
                ge.addError("asoCodigo", (Errores.ERROR_CODIGO_VACIO));
            } else if (aso.getAsoCodigo().length() > 4){
                ge.addError("asoCodigo", (Errores.ERROR_LARGO_CODIGO_4));
            }
            if (StringUtils.isBlank(aso.getAsoNombre())) {
                ge.addError("asoNombre", (Errores.ERROR_NOMBRE_VACIO));
            } else if (aso.getAsoNombre().length() > 100){
                ge.addError("asoNombre", (Errores.ERROR_LARGO_NOMBRE_100));
            }
            if(aso.getAsoTipoAsociacion()==null){
                ge.addError("asoTipoAsociacion", (Errores.ERROR_TIPO_ASOCIACION_VACIO));
            }
            if (StringUtils.isBlank(aso.getAsoNombreRepresentanteLegal())) {
                ge.addError("asoNombreRepresentanteLegal", (Errores.ERROR_NOMBRE_LEGAL_VACIO));
            } else if (aso.getAsoNombreRepresentanteLegal().length() > 100){
                ge.addError("asoNombreRepresentanteLegal", (Errores.ERROR_LARGO_NOMBRE_LEGAL_100));
            }
            if(aso.getAsoDireccionFk()!=null){
                if(aso.getAsoDireccionFk().getDirZona()==null){
                    ge.addError("asoDireccionFk.dirZona", (Errores.ERROR_ZONA_VACIO));
                }
                if(aso.getAsoDireccionFk().getDirDepartamento()==null){
                    ge.addError("asoDireccionFk.dirDepartamento", (Errores.ERROR_DEPARTAMENTO_VACIO));
                }
                if(aso.getAsoDireccionFk().getDirMunicipio()==null){
                    ge.addError("asoDireccionFk.dirMunicipio", (Errores.ERROR_MUNICIPIO_VACIO));
                }
                if(StringUtils.isBlank(aso.getAsoDireccionFk().getDirDireccion())){
                    ge.addError("asoDireccionFk.dirDireccion", (Errores.ERROR_DIRECCION_VACIO));   
                }else if(aso.getAsoDireccionFk().getDirDireccion().length()>255){
                    ge.addError("asoDireccionFk.dirDireccion", (Errores.ERROR_LARGO_DIRECCION_255));   
                }
            }else{
                ge.addError("asoDireccionFk", (Errores.ERROR_DIRECCION_VACIO));
            }
            
            if(aso.getAsoAnioFundacion() != null) {
                if(aso.getAsoAnioFundacion().compareTo(0)<= 0) {
                    ge.addError("asoAnioFundacion", (Errores.ERROR_ANIO_FUNDACION_MENOR_IGUAL_CERO));
                } else if(aso.getAsoAnioFundacion().compareTo(0)>= LocalDate.now().getYear()) {
                    ge.addError("asoAnioFundacion", (Errores.ERROR_ANIO_FUNDACION_MENOR_IGUAL_CERO));
                }
            }
            if(aso.getAsoResponsableInstitucional() != null && StringUtils.isNotBlank(aso.getAsoResponsableInstitucional()) &&  aso.getAsoResponsableInstitucional().length() > 100){
                ge.addError("asoResponsableInstitucional", (Errores.ERROR_LARGO_RESPONSABLE_INSTITUCIONAL_100));
            }
            
             
            if(aso.getAsoCorreo() != null && StringUtils.isNotBlank(aso.getAsoCorreo())) {
                if (!ValidationUtils.validarEmail(aso.getAsoCorreo(), null)) {
                    ge.addError("asoCorreo", (Errores.ERROR_CORREO_INVALIDO));
                }
                if(aso.getAsoCorreo().length() > 50) {
                    ge.addError("asoCorreoTamanio", (Errores.ERROR_LARGO_CORREO_50));
                }
            }
            if(aso.getAsoCorreoAlternativo() != null && StringUtils.isNotBlank(aso.getAsoCorreoAlternativo())) {
                if (!ValidationUtils.validarEmail(aso.getAsoCorreoAlternativo(), null)) {
                    ge.addError("asoCorreo", (Errores.ERROR_CORREO_ALTERNATIVO_INVALIDO));
                }
                if(aso.getAsoCorreoAlternativo().length() > 50) {
                    ge.addError("asoCorreoAlternativoTamanio", (Errores.ERROR_LARGO_CORREO_ALTERNATIVO_50));
                }
            }
            
            if(aso.getAsoCorreo() != null && StringUtils.isNotBlank(aso.getAsoCorreo()) && aso.getAsoCorreoAlternativo() != null 
                                                            && StringUtils.isNotBlank(aso.getAsoCorreoAlternativo())) {
                if(aso.getAsoCorreo().trim().equals(aso.getAsoCorreoAlternativo().trim())) {
                    ge.addError("asocorreoiguales", (Errores.ERROR_CORREO_CORREO_ALTERNATIVO_IGUALES));
                }
            }
            
            
            if(aso.getAsoNombreCoordiandor() != null && StringUtils.isNotBlank(aso.getAsoNombreCoordiandor()) &&  aso.getAsoNombreCoordiandor().length() > 100){
                ge.addError("asoNombreCoordiandor", (Errores.ERROR_LARGO_NOMBRE_COORDINADOR_GENERAL_100));
            }
            if(aso.getAsoTelefonoCoordiandor() != null && StringUtils.isNotBlank(aso.getAsoTelefonoCoordiandor())) {
                if(aso.getAsoTelefonoCoordiandor().length() > 20) {
                    ge.addError("asoTelefonoCoordiandor", (Errores.ERROR_LARGO_TELEFONO_COORDINADOR_20));
                }
            }
            
            if(aso.getAsoCorreoCoordiandor() != null && StringUtils.isNotBlank(aso.getAsoCorreoCoordiandor())) {
                if (!ValidationUtils.validarEmail(aso.getAsoCorreoCoordiandor(), null)) {
                    ge.addError("asoCorreoCoordiandor", (Errores.ERROR_CORREO_COORDINADOR_INVALIDO));
                }
                if(aso.getAsoCorreoAlternativo().length() > 50) {
                    ge.addError("asoCorreoCoordiandorTamanio", (Errores.ERROR_LARGO_CORREO_COORDINADOR_50));
                }
            }
            
            if(aso.getAsoNombreResponsableAdministrativo() != null && StringUtils.isNotBlank(aso.getAsoNombreResponsableAdministrativo()) &&  aso.getAsoNombreResponsableAdministrativo().length() > 100){
                ge.addError("asoNombreResponsableAdministrativo", (Errores.ERROR_LARGO_RESPONSABLE_ADMINISTRATIVO_100));
            }
            if(aso.getAsoTelefonoResponsableAdministrativo() != null && StringUtils.isNotBlank(aso.getAsoTelefonoResponsableAdministrativo())) {
                if(aso.getAsoTelefonoResponsableAdministrativo().length() > 20) {
                    ge.addError("asoTelefonoResponsableAdministrativo", (Errores.ERROR_LARGO_TELEFONO_RESPONSABLE_ADMINISTRATIVO_20));
                }
            }
            
            if(aso.getAsoCorreoResponsableAdministrativo() != null && StringUtils.isNotBlank(aso.getAsoCorreoResponsableAdministrativo())) {
                if (!ValidationUtils.validarEmail(aso.getAsoCorreoResponsableAdministrativo(), null)) {
                    ge.addError("asoCorreoResponsableAdministrativo", (Errores.ERROR_CORREO_RESPONSABLE_ADMINISTRATIVO_INVALIDO));
                }
                if(aso.getAsoCorreoResponsableAdministrativo().length() > 50) {
                    ge.addError("asoCorreoResponsableAdministrativoTamanio", (Errores.ERROR_LARGO_CORREO_RESPONSABLE_ADMINISTRATIVO_50));
                }
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
