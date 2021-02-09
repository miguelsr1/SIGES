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
import sv.gob.mined.siges.filtros.FiltroAsignacionPerfil;
import sv.gob.mined.siges.negocio.validaciones.AsignacionPerfilValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.AsignacionPerfilDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAsignacionPerfil;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class AsignacionPerfilBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SeguridadBean seguridadBean;
    
    @Inject
    private AsignacionPerfilPersonalBean asignacionPerfilPersonalBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAsignacionPerfil
     * @throws GeneralException
     */
    public SgAsignacionPerfil obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAsignacionPerfil> codDAO = new CodigueraDAO<>(em, SgAsignacionPerfil.class);
                SgAsignacionPerfil ap=codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_ASIGNACION_PERFIL);
                if(ap.getApeSedeFk()!=null){
                    ap.getApeSedeFk().getSedPk();
                }
                return ap;
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
                CodigueraDAO<SgAsignacionPerfil> codDAO = new CodigueraDAO<>(em, SgAsignacionPerfil.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_ASIGNACION_PERFIL);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param ape SgAsignacionPerfil
     * @return SgAsignacionPerfil
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAsignacionPerfil guardar(SgAsignacionPerfil ape) throws GeneralException {
        try {
            if (ape != null) {
                if (AsignacionPerfilValidacionNegocio.validar(ape)) {
                    CodigueraDAO<SgAsignacionPerfil> codDAO = new CodigueraDAO<>(em, SgAsignacionPerfil.class);
                    if(ape.getApeAsignacionesPerfilPersonal()!=null && !ape.getApeAsignacionesPerfilPersonal().isEmpty()){
                        asignacionPerfilPersonalBean.crearUsuarioRol(ape.getApeAsignacionesPerfilPersonal().get(0));
                    }
                    
                    return codDAO.guardar(ape, ape.getApePk() == null ? ConstantesOperaciones.CREAR_ASIGNACION_PERFIL : ConstantesOperaciones.ACTUALIZAR_ASIGNACION_PERFIL);

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ape;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroAsignacionPerfil filtro) throws GeneralException {
        try {
            AsignacionPerfilDAO codDAO = new AsignacionPerfilDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ASIGNACION_PERFIL);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgAsignacionPerfil>
     * @throws GeneralException
     */
    public List<SgAsignacionPerfil> obtenerPorFiltro(FiltroAsignacionPerfil filtro) throws GeneralException {
        try {
            AsignacionPerfilDAO codDAO = new AsignacionPerfilDAO(em, seguridadBean);
            List<SgAsignacionPerfil> list = codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ASIGNACION_PERFIL);
            if(BooleanUtils.isTrue(filtro.getAgregarSede())){
                for (SgAsignacionPerfil as : list) {
                    if(as.getApeSedeFk()!=null){
                        as.getApeSedeFk().getSedPk();
                    }
                }
            }
            if (BooleanUtils.isTrue(filtro.getIncluirAsignacionPerfilPersonal())) {
                for (SgAsignacionPerfil as : list) {
                    if (as.getApeAsignacionesPerfilPersonal() != null) {
                        as.getApeAsignacionesPerfilPersonal().size();
                    }
                }
            }
            return list;
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
                CodigueraDAO<SgAsignacionPerfil> codDAO = new CodigueraDAO<>(em, SgAsignacionPerfil.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
