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
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.EnfermedadNoTransmisibleValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEnfermedadNoTransmisible;

@Stateless
public class EnfermedadNoTransmisibleBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEnfermedadNoTransmisible
     * @throws GeneralException
     */
    public SgEnfermedadNoTransmisible obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgEnfermedadNoTransmisible> codDAO = new CodigueraDAO<>(em, SgEnfermedadNoTransmisible.class);
                return codDAO.obtenerPorId(id);
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
                CodigueraDAO<SgEnfermedadNoTransmisible> codDAO = new CodigueraDAO<>(em, SgEnfermedadNoTransmisible.class);
                return codDAO.objetoExistePorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
        

    /**
     * Guarda el objeto indicado
     *
     * @param ent SgEnfermedadNoTransmisible
     * @return SgEnfermedadNoTransmisible
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEnfermedadNoTransmisible guardar(SgEnfermedadNoTransmisible ent) throws GeneralException {
        try {
            if (ent != null) {
                if (EnfermedadNoTransmisibleValidacionNegocio.validar(ent)) {
                    CodigueraDAO<SgEnfermedadNoTransmisible> codDAO = new CodigueraDAO<>(em, SgEnfermedadNoTransmisible.class);
                    boolean guardar = true;
                    if (ent.getEntPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(ent.getClass(), ent.getEntPk() , ent.getEntVersion());
                        SgEnfermedadNoTransmisible valorAnterior = (SgEnfermedadNoTransmisible) valorAnt;
                        guardar = !EnfermedadNoTransmisibleValidacionNegocio.compararParaGrabar(valorAnterior, ent);
                    }
                    if (guardar) {
                        return codDAO.guardar(ent);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ent;
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
            CodigueraDAO<SgEnfermedadNoTransmisible> codDAO = new CodigueraDAO<>(em, SgEnfermedadNoTransmisible.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgEnfermedadNoTransmisible 
     * @throws GeneralException
     */
    public List<SgEnfermedadNoTransmisible> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEnfermedadNoTransmisible> codDAO = new CodigueraDAO<>(em, SgEnfermedadNoTransmisible.class);
            return codDAO.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgEnfermedadNoTransmisible> codDAO = new CodigueraDAO<>(em, SgEnfermedadNoTransmisible.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
