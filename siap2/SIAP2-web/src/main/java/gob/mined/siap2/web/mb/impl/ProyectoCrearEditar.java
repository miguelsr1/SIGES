/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import com.google.gson.Gson;
import gob.mined.siap2.business.datatype.ProyectoArbol;
import gob.mined.siap2.business.utils.ProyectoUtils;
import gob.mined.siap2.business.utils.ProyectoValidacion;
import gob.mined.siap2.entities.constantes.ConstantesConfiguracion;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.Convenio;
import gob.mined.siap2.entities.data.impl.FuenteFinanciamiento;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.MacroActividad;
import gob.mined.siap2.entities.data.impl.ProgramaIndicador;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporte;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.ProyectoCategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ProyectoCoEjecutora;
import gob.mined.siap2.entities.data.impl.ProyectoComponente;
import gob.mined.siap2.entities.data.impl.ProyectoDesembolso;
import gob.mined.siap2.entities.data.impl.ProyectoDocumentos;
import gob.mined.siap2.entities.data.impl.ProyectoEnmienda;
import gob.mined.siap2.entities.data.impl.ProyectoEstPorcentajeFuente;
import gob.mined.siap2.entities.data.impl.ProyectoEstProducto;
import gob.mined.siap2.entities.data.impl.ProyectoEstructura;
import gob.mined.siap2.entities.data.impl.ProyectoFuente;
import gob.mined.siap2.entities.data.impl.ProyectoMacroActividad;
import gob.mined.siap2.entities.data.impl.ProyectoParipassuTramo;
import gob.mined.siap2.entities.data.impl.ProyectoProrroga;
import gob.mined.siap2.entities.data.impl.TipoDocumento;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoProyecto;
import gob.mined.siap2.entities.enums.TipoEstructura;
import gob.mined.siap2.entities.enums.TipoMontoEstructura;
import gob.mined.siap2.entities.enums.TipoProyecto;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.ListUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import gob.mined.siap2.web.delegates.ConfiguracionDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.ProyectoDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.delegates.impl.ConvenioDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UtilsMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la
 * página que crea o edita un proyecto
 *
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "proyectoCE")
public class ProyectoCrearEditar extends SelectOneUTBean implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    UtilsMB utilsMB;
    @Inject
    ReporteDelegate reporteDelegate;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    ProyectoDelegate proyectoDelegate;
    @Inject
    ArchivoDelegate archivoDelegate;
    @Inject
    TextMB textMB;
    @Inject
    ConfiguracionDelegate configuracionDelegate;
    @Inject
    UsuarioDelegate usuarioDelegate;
    @Inject
    ConvenioDelegate convenioDelegate;

    private boolean update = false;
    private Proyecto objeto;

    private String idProgramaPresupuestario;
    private String idSubProgramaPresupuestario;
    private String idProgramaInstitucional;
    private List<TipoProyecto> tipoProyectosDisponibles = new ArrayList();
    private String restriccionXTipoProyecto;
    private Integer activeTab = 0;
    private ProyectoProrroga tmpProrroga;
    private ProyectoAporte tmpAporte;
    private String idCategoria;
    private ProyectoCategoriaConvenio tmpProyCategoriaConvenio;
    private List<ProyectoAporte> tmpFuentesParipassu;
    private ProyectoAporteTramoTramo tempTramo = null;
    private ProyectoDesembolso tmpDesembolso;
    private ProyectoEnmienda tmpEnmienda;
    private TreeNode selectedNode;
    private boolean ocultarFuentes = false;
    private boolean ocultarProductos = false;    
    private ProyectoComponente tmpComponente = null;
    private String tempUnidadTecnicaComponente;
    private String tempComponentePadreIndex;
    private ProyectoComponente tmpComponentePadre = null;
    private String unidadTecnicaId;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        //verifica si va a crear un proyecto nuevo o traer uno existente
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = proyectoDelegate.getProyecto(Integer.valueOf(id));
            if (objeto.getProgramaInstitucional() != null) {
                idProgramaInstitucional = objeto.getProgramaInstitucional().getId().toString();
            }
            if (objeto.getProgramaPresupuestario() != null) {
                idSubProgramaPresupuestario = objeto.getProgramaPresupuestario().getId().toString();
                idProgramaPresupuestario = objeto.getProgramaPresupuestario().getProgramaPresupuestario().getId().toString();
            }

        } else {
            objeto = new Proyecto();
            objeto.setEstado(EstadoProyecto.EN_FORMULACION);
            objeto.setPasoActual(0);
            objeto.setTipoEstructuraComponente(false);
            objeto.setTipoEstructuraMacroactividad(false);
            objeto.setProrrogas(new LinkedList());
            objeto.setAportesProyecto(new LinkedList());
            objeto.setProyectoDesembolso(new LinkedList());
            objeto.setProyectoEnmienda(new LinkedList());
            objeto.setProyectoCoEjecutoras(new LinkedList());
            objeto.setProyectoDocumentos(new LinkedList());
            objeto.setProyectoComponentes(new LinkedList());
            objeto.setProyectoMacroactividad(new LinkedList());
            objeto.setFuentesProyecto(new LinkedList());
            objeto.setDistribuccionCategorias(new LinkedList<ProyectoCategoriaConvenio>());
            objeto.setIndicadoresAsociados(new LinkedList<ProgramaIndicador>());
        }
        //Carga los tipo de proyectos disponibles para los filtros
        restriccionXTipoProyecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("restriccionXTipoProyecto");
        if (restriccionXTipoProyecto != null) {
            switch (restriccionXTipoProyecto) {
                case "1":
                    tipoProyectosDisponibles.add(TipoProyecto.ADMINISTRATIVO);
                    break;
                case "2":
                    tipoProyectosDisponibles.add(TipoProyecto.INVERSION);
                    tipoProyectosDisponibles.add(TipoProyecto.NO_INVERSION);
                    break;
            }
        } else {
            tipoProyectosDisponibles.add(TipoProyecto.ADMINISTRATIVO);
            tipoProyectosDisponibles.add(TipoProyecto.INVERSION);
            tipoProyectosDisponibles.add(TipoProyecto.NO_INVERSION);
        }
        convenioSelecionado = objeto.getConvenio();
        updateActiveTab();
        reloadProyectoTree();
    }

    /**
     * Este método retorna si solo se esta trabajando con proyectos
     * administrativos
     *
     * @return
     */
    public boolean esRestriccionSoloAdministrativos() {
        if (tipoProyectosDisponibles.size() == 1) {
            return tipoProyectosDisponibles.get(0).equals(TipoProyecto.ADMINISTRATIVO);
        }
        return false;
    }

    /**
     * si el no a completado todos los pasos del proyecto actualiza el tab al
     * paso actual
     */
    private void updateActiveTab() {
        //fixme poner en 4
        if (objeto.getPasoActual() <= 2) {
            activeTab = objeto.getPasoActual();
        }
    }

    /**
     * Este método es llamado cuando se actualiza el proyecto. Actualiza el tab
     * abierto y el el javascript que se muestra la estructura del proyecto
     */
    private void reloadProyecto() {
        updateActiveTab();
        reloadProyectoTree();
    }

    /**
     * Este método es el encargado de volver a traer el proyecto desde la base y
     * actualizar
     */
    private void reloadProyectoFromBase() {
        objeto = proyectoDelegate.getProyecto(objeto.getId());
        convenioSelecionado = objeto.getConvenio();
        reloadProyecto();
    }

    /**
     * guarda todo el proyecto
     *
     * @return
     */
    public String guardar() {
        try {
            objeto = proyectoDelegate.crearOActualizarProyecto(objeto, activeTab);
            reloadProyecto();

            String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");

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
     * pone el proyecto en estado cerrado
     *
     * @return
     */
    public String cerrarProyecto() {
        try {
            proyectoDelegate.cerrarProyecto(objeto.getId());
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .put("restriccionXTipoProyecto", restriccionXTipoProyecto);
            return "consultaProyecto.xhtml?faces-redirect=true&restriccionXTipoProyecto=" + restriccionXTipoProyecto;
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
     * retrocede una página
     *
     * @return
     */
    public String cerrar() {
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getFlash()
                .put("restriccionXTipoProyecto", restriccionXTipoProyecto);
        return "consultaProyecto.xhtml?faces-redirect=true&&restriccionXTipoProyecto=" + restriccionXTipoProyecto;
    }

    /**
     * asigna los programas presupuestarios seleccionados en el modal
     */
    public void asignarProgramas() {
        try {

            if (!TextUtils.isEmpty(idSubProgramaPresupuestario)) {
                ProgramaPresupuestario p = (ProgramaPresupuestario) emd.getEntityById(ProgramaPresupuestario.class.getName(), Integer.valueOf(idSubProgramaPresupuestario));
                objeto.setProgramaPresupuestario(p);
            } else {
                objeto.setProgramaPresupuestario(null);
            }
            if (!TextUtils.isEmpty(idProgramaInstitucional)) {
                ProgramaInstitucional p = (ProgramaInstitucional) emd.getEntityById(ProgramaInstitucional.class.getName(), Integer.valueOf(idProgramaInstitucional));
                objeto.setProgramaInstitucional(p);
            } else {
                objeto.setProgramaInstitucional(null);
            }

            RequestContext.getCurrentInstance().execute("$('#anadirAsignarPrograma').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * descarga el reportes
     */
    public void descargarReporte() {
        try {
            if (objeto.getId() != null) {
                byte[] bytespdf = reporteDelegate.generarReporteProyecto(objeto.getId());

                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                ec.responseReset();
                ec.setResponseContentType("application/pdf");
                ec.setResponseContentLength(bytespdf.length);
                String nombreArchivo = "ReporteProyecto_" + objeto.getId() + ".pdf";
                ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"");
                OutputStream output = ec.getResponseOutputStream();
                output.write(bytespdf);
                fc.responseComplete();
            } else {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_GUARDAR_EL_PROYECTO_ANTES_DE_GENERAR_REPORTE);
                throw b;
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * retorna el id del convenio del proyecto
     *
     * @return
     */
    public Integer getIdConvenio() {
        if (objeto.getConvenio() == null) {
            return null;
        }
        return objeto.getConvenio().getId();
    }

    /**
     * valida la financiación del proyecto y agrega el error en el campo de
     * mensajes
     */
    public void validarFinanciacion() {
        try {
            ProyectoValidacion.validarFinanciacion(this.objeto);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * LOGICA PARA EL MANEJO DE LOS APORTES DE FUENTES DEL PROYECTO
     */
    // <editor-fold defaultstate="collapsed" desc="FUENTES PROYECTO">
    private ProyectoFuente tmpFuente;

    private String tmpIdFuenteFinanciamiento;
    private String tmpIdFuenteRecurso;

    /**
     * Este método es el encargado de inicializar una fuente
     */
    public void reloadFuente() {
        if (tmpFuente == null) {
            tmpIdFuenteFinanciamiento = null;
            tmpIdFuenteRecurso = null;
            tmpFuente = new ProyectoFuente();
        } else {
            tmpIdFuenteRecurso = String.valueOf(tmpFuente.getFuenteRecursos().getId());
            tmpIdFuenteFinanciamiento = String.valueOf(tmpFuente.getFuenteFinanciamiento().getId());
        }
    }

    /**
     * Este método es el encargado de guardar una fuente
     */
    public void saveFuente() {
        try {
            tmpFuente.setFuenteFinanciamiento((FuenteFinanciamiento) emd.getEntityById(FuenteFinanciamiento.class.getName(), Integer.valueOf(tmpIdFuenteFinanciamiento)));
            tmpFuente.setFuenteRecursos((FuenteRecursos) emd.getEntityById(FuenteRecursos.class.getName(), Integer.valueOf(tmpIdFuenteRecurso)));

            //valida que las fuentes no se pasen dl monto del proyecto
            if (objeto.getMontoGlobalEnConstruccion().compareTo(BigDecimal.ZERO) != 0) {
                //valida las fuentes
                BigDecimal totalFuentes = BigDecimal.ZERO;
                for (ProyectoFuente fuente : objeto.getFuentesProyecto()) {
                    if (fuente.getMonto() != null && !fuente.equals(tmpFuente)) {
                        totalFuentes = totalFuentes.add(fuente.getMonto());
                    }
                }
                if (tmpFuente.getMonto() != null) {
                    totalFuentes = totalFuentes.add(tmpFuente.getMonto());
                }
                if (totalFuentes.compareTo(objeto.getMontoGlobalEnConstruccion()) > 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_LA_DISTRIBUCION_POR_FUENTES_SUPERA_MONTO_DEL_PROYECTO);
                    throw b;
                }
            }

            //añade la fuente al proyecto
            if (tmpFuente.getProyecto() == null) {
                objeto.getFuentesProyecto().add(tmpFuente);
                tmpFuente.setProyecto(objeto);
            }
            //valida la finacioacion
            ProyectoValidacion.validarFinanciacion(this.objeto);

            //se recargan los poartes por categoria
            ProyectoUtils.regenerarAportesProyecto(this.objeto);
            RequestContext.getCurrentInstance().execute("$('#anadirFuente').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método es el encargado de eliminar una fuente
     *
     * @param toDelete
     */
    public void eliminarFuente(ProyectoFuente toDelete) {
        try {
            objeto.getFuentesProyecto().remove(toDelete);

            //se recargan los poartes por categoria
            ProyectoUtils.regenerarAportesProyecto(objeto);
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
     * Este método es el encargado de sumar el total usado por las fuentes del
     * proyecto.
     *
     * @return
     */
    public BigDecimal getSumaTotalFuente() {
        BigDecimal total = BigDecimal.ZERO;
        for (ProyectoFuente fuente : objeto.getFuentesProyecto()) {
            total = total.add(fuente.getMonto());
        }
        return total;
    }

    // </editor-fold>
    /**
     * operación que se ejecuta cuando se cambia el convenio, y recrea la tabla
     * de categorías
     *
     * @param event
     */
    Convenio convenioSelecionado;

    public void onConvenioSelect(SelectEvent event) {
        Convenio conv = (Convenio) event.getObject();
        if (conv != null && !conv.equals(convenioSelecionado)) {
            objeto.setDistribuccionCategorias(new LinkedList<ProyectoCategoriaConvenio>());
            List<CategoriaConvenio> categorias = convenioDelegate.getCategorias(conv.getId());
            for (CategoriaConvenio categoria : categorias) {
                ProyectoCategoriaConvenio iter = new ProyectoCategoriaConvenio();
                iter.setTramos(new LinkedList<ProyectoAporteTramoTramo>());
                iter.setProyecto(objeto);
                iter.setCategoriaConvenio(categoria);
                iter.setMonto(BigDecimal.ZERO);
                objeto.getDistribuccionCategorias().add(iter);
            }
            convenioSelecionado = conv;
        }
        //se recargan los poartes por categoria
        ProyectoUtils.regenerarAportesProyecto(this.objeto);
    }

    /**
     * Este método es el encargado de sumar el total usado en un una categoría
     * del proyecto
     *
     * @return
     */
    public BigDecimal getSumaTotalCategorias() {
        BigDecimal total = BigDecimal.ZERO;
        for (ProyectoCategoriaConvenio iter : objeto.getDistribuccionCategorias()) {
            total = total.add(iter.getMonto());
        }
        return total;
    }

    /**
     * SELECCION DE PROGRAMA Y ASOCIACION DE INDICADORES
     */
    // <editor-fold defaultstate="collapsed" desc="SELECCION DE INDICADORES">
    //private  LinkedList<ProgramaIndicador> indicadoresSelecionados = new LinkedList<>();
    /**
     * Este método retorna la lista de indicadores disponibles a seleccionar en
     * el proyecto
     *
     * @return
     */
    public LinkedList<ProgramaIndicador> getIndicadoresASeleccionar() {
        LinkedList<ProgramaIndicador> indicadores = new LinkedList();
        if (!TextUtils.isEmpty(idProgramaInstitucional)) {
            List<ProgramaIndicador> res = emd.findByOneProperty(ProgramaIndicador.class.getName(), "programa.id", Integer.valueOf(idProgramaInstitucional));
            indicadores.addAll(res);
        }
        if (!TextUtils.isEmpty(idProgramaPresupuestario)) {
            List<ProgramaIndicador> res = emd.findByOneProperty(ProgramaIndicador.class.getName(), "programa.id", Integer.valueOf(idProgramaPresupuestario));
            indicadores.addAll(res);
        }
        return indicadores;
    }

    // </editor-fold>
    /**
     * LOGICA PARA EL MANEJO DE LOS APORTES DEL PROYECTO
     */
    // <editor-fold defaultstate="collapsed" desc="APORTES PROYECTO">
    /**
     * metodo que retorna todas los aportes de una categoría
     *
     * @param cate
     * @return
     */
    public List<ProyectoAporte> getAportesCategoria(CategoriaConvenio cate) {
        List<ProyectoAporte> res = new LinkedList<>();
        for (ProyectoAporte aporte : this.objeto.getAportesProyecto()) {
            if (aporte.getCategoriaConvenio().equals(cate)) {
                res.add(aporte);
            }
        }
        return res;
    }

    /**
     * retorna el aporte, que busca por categoría y fuente de recursos
     *
     * @param cate
     * @param fuenteR
     * @return
     */
    public ProyectoAporte getAporte(CategoriaConvenio cate, FuenteRecursos fuenteR) {
        return ProyectoUtils.getAporte(this.objeto, cate, fuenteR);
    }

    /**
     * metodo que retorna el monto toalUsado por la categoría
     *
     * @param cate
     * @return
     */
    public void recalcularTotalCategoria(ProyectoCategoriaConvenio proyectoCategoria) {
        ProyectoUtils.recalcularCategoria(objeto, proyectoCategoria);
    }

    /**
     * Se recalculan todas las categorías y se regenera la estructura de montos
     * a partir de un proyecto
     */
    public void cambioEnMontoAporte() {
        ProyectoUtils.recalcularTodasCategorias(objeto);
        ProyectoUtils.regenerarMontosEstructura(this.objeto);
    }

    /**
     * Al aporte de proyecto se le setea la categoría de convenio
     *
     */
    public void setCategoriaToAporte() {
        if (TextUtils.isEmpty(idCategoria)) {
            tmpAporte.setCategoriaConvenio(null);
        } else {
            tmpAporte.setCategoriaConvenio((CategoriaConvenio) emd.getEntityById(CategoriaConvenio.class.getName(), Integer.valueOf(idCategoria)));
        }
    }

    /**
     * Devuelve el monto de una categoría a partir de un aporte de proyecto
     *
     * @return
     */
    public BigDecimal getMontoCategoriaEnAporte() {
        if (tmpAporte != null) {
            if (tmpAporte.getCategoriaConvenio() != null) {
                for (ProyectoCategoriaConvenio iter : objeto.getDistribuccionCategorias()) {
                    if (tmpAporte.getCategoriaConvenio().equals(iter.getCategoriaConvenio())) {
                        return iter.getMonto();
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Devuelve el monto total de las fuentes de los aportes de proyecto
     *
     * @return
     */
    public BigDecimal getSumaTotalAporte() {
        BigDecimal total = BigDecimal.ZERO;
        for (ProyectoAporte fuente : objeto.getAportesProyecto()) {
            total = total.add(fuente.getMonto());
        }
        return total;
    }

    // </editor-fold>
    /**
     * LOGICA PARA EL MANEJO DE LOS Paripassu
     */
    // <editor-fold defaultstate="collapsed" desc="Paripassu">
    /**
     * Recarga el Pari passu
     */
    public void reloadParipassu() {
        tmpFuentesParipassu = new LinkedList();
        tempTramo = null;
        String nuevasSugerencias = "['MONTO'";
        for (ProyectoAporte aporte : objeto.getAportesProyecto()) {
            if (aporte.getCategoriaConvenio().equals(tmpProyCategoriaConvenio.getCategoriaConvenio())) {
                tmpFuentesParipassu.add(aporte);
                nuevasSugerencias = nuevasSugerencias + ",'MONTO_" + aporte.getFuenteRecursos().getCodigo() + "'";
            }
        }
        nuevasSugerencias = "sugerencias_fuentes = " + nuevasSugerencias + "];";
        RequestContext.getCurrentInstance().execute(nuevasSugerencias + "inicializarSugerencias();");
    }

    /**
     * Valida el Pari passu
     */
    public void saveParipassu() {
        try {
            ProyectoValidacion.validarParipassu(tmpProyCategoriaConvenio, tmpFuentesParipassu);
            RequestContext.getCurrentInstance().execute("$('#anadirParipassu').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Valida el Pari passu con tramos
     */
    public void saveParipassuConTramos() {
        try {
            ProyectoValidacion.validarDistribucionTramosEnCategoria(tmpProyCategoriaConvenio);
            RequestContext.getCurrentInstance().execute("$('#anadirParipassuConTramos').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Ejecuta un javascript que inicializa sugerencias
     */
    public void cambiaTipo() {
        RequestContext.getCurrentInstance().execute("inicializarSugerencias();");
    }

    /**
     * Devuelve el monto total de todos los Pari passu en construcción
     *
     * @return
     */
    public BigDecimal getTotalFuentesParipassu() {
        BigDecimal total = BigDecimal.ZERO;
        if (tmpFuentesParipassu != null) {
            for (ProyectoAporte aporte : tmpFuentesParipassu) {
                if (aporte.getMontoParipassuEnConstruccion() != null) {
                    total = total.add(aporte.getMontoParipassuEnConstruccion());
                }
            }
        }
        return total;
    }

    /**
     * Devuelve un Json que contiene los códigos de las fuentes de recurso del
     * Pari passu
     *
     * @return
     */
    public String getFuentesDiponiblesParipassu() {
        List<String> nombresFuentes = new LinkedList();
        nombresFuentes.add("MONTO");
        for (ProyectoAporte aporte : tmpFuentesParipassu) {
            nombresFuentes.add("MONTO_" + aporte.getFuenteRecursos().getCodigo());
        }
        Gson gson = new Gson();
        return gson.toJson(nombresFuentes);
    }

    // </editor-fold>
    /**
     * LOGICA PARA EL MANEJO DE TRAMOS EN UNA CATEGORIA
     */
    // <editor-fold defaultstate="collapsed" desc="MANEJO DE TRAMOS EN UNA CATEGORIA">
    /**
     * Recarga un tramo
     */
    public void reloadTramo() {
        if (tempTramo == null) {
            tempTramo = new ProyectoAporteTramoTramo();
            tempTramo.setParipassus(new LinkedList<ProyectoParipassuTramo>());
            ProyectoUtils.createParipassusTramo(objeto, tempTramo);
        }

    }

    /**
     * Remueve un tramo de una categoría de convenio
     *
     * @param tramo
     */
    public void eliminarTramo(ProyectoAporteTramoTramo tramo) {
        tmpProyCategoriaConvenio.getTramos().remove(tramo);
    }

    /**
     * Agrega un tramo a una categoría y reorganiza los tramos
     */
    public void saveTramo() {
        try {
            for (ProyectoAporteTramoTramo tramo2 : tmpProyCategoriaConvenio.getTramos()) {
                if ((!tempTramo.equals(tramo2)) && tempTramo.getMontoHastaEnConstruccion().compareTo(tramo2.getMontoHastaEnConstruccion()) == 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_HAY_DOS_TRAMOS_QUE_FINANCIAN_HASTA_EL_MISMO_MONTO);
                    throw b;
                }
            }
            if (tempTramo.getCategoria() == null) {
                tempTramo.setCategoria(tmpProyCategoriaConvenio);
                tmpProyCategoriaConvenio.getTramos().add(tempTramo);
            }
            ProyectoUtils.reorganizarAporteTramo(tmpProyCategoriaConvenio);
            tempTramo = null;
            RequestContext.getCurrentInstance().execute("inicializarSugerencias();");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }

    }

    // </editor-fold>
    /**
     * LOGICA PARA EL MANEJO DE PRORROGAS
     */
    // <editor-fold defaultstate="collapsed" desc="PRORROGA">
    /**
     * Recarga una prorroga
     */
    public void reloadProrroga() {
        if (tmpProrroga == null) {
            tmpProrroga = new ProyectoProrroga();
        }
        tmpDocumento = tmpProrroga.getDocumento();
        if (tmpDocumento == null) {
            tmpDocumento = new ProyectoDocumentos();
        }
    }

    /**
     * Devuelve un tipo de documento para un código específico
     *
     * @param codigo
     * @return
     */
    private TipoDocumento getTipoDocuento(String codigo) {
        Configuracion c = configuracionDelegate.obtenerCnfPorCodigo(codigo);
        if (c == null || TextUtils.isEmpty(codigo)) {
            return null;
        }

        List<TipoDocumento> l = emd.findByOneProperty(TipoDocumento.class.getName(), "codigo", c.getCnfValor());
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

    /**
     * Guarda una prorroga y actualiza el proyecto base
     */
    public void saveProrroga() {
        try {

            objeto = proyectoDelegate.guardarProrroga(objeto.getId(), tmpProrroga);
            reloadProyectoFromBase();
            RequestContext.getCurrentInstance().execute("$('#anadirProrroga').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina una prorroga
     *
     * @param toDelete
     */
    public void eliminarProrroga(ProyectoProrroga toDelete) {
        try {
            ListUtils.deleteElement(objeto.getProrrogas(), toDelete);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    // </editor-fold>

    /**
     * LOGICA PARA EL MANEJO DE DESEMBOLSOS
     */
    // <editor-fold defaultstate="collapsed" desc="DESEMBOLSOS">
    /**
     * Recarga un desembolso
     */
    public void reloadDesembolso() {
        if (tmpDesembolso == null) {
            tmpDesembolso = new ProyectoDesembolso();
        }
        tmpDocumento = tmpDesembolso.getDocumento();
        if (tmpDocumento == null) {
            tmpDocumento = new ProyectoDocumentos();
        }
    }

    /**
     * Guarda un desembolso
     */
    public void saveDesembolso() {
        try {
            //se guarda el desembolso
            if (tmpDesembolso.getProyecto() == null) {
                objeto.getProyectoDesembolso().add(tmpDesembolso);
                tmpDesembolso.setProyecto(objeto);
            }
            //se guarda el documento
            if (esVacioDocumeto(tmpDocumento)) {
                tmpDesembolso.setDocumento(null);
                objeto.getProyectoDocumentos().remove(tmpDocumento);
            } else {
                tmpDocumento.setTipoDocumento(getTipoDocuento(ConstantesConfiguracion.CODIGO_TIPO_DE_ARCHIVO_DESEMBOLSO));
                tmpDocumento.setDescripcion("Desembolso Fecha: " + DatesUtils.dateToString(tmpDesembolso.getFecha()));
                tmpDesembolso.setDocumento(tmpDocumento);
                if (tmpDocumento.getProyecto() == null) {
                    tmpDocumento.setProyecto(objeto);
                    objeto.getProyectoDocumentos().add(tmpDocumento);
                }
            }
            RequestContext.getCurrentInstance().execute("$('#anadirDesembolso').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina un desembolso
     *
     * @param toDelete
     */
    public void eliminarDesembolso(ProyectoDesembolso toDelete) {
        try {
            ListUtils.deleteElement(objeto.getProyectoDesembolso(), toDelete);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // </editor-fold>
    /**
     * LOGICA PARA EL MANEJO DE ENMIENDAS
     */
    // <editor-fold defaultstate="collapsed" desc="ENMIENDAS">
    /**
     * Recarga una enmienda
     */
    public void reloadEnmienda() {
        if (tmpEnmienda == null) {
            tmpEnmienda = new ProyectoEnmienda();
        }
        tmpDocumento = tmpEnmienda.getDocumento();
        if (tmpDocumento == null) {
            tmpDocumento = new ProyectoDocumentos();
        }

    }

    /**
     * Guarda una enmienda
     */
    public void saveEnmienda() {
        try {

            if (tmpEnmienda.getProyecto() == null) {
                objeto.getProyectoEnmienda().add(tmpEnmienda);
                tmpEnmienda.setProyecto(objeto);
            }
            //se guarda el documento
            if (esVacioDocumeto(tmpDocumento)) {
                tmpEnmienda.setDocumento(null);
                objeto.getProyectoDocumentos().remove(tmpDocumento);
            } else {
                tmpDocumento.setTipoDocumento(getTipoDocuento(ConstantesConfiguracion.CODIGO_TIPO_DE_ARCHIVO_ENMIENDA));
                tmpDocumento.setDescripcion("Enmienda Fecha: " + DatesUtils.dateToString(tmpEnmienda.getFecha()));
                tmpEnmienda.setDocumento(tmpDocumento);
                if (tmpDocumento.getProyecto() == null) {
                    tmpDocumento.setProyecto(objeto);
                    objeto.getProyectoDocumentos().add(tmpDocumento);
                }
            }
            RequestContext.getCurrentInstance().execute("$('#anadirEnmienda').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina una enmienda
     *
     * @param toDelete
     */
    public void eliminarEnmienda(ProyectoEnmienda toDelete) {
        try {
            ListUtils.deleteElement(objeto.getProyectoEnmienda(), toDelete);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // </editor-fold>
    /**
     * LOGICA PARA EL MANEJO DE COEJECUTORAS
     */
    // <editor-fold defaultstate="collapsed" desc="COEJECUTORAS">
    private ProyectoCoEjecutora tmpCoEjecutora;

    /**
     * Instancia una coejecutora
     */
    public void initCoEjecutora() {
        tmpCoEjecutora = new ProyectoCoEjecutora();
    }

    /**
     * Guarda una coejecutora
     */
    public void saveCoEjecutora() {
        try {

            if (tmpCoEjecutora.getProyecto() == null) {
                objeto.getProyectoCoEjecutoras().add(tmpCoEjecutora);
                tmpCoEjecutora.setProyecto(objeto);
            }
            RequestContext.getCurrentInstance().execute("$('#anadirCoEjecutora').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina una coejecutora
     *
     * @param toDelete
     */
    public void eliminarCoEjecutora(ProyectoCoEjecutora toDelete) {
        try {
            ListUtils.deleteElement(objeto.getProyectoCoEjecutoras(), toDelete);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // </editor-fold>
    /**
     * COSAS COMUNES PARA COMPONENTES Y MACROACTIVIDADES
     */
    /**
     * Devuelve una lista de fuente de recursos de un proyecto
     * @return 
     */
    public List<FuenteRecursos> getFuentesRecursosProyecto() {
        List<FuenteRecursos> l = new LinkedList();
        for (ProyectoAporte aporte : objeto.getAportesProyecto()) {
            if (!l.contains(aporte.getFuenteRecursos())) {
                l.add(aporte.getFuenteRecursos());
            }
        }
        return l;
    }

    /**
     * LÓGICA PARA EL MANEJO DE COMPONENTES
     */
    // <editor-fold defaultstate="collapsed" desc="COMPONENTES">

    /**Devuelve el id de la UT que tiene un componente que está seleccionado
     *
     * @return
     */
    public Integer getIdUTSelecionadaComponente() {
        if (tmpComponente == null || tmpComponente.getUnidadTecnica() == null) {
            return null;
        }
        return tmpComponente.getUnidadTecnica().getId();
    }

    /**
     * Recarga un componente
     */
    public void reloadComponente() {
        if (tmpComponente == null) {
            tmpComponente = new ProyectoComponente();
            tmpComponente.setTipo(TipoEstructura.COMPONENTE);
            tmpComponente.setMontosFuentes(new LinkedList());
            tmpComponente.setComponenteHijos(new LinkedList());
            tmpComponente.setProductos(new LinkedList());
            tmpComponente.setTipoMontoEstructura(TipoMontoEstructura.IMPORTE);
            ProyectoUtils.crearAportesParaEstrucutra(this.objeto, tmpComponente);
        }
        super.initSelectOneUTBean();
        unidadTecnicaSelecionada = tmpComponente.getUnidadTecnica();
        if (tmpComponente.getComponentePadre() != null) {
            tempComponentePadreIndex = String.valueOf(objeto.getProyectoComponentes().indexOf(tmpComponente.getComponentePadre()));
        } else {
            tempComponentePadreIndex = null;
        }

        tmpComponentePadre = tmpComponente.getComponentePadre();

    }

    /**
     * metodo que se llama cuando cambia el componente en el select, así se
     * actualiza el componente ene l objeto para cuando se recalculan los montos
     */
    public void updateComponentePadre() {
    }

    /**
     * Guarda un componente
     */
    public void saveComponente() {
        try {
            ProyectoValidacion.validarAportesEnEstructura(tmpComponente);

            if (tmpComponentePadre != null) {
                tmpComponentePadre.getComponenteHijos().remove(tmpComponente);
            }

            if (!TextUtils.isEmpty(tempComponentePadreIndex)) {
                ProyectoComponente padre = objeto.getProyectoComponentes().get(Integer.valueOf(tempComponentePadreIndex).intValue());
                tmpComponente.setComponentePadre(padre);
                padre.getComponenteHijos().add(tmpComponente);
            } else {
                tmpComponente.setComponentePadre(null);
            }

            recalcularImporte(tmpComponente);
            tmpComponente.setUnidadTecnica(unidadTecnicaSelecionada);

            if (tmpComponente.getNumero() == null || tmpComponente.getNumero() == 0) {
                Integer maxNumero = 0;
                if (tmpComponente.getComponentePadre() == null) {
                    for (ProyectoComponente iter : objeto.getProyectoComponentes()) {
                        if (iter.getComponentePadre() == null && iter.getNumero() != null && iter.getNumero().compareTo(maxNumero) > 0) {
                            maxNumero = iter.getNumero();
                        }
                    }
                } else {
                    for (ProyectoComponente iter : tmpComponente.getComponentePadre().getComponenteHijos()) {
                        if (iter.getNumero() != null && iter.getNumero().compareTo(maxNumero) > 0) {
                            maxNumero = iter.getNumero();
                        }
                    }
                }

                tmpComponente.setNumero(maxNumero + 1);
            }

            if (tmpComponente.getProyecto() == null) {
                tmpComponente.setProyecto(objeto);
                objeto.getProyectoComponentes().add(tmpComponente);
            }

            RequestContext.getCurrentInstance().execute("$('#anadirComponente').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Devuelve una lista ordenada de estructuras de proyecto
     * @param items
     * @return 
     */
    public List<ProyectoEstructura> ordenarProyectoEstructuraPorOrden(List<ProyectoEstructura> items) {
        ProyectoUtils.ordenarProyectoEstructuraPorOrden(items);
        return items;
    }

    /**
     * Devuelve una lista de posibles componentes padres
     * @return 
     */
    public Map<String, String> getPosiblesComponentePadres() {
        Map<String, String> map = new LinkedHashMap();
        for (int i = 0; i < objeto.getProyectoComponentes().size(); i++) {
            ProyectoComponente componente = objeto.getProyectoComponentes().get(i);
            if (!componente.equals(tmpComponente) && componente.getComponentePadre() == null) {
                map.put(componente.getNumero() + " " + componente.getNombre(), String.valueOf(i));
            }
        }
        return map;
    }

    /**
     * Devuelve el total de una estructura de proyecto
     * @param c
     * @param f
     * @return 
     */
    public BigDecimal getMontoProyEstructura(ProyectoEstructura c, FuenteRecursos f) {
        if (c == null || f == null) {
            return null;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (ProyectoEstPorcentajeFuente iter : c.getMontosFuentes()) {
            if (iter.getFuente().getFuenteRecursos().equals(f) && iter.getMontoEnConstruccion() != null) {
                total = total.add(iter.getMontoEnConstruccion());
            }
        }
        return total;

    }

    /**
     * Elimina un componente
     * @param toDelete 
     */
    public void eliminarComponente(ProyectoComponente toDelete) {
        try {
            boolean enUso = false;

            if (!toDelete.getComponenteHijos().isEmpty()) {
                enUso = true;
            }

            if (enUso) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA);
                throw b;
            }

            if (toDelete.getComponentePadre() != null) {
                ListUtils.deleteElement(toDelete.getComponentePadre().getComponenteHijos(), toDelete);
            }
            ListUtils.deleteElement(objeto.getProyectoComponentes(), toDelete);
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
     * Obtiene la numeración de un componente
     * @param componente
     * @return 
     */
    public String getNumeracionComponente(ProyectoComponente componente) {
        String num = String.valueOf(componente.getNumero());
        componente = componente.getComponentePadre();
        while (componente != null) {
            num = componente.getNumero() + "." + num;
            componente = componente.getComponentePadre();
        }
        return num;
    }

    /**
     * Devuelve el monto de un componente padre
     * @return 
     */
    public BigDecimal getImporteComponentePadre() {
        if (!TextUtils.isEmpty(tempComponentePadreIndex)) {
            ProyectoComponente padre = objeto.getProyectoComponentes().get(Integer.valueOf(tempComponentePadreIndex).intValue());
            return padre.getImporte();
        }
        return null;
    }

    /**
     * Devuelve una lista de ProyectoEstPorcentajeFuente ordenados por código
     * @param montos
     * @return 
     */
    public List<ProyectoEstPorcentajeFuente> getMontosEstructoraOrdenados(List<ProyectoEstPorcentajeFuente> montos) {
        if (montos == null) {
            return Collections.EMPTY_LIST;
        }
        //la idea es que quede ordenado por categoria y luego por fuente (entonces se ordena en orden inversio para que quede derecho)
        Collections.sort(montos, new Comparator<ProyectoEstPorcentajeFuente>() {
            @Override
            public int compare(ProyectoEstPorcentajeFuente o1, ProyectoEstPorcentajeFuente o2) {
                return o1.getFuente().getFuenteRecursos().getCodigo().compareTo(o2.getFuente().getFuenteRecursos().getCodigo());
            }
        });
        Collections.sort(montos, new Comparator<ProyectoEstPorcentajeFuente>() {
            @Override
            public int compare(ProyectoEstPorcentajeFuente o1, ProyectoEstPorcentajeFuente o2) {
                return o1.getFuente().getCategoriaConvenio().getCodigo().compareTo(o2.getFuente().getCategoriaConvenio().getCodigo());
            }
        });
        return montos;
    }
// </editor-fold>

    /**
     * Recalcula la distribución de montos para una estructura si es porcentaje
     * setea el monto y si es monto setea el porcentaje
     *
     * @param estructura
     */
    public void recalcularImporte(ProyectoEstructura estructura) {
        if (estructura.getTipoMontoEstructura() == TipoMontoEstructura.PORCENTAJE) {
            if (estructura.getTipo() == TipoEstructura.COMPONENTE && getImporteComponentePadre() != null) {
                estructura.setImporte(NumberUtils.porcentaje(estructura.getPorcentaje(), getImporteComponentePadre(), RoundingMode.DOWN));
            } else {
                estructura.setImporte(NumberUtils.porcentaje(estructura.getPorcentaje(), objeto.getMontoGlobalEnConstruccion(), RoundingMode.DOWN));
            }
        }
        if (estructura.getTipoMontoEstructura() == TipoMontoEstructura.IMPORTE) {
            BigDecimal montoglobal = objeto.getMontoGlobalEnConstruccion();
            if (estructura.getTipo() == TipoEstructura.COMPONENTE && getImporteComponentePadre() != null) {
                montoglobal = getImporteComponentePadre();
            }
            if (montoglobal != null && estructura.getImporte() != null) {
                BigDecimal porcentaje = estructura.getImporte().multiply(NumberUtils.CIEN);
                estructura.setPorcentaje(porcentaje.divide(montoglobal, 2, RoundingMode.DOWN));
            } else {
                estructura.setPorcentaje(null);
            }
        }
        recalcularMontosApoerte(estructura);
    }

    /**
     * Esto no se usa mas ahora se ingresa el monto directto
     * @param estructura 
     */
    public void recalcularMontosApoerte(ProyectoEstructura estructura) {
        for (ProyectoEstPorcentajeFuente monto : estructura.getMontosFuentes()) {
            if (monto.getMontoEnConstruccion() == null) {
                monto.setMontoEnConstruccion(BigDecimal.ZERO);
            }
        }
    }
    /**
     * LOGICA PARA EL MANEJO DE MACROACTIVIDADES
     */
    // <editor-fold defaultstate="collapsed" desc="MACROACTIVIDADES">
    private ProyectoMacroActividad tmpMacroActividad = null;
    private String idMacroActividad;

    /**
     * Devuelve el id de la UT que pertenece a la macro actividad seleccionada
     * @return 
     */
    public Integer getIdUTSelecionadaMacroActividad() {
        if (tmpMacroActividad == null || tmpMacroActividad.getUnidadTecnica() == null) {
            return null;
        }
        return tmpMacroActividad.getUnidadTecnica().getId();
    }

    /**
     * Recarga una macro actividad
     */
    public void reloadMacroActividad() {
        if (tmpMacroActividad == null) {
            tmpMacroActividad = new ProyectoMacroActividad();
            tmpMacroActividad.setMontosFuentes(new LinkedList());
            tmpMacroActividad.setProductos(new LinkedList());
            tmpMacroActividad.setTipo(TipoEstructura.MACROACTIVIDAD);
            tmpMacroActividad.setTipoMontoEstructura(TipoMontoEstructura.IMPORTE);
            ProyectoUtils.crearAportesParaEstrucutra(this.objeto, tmpMacroActividad);
        }
        if (tmpMacroActividad.getMacroActividad() != null) {
            idMacroActividad = String.valueOf(tmpMacroActividad.getMacroActividad().getId());
        } else {
            idMacroActividad = null;
        }

        super.initSelectOneUTBean();
        unidadTecnicaSelecionada = tmpMacroActividad.getUnidadTecnica();
    }

    /**
     * Guarda una macro actividad
     */
    public void saveMacroActividad() {
        try {
            ProyectoValidacion.validarAportesEnEstructura(tmpMacroActividad);

            if (!TextUtils.isEmpty(idMacroActividad)) {
                tmpMacroActividad.setMacroActividad((MacroActividad) emd.getEntityById(MacroActividad.class.getName(), Integer.valueOf(idMacroActividad)));
            } else {
                tmpComponente.setUnidadTecnica(null);
            }

            tmpMacroActividad.setUnidadTecnica(unidadTecnicaSelecionada);
            recalcularImporte(tmpMacroActividad);

            if (tmpMacroActividad.getProyecto() == null) {
                tmpMacroActividad.setProyecto(objeto);
                objeto.getProyectoMacroactividad().add(tmpMacroActividad);
            }

            //numeracion
            if (tmpMacroActividad.getNumero() == null || tmpMacroActividad.getNumero() == 0) {
                Integer maxNumero = 0;
                for (ProyectoMacroActividad iter : objeto.getProyectoMacroactividad()) {
                    if (iter.getNumero() != null && iter.getNumero().compareTo(maxNumero) > 0) {
                        maxNumero = iter.getNumero();
                    }
                }
                tmpMacroActividad.setNumero(maxNumero + 1);
            }

            RequestContext.getCurrentInstance().execute("$('#anadirMacroActividad').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina una macroactividad
     * @param toDelete 
     */
    public void eliminarMacroActividad(ProyectoMacroActividad toDelete) {
        try {
            ListUtils.deleteElement(objeto.getProyectoMacroactividad(), toDelete);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

// </editor-fold>
    /**
     * LOGICA PARA ASOCIAR PRODUCTO A COMPONENTE O MACROACTIVIDAD
     */
    // <editor-fold defaultstate="collapsed" desc="ASOCIAR PRODUCTO">
    private ProyectoEstructura itemEstructura;
    private ProyectoEstProducto pdroductoEstructura;
    private String idProducto;

    /**
     * Recarga un producto
     */
    public void reloadProductoEstructura() {
        initSelectOneUTBean();
        if (pdroductoEstructura == null) {
            pdroductoEstructura = new ProyectoEstProducto();
        }
        if (pdroductoEstructura.getProducto() != null) {
            idProducto = String.valueOf(pdroductoEstructura.getProducto().getId());
        } else {
            idProducto = null;
        }
        unidadTecnicaSelecionada = pdroductoEstructura.getUnidadTecnica();
    }

    /**
     * Guarda un producto
     */
    public void saveProductoEstructura() {
        try {
            pdroductoEstructura.setUnidadTecnica(unidadTecnicaSelecionada);
            if (!TextUtils.isEmpty(idProducto)) {
                Indicador p = (Indicador) emd.getEntityById(Indicador.class.getName(), Integer.valueOf(idProducto));
                pdroductoEstructura.setProducto(p);
            }
            if (pdroductoEstructura.getProyectoEstructura() == null) {
                pdroductoEstructura.setProyectoEstructura(itemEstructura);
                itemEstructura.getProductos().add(pdroductoEstructura);
            }
            //se numera el producto
            if (pdroductoEstructura.getNumero() == null || pdroductoEstructura.getNumero() == 0) {
                Integer maxNumero = 0;
                for (ProyectoEstProducto iter : itemEstructura.getProductos()) {
                    if (iter.getNumero() != null && iter.getNumero().compareTo(maxNumero) > 0) {
                        maxNumero = iter.getNumero();
                    }
                }
                pdroductoEstructura.setNumero(maxNumero + 1);
            }
            RequestContext.getCurrentInstance().execute("$('#asociarProducto').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina un producto
     * @param pdroductoEstructura 
     */
    public void eliminarProductoEstructura(ProyectoEstProducto pdroductoEstructura) {
        if (pdroductoEstructura.getProyectoEstructura() != null) {
            pdroductoEstructura.getProyectoEstructura().getProductos().remove(pdroductoEstructura);
        }
        RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
    }

    /**
     * Devuelve el nombre de una estructura
     * @return 
     */
    public String getNombreItemEstructura() {
        return ProyectoUtils.getNombreEstructura(itemEstructura);
    }

    // </editor-fold>
    UnidadTecnica utSelecionada;

    /**
     * obtiene la UT asociada a un nodo seleccionado
     * @param event 
     */
    public void onNodeSelect(NodeSelectEvent event) {
        utSelecionada = (UnidadTecnica) event.getTreeNode().getData();
    }

    /**
     * LOGICA PARA EL MANEJO DE DOCUMENTOS
     */
    // <editor-fold defaultstate="collapsed" desc="DOCUMENTOS">
    private UploadedFile uploadedFile;
    private String idTipoDocuemnto;

    /**
     * Método utilizado para cargar un archivo
     * @param event 
     */
    public void fileUploadListener(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            if (uploadedFile != null) {
                tmpDocumento.setTempUploadedFile(ArchivoUtils.getDataFile(uploadedFile));
            }
            uploadedFile = null;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    private ProyectoDocumentos tmpDocumento;

    /**
     * Recarga un documento
     */
    public void reloadDocumento() {
        if (tmpDocumento == null) {
            tmpDocumento = new ProyectoDocumentos();
        }
        uploadedFile = null;
    }

    /**
     * Verifica si el documento está vacío
     * @param documento
     * @return 
     */
    public boolean esVacioDocumeto(ProyectoDocumentos documento) {
        if (documento.getTempUploadedFile() == null && documento.getArchivo() == null && TextUtils.isEmpty(documento.getDescripcion())) {
            return true;
        }
        return false;
    }

    /**
     * Asocia un documento a un proyecto
     */
    private void ejecutarGuardaruardarDocuemnto() {
        TipoDocumento tp = (TipoDocumento) emd.getEntityById(TipoDocumento.class.getName(), Integer.valueOf(idTipoDocuemnto));
        tmpDocumento.setTipoDocumento(tp);

        if (esVacioDocumeto(tmpDocumento)) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_NO_PUEDE_CREAR_UN_DOCUEMNTO_VACIO);
            throw b;
        }
        if (tmpDocumento.getProyecto() == null) {
            objeto.getProyectoDocumentos().add(tmpDocumento);
            tmpDocumento.setProyecto(objeto);
        }
    }

    /**
     * Llama a la ejecución de guardado de un documento
     */
    public void saveDocumento() {
        try {
            ejecutarGuardaruardarDocuemnto();
            RequestContext.getCurrentInstance().execute("$('#anadirDocumento').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Guarda un documento en la base de datos
     */
    public void guardarDocumentoEnBase() {
        try {
            ejecutarGuardaruardarDocuemnto();
            tmpDocumento = proyectoDelegate.guardarDocumento(objeto.getId(), tmpDocumento);
            reloadProyectoFromBase();
            RequestContext.getCurrentInstance().execute("$('#anadirDocumento').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina un documento
     * @param toDelete 
     */
    public void eliminarDocuento(ProyectoDocumentos toDelete) {
        try {
            ListUtils.deleteElement(objeto.getProyectoDocumentos(), toDelete);
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
     * Verifica si hay un archivo cargado
     * @param documento
     * @return 
     */
    public boolean hayArchivo(ProyectoDocumentos documento) {
        if (documento == null) {
            return false;
        }
        if (documento.getTempUploadedFile() != null || documento.getArchivo() != null) {
            return true;
        }
        return false;
    }

    /**
     * Método que sirve para descargar un archivo
     * @param documento 
     */
    public void downloadFileDeDocumento(ProyectoDocumentos documento) {
        try {
            if (documento.getTempUploadedFile() != null) {
                ArchivoUtils.downloadFile(documento.getTempUploadedFile());
            } else {
                ArchivoUtils.downloadFile(documento.getArchivo(), archivoDelegate.getFile(documento.getArchivo()));
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
     * Devuelve el nombre de un archivo
     * @param documento
     * @return 
     */
    public String getNombreArchivo(ProyectoDocumentos documento) {
        if (documento.getTempUploadedFile() != null) {
            return documento.getTempUploadedFile().getFileName();
        } else if (documento.getArchivo() != null) {
            return documento.getArchivo().getNombreOriginal();
        }
        return null;
    }

    /**
     * Devuelve la descripción, el nombre actual o el nombre original de un archivo
     * @param documento
     * @return 
     */
    public String printDocumento(ProyectoDocumentos documento) {
        if (!TextUtils.isEmpty(documento.getDescripcion())) {
            return documento.getDescripcion();
        } else if (documento.getTempUploadedFile() != null) {
            return documento.getTempUploadedFile().getFileName();
        } else if (documento.getArchivo() != null) {
            return documento.getArchivo().getNombreOriginal();
        }
        return "";

    }

    // </editor-fold>
    /**
     * LOGICA PARA EL MANEJO DE INDICADORES
     */
    // <editor-fold defaultstate="collapsed" desc="INDICADORES">
    String idIndicador;

    LinkedHashMap<String, String> indicadoresDisponibles;

    /**
     * Devuelve una lista de indicadores disponibles
     * @return 
     */
    public Map<String, String> getIndicadoresDisponibles() {
        indicadoresDisponibles = new LinkedHashMap<>();
        loadIndicadoresPrograma(idProgramaInstitucional, indicadoresDisponibles);
        loadIndicadoresPrograma(idProgramaPresupuestario, indicadoresDisponibles);
        loadIndicadoresPrograma(idSubProgramaPresupuestario, indicadoresDisponibles);
        return indicadoresDisponibles;
    }
    
    /**
     * Carga los indicadores de programa
     * @param idPrograma
     * @param map 
     */
    private void loadIndicadoresPrograma(String idPrograma, Map<String, String> map) {
        if (!TextUtils.isEmpty(idPrograma)) {
            List<ProgramaIndicador> l = emd.findByOneProperty(ProgramaIndicador.class.getName(), "programa.id", Integer.valueOf(idPrograma));
            for (ProgramaIndicador iter : l) {
                map.put(iter.getIndicador().getNombre(), iter.getIndicador().getId().toString());
            }
        }
    }
    // </editor-fold>
    /**
     * LOGICA PARA EL MANEJO DE DIAAGRAMAS JS
     */
    // <editor-fold defaultstate="collapsed" desc="DIAGRAMAS JS">
    private String treeJson;
    private int maxHeightTree;

    private String jsonGraficoTorta;

    /**
     * Recarga el árbol de proyectos
     */
    public void reloadProyectoTree() {
        ProyectoArbol root = ProyectoUtils.convertProyectoToArbol(objeto);
        maxHeightTree = ProyectoUtils.getMaxWidth(root);
        treeJson = ProyectoUtils.getJsonProyectoToArbol(root);

        jsonGraficoTorta = ProyectoUtils.getJsonProyectoToGraficaTorta(objeto);
    }

    /**
     * Devuelve el Json del árbol de la estructura de proyecto
     * @return 
     */
    public String getJsonEstructuraProyecto() {
        return treeJson;
    }

    // </editor-fold>
    /**
     * GETTERS CALCULADOS PARA EL USO DE PROYECTO
     */
    // <editor-fold defaultstate="collapsed" desc="getter caluclados">
    /**
     * Completa un texto coincidente con el nombre de usuario
     * @param query
     * @return 
     */
    public List<SsUsuario> completeTextUsuario(String query) {
        if (unidadTecnicaSelecionada != null) {
            List<SsUsuario> l = usuarioDelegate.getUsuariosConNombreEnUT(unidadTecnicaSelecionada.getId(), query);
            return l;
        }
        return Collections.emptyList();
    }

    /***
     * Devuelve el nombre de un usuario
     * @param usuario
     * @return 
     */
    public String getNombreResponsable(SsUsuario usuario) {
        if (usuario == null) {
            return null;
        }
        return usuario.getUsuCod() + " " + usuario.getUsuPrimerNombre() + " " + usuario.getUsuPrimerApellido();
    }

    /**
     * Devuelve una lista de convenios
     * @param query
     * @return 
     */
    public List<Convenio> completeTextConvenio(String query) {
        List<Convenio> l = convenioDelegate.getConveniosHabilitadosPorCodigo(query);
        return l;
    }

    /**
     * Devuelve una lista de categorías en convenio
     * @return 
     */
    public Map<String, String> getCategoriasEnConvenio() {
        Map<String, String> res = new LinkedHashMap();
        if (objeto.getConvenio() != null) {
            List<CategoriaConvenio> categorias = convenioDelegate.getCategorias(objeto.getConvenio().getId());
            for (CategoriaConvenio iter : categorias) {
                res.put(iter.getNombre(), iter.getId().toString());
            }
        }
        return res;
    }

    /**
     * Devuelve una lista de categorías en distribución
     * @return 
     */
    public Map<String, String> getCategoriasEnDistribucion() {
        Map<String, String> res = new LinkedHashMap();
        for (ProyectoCategoriaConvenio iter : objeto.getDistribuccionCategorias()) {
            if (iter.getMonto() != null && iter.getMonto().compareTo(BigDecimal.ZERO) != 0) {
                res.put(iter.getCategoriaConvenio().getNombre(), iter.getCategoriaConvenio().getId().toString());
            }
        }
        return res;
    }

    /**
     * Devuelve el monto Asignado a un proyecto
     * @param aporte
     * @return 
     */
    public BigDecimal getMontoAsignado(ProyectoAporte aporte) {
        BigDecimal total = BigDecimal.ZERO;
        for (ProyectoComponente componente : this.objeto.getProyectoComponentes()) {
            if (componente.getComponentePadre() == null) {
                for (ProyectoEstPorcentajeFuente iter : componente.getMontosFuentes()) {
                    if (iter.getFuente().equals(aporte)) {
                        if (iter.getMontoEnConstruccion() != null) {
                            total = total.add(iter.getMontoEnConstruccion());
                        }
                    }
                }
            }
        }
        for (ProyectoMacroActividad macroactividad : objeto.getProyectoMacroactividad()) {
            for (ProyectoEstPorcentajeFuente iter : macroactividad.getMontosFuentes()) {
                if (iter.getFuente().equals(aporte)) {
                    if (iter.getMontoEnConstruccion() != null) {
                        total = total.add(iter.getMontoEnConstruccion());
                    }
                }
            }
        }
        return total;
    }

    /**
     * Devuelve el monto total asignado a un proyecto
     * @return 
     */
    public BigDecimal getMontoTotalAsignado() {
        BigDecimal total = BigDecimal.ZERO;
        for (ProyectoComponente componente : this.objeto.getProyectoComponentes()) {
            if (componente.getComponentePadre() == null) {
                for (ProyectoEstPorcentajeFuente iter : componente.getMontosFuentes()) {
                    if (iter.getMontoEnConstruccion() != null) {
                        total = total.add(iter.getMontoEnConstruccion());
                    }
                }
            }
        }
        for (ProyectoMacroActividad macroactividad : objeto.getProyectoMacroactividad()) {
            for (ProyectoEstPorcentajeFuente iter : macroactividad.getMontosFuentes()) {
                if (iter.getMontoEnConstruccion() != null) {
                    total = total.add(iter.getMontoEnConstruccion());
                }
            }
        }
        return total;
    }
    // </editor-fold>

    /**
     * GETTERS Y SETTERS
     */
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public List<TipoProyecto> getTipoProyectosDisponibles() {
        return tipoProyectosDisponibles;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public boolean isOcultarFuentes() {
        return ocultarFuentes;
    }

    public String getTreeJson() {
        return treeJson;
    }

    public void setTreeJson(String treeJson) {
        this.treeJson = treeJson;
    }

    public int getMaxHeightTree() {
        return maxHeightTree;
    }

    public void setMaxHeightTree(int maxHeightTree) {
        this.maxHeightTree = maxHeightTree;
    }

    public void setOcultarFuentes(boolean ocultarFuentes) {
        this.ocultarFuentes = ocultarFuentes;
    }

    public boolean isOcultarProductos() {
        return ocultarProductos;
    }

    public void setOcultarProductos(boolean ocultarProductos) {
        this.ocultarProductos = ocultarProductos;
    }

    public ProyectoCategoriaConvenio getTmpProyCategoriaConvenio() {
        return tmpProyCategoriaConvenio;
    }

    public void setTmpProyCategoriaConvenio(ProyectoCategoriaConvenio tmpProyCategoriaConvenio) {
        this.tmpProyCategoriaConvenio = tmpProyCategoriaConvenio;
    }

    public ProyectoEstProducto getPdroductoEstructura() {
        return pdroductoEstructura;
    }

    public void setPdroductoEstructura(ProyectoEstProducto pdroductoEstructura) {
        this.pdroductoEstructura = pdroductoEstructura;
    }

    public void setTipoProyectosDisponibles(List<TipoProyecto> tipoProyectosDisponibles) {
        this.tipoProyectosDisponibles = tipoProyectosDisponibles;
    }

    public ProyectoFuente getTmpFuente() {
        return tmpFuente;
    }

    public void setTmpFuente(ProyectoFuente tmpFuente) {
        this.tmpFuente = tmpFuente;
    }

    public String getIdTipoDocuemnto() {
        return idTipoDocuemnto;
    }

    public String getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(String idIndicador) {
        this.idIndicador = idIndicador;
    }

    public void setIdTipoDocuemnto(String idTipoDocuemnto) {
        this.idTipoDocuemnto = idTipoDocuemnto;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public String getTmpIdFuenteFinanciamiento() {
        return tmpIdFuenteFinanciamiento;
    }

    public String getUnidadTecnicaId() {
        return unidadTecnicaId;
    }

    public void setUnidadTecnicaId(String unidadTecnicaId) {
        this.unidadTecnicaId = unidadTecnicaId;
    }

    public ProyectoMacroActividad getTmpMacroActividad() {
        return tmpMacroActividad;
    }

    public void setTmpMacroActividad(ProyectoMacroActividad tmpMacroActividad) {
        this.tmpMacroActividad = tmpMacroActividad;
    }

    public String getIdMacroActividad() {
        return idMacroActividad;
    }

    public void setIdMacroActividad(String idMacroActividad) {
        this.idMacroActividad = idMacroActividad;
    }

    public Integer getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(Integer activeTab) {
        this.activeTab = activeTab;
    }

    public String getTempComponentePadreIndex() {
        return tempComponentePadreIndex;
    }

    public void setTempComponentePadreIndex(String tempComponentePadreIndex) {
        this.tempComponentePadreIndex = tempComponentePadreIndex;
    }

    public void setTmpIdFuenteFinanciamiento(String tmpIdFuenteFinanciamiento) {
        this.tmpIdFuenteFinanciamiento = tmpIdFuenteFinanciamiento;
    }

    public ProyectoDocumentos getTmpDocumento() {
        return tmpDocumento;
    }

    public ProyectoComponente getTmpComponente() {
        return tmpComponente;
    }

    public String getTempComponentePadreId() {
        return tempComponentePadreIndex;
    }

    public void setTempComponentePadreId(String tempComponentePadreId) {
        this.tempComponentePadreIndex = tempComponentePadreId;
    }

    public void setTmpComponente(ProyectoComponente tmpComponente) {
        this.tmpComponente = tmpComponente;
    }

    public String getTempUnidadTecnicaComponente() {
        return tempUnidadTecnicaComponente;
    }

    public void setTempUnidadTecnicaComponente(String tempUnidadTecnicaComponente) {
        this.tempUnidadTecnicaComponente = tempUnidadTecnicaComponente;
    }

    public String getTempComponentePadre() {
        return tempComponentePadreIndex;
    }

    public void setTempComponentePadre(String tempComponentePadre) {
        this.tempComponentePadreIndex = tempComponentePadre;
    }

    public ProyectoComponente getTmpComponentePadre() {
        return tmpComponentePadre;
    }

    public void setTmpComponentePadre(ProyectoComponente tmpComponentePadre) {
        this.tmpComponentePadre = tmpComponentePadre;
    }

    public void setTmpDocumento(ProyectoDocumentos tmpDocumento) {
        this.tmpDocumento = tmpDocumento;
    }

    public ProyectoProrroga getTmpProrroga() {
        return tmpProrroga;
    }

    public void setTmpProrroga(ProyectoProrroga tmpProrroga) {
        this.tmpProrroga = tmpProrroga;
    }

    public String getTmpIdFuenteRecurso() {
        return tmpIdFuenteRecurso;
    }

    public void setTmpIdFuenteRecurso(String tmpIdFuenteRecurso) {
        this.tmpIdFuenteRecurso = tmpIdFuenteRecurso;
    }

    public ProyectoEnmienda getTmpEnmienda() {
        return tmpEnmienda;
    }

    public ProyectoEstructura getItemEstructura() {
        return itemEstructura;
    }

    public void setItemEstructura(ProyectoEstructura itemEstructura) {
        this.itemEstructura = itemEstructura;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setTmpEnmienda(ProyectoEnmienda tmpEnmienda) {
        this.tmpEnmienda = tmpEnmienda;
    }

    public String getIdProgramaPresupuestario() {
        return idProgramaPresupuestario;
    }

    public void setIdProgramaPresupuestario(String idProgramaPresupuestario) {
        this.idProgramaPresupuestario = idProgramaPresupuestario;
    }

    public String getIdProgramaInstitucional() {
        return idProgramaInstitucional;
    }

    public void setIdProgramaInstitucional(String idProgramaInstitucional) {
        this.idProgramaInstitucional = idProgramaInstitucional;
    }

    public Proyecto getObjeto() {
        return objeto;
    }

    public void setObjeto(Proyecto objeto) {
        this.objeto = objeto;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public boolean isUpdate() {
        return update;
    }

    public ProyectoDesembolso getTmpDesembolso() {
        return tmpDesembolso;
    }

    public void setTmpDesembolso(ProyectoDesembolso tmpDesembolso) {
        this.tmpDesembolso = tmpDesembolso;
    }

    public ProyectoAporte getTmpAporte() {
        return tmpAporte;
    }

    public void setTmpAporte(ProyectoAporte tmpAporte) {
        this.tmpAporte = tmpAporte;
    }

    public ProyectoCoEjecutora getTmpCoEjecutora() {
        return tmpCoEjecutora;
    }

    public void setTmpCoEjecutora(ProyectoCoEjecutora tmpCoEjecutora) {
        this.tmpCoEjecutora = tmpCoEjecutora;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getRestriccionXTipoProyecto() {
        return restriccionXTipoProyecto;
    }

    public void setRestriccionXTipoProyecto(String restriccionXTipoProyecto) {
        this.restriccionXTipoProyecto = restriccionXTipoProyecto;
    }

    public ProyectoAporteTramoTramo getTempTramo() {
        return tempTramo;
    }

    public void setTempTramo(ProyectoAporteTramoTramo tempTramo) {
        this.tempTramo = tempTramo;
    }

    public String getIdSubProgramaPresupuestario() {
        return idSubProgramaPresupuestario;
    }

    public void setIdSubProgramaPresupuestario(String idSubProgramaPresupuestario) {
        this.idSubProgramaPresupuestario = idSubProgramaPresupuestario;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getJsonGraficoTorta() {
        return jsonGraficoTorta;
    }

    public void setJsonGraficoTorta(String jsonGraficoTorta) {
        this.jsonGraficoTorta = jsonGraficoTorta;
    }

    public List<ProyectoAporte> getTmpFuentesParipassu() {
        return tmpFuentesParipassu;
    }

    public void setTmpFuentesParipassu(List<ProyectoAporte> tmpFuentesParipassu) {
        this.tmpFuentesParipassu = tmpFuentesParipassu;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
