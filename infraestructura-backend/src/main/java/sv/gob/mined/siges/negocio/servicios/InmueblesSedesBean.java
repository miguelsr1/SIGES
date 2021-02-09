/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgAfBienDepreciable;
import sv.gob.mined.siges.dto.SgAfEstadosBienes;
import sv.gob.mined.siges.dto.SgAfEstadosCalidad;
import sv.gob.mined.siges.dto.SgAfTipoBienes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.filtros.FiltroInmueblesSedes;
import sv.gob.mined.siges.filtros.FiltroRelInmuebleUnidadResp;
import sv.gob.mined.siges.filtros.FiltroTipoBienes;
import sv.gob.mined.siges.negocio.validaciones.InmueblesSedesValidacionNegocio;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleEspacioComunValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.InmueblesSedesDAO;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedes;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedesLiteEspacio;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedesLiteObraExterior;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedesLiteOtroServicio;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedesLiteServicio;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleEspacioComun;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleUnidadResp;
import sv.gob.mined.siges.restclient.BienesDepreciablesRestClient;
import sv.gob.mined.siges.restclient.CatalogosRestClient;

@Stateless
public class InmueblesSedesBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private BienesDepreciablesRestClient bienesDepreciablesClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private RelInmuebleUnidadRespBean relInmuebleUnidadRespBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgInmueblesSedes
     * @throws GeneralException
     */
    public SgInmueblesSedes obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgInmueblesSedes> codDAO = new CodigueraDAO<>(em, SgInmueblesSedes.class);
                SgInmueblesSedes inmueble = codDAO.obtenerPorId(id);
                if (inmueble.getIvuInmueblesSede() != null) {
                    inmueble.getIvuInmueblesSede().size();
                }
                inmueble.getTisTipoAbastecimiento().size();
                return inmueble;
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgInmueblesSedes> codDAO = new CodigueraDAO<>(em, SgInmueblesSedes.class);
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
     * @param entity SgInmueblesSedes
     * @return SgInmueblesSedes
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgInmueblesSedes guardar(SgInmueblesSedes entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (InmueblesSedesValidacionNegocio.validar(entity)) {
                    if ((entity.getTisSedeFk() != null || entity.getTisAfUnidadAdministrativa() != null) && (BooleanUtils.isTrue(entity.getTisPropietario()))) {
                        entity.setTisActivoFijo(Boolean.TRUE);
                        CodigueraDAO<SgInmueblesSedes> codDAO = new CodigueraDAO<>(em, SgInmueblesSedes.class);
                        entity = codDAO.guardar(entity); // se genera por trigger el nro corrleativo en base a la sede.
                        em.refresh(entity); // actualizo para tener nro correlativo.

                        FiltroTipoBienes filtroTipoBien = new FiltroTipoBienes(); //No se usa incluir campos para enviar cabSeccionBienes
                        filtroTipoBien.setCodigoExacto(Constantes.CATALOGO_AF_TIPO_BIEN_TERRENO_CODIGO);
                        List<SgAfTipoBienes> tipos = catalogosClient.buscarTiposBienes(filtroTipoBien);

                        FiltroCodiguera fc = new FiltroCodiguera();
                        fc.setIncluirCampos(new String[]{"ecaVersion"});
                        fc.setCodigoExacto(Constantes.CATALOGO_AF_ESTADO_CALIDAD_BUENO_CODIGO);
                        List<SgAfEstadosCalidad> estadosCalidad = catalogosClient.buscarEstadosCalidad(fc);

                        fc = new FiltroCodiguera();
                        fc.setIncluirCampos(new String[]{"ebiVersion"});
                        fc.setCodigoExacto(Constantes.CATALOGO_AF_ESTADO_BIEN_EXISTENTE_CODIGO);
                        List<SgAfEstadosBienes> esadosBienes = catalogosClient.buscarEstadosBienes(fc);

                        //TODO: Método queda no transaccional. USAR APACHE KAFKA.
                        SgAfBienDepreciable dto = new SgAfBienDepreciable();
                        dto.setBdeDepreciado(Boolean.FALSE);
                        dto.setBdeEsValorEstimado(Boolean.FALSE);
                        if (BooleanUtils.isTrue(entity.getTisPropietario())) {
                            dto.setBdeDescripcion("MINEDUCYT");
                        } else {
                            dto.setBdeDescripcion(entity.getTisDetallePropietario());
                        }
                        dto.setBdeFechaAdquisicion(entity.getTisFechaAdquisicion());
                        dto.setBdeInmueblePk(entity.getTisPk());
                        dto.setBdeSede(entity.getTisSedeFk());
                        dto.setBdeUnidadesAdministrativas(entity.getTisAfUnidadAdministrativa());
                        dto.setBdeValorAdquisicion(entity.getTisValorAdquisicion());
                        if (!estadosCalidad.isEmpty()) {
                            dto.setBdeEstadoCalidad(estadosCalidad.get(0));
                        } else {
                            BusinessException be = new BusinessException();
                            be.addError("ESTADO_CALIDAD_INEXISTENTE_" + Constantes.CATALOGO_AF_ESTADO_CALIDAD_BUENO_CODIGO);
                            throw be;
                        }
                        if (!esadosBienes.isEmpty()) {
                            dto.setBdeEstadosBienes(esadosBienes.get(0));
                        } else {
                            BusinessException be = new BusinessException();
                            be.addError("ESTADO_BIEN_INEXISTENTE_" + Constantes.CATALOGO_AF_ESTADO_BIEN_EXISTENTE_CODIGO);
                            throw be;
                        }
                        if (!tipos.isEmpty()) {
                            dto.setBdeTipoBien(tipos.get(0));
                        } else {
                            BusinessException be = new BusinessException();
                            be.addError("TIPO_BIEN_INEXISTENTE_" + Constantes.CATALOGO_AF_TIPO_BIEN_TERRENO_CODIGO);
                            throw be;
                        }
                        //    bienesDepreciablesClient.guardar(dto);
                    } else {
                        CodigueraDAO<SgInmueblesSedes> codDAO = new CodigueraDAO<>(em, SgInmueblesSedes.class);
                        entity = codDAO.guardar(entity);
                        em.refresh(entity); // actualizo para tener nro correlativo.
                    }
                    if (entity.getTisPk() != null) {
                        if (BooleanUtils.isFalse(entity.getTisExisteOtrasSedesUnidadAdmi())) {
                            eliminarRelInmuebleUnidadResp(entity.getTisPk());
                        }
                    }

                    return entity;
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
     * Eliminar las alertas tempranas del estudiante indicado
     *
     * @param Long estPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarRelInmuebleUnidadResp(Long estPk) throws GeneralException {
        try {
            if (estPk != null) {
                FiltroRelInmuebleUnidadResp ue = new FiltroRelInmuebleUnidadResp();
                ue.setInmuebleFk(estPk);
                ue.setIncluirCampos(new String[]{"riuPk"});
                List<SgRelInmuebleUnidadResp> rel = relInmuebleUnidadRespBean.obtenerPorFiltro(ue);

                List<Long> relPks = rel.stream().map(c -> c.getRiuPk()).collect(Collectors.toList());
                if (relPks != null && !relPks.isEmpty()) {
                    Query q = em.createNativeQuery("delete from infraestructuras.sg_edificio_rel_inmueble_unidad_resp where riu_pk in (:listCal)");
                    q.setParameter("listCal", relPks);
                    q.executeUpdate();
                    em.flush();
                    em.createQuery("DELETE FROM SgRelInmuebleUnidadResp where riuInmuebleFk.tisPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                }
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroInmueblesSedes
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroInmueblesSedes filtro) throws GeneralException {
        try {
            InmueblesSedesDAO codDAO = new InmueblesSedesDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_INMUEBLE_O_TERRENO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroInmueblesSedes
     * @return Lista de SgInmueblesSedes
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgInmueblesSedes> obtenerPorFiltro(FiltroInmueblesSedes filtro) throws GeneralException {
        try {
            InmueblesSedesDAO codDAO = new InmueblesSedesDAO(em);
            return codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());

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
                eliminarRelInmuebleUnidadResp(id);
                CodigueraDAO<SgInmueblesSedes> codDAO = new CodigueraDAO<>(em, SgInmueblesSedes.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgInmueblesSedes
     * @return SgInmueblesSedes
     * @throws GeneralException
     */

    public SgInmueblesSedesLiteEspacio guardarLiteEspacio(SgInmueblesSedesLiteEspacio entity) throws GeneralException {
        try {
            if (entity != null) {                    //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if(entity.getTisPk()!=null){
                    Integer version=obtenerVersionActual(entity.getTisPk());
                    if(version!=null){
                        entity.setTisVersion(version);
                    }
                }
                for (SgRelInmuebleEspacioComun ree : entity.getTisRelInmuebleEspacioComun()) {
                    RelInmuebleEspacioComunValidacionNegocio.validar(ree);
                }
                CodigueraDAO<SgInmueblesSedesLiteEspacio> codDAO = new CodigueraDAO<>(em, SgInmueblesSedesLiteEspacio.class);
                return codDAO.guardar(entity);

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgInmueblesSedes
     * @return SgInmueblesSedes
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgInmueblesSedesLiteServicio guardarLiteServicio(SgInmueblesSedesLiteServicio entity) throws GeneralException {
        try {
            if (entity != null) {                    //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (entity.getTisPk() != null) {
                    Integer version = obtenerVersionActual(entity.getTisPk());
                    if (version != null) {
                        entity.setTisVersion(version);
                    }
                }

                CodigueraDAO<SgInmueblesSedesLiteServicio> codDAO = new CodigueraDAO<>(em, SgInmueblesSedesLiteServicio.class);
                return codDAO.guardar(entity);

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    public Integer obtenerVersionActual(Long id) {
        FiltroInmueblesSedes fis = new FiltroInmueblesSedes();
        fis.setInmueblePk(id);
        fis.setIncluirCampos(new String[]{"tisVersion"});
        List<SgInmueblesSedes> inm = this.obtenerPorFiltro(fis);
        if (!inm.isEmpty()) {
            return inm.get(0).getTisVersion();
        }
        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgInmueblesSedes
     * @return SgInmueblesSedes
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgInmueblesSedesLiteOtroServicio guardarLiteOtroServicio(SgInmueblesSedesLiteOtroServicio entity) throws GeneralException {
        try {
            if (entity != null) {                    //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (entity.getTisPk() != null) {
                    Integer version = obtenerVersionActual(entity.getTisPk());
                    if (version != null) {
                        entity.setTisVersion(version);
                    }
                }
                CodigueraDAO<SgInmueblesSedesLiteOtroServicio> codDAO = new CodigueraDAO<>(em, SgInmueblesSedesLiteOtroServicio.class);
                return codDAO.guardar(entity);

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgInmueblesSedes
     * @return SgInmueblesSedes
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgInmueblesSedesLiteObraExterior guardarLiteObraExterior(SgInmueblesSedesLiteObraExterior entity) throws GeneralException {
        try {
            if (entity != null) {                    //Validar el elemento a guardar. Si no valida, se lanza una excepcion

                CodigueraDAO<SgInmueblesSedesLiteObraExterior> codDAO = new CodigueraDAO<>(em, SgInmueblesSedesLiteObraExterior.class);
                return codDAO.guardar(entity);

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

}
