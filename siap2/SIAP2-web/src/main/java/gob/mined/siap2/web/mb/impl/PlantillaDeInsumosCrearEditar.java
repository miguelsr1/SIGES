/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.datatype.DataInsumoPlantilla;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo1Segmento;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo2Familia;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo3Clase;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo4Articulo;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.NodoPlantillaDeInsumos;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.PlantillaDeInsumosDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.util.TreeUtils;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "plantillaDeInsumosCE")
public class PlantillaDeInsumosCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    PlantillaDeInsumosDelegate plantillaDeInsumosDelegate;
    @Inject
    TextMB textMB;

    private boolean update = false;
    private NodoPlantillaDeInsumos objeto;
    private TreeNode rootNodeONU;
    private TreeNode[] selectedNodesONU;

    private TreeNode rootPlantilla;
    private TreeNode nodoPlantillaSeleccionado;

    private String nombreGrupo;

    private Boolean noMoverInsumos = true;

    private String filtroONURubro;
    private String filtroONUCodigo;
    private String filtroONUnombre;
    private Boolean filtroONUSoloSinPlantillas = false;
    private SofisComboG<NodoPlantillaDeInsumos> comboNodosPlantillaDeInsumos;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;

        } else {
            objeto = new NodoPlantillaDeInsumos();
            objeto.setInsumos(new LinkedList());
        }

        rootNodeONU = createRootNodeONU();
        rootPlantilla = createRootNodePlantilla();
        initComoNodosPlantillaDeInsumos();
    }

    /**
     * Crea un nodo Categoría, lo agrega al árbol padre y lo devuelve
     * @param c
     * @param padre
     * @return 
     */
    private TreeNode agregarCategoria(NodoPlantillaDeInsumos c, TreeNode padre) {
        String nombre = c.getNombre();
        DataInsumoPlantilla dip = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_CATEGORIA, nombre, false, c);
        TreeNode document1 = new DefaultTreeNode(dip, padre);

        if (!c.getHijos().isEmpty() || !c.getInsumos().isEmpty()) {
            DataInsumoPlantilla temp = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_DUMMY, null, false, null);
            TreeNode dummy = new DefaultTreeNode(temp, document1);
        }
        return document1;
    }

    /**
     * Crea un nodo insumo y lo agrega al árbol padre
     * @param i
     * @param padre 
     */
    private void agregarInsumo(Insumo i, TreeNode padre) {
        String nombre = i.getNombre();
        DataInsumoPlantilla dip = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_INSUMO, nombre, false, i);
        TreeNode document1 = new DefaultTreeNode(dip, padre);
    }

    /**
     * Construye el árbol de insumos
     * @return 
     */
    private TreeNode createRootNodePlantilla() {
        TreeNode root = new DefaultTreeNode(new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_ROOT, null, false, null), null);

        List<NodoPlantillaDeInsumos> l = plantillaDeInsumosDelegate.loadRootInsumos();
        for (NodoPlantillaDeInsumos c : l) {
            agregarCategoria(c, root);
        }

        TreeUtils.sortNode(root, ordenadorNodesPlantilla);
        return root;
    }

    /**
     * Ordena los nodos del árbol de insumos
     */
    private static Comparator ordenadorNodesPlantilla = new Comparator<TreeNode>() {
        @Override
        public int compare(TreeNode o1, TreeNode o2) {
            DataInsumoPlantilla dip1 = (DataInsumoPlantilla) o1.getData();
            DataInsumoPlantilla dip2 = (DataInsumoPlantilla) o2.getData();

            if (dip1.getType().equals(DataInsumoPlantilla.TIPO_CATEGORIA)
                    && dip2.getType().equals(DataInsumoPlantilla.TIPO_CATEGORIA)) {
                NodoPlantillaDeInsumos nodo1 = (NodoPlantillaDeInsumos) (dip1).getObjeto();
                NodoPlantillaDeInsumos nodo2 = (NodoPlantillaDeInsumos) (dip2).getObjeto();
                return nodo1.getNombre().compareTo(nodo2.getNombre());

            } else if (dip1.getType().equals(DataInsumoPlantilla.TIPO_INSUMO)
                    && dip2.getType().equals(DataInsumoPlantilla.TIPO_INSUMO)) {
                Insumo i1 = (Insumo) dip1.getObjeto();
                Insumo i2 = (Insumo) dip2.getObjeto();
                return i1.getNombre().compareTo(i2.getNombre());
            }

            return 0;
        }
    };

    /**
     * Al expandir un nodo del árbol carga los elementos hijos correspondientes
     * @param event 
     */
    public void onNodeExpandPlnatilla(NodeExpandEvent event) {
        DefaultTreeNode padre = (DefaultTreeNode) event.getTreeNode();
        DataInsumoPlantilla dataPadre = (DataInsumoPlantilla) padre.getData();
        //creo qeu ;ade uno como dummy y despues lo borra

        padre.getChildren().remove(0);
        padre.getChildren().clear();

        if (dataPadre.getType().equals(DataInsumoPlantilla.TIPO_CATEGORIA)) {
            NodoPlantillaDeInsumos nodo = plantillaDeInsumosDelegate.loadInsumo(((NodoPlantillaDeInsumos) dataPadre.getObjeto()).getId());
            for (NodoPlantillaDeInsumos c : nodo.getHijos()) {
                agregarCategoria(c, padre);
            }
            for (Insumo i : nodo.getInsumos()) {
                agregarInsumo(i, padre);
            }
        }

        TreeUtils.sortNode(padre, ordenadorNodesPlantilla);
    }

    /**
     * Crea un grupo para agregarle insumos y lo agrega al árbol de insumos
     */
    public void agregarGrupo() {
        try {
            TreeNode padre = rootPlantilla;
            DataInsumoPlantilla dataPadre = (DataInsumoPlantilla) padre.getData();
            NodoPlantillaDeInsumos oPadre = (NodoPlantillaDeInsumos) dataPadre.getObjeto();

            NodoPlantillaDeInsumos objeto = new NodoPlantillaDeInsumos();
            objeto.setInsumos(new LinkedList());
            objeto.setPadre(oPadre);
            objeto.setHijos(new LinkedList());
            objeto.setNombre(nombreGrupo);
            objeto = (NodoPlantillaDeInsumos) emd.saveOrUpdate(objeto);

            DataInsumoPlantilla dip = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_CATEGORIA, nombreGrupo, false, objeto);
            TreeNode nuevo = new DefaultTreeNode(dip, padre);

            this.nombreGrupo = null;
            initComoNodosPlantillaDeInsumos();
            String texto = textMB.obtenerTexto("labels.GrupoCreadoCorrectamente");
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
     * Elimina un grupo o un insumo del árbol (dependiendo del nodo seleccionado) de grupos 
     */
    public void eliminarInsumo() {
        try {
            if (nodoPlantillaSeleccionado == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_INSUMO);
                throw b;
            }
            DataInsumoPlantilla dip = (DataInsumoPlantilla) nodoPlantillaSeleccionado.getData();

            String texto = "";

            if (dip.getType().equals(DataInsumoPlantilla.TIPO_CATEGORIA)) {
                NodoPlantillaDeInsumos nodo = (NodoPlantillaDeInsumos) (dip).getObjeto();
                plantillaDeInsumosDelegate.eliminarNodoPlantilla(nodo.getId());
                texto = textMB.obtenerTexto("labels.GrupoEliminadoCorrectamente");
            } else if (dip.getType().equals(DataInsumoPlantilla.TIPO_INSUMO)) {
                Insumo i = (Insumo) dip.getObjeto();
                plantillaDeInsumosDelegate.eliminarInsumoPlantilla(i.getId());
                texto = textMB.obtenerTexto("labels.InsumoEliminadoCorrectamente");
            }

            nodoPlantillaSeleccionado.getParent().getChildren().remove(nodoPlantillaSeleccionado);
            filtrarArbolONU();
            initComoNodosPlantillaDeInsumos();

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
     * Agrega un insumo a un grupo
     */
    public void agregarInsumosSelecionados() {
        try {

            if (selectedNodesONU == null || selectedNodesONU.length == 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_INSUMO_EN_ARBOL_ONU);
                throw b;
            }
            if (nodoPlantillaSeleccionado == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_EL_GRUPO_EN_EN_PLANTILLA);
                throw b;
            } else {
                DataInsumoPlantilla dp = (DataInsumoPlantilla) nodoPlantillaSeleccionado.getData();
                if (!DataInsumoPlantilla.TIPO_CATEGORIA.equals(dp.getType())) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_EL_GRUPO_EN_EN_PLANTILLA);
                    throw b;
                }
            }

            //se pasan a una lista todos los elementos selecionados
            List<DataInsumoPlantilla> l = new LinkedList();
            for (TreeNode iter : selectedNodesONU) {
                DataInsumoPlantilla dataPadre = (DataInsumoPlantilla) iter.getData();
                l.add(dataPadre);
            }

            //agrega todos los insumos a la categoría
            NodoPlantillaDeInsumos nodo = (NodoPlantillaDeInsumos) ((DataInsumoPlantilla) nodoPlantillaSeleccionado.getData()).getObjeto();
            Integer rubro = NumberUtils.getIntegerONull(filtroONURubro);
            this.plantillaDeInsumosDelegate.agregarInsumos(l, nodo.getId(), noMoverInsumos, filtroONUCodigo, rubro, filtroONUnombre, filtroONUSoloSinPlantillas);

            rootPlantilla = createRootNodePlantilla();
            nodoPlantillaSeleccionado = null;
            RequestContext.getCurrentInstance().update("arbolDeInsumos");

            if (noMoverInsumos == null || !noMoverInsumos) {
                String texto = textMB.obtenerTexto("labels.InsumosAgregadosCorrectamente");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            }

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
     * Selecciona un nodo del árbol de insumos ONU
     * @param nodo 
     */
    public void seleccionarNodoEnONU(TreeNode nodo) {
        if (nodo != null) {
            nodo.setSelected(true);
            if (selectedNodesONU == null) {
                selectedNodesONU = new TreeNode[1];
            } else {
                selectedNodesONU = Arrays.copyOf(selectedNodesONU, selectedNodesONU.length + 1);
            }
            selectedNodesONU[selectedNodesONU.length - 1] = nodo;
        }
    }

    /**
     * Verifica si un nodo está seleccionado
     * @param nodo
     * @return 
     */
    public boolean estaSeleccionadoEnONU(Object nodo) {
        if (selectedNodesONU == null) {
            return false;
        }
        if (nodo == null) {
            return false;
        }
        for (TreeNode iter : selectedNodesONU) {
            Object iterO = ((DataInsumoPlantilla) iter.getData()).getObjeto();
            if (nodo.equals(iterO)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Llama al método que genera el árbol que contiene los insumos ONU
     */
    private void regenerarARbolONU() {
        rootNodeONU = createRootNodeONU();
    }

    /**
     * Genera el árbol que contiene los insumos ONU
     * @return 
     */
    private TreeNode createRootNodeONU() {
        TreeNode root = new CheckboxTreeNode(new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_ROOT, null, false, null), null);

        String[] orderBy = {"titulo"};
        boolean[] asc = {true};
        List<CodificacionInsumo1Segmento> l = emd.findEntityByCriteria(CodificacionInsumo1Segmento.class, null, orderBy, asc, null, null);
        for (CodificacionInsumo1Segmento c : l) {
            if (countInsumosEnONU(DataInsumoPlantilla.TIPO_SEGMENTO, c.getId()) > 0) {
                String nombre = c.getCodigo() + " " + c.getTitulo();
                DataInsumoPlantilla dip = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_SEGMENTO, nombre, false, c);
                TreeNode document1 = new CheckboxTreeNode(dip, root);

                //AÑADE UN DUMMY PARA TODOS
                DataInsumoPlantilla temp = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_DUMMY, null, false, c);
                TreeNode dummy = new CheckboxTreeNode(temp, document1);

                if (estaSeleccionadoEnONU(c)) {
                    seleccionarNodoEnONU(document1);
                    seleccionarNodoEnONU(dummy);
                }
            }
        }

        return root;
    }

    /**
     * Al expandir un nodo del árbol de insumos ONU, carga los elementos hijos correspondientes
     * @param event 
     */
    public void onNodeExpandONU(NodeExpandEvent event) {
        CheckboxTreeNode padre = (CheckboxTreeNode) event.getTreeNode();
        DataInsumoPlantilla dataPadre = (DataInsumoPlantilla) padre.getData();
        //creo qeu ;ade uno como dummy y despues lo borra
        padre.getChildren().remove(0);
        padre.getChildren().clear();

        if (dataPadre.getType().equals(DataInsumoPlantilla.TIPO_SEGMENTO)) {

            CodificacionInsumo1Segmento segmento = (CodificacionInsumo1Segmento) dataPadre.getObjeto();
            String[] orderBy = {"titulo"};
            boolean[] asc = {true};
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "padre.id", segmento.getId());
            List<CodificacionInsumo2Familia> l2 = emd.findEntityByCriteria(CodificacionInsumo2Familia.class, criterio, orderBy, asc, null, null);
            for (CodificacionInsumo2Familia c : l2) {
                if (countInsumosEnONU(DataInsumoPlantilla.TIPO_FAMILIA, c.getId()) > 0) {
                    String nombre = c.getCodigo() + " " + c.getTitulo();
                    DataInsumoPlantilla dip2 = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_FAMILIA, nombre, false, c);
                    TreeNode document = new CheckboxTreeNode(dip2, event.getTreeNode());

                    DataInsumoPlantilla temp = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_DUMMY, null, false, c);
                    TreeNode dummy = new CheckboxTreeNode(temp, document);

                    if (estaSeleccionadoEnONU(c.getPadre()) || estaSeleccionadoEnONU(c)) {
                        seleccionarNodoEnONU(document);
                        seleccionarNodoEnONU(dummy);
                    }
                }
            }

        } else if (dataPadre.getType().equals(DataInsumoPlantilla.TIPO_FAMILIA)) {
            CodificacionInsumo2Familia familia = (CodificacionInsumo2Familia) dataPadre.getObjeto();
            String[] orderBy = {"titulo"};
            boolean[] asc = {true};
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "padre.id", Integer.valueOf(familia.getId()));
            List<CodificacionInsumo3Clase> l3 = emd.findEntityByCriteria(CodificacionInsumo3Clase.class, criterio, orderBy, asc, null, null);
            for (CodificacionInsumo3Clase c : l3) {
                if (countInsumosEnONU(DataInsumoPlantilla.TIPO_CLASE, c.getId()) > 0) {
                    String nombre = c.getCodigo() + " " + c.getTitulo();
                    DataInsumoPlantilla dip3 = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_CLASE, nombre, false, c);
                    TreeNode document = new CheckboxTreeNode(dip3, padre);

                    DataInsumoPlantilla temp = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_DUMMY, null, false, c);
                    TreeNode dummy = new CheckboxTreeNode(temp, document);

                    if (estaSeleccionadoEnONU(c.getPadre().getPadre()) || estaSeleccionadoEnONU(c.getPadre()) || estaSeleccionadoEnONU(c)) {
                        seleccionarNodoEnONU(document);
                        seleccionarNodoEnONU(dummy);
                    }
                }
            }

        } else if (dataPadre.getType().equals(DataInsumoPlantilla.TIPO_CLASE)) {
            CodificacionInsumo3Clase clase = (CodificacionInsumo3Clase) dataPadre.getObjeto();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "padre.id", clase.getId());
            String[] orderBy = {"titulo"};
            boolean[] asc = {true};
            List<CodificacionInsumo4Articulo> l = emd.findEntityByCriteria(CodificacionInsumo4Articulo.class, criterio, orderBy, asc, null, null);
            for (CodificacionInsumo4Articulo c : l) {
                if (countInsumosEnONU(DataInsumoPlantilla.TIPO_ARTICULO, c.getId()) > 0) {
                    String nombre = c.getCodigo() + " " + c.getTitulo();
                    DataInsumoPlantilla dip3 = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_ARTICULO, nombre, false, c);
                    TreeNode document = new CheckboxTreeNode(dip3, padre);

                    TreeNode dummy = null;
                    DataInsumoPlantilla temp = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_DUMMY, null, false, c);
                    List<Insumo> hijos = emd.findByOneProperty(Insumo.class.getName(), "articulo.id", c.getId());
                    if (!hijos.isEmpty()) {
                        dummy = new CheckboxTreeNode(temp, document);
                    }

                    if (estaSeleccionadoEnONU(c.getPadre().getPadre().getPadre()) || estaSeleccionadoEnONU(c.getPadre().getPadre()) || estaSeleccionadoEnONU(c.getPadre()) || estaSeleccionadoEnONU(c)) {
                        seleccionarNodoEnONU(document);
                        seleccionarNodoEnONU(dummy);
                    }
                }
            }
        } else if (dataPadre.getType().equals(DataInsumoPlantilla.TIPO_ARTICULO)) {
            CodificacionInsumo4Articulo articulo = (CodificacionInsumo4Articulo) dataPadre.getObjeto();
            //VA A BUSCAR LOS INSUMOS
            List<Insumo> l = emd.findByOneProperty(Insumo.class.getName(), "articulo.id", articulo.getId());
            for (Insumo c : l) {
                if (countInsumosEnONU(DataInsumoPlantilla.TIPO_INSUMO, c.getId()) > 0) {
                    String nombre = c.getCodigo() + " " + c.getNombre();
                    DataInsumoPlantilla dip3 = new DataInsumoPlantilla(DataInsumoPlantilla.TIPO_INSUMO, nombre, false, c);
                    TreeNode document = new CheckboxTreeNode(dip3, padre);

                    if (estaSeleccionadoEnONU(c.getArticulo().getPadre().getPadre().getPadre()) || estaSeleccionadoEnONU(c.getArticulo().getPadre().getPadre()) || estaSeleccionadoEnONU(c.getArticulo().getPadre()) || estaSeleccionadoEnONU(c.getArticulo()) || estaSeleccionadoEnONU(c)) {
                        seleccionarNodoEnONU(document);
                    }
                }
            }
        }

    }

    /**
     * Devuelve la cantidad de insumos que hay en un nodo del árbol de Insumos ONU
     * @param tipo
     * @param id
     * @return 
     */
    private long countInsumosEnONU(String tipo, Integer id) {
        List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
        if (tipo.equals(DataInsumoPlantilla.TIPO_SEGMENTO)) {
            CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "articulo.padre.padre.padre.id", id);
            criterios.add(criterio);
        } else if (tipo.equals(DataInsumoPlantilla.TIPO_FAMILIA)) {
            CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "articulo.padre.padre.id", id);
            criterios.add(criterio);
        } else if (tipo.equals(DataInsumoPlantilla.TIPO_CLASE)) {
            CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "articulo.padre.id", id);
            criterios.add(criterio);
        } else if (tipo.equals(DataInsumoPlantilla.TIPO_ARTICULO)) {
            CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "articulo.id", id);
            criterios.add(criterio);
        } else if (tipo.equals(DataInsumoPlantilla.TIPO_INSUMO)) {
            CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", id);
            criterios.add(criterio);
        }

        if (!TextUtils.isEmpty(filtroONUCodigo)) {
            CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "codigo", filtroONUCodigo);
            criterios.add(criterio);
        }

        Integer rubro = NumberUtils.getIntegerONull(filtroONURubro);
        if (rubro != null) {
            CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "objetoEspecificoGasto.clasificador", rubro);
            criterios.add(criterio);
        }
        if (!TextUtils.isEmpty(filtroONUnombre)) {
            CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombre", filtroONUnombre);
            criterios.add(criterio);
        }
        if (filtroONUSoloSinPlantillas != null && filtroONUSoloSinPlantillas) {
            CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "plnatillaDeInsumos", 1);
            criterios.add(criterio);
        }

        CriteriaTO condicion = null;
        if (criterios.size() == 1) {
            condicion = criterios.get(0);
        } else {
            condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
        }

        return emd.getCountsByCriteria(Insumo.class.getName(), null, null, condicion);
    }

    /**
     * Devuelve la cantidad de insumos que no están asignados a un grupo
     * @return 
     */
    public long getCantidadDeInsumosSinAsignar() {
        CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "plnatillaDeInsumos", 1);
        return emd.getCountsByCriteria(Insumo.class.getName(), null, null, criterio);
    }

    /**
     * Borra todos los filtros del árbol de insumos ONU
     */
    public void limparArbolONU() {
        filtroONUCodigo = null;
        filtroONURubro = null;
        filtroONUnombre = null;
        filtroONUSoloSinPlantillas = false;
        filtrarArbolONU();
    }

    /**
     * Genera el Árbol de insumos ONU según los filtros ingresados
     */
    public void filtrarArbolONU() {
        try {
            regenerarARbolONU();
            selectedNodesONU = null;
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
     * Carga el combo de grupos
     */
    public void initComoNodosPlantillaDeInsumos() {
        List<NodoPlantillaDeInsumos> l = plantillaDeInsumosDelegate.loadRootInsumos();
        comboNodosPlantillaDeInsumos = new SofisComboG<>(l, "nombre");
        comboNodosPlantillaDeInsumos.setSelected(null);
    }

    /**
     * Mueve un insumo de un grupo a otro
     */
    public void moverInsumoAGrupo() {
        try {
            if (nodoPlantillaSeleccionado == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_INSUMO);
                throw b;
            }
            DataInsumoPlantilla dip = (DataInsumoPlantilla) nodoPlantillaSeleccionado.getData();
            if (comboNodosPlantillaDeInsumos.getSelectedT() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_GRUPO_DESTINO);
                throw b;
            }
            if (!dip.getType().equals(DataInsumoPlantilla.TIPO_INSUMO)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_SOLO_PUEDEN_MOVERSE_INSUMOS);
                throw b;
            }

            Insumo insumo = (Insumo) dip.getObjeto();
            plantillaDeInsumosDelegate.moverInsumoAGrupo(insumo.getId(), comboNodosPlantillaDeInsumos.getSelectedT().getId());
            rootPlantilla = createRootNodePlantilla();

            NodoPlantillaDeInsumos insPlantillaSelec = (NodoPlantillaDeInsumos) ((DataInsumoPlantilla) nodoPlantillaSeleccionado.getParent().getData()).getObjeto();
            for (TreeNode nodoPadre : rootPlantilla.getChildren()) {
                NodoPlantillaDeInsumos insPlantilla = (NodoPlantillaDeInsumos) ((DataInsumoPlantilla) nodoPadre.getData()).getObjeto();
                if (insPlantillaSelec.getId().equals(insPlantilla.getId())) {
                    nodoPadre.setExpanded(true);
                    for (NodoPlantillaDeInsumos c : insPlantilla.getHijos()) {
                        agregarCategoria(c, nodoPadre);
                    }
                    for (Insumo i : insPlantilla.getInsumos()) {
                        agregarInsumo(i, nodoPadre);
                    }
                    TreeUtils.sortNode(nodoPadre, ordenadorNodesPlantilla);
                    TreeNode nodoHijoRemover = null;
                    for (TreeNode nodoHijo : nodoPadre.getChildren()) {
                        if (((DataInsumoPlantilla) nodoHijo.getData()).getObjeto() == null) {
                            nodoHijoRemover = nodoHijo;
                        }
                    }
                    if (nodoHijoRemover != null) {
                        nodoPadre.getChildren().remove(nodoHijoRemover);
                    }
                }

            }
            String texto = textMB.obtenerTexto("labels.InsumoMovidoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$(mensajesArbol).scrollTop(0);");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$(mensajesArbol).scrollTop(0);");
        }

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

    public NodoPlantillaDeInsumos getObjeto() {
        return objeto;
    }

    public void setObjeto(NodoPlantillaDeInsumos objeto) {
        this.objeto = objeto;
    }

    public Boolean getNoMoverInsumos() {
        return noMoverInsumos;
    }

    public void setNoMoverInsumos(Boolean noMoverInsumos) {
        this.noMoverInsumos = noMoverInsumos;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getFiltroONURubro() {
        return filtroONURubro;
    }

    public void setFiltroONURubro(String filtroONURubro) {
        this.filtroONURubro = filtroONURubro;
    }

    public String getFiltroONUCodigo() {
        return filtroONUCodigo;
    }

    public void setFiltroONUCodigo(String filtroONUCodigo) {
        this.filtroONUCodigo = filtroONUCodigo;
    }

    public String getFiltroONUnombre() {
        return filtroONUnombre;
    }

    public void setFiltroONUnombre(String filtroONUnombre) {
        this.filtroONUnombre = filtroONUnombre;
    }

    public Boolean getFiltroONUSoloSinPlantillas() {
        return filtroONUSoloSinPlantillas;
    }

    public void setFiltroONUSoloSinPlantillas(Boolean filtroONUSoloSinPlantillas) {
        this.filtroONUSoloSinPlantillas = filtroONUSoloSinPlantillas;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodesONU;
    }

    public TreeNode getRootNode() {
        return rootNodeONU;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNodeONU = rootNode;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodesONU = selectedNodes;
    }

    public TreeNode getRootPlantilla() {
        return rootPlantilla;
    }

    public void setRootPlantilla(TreeNode rootPlantilla) {
        this.rootPlantilla = rootPlantilla;
    }

    public TreeNode[] getSelectedNodesONU() {
        return selectedNodesONU;
    }

    public void setSelectedNodesONU(TreeNode[] selectedNodesONU) {
        this.selectedNodesONU = selectedNodesONU;
    }

    public TreeNode getNodoPlantillaSeleccionado() {
        return nodoPlantillaSeleccionado;
    }

    public void setNodoPlantillaSeleccionado(TreeNode nodoPlantillaSeleccionado) {
        this.nodoPlantillaSeleccionado = nodoPlantillaSeleccionado;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public TreeNode getRootNodeONU() {
        return rootNodeONU;
    }

    public void setRootNodeONU(TreeNode rootNodeONU) {
        this.rootNodeONU = rootNodeONU;
    }

    public SofisComboG<NodoPlantillaDeInsumos> getComboNodosPlantillaDeInsumos() {
        return comboNodosPlantillaDeInsumos;
    }

    public void setComboNodosPlantillaDeInsumos(SofisComboG<NodoPlantillaDeInsumos> comboNodosPlantillaDeInsumos) {
        this.comboNodosPlantillaDeInsumos = comboNodosPlantillaDeInsumos;
    }

    // </editor-fold>
}
