/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.datatype.DataDistribuccionProgramacionPagosContrato;
import gob.mined.siap2.business.datatype.DistribucionMontoAdjudicado;
import gob.mined.siap2.business.datatype.DistribucionProgramacionPagosInsumos;
import gob.mined.siap2.business.datatype.DistribucionProgramacionPagosItem;
import gob.mined.siap2.business.datatype.DistribucionProgramacionPagosMes;
import gob.mined.siap2.business.ejbs.ConfiguracionBean;
import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.ejbs.LogAuditoriaBean;
import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.business.ejbs.impl.ArchivoBean;
import gob.mined.siap2.business.ejbs.impl.ReporteBean;
import gob.mined.siap2.business.utils.ContratoUtils;
import gob.mined.siap2.business.utils.InsumoUtils;
import gob.mined.siap2.business.utils.UtilsUACI;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesConfiguracion;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.entities.data.SsLock;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.ActaContrato;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ComprobanteDeRecepcionDeExpedienteDePago;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestario;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioModificativa;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioProceso;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.Factura;
import gob.mined.siap2.entities.data.impl.FacturaActaContratoOC;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.ModificativaContrato;
import gob.mined.siap2.entities.data.impl.Notificacion;
import gob.mined.siap2.entities.data.impl.NumeroComprobanteRecepcionDePAgo;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.PagoInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.QuedanEmitido;
import gob.mined.siap2.entities.data.impl.RelActaContratoCategoriaConvenio;
import gob.mined.siap2.entities.data.impl.RelActaItem;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siges.entities.data.impl.SgAfBienDepreciable;
import gob.mined.siap2.entities.data.impl.ValoresImpuestoQuedan;
import gob.mined.siap2.entities.enums.EstadoActa;
import gob.mined.siap2.entities.enums.EstadoCompromiso;
import gob.mined.siap2.entities.enums.EstadoContrato;
import gob.mined.siap2.entities.enums.EstadoModificativa;
import gob.mined.siap2.entities.enums.TipoActaContrato;
import gob.mined.siap2.entities.enums.TipoCompromisoPresupuestario;
import gob.mined.siap2.entities.enums.TipoContrato;
import gob.mined.siap2.entities.enums.TipoImpuesto;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.entities.enums.TipoPagoActa;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.AdminContatoDAO;
import gob.mined.siap2.persistence.dao.imp.MetodoAdquisicionDAO;
import gob.mined.siap2.persistence.dao.imp.POADAO;
import gob.mined.siap2.persistence.dao.imp.ProcesoAdquisicionDAO;
import gob.mined.siap2.persistence.dao.imp.UsuarioDAO;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
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
import org.apache.commons.lang.StringUtils;

/**
 * Esta clase incluye los métodos para contratos.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "ContratoBean")
@LocalBean
public class ContratoBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private ProcesoAdquisicionDAO pAdqDao;
    @Inject
    @JPADAO
    private POADAO poadao;
    @Inject
    @JPADAO
    private AdminContatoDAO adminContatoDAO;
    @Inject
    @JPADAO
    private MetodoAdquisicionDAO madao;
    @Inject
    private DatosUsuario usrBean;
    @Inject
    private ArchivoBean archivoBean;
    @Inject
    private ReporteBean reporteBean;
    @Inject
    private DatosUsuario datosUsuario;
    @Inject
    private UsuarioBean usuarioBean;
    @Inject
    private LogAuditoriaBean logBean;
    @Inject
    @JPADAO
    private UsuarioDAO usuarioDAO;
    @Inject
    private ConfiguracionBean configuracionBean;

    @Inject
    private BienesDepreciablesBean bienesBean;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método agrega un contrato en el sistema.
     *
     * @param id id del contrato.
     * @return
     */
    public ContratoOC cargarContrato(Integer id) {
        try {
            ContratoOC contrato = (ContratoOC) generalDAO.find(ContratoOC.class, id);
            for (ActaContrato pago : contrato.getPagos()) {
                pago.getPagosInsumo().isEmpty();
            }
            for (ProcesoAdquisicionItem item : contrato.getItems()) {
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                for (RelacionProAdqItemInsumo relPAdqInsumo : item.getRelItemInsumos()) {
                    ProcesoAdquisicionInsumo pAdqInsumo = relPAdqInsumo.getInsumo();
                    if (pAdqInsumo.getPoInsumo().getFlujosDeCajaAnio() != null) {
                        for (FlujoCajaAnio fca : pAdqInsumo.getPoInsumo().getFlujosDeCajaAnio()) {
                            fca.getFlujoCajaMenusal().isEmpty();
                        }
                    }
                    pAdqInsumo.getRelItemInsumos().isEmpty();
                }
                item.getProveedorOfertaAdjudicadaId();
            }

            contrato.getImpuestos().isEmpty();

            contrato.getModificativas().isEmpty();

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
     * Este método retorna un acta del contrato
     *
     * @param id
     * @return
     */
    public ActaContrato cargarPago(Integer id) {
        try {
            ActaContrato pago = (ActaContrato) generalDAO.find(ActaContrato.class, id);
            pago.getPagosInsumo().isEmpty();
            pago.getFacturas().isEmpty();
            return pago;
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
     * Este método permite guardar un contrato. Reglas de la lógica: 1 - La
     * fecha de fin debe ser mayor o igual a la fecha de inicio 2 - La fecha de
     * emisión debe ser posterior a la fecha de inicio
     *
     * @param contrato
     * @return
     */
    public ContratoOC guardarContraro(ContratoOC contrato) {
        try {
            BusinessException b = new BusinessException();
            if (contrato.getFechasDesdeOrdenInicio()) {
                //La fecha de fin debe ser mayor o igual a la fecha de inicio
                if (contrato.getFechaFin().compareTo(contrato.getFechaInicio()) < 0) {
                    b.addError(ConstantesErrores.ERR_FECHA_FIN_ORDEN_INICIO_MENOR_FECHA_INICIO);
                }
                //La fecha de inicio debe ser mayor o igual a la fecha de emisión del contrato/orden de compra
                if (contrato.getFechaInicio().compareTo(contrato.getFechaEmision()) < 0) {
                    b.addError(ConstantesErrores.ERR_FECHA_INICIO_ORDEN_INICIO_MENOR_FECHA_EMISION);
                }
            }
            if (b.getErrores().size() > 0) {
                throw b;
            }
            if (contrato.getFechasDesdeOrdenInicio()) {
                Integer plazo = Math.round(DatesUtils.getDateDiff(contrato.getFechaInicio(), contrato.getFechaFin(), TimeUnit.DAYS));
                contrato.setPlazoEntrega(plazo);
            }
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
     * Este método guarda un acta asociada a un contrato
     *
     * @param acta acta a guardar
     * @param idContrato id del contrato
     * @return
     */
    public ActaContrato guardarPago(ActaContrato acta, Integer idContrato) {
        try {
            acta.setConBienesActivoFijo(Boolean.FALSE);
            List<SgAfBienDepreciable> bienes = new LinkedList();
            ContratoOC contrato = (ContratoOC) generalDAO.find(ContratoOC.class, idContrato);
            //Como cada acta tiene algunos datos especificos, dependiendo el tipo de esta, borro lo innsecesario
            acta = this.limpiarDatosInnecesariosSegunTipoActa(acta);
            if (acta.getFechaGeneracion().compareTo(new Date()) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_FECHAGENERACION_MAYOR_DIA_ACTUAL);
                throw b;
            }
            if (acta.getTipo().equals(TipoActaContrato.ANTICIPO)) {
                Integer porcentajeContrato = contrato.getPorcentajeAnticipo();
                if (porcentajeContrato == 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_NO_SE_PUEDE_CREAR_ACTA_DE_ANTICIPO_PORQUE_EL_CONTRATO_ES_SIN_ANTICIPO);
                    throw b;
                }
                if (acta.getTipoPago().equals(TipoPagoActa.PORCENTAJE)) {
                    if (acta.getPorcentaje() == null || acta.getPorcentaje().compareTo(BigDecimal.ZERO) <= 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_PORCENTAJE_ACTA_INCORRECTO);
                        throw b;
                    }
                    BigDecimal montoFijo = NumberUtils.porcentaje(acta.getPorcentaje(), contrato.getMontoAdjudicado(), RoundingMode.DOWN);
                    acta.setMontoRecibido(montoFijo);
                } else if (acta.getTipoPago().equals(TipoPagoActa.MONTO_FIJO)) {
                    if (acta.getMontoRecibido() == null || acta.getMontoRecibido().compareTo(BigDecimal.ZERO) <= 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_MONTO_FIJO_ACTA_INCORRECTO);
                        throw b;
                    }
                    BigDecimal porcentaje = acta.getMontoRecibido().multiply(new BigDecimal(100));
                    porcentaje = porcentaje.divide(contrato.getMontoAdjudicado(), 2, RoundingMode.DOWN);
                    acta.setPorcentaje(porcentaje);
                }
            } else if (acta.getTipo().equals(TipoActaContrato.RECEPCION)) {
                Integer cantidadRecibida = 0;
                BigDecimal montoRecibido = BigDecimal.ZERO;
                for (PagoInsumo pago : acta.getPagosInsumo()) {
                    if (pago.getCantRecibida() == null || pago.getCantRecibida() < 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_CANTIDAD_RECIBIDA_EN_ACTA_RECEPCION_INCORRECTA);
                        throw b;
                    }
                    if (pago.getImporte() == null || pago.getImporte().compareTo(BigDecimal.ZERO) < 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_MONTO_RECIBIDO_EN_ACTA_RECEPCION_INCORRECTO);
                        throw b;
                    }
                    if (pago.getCantRecibida().compareTo(pago.getRelacionItemInsumo().getCantidadAdjudicada()) > 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_CANT_RECIBIDA_EN_ACTA_RECEPCION_MAYOR_MONT_ADJ);
                        throw b;
                    }
                    if (pago.getImporte().compareTo(pago.getRelacionItemInsumo().getMontoTotalAdjudicado()) > 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_MONTO_RECIBIDO_EN_ACTA_RECEPCION_MAYOR_MONT_ADJ);
                        throw b;
                    }
                    cantidadRecibida += pago.getCantRecibida();
                    montoRecibido = montoRecibido.add(pago.getImporte());
                }
                acta.setCantidadRecibida(cantidadRecibida);
                acta.setMontoRecibido(montoRecibido);
                
                for (RelActaItem relActa : acta.getRelActaItem()) {
                    ProcesoAdquisicionItem item = relActa.getItem();
                    Integer cantidadRecibidaItem = 0;
                    BigDecimal montoRecibidoItem = BigDecimal.ZERO;
                    
                    String proveedor =  item.getProveedorOfertaAdjudicadaId() != null && item.getProveedorOfertaAdjudicadaId().getProcesoAdquisicionProveedor() != null 
                                        && item.getProveedorOfertaAdjudicadaId().getProcesoAdquisicionProveedor().getProveedor() != null?
                                        item.getProveedorOfertaAdjudicadaId().getProcesoAdquisicionProveedor().getProveedor().getNombreComercial() : "";
                    for (PagoInsumo pago : relActa.getActa().getPagosInsumo()) {
                        RelacionProAdqItemInsumo rel = pago.getRelacionItemInsumo();
                        if (rel.getItem().getId().equals(item.getId())) {
                            cantidadRecibidaItem += pago.getCantRecibida();
                            montoRecibidoItem = montoRecibidoItem.add(pago.getImporte());
                            
                            //Generación de bien por cada insumo
                            Insumo insumo = rel.getInsumo() != null ? rel.getInsumo().getInsumo() : null;
                            if(insumo != null && insumo.getCorrespondeActivoFijo() != null && insumo.getCorrespondeActivoFijo()) {
                                acta.setConBienesActivoFijo(Boolean.TRUE);
                                SgAfBienDepreciable bien = new SgAfBienDepreciable();
                                if(pago.getCantRecibida() != null && pago.getCantRecibida() > 1){
                                    bien.setBdeCantidadLote(pago.getCantRecibida());
                                    bien.setBdeValorAdquisicion(pago.getImporte().divide(new BigDecimal(pago.getCantRecibida())));
                                } else {
                                    bien.setBdeCantidadLote(0);
                                    bien.setBdeValorAdquisicion(acta.getMontoRecibido());
                                }
                                
                                String numContrato = item.getContrato() != null ? String.valueOf(item.getContrato().getId()) : "";
                                if(relActa.getActa() != null && relActa.getActa().getNroActa() != null) {
                                    numContrato += "-"+ relActa.getActa().getNroActa();
                                }
                                
                                String descripcion = pago.getDescripcion() != null && StringUtils.isNotBlank(pago.getDescripcion()) ? pago.getDescripcion() : "";
                                if(StringUtils.isBlank(descripcion)) {
                                    descripcion = relActa.getDescripcion() != null && StringUtils.isNotBlank(relActa.getDescripcion()) ? relActa.getDescripcion() : "";
                                }
                                
                                bien.setBdeEsLote(Boolean.FALSE);
                                bien.setBdeTipoBien(insumo.getTipoBien());
                                bien.setBdeCategoriaFk(insumo.getTipoBien() != null ? insumo.getTipoBien().getTbiCategoriaBienes() : null);
                                bien.setBdeNumeroPartida("");
                                bien.setBdeEsValorEstimado(Boolean.FALSE);
                                bien.setBdeDocumentoAdquisicion(numContrato);
                                bien.setBdeFechaCreacion(new Date());
                                bien.setBdeFechaAdquisicion(acta.getFechaGeneracion());
                                bien.setBdeProveedor(proveedor);
                                bien.setBdeDescripcion(descripcion);
                                bienes.add(bien);
                            }
                        }
                        
                    }
                    relActa.setCantRecibida(cantidadRecibidaItem);
                    relActa.setImporte(montoRecibidoItem);
                }

            } else if (acta.getTipo().equals(TipoActaContrato.DEVOLUCION)) {

                Boolean existeOtraActaDevolucion = false;
                Iterator<ActaContrato> itActasContrato = contrato.getPagos().iterator();
                while (itActasContrato.hasNext() && !existeOtraActaDevolucion) {
                    ActaContrato actaContrato = itActasContrato.next();
                    if (actaContrato.getId() != acta.getId() && actaContrato.getTipo().equals(TipoActaContrato.DEVOLUCION)) {
                        existeOtraActaDevolucion = true;
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERROR_YA_EXISTE_ACTA_DEVOLUCION);
                        throw b;
                    }
                }
                Integer porcentajeContrato = contrato.getPorcentajeDevolucion();
                if (porcentajeContrato == 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_NO_SE_PUEDE_CREAR_ACTA_DE_DEVOLUCION_PORQUE_EL_CONTRATO_ES_SIN_DEVOLUCION);
                    throw b;
                }
                if (acta.getTipoPago().equals(TipoPagoActa.PORCENTAJE)) {
                    if (acta.getPorcentaje() == null || acta.getPorcentaje().compareTo(BigDecimal.ZERO) <= 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_MONTO_FIJO_ACTA_INCORRECTO);
                        throw b;
                    }
                    BigDecimal montoFijo = NumberUtils.porcentaje(acta.getPorcentaje(), contrato.getMontoAdjudicado(), RoundingMode.DOWN);
                    acta.setMontoRecibido(montoFijo);
                } else if (acta.getTipoPago().equals(TipoPagoActa.MONTO_FIJO)) {
                    if (acta.getMontoRecibido() == null || acta.getMontoRecibido().compareTo(BigDecimal.ZERO) <= 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_MONTO_FIJO_ACTA_INCORRECTO);
                        throw b;
                    }
                    BigDecimal porcentaje = acta.getMontoRecibido().multiply(new BigDecimal(100));
                    porcentaje = porcentaje.divide(contrato.getMontoAdjudicado(), 2, RoundingMode.DOWN);
                    acta.setPorcentaje(porcentaje);
                }
            }
            //Si el estado no es null, es porque deberá quedar en estado revertida
            if (acta.getEstado() == null) {
                acta.setEstado(EstadoActa.EN_DIGITACION);
            }
            if (acta.getContratoOC() == null) {
                generalDAO.lock(SsLock.class, ConstantesEstandares.LOCK_SECUENCIA_PAGO_CONTRATO);

                List<Integer> l = adminContatoDAO.getMaxPagoId(idContrato);
                Integer nroPago = 1;
                if (l != null && !l.isEmpty() && l.get(0) != null) {
                    nroPago = l.get(0) + 1;
                }
                acta.setNroActa(nroPago);
                acta.setContratoOC(contrato);
                contrato.getPagos().add(acta);
                if(bienes != null && !bienes.isEmpty() && bienes.size() > 0) {
                    bienesBean.guardarBienes(bienes);
                }
            } else {
                acta = (ActaContrato) generalDAO.update(acta); 
            }

            return acta;
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
     * Este método aplica un clear o null a los campos que no corresponden a un
     * tipo de acta
     *
     * @param acta
     * @return
     */
    private ActaContrato limpiarDatosInnecesariosSegunTipoActa(ActaContrato acta) {
        if (acta.getTipo().equals(TipoActaContrato.ANTICIPO)) {
            acta.setCantidadRecibida(null);
            acta.getPagosInsumo().clear();
            acta.getRelActaItem().clear();
            acta.setMesPago(null);
            acta.setAnioPago(null);
        } else if (acta.getTipo().equals(TipoActaContrato.RECEPCION)) {
            acta.setObservaciones(null);
            acta.setPorcentaje(null);
            acta.setTipoPago(null);
        } else if (acta.getTipo().equals(TipoActaContrato.DEVOLUCION)) {
            acta.setCantidadRecibida(null);
            acta.getPagosInsumo().clear();
            acta.getRelActaItem().clear();
            acta.setMesPago(null);
            acta.setAnioPago(null);
        }
        return acta;
    }

    /**
     * Este método registra la aprobación de un acta de anticipo
     *
     * @param contrato
     * @param acta
     */
    private void validarAprobacionActaAnticipo(ContratoOC contrato, ActaContrato acta) {

        boolean hayActaAprobada = false;
        Iterator<ActaContrato> itActasDelContrato = contrato.getPagos().iterator();
        while (itActasDelContrato.hasNext() && !hayActaAprobada) {
            ActaContrato otraActa = itActasDelContrato.next();
            if (otraActa.getEstado().equals(EstadoActa.APROBADA) && otraActa.getTipo().equals(TipoActaContrato.ANTICIPO)) {
                hayActaAprobada = true;
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_YA_EXISTE_ACTA_ANTICIPO_APROBADA);
                throw b;
            }
        }

        Integer porcentajeContrato = contrato.getPorcentajeAnticipo();
        if (acta.getTipoPago().equals(TipoPagoActa.PORCENTAJE)) {
            if (acta.getPorcentaje().compareTo(new BigDecimal(porcentajeContrato)) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PORCENTAJE_ANTICIPO_ACTA_SUPERA_PORCENTAJE_ANTICIPO_CONTRATO);
                throw b;
            }
        } else if (acta.getTipoPago().equals(TipoPagoActa.MONTO_FIJO)) {
            BigDecimal montoContratoSegunPorcentaje = contrato.getMontoAdjudicado().divide(new BigDecimal(100), 2, RoundingMode.DOWN);
            montoContratoSegunPorcentaje = montoContratoSegunPorcentaje.multiply(new BigDecimal(porcentajeContrato));
            if (acta.getMontoRecibido().compareTo(montoContratoSegunPorcentaje) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_MONTO_FIJO_ANTICIPO_ACTA_SUPERA_MONTO_FIJO_ANTICIPO_CONTRATO);
                throw b;
            }
        }

    }

    private void validarAprobacionActaRecepcion(ContratoOC contrato, ActaContrato acta) {
        BigDecimal montoTotalPagoFCM = acta.getMesPago().getMonto();

        //RECORRO TODAS LAS ACTAS DEL CONTRATO, ME QUEDO CON LAS QUE TIENEN EL PAGO pagoFCMARealizar ASOCIADO
        //Y CONTROLO QUE EL MONTO TOTAL RECIBIDO DE TODAS LAS ACTAS NO SEA SUPERIOR AL montoTotalPagoFCM
        BigDecimal montoTotalActasMismoPagoFCM = BigDecimal.ZERO;
        for (ActaContrato actaContrato : contrato.getPagos()) {
            if (actaContrato.getTipo().equals(TipoActaContrato.RECEPCION) && actaContrato.getEstado().equals(EstadoActa.APROBADA)) {
                //Evaluo todas las actas del contrato, excepto la actual (esto es para el caso de la edición)
                if (acta.getId() == null || !actaContrato.getId().equals(acta.getId())) {
                    if (actaContrato.getMesPago().getId().equals(acta.getMesPago().getId())) {
                        for (PagoInsumo pago : actaContrato.getPagosInsumo()) {
                            montoTotalActasMismoPagoFCM = montoTotalActasMismoPagoFCM.add(pago.getImporte());
                        }
                    }
                }
            }
        }
        //Al final sumo el importe de los pagos del acta que se va a guardar para ver si alcanzan los montos del pago FCM
        for (PagoInsumo pago : acta.getPagosInsumo()) {
            montoTotalActasMismoPagoFCM = montoTotalActasMismoPagoFCM.add(pago.getImporte());
        }
        //Si el total de importes de todas las actas que tienen asociado el mismo pago FCM, supera el importe del pago FCM, lanzo excepción
        if (montoTotalActasMismoPagoFCM.compareTo(montoTotalPagoFCM) > 0) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_IMPORTE_PAGOS_ACTAS_CON_MISMO_PAGO_ASOCIADO_SUPERA_PAGO_SELECCIONADO);
            throw b;
        }

        //Valido que las cantidades ingresadas en los pagos para cada insumo, no superen las cantidades adjudicadas
        for (ProcesoAdquisicionItem item : contrato.getItems()) {
            for (RelacionProAdqItemInsumo relacion : item.getRelItemInsumos()) {
                Integer cantidadTotalreRecibidaPorRelacion = 0;
                for (ActaContrato actaContrato : contrato.getPagos()) {
                    if (actaContrato.getEstado().equals(EstadoActa.APROBADA)) {
                        if (acta.getId() == null || !actaContrato.getId().equals(acta.getId())) {
                            for (PagoInsumo pagoInsumo : actaContrato.getPagosInsumo()) {
                                if (relacion.getId().equals(pagoInsumo.getRelacionItemInsumo().getId())) {
                                    cantidadTotalreRecibidaPorRelacion += pagoInsumo.getCantRecibida();
                                }
                            }
                        }
                    }
                }
                //Sumo la cantidad recibida del acta actual
                for (PagoInsumo pagoInsumoActaActual : acta.getPagosInsumo()) {
                    if (relacion.getId().equals(pagoInsumoActaActual.getRelacionItemInsumo().getId())) {
                        cantidadTotalreRecibidaPorRelacion += pagoInsumoActaActual.getCantRecibida();
                    }
                }
                if (cantidadTotalreRecibidaPorRelacion > relacion.getCantidadAdjudicada()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERROR_CANTIDAD_RECIBIDA_INSUMO_MAYOR_A_CANTIDAD_ADJUDICADA);
                    throw b;
                }
            }
        }

        //Valido que el monto recibido de cada insumo (en todas las actas), sumado al monto rescindido, no supere el monto adjudicado
        for (ProcesoAdquisicionItem item : contrato.getItems()) {
            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                BigDecimal montoRescindidoRel = rel.getMontoRescindido() != null ? rel.getMontoRescindido() : BigDecimal.ZERO;
                BigDecimal montoAdjudicadoRel = rel.getMontoTotalAdjudicado();
                BigDecimal totalrecibido = BigDecimal.ZERO;
                for (ActaContrato actaRec : contrato.getPagos()) {
                    if (actaRec.getTipo().equals(TipoActaContrato.RECEPCION) && actaRec.getEstado().equals(EstadoActa.APROBADA)) {
                        for (PagoInsumo pago : actaRec.getPagosInsumo()) {
                            if (pago.getRelacionItemInsumo().getId().equals(rel.getId())) {
                                totalrecibido = totalrecibido.add(pago.getImporte());
                            }
                        }
                    }
                }
                //Luego de sumar el monto recibido de todas las actas de recepción aprobadas para cada relación, sumo el monto recibido del acta actual
                for (PagoInsumo pago : acta.getPagosInsumo()) {
                    if (pago.getRelacionItemInsumo().getId().equals(rel.getId())) {
                        totalrecibido = totalrecibido.add(pago.getImporte());
                    }
                }
                if (montoRescindidoRel.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal totalRecibidoYrescindido = totalrecibido.add(montoRescindidoRel);
                    if (totalRecibidoYrescindido.compareTo(montoAdjudicadoRel) > 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_MONTO_RESCINDIDO_PARA_INSUMO_SUPERA_MONTO_INGRESADO_EN_ACTAS);
                        throw b;
                    }
                } else {
                    if (totalrecibido.compareTo(montoAdjudicadoRel) > 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERROR_MONTO_RECIBIDO_INSUMO_MAYOR_A_MONTO_ADJUDICADO);
                        throw b;
                    }
                }
            }
        }
    }

    private void validarAprobacionActaDevolucion(ContratoOC contrato, ActaContrato acta) {

        boolean hayActaAprobada = false;
        Iterator<ActaContrato> itActasDelContrato = contrato.getPagos().iterator();
        while (itActasDelContrato.hasNext() && !hayActaAprobada) {
            ActaContrato otraActa = itActasDelContrato.next();
            if (otraActa.getEstado().equals(EstadoActa.APROBADA) && otraActa.getTipo().equals(TipoActaContrato.DEVOLUCION)) {
                hayActaAprobada = true;
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_YA_EXISTE_ACTA_DEVOLUCION_APROBADA);
                throw b;
            }
        }

        Integer porcentajeContrato = contrato.getPorcentajeDevolucion();
        if (acta.getTipoPago().equals(TipoPagoActa.PORCENTAJE)) {
            if (acta.getPorcentaje().compareTo(new BigDecimal(porcentajeContrato)) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PORCENTAJE_DEVOLUCION_ACTA_SUPERA_PORCENTAJE_DEVOLUCION_CONTRATO);
                throw b;
            }
        } else if (acta.getTipoPago().equals(TipoPagoActa.MONTO_FIJO)) {
            BigDecimal montoContratoSegunPorcentaje = contrato.getMontoAdjudicado().divide(new BigDecimal(100), 2, RoundingMode.DOWN);
            montoContratoSegunPorcentaje = montoContratoSegunPorcentaje.multiply(new BigDecimal(porcentajeContrato));
            if (acta.getMontoRecibido().compareTo(montoContratoSegunPorcentaje) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_MONTO_FIJO_DEVOLUCION_ACTA_SUPERA_MONTO_FIJO_DEVOLUCION_CONTRATO);
                throw b;
            }
        }

    }

    /**
     * Este método elimina un pago (Acta de contrato)
     *
     * @param idPago
     */
    public void eliminarPago(Integer idPago) {
        try {
            ActaContrato pago = (ActaContrato) generalDAO.find(ActaContrato.class, idPago);
            if (!pago.getEstado().equals(EstadoActa.EN_DIGITACION)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_SOLO_SE_PUEDE_ELIMINAR_ACTA_EN_DIGITACION);
                throw b;
            }
            ContratoOC contratoOC = pago.getContratoOC();
            generalDAO.update(contratoOC);
            generalDAO.delete(pago);
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
     * Este método cierra un contrato
     *
     * @param id
     */
    public void cerrarContrato(Integer id) {
        try {
            ContratoOC contrato = (ContratoOC) generalDAO.find(ContratoOC.class, id);
            contrato.setEstado(EstadoContrato.CERRADO);

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
     * Este método guarda la orden de inicio en un contrato
     *
     * @param contrato
     * @return
     */
    public ContratoOC saveArchivoOrdenInicio(ContratoOC contrato) {
        ContratoOC contraoUpdate = (ContratoOC) generalDAO.find(ContratoOC.class, contrato.getId());

        try {
            contraoUpdate.setTempUploadedFile(contrato.getTempUploadedFile());
            if (contraoUpdate.getTempUploadedFile() != null) {
                if (contraoUpdate.getArchivoOrdenInicio() == null) {
                    contraoUpdate.setArchivoOrdenInicio(archivoBean.crearArchivo());
                }
                archivoBean.asociarArchivo(contraoUpdate.getArchivoOrdenInicio(), contrato.getTempUploadedFile(), false);
            }
            return contraoUpdate;
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
     * Este método notifica al responsable del proceso de adquisición que la
     * orden de inicio fue asignada
     *
     * @param contrato
     */
    public void notificarProcesoAdquisicionAsignadoAOrdenDeInicio(ContratoOC contrato) {
        try {
            if (contrato.getEstado().equals(EstadoContrato.EN_EJECUCION)) {
                SsUsuario u = contrato.getProcesoAdquisicion().getResponsable();
                Notificacion n = new Notificacion();
                n.setTipo(TipoNotificacion.PROCESO_ADQ_ASIGNADO_A_ORDEN_INICIO);
                n.setContenido("Tiene asignado el proceso de adquisición: '" + contrato.getProcesoAdquisicion().getNombre() + "' correspondiente a la orden de inicio del contrato Nro: " + contrato.getNroContrato());
                n.setUsuario(u);
                n.setFecha(new Date());
                n.setObjetoId(contrato.getProcesoAdquisicion().getId());
                generalDAO.update(n);
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
     * Este método valida los impuestos de un contrato
     *
     * @param contrato
     * @return
     */
    public ContratoOC validarImpuestosContrato(ContratoOC contrato) {
        try {
            contrato.setImpuestosValidados(true);

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
     * Este método crea un número de comprobante a un acta.
     *
     * @param idActaPago
     */
    public void crearNumeroActaPago(Integer idActaPago) {
        try {
            ActaContrato pago = (ActaContrato) generalDAO.find(ActaContrato.class, idActaPago);

            NumeroComprobanteRecepcionDePAgo numeroComprobantePago = new NumeroComprobanteRecepcionDePAgo();
            numeroComprobantePago.setActaPago(pago);

            numeroComprobantePago = (NumeroComprobanteRecepcionDePAgo) generalDAO.create(numeroComprobantePago);

            pago.setNumeroComprobanteRecepcionPago(numeroComprobantePago);
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
     * Este método guarda una factura
     *
     * @param idActaPago
     * @param factura
     * @return
     */
    public FacturaActaContratoOC guardarFactura(Integer idActaPago, FacturaActaContratoOC factura) {
        try {
            if (factura.getActaContrato() == null) {
                ActaContrato pago = (ActaContrato) generalDAO.find(ActaContrato.class, idActaPago);
                factura.setActaContrato(pago);
                pago.getFacturas().add(factura);
            } else {
                factura = (FacturaActaContratoOC) generalDAO.update(factura);
            }
            return (FacturaActaContratoOC) factura;

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
     * Este método elimina una factura
     *
     * @param idFactura
     */
    public void eliminarFactura(Integer idFactura) {
        try {
            FacturaActaContratoOC factura = (FacturaActaContratoOC) generalDAO.find(Factura.class, idFactura);
            factura.getActaContrato().getFacturas().remove(factura);

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
     * Al acta pasada por parámetro se le asigna el estado: Aprobada y se genera
     * un número de solicitud de pago
     *
     * @param idActa
     */
    public void aprobarActa(Integer idActa) {
        try {
            generalDAO.lock(SsLock.class, ConstantesEstandares.LOCK_SECUENCIA_SOLICITUD_PAGO_ACTA);
            ActaContrato acta = (ActaContrato) generalDAO.find(ActaContrato.class, idActa);
            ContratoOC contrato = acta.getContratoOC();

            if (acta.getTipo().equals(TipoActaContrato.ANTICIPO)) {
                try {
                    this.validarAprobacionActaAnticipo(contrato, acta);
                } catch (BusinessException be) {
                    throw be;
                }
            } else if (acta.getTipo().equals(TipoActaContrato.RECEPCION)) {
                try {
                    this.validarAprobacionActaRecepcion(contrato, acta);
                    this.actualizarMontoActaAprobadaEnPoInsumos(acta, true, true);
                } catch (BusinessException be) {
                    throw be;
                }

            } else if (acta.getTipo().equals(TipoActaContrato.DEVOLUCION)) {
                try {
                    this.validarAprobacionActaDevolucion(contrato, acta);
                } catch (BusinessException be) {
                    throw be;
                }
            }

            acta.setEstado(EstadoActa.APROBADA);

            List<Integer> l = adminContatoDAO.getMaxNumSolicitud(acta.getContratoOC().getId());
            Integer nroSolicitud = 1;
            if (l != null && !l.isEmpty() && l.get(0) != null) {
                nroSolicitud = l.get(0) + 1;
            }
            acta.setNumeroSolicitudPago(nroSolicitud);
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
     * Este método revierte un acta
     *
     * @param idActa
     */
    public void revertirActa(Integer idActa) {
        try {
            ActaContrato acta = (ActaContrato) generalDAO.find(ActaContrato.class, idActa);
            if (!acta.getEstado().equals(EstadoActa.APROBADA)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_NO_SE_PUEDE_REVERTIR_ACTA_NO_APROBADA);
                throw b;
            }
            Boolean quedanEmitido = acta.getQuedanEmitido();
            if (quedanEmitido == null || !quedanEmitido) {
                acta.setEstado(EstadoActa.REVERTIDA);
                acta.setNumeroSolicitudPago(null);
                if (acta.getTipo().equals(TipoActaContrato.RECEPCION)) {
                    this.actualizarMontoActaAprobadaEnPoInsumos(acta, false, true);
                }
            } else {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_QUEDAN_EMITIDO_POR_TESORERIA);
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
     * Cuando se aprueba o revierte un acta de recepción, se actualiza en el
     * PoInsumo el monto en el campo correspondiente
     *
     * @param acta
     * @param aprueba indica si aprueba o revierte
     * @param esActaRecepcion indica si es acta de recepción o si es un QUEDAN
     */
    private void actualizarMontoActaAprobadaEnPoInsumos(ActaContrato acta, Boolean aprueba, Boolean esActaRecepcion) {
        for (PagoInsumo pagoInsumo : acta.getPagosInsumo()) {
            POInsumos poInsumo = pagoInsumo.getRelacionItemInsumo().getInsumo().getPoInsumo();
            if (pagoInsumo.getImporte() != null) {
                if (esActaRecepcion) {
                    if (poInsumo.getMontoEnActasRecepcionAprobadas() == null) {
                        poInsumo.setMontoEnActasRecepcionAprobadas(BigDecimal.ZERO);
                    }
                    if (aprueba) {
                        BigDecimal montoActaRecAprobadoInsumo = poInsumo.getMontoEnActasRecepcionAprobadas().add(pagoInsumo.getImporte());
                        poInsumo.setMontoEnActasRecepcionAprobadas(montoActaRecAprobadoInsumo);
                    } else {
                        if (poInsumo.getMontoEnActasRecepcionAprobadas().compareTo(BigDecimal.ZERO) != 0) {
                            BigDecimal montoActaRecRevertidoInsumo = poInsumo.getMontoEnActasRecepcionAprobadas().subtract(pagoInsumo.getImporte());
                            poInsumo.setMontoEnActasRecepcionAprobadas(montoActaRecRevertidoInsumo);
                        }
                    }
                } else {
                    if (poInsumo.getMontoEnQUEDAN() == null) {
                        poInsumo.setMontoEnQUEDAN(BigDecimal.ZERO);
                    }
                    BigDecimal montoQuedanInsumo = poInsumo.getMontoEnQUEDAN().add(pagoInsumo.getImporte());
                    poInsumo.setMontoEnQUEDAN(montoQuedanInsumo);
                }

            }

        }
    }

    /**
     * Genera el comprobante de recepción de expediente de pago y guarda su
     * generación
     *
     * @param idActa
     * @return
     */
    public ComprobanteDeRecepcionDeExpedienteDePago generarComprobanteRecepcionExpedienteDepago(Integer idActa) {
        try {
            ActaContrato acta = (ActaContrato) generalDAO.find(ActaContrato.class, idActa);
            SsUsuario usuario = usuarioBean.obtenerSsUsuarioPorCodigo(datosUsuario.getCodigoUsuario());

            Archivo a = reporteBean.generarComprobanteDeRecepcionDeExpedienteDePago(acta, usuario);

            ComprobanteDeRecepcionDeExpedienteDePago comprobante = new ComprobanteDeRecepcionDeExpedienteDePago();
            comprobante.setActa(acta);
            comprobante.setFechaGeneracion(new Date());
            comprobante.setArchivo(a);

            comprobante = (ComprobanteDeRecepcionDeExpedienteDePago) generalDAO.create(comprobante);

            acta.setComprobanteDeRecepcionDeExpedienteDePago(comprobante);
            return comprobante;
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
     * Elimina el comprobante de recepción de expediente de pago
     *
     *
     * @param idActa
     * @return
     */
    public void eliminarComprobanteRecepcionExpedienteDepago(Integer idActa) {
        try {
            ActaContrato acta = (ActaContrato) generalDAO.find(ActaContrato.class, idActa);

            ComprobanteDeRecepcionDeExpedienteDePago comprobante = acta.getComprobanteDeRecepcionDeExpedienteDePago();
            acta.setComprobanteDeRecepcionDeExpedienteDePago(null);

            generalDAO.delete(comprobante);

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
     * Método que se encarga de generar el quedan, y actualizar los motos
     * utilizados hasta el momento
     *
     * @param idActa
     * @return
     */
    public QuedanEmitido generarQuedan(Integer idActa) {
        try {

            generalDAO.lock(SsLock.class, ConstantesEstandares.LOCK_GENERACION_QUEDAN);
            ActaContrato acta = (ActaContrato) generalDAO.find(ActaContrato.class, idActa);

            //se valida que se este en condiciones de generar el quedan
            if (acta.getQuedanEmitido() != null && acta.getQuedanEmitido().equals(Boolean.TRUE)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_QUEDAN_YA_FUE_EMITIDO);
                throw b;
            }

            if (acta.getNumeroComprobanteRecepcionPago() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PARA_DESCARGAR_REPORTE_ES_NECESARIO_GENERAR_NUMERO_COMPROBANTE_RECEPCION_PAGO);
                throw b;
            }

            BigDecimal totalFacturas = BigDecimal.ZERO;
            for (Factura factura : acta.getFacturas()) {
                totalFacturas = totalFacturas.add(factura.getImporte());
            }

            ContratoOC contrato = acta.getContratoOC();
            BigDecimal montoEsperadoFacturas = BigDecimal.ZERO;

            //Si el contrato tiene anticipo y el acta actual es de recepción, la suma de las facturas debe ser igual al monto del acta - % de anticipo que le correponde a esa acta.
            if (acta.getTipo().equals(TipoActaContrato.RECEPCION) && contrato.getPorcentajeAnticipo() != null && contrato.getPorcentajeAnticipo() > 0) {
                //La última acta de recepción aprobada es la que tiene el monto faltante para cubrir el monto del contrato.
                Boolean esUltimaActaRecepcionAprobada = false;
                BigDecimal montoTotalActasRecepcionConQuedan = BigDecimal.ZERO;
                BigDecimal montoTotalFacturasTodasLasActasConQuedan = BigDecimal.ZERO;
                for (ActaContrato actaConQuedan : contrato.getPagos()) {
                    if (actaConQuedan.getTipo() != null && actaConQuedan.getEstado() != null && actaConQuedan.getQuedanEmitido() != null
                            && actaConQuedan.getTipo().equals(TipoActaContrato.RECEPCION) && actaConQuedan.getEstado().equals(EstadoActa.APROBADA) && actaConQuedan.getQuedanEmitido()) {
                        if (actaConQuedan.getMontoRecibido() != null) {
                            montoTotalActasRecepcionConQuedan = montoTotalActasRecepcionConQuedan.add(actaConQuedan.getMontoRecibido());
                            for (Factura factura : actaConQuedan.getFacturas()) {
                                montoTotalFacturasTodasLasActasConQuedan = montoTotalFacturasTodasLasActasConQuedan.add(factura.getImporte());
                            }
                        }
                    }
                }

                //Le sumo al monto faltante para completar el monto del contrato, el monto del acta actual. 
                BigDecimal montoActasConQuedanMasActaActual = montoTotalActasRecepcionConQuedan.add(acta.getMontoRecibido());

                //Si el monto total del contrato es igual al de la suma de la vaiable anterior, entonces el acta actual es la última para generar el Quedan
                if (montoActasConQuedanMasActaActual.equals(contrato.getMontoAdjudicado())) {
                    esUltimaActaRecepcionAprobada = true;
                }

                BigDecimal montoADescontarPorAnticipo = BigDecimal.ZERO;

                if (esUltimaActaRecepcionAprobada) {
                    //Calculo el monto del contrato sacandole el monto por anticipo
                    montoADescontarPorAnticipo = NumberUtils.porcentaje(new BigDecimal(contrato.getPorcentajeAnticipo()), contrato.getMontoAdjudicado(), RoundingMode.DOWN);

                    BigDecimal montoContratoSinAnticipo = contrato.getMontoAdjudicado().subtract(montoADescontarPorAnticipo);

                    //Al monto del contrato sin el anticipo le quito lo que ya se ingresó en las facturas de las actas con quedan y eso es lo que deben sumar las facturas del acta actual
                    montoEsperadoFacturas = montoContratoSinAnticipo.subtract(montoTotalFacturasTodasLasActasConQuedan);

                } else {
                    montoADescontarPorAnticipo = NumberUtils.porcentaje(new BigDecimal(contrato.getPorcentajeAnticipo()), acta.getMontoRecibido(), RoundingMode.DOWN);//acta.getMontoRecibido().multiply(montoADescontarPorAnticipo);
                    montoEsperadoFacturas = acta.getMontoRecibido().subtract(montoADescontarPorAnticipo);
                }

                if (montoEsperadoFacturas == null || totalFacturas.compareTo(montoEsperadoFacturas) != 0) {
                    String[] params = {NumberUtils.nomberToString(totalFacturas), NumberUtils.nomberToString(montoEsperadoFacturas)};
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_PARA_EMITIR_QUEDAN_MONTO_TOTAL_FACTURAS_0_DEBE_COINCIDIR_CON_MONTO_ACTAS_MENOS_PORC_ANT_1, params);
                    throw b;
                }
            } else {
                //Si el acta no es de recepción o el contrato no tiene anticipo, la suma de montos de las facturas debe ser igual al monto del acta
                montoEsperadoFacturas = acta.getMontoRecibido();
                if (montoEsperadoFacturas == null || totalFacturas.compareTo(montoEsperadoFacturas) != 0) {
                    String[] params = {NumberUtils.nomberToString(totalFacturas), NumberUtils.nomberToString(acta.getMontoRecibido())};
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_PARA_EMITIR_QUEDAN_MONTOTOTAL_FACTURAS_0_DEBE_COINCIDIR_CON_MONTO_ACTA_1, params);
                    throw b;
                }
            }

            //Se calcula y guarda en el acta el monto correspondiente a GOES y el monto correspondiente a otras fuentes.
            //El calculo se hace en proporción al monto de cada pago respecto del monto certificado de las fuentes del insumo asociado al pago
            HashMap<Integer, BigDecimal[]> mapCategorias = new HashMap<>();

            for (PagoInsumo pago : acta.getPagosInsumo()) {
                List<POMontoFuenteInsumo> fuentesConCategoriaConvenio = getFuentesConCategoriaConvenio(pago.getRelacionItemInsumo().getInsumo().getPoInsumo().getMontosFuentes());
                BigDecimal montoTotalCertificadoInsumo = pago.getRelacionItemInsumo().getInsumo().getPoInsumo().getMontoTotalCertificado();
                //Si hay solo una fuente con categoria de convenio asociada, el monto del pago se va a sumar completo al monto GOES o al No GOES según corresponda
                if (fuentesConCategoriaConvenio.size() == 1) {
                    POMontoFuenteInsumo montoFuente = fuentesConCategoriaConvenio.get(0);
                    CategoriaConvenio catConvenio = montoFuente.getFuente().getCategoriaConvenio();
                    //Si la categoría ya se agregó al map, obtengo el monto y le agrego el monto del pago
                    if (mapCategorias.containsKey(catConvenio.getId())) {
                        //Obtengo lo acumulado hasta el momento para la categoría y le sumo el monto total del pago
                        if (InsumoUtils.esMontoDeGOES(montoFuente)) {
                            BigDecimal montoGoesFuenteTemp = mapCategorias.get(catConvenio.getId())[0];
                            mapCategorias.get(catConvenio.getId())[0] = montoGoesFuenteTemp.add(pago.getImporte());
                        } else {
                            BigDecimal montoOtrasFuentesTemp = mapCategorias.get(catConvenio.getId())[1];
                            mapCategorias.get(catConvenio.getId())[1] = montoOtrasFuentesTemp.add(pago.getImporte());
                        }
                        //Si la categoría aún no se agregó al map, creo el array y le seteo el monto del pago
                    } else {
                        BigDecimal[] montos = {BigDecimal.ZERO, BigDecimal.ZERO};
                        if (InsumoUtils.esMontoDeGOES(montoFuente)) {
                            montos[0] = pago.getImporte();
                        } else {
                            montos[1] = pago.getImporte();
                        }
                        mapCategorias.put(catConvenio.getId(), montos);
                    }
                } else if (fuentesConCategoriaConvenio.size() > 1) {
                    BigDecimal montoProporcionalSumado = BigDecimal.ZERO;
                    for (int i = 0; i < fuentesConCategoriaConvenio.size(); i++) {
                        POMontoFuenteInsumo montoFuente = fuentesConCategoriaConvenio.get(i);
                        CategoriaConvenio catConvenio = montoFuente.getFuente().getCategoriaConvenio();
                        BigDecimal proporcionPago = BigDecimal.ZERO;
                        if (i != fuentesConCategoriaConvenio.size() - 1) {
                            proporcionPago = pago.getImporte().divide(montoTotalCertificadoInsumo, 2, RoundingMode.DOWN);
                            proporcionPago = proporcionPago.multiply(montoFuente.getCertificado());
                            montoProporcionalSumado = montoProporcionalSumado.add(proporcionPago);
                            //Si es la última fuente, hago el redondeo: resto al monto del pago el monto proporcionado ya cargado
                        } else {
                            proporcionPago = pago.getImporte().subtract(montoProporcionalSumado);
                        }
                        if (mapCategorias.containsKey(catConvenio.getId())) {
                            //Obtengo lo acumulado hasta el momento para la categoría y le sumo la proporcion del pago-monto certificado de la fuente                            
                            if (InsumoUtils.esMontoDeGOES(montoFuente)) {
                                BigDecimal montoGoesFuenteTemp = mapCategorias.get(catConvenio.getId())[0];
                                mapCategorias.get(catConvenio.getId())[0] = montoGoesFuenteTemp.add(proporcionPago);
                            } else {
                                BigDecimal montoOtrasFuentesTemp = mapCategorias.get(catConvenio.getId())[1];
                                mapCategorias.get(catConvenio.getId())[1] = montoOtrasFuentesTemp.add(proporcionPago);
                            }
                            //Si la categoría aún no se agregó al map, creo el array y le seteo el monto del pago
                        } else {
                            BigDecimal[] montos = {BigDecimal.ZERO, BigDecimal.ZERO};
                            if (InsumoUtils.esMontoDeGOES(montoFuente)) {
                                montos[0] = proporcionPago;
                            } else {
                                montos[1] = proporcionPago;
                            }
                            mapCategorias.put(catConvenio.getId(), montos);
                        }
                    }
                }
            }
            //Instancio las entidades de relacion entre Acta y Categoría Convenio
            for (Map.Entry<Integer, BigDecimal[]> entry : mapCategorias.entrySet()) {
                Integer idCategoriaConvenio = entry.getKey();
                BigDecimal montoGoes = entry.getValue()[0];
                BigDecimal montoNoGoes = entry.getValue()[1];
                RelActaContratoCategoriaConvenio relActaCategoria = new RelActaContratoCategoriaConvenio();
                CategoriaConvenio catConvenio = (CategoriaConvenio) generalDAO.find(CategoriaConvenio.class, idCategoriaConvenio);
                relActaCategoria.setActaContrato(acta);
                relActaCategoria.setCategoriaConvenio(catConvenio);
                if (montoGoes == null) {
                    montoGoes = BigDecimal.ZERO;
                }
                if (montoNoGoes == null) {
                    montoNoGoes = BigDecimal.ZERO;
                }
                relActaCategoria.setMontoGoes(montoGoes);
                relActaCategoria.setMontoNoGoes(montoNoGoes);
                generalDAO.create(relActaCategoria);
                acta.getRelacionesActaCategoria().add(relActaCategoria);
                catConvenio.getRelacionesActaCategoria().add(relActaCategoria);
            }

            QuedanEmitido quedan = new QuedanEmitido();
            quedan.setActa(acta);
            quedan.setFechaGeneracion(new Date());
            quedan.setMontoquedan(acta.getMontoRecibido());
            quedan.setValoresImpuesto(new LinkedList());
            quedan.setNumeroComprobanteRecepcionPago(acta.getNumeroComprobanteRecepcionPago());

            /**
             * los valores y detalles de cada impuesto se hacen sobre el total
             * pagado. en el quedan se guarda ese desglose de los impuestos. el
             * total del acta de pago ya incluye los valores de los impuestos.
             * Por ello primero se calcula cuál es el total del pago sin
             * impuesto y luego lo que aporto cada impuesto de ese total
             */
            if (acta.getContratoOC().getProcesoAdquisicionProveedor().getProveedor().getPersonaJuridica() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NO_DEFINIDO_SI_PROVEEDOR_PERSONA_FISICA_O_JURDICA);
                throw b;
            }
            //se recorren los impuestos se inicializan y se calcula el total a restar del monto total
            Configuracion confCodigoPaisElSalvador = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.CODIGO_PAIS_PROVEEDOR_EL_SALVADOR);
            String codigoPaisElSalvador = confCodigoPaisElSalvador.getCnfValor().toUpperCase();
            BigDecimal totalARestar = BigDecimal.ZERO;
            BigDecimal totalPorcentaje = BigDecimal.ZERO;
            for (Impuesto impuesto : acta.getContratoOC().getImpuestos()) {
                ValoresImpuestoQuedan valorImpuesto = new ValoresImpuestoQuedan();
                valorImpuesto.setImpuesto(impuesto);
                valorImpuesto.setTipoImpuestoEnCodiguera(impuesto.getTipoImpuesto());
                valorImpuesto.setValorImpuestoEnCodiguera(impuesto.getValor());

                if (acta.getContratoOC().getProcesoAdquisicionProveedor().getProveedor().getPersonaJuridica() != null
                        && acta.getContratoOC().getProcesoAdquisicionProveedor().getProveedor().getPersonaJuridica()) {
                    if (acta.getContratoOC().getProcesoAdquisicionProveedor().getProveedor().getPais() == null
                            || acta.getContratoOC().getProcesoAdquisicionProveedor().getProveedor().getPais().toUpperCase().equals(codigoPaisElSalvador)) {
                        valorImpuesto.setPorcentajeRetencionEnCodiguera(impuesto.getPorcentajeRetencionPersonaJuridicaNacional());
                    } else {
                        valorImpuesto.setPorcentajeRetencionEnCodiguera(impuesto.getPorcentajeRetencionPersonaJuridicaExtranjera());
                    }
                } else {
                    if (acta.getContratoOC().getProcesoAdquisicionProveedor().getProveedor().getPais() == null
                            || acta.getContratoOC().getProcesoAdquisicionProveedor().getProveedor().getPais().toUpperCase().equals(codigoPaisElSalvador)) {
                        valorImpuesto.setPorcentajeRetencionEnCodiguera(impuesto.getPorcentajeRetencionPersonaFisicaNacional());
                    } else {
                        valorImpuesto.setPorcentajeRetencionEnCodiguera(impuesto.getPorcentajeRetencionPersonaFisicaExtranjera());
                    }
                }

                if (impuesto.getTipoImpuesto() == TipoImpuesto.PORCENTAJE) {
                    totalPorcentaje = totalPorcentaje.add(impuesto.getValor());
                } else {
                    totalARestar = totalARestar.add(impuesto.getValor());
                }

                valorImpuesto.setQuedan(quedan);
                quedan.getValoresImpuesto().add(valorImpuesto);
            }

            //calcula cuanto es el monto total del pago sin impuestos
            BigDecimal toalSinImpuestos = totalFacturas;
            if (totalPorcentaje.compareTo(BigDecimal.ZERO) != 0) {
                //hace una relga de 3 para saber cuál es el total sin impuestos
                BigDecimal porcentajeTotalConImpuestos = totalPorcentaje.add(NumberUtils.CIEN);
                toalSinImpuestos = toalSinImpuestos.multiply(NumberUtils.CIEN).divide(porcentajeTotalConImpuestos, 2, BigDecimal.ROUND_HALF_UP);

            }
            toalSinImpuestos = toalSinImpuestos.subtract(totalARestar);
            quedan.setMontoSinImpuestos(toalSinImpuestos);

            //se recorren los impuestos y se acutaliza cuanto fue cada impuesto lo que puso cada impuesto en el pago
            for (ValoresImpuestoQuedan valorQuedan : quedan.getValoresImpuesto()) {
                //en caso de que en el acta tenga motno
                if (quedan.getMontoSinImpuestos().compareTo(BigDecimal.ZERO) != 0) {
                    if (valorQuedan.getTipoImpuestoEnCodiguera() == TipoImpuesto.TAZA_FIJA) {
                        valorQuedan.setValorImpuestoEnPAgo(valorQuedan.getValorImpuestoEnCodiguera());
                    } else {
                        valorQuedan.setValorImpuestoEnPAgo(NumberUtils.porcentaje(valorQuedan.getValorImpuestoEnCodiguera(), quedan.getMontoSinImpuestos(), RoundingMode.HALF_UP));
                    }
                    valorQuedan.setValorRetencionEnPago(NumberUtils.porcentaje(valorQuedan.getPorcentajeRetencionEnCodiguera(), quedan.getMontoSinImpuestos(), RoundingMode.HALF_UP));
                } else {
                    //en caso de que no se pago nada se setean los impuestos en 0
                    valorQuedan.setValorImpuestoEnPAgo(BigDecimal.ZERO);
                    valorQuedan.setValorRetencionEnPago(BigDecimal.ZERO);
                }
            }
            
            // Se actualiza el monto Quedan en los insumos asociados a los pagos de acta
            this.actualizarMontoActaAprobadaEnPoInsumos(acta, null, false);
            
            /**
             * se crea el quedan
             */
            quedan = (QuedanEmitido) generalDAO.create(quedan);

            acta.setQuedan(quedan);
            acta.setQuedanEmitido(true);
            Archivo a = reporteBean.generarQUEDAN(acta);
            quedan.setArchivo(a);

            /**
             * se actualiza el monto ejecutado en el proceso
             *
             */
            ProcesoAdquisicion p = acta.getContratoOC().getProcesoAdquisicion();
            if (p.getMontoEjecutado() == null) {
                p.setMontoEjecutado(BigDecimal.ZERO);
            }
            if (contrato.getMontoEjecutado() == null) {
                contrato.setMontoEjecutado(BigDecimal.ZERO);
            }
            /**
             * Solo se suma el monto ejecutado en el contrato y en el proceso si
             * el acta es de recepción
             *
             */
            if (acta.getMontoRecibido() != null && acta.getTipo().equals(TipoActaContrato.RECEPCION)) {
                p.setMontoEjecutado(p.getMontoEjecutado().add(acta.getMontoRecibido()));
                contrato.setMontoEjecutado(contrato.getMontoEjecutado().add(acta.getMontoRecibido()));
            }

            return quedan;
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
     * Recorre una lista de fuentes de insumos de POA y devuelve otra con las
     * fuentes que tienen categoría de convenio asociada.
     *
     * @param listaFuentesPago
     * @return
     */
    private List<POMontoFuenteInsumo> getFuentesConCategoriaConvenio(List<POMontoFuenteInsumo> listaFuentesPago) {
        List<POMontoFuenteInsumo> fuentesConCategoriaConvenio = new LinkedList<>();
        for (POMontoFuenteInsumo montoFuente : listaFuentesPago) {
            if (montoFuente.getFuente() != null && montoFuente.getFuente().getCategoriaConvenio() != null) {
                fuentesConCategoriaConvenio.add(montoFuente);
            }
        }
        return fuentesConCategoriaConvenio;
    }

    /**
     * Método que se encarga de eliminar un quedan emitido
     *
     *
     * @param idActa
     * @return
     */
    public void eliminarQuedan(Integer idActa) {
        try {
            Long trn = logBean.loguear(null, "eliminarQuedan", null, String.format("Eliminar quedan con acta id: %s.", idActa), null);

            generalDAO
                    .lock(SsLock.class,
                            ConstantesEstandares.LOCK_GENERACION_QUEDAN);
            ActaContrato acta = (ActaContrato) generalDAO.find(ActaContrato.class,
                    idActa);

            //se valida que el quedan hall asido generado
            if (!acta.getQuedanEmitido().equals(Boolean.TRUE)) {
                logBean.loguear(trn, "eliminarQuedan", null, String.format("Error: quedan no emitido."), null);
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_QUEDAN_NO_EMITIDO);
                throw b;
            }

            //se acutaliza el monto ejecutado en el proceso
            ProcesoAdquisicion p = acta.getContratoOC().getProcesoAdquisicion();
            ContratoOC contrato = acta.getContratoOC();

            //Solo se suma el monto ejecutado en el contrato y en el proceso si el acta es de recepción
            if (acta.getMontoRecibido() != null && acta.getTipo().equals(TipoActaContrato.RECEPCION)) {
                p.setMontoEjecutado(p.getMontoEjecutado().subtract(acta.getMontoRecibido()));
                contrato.setMontoEjecutado(contrato.getMontoEjecutado().subtract(acta.getMontoRecibido()));
            }

            QuedanEmitido quedan = acta.getQuedan();

            acta.setQuedan(null);
            acta.setQuedanEmitido(false);

            //el quedan emitido no se elimina, solo se le saca al acta
            logBean.loguear(trn, "eliminarQuedan", null, String.format("Se realizado correctamente."), null);
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
     * Determina que: Si hay presupuesto pendiente de ejecutar. En caso que se
     * haya realizado retención, determina si se ejecuto la correspondiente
     * devolución. En caso que se haya realizado anticipo, determina si el total
     * del anticipo fue descontado en lo pagado
     *
     * @param contrato
     * @return
     */
    public Boolean sePuedeCerrarContrato(ContratoOC contrato) {
        Boolean sePuedeCerrar = contrato.getMontoAdjudicado().equals(contrato.getMontoEjecutado());

        if (sePuedeCerrar) {
            if (contrato.getPorcentajeAnticipo() > 0) {
                Iterator<ActaContrato> itActas = contrato.getPagos().iterator();
                boolean hayActaAnticipoAprobada = false;
                while (itActas.hasNext() && !hayActaAnticipoAprobada) {
                    ActaContrato acta = itActas.next();
                    if (acta.getTipo().equals(TipoActaContrato.ANTICIPO) && acta.getEstado().equals(EstadoActa.APROBADA)) {
                        if (acta.getQuedanEmitido() != null && acta.getQuedanEmitido()) {
                            hayActaAnticipoAprobada = true;
                        }
                    }
                }
                if (!hayActaAnticipoAprobada) {
                    sePuedeCerrar = false;
                }
            }
        }

        if (sePuedeCerrar) {
            if (contrato.getPorcentajeDevolucion() > 0) {
                Iterator<ActaContrato> itActas = contrato.getPagos().iterator();
                boolean hayActaDevAprobada = false;
                while (itActas.hasNext() && !hayActaDevAprobada) {
                    ActaContrato acta = itActas.next();
                    if (acta.getTipo().equals(TipoActaContrato.DEVOLUCION) && acta.getEstado().equals(EstadoActa.APROBADA)) {
                        if (acta.getQuedanEmitido() != null && acta.getQuedanEmitido()) {
                            hayActaDevAprobada = true;
                        }
                    }
                }
                if (!hayActaDevAprobada) {
                    sePuedeCerrar = false;
                }
            }
        }

        return sePuedeCerrar;
    }

    /**
     * Este método retorna lo pagado por un po insumo
     *
     * @param idPOInsumo
     * @return
     */
    public BigDecimal getMontoPagadoPorInsumo(Integer idPOInsumo) {
        try {
            BigDecimal res = BigDecimal.ZERO;
            List<PagoInsumo> list = adminContatoDAO.getPAgosFinalizadosPorInsumo(idPOInsumo);
            for (PagoInsumo pago : list) {
                if (pago.getImporte() != null) {
                    res = res.add(pago.getImporte());
                }
            }

            return res;
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
     * Este método cambia el estado de un contrato a ejecución
     *
     * @param idContratoOC
     */
    public void abrirContratoOC(Integer idContratoOC) {
        try {
            ContratoOC contrato = (ContratoOC) generalDAO.find(ContratoOC.class,
                    idContratoOC);
            contrato.setEstado(EstadoContrato.EN_EJECUCION);
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
     * Para poder rescindir un contrato/OC se controla que los pagos de cada
     * insumo en cada ítem (relación ítem insumo) no excedan el monto adjudicado
     * para esa relación si se suma el total recibido más el total rescindido.
     * Si no se excede, el contrato queda en estado rescindido (pudiendo
     * editarse)
     *
     * @param contrato
     * @return
     */
    public ContratoOC rescindirContratoOC(ContratoOC contrato) {
        try {
            if (contrato.getEstado().equals(EstadoContrato.RESCINDIDO)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_CONTRATO_YA_RESCINDIDO);
                throw b;
            }
            boolean montoResDistintoDeCero = false;
            Iterator<ProcesoAdquisicionItem> itItems = contrato.getItems().iterator();
            while (itItems.hasNext() && !montoResDistintoDeCero) {
                ProcesoAdquisicionItem item = itItems.next();
                Iterator<RelacionProAdqItemInsumo> itRel = item.getRelItemInsumos().iterator();
                while (itRel.hasNext() && !montoResDistintoDeCero) {
                    RelacionProAdqItemInsumo rel = itRel.next();
                    if (rel.getMontoRescindido() != null && rel.getMontoRescindido().compareTo(BigDecimal.ZERO) < 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_MONTO_RESCINDIDO_INSUMO_NEGATIVO);
                        throw b;
                    } else if (rel.getMontoRescindido() != null && rel.getMontoRescindido().compareTo(BigDecimal.ZERO) > 0) {
                        montoResDistintoDeCero = true;
                    }
                }
            }
            if (!montoResDistintoDeCero) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_MONTO_RESCINDIDO_CONTRATO_ES_CERO);
                throw b;
            }
            for (ProcesoAdquisicionItem item : contrato.getItems()) {
                for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                    BigDecimal montoRescindidoRel = rel.getMontoRescindido() != null ? rel.getMontoRescindido() : BigDecimal.ZERO;
                    BigDecimal montoAdjudicadoRel = rel.getMontoTotalAdjudicado();
                    if (montoRescindidoRel.compareTo(montoAdjudicadoRel) > 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_MONTO_RESCINDIDO_INSUMO_MAYOR_MONTO_ADJUDICADO);
                        throw b;
                    }
                    BigDecimal maximoRecibidoPosible = montoAdjudicadoRel.subtract(montoRescindidoRel);
                    BigDecimal totalrecibido = BigDecimal.ZERO;
                    for (ActaContrato acta : contrato.getPagos()) {
                        if (acta.getTipo().equals(TipoActaContrato.RECEPCION) && acta.getEstado().equals(EstadoActa.APROBADA)) {
                            for (PagoInsumo pago : acta.getPagosInsumo()) {
                                if (pago.getRelacionItemInsumo().getId().equals(rel.getId())) {
                                    totalrecibido = totalrecibido.add(pago.getImporte());
                                    if (totalrecibido.compareTo(maximoRecibidoPosible) > 0) {
                                        BusinessException b = new BusinessException();
                                        b.addError(ConstantesErrores.ERR_MONTO_RESCINDIDO_PARA_INSUMO_SUPERA_MONTO_INGRESADO_EN_ACTAS);
                                        throw b;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (contrato.getTempUploadedFile() != null) {
                contrato.setArchivoResicion(archivoBean.crearArchivo());
                archivoBean.asociarArchivo(contrato.getArchivoResicion(), contrato.getTempUploadedFile(), false);
            }

            contrato.setEstado(EstadoContrato.RESCINDIDO);
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
     * Devuelve el monto total de las actas (aprobadas y con quedan emitido) que
     * aparecen tras el filtro de la consulta de comprobante de retención de
     * impuestos
     *
     * @param proveedor
     * @param idImpuesto
     * @param fechaDesde
     * @param fechaHasta
     * @return
     */
    public BigDecimal getMontoTotalParaComprobanteRetencionImpuestosPorProv(Integer idProveedor, Integer idImpuesto, Date fechaDesde, Date fechaHasta) {
        List<ActaContrato> actasFiltradas = adminContatoDAO.getActasParaComprobanteRetencionImpuestosPorProv(idProveedor, idImpuesto, fechaDesde, fechaHasta);
        BigDecimal montoTotal = BigDecimal.ZERO;
        for (ActaContrato acta : actasFiltradas) {
            montoTotal = montoTotal.add(acta.getMontoRecibido());
        }
        return montoTotal;
    }

    /**
     * Habilita la extensión de plazo del contrato y guarda la nueva fecha de
     * fin (en caso que se haya modificado)
     *
     * @param idContrato
     * @param fechaFinParaExtender
     * @return
     */
    public void guardarExtensionPlazoContrato(Integer idContrato, Date fechaFinParaExtender) {
        try {
            ContratoOC contrato = (ContratoOC) generalDAO.find(ContratoOC.class,
                    idContrato);
            contrato.setExtensionPlazoContratoHabilitada(true);
            if (fechaFinParaExtender != null) {
                if (contrato.getFechaFin().compareTo(fechaFinParaExtender) > 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERROR_FECHA_EXTENSION_CONTRATO_INCORRECTA);
                    throw b;
                }
                contrato.setFechaFin(fechaFinParaExtender);
                Integer plazo = Math.round(DatesUtils.getDateDiff(contrato.getFechaInicio(), contrato.getFechaFin(), TimeUnit.DAYS));
                contrato.setPlazoEntrega(plazo);
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
     * Deshabilita la posibilidad de extenderle el plazo a un contrato
     *
     * @param idContrato
     */
    public void deshabilitarExtenderPlazoContrato(Integer idContrato) {
        try {
            ContratoOC contrato = (ContratoOC) generalDAO.find(ContratoOC.class,
                    idContrato);
            contrato.setExtensionPlazoContratoHabilitada(false);
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
     * Devuelve la lista de los POInsumos que no están agregados en un proceso
     * de adquisición, están en un PAC cerrado, tienen asociado un insumo que
     * está contenido en la lista pasada por parámetro y no están asociado a
     * ninguna modificativa
     *
     * @param listaIdInsumosPermitidos
     * @return
     */
    public List<POInsumos> getPoInsumosDisponiblesParaModificativasContratoOC(Integer idContrato, ModificativaContrato tempModificativa) {
        try {
            ContratoOC contrato = (ContratoOC) generalDAO.find(ContratoOC.class,
                    idContrato);

            LinkedList<RelacionProAdqItemInsumo> relacionesItemInsumos = new LinkedList();
            for (ProcesoAdquisicionItem item : contrato.getItems()) {
                relacionesItemInsumos.addAll(item.getRelItemInsumos());
            }

            Set<Integer> listaIdInsumosPermitidos = new HashSet<Integer>();
            for (RelacionProAdqItemInsumo rel : relacionesItemInsumos) {
                listaIdInsumosPermitidos.add(rel.getInsumo().getInsumo().getId());
            }

            Set<Integer> listaIdPoInsumosNoPermitidos = new HashSet<Integer>();
            if (tempModificativa.getPoInsumos() != null) {
                for (POInsumos poIns : tempModificativa.getPoInsumos()) {
                    listaIdPoInsumosNoPermitidos.add(poIns.getId());
                }
            }

            List<POInsumos> poInsumos = pAdqDao.getPoInsumosDisponiblesParaModificativasContratoOC(listaIdInsumosPermitidos, listaIdPoInsumosNoPermitidos);

            return poInsumos;
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
     * Devuelve una modificativa de Contrato/OC por id
     *
     * @param idModificativa
     * @return
     */
    public ModificativaContrato
            cargarModificativaContratoOC(Integer idModificativa) {
        ModificativaContrato modif = (ModificativaContrato) generalDAO.find(ModificativaContrato.class,
                idModificativa);
        modif.getPoInsumos().isEmpty();
        return modif;
    }

    /**
     * Guarda una modificativa en la base de datos
     *
     * @param tempModificativa
     */
    public ModificativaContrato
            guardarModificativa(ModificativaContrato tempModificativa, Integer idContrato, List<POInsumos> poInsumosAEliminarDeModificativa) {
        try {

            ContratoOC contrato = (ContratoOC) generalDAO.find(ContratoOC.class,
                    idContrato);

            if (tempModificativa.getPoInsumos() == null || tempModificativa.getPoInsumos().isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_MODIFICATIVA_SIN_INSUMOS_ASOCIADOS);
                throw b;

            }

            for (POInsumos poInsumo : tempModificativa.getPoInsumos()) {
                POInsumos poInsumoBase = (POInsumos) generalDAO.find(POInsumos.class,
                        poInsumo.getId());
                BusinessException bp = new BusinessException();

                //Verifico que el insumo no esté en un proceso de adquisición distinto al proceso del contrato actual
                if (poInsumoBase.getProcesoInsumo() != null) {
                    if (poInsumoBase.getProcesoInsumo().getProcesoAdquisicion() != null && contrato.getProcesoAdquisicion() != null) {
                        if (!poInsumoBase.getProcesoInsumo().getProcesoAdquisicion().getId().equals(contrato.getProcesoAdquisicion().getId())) {
                            bp.addError(ConstantesErrores.ERR_EXISTE_INSUMO_ASOCIADOS_A_PROCESO_ADQUISICION);
                        }
                    }
                }
                //Verifico que el inusmo no esté en una modificativa distinta a la actual
                if (poInsumoBase.getModificativa() != null) {
                    if (tempModificativa.getId() != null && !tempModificativa.getId().equals(poInsumoBase.getModificativa().getId())) {
                        bp.addError(ConstantesErrores.ERR_EXISTE_INSUMO_ASOCIADOS_A_OTRA_MODIFICATIVA);
                    }
                }

                if (!bp.getErrores().isEmpty()) {
                    throw bp;
                }
            }

            this.validarProgramacionPagosEnModificativa(contrato.getFechaInicio(), tempModificativa);

            if (tempModificativa.getId() == null) {
                tempModificativa.setFecha(new Date());
                tempModificativa.setEstado(EstadoModificativa.EN_DIGITACION);

                generalDAO
                        .lock(SsLock.class,
                                ConstantesEstandares.LOCK_SECUENCIA_MODIFICATIVA_CONTRATO);
                List<Integer> l = adminContatoDAO.getMaxModificativaId(idContrato);
                Integer nroModif = 1;
                if (l != null && !l.isEmpty() && l.get(0) != null) {
                    nroModif = l.get(0) + 1;
                }
                tempModificativa.setNumero(nroModif);

                tempModificativa.setContratoOC(contrato);

                contrato.getModificativas().add(tempModificativa);

                for (POInsumos poInsumo : tempModificativa.getPoInsumos()) {
                    POInsumos poInsumoBase = (POInsumos) generalDAO.find(POInsumos.class,
                            poInsumo.getId());
                    poInsumoBase.setModificativa(tempModificativa);

                    if (poInsumo.getProcesoInsumo() == null) {
                        ProcesoAdquisicionInsumo procesoInsumo = UtilsUACI.convertPOInsumosAProcesoAdquisicionInsumo(poInsumo);
                        poInsumoBase.setProcesoInsumo(procesoInsumo);
                        procesoInsumo.setProcesoAdquisicion(contrato.getProcesoAdquisicion());
                        contrato.getProcesoAdquisicion().getInsumos().add(procesoInsumo);

                    }
                }

            } else {
                for (POInsumos poInsumo : tempModificativa.getPoInsumos()) {
                    POInsumos poInsumoBase = (POInsumos) generalDAO.find(POInsumos.class,
                            poInsumo.getId());
                    poInsumoBase.setModificativa(tempModificativa);

                    if (poInsumo.getProcesoInsumo() == null) {
                        ProcesoAdquisicionInsumo procesoInsumo = UtilsUACI.convertPOInsumosAProcesoAdquisicionInsumo(poInsumo);
                        poInsumoBase.setProcesoInsumo(procesoInsumo);
                        procesoInsumo.setProcesoAdquisicion(contrato.getProcesoAdquisicion());
                        contrato.getProcesoAdquisicion().getInsumos().add(procesoInsumo);
                    }

                }
                tempModificativa = (ModificativaContrato) generalDAO.update(tempModificativa);

            }

            for (POInsumos poInsumoEliminar : poInsumosAEliminarDeModificativa) {
                POInsumos poInsumoEliminarBase = (POInsumos) generalDAO.find(POInsumos.class,
                        poInsumoEliminar.getId());
                poInsumoEliminarBase.setModificativa(null);
                //Si se eliminó un poInsumo que estaba en una modificatvia y que ya tenia un procesoInsumo asigando que pertencía al mismo proceso asociado al contrato
                if (poInsumoEliminar.getProcesoInsumo() != null) {
                    if (poInsumoEliminar.getProcesoInsumo().getProcesoAdquisicion().getId().equals(contrato.getProcesoAdquisicion().getId())) {
                        poInsumoEliminarBase.getProcesoInsumo().getProcesoAdquisicion().getInsumos().remove(poInsumoEliminarBase.getProcesoInsumo());
                        poInsumoEliminarBase.getProcesoInsumo().setProcesoAdquisicion(null);
                        poInsumoEliminarBase.getProcesoInsumo().setPoInsumo(null);
                        poInsumoEliminarBase.setProcesoInsumo(null);
                    }
                }
            }

            return tempModificativa;

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
     * Crea un compromiso presupuestario y lo asocia a una modificativa
     *
     * @param proId
     */
    public ModificativaContrato
            solicitarCompromisoPresupuestarioParaModificativa(Integer idModificativa) {
        try {
            ModificativaContrato modificativa = (ModificativaContrato) generalDAO.selectForUpdate(ModificativaContrato.class,
                    idModificativa);
            if (modificativa.getCompromisoPresupuestario() != null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_LA_MODIFICATIVA_YA_TIENE_UN_COMPROMISO_PRESUPUESTARIO_GENERADO);
                throw b;
            }

            CompromisoPresupuestarioModificativa compromiso = new CompromisoPresupuestarioModificativa();
            compromiso.setTipo(TipoCompromisoPresupuestario.MODIFICATIVA);
            compromiso.setEstado(EstadoCompromiso.PENDIENTE);
            compromiso.setModificativaContrato(modificativa);
            compromiso.setFechaSolicitud(new Date());

            modificativa.setCompromisoPresupuestario(compromiso);

            return modificativa;

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
     * Aprueba una modificativa, cambiándole el estado, actualizando el monto,
     * insumos y programación de pagos del contrato
     *
     * @param tempModificativa
     * @param poInsumosAEliminarDeModificativa
     * @return
     */
    public ModificativaContrato aprobarModificativa(ModificativaContrato tempModificativa, List<POInsumos> poInsumosAEliminarDeModificativa) {
        try {
            //Primero guardo los últimos cambios que se le hicieron a la modificativa
            ModificativaContrato modificativa = guardarModificativa(tempModificativa, tempModificativa.getContratoOC().getId(), poInsumosAEliminarDeModificativa);

            ContratoOC contrato = (ContratoOC) generalDAO.find(ContratoOC.class,
                    modificativa.getContratoOC().getId());

            BigDecimal montoTotalModificativa = this.calcularMontoTotalModificativa(tempModificativa.getId());

            // Actualizo el monto del contrato
            BigDecimal montoTotalAdjudicadoContrato = modificativa.getContratoOC().getMontoAdjudicado() != null ? modificativa.getContratoOC().getMontoAdjudicado() : BigDecimal.ZERO;
            montoTotalAdjudicadoContrato = montoTotalAdjudicadoContrato.add(montoTotalModificativa);
            contrato.setMontoAdjudicado(montoTotalAdjudicadoContrato);

            BigDecimal montoTotalComprometidoContrato = modificativa.getContratoOC().getMontoComprometido() != null ? modificativa.getContratoOC().getMontoComprometido() : BigDecimal.ZERO;
            montoTotalComprometidoContrato = montoTotalComprometidoContrato.add(montoTotalModificativa);
            contrato.setMontoComprometido(montoTotalComprometidoContrato);

            //Actualizo los insumos del contrato
            for (POInsumos poInsumo : modificativa.getPoInsumos()) {

                ProcesoAdquisicionItem itemContrato = ContratoUtils.getItemDeInsumoParaModificativa(modificativa.getContratoOC(), poInsumo);
                ProcesoAdquisicionInsumo procesoInsumo = poInsumo.getProcesoInsumo();

                procesoInsumo.setMontoTotalAdjudicado(poInsumo.getMontoTotalCertificado());
                procesoInsumo.setCantidadAdjudicada(poInsumo.getCantidad());

                RelacionProAdqItemInsumo relItemInsumo = new RelacionProAdqItemInsumo();
                relItemInsumo.setItem(itemContrato);
                relItemInsumo.setInsumo(procesoInsumo);
                relItemInsumo.setMontoUnitAdjudicado(poInsumo.getMontoUnit());
                relItemInsumo.setMontoTotalAdjudicado(poInsumo.getMontoTotalCertificado());
                relItemInsumo.setCantidadAdjudicada(poInsumo.getCantidad());
                relItemInsumo.setMontoTotalSolicitado(poInsumo.getMontoTotalCertificado());
                relItemInsumo.setCantidad(poInsumo.getCantidad());
                relItemInsumo.setFechaContratacion(modificativa.getFecha());

                procesoInsumo.getRelItemInsumos().add(relItemInsumo);
                itemContrato.getRelItemInsumos().add(relItemInsumo);

            }

            //Actualizo la programación de pagos del contrato
            //Fusiono los meses del contrato con los de la modificativa
            for (FlujoCajaAnio fcaContrato : contrato.getProgramacionPagosContrato()) {
                for (FlujoCajaAnio fcaModif : modificativa.getProgramacionPagos()) {
                    if (fcaContrato.getAnio().equals(fcaModif.getAnio())) {
                        for (POFlujoCajaMenusal fcmContrato : fcaContrato.getFlujoCajaMenusal()) {
                            for (POFlujoCajaMenusal fcmModif : fcaModif.getFlujoCajaMenusal()) {
                                if (fcmContrato.getMes().equals(fcmModif.getMes())) {
                                    if (fcmModif.getMes() != null) {
                                        if (fcmContrato.getMonto() == null) {
                                            fcmContrato.setMonto(BigDecimal.ZERO);
                                        }
                                        BigDecimal montoNuevoMes = fcmContrato.getMonto().add(fcmModif.getMonto());
                                        fcmContrato.setMonto(montoNuevoMes);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //Reviso si alguno de los años de la modificativa no está en el contrato y lo actualizo

            Set<FlujoCajaAnio> setAniosNoEstanEnContrato = new HashSet<>();
            Iterator<FlujoCajaAnio> itFCAModif = modificativa.getProgramacionPagos().iterator();
            Iterator<FlujoCajaAnio> itFCAContrato = contrato.getProgramacionPagosContrato().iterator();

            while (itFCAModif.hasNext()) {
                FlujoCajaAnio FCAModif = itFCAModif.next();
                boolean anioEsta = false;
                while (itFCAContrato.hasNext() && !anioEsta) {
                    FlujoCajaAnio FCAContrato = itFCAContrato.next();
                    if (FCAModif.getAnio().equals(FCAContrato.getAnio())) {
                        anioEsta = true;
                    }
                }
                if (!anioEsta) {
                    setAniosNoEstanEnContrato.add(FCAModif);
                }
            }

            for (FlujoCajaAnio fcaModif : setAniosNoEstanEnContrato) {
                FlujoCajaAnio fcaNuevo = new FlujoCajaAnio();
                fcaNuevo.setAnio(fcaModif.getAnio());
                fcaNuevo.setFlujoCajaMenusal(new LinkedList<POFlujoCajaMenusal>());
                for (POFlujoCajaMenusal fcmModif : fcaModif.getFlujoCajaMenusal()) {
                    POFlujoCajaMenusal fcmNuevo = new POFlujoCajaMenusal();
                    fcmNuevo.setMes(fcmModif.getMes());
                    fcmNuevo.setMonto(fcmModif.getMonto());
                    fcaNuevo.getFlujoCajaMenusal().add(fcmNuevo);
                }
                contrato.getProgramacionPagosContrato().add(fcaNuevo);
            }

            modificativa.setEstado(EstadoModificativa.APROBADA);

            modificativa = (ModificativaContrato) generalDAO.update(modificativa);
            return modificativa;
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
     * Calcula el monto total de una modificativa en base al monto certificado
     * de los insumos que tiene asociado
     *
     * @param idContrato
     * @return
     */
    public BigDecimal calcularMontoTotalModificativa(Integer idModificativa) {
        try {
            BigDecimal montoTotal = BigDecimal.ZERO;
            ModificativaContrato modificativa = (ModificativaContrato) generalDAO.find(ModificativaContrato.class,
                    idModificativa);
            for (POInsumos insumo : modificativa.getPoInsumos()) {
                if (insumo.getMontoTotalCertificado() != null) {
                    montoTotal = montoTotal.add(insumo.getMontoTotalCertificado());
                }
            }
            return montoTotal;
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
     * Valida que la programación de pagos de una modificativa sea correcta
     *
     * @param fechaInicioContraro
     * @param modificativa
     */
    public void validarProgramacionPagosEnModificativa(Date fechaInicioContraro, ModificativaContrato modificativa) {

        Set<FlujoCajaAnio> nuevaProgramacionPagos = modificativa.getProgramacionPagos();
        if (nuevaProgramacionPagos == null || nuevaProgramacionPagos.isEmpty()) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_FALTA_PROGRAMACION_PAGOS_MODIFICATIVA);
            throw b;
        }
        //si el contrato tiene fecha de inicio verifica que la programacion de pagos sea mayor a la fecha de inicio
        if (fechaInicioContraro != null) {
            Calendar calendarFechaInicioContrato = new GregorianCalendar();
            calendarFechaInicioContrato.setTime(fechaInicioContraro);
            Integer anioInicioContrato = calendarFechaInicioContrato.get(Calendar.YEAR);
            Integer mesInicioContrato = calendarFechaInicioContrato.get(Calendar.MONTH) + 1;
            //que todos los pagos se hagan luego de la fecha de inicio del contrato

            for (FlujoCajaAnio fc : nuevaProgramacionPagos) {
                if (fc.getAnio() == null) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EXISTE_PROGRAMACION_PAGO_SIN_ANIO);
                    throw b;
                }
                if (fc.getAnio().compareTo(anioInicioContrato) < 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_PAGOS_DEBEN_SER_POSTERIORES_A_FECHA_INCIO_CONTRATO);
                    throw b;
                }
                //Si es el mismo año verifico que los meses sean posteriores al del inicio del contrato
                if (fc.getAnio().equals(anioInicioContrato)) {
                    for (POFlujoCajaMenusal fcm : fc.getFlujoCajaMenusal()) {
                        if (fcm.getMonto() != null && fcm.getMonto().compareTo(BigDecimal.ZERO) > 0 && fcm.getMes().compareTo(mesInicioContrato) < 0) {
                            BusinessException b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_PAGOS_DEBEN_SER_POSTERIORES_A_FECHA_INCIO_CONTRATO);
                            throw b;
                        }
                    }
                }
            }
        }

        //que todas tengan años
        for (FlujoCajaAnio fc : nuevaProgramacionPagos) {
            if (fc.getAnio() == null || fc.getAnio().intValue() <= 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_PROGRAMACION_PAGO_SIN_ANIO);
                throw b;
            }
        }

        //que no existan años repetidos
        for (FlujoCajaAnio fc1 : nuevaProgramacionPagos) {
            for (FlujoCajaAnio fc2 : nuevaProgramacionPagos) {
                if (!fc1.equals(fc2) && fc1.getAnio().equals(fc2.getAnio())) {
                    String[] params = {fc1.getAnio().toString()};
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EXISTEN_DOS_PROGRAMACIONES_PAGO_PARA_ANIO_0, params);
                    throw b;
                }
            }
        }

        //que no se pase del monto de la modificativa
        BigDecimal totalProgramado = BigDecimal.ZERO;
        for (FlujoCajaAnio anio : nuevaProgramacionPagos) {
            for (POFlujoCajaMenusal mes : anio.getFlujoCajaMenusal()) {
                if (mes.getMonto() != null) {
                    totalProgramado = totalProgramado.add(mes.getMonto());
                }
            }
        }

        BigDecimal montoTotalCertModificativa = BigDecimal.ZERO;
        if (modificativa.getPoInsumos() != null) {
            for (POInsumos poInsumo : modificativa.getPoInsumos()) {
                if (poInsumo.getMontoTotalCertificado() != null) {
                    montoTotalCertModificativa = montoTotalCertModificativa.add(poInsumo.getMontoTotalCertificado());
                }
            }
        }

        if (totalProgramado.compareTo(montoTotalCertModificativa) != 0) {
            BusinessException b = new BusinessException();
            String[] params = {NumberUtils.nomberToString(totalProgramado), NumberUtils.nomberToString(montoTotalCertModificativa)};
            b.addError(ConstantesErrores.ERR_LA_SUMA_DE_PROGRAMACION_PAGOS_0_NO_COINCIDE_CON_MONTO_TOTAL_DE_LA_MODIFICATIVA_1, params);
            throw b;
        }
    }

    /**
     * Valida que la programación de pagos de un contrato sea correcta y la
     * guarda.
     *
     * @param contrato
     * @return
     */
    public ContratoOC guardarProgramacionDePagosDeContrato(ContratoOC contrato) {
        try {
            ContratoUtils.validarProgramacionPagosParaContrato(contrato, contrato.getProgramacionPagosContrato());
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
     * Este método se encarga de generar la reserva de fondos para un compromiso
     * presupuestario Calcula la distribución por fuentes de la reserva y la
     * guarda
     *
     * @param idCompromiso
     * @return
     * @throws GeneralException
     */
    public CompromisoPresupuestario
            generarReservaDeFondos(Integer idCompromiso) throws GeneralException {
        try {
            //se bloquea el compromiso presupuestario
            CompromisoPresupuestario compromiso = (CompromisoPresupuestario) generalDAO.selectForUpdate(CompromisoPresupuestario.class,
                    idCompromiso);

            if (compromiso.getReservaFondos() != null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_COMPORMISO_SELECIONADO_YA_TIENE_RESERVA_DE_FONDOS_GENERADA);
                throw b;
            }

            //calcula la distribuccion financiera
            List<DataDistribuccionProgramacionPagosContrato> res = ContratoUtils.getDistribuccionFinanciera(compromiso);

            for (DataDistribuccionProgramacionPagosContrato distContrato : res) {
                for (DistribucionProgramacionPagosMes distMes : distContrato.getDistribuccionMeses()) {
                    for (DistribucionProgramacionPagosItem distItem : distMes.getDistribuccionItems()) {
                        for (DistribucionProgramacionPagosInsumos distInsumo : distItem.getDistribuccionInsumos()) {
                            for (DistribucionMontoAdjudicado distFuente : distInsumo.getDistribuccion()) {
                                POMontoFuenteInsumo fuente = distFuente.getFuente();
                                POInsumos pOInsumo = fuente.getInsumo();

                                //se suma porque la districuccion se hace por contrato entonces un insumo puede caer en dos contratos y tener distitnos montos en cada fuente para esos contratos
                                if (fuente.getReservaFondos() == null) {
                                    fuente.setReservaFondos(BigDecimal.ZERO);
                                }
                                fuente.setReservaFondos(fuente.getReservaFondos().add(distFuente.getMonto()));

                                if (pOInsumo.getTotalReservaFondos() == null) {
                                    pOInsumo.setTotalReservaFondos(BigDecimal.ZERO);
                                }
                                pOInsumo.setTotalReservaFondos(pOInsumo.getTotalReservaFondos().add(distFuente.getMonto()));

                            }
                        }
                    }
                }
            }

            Archivo reservaFondos = reporteBean.generarReporteCompromisoAPartirProgramaiconPagos(compromiso, res);
            compromiso.setReservaFondos(reservaFondos);
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
     * Devuelve los insumos asociados al proceso o la modificativa del
     * compromiso presupuestario seleccionado
     *
     * @param idCompromisoPresupuestario
     * @return
     */
    public List<POInsumos> getPoInsumosCompromisoPresupuestario(Integer idCompromisoPresupuestario) {
        List<POInsumos> listaPoInsumosDelCompPresup = new LinkedList<>();
        CompromisoPresupuestario cp = (CompromisoPresupuestario) generalDAO.find(CompromisoPresupuestario.class,
                idCompromisoPresupuestario);
        if (cp.getTipo().equals(TipoCompromisoPresupuestario.PROCESO)) {
            CompromisoPresupuestarioProceso cpp = (CompromisoPresupuestarioProceso) cp;
            for (ProcesoAdquisicionInsumo procesoInsumo : cpp.getProcesoAdquisicion().getInsumos()) {
                POInsumos poInsumo = procesoInsumo.getPoInsumo();
                listaPoInsumosDelCompPresup.add(poInsumo);
            }
        } else {
            CompromisoPresupuestarioModificativa cpm = (CompromisoPresupuestarioModificativa) cp;
            for (POInsumos poInsumo : cpm.getModificativaContrato().getPoInsumos()) {
                listaPoInsumosDelCompPresup.add(poInsumo);
            }
        }
        return listaPoInsumosDelCompPresup;
    }

    /**
     * Valida el monto a descomprometer de cada insumo y actualiza el monto
     * comprometido
     */
    public void confirmarDescomprometerInsumos(List<POInsumos> listaPoInsumosADescomprometer) {
        try {
            Set<String> poInsumosDescomprometer = new LinkedHashSet<>();
            for (POInsumos poInsumo : listaPoInsumosADescomprometer) {
                if (poInsumo.getMontoComprometido() == null && poInsumo.getMontoADescomprometer() != null && poInsumo.getMontoADescomprometer().compareTo(BigDecimal.ZERO) != 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_INSUMO_NO_TIENE_MONTO_COMPROMETIDO);
                    throw b;
                }
                if (poInsumo.getMontoComprometido() != null && poInsumo.getMontoADescomprometer() != null && poInsumo.getMontoADescomprometer().compareTo(poInsumo.getMontoComprometido()) > 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_MONTO_A_DESCOMPROMETER_DEBE_SER_MENOR_A_MONTO_COMPROMETIDO);
                    throw b;
                }
                if (poInsumo.getMontoComprometido() != null && poInsumo.getMontoADescomprometer() != null && poInsumo.getMontoADescomprometer().compareTo(BigDecimal.ZERO) < 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_MONTO_A_DESCOMPROMETER_DEBE_SER_MAYOR_A_CERO);
                    throw b;
                }
                if (poInsumo.getMontoComprometido() != null && poInsumo.getMontoADescomprometer().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal nuevoMontoComprometido = poInsumo.getMontoComprometido().subtract(poInsumo.getMontoADescomprometer());
                    poInsumo.setMontoComprometido(nuevoMontoComprometido);
                    generalDAO.update(poInsumo);
                    poInsumosDescomprometer.add(poInsumo.getId() + "(" + NumberUtils.nomberToString(poInsumo.getMontoADescomprometer()) + ")");
                }
            }
            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConOperacion(ConstantesEstandares.Operaciones.RECIBIR_NOTIFICACIONES_PRESUPUESTO, null);
            for (SsUsuario usu : anotificar) {
                Notificacion notificacion = new Notificacion();
                notificacion.setTipo(TipoNotificacion.DESCOMPROMISO_INSUMO);
                notificacion.setUsuario(usu);
                notificacion.setFecha(new Date());
                String contenido = TextUtils.join(", ", poInsumosDescomprometer);
                if (contenido.length() >= 255) {
                    contenido = contenido.substring(0, 252) + "...";
                }
                notificacion.setContenido(contenido);
                notificacion = (Notificacion) generalDAO.update(notificacion);
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
     * Filtra la consulta de Contratos / OC
     *
     * @param nroContratoOC
     * @param tipoContratoOC
     * @param nombreProceso
     * @param fechaDesde
     * @param fechaHasta
     * @param proveedor
     * @param estadoContratoOC
     * @param nroInsumo
     * @param estadoContrato
     * @param usuarioCodigo
     * @param firstResult
     * @param maxResults
     * @param orderBy
     * @param ascending
     * @return
     */
    public List<ContratoOC> getConsultaContratosOC(Integer nroContratoOC, TipoContrato tipoContratoOC, String nombreProceso, Date fechaDesde, Date fechaHasta, String proveedor, EstadoContrato estadoContratoOC, Integer nroInsumo, EstadoContrato estadoContrato, String usuarioCodigo, Integer firstResult, Integer maxResults, String[] orderBy, boolean[] ascending) {
        logger.log(Level.INFO, "getConsultaContratosOC");
        try {
            if (usuarioBean.usuarioActualTieneOperacion(ConstantesEstandares.Operaciones.VER_TODOS_LOS_CONTRATOS)) {
                usuarioCodigo = null;
            }
            List<ContratoOC> res = adminContatoDAO.getConsultaContratosOC(nroContratoOC, tipoContratoOC, nombreProceso, fechaDesde, fechaHasta, proveedor, estadoContratoOC, nroInsumo, estadoContrato, usuarioCodigo, firstResult, maxResults, orderBy, ascending);
            res.isEmpty();
            return res;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve la cantidad de contratos que va a mostrar la consulta según los
     * filtros que hayan sido aplicados
     *
     * @param nroContratoOC
     * @param tipoContratoOC
     * @param nombreProceso
     * @param fechaDesde
     * @param fechaHasta
     * @param proveedor
     * @param estadoContratoOC
     * @param nroInsumo
     * @param estadoContrato
     * @param usuarioCodigo
     * @return
     */
    public long countConsultaContratosOC(Integer nroContratoOC, TipoContrato tipoContratoOC, String nombreProceso, Date fechaDesde, Date fechaHasta, String proveedor, EstadoContrato estadoContratoOC, Integer nroInsumo, EstadoContrato estadoContrato, String usuarioCodigo) {
        logger.log(Level.INFO, "countConsultaContratosOC");
        try {
            if (usuarioBean.usuarioActualTieneOperacion(ConstantesEstandares.Operaciones.VER_TODOS_LOS_CONTRATOS)) {
                usuarioCodigo = null;
            }
            return adminContatoDAO.countConsultaContratosOC(nroContratoOC, tipoContratoOC, nombreProceso, fechaDesde, fechaHasta, proveedor, estadoContratoOC, nroInsumo, estadoContrato, usuarioCodigo);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Comprueba si el usuario logueado es el administrador de determinado
     * contrato
     *
     * @param contrato
     * @param codigoUsuarioLogueado
     * @return
     */
    public Boolean usuarioEsAdministradorDelContrato(ContratoOC contrato, String codigoUsuarioLogueado) {
        Boolean esAdminDeContrato = false;
        SsUsuario usuLogueado = usuarioBean.obtenerSsUsuarioPorCodigo(codigoUsuarioLogueado);
        if (contrato.getAdministrador() != null) {
            if (usuLogueado.getUsuId().equals(contrato.getAdministrador().getUsuId())) {
                esAdminDeContrato = true;
            }
        }
        return esAdminDeContrato;
    }

    /**
     * Guarda la duración del insumo en el contrato
     *
     * @param relacionItemInsumoEnEdicion
     * @return
     */
    public RelacionProAdqItemInsumo
            guardarDuracionInsumoEnContrato(RelacionProAdqItemInsumo relacionItemInsumoEnEdicion) {
        try {
            RelacionProAdqItemInsumo relItemInsumo = (RelacionProAdqItemInsumo) generalDAO.find((RelacionProAdqItemInsumo.class), relacionItemInsumoEnEdicion.getId());
            relItemInsumo.setDuracionInsumoEnContrato(relacionItemInsumoEnEdicion.getDuracionInsumoEnContrato());
            return relItemInsumo;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

}
