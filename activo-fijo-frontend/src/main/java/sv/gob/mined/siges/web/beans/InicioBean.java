/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.caux.AlertaInicio;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgAfBienDepreciable;
import sv.gob.mined.siges.web.dto.SgAfLoteBienes;
import sv.gob.mined.siges.web.dto.SgAfNotificacionCumplimiento;
import sv.gob.mined.siges.web.dto.catalogo.SgNoticia;
import sv.gob.mined.siges.web.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLoteBienes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNoticia;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNotificacionCumplimiento;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.BienesDepreciablesRestClient;
import sv.gob.mined.siges.web.restclient.DescargosRestClient;
import sv.gob.mined.siges.web.restclient.LoteBienesRestClient;
import sv.gob.mined.siges.web.restclient.NoticiaRestClient;
import sv.gob.mined.siges.web.restclient.NotificacionCumplimientoRestClient;
import sv.gob.mined.siges.web.restclient.TrasladosRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class InicioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(InicioBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private NoticiaRestClient notClient;

    @Inject
    private TrasladosRestClient trasladosRestClient;

    @Inject
    private DescargosRestClient descargosRestClient;

    @Inject
    private LoteBienesRestClient loteBienesRestClient;

    @Inject
    private NotificacionCumplimientoRestClient notificacionCumplimientoRestClient;

    @Inject
    private BienesDepreciablesRestClient bienesRestClient;

    private List<SgNoticia> listaNoticias = new ArrayList();
    private List<AlertaInicio> listaAlertas = new ArrayList<>();
    private DashboardModel modeloDashboard;
    private FiltroNoticia filtro = new FiltroNoticia();
    private LocalDate desde;
    private LocalDate hasta;

    public InicioBean() {
    }

    @PostConstruct
    public void init() {
        try {

            desde = LocalDate.now().minusDays(15);
            hasta = LocalDate.now();

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
            columna2.addWidget("alertas");
            modeloDashboard.addColumn(columna1);
            modeloDashboard.addColumn(columna2);
            verificarBienesNoCompletados();
            verificarBienesSIAPNoCompletados();
            verificarNotificacionesCumplimiento();
            verificarTrasladosBienes();
            verificarDescargos();
            verificarNotificacionesLoteCreado();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void verificarTrasladosBienes() { 
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_TRASLADOS_BIENES)) {
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_TRASLADO_BIENES)) {
                try {
                    FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                    filtro.setIncluirCampos(new String[]{"traPk"});
                    filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SOLICITUDES_TRASLADO_BIENES);
                    filtro.setEstadoCodigo(Constantes.CODIGO_ESTADO_PROCESO_TRASLADADO);
                    if(sessionBean.getUnidadADDefecto() != null) {
                        filtro.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                        filtro.setUnidadAdministrativaDestinoId(sessionBean.getUnidadADDefecto().getUadPk());
                    } else if(sessionBean.getSedeDefecto() != null) {
                        filtro.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                        filtro.setUnidadAdministrativaDestinoId(sessionBean.getSedeDefecto().getSedPk());
                    }
                    if (trasladosRestClient.buscarTotal(filtro) > 0) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesTrasladosPendientesAutorizacion"), ConstantesPaginas.TRASLADOS, "Ir a traslados");
                        listaAlertas.add(alerta);
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }

            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_SOLICITUDES_TRASLADO_APROBADAS)) {
                try {
                    FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                    filtro.setIncluirCampos(new String[]{"traPk"});
                    filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SOLICITUDES_TRASLADO_BIENES);
                    filtro.setEstadoCodigo(Constantes.CODIGO_ESTADO_TRASLADADO);
                    if(sessionBean.getUnidadADDefecto() != null) {
                        filtro.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                        filtro.setUnidadAdministrativaId(sessionBean.getUnidadADDefecto().getUadPk());
                    } else if(sessionBean.getSedeDefecto() != null) {
                        filtro.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                        filtro.setUnidadAdministrativaId(sessionBean.getSedeDefecto().getSedPk());
                    }
                    
                    filtro.setFechaTrasladoDesde(desde);
                    filtro.setFechaTrasladoHasta(hasta);
                    if (trasladosRestClient.buscarTotal(filtro) > 0) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesTrasladosAprobados"), ConstantesPaginas.TRASLADOS, "Ir a traslados");
                        listaAlertas.add(alerta);
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_SOLICITUDES_TRASLADO_BIENES_RECHAZADAS)) {
                try {
                    FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                    filtro.setIncluirCampos(new String[]{"traPk"});
                    filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SOLICITUDES_TRASLADO_BIENES);
                    filtro.setEstadoCodigo(Constantes.CODIGO_ESTADO_SOLICITUD_RECHAZADA);
                    if(sessionBean.getUnidadADDefecto() != null) {
                        filtro.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                        filtro.setUnidadAdministrativaId(sessionBean.getUnidadADDefecto().getUadPk());
                    } else if(sessionBean.getSedeDefecto() != null) {
                        filtro.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                        filtro.setUnidadAdministrativaId(sessionBean.getSedeDefecto().getSedPk());
                    }
                    if (trasladosRestClient.buscarTotal(filtro) > 0) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesTrasladosRechazados"), ConstantesPaginas.TRASLADOS, "Ir a traslados");
                        listaAlertas.add(alerta);
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_SOLICITUDES_TRASLADO_VENCIDAS)) {
                try {
                    FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                    filtro.setIncluirCampos(new String[]{"traPk"});
                    filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SOLICITUDES_TRASLADO_BIENES);
                    filtro.setEstadoCodigo(Constantes.CODIGO_ESTADO_PROCESO_TRASLADADO);
                    filtro.setSolicitudVencida(Boolean.TRUE);
                    if(sessionBean.getUnidadADDefecto() != null) {
                        filtro.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                        filtro.setUnidadAdministrativaId(sessionBean.getUnidadADDefecto().getUadPk());
                    } else if(sessionBean.getSedeDefecto() != null) {
                        filtro.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                        filtro.setUnidadAdministrativaId(sessionBean.getSedeDefecto().getSedPk());
                    }
                    if (trasladosRestClient.buscarTotal(filtro) > 0) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesTrasladosVencidos"), ConstantesPaginas.TRASLADOS, "Ir a traslados");
                        listaAlertas.add(alerta);
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
        }
    }

    
    public void verificarDescargos() {
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_DESCARGOS_BIENES)) {
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_DESCARGO_BIENES) || sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_DESCARGO_BIENES_VALOR_ADQUISICION_MENOR_LIMITE_AF)) {
                try {
                    FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                    filtro.setIncluirCampos(new String[]{"desPk"});
                    filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SOLICITUDES_DESCARGO);
                    filtro.setEstadoCodigo(Constantes.CODIGO_ESTADO_PROCESO_DESCARGO);
                    
                    if(sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_DESCARGO_BIENES)) {
                       filtro.setActivos(null); //para todos bienes que son activo fijo
                    } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_DESCARGO_BIENES_VALOR_ADQUISICION_MENOR_LIMITE_AF)) {
                       filtro.setActivos(Boolean.FALSE); //para bienes que no son activo fijo
                    }
                    if (descargosRestClient.buscarTotal(filtro) > 0) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesDescargosPendientesAutorizacion"), ConstantesPaginas.DESCARGOS, "Ir a descargos");
                        listaAlertas.add(alerta);
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ENVIAR_DESCARGO_BIENES)) {
                try {
                    FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                    filtro.setIncluirCampos(new String[]{"desPk"});
                    filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SOLICITUDES_DESCARGO);
                    filtro.setEstadoCodigo(Constantes.CODIGO_ESTADO_PROCESO_DESCARGO);
                    if (descargosRestClient.buscarTotal(filtro) > 0) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesDescargosEnviadosAutorizacion"), ConstantesPaginas.DESCARGOS, "Ir a descargos");
                        listaAlertas.add(alerta);
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_SOLICITUDES_DESCARGO_APROBADAS)) {
                try {
                    //Ver solicitudes de descargadas en los ultimos 15 dias
                    FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                    filtro.setIncluirCampos(new String[]{"desPk"});
                    filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SOLICITUDES_DESCARGO);
                    filtro.setEstadoCodigo(Constantes.CODIGO_ESTADO_DESCARGADO);
                    filtro.setFechaTrasladoDesde(desde);
                    filtro.setFechaTrasladoHasta(hasta);
                    if (descargosRestClient.buscarTotal(filtro) > 0) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesDescargosAprobados"), ConstantesPaginas.DESCARGOS, "Ir a descargos");
                        listaAlertas.add(alerta);
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_SOLICITUDES_DESCARGO_BIENES_RECHAZADAS)) {
                try {
                    FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                    filtro.setIncluirCampos(new String[]{"desPk"});
                    filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SOLICITUDES_DESCARGO);
                    filtro.setEstadoCodigo(Constantes.CODIGO_ESTADO_SOLICITUD_RECHAZADA);
                    if (descargosRestClient.buscarTotal(filtro) > 0) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesDescargosRechazados"), ConstantesPaginas.DESCARGOS, "Ir a descargos");
                        listaAlertas.add(alerta);
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }

            }
        }
    }

    public void verificarNotificacionesCumplimiento() {
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_NOTIFICACION_CUMPLIMIENTO)) {
            try {
                //Lotes procesados en los últimos 15 días, máximo 10 resultados
                FiltroNotificacionCumplimiento filtro = new FiltroNotificacionCumplimiento();
                filtro.setIncluirCampos(new String[]{"ncuId", "ncuVersion"});
                filtro.setMaxResults(10L);
                filtro.setLeida(Boolean.FALSE);
                List<SgAfNotificacionCumplimiento> lista = notificacionCumplimientoRestClient.buscar(filtro);
                if (lista != null && !lista.isEmpty()) {
                   // for(SgAfNotificacionCumplimiento notificacion : lista) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesNotificacionCumplimientoPendientes"), ConstantesPaginas.REPORTE_NOTIFICACION_INCUMPLIMIENTO, "Ver notificación");
                        listaAlertas.add(alerta);
                    //}
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            }
        }
    }

    public void verificarNotificacionesLoteCreado() {
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_LOTE_CREADO)) {
            try {
                //Lotes procesados en los últimos 15 días, máximo 10 resultados
                FiltroLoteBienes filtro = new FiltroLoteBienes();
                filtro.setIncluirCampos(new String[]{"lbiCodigoInventarioPadre","lbiPrimerCodInventario","lbiUltimoCodInventario"});
                filtro.setEstado(EnumEstadosProceso.PROCESADO);
                filtro.setFechaProcesoDesde(LocalDate.now().minusDays(15));
                filtro.setFechaProcesoHasta(LocalDate.now());
                filtro.setMaxResults(10L);
                filtro.setOrderBy(new String[]{"lbiFechaFinalProcesamiento"});
                filtro.setAscending(new boolean[]{false});
                List<SgAfLoteBienes> lotes = loteBienesRestClient.buscar(filtro);
                if (lotes != null && !lotes.isEmpty()) {
                    for (SgAfLoteBienes lote : lotes) {
                        if(lote.getLbiCodigoInventarioPadre() != null && lote.getLbiPrimerCodInventario() != null && lote.getLbiUltimoCodInventario() != null
                                &&  StringUtils.isNotBlank(lote.getLbiCodigoInventarioPadre()) && StringUtils.isNotBlank(lote.getLbiPrimerCodInventario()) && StringUtils.isNotBlank(lote.getLbiUltimoCodInventario())) {
                            AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tienesLoteCreado").replace("{codigoBienpadre}", lote.getLbiCodigoInventarioPadre()).replace("{bienInicial}", lote.getLbiPrimerCodInventario()).replace("{bienFinal}", lote.getLbiUltimoCodInventario()), ConstantesPaginas.CARGOS, "Ir a cargos");
                            listaAlertas.add(alerta);
                        }
                    }
                }

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            }
        }
    }

    public void verificarBienesNoCompletados() {
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_BIENES_NO_COMPLETADOS)) {
            try {
                FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                filtro.setIncluirCampos(new String[]{"bdeCodigoInventario"});
                filtro.setCompletado(Boolean.FALSE);
                filtro.setEsBienSiap(Boolean.FALSE);
                filtro.setMaxResults(20L);
                List<SgAfBienDepreciable> bienes = bienesRestClient.buscar(filtro);
                if (bienes != null && !bienes.isEmpty()) {
                    for (SgAfBienDepreciable bien : bienes) {
                        AlertaInicio alerta = new AlertaInicio(Etiquetas.getValue("tieneBienesPendientesCompletar").replace("{codigoBien}",bien.getBdeCodigoInventario()), (ConstantesPaginas.CARGO + "?id=" + bien.getBdePk() + "&edit=true"), "Ir al cargo");
                        listaAlertas.add(alerta);
                    }
                }

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            }
        }
    }
    public void verificarBienesSIAPNoCompletados() {
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VER_ALERTAS_BIENES_NO_COMPLETADOS)) {
            try {
                FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                filtro.setIncluirCampos(new String[]{"bdeCodigoInventario","bdeEsLoteSiap","bdeCantidadLote"});
                filtro.setCompletado(Boolean.FALSE);
                filtro.setEsBienSiap(Boolean.TRUE);
                filtro.setMaxResults(20L);
                List<SgAfBienDepreciable> bienes = bienesRestClient.buscar(filtro);
                List<AlertaInicio> alertasBienes = new LinkedList();
                List<AlertaInicio> alertasBienesLote = new LinkedList();
                if (bienes != null && !bienes.isEmpty()) {
                    for (SgAfBienDepreciable bien : bienes) {
                        AlertaInicio alerta = null;
                        if(bien.getBdeEsLoteSiap() != null && bien.getBdeEsLoteSiap()) {
                            alerta = new AlertaInicio(Etiquetas.getValue("tieneBienesSIAPLotePendientesCompletar").replace("{codigoBien}",bien.getBdeCodigoInventario()).replace("{cantidadlote}",String.valueOf(bien.getBdeCantidadLote())), (ConstantesPaginas.CARGO + "?id=" + bien.getBdePk() + "&edit=true"), "Ir al cargo");
                            alertasBienesLote.add(alerta);
                        } else {
                            alerta = new AlertaInicio(Etiquetas.getValue("tieneBienesSIAPPendientesCompletar").replace("{codigoBien}",bien.getBdeCodigoInventario()), (ConstantesPaginas.CARGO + "?id=" + bien.getBdePk() + "&edit=true"), "Ir al cargo");
                            alertasBienes.add(alerta);
                        }
                    }
                    listaAlertas.addAll(alertasBienes);
                    listaAlertas.addAll(alertasBienesLote);
                }

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            }
        }
    }
    public List<SgNoticia> getListaNoticias() {
        return listaNoticias;
    }

    public void setListaNoticias(List<SgNoticia> listaNoticias) {
        this.listaNoticias = listaNoticias;
    }

    public DashboardModel getModeloDashboard() {
        return modeloDashboard;
    }

    public void setModeloDashboard(DashboardModel modeloDashboard) {
        this.modeloDashboard = modeloDashboard;
    }

    public List<AlertaInicio> getListaAlertas() {
        return listaAlertas;
    }

    public void setListaAlertas(List<AlertaInicio> listaAlertas) {
        this.listaAlertas = listaAlertas;
    }

    public FiltroNoticia getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroNoticia filtro) {
        this.filtro = filtro;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public void setDesde(LocalDate desde) {
        this.desde = desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    public void setHasta(LocalDate hasta) {
        this.hasta = hasta;
    }

}
