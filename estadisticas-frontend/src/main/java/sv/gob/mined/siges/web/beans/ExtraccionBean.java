/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgAlcanceExtraccion;
import sv.gob.mined.siges.web.dto.SgAlcanceTreeNode;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgDuplicarExtraccion;
import sv.gob.mined.siges.web.dto.SgExtraccion;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgGradoReportaEn;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgOrganizacionCurricular;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.catalogo.SgEstDatasets;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ExtraccionRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ExtraccionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ExtraccionBean.class.getName());

    @Inject
    private ExtraccionRestClient restClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private GradoRestClient gradoClient;

    @Inject
    private SeleccionarGradoBean gradoBean;

    @Inject
    @Param(name = "id")
    private Long extId;

    private SgExtraccion entidadEnEdicion = new SgExtraccion();
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<SgEstDatasets> comboDatasets;
    private SofisComboG<SgEstNombreExtraccion> comboNombresExtr;

    private List<SgGrado> grados;

    private TreeNode orgTree;
    private TreeNode[] selectedNodes;
    private List<TreeNode> gradoNodes;
    private SgAlcanceTreeNode alcanceEdicion;
    private SgGrado gradoSeleccionado;
    
    private SgDuplicarExtraccion duplicarExtraccion = new SgDuplicarExtraccion();
    private SofisComboG<SgEstNombreExtraccion> comboNombresExtrDuplicar;

    public ExtraccionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            if (extId != null && extId > 0) {
                actualizar(restClient.obtenerPorId(extId));
            } else {
                agregar();
            }
            gradoBean.cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_EXTRACCIONES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"datNombre"});
            fc.setIncluirCampos(new String[]{"datNombre", "datCodigo", "datVersion"});
            comboDatasets = new SofisComboG<>(catalogosClient.buscarDatasets(fc), "datNombre");
            comboDatasets.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"nexNombre"});
            fc.setIncluirCampos(new String[]{"nexNombre", "nexVersion"});
            
            List<SgEstNombreExtraccion> nombres = catalogosClient.buscarNombresExtraccion(fc);
            
            comboNombresExtr = new SofisComboG<>(nombres, "nexNombre");
            comboNombresExtr.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboNombresExtrDuplicar = new SofisComboG<>(nombres, "nexNombre");
            comboNombresExtrDuplicar.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void inicializarDuplicarExtraccion(){
        this.duplicarExtraccion = new SgDuplicarExtraccion();
    }

    public void cargarArbol() {
        try {

            if (orgTree != null) {
                return;
            }

            if (!this.comboDatasets.getSelectedT().getDatCodigo().equals(Constantes.DATASET_ESTUDIANTES_CODIGO)) {
                return;
            }

            this.gradoNodes = new ArrayList<>();

            FiltroGrado fil = new FiltroGrado();
            fil.setHabilitado(Boolean.TRUE);

            fil.setIncluirCampos(new String[]{"graNombre", "graPk",
                "graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrganizacionCurricular.ocuPk",
                "graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrganizacionCurricular.ocuNombre",
                "graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk",
                "graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                "graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk",
                "graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                "graRelacionModalidad.reaModalidadEducativa.modPk",
                "graRelacionModalidad.reaModalidadEducativa.modNombre",
                "graRelacionModalidad.reaModalidadAtencion.matPk",
                "graRelacionModalidad.reaModalidadAtencion.matNombre",
                "graRelacionModalidad.reaSubModalidadAtencion.smoPk",
                "graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                "graRelacionModalidad.reaPk"
            });

            grados = gradoClient.buscarConCache(fil);

            HashMap<SgOrganizacionCurricular, Set<SgNivel>> organizaciones = new HashMap<>();
            HashMap<SgNivel, Set<SgCiclo>> niveles = new HashMap<>();
            HashMap<SgCiclo, Set<SgModalidad>> ciclos = new HashMap<>();
            HashMap<SgModalidad, Set<SgRelModEdModAten>> relModalidades = new HashMap<>();
            HashMap<SgRelModEdModAten, Set<SgGrado>> relGrados = new HashMap<>();

            for (SgGrado g : grados) {

                SgOrganizacionCurricular org = g.getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivOrganizacionCurricular();
                SgNivel nivel = g.getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel();
                SgCiclo ciclo = g.getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo();
                SgModalidad modalidad = g.getGraRelacionModalidad().getReaModalidadEducativa();
                SgRelModEdModAten rel = g.getGraRelacionModalidad();

                organizaciones.computeIfAbsent(org, s -> new HashSet<>()).add(nivel);
                niveles.computeIfAbsent(nivel, s -> new HashSet<>()).add(ciclo);
                ciclos.computeIfAbsent(ciclo, s -> new HashSet<>()).add(modalidad);
                relModalidades.computeIfAbsent(modalidad, s -> new HashSet<>()).add(rel);
                relGrados.computeIfAbsent(rel, s -> new HashSet<>()).add(g);

            }

            TreeNode raiz = new DefaultTreeNode("raiz");

            for (SgOrganizacionCurricular o : organizaciones.keySet()) {

                SgAlcanceTreeNode alcanceNode = new SgAlcanceTreeNode();
                alcanceNode.setType("org");
                alcanceNode.setObjeto(o);

                TreeNode orgnode = new DefaultTreeNode("org", alcanceNode, raiz);
                orgnode.setSelected(false);

                for (SgNivel niv : organizaciones.get(o)) {

                    alcanceNode = new SgAlcanceTreeNode();
                    alcanceNode.setType("nivel");
                    alcanceNode.setObjeto(niv);

                    TreeNode nivnode = new DefaultTreeNode("nivel", alcanceNode, orgnode);
                    nivnode.setSelected(false);

                    for (SgCiclo cic : niveles.get(niv)) {

                        alcanceNode = new SgAlcanceTreeNode();
                        alcanceNode.setType("ciclo");
                        alcanceNode.setObjeto(cic);

                        TreeNode cicnode = new DefaultTreeNode("ciclo", alcanceNode, nivnode);
                        cicnode.setSelected(false);

                        for (SgModalidad mod : ciclos.get(cic)) {

                            alcanceNode = new SgAlcanceTreeNode();
                            alcanceNode.setType("modalidad");
                            alcanceNode.setObjeto(mod);

                            TreeNode modnode = new DefaultTreeNode("modalidad", alcanceNode, cicnode);
                            modnode.setSelected(false);

                            Set<SgRelModEdModAten> relacionesModalidad = relModalidades.get(mod);

                            List<SgModalidadAtencion> listaMA = new ArrayList();
                            for (SgRelModEdModAten rel : relacionesModalidad) {
                                if (!listaMA.contains(rel.getReaModalidadAtencion())) {
                                    listaMA.add(rel.getReaModalidadAtencion());
                                }
                            }

                            for (SgModalidadAtencion modaten : listaMA) {
                                alcanceNode = new SgAlcanceTreeNode();
                                alcanceNode.setType("modalidad_atencion");
                                alcanceNode.setObjeto(modaten);

                                TreeNode modAten = new DefaultTreeNode("modalidad_atencion", alcanceNode, modnode);
                                modAten.setSelected(false);

                                List<SgSubModalidadAtencion> listaSubMod = new ArrayList<>();

                                for (SgRelModEdModAten relAux : relacionesModalidad) {
                                    if (relAux.getReaSubModalidadAtencion() != null && modaten.equals(relAux.getReaModalidadAtencion())) {
                                        listaSubMod.add(relAux.getReaSubModalidadAtencion());
                                    }
                                }
                                if (!listaSubMod.isEmpty()) {

                                    for (SgSubModalidadAtencion submod : listaSubMod) {

                                        alcanceNode = new SgAlcanceTreeNode();
                                        alcanceNode.setType("submodalidad_atencion");
                                        alcanceNode.setObjeto(submod);

                                        TreeNode submodnode = new DefaultTreeNode("submodalidad_atencion", alcanceNode, modAten);
                                        submodnode.setSelected(false);

                                        List<SgGrado> listaGrados = new ArrayList<>();

                                        for (SgRelModEdModAten relAux : relacionesModalidad) {
                                            if (modaten.equals(relAux.getReaModalidadAtencion()) && submod.equals(relAux.getReaSubModalidadAtencion())) {
                                                listaGrados.addAll(relGrados.get(relAux));
                                            }
                                        }

                                        for (SgGrado gra : listaGrados) {

                                            alcanceNode = new SgAlcanceTreeNode();
                                            alcanceNode.setType("grado");
                                            alcanceNode.setObjeto(gra);

                                            TreeNode n = new DefaultTreeNode("grado", alcanceNode, submodnode);
                                            n.setSelected(false);

                                            gradoNodes.add(n);
                                        }

                                    }

                                } else {

                                    List<SgGrado> listaGrados = new ArrayList<>();

                                    for (SgRelModEdModAten relAux : relacionesModalidad) {
                                        if (modaten.equals(relAux.getReaModalidadAtencion())) {
                                            listaGrados.addAll(relGrados.get(relAux));
                                        }
                                    }

                                    for (SgGrado gra : listaGrados) {

                                        alcanceNode = new SgAlcanceTreeNode();
                                        alcanceNode.setType("grado");
                                        alcanceNode.setObjeto(gra);

                                        TreeNode n = new DefaultTreeNode("grado", alcanceNode, modAten);
                                        n.setSelected(false);

                                        gradoNodes.add(n);
                                    }

                                }

                            }

                        }

                    }

                }

            }

            orgTree = raiz;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void actualizarAlcanceEdicion(SgAlcanceTreeNode node) {
        try {
            this.alcanceEdicion = node;
            this.gradoSeleccionado = node.getReportaEnGrado();
            gradoBean.seleccionarGrado(gradoSeleccionado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarGrado(SgGrado var) {
        if (var == null) {
            this.gradoSeleccionado = null;
        } else {
            Optional<SgGrado> op = grados.stream().filter(g -> g.getGraPk().equals(var.getGraPk())).findFirst();
            this.gradoSeleccionado = op.orElse(var);
        }
    }

    public void aceptarGrado() {
        this.alcanceEdicion.setReportaEnGrado(this.gradoSeleccionado);
        PrimeFaces.current().executeScript("PF('itemDialog').hide()");
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgExtraccion();
    }

    public void actualizar(SgExtraccion var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = var;
        this.comboDatasets.setSelectedT(var.getExtDataset());
        this.comboNombresExtr.setSelectedT(var.getExtNombre());

        cargarArbol();

        TreeNode rec;

        if (var.getExtAlcance() != null) {

            for (SgAlcanceExtraccion alcance : var.getExtAlcance()) {

                rec = find(this.gradoNodes, alcance.getAlcGradoPk());

                if (rec != null) {
                    SgAlcanceTreeNode node = (SgAlcanceTreeNode) rec.getData();
                    node.setFechaMatricula(alcance.getAlcFechaMatriculas());
                    rec.setSelected(true);

                    TreeNode parent = rec.getParent();
                    while (parent != null) {
                        parent.setExpanded(true);
                        parent = parent.getParent();
                    }
                }
            }

        }

        if (var.getExtGradoReportaEn() != null) {

            for (SgGradoReportaEn reportaEn : var.getExtGradoReportaEn()) {

                TreeNode n = find(this.gradoNodes, reportaEn.getRepGradoOrigen());
                if (n != null) {
                    SgAlcanceTreeNode node = (SgAlcanceTreeNode) n.getData();

                    Optional<SgGrado> op = grados.stream().filter(g -> g.getGraPk().equals(reportaEn.getRepGradoDestino())).findFirst();
                    node.setReportaEnGrado(op.orElse(new SgGrado(reportaEn.getRepGradoDestino())));

                    TreeNode parent = n.getParent();
                    while (parent != null) {
                        parent.setExpanded(true);
                        parent = parent.getParent();
                    }

                }

            }

        }

    }

    private TreeNode find(List<TreeNode> nodes, Long pk) {
        for (TreeNode t : nodes) {
            SgAlcanceTreeNode node = (SgAlcanceTreeNode) t.getData();
            if (node.getPk().equals(pk)) {
                return t;
            }
        }
        return null;
    }

    public void duplicar() {
        try {
            duplicarExtraccion.setNuevoNombre(this.comboNombresExtrDuplicar.getSelectedT());
            duplicarExtraccion.setExtPk(this.entidadEnEdicion.getExtPk());
            SgExtraccion guardada = restClient.duplicarExtraccionParaOtroAnio(this.duplicarExtraccion);
            this.actualizar(guardada);
            PrimeFaces.current().ajax().update("form");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {

            if (this.comboDatasets.getSelectedT().getDatCodigo().equals(Constantes.DATASET_ESTUDIANTES_CODIGO)) {

                List<SgAlcanceExtraccion> alcances = new ArrayList<>();

                forSelectedNodes:
                for (TreeNode selected : selectedNodes) {

                    SgAlcanceExtraccion alcance = new SgAlcanceExtraccion();
                    SgAlcanceTreeNode node = (SgAlcanceTreeNode) selected.getData();

                    //Obtener fecha ingresada
                    TreeNode rec = selected;
                    SgAlcanceTreeNode recNode = (SgAlcanceTreeNode) rec.getData();
                    LocalDate fechaSuperior = recNode.getFechaMatricula();
                    while (rec.getParent() != null && rec.getParent().getParent() != null) {
                        rec = rec.getParent();
                        recNode = (SgAlcanceTreeNode) rec.getData();

                        if (recNode.getFechaMatricula() != null) {
                            fechaSuperior = recNode.getFechaMatricula();
                        }
                    }
                    alcance.setAlcFechaMatriculas(fechaSuperior);

                    if (node.getType().equals("grado")) {
                        SgGrado gra = (SgGrado) node.getObjeto();
                        alcance.setAlcGradoPk(gra.getGraPk());
                        alcance.setAlcModalidadAtencionPk(gra.getGraRelacionModalidad().getReaModalidadAtencion().getMatPk());
                        alcance.setAlcSubModalidadAtencionPk(gra.getGraRelacionModalidad().getReaSubModalidadAtencion() != null ? gra.getGraRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                        alcance.setAlcModalidadPk(gra.getGraRelacionModalidad().getReaModalidadEducativa().getModPk());
                        alcance.setAlcCicloPk(gra.getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicPk());
                        alcance.setAlcNivelPk(gra.getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk());
                        alcance.setAlcOrganizacionPk(gra.getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivOrganizacionCurricular().getOcuPk());
                    } else {
                        continue;
                    }

                    alcances.add(alcance);

                }

                List<SgGradoReportaEn> reportaEnList = new ArrayList<>();
                for (TreeNode gradoNode : gradoNodes) {

                    SgAlcanceTreeNode node = (SgAlcanceTreeNode) gradoNode.getData();

                    SgGrado gradoDestino = node.getReportaEnGrado();

                    if (gradoDestino != null) {
                        SgGradoReportaEn reportaEn = new SgGradoReportaEn();
                        SgGrado origen = (SgGrado) node.getObjeto();

                        reportaEn.setRepGradoOrigen(origen.getGraPk());
                        reportaEn.setRepGradoDestino(gradoDestino.getGraPk());

                        reportaEnList.add(reportaEn);
                    }

                }

                entidadEnEdicion.setExtAlcance(alcances);
                entidadEnEdicion.setExtGradoReportaEn(reportaEnList);
            }

            entidadEnEdicion.setExtDataset(this.comboDatasets.getSelectedT());
            entidadEnEdicion.setExtNombre(this.comboNombresExtr.getSelectedT());
            SgExtraccion guardada = restClient.guardar(entidadEnEdicion);
            this.actualizar(guardada);
            PrimeFaces.current().ajax().update("form");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public SgExtraccion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgExtraccion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public ExtraccionRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ExtraccionRestClient restClient) {
        this.restClient = restClient;
    }

    public SofisComboG<SgEstDatasets> getComboDatasets() {
        return comboDatasets;
    }

    public void setComboDatasets(SofisComboG<SgEstDatasets> comboDatasets) {
        this.comboDatasets = comboDatasets;
    }

    public SofisComboG<SgEstNombreExtraccion> getComboNombresExtr() {
        return comboNombresExtr;
    }

    public void setComboNombresExtr(SofisComboG<SgEstNombreExtraccion> comboNombresExtr) {
        this.comboNombresExtr = comboNombresExtr;
    }

    public TreeNode getOrgTree() {
        return orgTree;
    }

    public void setOrgTree(TreeNode orgTree) {
        this.orgTree = orgTree;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public SgAlcanceTreeNode getAlcanceEdicion() {
        return alcanceEdicion;
    }

    public void setAlcanceEdicion(SgAlcanceTreeNode alcanceEdicion) {
        this.alcanceEdicion = alcanceEdicion;
    }

    public SgGrado getGradoSeleccionado() {
        return gradoSeleccionado;
    }

    public void setGradoSeleccionado(SgGrado gradoSeleccionado) {
        this.gradoSeleccionado = gradoSeleccionado;
    }

    public List<TreeNode> getGradoNodes() {
        return gradoNodes;
    }

    public void setGradoNodes(List<TreeNode> gradoNodes) {
        this.gradoNodes = gradoNodes;
    }

    public SgDuplicarExtraccion getDuplicarExtraccion() {
        return duplicarExtraccion;
    }

    public void setDuplicarExtraccion(SgDuplicarExtraccion duplicarExtraccion) {
        this.duplicarExtraccion = duplicarExtraccion;
    }

    public SofisComboG<SgEstNombreExtraccion> getComboNombresExtrDuplicar() {
        return comboNombresExtrDuplicar;
    }

    public void setComboNombresExtrDuplicar(SofisComboG<SgEstNombreExtraccion> comboNombresExtrDuplicar) {
        this.comboNombresExtrDuplicar = comboNombresExtrDuplicar;
    }
    
    

    
    
}
