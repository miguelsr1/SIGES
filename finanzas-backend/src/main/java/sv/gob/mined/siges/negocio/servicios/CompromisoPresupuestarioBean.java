/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCompromisoPresupuestario;
import sv.gob.mined.siges.negocio.validaciones.CompromisoPresupuestarioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.CompromisoPresupuestarioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCompromisoPresupuestario;

/**
 * Servicio que gestiona el compromiso presupuestario
 * @author Sofis Solutions
 */
@Stateless
public class CompromisoPresupuestarioBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCompromisoPresupuestario
     * @return Lista de <SgCompromisoPresupuestario>
     * @throws GeneralException
     */
    public List<SgCompromisoPresupuestario> obtenerPorFiltro(FiltroCompromisoPresupuestario filtro) throws GeneralException {
        try {
            CompromisoPresupuestarioDAO codDAO = new CompromisoPresupuestarioDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCompromisoPresupuestarios
     * @throws GeneralException
     */
    public SgCompromisoPresupuestario obtenerPorIdLazy(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCompromisoPresupuestario> codDAO = new CodigueraDAO<>(em, SgCompromisoPresupuestario.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgCompromisoPresupuestario
     * @return SgCompromisoPresupuestario
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgCompromisoPresupuestario guardar(SgCompromisoPresupuestario entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (CompromisoPresupuestarioValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgCompromisoPresupuestario> codDAO = new CodigueraDAO<>(em, SgCompromisoPresupuestario.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getCprPk() == null ? ConstantesOperaciones.CREAR_SOLICITUD_DE_TRANSFERENCIA : ConstantesOperaciones.CREAR_SOLICITUD_DE_TRANSFERENCIA);
                    } else {
                        return codDAO.guardar(entity, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCompromisoPresupuestario
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCompromisoPresupuestario filtro) throws GeneralException {
        try {
            CompromisoPresupuestarioDAO codDAO = new CompromisoPresupuestarioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
