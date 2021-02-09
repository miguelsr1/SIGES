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
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesAyuda;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.DesembolsoCE;
import sv.gob.mined.siges.dto.DesembolsoMovimiento;
import sv.gob.mined.siges.enumerados.EnumDesembolsoEstado;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.filtros.FiltroCuentasBancarias;
import sv.gob.mined.siges.filtros.FiltroDesembolsoCed;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.DesembolsoCedValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DesembolsoCedDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCuentasBancarias;
import sv.gob.mined.siges.persistencia.entidades.SgDesembolsoCed;
import sv.gob.mined.siges.persistencia.entidades.SgDetalleDesembolso;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.persistencia.entidades.SgReqFondoCed;
import sv.gob.mined.siges.persistencia.entidades.SgTransferenciaACed;

/**
 * Servicio que gestiona los desembolsos
 * @author Sofis Solutions
 */
@Stateless
public class DesembolsoCedBean {
    

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean seguridadBean;
    
    @Inject 
    private CuentasBancariasBean cuentasBean;
    
    @Inject
    private MovimientoCuentaBancariaBean movCuentaBancBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDesembolsoCed
     * @throws GeneralException
     */
    public SgDesembolsoCed obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDesembolsoCed> codDAO = new CodigueraDAO<>(em, SgDesembolsoCed.class);
                return codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_DESEMBOLSO_CED);
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
                CodigueraDAO<SgDesembolsoCed> codDAO = new CodigueraDAO<>(em, SgDesembolsoCed.class);
                return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_DESEMBOLSO_CED);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param dce SgDesembolsoCed
     * @return SgDesembolsoCed
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgDesembolsoCed guardar(SgDesembolsoCed dce, Boolean dataSecurity) throws GeneralException {
        try {
            if (dce != null) {
                if (DesembolsoCedValidacionNegocio.validar(dce)) {
                    CodigueraDAO<SgDesembolsoCed> codDAO = new CodigueraDAO<>(em, SgDesembolsoCed.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(dce, dce.getDcePk() == null ? ConstantesOperaciones.CREAR_DETALLE_DESEMBOLSO : ConstantesOperaciones.ACTUALIZAR_DETALLE_DESEMBOLSO);
                    } else {
                        return codDAO.guardar(dce, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return dce;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgDesembolsoCed> codDAO = new CodigueraDAO<>(em, SgDesembolsoCed.class);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_DESEMBOLSO_CED);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgDesembolsoCed>
     * @throws GeneralException
     */
    public List<SgDesembolsoCed> obtenerPorFiltro(FiltroDesembolsoCed filtro) throws GeneralException {
        try {
            DesembolsoCedDAO codDAO = new DesembolsoCedDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_DESEMBOLSO_CED);
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
                CodigueraDAO<SgDesembolsoCed> codDAO = new CodigueraDAO<>(em, SgDesembolsoCed.class);
                codDAO.eliminarPorId(id,ConstantesOperaciones.ELIMINAR_DESEMBOLSO_CED);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    
    /**
     * Guarda los movimientos a cuenta bancaria
     *
     * @param desembolsos List
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void guardarMovimientos(DesembolsoMovimiento desMov) throws GeneralException {
        try {
            BusinessException be = new BusinessException();
            FiltroDesembolsoCed filtroCed = new FiltroDesembolsoCed();
            filtroCed.setIncluirCampos(new String[]{"dcePk","dceMonto","dceVersion",
                "dceDetDesembolsoFk.dedPk",
                "dceDetDesembolsoFk.dedVersion",
                "dceReqFondoCedFk.rfcPk",
                "dceReqFondoCedFk.rfcVersion",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacPk",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedPk",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedCodigo",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedNombre",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedTipo",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedVersion",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacTransferenciaFk.subComponente.gesId",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacTransferenciaFk.subComponente.gesVersion",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacTransferenciaFk.subComponente.tipoCuentaBancaria.tcbPk",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacTransferenciaFk.subComponente.tipoCuentaBancaria.tcbVersion",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.recibo.recPk",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacVersion"
            });
            filtroCed.setDceDetDesembolsoFk(desMov.getDesembolsoPk());
            List<SgDesembolsoCed> desembolsos = obtenerPorFiltro(filtroCed);
            
            if (desembolsos != null && !desembolsos.isEmpty()) {
                for(SgDesembolsoCed ced : desembolsos){
                    SgTransferenciaACed tCed= ced.getDceReqFondoCedFk().getRfcTransferenciaCedFk();
                    SgMovimientoCuentaBancaria mov = new SgMovimientoCuentaBancaria();
                    
                    List<SgCuentasBancarias> list = new ArrayList();
                    if(tCed.getTacTransferenciaFk().getSubComponente().getTipoCuentaBancaria()!=null){
                        FiltroCuentasBancarias filtro = new FiltroCuentasBancarias();
                        filtro.setIncluirCampos(new String[]{"cbcPk","cbcVersion"});
                        filtro.setCbcSedeFk(tCed.getTacCedFk().getSedPk());
                        filtro.setCbcCuentaTipoCuenta(tCed.getTacTransferenciaFk().getSubComponente().getTipoCuentaBancaria().getTcbPk());
                        filtro.setCbcHabilitado(Boolean.TRUE);
                        list = cuentasBean.obtenerPorFiltro(filtro);
                    }
                    
                    if(list!=null && !list.isEmpty()){
                        mov.setMcbCuentaFK(list.get(0));
                        String des = ced!=null ? ConstantesAyuda.DESEMBOLSO.concat(" # ").concat(ced.getDcePk().toString()) : StringUtils.EMPTY;
                        String trans = tCed!=null ? ConstantesAyuda.TRANSFERENCIA.concat(" # ").concat(tCed.getTacPk().toString()) : StringUtils.EMPTY;
                        String recibo = tCed.getRecibo()!=null ? ConstantesAyuda.RECIBO.concat(" # ").concat(tCed.getRecibo().getRecPk().toString()) : StringUtils.EMPTY;
                        
                        String detalle = des.concat(" ").concat(trans).concat(" ").concat(recibo);
                        mov.setMcbDetalle(detalle);
                        mov.setMcbFecha(desMov.getFechaMov());
                        mov.setMcbMonto(ced.getDceMonto());
                        mov.setMcbTipoMovimiento(TipoMovimiento.HABER);
                        mov.setMcbDesembolsoCedFk(ced);
                        movCuentaBancBean.guardar(mov);
                        
                        ced.setDceEstado(EnumDesembolsoEstado.DEPOSITADO);
                        guardar(ced, Boolean.TRUE);
                    }
                    else{
                        //mensaje de error
                        be.addError(Errores.ERROR_DESEMBOLSO_CED_CUENTA);
                        throw be;
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    
    /**
     * Guarda el objeto indicado desde una lista
     *
     * @param listDes List<DesembolsoCE>
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void guardarListado(List<DesembolsoCE> listDes, Boolean dataSecurity) throws GeneralException {
        try {
            if (listDes != null) {
                for(DesembolsoCE des:listDes){    
                    SgDesembolsoCed ced = new SgDesembolsoCed();
                    ced.setDcePk(null);
                    ced.setDceVersion(0);
                    ced.setDceDetDesembolsoFk(em.getReference(SgDetalleDesembolso.class, des.getDedPk()));
                    ced.setDceReqFondoCedFk(em.getReference(SgReqFondoCed.class, des.getReqFondoCedPk()));
                    ced.setDceEstado(EnumDesembolsoEstado.AUTORIZADO);
                    ced.setDceMonto(des.getMontoADesembolsar());
                    guardar(ced,true);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Se obtienen los desembolsos no depositados
     * @param fila
     * @return
     */
    public List<DesembolsoCE> desembolsosNoDepositados(Long reqId, Long fuenteId) throws GeneralException {
        try {
           
            String squery = "select * from finanzas.vwDesembolsoNoDepositados " +
                            "where str_pk="+reqId+" and fue_id="+fuenteId+";";

            Query query = em.createNativeQuery(squery);
            
            List resultado = query.getResultList();
            
            List<DesembolsoCE> respuesta = new ArrayList<>();

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
    public DesembolsoCE transformarFilaEnDTO(Object[] fila) {

        DesembolsoCE ele = new DesembolsoCE();
        ele.setTacPk(new Long(fila[0].toString()));
        ele.setReqFondoCedPk(new Long(fila[1].toString()));
        ele.setSedPk(new Long(fila[2].toString()));
        ele.setSedCodigo(fila[3].toString());
        ele.setSedNombre(fila[4].toString());
        ele.setSedHabilitado(Boolean.valueOf(fila[5].toString()));
        ele.setRecibo(Boolean.valueOf(fila[6].toString()));
        ele.setOaeMiembrosVigente(Boolean.valueOf(fila[7].toString()));
        ele.setOeaFechaVencimiento(fila[8]!=null ? LocalDate.parse(fila[8].toString()) : null);
        ele.setConvenio(fila[9].toString());
        ele.setCcf(fila[10].toString());
        ele.setMontoAutorizado((BigDecimal) fila[11]);
        ele.setMontoDesembolsado((BigDecimal) fila[12]);
        //ele.setSaldo((BigDecimal) fila[13]);
       
        
        return ele;
    }
    
}
