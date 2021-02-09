/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.datatypes.DataActividadesPOA;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.POA;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.entities.data.impl.ProgramacionTrimestral;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroPOA;
import gob.mined.siap2.filtros.FiltroPrograma;
import gob.mined.siap2.filtros.FiltroUnidadTecnica;
import gob.mined.siap2.web.delegates.UnidadTecnicaDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.delegates.impl.POADelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaDelegate;
import gob.mined.siap2.web.delegates.impl.ProgramacionTrimestralDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.mb.UtilsMB;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "seguimientoIndicadoresPOA")
public class SeguimientoIndicadoresPOA implements Serializable {
@Inject
    JSFUtils jSFUtils;
    
    @Inject
    POADelegate poaDelegate;

    @Inject
    UsuarioDelegate usuarioDelegate;

    @Inject
    UnidadTecnicaDelegate unidadTecnicaDelegate;
    
    @Inject
    ProgramaDelegate progamaDelegate;
 
    @Inject
    VersionesDelegate versionDelegate;
    
     @Inject
    ProgramacionTrimestralDelegate progTrimestralDelegate;
     
    @Inject
    private UsuarioInfo usuarioInfo;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    UtilsMB utilsMB;
    private Integer openTab = 0;
    private Integer filtroAnioId;
    private Integer filtroAnioIdUT;
    private Integer filtroAnioIdIndicador;
    private UnidadTecnica filtroUnidadTecnicaSelected;
    private UnidadTecnica filtroUnidadTecnicaSelectedUT;
    private UnidadTecnica filtroUnidadTecnicaSelectedIndicador;
    private Programa filtroProgramaInstitucionalSelected;
    private POA filtroPOASelected;
    private POA filtroPOASelectedUT;
    private POA filtroPOASelectedIndicador;
    private Integer anioFiscalId;
    private List<DataActividadesPOA> listaActividades;
    private List<DataActividadesPOA> listaActividadesUT;
    private List<DataActividadesPOA> listaAvanceMetasIndicadores;
    private UnidadTecnica unidadTecnicaSelected;
    private Programa programaInstitucionalSelected;
    private AnioFiscal anioSelected;
    private Integer ultPeriodoHabilitado;
    private Integer cantidadActividadesNoCompletadas =0;
    private Integer cantidadActividadesPorcentajeAvance = 0;
    AnioFiscal anioFiscal;
    @PostConstruct
    public void init() {
        anioFiscal = utilsMB.getAnioFiscalActual();
        if (anioFiscal != null) {
            filtroAnioId = anioFiscal.getId();
            filtroAnioIdUT = anioFiscal.getId();
            filtroAnioIdIndicador = anioFiscal.getId();
            if(anioFiscal.getId() != null && anioFiscal.getId() > 0) {
                ProgramacionTrimestral prog = progTrimestralDelegate.obtenerPorAnioFiscal(anioFiscal.getId());
                if(prog != null) {
                    LocalDateTime dateActual = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0);
                    LocalDateTime dateDesde = null;
                    LocalDateTime dateHasta = null;
                    if(prog.getFechaDesdePrimerTrimestre() != null && prog.getFechaHastaPrimerTrimestre() != null) {
                        dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdePrimerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                        dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaPrimerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);
                        
                        if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                            ultPeriodoHabilitado = 1;
                        }
                    }
                    
                    if(prog.getFechaDesdeSegundoTrimestre() != null && prog.getFechaHastaSegundoTrimestre()!= null) {
                        
                        dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdeSegundoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                        dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaSegundoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);
                        
                        if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                            ultPeriodoHabilitado = 2;
                        }
                    }
                    
                    if(prog.getFechaDesdeTercerTrimestre() != null && prog.getFechaHastaTercerTrimestre() != null) {
                        dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdeTercerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                        dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaTercerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);
                        
                        if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                            ultPeriodoHabilitado = 3;
                        }
                    }
                    
                    if(prog.getFechaDesdeCuartoTrimestre() != null && prog.getFechaHastaCuartoTrimestre() != null) {
                        dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdeCuartoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                        dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaCuartoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);
                        
                        if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                            ultPeriodoHabilitado = 4;
                        }
                    }
                }
            }
            filterTableCumplimientoMetasPOA();
        }
    }

    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        filtroUnidadTecnicaSelected = null;
        filtroProgramaInstitucionalSelected = null;
        programaInstitucionalSelected = null;
        filtroPOASelected = null;
        filtroAnioId = anioFiscal != null ? anioFiscal.getId() : null;
    }
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilterUT(){
        filtroUnidadTecnicaSelectedUT = null;
        filtroPOASelectedUT = null;
        filtroAnioIdUT = anioFiscal != null ? anioFiscal.getId() : null;
    }
    
     /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilterCumplimientoMetasPOA(){
        filtroUnidadTecnicaSelectedUT = null;
        filtroPOASelectedUT = null;
        filtroAnioIdUT = anioFiscal != null ? anioFiscal.getId() : null;
    }
    
    /**
     * Método utilizado para cargar los combos a utilizar en la pestaña de Presupuesto, cuando se seleccione dicha pestaña
     * @param event 
     */
    public void changeTab(TabChangeEvent event) {
        String tabActiveId = event.getTab().getId();
        
        if (tabActiveId.equals("idTabIndicador")) {
            if (filtroAnioIdIndicador != null && filtroAnioIdIndicador > 0) {
                filterTableCumplimientoMetasPOA();
            }
        }
        if (tabActiveId.equals("idtabUnidadesTecnicas")) {
            if (filtroAnioIdUT != null && filtroAnioIdUT > 0) {
                filterTableUT();
            }
        } else if (tabActiveId.equals("idTabAvanceActividades")) {
            if (filtroAnioId != null && filtroAnioId > 0) {
                filterTable();
            }
        }
    }
    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            String[] orderBy = {"meta.poa.unidadTecnica.nombre","meta.poa.nombre"};
            boolean[] asc = {true};
            FiltroPOA filtro = new FiltroPOA();
            filtro.setId(filtroPOASelected != null ? filtroPOASelected.getId() : null);
            filtro.setAnioFiscalId(filtroAnioId != null && filtroAnioId > 0 ? filtroAnioId : null);
            filtro.setOrderBy(orderBy);
            filtro.setAscending(asc);
            filtro.setUnidadTecnicaId(filtroUnidadTecnicaSelected != null ? filtroUnidadTecnicaSelected.getId() : null);
            listaActividades = poaDelegate.obtenerActividadesPOAPorFiltro(filtro);
            cantidadActividadesPorcentajeAvance = listaActividades != null ? listaActividades.size() : 0;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTableUT() {
        try {
            String[] orderBy = {"meta.poa.unidadTecnica.nombre","meta.poa.nombre"};
            boolean[] asc = {true};
            FiltroPOA filtro = new FiltroPOA();
            filtro.setUltPeriodoHabilitado(ultPeriodoHabilitado);
            filtro.setId(filtroPOASelectedUT != null ? filtroPOASelectedUT.getId() : null);
            filtro.setAnioFiscalId(filtroAnioIdUT != null && filtroAnioIdUT > 0 ? filtroAnioIdUT : null);
            filtro.setOrderBy(orderBy);
            filtro.setAscending(asc);
            filtro.setUnidadTecnicaId(filtroUnidadTecnicaSelectedUT != null ? filtroUnidadTecnicaSelectedUT.getId() : null);
            listaActividadesUT = poaDelegate.obtenerActividadesPOAPorFiltro(filtro);
            cantidadActividadesNoCompletadas = listaActividadesUT != null ? listaActividadesUT.size() : 0;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTableCumplimientoMetasPOA() {
        try {
            String[] orderBy = {"poa.unidadTecnica.nombre","poa.nombre"};
            boolean[] asc = {true};
            FiltroPOA filtro = new FiltroPOA();
            //filtro.setUltPeriodoHabilitado(ultPeriodoHabilitado);
            filtro.setId(filtroPOASelectedIndicador != null ? filtroPOASelectedIndicador.getId() : null);
            filtro.setAnioFiscalId(filtroAnioIdIndicador != null && filtroAnioIdIndicador > 0 ? filtroAnioIdIndicador : null);
            filtro.setOrderBy(orderBy);
            filtro.setAscending(asc);
            filtro.setUnidadTecnicaId(filtroUnidadTecnicaSelectedIndicador != null ? filtroUnidadTecnicaSelectedIndicador.getId() : null);
            listaAvanceMetasIndicadores = poaDelegate.obtenerMetasPOAPorFiltro(filtro);
           // cantidadActividadesNoCompletadas = listaActividadesUT != null ? listaActividadesUT.size() : 0;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiarUT() {
        initFilterUT();
        filterTableUT();
    }
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiarCumplimientoMetasPOA() {
        initFilterCumplimientoMetasPOA();
        filterTableCumplimientoMetasPOA();
    }
    
    public List<UnidadTecnica> completeUnidadesTecnicas(String query){
        try {
           // if(query != null && !query.isEmpty()) {
                FiltroUnidadTecnica filtro = new FiltroUnidadTecnica();
                filtro.setNombre(query);
                filtro.setCodUsuario(usuarioInfo.getUserCod());
                filtro.setAscending(new boolean[]{true});
                filtro.setUnidadOperativa(Boolean.TRUE);
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(10L);
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                filtro.setOperacion(ConstantesEstandares.Operaciones.GESTION_POA_UNIDADES_APOYO);
                return usuarioDelegate.getUTDeUsuarioConOperacionByNombre(filtro);
           // }
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    public List<Programa> completeProgramasInstitucionales(String query){
        try {
            if(query != null && !query.isEmpty()) {
                FiltroPrograma filtro = new FiltroPrograma();
                filtro.setNombre(query);
                filtro.setAscending(new boolean[]{true});
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(11L);
                filtro.setTipo("INSTITUCIONAL");
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                return progamaDelegate.obtenerProgramasPorFiltro(filtro);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    public List<POA> completePOA(String query){
        try {
            if(query != null && !query.isEmpty()) {
                FiltroPOA filtro = new FiltroPOA();
                filtro.setId(filtroPOASelected != null ? filtroPOASelected.getId() : null);
                filtro.setNombre(query);
                filtro.setUnidadTecnicaId(filtroUnidadTecnicaSelected != null ? filtroUnidadTecnicaSelected.getId() : null);
                filtro.setMaxResults(11L);
                filtro.setIncluirCampos(new String[]{"nombre","version"});
                return poaDelegate.obtenerPorFiltro(filtro);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">   

    public List<DataActividadesPOA> getListaActividades() {
        return listaActividades;
    }

    public void setListaActividades(List<DataActividadesPOA> listaActividades) {
        this.listaActividades = listaActividades;
    }

    
    public UnidadTecnica getUnidadTecnicaSelected() {
        return unidadTecnicaSelected;
    }

    public void setUnidadTecnicaSelected(UnidadTecnica unidadTecnicaSelected) {
        this.unidadTecnicaSelected = unidadTecnicaSelected;
    }

    public Programa getProgramaInstitucionalSelected() {
        return programaInstitucionalSelected;
    }

    public void setProgramaInstitucionalSelected(Programa programaInstitucionalSelected) {
        this.programaInstitucionalSelected = programaInstitucionalSelected;
    }

    public AnioFiscal getAnioSelected() {
        return anioSelected;
    }

    public void setAnioSelected(AnioFiscal anioSelected) {
        this.anioSelected = anioSelected;
    }
    
    
    
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public Integer getFiltroAnioId() {
        return filtroAnioId;
    }

    public void setFiltroAnioId(Integer filtroAnioId) {
        this.filtroAnioId = filtroAnioId;
    }
    
    public Integer getAnioFiscalId() {
        return anioFiscalId;
    }

    public void setAnioFiscalId(Integer anioFiscalId) {
        this.anioFiscalId = anioFiscalId;
    }

    public UnidadTecnica getFiltroUnidadTecnicaSelected() {
        return filtroUnidadTecnicaSelected;
    }

    public void setFiltroUnidadTecnicaSelected(UnidadTecnica filtroUnidadTecnicaSelected) {
        this.filtroUnidadTecnicaSelected = filtroUnidadTecnicaSelected;
    }

    public Programa getFiltroProgramaInstitucionalSelected() {
        return filtroProgramaInstitucionalSelected;
    }

    public void setFiltroProgramaInstitucionalSelected(Programa filtroProgramaInstitucionalSelected) {
        this.filtroProgramaInstitucionalSelected = filtroProgramaInstitucionalSelected;
    }

    // </editor-fold>

    public POA getFiltroPOASelected() {
        return filtroPOASelected;
    }

    public void setFiltroPOASelected(POA filtroPOASelected) {
        this.filtroPOASelected = filtroPOASelected;
    }

    public Integer getFiltroAnioIdUT() {
        return filtroAnioIdUT;
    }

    public void setFiltroAnioIdUT(Integer filtroAnioIdUT) {
        this.filtroAnioIdUT = filtroAnioIdUT;
    }

    public UnidadTecnica getFiltroUnidadTecnicaSelectedUT() {
        return filtroUnidadTecnicaSelectedUT;
    }

    public void setFiltroUnidadTecnicaSelectedUT(UnidadTecnica filtroUnidadTecnicaSelectedUT) {
        this.filtroUnidadTecnicaSelectedUT = filtroUnidadTecnicaSelectedUT;
    }

    public POA getFiltroPOASelectedUT() {
        return filtroPOASelectedUT;
    }

    public void setFiltroPOASelectedUT(POA filtroPOASelectedUT) {
        this.filtroPOASelectedUT = filtroPOASelectedUT;
    }

    public List<DataActividadesPOA> getListaActividadesUT() {
        return listaActividadesUT;
    }

    public void setListaActividadesUT(List<DataActividadesPOA> listaActividadesUT) {
        this.listaActividadesUT = listaActividadesUT;
    }

    public Integer getUltPeriodoHabilitado() {
        return ultPeriodoHabilitado;
    }

    public void setUltPeriodoHabilitado(Integer ultPeriodoHabilitado) {
        this.ultPeriodoHabilitado = ultPeriodoHabilitado;
    }

    public Integer getCantidadActividadesNoCompletadas() {
        return cantidadActividadesNoCompletadas;
    }

    public void setCantidadActividadesNoCompletadas(Integer cantidadActividadesNoCompletadas) {
        this.cantidadActividadesNoCompletadas = cantidadActividadesNoCompletadas;
    }

    public Integer getCantidadActividadesPorcentajeAvance() {
        return cantidadActividadesPorcentajeAvance;
    }

    public void setCantidadActividadesPorcentajeAvance(Integer cantidadActividadesPorcentajeAvance) {
        this.cantidadActividadesPorcentajeAvance = cantidadActividadesPorcentajeAvance;
    }

    public Integer getOpenTab() {
        return openTab;
    }

    public void setOpenTab(Integer openTab) {
        this.openTab = openTab;
    }

    public List<DataActividadesPOA> getListaAvanceMetasIndicadores() {
        return listaAvanceMetasIndicadores;
    }

    public void setListaAvanceMetasIndicadores(List<DataActividadesPOA> listaAvanceMetasIndicadores) {
        this.listaAvanceMetasIndicadores = listaAvanceMetasIndicadores;
    }

    public Integer getFiltroAnioIdIndicador() {
        return filtroAnioIdIndicador;
    }

    public void setFiltroAnioIdIndicador(Integer filtroAnioIdIndicador) {
        this.filtroAnioIdIndicador = filtroAnioIdIndicador;
    }

    public UnidadTecnica getFiltroUnidadTecnicaSelectedIndicador() {
        return filtroUnidadTecnicaSelectedIndicador;
    }

    public void setFiltroUnidadTecnicaSelectedIndicador(UnidadTecnica filtroUnidadTecnicaSelectedIndicador) {
        this.filtroUnidadTecnicaSelectedIndicador = filtroUnidadTecnicaSelectedIndicador;
    }

    public POA getFiltroPOASelectedIndicador() {
        return filtroPOASelectedIndicador;
    }

    public void setFiltroPOASelectedIndicador(POA filtroPOASelectedIndicador) {
        this.filtroPOASelectedIndicador = filtroPOASelectedIndicador;
    }
}