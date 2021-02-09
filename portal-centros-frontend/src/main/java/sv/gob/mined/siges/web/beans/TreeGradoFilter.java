/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgOrganizacionCurricular;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOrganizacionCurricular;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.OrganizacionCurricularRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;

@Named
@ViewScoped
public class TreeGradoFilter implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TreeGradoFilter.class.getName());

    @Inject
    private SedeRestClient restClient;

    @Inject
    private OrganizacionCurricularRestClient organizacionCurricularClient;

    @Inject
    private NivelRestClient nivelClient;

    @Inject
    private CicloRestClient cicloClient;

    @Inject
    private ModalidadRestClient modalidadClient;

    @Inject
    private RelModEdModAtenRestClient relModRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ApplicationBean applicationBean;
    ;

    @Inject
    private GradoRestClient gradoClient;

    @Inject
    private SedeRestClient restSede;

    private Boolean soloLectura = Boolean.FALSE;
    private Long sedePk;

    private TreeNode filtrosRoot;
    private TreeNode selectedNode;
    private List<SgRelModEdModAten> relModEdModAtenSelected;

    public TreeGradoFilter() {
    }

    @PostConstruct
    public void init() {
    }

    public void cargarOrganizacionesCurriculares() {
        try {

            FiltroOrganizacionCurricular fc = new FiltroOrganizacionCurricular();
            fc.setSedePk(sedePk);
            fc.setHabilitado(Boolean.TRUE);
            fc.setOrderBy(new String[]{"ocuNombre"});
            fc.setAscending(new boolean[]{true});
            fc.setIncluirCampos(new String[]{"ocuNombre", "ocuVersion"});
            List<SgOrganizacionCurricular> organizaciones = organizacionCurricularClient.buscarConCache(fc);

            filtrosRoot = new DefaultTreeNode("Root", null);

            for (SgOrganizacionCurricular o : organizaciones) {
                TreeNode n = new DefaultTreeNode("org", o, filtrosRoot);
                n.setSelectable(false);
                n.getChildren().add(new DefaultTreeNode("Dummy"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void onNodeExpand(NodeExpandEvent event) {
        try {
            TreeNode node = event.getTreeNode();
            if (node.getType().equals("org")) {
                node.getChildren().clear();
                SgOrganizacionCurricular org = (SgOrganizacionCurricular) node.getData();
                FiltroNivel fNivel = new FiltroNivel();
                fNivel.setOrderBy(new String[]{"nivOrden"});
                fNivel.setAscending(new boolean[]{true});
                fNivel.setIncluirCampos(new String[]{"nivNombre", "nivVersion"});
                fNivel.setSedPk(sedePk);
                fNivel.setOrganizacionCurricularPk(org.getOcuPk());
                List<SgNivel> niveles = nivelClient.buscarConCache(fNivel);

                for (SgNivel nivel : niveles) {
                    TreeNode n = new DefaultTreeNode("nivel", nivel, node);
                    n.setSelectable(false);
                    n.getChildren().add(new DefaultTreeNode("Dummy"));
                }
            }
            if (node.getType().equals("nivel")) {

                node.getChildren().clear();
                SgNivel niv = (SgNivel) node.getData();

                FiltroCiclo fCiclo = new FiltroCiclo();
                fCiclo.setAscending(new boolean[]{true});
                fCiclo.setOrderBy(new String[]{"cicNombre"});
                fCiclo.setIncluirCampos(new String[]{"cicNombre", "cicVersion"});
                fCiclo.setCicHabilitado(Boolean.TRUE);
                fCiclo.setSedePk(sedePk);
                fCiclo.setNivPk(niv.getNivPk());
                List<SgCiclo> ciclos = cicloClient.buscarConCache(fCiclo);

                for (SgCiclo ciclo : ciclos) {
                    TreeNode n = new DefaultTreeNode("ciclo", ciclo, node);
                    n.setSelectable(false);
                    n.getChildren().add(new DefaultTreeNode("Dummy"));
                }
            }
            if (node.getType().equals("ciclo")) {

                node.getChildren().clear();
                SgCiclo cic = (SgCiclo) node.getData();

                FiltroModalidad fModalidad = new FiltroModalidad();
                fModalidad.setCicPk(cic.getCicPk());
                fModalidad.setSedePk(sedePk);
                fModalidad.setOrderBy(new String[]{"modNombre"});
                fModalidad.setAscending(new boolean[]{true});
                fModalidad.setIncluirCampos(new String[]{"modNombre", "modVersion"});
                List<SgModalidad> modalidades = modalidadClient.buscarConCache(fModalidad);

                for (SgModalidad modalidad : modalidades) {
                    TreeNode n = new DefaultTreeNode("modalidad", modalidad, node);
                    n.setSelectable(false);
                    n.getChildren().add(new DefaultTreeNode("Dummy"));
                }
            }
            if (node.getType().equals("modalidad")) {

                node.getChildren().clear();
                SgModalidad mod = (SgModalidad) node.getData();

                FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
                filtroRel.setReaModalidadEducativa(mod.getModPk());
                filtroRel.setSedePk(sedePk);
                List<SgModalidadAtencion> listaMA = new ArrayList();
                relModEdModAtenSelected = relModRestClient.buscarConCache(filtroRel);
                for (SgRelModEdModAten rel : relModEdModAtenSelected) {
                    if (!listaMA.contains(rel.getReaModalidadAtencion())) {
                        listaMA.add(rel.getReaModalidadAtencion());
                    }
                }

                for (SgModalidadAtencion modaten : listaMA) {
                    TreeNode n = new DefaultTreeNode("modalidad_atencion", modaten, node);
                    n.setSelectable(false);
                    n.getChildren().add(new DefaultTreeNode("Dummy"));
                }
            }
            if (node.getType().equals("modalidad_atencion")) {

                node.getChildren().clear();
                SgModalidadAtencion modAtencionSelect = (SgModalidadAtencion) node.getData();
                SgModalidad modalidadSelect = (SgModalidad) node.getParent().getData();

                //Verificar si esta modalidad de atención tiene submodalidades
                List<SgSubModalidadAtencion> listaSubMod = new ArrayList<>();

                for (SgRelModEdModAten relAux : relModEdModAtenSelected) {
                    if (relAux.getReaSubModalidadAtencion() != null && modAtencionSelect.equals(relAux.getReaModalidadAtencion()) && BooleanUtils.isTrue(relAux.getReaSubModalidadAtencion().getSmoHabilitado())) {
                        listaSubMod.add(relAux.getReaSubModalidadAtencion());
                    }
                }
                if (!listaSubMod.isEmpty()) {

                    for (SgSubModalidadAtencion modaten : listaSubMod) {
                        TreeNode n = new DefaultTreeNode("submodalidad_atencion", modaten, node);
                        n.setSelectable(false);
                        n.getChildren().add(new DefaultTreeNode("Dummy"));
                    }

                } else {

                    FiltroGrado fGrado = new FiltroGrado();
                    fGrado.setModAtencionPk(modAtencionSelect.getMatPk());
                    fGrado.setModPk(modalidadSelect.getModPk());
                    fGrado.setSedePk(sedePk);
                    fGrado.setHabilitado(Boolean.TRUE);
                    fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
                    fGrado.setOrderBy(new String[]{"graOrden"});
                    fGrado.setAscending(new boolean[]{true});
                    List<SgGrado> grados = gradoClient.buscarConCache(fGrado);

                    for (SgGrado gra : grados) {
                        TreeNode n = new DefaultTreeNode("grado", gra, node);
                    }

                }

            }
            if (node.getType().equals("submodalidad_atencion")) {

                node.getChildren().clear();
                SgSubModalidadAtencion submodAtencionSelect = (SgSubModalidadAtencion) node.getData();
                SgModalidad modalidadSelect = (SgModalidad) node.getParent().getParent().getData();

                FiltroGrado fGrado = new FiltroGrado();
                fGrado.setSubModAtencionPk(submodAtencionSelect.getSmoPk());
                fGrado.setModPk(modalidadSelect.getModPk());
                fGrado.setOrderBy(new String[]{"graOrden"});
                fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
                List<SgGrado> grados = gradoClient.buscarConCache(fGrado);

                for (SgGrado gra : grados) {
                    TreeNode n = new DefaultTreeNode("grado", gra, node);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getNodeFullLabel(TreeNode node) {
        if (node.getParent() != null && node.getParent().getParent() != null) { //Recordar root node
            return getNodeFullLabel(node.getParent()) + " > " + getNodeLabel(node);
        } else {
            return getNodeLabel(node);
        }
    }

    public String getNodeLabel(TreeNode node) {
        if (node != null) {
            if (node.getType().equals("org")) {
                SgOrganizacionCurricular org = (SgOrganizacionCurricular) node.getData();
                return org.getOcuNombre();
            }
            if (node.getType().equals("nivel")) {
                SgNivel niv = (SgNivel) node.getData();
                return niv.getNivNombre();
            }
            if (node.getType().equals("ciclo")) {
                SgCiclo cic = (SgCiclo) node.getData();
                return cic.getCicNombre();
            }
            if (node.getType().equals("modalidad")) {
                SgModalidad mod = (SgModalidad) node.getData();
                return mod.getModNombre();
            }
            if (node.getType().equals("modalidad_atencion")) {
                SgModalidadAtencion modAten = (SgModalidadAtencion) node.getData();
                return modAten.getMatNombre();
            }
            if (node.getType().equals("submodalidad_atencion")) {
                SgSubModalidadAtencion submodAten = (SgSubModalidadAtencion) node.getData();
                return submodAten.getSmoNombre();
            }
            if (node.getType().equals("grado")) {
                SgGrado grado = (SgGrado) node.getData();
                return grado.getGraNombre();
            }
        }
        return "";
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        limpiarCombos();
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SedeRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(SedeRestClient restClient) {
        this.restClient = restClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public TreeNode getFiltrosRoot() {
        return filtrosRoot;
    }

    public void setFiltrosRoot(TreeNode filtrosRoot) {
        this.filtrosRoot = filtrosRoot;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    
}
