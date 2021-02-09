/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.CajaChicaReporte;
import sv.gob.mined.siges.dto.LibroCajaChica;
import sv.gob.mined.siges.enumerados.EnumTipoMovimientoCajaChica;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroChequera;
import sv.gob.mined.siges.filtros.FiltroMovimientoCajaChica;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.MovimientoCajaChicaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MovimientoCajaChicaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCajaChica;
import sv.gob.mined.siges.persistencia.entidades.SgChequera;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoCajaChica;

/**
 * Servicio que gestiona los movimientos de caja chica
 *
 * @author Sofis Solutions
 */
@Stateless
public class MovimientoCajaChicaBean {

    private static final Logger LOGGER = Logger.getLogger(MovimientoCajaChicaBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private ChequeraBean chequeraBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMovimientoCajaChica
     * @throws GeneralException
     */
    public SgMovimientoCajaChica obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientoCajaChica> codDAO = new CodigueraDAO<>(em, SgMovimientoCajaChica.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_MOVIMIENTOSCAJACHICA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientoCajaChica> codDAO = new CodigueraDAO<>(em, SgMovimientoCajaChica.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_MOVIMIENTOSCAJACHICA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param mcc SgMovimientoCajaChica
     * @return SgMovimientoCajaChica
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMovimientoCajaChica guardar(SgMovimientoCajaChica mcc) throws GeneralException {
        try {
            BusinessException ge = new BusinessException();
            Boolean aplicaGuardar = Boolean.FALSE;
            if (mcc != null) {
                if (MovimientoCajaChicaValidacionNegocio.validar(mcc)) {
                    //Código encargado de realizar la mutua exclusión de los movimientos para la cuenta bancaria en edición
                    em.find(SgCajaChica.class, mcc.getMccCuentaFK().getBccPk(), LockModeType.PESSIMISTIC_WRITE);
                    LOGGER.log(Level.SEVERE, "ENTRO. 0.1-- >" + mcc.getMccCuentaFK().getBccOtroIngreso());
                    LOGGER.log(Level.SEVERE, "ENTRO. 0.2-- >" + mcc.getMccTipoMovimiento());
                    LOGGER.log(Level.SEVERE, "ENTRO. 0.3-- >" + mcc.getMccTipoMovimiento().getText());
                    LOGGER.log(Level.SEVERE, "ENTRO. 0.34- >" + aplicaGuardar);

                    if (mcc.getMccTipoMovimiento().equals(TipoMovimiento.HABER) && !mcc.getMccCuentaFK().getBccOtroIngreso() && mcc.getMccTipoIngreso().equals(EnumTipoMovimientoCajaChica.MANUAL)) {
                        LOGGER.log(Level.SEVERE, "ENTRO. 1");
                        FiltroChequera chequera = new FiltroChequera();
                        chequera.setSede(mcc.getMccCuentaFK().getBccSedeFk().getSedPk());
                        chequera.setChePk(mcc.getMccChequeraFK().getChePk());
                        chequera.setIncluirCampos(new String[]{
                            "chePk",
                            "cheSedeFk.sedPk",
                            "cheSedeFk.sedTipo",
                            "cheSedeFk.sedVersion",
                            "cheSerie",
                            "cheNumeroInicial",
                            "cheNumeroFinal",
                            "cheVersion"});
                        List<SgChequera> listChequeras = chequeraBean.obtenerPorFiltro(chequera);

                        LOGGER.log(Level.SEVERE, "ENTRO. 2 listChequeras-- >" + listChequeras.size());

                        LOGGER.log(Level.SEVERE, "aplicaGuardar- >" + aplicaGuardar);

                        if (!listChequeras.isEmpty()) {
                            if (listChequeras.size() > 0) {
                                for (int i = 0; i < listChequeras.size(); i++) {
                                    SgChequera che = listChequeras.get(i);
                                    if (mcc.getMccCheque() >= che.getCheNumeroInicial() && mcc.getMccCheque() <= che.getCheNumeroFinal()) {
                                        aplicaGuardar = Boolean.TRUE;
                                    }
                                }
                            }

                        }

                        if (aplicaGuardar) {
                            LOGGER.log(Level.SEVERE, "ENTRO. 3 aplicaGuardar-- >");
                            mcc = calcularSaldosMovimientos(mcc);
                            //return codDAO.guardar(mcc, ConstantesOperaciones.CREAR_MOVIMIENTOSCAJACHICA);
                        } else {
                            ge.addError("rangoInvalido", Errores.ERROR_CHEQUE_NO_EXITE);

                        }

                    } else {
                        mcc = calcularSaldosMovimientos(mcc);
                        //return codDAO.guardar(mcc, ConstantesOperaciones.CREAR_MOVIMIENTOSCAJACHICA);
                    }

                }
            }
            if (ge.getErrores().size() > 0) {
                throw ge;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mcc;
    }

    /**
     * Realiza el cálculo de saldos para todos los movimientos existentes.
     *
     * @param movimiento SgMovimientoCajaChica
     * @return SgMovimientoCajaChica
     */
    public SgMovimientoCajaChica calcularSaldosMovimientos(SgMovimientoCajaChica movimiento) {
        try {
            CodigueraDAO<SgMovimientoCajaChica> codDAO = new CodigueraDAO<>(em, SgMovimientoCajaChica.class);
            LocalTime time = LocalTime.now();
            movimiento.setMccFecha(movimiento.getMccFecha().with(time));
            //Se obtienen los demás movimientos para esta cuenta bancaria
            FiltroMovimientoCajaChica filtro = new FiltroMovimientoCajaChica();
            filtro.setMccCuentaFK(movimiento.getMccCuentaFK().getBccPk());
            filtro.setFirst(new Long(0));
            filtro.setOrderBy(new String[]{"mccFecha"});
            filtro.setAscending(new boolean[]{true});
            List<SgMovimientoCajaChica> listado = obtenerPorFiltro(filtro);
            movimiento = SaldoMovimientoActual(listado, movimiento);
            //Se guarda el movimiento en edición
            codDAO.guardar(movimiento, null);
            //Se verifican los saldos para los demás movimientos
            saldoMovimientosPosteriores(listado, movimiento);
            return movimiento;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Se realizan las operaciones para el movimiento actual.
     *
     * @param listado List<SgMovimientoCajaChica> listado
     * @param movimiento SgMovimientoCajaChica
     * @return SgMovimientoCajaChica
     */
    public SgMovimientoCajaChica SaldoMovimientoActual(List<SgMovimientoCajaChica> listado, SgMovimientoCajaChica movimiento) {
        BigDecimal montoDebe = BigDecimal.ZERO;
        BigDecimal montoHaber = BigDecimal.ZERO;
        if (!listado.isEmpty()) {
            List<SgMovimientoCajaChica> debe = listado.stream().filter(m -> m.getMccTipoMovimiento().equals(TipoMovimiento.DEBE) && (m.getMccFecha().isBefore(movimiento.getMccFecha()))).collect(Collectors.toList());
            List<SgMovimientoCajaChica> haber = listado.stream().filter(m -> m.getMccTipoMovimiento().equals(TipoMovimiento.HABER) && (m.getMccFecha().isBefore(movimiento.getMccFecha()))).collect(Collectors.toList());
            if (!debe.isEmpty()) {
                montoDebe = debe.stream().map(m -> m.getMccMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            if (!haber.isEmpty()) {
                montoHaber = haber.stream().map(m -> m.getMccMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
        }
        if (movimiento.getMccTipoMovimiento().equals(TipoMovimiento.DEBE)) {
            montoDebe = montoDebe.add(movimiento.getMccMonto());
        } else {
            montoHaber = montoHaber.add(movimiento.getMccMonto());
        }
        movimiento.setMccSaldo(montoHaber.subtract(montoDebe));
        return movimiento;
    }

    /**
     * Se actualizan los saldos para los movimientos más actuales.
     *
     * @param listado List<SgMovimientoCajaChica>
     * @param mov SgMovimientoCajaChica
     */
    public void saldoMovimientosPosteriores(List<SgMovimientoCajaChica> listado, SgMovimientoCajaChica mov) {
        try {
            listado = listado.stream().filter(m -> m.getMccFecha().isAfter(mov.getMccFecha())).collect(Collectors.toList());
            if (!listado.isEmpty()) {
                BigDecimal montoHaber = BigDecimal.ZERO;
                BigDecimal montoDebe = BigDecimal.ZERO;
                CodigueraDAO<SgMovimientoCajaChica> codDAO = new CodigueraDAO<>(em, SgMovimientoCajaChica.class);
                for (SgMovimientoCajaChica movimiento : listado) {
                    if (mov.getMccTipoMovimiento().equals(TipoMovimiento.DEBE)) {
                        montoDebe = mov.getMccMonto();
                    } else {
                        montoHaber = mov.getMccMonto();
                    }
                    movimiento.setMccSaldo(movimiento.getMccSaldo().add(montoHaber.subtract(montoDebe)));
                    codDAO.guardar(movimiento, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroMovimientoCajaChica filtro) throws GeneralException {
        try {
            MovimientoCajaChicaDAO codDAO = new MovimientoCajaChicaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMovimientoCajaChica>
     * @throws GeneralException
     */
    public List<SgMovimientoCajaChica> obtenerPorFiltro(FiltroMovimientoCajaChica filtro) throws GeneralException {
        try {
            MovimientoCajaChicaDAO codDAO = new MovimientoCajaChicaDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientoCajaChica> codDAO = new CodigueraDAO<>(em, SgMovimientoCajaChica.class);
                codDAO.eliminarPorId(id, "");
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMovimientoCuentaBancaria>
     * @throws GeneralException
     */
    public List<CajaChicaReporte> buscarParaReporte(LibroCajaChica libCaja) throws GeneralException {
        try {

            if (libCaja.getSubcomponetePk() != null) {

                Query libroBanco = em.createNativeQuery("select * from finanzas.vwLibroCajaChica where mcc_cuenta_fk = " + libCaja.getCajaPk()
                        + " and EXTRACT(YEAR FROM mcc_ult_mod_fecha) =  " + libCaja.getAnio() + "and bcc_subcomponente_fk = " + libCaja.getSubcomponetePk());
                List resultado = libroBanco.getResultList();

                List<CajaChicaReporte> respuesta = new ArrayList<>();

                resultado.forEach(
                        z -> {
                            Object[] fila = (Object[]) z;
                            respuesta.add(transformarFilaEnDTO(fila));
                        });
                return respuesta;

            } else {
                Query libroBanco = em.createNativeQuery("select * from finanzas.vwLibroCajaChica where mcc_cuenta_fk = " + libCaja.getCajaPk()
                        + " and EXTRACT(YEAR FROM mcc_ult_mod_fecha) =  " + libCaja.getAnio());
                List resultado = libroBanco.getResultList();

                List<CajaChicaReporte> respuesta = new ArrayList<>();

                resultado.forEach(
                        z -> {
                            Object[] fila = (Object[]) z;
                            respuesta.add(transformarFilaEnDTO(fila));
                        });
                return respuesta;
            }

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Transforma una fila en el objeto TransferenciaCedAgrup
     *
     * @param fila
     * @return
     */
    public CajaChicaReporte transformarFilaEnDTO(Object[] fila) {

        DateTimeFormatter df = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        String fecha1 = null;
        if (fila[1] != null) {
            fecha1 = fila[1].toString();
            String inputModified = fecha1.replace(" ", "T");
            int lengthOfAbbreviatedOffset = 3;
            if (inputModified.indexOf("+") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
                // If third character from end is a PLUS SIGN, append ':00'.
                inputModified = inputModified + ":00";
            }
            if (inputModified.indexOf("-") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
                // If third character from end is a PLUS SIGN, append ':00'.
                inputModified = inputModified + ":00";
            }
            fecha1 = inputModified;
        }

        String fecha2 = null;
        if (fila[8] != null) {
            fecha2 = fila[8].toString();
            String inputModified = fecha2.replace(" ", "T");
            int lengthOfAbbreviatedOffset = 3;
            if (inputModified.indexOf("+") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
                // If third character from end is a PLUS SIGN, append ':00'.
                inputModified = inputModified + ":00";
            }
            if (inputModified.indexOf("-") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
                // If third character from end is a PLUS SIGN, append ':00'.
                inputModified = inputModified + ":00";
            }
            fecha2 = inputModified;
        }

        CajaChicaReporte ele = new CajaChicaReporte();
        ele.setCajaPk(new Long(fila[0].toString()));
        ele.setFecha(LocalDateTime.parse(fecha1));
        ele.setaNombreDe(fila[2] != null ? fila[2].toString() : "");
        ele.setDetalle(fila[3] != null ? fila[3].toString() : "");
        ele.setTipo(fila[4] != null ? fila[4].toString() : "");
        ele.setMonto((BigDecimal) fila[5]);
        ele.setSaldo((BigDecimal) fila[6]);
        ele.setMovCajaPk(new Long(fila[7].toString()));
        ele.setUltModFecha(LocalDateTime.parse(fecha2));
        ele.setSubcomponentePk(new Long(fila[9] != null ? fila[9].toString() : ""));
        return ele;
    }

}
