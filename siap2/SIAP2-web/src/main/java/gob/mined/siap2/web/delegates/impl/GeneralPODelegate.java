/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.GeneralPOBean;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.TDR;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate de Planes Operativos Anuales
 *
 * @author Sofis Solutions
 */
@Named
public class GeneralPODelegate implements Serializable {

    @Inject
    private GeneralPOBean bean;

    /**
     * Este método permite obtener los TDR de un insumo en un POA.
     *
     * @param idInsumo
     * @return
     */
    public TDR getTDRInsumo(Integer idInsumo) {
        return bean.getTDRInsumo(idInsumo);
    }

    /**
     * Este método permite guardar los TDR de un insumo de un POA.
     *
     * @param idInsumo
     * @param tdr
     */
    public void saveTDRInsumo(Integer idInsumo, TDR tdr) {
        bean.saveTDRInsumo(idInsumo, tdr);
    }

    /**
     * Este método permite obtener el POA de un proyecto a partir de un insumo
     *
     * @param idPOinsumo
     * @return
     */
    public POAProyecto obtenerPOAProyectoPorIdPoInsumo(Integer idPOinsumo) {
        return bean.obtenerPOAProyectoPorIdPoInsumo(idPOinsumo);
    }

    /**
     * Este método permite obtener los POA con Actividades para un insumo dado.
     *
     * @param idPOinsumo
     * @return
     */
    public POAConActividades obtenerPOAConActividadesPorIdPoInsumo(Integer idPOinsumo) {
        return bean.obtenerPOAConActividadesPorIdPoInsumo(idPOinsumo);
    }

    /**
     * Este método permite obtener una línea de POA a partir de un insumo.
     *
     * @param idPoInsumo
     * @return
     */
    public POLinea obtenerPOLineaPorIdPoInsumo(Integer idPoInsumo) {
        return bean.obtenerPOLineaPorIdPoInsumo(idPoInsumo);
    }

    /**
     * Este método corresponde a la acción de descertificación de un insumo
     *
     * @param idMonto
     * @return
     */
    public POInsumos solicitarDescertificacionMontoPOInsumo(Integer idMonto) {
        return bean.solicitarDescertificacionMontoPOInsumo(idMonto);
    }

    /**
     * Este método permite cargar los montos por fuente de un insumo
     *
     * @param idMonto
     * @return
     */
    public POMontoFuenteInsumo loadPOMontoFuenteInsumo(Integer idMonto) {
        return bean.loadPOMontoFuenteInsumo(idMonto);
    }

    /**
     * Este método permite aprobar la descertificación de un insumo
     *
     * @param idMontoFuente
     * @return
     */
    public POMontoFuenteInsumo aprobarDescertificacionMontoFuenteInsumo(Integer idMontoFuente) {
        return bean.aprobarDescertificacionMontoFuenteInsumo(idMontoFuente);
    }

    /**
     * Este método permite obtener un insumo por su id.
     *
     * @param idInsumo
     * @return
     */
    public POInsumos getPOInsumoByID(Integer idInsumo) {
        return bean.getPOInsumoByID(idInsumo);
    }

    /**
     * Este método permite obtener el menor año en ejecución.
     *
     * @return
     */
    public AnioFiscal getMenorAnioEjecucion() {
        return bean.getMenorAnioEjecucion();
    }

    /**
     * Este método permite obtener un monto fuente por su id.
     *
     * @param idFuente
     * @return
     */
    public POMontoFuenteInsumo getMontoFuenteByID(Integer idFuente) {
        return bean.getMontoFuenteByID(idFuente);
    }

    /**
     * Verifica si alguna de las fuentes del insumo fue enviada para
     * certificación o ya fue certificada
     *
     * @return
     */
    public Boolean insumoTieneFuenteCertificadaOParaCertificar(POInsumos poInsumo) {
        return bean.insumoTieneFuenteCertificadaOParaCertificar(poInsumo);
    }

    /**
     * Devuelve el id de un contrato al que pertenece un PoInsumo
     * @param poInsumo 
     * @return 
     */
    public ContratoOC getContratoAsociadoAInsumo(POInsumos poInsumo) {
        return bean.getContratoAsociadoAInsumo(poInsumo);
    }
}
