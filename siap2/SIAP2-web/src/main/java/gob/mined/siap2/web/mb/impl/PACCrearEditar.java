/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.enums.EstadoGrupoPAC;
import gob.mined.siap2.entities.enums.EstadoPAC;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.datatypes.DataPacItem;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.AccionCentralDelegate;
import gob.mined.siap2.web.delegates.impl.PACDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "pacCE")
public class PACCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private static final String ESTADO_INICIO = "ESTADO_INICIO";
    private static final String ESTADO_CREAR_EDITAR_PAC = "ESTADO_CREAR_EDITAR_PAC";

    @Inject
    PACDelegate pACDelegate;
    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    AccionCentralDelegate accionCentralD;
    @Inject
    TextMB textMB;

    private boolean update = false;
    private PAC objeto;
    private PACGrupo tempGrupo;
    private POInsumos insumoEnEdicion;
    private String estado = ESTADO_INICIO;

    private TreeNode selectedNode1;
    private TreeNode selectedNode2;

    private String filtroGrupoNro;
    private EstadoGrupoPAC filtroGrupoEstado;
    private String filtroGrupoidMetodoAdquisicion;

    private String filtroInsmoRubro;
    private String filtroInsmoCodigo;
    private String filtroInsmoNombre;
    private String filtroInsumoEstado = "SIN_GRUPO";    
    private POInsumos tmpI = null;
    private String idGrupo2;
    private PACGrupo unificarGrupo1;

    private List<POInsumos> insumosSeleccionados = new LinkedList();

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = pACDelegate.getPAC(Integer.valueOf(id));
        } 

    }

    /**
     * Recarga el objeto PAC que hay en memoria desde la base
     */
    public void recargarPAC() {
        objeto = pACDelegate.getPAC(objeto.getId());
        //si hay algun grupo preselecionado lo vuelve a cargar
        if (tempGrupo != null) {
            boolean encontro = false;
            Iterator<PACGrupo> iter = objeto.getGrupos().iterator();
            while (iter.hasNext() && !encontro) {
                PACGrupo iterg = iter.next();
                if (iterg.getId().equals(tempGrupo.getId())) {
                    encontro = true;
                    tempGrupo = iterg;
                }
            }
        }
    }

    /**
     * Guarda las modificaciones realizadas a un PAC
     */
    public void guardar() {
        try {
            objeto = pACDelegate.guardarPAC(objeto);
            String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
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
     * Bolquea un PAC
     */
    public void bloquear() {
        try {
            objeto = pACDelegate.bloquearPAC(objeto.getId());
            String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
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
     * Desbloquea un PAC
     */
    public void desBloquear() {
        try {
            objeto = pACDelegate.desbloquear(objeto.getId());
            String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
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
     * Inicializa un PAC
     */
    public void iniciarPAC() {
        try {
            objeto = pACDelegate.iniciarPAC(objeto);
            String texto = textMB.obtenerTexto("labels.PACIniciadoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
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
     * Mueve insumos de un grupo de un PAC a otro grupo
     */
    public void moverInsumos() {
        try {
            if (selectedNode1 == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_GRUPO);
                throw b;
            }
            DataPacItem dg = (DataPacItem) selectedNode1.getData();
            if (!dg.getTipo().equals(DataPacItem.GRUPO)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_GRUPO);
                throw b;
            }
            if (insumosSeleccionados == null || insumosSeleccionados.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_AL_MENOS_DEBE_SELECCIONAR_UN_INSUMO);
                throw b;
            }

            List<Integer> insumosList = new ArrayList<Integer>(insumosSeleccionados.size());
            for (POInsumos i : insumosSeleccionados) {
                insumosList.add(i.getId());
            }

            pACDelegate.asociarInsumo(((PACGrupo) dg.getObjeto()).getId(), insumosList);
            recargarPAC();
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
     * Devuelve un nodo correspondiente a un Grupo de un PAC
     * @return 
     */
    public TreeNode getRootNodeGrupo() {
        TreeNode root = new DefaultTreeNode(new DataPacItem(DataPacItem.ROOT, null), null);
        for (PACGrupo g : objeto.getGrupos()) {
            boolean cumpleFiltro = true;
            if (cumpleFiltro && !TextUtils.isEmpty(filtroGrupoNro)) {
                if (!g.getId().toString().contains(filtroGrupoNro)) {
                    cumpleFiltro = false;
                }
            }
            if (cumpleFiltro && filtroGrupoEstado != null) {
                if (filtroGrupoEstado != g.getEstado()) {
                    cumpleFiltro = false;
                }
            }

            if (cumpleFiltro && !TextUtils.isEmpty(filtroGrupoidMetodoAdquisicion)) {
                if (g.getMetodoAdquisicion() == null) {
                    cumpleFiltro = false;
                } else if (!g.getMetodoAdquisicion().getId().equals(Integer.valueOf(filtroGrupoidMetodoAdquisicion))) {
                    cumpleFiltro = false;
                }
            }

            if (cumpleFiltro) {
                if(g.getInsumos() != null) {
                    List<POInsumos> listaInsumos = new ArrayList();
                    for(POInsumos ins : g.getInsumos()) {
                        if(ins.getHabilitado() != null && ins.getHabilitado()) {
                            listaInsumos.add(ins);
                        }
                    }
                    if(listaInsumos != null && !listaInsumos.isEmpty() && listaInsumos.size() > 0) {
                        DataPacItem dg = new DataPacItem(DataPacItem.GRUPO, g);
                        dg.setNombre("grupoG - " + g.getNombre());
                        TreeNode grupoNode = new DefaultTreeNode(dg, root);

                        for (POInsumos insumo : listaInsumos) {
                            DataPacItem di = new DataPacItem(DataPacItem.INSUMO, insumo);
                            di.setNombre("insumoG - " + insumo.getInsumo().getNombre());
                            TreeNode insumoNode = new DefaultTreeNode(di, grupoNode);

                        }
                    }
                } 
            }
        }
        return root;
    }

    /**
     * Devuelve los insumos que no han sido asignados a grupos
     * @return 
     */
    public List<POInsumos> getInsumosPendientesAgrupar() {
        List<POInsumos> lista = new LinkedList();
        for (POInsumos insumoToAdd : objeto.getInsumos()) {
            boolean cumple = true;

            if (!"TODOS".equals(filtroInsumoEstado)) {
                boolean encontro = false;
                Iterator<PACGrupo> iterGrupo = objeto.getGrupos().iterator();
                while (!encontro && iterGrupo.hasNext()) {
                    PACGrupo g = iterGrupo.next();
                    Iterator<POInsumos> iterInsumos = g.getInsumos().iterator();
                    while (!encontro && iterInsumos.hasNext()) {
                        POInsumos i = iterInsumos.next();
                        if (i.equals(insumoToAdd)) {
                            encontro = true;
                        }
                    }
                }
                if ("SIN_GRUPO".equals(filtroInsumoEstado) && encontro) {
                    cumple = false;
                } else if ("CON_GRUPO".equals(filtroInsumoEstado) && !encontro) {
                    cumple = false;
                }
            }
            if (cumple && !TextUtils.isEmpty(filtroInsmoRubro)) {
                cumple = String.valueOf(insumoToAdd.getInsumo().getObjetoEspecificoGasto().getClasificador()).contains(filtroInsmoRubro);
            }
            if (cumple && !TextUtils.isEmpty(filtroInsmoCodigo)) {
                cumple = insumoToAdd.getInsumo().getCodigo().contains(filtroInsmoCodigo);
            }
            if (cumple && !TextUtils.isEmpty(filtroInsmoNombre)) {
                cumple = String.valueOf(insumoToAdd.getInsumo().getNombre()).contains(filtroInsmoNombre);
            }
            if (cumple) {
                lista.add(insumoToAdd);
            }

        }
        return lista;
    }

    /**
     * Crea los grupos de PAC sugeridos y recarga el PAC
     */
    public void regenerarGrupos() {
        try {
            pACDelegate.generarGruposSugeridos(objeto.getId());
            recargarPAC();
            estado = ESTADO_INICIO;
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
     * Desasocia un insumo de un grupo de un PAC
     * @param insumo 
     */
    public void desacociarInsumo(POInsumos insumo) {
        pACDelegate.desacociarInsumo(insumo.getId());
        recargarPAC();
        RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        tmpI = null;

    }

    /**
     * Elimina un grupo de un PAC
     * @param grupo 
     */
    public void eliminarGrupo(PACGrupo grupo) {
        pACDelegate.eliminarGrupo(grupo.getId());
        recargarPAC();
        RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
    }

    /**
     * Si el nodo seleccionado es un grupo lo elimina, si es un insumo lo desasocia del grupo de PAC al que pertenecía
     */
    public void eliminarEnGrupo() {
        try {
            if (selectedNode1 == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_GRUPO);
                throw b;
            }
            DataPacItem dpi = (DataPacItem) selectedNode1.getData();
            if (DataPacItem.INSUMO.equals(dpi.getTipo())) {
                desacociarInsumo((POInsumos) dpi.getObjeto());
            } else if (DataPacItem.GRUPO.equals(dpi.getTipo())) {
                eliminarGrupo((PACGrupo) dpi.getObjeto());
            }
            selectedNode1 = null;

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
     * Fusiona dos grupos en uno solo y recarga el PAC
     */
    public void unificarGrupos() {
        try {
            pACDelegate.unificarGrupos(Integer.valueOf(idGrupo2), unificarGrupo1.getId());
            recargarPAC();
            RequestContext.getCurrentInstance().execute("$('#unificarGrupos').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Abre el pop-up para añadir un insumo
     */
    public void cargarInsumo() {
        RequestContext.getCurrentInstance().execute("$('#veranadirInsumo').modal('show');");
    }

    // <editor-fold defaultstate="collapsed" desc="getter-generados">
    public Map<String, String> getGruposParaUnificar() {
        Map<String, String> map = new HashMap();
        for (PACGrupo iter : objeto.getGrupos()) {
            if (!iter.equals(unificarGrupo1)) {
                map.put(iter.getNombre(), iter.getId().toString());
            }
        }
        return map;
    }

    public EstadoGrupoPAC[] getEstadosGrupos() {
        return EstadoGrupoPAC.values();
    }

    public EstadoPAC[] getEstadoPAC() {
        return EstadoPAC.values();
    }

    public void limpiarGrupo() {
        try {
            filtroGrupoNro = null;
            filtroGrupoEstado = null;
            filtroGrupoidMetodoAdquisicion = null;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    public void limpiarInsumo() {
        try {
            insumosSeleccionados = null;
            filtroInsmoRubro = null;
            filtroInsmoCodigo = null;
            filtroInsmoNombre = null;
            filtroInsumoEstado = "SIN_GRUPO";
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    public Map<String, String> getMetodosAdquisicion() {
        Map<String, String> map = new HashMap();
        List<MetodoAdquisicion> l = emd.getEntities(MetodoAdquisicion.class.getName(), "nombre");
        for (MetodoAdquisicion o : l) {
            map.put(o.getNombre(), o.getId().toString());
        }
        return map;
    }

    public void test() {
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public PACGrupo getTempGrupo() {
        return tempGrupo;
    }

    public void setTempGrupo(PACGrupo tempGrupo) {
        this.tempGrupo = tempGrupo;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public PAC getObjeto() {
        return objeto;
    }

    public String getIdGrupo2() {
        return idGrupo2;
    }

    public void setIdGrupo2(String idGrupo2) {
        this.idGrupo2 = idGrupo2;
    }

    public PACGrupo getUnificarGrupo1() {
        return unificarGrupo1;
    }

    public void setUnificarGrupo1(PACGrupo unificarGrupo1) {
        this.unificarGrupo1 = unificarGrupo1;
    }

    public POInsumos getInsumoEnEdicion() {
        return insumoEnEdicion;
    }

    public void setInsumoEnEdicion(POInsumos insumoEnEdicion) {
        this.insumoEnEdicion = insumoEnEdicion;
    }

    public TreeNode getSelectedNode1() {
        return selectedNode1;
    }

    public void setSelectedNode1(TreeNode selectedNode1) {
        this.selectedNode1 = selectedNode1;
    }

    public TreeNode getSelectedNode2() {
        return selectedNode2;
    }

    public void setSelectedNode2(TreeNode selectedNode2) {
        this.selectedNode2 = selectedNode2;
    }

    public void setObjeto(PAC objeto) {
        this.objeto = objeto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isUpdate() {
        return update;
    }

    public String getFiltroGrupoNro() {
        return filtroGrupoNro;
    }

    public List<POInsumos> getInsumosSeleccionados() {
        return insumosSeleccionados;
    }

    public void setInsumosSeleccionados(List<POInsumos> insumosSeleccionados) {
        this.insumosSeleccionados = insumosSeleccionados;
    }

    public void setFiltroGrupoNro(String filtroGrupoNro) {
        this.filtroGrupoNro = filtroGrupoNro;
    }

    public EstadoGrupoPAC getFiltroGrupoEstado() {
        return filtroGrupoEstado;
    }

    public void setFiltroGrupoEstado(EstadoGrupoPAC filtroGrupoEstado) {
        this.filtroGrupoEstado = filtroGrupoEstado;
    }

    public String getFiltroInsmoRubro() {
        return filtroInsmoRubro;
    }

    public void setFiltroInsmoRubro(String filtroInsmoRubro) {
        this.filtroInsmoRubro = filtroInsmoRubro;
    }

    public String getFiltroInsmoCodigo() {
        return filtroInsmoCodigo;
    }

    public void setFiltroInsmoCodigo(String filtroInsmoCodigo) {
        this.filtroInsmoCodigo = filtroInsmoCodigo;
    }

    public String getFiltroInsmoNombre() {
        return filtroInsmoNombre;
    }

    public void setFiltroInsmoNombre(String filtroInsmoNombre) {
        this.filtroInsmoNombre = filtroInsmoNombre;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getFiltroGrupoidMetodoAdquisicion() {
        return filtroGrupoidMetodoAdquisicion;
    }

    public void setFiltroGrupoidMetodoAdquisicion(String filtroGrupoidMetodoAdquisicion) {
        this.filtroGrupoidMetodoAdquisicion = filtroGrupoidMetodoAdquisicion;
    }

    public String getFiltroInsumoEstado() {
        return filtroInsumoEstado;
    }

    public POInsumos getTmpI() {
        return tmpI;
    }

    public void setTmpI(POInsumos tmpI) {
        this.tmpI = tmpI;
    }

    public void setFiltroInsumoEstado(String filtroInsumoEstado) {
        this.filtroInsumoEstado = filtroInsumoEstado;
    }

    // </editor-fold>
}
