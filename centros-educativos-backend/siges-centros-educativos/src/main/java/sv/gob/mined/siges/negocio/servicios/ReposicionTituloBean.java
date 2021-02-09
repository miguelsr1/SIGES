/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import sv.gob.mined.siges.dto.SgReposicionSolicitud;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.enumerados.EnumTipoSolicitudImpresion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroReposicionTitulo;
import sv.gob.mined.siges.filtros.FiltroSelloFirmaTitular;
import sv.gob.mined.siges.filtros.FiltroTitulo;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.ReposicionTituloValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ReposicionTituloDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteImpresion;
import sv.gob.mined.siges.persistencia.entidades.SgReposicionTitulo;
import sv.gob.mined.siges.persistencia.entidades.SgSelloFirmaTitular;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudImpresion;
import sv.gob.mined.siges.persistencia.entidades.SgTitulo;
import sv.gob.mined.siges.persistencia.entidades.SgTituloLite;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ReposicionTituloBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SelloFirmaTitularBean selloFirmaTitularBean;

    @Inject
    private SolicitudImpresionBean solicitudImpresionBean;

    @Inject
    private EstudianteImpresionBean estudianteImpresionBean;



    @Inject
    private TituloBean tituloBean;

    @Inject
    private ArchivoBean archivoBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgReposicionTitulo
     * @throws GeneralException
     */
    public SgReposicionTitulo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgReposicionTitulo> codDAO = new CodigueraDAO<>(em, SgReposicionTitulo.class);
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
                CodigueraDAO<SgReposicionTitulo> codDAO = new CodigueraDAO<>(em, SgReposicionTitulo.class);
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
     * @param ret SgReposicionTitulo
     * @return SgReposicionTitulo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgReposicionTitulo guardar(SgReposicionTitulo ret) throws GeneralException {
        try {
            if (ret != null) {
                if (ReposicionTituloValidacionNegocio.validar(ret)) {
                    if (ret.getRetPk() == null) {
                        SgSolicitudImpresion si = ret.getRetSolicitudImpresion();
                        si.setSimEstado(EnumEstadoSolicitudImpresion.ENVIADA);
                        si.setSimFechaSolicitud(LocalDate.now());
                        si.setSimReposicionTitulo(ret);

                        FiltroSelloFirmaTitular fsft = new FiltroSelloFirmaTitular();
                        fsft.setSftHabilitado(Boolean.TRUE);
                        fsft.setSftCodigoCargo(Constantes.CARGO_MINISTRO_SELLO_FIRMA);
                        List<SgSelloFirmaTitular> listSelloFirmaTitular = selloFirmaTitularBean.obtenerPorFiltro(fsft);
                        if (listSelloFirmaTitular.size() > 0) {
                            si.setSimSelloMinistro(listSelloFirmaTitular.get(0));
                        } else {
                            BusinessException ge = new BusinessException();
                            ge.addError(Errores.ERROR_SELLO_FIRMA_MINISTRO_VACIO);
                            throw ge;
                        }
                        
                        if(ret.getRetSolicitudImpresion().getSimResolucionFk()!=null){
                            SgArchivo ar = archivoBean.guardar(ret.getRetSolicitudImpresion().getSimResolucionFk());
                            ret.getRetSolicitudImpresion().setSimResolucionFk(ar);
                        }else{
                            ret.getRetSolicitudImpresion().setSimResolucionFk(null);
                        }

                        CodigueraDAO<SgSolicitudImpresion> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresion.class);
                        si = codDAO.guardar(si, si.getSimPk() == null ? ConstantesOperaciones.CREAR_SOLICITUD_IMPRESION : ConstantesOperaciones.ACTUALIZAR_SOLICITUD_IMPRESION);

                        ret.setRetSolicitudImpresion(si);
                        
                    }

                    CodigueraDAO<SgReposicionTitulo> codDAO = new CodigueraDAO<>(em, SgReposicionTitulo.class);

                    return codDAO.guardar(ret, ret.getRetPk() != null ? ConstantesOperaciones.ACTUALIZAR_REPOSICION_TITULO : ConstantesOperaciones.CREAR_REPOSICION_TITULO);

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ret;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroReposicionTitulo filtro) throws GeneralException {
        try {
            ReposicionTituloDAO codDAO = new ReposicionTituloDAO(em);
            return codDAO.cantidadTotal(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgReposicionTitulo>
     * @throws GeneralException
     */
    public List<SgReposicionTitulo> obtenerPorFiltro(FiltroReposicionTitulo filtro) throws GeneralException {
        try {
            ReposicionTituloDAO codDAO = new ReposicionTituloDAO(em);
            List<SgReposicionTitulo> rep = codDAO.obtenerPorFiltro(filtro, null);
            for (SgReposicionTitulo r : rep) {
                if (r.getRetSolicitudImpresion() != null) {
                    r.getRetSolicitudImpresion().getSimPk();
                }
            }
            return rep;
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
                CodigueraDAO<SgReposicionTitulo> codDAO = new CodigueraDAO<>(em, SgReposicionTitulo.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    public SgSolicitudImpresion reponerSolicitud(SgReposicionSolicitud reposicion) throws DAOGeneralException, Exception {
        BusinessException be = new BusinessException();
        if (reposicion.getNumeroResolucion() == null) {
            be.addError("calSeccion", Errores.ERROR_NUMERO_RESOLUCION_VACIO);
        }
        if (reposicion.getResolucion() == null) {
            be.addError("calSeccion", Errores.ERROR_RESOLUCION_VACIO);
        }
        if (reposicion.getTituloPk() == null) {
            be.addError("calSeccion", Errores.ERROR_TITULO_VACIO);
        }
        if (be.getErrores().size() > 0) {
            throw be;
        }

        if (reposicion.getTituloPk() != null) {
            FiltroTitulo ftit = new FiltroTitulo();
            ftit.setTitPk(reposicion.getTituloPk());
            ftit.setTitNoAnulada(Boolean.TRUE);
            ftit.setIncluirCampos(new String[]{"titSolicitudImpresionFk.simFechaSolicitud",
                "titSolicitudImpresionFk.simFechaEnviadoImprimir",
                "titSolicitudImpresionFk.simEstado",
                "titSolicitudImpresionFk.simTipoImpresion",
                "titSolicitudImpresionFk.simSeccion.secPk",
                "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedPk",
                "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedTipo",
                "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedVersion",
                "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk",
                "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedVersion",
                "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedTipo",
                "titSolicitudImpresionFk.simSeccion.secVersion",
                "titSolicitudImpresionFk.simUsuario.usuPk",
                "titSolicitudImpresionFk.simUsuario.usuVersion",
                "titSolicitudImpresionFk.simInicioNumeroCorrelativo",
                "titSolicitudImpresionFk.simVersion",
                "titSolicitudImpresionFk.simDefinicionTitulo.dtiPk",
                "titSolicitudImpresionFk.simDefinicionTitulo.dtiVersion",
                "titSolicitudImpresionFk.simDefinicionTitulo.dtiNombreCertificado",
                "titSolicitudImpresionFk.simReposicionTitulo.retPk",
                "titSolicitudImpresionFk.simReposicionTitulo.retVersion",
                "titSolicitudImpresionFk.simReposicionTitulo.retNombreEstudiantePartida",
                "titSolicitudImpresionFk.simReposicionTitulo.retNombreEstudiantePartidaBusqueda",
                "titSolicitudImpresionFk.simReposicionTitulo.retAnio",
                "titSolicitudImpresionFk.simReposicionTitulo.retSedeNombre",
                "titSolicitudImpresionFk.simReposicionTitulo.retSedeNombreBusqueda",
                "titSolicitudImpresionFk.simReposicionTitulo.retEstadoReposicion",
                "titSolicitudImpresionFk.simReposicionTitulo.retUsuarioEnviaImprimir",
                "titSolicitudImpresionFk.simNumeroResolucion",
                "titSolicitudImpresionFk.simReposicionTitulo.retSede.sedPk",
                "titSolicitudImpresionFk.simReposicionTitulo.retSede.sedVersion",
                "titSolicitudImpresionFk.simReposicionTitulo.retSede.sedTipo",
                "titSolicitudImpresionFk.simReposicionTitulo.retSede.sedCodigo",
                "titSolicitudImpresionFk.simReposicionTitulo.retSede.sedNombre",
                "titSolicitudImpresionFk.simReposicionTitulo.retSede.sedTipo",
                "titSolicitudImpresionFk.simReposicionTitulo.retSede.sedSedeAdscritaDe.sedPk",
                "titSolicitudImpresionFk.simReposicionTitulo.retSede.sedSedeAdscritaDe.sedCodigo",
                "titSolicitudImpresionFk.simReposicionTitulo.retSede.sedSedeAdscritaDe.sedNombre",
                "titSolicitudImpresionFk.simReposicionTitulo.retSede.sedSedeAdscritaDe.sedTipo",
                "titSolicitudImpresionFk.simReposicionTitulo.retSede.sedSedeAdscritaDe.sedVersion",
                "titSolicitudImpresionFk.simReposicionTitulo.retSolicitudImpresion.simPk",
                "titSolicitudImpresionFk.simReposicionTitulo.retSolicitudImpresion.simVersion",
                "titSolicitudImpresionFk.simReposicionTitulo.retAnulada",
                "titSolicitudImpresionFk.simReposicionTitulo.retMotivoAnulacion.matPk",
                "titSolicitudImpresionFk.simReposicionTitulo.retMotivoAnulacion.matVersion",
                "titSolicitudImpresionFk.simReposicionTitulo.retObservacionAnulada",
                "titSolicitudImpresionFk.simReposicionTitulo.retOpcionBachillerato",
                "titSolicitudImpresionFk.simReposicionTitulo.retDuiSolicitante",
                "titSolicitudImpresionFk.simReposicionTitulo.retFechaLegalizacionTitulo",
                "titSolicitudImpresionFk.simReposicionTitulo.retTituloAnterior2008.tanNombre",
                "titSolicitudImpresionFk.simReposicionTitulo.retTituloAnterior2008.tanPk",
                "titSolicitudImpresionFk.simReposicionTitulo.retTituloAnterior2008.tanVersion",
                "titSolicitudImpresionFk.simNumeroResolucion",
                "titSolicitudImpresionFk.simImpresora",
                "titSolicitudImpresionFk.simReposicionEstudianteSiges",
                "titSolicitudImpresionFk.simNumeroRegistro",
                "titSolicitudImpresionFk.simFechaValidez",
                "titEstudianteFk.estPk",
                "titEstudianteFk.estVersion",
                "titNombreEstudiante",
                "titVersion",
                "titEstudianteFk.estPk",
                "titEstudianteFk.estVersion",
                "titNombreEstudiante",
                "titNombreEstudiantePartida",
                "titNombreCertificado",
                "titFechaValidez",
                "titFechaEmision",
                "titAnio",
                "titSedeFk.sedPk",
                "titSedeFk.sedVersion",
                "titSedeFk.sedTipo",
                "titSedeCodigo",
                "titSedeNombre",
                "titServicioEducativoFk.sduPk",
                "titServicioEducativoFk.sduVersion",
                "titSeccionFk.secPk",
                "titSeccionFk.secVersion",
                "titSeccionFk.secServicioEducativo.sduSede.sedPk",
                "titSeccionFk.secServicioEducativo.sduSede.sedTipo",
                "titSeccionFk.secServicioEducativo.sduSede.sedVersion",
                "titSeccionFk.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk",
                "titSeccionFk.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedVersion",
                "titSeccionFk.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedTipo",
            });
            List<SgTitulo> titulos = tituloBean.obtenerPorFiltro(ftit);

            if (!titulos.isEmpty()) {
                SgSolicitudImpresion solOriginal = titulos.get(0).getTitSolicitudImpresionFk();

                SgSolicitudImpresion simp = new SgSolicitudImpresion();

                simp.setSimEstado(EnumEstadoSolicitudImpresion.ENVIADA);
                simp.setSimTipoImpresion(EnumTipoSolicitudImpresion.REP);
                simp.setSimFechaSolicitud(LocalDate.now());
                simp.setSimUsuario(reposicion.getUsuarioSolicitante());
                
                if(titulos.get(0).getTitSeccionFk()==null && (solOriginal.getSimSeccion()==null 
                        && solOriginal.getSimTipoImpresion().equals(EnumTipoSolicitudImpresion.REP)
                        && solOriginal.getSimReposicionEstudianteSiges()==null)){
                    be.addError("calSeccion", Errores.ERROR_SECCION_VACIO);
                    throw be;
                }
                if(reposicion.getDefinicionTitulo()==null && (solOriginal==null ||solOriginal.getSimDefinicionTitulo()==null) ){
                    be.addError("calSeccion", Errores.ERROR_DEFINICION_TITULO_VACIO);
                    throw be;
                }                
                
                simp.setSimSeccion(titulos.get(0).getTitSeccionFk()!=null?titulos.get(0).getTitSeccionFk(): solOriginal.getSimSeccion());
                simp.setSimDefinicionTitulo(solOriginal!=null?solOriginal.getSimDefinicionTitulo():reposicion.getDefinicionTitulo());                
                
                simp.setSimNumeroResolucion(reposicion.getNumeroResolucion());
                simp.setSimNumeroRegistro(solOriginal!=null?solOriginal.getSimNumeroRegistro():null);
                simp.setSimReposicionEstudianteSiges(Boolean.TRUE);
                simp.setSimFechaValidez(null);
                SgArchivo ar = archivoBean.guardar(reposicion.getResolucion());

                simp.setSimResolucionFk(ar);
                
                //Se carga en Generacion titulo
                simp.setSimSelloDirector(null);
                simp.setSimSelloMinistro(null);
                simp.setSimSelloAutentica(null);

                if (solOriginal==null ||
                        solOriginal.getSimTipoImpresion().equals(EnumTipoSolicitudImpresion.IMP)
                        || solOriginal.getSimTipoImpresion().equals(EnumTipoSolicitudImpresion.REI)
                        || (solOriginal.getSimTipoImpresion().equals(EnumTipoSolicitudImpresion.REP)
                        && BooleanUtils.isTrue(solOriginal.getSimReposicionEstudianteSiges()))) {

                    SgEstudianteImpresion estimp = new SgEstudianteImpresion();
                    estimp.setEimEstudiante(titulos.get(0).getTitEstudianteFk());
                    List<SgEstudianteImpresion> listEstImpresion = new ArrayList<>();
                    listEstImpresion.add(estimp);
                    estimp.setEimSolicitudImpresion(simp);
                    simp.setSimEstudianteImpresion(listEstImpresion);
                    
                    simp.setSimEvitarValidacionSelloFirma(Boolean.TRUE);//Con este dato en true se evita la validación de q tenga sello firma
                    
                    simp = solicitudImpresionBean.guardar(simp);

                    titulos.get(0).setTitAnulada(Boolean.TRUE);
                    SgTituloLite titLite = new SgTituloLite();
                    titLite.setTitAnulada(Boolean.TRUE);
                    titLite.setTitPk(titulos.get(0).getTitPk());
                    titLite.setTitVersion(titulos.get(0).getTitVersion());
                    CodigueraDAO<SgTituloLite> codDAO = new CodigueraDAO<>(em, SgTituloLite.class);
                    codDAO.guardar(titLite, null);
                    return simp;

                } else {
                    if ((solOriginal.getSimTipoImpresion().equals(EnumTipoSolicitudImpresion.REP)
                            && BooleanUtils.isNotTrue(solOriginal.getSimReposicionEstudianteSiges()))) {
                        SgReposicionTitulo repOriginal = titulos.get(0).getTitSolicitudImpresionFk().getSimReposicionTitulo();

                        SgReposicionTitulo repTit = new SgReposicionTitulo();
                        repTit.setRetAnio(repOriginal.getRetAnio());
                        repTit.setRetDuiSolicitante(repOriginal.getRetDuiSolicitante());
                        repTit.setRetFechaLegalizacionTitulo(repOriginal.getRetFechaLegalizacionTitulo());
                        repTit.setRetNombreEstudiantePartida(repOriginal.getRetNombreEstudiantePartida());
                        repTit.setRetNombreEstudiantePartidaBusqueda(repOriginal.getRetNombreEstudiantePartidaBusqueda());
                        repTit.setRetOpcionBachillerato(repOriginal.getRetOpcionBachillerato());
                        repTit.setRetSede(repOriginal.getRetSede());
                        repTit.setRetSedeNombre(repOriginal.getRetSedeNombre());
                        repTit.setRetSedeNombreBusqueda(repOriginal.getRetSedeNombreBusqueda());
                        repTit.setRetTituloAnterior2008(repOriginal.getRetTituloAnterior2008());
                        repTit.setRetUsuarioEnviaImprimir(repOriginal.getRetUsuarioEnviaImprimir());
                        

                        repTit.setRetSolicitudImpresion(simp);
                        simp.setSimReposicionEstudianteSiges(Boolean.FALSE);
                        simp.setSimReposicionTitulo(repTit);
                        this.guardar(repTit);

                        titulos.get(0).setTitAnulada(Boolean.TRUE);
                        SgTituloLite titLite = new SgTituloLite();
                        titLite.setTitAnulada(Boolean.TRUE);
                        titLite.setTitPk(titulos.get(0).getTitPk());
                        titLite.setTitVersion(titulos.get(0).getTitVersion());
                        CodigueraDAO<SgTituloLite> codDAO = new CodigueraDAO<>(em, SgTituloLite.class);
                        codDAO.guardar(titLite, null);
                        return simp;

                    }
                }

            }
        }
        return null;
    }

}
