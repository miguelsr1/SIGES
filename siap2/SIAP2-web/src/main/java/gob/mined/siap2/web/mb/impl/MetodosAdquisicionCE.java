/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.utils.MetodoAdquisicionUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicionRango;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicionTarea;
import gob.mined.siap2.entities.data.impl.Normativa;
import gob.mined.siap2.entities.enums.TipoTarea;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.StringsUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.CatalogoInsumosDelegate;
import gob.mined.siap2.web.delegates.impl.UACIDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "metodosAdquisicionCE")
public class MetodosAdquisicionCE implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    UACIDelegate uaciDelegate;
    @Inject
    CatalogoInsumosDelegate catalogoInsumosDelegate;
    @Inject
    VersionesDelegate versionesDelegate;

    private boolean update = false;
    private boolean editRango = false;
    private boolean editTarea = false;
    private MetodoAdquisicion objeto;
    private MetodoAdquisicionTarea objetoTarea;
    private MetodoAdquisicionRango objetoRango;
    private SofisComboG<Normativa> comboNormativas = new SofisComboG<Normativa>();
    private List<MetodoAdquisicionRango> rangosDelete = new ArrayList<>();
    private List<MetodoAdquisicionTarea> tareasDelete = new ArrayList<>();
    //tareas
    private String nombreTarea;
    private Integer inicioEnDias;
    private Integer duracionEnDias;
    private Integer predecesora;
    private TipoTarea tipoTarea;
    private boolean editInicioDias = true;

    //Rangos
    private Integer anio;
    private BigDecimal montoMin;
    private BigDecimal montoMax;
    private Integer contadorConfirmar;
    private String nombre2;
    private String metodoEsUACI;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = uaciDelegate.getMetodoAdquisicion(Integer.valueOf(id));
            if (objeto.getEsUACI() != null && objeto.getEsUACI()) {
                metodoEsUACI = "true";
            } else {
                metodoEsUACI = "false";
            }
        } else {
            objeto = createMetodoAdq();
            metodoEsUACI = null;
        }
        objetoTarea = new MetodoAdquisicionTarea();
        objetoRango = new MetodoAdquisicionRango();

        //cargo combo de normativas
        List<Normativa> normativas = versionesDelegate.getClasesGeneralCodiguera(Normativa.class);
        comboNormativas = new SofisComboG<>(normativas, "nombre");
        comboNormativas.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        if (update) {
            comboNormativas.setSelectedT(objeto.getNormativa());
        }
        contadorConfirmar = 0;
        MetodoAdquisicionUtils.ordenarTareasPorInicio(objeto.getTareas());

    }

    /**
     * Guarda el objeto en edición
     *
     * @return
     */
    public String guardar() {
        try {

            Normativa nor = comboNormativas.getSelectedT();
            if (nor != null) {
                objeto.setNormativa(nor);
            } else {
                objeto.setNormativa(null);
            }

            if (metodoEsUACI != null && metodoEsUACI.equals("true")) {
                objeto.setEsUACI(true);
            } else {
                objeto.setEsUACI(false);
            }

            if (update) {
                uaciDelegate.guardarOEditarMetodoAdquisicion(objeto, tareasDelete, rangosDelete);
            } else {
                uaciDelegate.guardarOEditarMetodoAdquisicion(objeto, null, null);
            }
            return "consultaMetodosAdquisicion.xhtml?faces-redirect=true";
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
     * Deshabilita un método de adquisición
     *
     * @return
     */
    public String deshabilitar() {
        try {

            contadorConfirmar++;
            List<String> nombresPacs = uaciDelegate.obtenerNombresPacsAsociadosNoFinalizados(objeto.getId());
            if (nombresPacs.isEmpty()) {
                objeto.setHabilitado(false);
                uaciDelegate.guardarOEditarMetodoAdquisicion(objeto, null, null);
                RequestContext.getCurrentInstance().execute("$('#modalDeshabilitar').modal('hide');");
                return "consultaMetodosAdquisicion.xhtml?faces-redirect=true";
            } else {
                if (contadorConfirmar < 2) {
                    String texto = "";
                    int i = 0;
                    for (String nombresPac : nombresPacs) {
                        if (i == 0) {
                            texto += nombresPac;
                        } else {
                            texto += ", " + nombresPac;
                        }

                        i++;
                    }

                    String params = "#" + texto + "#";
                    jSFUtils.mostrarAdvertenciaByPropertieCode(ConstantesErrores.ADVERTENCIA_ADQUISICION_DEHABILITAR + params);
                } else {
                    objeto.setHabilitado(false);
                    uaciDelegate.guardarOEditarMetodoAdquisicion(objeto, null, null);
                    RequestContext.getCurrentInstance().execute("$('#modalDeshabilitar').modal('hide');");
                    return "consultaMetodosAdquisicion.xhtml?faces-redirect=true";
                }

            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
        return null;

    }

    /**
     * Cancela la deshabilitación de un método de adquisición
     */
    public void cancelarDeshabilitar() {
        contadorConfirmar = 0;
        RequestContext.getCurrentInstance().execute("$('#modalDeshabilitar').modal('hide');");
    }

    /**
     * Dirige al sitio de la consulta de métodos de adquisición
     *
     * @return
     */
    public String cerrar() {
        return "consultaMetodosAdquisicion.xhtml?faces-redirect=true";
    }

    public void agregarTarea() {
        try {
            controlTarea();
            objetoTarea.setMetodo(objeto);
            objetoTarea.setDuracionEnDias(duracionEnDias);
            objetoTarea.setInicioEnDias(inicioEnDias);
            objetoTarea.setTipoTarea(tipoTarea);
            objetoTarea.setNombre(nombreTarea);
            if (predecesora != -1) {
                MetodoAdquisicionTarea tareaPredecesora = objeto.getTareas().get(predecesora);
                objetoTarea.setPredecesora(tareaPredecesora);

            }
            objeto.getTareas().add(objetoTarea);
            MetodoAdquisicionUtils.ordenarTareasPorInicio(objeto.getTareas());
            limpiarTarea();
            RequestContext.getCurrentInstance().execute("$('#agregarMetodoAdqModal').modal('hide')");
        } catch (GeneralException ex) {
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            logger.log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Resetea los atributos del método de adquisición
     */
    public void limpiarTarea() {
        objetoTarea = new MetodoAdquisicionTarea();
        duracionEnDias = 0;
        inicioEnDias = 0;
        nombreTarea = "";
        predecesora = null;
        editTarea = false;
        tipoTarea = null;

    }

    /**
     * Resetea los atributos de un rango de método de adquisición
     */
    public void limpiarRango() {
        objetoRango = new MetodoAdquisicionRango();
        anio = 0;
        montoMax = BigDecimal.ZERO;
        montoMin = BigDecimal.ZERO;
        editRango = false;
    }

    /**
     * Carga una tarea de método de adquisición en un pop-up
     */
    public void editarTarea() {
        try {
            controlTarea();
            objetoTarea.setDuracionEnDias(duracionEnDias);
            objetoTarea.setInicioEnDias(inicioEnDias);
            objetoTarea.setTipoTarea(tipoTarea);
            objetoTarea.setNombre(nombreTarea);
            if (predecesora != -1) {
                MetodoAdquisicionTarea tareaPredecesora = objeto.getTareas().get(predecesora);
                objetoTarea.setPredecesora(tareaPredecesora);
                editInicioDias = false;
            } else {
                objetoTarea.setPredecesora(null);
                editInicioDias = true;
            }

            MetodoAdquisicionUtils.ordenarTareasPorInicio(objeto.getTareas());
            limpiarTarea();
            RequestContext.getCurrentInstance().execute("$('#agregarMetodoAdqModal').modal('hide')");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        }

    }

    /**
     * Carga un rango de un método de adquisición en un pop-up
     */
    public void agregarRango() {
        try {
            controlRango();
            objetoRango.setMetodo(objeto);
            objetoRango.setAnio(anio);
            objetoRango.setMontoMax(montoMax);
            objetoRango.setMontoMin(montoMin);

            objeto.getRangos().add(objetoRango);
            limpiarRango();

            RequestContext.getCurrentInstance().execute("$('#agregarRangoPorAnioModal').modal('hide')");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        }
    }

    /**
     * Carga un rango de un método de adquisición en un pop-up
     */
    public void editarRango() {
        try {
            controlRango();
            objetoRango.setAnio(anio);
            objetoRango.setMontoMax(montoMax);
            objetoRango.setMontoMin(montoMin);
            limpiarRango();

            RequestContext.getCurrentInstance().execute("$('#agregarRangoPorAnioModal').modal('hide')");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Carga una tarea de un método de adquisición
     */
    public void cargarTarea() {
        duracionEnDias = objetoTarea.getDuracionEnDias();
        tipoTarea = objetoTarea.getTipoTarea();
        inicioEnDias = objetoTarea.getInicioEnDias();
        nombreTarea = objetoTarea.getNombre();
        if (objetoTarea.getPredecesora() != null) {
            predecesora = objeto.getTareas().indexOf(objetoTarea.getPredecesora());
            editInicioDias = false;
        } else {            
            editInicioDias = true;
        }

        this.editTarea = true;
    }

    /**
     * Carga los datos de un rango de un método de adquisición
     */
    public void cargarRango() {
        anio = objetoRango.getAnio();
        montoMax = objetoRango.getMontoMax();
        montoMin = objetoRango.getMontoMin();

        this.editRango = true;
    }

    /**
     * Verifica que la tarea de un método de adquisición sea valida
     *
     * @throws BusinessException
     */
    public void controlTarea() throws BusinessException {
        BusinessException b = new BusinessException();
        if (StringsUtils.isEmpty(nombreTarea)) {
            b.addError(ConstantesErrores.ERR_NOMBRE_ADQUISICION_TAREA_VACIO);
        }
        if (tipoTarea == TipoTarea.RECEPCION_DE_TDR_O_ESPECIFICACIONES_TECNICAS) {
            MetodoAdquisicionTarea temp = MetodoAdquisicionUtils.findTarea(objeto.getTareas(), TipoTarea.RECEPCION_DE_TDR_O_ESPECIFICACIONES_TECNICAS);
            if (temp != null && !objetoTarea.equals(temp)) {
                b.addError(ConstantesErrores.ERR_ADQUISICION_TAREA_ENTREGA_TDR);
            }
        }
        if (tipoTarea == TipoTarea.CONTRATACION) {
            MetodoAdquisicionTarea temp = MetodoAdquisicionUtils.findTarea(objeto.getTareas(), TipoTarea.CONTRATACION);
            if (temp != null && !objetoTarea.equals(temp)) {
                b.addError(ConstantesErrores.ERR_ADQUISICION_TAREA_INICIO_CONTRATACION);
            }
        }

        if (duracionEnDias <= 0) {
            b.addError(ConstantesErrores.ERR_ADQUISICION_TAREA_DURACION_DIAS);
        }
        if (inicioEnDias < 0) {
            b.addError(ConstantesErrores.ERR_ADQUISICION_TAREA_INICIO_DIAS);
        }

        if (predecesora != -1) {
            MetodoAdquisicionTarea tareaPredecesora = objeto.getTareas().get(predecesora);
            if (objetoTarea.equals(tareaPredecesora)) {
                b.addError(ConstantesErrores.ERR_ADQUISICION_TAREA_PREDECESORA_DE_SI_MISMA);
            }
            Integer inicioExigido = tareaPredecesora.getInicioEnDias() + tareaPredecesora.getDuracionEnDias();
            if (inicioExigido > inicioEnDias) {
                b.addError(ConstantesErrores.ERR_ADQUISICION_TAREA_INICIO_DIAS_INCORRECTO);
            }
        }

        if (!b.getErrores().isEmpty()) {
            throw b;
        }

    }

    /**
     * Verifica que el rango de un método de adquisición sea valido
     *
     * @throws BusinessException
     */
    public void controlRango() throws BusinessException {
        BusinessException b = new BusinessException();

        Iterator<MetodoAdquisicionRango> it = objeto.getRangos().iterator();
        boolean yaExisteRango = false;
        while (!yaExisteRango && it.hasNext()) {
            MetodoAdquisicionRango next = it.next();
            if (next.getAnio().equals(anio) && !next.equals(objetoRango)) {
                b.addError(ConstantesErrores.ERR_ADQUISICION_RANGO_ANIO_EXISTE);
                yaExisteRango = true;
            }

        }
        if (montoMax.compareTo(BigDecimal.ZERO) < 0) {
            b.addError(ConstantesErrores.ERR_ADQUISICION_RANGO_MONTO_MAX);
        }
        if (montoMin.compareTo(BigDecimal.ZERO) < 0) {
            b.addError(ConstantesErrores.ERR_ADQUISICION_RANGO_MONTO_MIN);
        }

        if (montoMax.compareTo(montoMin) <= 0) {
            b.addError(ConstantesErrores.ERR_ADQUISICION_RANGO_MONTO_MAXIMO_MENOR_IGUAL_MONTO_MINIMO);
        }
        if (anio.equals(0)) {
            b.addError(ConstantesErrores.ERR_ADQUISICION_RANGO_ANIO);
        }
        if (String.valueOf(anio).length() != 4) {
            b.addError(ConstantesErrores.ERR_ADQUISICION_RANGO_ANIO_INVALIDO);
        }

        if (update) {
            boolean exiteRango = false;
            for (MetodoAdquisicionRango rango : objeto.getRangos()) {
                if (rango.getAnio().equals(anio)) {
                    exiteRango = true;
                    break;
                }
            }
            if (exiteRango) {
                Boolean existePacs = uaciDelegate.existePacsAnioRangoAsociadosPorMetIdNoCerrado(anio, objeto.getId());
                if (existePacs) {

                    b.addError(ConstantesErrores.ERR_ADQUISICION_RANGO_EXISTE_PAC_DERIVADO_POA_ANIO_NO_CERRADO);

                }
            }

        }

        if (!b.getErrores().isEmpty()) {
            throw b;
        }

    }

    /**
     * Elimina una tarea de un método de adquisición
     *
     * @param tarea
     */
    public void eliminarTarea(MetodoAdquisicionTarea tarea) {
        try {
            for (MetodoAdquisicionTarea t : objeto.getTareas()) {
                if (t.getPredecesora() != null && t.getPredecesora().equals(tarea)) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_ADQUISICION_TAREA_DELETE_PREDECESORA);
                    throw b;
                }
            }
            objeto.getTareas().remove(tarea);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
            if (update) {
                tareasDelete.add(tarea);
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina un rango de un método de adquisición
     *
     * @param rango
     */
    public void eliminarRango(MetodoAdquisicionRango rango) {
        try {
            if (update) {
                Boolean existePacs = uaciDelegate.existePacsAnioRangoAsociadosPorMetIdNoCerrado(rango.getAnio(), objeto.getId());
                if (existePacs) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_ADQUISICION_RANGO_EXISTE_PAC_DERIVADO_POA_ANIO_NO_CERRADO);
                    throw b;
                }
                rangosDelete.add(rango);
            }
            objeto.getRangos().remove(rango);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Crea un método de adquisición con sus atributos vacíos
     *
     * @return
     */
    private MetodoAdquisicion createMetodoAdq() {
        MetodoAdquisicion m = new MetodoAdquisicion();
        m.setRangos(new ArrayList<MetodoAdquisicionRango>());
        m.setTareas(new ArrayList<MetodoAdquisicionTarea>());
        m.setHabilitado(true);
        return m;

    }

    /**
     * Calcula el inicio en días de una tarea que tiene predecesora
     */
    public void calculoInicioDias() {

        if (predecesora != -1) {
            MetodoAdquisicionTarea tareaPredecesora = objeto.getTareas().get(predecesora);

            inicioEnDias = tareaPredecesora.getDuracionEnDias() + tareaPredecesora.getInicioEnDias() + 1;

            editInicioDias = false;
        } else {
            editInicioDias = true;
        }

    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public static Logger getLogger() {
        return logger;
    }

    public MetodoAdquisicion getObjeto() {
        return objeto;
    }

    public void setObjeto(MetodoAdquisicion objeto) {
        this.objeto = objeto;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public MetodoAdquisicionTarea getObjetoTarea() {
        return objetoTarea;
    }

    public void setObjetoTarea(MetodoAdquisicionTarea objetoTarea) {
        this.objetoTarea = objetoTarea;
    }

    public Map getTareasCombo() {
        Map tareasMap = new HashMap();

        for (MetodoAdquisicionTarea t : objeto.getTareas()) {
            if (!t.equals(objetoTarea)) {
                tareasMap.put(t.getNombre(), objeto.getTareas().indexOf(t));
            }

        }
        return tareasMap;
    }

    public MetodoAdquisicionRango getObjetoRango() {
        return objetoRango;
    }

    public void setObjetoRango(MetodoAdquisicionRango objetoRango) {
        this.objetoRango = objetoRango;
    }

    public boolean getEditRango() {
        return editRango;
    }

    public void setEditRango(boolean editarRango) {
        this.editRango = editarRango;
    }

    public boolean getEditTarea() {
        return editTarea;
    }

    public void setEditTarea(boolean editarTarea) {
        this.editTarea = editarTarea;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public Integer getInicioEnDias() {
        return inicioEnDias;
    }

    public void setInicioEnDias(Integer inicioEnDias) {
        this.inicioEnDias = inicioEnDias;
    }

    public Integer getDuracionEnDias() {
        return duracionEnDias;
    }

    public void setDuracionEnDias(Integer duracionEnDias) {
        this.duracionEnDias = duracionEnDias;
    }

    public Integer getPredecesora() {
        return predecesora;
    }

    public void setPredecesora(Integer predecesora) {
        this.predecesora = predecesora;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getMontoMin() {
        return montoMin;
    }

    public void setMontoMin(BigDecimal montoMin) {
        this.montoMin = montoMin;
    }

    public BigDecimal getMontoMax() {
        return montoMax;
    }

    public void setMontoMax(BigDecimal montoMax) {
        this.montoMax = montoMax;
    }

    public boolean isEditInicioDias() {
        return editInicioDias;
    }

    public void setEditInicioDias(boolean editInicioDias) {
        this.editInicioDias = editInicioDias;
    }

    public String getNombre2() {
        return nombre2;
    }

    public TipoTarea getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(TipoTarea tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getMetodoEsUACI() {
        return metodoEsUACI;
    }

    public void setMetodoEsUACI(String metodoEsUACI) {
        this.metodoEsUACI = metodoEsUACI;
    }

    public SofisComboG<Normativa> getComboNormativas() {
        return comboNormativas;
    }

    public void setComboNormativas(SofisComboG<Normativa> comboNormativas) {
        this.comboNormativas = comboNormativas;
    }

    // </editor-fold>
}
