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
import sv.gob.mined.siges.filtros.FiltroEdificio;
import sv.gob.mined.siges.filtros.FiltroTipoBienes;
import sv.gob.mined.siges.negocio.validaciones.EdificioValidacionNegocio;
import sv.gob.mined.siges.negocio.validaciones.RelEdificioEspacioValidacionNegocio;
import sv.gob.mined.siges.negocio.validaciones.RelEdificioServicioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EdificioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEdificio;
import sv.gob.mined.siges.persistencia.entidades.SgEdificioLiteEspacio;
import sv.gob.mined.siges.persistencia.entidades.SgEdificioLiteServicio;
import sv.gob.mined.siges.persistencia.entidades.SgRelEdificioEspacio;
import sv.gob.mined.siges.persistencia.entidades.SgRelEdificioServicio;
import sv.gob.mined.siges.restclient.BienesDepreciablesRestClient;
import sv.gob.mined.siges.restclient.CatalogosRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class EdificioBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private BienesDepreciablesRestClient bienesDepreciablesClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEdificio
     * @throws GeneralException
     */
    public SgEdificio obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEdificio> codDAO = new CodigueraDAO<>(em, SgEdificio.class);
                SgEdificio edf=codDAO.obtenerPorId(id);
                if(edf.getEdiRelInmuebleUnidadResp()!=null){
                    edf.getEdiRelInmuebleUnidadResp().size();
                }
                return edf;
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
                CodigueraDAO<SgEdificio> codDAO = new CodigueraDAO<>(em, SgEdificio.class);
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
     * @param edi SgEdificio
     * @return SgEdificio
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEdificio guardar(SgEdificio edi) throws GeneralException {
        try {
            if (edi != null) {
                if (EdificioValidacionNegocio.validar(edi)) {
                    CodigueraDAO<SgEdificio> codDAO = new CodigueraDAO<>(em, SgEdificio.class);

                    edi = codDAO.guardar(edi);
                    if ((edi.getEdiInmuebleSedeFk().getTisSedeFk() != null || edi.getEdiInmuebleSedeFk().getTisAfUnidadAdministrativa() != null) && (BooleanUtils.isTrue(edi.getEdiInmuebleSedeFk().getTisPropietario()))) {

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
                        if (BooleanUtils.isTrue(edi.getEdiInmuebleSedeFk().getTisPropietario())) {
                            dto.setBdeDescripcion("MINEDUCYT");
                        } else {
                            dto.setBdeDescripcion(edi.getEdiInmuebleSedeFk().getTisDetallePropietario());
                        }
                        dto.setBdeFechaAdquisicion(edi.getEdiInmuebleSedeFk().getTisFechaAdquisicion());
                        dto.setBdeEdificioPk(edi.getEdiPk());
                        dto.setBdeSede(edi.getEdiInmuebleSedeFk().getTisSedeFk());
                        dto.setBdeUnidadesAdministrativas(edi.getEdiInmuebleSedeFk().getTisAfUnidadAdministrativa());
                        dto.setBdeValorAdquisicion(edi.getEdiInversion());
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
                            be.addError("TIPO_BIEN_INEXISTENTE_" + Constantes.CATALOGO_AF_TIPO_BIEN_EDIFICIO_CODIGO);
                            throw be;
                        }
                        bienesDepreciablesClient.guardar(dto);
                    }

                    return edi;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return edi;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEdificio filtro) throws GeneralException {
        try {
            EdificioDAO codDAO = new EdificioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_EDIFICIO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgEdificio>
     * @throws GeneralException
     */
    public List<SgEdificio> obtenerPorFiltro(FiltroEdificio filtro) throws GeneralException {
        try {
            EdificioDAO codDAO = new EdificioDAO(em);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_EDIFICIO);
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
                CodigueraDAO<SgEdificio> codDAO = new CodigueraDAO<>(em, SgEdificio.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgEdificio
     * @return SgEdificio
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgEdificioLiteEspacio guardarLiteEspacio(SgEdificioLiteEspacio entity) throws GeneralException {
        try {
            if (entity != null) {                   //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (entity.getEdiRelEdificioEspacio() != null) {

                    for (SgRelEdificioEspacio ree : entity.getEdiRelEdificioEspacio()) {
                        RelEdificioEspacioValidacionNegocio.validar(ree);
                    }

                    CodigueraDAO<SgEdificioLiteEspacio> codDAO = new CodigueraDAO<>(em, SgEdificioLiteEspacio.class);
                    return codDAO.guardar(entity);

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
     * Guarda el objeto indicado
     *
     * @param entity SgEdificio
     * @return SgEdificio
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgEdificioLiteServicio guardarLiteServicio(SgEdificioLiteServicio entity) throws GeneralException {
        try {
            if (entity != null) {                    
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                for (SgRelEdificioServicio ree : entity.getEdiRelEdificioServicio()) {
                    RelEdificioServicioValidacionNegocio.validar(ree);

                }
                CodigueraDAO<SgEdificioLiteServicio> codDAO = new CodigueraDAO<>(em, SgEdificioLiteServicio.class);
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
