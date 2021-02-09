/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAplicacionPlaza;
import sv.gob.mined.siges.filtros.FiltroDatoContratacion;
import sv.gob.mined.siges.filtros.FiltroEspecialidadesPersonalAlAplicar;
import sv.gob.mined.siges.filtros.FiltroRelPersonalEspecialidad;
import sv.gob.mined.siges.negocio.validaciones.AplicacionPlazaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.AplicacionPlazaDAO;
import sv.gob.mined.siges.persistencia.daos.EspecialidadPersonalAlAplicarDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAplicacionPlaza;
import sv.gob.mined.siges.persistencia.entidades.SgDatoContratacion;
import sv.gob.mined.siges.persistencia.entidades.SgDatoEmpleado;
import sv.gob.mined.siges.persistencia.entidades.SgEspecialidadesPersonalAlAplicar;
import sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativa;
import sv.gob.mined.siges.persistencia.entidades.SgRelPersonalEspecialidad;
import sv.gob.mined.siges.persistencia.utilidades.RevHistorico;

@Stateless
@Traced
public class AplicacionPlazaBean {

    private static final Logger LOGGER = Logger.getLogger(AplicacionPlazaBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private DatoContratacionBean datoContratacionBean;

    @Inject
    private RelPersonalEspecialidadBean relPersonalEspecialidadBean;
    
    @Inject
    private PersonaBean personaBean;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id
     * @return SgAplicacionPlaza
     * @throws GeneralException
     */
    public SgAplicacionPlaza obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAplicacionPlaza> codDAO = new CodigueraDAO<>(em, SgAplicacionPlaza.class);
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
     * @param id
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAplicacionPlaza> codDAO = new CodigueraDAO<>(em, SgAplicacionPlaza.class);
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
     * @param entity
     * @return SgAplicacionPlaza
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAplicacionPlaza guardar(SgAplicacionPlaza entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (AplicacionPlazaValidacionNegocio.validar(entity)) {

                    if (entity.getAplPk() == null) {
                        List<RevHistorico> rev = ch.obtenerHistorialRevisionesPorId(SgPersonalSedeEducativa.class, entity.getAplPersonal().getPsePk());
                        if (!rev.isEmpty()) {
                            entity.setAplRevPersonalCuandoAplica(rev.get(0).getRevEntity().getRev());
                        }
                        FiltroRelPersonalEspecialidad perEsp=new FiltroRelPersonalEspecialidad();
                        perEsp.setPersonal(entity.getAplPersonal().getPsePk());
                        perEsp.setIncluirCampos(new String[]{"rpeEspecialidad.espPk","rpeEspecialidad.espVersion","rpeFechaGraduacion","rpeCum"});
                        List<SgRelPersonalEspecialidad> especialidades= relPersonalEspecialidadBean.obtenerPorFiltro(perEsp);
                        if(!especialidades.isEmpty()){
                            List<SgEspecialidadesPersonalAlAplicar> espAlAplList=new ArrayList();
                            for(SgRelPersonalEspecialidad esp:especialidades){
                                SgEspecialidadesPersonalAlAplicar espAlApl=new SgEspecialidadesPersonalAlAplicar();
                                espAlApl.setEpaAplicacionPlazaFk(entity);
                                espAlApl.setEpaCum(esp.getRpeCum());
                                espAlApl.setEpaEspecialidad(esp.getRpeEspecialidad());
                                espAlApl.setEpaFechaGraduacion(esp.getRpeFechaGraduacion());
                                espAlAplList.add(espAlApl);
                            }
                            entity.setAplEspecialidadesAlAplicar(espAlAplList);
                        }
                    }
                    CodigueraDAO<SgAplicacionPlaza> codDAO = new CodigueraDAO<>(em, SgAplicacionPlaza.class);
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

    /**
     * Guarda el objeto indicado
     *
     * @param entity
     * @return SgAplicacionPlaza
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAplicacionPlaza guardarSeleccionadoMatriz(SgAplicacionPlaza entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (AplicacionPlazaValidacionNegocio.validar(entity)) {
                    FiltroAplicacionPlaza fap = new FiltroAplicacionPlaza();
                    fap.setSeleccionadoEnMatriz(Boolean.TRUE);
                    fap.setAplPlaza(entity.getAplPlaza().getSplPk());
                    List<SgAplicacionPlaza> list = this.obtenerPorFiltro(fap);

                    CodigueraDAO<SgAplicacionPlaza> codDAO = new CodigueraDAO<>(em, SgAplicacionPlaza.class);
                    if (!list.isEmpty()) {
                        for (SgAplicacionPlaza apl : list) {
                            if (!apl.getAplPersonal().getPsePk().equals(entity.getAplPersonal().getPsePk())) {
                                apl.setAplSeleccionadoEnMatriz(Boolean.FALSE);
                                apl.setAplObservacion(null);
                                codDAO.guardar(apl, null);
                            }

                        }
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

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroAplicacionPlaza filtro) throws GeneralException {
        try {
            AplicacionPlazaDAO codDAO = new AplicacionPlazaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro
     * @return List
     * @throws GeneralException
     */
    public List<SgAplicacionPlaza> obtenerPorFiltro(FiltroAplicacionPlaza filtro) throws GeneralException {
        try {
            AplicacionPlazaDAO codDAO = new AplicacionPlazaDAO(em);
            List<SgAplicacionPlaza> aplicaciones = codDAO.obtenerPorFiltro(filtro);

            if (!aplicaciones.isEmpty()) {
                if (BooleanUtils.isTrue(filtro.getInicializarSedesPersonal())) {
                    FiltroDatoContratacion fDato = new FiltroDatoContratacion();
                    fDato.setOrderBy(new String[]{"dcoCentroEducativo.sedNombre"});
                    fDato.setAscending(new boolean[]{true});
                    fDato.setIncluirCampos(new String[]{"dcoCentroEducativo.sedPk",
                        "dcoCentroEducativo.sedCodigo",
                        "dcoCentroEducativo.sedNombre",
                        "dcoCentroEducativo.sedTipo",
                        "dcoDatoEmpleado.demPersonalSede.psePk",
                        "dcoCentroEducativo.sedDireccion.dirDepartamento.depNombre",
                        "dcoCentroEducativo.sedDireccion.dirMunicipio.munNombre"});
                    fDato.setContratosActivos(Boolean.TRUE);
                    fDato.setDcoPersonalesPk(aplicaciones.stream().map(p -> p.getAplPersonal().getPsePk()).collect(Collectors.toList()));
                    List<SgDatoContratacion> datos = datoContratacionBean.obtenerPorFiltro(fDato);

                    HashMap<Long, List<SgDatoContratacion>> datosContratAgrupados = new HashMap<>();
                    for (SgDatoContratacion dc : datos) {
                        datosContratAgrupados.computeIfAbsent(dc.getDcoDatoEmpleado().getDemPersonalSede().getPsePk(), s -> new ArrayList<>()).add(dc);
                    }
                    for (SgAplicacionPlaza app : aplicaciones) {
                        app.getAplPersonal().setPseDatoEmpleado(new SgDatoEmpleado());
                        app.getAplPersonal().getPseDatoEmpleado().setDemDatoContratacion(datosContratAgrupados.get(app.getAplPersonal().getPsePk()) != null ? datosContratAgrupados.get(app.getAplPersonal().getPsePk()) : new ArrayList<>());
                    }
                }
                if (BooleanUtils.isTrue(filtro.getInicializarDiscapacidades())) {
                    for (SgAplicacionPlaza app : aplicaciones) {
                        if(app.getAplPersonal() != null && app.getAplPersonal().getPsePersona() != null){
                            app.getAplPersonal().getPsePersona().setPerDiscapacidades(personaBean.obtenerDiscapacidades(app.getAplPersonal().getPsePersona().getPerPk()));
                        }
                    }
                }
                
                if (BooleanUtils.isTrue(filtro.getInicializarMotivosSeleccion())) {
                    FiltroAplicacionPlaza filtroAp = new FiltroAplicacionPlaza();
                    for (SgAplicacionPlaza app : aplicaciones) {
                        app.setAplMotivosSeleccionPLaza(codDAO.obtenerMotivosSeleccion(app.getAplPk()));
                        
                        //Se buscan si el docente ya ha sido seleccionado en otra plaza
                        filtroAp = new FiltroAplicacionPlaza();
                        filtroAp.setAplPersonal(app.getAplPersonal().getPsePk());
                        filtroAp.setSeleccionadoEnMatriz(Boolean.TRUE);
                        List<SgAplicacionPlaza> resultado = codDAO.obtenerPorFiltro(filtroAp);
                        if(!resultado.isEmpty()){
                            resultado.forEach(x -> {
                                if(!x.getAplPk().equals(app.getAplPk())){
                                    app.setPlazaSeleccionado(resultado.get(0));
                                }
                            });
                        }
                    }
                }

                if (BooleanUtils.isTrue(filtro.getInicializarEspecialidades())) {
                    FiltroEspecialidadesPersonalAlAplicar fepa=new FiltroEspecialidadesPersonalAlAplicar();
                    fepa.setAplicacionesPlazaFk(aplicaciones.stream().map(p -> p.getAplPk()).collect(Collectors.toList()));
                    fepa.setEspecialidades(filtro.getEspecialidades());
                    fepa.setEpaCum(filtro.getEpaCum());
                    fepa.setEpaFechaGraduacionDesde(filtro.getEpaFechaGraduacionDesde());
                    fepa.setEpaFechaGraduacionHasta(filtro.getEpaFechaGraduacionHasta());
                    fepa.setIncluirCampos(new String[]{"epaCum",
                        "epaEspecialidad.espPk",
                        "epaEspecialidad.espNombre",
                        "epaEspecialidad.espVersion",
                        "epaFechaGraduacion",
                        "epaVersion", "epaAplicacionPlazaFk.aplPk"});
                    EspecialidadPersonalAlAplicarDAO codDAOEsp = new EspecialidadPersonalAlAplicarDAO(em);
                    List<SgEspecialidadesPersonalAlAplicar> especialidades = codDAOEsp.obtenerPorFiltro(fepa);
                    
                     if (!especialidades.isEmpty()) {
                        for (SgAplicacionPlaza app : aplicaciones) {
                            app.setAplEspecialidadesAlAplicar(new ArrayList());
                            app.setAplEspecialidadesAlAplicar(especialidades.stream().filter(p -> p.getEpaAplicacionPlazaFk().getAplPk().equals(app.getAplPk())).collect(Collectors.toList()));
                        }
                     }
                    
                    /*   FiltroRelPersonalEspecialidad frel = new FiltroRelPersonalEspecialidad();
                    frel.setIncluirCampos(new String[]{"rpeCum",
                        "rpeEspecialidad.espPk",
                        "rpeEspecialidad.espNombre",
                        "rpeEspecialidad.espVersion",
                        "rpeFechaGraduacion",
                        "rpeVersion", "rpePersonal.psePk"});
                    frel.setRplPersonalesPk(aplicaciones.stream().map(p -> p.getAplPersonal().getPsePk()).collect(Collectors.toList()));
                    List<SgRelPersonalEspecialidad> especialidades = relPersonalEspecialidadBean.obtenerPorFiltro(frel);

                    if (!especialidades.isEmpty()) {
                        for (SgAplicacionPlaza app : aplicaciones) {
                            app.getAplPersonal().setPseRelEspecialidades(new ArrayList());
                            app.getAplPersonal().setPseRelEspecialidades(especialidades.stream().filter(p -> p.getRpePersonal().getPsePk().equals(app.getAplPersonal().getPsePk())).collect(Collectors.toList()));
                        }
                    }*/
                }
            }

            return aplicaciones;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAplicacionPlaza> codDAO = new CodigueraDAO<>(em, SgAplicacionPlaza.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
