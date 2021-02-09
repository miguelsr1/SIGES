/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Sofis Solutions
 */
public class FlujoCajaMensualUtils {

    /**
     * Genera un flujo de caja anual para una cantidad de meses y un monto total
     * dados
     *
     * @param desdeFCM
     * @param cantMeses
     * @param total
     * @return
     */
    public static Set<FlujoCajaAnio> generarFlujoCajaMensual(Date desdeFCM, Integer cantMeses, BigDecimal total) {

        Set<FlujoCajaAnio> fcmA = (new LinkedHashSet());

        Calendar calHasta = Calendar.getInstance();
        calHasta.setTime(desdeFCM);
        calHasta.add(Calendar.MONTH, cantMeses);
        Date hastaFCM = calHasta.getTime();

        //se calcula cuanto se reparte a cada mes
        int mesesEntreFechas = DatesUtils.monthsBetween(desdeFCM, hastaFCM);
        BigDecimal cadaMes = total.divide(new BigDecimal(mesesEntreFechas), 2, RoundingMode.HALF_UP);

        //se itera en los meses y se reparte
        Calendar start = Calendar.getInstance();
        start.setTime(desdeFCM);
        Calendar end = Calendar.getInstance();
        end.setTime(hastaFCM);
        for (Date date = start.getTime(); start.compareTo(end) < 0; start.add(Calendar.MONTH, 1), date = start.getTime()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);

            FlujoCajaAnio fcm = null;
            Iterator<FlujoCajaAnio> iter = fcmA.iterator();
            while (iter.hasNext() && fcm == null) {
                FlujoCajaAnio tmp = iter.next();
                if (tmp.getAnio().intValue() == year) {
                    fcm = tmp;
                }
            }
            if (fcm == null) {
                fcm = generarFCM(year);
                fcmA.add(fcm);
            }

            fcm.getFlujoCajaMenusal().get(month).setMonto(cadaMes);
        }

        return fcmA;
    }

    /**
     * Setea los montos prorrateados al mes mayor del año mayor en base a la
     * diferencia entre el total y los montos redondeados
     *
     * @param fcmA
     * @param total
     */
    public static void aplicarProrrateoAlFlujoDeCajaAnual(Set<FlujoCajaAnio> fcmA, BigDecimal total) {

        Integer anioMayor = 0;
        Integer mesMayor = 0;

        BigDecimal totalDistribuido = BigDecimal.ZERO;
        BigDecimal montoMesMayor = BigDecimal.ZERO;

        for (FlujoCajaAnio fca : fcmA) {
            if (fca.getAnio().compareTo(anioMayor) >= 0) {
                anioMayor = fca.getAnio();
                mesMayor = 0;//Lo reseteo para obtenerlo del nuevo año que es mayor
            }
            for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                if (fcm.getMonto() != null && (fcm.getMonto().compareTo(BigDecimal.ZERO) > 0)) {

                    totalDistribuido = totalDistribuido.add(fcm.getMonto());
                    if (anioMayor == fca.getAnio()) {
                        if (fcm.getMes().compareTo(mesMayor) >= 0) {
                            mesMayor = fcm.getMes();
                            montoMesMayor = fcm.getMonto();
                        }
                    }
                }
            }
        }

        BigDecimal diferenciaEntreTotalyDistribuido = total.subtract(totalDistribuido);
        BigDecimal nuevoMonto = montoMesMayor.add(diferenciaEntreTotalyDistribuido);

        Iterator<FlujoCajaAnio> itFCMA = fcmA.iterator();
        Boolean encontroAnioYMes = false;

        while (itFCMA.hasNext() && !encontroAnioYMes) {
            FlujoCajaAnio fca = itFCMA.next();
            Iterator<POFlujoCajaMenusal> itFCM = fca.getFlujoCajaMenusal().iterator();
            if (fca.getAnio().equals(anioMayor)) {
                while (itFCM.hasNext() && !encontroAnioYMes) {
                    POFlujoCajaMenusal fcm = itFCM.next();
                    if (fcm.getMes().equals(mesMayor)) {
                        encontroAnioYMes = true;
                        fcm.setMonto(nuevoMonto);
                    }
                }
            }
        }
    }

    /**
     * Genera un flujo de caja de año
     *
     * @param anio
     * @return
     */
    public static FlujoCajaAnio generarFCM(Integer anio) {
        FlujoCajaAnio fcm = new FlujoCajaAnio();
        fcm.setFlujoCajaMenusal(generarFCM12Meses());
        fcm.setAnio(anio);
        return fcm;
    }

    /**
     * Genera la lista de los 12 meses para un flujo de caja mensual
     *
     * @return
     */
    public static List<POFlujoCajaMenusal> generarFCM12Meses() {
        List<POFlujoCajaMenusal> l = new LinkedList();
        for (int i = 1; i <= 12; i++) {
            POFlujoCajaMenusal f = new POFlujoCajaMenusal();
            f.setMes(i);
            f.setMonto(BigDecimal.ZERO);
            l.add(f);
        }
        return l;
    }

    /**
     * Devuelve un año que se encuentra en un flujo de caja anual
     *
     * @param list
     * @param anio
     * @return
     */
    public static FlujoCajaAnio findAnio(Set<FlujoCajaAnio> list, int anio) {
        if (list != null) {
            for (FlujoCajaAnio fc : list) {
                if (fc.getAnio().intValue() == anio) {
                    return fc;
                }
            }
        }
        return null;
    }

    /**
     * Devuelve un mes que se encuentra en un flujo de caja mesnual
     *
     * @param fca
     * @param mes
     * @return
     */
    public static POFlujoCajaMenusal findMes(FlujoCajaAnio fca, int mes) {
        if (fca != null) {
            return findMes(fca.getFlujoCajaMenusal(), mes);
        }
        return null;
    }

    /**
     * Devuelve un mes que se encuentra en un flujo de caja mensual
     *
     * @param l
     * @param mes
     * @return
     */
    public static POFlujoCajaMenusal findMes(List<POFlujoCajaMenusal> l, int mes) {
        if (l != null) {
            for (POFlujoCajaMenusal fc : l) {
                if (fc.getMes().intValue() == mes) {
                    return fc;
                }
            }
        }
        return null;

    }

    /**
     * Este método calcula el flujo de caja mensual para un insumo dado.
     *
     * @param insumo
     * @param flujo
     * @param total
     */
    private static void calularFlujoCajaInsumo(POInsumos insumo, FlujoCajaAnio flujo, BigDecimal total) {
        FlujoCajaAnio flujoCajaAnioInsumo = FlujoCajaMensualUtils.findAnio(insumo.getFlujosDeCajaAnio(), flujo.getAnio());
        if (flujoCajaAnioInsumo == null) {
            flujoCajaAnioInsumo = FlujoCajaMensualUtils.generarFCM(flujo.getAnio());
            insumo.getFlujosDeCajaAnio().add(flujoCajaAnioInsumo);
        }
        for (POFlujoCajaMenusal flujoCajaMensualItem : flujo.getFlujoCajaMenusal()) {
            POFlujoCajaMenusal flujoCajaMensualInsumo = FlujoCajaMensualUtils.findMes(flujoCajaAnioInsumo, flujoCajaMensualItem.getMes());
            if (total.equals(BigDecimal.ZERO)) {
                flujoCajaMensualInsumo.setMonto(BigDecimal.ZERO);
            } else {
                BigDecimal montoInsumo = insumo.getMontoTotal().multiply(flujoCajaMensualItem.getMonto());
                montoInsumo = montoInsumo.divide(total, 2, BigDecimal.ROUND_HALF_UP);
                flujoCajaMensualInsumo.setMonto(montoInsumo);
            }
        }
    }

    /**
     * Este método genera el FCM planificado para un grupo
     *
     * @param idigrupo
     * @param flujos
     * @throws GeneralException
     */
    public static void generarFCMPlanificadoGrupo(PACGrupo grupo, Set<FlujoCajaAnio> flujos) throws GeneralException {
        try {
            //calcula los totales
            BigDecimal total = BigDecimal.ZERO;
            for (POInsumos insumo : grupo.getInsumos()) {
                total = total.add(insumo.getMontoTotal());
                insumo.getFlujosDeCajaAnio().clear();
            }

            //se guardan los totales
            for (FlujoCajaAnio flujo : flujos) {
                for (POInsumos insumo : grupo.getInsumos()) {
                    calularFlujoCajaInsumo(insumo, flujo, total);
                }
            }
            //se aplica el prorrate al último mes del flujo de cada insumo
            for (POInsumos insumo : grupo.getInsumos()) {
                aplicarProrrateoAlFlujoDeCajaAnual(insumo.getFlujosDeCajaAnio(), insumo.getMontoTotal());
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método guarda FCM planificado para un grupo dado.
     *
     * @param idGrupo
     * @param flujo
     * @throws GeneralException
     */
    public static void distribuirFCMPlanificadoGrupo(PACGrupo grupo, FlujoCajaAnio flujo, Set<FlujoCajaAnio> flujos) throws GeneralException {
        try {
            //calcula los totales
            /*Valido que los montos ingresados en los meses no se excedan el monto total del grupo*/
            BigDecimal montoTotalGrupo = grupo.getTotal();
            BigDecimal montoIngresado = BigDecimal.ZERO;
            for (FlujoCajaAnio fca : flujos) {
                for (POFlujoCajaMenusal poFCM : fca.getFlujoCajaMenusal()) {
                    montoIngresado = montoIngresado.add(poFCM.getMonto());
                }
                if (montoIngresado.compareTo(montoTotalGrupo) != 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_MONTOS_INGRESADOS_DISTINTOS_MONTO_TOTAL_GRUPO);
                    throw b;
                }
            }

            BigDecimal total = BigDecimal.ZERO;
            for (POInsumos insumo : grupo.getInsumos()) {
                total = total.add(insumo.getMontoTotal());
            }

            //se guardan los totales
            for (POInsumos insumo : grupo.getInsumos()) {
                calularFlujoCajaInsumo(insumo, flujo, total);
            }
        } catch (BusinessException be) {
            throw be;

        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Valida los montos ingresados en un flujo de caja anual
     *
     * @param insumo
     * @param flujo
     * @throws GeneralException
     */
    public static void validarFCMPlanificadoInsumo(POInsumos insumo, Set<FlujoCajaAnio> flujo) throws GeneralException {
        /*Valido que los montos ingresados en los meses no se excedan el monto total del insumo*/
        BigDecimal montoIngresado = BigDecimal.ZERO;
        for (FlujoCajaAnio fc : insumo.getFlujosDeCajaAnio()) {
            for (POFlujoCajaMenusal poFCM : fc.getFlujoCajaMenusal()) {
                if (poFCM.getMonto() != null) {
                    montoIngresado = montoIngresado.add(poFCM.getMonto());
                }
            }
        }
        if (montoIngresado.compareTo(insumo.getMontoTotal()) != 0) {
            String[] params = {insumo.getId() + " " + insumo.getNombre(),
                insumo.getMontoTotal().toString(),
                montoIngresado.toString()
            };
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EN_EL_INSUMO_0_SU_MONTO_TOTAL_ES_DE_1_Y_ES_DISTINTO_AL_FLUJO_DE_CAJA_2, params);
            throw b;
        }

        /*Valido que no se repitan años*/
        for (FlujoCajaAnio fca1 : flujo) {
            for (FlujoCajaAnio fca2 : flujo) {
                if (fca1 != fca2) {
                    if (fca1.getAnio().equals(fca2.getAnio())) {
                        String[] params = {insumo.getId() + " " + insumo.getNombre(),
                            fca1.getAnio().toString()
                        };
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_EN_EL_INSUMO_0_EL_ANIO_1_ESTA_DUPLICADO, params);
                        throw b;
                    }
                }
            }
        }

    }

    /**
     * Este método obtiene el flujo de un grupo particular.
     *
     * @param idGrupo
     * @return
     */
    public static Set<FlujoCajaAnio> getFCMPlanificadoGrupo(PACGrupo grupo) {
        Set<FlujoCajaAnio> fca = new LinkedHashSet();
        for (POInsumos insumo : grupo.getInsumos()) {
            for (FlujoCajaAnio flujoCajaAnioInsumo : insumo.getFlujosDeCajaAnio()) {
                FlujoCajaAnio flujoCajaAnioItem = FlujoCajaMensualUtils.findAnio(fca, flujoCajaAnioInsumo.getAnio());
                if (flujoCajaAnioItem == null) {
                    flujoCajaAnioItem = FlujoCajaMensualUtils.generarFCM(flujoCajaAnioInsumo.getAnio());
                    fca.add(flujoCajaAnioItem);
                }
                for (POFlujoCajaMenusal flujoCajaMensualInsumo : flujoCajaAnioInsumo.getFlujoCajaMenusal()) {
                    POFlujoCajaMenusal flujoCajaMensualItem = FlujoCajaMensualUtils.findMes(flujoCajaAnioItem, flujoCajaMensualInsumo.getMes());
                    flujoCajaMensualItem.setMonto(flujoCajaMensualItem.getMonto().add(flujoCajaMensualInsumo.getMonto()));
                }
            }
        }
        return fca;
    }

    /**
     * Retorna un Map con el número y el nombre de cada mes
     *
     * @return
     */
    public static Map<String, String> getMapMesesDelAnio() {
        Map<String, String> mapMeses = new LinkedHashMap();
        List<String> meses = new LinkedList<>();
        meses.add("Enero");
        meses.add("Febrero");
        meses.add("Marzo");
        meses.add("Abril");
        meses.add("Mayo");
        meses.add("Junio");
        meses.add("Julio");
        meses.add("Agosto");
        meses.add("Septiembre");
        meses.add("Octubre");
        meses.add("Noviembre");
        meses.add("Diciembre");

        for (int i = 0; i < meses.size(); i++) {
            String idMes = (i + 1) + "";
            mapMeses.put(idMes, meses.get(i));
        }

        return mapMeses;
    }
}
