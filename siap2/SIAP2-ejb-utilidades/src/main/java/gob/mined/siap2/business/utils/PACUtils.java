/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicionRango;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoPAC;
import gob.mined.siap2.entities.enums.TipoPOAPAC;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class PACUtils {

    public static PAC crearPAC(List<GeneralPOA> poas, Integer anio, TipoPOAPAC tipoPOA) {
        PAC pac = new PAC();
        pac.setAnio(anio);
        pac.setEstado(EstadoPAC.EN_FORMULACION);
        pac.setPoas(poas);
        pac.setTipoPOA(tipoPOA);
        pac.setIniciado(false);
        //se asocian los insumos con las unidades tecnicas
        for (GeneralPOA poa : poas) {
            UnidadTecnica unidadTecnica = poa.getUnidadTecnica();
            if (tipoPOA == TipoPOAPAC.POA_ACCION_CENTRAL || tipoPOA == TipoPOAPAC.POA_ASIGNACION_NO_PROGRAMABLE) {
                POAConActividades pa = (POAConActividades) poa;
                for (POActividadBase a : pa.getActividades()) {
                    for (POInsumos i : a.getInsumos()) {
                        i.setUnidadTecnica(unidadTecnica);
                        i.setPoa(poa);
                    }
                }
            } else if (tipoPOA == TipoPOAPAC.POA_PROYECTO ) {
                POAProyecto pa = (POAProyecto) poa;
                for (POLinea l : pa.getLineas()) {
                    for (POActividadBase a : l.getActividades()) {
                        for (POInsumos i : a.getInsumos()) {
                            i.setUnidadTecnica(unidadTecnica);
                            i.setPoa(poa);
                        }
                    }
                }
            }
        }
        return pac;
    }
    
    
    public static void ordenarRangosPorAnio(List<MetodoAdquisicionRango> rangos){
        Collections.sort(rangos, new Comparator<MetodoAdquisicionRango>() {
            @Override
            public int compare(MetodoAdquisicionRango o1, MetodoAdquisicionRango o2) {
                return o1.getAnio().compareTo(o2.getAnio());
            }
        });
    }

        
    public static MetodoAdquisicionRango getRangoParaAnio(List<MetodoAdquisicionRango> rangos, Integer anio){
        MetodoAdquisicionRango res = null;
        for (MetodoAdquisicionRango iterRango: rangos){
            if ((anio.compareTo(iterRango.getAnio()) >=0) &&  
                   ( res == null || res.getAnio().compareTo(iterRango.getAnio()) <0 )){
                res = iterRango;
            }            
        }
        return res;
    }
    
    /**
     * ordena los insumos segun OEG
     * 
     * @deprecated true
     * @param insumos
     * @return 
     */
    public static List<POInsumos> ordearInsumosSegunOEG(List<POInsumos> insumos) {
        List<POInsumos> res = new ArrayList<POInsumos>(insumos);
        //se ordenan la lista en orden inverso para que queden bien
//        Collections.sort(res, new Comparator<POInsumos>() {
//            @Override
//            public int compare(POInsumos o1, POInsumos o2) {
//                return o1.getInsumo().getObjetoEspecificoGasto().getOeg().compareTo(o2.getInsumo().getObjetoEspecificoGasto().getOeg());
//            }
//        });
//        
//        Collections.sort(res, new Comparator<POInsumos>() {
//            @Override
//            public int compare(POInsumos o1, POInsumos o2) {
//                return o1.getInsumo().getObjetoEspecificoGasto().getCuenta().compareTo(o2.getInsumo().getObjetoEspecificoGasto().getCuenta());
//            }
//        });
//              
//        Collections.sort(res, new Comparator<POInsumos>() {
//            @Override
//            public int compare(POInsumos o1, POInsumos o2) {
//                return o1.getInsumo().getObjetoEspecificoGasto().getRubro().compareTo(o2.getInsumo().getObjetoEspecificoGasto().getRubro());
//            }
//        });
        
        
        
//        
//        
//        
//        POInsumos ultimoAgregado = null;
//        POInsumos aAagregar = null;
//        int aAagregarPuntaje = -1;
//        do {
//            aAagregar = null;
//            aAagregarPuntaje = -1;
//
//            Iterator<POInsumos> iter = insumos.iterator();
//            while (iter.hasNext() && aAagregarPuntaje != 3) {
//                POInsumos insumo = iter.next();
//                if (!res.contains(insumo)) {  
//                    int puntajeIter = 0;
//                    if (ultimoAgregado == null
//                            || !Objects.equals(insumo.getInsumo().getObjetoEspecificoGasto().getOeg(),
//                                    ultimoAgregado.getInsumo().getObjetoEspecificoGasto().getOeg())
//                            || !Objects.equals(insumo.getInsumo().getObjetoEspecificoGasto().getCuenta(),
//                                    ultimoAgregado.getInsumo().getObjetoEspecificoGasto().getCuenta())
//                            || !Objects.equals(insumo.getInsumo().getObjetoEspecificoGasto().getRubro(),
//                                    ultimoAgregado.getInsumo().getObjetoEspecificoGasto().getRubro())) {
//                        puntajeIter = 3;
//                    } else if (!Objects.equals(insumo.getInsumo().getObjetoEspecificoGasto().getCuenta(),
//                            ultimoAgregado.getInsumo().getObjetoEspecificoGasto().getCuenta())
//                            || !Objects.equals(insumo.getInsumo().getObjetoEspecificoGasto().getRubro(),
//                                    ultimoAgregado.getInsumo().getObjetoEspecificoGasto().getRubro())) {
//                        puntajeIter = 2;
//                    } else if (!Objects.equals(insumo.getInsumo().getObjetoEspecificoGasto().getRubro(),
//                            ultimoAgregado.getInsumo().getObjetoEspecificoGasto().getRubro())) {
//                        puntajeIter = 1;
//
//                    }
//
//                    if (puntajeIter > aAagregarPuntaje) {
//                        aAagregar = insumo;
//                        aAagregarPuntaje = puntajeIter;
//                    }
//                }
//            }
//            if (aAagregar!=null){
//                res.add(aAagregar);   
//                ultimoAgregado =aAagregar;
//            }
//        }while (aAagregar!=null);

        return res;
    }
}
