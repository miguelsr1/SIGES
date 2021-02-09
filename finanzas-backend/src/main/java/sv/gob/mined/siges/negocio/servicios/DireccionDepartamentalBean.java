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
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDireccionDepartamental;
import sv.gob.mined.siges.negocio.validaciones.DireccionDepartamentalValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DireccionDepartamentalDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDireccionDepartamental;

/**
 * Servicio que gestiona las pagadurías de las Direcciones Departamentales de
 * Educación
 *
 * @author Sofis Solutions
 */
@Stateless
public class DireccionDepartamentalBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDireccionDepartamental
     * @throws GeneralException
     */
    public SgDireccionDepartamental obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDireccionDepartamental> codDAO = new CodigueraDAO<>(em, SgDireccionDepartamental.class);
                return codDAO.obtenerPorId(id, null);
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
                CodigueraDAO<SgDireccionDepartamental> codDAO = new CodigueraDAO<>(em, SgDireccionDepartamental.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param ded SgDireccionDepartamental
     * @return SgDireccionDepartamental
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgDireccionDepartamental guardar(SgDireccionDepartamental ded, Boolean dataSecurity) throws GeneralException {
        try {
            if (ded != null) {
                if (DireccionDepartamentalValidacionNegocio.validar(ded)) {
                    CodigueraDAO<SgDireccionDepartamental> codDAO = new CodigueraDAO<>(em, SgDireccionDepartamental.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(ded, ded.getDedPk() == null ? ConstantesOperaciones.CREAR_DIRECCION_DEPARTAMENTAL : ConstantesOperaciones.ACTUALIZAR_DIRECCION_DEPARTAMENTAL);
                    } else {
                        return codDAO.guardar(ded, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ded;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroDireccionDepartamental
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDireccionDepartamental filtro) throws GeneralException {
        try {
            DireccionDepartamentalDAO codDAO = new DireccionDepartamentalDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_DIRECCION_DEPARTAMENTAL);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroDireccionDepartamental
     * @return Lista de <SgDireccionDepartamental>
     * @throws GeneralException
     */
    public List<SgDireccionDepartamental> obtenerPorFiltro(FiltroDireccionDepartamental filtro) throws GeneralException {
        try {
            DireccionDepartamentalDAO codDAO = new DireccionDepartamentalDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_DIRECCION_DEPARTAMENTAL);
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
                CodigueraDAO<SgDireccionDepartamental> codDAO = new CodigueraDAO<>(em, SgDireccionDepartamental.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_DIRECCION_DEPARTAMENTAL);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
