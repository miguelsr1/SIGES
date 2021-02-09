/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.POAConActividadesBean;
import gob.mined.siap2.entities.data.impl.ConMontoPorAnio;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.Reprogramacion;
import gob.mined.siap2.entities.data.impl.ReprogramacionDetalle;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.tipos.FiltroMA;
import gob.mined.siap2.entities.tipos.FiltroReprogramacion;
import gob.mined.siap2.exceptions.BusinessException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class POAConActividadesDelegate implements Serializable {

    @Inject
    private POAConActividadesBean bean;

    public boolean tienePermisoEdicionPOAAccionCentral(POAConActividades poa) {
        return bean.tienePermisoEdicionPOAAccionCentral(poa);
    }

    public POAConActividades getPOATrabajo(Integer idConMontoPorAnio, Integer idAnioFiscal, Integer idUnidadTecnica) {
        return bean.getPOATrabajo(idConMontoPorAnio, idAnioFiscal, idUnidadTecnica);
    }

    public POActividadBase guardarActividad(Integer idPOA, POActividadBase actividad) {
        return bean.guardarActividad(idPOA, actividad);
    }

    public POInsumos guardarInsumo(Integer idPOA, Integer idActividad, POInsumos insumo) {
        return bean.guardarInsumo(idPOA, idActividad, insumo);
    }

    public void eliminarInsumo(Integer idPOA, Integer idActividad, POInsumos insumo) {
        bean.eliminarInsumo(idPOA, idActividad, insumo);
    }

    public void eliminarActividad(Integer idPOA, POActividadBase actividad) {
        bean.eliminarActividad(idPOA, actividad);
    }

    public POAConActividades enviarPOAConActividades(Integer idConMontoPorAnio, Integer idPOA, TipoMontoPorAnio tipoMontoPorAnio) {
        return bean.enviarPOAConActividades(idConMontoPorAnio, idPOA, tipoMontoPorAnio);
    }

    public void generarLineaBase(Integer idPoa) {
        bean.generarLineaBase(idPoa);
    }

    public boolean esNecesarioGenerarPAC(List<POAConActividades> listPoa) {
        return bean.esNecesarioGenerarPAC(listPoa);
    }

    public void rechazarPOA(Integer idPoa, Integer idAccionCentral, String motivoRechazo, TipoMontoPorAnio tipoMontoPorAnio) {
        bean.rechazarPOA(idPoa, idAccionCentral, motivoRechazo, tipoMontoPorAnio);
    }

    public void validarPOA(Integer idPoa) {
        bean.validarPOA(idPoa);
    }

    public Map<String, Integer> getPOASEnLineaBase(Integer idConMontoPorAnio, Integer idAnioFiscal, Integer idUnidadTecnica) {
        return bean.getPOASEnLineaBase(idConMontoPorAnio, idAnioFiscal, idUnidadTecnica);
    }

    public POAConActividades getPOAEnLineaBase(Integer idPOA) {
        return bean.getPOAEnLineaBase(idPOA);
    }

    public List<POAConActividades> getPOAsTrabajo(Integer idConMontoPorAnio, Integer idAnioFiscal, EstadoPOA estado, TipoMontoPorAnio tipo) {
        return bean.getPOAsTrabajo(idConMontoPorAnio, idAnioFiscal, estado, tipo);
    }


    public List<POAConActividades> getPOAsParaReprogramacion(boolean filtrarUT, List<UnidadTecnica> utAfiltrar, Integer idAnioFiscal, TipoMontoPorAnio tipo) {
        return bean.getPOAsParaReprogramacion(filtrarUT, utAfiltrar,  idAnioFiscal,  tipo) ;
    }
    
    
    public List<POAConActividades> getPOAsTrabajo(Integer idConMontoPorAnio, Integer idAnioFiscal, EstadoPOA estado) {
        return bean.getPOAsTrabajo(idConMontoPorAnio, idAnioFiscal, estado);
    }

    public List<POAConActividades> getPOALineaBase(Integer idConMontoPorAnio, Integer idAnioFiscal) {
        return bean.getPOALineaBase(idConMontoPorAnio, idAnioFiscal);
    }

    public boolean isCompletoParaConsolidado(Integer idAccionCentral, Integer idAnioFiscal) {
        return bean.isCompletoParaConsolidado(idAccionCentral, idAnioFiscal);
    }

    public void consolidar(List<POAConActividades> listPoa, Integer idConMontoPorAnio) {
        bean.consolidar(listPoa, idConMontoPorAnio);
    }

    public Reprogramacion guardarReprogramacion(Reprogramacion reprog) {
        return bean.guardarReprogramacion(reprog);
    }

    public ReprogramacionDetalle guardarReprogramacionDetalle(ReprogramacionDetalle reprog) {
        return bean.guardarReprogramacionDetalle(reprog);
    }

    public Reprogramacion enviarReprogramacion(Reprogramacion reprog) {
        return bean.enviarReprogramacion(reprog);
    }

    public Reprogramacion rechazarEnPac(Reprogramacion reprog) {
        return bean.rechazarEnPac(reprog);
    }

    public Reprogramacion aprobarEnPac(Reprogramacion reprog) throws BusinessException {
        return bean.aprobarEnPac(reprog);
    }

    public Collection<Reprogramacion> obtenerReprogramacionesPorFiltro(FiltroReprogramacion filtro) {
        return bean.obtenerReprogramacionesPorFiltro(filtro);
    }

    public Collection<PACGrupo> obtenerGruposPorPOAId(Integer id) {
        return bean.obtenerGruposPorPOAId(id);
    }

    public Collection<ConMontoPorAnio> obtenerConMontoPorAnioPorFiltro(FiltroMA filtro) {
        return bean.obtenerConMontoPorAnioPorFiltro(filtro);
    }

    public Reprogramacion aplicarReprogramacionPOAConActividades(Integer idReprogramacion) {
        return bean.aplicarReprogramacionPOAConActividades(idReprogramacion);
    }

    public void confirmarCierreAnual(Integer idConMontoConAnio, Integer idAnioFiscal) {
        bean.confirmarCierreAnual(idConMontoConAnio, idAnioFiscal);
    }

    public Boolean existenActividadesPendientesEnPOA(Integer idPOA) {
        return bean.existenActividadesPendientesEnPOA(idPOA);
    }

    public Boolean existenInsumosPendientesEnPOA(Integer idPOA) {
        return bean.existenInsumosPendientesEnPOA(idPOA);
    }

}
