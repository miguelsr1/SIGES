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
import sv.gob.mined.siges.negocio.validaciones.ProgramaEducativoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgProgramaEducativo;

@Stateless
public class ProgramaEducativoBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgProgramaEducativo
     * @throws GeneralException
     */
    public SgProgramaEducativo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgProgramaEducativo> codDAO = new CodigueraDAO<>(em, SgProgramaEducativo.class);
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
                CodigueraDAO<SgProgramaEducativo> codDAO = new CodigueraDAO<>(em, SgProgramaEducativo.class);
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
     * @param ped SgProgramaEducativo
     * @return SgProgramaEducativo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgProgramaEducativo guardar(SgProgramaEducativo ped) throws GeneralException {
        try {
            if (ped != null) {
                if (ProgramaEducativoValidacionNegocio.validar(ped)) {
                    CodigueraDAO<SgProgramaEducativo> codDAO = new CodigueraDAO<>(em, SgProgramaEducativo.class);
                    boolean guardar = true;
                    if (ped.getPedPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(ped.getClass(), ped.getPedPk() , ped.getPedVersion());
                        SgProgramaEducativo valorAnterior = (SgProgramaEducativo) valorAnt;
                        guardar = !ProgramaEducativoValidacionNegocio.compararParaGrabar(valorAnterior, ped);
                    }
                    if (guardar) {
                        return codDAO.guardar(ped);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ped;
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
            CodigueraDAO<SgProgramaEducativo> codDAO = new CodigueraDAO<>(em, SgProgramaEducativo.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgProgramaEducativo 
     * @throws GeneralException
     */
    public List<SgProgramaEducativo> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgProgramaEducativo> codDAO = new CodigueraDAO<>(em, SgProgramaEducativo.class);
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
                CodigueraDAO<SgProgramaEducativo> codDAO = new CodigueraDAO<>(em, SgProgramaEducativo.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
