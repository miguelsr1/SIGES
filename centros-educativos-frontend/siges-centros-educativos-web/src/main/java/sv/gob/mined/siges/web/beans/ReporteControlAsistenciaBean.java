/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.enumerados.EnumMes;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ReporteControlAsistenciaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ReporteControlAsistenciaBean.class.getName());

    @Inject
    private SeccionRestClient restSeccion;

    @Inject
    @Param(name = "seccionId")
    private Long seccionId;

    @Inject
    private SessionBean sessionBean;

    private SgSeccion entidadEnEdicion;
    private Integer mesSeleccionado;
    private SofisComboG<EnumMes> comboMeses;

    public ReporteControlAsistenciaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            if (seccionId != null) {
                entidadEnEdicion = restSeccion.obtenerPorId(seccionId);
                comboMeses = new SofisComboG(Arrays.asList(EnumMes.values()), "text");
                comboMeses.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            } else {
                JSFUtils.redirectToIndex();
            }
            validarAcceso();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_ASISTENCIAS_ESTUDIANTE)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String getTituloPagina() {
        return Etiquetas.getValue("reporteAsistencias");
    }

    public void mesSelected() {
        try {
            if (this.comboMeses.getSelectedT() != null) {
                mesSeleccionado = this.comboMeses.getSelectedT().getNumero();
            } else {
                mesSeleccionado = null;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgSeccion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSeccion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Integer getMesSeleccionado() {
        return mesSeleccionado;
    }

    public void setMesSeleccionado(Integer mesSeleccionado) {
        this.mesSeleccionado = mesSeleccionado;
    }

    public SofisComboG<EnumMes> getComboMeses() {
        return comboMeses;
    }

    public void setComboMeses(SofisComboG<EnumMes> comboMeses) {
        this.comboMeses = comboMeses;
    }

}
