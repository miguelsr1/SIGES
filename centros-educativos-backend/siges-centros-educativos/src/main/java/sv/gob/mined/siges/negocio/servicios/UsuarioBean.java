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
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.seguridad.FiltroUsuario;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.UsuarioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.UsuarioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuario;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Stateless
public class UsuarioBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    @ConfigProperty(name = "users.defaultpassword", defaultValue = "BienvenidoSiges")
    private String defaultPassword;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgUsuario
     * @throws GeneralException
     */
    public SgUsuario obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve el usuario por codigo
     *
     * @param codigo String
     * @return SgUsuario
     * @throws GeneralException
     */
    public SgUsuario obtenerPorCodigo(String codigo) throws GeneralException {
        if (codigo != null) {
            try {
                FiltroUsuario filtro = new FiltroUsuario();
                filtro.setCodigoExacto(codigo);
                List<SgUsuario> usuarios = this.obtenerPorFiltro(filtro);
                if (!usuarios.isEmpty()) {
                    return usuarios.get(0);
                }
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
                CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);
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
     * @param entity SgUsuario
     * @return SgUsuario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgUsuario guardar(SgUsuario entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (UsuarioValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);

                    if (entity.getUsuPk() == null) {
                        entity.setUsuPassword(SofisStringUtils.encodeSHA256Hex(defaultPassword));
                    }
                    return codDAO.guardar(entity, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    public SgUsuario crearObtenerUsuarioPersona(SgPersona per) throws GeneralException {
        try {

            if (per == null) {
                throw new IllegalStateException();
            }

            FiltroUsuario fusu = new FiltroUsuario();

            if (per.getPerUsuarioPk() != null) {
                fusu.setUsuPk(per.getPerUsuarioPk());
            } else if (!StringUtils.isBlank(per.getPerDui())) {
                fusu.setCodigoExacto(per.getPerDui());
            } else if (!StringUtils.isBlank(per.getPerNip())) {
                fusu.setCodigoExacto(per.getPerNip());
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_GENERAR_USUARIO_PERSONAL_SIN_DUI_NI_NIP);
                throw be;
            }
            List<SgUsuario> listusu = this.obtenerPorFiltro(fusu);
            SgUsuario usuario = new SgUsuario();

            if (listusu.isEmpty()) {
                if (!StringUtils.isBlank(per.getPerDui())) {
                    usuario.setUsuCodigo(per.getPerDui());
                } else if (!StringUtils.isBlank(per.getPerNip())) {
                    usuario.setUsuCodigo(per.getPerNip());
                }
                usuario.setUsuNombre(per.getPerNombreCompleto());
                usuario.setUsuHabilitado(Boolean.TRUE);
                usuario.setUsuPersonaPk(per.getPerPk());
                usuario = this.guardar(usuario);
            } else {
                usuario = listusu.get(0);
                if (usuario.getUsuPersonaPk() == null) {
                    usuario.setUsuPersonaPk(per.getPerPk());
                    usuario = this.guardar(usuario);
                }
            }
            per.setPerUsuarioPk(usuario.getUsuPk());
            return usuario;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroUsuario filtro) throws GeneralException {
        try {
            UsuarioDAO codDAO = new UsuarioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgUsuario
     * @throws GeneralException
     */
    public List<SgUsuario> obtenerPorFiltro(FiltroUsuario filtro) throws GeneralException {
        try {
            UsuarioDAO codDAO = new UsuarioDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
