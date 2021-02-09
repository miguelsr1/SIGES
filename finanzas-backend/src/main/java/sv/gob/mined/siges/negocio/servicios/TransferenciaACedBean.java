/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.TransferenciaCedAgrup;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroTransferenciaACed;
import sv.gob.mined.siges.negocio.validaciones.TransferenciaACedValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TransferenciaACedDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTransferenciaACed;

/**
 * Servicio que gestiona las transferencias los centros educativos
 *
 * @author Sofis Solutions
 */
@Stateless
public class TransferenciaACedBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTransferenciaACed
     * @throws GeneralException
     */
    public SgTransferenciaACed obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTransferenciaACed> codDAO = new CodigueraDAO<>(em, SgTransferenciaACed.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_A_CED);
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
                CodigueraDAO<SgTransferenciaACed> codDAO = new CodigueraDAO<>(em, SgTransferenciaACed.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_A_CED);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param tac SgTransferenciaACed
     * @return SgTransferenciaACed
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTransferenciaACed guardar(SgTransferenciaACed tac, Boolean dataSecurity) throws GeneralException {
        try {
            if (tac != null) {
                if (TransferenciaACedValidacionNegocio.validar(tac)) {
                    CodigueraDAO<SgTransferenciaACed> codDAO = new CodigueraDAO<>(em, SgTransferenciaACed.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(tac, tac.getTacPk() == null ? ConstantesOperaciones.CREAR_TRANSFERENCIAS_A_CED : ConstantesOperaciones.ACTUALIZAR_TRANSFERENCIAS_A_CED);
                    } else {
                        return codDAO.guardar(tac, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tac;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTransferenciaACed filtro) throws GeneralException {
        try {
            TransferenciaACedDAO codDAO = new TransferenciaACedDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_A_CED);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTransferenciaACed>
     * @throws GeneralException
     */
    public List<SgTransferenciaACed> obtenerPorFiltro(FiltroTransferenciaACed filtro) throws GeneralException {
        try {
            TransferenciaACedDAO codDAO = new TransferenciaACedDAO(em, seguridadBean);
            List<SgTransferenciaACed> listado = codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_A_CED);
            for (SgTransferenciaACed t : listado) {
                if (t.getTacCedFk() != null && t.getTacCedFk().getOaes() != null) {
                    t.getTacCedFk().getOaes().size();
                }

                if (t.getTacCedFk() != null && t.getTacCedFk().getSedPresupuestos() != null) {
                    t.getTacCedFk().getSedPresupuestos().size();
                    if (!t.getTacCedFk().getSedPresupuestos().isEmpty()) {
                        t.getTacCedFk().getSedPresupuestos().stream().forEach(p -> {
                            if (p.getPesDocumentos() != null) {
                                p.getPesDocumentos().size();
                            }
                        });
                    }
                }
            }
            return listado;
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
                CodigueraDAO<SgTransferenciaACed> codDAO = new CodigueraDAO<>(em, SgTransferenciaACed.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_TRANSFERENCIAS_A_CED);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Devuelve las transferencias de sedes agrupadas por la transferencia global
     *
     * @param idTransferencia Integer
     * @return
     * @throws Exception
     */
    public List<TransferenciaCedAgrup> obtenerTransferenciasAgrup(Integer idTransferencia,Long idDep) throws GeneralException {
        try {
            String squery = "select sed.sed_pk,sed.sed_codigo,sed.sed_nombre,sed.sed_habilitado, " +
                            "ROUND (sum(tced.tac_monto_autorizado),2) monto, " +
                            "CASE WHEN oae.oae_miembros_vigentes IS NULL then false else oae.oae_miembros_vigentes end as oae_miembros_vigentes, " +
                            "CASE WHEN oae.oae_miembros_vigentes IS NULL then NULL else oae.oae_fecha_vencimiento end as oae_fecha_vencimiento, " +
                            "CASE WHEN (select doc_pk from finanzas.sg_documentos_presupuesto where doc_presupuesto_fk=pre.pes_pk and doc_tipo_documento='CONVENIO') IS NULL  " +
                            "then 'PENDIENTE' else (select doc_estado_documento from finanzas.sg_documentos_presupuesto where doc_presupuesto_fk=pre.pes_pk and doc_tipo_documento='CONVENIO') end as convenio, " +
                            "CASE WHEN (select doc_pk from finanzas.sg_documentos_presupuesto where doc_presupuesto_fk=pre.pes_pk and doc_tipo_documento='CONGELACION') IS NULL  " +
                            "then 'PENDIENTE' else (select doc_estado_documento from finanzas.sg_documentos_presupuesto where doc_presupuesto_fk=pre.pes_pk and doc_tipo_documento='CONGELACION') end as ccf, " +
                            "(select (select count(*) from finanzas.sg_recibos " +
                            "where rec_transferencia_fk in (select tac_pk from finanzas.sg_transferencias_a_ced  tced1 " +
                            "inner join siap2.ss_transferencias_componente tac1 on tced1.tac_transferencia_fk=tac1.tc_id  " +
                            "where tced1.tac_ced_fk=sed.sed_pk and tac1.tc_transferencia="+idTransferencia +"))=(select count(*) from finanzas.sg_transferencias_a_ced  tced2  " +
                            "inner join siap2.ss_transferencias_componente tac2 on tced2.tac_transferencia_fk=tac2.tc_id  where tced2.tac_ced_fk=sed.sed_pk and tac2.tc_transferencia="+idTransferencia +")) as recibo, " +
                            "tac.tc_porcentaje " +
                            "from finanzas.sg_transferencias_a_ced tced " +
                            "inner join finanzas.sg_transferencia_direccion_departamental tde on tced.tac_transferencia_direccion_dep_fk=tde.tdd_pk " +
                            "inner join finanzas.sg_direcciones_departamentales dde on tde.tdd_direccion_dep_fk=dde.ded_pk " +
                            "inner join centros_educativos.sg_sedes sed on tced.tac_ced_fk=sed.sed_pk  " +
                            "inner join siap2.ss_transferencias_componente tac on tced.tac_transferencia_fk=tac.tc_id  " +
                            "left join centros_educativos.sg_organismo_administracion_escolar oae on sed.sed_pk=oae.oae_sede_fk and oae.oae_estado='APROBADO' " +
                            "left join finanzas.sg_presupuesto_escolar pre on pre.pes_sede_fk=tced.tac_ced_fk and pre.pes_estado='APROBADO' " +
                            "where tac.tc_transferencia="+idTransferencia + " and dde.ded_departamento_fk="+idDep+ " " +
                            "and tced.tac_pk not in (select rfc_transferencia_ced_fk from finanzas.sg_req_fond_ced)  " +
                            "group by sed.sed_pk,sed.sed_codigo,sed.sed_nombre,sed.sed_habilitado,oae.oae_pk,tac.tc_id,pre.pes_pk";

            Query query = em.createNativeQuery(squery);
            
            List resultado = query.getResultList();
            
            List<TransferenciaCedAgrup> respuesta = new ArrayList<>();

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
     * @param fila
     * @return
     */
    public TransferenciaCedAgrup transformarFilaEnDTO(Object[] fila) {

        TransferenciaCedAgrup ele = new TransferenciaCedAgrup();
        ele.setSedePk(new Long(fila[0].toString()));
        ele.setCodSede(fila[1].toString());
        ele.setNomSede(fila[2].toString());
        ele.setHabilitado(Boolean.valueOf(fila[3].toString()));
        ele.setMontoTotal((BigDecimal) fila[4]);
        ele.setOeaMiembrosVigente(Boolean.valueOf(fila[5].toString()));
        ele.setOeaFechaVencimiento(fila[6]!=null ? LocalDate.parse(fila[6].toString()) : null);
        ele.setConvenio(fila[7].toString());
        ele.setCcf(fila[8].toString());
        ele.setRecibo(Boolean.valueOf(fila[9].toString()));
        ele.setPorcentaje(fila[10]!=null ? fila[10].toString() : null);
        
        return ele;
    }

}
