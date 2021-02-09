/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.TechoActividadAccionCentral;
import gob.mined.siap2.entities.data.impl.TechoPlanificacionAccionCentral;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.AccionCentralDAO;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos correspondientes a acción central.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "AccionCentralBean")
@LocalBean
public class AccionCentralBean {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private AccionCentralDAO accionCentralDAO;
    @Inject
    private GeneralBean generalBean;

    /**
     * Este método crea o actualiza una acción central en una determinada
     * planificación estratégica.
     *
     * @param idPlanificacion
     * @param accion
     */
    public void crearOActualizarAccionCentral(Integer idPlanificacion, AccionCentral accion) {
        try {
            //si edita verifica que sea editable
            generalBean.chequearIgualNombre(AccionCentral.class, accion.getId(), accion.getNombre());

            PlanificacionEstrategica p = (PlanificacionEstrategica) generalDAO.find(PlanificacionEstrategica.class, idPlanificacion);
            accion.setPlanificacionEstrategica(p);

            Iterator<TechoActividadAccionCentral> iter = accion.getMontosUT().iterator();
            while (iter.hasNext()) {
                TechoActividadAccionCentral techo = iter.next();
                //se remueven los que son 0, asi se puede reducir años a la planificacion
                if (techo.getMonto() == null || techo.getMonto().equals(BigDecimal.ZERO)) {
                    iter.remove();
                } else {
                    //VERIFICA QUE NO SE SOBREPASE DEL TECHO PARA EL AÑO
                    BigDecimal total = accionCentralDAO.sumarMontosUTPlanificacion(p.getId(), techo.getAnioFiscal().getAnio(), techo.getUnidadTecnica().getId(), accion.getId());
                    if (total == null) {
                        total = BigDecimal.ZERO;
                    }
                    //se busca el techo en la planificación
                    BigDecimal techoUT = null;
                    Iterator<TechoPlanificacionAccionCentral> iterT = p.getTechosAccionCentral().iterator();
                    while (iterT.hasNext() && techoUT == null) {
                        TechoPlanificacionAccionCentral techoP = iterT.next();
                        if (techoP.getAnioFiscal().equals(techo.getAnioFiscal()) && techoP.getUnidadTecnica().equals(techo.getUnidadTecnica())) {
                            techoUT = techoP.getMonto();
                        }
                    }
                    if (techoUT == null) {
                        techoUT = BigDecimal.ZERO;
                    }
                    if (total.add(techo.getMonto()).compareTo(techoUT) > 0) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_LA_SUMA_DE_TECHOS_PARA_LA_ACTIVIDAD_SOBREPASA_TECHO_PARA_EL_ANIO);
                        throw b;
                    }
                }
            }

            generalDAO.update(accion);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método calcula el monto total usado en acción central para una
     * unidad técnica en una determinada planificación.
     *
     * @param idPlanificacion
     * @param anio
     * @param idUnidadTecnica
     * @param idAccionCentralExcluir
     * @return
     */
    public BigDecimal getMontoUsadoAccionCentralUTPlanificacion(Integer idPlanificacion, Integer anio, Integer idUnidadTecnica, Integer idAccionCentralExcluir) {
        BigDecimal total = accionCentralDAO.sumarMontosUTPlanificacion(idPlanificacion, anio, idUnidadTecnica, idAccionCentralExcluir);
        if (total == null) {
            return BigDecimal.ZERO;
        }
        return total;
    }

    /**
     * Este método se encarga de eliminar una acción central
     *
     * @param idAccion
     */
    public void eleiminarAccionCentral(Integer idAccion) {
        try {
            AccionCentral a = (AccionCentral) generalDAO.find(AccionCentral.class, idAccion);

            if (!a.getPoas().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"POA", "la acción central"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }

            generalDAO.delete(a);

        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA_SIN_PARAMETROS);
            throw b;
        }
    }

}
