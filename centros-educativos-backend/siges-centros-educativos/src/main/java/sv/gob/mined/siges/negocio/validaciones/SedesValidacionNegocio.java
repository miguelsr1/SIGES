/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.TipoSede;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSede;

public class SedesValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgSedes
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSede entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(entity.getSedNombre())) {
                ge.addError("sedNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getSedNombre().length() > 500) {
                ge.addError("sedNombre", Errores.ERROR_LARGO_NOMBRE_500);
            }

            if (!TipoSede.CIR_ALF.equals(entity.getSedTipo())){ //Alfab genera cod mediante trigger
                if (StringUtils.isBlank(entity.getSedCodigo())) {
                    ge.addError("sedCodigo", Errores.ERROR_CODIGO_VACIO);
                } else if (entity.getSedCodigo().length() > 15) {
                    ge.addError("sedCodigo", Errores.ERROR_LARGO_CODIGO_15);
                }
            }

            if (entity.getSedAnioInicioAct() != null) {
                if (entity.getSedAnioInicioAct() < 1800) {
                    ge.addError("sedAnioInicioAct", Errores.ERROR_ANIO_INICIO_MENOR_1800);
                }
                if (entity.getSedAnioInicioAct() > LocalDate.now().getYear()) {
                    ge.addError("sedAnioInicioAct", Errores.ERROR_ANIO_INICIO_MAYOR_ACTUAL);
                }
            }
            if (!StringUtils.isBlank(entity.getSedNota())) {
               if (entity.getSedNota().length() > 4000) {
                ge.addError("sedCodigo", Errores.ERROR_LARGO_NOTA_4000);
            }}

            if (!StringUtils.isBlank(entity.getSedCorreoElectronico())) {
                if (entity.getSedCorreoElectronico().length() > 100) {
                    ge.addError("sedCorreoElectronico", Errores.ERROR_LARGO_CORREO_100);
                }
            }
            if (!StringUtils.isBlank(entity.getSedCorreoElectronico2())) {
                if (entity.getSedCorreoElectronico2().length() > 100) {
                    ge.addError("sedCorreoElectronico", Errores.ERROR_LARGO_CORREO_100);
                }
            }
            if (!StringUtils.isBlank(entity.getSedSitioWeb())) {
                if (entity.getSedSitioWeb().length() > 100) {
                    ge.addError("sedSitioWeb", Errores.ERROR_LARGO_SITIO_WEB_100);
                }
            }
            if (StringUtils.isBlank(entity.getSedPropietario())) {
                ge.addError("sedPropietario", Errores.ERROR_PROPIETARIO_VACIO);
            }
            if (entity.getSedTipoCalendario() == null) {
                ge.addError("sedTipoCalendario", Errores.ERROR_TIPO_CALENDARIO_VACIO);
            }
            if (entity.getSedJornadasLaborales() == null || entity.getSedJornadasLaborales().isEmpty()) {
                ge.addError("sedJornadasLaborales", Errores.ERROR_JORNADA_VACIO);
            }

            if (BooleanUtils.isFalse(entity.getSedOrigenExterno())) {
                if (entity.getSedClasificacion() == null) {
                    ge.addError("sedClasificacion", Errores.ERROR_DENOMINACION_VACIO);
                }
            }

            try {
                DireccionValidacionNegocio.validar(entity.getSedDireccion());
            } catch (BusinessException be) {
                ge.getErrores().addAll(be.getErrores());
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    public static boolean validarCierre(SgSede entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        }else{
            if(entity.getSedFechaCierre()==null){
                ge.addError(Errores.ERROR_FECHA_CIERRE_VACIO);
            }
            if(entity.getSedMotivoCierre()==null){
                ge.addError(Errores.ERROR_MOTIVO_CIERRE_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
