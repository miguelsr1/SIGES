/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.PolizaDeConcentracion;
import gob.mined.siap2.entities.enums.EstadoPoliza;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Query;

/**
 * Esta clase incluye los métodos de acceso a datos específicos de las pólizas
 * de concentración.
 *
 * @author Sofis Solutions
 */
@JPADAO
public class PolizaDAO extends EclipselinkJpaDAOImp<PolizaDeConcentracion, Integer> {

    /**
     * Este método devuelve todas las pólizas de concentración aplicadas
     * asociadas a un insumo dado.
     *
     * @param idInsumo
     * @return
     */
    public Collection<PolizaDeConcentracion> obtenerPolizasPorInsumo(Integer idInsumo) {
        String consulta = "select s "
                + "from PolizaDeConcentracion s "
                + "where s.estado=:estado "
                + "and s.fuente.insumo.id=:id";
        Query query = entityManager.createQuery(consulta);
        query.setParameter("id", idInsumo);
        query.setParameter("estado", EstadoPoliza.APLICADA);
        return query.getResultList();
    }

    /**
     * Este método devuelve todas las pólizas de concentración aplicadas, que
     * tienen asociado un pago de una fuente para un mes determinado.
     *
     * @param idFuente
     * @return
     */
    public Collection<PolizaDeConcentracion> obtenerPolizasAplicadasPorPagoFuenteFCM(Integer idPagoFuenteFCM) {
        String consulta = "select poliza "
                + "from PolizaDeConcentracion poliza  "
                + "where poliza.estado=:estado "
                + "and poliza.pagoFuenteFCM.id=:idPagoFuenteFCM";
        Query query = entityManager.createQuery(consulta);
        query.setParameter("idPagoFuenteFCM", idPagoFuenteFCM);
        query.setParameter("estado", EstadoPoliza.APLICADA);
        return query.getResultList();
    }

    public BigDecimal obtenerMontoEnPolizasAprobadasPorInsumo(Integer idPoInsumo) {
        String consulta = "select SUM(poliza.monto) "
                + "from PolizaDeConcentracion poliza  "
                + "where poliza.estado = :estado "
                + "and poliza.fuente.insumo.id = :idPoInsumo";
        Query query = entityManager.createQuery(consulta);
        query.setParameter("idPoInsumo", idPoInsumo);
        query.setParameter("estado", EstadoPoliza.APLICADA);
        return (BigDecimal) query.getSingleResult();
    }

}
