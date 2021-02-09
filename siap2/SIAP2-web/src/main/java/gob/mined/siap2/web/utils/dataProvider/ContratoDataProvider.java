/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils.dataProvider;

import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.enums.EstadoContrato;
import gob.mined.siap2.entities.enums.TipoContrato;
import gob.mined.siap2.web.delegates.impl.ContratoDelegate;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;

@Named
public class ContratoDataProvider implements IDataProvider, Serializable {

    private ContratoDelegate delegate;

    private Integer nroContratoOC;
    private TipoContrato tipoContratoOC;
    private String nombreProceso;
    private Date fechaDesde;
    private Date fechaHasta;
    private String proveedor;
    private EstadoContrato estadoContratoOC;
    private Integer nroInsumo;
    private EstadoContrato estadoContrato;
    private String usuarioCodigo;
    private String[] orderBy;
    private boolean[] ascending;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    public ContratoDataProvider() {

    }

    public ContratoDataProvider(ContratoDelegate contratoDelegate, Integer mNroContratoOC, TipoContrato mTipoContratoOC, String mNombreProceso, Date mFechaDesde, Date mFechaHasta, String mProveedor, EstadoContrato mEstadoContratoOC, Integer mNroInsumo, EstadoContrato mEstadoContrato, String mUsuarioCodigo, String[] orderBy, boolean[] ascending) {
        this.delegate = contratoDelegate;
        this.nroContratoOC = mNroContratoOC;
        this.tipoContratoOC = mTipoContratoOC;
        this.nombreProceso = mNombreProceso;
        this.fechaDesde = mFechaDesde;
        this.fechaHasta = mFechaHasta;
        this.proveedor = mProveedor;
        this.estadoContratoOC = mEstadoContratoOC;
        this.nroInsumo = mNroInsumo;
        this.estadoContrato = mEstadoContrato;
        this.usuarioCodigo = mUsuarioCodigo;
        this.orderBy = orderBy;
        this.ascending = ascending;
    }

    @Override
    public List<Object> getBufferedData(Integer startRowI, Integer offsetI) {
        try {

            List l = delegate.getConsultaContratosOC(nroContratoOC, tipoContratoOC, nombreProceso, fechaDesde, fechaHasta, proveedor, estadoContratoOC, nroInsumo, estadoContrato, usuarioCodigo, startRowI, startRowI + offsetI, orderBy, ascending);
            return l;
        } catch (Exception ex) {
            return new LinkedList();
        }
    }

    @Override
    public Integer getCountData() {
        try {
            int count = ((int) delegate.countConsultaContratosOC(nroContratoOC, tipoContratoOC, nombreProceso, fechaDesde, fechaHasta, proveedor, estadoContratoOC, nroInsumo, estadoContrato, usuarioCodigo));
            return count;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return 0;
        }
    }

    @Override
    public void setOrderBy(String[] orderBy, boolean[] asc) {
        this.orderBy = orderBy;
        this.ascending = asc;
    }
}
