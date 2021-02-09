/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidadAtencion;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ModalidadAtencionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ModalidadAtencionBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long orgCurrId;

    @Inject
    private CatalogosRestClient catalogoClient;

    @Inject
    private ModalidadRestClient modClient;

    @Inject
    private SessionBean sessionBean;

    private List<Object> listaModalidadAtencion = new ArrayList();
    private Boolean[][] matriz;
    private Boolean[][] matriz_aux;
    private int cantidadModalidades;
    private int cantidadModalidadesAt;
    private int actualrow = -1;
    private List<SgModalidadAtencion> listaModalidades;

    private List<SgModalidad> modalidades;

    public ModalidadAtencionBean() {

    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            if (orgCurrId == null || orgCurrId <= 0) {
                redirectToIndex();
            }

            this.actualizar();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_MODALIDAD)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void redirectToIndex() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath());
    }

    public void actualizar() {
        try {
            modalidades = new ArrayList<>();
            listaModalidades = new ArrayList();
            FiltroModalidad filtroModalidad = new FiltroModalidad();
            filtroModalidad.setModHabilitado(Boolean.TRUE);
            filtroModalidad.setOrgCurricularPk(orgCurrId);
            filtroModalidad.setInicializarRel(Boolean.TRUE);
            modalidades = modClient.buscar(filtroModalidad);

            FiltroModalidadAtencion fc = new FiltroModalidadAtencion();
            fc.setInicializarSubModalidades(Boolean.TRUE);
            fc.setHabilitado(Boolean.TRUE);
            listaModalidadAtencion = new ArrayList<>();
            for (SgModalidadAtencion m : catalogoClient.buscarModalidadAtencion(fc)) {
                if (m.getMatSubmodalidades() != null && !m.getMatSubmodalidades().isEmpty()) {
                    listaModalidades.add(m);
                    for (SgSubModalidadAtencion sm : m.getMatSubmodalidades()) {
                        listaModalidadAtencion.add(sm);
                    }
                } else {
                    listaModalidadAtencion.add(m);
                    if (m.getMatSubmodalidades() == null) {
                        m.setMatSubmodalidades(new ArrayList());
                    }
                    listaModalidades.add(m);
                }

            }

            cantidadModalidades = modalidades.size();

            matriz = new Boolean[cantidadModalidades][listaModalidadAtencion.size()];
            matriz_aux = new Boolean[cantidadModalidades][listaModalidadAtencion.size()];
            for (Boolean[] row : matriz) {
                Arrays.fill(row, false);
            }
            for (Boolean[] row : matriz_aux) {
                Arrays.fill(row, false);
            }

            int i = 0;
            for (SgModalidad m : modalidades) {
                for (SgRelModEdModAten rel : m.getModRelModalidadAtencion()) {
                    if (rel.getReaSubModalidadAtencion() != null) {
                        matriz[i][listaModalidadAtencion.indexOf(rel.getReaSubModalidadAtencion())] = true;
                        matriz_aux[i][listaModalidadAtencion.indexOf(rel.getReaSubModalidadAtencion())] = true;
                    } else if (rel.getReaModalidadAtencion() != null) {
                        matriz[i][listaModalidadAtencion.indexOf(rel.getReaModalidadAtencion())] = true;
                        matriz_aux[i][listaModalidadAtencion.indexOf(rel.getReaModalidadAtencion())] = true;
                    }
                }
                i++;
            }
            cantidadModalidadesAt = listaModalidadAtencion.size();

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() {
        try {
            for (int i = 0; i < cantidadModalidades; i++) {

                SgModalidad m = modalidades.get(i);
                for (int j = 0; j < cantidadModalidadesAt; j++) {

                    if ((matriz[i][j] == true) && (matriz_aux[i][j] == false)) {
                        //Agregar relación
                        SgRelModEdModAten rel = new SgRelModEdModAten();
                        rel.setReaModalidadEducativa(m);
                        if (listaModalidadAtencion.get(j) instanceof SgModalidadAtencion) {
                            SgModalidadAtencion ma = (SgModalidadAtencion) listaModalidadAtencion.get(j);
                            rel.setReaModalidadAtencion(ma);
                        }
                        if (listaModalidadAtencion.get(j) instanceof SgSubModalidadAtencion) {
                            SgSubModalidadAtencion sm = (SgSubModalidadAtencion) listaModalidadAtencion.get(j);
                            rel.setReaSubModalidadAtencion(sm);
                            rel.setReaModalidadAtencion(sm.getSmoModalidadFk());
                        }
                        m.getModRelModalidadAtencion().add(rel);

                        matriz_aux[i][j] = true;
                    }
                    if ((matriz[i][j] == false) && (matriz_aux[i][j] == true)) {
                        //Eliminar relación
                        SgModalidadAtencion ma;
                        SgSubModalidadAtencion sm;
                        if (listaModalidadAtencion.get(j) instanceof SgModalidadAtencion) {
                            ma = (SgModalidadAtencion) listaModalidadAtencion.get(j);

                            for (SgRelModEdModAten rel_aux : m.getModRelModalidadAtencion()) {
                                if ((rel_aux.getReaModalidadEducativa().getModPk().equals(m.getModPk()))
                                        && (rel_aux.getReaModalidadAtencion().getMatPk().equals(ma.getMatPk()))) {
                                    m.getModRelModalidadAtencion().remove(rel_aux);
                                    break;
                                }
                            }
                        }
                        if (listaModalidadAtencion.get(j) instanceof SgSubModalidadAtencion) {
                            sm = (SgSubModalidadAtencion) listaModalidadAtencion.get(j);
                            ma = sm.getSmoModalidadFk();

                            for (SgRelModEdModAten rel_aux : m.getModRelModalidadAtencion()) {
                                if ((rel_aux.getReaModalidadEducativa().getModPk().equals(m.getModPk()))
                                        && (rel_aux.getReaModalidadAtencion().getMatPk().equals(ma.getMatPk()))) {
                                    if (rel_aux.getReaSubModalidadAtencion().getSmoPk().equals(sm.getSmoPk())) {
                                        m.getModRelModalidadAtencion().remove(rel_aux);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                this.modClient.guardar(m); // No es necesario que el guardado de todas las modalidades sea transaccional.
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        this.actualizar();
    }

    public Long getOrgCurrId() {
        return orgCurrId;
    }

    public void setOrgCurrId(Long orgCurrId) {
        this.orgCurrId = orgCurrId;
    }

    public int getActualrow() {
        return actualrow;
    }

    public void setActualrow(int actualrow) {
        this.actualrow = actualrow;
    }

    public CatalogosRestClient getCatalogoClient() {
        return catalogoClient;
    }

    public void setCatalogoClient(CatalogosRestClient catalogoClient) {
        this.catalogoClient = catalogoClient;
    }

    public List<Object> getListaModalidadAtencion() {
        return listaModalidadAtencion;
    }

    public void setListaModalidadAtencion(List<Object> listaModalidadAtencion) {
        this.listaModalidadAtencion = listaModalidadAtencion;
    }

    public Boolean[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(Boolean[][] matriz) {
        this.matriz = matriz;
    }

    public List<SgModalidad> getModalidades() {
        return modalidades;
    }

    public void setModalidades(List<SgModalidad> modalidades) {
        this.modalidades = modalidades;
    }

    public int getCantidadModalidades() {
        return cantidadModalidades;
    }

    public void setCantidadModalidades(int cantidadModalidades) {
        this.cantidadModalidades = cantidadModalidades;
    }

    public int getCantidadModalidadesAt() {
        return cantidadModalidadesAt;
    }

    public void setCantidadModalidadesAt(int cantidadModalidadesAt) {
        this.cantidadModalidadesAt = cantidadModalidadesAt;
    }

    public Boolean[][] getMatriz_aux() {
        return matriz_aux;
    }

    public void setMatriz_aux(Boolean[][] matriz_aux) {
        this.matriz_aux = matriz_aux;
    }

    public List<Integer> getValues() {
        List<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < cantidadModalidadesAt; i++) {
            values.add(i);
        }
        return values;
    }

    public List<SgModalidadAtencion> getListaModalidades() {
        return listaModalidades;
    }

    public void setListaModalidades(List<SgModalidadAtencion> listaModalidades) {
        this.listaModalidades = listaModalidades;
    }

    public String obtenerNombre(int i) {
        if (listaModalidadAtencion.get(i) instanceof SgModalidadAtencion) {
            SgModalidadAtencion ma = (SgModalidadAtencion) listaModalidadAtencion.get(i);
            return ma.getMatNombre();
        } else {
            SgSubModalidadAtencion sm = (SgSubModalidadAtencion) listaModalidadAtencion.get(i);
            return sm.getSmoNombre();
        }
    }

}
