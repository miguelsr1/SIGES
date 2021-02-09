/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;

@Stateless
public class DepartamentoBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTipoDepartamento
     * @throws GeneralException
     */
    public SgDepartamento obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDepartamento> codDAO = new CodigueraDAO<>(em, SgDepartamento.class);
                return codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_DEPARTAMENTO);
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
                CodigueraDAO<SgDepartamento> codDAO = new CodigueraDAO<>(em, SgDepartamento.class);
                return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_DEPARTAMENTO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
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
            CodigueraDAO<SgDepartamento> codDAO = new CodigueraDAO<>(em, SgDepartamento.class);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_DEPARTAMENTO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgDepartamento 
     * @throws GeneralException
     */
    public List<SgDepartamento> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgDepartamento> codDAO = new CodigueraDAO<>(em, SgDepartamento.class);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_DEPARTAMENTO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
 

}

