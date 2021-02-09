/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.ReportePAC;
import gob.mined.siap2.entities.data.impl.ReportePEP;
import gob.mined.siap2.entities.enums.EstadoPAC;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.PACDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.web.utils.dataProvider.EntityGruposSinFCMDataProvider;
import gob.mined.siap2.web.utils.dataProvider.EntityInsumosSinFCMDataProvider;
import gob.mined.siap2.web.utils.dataProvider.EntityPACDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "pacConsulta")
public class PACConsulta implements Serializable {

    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    PACDelegate pacd;
    @Inject
    ArchivoDelegate archivoDelegate;
    @Inject
    PACDelegate pACDelegate;
    @Inject
    ReporteDelegate reporteDelegate;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String nombrePAC;
    private String nroPAC;
    private EstadoPAC estadoPAC;

    private SofisComboG<AnioFiscal> comboAnioFiscal;
    private SofisComboG<AnioFiscal> comboAnioFiscalConsulta;

    /**
     * reporte PAC *
     */
    private boolean pacBloqueado;
    private boolean pacReporteGenerado;
    private boolean mostrarBloquearDesbloquearPAC;
    private boolean mostrarGenerarReportePAC;
    
    /**
     * **** reporte PEP ******
     */
    private GeneralLazyDataModel insumosSinFCMDataProvider;
    private GeneralLazyDataModel gruposSinFCMDataProvider;
    private boolean hayPACSinCerrar;

    private boolean pepBloqueado;
    private boolean pepReporteGenerado;
    private boolean mostrarErroresPEP;
    private boolean mostrarBloquearDesbloquearPEP;
    private boolean mostrarGenerarReportePEP;
    
    @PostConstruct
    public void init() {
        initAnioFisscales();
        initAnioFisscalesConsulta();
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        nombrePAC = null;
        nroPAC = null;
        estadoPAC = null;
        initAnioFisscalesConsulta();
    }

    public void filterTable() {
        try {

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (comboAnioFiscalConsulta.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anio", comboAnioFiscalConsulta.getSelectedT().getAnio());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(nombrePAC)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombre", nombrePAC);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(nroPAC)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", Integer.valueOf(nroPAC));
                criterios.add(criterio);
            }
            if (estadoPAC != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", estadoPAC);
                criterios.add(criterio);
            }

            CriteriaTO condicion = null;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

            String className = PAC.class.getName();
            String[] orderBy = {"anio", "nombre"};
            boolean[] asc = {false, true};

            IDataProvider dataProvider = new EntityPACDataProvider(pacd, className, condicion, orderBy, asc);
            lazyModel = new GeneralLazyDataModel(dataProvider);

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
     * Devuelve el nombre de un POA a partir de un PAC 
     * @param pac
     * @return 
     */
    public String getNombrePOA(PAC pac) {
        if (!pac.getPoas().isEmpty()) {
            GeneralPOA poa = pac.getPoas().get(0);
            if (poa.getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                POAConActividades pa = (POAConActividades) poa;
                return pa.getConMontoPorAnio().getNombre();
            } else if (poa.getTipo() == TipoPOA.POA_PROYECTO) {
                POAProyecto pa = (POAProyecto) poa;
                return pa.getProyecto().getNombre();
            }
        }
        return "";
    }

    /**
     * Devuelve la cantidad de POAs asociados a un PAC
     * @param pac
     * @return 
     */
    public Integer countUnidadTecnicas(PAC pac) {
        return pac.getPoas().size();
    }

    /**
     * Devuelve la suma total de los montos totales de los grupos de un PAC
     * @param pac
     * @return 
     */
    public BigDecimal sumarTotal(PAC pac) {
        BigDecimal total = BigDecimal.ZERO;
        for (PACGrupo g : pac.getGrupos()) {
            total = total.add(g.getTotal());
        }
        return total;
    }

    /**
     * Carga un combo con años fiscales
     */
    public void initAnioFisscales() {
        List<AnioFiscal> l = emd.getEntities(AnioFiscal.class.getName(), "anio");
        int anio = Calendar.getInstance().get(Calendar.YEAR);
        AnioFiscal selecionado = null;
        Iterator<AnioFiscal> iter = l.iterator();
        while (iter.hasNext() && selecionado == null) {
            AnioFiscal temp = iter.next();
            if (temp.getAnio().intValue() == anio) {
                selecionado = temp;
            }
        }
        comboAnioFiscal = new SofisComboG<>(l, "anio");
        comboAnioFiscal.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        comboAnioFiscal.setSelectedT(selecionado);
    }

    /**
     *  Carga un combo con años fiscales
     */
    public void initAnioFisscalesConsulta() {
        List<AnioFiscal> l = emd.getEntities(AnioFiscal.class.getName(), "anio");
        int anio = Calendar.getInstance().get(Calendar.YEAR);
        AnioFiscal selecionado = null;
        Iterator<AnioFiscal> iter = l.iterator();
        while (iter.hasNext() && selecionado == null) {
            AnioFiscal temp = iter.next();
            if (temp.getAnio().intValue() == anio) {
                selecionado = temp;
            }
        }
        comboAnioFiscalConsulta = new SofisComboG<>(l, "anio");
        comboAnioFiscalConsulta.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        comboAnioFiscalConsulta.setSelectedT(selecionado);
    }

    /**
     * Bloquea los PAC de un año particular.
     * @param bloquear 
     */
    public void bloquearPAC(Boolean bloquear) {
        try {
            pacd.setBloquearPAC(bloquear, comboAnioFiscal.getSelectedT().getAnio());
            RequestContext.getCurrentInstance().execute("$('#bloquearPAC').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Genera y descarga un reporte PAC
     * @param borrador 
     */
    public void generarReportePAC(boolean borrador) {
        try {
            pacd.generarReportePAC(comboAnioFiscal.getSelectedT().getAnio(), borrador);
            RequestContext.getCurrentInstance().execute("$('#generarReportePAC').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Genera y descarga un reporte PAC global
     */
    public void generarReportePACGlobal() {
        try {
            byte[] bytes = reporteDelegate.generarReportePACGlobal(comboAnioFiscal.getSelectedT().getAnio());
            ArchivoUtils.downloadPdfFromBytes(bytes, "reportePACGlobal-" + comboAnioFiscal.getSelectedT().getAnio() + ".pdf");
            RequestContext.getCurrentInstance().execute("$('#generarReportePAC').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Genera y descarga un reporte PEP
     * @param borrador 
     */
    public void generarReportePEP(boolean borrador) {
        try {
            byte[] bytes = reporteDelegate.generarReportePEPGlobal(comboAnioFiscal.getSelectedT().getId());
            ArchivoUtils.downloadPdfFromBytes(bytes, "reportePEP-" + comboAnioFiscal.getSelectedT().getAnio() + ".pdf");
            RequestContext.getCurrentInstance().execute("$('#generarReportePEP').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
      
    /**
     * Inicializa las variables necesarias para cargar el reporte PAC para un año especifico
     */
    public void initConfigReportePACParaAnio() {
        boolean anioSeleccionado = false;
        AnioFiscal s = comboAnioFiscal.getSelectedT();
        if (s != null) {
            anioSeleccionado = true;
        }
        mostrarBloquearDesbloquearPAC = false;
        mostrarGenerarReportePAC = false;

        pacBloqueado = false;
        pacReporteGenerado = false;

        if (anioSeleccionado) {
            //calcula el estado actual del pac
            List<ReportePAC> reportes = emd.findByOneProperty(ReportePAC.class.getName(), "anio", s.getAnio());
            if (!reportes.isEmpty()) {
                pacBloqueado = reportes.get(0).getPacBloqueados();
                pacReporteGenerado = reportes.get(0).getReporteEmitido();
            }

            //habilita deshabilita los botones de bloquear y desbloquear en funcion de los errores
            mostrarBloquearDesbloquearPAC = (!pacReporteGenerado);
            mostrarGenerarReportePAC = pacBloqueado;
        }
    }

    /**
     * Método que sirve para descargar archivos
     * @param archivo 
     */
    public void downloadFile(Archivo archivo) {
        try {
            ArchivoUtils.downloadFile(archivo, archivoDelegate.getFile(archivo));
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Inicializa las variables necesarias para cargar el reporte PEP para un año especifico
     */
    public void initConfigReportePEPParaAnio() {
        boolean anioSeleccionado = false;
        AnioFiscal s = comboAnioFiscal.getSelectedT();
        if (s != null) {
            anioSeleccionado = true;
        }

        mostrarErroresPEP = false;
        mostrarBloquearDesbloquearPEP = false;
        mostrarGenerarReportePEP = false;

        pepBloqueado = false;
        pepReporteGenerado = false;

        if (anioSeleccionado) {
            //verifica si hay errores
            mostrarErroresPEP = !pACDelegate.estaHabilitadoReportePEP(s.getId());

            //si hay errores inicializa los errores
            if (mostrarErroresPEP) {
                hayPACSinCerrar = pACDelegate.hayPACSinCerrar(s.getAnio());

                IDataProvider dataProviderI = new EntityInsumosSinFCMDataProvider(pACDelegate, s);
                insumosSinFCMDataProvider = new GeneralLazyDataModel(dataProviderI);

                IDataProvider dataProviderG = new EntityGruposSinFCMDataProvider(pACDelegate, s);
                gruposSinFCMDataProvider = new GeneralLazyDataModel(dataProviderG);
            }

            //calcula el estado actual del pep
            List<ReportePEP> reportes = emd.findByOneProperty(ReportePEP.class.getName(), "anio", s.getAnio());
            if (!reportes.isEmpty()) {
                pepBloqueado = reportes.get(0).getPepBloqueados();
                pepReporteGenerado = reportes.get(0).getReporteEmitido();
            }

            //habilita deshabilita los botones de bloquear y desbloquear en función de los errores
            mostrarBloquearDesbloquearPEP = !mostrarErroresPEP && (!pepReporteGenerado);
            mostrarGenerarReportePEP = pepBloqueado;
        }
    }

    /**
     * Bloquea el PEP de un año específico
     * @param bloquear 
     */
    public void bloquearFCM(Boolean bloquear) {
        try {
            pacd.setBloquearPEP(bloquear, comboAnioFiscal.getSelectedT().getId());
            RequestContext.getCurrentInstance().execute("$('#bloquearFCM').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    //geters
    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public GeneralLazyDataModel getGruposSinFCMDataProvider() {
        return gruposSinFCMDataProvider;
    }

    public void setGruposSinFCMDataProvider(GeneralLazyDataModel gruposSinFCMDataProvider) {
        this.gruposSinFCMDataProvider = gruposSinFCMDataProvider;
    }

    public String getNombrePAC() {
        return nombrePAC;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscalConsulta() {
        return comboAnioFiscalConsulta;
    }

    public void setComboAnioFiscalConsulta(SofisComboG<AnioFiscal> comboAnioFiscalConsulta) {
        this.comboAnioFiscalConsulta = comboAnioFiscalConsulta;
    }

    public void setNombrePAC(String nombrePAC) {
        this.nombrePAC = nombrePAC;
    }

    public ArchivoDelegate getArchivoDelegate() {
        return archivoDelegate;
    }

    public void setArchivoDelegate(ArchivoDelegate archivoDelegate) {
        this.archivoDelegate = archivoDelegate;
    }

    public String getNroPAC() {
        return nroPAC;
    }

    public void setNroPAC(String nroPAC) {
        this.nroPAC = nroPAC;
    }

    public EstadoPAC getEstadoPAC() {
        return estadoPAC;
    }

    public void setEstadoPAC(EstadoPAC estadoPAC) {
        this.estadoPAC = estadoPAC;
    }

    public PermisosMB getPermisosMB() {
        return permisosMB;
    }

    public boolean isPepBloqueado() {
        return pepBloqueado;
    }

    public void setPepBloqueado(boolean pepBloqueado) {
        this.pepBloqueado = pepBloqueado;
    }

    public void setPermisosMB(PermisosMB permisosMB) {
        this.permisosMB = permisosMB;
    }

    public PACDelegate getPacd() {
        return pacd;
    }

    public void setPacd(PACDelegate pacd) {
        this.pacd = pacd;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscal() {
        return comboAnioFiscal;
    }

    public void setComboAnioFiscal(SofisComboG<AnioFiscal> comboAnioFiscal) {
        this.comboAnioFiscal = comboAnioFiscal;
    }

    public boolean isHayPACSinCerrar() {
        return hayPACSinCerrar;
    }

    public void setHayPACSinCerrar(boolean hayPACSinCerrar) {
        this.hayPACSinCerrar = hayPACSinCerrar;
    }

    public GeneralLazyDataModel getInsumosSinFCMDataProvider() {
        return insumosSinFCMDataProvider;
    }

    public void setInsumosSinFCMDataProvider(GeneralLazyDataModel insumosSinFCMDataProvider) {
        this.insumosSinFCMDataProvider = insumosSinFCMDataProvider;
    }

    public PACDelegate getpACDelegate() {
        return pACDelegate;
    }

    public void setpACDelegate(PACDelegate pACDelegate) {
        this.pACDelegate = pACDelegate;
    }

    public boolean isPepReporteGenerado() {
        return pepReporteGenerado;
    }

    public void setPepReporteGenerado(boolean pepReporteGenerado) {
        this.pepReporteGenerado = pepReporteGenerado;
    }

    public boolean isMostrarErroresPEP() {
        return mostrarErroresPEP;
    }

    public void setMostrarErroresPEP(boolean mostrarErroresPEP) {
        this.mostrarErroresPEP = mostrarErroresPEP;
    }

    public boolean isMostrarBloquearDesbloquearPEP() {
        return mostrarBloquearDesbloquearPEP;
    }

    public void setMostrarBloquearDesbloquearPEP(boolean mostrarBloquearDesbloquearPEP) {
        this.mostrarBloquearDesbloquearPEP = mostrarBloquearDesbloquearPEP;
    }

    public boolean isMostrarGenerarReportePEP() {
        return mostrarGenerarReportePEP;
    }

    public void setMostrarGenerarReportePEP(boolean mostrarGenerarReportePEP) {
        this.mostrarGenerarReportePEP = mostrarGenerarReportePEP;
    }

    public boolean isPacBloqueado() {
        return pacBloqueado;
    }

    public void setPacBloqueado(boolean pacBloqueado) {
        this.pacBloqueado = pacBloqueado;
    }

    public boolean isPacReporteGenerado() {
        return pacReporteGenerado;
    }

    public void setPacReporteGenerado(boolean pacReporteGenerado) {
        this.pacReporteGenerado = pacReporteGenerado;
    }

    public boolean isMostrarBloquearDesbloquearPAC() {
        return mostrarBloquearDesbloquearPAC;
    }

    public void setMostrarBloquearDesbloquearPAC(boolean mostrarBloquearDesbloquearPAC) {
        this.mostrarBloquearDesbloquearPAC = mostrarBloquearDesbloquearPAC;
    }

    public boolean isMostrarGenerarReportePAC() {
        return mostrarGenerarReportePAC;
    }

    public void setMostrarGenerarReportePAC(boolean mostrarGenerarReportePAC) {
        this.mostrarGenerarReportePAC = mostrarGenerarReportePAC;
    }

    public static Logger getLogger() {
        return logger;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    // </editor-fold>
}
