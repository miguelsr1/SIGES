/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import org.eclipse.microprofile.opentracing.Traced;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgCountAlertas;
import sv.gob.mined.siges.enumerados.EnumRiesgoAlerta;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAlerta;
import sv.gob.mined.siges.persistencia.daos.AlertaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAlerta;

@Stateless
@Traced
public class AlertaBean {

    @PersistenceContext
    private EntityManager em;
    @Inject
    private SeguridadBean seguridadBean;
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroAlerta filtro) throws GeneralException {
        try {
            AlertaDAO codDAO = new AlertaDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ALERTA_TEMPRANA);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SgCountAlertas obtenerTotalRiesgosPorFiltro(FiltroAlerta filtro) throws GeneralException {
        try {
            AlertaDAO codDAO = new AlertaDAO(em, seguridadBean);
            SgCountAlertas count = new SgCountAlertas();

            List<EnumRiesgoAlerta> riesgosFiltro = filtro.getAleRiesgos();

            count.setTotal(codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ALERTA_TEMPRANA));

            if (riesgosFiltro == null || riesgosFiltro.isEmpty() || riesgosFiltro.contains(EnumRiesgoAlerta.MUY_ALTO)) {
                List<EnumRiesgoAlerta> nuevoRiesgo = new ArrayList<>();
                nuevoRiesgo.add(EnumRiesgoAlerta.MUY_ALTO);
                filtro.setAleRiesgos(nuevoRiesgo);
                count.setRiesgoMuyAlto(codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ALERTA_TEMPRANA));
            }

            if (riesgosFiltro == null || riesgosFiltro.isEmpty() || riesgosFiltro.contains(EnumRiesgoAlerta.ALTO)) {
                List<EnumRiesgoAlerta> nuevoRiesgo = new ArrayList<>();
                nuevoRiesgo.add(EnumRiesgoAlerta.ALTO);
                filtro.setAleRiesgos(nuevoRiesgo);
                count.setRiesgoAlto(codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ALERTA_TEMPRANA));
            }

            if (riesgosFiltro == null || riesgosFiltro.isEmpty() || riesgosFiltro.contains(EnumRiesgoAlerta.MEDIO)) {
                List<EnumRiesgoAlerta> nuevoRiesgo = new ArrayList<>();
                nuevoRiesgo.add(EnumRiesgoAlerta.MEDIO);
                filtro.setAleRiesgos(nuevoRiesgo);
                count.setRiesgoMedio(codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ALERTA_TEMPRANA));
            }

            return count;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgAllegado
     * @throws GeneralException
     */
    public List<SgAlerta> obtenerPorFiltro(FiltroAlerta filtro) throws GeneralException {
        try {
            AlertaDAO codDAO = new AlertaDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ALERTA_TEMPRANA);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    
    /**
     * Eliminar las alertas tempranas del estudiante indicado
     *
     * @param Long estPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarAlertasTempranas(Long estPk) throws GeneralException {
        try {
            if (estPk != null) {
                em.createQuery("DELETE FROM SgAlerta where aleEstudiante.estPk = :estPk")
                        .setParameter("estPk", estPk)
                        .executeUpdate();
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
