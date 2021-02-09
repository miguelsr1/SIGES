/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.exceptions.BusinessException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class ProrrateoUtils {

    /**
     * genera la distribución de pagos
     *
     * @param montoTotal
     * @param anioEmpieza
     * @param mesInicial
     * @param cantidadMeses
     * @return
     */
    public static List<FlujoCajaAnio> generarFlujoDeCajaAutomatico(BigDecimal montoTotal, Integer anioEmpieza, Integer mesInicial, Integer cantidadMeses) {
        List<FlujoCajaAnio> res = new LinkedList();

        if (mesInicial < 0 || mesInicial > 11) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_MES_INICIAL_INCORRECTO);
            throw b;
        }

        FlujoCajaAnio flujoActual = null;
        int mesActual = mesInicial;
        int anioActual = anioEmpieza;

        int cantMesesGenerados = 0;
        while (cantMesesGenerados < cantidadMeses) {
            if (flujoActual == null) {
                flujoActual = new FlujoCajaAnio();
                flujoActual.setAnio(anioActual);
                flujoActual.setFlujoCajaMenusal(FlujoCajaMensualUtils.generarFCM12Meses());
                res.add(flujoActual);
            }

            //si no es el ultimo mes
            BigDecimal montoMes;
            if (cantMesesGenerados + 1 != cantidadMeses) {
                montoMes = montoTotal.divide(new BigDecimal(cantidadMeses), 2, RoundingMode.DOWN);
            } else {
                BigDecimal totalGenerado = BigDecimal.ZERO;
                for (FlujoCajaAnio iter : res) {
                    for (POFlujoCajaMenusal iterMes : iter.getFlujoCajaMenusal()) {
                        if (iterMes.getMonto() == null) {
                            iterMes.setMonto(BigDecimal.ZERO);
                        }
                        totalGenerado = totalGenerado.add(iterMes.getMonto());
                    }
                }
                montoMes = montoTotal.subtract(totalGenerado);
            }
            flujoActual.getFlujoCajaMenusal().get(mesActual).setMonto(montoMes);

            //se suma para el mes siguiente
            cantMesesGenerados++;
            mesActual++;
            if (mesActual == 12) {
                mesActual = 0;
                anioActual = anioActual + 1;
                flujoActual = null;
            }
        }
        return res;
    }

}
