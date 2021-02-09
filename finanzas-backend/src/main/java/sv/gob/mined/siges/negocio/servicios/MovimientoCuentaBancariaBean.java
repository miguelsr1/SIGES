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
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.CuentaBancariaReporte;
import sv.gob.mined.siges.dto.LibroBanco;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.negocio.validaciones.MovimientoCuentaBancariaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MovimientoCuentaBancariaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCuentasBancarias;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoCuentaBancaria;

/**
 * Servicio que gestiona los movimientos de las cuentas bancarias
 *
 * @author Sofis Solutions
 */
@Stateless
public class MovimientoCuentaBancariaBean {

    private static final Logger LOGGER = Logger.getLogger(MovimientoCuentaBancariaBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMovimientoCuentaBancaria
     * @throws GeneralException
     */
    public SgMovimientoCuentaBancaria obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientoCuentaBancaria> codDAO = new CodigueraDAO<>(em, SgMovimientoCuentaBancaria.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_MOVIMIENTO_CUENTA_BANCARIA);
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
                CodigueraDAO<SgMovimientoCuentaBancaria> codDAO = new CodigueraDAO<>(em, SgMovimientoCuentaBancaria.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_MOVIMIENTO_CUENTA_BANCARIA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param mcb SgMovimientoCuentaBancaria
     * @return SgMovimientoCuentaBancaria
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMovimientoCuentaBancaria guardar(SgMovimientoCuentaBancaria mcb) throws GeneralException {
        try {
            if (mcb != null) {
                if (MovimientoCuentaBancariaValidacionNegocio.validar(mcb)) {
                    //Código encargado de realizar la mutua exclusión de los movimientos para la cuenta bancaria en edición
                    em.find(SgCuentasBancarias.class, mcb.getMcbCuentaFK().getCbcPk(), LockModeType.PESSIMISTIC_WRITE);
                    CodigueraDAO<SgMovimientoCuentaBancaria> codDAO = new CodigueraDAO<>(em, SgMovimientoCuentaBancaria.class);
                    mcb = calcularSaldosMovimientos(mcb);
                    return mcb;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mcb;
    }

    /**
     * Realiza el cálculo para todos los movimientos existentes.
     *
     * @param movimiento SgMovimientoCuentaBancaria
     * @return SgMovimientoCuentaBancaria
     */
    public SgMovimientoCuentaBancaria calcularSaldosMovimientos(SgMovimientoCuentaBancaria movimiento) {
        try {
            CodigueraDAO<SgMovimientoCuentaBancaria> codDAO = new CodigueraDAO<>(em, SgMovimientoCuentaBancaria.class);
            LocalTime time = LocalTime.now();
            movimiento.setMcbFecha(movimiento.getMcbFecha().with(time));
            //Se obtienen los demás movimientos para esta cuenta bancaria
            FiltroMovimientoCuentaBancaria filtro = new FiltroMovimientoCuentaBancaria();
            filtro.setMcbCuentaFK(movimiento.getMcbCuentaFK().getCbcPk());
            filtro.setFirst(new Long(0));
            filtro.setOrderBy(new String[]{"mcbFecha"});
            filtro.setAscending(new boolean[]{true});
            List<SgMovimientoCuentaBancaria> listado = obtenerPorFiltro(filtro);
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
     * @param listado List<SgMovimientoCuentaBancaria>
     * @param movimiento SgMovimientoCuentaBancaria
     * @return SgMovimientoCuentaBancaria
     */
    public SgMovimientoCuentaBancaria SaldoMovimientoActual(List<SgMovimientoCuentaBancaria> listado, SgMovimientoCuentaBancaria movimiento) {
        BigDecimal montoDebe = BigDecimal.ZERO;
        BigDecimal montoHaber = BigDecimal.ZERO;
        if (!listado.isEmpty()) {
            List<SgMovimientoCuentaBancaria> debe = listado.stream().filter(m -> m.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE) && (m.getMcbFecha().isBefore(movimiento.getMcbFecha()))).collect(Collectors.toList());
            List<SgMovimientoCuentaBancaria> haber = listado.stream().filter(m -> m.getMcbTipoMovimiento().equals(TipoMovimiento.HABER) && (m.getMcbFecha().isBefore(movimiento.getMcbFecha()))).collect(Collectors.toList());
            if (!debe.isEmpty()) {
                montoDebe = debe.stream().map(m -> m.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            if (!haber.isEmpty()) {
                montoHaber = haber.stream().map(m -> m.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
        }
        if (movimiento.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE)) {
            montoDebe = montoDebe.add(movimiento.getMcbMonto());
        } else {
            montoHaber = montoHaber.add(movimiento.getMcbMonto());
        }
        movimiento.setMcbSaldo(montoHaber.subtract(montoDebe));
        return movimiento;
    }

    /**
     * Se actualizan los saldos para los movimientos más actuales.
     *
     * @param listado List<SgMovimientoCuentaBancaria>
     * @param mov SgMovimientoCuentaBancaria
     */
    public void saldoMovimientosPosteriores(List<SgMovimientoCuentaBancaria> listado, SgMovimientoCuentaBancaria mov) {
        try {
            listado = listado.stream().filter(m -> m.getMcbFecha().isAfter(mov.getMcbFecha())).collect(Collectors.toList());
            if (!listado.isEmpty()) {
                BigDecimal montoHaber = BigDecimal.ZERO;
                BigDecimal montoDebe = BigDecimal.ZERO;
                CodigueraDAO<SgMovimientoCuentaBancaria> codDAO = new CodigueraDAO<>(em, SgMovimientoCuentaBancaria.class);
                for (SgMovimientoCuentaBancaria movimiento : listado) {
                    if (mov.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE)) {
                        montoDebe = mov.getMcbMonto();
                    } else {
                        montoHaber = mov.getMcbMonto();
                    }
                    movimiento.setMcbSaldo(movimiento.getMcbSaldo().add(montoHaber.subtract(montoDebe)));
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
    public Long obtenerTotalPorFiltro(FiltroMovimientoCuentaBancaria filtro) throws GeneralException {
        try {
            MovimientoCuentaBancariaDAO codDAO = new MovimientoCuentaBancariaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMovimientoCuentaBancaria>
     * @throws GeneralException
     */
    public List<SgMovimientoCuentaBancaria> obtenerPorFiltro(FiltroMovimientoCuentaBancaria filtro) throws GeneralException {
        try {
            MovimientoCuentaBancariaDAO codDAO = new MovimientoCuentaBancariaDAO(em);
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
                CodigueraDAO<SgMovimientoCuentaBancaria> codDAO = new CodigueraDAO<>(em, SgMovimientoCuentaBancaria.class);
                codDAO.eliminarPorId(id, "");
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Anula el objeto indicado
     *
     * @param entity SgMovimientoCuentaBancaria
     * @return SgMovimientoCuentaBancaria
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMovimientoCuentaBancaria cobrar(SgMovimientoCuentaBancaria entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {

                CodigueraDAO<SgMovimientoCuentaBancaria> codDAO = new CodigueraDAO<>(em, SgMovimientoCuentaBancaria.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.guardar(entity, entity.getMcbPk() == null ? ConstantesOperaciones.CREAR_MOVIMIENTO_CUENTA_BANCARIA : ConstantesOperaciones.ACTUALIZAR_MOVIMIENTO_CUENTA_BANCARIA);
                } else {
                    return codDAO.guardar(entity, null);
                }

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMovimientoCuentaBancaria>
     * @throws GeneralException
     */
    public List<CuentaBancariaReporte> buscarParaReporte(LibroBanco libBac) throws GeneralException {
        try {
            Query libroBanco = em.createNativeQuery("select * from finanzas.vwLibroBanco where mcb_cuenta_fk = " + libBac.getCuentaPk()
                    + " and EXTRACT(YEAR FROM mcb_ult_mod_fecha) =  " + libBac.getAnio());

            List resultado = libroBanco.getResultList();

            List<CuentaBancariaReporte> respuesta = new ArrayList<>();

            resultado.forEach(
                    z -> {
                        Object[] fila = (Object[]) z;
                        respuesta.add(transformarFilaEnDTO(fila));
                    });

            return respuesta;

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
    public CuentaBancariaReporte transformarFilaEnDTO(Object[] fila) {

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
        if (fila[9] != null) {
            fecha2 = fila[9].toString();
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

        CuentaBancariaReporte ele = new CuentaBancariaReporte();
        ele.setCuentaPk(new Long(fila[0].toString()));
        ele.setFecha(LocalDateTime.parse(fecha1));
        ele.setNoCheque(fila[2] != null ? fila[2].toString() : "");
        ele.setDetalle(fila[3] != null ? fila[3].toString() : "");
        ele.setaNombreDe(fila[4] != null ? fila[4].toString() : "");
        ele.setTipo(fila[5] != null ? fila[5].toString() : "");
        ele.setMonto((BigDecimal) fila[6]);
        ele.setSaldo((BigDecimal) fila[7]);
        ele.setMovCuentaPk(new Long(fila[8].toString()));
        ele.setUltModFecha(LocalDateTime.parse(fecha2));

        return ele;
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMovimientoCuentaBancaria>
     * @throws GeneralException
     */
    public List<CuentaBancariaReporte> buscarComponentes(LibroBanco libBac) throws GeneralException {
        try {
            Query libroBanco = em.createNativeQuery("select\n"
                    + "com.cpe_nombre as componente,\n"
                    + "sub.ges_nombre as subcomponente\n"
                    + "from finanzas.sg_presupuesto_escolar_movimiento pem\n"
                    + "LEFT join siap2.ss_tope_presupestal tp on tp.tp_id = pem.mov_techo_presupuestal \n"
                    + "LEFT join siap2.ss_ges_pres_es sub on sub.ges_id = tp.tp_componente \n"
                    + "LEFT join siap2.ss_categoria_presupuesto_escolar com on com.cpe_id = sub.ges_categoria_componente \n"
                    + "left join centros_educativos.sg_sedes sed on sed.sed_pk = tp.tp_sede \n"
                    + "left join finanzas.sg_cuentas_bancarias cu on cu.cbc_sede_fk = sed.sed_pk \n"
                    + "left join siap2.ss_anio_fiscal an on an.ani_id = tp.tp_anio_fiscal \n"
                    + "where tp.tp_componente = " + libBac.getComponentePk() + " \n"
                    + "and com.cpe_id = " + libBac.getSubComponentePk() + " \n"
                    + "and cu.cbc_pk  = " + libBac.getCuentaPk() + "\n"
                    + "and an.ani_anio = " + libBac.getAnio() + "");

            List resultado = libroBanco.getResultList();

            List<CuentaBancariaReporte> respuesta = new ArrayList<>();

            resultado.forEach(
                    z -> {
                        Object[] fila = (Object[]) z;
                        respuesta.add(transformarComponentesDTO(fila));
                    });

            return respuesta;

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
    public CuentaBancariaReporte transformarComponentesDTO(Object[] fila) {

        CuentaBancariaReporte ele = new CuentaBancariaReporte();
        ele.setComponente(fila[0] != null ? fila[0].toString() : "");
        ele.setSubComponente(fila[1] != null ? fila[1].toString() : "");

        return ele;
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMovimientoCuentaBancaria>
     * @throws GeneralException
     */
    public List<CuentaBancariaReporte> buscarParaReporteComponente(LibroBanco libBac) throws GeneralException {
        try {
            Query libroBanco = em.createNativeQuery("select\n"
                    + "	pem.mov_ult_mod_fecha as fecha,\n"
                    + "	cast ('N/A' as VARCHAR) as cheque,\n"
                    + "	sed.sed_nombre as anombrede,\n"
                    + "	'TRANSFERENCIA' as concepto,\n"
                    + "	pem.mov_monto_aprobado as ingreso,\n"
                    + "	cast ('0' as numeric) as egreso,\n"
                    + "	cast ((pem.mov_monto_aprobado - 0) as numeric) as saldo,\n"
                    + "	cast ('HABER' as VARCHAR) as tipo\n"
                    + "from\n"
                    + "	finanzas.sg_presupuesto_escolar_movimiento pem\n"
                    + "left join siap2.ss_tope_presupestal tp on tp.tp_id = pem.mov_techo_presupuestal\n"
                    + "left join siap2.ss_ges_pres_es sub on sub.ges_id = tp.tp_componente\n"
                    + "left join siap2.ss_categoria_presupuesto_escolar com on com.cpe_id = sub.ges_categoria_componente\n"
                    + "left join centros_educativos.sg_sedes sed on sed.sed_pk = tp.tp_sede\n"
                    + "left join finanzas.sg_cuentas_bancarias cu on cu.cbc_sede_fk = sed.sed_pk\n"
                    + "left join siap2.ss_anio_fiscal an on an.ani_id = tp.tp_anio_fiscal\n"
                    + "where\n"
                    + "	tp.tp_componente = " + libBac.getComponentePk() + "\n"
                    + "	and com.cpe_id = " + libBac.getSubComponentePk() + "\n"
                    + "	and cu.cbc_pk = " + libBac.getCuentaPk() + "\n"
                    + "	and an.ani_anio = " + libBac.getAnio() + "\n"
                    + "union all\n"
                    + "select\n"
                    + "	sp.pgs_fecha_pago as fecha,\n"
                    + "	case\n"
                    + "		when fac.fac_item_movimiento is null then cast ((sp.pgs_numero_cheque)as varchar)\n"
                    + "		else cast ((sp.pgs_numero_cheque) as varchar)\n"
                    + "	end as cheque,\n"
                    + "	pro.pro_nombre as anombrede,\n"
                    + "	case\n"
                    + "		when mov.mov_pk is null then (mov1.mov_fuente_recursos)\n"
                    + "		else (mov.mov_fuente_recursos)\n"
                    + "	end as concepto,\n"
                    + "	sp.pgs_importe as ingreso,\n"
                    + "	'0' as egreso,\n"
                    + "	cast ('0' as numeric) as saldo,\n"
                    + "	cast ('DEBE' as VARCHAR) as tipo\n"
                    + "from\n"
                    + "	finanzas.sg_pagos sp\n"
                    + "inner join finanzas.sg_facturas fac on 	fac.fac_pk = sp.pgs_factura_fk\n"
                    + "left join finanzas.sg_presupuesto_escolar_movimiento mov on mov.mov_pk = fac.fac_item_movimiento\n"
                    + "left join finanzas.sg_plan_compra plan on fac.fac_item_plan_compra = plan.com_pk\n"
                    + "left join finanzas.sg_presupuesto_escolar_movimiento mov1 on mov1.mov_pk = plan.com_movimiento_fk\n"
                    + "left join siap2.ss_proveedor pro on pro.pro_id = fac.fac_proveedor_fk\n"
                    + "where\n"
                    + "	mov.mov_fuente_ingreso_pk = (select pem.mov_pk from finanzas.sg_presupuesto_escolar_movimiento pem \n"
                    + "	left join siap2.ss_tope_presupestal tp on tp.tp_id = pem.mov_techo_presupuestal\n"
                    + "	left join siap2.ss_ges_pres_es sub on sub.ges_id = tp.tp_componente\n"
                    + "	left join siap2.ss_categoria_presupuesto_escolar com on com.cpe_id = sub.ges_categoria_componente\n"
                    + "	left join centros_educativos.sg_sedes sed on sed.sed_pk = tp.tp_sede\n"
                    + "	left join finanzas.sg_cuentas_bancarias cu on cu.cbc_sede_fk = sed.sed_pk\n"
                    + "	left join siap2.ss_anio_fiscal an on an.ani_id = tp.tp_anio_fiscal\n"
                    + "	where\n"
                    + "		tp.tp_componente = " + libBac.getComponentePk() + "\n"
                    + "		and com.cpe_id = " + libBac.getSubComponentePk() + "\n"
                    + "		and cu.cbc_pk = " + libBac.getCuentaPk() + "\n"
                    + "		and an.ani_anio = " + libBac.getAnio() + ")\n"
                    + "	and sp.pgs_modo_pago = 'CHEQUE'\n"
                    + "	or mov1.mov_fuente_ingreso_pk = (\n"
                    + "	select\n"
                    + "		pem.mov_pk\n"
                    + "	from\n"
                    + "		finanzas.sg_presupuesto_escolar_movimiento pem\n"
                    + "	left join siap2.ss_tope_presupestal tp on tp.tp_id = pem.mov_techo_presupuestal\n"
                    + "	left join siap2.ss_ges_pres_es sub on sub.ges_id = tp.tp_componente\n"
                    + "	left join siap2.ss_categoria_presupuesto_escolar com on com.cpe_id = sub.ges_categoria_componente\n"
                    + "	left join centros_educativos.sg_sedes sed on sed.sed_pk = tp.tp_sede\n"
                    + "	left join finanzas.sg_cuentas_bancarias cu on cu.cbc_sede_fk = sed.sed_pk\n"
                    + "	left join siap2.ss_anio_fiscal an on an.ani_id = tp.tp_anio_fiscal\n"
                    + "	where\n"
                    + "		tp.tp_componente = " + libBac.getComponentePk() + "\n"
                    + "		and com.cpe_id = " + libBac.getSubComponentePk() + "\n"
                    + "		and cu.cbc_pk = " + libBac.getCuentaPk() + "\n"
                    + "		and an.ani_anio = " + libBac.getAnio() + ")\n"
                    + "	and sp.pgs_modo_pago = 'CHEQUE'");

            List resultado = libroBanco.getResultList();

            List<CuentaBancariaReporte> respuesta = new ArrayList<>();

            resultado.forEach(
                    z -> {
                        Object[] fila = (Object[]) z;
                        respuesta.add(transformarFilaEnDTOComponentes(fila));
                    });

            return respuesta;

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
    public CuentaBancariaReporte transformarFilaEnDTOComponentes(Object[] fila) {

        DateTimeFormatter df = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        String fecha1 = null;
        if (fila[0] != null) {
            fecha1 = fila[0].toString();
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

        CuentaBancariaReporte ele = new CuentaBancariaReporte();

        ele.setFecha(LocalDateTime.parse(fecha1));
        ele.setNoCheque(fila[1] != null ? fila[1].toString() : "");
        ele.setaNombreDe(fila[2] != null ? fila[2].toString() : "");
        ele.setDetalle(fila[3] != null ? fila[3].toString() : "");
        ele.setMonto((BigDecimal) fila[4]);
        ele.setGasto((BigDecimal) fila[5]);
        ele.setSaldo((BigDecimal) fila[6]);
        ele.setTipo(fila[7] != null ? fila[7].toString() : "");

        return ele;
    }
}
