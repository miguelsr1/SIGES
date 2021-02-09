/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.ActividadPOProyecto;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.ClasificadorFuncional;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.ProgramaIndicador;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.data.impl.ProgramacionFinancieraAccionCentral;
import gob.mined.siap2.entities.data.impl.ProgramacionFinancieraAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class VersionesDelegate implements Serializable {

    @Inject
    private GeneralBean bean;

    public Proyecto getProyecto(Integer id) {
        return bean.getProyecto(id);
    }

    public AccionCentral getAccionCentral(Integer id) {
        return bean.getAccionCentral(id);
    }

    public AsignacionNoProgramable getAsignacionNoProgramable(Integer id) {
        return bean.getAsignacionNoProgramable(id);
    }

    public ProgramaInstitucional getProgramaInstitucional(Integer id) {
        return bean.getProgramaInstitucional(id);
    }

    public ProgramaPresupuestario getProgramaPresupuestario(Integer id) {
        return bean.getProgramaPresupuestario(id);
    }

    public ProgramaIndicador getProgramaIndicador(Integer id) {
        return bean.getProgramaIndicador(id);
    }

    public List<PlanificacionEstrategica> getPlanificacionesEstrategicasVigentes() {
        return bean.getPlanificacionesEstrategicasVigentes();
    }

    public List<LineaEstrategica> getLineasEstrategicasVigetnes(Integer idPlanificacion) {
        return bean.getLineasEstrategicasVigetnes(idPlanificacion);
    }

    public List<ProgramaInstitucional> getProgramasInstitucionalesVigentes() {
        return bean.getProgramasInstitucionalesVigentes();
    }

    public List<Indicador> getProductosVigentes() {
        return bean.getProductosVigentes();
    }

    public List<ProgramaPresupuestario> getSubProgramasPresupuestariosVigentes(Integer idPadre) {
        return bean.getSubProgramasPresupuestariosVigentes(idPadre);
    }

    public List<ProgramaPresupuestario> getProgramasPresupuestariosVigentes() {
        return bean.getProgramasPresupuestariosVigentes();
    }

    public List getClasesGeneralCodiguera(Class clase) {
        return bean.getClasesGeneralCodiguera(clase);
    }

    public List<FuenteRecursos> getFuentesRecursos(Integer idFuenteFinanciamiento) {
        return bean.getFuentesRecursos(idFuenteFinanciamiento);
    }

    public List<ActividadPOProyecto> completeTextCodigueraActividadesPO(String query) {
        return bean.completeTextCodigueraActividadesPO(query);
    }

    public List<AnioFiscal> getAniosFiscalesPlanificacion() {
        return bean.getAniosFiscalesPlanificacion();
    }

    public List<AnioFiscal> getAniosFiscalesFormulacion() {
        return bean.getAniosFiscalesFormulacion();
    }

    public AnioFiscal getUltimoAnioFiscalFormulacion() {
        return bean.getUltimoAnioFiscalFormulacion();
    }
    public AnioFiscal getAniosFiscalesPorId(Integer id) {
        return bean.getAnioFiscalPorId(id);
    }
    
    public List<UnidadTecnica> getUnidadesTecnicasDireccion() {
        return bean.getUnidadesTecnicasDireccion();
    }

    public PlanificacionEstrategica getPlanificacionEstrategica(Integer id) {
        return bean.getPlanificacionEstrategica(id);
    }

    public List<ClasificadorFuncional> getClasificadoresFuncionalesAsignables() {
        return bean.getClasificadoresFuncionalesAsignables();
    }

    public List<ProgramacionFinancieraAccionCentral> getProgramacionFinancieraAccionCentral(Integer minAnio, Integer maxAnio) {
        return bean.getProgramacionFinancieraAccionCentral(minAnio, maxAnio);
    }

    public List<ProgramacionFinancieraAsignacionNoProgramable> getProgramacionFinancieraAsigNP(Integer minAnio, Integer maxAnio) {
        return bean.getProgramacionFinancieraAsigNP(minAnio, maxAnio);
    }

    public void guardarProgramacionFinancieraAccionCentral(List<ProgramacionFinancieraAccionCentral> programacion) {
        bean.guardarProgramacionFinancieraAccionCentral(programacion);
    }

    public void guardarProgramacionFinancieraAsigNP(List<ProgramacionFinancieraAsignacionNoProgramable> programacion) {
        bean.guardarProgramacionFinancieraAsigNP(programacion);
    }

    public List<Proyecto> getProyectosConNombre(String nombre) {
        return bean.getProyectosConNombre(nombre);
    }
    
    public List<GeneralPOA> getPOAsTrabago (Integer idAnioFiscal){
        return bean.getPOAsTrabago(idAnioFiscal);
    }
    public List<Insumo> getInsumos(String nombre) {
        return bean.getInsumos(nombre);
    }
    
    public List<LineaEstrategica> getLineasEstrategicasVigentes(){
        return bean.getLineasEstrategicasVigentes();
    }
    
    public List<POInsumos> getInsumosNOUACIporCodigo(Integer id) {
        return bean.getInsumosNOUACIporCodigo(id);
    }
    
    public List<POInsumos> getInsumosNOUACICertificadosPorCodigo(Integer id) {
        return bean.getInsumosNOUACICertificadosPorCodigo(id);
    }
    
    public Boolean anioSeleccionadoEstaFinalizado(Integer idAnioFiscal) {
        return bean.anioSeleccionadoEstaFinalizado(idAnioFiscal);
    }
        
}
