/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.enums;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public enum PasosProcesoAdquisicion {
    INICIALIZACION(0,"EstadoProcesoAdquisicion.INICIALIZACION"),
    RECEPCION_TDR_ET_CERT_DISP(1,"EstadoProcesoAdquisicion.RECEPCION_TDR_ET_CERT_DISP"),
    REVISION_JEFE_UACI(2,"EstadoProcesoAdquisicion.REVISION_JEFE_DEPARTAMENTO"),
    REVISION_TECNICO_UACI(3,"EstadoProcesoAdquisicion.REVISION_TECNICO_UACI"),
    INVITACION(4,"EstadoProcesoAdquisicion.INVITACION"),
    RECEPCION_OFERTAS(5,"EstadoProcesoAdquisicion.RECEPCION_OFERTAS"),
    EVALUACION(6,"EstadoProcesoAdquisicion.EVALUACION"),
    ADJUDICACION(7,"EstadoProcesoAdquisicion.ADJUDICACION"),
    COMPROMISO_PRESUPUESTARIO(8,"EstadoProcesoAdquisicion.COMPROMISO_PRESUPUESTARIO"),
    CONTRATO_ORDEN_DE_COMPRA(9,"EstadoProcesoAdquisicion.CONTRATO_ORDEN_DE_COMPRA"),
    ORDEN_INICIO(10,"EstadoProcesoAdquisicion.ORDEN_INICIO"),
    CERRADO(11,"EstadoProcesoAdquisicion.CERRADO");
    

    

    private static class Cache {
        private static List<PasosProcesoAdquisicion> CACHE = new LinkedList();
        static {
            for (final PasosProcesoAdquisicion d : PasosProcesoAdquisicion.values()) {
                CACHE.add(d.posicion, d);
            }
        }
    }

    public static PasosProcesoAdquisicion getEstadoByPos(int i) {
        return Cache.CACHE.get(i  % Cache.CACHE.size());
    }
    
    
    private final String label;
    private final int posicion;

    private PasosProcesoAdquisicion(int posicion, String label) {
        this.label = label;
        this.posicion=posicion;
    }
    public String getLabel() {
        return label;
    }
    public int getPosicion() {
        return posicion;
    }
        
}
