/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POARiesgo;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.OrigenRiesgo;
import gob.mined.siap2.entities.enums.ValoracionRiesgo;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.datatypes.DataPOTablaLinea;
import static gob.mined.siap2.web.mb.impl.po.POConActividadesEInsumosAbstract.logger;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que trabaja con los POA de proyecto.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaProyectoCE")
public class POAProyectoCE extends POAProyectoBasico implements Serializable {


    protected String idProyecto;
    protected String idAnioFiscal;
    private List<AnioFiscal> posiblesAniosPOA;
    private Boolean actividadesPendientesParaCierreAnualPOA;
    private Boolean insumosPendientesParaCierreAnualPOA;
    private Boolean anioSeleccionadoFinalizado;
    private Boolean vuelveABandejaDeEntrada;
    private List<String> advertencias = new ArrayList();
    private Boolean cerrarAnio = Boolean.FALSE;
    Integer anioActual = null;
    @PostConstruct
    @Override
    public void init() {
        super.init();
        anioActual = LocalDate.now().getYear();
        idProyecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        idAnioFiscal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idAnioFiscal");
        idUnidadTecnica = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUnidadTecnica");
        vuelveABandejaDeEntrada = idUnidadTecnica != null;

        if (!TextUtils.isEmpty(idProyecto)) {
            objeto = proyectoDelegate.getProyecto(Integer.valueOf(idProyecto));
            posiblesAniosPOA = pOAProyectoDelegate.getAniosDisponiblesProgramacionPOA(objeto.getId());
            //si no va a ver nignun año y hay uno solo disponible ya lo setea
            if (TextUtils.isEmpty(idAnioFiscal) && posiblesAniosPOA.size() == 1) {
                idAnioFiscal = posiblesAniosPOA.get(0).getId().toString();
            }
        }
        //realoadUTDisponiblesParaUsuario();
        cambiaAnioSelecionado();
        //initProyecto();
        
        if(poa != null && poa.getId() != null ) {
            if(poa.getCierreAnual() != null && !poa.getCierreAnual() && poa.getAnioFiscal() != null) {
                if(poa.getAnioFiscal().getAnio().compareTo(anioActual) < 0) {
                    if(poa.getUnidadTecnica() != null && poa.getUnidadTecnica().getId() != null) {
                        cerrarAnio = usuarioDelegate.getUsuarioTienePermisoEnUTPadreConOperacion(poa.getUnidadTecnica().getId(), ConstantesEstandares.Operaciones.CERRAR_POA_PROYECTO_UNIDAD_RESPONSABLE, usuarioInfo.getUserCod());    
                    }
                }
            }
            
        }
    }

    /**
     * carga las unidades técnicas disponibles para el usuario
     */
    public void realoadUTDisponiblesParaUsuario() {
        if (!TextUtils.isEmpty(idAnioFiscal) && objeto != null) {
            usuarioUnidadTecnicas = pOAProyectoDelegate.getUTDisponiblesParaUsuario(objeto.getId(), Integer.valueOf(idAnioFiscal));
        } else {
            usuarioUnidadTecnicas = Collections.EMPTY_LIST;
        }
    }

    /**
     * método que se ejecuta cuando el usuario cambia el año seleccionado
     */
    public void cambiaAnioSelecionado() {
        realoadUTDisponiblesParaUsuario();
        if (TextUtils.isEmpty(idUnidadTecnica) && usuarioUnidadTecnicas.size() == 1) {
            this.idUnidadTecnica = usuarioUnidadTecnicas.get(0).getId().toString();
        }
        initProyecto();
    }

    /**
     * Reinicializa el proyecto cuanto entra en el init, cuando el usuario
     * cambia la UT, o cuando se cambia el año fiscal
     */
    public void initProyecto() {
        try {
            poa = null;
            if (!TextUtils.isEmpty(idAnioFiscal) && !TextUtils.isEmpty(idUnidadTecnica)) {
                reloadPO();
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            //RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método se utiliza para recargar el POA
     */
    @Override
    public void reloadPO() {
        poa = pOAProyectoDelegate.getPOATrabajo(objeto.getId(), Integer.valueOf(idAnioFiscal), Integer.valueOf(idUnidadTecnica));
        if (poa.getFechaHasta() == null) {
            poa.setFechaHasta(getFinPO());
        }
        if (poa.getFechaDesde() == null) {
            poa.setFechaDesde(getInicioPO());
        }
        //lineasTablaPOA = transformarPOAEnTabla(poa);
    }

    List<DataPOTablaLinea> lineasTablaPOA;

    /**
     * sobreescribe el método que retorna las unidades técnicas para que retorne
     * las que tiene acceso en el POG para el usuario
     *
     * @return
     */
    public List<UnidadTecnica> getUsuarioUnidadTecnicas() {
        return usuarioUnidadTecnicas;
    }

    /**
     * Se sobre-escribe el método que retorna las lineas disponibles en el POA actual
     * @return 
     */
    @Override
    public List<POLinea> getLineas() {
        if (poa != null) {
            return poa.getLineas();
        }
        return new LinkedList();
    }

    
    public void guardar(){
        poa = bean.guardarRiesgosPOAProyecto(poa);
    }
    /**
     * Este método se encarga de enviar el POA en edición para validar 
     */
    public void enviar() {
        try {
            pOAProyectoDelegate.enviarPOA(objeto.getId(), poa.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("consultaPOAProyecto.xhtml");
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

    
    public void cierreAnualPOA() {
        advertencias = new ArrayList();
        try {
            pOAProyectoDelegate.confirmarCierreAnualPOA(poa.getId(), Boolean.FALSE);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCierreCallBack').modal('show');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);

            if (ex.getErrores() == null || ex.getErrores().isEmpty()) {
                logger.log(Level.SEVERE, "la exepcion no tiene nada en la lista de mensajes");
            }

            for (String mensaje : ex.getErrores()) {
                logger.log(Level.SEVERE, "va a mostrar con el codigo " + mensaje);
                advertencias.add(jSFUtils.obtenerMensajeByProperty(mensaje));
            }
            RequestContext.getCurrentInstance().execute("$('#confirmModalCierreCallBack').modal('show');");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
       
    }
    
  
    public void confirmarCierreAnualPOA() {
        try {
            pOAProyectoDelegate.confirmarCierreAnualPOA(poa.getId(), Boolean.TRUE);
            FacesContext.getCurrentInstance().getExternalContext().redirect("consultaPOAProyecto.xhtml");
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
     * Este método retorna si el usuario tiene permiso de edición sobre el POA 
     * que se esta trabajando
     * 
     * @return 
     */
    public boolean tienePermisoEdicion() {
        if (poa != null) {
            return pOAProyectoDelegate.tienePermisoEdicionPOA(poa);
        }
        return true;
    }

    /**
     * método que retorna los años a seleccionar
     *
     * @return
     */
    public Map<String, String> getPosiblesAniosPOA() {
        Map<String, String> m = new LinkedHashMap<>();
        if (objeto != null) {
            for (AnioFiscal anio : posiblesAniosPOA) {
                m.put(String.valueOf(anio.getAnio()), String.valueOf(anio.getId()));
            }
        }
        return m;
    }

    /**
     * sobreescribe método de los años disponibles para trabajar en el POA
     *
     * @return
     */
    @Override
    public List<Integer> getListAniosPO() {
        List<Integer> l = new LinkedList<>();
        if (idAnioFiscal != null) {
            l.add(Integer.valueOf(idAnioFiscal));
        }
        return l;
    }

    /**
     * Se sobre escribe el método de añadir lineas al POA
     * @param tempLinea 
     */
    @Override
    public void addTmpLinea(POLinea tempLinea) {
        if (!poa.getLineas().contains(tempLinea)) {
            poa.getLineas().add(tempLinea);
        }
    }

    /**
     * Se sobre escribe el método de eliminar lineas del poa
     * 
     * @param tempLinea 
     */
    @Override
    public void eliminarTmpLinea(POLinea tempLinea) {
        poa.getLineas().remove(tempLinea);
    }

    /**
     * Este método es el encargado de dar la fecha de inicio del POA
     * @return 
     */
    @Override
    public Date getInicioPO() {
        if (TextUtils.isEmpty(idAnioFiscal)) {
            if(objeto != null) {
                return objeto.getInicio();
            }
        }
        AnioFiscal anio = (AnioFiscal) emd.getEntityById(AnioFiscal.class.getName(), Integer.valueOf(idAnioFiscal));

        Calendar calDesde = Calendar.getInstance();
        calDesde.set(Calendar.YEAR, anio.getAnio());
        calDesde.set(Calendar.DAY_OF_YEAR, 1);
        return calDesde.getTime();
    }

    /**
     * Este método es el encargado de dar la fecha de fin del POA
     * @return 
     */
    @Override
    public Date getFinPO() {
        if (TextUtils.isEmpty(idAnioFiscal)) {
            if(objeto != null) {
                return objeto.getFin();
            }
        }
        AnioFiscal anio = (AnioFiscal) emd.getEntityById(AnioFiscal.class.getName(), Integer.valueOf(idAnioFiscal));

        Calendar calHasta = Calendar.getInstance();
        calHasta.set(Calendar.YEAR, anio.getAnio());
        calHasta.set(Calendar.MONTH, 11);
        calHasta.set(Calendar.DAY_OF_MONTH, 31);
        return calHasta.getTime();
    }

    // <editor-fold defaultstate="collapsed" desc="MANEJO DE RIESGOS">
    protected POARiesgo tmpRiesgo;

    /**
     * Este método se utiliza para inicializar un riesgo, en caso que no exista lo crea.
     */
    public void initRiesgo() {
        if (tmpRiesgo == null) {
            tmpRiesgo = new POARiesgo();
        }
    }

    /**
     * Este método se utiliza para guardar un riesgo
     */
    public void saveRiesgo() {
        try {
            if (!poa.getRiesgos().contains(tmpRiesgo)) {
                poa.getRiesgos().add(tmpRiesgo);
            }
            guardar();
            RequestContext.getCurrentInstance().execute("$('#anadirRiesgo').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método se utiliza para eliminar un riesgo
     */
    public void eliminarRiesgo() {
        try {
            poa.getRiesgos().remove(tmpRiesgo);
            guardar();
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
    
    public OrigenRiesgo[] getOrigenRiesgos() {
        return OrigenRiesgo.values();
    }

    public ValoracionRiesgo[] getValoracionRiesgos() {
        return ValoracionRiesgo.values();
    }
    
    /**
     * Este método se utiliza para volver al menú anterior
     * 
     * @return 
     */
    public String volver(){
        String urlVuelta = "";
        if(vuelveABandejaDeEntrada){
            urlVuelta = "consultaNotificaciones.xhtml?faces-redirect=true";
        } else {
            urlVuelta = "consultaPOAProyecto.xhtml?faces-redirect=true";
        }
        return urlVuelta;
    }

    public List<String> getAdvertencias() {
        return advertencias;
    }

    public void setAdvertencias(List<String> advertencias) {
        this.advertencias = advertencias;
    }

    public Boolean getCerrarAnio() {
        return cerrarAnio;
    }

    public void setCerrarAnio(Boolean cerrarAnio) {
        this.cerrarAnio = cerrarAnio;
    }



    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public List<DataPOTablaLinea> getLineasTablaPOA() {
        return lineasTablaPOA;
    }

    public void setLineasTablaPOA(List<DataPOTablaLinea> lineasTablaPOA) {
        this.lineasTablaPOA = lineasTablaPOA;
    }

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public POAProyecto getPoa() {
        return poa;
    }

    public POARiesgo getTmpRiesgo() {
        return tmpRiesgo;
    }

    public void setTmpRiesgo(POARiesgo tmpRiesgo) {
        this.tmpRiesgo = tmpRiesgo;
    }

    public void setPoa(POAProyecto poa) {
        this.poa = poa;
    }

    public Boolean getActividadesPendientesParaCierreAnualPOA() {
        return actividadesPendientesParaCierreAnualPOA;
    }

    public void setActividadesPendientesParaCierreAnualPOA(Boolean actividadesPendientesParaCierreAnualPOA) {
        this.actividadesPendientesParaCierreAnualPOA = actividadesPendientesParaCierreAnualPOA;
    }

    public Boolean getInsumosPendientesParaCierreAnualPOA() {
        return insumosPendientesParaCierreAnualPOA;
    }

    public void setInsumosPendientesParaCierreAnualPOA(Boolean insumosPendientesParaCierreAnualPOA) {
        this.insumosPendientesParaCierreAnualPOA = insumosPendientesParaCierreAnualPOA;
    }

    public Boolean getAnioSeleccionadoFinalizado() {
        return anioSeleccionadoFinalizado;
    }

    public void setAnioSeleccionadoFinalizado(Boolean anioSeleccionadoFinalizado) {
        this.anioSeleccionadoFinalizado = anioSeleccionadoFinalizado;
    }
        
    // </editor-fold>

    
}
