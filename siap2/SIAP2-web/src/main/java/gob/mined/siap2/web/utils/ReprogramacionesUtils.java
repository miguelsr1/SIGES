/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils;

import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.web.datatypes.DataPOAProyectoView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class ReprogramacionesUtils {

    public static List<DataPOAProyectoView> actualizarListaPOA(POAConActividades poa) {
        List<DataPOAProyectoView> listaDatosPOA = new ArrayList();

        if (poa != null) {

            for (POActividadBase act : poa.getActividades()) {
                for (POInsumos in : act.getInsumos()) {
                    DataPOAProyectoView dp = new DataPOAProyectoView();
                    dp.setActividad(act.getNombre());
                    dp.setInsumoCodigo("----");
                    dp.setInsumoNombre("----");
                    if (in.getInsumo() != null) {
                        dp.setInsumoCodigo(in.getInsumo().getCodigo());
                        dp.setInsumoNombre(in.getInsumo().getNombre());
                    }
                    dp.setInsumoId(in.getId());

                    dp.setMonto(in.getMontoTotal());
                    if (in.getNoUACI() != null) {
                        if (in.getNoUACI()) {
                            dp.setUaci("NO");
                        } else {
                            dp.setUaci("SI");
                        }
                    }
                    dp.setMontoCertificado(in.getMontoTotalCertificado());
                    if (in.getProcesoInsumo() != null) {
                        dp.setMontoAdjudicado(in.getProcesoInsumo().getMontoTotalAdjudicado());
                    }
                    dp.setMontoComprometido(in.getMontoComprometido());
                    if (in.getMontoPep() != null) {
                        if (in.getMontoTotalCertificado() != null) {
                            dp.setMontoDisponibleCertificado(in.getMontoPep().subtract(in.getMontoTotalCertificado()));
                        } else {
                            dp.setMontoDisponibleCertificado(in.getMontoPep());
                        }
                    }
                    if (in.getMontoPep() != null) {
                        if (in.getMontoComprometido() != null) {
                            dp.setMontoDisponibleComprometido(in.getMontoPep().subtract(in.getMontoComprometido()));
                        } else {
                            dp.setMontoDisponibleComprometido(in.getMontoPep());
                        }
                    }

                    listaDatosPOA.add(dp);

                }
            }
        }
        return listaDatosPOA;
    }

}
