/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ImpuestosBean;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.entities.data.impl.QuedanEmitido;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate de impuestos
 * @author Sofis Solutions
 */
@Named
public class ImpuestoDelegate implements Serializable {

    @Inject
    private ImpuestosBean bean;

    /**
     * Este método permite obtener los quedan emitidos par aun proyecto dado en un rango de fechas y que tengan retenciones de un determinado impuesto.
     * @param idProyecto
     * @param fechaDesde
     * @param fechaHasta
     * @param idImpuesto
     * @return 
     */
    public List<QuedanEmitido> getActasEnProyecto(Integer idProyecto, Date fechaDesde, Date fechaHasta, Integer idImpuesto) {
        return bean.getActasEnProyecto(idProyecto, fechaDesde, fechaHasta, idImpuesto);
    }

    /**
     * Este método permite obtener el monto retenido para un acta en un impuesto.
     * @param idActa
     * @param idImpuesto
     * @return 
     */
    public BigDecimal obtenerMontoRetenidoParaImpuesto(Integer idActa, Integer idImpuesto) {
        return bean.obtenerMontoRetenidoParaImpuesto(idActa, idImpuesto);
    }

    /**
     * Este método guarda un impuesto
     * @param impuesto
     * @return 
     */
    public Impuesto guardarImpuesto(Impuesto impuesto) {
        return bean.guardarImpuesto(impuesto);
    }

}
