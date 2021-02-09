/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.IngresosEgresosReporte;
import sv.gob.mined.siges.dto.LibroIngresosEgresos;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroTransferencia;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TransferenciaDAO;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsTransferencia;

/**
 * Servicio que gestiona las transferencias
 *
 * @author Sofis Solutions
 */
@Stateless
public class TransferenciasBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SsTransferencia
     * @throws GeneralException
     */
    public SsTransferencia obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SsTransferencia> codDAO = new CodigueraDAO<>(em, SsTransferencia.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_COMPONENTES);
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
                CodigueraDAO<SsTransferencia> codDAO = new CodigueraDAO<>(em, SsTransferencia.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_COMPONENTES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTransferencia filtro) throws GeneralException {
        try {
            TransferenciaDAO codDAO = new TransferenciaDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_COMPONENTES);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTransferencia>
     * @throws GeneralException
     */
    public List<SsTransferencia> obtenerPorFiltro(FiltroTransferencia filtro) throws GeneralException {
        try {
            TransferenciaDAO codDAO = new TransferenciaDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_COMPONENTES);
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
    public List<IngresosEgresosReporte> buscarParaReporte(LibroIngresosEgresos libTransferencia) throws GeneralException {
        try {
            Query libroBanco = em.createNativeQuery("select\n"
                    + "	stac.tac_ult_mod_fecha as fecha,\n"
                    + "	CAST (re.rec_pk  AS VARCHAR) as referencia,\n"
                    + "	sede.sed_codigo || ' - ' || sede.sed_nombre as concepto,\n"
                    + "	stac.tac_monto_autorizado as ingreso,\n"
                    + "	'0' as gasto,\n"
                    + "	stac.tac_monto_autorizado - 0 as saldo\n"
                    + "from\n"
                    + "	finanzas.sg_transferencias_a_ced stac \n"
                    + "	inner join siap2.ss_transferencias_componente tc on	tc.tc_id = stac.tac_transferencia_fk \n"
                    + "	inner join finanzas.sg_recibos re on re.rec_transferencia_fk = stac.tac_pk\n"
                    + "	inner join siap2.ss_anio_fiscal an on an.ani_id = tc.tc_anio_fiscal \n"
                    + "	inner join centros_educativos.sg_sedes sede on sede.sed_pk = stac.tac_ced_fk\n"
                    + "where\n"
                    + "	stac.tac_ced_fk = " + libTransferencia.getSedePk() + "\n"
                    + "	and an.ani_anio = " + libTransferencia.getAnio() + "\n"
                    + "	and tc.tc_subcomponente = (select tp_componente from siap2.ss_tope_presupestal	where tp_movimiento = " + libTransferencia.getMovTransferenciaPk() + " limit 1)\n"
                    + "	and tc.tc_linea_presupuestaria =(select tp_sub_cuenta from siap2.ss_tope_presupestal where tp_movimiento = " + libTransferencia.getMovTransferenciaPk() + " limit 1)\n"
                    + "union all\n"
                    + "select\n"
                    + "	sp.pgs_fecha_pago as fecha,\n"
                    + "	CAST (fac.fac_numero AS VARCHAR)  as referencia,\n"
                    + "	case when mov.mov_pk is null then (mov1.mov_fuente_recursos) else \n"
                    + "	(mov.mov_fuente_recursos)end as concepto1,\n"
                    + "	'0' as ingreso,\n"
                    + "	sp.pgs_importe as gasto,\n"
                    + "	'0' as saldo\n"
                    + "from\n"
                    + "	finanzas.sg_pagos sp inner join finanzas.sg_facturas fac on fac.fac_pk = sp.pgs_factura_fk\n"
                    + "	left join finanzas.sg_presupuesto_escolar_movimiento mov on	mov.mov_pk = fac.fac_item_movimiento \n"
                    + "	left join finanzas.sg_plan_compra plan on fac.fac_item_plan_compra = plan.com_pk\n"
                    + "	left join finanzas.sg_presupuesto_escolar_movimiento mov1 on mov1.mov_pk = plan.com_movimiento_fk\n"
                    + "where\n"
                    + "	mov.mov_fuente_ingreso_pk = " + libTransferencia.getMovTransferenciaPk() + "\n"
                    + "	or mov1.mov_fuente_ingreso_pk = " + libTransferencia.getMovTransferenciaPk() + "");

            List resultado = libroBanco.getResultList();

            List<IngresosEgresosReporte> respuesta = new ArrayList<>();

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
    public IngresosEgresosReporte transformarFilaEnDTO(Object[] fila) {

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
        IngresosEgresosReporte ele = new IngresosEgresosReporte();
        ele.setFecha(LocalDateTime.parse(fecha1));
        ele.setReferencia(fila[1].toString());
        ele.setConcepto(fila[2].toString());
        ele.setIngreso(fila[3].toString());
        ele.setGasto((BigDecimal) fila[4]);
        ele.setSaldo((BigDecimal) fila[5]);

        return ele;
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMovimientoCuentaBancaria>
     * @throws GeneralException
     */
    public List<IngresosEgresosReporte> buscarParaReporteComponentes(LibroIngresosEgresos libTransferencia) throws GeneralException {
        try {
            Query libroBanco = em.createNativeQuery("select\n"
                    + " 	stac.tac_ult_mod_fecha as fecha,\n"
                    + " 	CAST (re.rec_pk  AS VARCHAR) as referencia,\n"
                    + " 	sede.sed_codigo || ' - ' || sede.sed_nombre as concepto,\n"
                    + " 	stac.tac_monto_autorizado as ingreso,\n"
                    + " 	'0' as gasto,\n"
                    + " 	stac.tac_monto_autorizado - 0 as saldo\n"
                    + " from\n"
                    + " 	finanzas.sg_transferencias_a_ced stac \n"
                    + " 	inner join siap2.ss_transferencias_componente tc on	tc.tc_id = stac.tac_transferencia_fk \n"
                    + " 	inner join finanzas.sg_recibos re on re.rec_transferencia_fk = stac.tac_pk\n"
                    + " 	inner join siap2.ss_anio_fiscal an on an.ani_id = tc.tc_anio_fiscal \n"
                    + " 	inner join centros_educativos.sg_sedes sede on sede.sed_pk = stac.tac_ced_fk\n"
                    + " where	\n"
                    + "	stac.tac_ced_fk = " + libTransferencia.getSedePk() + "\n"
                    + "	and an.ani_anio = " + libTransferencia.getAnio() + "\n"
                    + " 	and tc.tc_componente = " + libTransferencia.getComponentePk() + "\n"
                    + " 	and tc.tc_subcomponente = " + libTransferencia.getSubComponentePk() + "\n"
                    + "\n"
                    + " union all\n"
                    + " select\n"
                    + " 	sp.pgs_fecha_pago as fecha,\n"
                    + " 	CAST (fac.fac_numero AS VARCHAR)  as referencia,\n"
                    + " 	case when mov.mov_pk is null then (mov1.mov_fuente_recursos) else \n"
                    + " 	(mov.mov_fuente_recursos)end as concepto1,\n"
                    + " 	'0' as ingreso,\n"
                    + " 	sp.pgs_importe as gasto,\n"
                    + " 	'0' as saldo\n"
                    + " from\n"
                    + " 	finanzas.sg_pagos sp inner join finanzas.sg_facturas fac on fac.fac_pk = sp.pgs_factura_fk\n"
                    + " 	left join finanzas.sg_presupuesto_escolar_movimiento mov on	mov.mov_pk = fac.fac_item_movimiento \n"
                    + " 	left join finanzas.sg_plan_compra plan on fac.fac_item_plan_compra = plan.com_pk\n"
                    + " 	left join finanzas.sg_presupuesto_escolar_movimiento mov1 on mov1.mov_pk = plan.com_movimiento_fk\n"
                    + " where\n"
                    + " 	mov.mov_fuente_ingreso_pk in (select pem.mov_pk from finanzas.sg_presupuesto_escolar_movimiento pem \n"
                    + "	left join siap2.ss_tope_presupestal tp on tp.tp_id = pem.mov_techo_presupuestal\n"
                    + "	left join siap2.ss_ges_pres_es sub on sub.ges_id = tp.tp_componente\n"
                    + "	left join siap2.ss_categoria_presupuesto_escolar com on com.cpe_id = sub.ges_categoria_componente\n"
                    + "	left join centros_educativos.sg_sedes sed on sed.sed_pk = tp.tp_sede\n"
                    + "	left join finanzas.sg_cuentas_bancarias cu on cu.cbc_sede_fk = sed.sed_pk\n"
                    + "	left join siap2.ss_anio_fiscal an on an.ani_id = tp.tp_anio_fiscal\n"
                    + "	where\n"
                    + "		com.cpe_id = " + libTransferencia.getComponentePk() + "\n"
                    + "		and tp.tp_componente =  " + libTransferencia.getSubComponentePk() + "\n"
                    + "		and an.ani_anio = 2021)\n"
                    + " 	or mov1.mov_fuente_ingreso_pk in (select pem.mov_pk from finanzas.sg_presupuesto_escolar_movimiento pem \n"
                    + "	left join siap2.ss_tope_presupestal tp on tp.tp_id = pem.mov_techo_presupuestal\n"
                    + "	left join siap2.ss_ges_pres_es sub on sub.ges_id = tp.tp_componente\n"
                    + "	left join siap2.ss_categoria_presupuesto_escolar com on com.cpe_id = sub.ges_categoria_componente\n"
                    + "	left join centros_educativos.sg_sedes sed on sed.sed_pk = tp.tp_sede\n"
                    + "	left join finanzas.sg_cuentas_bancarias cu on cu.cbc_sede_fk = sed.sed_pk\n"
                    + "	left join siap2.ss_anio_fiscal an on an.ani_id = tp.tp_anio_fiscal\n"
                    + "	where\n"
                    + "		com.cpe_id = " + libTransferencia.getComponentePk() + "\n"
                    + "		and	tp.tp_componente =  " + libTransferencia.getSubComponentePk() + "\n"
                    + "		and an.ani_anio = " + libTransferencia.getAnio() + ")");

            List resultado = libroBanco.getResultList();

            List<IngresosEgresosReporte> respuesta = new ArrayList<>();

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

}
