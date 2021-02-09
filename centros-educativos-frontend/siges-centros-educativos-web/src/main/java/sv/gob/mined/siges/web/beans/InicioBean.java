/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.caux.AlertaInicio;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgNoticia;
import sv.gob.mined.siges.web.dto.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudTraslado;
import sv.gob.mined.siges.web.enumerados.EnumTipoSolicitudImpresion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNoticia;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOrganismoAdministrativoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudImpresion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudTraslado;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.NoticiaRestClient;
import sv.gob.mined.siges.web.restclient.OrganismoAdministracionEscolarRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudImpresionRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudTrasladoRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class InicioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(InicioBean.class.getName());

    @Inject
    private NoticiaRestClient notClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SolicitudTrasladoRestClient solTrasladoClient;

    @Inject
    private SolicitudImpresionRestClient solicitudImpresionRestClient;

    @Inject
    private OrganismoAdministracionEscolarRestClient organismosClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    private List<SgNoticia> listaNoticias = new ArrayList();
    private List<AlertaInicio> listaAlertas = new ArrayList<>();
    private DashboardModel modeloDashboard;
    private FiltroNoticia filtro = new FiltroNoticia();

    public InicioBean() {
    }

    @PostConstruct
    public void init() {
        try {
            filtro = new FiltroNoticia();
            filtro.setNotDesde(LocalDate.now());
            filtro.setNotHasta(LocalDate.now());
            filtro.setOrderBy(new String[]{"notOrden"});
            filtro.setAscending(new boolean[]{true});
            listaNoticias = notClient.buscar(filtro);
            modeloDashboard = new DefaultDashboardModel();
            DashboardColumn columna1 = new DefaultDashboardColumn();
            columna1.addWidget("noticias");
            DashboardColumn columna2 = new DefaultDashboardColumn();
            columna2.addWidget("alertasTempranas");
            columna2.addWidget("alertas");
            modeloDashboard.addColumn(columna1);
            modeloDashboard.addColumn(columna2);
            verificarTraslados();
            verificarCambiosOAE();
            verificarSolicitudImpresion();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void verificarTraslados() {

        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_TRASLADOS)) {

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_TRASLADO)) {

                try {
                    FiltroSolicitudTraslado filtro = new FiltroSolicitudTraslado();
                    filtro.setSotEstados(Arrays.asList(new EnumEstadoSolicitudTraslado[]{EnumEstadoSolicitudTraslado.PENDIENTE, EnumEstadoSolicitudTraslado.PENDIENTE_RESOLUCION}));
                    filtro.setVerOrigen(Boolean.TRUE);
                    filtro.setVerDestino(Boolean.FALSE);
                    if (solTrasladoClient.buscarTotal(filtro) > 0) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesTrasladosPendientesAutorizacion"), ConstantesPaginas.SOLICITUDES_TRASLADO, "Ir a traslados");
                        listaAlertas.add(alerta);
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }

            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CONFIRMAR_TRASLADO)) {

                try {
                    FiltroSolicitudTraslado filtro = new FiltroSolicitudTraslado();
                    filtro.setSotEstado(EnumEstadoSolicitudTraslado.AUTORIZADA);
                    filtro.setVerOrigen(Boolean.FALSE);
                    filtro.setVerDestino(Boolean.TRUE);
                    if (solTrasladoClient.buscarTotal(filtro) > 0) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesTrasladosPendientesConfirmacion"), ConstantesPaginas.SOLICITUDES_TRASLADO, "Ir a traslados");
                        listaAlertas.add(alerta);
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }

            }
        }

    }

    public void verificarCambiosOAE() {

        Integer diasVisualizacionCambioEstado = 5;
        try {
            SgConfiguracion configVisua = catalogosClient.buscarConfiguracionPorCodigo(Constantes.CONFIG_DIAS_VISUALIZACION_CAMBIO_ESTADO_ALERTAS_OAE);
            diasVisualizacionCambioEstado = Integer.parseInt(configVisua.getConValor());
        } catch (Exception ex) {
        }

        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_OAE_APROBADA)) {
            try {

                FiltroOrganismoAdministrativoEscolar filtro = new FiltroOrganismoAdministrativoEscolar();
                filtro.setOaeEstado(EnumEstadoOrganismoAdministrativo.APROBADO);
                filtro.setMaxResults(1L);
                filtro.setIncluirCampos(new String[]{"oaePk", "oaeFechaVencimiento", "oaeMiembrosVigentes", "oaeUltModFecha"});
                filtro.setOrderBy(new String[]{"oaeFechaActaIntegracion"});
                filtro.setAscending(new boolean[]{false});
                List<SgOrganismoAdministracionEscolar> list = organismosClient.buscar(filtro);
                if (list != null && !list.isEmpty()) {

                    SgOrganismoAdministracionEscolar org = list.get(0);

                    SgConfiguracion config = catalogosClient.buscarConfiguracionPorCodigo(Constantes.CONFIG_DIAS_ANTICIPO_ALERTA_VENCIMIENTO_OAE);
                    Integer dias = 30;
                    try {
                        dias = Integer.parseInt(config.getConValor());
                    } catch (Exception ex) {
                    }

                    if (BooleanUtils.isFalse(org.getOaeMiembrosVigentes())) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("existeOAEAprobadoMiembrosVencidos"), ConstantesPaginas.ORGANISMOS_ADMINISTRACION_ESCOLAR, "Ir a organismos de administración escolar");
                        listaAlertas.add(alerta);
                    } else if (org.getOaeFechaVencimiento() != null && LocalDate.now().plusDays(dias).isAfter(org.getOaeFechaVencimiento())) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("existeOAEAprobadoProximoAVencer"), ConstantesPaginas.ORGANISMOS_ADMINISTRACION_ESCOLAR, "Ir a organismos de administración escolar");
                        listaAlertas.add(alerta);
                    } else {
                        if (org.getOaeUltModFecha().plusDays(diasVisualizacionCambioEstado).isAfter(LocalDateTime.now())) {
                            AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("existenOAEAprobadas"), ConstantesPaginas.ORGANISMOS_ADMINISTRACION_ESCOLAR, "Ir a organismos de administración escolar");
                            listaAlertas.add(alerta);
                        }
                    }

                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            }
        }
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_OAE_RECHAZADA)) {
            try {
                FiltroOrganismoAdministrativoEscolar filtro = new FiltroOrganismoAdministrativoEscolar();
                filtro.setOaeEstado(EnumEstadoOrganismoAdministrativo.RECHAZADO);
                filtro.setMaxResults(1L);
                filtro.setIncluirCampos(new String[]{"oaePk", "oaeUltModFecha"});
                filtro.setOrderBy(new String[]{"oaeFechaActaIntegracion"});
                filtro.setAscending(new boolean[]{false});
                List<SgOrganismoAdministracionEscolar> list = organismosClient.buscar(filtro);
                if (list != null && !list.isEmpty()) {
                    SgOrganismoAdministracionEscolar org = list.get(0);
                    if (org.getOaeUltModFecha().plusDays(diasVisualizacionCambioEstado).isAfter(LocalDateTime.now())) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("existenOAERechazadas"), ConstantesPaginas.ORGANISMOS_ADMINISTRACION_ESCOLAR, "Ir a organismos de administración escolar");
                        listaAlertas.add(alerta);
                    }
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            }
        }
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_OAE_MASDATOS)) {
            try {
                FiltroOrganismoAdministrativoEscolar filtro = new FiltroOrganismoAdministrativoEscolar();
                filtro.setOaeEstado(EnumEstadoOrganismoAdministrativo.AMPLIAR_DATOS);
                filtro.setMaxResults(1L);
                filtro.setIncluirCampos(new String[]{"oaePk"});
                filtro.setOrderBy(new String[]{"oaeFechaActaIntegracion"});
                filtro.setAscending(new boolean[]{false});
                List<SgOrganismoAdministracionEscolar> list = organismosClient.buscar(filtro);
                if (list != null && !list.isEmpty()) {
                    AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("existenOAEMasDatos"), ConstantesPaginas.ORGANISMOS_ADMINISTRACION_ESCOLAR, "Ir a organismos de administración escolar");
                    listaAlertas.add(alerta);
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            }
        }
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_OAE_ENVIADA)) {
            try {
                FiltroOrganismoAdministrativoEscolar filtro = new FiltroOrganismoAdministrativoEscolar();
                filtro.setOaeEstado(EnumEstadoOrganismoAdministrativo.ENVIADO);
                filtro.setMaxResults(1L);
                filtro.setIncluirCampos(new String[]{"oaePk"});
                filtro.setOrderBy(new String[]{"oaeFechaActaIntegracion"});
                filtro.setAscending(new boolean[]{false});
                List<SgOrganismoAdministracionEscolar> list = organismosClient.buscar(filtro);
                if (list != null && !list.isEmpty()) {
                    SgOrganismoAdministracionEscolar org = list.get(0);
                    AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("existenOAEEnviada"), ConstantesPaginas.EVALUACIONES_ORGANISMOS_ADMINISTRACION_ESCOLAR, "Ir a evaluación OAE");
                    listaAlertas.add(alerta);
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            }
        }
    }

    public void verificarAlertasTempranas() {

    }

    public void verificarSolicitudImpresion() {
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_TITULOS)) {

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_SOLICITUD_IMPRESION) && sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SOLICITUDES_IMPRESIONES)) {
                try {
                    FiltroSolicitudImpresion fsi = new FiltroSolicitudImpresion();
                    fsi.setSimTipoSolicitudImpresion(EnumTipoSolicitudImpresion.IMP);
                    fsi.setSimEstado(EnumEstadoSolicitudImpresion.IMPRESA);
                    fsi.setMaxResults(1L);
                    if (solicitudImpresionRestClient.buscarTotal(fsi) > 0) {
                        AlertaInicio alerta = new AlertaInicio("Existen solicitudes de impresión de títulos impresas", ConstantesPaginas.SOLICITUDES_IMPRESIONES + "?estado=" + EnumEstadoSolicitudImpresion.IMPRESA, "Ir a solicitudes");
                        listaAlertas.add(alerta);
                    }
                    fsi.setSimEstado(EnumEstadoSolicitudImpresion.CONFIRMADA);
                    if (solicitudImpresionRestClient.buscarTotal(fsi) > 0) {
                        AlertaInicio alerta = new AlertaInicio("Existen solicitudes de impresión de títulos confirmadas", ConstantesPaginas.SOLICITUDES_IMPRESIONES + "?estado=" + EnumEstadoSolicitudImpresion.CONFIRMADA, "Ir a solicitudes");
                        listaAlertas.add(alerta);
                    }
                    fsi.setSimEstado(EnumEstadoSolicitudImpresion.CON_EXCEPCIONES);
                    if (solicitudImpresionRestClient.buscarTotal(fsi) > 0) {
                        AlertaInicio alerta = new AlertaInicio("Existen solicitudes de impresión de títulos confirmadas con excepciones", ConstantesPaginas.SOLICITUDES_IMPRESIONES + "?estado=" + EnumEstadoSolicitudImpresion.CON_EXCEPCIONES, "Ir a solicitudes");
                        listaAlertas.add(alerta);
                    }
                    fsi.setSimEstado(EnumEstadoSolicitudImpresion.RECHAZADA);
                    if (solicitudImpresionRestClient.buscarTotal(fsi) > 0) {
                        AlertaInicio alerta = new AlertaInicio("Existen solicitudes de impresión de títulos rechazadas", ConstantesPaginas.SOLICITUDES_IMPRESIONES + "?estado=" + EnumEstadoSolicitudImpresion.RECHAZADA, "Ir a solicitudes");
                        listaAlertas.add(alerta);
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_SOLICITUD_IMPRESION) && sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SOLICITUDES_IMPRESIONES)) {
                try {
                    FiltroSolicitudImpresion fsi = new FiltroSolicitudImpresion();
                    fsi.setSimTipoSolicitudImpresion(EnumTipoSolicitudImpresion.REI);
                    fsi.setSimEstado(EnumEstadoSolicitudImpresion.IMPRESA);
                    fsi.setMaxResults(1L);
                    if (solicitudImpresionRestClient.buscarTotal(fsi) > 0) {
                        AlertaInicio alerta = new AlertaInicio("Existen solicitudes de reimpresión de títulos impresas", ConstantesPaginas.SOLICITUDES_IMPRESIONES + "?estado=" + EnumEstadoSolicitudImpresion.IMPRESA, "Ir a solicitudes");
                        listaAlertas.add(alerta);
                    }
                    fsi.setSimEstado(EnumEstadoSolicitudImpresion.CONFIRMADA);
                    if (solicitudImpresionRestClient.buscarTotal(fsi) > 0) {
                        AlertaInicio alerta = new AlertaInicio("Existen solicitudes de reimpresión de títulos confirmadas", ConstantesPaginas.SOLICITUDES_IMPRESIONES + "?estado=" + EnumEstadoSolicitudImpresion.CONFIRMADA, "Ir a solicitudes");
                        listaAlertas.add(alerta);
                    }
                    fsi.setSimEstado(EnumEstadoSolicitudImpresion.CON_EXCEPCIONES);
                    if (solicitudImpresionRestClient.buscarTotal(fsi) > 0) {
                        AlertaInicio alerta = new AlertaInicio("Existen solicitudes de reimpresión de títulos confirmadas con excepciones", ConstantesPaginas.SOLICITUDES_IMPRESIONES + "?estado=" + EnumEstadoSolicitudImpresion.CON_EXCEPCIONES, "Ir a solicitudes");
                        listaAlertas.add(alerta);
                    }
                    fsi.setSimEstado(EnumEstadoSolicitudImpresion.RECHAZADA);
                    if (solicitudImpresionRestClient.buscarTotal(fsi) > 0) {
                        AlertaInicio alerta = new AlertaInicio("Existen solicitudes de reimpresión de títulos rechazadas", ConstantesPaginas.SOLICITUDES_IMPRESIONES + "?estado=" + EnumEstadoSolicitudImpresion.RECHAZADA, "Ir a solicitudes");
                        listaAlertas.add(alerta);
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_GENERACION_TITULO) && sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_SOLICITUD_IMPRESION)) {
                try {
                    FiltroSolicitudImpresion fsi = new FiltroSolicitudImpresion();
                    fsi.setSimEstado(EnumEstadoSolicitudImpresion.ENVIADA);
                    fsi.setMaxResults(1L);
                    if (solicitudImpresionRestClient.buscarTotal(fsi) > 0) {
                        AlertaInicio alerta = new AlertaInicio("Existen solicitudes de impresión de títulos pendientes", ConstantesPaginas.GENERACION_TITULOS, "Ir a solicitudes");
                        listaAlertas.add(alerta);
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
        }
    }

    public List<AlertaInicio> getListaAlertas() {
        return listaAlertas;
    }

    public List<SgNoticia> getListaNoticias() {
        return listaNoticias;
    }

    public DashboardModel getModeloDashboard() {
        return modeloDashboard;
    }

}
