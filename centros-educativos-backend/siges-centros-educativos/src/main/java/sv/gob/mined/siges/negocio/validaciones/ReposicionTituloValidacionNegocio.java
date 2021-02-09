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
import sv.gob.mined.siges.persistencia.entidades.SgReposicionTitulo;
import sv.gob.mined.siges.utils.ValidationUtils;

/**
 *
 * @author Sofis Solutions
 */
public class ReposicionTituloValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ret SgReposicionTitulo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgReposicionTitulo ret) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ret == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (ret.getRetAnio() == null) {
                ge.addError("rdiSede", Errores.ERROR_ANIO_VACIO);
            }
            if (StringUtils.isBlank(ret.getRetNombreEstudiantePartida())) {
                ge.addError("rdiSede", Errores.ERROR_NOMBRE_SOLICITANTE_VACIO);
            }else{
                if(ret.getRetNombreEstudiantePartida().length()>100){
                    ge.addError("rdiSede", Errores.ERROR_NOMBRE_SOLICITANTE_LARGO_100);
                }
            }
            if (StringUtils.isBlank(ret.getRetSedeNombre())) {
                ge.addError("rdiSede", Errores.ERROR_NOMBRE_SEDE_VACIO);

            } else {
                if (ret.getRetSedeNombre().length() > 150) {
                    ge.addError("sedNombre", Errores.ERROR_LARGO_NOMBRE_SEDE_150);
                }
            }
            
            if(ret.getRetSolicitudImpresion()!=null && ret.getRetSolicitudImpresion().getSimResolucionFk()==null){
                ge.addError("calSeccion", Errores.ERROR_RESOLUCION_VACIO);
            }
            

            if (ret.getRetSolicitudImpresion() == null) {
                ge.addError("rdiSede", Errores.ERROR_SOLICITUD_IMPRESION_VACIO);
            } else {
                if (ret.getRetSolicitudImpresion().getSimDefinicionTitulo() == null) {
                    ge.addError("rdiSede", Errores.ERROR_DEFINICION_TITULO_VACIO);
                }
                if (StringUtils.isBlank(ret.getRetSolicitudImpresion().getSimNumeroRegistro())) {
                    ge.addError("rdiSede", Errores.ERROR_NUMERO_REGISTRO_VACIO);
                }else{
                    if(ret.getRetSolicitudImpresion().getSimNumeroRegistro().length()>100){
                        ge.addError("rdiSede", Errores.ERROR_NUMERO_REGISTRO_LARGO_100);
                    }
                }
                if (ret.getRetSolicitudImpresion().getSimNumeroResolucion()== null) {
                    ge.addError("rdiSede", Errores.ERROR_NUMERO_RESOLUCION_VACIO);
                }else{
                    if(ret.getRetSolicitudImpresion().getSimNumeroResolucion().toString().length()>100){
                        ge.addError("rdiSede", Errores.ERROR_NUMERO_RESOLUCION_LARGO_100);
                    }
                }
            }
            if(ret.getRetEsAnterior2008()==null){
                ge.addError("rdiSede", Errores.ERROR_ANTERIOR_O_POSTERIOR_2008_VACIO);
            }else{
                if (BooleanUtils.isTrue(ret.getRetEsAnterior2008()) && ret.getRetTituloAnterior2008() == null) {
                    ge.addError("rdiSede", Errores.ERROR_TITULO_ANTERIOR_2008_VACIO);
                }
                if (BooleanUtils.isFalse(ret.getRetEsAnterior2008()) && StringUtils.isBlank(ret.getRetNombreTituloPosterior2008())) {
                    ge.addError("rdiSede", Errores.ERROR_NOMBRE_TITULO_VACIO);
                }
            }
            
            if (StringUtils.isBlank(ret.getRetDuiSolicitante())) {
                ge.addError("rdiSede", Errores.ERROR_DUI_SOLICITANTE_VACIO);
            } else {
                if (!ValidationUtils.validarDUI(ret.getRetDuiSolicitante())) {
                    ge.addError("perDui", Errores.ERROR_DUI_INCORRECTO);
                }
            }
            if (ret.getRetFechaLegalizacionTitulo() == null) {
                ge.addError("rdiSede", Errores.ERROR_FECHA_LEGALIZACION_VACIO);
            }else{
                if(ret.getRetFechaLegalizacionTitulo().isAfter(LocalDate.now())){
                    ge.addError("aseFechaGeneracion", Errores.ERROR_FECHA_LEGALIZACION_MAYOR_HOY);
                }
            }
            if (!StringUtils.isBlank(ret.getRetOpcionBachillerato())) {
                if (ret.getRetOpcionBachillerato().length() > 200) {
                    ge.addError("rdiSede", Errores.ERROR_OPCION_BACHILLERATO_200);
                }
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
