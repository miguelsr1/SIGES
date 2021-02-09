/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgDepartamento;
import sv.gob.mined.siges.web.dto.SgMunicipio;
import sv.gob.mined.siges.web.dto.SgZona;
import sv.gob.mined.siges.web.dto.SgDireccion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DepartamentoRestClient;
import sv.gob.mined.siges.web.restclient.MunicipioRestClient;
import sv.gob.mined.siges.web.restclient.ZonaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class DireccionComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DireccionComponenteBean.class.getName());

    @Inject
    private DepartamentoRestClient restDepartamento;
    
    @Inject
    private MunicipioRestClient restMunicipio;
    
    @Inject
    private ZonaRestClient restZona;

    private SgDireccion direccion = new SgDireccion();
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SofisComboG<SgZona> comboZona;
    
    @PostConstruct
    public void init() {
            cargarCombos();
    }

    public void actualizar(SgDireccion dir) {
        //Si cambia la dirección, actualizo formulario
        if (comboDepartamento == null){
            cargarCombos();
        }
        if (dir != null && !dir.equals(this.direccion)) {
            limpiarCombos();
            direccion = dir;
            if (dir.getDirDepartamento() != null) {
                comboDepartamento.setSelectedT(dir.getDirDepartamento());
                seleccionarDepartamento();
                if (dir.getDirMunicipio() != null) {
                    comboMunicipio.setSelectedT(dir.getDirMunicipio());
                }
            }
            if (dir.getDirZona() != null) {
                comboZona.setSelectedT(dir.getDirZona());
            }
        } else if (dir == null) {
            limpiarCombos();
        }
        direccion = dir;
    }

    public void limpiarCombos() {
        if (comboDepartamento != null){
            comboDepartamento.setSelected(-1);
            comboZona.setSelected(-1);
            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        }
    }

    public void cargarCombos() {
        try {

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> departamentos = restDepartamento.buscar(fc);
            comboDepartamento = new SofisComboG(departamentos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setIncluirCampos(new String[]{"zonNombre", "zonVersion"});
            fc.setOrderBy(new String[]{"zonNombre"});
            List<SgZona> zonas = restZona.buscar(fc);
            comboZona = new SofisComboG(zonas, "zonNombre");
            comboZona.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarDepartamento() {
        try {
            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboDepartamento.getSelectedT() != null) {
                FiltroMunicipio filtro = new FiltroMunicipio();
                filtro.setOrderBy(new String[]{"munNombre"});
                filtro.setAscending(new boolean[]{true});
                filtro.setMunDepartamentoFk(comboDepartamento.getSelectedT().getDepPk());
                
                comboMunicipio = new SofisComboG(restMunicipio.buscar(filtro), "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            direccion.setDirDepartamento(comboDepartamento.getSelectedT());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarMunicipio() {
        direccion.setDirMunicipio(comboMunicipio.getSelectedT());
    }

    public void seleccionarZona() {
        direccion.setDirZona(comboZona.getSelectedT());
    }
    

   

    public SgDireccion getDireccion() {
        return direccion;
    }

    public void setDireccion(SgDireccion direccion) {
        this.direccion = direccion;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }
    
    public SofisComboG<SgZona> getComboZona() {
        return comboZona;
    }

    public void setComboZona(SofisComboG<SgZona> comboZona) {
        this.comboZona = comboZona;
    }

}
