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
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.AlertaInicio;

/**
 * Bean correspondiente a la página de inicio.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class InicioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(InicioBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    private DashboardModel modeloDashboard;
    
    private List<AlertaInicio> listaAlertas = new ArrayList<>();

    public InicioBean() {
    }

    /**
     * Inicializa el contenido de la página de inicio
     */
    @PostConstruct
    public void init() {
        try {
            modeloDashboard = new DefaultDashboardModel();
            DashboardColumn columna1 = new DefaultDashboardColumn();
            columna1.addWidget("noticias");
            DashboardColumn columna2 = new DefaultDashboardColumn();
            columna2.addWidget("alertasTempranas");
            columna2.addWidget("alertas");
            modeloDashboard.addColumn(columna1);
            modeloDashboard.addColumn(columna2);
            
            verificarCambiosPresupuestos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }
    
    public void verificarCambiosPresupuestos() {

//        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_OAE_APROBADA)) {
//            try {
//                FiltroOrganismoAdministrativoEscolar filtro = new FiltroOrganismoAdministrativoEscolar();
//                filtro.setOaeEstado(EnumEstadoOrganismoAdministrativo.APROBADO);
//                organismosClient.buscar(filtro);
//                if (organismosClient.buscarTotal(filtro) > 0) {
//                    AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("existenOAEAprobadas"), ConstantesPaginas.ORGANISMOS_ADMINISTRACION_ESCOLAR, "Ir a organismos de administración escolar");
//                    listaAlertas.add(alerta);
//                }
//            } catch (Exception ex) {
//                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
//            }
//        }
//        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_OAE_RECHAZADA)) {
//            try {
//                FiltroOrganismoAdministrativoEscolar filtro = new FiltroOrganismoAdministrativoEscolar();
//                filtro.setOaeEstado(EnumEstadoOrganismoAdministrativo.RECHAZADO);
//                organismosClient.buscar(filtro);
//                if (organismosClient.buscarTotal(filtro) > 0) {
//                    AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("existenOAERechazadas"), ConstantesPaginas.ORGANISMOS_ADMINISTRACION_ESCOLAR, "Ir a organismos de administración escolar");
//                    listaAlertas.add(alerta);
//                }
//            } catch (Exception ex) {
//                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
//            }
//        }
//        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_OAE_MASDATOS)) {
//            try {
//                FiltroOrganismoAdministrativoEscolar filtro = new FiltroOrganismoAdministrativoEscolar();
//                filtro.setOaeEstado(EnumEstadoOrganismoAdministrativo.AMPLIAR_DATOS);
//                organismosClient.buscar(filtro);
//                if (organismosClient.buscarTotal(filtro) > 0) {
//                    AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("existenOAEMasDatos"), ConstantesPaginas.ORGANISMOS_ADMINISTRACION_ESCOLAR, "Ir a organismos de administración escolar");
//                    listaAlertas.add(alerta);
//                }
//            } catch (Exception ex) {
//                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
//            }
//        }
//        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_OAE_ENVIADA)) {
//            try {
//                FiltroOrganismoAdministrativoEscolar filtro = new FiltroOrganismoAdministrativoEscolar();
//                filtro.setOaeEstado(EnumEstadoOrganismoAdministrativo.ENVIADO);
//                organismosClient.buscar(filtro);
//                if (organismosClient.buscarTotal(filtro) > 0) {
//                    AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("existenOAEEnviada"), ConstantesPaginas.EVALUACIONES_ORGANISMOS_ADMINISTRACION_ESCOLAR, "Ir a evaluación OAE");
//                    listaAlertas.add(alerta);
//                }
//            } catch (Exception ex) {
//                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
//            }
//        }
    }

    public DashboardModel getModeloDashboard() {
        return modeloDashboard;
    }

    public List<AlertaInicio> getListaAlertas() {
        return listaAlertas;
    }

    public void setListaAlertas(List<AlertaInicio> listaAlertas) {
        this.listaAlertas = listaAlertas;
    }
    
    
    
}
