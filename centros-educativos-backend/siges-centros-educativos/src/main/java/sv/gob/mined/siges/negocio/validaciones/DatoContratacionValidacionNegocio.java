/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumModeloContrato;
import sv.gob.mined.siges.enumerados.TipoPersonalSedeEducativa;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDatoContratacion;

public class DatoContratacionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgDatoContratacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDatoContratacion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (entity.getDcoModeloContrato() == null){
                ge.addError("dcoModeloContrato", Errores.ERROR_MODELO_CONTRATIO_VACIO);
                throw ge;
            }
            
            if (entity.getDcoTipo() == null){
                ge.addError("dcoTipo", Errores.ERROR_TIPO_VACIO);
                throw ge;
            }

            if (entity.getDcoCargo() == null) {
                ge.addError("dcoCargo", Errores.ERROR_CARGO_VACIO);
            }
            if (entity.getDcoTipoNombramiento() == null) {
                if (EnumModeloContrato.ACUERDO.equals(entity.getDcoModeloContrato()) || EnumModeloContrato.CONTRATO.equals(entity.getDcoModeloContrato())) {
                    ge.addError("dcoTipoNombramiento", Errores.ERROR_TIPO_NOMBRAMIENTO_VACIO);
                }
            }
            if (entity.getDcoFuenteFinanciamiento() == null) {
                if (EnumModeloContrato.ACUERDO.equals(entity.getDcoModeloContrato()) || EnumModeloContrato.CONTRATO.equals(entity.getDcoModeloContrato())) {
                    ge.addError("dcoFuenteFinanciamiento", Errores.ERROR_FUENTE_FINANCIAMIENTO_VACIO);
                }
            }
            if (entity.getDcoInstitucionPagaSalario() == null) {
                if (EnumModeloContrato.CONTRATO.equals(entity.getDcoModeloContrato())) {
                    ge.addError("dcoInstitucionPagaSalario", Errores.ERROR_INSTITUCION_PAGA_SALARIO_VACIO);
                }
            } else if (entity.getDcoInstitucionPagaSalario().getIpsCodigo().equals(Constantes.OTRO_CODIGO)) {
                if (StringUtils.isBlank(entity.getDcoInstitucionPagaSalarioOtro())) {
                    ge.addError("dcoInstitucionPagaSalarioOtro", Errores.ERROR_INSTITUCION_PAGA_SALARIO_VACIO);
                }
            }
            if (entity.getDcoTipoInstitucionPaga() == null) {
                if (EnumModeloContrato.CONTRATO.equals(entity.getDcoModeloContrato())) {
                    ge.addError("dcoTipoInstitucionPaga", Errores.ERROR_TIPO_INSTITUCION_PAGA_VACIO);
                }
            }
            if (entity.getDcoDesde() != null) {
                if (entity.getDcoDesde().isAfter(LocalDate.now())) {
                    ge.addError("dcoDesde", Errores.ERROR_FECHA_DESDE_MAYOR);
                }
            } else {
                ge.addError("dcoDesde", Errores.ERROR_FECHA_DESDE_VACIO);
            }

            if (entity.getDcoTipoContrato() == null) {
                if (EnumModeloContrato.ACUERDO.equals(entity.getDcoModeloContrato()) || EnumModeloContrato.CONTRATO.equals(entity.getDcoModeloContrato())) {
                    ge.addError("dcoTipoContrato", Errores.ERROR_TIPO_CONTRATO_VACIO);
                }
            } else {
                if (BooleanUtils.isTrue(entity.getDcoTipoContrato().getTcoRequiereFechaHasta()) && entity.getDcoHasta() == null) {
                    ge.addError("dcoHasta", Errores.ERROR_FECHA_HASTA_VACIO);
                }
                if (BooleanUtils.isTrue(entity.getDcoTipoContrato().getTcoEsInterinato())) {
                    if (StringUtils.isBlank(entity.getDcoRazonesInterinato())) {
                        ge.addError("dcoRazonesInterinato", Errores.ERROR_RAZON_INTERINATO_VACIO);
                    } else if (entity.getDcoRazonesInterinato().length() > 255) {
                        ge.addError("dcoRazonesInterinato", Errores.ERROR_LARGO_RAZON_INTERINATO_255);
                    }
                }
            }

            if (entity.getDcoTipoAlfabetizador() != null && !(EnumModeloContrato.OTRO.equals(entity.getDcoModeloContrato()) && TipoPersonalSedeEducativa.ALF.equals(entity.getDcoTipo()))) {
                ge.addError("dcoTipoAlfabetizador", Errores.ERROR_TIPO_ALFABETIZADOR_NO_CORRESPONDE);
            }

            if (entity.getDcoDesde() != null && entity.getDcoHasta() != null) {
                if (entity.getDcoDesde().isAfter(entity.getDcoHasta())) {
                    ge.addError("dcoDesde", Errores.ERROR_FECHA_DESDE_MAYOR_HASTA);
                }
            }
            if (entity.getDcoCentroEducativo() == null) {
                ge.addError("dcoCentroEducativo", Errores.ERROR_SEDE_VACIO);
            }
            if (entity.getDcoModoPago() == null) {
                if (EnumModeloContrato.ACUERDO.equals(entity.getDcoModeloContrato()) || EnumModeloContrato.CONTRATO.equals(entity.getDcoModeloContrato())) {
                    ge.addError("dcoModoPago", Errores.ERROR_MODO_PAGO_VACIO);
                }
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
