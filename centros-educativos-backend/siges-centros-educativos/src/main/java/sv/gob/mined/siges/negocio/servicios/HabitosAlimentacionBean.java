/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroHabitosAlimentacion;
import sv.gob.mined.siges.negocio.validaciones.HabitosAlimentacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.HabitosAlimentacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgHabitosAlimentacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTiemposComidaDia;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class HabitosAlimentacionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgHabitosAlimentacion
     * @throws GeneralException
     */
    public SgHabitosAlimentacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgHabitosAlimentacion> codDAO = new CodigueraDAO<>(em, SgHabitosAlimentacion.class);
                SgHabitosAlimentacion habito=codDAO.obtenerPorId(id,null);
                habito.setHalTiempoComidaDia(obtenerTiemposComida(habito.getHalPk()));
                return habito; 
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
                CodigueraDAO<SgHabitosAlimentacion> codDAO = new CodigueraDAO<>(em, SgHabitosAlimentacion.class);
                return codDAO.objetoExistePorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param hal SgHabitosAlimentacion
     * @return SgHabitosAlimentacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgHabitosAlimentacion guardar(SgHabitosAlimentacion hal) throws GeneralException {
        try {
            if (hal != null) {
                if (HabitosAlimentacionValidacionNegocio.validar(hal)) {
                    CodigueraDAO<SgHabitosAlimentacion> codDAO = new CodigueraDAO<>(em, SgHabitosAlimentacion.class);              
                    return codDAO.guardar(hal,null);               
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return hal;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroHabitosAlimentacion filtro) throws GeneralException {
        try {
            HabitosAlimentacionDAO codDAO = new HabitosAlimentacionDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgHabitosAlimentacion>
     * @throws GeneralException
     */
    public List<SgHabitosAlimentacion> obtenerPorFiltro(FiltroHabitosAlimentacion filtro) throws GeneralException {
        try {
            HabitosAlimentacionDAO codDAO = new HabitosAlimentacionDAO(em);
            List<SgHabitosAlimentacion> list=codDAO.obtenerPorFiltro(filtro);
            for(SgHabitosAlimentacion hab:list){
                if(BooleanUtils.isTrue(filtro.getIncluirTiempoComida())){
                    hab.setHalTiempoComidaDia(obtenerTiemposComida(hab.getHalPk()));
                }
            }
            return list;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    public List<SgTiemposComidaDia> obtenerTiemposComida(Long fichaPk) {
        String query = "select dis.* from "
                + "centros_educativos.sg_habito_alimentacion_tiempo_comida_dia spd\n" +
                "join catalogo.sg_tiempos_comida_dia dis on spd.tcd_pk = dis.tcd_pk\n" +
                "where spd.hal_pk = :perPk";
        javax.persistence.Query q = em.createNativeQuery(query, SgTiemposComidaDia.class);
        q.setParameter("perPk", fichaPk);

        List<SgTiemposComidaDia> objs = q.getResultList();
        return objs;
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
                CodigueraDAO<SgHabitosAlimentacion> codDAO = new CodigueraDAO<>(em, SgHabitosAlimentacion.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
