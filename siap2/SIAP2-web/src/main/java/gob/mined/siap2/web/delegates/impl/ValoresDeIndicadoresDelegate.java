/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ValoresDeIndicadores;
import gob.mined.siap2.business.datatypes.DataActividad;
import gob.mined.siap2.business.datatypes.DataTipoIndicador;
import gob.mined.siap2.business.datatypes.DataVerIndicadorTipo;
import gob.mined.siap2.entities.data.impl.MetaIndicador;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.TipoProyecto;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class ValoresDeIndicadoresDelegate implements Serializable {

    @Inject
    private ValoresDeIndicadores bean;

    public List<DataTipoIndicador> getValoresDeIndicadores(List<Integer> unidadesTecnicas, Integer idAnioFiscal) {
        return bean.getValoresDeIndicadores(unidadesTecnicas, idAnioFiscal);
    }

    public List<Boolean> getPosicionesHabilitadasParaCarga(TipoSeguimiento tipoSeguimiento, TipoProyecto tipoProyecto, Integer idAnioFiscal) {
        return bean.getPosicionesHabilitadasParaCarga(tipoSeguimiento, tipoProyecto, idAnioFiscal);
    }

    public MetaIndicador guardar(MetaIndicador valorEditando) {
        return bean.guardar(valorEditando);
    }

    public void guardarTechos(List<DataTipoIndicador> techos) {
        bean.guardarTechos(techos);
    }

    public List<DataVerIndicadorTipo> getVisualizacionDeValoresDeIndicadores(Integer idAnioFiscal, Integer idProgramaInstitucional, Integer idProgramaPresupuestario) {
        return bean.getVisualizacionDeValoresDeIndicadores(idAnioFiscal, idProgramaInstitucional, idProgramaPresupuestario);
    }

    public List<DataActividad> getActividadesParaCarga(List<Integer> unidadesTecnicas, Integer idAnioFiscal) {
        return bean.getActividadesParaCarga(unidadesTecnicas, idAnioFiscal);
    }
    
     public POActividadBase cambiarEstadoActividad(Integer idActividad, EstadoActividadPOA estado) {
         return bean.cambiarEstadoActividad(idActividad, estado);
     }
     
     public List<HashMap> obtenerLineasMetasIndicadores(Integer lineaEstrategicaId, Integer anioFiscalId){
         return bean.obtenerLineasMetasIndicadores(lineaEstrategicaId, anioFiscalId);
     }
     
     public List<HashMap> obtenerInfoReportePoaProyectoAdministrativo(Integer proyectoId, Integer anioFiscalId){
         return bean.obtenerInfoReportePoaProyectoAdministrativo(proyectoId, anioFiscalId);
     }
     
     public List<HashMap> obtenerInfoReportePOIProyectosAdministrativosProgPresupuestario(Integer planificacionId, Integer anioFiscalId){
         return bean.obtenerInfoReportePOIProyectosAdministrativosProgPresupuestario(planificacionId, anioFiscalId);
     }
     
     public List<HashMap> obtenerInfoReportePOIProyectosAdministrativosProgInstitucional(Integer planificacionId, Integer anioFiscalId){
         return bean.obtenerInfoReportePOIProyectosAdministrativosProgInstitucional(planificacionId, anioFiscalId);
     }
     
     public List<HashMap> obtenerInfoReporteSeguimientoProyectoAdministrativo(Integer utId, Integer anioFiscalId, String seguimiento){
         return bean.obtenerInfoReporteSeguimientoProyectoAdministrativo(utId, anioFiscalId, seguimiento);
     }
     
     
}
