/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.ConMontoPorAnio;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.exceptions.BusinessException;
import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class POAConActividadesUtils {

    /**
     * Este método inicializa un POA
     *
     * @param poa
     */
    public static void initPOA(POAConActividades poa) {
        for (POActividadBase a : poa.getActividades()) {
            for (POInsumos i : a.getInsumos()) {
                for (FlujoCajaAnio fca : i.getFlujosDeCajaAnio()) {
                    fca.getFlujoCajaMenusal().isEmpty();
                }
            }
        }
    }

    /**
     * Este método valida un POA
     *
     * @param objeto
     * @param poa
     */
    public static void validatePOA(ConMontoPorAnio objeto, POAConActividades poa) {
        System.out.println("*************validatePOA");
        BigDecimal montoUT = POAUtils.getMonto(objeto, poa.getUnidadTecnica(), poa.getAnioFiscal());

        //SE SUMAN LOS MONTOS USADOS
        BigDecimal montoUsado = BigDecimal.ZERO;
        for (POActividadBase a : poa.getActividades()) {
            for (POInsumos i : a.getInsumos()) {
                montoUsado = montoUsado.add(i.getMontoTotal());
            }
        }

        if (montoUT == null || BigDecimal.ZERO.compareTo(montoUT) == 0) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_UT_NO_TIENE_ASIGNADO_MONTO_PARA_ANIO);
            throw b;
        }

        if (/*BigDecimal.ZERO.compareTo(montoUsado) != 0 && */montoUsado.compareTo(montoUT) > 0) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_UT_MONTO_USADO_SOBREPASA_MONTO_UT);
            throw b;
        }
    }

    /**
     * Este método valida un POA para ser enviado
     *
     * @param objeto
     * @param poa
     */
    public static void validatePOAtoSend(ConMontoPorAnio objeto, POAConActividades poa) {
        //busco el monto que tiene asignado la UT para el anio
        BigDecimal montoUT = POAUtils.getMonto(objeto, poa.getUnidadTecnica(), poa.getAnioFiscal());
        if (montoUT == null || montoUT.equals(BigDecimal.ZERO)) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_UT_NO_TIENE_ASIGNADO_MONTO_PARA_ANIO);
            throw b;
        }

        //SE SUMAN LOS MONTOS USADOS
        BigDecimal montoUsado = BigDecimal.ZERO;
        for (POActividadBase a : poa.getActividades()) {
            for (POInsumos i : a.getInsumos()) {
                montoUsado = montoUsado.add(i.getMontoTotal());
            }
        }

        if (montoUsado.compareTo(montoUT) != 0) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_DEBE_USAR_EXACTAMENTE_EL_MISMO_DINERO_QUE_EL_ASIGNADO);
            throw b;
        }
    }

    /**
     * Valida un insumo de una AC o una ANP
     */
    public static void validateInsumo(POInsumos poInsumo) {
        if (poInsumo.getNoUACI() != null && poInsumo.getNoUACI()) {
            BigDecimal montoTotalInsumo = poInsumo.getMontoTotal();
            if (montoTotalInsumo == null) {
                montoTotalInsumo = BigDecimal.ZERO;
            }
            BigDecimal montoTotal12Meses = BigDecimal.ZERO;
            for (FlujoCajaAnio fca : poInsumo.getFlujosDeCajaAnio()) {
                for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                    if (fcm.getMonto() != null) {
                        montoTotal12Meses = montoTotal12Meses.add(fcm.getMonto());
                    }
                }
            }
            if (montoTotalInsumo.compareTo(montoTotal12Meses) != 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_SUMA_MONTOS_INSUMO_EN_MESES_DIFERENTE_MONTO_INSUMO_TOTAL);
                throw b;
            }
        }
    }

    /**
     * Este método devuelve las actividades de un POA
     *
     * @param poa
     * @param idActividad
     * @return
     */
    public static POActividadBase getActividadEnPOA(POAConActividades poa, Integer idActividad) {
        for (POActividadBase a : poa.getActividades()) {
            if (a.getId().equals(idActividad)) {
                return a;
            }
        }
        return null;

    }

}
