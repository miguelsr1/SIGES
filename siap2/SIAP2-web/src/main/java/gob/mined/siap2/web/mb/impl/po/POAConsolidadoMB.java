/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.business.utils.POAUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ActividadAsignacionNP;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POActividadAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que se utiliza para consolidar los POA de acción central y asignación no 
 * programable
 *
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaConsolidadoMB")
public class POAConsolidadoMB extends POAConActividadesBasico implements Serializable {

    private static final long serialVersionUID = 1L;
    protected static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private static final String PAGE_VOLVER = "consultaPOAporACoANP.xhtml";

    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;

    protected String id;
    protected String tipo;
    protected TipoMontoPorAnio tmpa = null;
    protected Integer idActividadNP;
    protected List<POAConActividades> listPoa;
    protected boolean completoParaConsolidado = false;

    @PostConstruct
    public void init() {
        super.init();
        id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        tipo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tipo");
        idAnioFiscal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idAnioFiscal");
        idUnidadTecnica = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUnidadTecnica");


        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(tipo)) {
            update = true;
            if (tipo.equalsIgnoreCase(TipoMontoPorAnio.ACCION_CENTRAL.name())) {
                tmpa = TipoMontoPorAnio.ACCION_CENTRAL;
                objeto = versionDelegate.getAccionCentral(Integer.valueOf(id));
            } else if (tipo.equalsIgnoreCase(TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE.name())) {
                tmpa = TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE;
                objeto = versionDelegate.getAsignacionNoProgramable(Integer.valueOf(id));
            } else {
                objeto = null;
            }
        }
        initConMontoPorAnio();
    }

    /**
     * Este método es utilizado para realizar la inicialización.
     */
    public void initConMontoPorAnio() {
        try {
            if (!TextUtils.isEmpty(idAnioFiscal)) {
                listPoa = pOAConActividadesDelegate.getPOAsTrabajo(objeto.getId(), Integer.valueOf(idAnioFiscal), EstadoPOA.CONSOLIDANDO_POA);
                completoParaConsolidado = pOAConActividadesDelegate.isCompletoParaConsolidado(objeto.getId(), Integer.valueOf(idAnioFiscal)) && (listPoa != null && !listPoa.isEmpty());
            }

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método recarga el POA
     */
    @Override
    public void reloadPO() {
        initConMontoPorAnio();
    }
    
    
    /**
     * Este método inicializa un actividad
     */
    @Override
    public void initActividad() {
        // poa= buscarPOAparaUt(utId)
        if (tmpa.equals(TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE)) {
            if (tempActividad == null) {
                tempActividad = new POActividadAsignacionNoProgramable();
                tempActividad.setEstado(EstadoActividadPOA.NO_FINALIZADA);
                tempActividad.setInsumos(new LinkedList());
            }
            POActividadAsignacionNoProgramable tanp = (POActividadAsignacionNoProgramable) tempActividad;
            if (tanp.getResponsable() == null) {
                idUsuarioActividad = null;
            } else {
                idUsuarioActividad = tanp.getResponsable().getUsuId().toString();
            }
            if (tanp.getActividadNP() == null) {
                idUsuarioActividad = null;
            } else {
                idActividadNP = tanp.getActividadNP().getId();
            }
        } else {
            super.initActividad();
        }
    }

    /**
     * Se sobre-escribe el método de guardar actividad ya que hay que ver a que POA se guarda
     * @param actividad 
     */
    @Override
    public void guardarActividad(POActividadBase actividad) {
        if (tmpa.equals(TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE)) {
            if (!poa.getActividades().contains(actividad)) {
                //actividad.setLineaPOProyecto(tempLinea);
                poa.getActividades().add(actividad);
            }
            ActividadAsignacionNP anp = (ActividadAsignacionNP) emd.getEntityById(ActividadAsignacionNP.class.getName(), idActividadNP);
            ((POActividadAsignacionNoProgramable) actividad).setActividadNP(anp);
        } else {
            super.guardarActividad(actividad);
        }
    }


    /**
     * Este método envía para validar el POA
     * 
     * @throws IOException 
     */
    public void enviar() throws IOException {
        try {
            podaAConActividadesDelegate.enviarPOAConActividades(objeto.getId(), poa.getId(), tmpa);
            FacesContext.getCurrentInstance().getExternalContext().redirect(PAGE_VOLVER);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método consolida el POA
     * 
     * @throws IOException 
     */
    public void consolidar() throws IOException {
        try {            
            pOAConActividadesDelegate.consolidar(listPoa, objeto.getId());
            
            FacesContext.getCurrentInstance().getExternalContext().redirect(PAGE_VOLVER);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método se utiliza para rechazar el POA
     * 
     * @throws IOException 
     */
    public void rechazarPOA() throws IOException {
        try {
            podaAConActividadesDelegate.rechazarPOA(poa.getId(), objeto.getId(), motivoRechazo, tmpa);
            FacesContext.getCurrentInstance().getExternalContext().redirect(PAGE_VOLVER);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método retorna el total disponible en el año.
     * 
     * @return 
     */
    public BigDecimal getSumaMontoTotal() {
        if (objeto != null && !TextUtils.isEmpty(idAnioFiscal)) {
            AnioFiscal anioFiscal = (AnioFiscal) emd.getEntityById(AnioFiscal.class.getName(), Integer.valueOf(idAnioFiscal));
            return POAUtils.getMontoTotal(objeto, anioFiscal);
        }

 
        return null;
    }

    /**
     * Este método retorna lo usado por la lista de POA
     * 
     * @return 
     */
    public BigDecimal getSumaMontoUsado() {
        if (listPoa != null) {
            BigDecimal montoUsado = BigDecimal.ZERO;
            for (POAConActividades pOA : listPoa) {
                for (POActividadBase a : pOA.getActividades()) {
                    for (POInsumos i : a.getInsumos()) {
                        montoUsado = montoUsado.add(i.getMontoTotal());
                    }
                }
            }
            return montoUsado;
        }
        return null;
    }

    /**
     * Este método retorna el saldo
     * 
     * @return 
     */
    public BigDecimal getSumaSaldo() {
        BigDecimal total = getSumaMontoTotal();
        if (total == null) {
            return null;
        }
        return total.subtract(getSumaMontoUsado());
    }

    /**
     * Este método retorna si es un POA de acción central
     * 
     * @return 
     */
    public boolean isAccionCentral() {
        return tmpa != null && tmpa.equals(TipoMontoPorAnio.ACCION_CENTRAL);
    }


    /***
     * Este método retorna el tipo de POA
     * @return 
     */
    @Override
    public String getTipoPO() {
        if (esActividadNoProgramable()) {
            return POConActividadesEInsumosAbstract.TIPO_PO_ASIGNACION_NP;
        } else {
            return POConActividadesEInsumosAbstract.TIPO_PO_ACCION_CENTRAL;
        }
    }

    
    /**
     * Este método retorna si ya se realizo el cierre anual
     * @return 
     */
    public Boolean cirreAnualCompleto(){
        if (listPoa == null || listPoa.isEmpty()){
            return null;
        }
        
        for (POAConActividades p :listPoa ){
            if (p.getCierreAnual() == null || p.getCierreAnual().booleanValue() == false){
                return false;
            }
        }
        return true;
    }
    
    
    
    
    //@Override
    /**
     * Este método retorna si las actividad es de asignación no programable
     * 
     * @return 
     */
    public boolean esActividadNoProgramable() {
        return tmpa != null && tmpa.equals(TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE);
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getIdAccionCentral() {
        return id;
    }

    public void setIdAccionCentral(String idAccionCentral) {
        this.id = idAccionCentral;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public VersionesDelegate getVersionDelegate() {
        return versionDelegate;
    }

    public void setVersionDelegate(VersionesDelegate versionDelegate) {
        this.versionDelegate = versionDelegate;
    }


    public List<POAConActividades> getListPoa() {
        return listPoa;
    }

    public void setListPoa(List<POAConActividades> listPoa) {
        this.listPoa = listPoa;
    }

    public boolean isCompletoParaConsolidado() {
        return completoParaConsolidado;
    }

    public void setCompletoParaConsolidado(boolean completoParaConsolidado) {
        this.completoParaConsolidado = completoParaConsolidado;
    }

    public Integer getIdActividadNP() {
        return idActividadNP;
    }

    public void setIdActividadNP(Integer idActividadNP) {
        this.idActividadNP = idActividadNP;
    }
    // </editor-fold>
}
