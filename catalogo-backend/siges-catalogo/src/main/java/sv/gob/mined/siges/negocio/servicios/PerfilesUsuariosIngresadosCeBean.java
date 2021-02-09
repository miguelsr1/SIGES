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
import sv.gob.mined.siges.negocio.validaciones.PerfilesUsuariosIngresadosCeValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPerfilRoles;
import sv.gob.mined.siges.persistencia.entidades.SgPerfilesUsuariosIngresadosCe;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class PerfilesUsuariosIngresadosCeBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPerfilesUsuariosIngresadosCe
     * @throws GeneralException
     */
    public SgPerfilesUsuariosIngresadosCe obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPerfilesUsuariosIngresadosCe> codDAO = new CodigueraDAO<>(em, SgPerfilesUsuariosIngresadosCe.class);
                SgPerfilesUsuariosIngresadosCe entidad = codDAO.obtenerPorId(id);
                if(entidad.getPuiPerfilRoles() != null){
                    entidad.getPuiPerfilRoles().size();
                }
                return entidad;
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
                CodigueraDAO<SgPerfilesUsuariosIngresadosCe> codDAO = new CodigueraDAO<>(em, SgPerfilesUsuariosIngresadosCe.class);
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
     * @param pui SgPerfilesUsuariosIngresadosCe
     * @return SgPerfilesUsuariosIngresadosCe
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgPerfilesUsuariosIngresadosCe guardar(SgPerfilesUsuariosIngresadosCe pui) throws GeneralException {
        try {
            if (pui != null) {
                if (PerfilesUsuariosIngresadosCeValidacionNegocio.validar(pui)) {
                    CodigueraDAO<SgPerfilesUsuariosIngresadosCe> codDAO = new CodigueraDAO<>(em, SgPerfilesUsuariosIngresadosCe.class);
                    if(!pui.getPuiPerfilRoles().isEmpty()){
                        for(SgPerfilRoles per : pui.getPuiPerfilRoles()){
                            per.setUirPerfil(pui);
                        }
                    }
                    return codDAO.guardar(pui);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return pui;
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
            CodigueraDAO<SgPerfilesUsuariosIngresadosCe> codDAO = new CodigueraDAO<>(em, SgPerfilesUsuariosIngresadosCe.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgPerfilesUsuariosIngresadosCe>
     * @throws GeneralException
     */
    public List<SgPerfilesUsuariosIngresadosCe> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgPerfilesUsuariosIngresadosCe> codDAO = new CodigueraDAO<>(em, SgPerfilesUsuariosIngresadosCe.class);
            List<SgPerfilesUsuariosIngresadosCe> resultado = codDAO.obtenerPorFiltro(filtro);
            if(!resultado.isEmpty()){
                resultado.forEach(r -> {
                    if(r.getPuiPerfilRoles() != null){
                        r.getPuiPerfilRoles().size();
                    }
                });
            }
            return resultado;
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
                CodigueraDAO<SgPerfilesUsuariosIngresadosCe> codDAO = new CodigueraDAO<>(em, SgPerfilesUsuariosIngresadosCe.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
