/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import com.mined.siap2.interfaces.ManejoLineaBaseTrabajo;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ComboValorSeguimiento;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POARiesgo;
import gob.mined.siap2.entities.data.impl.POActividad;
import gob.mined.siap2.entities.data.impl.POActividadAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POGActividadProyecto;
import gob.mined.siap2.entities.data.impl.POGInsumo;
import gob.mined.siap2.entities.data.impl.POGInsumoAnio;
import gob.mined.siap2.entities.data.impl.POGMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POGProyecto;
import gob.mined.siap2.entities.data.impl.POIndicadorLinea;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.data.impl.ValoresSeguimiento;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Sofis Solutions
 */
public class POAConverter {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private Map<String, Object> convertidos = new HashMap();
    private GeneralPOA poaEnConstruccion = null;

    private String getObjectKey(String clase, Integer id) {
        return (clase + "-" + id);
    }

    /**
     * base es una copia de línea de trabajo que se va a fijar por ahora solo se
     * reajusta lineas en el POA grande así se pueden eliminar cosas de los hijos
     *
     * @param trabajo
     * @param base
     */
    private void reajustarLineas(ManejoLineaBaseTrabajo trabajo, ManejoLineaBaseTrabajo base) {
        ManejoLineaBaseTrabajo baseVieja = null;
        if (trabajo.getLineaBase() != null) {
            baseVieja = (ManejoLineaBaseTrabajo) trabajo.getLineaBase();
            baseVieja.setLineaTrabajo(base);
        }
        base.setLineaTrabajo(trabajo);
        base.setLineaBase(baseVieja);
        base.setFechaFijacion(new Date());

        trabajo.setLineaBase(base);
    }

    public POAProyecto convertPOAPOAProyecto(POAProyecto trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POAProyecto", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POAProyecto) convertidos.get(key);
        }
        POAProyecto base = new POAProyecto();
        convertidos.put(key, base);
        //convierte       

        poaEnConstruccion = base;

        base.setEstado(trabajo.getEstado());
        base.setTipo(trabajo.getTipo());
        base.setUnidadTecnica(trabajo.getUnidadTecnica());
        base.setAnioFiscal(trabajo.getAnioFiscal());
        base.setProyecto(trabajo.getProyecto());
        base.setFechaDesde(trabajo.getFechaDesde());
        base.setFechaHasta(trabajo.getFechaHasta());
        base.setLineas(new LinkedList());
        base.setCierreAnual(Boolean.FALSE);
        for (POLinea linea : trabajo.getLineas()) {
            base.getLineas().add(convertPOLinea(linea));
        }
        base.setRiesgos(new LinkedList());
        for (POARiesgo riesgo : trabajo.getRiesgos()) {
            base.getRiesgos().add(convertPOARiesgo(riesgo));
        }
        //se actualiza la referencia de los viejos
        reajustarLineas(trabajo, base);
        return base;
    }
    
    public POGProyecto convertPOGProyecto(POGProyecto trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POGProyecto", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POGProyecto) convertidos.get(key);
        }
        POGProyecto base = new POGProyecto();
        convertidos.put(key, base);
        //convierte       
        base.setEstado(trabajo.getEstado());
        base.setLineas(new LinkedList());
        for (POLinea linea : trabajo.getLineas()) {
            base.getLineas().add(convertPOLinea(linea));
        }
        //se actualiza la referencia de los viejos
        reajustarLineas(trabajo, base);
        return base;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    private POARiesgo convertPOARiesgo(POARiesgo trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POARiesgo", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POARiesgo) convertidos.get(key);
        }
        POARiesgo base = new POARiesgo();
        convertidos.put(key, base);
        //convierte
        base.setAccionesDeContingencia(trabajo.getAccionesDeContingencia());
        base.setAccionesDeMitigacion(trabajo.getAccionesDeMitigacion());
        base.setNombreFuncionarioResponsable(trabajo.getNombreFuncionarioResponsable());
        base.setOrigen(trabajo.getOrigen());
        base.setRiesgo(trabajo.getRiesgo());
        base.setValoracionDelRiesgo(trabajo.getValoracionDelRiesgo());

        return base;
    }

    private POLinea convertPOLinea(POLinea trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POLinea", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POLinea) convertidos.get(key);
        }
        POLinea base = new POLinea();
        convertidos.put(key, base);
        //convierte

        base.setPosicion(trabajo.getPosicion());
        base.setProducto(trabajo.getProducto());

        base.setActividades(new LinkedList());
        for (POActividadBase actividad : trabajo.getActividades()) {
            base.getActividades().add(convertPOActividadBase(actividad));
        }
        base.setIndicadores(new LinkedList());
        for (POIndicadorLinea indicador : trabajo.getIndicadores()) {
            base.getIndicadores().add(convertPOIndicadorLinea(indicador));
        }
        base.setValoresProducto(new LinkedList());
        for (ComboValorSeguimiento comboValorSeguimiento : trabajo.getValoresProducto()) {
            base.getValoresProducto().add(convertComboValorSeguimiento(comboValorSeguimiento));
        }
        base.setColaboradoras(new LinkedList<UnidadTecnica>());
        for (UnidadTecnica colaborador : trabajo.getColaboradoras()) {
            base.getColaboradoras().add(colaborador);
        }
        return base;
    }

    private POIndicadorLinea convertPOIndicadorLinea(POIndicadorLinea trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POIndicadorLinea", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POIndicadorLinea) convertidos.get(key);
        }
        POIndicadorLinea base = new POIndicadorLinea();
        convertidos.put(key, base);
        //convierte
        base.setIndicador(trabajo.getIndicador());
        base.setComboValoresSeguimiento(new LinkedList());
        for (ComboValorSeguimiento seg : trabajo.getComboValoresSeguimiento()) {
            base.getComboValoresSeguimiento().add(convertComboValorSeguimiento(seg));
        }
        base.setLineaPOProyecto(convertPOLinea(trabajo.getLineaPOProyecto()));

        return base;
    }

    private ComboValorSeguimiento convertComboValorSeguimiento(ComboValorSeguimiento trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("ComboValorSeguimiento", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (ComboValorSeguimiento) convertidos.get(key);
        }
        ComboValorSeguimiento base = new ComboValorSeguimiento();
        convertidos.put(key, base);
        //convierte
        base.setAnio(trabajo.getAnio());
        base.setTipoSeguimiento(trabajo.getTipoSeguimiento());
        base.setValores(new LinkedList());
        for (ValoresSeguimiento seg : trabajo.getValores()) {
            base.getValores().add(convertValoresSeguimiento(seg));
        }

        return base;
    }

    private ValoresSeguimiento convertValoresSeguimiento(ValoresSeguimiento trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("ValoresSeguimiento", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (ValoresSeguimiento) convertidos.get(key);
        }
        ValoresSeguimiento base = new ValoresSeguimiento();
        convertidos.put(key, base);
        //convierte
        base.setPosicion(trabajo.getPosicion());
        base.setTipoValorSeguimiento(convertComboValorSeguimiento(trabajo.getTipoValorSeguimiento()));
        base.setValor(trabajo.getValor());
        base.setPosicionjpa(trabajo.getPosicionjpa());

        return base;
    }

    public POAConActividades convertPOAConActividades(POAConActividades trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POAConActividades", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POAConActividades) convertidos.get(key);
        }
        POAConActividades base = new POAConActividades();
        convertidos.put(key, base);
        //convierte        

        poaEnConstruccion = base;

        base.setEstado(trabajo.getEstado());
        base.setAnioFiscal(trabajo.getAnioFiscal());
        base.setUnidadTecnica(trabajo.getUnidadTecnica());
        base.setConMontoPorAnio(trabajo.getConMontoPorAnio());
        base.setCierreAnual(trabajo.getCierreAnual());

        base.setActividades(new LinkedList());
        for (POActividadBase actividadBaseTrabajo : trabajo.getActividades()) {
            base.getActividades().add(convertPOActividadBase(actividadBaseTrabajo));
        }
        //se actualiza la referencia de los viejos
        reajustarLineas(trabajo, base);
        return base;
    }

    POActividadBase convertPOActividadBase(POActividadBase trabajo) {
        if (trabajo instanceof POGActividadProyecto) {
            return convertPOGActividadProyecto((POGActividadProyecto) trabajo);        
        } else if (trabajo instanceof POActividad) {
            return convertPOActividad((POActividad) trabajo);
        } else if (trabajo instanceof POActividadAsignacionNoProgramable) {
            return convertPOActividadAsignacionNoProgramable((POActividadAsignacionNoProgramable) trabajo);
        } else if (trabajo instanceof POActividadProyecto) {
            return convertPOActividadProyecto((POActividadProyecto) trabajo);
        }
        throw new UnsupportedOperationException("Invalid operation.");
    }

    private void copyTrabajoToBase(POActividadBase trabajo, POActividadBase base) {
        base.setPosicion(trabajo.getPosicion());
        base.setUbicacion(trabajo.getUbicacion());
        base.setDuracion(trabajo.getDuracion());
        base.setInsumos(new LinkedList());
        for (POInsumos poaInsumosTrabo : trabajo.getInsumos()) {
            base.getInsumos().add(convertPOInsumos(poaInsumosTrabo));
        }
        base.setUtResponsable(trabajo.getUtResponsable());
        base.setResponsable(trabajo.getResponsable());
    }

    POActividad convertPOActividad(POActividad trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POActividadBase", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POActividad) convertidos.get(key);
        }
        POActividad base = new POActividad();
        convertidos.put(key, base);
        //convierte       
        copyTrabajoToBase(trabajo, base);
        base.setNombre(trabajo.getNombre());
        //se actualiza la referencia de los viejos
        //reajustarLineas(trabajo, base);
        return base;
    }

    POActividadAsignacionNoProgramable convertPOActividadAsignacionNoProgramable(POActividadAsignacionNoProgramable trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POActividadAsignacionNoProgramable", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POActividadAsignacionNoProgramable) convertidos.get(key);
        }
        POActividadAsignacionNoProgramable base = new POActividadAsignacionNoProgramable();
        convertidos.put(key, base);
        //convierte        
        copyTrabajoToBase(trabajo, base);
        base.setActividadNP(trabajo.getActividadNP());
        //se actualiza la referencia de los viejos
        return base;
    }

    POActividadProyecto convertPOActividadProyecto(POActividadProyecto trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POActividadProyecto", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POActividadProyecto) convertidos.get(key);
        }
        POActividadProyecto base = new POActividadProyecto();
        convertidos.put(key, base);
        //convierte
        copyTrabajoToBase(trabajo, base);
        setPOActividadProyecto(base, trabajo);
        //se actualiza la referencia de los viejos
        return base;
    }

    POGActividadProyecto convertPOGActividadProyecto(POGActividadProyecto trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POGActividadProyecto", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POGActividadProyecto) convertidos.get(key);
        }
        POGActividadProyecto base = new POGActividadProyecto();
        convertidos.put(key, base);
        //convierte
        copyTrabajoToBase(trabajo, base);
        setPOActividadProyecto(base, trabajo);
        base.setAnioInicio(trabajo.getAnioInicio());
        base.setAnioFin(trabajo.getAnioFin());
        //se actualiza la referencia de los viejos
        return base;
    }

    void setPOActividadProyecto(POActividadProyecto base, POActividadProyecto trabajo) {
        base.setActividadCodiguera(trabajo.getActividadCodiguera());
    }

    POInsumos convertPOInsumos(POInsumos trabajo) {
        if (trabajo instanceof POGInsumo) {
            return convertPOGInsumo((POGInsumo) trabajo);
        } else if (trabajo instanceof POInsumos) {
            return convertPOInsumosSolo((POInsumos) trabajo);
        }

        throw new UnsupportedOperationException("Invalid operation.");
    }

    void setValuesToPOInsumos(POInsumos base, POInsumos trabajo) {
        base.setPosicion(trabajo.getPosicion());
        base.setActividad(convertPOActividadBase(trabajo.getActividad()));
        base.setFechaContratacion(trabajo.getFechaContratacion());
        base.setObservacion(trabajo.getObservacion());
        base.setInsumo(trabajo.getInsumo());
        base.setNoUACI(trabajo.getNoUACI());
        base.setCantidad(trabajo.getCantidad());
        base.setMontosFuentes(new LinkedList());
        for (POMontoFuenteInsumo pOMontoFuenteInsumoTrabajo : trabajo.getMontosFuentes()) {
            base.getMontosFuentes().add(convertPOMontoFuenteInsumo(pOMontoFuenteInsumoTrabajo));
        }
        base.setMontoUnit(trabajo.getMontoUnit());
        base.setMontoTotal(trabajo.getMontoTotal());
        base.setUnidadTecnica(trabajo.getUnidadTecnica());

    }

    POInsumos convertPOInsumosSolo(POInsumos trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POInsumos", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POInsumos) convertidos.get(key);
        }
        POInsumos base = new POInsumos();
        convertidos.put(key, base);
        //convierte        
        setValuesToPOInsumos(base, trabajo);
        base.setPoa(poaEnConstruccion);

        //se actualiza la referencia de los viejos
        return base;
    }

    POGInsumo convertPOGInsumo(POGInsumo trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POGInsumo", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POGInsumo) convertidos.get(key);
        }
        POGInsumo base = new POGInsumo();
        convertidos.put(key, base);
        //convierte    
        setValuesToPOInsumos(base, trabajo);
        base.setDistribucionAnios(new LinkedList());
        for (POGInsumoAnio iter : trabajo.getDistribucionAnios()) {
            base.getDistribucionAnios().add(convertPOGInsumoAnio(iter));
        }
        return base;
    }

    POGInsumoAnio convertPOGInsumoAnio(POGInsumoAnio trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POGInsumoAnio", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POGInsumoAnio) convertidos.get(key);
        }
        POGInsumoAnio base = new POGInsumoAnio();
        convertidos.put(key, base);
        //convierte
        base.setAnio(trabajo.getAnio());
        base.setCantidadInsumo(trabajo.getCantidadInsumo());
        base.setInsumo(convertPOGInsumo(trabajo.getInsumo()));
        base.setMontosFuentes(new LinkedList());
        for (POGMontoFuenteInsumo iter : trabajo.getMontosFuentes()) {
            base.getMontosFuentes().add(convertPOGMontoFuenteInsumo(iter));
        }

        return base;
    }

    POGMontoFuenteInsumo convertPOGMontoFuenteInsumo(POGMontoFuenteInsumo trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POGMontoFuenteInsumo", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POGMontoFuenteInsumo) convertidos.get(key);
        }
        POGMontoFuenteInsumo base = new POGMontoFuenteInsumo();
        convertidos.put(key, base);
        //convierte        
        base.setInsumo(convertPOGInsumoAnio(trabajo.getInsumo()));
        base.setFuente(trabajo.getFuente());
        base.setMonto(trabajo.getMonto());      
        return base;
    }

    POFlujoCajaMenusal convertPOFlujoCajaMenusalInsumo(POFlujoCajaMenusal trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POFlujoCajaMenusalInsumo", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POFlujoCajaMenusal) convertidos.get(key);
        }
        POFlujoCajaMenusal base = new POFlujoCajaMenusal();
        convertidos.put(key, base);
        base.setMes(trabajo.getMes());
        base.setMonto(trabajo.getMonto());

        return base;
    }

    POMontoFuenteInsumo convertPOMontoFuenteInsumo(POMontoFuenteInsumo trabajo) {
        //verifica si ya fue convertido
        String key = getObjectKey("POMontoFuenteInsumo", trabajo.getId());
        if (convertidos.containsKey(key)) {
            return (POMontoFuenteInsumo) convertidos.get(key);
        }
        POMontoFuenteInsumo base = new POMontoFuenteInsumo();
        convertidos.put(key, base);
        //convierte        
        base.setInsumo(convertPOInsumos(trabajo.getInsumo()));
        base.setFuente(trabajo.getFuente());
        base.setMonto(trabajo.getMonto());   
        return base;
    }

    
}
