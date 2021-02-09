/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.PACBean;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.enums.EstadoGrupoPAC;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class PACDelegate implements Serializable {

    @Inject
    private PACBean bean;

    public PAC getPAC(Integer idPAC) {
        return bean.getPAC(idPAC);
    }

    public PACGrupo guardarGrupo(PACGrupo grupo, Integer idPAC, Integer idMetodoAdquisicion, String json) {
        return bean.guardarGrupo(grupo, idPAC, idMetodoAdquisicion, json);
    }

    public PACGrupo iniciarGrupo(Integer idPAC, PACGrupo grupo) {
        return bean.iniciarGrupo(idPAC, grupo);
    }

    public PACGrupo cambiarEstadoGrupo(Integer idPAC, Integer idGrupo, EstadoGrupoPAC estadoPAC) {
        return bean.cambiarEstadoGrupo(idPAC, idGrupo, estadoPAC);
    }

    public void generarGruposSugeridos(Integer idPAC) {
        bean.generarGruposSugeridos(idPAC);
    }

    public void eliminarGrupo(Integer idGrupo) {
        bean.eliminarGrupo(idGrupo);
    }

    public void desacociarInsumo(Integer idInsumo) {
        bean.desacociarInsumo(idInsumo);
    }

    public PAC guardarPAC(PAC pac) {
        return bean.guardarPAC(pac);
    }

    public PAC bloquearPAC(Integer idPAC) {
        return bean.bloquearPAC(idPAC);
    }

    public PAC desbloquear(Integer idPAC) {
        return bean.desbloquear(idPAC);
    }

    public PAC iniciarPAC(PAC pac) {
        return bean.iniciarPAC(pac);
    }

    public void guardarFCMPAC(PAC pac) {
        bean.guardarFCMPAC(pac);
    }

    public void asociarInsumo(Integer grupo, List<Integer> insumos) {
        bean.asociarInsumo(grupo, insumos);
    }

    public PACGrupo getGrupo(Integer idGrupo) {
        return bean.getGrupo(idGrupo);
    }

    public List filtrarPAC(CriteriaTO criteria, Long startPosition, Long maxResult, String[] orderBy, boolean[] ascending) {
        return bean.filtrarPAC(criteria, startPosition, maxResult, orderBy, ascending);
    }

    public void unificarGrupos(Integer idGrupo1, Integer idGrupo2) {
        bean.unificarGrupos(idGrupo1, idGrupo2);
    }

    public List<PAC> getPacsCerrados() throws GeneralException {
        return bean.getPACsCerrados();
    }

    public void setBloquearPAC(boolean bloquear, Integer anio) {
        bean.setBloquearPAC(bloquear, anio);
    }

    public void generarReportePAC(Integer anio, boolean borrador) {
        bean.generarReportePAC(anio, borrador);
    }

    public void generarReportePEP(Integer anio, boolean borrador) {
        bean.generarReportePEP(anio, borrador);
    }

    public PAC getPACForFCM(Integer idPAC) {
        return bean.getPACForFCM(idPAC);
    }

    public boolean estaHabilitadoReportePEP(Integer anio) {
        return bean.estaHabilitadoReportePEP(anio);
    }

    public boolean hayPACSinCerrar(Integer anio) {
        return bean.hayPACSinCerrar(anio);
    }

    public List getPACGrupoSinFCM(Integer anio, Integer firstResult, Integer maxResults) {
        return bean.getPACGrupoSinFCM(anio, firstResult, maxResults);
    }

    public List getInsumosUACISinFCM(Integer anio, Integer firstResult, Integer maxResults) {
        return bean.getInsumosUACISinFCM(anio, firstResult, maxResults);
    }

    public long countPACGrupoSinFCM(Integer idAnioFiscal) {
        return bean.countPACGrupoSinFCM(idAnioFiscal);
    }

    public long countInsumosUACISinFCM(Integer anio) {
        return bean.countInsumosUACISinFCM(anio);
    }

    public void setBloquearPEP(boolean bloquear, Integer idAnioFiscal) {
        bean.setBloquearPEP(bloquear, idAnioFiscal);
    }

    public Set<FlujoCajaAnio> getFCMPlanificadoInsumo(Integer idInsumo) {
        return bean.getFCMPlanificadoInsumo(idInsumo);
    }

    public void eliminarFlujoDeCajaPlanificado(Integer idInsumo, Integer idFCanio) throws GeneralException {
        bean.eliminarFlujoDeCajaPlanificado(idInsumo, idFCanio);
    }

    public List<HashMap> obtenerInsumosDelPAC(Integer idPAC, String nombrePac) {
        return bean.obtenerInsumosDelPAC(idPAC, nombrePac);
    }
}
