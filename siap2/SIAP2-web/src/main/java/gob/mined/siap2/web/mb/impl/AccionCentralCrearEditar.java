/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.utils.AnioFiscalUtils;
import gob.mined.siap2.business.utils.PlanificacionEstrategicaUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.ClasificadorFuncional;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.TechoAccionCentral;
import gob.mined.siap2.entities.data.impl.TechoActividadAccionCentral;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.AccionCentralDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.presentacion.tipos.DataMontoAnio;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página que crea o edita acciones centrales
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "accionCentralCE")
public class AccionCentralCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    protected DualListModel<LineaEstrategica> lineasEstrategicas;

    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    AccionCentralDelegate accionCentralD;
    
    @Inject
    private ReporteDelegate reporteDelegate;

    private List<UnidadTecnica> direciones;
    private List<DataMontoAnio> montosUT;

    private boolean update = false;
    private AccionCentral objeto;
    private TechoAccionCentral tempMontoUnidadTecnica;

    private String idPlanificacion;
    private List<Integer> aniosPlanificacion;
    private PlanificacionEstrategica planificacion;
    private String idClasificadorFuncional;
    private String idUnidadTecnica;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        direciones = versionDelegate.getUnidadesTecnicasDireccion();
        idPlanificacion = null;
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = versionDelegate.getAccionCentral(Integer.valueOf(id));
            if (objeto.getPlanificacionEstrategica() != null) {
                idPlanificacion = String.valueOf(objeto.getPlanificacionEstrategica().getId());
            }
            if (objeto.getClasificadorFuncional() != null) {
                idClasificadorFuncional = String.valueOf(objeto.getClasificadorFuncional().getId());
            }
            if (objeto.getUnidadTecnica() != null) {
                idUnidadTecnica = String.valueOf(objeto.getUnidadTecnica().getId());
            }
            loadLineasDisponibles(true);
        } else {
            objeto = new AccionCentral();
            objeto.setMontosUT(new LinkedList());
            objeto.setLineasEstrategicas(new HashSet<LineaEstrategica>());
            loadLineasDisponibles(false);
        }
        loadTechosUT();
    }

    /**
     * Carga los techos de las unidades técnicas
     */
    public void loadTechosUT() {
        aniosPlanificacion = new LinkedList();
 
        montosUT = new LinkedList();
        //se cargan los montos actuales de las direcciones
        for (TechoActividadAccionCentral montoUT : objeto.getMontosUT()) {
            List<TechoActividadAccionCentral> l = getMontosUT(montoUT.getUnidadTecnica());
            l.add(montoUT);
        }
        if (!TextUtils.isEmpty(idPlanificacion)) {
            //se cargan los anios de la planificacion en una lista
            planificacion = (PlanificacionEstrategica) emd.getEntityById(PlanificacionEstrategica.class.getName(), Integer.valueOf(idPlanificacion));
            Integer desde = DatesUtils.getYearOfDate(planificacion.getDesde());
            Integer hasta = DatesUtils.getYearOfDate(planificacion.getHasta());
            for (int anio = desde; anio <= hasta; anio++) {
                aniosPlanificacion.add(anio);
                AnioFiscal anioFiscal = null;
                List<AnioFiscal> aniosFiscales = emd.findByOneProperty(AnioFiscal.class.getName(), "anio", anio);
                if (!aniosFiscales.isEmpty()) {
                    anioFiscal = aniosFiscales.get(0);
                }else{
                    anioFiscal = AnioFiscalUtils.crearAnioFiscal(anio);
                }
                
                //se a;ade el techo para la direccion si no existe
                for (UnidadTecnica ut : direciones) {
                    agrreglarTechoParaDireccion(ut, anioFiscal, anio - desde);
                }
            }
        }
    }

    /**
     * agrega un techo a la dirección para el año en la posición pasada por
     * parámetro
     *
     * @param direccion
     * @param anio
     * @param pos
     */
    private void agrreglarTechoParaDireccion(UnidadTecnica direccion, AnioFiscal anioFiscal, int pos) {
        int iter = 0;
        boolean encontro = false;
        List<TechoActividadAccionCentral> montos = getMontosUT(direccion);

        while (iter < montos.size()) {
            if (montos.get(iter).getAnioFiscal().getAnio().equals(anioFiscal.getAnio())) {
                encontro = true;
                break;
            }
            iter++;
        }
        //si lo encontro en otra posicion lo cambia de lugar
        if (encontro && iter != pos) {
            TechoActividadAccionCentral techo = montos.get(iter);
            montos.remove(iter);
            montos.add(pos, techo);
        }

        if (!encontro) {
            TechoActividadAccionCentral techo = new TechoActividadAccionCentral();
            techo.setUnidadTecnica(direccion);
            techo.setAnioFiscal(anioFiscal);
            techo.setMonto(BigDecimal.ZERO);
            techo.setAccionCentral(objeto);
            montos.add(pos, techo);
        }
    }

    /**
     * retorna la lista de montos para la unidad técnica pasada por parámetro
     *
     * @param UT
     * @return
     */
    public List<TechoActividadAccionCentral> getMontosUT(UnidadTecnica UT) {
        for (DataMontoAnio monto : montosUT) {
            if (monto.getUt().equals(UT)) {
                return monto.getMontoAnios();
            }
        }
        DataMontoAnio monto = new DataMontoAnio();
        monto.setMontoAnios(new LinkedList());
        monto.setUt(UT);
        montosUT.add(monto);
        return monto.getMontoAnios();

    }

    /**
     * Guarda el objeto en edición 
     * 
     * @return 
     */
    public String guardar() {
        try {
            //se carga el clasificadorFuncioal
            if (TextUtils.isEmpty(idClasificadorFuncional)) {
                objeto.setClasificadorFuncional(null);
            } else {
                ClasificadorFuncional c = (ClasificadorFuncional) emd.getEntityById(ClasificadorFuncional.class.getName(), Integer.valueOf(idClasificadorFuncional));
                objeto.setClasificadorFuncional(c);
            }
            //se añaden los montos
            objeto.getMontosUT().clear();
            for (DataMontoAnio data : montosUT) {
                for (TechoActividadAccionCentral iter : (List<TechoActividadAccionCentral>) data.getMontoAnios()){
                    if (iter.getAnioFiscal().getId()!= null){
                        objeto.getMontosUT().add(iter);
                    }
                }
            }
            //se setea la ut
            UnidadTecnica u = null;
            if (!TextUtils.isEmpty(idUnidadTecnica)) {
                u = (UnidadTecnica) emd.getEntityById(UnidadTecnica.class.getName(), Integer.valueOf(idUnidadTecnica));
            }
            objeto.setUnidadTecnica(u);

            //se setean las lineas estrategicas
            objeto.setLineasEstrategicas(new HashSet(lineasEstrategicas.getTarget()));
            accionCentralD.crearOActualizarAccionCentral(Integer.valueOf(idPlanificacion), objeto);
            return "consultaAccionCentral.xhtml?faces-redirect=true";
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    /**
     * Este método corresponde al evento de descarga del reporte 
     */
    public void descargarReporte() {
        try {
            byte[] bytespdf = reporteDelegate.generarReporteAccionCentral(objeto.getId());
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();

            ec.setResponseContentType("application/pdf");

            ec.setResponseContentLength(bytespdf.length);

            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "ReporteAccionCentral.pdf" + "\"");
            OutputStream output;
            try {
                output = ec.getResponseOutputStream();
                output.write(bytespdf);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            }
            fc.responseComplete();

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método corresponde al evento de validación del monto de una unidad técnica.
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    public void validarMontoUT(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value != null && !TextUtils.isEmpty(value.toString())) {
            BigDecimal cantidadIngresada = new BigDecimal(value.toString());
            Integer anio = (Integer) component.getAttributes().get("anio");
            Integer idUT = (Integer) component.getAttributes().get("idUT");

            PlanificacionEstrategica planificcion = versionDelegate.getPlanificacionEstrategica(Integer.valueOf(idPlanificacion));
            BigDecimal total = PlanificacionEstrategicaUtils.getAccionCentralTechoUT(planificcion, idUT, anio);

            BigDecimal usado = accionCentralD.getMontoUsadoAccionCentralUTPlanificacion(planificcion.getId(), anio, idUT, objeto.getId());
            usado = usado.add(cantidadIngresada);

            if (usado.compareTo(total) > 0) {
                FacesMessage msg = new FacesMessage("Error al validar techo", "Error: la suma de los techos para el año ( " + usado + "), sobrepasa el total de la planificación (" + total + ")");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }

    /**
     * Este método corresponde al evento de cambio de planificación.
     */
    public void cambioPlanificacion() {
        loadLineasDisponibles(false);
        loadTechosUT();
    }

    /**
     * Este método corresponde a la carga de las líneas disponibles
     * @param conservarOriginales 
     */
    public void loadLineasDisponibles(boolean conservarOriginales) {
        List<LineaEstrategica> disponibles = new LinkedList<>();
        if (!TextUtils.isEmpty(idPlanificacion)) {
            disponibles = versionDelegate.getLineasEstrategicasVigetnes(Integer.valueOf(idPlanificacion));
        }
        List<LineaEstrategica> enUso = new LinkedList<>();
        if (conservarOriginales) {
            for (LineaEstrategica l : objeto.getLineasEstrategicas()) {
                enUso.add(l);
                if (disponibles.contains(l)) {
                    disponibles.remove(l);
                }
            }

        }
        lineasEstrategicas = new DualListModel(disponibles, enUso);
    }

    public String cerrar() {
        return "consultaAccionCentral.xhtml?faces-redirect=true";
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public DualListModel<LineaEstrategica> getLineasEstrategicas() {
        return lineasEstrategicas;
    }

    public PlanificacionEstrategica getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(PlanificacionEstrategica planificacion) {
        this.planificacion = planificacion;
    }

    public String getIdClasificadorFuncional() {
        return idClasificadorFuncional;
    }

    public void setIdClasificadorFuncional(String idClasificadorFuncional) {
        this.idClasificadorFuncional = idClasificadorFuncional;
    }

    public String getIdUnidadTecnica() {
        return idUnidadTecnica;
    }

    public void setIdUnidadTecnica(String idUnidadTecnica) {
        this.idUnidadTecnica = idUnidadTecnica;
    }

    public List<UnidadTecnica> getDireciones() {
        return direciones;
    }

    public void setDireciones(List<UnidadTecnica> direciones) {
        this.direciones = direciones;
    }

    public List<Integer> getAniosPlanificacion() {
        return aniosPlanificacion;
    }

    public void setAniosPlanificacion(List<Integer> aniosPlanificacion) {
        this.aniosPlanificacion = aniosPlanificacion;
    }

    public void setLineasEstrategicas(DualListModel<LineaEstrategica> lineasEstrategicas) {
        this.lineasEstrategicas = lineasEstrategicas;
    }



    public TechoAccionCentral getTempMontoUnidadTecnica() {
        return tempMontoUnidadTecnica;
    }

    public void setTempMontoUnidadTecnica(TechoAccionCentral tempMontoUnidadTecnica) {
        this.tempMontoUnidadTecnica = tempMontoUnidadTecnica;
    }

    public String getIdPlanificacion() {
        return idPlanificacion;
    }

    public void setIdPlanificacion(String idPlanificacion) {
        this.idPlanificacion = idPlanificacion;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public AccionCentral getObjeto() {
        return objeto;
    }

    public List<DataMontoAnio> getMontosUT() {
        return montosUT;
    }

    public void setMontosUT(List<DataMontoAnio> montosUT) {
        this.montosUT = montosUT;
    }

    public void setObjeto(AccionCentral objeto) {
        this.objeto = objeto;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
