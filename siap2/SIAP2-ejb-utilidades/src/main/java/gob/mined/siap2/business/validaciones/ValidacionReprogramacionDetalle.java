/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.validaciones;

import gob.mined.siap2.business.utils.FlujoCajaMensualUtils;
import gob.mined.siap2.business.utils.Utils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumoFlujoCajaMensual;
import gob.mined.siap2.entities.data.impl.PolizaDeConcentracion;
import gob.mined.siap2.entities.data.impl.ReprogramacionDetalle;
import gob.mined.siap2.entities.enums.EstadoReprogramacion;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.BooleanUtils;
import org.infinispan.commons.hash.Hash;

/**
 *
 * @author Sofis Solutions
 */
public class ValidacionReprogramacionDetalle implements Serializable {

    /**
     * Aplica las reglas de validación para una línea de reprogramación
     *
     * @param rep
     * @return
     * @throws BusinessException
     */
    public static void validar(ReprogramacionDetalle rep, EstadoReprogramacion estado) throws BusinessException {
        List<String> errores = new ArrayList();

        if (rep.getPoaInsumo() != null) {
            POInsumos poi = rep.getPoaInsumo();
            //Caso 1: el insumo tiene certificación
            if (poi.getMontoTotalCertificado() != null) {
                //El nuevo monto, no puede ser menor al certificado
                if (rep.getInsumoNuevoTotal().compareTo(poi.getMontoTotalCertificado()) < 0) {
                    String[] params = {poi.getMontoTotalCertificado().toString()};
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EL_NUEVO_MONTO_NO_PUEDE_SERMENOR_AL_MONTO_CERTIFICADO_0, params);
                    throw b;
                }
            }
            // El monto certificado de las fuentes no puede disminuir
            for (POMontoFuenteInsumo montoFuente : poi.getMontosFuentes()) {
                for (POMontoFuenteInsumo montoFuenteReprog : rep.getInsumoNuevoMontosFuentes()) {
                    //Si es la misma fuente o si es una AC o ANP que tiene una sola fuente
                    if (montoFuente.getFuente().getFuenteRecursos().getId().equals(montoFuenteReprog.getFuente().getFuenteRecursos().getId()) || rep.getPoa().getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                         
                        if (montoFuenteReprog.getCertificado() !=null && montoFuente.getCertificado() !=null && montoFuenteReprog.getCertificado().compareTo(montoFuente.getCertificado()) < 0) {
                            BusinessException b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_LAS_FUENTES_NO_PUEDEN_DISMINUIR_EL_MONTO_CERTIFICADO);
                            throw b;
                        }
                    }
                }
            }

            //si ya fue adjudicado, el nuevo monto no puede ser menor al adjudicado
            if (poi.getProcesoInsumo() != null) {
                if (poi.getProcesoInsumo().getMontoTotalAdjudicado() != null
                        && poi.getProcesoInsumo().getMontoTotalAdjudicado().compareTo(rep.getInsumoNuevoTotal()) > 0) {
                    String[] params = {poi.getProcesoInsumo().getMontoTotalAdjudicado().toString()};
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EL_NUEVO_MONTO_NO_PUEDE_SERMENOR_AL_MONTO_ADJUDICADO_0, params);
                    throw b;
                }
            }

        }
        //Caso 3: Alguna actividad está seleccionada
        //se valida el monto de las fuentes
        BigDecimal totalFuentes = BigDecimal.ZERO;
        for (POMontoFuenteInsumo iter : rep.getInsumoNuevoMontosFuentes()) {
            if (iter.getMonto() == null) {
                iter.setMonto(BigDecimal.ZERO);
            }
            if (iter.getCertificado() == null) {
                iter.setCertificado(BigDecimal.ZERO);
            }

            if (iter.getMonto().compareTo(iter.getCertificado()) < 0) {
                String[] params = {iter.getFuente().getFuenteRecursos().getNombre()};
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EN_LA_FUENTE_0_SE_CERTIFICA_POR_MONTO_MAYOR_AL_DE_LA_FUENTE, params);
                throw b;
            }

            totalFuentes = totalFuentes.add(iter.getMonto());

        }

        if (rep.getInsumoNuevoTotal().compareTo(totalFuentes) != 0) {
            errores.add(ConstantesErrores.ERR_LA_SUMA_DE_MONTOS_DE_FUENTE_DISTINTO_EL_MONTO_TOTAL_DEL_INSUMO);
        }

        //Caso 4: Si es no UACI, la programacion debe coincidir con el nuevo monto
        boolean enFormulacion = estado == null || estado == EstadoReprogramacion.FORMULACION;
        if (BooleanUtils.isTrue(rep.getInsumoNuevoNoUaci()) || !enFormulacion) {
            BigDecimal valor = BigDecimal.ZERO;
            valor = Utils.sumar(valor, rep.getRdeMes1());
            valor = Utils.sumar(valor, rep.getRdeMes2());
            valor = Utils.sumar(valor, rep.getRdeMes3());
            valor = Utils.sumar(valor, rep.getRdeMes4());
            valor = Utils.sumar(valor, rep.getRdeMes5());
            valor = Utils.sumar(valor, rep.getRdeMes6());
            valor = Utils.sumar(valor, rep.getRdeMes7());
            valor = Utils.sumar(valor, rep.getRdeMes8());
            valor = Utils.sumar(valor, rep.getRdeMes9());
            valor = Utils.sumar(valor, rep.getRdeMes10());
            valor = Utils.sumar(valor, rep.getRdeMes11());
            valor = Utils.sumar(valor, rep.getRdeMes12());

            if (valor.compareTo(rep.getInsumoNuevoTotal()) != 0) {
                errores.add(ConstantesErrores.ERR_REP_EL_FLUJO_DE_CAJA_NO_COINCIDE_CON_TOTAL_INSUMO);
            }

        }

        if (!enFormulacion) {
            if (!BooleanUtils.isTrue(rep.getInsumoNuevoNoUaci())) {
                if (rep.getGrupoPAC() == null) {
                    errores.add(ConstantesErrores.ERROR_INSUMO_SIN_GRUPO);
                }
            }
        }

        if (rep.getInsumoNuevoNoUaci() != null && rep.getInsumoNuevoNoUaci()) {

            //valida que la suma de montos de las fuentes por mes sea igual al monto del insumo para ese mes
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes1(), 1);
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes2(), 2);
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes3(), 3);
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes4(), 4);
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes5(), 5);
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes6(), 6);
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes7(), 7);
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes8(), 8);
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes9(), 9);
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes10(), 10);
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes11(), 11);
            validarMontosFuentesPorMes(rep.getInsumoNuevoMontosFuentes(), rep.getRdeMes12(), 12);

            //valida que la suma de cada fuente en los 12 meses no supere el monto asignado a la fuente para ese insumo
            for (POMontoFuenteInsumo montoFuente : rep.getInsumoNuevoMontosFuentes()) {
                if (montoFuente.getMonto() != null && montoFuente.getMonto().compareTo(BigDecimal.ZERO) < 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_MONTO_FUENTE_INCORRECTO);
                    throw b;
                }
                BigDecimal montoTotalFuente12Meses = BigDecimal.ZERO;
                for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                    if (montoFuenteFCM.getMonto() != null && montoFuenteFCM.getMonto().compareTo(BigDecimal.ZERO) < 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_MONTO_FUENTE_MES_INCORRECTO);
                        throw b;
                    }
                    if (montoFuenteFCM.getMonto() != null) {
                        montoTotalFuente12Meses = montoTotalFuente12Meses.add(montoFuenteFCM.getMonto());
                    }
                }
                if (montoTotalFuente12Meses.compareTo(montoFuente.getMonto()) > 0) {
                    BusinessException b = new BusinessException();
                    String[] params = {montoFuente.getFuente().getFuenteRecursos().getCodigo()};
                    b.addError(ConstantesErrores.ERR_SUMA_MONTOS_FUENTE_1_SUPERA_MONTO_ASIGNADO_A_FUENTE_EN_INSUMO, params);
                    throw b;
                }
            }

        }

        if (errores.size() > 0) {
            BusinessException be = new BusinessException();
            be.setErrores(errores);
            throw be;
        }

        //Valido si alguna de las fuentes del insumo seleccionado está asociada a una póliza. Si lo está, los meses que coincidan con los de la póliza deben ser iguales o superiores en la reprogramación
        if (rep.getNuevoInsumo() != null && !rep.getNuevoInsumo()) {
            if (rep.getPoaInsumo().getNoUACI() != null && rep.getPoaInsumo().getNoUACI()) {
                if (rep.getPoaInsumo().getMontosFuentes() != null) {
                    for (POMontoFuenteInsumo montoFuente : rep.getPoaInsumo().getMontosFuentes()) {
                        if (montoFuente.getMontosFuentesInsumosFCM() != null) {
                            for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                                if (montoFuenteFCM.getPolizasConcentracion() != null && !montoFuenteFCM.getPolizasConcentracion().isEmpty()) {
                                    if (montoFuenteFCM.getMonto() != null && montoFuenteFCM.getMonto().compareTo(BigDecimal.ZERO) > 0) {
                                        for (PolizaDeConcentracion poliza : montoFuenteFCM.getPolizasConcentracion()) {
                                            for (POMontoFuenteInsumo mfReprog : rep.getInsumoNuevoMontosFuentes()) {
                                                if (montoFuente.getFuente().getFuenteRecursos().getId().equals(mfReprog.getFuente().getFuenteRecursos().getId()) || rep.getPoa().getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                                                    for (POMontoFuenteInsumoFlujoCajaMensual mfFCMReprog : mfReprog.getMontosFuentesInsumosFCM()) {
                                                        if (montoFuenteFCM.getFlujoCajaMensual().getMes().equals(mfFCMReprog.getFlujoCajaMensual().getMes())) {
                                                            if (mfFCMReprog.getMonto() == null || mfFCMReprog.getMonto().compareTo(montoFuenteFCM.getMonto()) < 0) {
                                                                BusinessException b = new BusinessException();
                                                                String[] params = {montoFuente.getFuente().getFuenteRecursos().getCodigo(), NumberUtils.nomberToString(montoFuenteFCM.getMonto()), NumberUtils.nomberToString(montoFuenteFCM.getMontoCertificado()), FlujoCajaMensualUtils.getMapMesesDelAnio().get(montoFuenteFCM.getFlujoCajaMensual().getMes().toString())};
                                                                b.addError(ConstantesErrores.ERR_FUENTE_0_ASOCIADA_A_POLIZA_CON_PAGO_1_EN_MES_2, params);
                                                                throw b;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
//                                    BusinessException b = new BusinessException();
//                                    b.addError(ConstantesErrores.ERR_INSUMO_TIENE_FUENTE_CON_POLIZA_ASOCIADADA);
//                                    throw b;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void validarMontosFuentesPorMes(List<POMontoFuenteInsumo> montosFuentesRep, BigDecimal montoRepMes, int mes) {
        BigDecimal montoDeFuentesEnMes = BigDecimal.ZERO;
        for (POMontoFuenteInsumo montoFuente : montosFuentesRep) {
            for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                if (montoFuenteFCM.getFlujoCajaMensual().getMes().equals(mes)) {
                    if (montoFuenteFCM.getMonto() != null) {
                        montoDeFuentesEnMes = montoDeFuentesEnMes.add(montoFuenteFCM.getMonto());
                    }
                }
            }
        }
        //Si el insumo tiene un monto nulo y el monto de las fuentes en ese mes es distinto de 0 o
        //si el insumo para ese mes no tiene monto nulo pero es distinto al de las fuentes en ese mes
        if ((montoRepMes == null && montoDeFuentesEnMes.compareTo(BigDecimal.ZERO) != 0) || (montoRepMes != null && montoRepMes.compareTo(montoDeFuentesEnMes) != 0)) {
            BusinessException b = new BusinessException();
            String mesString = FlujoCajaMensualUtils.getMapMesesDelAnio().get(mes + "");
            String[] params = {mesString};
            b.addError(ConstantesErrores.ERR_SUMA_MONTOS_FUENTES_MES_1_DIFERENTE_MONTO_INSUMO_MES, params);
            throw b;
        }
    }

}
