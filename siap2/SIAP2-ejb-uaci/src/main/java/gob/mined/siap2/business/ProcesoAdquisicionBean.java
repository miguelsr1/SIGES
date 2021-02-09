/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import com.google.gson.Gson;
import gob.mined.siap2.business.datatype.gantt.DataGantt;
import gob.mined.siap2.business.ejbs.ConfiguracionBean;
import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.business.ejbs.impl.ArchivoBean;
import gob.mined.siap2.business.utils.ContratoUtils;
import gob.mined.siap2.business.utils.GanttUtils;
import gob.mined.siap2.business.utils.ProrrateoUtils;
import gob.mined.siap2.business.utils.ReportesUtils;
import gob.mined.siap2.business.utils.UtilsUACI;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesConfiguracion;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.entities.data.SsLock;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestario;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioModificativa;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioProceso;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.Gantt;
import gob.mined.siap2.entities.data.impl.GanttTask;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicionRango;
import gob.mined.siap2.entities.data.impl.Notificacion;
import gob.mined.siap2.entities.data.impl.NotificacionPOA;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POGInsumo;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItemOfertaInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItemProvOferta;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionLote;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionProveedor;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.data.impl.SecuenciaContratoOrden;
import gob.mined.siap2.entities.data.impl.SecuenciaProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.TDR;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoCompromiso;
import gob.mined.siap2.entities.enums.EstadoContrato;
import gob.mined.siap2.entities.enums.EstadoItem;
import gob.mined.siap2.entities.enums.EstadoProcesoAdq;
import gob.mined.siap2.entities.enums.PasosProcesoAdquisicion;
import gob.mined.siap2.entities.enums.TipoCompromisoPresupuestario;
import gob.mined.siap2.entities.enums.TipoGeneracionContrato;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.enums.TipoProyecto;
import gob.mined.siap2.entities.enums.TipoTarea;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.ImpuestoDAO;
import gob.mined.siap2.persistence.dao.imp.MetodoAdquisicionDAO;
import gob.mined.siap2.persistence.dao.imp.ProcesoAdquisicionDAO;
import gob.mined.siap2.persistence.dao.imp.UsuarioDAO;
import gob.mined.siap2.sofisform.to.AND_TO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.ws.to.DataFile;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos correspondientes al proceso de adquisición
 *
 * @author Sofis Solutions
 */
@Stateless(name = "ProcesoAdquisicionBean")
@LocalBean
public class ProcesoAdquisicionBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private ProcesoAdquisicionDAO pAdqDao;
    @Inject
    @JPADAO
    private ImpuestoDAO impuestoDao;

    @Inject
    @JPADAO
    private UsuarioDAO usuarioDAO;
    @Inject
    private DatosUsuario usrBean;
    @Inject
    private ArchivoBean archivoBean;
    @Inject
    private ConfiguracionBean confBean;
    @Inject
    @JPADAO
    private MetodoAdquisicionDAO metodoAdqDAO;
    @Inject
    private UsuarioBean usuarioBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Guarda el proceso de adquisición
     *
     * @param proceso
     * @param idMetodo
     * @param fechaInicio
     * @param ganttJson
     * @return
     */
    public ProcesoAdquisicion guardarProceso(ProcesoAdquisicion proceso, Integer idMetodo, Date fechaInicio, String ganttJson) {
        try {
            Integer idProceso = (proceso.getId() == null) ? 0 : proceso.getId();
            if (pAdqDao.existeProcesoByNombreAnio(idProceso, proceso.getNombre(), proceso.getAnio().getId())) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PROC_YA_EXISTE_POR_NOMBRE_ANIO);
                throw b;
            }
            //si es nuevo se setea la secuencia correlativa por año y el estado en inicializado
            List<SecuenciaProcesoAdquisicion> listaSecuencia = generalDAO.findByOneProperty(SecuenciaProcesoAdquisicion.class, "anio", proceso.getAnio().getAnio());
            if (proceso.getId() == null) {
                generalDAO.lock(SsLock.class, ConstantesEstandares.LOCK_SECUENCIA_PROCESO_ADQUISICION);
                SecuenciaProcesoAdquisicion secuencia = null;
                if (!listaSecuencia.isEmpty()) {
                    secuencia = listaSecuencia.get(0);
                } else {
                    secuencia = new SecuenciaProcesoAdquisicion();
                    secuencia.setAnio(proceso.getAnio().getAnio());
                    secuencia.setUltimoGenerado(0);
                }
                proceso.setSecuenciaAnio(secuencia.getAnio());
                proceso.setSecuenciaNumero(secuencia.getUltimoGenerado() + 1);
                secuencia.setUltimoGenerado(proceso.getSecuenciaNumero());
                generalDAO.update(secuencia);

                proceso.setEstado(PasosProcesoAdquisicion.INICIALIZACION);
                proceso.setEstadoProceso(EstadoProcesoAdq.NORMAL);
                proceso.setMontoTotal(BigDecimal.ZERO);
            } else {
                estaDesiertoOSinEfecto(proceso);
                if (idMetodo != null && fechaInicio != null) {

                    proceso.setGantt(null);
                    Gantt g = null;
                    Date fechaMinimaGantt = fechaInicio;
                    Date fechaMaximaGantt = null;

                    MetodoAdquisicion metodo = (MetodoAdquisicion) generalDAO.find(MetodoAdquisicion.class, idMetodo);
                    proceso.setMetodoAdquisicion(metodo);

                    Gson gson = new Gson();
                    DataGantt datGantt = gson.fromJson(ganttJson, DataGantt.class);
                    g = GanttUtils.getGantt(datGantt);

                    //Si se encuentra en el paso de invitación o luego de este, se controla
                    // que la fecha de recepción de ofertas del cronograma real a guardar, 
                    //sea mayor o igual a la fecha que ya tenía establecida
                    this.verificarCambioFechaYHoraRecepcionOfertas(proceso, proceso.getEstado(), g);

                    fechaMinimaGantt = GanttUtils.getFechaMinima(g);
                    fechaMaximaGantt = GanttUtils.getFechaMaxima(g);

                    proceso.setMenorFechaGantt(fechaMinimaGantt);
                    proceso.setMayorFechaGantt(fechaMaximaGantt);
                    proceso.setGantt(g);
                }
                if(listaSecuencia != null && !listaSecuencia.isEmpty() && listaSecuencia.size() > 0) {
                    SecuenciaProcesoAdquisicion secuencia = listaSecuencia.get(0);
                    //Si al proceso se le editó el Nro y se ingresó uno mayor a todos los del mismo año, lo guardo como la secuencia mayor
                    if (proceso.getSecuenciaNumero() > secuencia.getUltimoGenerado()) {
                        secuencia.setUltimoGenerado(proceso.getSecuenciaNumero());
                        generalDAO.update(secuencia);
                    }
                }
            }

            proceso = (ProcesoAdquisicion) generalDAO.update(proceso);
            return proceso;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Asigna un método de adquisición a un proceso de adquisición y otros datos
     * relativos al Gantt
     *
     * @param idProceso
     * @param idMetodo
     * @param fechaInicio
     * @param ganttJson
     */
    public void guardarMetodoAdquisicion(Integer idProceso, Integer idMetodo, Date fechaInicio, String ganttJson) {
        try {
            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class, idProceso);
            estaDesiertoOSinEfecto(proceso);
            //si tiene método de adquisicion
            Gantt g = null;
            Date fechaMinimaGantt = fechaInicio;
            Date fechaMaximaGantt = null;

            MetodoAdquisicion metodo = (MetodoAdquisicion) generalDAO.find(MetodoAdquisicion.class, idMetodo);
            proceso.setMetodoAdquisicion(metodo);

            Gson gson = new Gson();
            DataGantt datGantt = gson.fromJson(ganttJson, DataGantt.class);
            g = GanttUtils.getGantt(datGantt);
            fechaMinimaGantt = GanttUtils.getFechaMinima(g);
            fechaMaximaGantt = GanttUtils.getFechaMaxima(g);

            proceso.setMenorFechaGantt(fechaMinimaGantt);
            proceso.setMayorFechaGantt(fechaMaximaGantt);
            proceso.setGantt(g);

            proceso = (ProcesoAdquisicion) generalDAO.update(proceso);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Obtiene una lista de PACs con Insumos de un proceso de adquisición según
     * distintos criterios
     *
     * @param pacId
     * @param nombrePac
     * @param rubro
     * @param codigoIns
     * @param nombreIns
     * @param unidadTecId
     * @param idPacGrupo
     * @param nombreGrupo
     * @return
     */
    public List<PAC> obtenerPacsParaAgregarInsumos(Integer pacId, String nombrePac, Integer rubro, String codigoIns, String nombreIns, Integer unidadTecId, Integer idPacGrupo, String nombreGrupo) {
        List<PAC> pacs = pAdqDao.obtenerPacsConInsumosParaProcesosDeAdquisicion(rubro, codigoIns, nombreIns, unidadTecId, pacId, nombrePac, idPacGrupo, nombreGrupo);

        return pacs;
    }

    /**
     * Este método devuelve la lista de grupos de PAC según distintos criterios.
     *
     * @param pacId
     * @param rubro
     * @param codigoIns
     * @param nombreIns
     * @param unidadTecId
     * @param pacGrupoId
     * @param nombreGrupo
     * @return
     */
    public List<PACGrupo> obtenerPacsGruposConInsumosParaProcesosDeAdquisicionPorPacId(Integer pacId, Integer rubro, String codigoIns, String nombreIns, Integer unidadTecId, Integer pacGrupoId, String nombreGrupo) {
        List<PACGrupo> grupos = pAdqDao.obtenerPacsGruposConInsumosParaProcesosDeAdquisicionPorPacId(pacId, rubro, codigoIns, nombreIns, unidadTecId, pacGrupoId, nombreGrupo);
        return grupos;
    }

    /**
     * Este método devuelve la lista de insumos en un POA
     *
     * @param pacGrupoId
     * @param rubro
     * @param codigoIns
     * @param nombreIns
     * @param unidadTecId
     * @return
     */
    public List<POInsumos> obtenerPoInsumosParaProcesosDeAdquisicionPorPacGrupoId(Integer pacGrupoId, Integer rubro, String codigoIns, String nombreIns, Integer unidadTecId) {
        List<POInsumos> insumos = pAdqDao.obtenerPoInsumosParaProcesosDeAdquisicionPorPacGrupoId(pacGrupoId, rubro, codigoIns, nombreIns, unidadTecId);
        return insumos;
    }

    /**
     * Obtiene un proceso de adquisición y carga todas sus listas asociadas
     *
     * @param id
     * @return
     */
    public ProcesoAdquisicion cargarProceso(Integer id) {
        try {
            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class, id);

            proceso.getInsumos().isEmpty();
            for (ProcesoAdquisicionInsumo insumoProceso : proceso.getInsumos()) {
                insumoProceso.getPoInsumo().getArchivosCertificacionPresupuestaria().isEmpty();
                insumoProceso.getRelItemInsumos().isEmpty();
            }
            for (ProcesoAdquisicionItem item : proceso.getItems()) {
                item.getRelItemInsumos().isEmpty();
                item.getProveedorOfertaAdjudicadaId();
                item.getOfertasProvedor().isEmpty();
            }
            for (ProcesoAdquisicionLote lote : proceso.getLotes()) {
                lote.getItems().isEmpty();
                for (ProcesoAdquisicionItem item : lote.getItems()) {
                    item.getProveedorOfertaAdjudicadaId();
                    item.getRelItemInsumos().isEmpty();
                    item.getOfertasProvedor().isEmpty();
                }
            }

            for (ContratoOC contrato : proceso.getContratos()) {
                contrato.getImpuestos().isEmpty();
                for (FlujoCajaAnio fc : contrato.getProgramacionPagosProceso()) {
                    fc.getFlujoCajaMenusal().isEmpty();
                }
                for (ProcesoAdquisicionItem item : contrato.getItems()) {
                    item.getOfertasProvedor().isEmpty();
                    item.getProveedorOfertaAdjudicadaId();
                }
            }

            return proceso;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Cambia el estado del proceso si pasa las validaciones correspondientes al
     * paso actual
     *
     * @param idProceso
     * @param nuevoEstado
     * @return
     * @throws GeneralException
     */
    public ProcesoAdquisicion cambiarEstadoProceso(int idProceso, PasosProcesoAdquisicion nuevoEstado, String ganttNuevo) throws GeneralException {
        try {
            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class, idProceso);

            estaDesiertoOSinEfecto(proceso);
            //Si quiere adelantar de paso, se verifica que el técnico UACI seleccionado sea el que está logueado
            if (nuevoEstado.getPosicion() > proceso.getEstado().getPosicion()) {
                tecnicoUACIActualCoincideConProximoEstado(proceso);
            }

            //Si el proceso está en pausa no puede cambiar de estado
            if (proceso.getEstadoProceso() == EstadoProcesoAdq.PAUSA) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PROCESO_EN_PAUSA);
                throw b;
            }

            //si ya fue adjudicado, no puede volver para un estado anterior si un item ya fue asignado
            if (proceso.getEstado().getPosicion() >= PasosProcesoAdquisicion.ADJUDICACION.getPosicion()
                    && nuevoEstado.getPosicion() < PasosProcesoAdquisicion.ADJUDICACION.getPosicion()) {
                for (ProcesoAdquisicionItem item : proceso.getItems()) {
                    if (item.getEstado() != null) {
                        String[] args = {item.getNombre()};
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_EL_ITEM_0_YA_FUE_ASIGNADO_NO_PUEDE_RETROCEDER, args);
                        throw b;
                    }
                }
                for (ProcesoAdquisicionLote lote : proceso.getLotes()) {
                    for (ProcesoAdquisicionItem item : lote.getItems()) {
                        if (item.getEstado() != null) {
                            String[] args = {item.getNombre()};
                            BusinessException b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_EL_ITEM_0_YA_FUE_ASIGNADO_NO_PUEDE_RETROCEDER, args);
                            throw b;
                        }
                    }
                }
            }

            //Si el proceso tiene compromiso presupuestario generado no deberia poder volver para atras
            if (proceso.getEstado().getPosicion() >= PasosProcesoAdquisicion.CONTRATO_ORDEN_DE_COMPRA.getPosicion()
                    && nuevoEstado.getPosicion() < PasosProcesoAdquisicion.CONTRATO_ORDEN_DE_COMPRA.getPosicion()) {
                if (proceso.getCompromisosPresupuestarios() != null && !proceso.getCompromisosPresupuestarios().isEmpty()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EL_PROCESO_YA_TIENE_UN_COMPROMISO_PRESUPUESTARIO_GENERADO);
                    throw b;
                }
            }

            //Verifica que la fecha de recepción de ofertas del cronograma real de un
            // proceso de adquisión, sea mayor o igual a la fecha que ya tenía establecida
            Gantt g = null;
            Gson gson = new Gson();
            DataGantt datGantt = gson.fromJson(ganttNuevo, DataGantt.class);
            g = GanttUtils.getGantt(datGantt);
            this.verificarCambioFechaYHoraRecepcionOfertas(proceso, nuevoEstado, g);

            PasosProcesoAdquisicion estadoOrigen = proceso.getEstado();
            if (estadoOrigen.getPosicion() < nuevoEstado.getPosicion()) {
                BusinessException b;
                switch (nuevoEstado) {
                    case RECEPCION_TDR_ET_CERT_DISP:
                        if (proceso.getInsumos().isEmpty()) {
                            b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_SIN_INSUMOS);
                            throw b;
                        }
                        if (proceso.getMetodoAdquisicion() == null) {
                            b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_METODO_ADQ_VACIO);
                            throw b;
                        }

                        //valida que esten cargados los tdr y certificados validados
                        for (ProcesoAdquisicionInsumo insumoProceso : proceso.getInsumos()) {
                            POInsumos poInsumo = insumoProceso.getPoInsumo();

                            boolean faltaTDR = false;
                            boolean faltaCertificadoDisp = false;
                            if (poInsumo.getTdr() == null) {
                                faltaTDR = true;
                            }
                            if (poInsumo.getPasoValidacionCertificadoDeDispPresupuestaria() == null
                                    || !poInsumo.getPasoValidacionCertificadoDeDispPresupuestaria()) {
                                faltaCertificadoDisp = true;
                            }

                            if (faltaTDR || faltaCertificadoDisp) {
                                String notificacion = "";
                                notificacion = "Debe ";

                                if (faltaTDR && faltaCertificadoDisp) {
                                    notificacion += "ingresar TDR/ET y validar certidicado de diponibilidad ";
                                } else if (faltaTDR) {
                                    notificacion += "ingresar TDR/ET ";
                                } else {
                                    notificacion += "validar certidicado de diponibilidad ";
                                }
                                notificacion += "para el insumo " + poInsumo.getId() + " - " + poInsumo.getInsumo().getNombre();
                                TipoNotificacion tipoN = null;
                                if (poInsumo.getPoa().getTipo() == TipoPOA.POA_PROYECTO) {
                                    tipoN = TipoNotificacion.INGRESO_TDR_CERT_DISP_INSUMO_PROY;
                                } else {
                                    POAConActividades poaAct = (POAConActividades) poInsumo.getPoa();
                                    if (poaAct.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ACCION_CENTRAL) {
                                        tipoN = TipoNotificacion.INGRESO_TDR_CERT_DISP_INSUMO_ACC_CENTR;
                                    } else {
                                        tipoN = TipoNotificacion.INGRESO_TDR_CERT_DISP_INSUMO_ASIG_NP;
                                    }
                                }

                                List<SsUsuario> anotificar = usuarioDAO.getUsuariosConUT(poInsumo.getPoa().getUnidadTecnica().getId(), null);
                                for (SsUsuario u : anotificar) {
                                    NotificacionPOA n = new NotificacionPOA();
                                    n.setTipo(tipoN);
                                    n.setContenido(notificacion);
                                    n.setUsuario(u);
                                    n.setFecha(new Date());
                                    n.setObjetoId(poInsumo.getPoa().getId());
                                    if (poInsumo.getPoa().getTipo() == TipoPOA.POA_PROYECTO) {
                                        POAProyecto poaProyecto = (POAProyecto) poInsumo.getPoa();
                                        n.setPoaAnio(poaProyecto.getAnioFiscal().getId());
                                        n.setObjetoId(poaProyecto.getProyecto().getId());
                                    } else {
                                        POAConActividades poaActividades = (POAConActividades) poInsumo.getPoa();
                                        n.setPoaAnio(poaActividades.getAnioFiscal().getId());
                                        n.setObjetoId(poaActividades.getConMontoPorAnio().getId());
                                    }

                                    n.setPoaUT(poInsumo.getPoa().getUnidadTecnica().getId());

                                    generalDAO.update(n);
                                }
                            }

                        }
                        if (proceso.getGanttPlanificado() == null && proceso.getGantt() != null) {
                            Gantt ganttPlanificado = GanttUtils.copiarGantt(proceso.getGantt());
                            proceso.setGanttPlanificado(ganttPlanificado);
                        }

                        break;
                    case REVISION_JEFE_UACI:
                        b = new BusinessException();
                        try {
                            chequeoPasajeAEstadoRevisionJefeUACI(proceso);
                        } catch (BusinessException be) {
                            b.getErrores().addAll(be.getErrores());
                        }
                        if (!b.getErrores().isEmpty()) {
                            throw b;
                        }
                        break;
                    case REVISION_TECNICO_UACI:
                        //TODO: CONTROL DE MONTOS DE CERTIFICADO DISPONIBILIDAD
                        if (proceso.getMetodoAdquisicion() == null) {
                            b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_METODO_ADQ_VACIO);
                            throw b;
                        }
                        if (proceso.getResponsable() == null) {
                            b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_SIN_RESPONSABLE_ASIGNADO);
                            throw b;
                        }
                        break;
                    case INVITACION:
                        b = new BusinessException();
                        if (proceso.getMetodoAdquisicion() == null) {
                            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_METODO_ADQ_VACIO);
                        }
                        try {
                            chequeoPasajeAEstadoInvitacion(proceso);
                        } catch (BusinessException be) {
                            b.getErrores().addAll(be.getErrores());
                        }

                        if (!b.getErrores().isEmpty()) {
                            throw b;
                        }
                        break;
                    case RECEPCION_OFERTAS:
                        int cantInvitados = 0;
                        for (ProcesoAdquisicionProveedor proveedorAdq : proceso.getProveedores()) {
                            if (proveedorAdq.getInvitado()) {
                                cantInvitados++;
                            }
                        }
                        b = new BusinessException();
                        if (cantInvitados < 1) {

                            b.addError(ConstantesErrores.ERR_PROCESO_DEBE_TENER_AL_MENOS_UN_INVITADO);

                        }
                        if (proceso.getMetodoAdquisicion() == null) {

                            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_METODO_ADQ_VACIO);

                        }

                        if (!b.getErrores().isEmpty()) {
                            throw b;
                        }

                        break;
                    case EVALUACION:
                        b = new BusinessException();
                        try {
                            chequeoPasajeAEstadoInvitacion(proceso);
                        } catch (BusinessException be) {
                            b.getErrores().addAll(be.getErrores());
                        }
                        if (proceso.getProveedores() == null && proceso.getProveedores().isEmpty()) {

                            b.addError(ConstantesErrores.ERR_PROCESO_DEBE_TENER_AL_MENOS_UN_PROVEEDOR);

                        }
                        if (proceso.getMetodoAdquisicion() == null) {

                            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_METODO_ADQ_VACIO);

                        }
                        if (!b.getErrores().isEmpty()) {
                            throw b;
                        }
                        break;
                    case ADJUDICACION:

                        List<ProcesoAdquisicionItem> items = pAdqDao.obtenerItemsDelProceso(proceso.getId());
                        for (ProcesoAdquisicionItem item : items) {
                            if (item.getOfertasProvedor().isEmpty()) {
                                b = new BusinessException();
                                b.addError(ConstantesErrores.ERR_ITEM_SIN_OFERTAS);
                                throw b;
                            }
                        }
                        if (proceso.getMetodoAdquisicion() == null) {
                            b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_METODO_ADQ_VACIO);
                            throw b;

                        }

                        break;
                    case COMPROMISO_PRESUPUESTARIO:
                        for (ProcesoAdquisicionItem item : proceso.getItems()) {
                            if (item.getEstado() == null || item.getEstado().equals(EstadoItem.PAUSA)) {
                                b = new BusinessException();
                                b.addError(ConstantesErrores.ERR_ITEMS_SIN_DEFINICION);
                                throw b;
                            }

                        }
                        List<ProcesoAdquisicionLote> lotes = proceso.getLotes();
                        for (ProcesoAdquisicionLote lote : lotes) {
                            for (ProcesoAdquisicionItem item : lote.getItems()) {
                                if (item.getEstado() == null || item.getEstado().equals(EstadoItem.PAUSA)) {
                                    b = new BusinessException();
                                    b.addError(ConstantesErrores.ERR_ITEMS_SIN_DEFINICION);
                                    throw b;
                                }

                            }
                        }

                        if (proceso.getMetodoAdquisicion() == null) {
                            b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_METODO_ADQ_VACIO);
                            throw b;

                        }
                        break;
                    case CONTRATO_ORDEN_DE_COMPRA:
                        //TODO: CONTROL DE MONTOS DE CERTIFICADO PRESUPUESTARIO
                        if (proceso.getMetodoAdquisicion() == null) {
                            b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_METODO_ADQ_VACIO);
                            throw b;

                        }
                        break;
                    case ORDEN_INICIO:
                        //TODO: CONTROL DE MONTOS DE CERTIFICADO PRESUPUESTARIO
                        if (proceso.getMetodoAdquisicion() == null) {
                            b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_METODO_ADQ_VACIO);
                            throw b;

                        }
                        if (proceso.getContratos().isEmpty()) {
                            b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_NO_HAY_CONTRATOS_GENERADOS);
                            throw b;

                        }

                        List<ProcesoAdquisicionItem> itemsAdjudicados = pAdqDao.obtenerItemsDelProcesoAdjudicados(proceso.getId());

                        for (ProcesoAdquisicionItem itemAdjudicado : itemsAdjudicados) {
                            if (itemAdjudicado.getEstado().equals(EstadoItem.ADJUDICADO)) {
                                if (itemAdjudicado.getContrato() == null) {
                                    b = new BusinessException();
                                    b.addError(ConstantesErrores.ERR_HAY_CONTRATOS_SIN_GENERAR);
                                    throw b;
                                }
                            }
                        }
                        for (ContratoOC contrato : proceso.getContratos()) {
                            if (contrato.getEstado().equals(EstadoContrato.PENDIENTE)) {

                                SsUsuario u = contrato.getAdministrador();
                                Notificacion n = new Notificacion();
                                n.setTipo(TipoNotificacion.INGRESO_ORDEN_INICIO_CONTRATO);
                                n.setContenido("El contrato de número " + contrato.getNroContrato() + " y año " + contrato.getNroAnio() + " tiene pendiente el ingreso de la orden de inicio.");
                                n.setUsuario(u);
                                n.setFecha(new Date());
                                n.setObjetoId(contrato.getId());
                                generalDAO.update(n);
                            }

                        }
                        break;
                    case CERRADO:
                        for (ContratoOC contrato : proceso.getContratos()) {
                            if (contrato.getEstado().equals(EstadoContrato.EN_CREACION_DENTRO_PROCESO) || contrato.getEstado().equals(EstadoContrato.PENDIENTE)) {
                                b = new BusinessException();
                                b.addError(ConstantesErrores.ERR_CONTRATO_SIN_INICIAR);
                                throw b;
                            }
                        }

                        break;
                    default:
                        b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_PROCESO_ESTADO_INVALIDO);
                        throw b;

                }
            }

            proceso.setEstado(nuevoEstado);
            return (ProcesoAdquisicion) generalDAO.update(proceso);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Elimina un insumo de proceso de adquisición, desasociándolo primero del
     * proceso de adquisición al que estaba asociado y del poInsumo.
     *
     * @param idProceso
     * @param idInsumo
     */
    public void eliminarInsumoProceso(int idProceso, int idInsumo) {
        try {
            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class, idProceso);
            estaDesiertoOSinEfecto(proceso);
            ProcesoAdquisicionInsumo insumo = (ProcesoAdquisicionInsumo) generalDAO.find(ProcesoAdquisicionInsumo.class, idInsumo);
            insumo.getPoInsumo().setRecepcionFisicaTDR(Boolean.FALSE);
            if (proceso.getInsumos().contains(insumo)) {
                proceso.setMontoTotal(proceso.getMontoTotal().subtract(insumo.getPoInsumo().getMontoTotal()));
                proceso.getInsumos().remove(insumo);
                insumo.setProcesoAdquisicion(null);

                if (insumo.getFechaContratacion() != null && insumo.getFechaContratacion().compareTo(proceso.getMenorFechaInsumo()) == 0) {
                    Date menorFecha = null;
                    for (ProcesoAdquisicionInsumo i : proceso.getInsumos()) {
                        if (menorFecha == null || insumo.getFechaContratacion().compareTo(menorFecha) < 0) {
                            menorFecha = insumo.getFechaContratacion();
                        }
                    }
                    proceso.setMenorFechaInsumo(menorFecha);
                }

                if (insumo.getRelItemInsumos() != null && !insumo.getRelItemInsumos().isEmpty()) {
                    BusinessException b = new BusinessException();
                    String[] values = {"Items", "al Insumo"};
                    b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, values);
                    throw b;
                }

                try {
                    POInsumos poinsumo = insumo.getPoInsumo();
                    poinsumo.setProcesoInsumo(null);
                    generalDAO.update(poinsumo);
                    generalDAO.delete(insumo);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA_SIN_PARAMETROS);
                    throw b;
                }
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve la lista de grupos de PAC de un determinado proceso de
     * adquisición
     *
     * @param proId
     * @return
     */
    public Map obtenerPacsYGruposDeInsumosdeProcesoAdquisicion(Integer proId) {
        List<PACGrupo> pacGrupos = pAdqDao.obtenerPacGrupoPorInsumosDelProceso(proId);
        Integer idPac = null;
        Map mapPacGrupo = new HashMap();
        List<PACGrupo> grupos = new ArrayList<>();

        for (PACGrupo pacGrupo : pacGrupos) {
            pacGrupo.getInsumos().isEmpty();
            if (idPac == null || !idPac.equals(pacGrupo.getPac().getId())) {
                if (idPac != null) {
                    mapPacGrupo.put(idPac, grupos);
                    grupos = new ArrayList<>();
                }
                idPac = pacGrupo.getPac().getId();
            }
            grupos.add(pacGrupo);

        }
        mapPacGrupo.put(idPac, grupos);
        return mapPacGrupo;
    }

    /**
     * Asocia al proceso de adquisición los PoInsumos seleccionados
     *
     * @param procAdq
     * @param insumosAAgregar
     * @param chequearMontos
     * @return
     */
    public Boolean agregarInsumosProceso(ProcesoAdquisicion procAdq, Set<POInsumos> insumosAAgregar, boolean chequearMontos) {
        try {
            ProcesoAdquisicion procesoAdqDB = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class, procAdq.getId());
            estaDesiertoOSinEfecto(procesoAdqDB);

            //de la lista a agregar elimina los insumos que ya estan en el proceso. Esto no tendria que pasar
            for (ProcesoAdquisicionInsumo padqI : procesoAdqDB.getInsumos()) {
                insumosAAgregar.remove(padqI.getPoInsumo());
            }

            //calcula la fecha minima y suma el monto de los insumos a agregar
            BigDecimal montoInsumos = BigDecimal.ZERO;
            Date fechaMinimaInsumo = null;
            for (POInsumos insumo : insumosAAgregar) {
                if (fechaMinimaInsumo == null || insumo.getFechaContratacion().after(fechaMinimaInsumo)) {
                    fechaMinimaInsumo = insumo.getFechaContratacion();
                }
                montoInsumos = montoInsumos.add(insumo.getMontoTotal());
            }
            //verifica el monto total del proceso
            BigDecimal montoProceso = procesoAdqDB.getMontoTotal();
            if (montoProceso == null) {
                montoProceso = BigDecimal.ZERO;
            }
            montoProceso = montoProceso.add(montoInsumos);

            //recorre los insumos para verificar si se pasa del método de adquisicion
            BigDecimal montoMetodoAdquisicion = BigDecimal.ZERO;
            if (procesoAdqDB.getMetodoAdquisicion() != null) {
                Integer anio = procesoAdqDB.getAnio().getAnio();
                MetodoAdquisicionRango rango = obtenerRangoPorAnio(anio, procesoAdqDB.getMetodoAdquisicion().getRangos());
                if (rango != null) {
                    montoMetodoAdquisicion = rango.getMontoMax();
                } else {
                    montoMetodoAdquisicion = BigDecimal.ZERO;
                }

                if (chequearMontos && montoMetodoAdquisicion.compareTo(montoProceso) < 0) {
                    return false;
                }
                //SETEA EL método EN ADQUISICION EN FALSE
                if (!chequearMontos && montoMetodoAdquisicion.compareTo(montoProceso) < 0) {
                    procesoAdqDB.setMetodoAdquisicion(null);
                    procesoAdqDB.setMayorFechaGantt(null);
                    procesoAdqDB.setMenorFechaGantt(null);
                    procesoAdqDB.setGantt(null);
                }
            }

            //le setea los insumos al proceso
            for (POInsumos poinsumo : insumosAAgregar) {
                POInsumos poInsumoBD = (POInsumos) generalDAO.find(POInsumos.class, poinsumo.getId());
                if (poInsumoBD.getProcesoInsumo() != null) {
                    String[] params = {poInsumoBD.getId().toString(), poInsumoBD.getProcesoInsumo().getProcesoAdquisicion().getNombre()};
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EL_INSUMO_0_YA_ESTA_ASOCIADO_AL_PROCESO_1, params);
                    throw b;
                }
                ProcesoAdquisicionInsumo padqInsumo = UtilsUACI.convertPOInsumosAProcesoAdquisicionInsumo(poinsumo);
                poinsumo.setProcesoInsumo(padqInsumo);
                poinsumo = (POInsumos) generalDAO.update(poinsumo);
                padqInsumo = poinsumo.getProcesoInsumo();
                padqInsumo.setPoInsumo(poinsumo);
                padqInsumo.setProcesoAdquisicion(procesoAdqDB);
                procesoAdqDB.getInsumos().add(padqInsumo);

            }
            //setea los nuevos insumos al proceso
            procesoAdqDB.setMontoTotal(montoProceso);

            //se tea la nueva fecha al proceso
            if (procesoAdqDB.getMenorFechaInsumo() == null || procesoAdqDB.getMenorFechaInsumo().after(fechaMinimaInsumo)) {
                procesoAdqDB.setMenorFechaInsumo(fechaMinimaInsumo);
            }

            generalDAO.update(procesoAdqDB);
            return true;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve la lista de Lotes de un proceso de adquisición
     *
     * @param proId
     * @return
     * @throws GeneralException
     */
    public List<ProcesoAdquisicionLote> obtenerLotesProcesoAdquisicion(Integer proId) throws GeneralException {
        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "procesoAdquisicion.id", proId);
        String[] orderBy = {"id"};
        boolean[] ascending = {true};
        try {
            List<ProcesoAdquisicionLote> lotes = generalDAO.findEntityByCriteria(ProcesoAdquisicionLote.class, criterio, orderBy, ascending, null, null);
            for (ProcesoAdquisicionLote lote : lotes) {
                List<ProcesoAdquisicionItem> items = lote.getItems();
                for (ProcesoAdquisicionItem item : items) {
                    //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                    List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();
                    insumosDelItem.isEmpty();
                    ProcesoAdquisicionItemProvOferta oferta = item.getProveedorOfertaAdjudicadaId();
                    if (oferta != null) {
                        oferta.getOfertasPorInsumo().isEmpty();
                    }

                }
            }

            return lotes;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Crea, actualiza o elimina los lotes e ítems manipulados desde la gestión
     * de lotes e ítems y los asocia o desasocia del proceso de adquisición,
     * según corresponda.
     *
     * @param porcesoTemp
     * @param items
     * @param lotes
     * @param itemsAEliminar
     * @param lotesAEliminar
     * @param relacionesItemsInsumosEliminadas
     * @return
     * @throws GeneralException
     */
    public ProcesoAdquisicion guardarLoteItem(ProcesoAdquisicion porcesoTemp, List<ProcesoAdquisicionItem> items, List<ProcesoAdquisicionLote> lotes, List<ProcesoAdquisicionItem> itemsAEliminar, List<ProcesoAdquisicionLote> lotesAEliminar, List<RelacionProAdqItemInsumo> relacionesItemsInsumosEliminadas) throws GeneralException {
        try {

            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class, porcesoTemp.getId());
            estaDesiertoOSinEfecto(proceso);

            /**
             * fixeme: esto tendría que venir bien de arriba. pero se arregla la
             * estructura por las dudas
             */
            List<ProcesoAdquisicionItem> itemsAGuardar = new LinkedList<>();
            for (ProcesoAdquisicionItem item : items) {
                //setea las relaciones
                item.setProcesoAdquisicion(porcesoTemp);
                item.setLote(null);
                for (ProcesoAdquisicionLote lote : lotes) {
                    lote.getItems().remove(item);
                }
                for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                    rel.setItem(item);
                }
                item = (ProcesoAdquisicionItem) generalDAO.update(item);
                itemsAGuardar.add(item);
            }
            List<ProcesoAdquisicionLote> lotesAGuardar = new LinkedList<>();
            for (ProcesoAdquisicionLote lote : lotes) {
                lote.setProcesoAdquisicion(porcesoTemp);
                for (ProcesoAdquisicionItem item : lote.getItems()) {
                    item.setProcesoAdquisicion(null);
                    item.setLote(lote);
                    for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                        rel.setItem(item);
                    }
                }
                lote = (ProcesoAdquisicionLote) generalDAO.update(lote);
                lotesAGuardar.add(lote);
            }

            /**
             * se guardan los nuevos lotes y se arreglan las relaciones con el
             * tema del insumo
             */
            for (ProcesoAdquisicionInsumo insumo : proceso.getInsumos()) {
                insumo.getRelItemInsumos().clear();
            }

            proceso.getItems().clear();
            for (ProcesoAdquisicionItem item : itemsAGuardar) {
                proceso.getItems().add(item);
                for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                    rel.getInsumo().getRelItemInsumos().add(rel);
                }
            }
            proceso.getLotes().clear();
            for (ProcesoAdquisicionLote lote : lotesAGuardar) {
                proceso.getLotes().add(lote);
                for (ProcesoAdquisicionItem item : lote.getItems()) {
                    for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                        rel.getInsumo().getRelItemInsumos().add(rel);
                    }
                }
            }

            for (ProcesoAdquisicionItem itemEliminar : itemsAEliminar) {
                itemEliminar.setProcesoAdquisicion(null);
                generalDAO.update(itemEliminar);
            }

            return proceso;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Compara el contenido de un lote que está en memoria con el contenido del
     * mismo lote que está guardado en la base.
     *
     * @param lote
     * @param loteDB
     * @return
     */
    private Boolean comparadorLote(ProcesoAdquisicionLote lote, ProcesoAdquisicionLote loteDB) {
        /*
         si son iguales devuvle true
         */
        if (!lote.getNombre().equals(loteDB.getNombre())) {
            return false;
        }
        if (lote.getItems() != null) {
            if (loteDB.getItems() == null) {
                return false;
            } else {
                if (lote.getItems().size() != loteDB.getItems().size()) {
                    return false;
                } else {
                    for (ProcesoAdquisicionItem item : lote.getItems()) {
                        Iterator<ProcesoAdquisicionItem> itItemDB = loteDB.getItems().iterator();
                        ProcesoAdquisicionItem itemDBCompare = null;
                        while (itItemDB.hasNext() && itemDBCompare == null) {
                            ProcesoAdquisicionItem itemDB = itItemDB.next();
                            if (itemDB.getId().equals(item.getId())) {
                                itemDBCompare = itemDB;
                            }

                        }
                        if (itemDBCompare == null) {
                            //no se encontró el insumo
                            return false;
                        } else {
                            //se encontro el insumo a comparar
                            return comparadorItem(item, itemDBCompare);
                        }

                    }
                }
            }
        } else {
            if (loteDB.getItems() != null) {
                return false;
            } else {
                return true;
            }
        }

        return false;

    }

    /**
     * Compara el contenido de un ítem que está en memoria con el contenido del
     * mismo ítem que está guardado en la base.
     *
     * @param item
     * @param itemDB
     * @return
     */
    private Boolean comparadorItem(ProcesoAdquisicionItem item, ProcesoAdquisicionItem itemDB) {
        /*
         si son iguales devuvle true
         */
        if (!item.getNombre().equals(itemDB.getNombre())) {
            return false;
        }
        if (!item.getNroItem().equals(itemDB.getNroItem())) {
            return false;
        }

        if (item.getLote() == null) {
            if (itemDB.getLote() != null) {
                return false;
            }

        }
        //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
        List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();

        if (insumosDelItem != null) {
            //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
            List<ProcesoAdquisicionInsumo> insumosDelItemDB = itemDB.getInsumosTemporalesDelItem();//pAdqDao.obtenerInsumosDelItem(itemDB.getId());
            if (insumosDelItemDB == null) {
                return false;
            } else {
                if (insumosDelItem.size() != insumosDelItemDB.size()) {
                    return false;
                } else {
                    for (ProcesoAdquisicionInsumo insumo : insumosDelItem) {
                        Iterator<ProcesoAdquisicionInsumo> itInsDB = insumosDelItemDB.iterator();
                        ProcesoAdquisicionInsumo insumoDBCompare = null;
                        while (itInsDB.hasNext() && insumoDBCompare == null) {
                            ProcesoAdquisicionInsumo insumoDB = itInsDB.next();
                            if (insumoDB.getId().equals(insumo.getId())) {
                                insumoDBCompare = insumoDB;
                            }

                        }
                        if (insumoDBCompare == null) {
                            //no se encontró el insumo
                            return false;
                        } else {
                            //se encontro el insumo a comparar
                            return comparadorInsumo(insumo, insumoDBCompare);
                        }

                    }
                }
            }
        } else {
            return true;
        }
        return false;

    }

    /**
     * Compara el contenido de un insumo que está en memoria con el contenido
     * del mismo insumo que está guardado en la base.
     *
     * @param insumo
     * @param insumoDB
     * @return
     */
    private boolean comparadorInsumo(ProcesoAdquisicionInsumo insumo, ProcesoAdquisicionInsumo insumoDB) {
        //retorna true si son iguales
        boolean igual = insumo.getCantidadAdjudicada().equals(insumoDB.getCantidadAdjudicada());
        igual = igual && insumo.getInsumo().equals(insumoDB.getInsumo());
        igual = igual && insumo.getMontoTotalAdjudicado().equals(insumoDB.getMontoTotalAdjudicado());
        igual = igual && insumo.getPoInsumo().equals(insumoDB.getPoInsumo());

        return igual;
    }

    /**
     * Devuelve la lista de proveedores no asociados a un proceso
     *
     * @param proId
     * @return
     */
    public List<Proveedor> obtenerProveedoresNoAsignadosAlproceso(Integer proId) {
        return pAdqDao.obtenerProveedoresNoAsociadosAlProceso(proId);
    }

    /**
     * Asocia proveedores a un proceso de adquisición
     *
     * @param proceso
     * @param proveedores
     * @param eliminarProveedores
     * @throws GeneralException
     */
    public void guardarProveedores(ProcesoAdquisicion proceso, List<ProcesoAdquisicionProveedor> proveedores, List<ProcesoAdquisicionProveedor> eliminarProveedores) throws GeneralException {
        try {
            estaDesiertoOSinEfecto(proceso);
            for (ProcesoAdquisicionProveedor proveedor : proveedores) {
                proveedor.setProcesoAdquisicion(proceso);
                if (proveedor.getTempUploadedFile() != null) {
                    if (proveedor.getArchivo() == null) {
                        proveedor.setArchivo(archivoBean.crearArchivo());
                    }
                    archivoBean.asociarArchivo(proveedor.getArchivo(), proveedor.getTempUploadedFile(), false);

                }
                if (proveedor.getId() == null) {
                    proveedor.setFechaAsociacion(new Date());
                }
            }
            for (ProcesoAdquisicionProveedor eliminarProveedor : eliminarProveedores) {
                //Si el estado del proceso es Recepción de Ofertas y el proveedor a eliminar está invitado, no se podrá eliminar
                if (eliminarProveedor.getInvitado()
                        && proceso.getEstado() == PasosProcesoAdquisicion.RECEPCION_OFERTAS) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_NO_SE_PUEDE_ELIMINAR_PROVEEDOR_INVITADO);
                    throw b;
                }
                ProcesoAdquisicionProveedor delProveedor = (ProcesoAdquisicionProveedor) generalDAO.find(ProcesoAdquisicionProveedor.class, eliminarProveedor.getId());
                proceso.getProveedores().remove(delProveedor);
                delProveedor.setProcesoAdquisicion(null);

            }
            proceso.setProveedores(proveedores);

            generalDAO.update(proceso);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve la lista de proveedores no asociados a un proceso
     *
     * @param proId
     * @return
     * @throws GeneralException
     */
    public List<Proveedor> obtenerProveedoresDelProceso(Integer proId) throws GeneralException {
        List<Proveedor> proveedores = pAdqDao.obtenerProveedoresAsociadosAlProceso(proId);
        return proveedores;
    }

    /**
     * Crea o actualiza una oferta de un proceso de adquisición
     *
     * @param oferta
     * @param idItem
     * @return
     * @throws GeneralException
     */
    public ProcesoAdquisicionItemProvOferta agregarEditarOfertaItem(ProcesoAdquisicionItemProvOferta oferta, Integer idItem) throws GeneralException {
        try {
            BusinessException b = new BusinessException();
            if (oferta.getOfertasPorInsumo() == null || oferta.getOfertasPorInsumo().isEmpty()) {
                b.addError(ConstantesErrores.ERROR_NO_HAY_OFERTAS_POR_INSUMOS);
            }

            if (oferta.getProcesoAdquisicionProveedor() == null) {
                b.addError(ConstantesErrores.ERROR_DEBE_ASOCIAR_PROVEEDOR_OFERTA);
            }

            if (oferta.getPrecioUnitOferta().equals(BigDecimal.ZERO)) {
                b.addError(ConstantesErrores.ERROR_DEBE_AGREGAR_PRECIO_UNITARIO);
            }

            if (!b.getErrores().isEmpty()) {
                throw b;
            }

            if (oferta.getId() == null) {
                ProcesoAdquisicionItem item = (ProcesoAdquisicionItem) generalDAO.find(ProcesoAdquisicionItem.class, idItem);
                oferta.setProcesoAdquisicionItem(item);
                if (item.getOfertasProvedor() == null) {
                    item.setOfertasProvedor(new LinkedList<ProcesoAdquisicionItemProvOferta>());
                }
                item.getOfertasProvedor().add(oferta);
            } else {
                oferta = (ProcesoAdquisicionItemProvOferta) generalDAO.update(oferta);
            }

            oferta.getOfertasPorInsumo().isEmpty();
            return oferta;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Elimina una oferta de un proceso de adquisición
     *
     * @param eliminarOferta
     * @throws GeneralException
     */
    public void eliminarOfertaItem(ProcesoAdquisicionItemProvOferta eliminarOferta) throws GeneralException {
        try {
            ProcesoAdquisicionItemProvOferta ofertaAEliminar = (ProcesoAdquisicionItemProvOferta) generalDAO.find(ProcesoAdquisicionItemProvOferta.class, eliminarOferta.getId());
            ProcesoAdquisicionItem item = (ProcesoAdquisicionItem) generalDAO.find(ProcesoAdquisicionItem.class, ofertaAEliminar.getProcesoAdquisicionItem().getId());

            if (item.getProveedorOfertaAdjudicadaId() != null && item.getProveedorOfertaAdjudicadaId().equals(ofertaAEliminar.getId())) {
                item.setProveedorOfertaAdjudicadaId(null);
                item.setEstado(null);
            }
            item.getOfertasProvedor().remove(ofertaAEliminar);
            ofertaAEliminar.setProcesoAdquisicionItem(null);
            generalDAO.delete(ofertaAEliminar);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Devuelve las ofertas asociadas a un ítem de un proceso de adquisición
     *
     * @param proItemId
     * @return
     * @throws GeneralException
     */
    public List<ProcesoAdquisicionItemProvOferta> obtenerOfertasDelItem(Integer proItemId) throws GeneralException {
        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "procesoAdquisicionItem.id", proItemId);
        String[] orderBy = {"id"};
        boolean[] ascending = {true};
        try {
            List<ProcesoAdquisicionItemProvOferta> ofertas = generalDAO.findEntityByCriteria(ProcesoAdquisicionItemProvOferta.class, criterio, orderBy, ascending, null, null);
            for (ProcesoAdquisicionItemProvOferta oferta : ofertas) {
                oferta.getOfertasPorInsumo().isEmpty();
                oferta.calcularPrecioTotal();
            }
            return ofertas;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Devuelve las ofertas asociadas a un ítem de un proceso de adquisición
     *
     * @param proItemId
     * @return
     * @throws GeneralException
     */
    public List<ProcesoAdquisicionItemProvOferta> obtenerOfertasDelItemV2(Integer proItemId) throws GeneralException {

        try {
            Date d1 = new Date();
            List<ProcesoAdquisicionItemProvOferta> ofertas = pAdqDao.obtenerOfertasPorItemId(proItemId);
            for (ProcesoAdquisicionItemProvOferta oferta : ofertas) {
                oferta.getOfertasPorInsumo().isEmpty();
                oferta.calcularPrecioTotal();
            }
            Date d2 = new Date();
            return ofertas;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Devuelve un proveedor que esta asociado a un proceso de adquisición
     *
     * @param proveedorId
     * @param procesoId
     * @return
     */
    public ProcesoAdquisicionProveedor obtenerProcesoAdquisicionProveedorPorProveedorIdyProcesoId(Integer proveedorId, Integer procesoId) {

        MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "proveedor.id", proveedorId);
        MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "procesoAdquisicion.id", procesoId);

        AND_TO andCriterio = CriteriaTOUtils.createANDTO(criterio1, criterio2);

        String[] orderBy = {"id"};
        boolean[] ascending = {true};
        try {
            List<ProcesoAdquisicionProveedor> proveedores = generalDAO.findEntityByCriteria(ProcesoAdquisicionProveedor.class, andCriterio, orderBy, ascending, null, null);
            return proveedores.get(0);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve la lista de proveedores asociados a un proceso de adquisición
     *
     * @param proId
     * @return
     * @throws GeneralException
     */
    public List<ProcesoAdquisicionProveedor> obtenerProveedoresAdquisicionDelProceso(Integer proId) throws GeneralException {
        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "procesoAdquisicion.id", proId);
        String[] orderBy = {"proveedor.nombreComercial"};
        boolean[] ascending = {true};
        try {
            List<ProcesoAdquisicionProveedor> proveedores = generalDAO.findEntityByCriteria(ProcesoAdquisicionProveedor.class, criterio, orderBy, ascending, null, null);

            return proveedores;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Actualiza el estado de un ítem. En caso de una adjudicación, asocia una
     * oferta a un ítem de un proceso de adquisición, actualizando las
     * cantidades y montos adjudicados de los insumos que componen dicho ítem.
     * En caso de ser una declaración de estado desierto o sin efecto, se
     * liberan los poInsumos para poder ser utilizados en otros procesos de
     * adquisición y se genera una notificación a los usuarios de las UT
     * correspondientes
     *
     * @param item
     * @param selectOferta
     * @param estadoItem
     * @param idProceso
     * @return
     * @throws GeneralException
     */
    public ProcesoAdquisicion asignarItem(ProcesoAdquisicionItem item, ProcesoAdquisicionItemProvOferta selectOferta, EstadoItem estadoItem, Integer idProceso) throws GeneralException {
        try {
            ProcesoAdquisicion procesoBD = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class, idProceso);

            ProcesoAdquisicionItem itemDB = null;
            Iterator<ProcesoAdquisicionItem> itItems = procesoBD.getItems().iterator();
            while (itItems.hasNext() && itemDB == null) {
                ProcesoAdquisicionItem itemBusqueda = itItems.next();
                if (itemBusqueda.getId().equals(item.getId())) {
                    itemDB = itemBusqueda;
                }
            }
            //Si no está en los items lo buscó en los lotes
            if (itemDB == null) {
                Iterator<ProcesoAdquisicionLote> itLotes = procesoBD.getLotes().iterator();
                while (itLotes.hasNext() && itemDB == null) {
                    Iterator<ProcesoAdquisicionItem> itLoteItems = itLotes.next().getItems().iterator();
                    while (itLoteItems.hasNext() && itemDB == null) {
                        ProcesoAdquisicionItem itemBusqueda = itLoteItems.next();
                        if (itemBusqueda.getId().equals(item.getId())) {
                            itemDB = itemBusqueda;
                        }
                    }
                }
            }

            itemDB.setEstado(estadoItem);
            if (selectOferta != null) {
                itemDB.setProveedorOfertaAdjudicadaId(selectOferta);
                if (estadoItem.equals(EstadoItem.PAUSA)) {
                    Configuracion conf = confBean.obtenerCnfPorCodigo(ConstantesConfiguracion.PLAZO_PAUZA_ITEM);
                } else if (estadoItem.equals(EstadoItem.ADJUDICADO)) {
                    //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                    List<ProcesoAdquisicionInsumo> insumosDelItem = itemDB.getInsumosTemporalesDelItem();
                    for (ProcesoAdquisicionInsumo procesoInsumo : insumosDelItem) {
                        Iterator<ProcesoAdquisicionItemOfertaInsumo> it = selectOferta.getOfertasPorInsumo().iterator();
                        ProcesoAdquisicionItemOfertaInsumo ofertaInsumo = null;
                        while (it.hasNext() && ofertaInsumo == null) {
                            ProcesoAdquisicionItemOfertaInsumo next = it.next();
                            if (next.getProcesoAdqInsumo().getId().equals(procesoInsumo.getId())) {
                                ofertaInsumo = next;
                            }
                        }
                        if (ofertaInsumo != null) {
                            Integer cantidadAdjAnterior = procesoInsumo.getCantidadAdjudicada() != null ? procesoInsumo.getCantidadAdjudicada() : 0;
                            procesoInsumo.setCantidadAdjudicada(cantidadAdjAnterior + ofertaInsumo.getCantidadOferta());
                            BigDecimal montoAdjAnterior = procesoInsumo.getMontoTotalAdjudicado() != null ? procesoInsumo.getMontoTotalAdjudicado() : BigDecimal.ZERO;
                            procesoInsumo.setMontoTotalAdjudicado(montoAdjAnterior.add(ofertaInsumo.getMontoOferta()));
                            RelacionProAdqItemInsumo rel = procesoInsumo.getRelacionItemInsumoPorItem(itemDB);
                            rel.setCantidadAdjudicada(ofertaInsumo.getCantidadOferta());
                            rel.setMontoUnitAdjudicado(selectOferta.getPrecioUnitOferta());
                            rel.setMontoTotalAdjudicado(ofertaInsumo.getMontoOferta());
                        }
                    }
                }
            } else {
                itemDB.setProveedorOfertaAdjudicadaId(null);
                if (estadoItem.equals(EstadoItem.DESIERTO) || estadoItem.equals(EstadoItem.SIN_EFECTO)) {
                    List<GeneralPOA> poasNotificar = new ArrayList<>();
                    //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                    List<ProcesoAdquisicionInsumo> insumosDelItem = itemDB.getInsumosTemporalesDelItem();
                    for (ProcesoAdquisicionInsumo insumoProceso : insumosDelItem) {
                        if (UtilsUACI.insumoQuedaLibreParaOtroProceso(insumoProceso)) {
                            POInsumos poinsumo = insumoProceso.getPoInsumo();
                            poinsumo.setProcesoInsumo(null);
                            generalDAO.update(poinsumo);
                            GeneralPOA poa = poinsumo.getPoa();
                            if (!poasNotificar.contains(poa)) {
                                poasNotificar.add(poa);
                            }
                        }
                        for (GeneralPOA poa : poasNotificar) {
                            notificarUTItemDesiertoSinEfecto(insumosDelItem, poa, estadoItem);
                        }
                    }
                }

            }

            return procesoBD;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Adjudica un ítem sin compromiso presupuestario. Adjudica un ítem sin
     * compromiso presupuestario mientras cumpla las siguientes condiciones:
     * Todos los insumos tengan asociados proyectos administrativos, de acción
     * central, o de asignación no programable. El precio total de los
     * insumos(precio unitario por suma de cantidades solicitadas) sea igual a
     * la suma de montos totales de las ofertas de los insumos. Para cada
     * insumo, cada monto total de oferta debe ser menor o igual que el monto
     * certificado
     *
     * @param item
     * @throws GeneralException
     */
    public ProcesoAdquisicionItemProvOferta adjudicarItemSinCompromisoPresupuestario(ProcesoAdquisicionItemProvOferta oferta) throws GeneralException {
        try {

            ProcesoAdquisicionItem itemDB = (ProcesoAdquisicionItem) generalDAO.find(ProcesoAdquisicionItem.class, oferta.getProcesoAdquisicionItem().getId());
            List<ProcesoAdquisicionInsumo> insumosDelItem = itemDB.getInsumosTemporalesDelItem();
            Iterator<ProcesoAdquisicionInsumo> it = insumosDelItem.iterator();
            boolean insumosConProyectosApropiados = true;
            while (it.hasNext() && insumosConProyectosApropiados) {
                ProcesoAdquisicionInsumo insumoDelItem = it.next();
                if (insumoDelItem.getPoInsumo().getPoa().getTipo() == TipoPOA.POA_PROYECTO) {
                    POAProyecto poaProyecto = (POAProyecto) insumoDelItem.getPoInsumo().getPoa();
                    //Busco por los dos que no puede ser: de inversión y de no inversión
                    if (poaProyecto.getProyecto().getTipoProyecto().equals(TipoProyecto.INVERSION)
                            || (poaProyecto.getProyecto().getTipoProyecto().equals(TipoProyecto.NO_INVERSION))) {
                        insumosConProyectosApropiados = false;
                    }
                } else {
                    insumosConProyectosApropiados = false;
                }
            }

            //Sumo los montos de las ofertas de los insumos del item (pueden haber sido editados)
            BigDecimal montoTotalInsumosOferta = oferta.calcularPrecioTotal();
            // Calculo el precio unitario del item por la cantidad de insumos asociados al item
            BigDecimal precioUnitProveedor = oferta.getPrecioUnitOferta();
            Integer cantidadTotalInsumosDelItem = 0;
            for (ProcesoAdquisicionItemOfertaInsumo ofertaInsumo : oferta.getOfertasPorInsumo()) {
                Integer cantidadPorInsumo = ofertaInsumo.getProcesoAdqInsumo().getRelacionItemInsumoPorItem(itemDB).getCantidad();
                cantidadTotalInsumosDelItem += cantidadPorInsumo;
            }
            BigDecimal precioTotalProveedor = precioUnitProveedor.multiply(new BigDecimal(cantidadTotalInsumosDelItem));
            /**
             * Si la suma de montos totales de las ofertas de los insumo es
             * diferente al precio total calculado por el precio unitario de
             * cada insumo, entonces hay error
             */
            if (montoTotalInsumosOferta.compareTo(precioTotalProveedor) != 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_MONTO_TOTAL_ASIGNADO_PARA_INSUMOS_DISTINTO_A_PRECIO_TOTAL_PROVEEDOR);
                throw b;
            }

            //Valido que el total de la oferta no sea mayor al monto certificado restante (es restante porque quizas ya se utilizó algo en la adjudicación del insumo en otro item)
            Iterator<ProcesoAdquisicionItemOfertaInsumo> itOfertasPorInsumo = oferta.getOfertasPorInsumo().iterator();
            boolean hayError = false;
            while (itOfertasPorInsumo.hasNext() && !hayError) {
                ProcesoAdquisicionItemOfertaInsumo ofertaInsumo = itOfertasPorInsumo.next();
                BigDecimal montoRestanteCertificado = calcularMontoTotalCertificadoRestanteParaInsumo(ofertaInsumo.getProcesoAdqInsumo());
                if (montoRestanteCertificado.compareTo(ofertaInsumo.getMontoOferta()) < 0) {
                    hayError = true;
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_MONTOS_ASIGNADOS_PARA_INSUMOS_MENOR_A_PRECIO_PROVEEDOR);
                    throw b;
                }
            }

            for (ProcesoAdquisicionInsumo procesoInsumo : insumosDelItem) {
                Iterator<ProcesoAdquisicionItemOfertaInsumo> itOfertas = oferta.getOfertasPorInsumo().iterator();
                ProcesoAdquisicionItemOfertaInsumo ofertaInsumo = null;
                while (itOfertas.hasNext() && ofertaInsumo == null) {
                    ProcesoAdquisicionItemOfertaInsumo next = itOfertas.next();
                    if (next.getProcesoAdqInsumo().getId().equals(procesoInsumo.getId())) {
                        ofertaInsumo = next;
                    }
                }
                if (ofertaInsumo != null) {
                    Integer cantidadAdjAnterior = procesoInsumo.getCantidadAdjudicada() != null ? procesoInsumo.getCantidadAdjudicada() : 0;
                    procesoInsumo.setCantidadAdjudicada(cantidadAdjAnterior + ofertaInsumo.getCantidadOferta());
                    BigDecimal montoAdjAnterior = procesoInsumo.getMontoTotalAdjudicado() != null ? procesoInsumo.getMontoTotalAdjudicado() : BigDecimal.ZERO;
                    procesoInsumo.setMontoTotalAdjudicado(montoAdjAnterior.add(ofertaInsumo.getMontoOferta()));
                    procesoInsumo.getRelacionItemInsumoPorItem(itemDB).setCantidadAdjudicada(ofertaInsumo.getCantidadOferta());
                    procesoInsumo.getRelacionItemInsumoPorItem(itemDB).setMontoUnitAdjudicado(oferta.getPrecioUnitOferta());
                    procesoInsumo.getRelacionItemInsumoPorItem(itemDB).setMontoTotalAdjudicado(ofertaInsumo.getMontoOferta());
                    generalDAO.update(procesoInsumo);
                }
            }

            itemDB.setEstado(EstadoItem.ADJUDICADO);
            itemDB.setProveedorOfertaAdjudicadaId(oferta);
            oferta = (ProcesoAdquisicionItemProvOferta) generalDAO.update(oferta);
            return oferta;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Notifica a los usuarios de una UT sobre la puesta en pausa de un ítem de
     * un proceso de adquisición
     *
     * @param insumo
     * @param fechaPlazo
     * @throws DAOGeneralException
     */
    private void notificarUTItemPausa(POInsumos insumo, Date fechaPlazo) throws DAOGeneralException {
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaStr = formatoFecha.format(fechaPlazo);
        List<SsUsuario> anotificar = usuarioDAO.getUsuariosConUT(insumo.getPoa().getUnidadTecnica().getId(), null);
        for (SsUsuario u : anotificar) {
            NotificacionPOA n = new NotificacionPOA();
            n.setTipo(TipoNotificacion.PROCESO_ADQUISICION_EN_PAUSA);
            //Unidad Técnica solicitándole que reduzca la cantidad o aumente el presupuesto asignado
            String notificacion = "Por favor reduzca la cantidad o aumente el presupuesto asignado para el insumo " + insumo.getId() + "-" + insumo.getInsumo().getNombre() + ", antes de la fecha " + fechaStr;
            n.setContenido(notificacion);
            n.setUsuario(u);
            n.setFecha(new Date());
            n.setObjetoId(insumo.getPoa().getId());
            if (insumo.getPoa().getTipo() == TipoPOA.POA_PROYECTO) {
                POAProyecto poaProyecto = (POAProyecto) insumo.getPoa();
                n.setPoaAnio(poaProyecto.getAnioFiscal().getId());
                n.setObjetoId(poaProyecto.getProyecto().getId());
            } else {
                POAConActividades poaActividades = (POAConActividades) insumo.getPoa();
                n.setPoaAnio(poaActividades.getAnioFiscal().getId());
                n.setObjetoId(poaActividades.getConMontoPorAnio().getId());
            }

            n.setPoaUT(insumo.getPoa().getUnidadTecnica().getId());

            generalDAO.update(n);
        }

    }

    /**
     * Elimina el estado de un ítem de un proceso de adquisición. En caso que el
     * estado anterior fuera desierto o sin efecto, vuelve a asociar los
     * poInsumo correspondientes a los insumos del ítem. En caso que el estado
     * anterior fuera adjudicado, resetea las cantidades y montos adjudicados
     * establecidas en los insumos del ítem y en el proceso de adquisición
     *
     * @param IdItem
     * @return
     * @throws GeneralException
     */
    public ProcesoAdquisicionItem deshacerEstadoItem(Integer IdItem) throws GeneralException {
        try {
            ProcesoAdquisicionItem itemBD = (ProcesoAdquisicionItem) generalDAO.find(ProcesoAdquisicionItem.class, IdItem);
            ProcesoAdquisicion proceso = null;
            if (itemBD.getLote() != null) {
                proceso = itemBD.getLote().getProcesoAdquisicion();

            } else {
                proceso = itemBD.getProcesoAdquisicion();
            }

            if (itemBD.getContrato() != null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXITE_CONTRATO_ASOCIADO_AL_ITEM);
                throw b;
            }

            if (itemBD.getEstado().equals(EstadoItem.DESIERTO) || itemBD.getEstado().equals(EstadoItem.SIN_EFECTO)) {
                /*
                 Si el estado era desierto o sin efecto, debemos volver asociar el poinsumo al insumo del proceso, 
                 si es que el po insumo ya no está participando en otro proceso
                 */
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                for (RelacionProAdqItemInsumo rel : itemBD.getRelItemInsumos()) {
                    ProcesoAdquisicionInsumo insumo = rel.getInsumo();
                    POInsumos poInsumo = insumo.getPoInsumo();
                    if (pAdqDao.poInsumoEstaEnOtroInsumoProceso(poInsumo.getId(), rel.getInsumo().getId())) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_INSUMO_DE_ITEM_ASOCIADO_OTRO_PROCESO);
                        throw b;
                    }
                    poInsumo.setProcesoInsumo(insumo);
                }
            } else if (itemBD.getEstado().equals(EstadoItem.ADJUDICADO)) {
                for (RelacionProAdqItemInsumo rel : itemBD.getRelItemInsumos()) {
                    ProcesoAdquisicionInsumo insumo = rel.getInsumo();
                    Integer cantidadActualAdjudicada = insumo.getCantidadAdjudicada();
                    Integer cantidadAdjudicadaRestar = insumo.getRelacionItemInsumoPorItem(itemBD).getCantidadAdjudicada();
                    insumo.setCantidadAdjudicada(cantidadActualAdjudicada - cantidadAdjudicadaRestar);
                    BigDecimal montoActualAdjudicado = insumo.getMontoTotalAdjudicado();
                    BigDecimal montoAdjudicadoRestar = rel.getMontoTotalAdjudicado();
                    insumo.setMontoTotalAdjudicado(montoActualAdjudicado.subtract(montoAdjudicadoRestar));
                    rel.setCantidadAdjudicada(0);
                    rel.setMontoUnitAdjudicado(new BigDecimal(BigInteger.ZERO));
                    rel.setMontoTotalAdjudicado(new BigDecimal(BigInteger.ZERO));
                }

            }
            itemBD.setEstado(null);
            itemBD.setProveedorOfertaAdjudicadaId(null);

            List<ProcesoAdquisicionItem> itemsProceso = pAdqDao.obtenerItemsDelProceso(proceso.getId());

            if (proceso.getEstadoProceso() != null && proceso.getEstadoProceso().equals(EstadoProcesoAdq.PAUSA)) {
                Boolean itemPausa = false;
                for (ProcesoAdquisicionItem itemProceso : itemsProceso) {
                    if (itemProceso.getEstado() != null && itemProceso.getEstado().equals(EstadoItem.PAUSA)) {
                        itemPausa = true;
                        break;
                    }
                }
                if (!itemPausa) {
                    proceso.setEstadoProceso(EstadoProcesoAdq.NORMAL);
                }
            }

            return itemBD;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Asocia números de contrato a un proceso de adquisición
     *
     * @param proId
     * @param cantidadNros
     * @throws GeneralException
     */
    public void reservarNumerosContrato(Integer proId, Integer cantidadNros) throws GeneralException {
        try {
            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.findById(ProcesoAdquisicion.class, proId);
            estaDesiertoOSinEfecto(proceso);
            generalDAO.lock(SsLock.class, ConstantesEstandares.LOCK_SECUENCIA_CONTRATO_ORDEN_COMPRA);
            List<SecuenciaContratoOrden> listaSecuencia = generalDAO.findByOneProperty(SecuenciaContratoOrden.class, "anio", proceso.getAnio().getAnio());
            SecuenciaContratoOrden secuencia = null;
            if (!listaSecuencia.isEmpty()) {

                secuencia = listaSecuencia.get(0);
            } else {

                secuencia = new SecuenciaContratoOrden();
                secuencia.setAnio(proceso.getAnio().getAnio());
                secuencia.setUltimoGenerado(0);
            }

            proceso.setReservaNroContratoInicial(secuencia.getUltimoGenerado() + 1);
            if (cantidadNros > 1) {
                proceso.setReservaNroContratoFinal(secuencia.getUltimoGenerado() + cantidadNros);
            } else {
                proceso.setReservaNroContratoFinal(secuencia.getUltimoGenerado() + 1);
            }

            secuencia.setUltimoGenerado(secuencia.getUltimoGenerado() + cantidadNros);

            generalDAO.update(secuencia);
            generalDAO.update(proceso);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve los ítems de un proceso de adquisición
     *
     * @param proId
     * @return
     */
    public List<ProcesoAdquisicionItem> obtenerTodosLosItemsDelProceso(Integer proId) {
        return (List<ProcesoAdquisicionItem>) pAdqDao.obtenerItemsDelProceso(proId);
    }

    /**
     * Devuelve una lista con los ítems de un proceso de adquisición que están
     * adjudicados.
     *
     * @param proId
     * @return
     */
    public List<ProcesoAdquisicionItem> obtenerItemsDelProcesoAdjudicados(Integer proId) {
        return (List<ProcesoAdquisicionItem>) pAdqDao.obtenerItemsDelProcesoAdjudicados(proId);
    }

    /**
     * Devuelve los lotes con ítems adjudicados en un proceso de adquisición.
     *
     * @param proId
     * @return
     */
    public List<ProcesoAdquisicionLote> obtenerLotesConItemsAdjudicados(Integer proId) {
        return (List<ProcesoAdquisicionLote>) pAdqDao.obtenerLotesConItemsAdjudicados(proId);
    }

    /**
     * Devuelve todos los ítems que están asociados a una oferta adjudicada pero
     * que aún no se ha registrado el contrato
     *
     * @param proId
     * @return
     */
    public Map obtenerTodosLosItemsDelProcesoConOfertaGanadoraSinContratoAsigando(Integer proId) {
        Map mapItemOfertaGanadora = new HashMap<ProcesoAdquisicionItem, ProcesoAdquisicionItemProvOferta>();
        List<ProcesoAdquisicionItem> items = (List<ProcesoAdquisicionItem>) pAdqDao.obtenerItemsDelProcesoSinContrato(proId);
        for (ProcesoAdquisicionItem item : items) {
            item.getOfertasProvedor().isEmpty();
            ProcesoAdquisicionItemProvOferta oferta = item.getProveedorOfertaAdjudicadaId();
            if (oferta != null) {
                oferta.getOfertasPorInsumo().isEmpty();
            }

            mapItemOfertaGanadora.put(item, item.getProveedorOfertaAdjudicadaId());
        }
        return mapItemOfertaGanadora;
    }

    /**
     * Devuelve los proveedores adjudicados en un proceso de adquisición.
     *
     * @param proId
     * @return
     */
    public List<ProcesoAdquisicionProveedor> obtenerProveedoresGanadoresItemProceso(Integer proId) {
        return (List<ProcesoAdquisicionProveedor>) pAdqDao.obtenerProveedoresGanadoresItemProceso(proId);
    }

    /**
     * Devuelve los contratos asociados a un proceso de adquisición
     *
     * @param proId
     * @return
     * @throws GeneralException
     */
    public List<ContratoOC> obtenerContratos(Integer proId) throws GeneralException {
        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "procesoAdquisicion.id", proId);
        String[] orderBy = {"id"};
        boolean[] ascending = {true};
        try {
            List<ContratoOC> contratos = generalDAO.findEntityByCriteria(ContratoOC.class, criterio, orderBy, ascending, null, null);
            for (ContratoOC contrato : contratos) {
                contrato.getImpuestos().isEmpty();
                for (FlujoCajaAnio fc : contrato.getProgramacionPagosContrato()) {
                    fc.getFlujoCajaMenusal().isEmpty();
                }
                for (FlujoCajaAnio fc : contrato.getProgramacionPagosProceso()) {
                    fc.getFlujoCajaMenusal().isEmpty();
                }
                for (ProcesoAdquisicionItem item : contrato.getItems()) {
                    item.getOfertasProvedor().isEmpty();
                    item.getProveedorOfertaAdjudicadaId();
                }

            }
            return contratos;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Método que se encarga de crear o actualizar los contratos. Si el método
     * de generación es el mismo solo los actualiza.
     *
     * @param idProcesoAdquisicion
     * @param tipoGeneracion
     * @return
     */
    public ProcesoAdquisicion generarActualizarContratos(Integer idProcesoAdquisicion, TipoGeneracionContrato tipoGeneracion) {
        try {
            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class, idProcesoAdquisicion);
            //si no tiene generacion o el tipo de generacion cambia lo setea
            if (proceso.getTipoContratos() == null
                    || proceso.getTipoContratos() != tipoGeneracion) {
                //si ya se generaron no se pueden volver a generar
                for (ContratoOC contrato : proceso.getContratos()) {
                    if (contrato.getEstado() != EstadoContrato.EN_CREACION_DENTRO_PROCESO) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_CONTRATOS_YA_GENERADOS_NO_PUEDE_CAMBIAR_GENERACION);
                        throw b;
                    }
                }

                //se eliminan los contratos
                Iterator<ContratoOC> iter = proceso.getContratos().iterator();
                while (iter.hasNext()) {
                    ContratoOC contrato = iter.next();
                    //desasocia los items
                    Iterator<ProcesoAdquisicionItem> iterItems = contrato.getItems().iterator();
                    while (iterItems.hasNext()) {
                        ProcesoAdquisicionItem item = iterItems.next();
                        item.setContrato(null);
                        iterItems.remove();
                    }
                    //se eliminan los impuestos
                    contrato.getImpuestos().clear();

                    //se elimina el contrato
                    iter.remove();
                    generalDAO.delete(contrato);
                }
            }

            if (tipoGeneracion == TipoGeneracionContrato.POR_ITEM) {
                crearTodosContratoPorItem(proceso);
            } else if (tipoGeneracion == TipoGeneracionContrato.POR_PROVEEDOR) {
                crearTodosContratoPorProveedor(proceso);
            }
            proceso.setTipoContratos(tipoGeneracion);

            proceso = (ProcesoAdquisicion) generalDAO.update(proceso);
            return proceso;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Método que se encarga de generar los contratos por ítem para un método de
     * adquisición
     *
     * @param idProcesoAdquisicion
     */
    private void crearTodosContratoPorProveedor(ProcesoAdquisicion proceso) {
        for (ProcesoAdquisicionItem item : UtilsUACI.getItemsAdjudicados(proceso)) {
            ProcesoAdquisicionItemProvOferta ofertaGanadora = item.getProveedorOfertaAdjudicadaId();
            ProcesoAdquisicionProveedor adqProv = ofertaGanadora.getProcesoAdquisicionProveedor();

            if (!UtilsUACI.existeItemEnContratos(proceso, item)) {

                ContratoOC datacontrato = UtilsUACI.getContratoPorProveedor(proceso, adqProv);

                if (datacontrato == null) {
                    datacontrato = new ContratoOC();
                    datacontrato.setImpuestosValidados(false);
                    datacontrato.setEstado(EstadoContrato.EN_CREACION_DENTRO_PROCESO);
                    datacontrato.setFechasDesdeOrdenInicio(Boolean.FALSE);
                    datacontrato.setProcesoAdquisicionProveedor(adqProv);
                    datacontrato.setMontoAdjudicado(ofertaGanadora.calcularPrecioTotal());
                    datacontrato.setItems(new LinkedList<ProcesoAdquisicionItem>());
                    datacontrato.setProgramacionPagosProceso(new LinkedHashSet<FlujoCajaAnio>());
                    datacontrato.setProgramacionPagosContrato(new LinkedHashSet<FlujoCajaAnio>());
                    datacontrato.setProcesoAdquisicion(proceso);
                    datacontrato.setPorcentajeAnticipo(0);
                    datacontrato.setPorcentajeDevolucion(0);
                    datacontrato.setImpuestos(new LinkedList<Impuesto>());
                    List<Impuesto> impuestosHabilitados = impuestoDao.getImpuestosIniciales();
                    for (Impuesto i : impuestosHabilitados) {
                        datacontrato.getImpuestos().add(i);
                    }

                    proceso.getContratos().add(datacontrato);
                } else {
                    //ya se  creò un data contrato para este proveedor, sumamos monto
                    BigDecimal montoContrato = datacontrato.getMontoAdjudicado();
                    montoContrato = montoContrato.add(ofertaGanadora.calcularPrecioTotal());
                    datacontrato.setMontoAdjudicado(montoContrato);
                }

                item.setContrato(datacontrato);
                datacontrato.getItems().add(item);

            }
        }

    }

    /**
     * Método que se encarga de generar los contratos por ítem
     *
     * @param proceso
     */
    public void crearTodosContratoPorItem(ProcesoAdquisicion proceso) {
        for (ProcesoAdquisicionItem item : UtilsUACI.getItemsAdjudicados(proceso)) {
            ProcesoAdquisicionItemProvOferta ofertaGanadora = item.getProveedorOfertaAdjudicadaId();

            if (!UtilsUACI.existeItemEnContratos(proceso, item)) {
                ContratoOC dataContrato = new ContratoOC();
                dataContrato.setImpuestosValidados(false);
                dataContrato.setEstado(EstadoContrato.EN_CREACION_DENTRO_PROCESO);
                dataContrato.setFechasDesdeOrdenInicio(Boolean.FALSE);
                dataContrato.setProcesoAdquisicionProveedor(ofertaGanadora.getProcesoAdquisicionProveedor());
                dataContrato.setMontoAdjudicado(ofertaGanadora.calcularPrecioTotal());
                dataContrato.setItems(new LinkedList<ProcesoAdquisicionItem>());
                dataContrato.setProgramacionPagosProceso(new LinkedHashSet<FlujoCajaAnio>());
                dataContrato.setProgramacionPagosContrato(new LinkedHashSet<FlujoCajaAnio>());
                dataContrato.setProcesoAdquisicion(proceso);
                dataContrato.setPorcentajeAnticipo(0);
                dataContrato.setPorcentajeDevolucion(0);
                dataContrato.setImpuestos(new LinkedList<Impuesto>());
                List<Impuesto> impuestosHabilitados = impuestoDao.getImpuestosIniciales();
                for (Impuesto i : impuestosHabilitados) {
                    dataContrato.getImpuestos().add(i);
                }

                item.setContrato(dataContrato);
                dataContrato.getItems().add(item);

                proceso.getContratos().add(dataContrato);
            }
        }

    }

    /**
     * Genera contratos para un proceso de adquisición
     *
     * @param proId
     */
    public void generarContratos(Integer proId) {
        try {
            generalDAO.lock(SsLock.class, ConstantesEstandares.LOCK_SECUENCIA_CONTRATO_ORDEN_COMPRA);
            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.findById(ProcesoAdquisicion.class, proId);

            //valida todos los contratos cumplan
            for (ContratoOC contrato : proceso.getContratos()) {
                try {
                    UtilsUACI.validarContrato(contrato);
                } catch (Exception e) {
                    BusinessException b = new BusinessException();
                    String[] params = {contrato.getNroContrato() + ""};
                    b.addError(ConstantesErrores.ERR_El_CONTRATO_CON_NRO_0_NO_VALIDA, params);
                    throw b;
                }

                //valida que para cada contrato la suma de la programacion de pagos coincida con el contrato
                BigDecimal totalProgramado = BigDecimal.ZERO;
                for (FlujoCajaAnio anio : contrato.getProgramacionPagosProceso()) {
                    for (POFlujoCajaMenusal mes : anio.getFlujoCajaMenusal()) {
                        if (mes.getMonto() != null) {
                            totalProgramado = totalProgramado.add(mes.getMonto());
                        }
                    }
                }
                if (totalProgramado.compareTo(contrato.getMontoAdjudicado()) != 0) {
                    BusinessException b = new BusinessException();
                    String[] params = {contrato.getNroContrato() + "", totalProgramado.toString(), contrato.getMontoAdjudicado().toString()};
                    b.addError(ConstantesErrores.ERR_EN_EL_CONTRATO_0_EL_MONTO_DE_PROGRAMACION_PAGOS_1_DISTINTO_DEL_MOTNO_ADJUDICADO_2, params);
                    throw b;
                }
            }

            List<SecuenciaContratoOrden> listaSecuencia = generalDAO.findByOneProperty(SecuenciaContratoOrden.class, "anio", proceso.getAnio().getAnio());
            for (ContratoOC contrato : proceso.getContratos()) {
                if (contrato.getEstado() == null || contrato.getEstado() == EstadoContrato.EN_CREACION_DENTRO_PROCESO) {
                    //se setea el flujo de caja cargado en el proceso como flujo de caja inicial en el contrato
                    contrato.setProgramacionPagosContrato(new LinkedHashSet());
                    for (FlujoCajaAnio flujoAnioProceso : contrato.getProgramacionPagosProceso()) {
                        FlujoCajaAnio flujoAnioContrato = new FlujoCajaAnio();
                        flujoAnioContrato.setAnio(flujoAnioProceso.getAnio());
                        flujoAnioContrato.setFlujoCajaMenusal(new LinkedList());
                        flujoAnioContrato.setActas(new LinkedList());
                        for (POFlujoCajaMenusal mesProceso : flujoAnioProceso.getFlujoCajaMenusal()) {
                            POFlujoCajaMenusal mesContrato = new POFlujoCajaMenusal();
                            mesContrato.setMes(mesProceso.getMes());
                            mesContrato.setMonto(mesProceso.getMonto());

                            flujoAnioContrato.getFlujoCajaMenusal().add(mesContrato);
                        }
                        contrato.getProgramacionPagosContrato().add(flujoAnioContrato);
                    }

                    //se guardan otros datos del cotnrato
                    contrato.setEstado(EstadoContrato.PENDIENTE);
                    if (contrato.getNroContrato() == null || contrato.getNroContrato().intValue() <= 0) {
                        SecuenciaContratoOrden secuencia = null;
                        if (!listaSecuencia.isEmpty()) {
                            secuencia = listaSecuencia.get(0);
                        } else {
                            secuencia = new SecuenciaContratoOrden();
                            secuencia.setUltimoGenerado(0);
                        }

                        contrato.setNroContrato(secuencia.getUltimoGenerado() + 1);
                        secuencia.setUltimoGenerado(secuencia.getUltimoGenerado() + 1);
                        generalDAO.update(secuencia);
                    }
                    //se envia notificacion que hay que validar los impuestos para dicho contrato
                    List<SsUsuario> anotificar = usuarioDAO.getUsuariosConOperacion(ConstantesEstandares.Operaciones.VALIDAR_IMPUESTO_CONTRATO, null);
                    for (SsUsuario u : anotificar) {
                        Notificacion n = new Notificacion();
                        n.setTipo(TipoNotificacion.VALIDAR_IMPUESTOS_CONTRATO);
                        n.setContenido("Nro. Contrato: " + contrato.getNroContrato());
                        n.setUsuario(u);
                        n.setObjetoId(contrato.getId());

                        n.setFecha(new Date());
                        generalDAO.update(n);
                    }

                }

            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Crea un compromiso presupuestario por cada contrato y fuente (distinta) y
     * lo asocia a un proceso de adquisición
     *
     * @param proId
     */
    public void solicitarCompromisoPresupuestario(Integer proId) {
        try {
            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.selectForUpdate(ProcesoAdquisicion.class, proId);
            if (proceso.getCompromisosPresupuestarios() != null && !proceso.getCompromisosPresupuestarios().isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EL_PROCESO_YA_TIENE_COMPROMISOS_PRESUPUESTARIOS_GENERADOS);
                throw b;
            }

            //Para cada contrato,
            for (ContratoOC contrato : proceso.getContratos()) {

                List<FuenteRecursos> listaFuentesInsumoDistintas = this.obtenerFuentesDeRecursosDeContrato(contrato);

                for (FuenteRecursos fuenteRecurso : listaFuentesInsumoDistintas) {

                    CompromisoPresupuestarioProceso compromiso = new CompromisoPresupuestarioProceso();
                    compromiso.setTipo(TipoCompromisoPresupuestario.PROCESO);
                    compromiso.setEstado(EstadoCompromiso.PENDIENTE);
                    compromiso.setProcesoAdquisicion(proceso);
                    compromiso.setFechaSolicitud(new Date());
                    compromiso.setContratoOC(contrato);
                    compromiso.setFuenteRecursos(fuenteRecurso);

                    proceso.getCompromisosPresupuestarios().add(compromiso);

                }

            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve los montos fuente distintos de un Contrato / OC
     *
     * @param contratoOC
     * @return
     */
    private List<FuenteRecursos> obtenerFuentesDeRecursosDeContrato(ContratoOC contratoOC) {
        List<FuenteRecursos> listaFuentesRecursos = new LinkedList<>();
        List<Integer> listaIdFuentesRecursos = new LinkedList<>();
        for (ProcesoAdquisicionItem item : contratoOC.getItems()) {
            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                POInsumos poInsumo = rel.getInsumo().getPoInsumo();
                for (POMontoFuenteInsumo montoFuente : poInsumo.getMontosFuentes()) {
                    FuenteRecursos fuenteRecurso = montoFuente.getFuente().getFuenteRecursos();
                    if (!listaIdFuentesRecursos.contains(fuenteRecurso.getId())) {
                        listaFuentesRecursos.add(fuenteRecurso);
                        listaIdFuentesRecursos.add(fuenteRecurso.getId());
                    }
                }
            }
        }
        return listaFuentesRecursos;
    }

    /**
     * Emite un compromiso presupuestario (cambiando el estado) y notifica a los
     * usuarios correspondientes que tienen dicho compromiso disponible para
     * validar. Además setea el monto comprometido correspondiente al PoInsumo
     * de la fuente asociada al compromiso presupuestario
     *
     * @param compromidoId
     * @return
     */
    public CompromisoPresupuestario emitirCompromisoPresupuestario(Integer compromidoId) {
        try {
            CompromisoPresupuestario compromiso = (CompromisoPresupuestario) generalDAO.find(CompromisoPresupuestario.class, compromidoId);

            if (compromiso.getEstado() != EstadoCompromiso.PENDIENTE) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PARA_EMITIR_COMPROMISO_PRESUPUESTARIO_DEBE_ESTAR_PENDIENTE);
                throw b;
            }

            compromiso.setEstado(EstadoCompromiso.EMITIDO);
            compromiso.setFechaEmision(new Date());
            compromiso.setUsuarioEmision(usuarioDAO.getUsuarioByCodigo(usrBean.getCodigoUsuario()));

            //se toma como si fueraa un compromiso de proceso
            String contenidoNotificacion = null;
            if (compromiso.getTipo() == TipoCompromisoPresupuestario.PROCESO) {
                CompromisoPresupuestarioProceso compromisoProceso = (CompromisoPresupuestarioProceso) compromiso;

                //Para calcular el monto comprometido que le corresponde a un PoInsumo por la emisión del compromiso presupuestario,
                // se debe calcular la proporción correspondiente al monto de la fuente para el contrato asociado al compromiso.
                if (compromisoProceso.getFuenteRecursos() != null) {
                    ContratoOC contratoOC = compromisoProceso.getContratoOC();
                    //Si es el último compromiso que falta emitir, seteo el monto comprometido de todos los poInsumos con el monto adjudicado de su correspondiente procesoInsumo
                    if (esUltimoCompromisoAEmitirEnProceso(compromisoProceso.getProcesoAdquisicion(), compromisoProceso.getId())) {
                        for (ProcesoAdquisicionInsumo insumo : UtilsUACI.getInsumosAdjudicados(compromisoProceso.getProcesoAdquisicion())) {
                            BigDecimal montoAdjudicado = insumo.getMontoTotalAdjudicado();
                            if (insumo.getPoInsumo().getMontoTotalCertificado().compareTo(montoAdjudicado) < 0) {
                                String[] prams = {insumo.getPoInsumo().getId().toString()};
                                BusinessException b = new BusinessException();
                                b.addError(ConstantesErrores.ERR_MONTO_ADJUDICADO_MAYOR_A_MONTO_CERTIFICADO_PARA_INSUMO_0, prams);
                                throw b;
                            }
                            insumo.getPoInsumo().setMontoComprometido(montoAdjudicado);
                        }
                    } else {
                        for (ProcesoAdquisicionItem item : contratoOC.getItems()) {
                            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                                POInsumos poInsumo = rel.getInsumo().getPoInsumo();
                                if (poInsumo.getMontoComprometido() == null) {
                                    poInsumo.setMontoComprometido(BigDecimal.ZERO);
                                }
                                for (POMontoFuenteInsumo montoFuente : poInsumo.getMontosFuentes()) {
                                    if (compromisoProceso.getFuenteRecursos().getId().equals(montoFuente.getFuente().getFuenteRecursos().getId())) {
                                        //Si la fuente de recursos del insumo del item del contrato es igual a la que está asociada
                                        //al compromiso presupuestario, calculo la proporción en base al monto adjudicado en la relación item-insumo
                                        //y el monto certificado de la fuente
                                        // montoProporcional = montoAdjudicadoRelacion / montoCertificadoPoInsumo * montoCertificadoFuente
                                        BigDecimal montoProporcionalComprometido = BigDecimal.ZERO;
                                        BigDecimal montoAdjudicadoRelacion = rel.getMontoTotalAdjudicado();
                                        BigDecimal montoCertificadoPoInsumo = poInsumo.getMontoTotalCertificado();
                                        BigDecimal montoCertificadoFuente = montoFuente.getCertificado();
                                        montoProporcionalComprometido = montoAdjudicadoRelacion.divide(montoCertificadoPoInsumo, 2, RoundingMode.DOWN);
                                        montoProporcionalComprometido = montoProporcionalComprometido.multiply(montoCertificadoFuente);
                                        BigDecimal nuevoMontoComprometidoPoInsumo = poInsumo.getMontoComprometido().add(montoProporcionalComprometido);
                                        poInsumo.setMontoComprometido(nuevoMontoComprometidoPoInsumo);
                                    }
                                }

                            }
                        }
                    }
                }

                String numeroProceso = compromisoProceso.getProcesoAdquisicion().getSecuenciaAnio() + " - " + compromisoProceso.getProcesoAdquisicion().getSecuenciaNumero();
                String numeroContratoOC = "";
                String nombreProveedor = "";
                if (compromisoProceso.getContratoOC() != null) {
                    numeroContratoOC = compromisoProceso.getContratoOC().getNroContrato().toString();
                    nombreProveedor = compromisoProceso.getContratoOC().getProcesoAdquisicionProveedor().getProveedor().getNombreComercial();
                }
                String codigoMontoFuenteInsumo = "";
                if (compromisoProceso.getFuenteRecursos() != null) {
                    codigoMontoFuenteInsumo = compromisoProceso.getFuenteRecursos().getCodigo();
                }
                String numeroCompromiso = compromiso.getId().toString();
                contenidoNotificacion = "Proceso no.: " + numeroProceso + ", Contrato/OC no.: " + numeroContratoOC + ", Proveedor: " + nombreProveedor + ", Fuente: " + codigoMontoFuenteInsumo + ", Compromiso: " + numeroCompromiso;

            } else {
                //como si fuera un compromiso de una modificativa
                CompromisoPresupuestarioModificativa compromisoModificativa = (CompromisoPresupuestarioModificativa) compromiso;
                for (POInsumos insumo : compromisoModificativa.getModificativaContrato().getPoInsumos()) {
                    BigDecimal montoAdjudicado = insumo.getMontoTotalCertificado();
                    insumo.setMontoComprometido(montoAdjudicado);
                }

                String numeroContrato = compromisoModificativa.getModificativaContrato().getContratoOC().getNroAnio() + " - " + compromisoModificativa.getModificativaContrato().getContratoOC().getNroContrato();
                String numeroCompromiso = compromiso.getId().toString();
                contenidoNotificacion = "Contrato no.: " + numeroContrato + ", Compromiso: " + numeroCompromiso;
            }

            //se notifica que hay un compromiso nuevo para validar
            List<SsUsuario> usuariosANotificar = usuarioBean.getUsuariosConOperacion(ConstantesEstandares.Operaciones.VALIDAR_COMPROMISO_PRESUPUESTARIO);

            for (SsUsuario usuario : usuariosANotificar) {
                Notificacion n = new Notificacion();
                n.setTipo(TipoNotificacion.VALIDACION_COMPROMISO_PRESUPUESTARIO);
                n.setContenido(contenidoNotificacion);
                n.setUsuario(usuario);
                n.setFecha(new Date());
                n.setObjetoId(compromiso.getId());
                generalDAO.update(n);
            }

            return compromiso;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Verifica que el compromiso pasado por parámetro sea el último que resta
     * por emitir
     *
     * @param proceso
     * @param idCompromiso
     * @return
     */
    private Boolean esUltimoCompromisoAEmitirEnProceso(ProcesoAdquisicion proceso, Integer idCompromiso) {
        Iterator<CompromisoPresupuestarioProceso> itCompromisos = proceso.getCompromisosPresupuestarios().iterator();
        Boolean esUltimo = true;
        while (itCompromisos.hasNext() && esUltimo) {
            CompromisoPresupuestarioProceso compromiso = itCompromisos.next();
            if (!compromiso.getId().equals(idCompromiso) && compromiso.getEstado().equals(EstadoCompromiso.PENDIENTE)) {
                esUltimo = false;
            }
        }
        return esUltimo;
    }

    /**
     * Valida un compromiso presupuestario (actualizando su estado, fecha de
     * validación, usuario validador y número SAFI)
     *
     * @param compromidoId
     * @param codigoSAFI
     * @return
     */
    public CompromisoPresupuestario
            validarCompromisoPresupuestario(Integer compromidoId, String codigoSAFI) {
        try {
            CompromisoPresupuestario compromiso = (CompromisoPresupuestario) generalDAO.find(CompromisoPresupuestario.class,
                    compromidoId);

            if (compromiso.getEstado() != EstadoCompromiso.EMITIDO) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PARA_VALIDAR_COMPROMISO_PRESUPUESTARIO_DEBE_ESTAR_EMITIDO);
                throw b;
            }

            compromiso.setEstado(EstadoCompromiso.VALIDADO);
            compromiso.setFechaValidacion(new Date());
            compromiso.setUsuarioValidacion(usuarioDAO.getUsuarioByCodigo(usrBean.getCodigoUsuario()));
            compromiso.setNumeroSAFI(codigoSAFI);

            return compromiso;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Valida que la programación de pagos de un contrato sea correcta y la
     * guarda.
     *
     * @param contrato
     * @return
     */
    public ContratoOC guardarProgramacionDePagosProceso(ContratoOC contrato) {
        try {

            ContratoUtils.validarProgramacionPagosParaContrato(contrato, contrato.getProgramacionPagosProceso());
            contrato = (ContratoOC) generalDAO.update(contrato);
            return contrato;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Genera una programación de pagos automática, dividiendo el monto
     * adjudicado de un contrato en tantas partes iguales como meses se hayan
     * indicado, colocando dichos pagos a partir de un año y un mes inicial
     * seleccionados
     *
     * @param idContrato
     * @param anioEmpieza
     * @param mesInicial
     * @param cantidadMeses
     * @return
     */
    public ContratoOC
            generarDistribucionPagosAutomaticaProceso(Integer idContrato, Integer anioEmpieza, Integer mesInicial, Integer cantidadMeses) {
        try {
            ContratoOC contrato = (ContratoOC) generalDAO.find(ContratoOC.class,
                    idContrato);
            Calendar fechaInicioContrato = new GregorianCalendar();
            if (contrato.getFechaInicio() != null) {
                fechaInicioContrato.setTime(contrato.getFechaInicio());
                Integer anioInicioContrato = fechaInicioContrato.get(Calendar.YEAR);
                Integer mesInicioContrato = fechaInicioContrato.get(Calendar.MONTH) + 1;
                //que todos los pagos se hagan luego de la fecha de inicio del contrato
                if (anioEmpieza.compareTo(anioInicioContrato) < 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_PAGOS_DEBEN_SER_POSTERIORES_A_FECHA_INCIO_CONTRATO);
                    throw b;
                }
                if (anioEmpieza.compareTo(anioInicioContrato) == 0 && mesInicial.compareTo(mesInicioContrato) < 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_PAGOS_DEBEN_SER_POSTERIORES_A_FECHA_INCIO_CONTRATO);
                    throw b;
                }
            }
            List<FlujoCajaAnio> nuevaProgramacion = ProrrateoUtils.generarFlujoDeCajaAutomatico(contrato.getMontoAdjudicado(), anioEmpieza, mesInicial - 1, cantidadMeses);
            contrato.getProgramacionPagosProceso().clear();
            contrato.getProgramacionPagosProceso().addAll(nuevaProgramacion);

            return contrato;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Se validan los cambios de un contrato y en caso que todo sea correcto, se
     * actualiza.
     *
     * @param contrato
     * @return
     */
    public ContratoOC actualizarContrato(ContratoOC contrato) {
        try {
            UtilsUACI.validarContrato(contrato);
            if (!contrato.getFechasDesdeOrdenInicio()) {
                contrato.setFechaEmision(new Date());
            }
            return (ContratoOC) generalDAO.update(contrato);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve el TDR/ET de un proceso de adquisición
     *
     * @param idProceso
     * @return
     * @throws GeneralException
     */
    public TDR
            getTDRProceso(Integer idProceso) throws GeneralException {
        try {
            ProcesoAdquisicion i = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                    idProceso);
            TDR t = i.getTdr();
            return t;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Asocia un TDR/ET a un proceso de adquisición.
     *
     * @param idProceso
     * @param tdr
     * @return
     * @throws GeneralException
     */
    public ProcesoAdquisicion
            saveTDRProceso(Integer idProceso, TDR tdr) throws GeneralException {
        try {
            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                    idProceso);
            estaDesiertoOSinEfecto(proceso);
            TDR tdrBase = null;

            if (tdr.getId() != null) {
                tdrBase = (TDR) generalDAO.find(TDR.class,
                        tdr.getId());
                tdrBase.setTempUploadedFile(tdr.getTempUploadedFile());
                tdrBase.setContenido(tdr.getContenido());
            } else {
                tdrBase = tdr;
            }
            if (tdrBase.getTempUploadedFile() != null) {
                if (tdrBase.getArchivo() == null) {
                    tdrBase.setArchivo(archivoBean.crearArchivo());
                }
                archivoBean.asociarArchivo(tdrBase.getArchivo(), tdrBase.getTempUploadedFile(), false);
            }
            tdrBase = (TDR) generalDAO.update(tdrBase);
            proceso.setTdr(tdrBase);
            return proceso;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve el TDR/ET de un ítem de un proceso de adquisición
     *
     * @param idItem
     * @return
     * @throws GeneralException
     */
    public TDR
            getTDRItem(Integer idItem) throws GeneralException {
        try {
            ProcesoAdquisicionItem i = (ProcesoAdquisicionItem) generalDAO.find(ProcesoAdquisicionItem.class,
                    idItem);
            TDR t = i.getTdr();
            return t;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Asocia un TDR/ET a un ítem de un proceso de adquisición.
     *
     * @param idItem
     * @param tdr
     * @throws GeneralException
     */
    public void saveTDRItem(Integer idItem, TDR tdr) throws GeneralException {
        try {
            ProcesoAdquisicionItem item = (ProcesoAdquisicionItem) generalDAO.find(ProcesoAdquisicionItem.class,
                    idItem);
            TDR tdrBase = null;

            if (tdr.getId() != null) {
                tdrBase = (TDR) generalDAO.find(TDR.class,
                        tdr.getId());
                tdrBase.setTempUploadedFile(tdr.getTempUploadedFile());
                tdrBase.setContenido(tdr.getContenido());
            } else {
                tdrBase = tdr;
            }
            if (tdrBase.getTempUploadedFile() != null) {
                if (tdrBase.getArchivo() == null) {
                    tdrBase.setArchivo(archivoBean.crearArchivo());
                }
                archivoBean.asociarArchivo(tdrBase.getArchivo(), tdrBase.getTempUploadedFile(), false);
            }
            tdrBase = (TDR) generalDAO.update(tdrBase);
            item.setTdr(tdrBase);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve un ítem que está en un proceso de adquisición y que tiene
     * asociado determinado insumo
     *
     * @param idInsumo
     * @return
     */
    public ProcesoAdquisicionItem obtenerItemDelInsumo(Integer idInsumo) {
        try {
            return pAdqDao.obtenerItemDelInsumo(idInsumo);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Deja un proceso de adquisición en pausa (cambia el estado)
     *
     * @param procesoAdquisicionId
     */
    public void pausarProceso(Integer procesoAdquisicionId) {
        try {
            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                    procesoAdquisicionId);
            estaDesiertoOSinEfecto(proceso);
            proceso.setEstadoProceso(EstadoProcesoAdq.PAUSA);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Guarda la pausa o reanudación del proceso de adquisición. Devuelve el
     * objeto ProcesoAdquisición guardado en la base para que en la vista
     * solamente se actualicen los datos referentes a la pausa
     *
     * @param proceso
     * @return
     */
    public ProcesoAdquisicion
            pausarProceso(Integer idProceso, EstadoProcesoAdq estadoProceso) {
        try {
            ProcesoAdquisicion procesoBD = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                    idProceso);
            estaDesiertoOSinEfecto(procesoBD);
            procesoBD.setEstadoProceso(estadoProceso);
            Date fechaActual = new Date();
            if (procesoBD.getDiasEnpausa() == null) {
                procesoBD.setDiasEnpausa(0);
            }
            if (estadoProceso == EstadoProcesoAdq.PAUSA) {
                procesoBD.setFechaUltimapausa(fechaActual);
            } else {
                Integer diasDePausa = Math.round(DatesUtils.getDateDiff(procesoBD.getFechaUltimapausa(), fechaActual, TimeUnit.DAYS));
                procesoBD.setDiasEnpausa(procesoBD.getDiasEnpausa() + diasDePausa);
            }
            generalDAO.update(procesoBD);
            return procesoBD;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Declara un proceso de adquisición como desierto o sin efecto (cambia el
     * estado)
     *
     * @param idProceso
     * @param estado
     * @return
     */
    public ProcesoAdquisicion
            declararProcesoDesiertoOSinEfecto(Integer idProceso, EstadoProcesoAdq estado) {
        try {
            ProcesoAdquisicion procesoBD = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                    idProceso);
            if (estado == EstadoProcesoAdq.DESIERTO) {
                procesoBD.setEstadoProceso(EstadoProcesoAdq.DESIERTO);
            } else {
                procesoBD.setEstadoProceso(EstadoProcesoAdq.SIN_EFECTO);
            }

            for (ProcesoAdquisicionInsumo insumo : procesoBD.getInsumos()) {
                POInsumos poinsumo = insumo.getPoInsumo();
                poinsumo.setProcesoInsumo(null);
                generalDAO.update(poinsumo);
            }
            return procesoBD;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve un rango de método de adquisición para un año especifico
     *
     * @param anio
     * @param listaRangos
     * @return
     */
    private MetodoAdquisicionRango obtenerRangoPorAnio(Integer anio, List<MetodoAdquisicionRango> listaRangos) {

        Iterator<MetodoAdquisicionRango> it = listaRangos.iterator();

        MetodoAdquisicionRango rangoSelect = null;
        MetodoAdquisicionRango rangoMenorMayor = null;
        while (it.hasNext() && rangoSelect == null) {
            MetodoAdquisicionRango rango = it.next();
            if (rango.getAnio().compareTo(anio) == 0) {
                rangoSelect = rango;
            } else if (rango.getAnio().compareTo(anio) < 0) {
                if (rangoMenorMayor == null || rangoMenorMayor.getAnio().compareTo(rango.getAnio()) < 0) {
                    rangoMenorMayor = rango;
                }
            }
        }

        if (rangoSelect != null) {
            return rangoSelect;
        } else {
            return rangoMenorMayor;
        }

    }

    /**
     * Genera una notificación para los usuarios de la UT de determinado POA,
     * donde se les informa que determinados insumos de un proceso de
     * adquisición pertenecen a un ítem que se declaró desierto o sin efecto
     *
     * @param insumos
     * @param poa
     * @param estadoItem
     * @throws DAOGeneralException
     */
    private void notificarUTItemDesiertoSinEfecto(List<ProcesoAdquisicionInsumo> insumos, GeneralPOA poa, EstadoItem estadoItem) throws DAOGeneralException {

        List<SsUsuario> anotificar = usuarioDAO.getUsuariosConUT(poa.getUnidadTecnica().getId(), null);
        String notificacion = null;
        if (insumos.size() == 1) {
            notificacion = "El insumo ";
        } else {
            notificacion = "Los insumos ";
        }

        int contador = 0;
        for (ProcesoAdquisicionInsumo insumoProceso : insumos) {
            if (contador == 0) {
                notificacion += insumoProceso.getPoInsumo().getId() + "-" + insumoProceso.getPoInsumo().getInsumo().getNombre();
            } else {
                notificacion += " ," + insumoProceso.getPoInsumo().getId() + "-" + insumoProceso.getPoInsumo().getInsumo().getNombre();
            }
            contador++;
        }

        notificacion += " pertenece a un item que quedó";

        if (estadoItem.equals(EstadoItem.DESIERTO)) {

            notificacion += " desierto";

        } else {

            notificacion += " sin efecto";

        }
        for (SsUsuario u : anotificar) {
            NotificacionPOA n = new NotificacionPOA();

            if (estadoItem.equals(EstadoItem.DESIERTO)) {
                n.setTipo(TipoNotificacion.ITEM_DESIERTO);

            } else {
                n.setTipo(TipoNotificacion.ITEM_SIN_EFECTO);

            }

            n.setContenido(notificacion);
            n.setUsuario(u);
            n.setFecha(new Date());
            n.setObjetoId(poa.getId());
            if (poa.getTipo() == TipoPOA.POA_PROYECTO) {
                POAProyecto poaProyecto = (POAProyecto) poa;
                n.setPoaAnio(poaProyecto.getAnioFiscal().getId());
                n.setObjetoId(poaProyecto.getProyecto().getId());
            } else {
                POAConActividades poaActividades = (POAConActividades) poa;
                n.setPoaAnio(poaActividades.getAnioFiscal().getId());
                n.setObjetoId(poaActividades.getConMontoPorAnio().getId());
            }

            n.setPoaUT(poa.getUnidadTecnica().getId());

            generalDAO.update(n);
        }

    }

    /**
     * Devuelve el último número de ítem en el proceso.
     *
     * @param procesoAdquisicionId
     * @return
     */
    public Integer obtenerUltimoNroItemProceso(Integer procesoAdquisicionId) {
        return pAdqDao.obtenerUltimoNroItemsDelProceso(procesoAdquisicionId);
    }

    /**
     * Elimina un contrato, validando antes que este no se encuentre en
     * ejecución o cerrado
     *
     * @param contratoId
     * @throws GeneralException
     */
    public void eliminarContrato(Integer contratoId) throws GeneralException {
        try {

            ContratoOC contrato = (ContratoOC) generalDAO.find(ContratoOC.class,
                    contratoId);
            if (contrato.getEstado().equals(EstadoContrato.EN_EJECUCION) || contrato.getEstado().equals(EstadoContrato.CERRADO)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_CONTRATO_TIENE_ORDEN_INICIO);
                throw b;
            }
            for (ProcesoAdquisicionItem item : contrato.getItems()) {
                item.setContrato(null);
                generalDAO.update(item);
            }
            generalDAO.delete(contrato);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Actualiza la relación entre un insumo y un ítem de un proceso de
     * adquisición
     *
     * @param relacionesInsumoItem
     * @throws GeneralException
     */
    public void editarInsumosProceso(RelacionProAdqItemInsumo relacionesInsumoItem) throws GeneralException {
        try {
            Integer nuevaCantidad = relacionesInsumoItem.getCantidad();
            if (nuevaCantidad < 1) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_CANTIDAD_INSUMO_MENOR_QUE_1);
                throw b;
            }
            if (relacionesInsumoItem.getItem().getEstado() != null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NO_SE_PUEDE_EDITAR_CANTIDAD_POR_ESTADO_ITEM);
                throw b;

            }
            RelacionProAdqItemInsumo relacionDB = (RelacionProAdqItemInsumo) generalDAO.find(RelacionProAdqItemInsumo.class,
                    relacionesInsumoItem.getId());
            relacionDB.setCantidad(nuevaCantidad);

            for (ProcesoAdquisicionItemProvOferta ofertaProv : relacionDB.getItem().getOfertasProvedor()) {
                for (ProcesoAdquisicionItemOfertaInsumo ofertaInsumo : ofertaProv.getOfertasPorInsumo()) {
                    if (ofertaInsumo.getProcesoAdqInsumo().getId().equals(relacionesInsumoItem.getInsumo().getId())) {
                        ofertaInsumo = (ProcesoAdquisicionItemOfertaInsumo) generalDAO.find(ProcesoAdquisicionItemOfertaInsumo.class,
                                ofertaInsumo.getId());
                        ofertaInsumo.setCantidadOferta(nuevaCantidad);
                        BigDecimal nuevoMontoOferta = ofertaProv.getPrecioUnitOferta().multiply(new BigDecimal(nuevaCantidad));
                        ofertaInsumo.setMontoOferta(nuevoMontoOferta);
                    }
                }
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve la lista de ítems de un proceso de adquisición que no están
     * asociados a un lote
     *
     * @param proId
     * @return
     * @throws GeneralException
     */
    public List<ProcesoAdquisicionItem> obtenerItemsSinLoteProcesoAdquisicion(Integer proId) throws GeneralException {
        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "procesoAdquisicion.id", proId);
        String[] orderBy = {"id"};
        boolean[] ascending = {true};

        try {
            List<ProcesoAdquisicionItem> items = generalDAO.findEntityByCriteria(ProcesoAdquisicionItem.class,
                    criterio, orderBy, ascending, null, null);
            for (ProcesoAdquisicionItem item : items) {
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();
                insumosDelItem.isEmpty();
                ProcesoAdquisicionItemProvOferta oferta = item.getProveedorOfertaAdjudicadaId();
                if (oferta != null) {
                    oferta.getOfertasPorInsumo().isEmpty();
                }

            }
            return items;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Devuelve los insumos en un determinado ítem.
     *
     * @param idItem
     * @return
     */
    public List<ProcesoAdquisicionInsumo> obtenerInsumosDelItem(Integer idItem) {
        List<ProcesoAdquisicionInsumo> insumos = pAdqDao.obtenerInsumosDelItem(idItem);
        return insumos;
    }

    /**
     * Notifica a los usuarios de las determinadas UT que tienen pendiente el
     * ingreso de TDR/ET para determinados insumos
     *
     * @param idProcesoAdquisicion
     */
    public void notificarUsuariosUTSobreTDR(Integer idProcesoAdquisicion) {
        try {
            ProcesoAdquisicion procesoAdq = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                    idProcesoAdquisicion);
            List<ProcesoAdquisicionInsumo> insumosSinTDR = getInsumosSinTDR(procesoAdq.getInsumos());
            Map<Integer, List<ProcesoAdquisicionInsumo>> mapUsuariosInsumos = getInsumosANotificarPorUsuario(insumosSinTDR);
            Date fechaRecepcionTDR = GanttUtils.getFechaMinimaRecepcionTDR(procesoAdq.getGantt());
            for (Integer idUsuarioANotificar : mapUsuariosInsumos.keySet()) {
                String contenido = "En el Proceso de Adquisición: '" + procesoAdq.getNombre()
                        + " (" + procesoAdq.getAnio().getAnio() + ")', ";
                boolean hayUnSoloInsumo = mapUsuariosInsumos.get(idUsuarioANotificar).size() == 1;
                if (hayUnSoloInsumo) {
                    contenido += "el insumo: ";
                } else {
                    contenido += "los insumos: ";
                }
                POInsumos poInsumo = new POGInsumo();
                for (int i = 0; i < mapUsuariosInsumos.get(idUsuarioANotificar).size(); i++) {
                    boolean esElPenultimo = mapUsuariosInsumos.get(idUsuarioANotificar).size() == i + 2;
                    ProcesoAdquisicionInsumo insumo = mapUsuariosInsumos.get(idUsuarioANotificar).get(i);
                    poInsumo = insumo.getPoInsumo();
                    if (hayUnSoloInsumo || !esElPenultimo) {
                        contenido += insumo.getInsumo().getNombre() + "(" + insumo.getPoInsumo().getId() + "), ";
                    } else {
                        contenido += insumo.getInsumo().getNombre() + "(" + insumo.getPoInsumo().getId() + ") y ";
                    }
                }
                if (hayUnSoloInsumo) {
                    contenido += "tiene pendiente el ingreso de su TDR/ET.";
                } else {
                    contenido += "tienen pendiente el ingreso de sus TDR/ET.";
                }
                if (fechaRecepcionTDR != null) {
                    SimpleDateFormat d = new SimpleDateFormat("dd-MM-yy");
                    String fechaRecepcionTDRFormateada = d.format(fechaRecepcionTDR);
                    contenido += "Fecha límite para hacerlo es: " + fechaRecepcionTDRFormateada + ".";
                }

                if (contenido.length() > 255) {
                    Integer cantidad = contenido.length() - 250;
                    contenido = contenido.substring(0, contenido.length() - cantidad) + "(...)";

                }

                SsUsuario usuario = (SsUsuario) generalDAO.find(SsUsuario.class,
                        idUsuarioANotificar);
                NotificacionPOA notificacion = new NotificacionPOA();
                notificacion.setUsuario(usuario);
                notificacion.setFecha(new Date());
                notificacion.setContenido(contenido);
                notificacion.setObjetoId(procesoAdq.getId());
                TipoNotificacion tipoNotificacion = TipoNotificacion.GENERAL;
                if (poInsumo.getPoa().getTipo() == TipoPOA.POA_PROYECTO) {
                    tipoNotificacion = TipoNotificacion.INSUMOS_PROCESO_ADQ_PENDIENTE_CARGA_TDR_ET_POA_PROYECTO;
                    POAProyecto poaProyecto = (POAProyecto) poInsumo.getPoa();
                    notificacion.setPoaAnio(poaProyecto.getAnioFiscal().getId());
                    notificacion.setObjetoId(poaProyecto.getProyecto().getId());
                } else {
                    tipoNotificacion = TipoNotificacion.INSUMOS_PROCESO_ADQ_PENDIENTE_CARGA_TDR_ET;
                    POAConActividades poaActividades = (POAConActividades) poInsumo.getPoa();
                    notificacion.setPoaAnio(poaActividades.getAnioFiscal().getId());
                    notificacion.setObjetoId(poaActividades.getConMontoPorAnio().getId());
                }
                notificacion.setTipo(tipoNotificacion);
                notificacion.setPoaUT(poInsumo.getPoa().getUnidadTecnica().getId());

                generalDAO.update(notificacion);

            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve la lista de insumos de un proceso de adquisición que no tienen
     * asociado un TDR/ET
     *
     * @param insumosDelProceso
     * @return
     */
    private List<ProcesoAdquisicionInsumo> getInsumosSinTDR(Collection<ProcesoAdquisicionInsumo> insumosDelProceso) {
        ArrayList<ProcesoAdquisicionInsumo> insumosSinTDR = new ArrayList<ProcesoAdquisicionInsumo>();
        for (ProcesoAdquisicionInsumo insumo : insumosDelProceso) {
            if (insumo.getPoInsumo().getTdr() == null) {
                insumosSinTDR.add(insumo);
            }
        }
        return insumosSinTDR;
    }

    /**
     * Este método se utiliza para agrupar insumos a los usuarios de las
     * determinadas unidades técnicas asociadas a los insumos de un proceso de
     * adquisición. Esto sirve para luego poder notificar solo una vez a cada
     * usuario por todos los insumos a los que aún resta cargarle el TDR/ET
     *
     * @param insumosSinTDR = Insumos de un Proceso de Adquisición que aún no
     * tienen asignado un TDR/ET
     * @return = Map que contiene como clave: el id de los usuarios
     * pertenecientes a las distintas unidades técnicas de los distintos insumos
     * del proceso de adquisición, y como valor: la lista de insumos que se
     * termina asociando con cada usuario
     */
    private Map<Integer, List<ProcesoAdquisicionInsumo>> getInsumosANotificarPorUsuario(List<ProcesoAdquisicionInsumo> insumosSinTDR) {
        Map<Integer, List<ProcesoAdquisicionInsumo>> mapUsuariosInsumos = new HashMap<Integer, List<ProcesoAdquisicionInsumo>>();
        for (ProcesoAdquisicionInsumo pAInsumo : insumosSinTDR) {
            UnidadTecnica uT = pAInsumo.getUnidadTecnica();
            List<SsUsuario> usuariosDeUTDelInsumo = usuarioDAO.getUsuariosConUT(uT.getId(), null);
            for (SsUsuario usuario : usuariosDeUTDelInsumo) {
                //Si el usuario está asociado a la mismo u otra UT que está asociada a un insumo distinto, solo agrego el insumo a la lista
                if (mapUsuariosInsumos.containsKey(usuario.getUsuId())) {
                    mapUsuariosInsumos.get(usuario.getUsuId()).add(pAInsumo);
                } else {
                    List<ProcesoAdquisicionInsumo> insumos = new ArrayList<ProcesoAdquisicionInsumo>();
                    insumos.add(pAInsumo);
                    mapUsuariosInsumos.put(usuario.getUsuId(), insumos);
                }
            }
        }
        return mapUsuariosInsumos;
    }

    /**
     * Guarda el booleano de la recepción de TDR/ET en el PoInsumo de cada
     * Insumo
     *
     * @param insumos: Son los insumos que aparecen en el Tab de Insumos del
     * proceso
     * @param mapPoInsumosRecepcionTDR: es un Map que contiene el id de cada
     * PoInsumo que aparece en la tabla de Insumos del proceso, y para cada uno
     * de estos el valor booleano de la recepción física de TDR/ET
     * correspondiente
     */
    public void guardarInsumos(List<ProcesoAdquisicionInsumo> insumos, Map<Integer, Boolean> mapPoInsumosRecepcionTDR) {
        try {
            for (ProcesoAdquisicionInsumo insumo : insumos) {
                ProcesoAdquisicionInsumo insumoGuardado = (ProcesoAdquisicionInsumo) generalDAO.find(ProcesoAdquisicionInsumo.class,
                        insumo.getId());
                Boolean recepcionTDR = mapPoInsumosRecepcionTDR.get(insumoGuardado.getPoInsumo().getId());
                insumoGuardado.getPoInsumo().setRecepcionFisicaTDR(recepcionTDR);

            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve los insumos de un proceso.
     *
     * @param idProceso
     * @return
     */
    public List<ProcesoAdquisicionInsumo> obtenerInsumosDelProceso(Integer idProceso) {
        return pAdqDao.obtenerInsumos(idProceso);
    }

    /**
     * Devuelve un PoInsumo por su id
     *
     * @param idPoInsumo
     * @return
     */
    public POInsumos
            obtenerPoInsumoById(Integer idPoInsumo) {
        try {
            return (POInsumos) generalDAO.findById(POInsumos.class,
                    idPoInsumo);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Método que asocia un poInsumo con sus certificados de disponibilidad
     * presupuestaria
     *
     * @param idInsumo id del insumo
     * @param archivos lista de archivos actual
     * @param aAgregar lista de dataFiles para agregar a archivos
     * @return
     */
    public POInsumos
            actualizarCertificadosDeDisponibilidadPresupuestaria(Integer idPOInsumo, List<Archivo> archivos, List<DataFile> aAgregar) {
        try {
            POInsumos insumo = (POInsumos) generalDAO.findById(POInsumos.class,
                    idPOInsumo);

            //se actualiza la lista actual de archivos
            Iterator<Archivo> iter = insumo.getArchivosCertificacionPresupuestaria().iterator();
            while (iter.hasNext()) {
                Archivo a = iter.next();
                if (!archivos.contains(a)) {
                    iter.remove();
                    generalDAO.delete(a);
                }
            }

            //se agregan los nuevos
            for (DataFile nuevo : aAgregar) {
                Archivo a = archivoBean.crearArchivo();
                a = archivoBean.asociarArchivo(a, nuevo, false);
                insumo.getArchivosCertificacionPresupuestaria().add(a);
            }

            return insumo;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método concentra todas las verificaciones de consistencia existentes
     * para cada estado del proceso.
     *
     * @param idProceso
     */
    public void verificarConsistenciaProcesoParaEstadoRecepcionTDR(Integer idProceso) {
        try {
            BusinessException b = new BusinessException();
            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                    idProceso);
            estaDesiertoOSinEfecto(proceso);
            try {
                chequeoPasajeAEstadoRevisionJefeUACI(proceso);
            } catch (BusinessException be) {
                logger.log(Level.SEVERE, null, be);
                b.getErrores().addAll(be.getErrores());
            }
            try {
                chequeoPasajeAEstadoInvitacion(proceso);
            } catch (BusinessException be) {
                logger.log(Level.SEVERE, null, be);
                b.getErrores().addAll(be.getErrores());
            }
            if (!b.getErrores().isEmpty()) {
                throw b;
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Verifica que un insumo de un proceso de adquisición no esté asociado a
     * otro proceso
     *
     * @param idInsumoDelProceso
     * @param idProceso
     * @return
     */
    private boolean insumoEstaEnOtroProceso(Integer idInsumoDelProceso, Integer idProceso) {
        return pAdqDao.insumoEstaEnOtroProceso(idInsumoDelProceso, idProceso);
    }

    /**
     * Controla que el presupuesto total estimado del proceso es compatible con
     * el rango permitido para el método. Para eso obtiene una lista de los
     * métodos que son compatibles con el monto y año del proceso y verifica que
     * el método asociado al proceso este dentro de dicha lista
     *
     * @param proceso
     * @return
     */
    private boolean presupuestoProcesoCompatiblePresupuestoMetodo(ProcesoAdquisicion proceso) {
        boolean compatible = false;
        if (proceso.getMetodoAdquisicion() != null && proceso.getMontoTotal() != null && proceso.getAnio() != null) {
            List<MetodoAdquisicion> metodosAdqMismoAnioYCumplenRango = metodoAdqDAO.getMetodosQueCumplanRango(proceso.getMontoTotal(), proceso.getAnio().getAnio());
            Iterator<MetodoAdquisicion> itMetodosAdqMismoAnioYCumplenRango = metodosAdqMismoAnioYCumplenRango.iterator();
            while (itMetodosAdqMismoAnioYCumplenRango.hasNext() && !compatible) {
                MetodoAdquisicion metodo = itMetodosAdqMismoAnioYCumplenRango.next();
                if (metodo.getId().equals(proceso.getMetodoAdquisicion().getId())) {
                    compatible = true;
                }
            }
        }
        return compatible;
    }

    /**
     * Este método verifica que: 1. Todos los insumos del proceso solo podrán
     * estar participando del proceso actual. 2. Todos los insumos del proceso
     * deben tener TDR o ET, Certificado de Disponibilidad cargados y recepción
     * de TDR/ET cargado. 3. El proceso tiene un método de adquisición asignado
     * y el presupuesto total estimado del proceso es compatible con el rango
     * permitido para el método. 4. El proceso tiene un cronograma planificado
     * establecido y están marcadas las dos fechas clave: recepción del TDR/ET y
     * fecha estimada de inicio de la contratación.
     *
     */
    private void chequeoPasajeAEstadoRevisionJefeUACI(ProcesoAdquisicion proceso) {
        BusinessException b = new BusinessException();
        boolean insumoSinCertDisp = false;
        boolean insumoSinTDR = false;
        boolean insumoSinRecepcionTDR = false;
        Collection<ProcesoAdquisicionInsumo> insumosProceso = proceso.getInsumos();
        boolean todosLosErroresEncontrados = false; //Esta variable se usar para que una vez que encuentre todos los errores, no siga recorriendo
        Iterator<ProcesoAdquisicionInsumo> itInsumosProceso = insumosProceso.iterator();
        while (itInsumosProceso.hasNext() && !todosLosErroresEncontrados) {
            ProcesoAdquisicionInsumo insumo = itInsumosProceso.next();
            POInsumos poInsumo = insumo.getPoInsumo();

            if (poInsumo.getPasoValidacionCertificadoDeDispPresupuestaria() == null
                    || !poInsumo.getPasoValidacionCertificadoDeDispPresupuestaria()) {
                logger.log(Level.INFO, "No tiene validacion: " + poInsumo.getId());
                insumoSinCertDisp = true;
            }
            if (insumo.getPoInsumo().getTdr() == null) {
                insumoSinTDR = true;
            }
            if (insumo.getPoInsumo().getRecepcionFisicaTDR() == null
                    || !insumo.getPoInsumo().getRecepcionFisicaTDR()) {
                insumoSinRecepcionTDR = true;
            }
            if (insumoSinCertDisp && insumoSinTDR && insumoSinRecepcionTDR) {
                todosLosErroresEncontrados = true;
            }
        }

        if (proceso.getMetodoAdquisicion() == null) {
            b.addError(ConstantesErrores.ERR_PROCESO_ADQ_METODO_ADQ_VACIO);
        }
        if (proceso.getMetodoAdquisicion() != null && !presupuestoProcesoCompatiblePresupuestoMetodo(proceso)) {
            b.addError(ConstantesErrores.ERR_PRESUP_PROC_INCOMPATIBLE_PRESUP_MET);
        }
        if (insumoSinTDR) {
            b.addError(ConstantesErrores.ERR_INSUMO_SIN_TDR_ET);
        }
        if (insumoSinRecepcionTDR) {
            b.addError(ConstantesErrores.ERR_INSUMO_SIN_RECEPCION_TDR_ET);
        }
        if (insumoSinCertDisp) {
            b.addError(ConstantesErrores.ERR_INSUMO_SIN_CERT_DISP_VALIDADO);
        }

        if (!b.getErrores().isEmpty()) {
            throw b;
        }
    }

    /**
     * Este método verifica:. - que todos los ítems creados tengan insumos y
     * TDR/ET asociados. - que todos los Lotes creados tengan ítems asociados y
     * que a su vez estos tengan sus correspondientes TDR/ET cargados. - que
     * todos los insumos estén asociados a ítems - que el proceso tenga un
     * TDR/ET asociado
     *
     * @param proceso
     */
    private void chequeoPasajeAEstadoInvitacion(ProcesoAdquisicion proceso) {
        BusinessException b = new BusinessException();
        int canInsumosItemLote = 0;
        boolean faltaTDRItem = false;
        boolean itemSinInsumo = false;
        for (ProcesoAdquisicionItem item : proceso.getItems()) {
            //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
            List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();
            canInsumosItemLote += insumosDelItem.size();
            if (!itemSinInsumo && insumosDelItem.isEmpty()) {
                itemSinInsumo = true;
                b.addError(ConstantesErrores.ERR_ITEM_NO_TIENE_INSUMOS);
            }
            if (!faltaTDRItem && item.getTdr() == null) {
                b.addError(ConstantesErrores.ERR_PROCESO_ITEM_SIN_TDR);
                faltaTDRItem = true;
            }

        }
        boolean loteSinItem = false;
        for (ProcesoAdquisicionLote lote : proceso.getLotes()) {
            if (!loteSinItem && lote.getItems().isEmpty()) {
                loteSinItem = true;
                b.addError(ConstantesErrores.ERR_LOTE_SIN_ITEMS);
            }
            for (ProcesoAdquisicionItem itemLote : lote.getItems()) {
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                List<ProcesoAdquisicionInsumo> insumosDelItem = itemLote.getInsumosTemporalesDelItem();
                canInsumosItemLote += insumosDelItem.size();

                if (!faltaTDRItem && itemLote.getTdr() == null) {
                    b.addError(ConstantesErrores.ERR_PROCESO_ITEM_SIN_TDR);
                    faltaTDRItem = true;
                }
            }

        }
        if (proceso.getTdr() == null) {
            b.addError(ConstantesErrores.ERR_PROCESO_SIN_TDR);

        }
        if (canInsumosItemLote < proceso.getInsumos().size()) {

            b.addError(ConstantesErrores.ERR_INSUMOS_SIN_ITEM);

        }
        throw b;

    }

    /**
     * Controla que no se puedan hacer cambios en el proceso de adquisición
     * luego que este se haya declarado desierto o sin efecto
     *
     * @param proceso
     */
    private void estaDesiertoOSinEfecto(ProcesoAdquisicion proceso) {
        if (proceso.getEstadoProceso() == EstadoProcesoAdq.DESIERTO) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_PROC_DESIERTO);
            throw b;
        }
        if (proceso.getEstadoProceso() == EstadoProcesoAdq.SIN_EFECTO) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_PROC_SIN_EFECTO);
            throw b;
        }
    }

    /**
     * Deja en pausa un ítem de un proceso de adquisición (cambia el estado)
     *
     * @param idItem
     * @return
     */
    public ProcesoAdquisicionItem
            pausarItem(Integer idItem) {
        try {
            ProcesoAdquisicionItem itemDB = (ProcesoAdquisicionItem) generalDAO.find(ProcesoAdquisicionItem.class,
                    idItem);
            itemDB.setEstado(EstadoItem.PAUSA);
            return itemDB;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve la diferencia entre el monto total certificado y el total
     * adjudicado para determinado insumo de un proceso de adquisición
     *
     * @param insumo
     * @return
     */
    private BigDecimal calcularMontoTotalCertificadoRestanteParaInsumo(ProcesoAdquisicionInsumo insumo) {
        BigDecimal montoTotalCertificado = insumo.getPoInsumo().getMontoTotalCertificado();
        BigDecimal montoAdjudicado = insumo.getMontoTotalAdjudicado();
        return montoTotalCertificado.subtract(montoAdjudicado);
    }

    /**
     * getColorSegunFechaPlanificada. Compara el estado actual en el que se
     * encuentra un proceso de adquisición con la tarea correspondiente (busca
     * la tarea que coincida con el estado) asociada al método de adquisición
     * seleccionado para dicho proceso. Luego de la comparación devuelve un
     * color: Verde = si la fecha de inicio de la etapa en que se está es menor
     * a la fecha de inicio planificada. Amarillo = Si la fecha del día está
     * entre la fecha de inicio planificada y la fecha de fin planificada. Rojo
     * = si la fecha del día es mayor a la fecha de inicio planificada.
     *
     * @param procesoAdq = Proceso de adquisición a analizar
     * @return "VERDE", "AMARILLO" o "ROJO"
     */
    public String getColorSegunFechaPlanificada(ProcesoAdquisicion procesoAdq) {
        String color = ConstantesEstandares.COLORES_PROCESO.COLOR_VERDE;
        Date fechaActual = new Date();
        PasosProcesoAdquisicion estadoActual = procesoAdq.getEstado();
        Long start = null;
        Long end = null;

        if (procesoAdq.getGantt() != null) {

            TipoTarea[] tiposTareasDelEstadoActual = UtilsUACI.getMapeoProceso().get(estadoActual);

            Long mayor = Long.MIN_VALUE;
            Long menor = Long.MAX_VALUE;
            for (int i = 0; i < tiposTareasDelEstadoActual.length; i++) {
                TipoTarea tipoTarea = tiposTareasDelEstadoActual[i];
                Iterator<GanttTask> itTareas = procesoAdq.getGantt().getTasks().iterator();
                boolean tareaEncontrada = false;
                while (itTareas.hasNext() && !tareaEncontrada) {
                    GanttTask tarea = itTareas.next();
                    if (tipoTarea.equals(tarea.getTipoTarea())) {
                        /**
                         * Si varias tareas que pertenecen al estado actual
                         * están en el método de adquisición, me voy a quedar
                         * con la fecha menor de ellas como la fecha de inicio y
                         * con la fecha mayor de ellas como la fecha de fin
                         */
                        tareaEncontrada = true;
                        if (tarea.getStart() < menor) {
                            menor = tarea.getStart();
                            start = tarea.getStart();
                        }
                        if (tarea.getEnd() > mayor) {
                            mayor = tarea.getEnd();
                            end = tarea.getEnd();
                        }
                    }
                }
            }

            Date fechaInicio = null;
            Date fechaFin = null;
            if (start != null && end != null) {
                fechaInicio = new Date(start);
                fechaFin = new Date(end);

                //Si la fecha de inicio planificada es mayor a la actual, el proceso esta adelantado con respecto a lo planificado
                if (fechaActual.compareTo(fechaInicio) < 0) {
                    color = ConstantesEstandares.COLORES_PROCESO.COLOR_VERDE;
                    //Si la fecha de inicio planificada es menor/igual a la actual y la de fin mayor/igual a la acual el proceso está acorde a lo planificado
                } else if (fechaActual.compareTo(fechaInicio) >= 0 && fechaActual.compareTo(fechaFin) <= 0) {
                    color = ConstantesEstandares.COLORES_PROCESO.COLOR_AMARILLO;
                    //Si la fecha de fin planificada es menor a la actual, el proceso está atrazado con respecto a lo planificado
                } else if (fechaActual.compareTo(fechaFin) > 0) {
                    color = ConstantesEstandares.COLORES_PROCESO.COLOR_ROJO;
                }
            }
        }

        return color;
    }

    /**
     * Devuelve un ítem que pertenece a determinado proceso de adquisición
     *
     * @param idItemBuscado
     * @param proceso
     * @return
     */
    private ProcesoAdquisicionItem seleccionarItemDeProcesoAdqPorId(Integer idItemBuscado, ProcesoAdquisicion proceso) {
        for (ProcesoAdquisicionItem item : proceso.getItems()) {
            if (item.getId().equals(idItemBuscado)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Devuelve un ítem que pertenece a determinado lote de un proceso de
     * adquisición
     *
     * @param idItemBuscado
     * @param lote
     * @return
     */
    private ProcesoAdquisicionItem seleccionarItemDeLotePorId(Integer idItemBuscado, ProcesoAdquisicionLote lote) {
        boolean encontro = false;
        ProcesoAdquisicionItem itemBuscado = null;
        Iterator<ProcesoAdquisicionItem> it = lote.getItems().iterator();
        while (it.hasNext() && !encontro) {
            ProcesoAdquisicionItem item = it.next();
            if (item.getId().equals(idItemBuscado)) {
                itemBuscado = item;
                encontro = true;
            }
        }
        return itemBuscado;
    }

    /**
     * Devuelve un lote que pertenece a determinado proceso de adquisición
     *
     * @param idLoteBuscado
     * @param proceso
     * @return
     */
    private ProcesoAdquisicionLote seleccionarLoteDeProcesoAdqPorId(Integer idLoteBuscado, ProcesoAdquisicion proceso) {
        for (ProcesoAdquisicionLote iterLote : proceso.getLotes()) {
            if (iterLote.getId() != null && iterLote.getId().equals(idLoteBuscado)) {
                return iterLote;
            }
        }
        return null;
    }

    /**
     * Determina si determinado insumo de un proceso de adquisición tiene
     * asociada una relación entre insumo e ítem
     *
     * @param insumo
     * @param relacion
     * @return
     */
    private boolean insumoTieneRelacion(ProcesoAdquisicionInsumo insumo, RelacionProAdqItemInsumo relacion) {
        boolean tiene = false;
        Iterator<RelacionProAdqItemInsumo> it = insumo.getRelItemInsumos().iterator();
        while (it.hasNext() && !tiene) {
            RelacionProAdqItemInsumo rel = it.next();
            if (rel.getId() != null && relacion.getId() != null && rel.getId().equals(relacion.getId())) {
                tiene = true;
            }
        }
        return tiene;
    }

    /**
     * Obtiene un insumo que pertenece a un determinado proceso de adquisición
     *
     * @param idInsumo
     * @param proceso
     * @return
     */
    private ProcesoAdquisicionInsumo obtenerInsumoDelProceso(Integer idInsumo, ProcesoAdquisicion proceso) {
        ProcesoAdquisicionInsumo insumo = null;
        boolean encontro = false;
        Iterator<ProcesoAdquisicionInsumo> it = proceso.getInsumos().iterator();
        while (it.hasNext() && !encontro) {
            ProcesoAdquisicionInsumo ins = it.next();
            if (ins.getId().equals(idInsumo)) {
                insumo = ins;
            }
        }
        return insumo;
    }

    /**
     * Determina si una relación entre insumo e ítem está asociada a alguno de
     * los ítems de determinado proceso de adquisición
     *
     * @param relacion
     * @param proceso
     * @return
     */
    private boolean relacionEstaEnItems(RelacionProAdqItemInsumo relacion, ProcesoAdquisicion proceso) {
        boolean esta = false;
        Iterator<ProcesoAdquisicionItem> itItems = proceso.getItems().iterator();
        while (itItems.hasNext() && !esta) {
            ProcesoAdquisicionItem item = itItems.next();
            Iterator<RelacionProAdqItemInsumo> itRelaciones = item.getRelItemInsumos().iterator();
            while (itRelaciones.hasNext() && !esta) {
                RelacionProAdqItemInsumo relDelItem = itRelaciones.next();
                if (relDelItem.equals(relacion.getId())) {
                    esta = true;
                }
            }
        }
        if ((!esta) && proceso.getLotes() != null) {
            List<ProcesoAdquisicionLote> lotes = proceso.getLotes();
            for (ProcesoAdquisicionLote lote : lotes) {
                Iterator<ProcesoAdquisicionItem> itItemsDeLotes = lote.getItems().iterator();
                while (itItems.hasNext() && !esta) {
                    ProcesoAdquisicionItem item = itItems.next();
                    Iterator<RelacionProAdqItemInsumo> itRelaciones = item.getRelItemInsumos().iterator();
                    while (itRelaciones.hasNext() && !esta) {
                        RelacionProAdqItemInsumo relDelItem = itRelaciones.next();
                        if (relDelItem.equals(relacion.getId())) {
                            esta = true;
                        }
                    }
                }
            }
        }
        return esta;
    }

    /**
     * Verifica que el usuario logueado coincide con el usuario asociado a un
     * proceso de adquisición y que deberá trabajar el siguiente estado (paso)
     * del mismo
     *
     * @param proceso
     */
    private void tecnicoUACIActualCoincideConProximoEstado(ProcesoAdquisicion proceso) {
        if (proceso.getResponsable() != null) {
            if (!usrBean.getCodigoUsuario().equals(proceso.getResponsable().getUsuCod())) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_TECNICO_UACI_SELECCIONADO_DIFERENTE_USUARIO_ACTUAL);
                throw b;
            }
        }
    }

    /**
     * Devuelve la cantidad de días planificada para finalizar el paso en el que
     * se encuentra el proceso de adquisición actualmente.
     *
     * @param idProceso
     * @return
     */
    public Integer
            obtenerTiempoPlanificadoParaEstadoActual(Integer idProceso) {
        ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                idProceso);
        Integer cantidadDias = 0;

        if (proceso.getGanttPlanificado() != null && proceso.getMetodoAdquisicion() != null) {

            PasosProcesoAdquisicion estadoActual = proceso.getEstado();

            Date fechaInicioCronograma = new Date(Long.MAX_VALUE);
            for (GanttTask tareaPlanificada : proceso.getGanttPlanificado().getTasks()) {
                Date fechaInicioTarea = new Date(tareaPlanificada.getStart());
                if (fechaInicioTarea.compareTo(fechaInicioCronograma) < 0) {
                    fechaInicioCronograma = fechaInicioTarea;
                }
            }
            TipoTarea[] tiposTareasDelEstadoActual = UtilsUACI.getMapeoProceso().get(estadoActual);
            // Se obtienen las tareas del cronograma que matchean con el paso actual, me quedo con la que tiene fecha de fin superior
            List<GanttTask> tareasPlanificadasParaPasoActual = new LinkedList<GanttTask>();
            for (GanttTask tarea : proceso.getGanttPlanificado().getTasks()) {
                for (int i = 0; i < tiposTareasDelEstadoActual.length; i++) {
                    TipoTarea tipoTarea = tiposTareasDelEstadoActual[i];
                    if (tarea.getTipoTarea() != null) {
                        if (tarea.getTipoTarea().equals(tipoTarea)) {
                            tareasPlanificadasParaPasoActual.add(tarea);
                        }
                    }
                }
            }
            Date maximaFechaFin = new Date(Long.MIN_VALUE);
            for (GanttTask tarea : tareasPlanificadasParaPasoActual) {
                Date fechaFinTarea = new Date(tarea.getEnd());
                if (fechaFinTarea.compareTo(maximaFechaFin) > 0) {
                    maximaFechaFin = fechaFinTarea;
                }
            }
            for (GanttTask tareaPlanif : proceso.getGanttPlanificado().getTasks()) {
                //Sumo los días de duración de las tareas que son anteriores a la tarea actual
                Date fechaFin = new Date(tareaPlanif.getEnd());
                if (fechaFin.compareTo(maximaFechaFin) <= 0) {
                    cantidadDias += tareaPlanif.getDuration();
                }
            }
        }
        return cantidadDias;
    }

    /**
     * Devuelve la cantidad de días existente entre la fecha actual y la fecha
     * de inicio del cronograma, restando los días en los que el procesos estuvo
     * en pausa.
     *
     * @param idProceso
     * @return
     */
    public Integer
            obtenerTiempoRealParaEstadoActual(Integer idProceso) {
        ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                idProceso);
        Integer cantidadDias = 0;
        Date fechaActual = new Date();

        if (proceso.getGantt() != null && proceso.getMetodoAdquisicion() != null) {
            //Si la fecha de inicio del cronograma es menor a la fecha actual, hago los calculos, si no lla cantidad de días será 0
            if (proceso.getMenorFechaGantt().compareTo(fechaActual) < 0) {
                Date fechaInicioCronograma = new Date(Long.MAX_VALUE);
                for (GanttTask tareaPlanificada : proceso.getGantt().getTasks()) {
                    Date fechaInicioTarea = new Date(tareaPlanificada.getStart());
                    if (fechaInicioTarea.compareTo(fechaInicioCronograma) < 0) {
                        fechaInicioCronograma = fechaInicioTarea;
                    }
                }

                PasosProcesoAdquisicion estadoActual = proceso.getEstado();

                TipoTarea[] tiposTareasDelEstadoActual = UtilsUACI.getMapeoProceso().get(estadoActual);
                // Se obtienen las tareas del cronograma que matchean con el paso actual, me quedo con la que tiene fecha de fin superior
                List<GanttTask> tareasRealesParaPasoActual = new LinkedList<GanttTask>();
                for (GanttTask tarea : proceso.getGantt().getTasks()) {
                    for (int i = 0; i < tiposTareasDelEstadoActual.length; i++) {
                        TipoTarea tipoTarea = tiposTareasDelEstadoActual[i];
                        if (tarea.getTipoTarea() != null) {
                            if (tarea.getTipoTarea().equals(tipoTarea)) {
                                tareasRealesParaPasoActual.add(tarea);
                            }
                        }
                    }
                }
                Date maximaFechaFin = new Date(Long.MIN_VALUE);
                for (GanttTask tarea : tareasRealesParaPasoActual) {
                    Date fechaFinTarea = new Date(tarea.getEnd());
                    if (fechaFinTarea.compareTo(maximaFechaFin) > 0) {
                        maximaFechaFin = fechaFinTarea;
                    }
                }

                Integer cantidadDiasEnPausa = proceso.getDiasEnPausa() != null ? proceso.getDiasEnPausa() : 0;

                for (GanttTask tareaReal : proceso.getGantt().getTasks()) {
                    //Sumo los días de duración de las tareas que son anteriores a la tarea actual
                    Date fechaFin = new Date(tareaReal.getEnd());
                    if (fechaFin.compareTo(maximaFechaFin) <= 0) {
                        cantidadDias += tareaReal.getDuration();
                    }
                }
                cantidadDias = cantidadDias - cantidadDiasEnPausa;
                if (cantidadDias < 0) {
                    cantidadDias = 0;
                }
            }
        }

        return cantidadDias;
    }

    /**
     * Se recorren todas las tareas del Gantt planificado y se devuelve la menor
     * fecha de todas, si el proceso no tiene un Gantt planificado, la celda se
     * muestra vacía.
     *
     * @param idProceso
     * @return
     */
    public Date
            obtenerFechaInicioPlanificada(Integer idProceso) {
        ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                idProceso);
        Date fechaInicioCronograma = new Date(Long.MAX_VALUE);
        if (proceso.getGanttPlanificado() != null && proceso.getMetodoAdquisicion() != null) {

            for (GanttTask tareaPlanificada : proceso.getGanttPlanificado().getTasks()) {
                Date fechaInicioTarea = new Date(tareaPlanificada.getStart());
                if (fechaInicioTarea.compareTo(fechaInicioCronograma) < 0) {
                    fechaInicioCronograma = fechaInicioTarea;
                }
            }
        } else {
            fechaInicioCronograma = null;
        }
        return fechaInicioCronograma;
    }

    /**
     * Se obtiene el estado actual del proceso y las tareas que corresponden al
     * paso actual que están en el Gantt planificado asociado al proceso. De
     * esas tareas, se obtiene la mayor fecha de fin y se devuelve. Si el
     * proceso no tiene un Gantt planificado, se devuelve null.
     *
     * @param idProceso
     * @return
     */
    public Date
            obtenerFechaFinPlanificadaParaPasoActual(Integer idProceso) {
        ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                idProceso);
        Date maximaFechaFin = new Date(Long.MIN_VALUE);
        if (proceso.getGanttPlanificado() != null && proceso.getMetodoAdquisicion() != null) {
            PasosProcesoAdquisicion estadoActual = proceso.getEstado();
            TipoTarea[] tiposTareasDelEstadoActual = UtilsUACI.getMapeoProceso().get(estadoActual);
            // Se obtienen las tareas del cronograma que matchean con el paso actual, me quedo con la que tiene fecha de fin superior
            List<GanttTask> tareasPlanificadasParaPasoActual = new LinkedList<GanttTask>();
            for (GanttTask tarea : proceso.getGanttPlanificado().getTasks()) {
                for (int i = 0; i < tiposTareasDelEstadoActual.length; i++) {
                    TipoTarea tipoTarea = tiposTareasDelEstadoActual[i];
                    if (tarea.getTipoTarea() != null) {
                        if (tarea.getTipoTarea().equals(tipoTarea)) {
                            tareasPlanificadasParaPasoActual.add(tarea);
                        }
                    }
                }
            }
            for (GanttTask tarea : tareasPlanificadasParaPasoActual) {
                Date fechaFinTarea = new Date(tarea.getEnd());
                if (fechaFinTarea.compareTo(maximaFechaFin) > 0) {
                    maximaFechaFin = fechaFinTarea;
                }
            }
        } else {
            maximaFechaFin = null;
        }
        if (maximaFechaFin != null && maximaFechaFin.compareTo(new Date(Long.MIN_VALUE)) == 0 || maximaFechaFin.compareTo(new Date(Long.MAX_VALUE)) == 0) {
            maximaFechaFin = null;
        }
        return maximaFechaFin;
    }

    /**
     * Se recorren todas las tareas del Gantt real y devuelve la menor de todas.
     * Si el proceso no tiene un Gantt real asociado se devuelve null.
     *
     * @param idProceso
     * @return
     */
    public Date
            obtenerFechaInicioReal(Integer idProceso) {
        ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                idProceso);
        Date fechaInicioCronograma = new Date(Long.MAX_VALUE);
        if (proceso.getGantt() != null && proceso.getMetodoAdquisicion() != null) {
            for (GanttTask tareaPlanificada : proceso.getGantt().getTasks()) {
                Date fechaInicioTarea = new Date(tareaPlanificada.getStart());
                if (fechaInicioTarea.compareTo(fechaInicioCronograma) < 0) {
                    fechaInicioCronograma = fechaInicioTarea;
                }
            }
        } else {
            fechaInicioCronograma = null;
        }
        return fechaInicioCronograma;
    }

    /**
     * Devuelve la cantidad de días de diferencia existente entre el tiempo real
     * (Gantt real) y el tiempo planificado (Gantt planificado). Si el tiempo
     * planificado es mayor al tiempo real devuelve 0.
     *
     * @param idProceso
     * @return
     */
    public Integer obtenerTiempoAtraso(Integer idProceso) {
        Integer tiempoPlanificado = this.obtenerTiempoPlanificadoParaEstadoActual(idProceso);
        Integer tiempoReal = this.obtenerTiempoRealParaEstadoActual(idProceso);
        Integer diasAtraso = 0;
        if (tiempoReal > tiempoPlanificado && tiempoPlanificado > 0) {
            diasAtraso = tiempoReal - tiempoPlanificado;
        }
        return diasAtraso;
    }

    /**
     * Devuelve la cantidad de días de diferencia existente entre el tiempo
     * planificado (Gantt planificado) y el tiempo real (Gantt real). Si el
     * tiempo real es mayor al tiempo planificado devuelve 0.
     *
     * @param idProceso
     * @return
     */
    public Integer obtenerTiempoAdelanto(Integer idProceso) {
        Integer tiempoPlanificado = this.obtenerTiempoPlanificadoParaEstadoActual(idProceso);
        Integer tiempoReal = this.obtenerTiempoRealParaEstadoActual(idProceso);

        Integer diasAdelanto = 0;
        if (tiempoReal < tiempoPlanificado && tiempoReal > 0) {
            diasAdelanto = tiempoPlanificado - tiempoReal;
        }
        return diasAdelanto;
    }

    /**
     * Devuelve los últimos 5 precios unitarios adjudicados.
     *
     * @param idInsumo
     * @return
     */
    public String obtenerUltimos5PreciosInsumoAdjudicado(Integer idInsumo) {
        String respuesta = "";
        List<BigDecimal> precios = pAdqDao.obtenerUltimos5PreciosInsumoAdjudicado(idInsumo);
        for (int i = 0; i < 5 && i < precios.size(); i++) {
            if (respuesta.length() > 0) {
                respuesta = respuesta + ", ";
            }
            respuesta = respuesta + ReportesUtils.getNumber(precios.get(i));
        }
        return respuesta;
    }

    /**
     * Verifica que la fecha de recepción de ofertas del cronograma real de un
     * proceso de adquisición, sea mayor o igual a la fecha que ya tenía
     * establecida
     *
     * @param proceso
     * @param ganttNuevo
     */
    private void verificarCambioFechaYHoraRecepcionOfertas(ProcesoAdquisicion proceso, PasosProcesoAdquisicion estadoProceso, Gantt ganttNuevo) {
        BusinessException b = new BusinessException();
        int posicionInvitacion = PasosProcesoAdquisicion.INVITACION.getPosicion();

        if (estadoProceso.getPosicion() >= posicionInvitacion) {
            ProcesoAdquisicion procesoBase = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                    proceso.getId());
            if (procesoBase.getGantt() != null) {
                Date fechaAnterior = null;
                String horaAnterior = "";

                GanttTask mayorTareaRecepcionOfertasGanttAnt = GanttUtils.obtenerTareaConMayorFechaFinPorTipoTarea(procesoBase.getGantt(), TipoTarea.RECEPCION_DE_OFERTAS);

                if (mayorTareaRecepcionOfertasGanttAnt != null) {
                    if (mayorTareaRecepcionOfertasGanttAnt.getEnd() != null) {
                        fechaAnterior = new Date(mayorTareaRecepcionOfertasGanttAnt.getEnd());
                    }
                    if (!TextUtils.isEmpty(mayorTareaRecepcionOfertasGanttAnt.getHoraFin())) {
                        horaAnterior = mayorTareaRecepcionOfertasGanttAnt.getHoraFin();
                    }
                }

                if (fechaAnterior != null) {
                    Date fechaNueva = null;
                    String horaNueva = "";

                    GanttTask mayorTareaRecepcionOfertasGanttNuevo = GanttUtils.obtenerTareaConMayorFechaFinPorTipoTarea(ganttNuevo, TipoTarea.RECEPCION_DE_OFERTAS);

                    if (mayorTareaRecepcionOfertasGanttNuevo != null) {
                        if (mayorTareaRecepcionOfertasGanttNuevo.getEnd() != null) {
                            fechaNueva = new Date(mayorTareaRecepcionOfertasGanttNuevo.getEnd());
                        } else {
                            b.addError(ConstantesErrores.ERROR_FALTA_INGRESAR_FECHA_FIN_RECEPCION_OFERTAS);
                        }
                        if (!TextUtils.isEmpty(mayorTareaRecepcionOfertasGanttNuevo.getHoraFin())) {
                            horaNueva = mayorTareaRecepcionOfertasGanttNuevo.getHoraFin();
                        } else {
                            b.addError(ConstantesErrores.ERROR_FALTA_INGRESAR_HORA_FIN_RECEPCION_OFERTAS);
                        }
                    }

                    if (fechaNueva != null) {
                        //Si la fecha anterior es mayor a la nueva, lanzo la excepción
                        if (fechaAnterior.compareTo(fechaNueva) > 0) {
                            b.addError(ConstantesErrores.ERR_FECHA_RECEPCION_OFERTAS_INCORRECTA);
                        } else if (fechaAnterior.compareTo(fechaNueva) == 0) {
                            //Si la fecha anterior y la nueva son iguales reviso que la hora nueva sea mayor o igual a la anterior
                            if (!horaAnterior.isEmpty() && !horaNueva.isEmpty() && !horaAnterior.equals(horaNueva)) {//Si no son iguales
                                if (this.esHoraAnteriorMayorQueHoraActual(horaAnterior, horaNueva)) {
                                    b.addError(ConstantesErrores.ERR_HORA_RECEPCION_OFERTAS_INCORRECTA);
                                }
                            }
                        }
                    }
                }

            }
        }
        if (!b.getErrores().isEmpty()) {
            throw b;
        }
    }

    private Boolean esHoraAnteriorMayorQueHoraActual(String horaAnterior, String horaNueva) {
        Boolean anteriorEsMayor = true;
        String[] horaAntSplit = horaAnterior.split(":");
        String[] horaActSplit = horaNueva.split(":");
        Integer horaAnt = Integer.parseInt(horaAntSplit[0]);
        Integer horaAct = Integer.parseInt(horaActSplit[0]);
        if (horaAct > horaAnt) {
            anteriorEsMayor = false;
        } else if (horaAct == horaAnt) {
            Integer minAnt = Integer.parseInt(horaAntSplit[1]);
            Integer minAct = Integer.parseInt(horaActSplit[1]);
            if (minAct >= minAnt) {
                anteriorEsMayor = false;
            }
        }
        return anteriorEsMayor;
    }

    /**
     * Verifica que los datos necesarios para generar la invitación de
     * proveedores al proceso de adquisición sean correctos
     *
     * @param proceso
     */
    public void verificarGeneracionInvitaciones(Integer idProceso) {
        ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class,
                idProceso);
        BusinessException b = new BusinessException();

        GanttTask mayorTareaRecepcionOfertas = GanttUtils.obtenerTareaConMayorFechaFinPorTipoTarea(proceso.getGantt(), TipoTarea.RECEPCION_DE_OFERTAS);

        if (mayorTareaRecepcionOfertas != null) {
            if (mayorTareaRecepcionOfertas.getTipoTarea() != null && mayorTareaRecepcionOfertas.getTipoTarea().equals(TipoTarea.RECEPCION_DE_OFERTAS)) {
                if (mayorTareaRecepcionOfertas.getEnd() == null) {
                    b.addError(ConstantesErrores.ERROR_FALTA_INGRESAR_FECHA_FIN_RECEPCION_OFERTAS);
                }
                if (TextUtils.isEmpty(mayorTareaRecepcionOfertas.getHoraFin())) {
                    b.addError(ConstantesErrores.ERROR_FALTA_INGRESAR_HORA_FIN_RECEPCION_OFERTAS);
                }
            }
        }

        if (!b.getErrores().isEmpty()) {
            throw b;
        }
    }

}
