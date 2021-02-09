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
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class ConsultaSeccionCierreAnioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConsultaSeccionCierreAnioBean.class.getName());

    @Inject
    private SeccionRestClient seccionClient;

    @Inject
    private SessionBean sessionBean;

    // mapeo los elementos seleccionados en los combos del componente para generar el reporte
    @Inject
    private FiltrosSeccionBean filtrosSeccionBean;

    private SgSeccion entidadEnEdicion;
    private FiltroSeccion filtroSeccion= new FiltroSeccion();
    private List<SgSeccion> listSeccion;

    public ConsultaSeccionCierreAnioBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
           
            inicializarFiltrosSeccion();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

  

    public void inicializarFiltrosSeccion() {
        this.filtrosSeccionBean.setFiltro(filtroSeccion);
        this.filtrosSeccionBean.cargarCombos();
        this.filtrosSeccionBean.seleccionarUltimoAnio();
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONSULTA_SECCION_CIERRE_ANIO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String getTituloPagina() {
        return Etiquetas.getValue("consultaCierreAnioSecciones");
    }

    public void generar() {
        try {    
            filtroSeccion.setIncluirCampos(new String[]{"secPk", "secNombre", "secJornadaLaboral.jlaNombre", "secServicioEducativo.sduGrado.graNombre", "secEstado"});
            filtroSeccion.setOrderBy(new String[]{"secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrden", 
                                                    "secServicioEducativo.sduGrado.graOrden",
                                                    "secCodigo"});
            filtroSeccion.setAscending(new boolean[]{true, true, true});
            listSeccion= seccionClient.buscar(filtroSeccion);

            if (listSeccion.isEmpty()) {
                LOGGER.log(Level.INFO, "no hay secciones");
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.WARNING_NO_HAY_SECCIONES), "");
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String estiloAsistenciaSeccion(SgSeccion sec) {
        switch (sec.getSecEstado()) {
            case CERRADA:
                return "glyphicon glyphicon-ok asistencia";
            case ABIERTA:
                return "glyphicon glyphicon-remove asistencia";
            default:
                return "";
        }
    }

    public void limpiar() {
        filtrosSeccionBean.limpiarCombos();
        this.filtrosSeccionBean.seleccionarUltimoAnio();
        listSeccion=new ArrayList();
       
    }

    public SgSeccion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSeccion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroSeccion getFiltroSeccion() {
        return filtroSeccion;
    }

    public void setFiltroSeccion(FiltroSeccion filtroSeccion) {
        this.filtroSeccion = filtroSeccion;
    }

    public List<SgSeccion> getListSeccion() {
        return listSeccion;
    }

    public void setListSeccion(List<SgSeccion> listSeccion) {
        this.listSeccion = listSeccion;
    }

 
}
