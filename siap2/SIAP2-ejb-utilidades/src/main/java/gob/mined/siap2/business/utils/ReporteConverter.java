/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.business.datatype.DataReporteProyectoEstructura;
import gob.mined.siap2.business.datatype.DataReporteProyectoEstructuraMontoFuente;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.ProyectoComponente;
import gob.mined.siap2.entities.data.impl.ProyectoEstPorcentajeFuente;
import gob.mined.siap2.entities.data.impl.ProyectoEstProducto;
import gob.mined.siap2.entities.data.impl.ProyectoEstructura;
import gob.mined.siap2.entities.data.impl.ProyectoMacroActividad;
import gob.mined.siap2.entities.enums.TipoEstructura;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class ReporteConverter {

    public static void addEstrucutra(Integer numeroPadre, ProyectoEstructura estructura, List<DataReporteProyectoEstructura> toAdd) {
        DataReporteProyectoEstructura dataEstructura = new DataReporteProyectoEstructura();
        String numeroBase = numeroPadre != null ? numeroPadre + "." : "";
        numeroBase = numeroBase + ReportesUtils.getNumber(estructura.getNumero());
        dataEstructura.setNumero(numeroBase);
        if (estructura.getTipo() == TipoEstructura.MACROACTIVIDAD) {
            ProyectoMacroActividad macroActividad = (ProyectoMacroActividad) estructura;
            dataEstructura.setNombre(macroActividad.getMacroActividad().getNombre());
        } else {
            ProyectoComponente componente = (ProyectoComponente) estructura;
            dataEstructura.setNombre(componente.getNombre());
        }
        dataEstructura.setImporte(ReportesUtils.getNumber(estructura.getImporte()));
        dataEstructura.setPorcentaje(ReportesUtils.getNumber(estructura.getPorcentaje()));
        dataEstructura.setProducto("");
        if (estructura.getUnidadTecnica() != null) {
            dataEstructura.setUnidadTecnica(estructura.getUnidadTecnica().getNombre());
        }
        toAdd.add(dataEstructura);

        for (ProyectoEstProducto producto : ReporteConverter.ordenarProductos(estructura.getProductos())) {
            DataReporteProyectoEstructura dataProducto = new DataReporteProyectoEstructura();
            dataProducto.setNumero(numeroBase + "." + ReportesUtils.getNumber(producto.getNumero()));
            dataProducto.setNombre("");
            if (producto.getProducto() != null) {
                dataProducto.setProducto(producto.getProducto().getNombre() + "(Meta: " + producto.getCantidad() + " )");
            }
            dataProducto.setImporte("");
            if (producto.getUnidadTecnica() != null) {
                dataProducto.setUnidadTecnica(producto.getUnidadTecnica().getNombre());
            }
            toAdd.add(dataProducto);
        }
    }

    public static void generarMontoEstructura(Integer numeroPadre, ProyectoEstructura estructura, List<DataReporteProyectoEstructuraMontoFuente> toAdd) {
        String numeroBase = numeroPadre != null ? numeroPadre + "." : "";

        for (FuenteRecursos f : getFuentesRecursosProyecto(estructura.getMontosFuentes())) {
            BigDecimal total = BigDecimal.ZERO;
            for (ProyectoEstPorcentajeFuente fuente : estructura.getMontosFuentes()) {
                if (fuente.getFuente().getFuenteRecursos().equals(f)) {
                    if (fuente.getMontoEnConstruccion() != null) {
                        total = total.add(fuente.getMontoEnConstruccion());
                    }
                }
            }
            DataReporteProyectoEstructuraMontoFuente dataFuente = new DataReporteProyectoEstructuraMontoFuente();
            dataFuente.setNumero(numeroBase + ReportesUtils.getNumber(estructura.getNumero()));
            dataFuente.setFuente(f.getNombre());
            dataFuente.setImporte(ReportesUtils.getNumber(total));
            if (estructura.getTipo() == TipoEstructura.MACROACTIVIDAD) {
                ProyectoMacroActividad macroActividad = (ProyectoMacroActividad) estructura;
                dataFuente.setNombre(macroActividad.getMacroActividad().getNombre());
            } else {
                ProyectoComponente componente = (ProyectoComponente) estructura;
                dataFuente.setNombre(componente.getNombre());
            }
            toAdd.add(dataFuente);
        }
    }

    public static List<FuenteRecursos> getFuentesRecursosProyecto(List<ProyectoEstPorcentajeFuente> fuentes) {
        List<FuenteRecursos> l = new LinkedList();
        for (ProyectoEstPorcentajeFuente aporte : fuentes) {
            if (!l.contains(aporte.getFuente().getFuenteRecursos())) {
                l.add(aporte.getFuente().getFuenteRecursos());
            }
        }
        return l;
    }

    public static List ordenarProyectoEstructuraPorOrden(List componentes) {
        if (componentes == null) {
            return null;
        }
        Collections.sort(componentes, new Comparator<ProyectoEstructura>() {
            @Override
            public int compare(ProyectoEstructura o1, ProyectoEstructura o2) {
                //los do snulos
                if (o1.getNumero() == o2.getNumero()) {
                    return 0;
                }
                if (o1.getNumero() != null && o2.getNumero() == null) {
                    return -1;
                }
                if (o2.getNumero() != null && o1.getNumero() == null) {
                    return 1;
                }
                return o1.getNumero().compareTo(o2.getNumero());
            }
        });
        return componentes;
    }

    public static List<ProyectoEstProducto> ordenarProductos(List<ProyectoEstProducto> prods) {
        if (prods == null) {
            return null;
        }
        Collections.sort(prods, new Comparator<ProyectoEstProducto>() {
            @Override
            public int compare(ProyectoEstProducto o1, ProyectoEstProducto o2) {
                //los do snulos
                if (o1.getNumero() == o2.getNumero()) {
                    return 0;
                }
                if (o1.getNumero() != null && o2.getNumero() == null) {
                    return -1;
                }
                if (o2.getNumero() != null && o1.getNumero() == null) {
                    return 1;
                }
                return o1.getNumero().compareTo(o2.getNumero());
            }
        });
        return prods;
    }
}
