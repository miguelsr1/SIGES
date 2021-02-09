/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.dto.SgGradoPlan;
import sv.gob.mined.siges.dto.SgGradoPlanOrigenDestino;
import sv.gob.mined.siges.enumerados.EnumOperacionReglaEquivalencia;
import sv.gob.mined.siges.enumerados.EnumPromocionGradoMatricula;
import sv.gob.mined.siges.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEscolaridadEstudiante;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.filtros.FiltroReglaEquivalencia;
import sv.gob.mined.siges.filtros.FiltroReglaEquivalenciaDetalle;
import sv.gob.mined.siges.filtros.FiltroRelGradoPrecedencia;
import sv.gob.mined.siges.negocio.validaciones.ReglaEquivalenciaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ReglaEquivalenciaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEscolaridadEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgGrado;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgReglaEquivalencia;
import sv.gob.mined.siges.persistencia.entidades.SgReglaEquivalenciaDetalle;
import sv.gob.mined.siges.persistencia.entidades.SgRelGradoPrecedencia;

@Stateless
@Traced
public class ReglaEquivalenciaBean {

    private static final Logger LOGGER = Logger.getLogger(ReglaEquivalenciaBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ReglaEquivalenciaDetalleBean reglaEquivalenciaDetalleBean;

    @Inject
    private MatriculaBean matriculaBean;

    @Inject
    private EscolaridadEstudianteBean escolaridadEstudianteBean;

    @Inject
    private RelGradoPrecedenciaBean relGradoPrecedenciaBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgReglaEquivalencia
     * @throws GeneralException
     */
    public SgReglaEquivalencia obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgReglaEquivalencia> codDAO = new CodigueraDAO<>(em, SgReglaEquivalencia.class);
                SgReglaEquivalencia ret = codDAO.obtenerPorId(id, null);
                return ret;
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
                CodigueraDAO<SgReglaEquivalencia> codDAO = new CodigueraDAO<>(em, SgReglaEquivalencia.class);
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
     * @param entity SgReglaEquivalencia
     * @return SgReglaEquivalencia
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgReglaEquivalencia guardar(SgReglaEquivalencia entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ReglaEquivalenciaValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgReglaEquivalencia> codDAO = new CodigueraDAO<>(em, SgReglaEquivalencia.class);
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
     * @param filtro FiltroReglaEquivalencia
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroReglaEquivalencia filtro) throws GeneralException {
        try {
            ReglaEquivalenciaDAO codDAO = new ReglaEquivalenciaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroReglaEquivalencia
     * @return Lista de SgReglaEquivalencia
     * @throws GeneralException
     */
    public List<SgReglaEquivalencia> obtenerPorFiltro(FiltroReglaEquivalencia filtro) throws GeneralException {
        try {
            ReglaEquivalenciaDAO codDAO = new ReglaEquivalenciaDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgReglaEquivalencia> codDAO = new CodigueraDAO<>(em, SgReglaEquivalencia.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    public Boolean cumpleReglaEquivalencia(SgGradoPlanOrigenDestino od) {
        if (!od.getOrigen().getGrado().equals(od.getDestino().getGrado())) {

            List<SgGradoPlan> listaParBuscarEqui = new ArrayList();
            listaParBuscarEqui.add(od.getOrigen());

            Set<SgGradoPlan> elementosYaAnalizados = new HashSet<SgGradoPlan>();
            elementosYaAnalizados.add(od.getOrigen());

            while (!listaParBuscarEqui.isEmpty()) {

                FiltroReglaEquivalenciaDetalle filtroEqDet = new FiltroReglaEquivalenciaDetalle();
                filtroEqDet.setHabilitado(Boolean.TRUE);
                filtroEqDet.setReglaHabilitada(Boolean.TRUE);
                filtroEqDet.setOperacion(EnumOperacionReglaEquivalencia.ORIGEN);
                filtroEqDet.setGradoPlan(listaParBuscarEqui);

                filtroEqDet.setIncluirCampos(new String[]{"redReglaEquivalencia.reqPk", "redReglaEquivalencia.reqVersion"});
                List<SgReglaEquivalenciaDetalle> lreglasDetalle = reglaEquivalenciaDetalleBean.obtenerPorFiltro(filtroEqDet);

                listaParBuscarEqui = new ArrayList();
                if (!lreglasDetalle.isEmpty()) {
                    //Se consultan los destinos permitidos
                    filtroEqDet = new FiltroReglaEquivalenciaDetalle();
                    filtroEqDet.setHabilitado(Boolean.TRUE);
                    filtroEqDet.setReglaHabilitada(Boolean.TRUE);
                    filtroEqDet.setReglaPk(lreglasDetalle.stream().map(c -> c.getRedReglaEquivalencia().getReqPk()).collect(Collectors.toList()));
                    filtroEqDet.setOperacion(EnumOperacionReglaEquivalencia.DESTINO);
                    filtroEqDet.setIncluirCampos(new String[]{
                        "redPk", "redVersion",
                        "redGrado.graPk",
                        "redGrado.graVersion",
                        "redPlanEstudio.pesPk",
                        "redOpcion.opcPk",
                        "redOpcion.opcVersion",
                        "redProgramaEducativo.pedPk",
                        "redProgramaEducativo.pedVersion"});
                    List<SgReglaEquivalenciaDetalle> lreglasDetalleDestino = reglaEquivalenciaDetalleBean.obtenerPorFiltro(filtroEqDet);

                    if (!lreglasDetalleDestino.isEmpty()) {
                        Set<SgGradoPlan> gradosPlanCursadosEquivalentes = new HashSet<SgGradoPlan>();

                        for (SgReglaEquivalenciaDetalle rgp : lreglasDetalleDestino) {
                            SgGradoPlan gp = new SgGradoPlan();
                            gp.setGrado(rgp.getRedGrado().getGraPk());
                            gp.setPlanEstudio(rgp.getRedPlanEstudio().getPesPk());
                            gp.setOpcion(rgp.getRedOpcion() != null ? rgp.getRedOpcion().getOpcPk() : null);
                            gp.setProgramaEducativo(rgp.getRedProgramaEducativo() != null ? rgp.getRedProgramaEducativo().getPedPk() : null);
                            gradosPlanCursadosEquivalentes.add(gp);
                        }

                        //Buscar match
                        Iterator<SgGradoPlan> gpit = gradosPlanCursadosEquivalentes.iterator();

                        while (gpit.hasNext()) {

                            SgGradoPlan current = gpit.next();

                            if (BooleanUtils.isFalse(od.getValidarPlanEstudioDestino())) {

                                if (Objects.equals(current.getGrado(), od.getDestino().getGrado())
                                        && Objects.equals(current.getOpcion(), od.getDestino().getOpcion())
                                        && Objects.equals(current.getProgramaEducativo(), od.getDestino().getProgramaEducativo())) {
                                    return Boolean.TRUE;
                                }

                            } else {

                                if (Objects.equals(current.getGrado(), od.getDestino().getGrado())
                                        && Objects.equals(current.getPlanEstudio(), od.getDestino().getPlanEstudio())
                                        && Objects.equals(current.getOpcion(), od.getDestino().getOpcion())
                                        && Objects.equals(current.getProgramaEducativo(), od.getDestino().getProgramaEducativo())) {
                                    return Boolean.TRUE;
                                }

                            }
                        }

                        Set<SgGradoPlan> gradosSinAnalizar = new HashSet<SgGradoPlan>(gradosPlanCursadosEquivalentes);
                        gradosSinAnalizar.removeAll(elementosYaAnalizados);

                        if (gradosSinAnalizar.isEmpty()) {
                            return Boolean.FALSE;
                        } else {
                            elementosYaAnalizados.addAll(gradosSinAnalizar);
                            listaParBuscarEqui = new ArrayList(gradosSinAnalizar);
                        }

                    }
                } else {
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    //Busca matriculas anteriores de estudiante
    //Busca escolaridades equivalentes
    //Busca precedencia de grado de matrícula nueva
    //Si precedencia pertenece a grado de matriculas anteriores retorna true sino sigue
    //Busca reglas de equivalencia de grados de matriculas anteriores
    //En caso que se corresponda alguna de la precedencia con equivalencia devuelve true
    public Boolean gradoCumplePrecedenciaOEquivalenciaPrecedenciaParaEstudiante(Long gradoDestinoPk, Long estPk) {
        if (estPk == null || gradoDestinoPk == null) {
            return Boolean.FALSE;
        }

        //BUusca las matrículas del estudiante para tener GRADO-PLAN-OPCION-PROG_ED de cada una
        FiltroMatricula fmat = new FiltroMatricula();
        fmat.setMatEstudiantePk(estPk);
        fmat.setOrderBy(new String[]{"matFechaIngreso"});
        fmat.setAscending(new boolean[]{false});
        fmat.setMatPromocionGrado(EnumPromocionGradoMatricula.PROMOVIDO);
        fmat.setIncluirCampos(new String[]{"matSeccion.secServicioEducativo.sduGrado.graPk",
            "matSeccion.secPlanEstudio.pesPk",
            "matSeccion.secServicioEducativo.sduOpcion.opcPk",
            "matSeccion.secServicioEducativo.sduProgramaEducativo.pedPk"});
        fmat.setSecurityOperation(null);
        List<SgMatricula> mat = matriculaBean.obtenerPorFiltro(fmat);

        FiltroEscolaridadEstudiante fee = new FiltroEscolaridadEstudiante();
        fee.setIncluirCampos(new String[]{"escEqGrado.graPk",
            "escEqPlanEstudio.pesPk",
            "escEqOpcion.opcPk",
            "escEqProgramaEducativo.pedPk"});
        fee.setEscGeneradaPorEquivalencia(Boolean.TRUE);
        fee.setResultado(EnumPromovidoCalificacion.PROMOVIDO);
        fee.setEstudiantePk(estPk);
        List<SgEscolaridadEstudiante> escolaridades = escolaridadEstudianteBean.obtenerPorFiltro(fee);

        if (mat.isEmpty() && escolaridades.isEmpty()) {
            return Boolean.FALSE;
        }

        Set<SgGradoPlan> gradosPlanCursados = new HashSet<SgGradoPlan>();
        Set<SgGrado> gradosCursados = new HashSet<SgGrado>();

        for (SgMatricula m : mat) {
            if (m.getMatSeccion().getSecPlanEstudio() != null) {
                SgGradoPlan gp = new SgGradoPlan();
                gp.setGrado(m.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
                gp.setPlanEstudio(m.getMatSeccion().getSecPlanEstudio().getPesPk());
                gp.setOpcion(m.getMatSeccion().getSecServicioEducativo().getSduOpcion() != null ? m.getMatSeccion().getSecServicioEducativo().getSduOpcion().getOpcPk() : null);
                gp.setProgramaEducativo(m.getMatSeccion().getSecServicioEducativo().getSduProgramaEducativo() != null ? m.getMatSeccion().getSecServicioEducativo().getSduProgramaEducativo().getPedPk() : null);
                gradosPlanCursados.add(gp);
            }
            gradosCursados.add(m.getMatSeccion().getSecServicioEducativo().getSduGrado());
        }

        for (SgEscolaridadEstudiante esc : escolaridades) {
            if (esc.getEscEqPlanEstudio() != null) {
                SgGradoPlan gp = new SgGradoPlan();
                gp.setGrado(esc.getEscEqGrado().getGraPk());
                gp.setPlanEstudio(esc.getEscEqPlanEstudio().getPesPk());
                gp.setOpcion(esc.getEscEqOpcion() != null ? esc.getEscEqOpcion().getOpcPk() : null);
                gp.setProgramaEducativo(esc.getEscEqProgramaEducativo() != null ? esc.getEscEqProgramaEducativo().getPedPk() : null);
                gradosPlanCursados.add(gp);
            }
            gradosCursados.add(esc.getEscEqGrado());
        }

        //Busca precedencias de grado en que se está queriendo matricular
        //En caso de que no tenga se devuelve true
        FiltroRelGradoPrecedencia fgp = new FiltroRelGradoPrecedencia();
        fgp.setRgpGradoDestino(gradoDestinoPk);
        fgp.setIncluirCampos(new String[]{"rgpGradoOrigenFk.graPk"});

        Set<SgGrado> gradosPlanSeccionNuevaPrecedencia = new HashSet<SgGrado>();

        List<SgRelGradoPrecedencia> precedenciasDeGradoNuevo = relGradoPrecedenciaBean.obtenerPorFiltro(fgp);
        if (!precedenciasDeGradoNuevo.isEmpty()) {
            /*
            LOGGER.log(Level.SEVERE, "PRECEDENCIAS DE SECCION: ");
             for (SgRelGradoPrecedencia rgp : precedenciasDeGradoNuevo) {
                LOGGER.log(Level.SEVERE, "PREC: ");
                LOGGER.log(Level.SEVERE, "--GRADO PK: " + rgp.getRgpGradoOrigenFk().getGraPk());
                LOGGER.log(Level.SEVERE, "--PLAN PK: " + seccion.getSecPlanEstudio().getPesPk());
                LOGGER.log(Level.SEVERE, "--OPCION PK: " + (seccion.getSecServicioEducativo().getSduOpcion() != null ? seccion.getSecServicioEducativo().getSduOpcion().getOpcPk() : null));
                LOGGER.log(Level.SEVERE, "--PROGED PK: " + (seccion.getSecServicioEducativo().getSduProgramaEducativo() != null ? seccion.getSecServicioEducativo().getSduProgramaEducativo().getPedPk() : null));
            }*/

            for (SgRelGradoPrecedencia rgp : precedenciasDeGradoNuevo) {

                gradosPlanSeccionNuevaPrecedencia.add(rgp.getRgpGradoOrigenFk());
            }
        } else {
            return Boolean.TRUE;
        }
        
        
        gradosPlanSeccionNuevaPrecedencia.add(new SgGrado(gradoDestinoPk, 0)); //Un grado es siempre precedente de si mismo

        //Intersección entre precedencia de grado de matrícula nueva y grados de matriculas anteriores
        //Si la intersección es distinta de vacío devuelvo true
        Set<SgGrado> interseccionGrados = new HashSet<SgGrado>(gradosCursados);
        interseccionGrados.retainAll(gradosPlanSeccionNuevaPrecedencia);
        if (!interseccionGrados.isEmpty()) {
            return Boolean.TRUE;
        }

        //EN CASO DE QUE LAS PRECEDENCIAS DEL GRADO DE LA MATRÍCULA NUEVA NO SE CORRESPONDA CON LOS GRADOS DE
        //LAS MATRÍCULAS ANTERIORES, SE BUSCA LAS EQUIVALENCIAS DE LOS GRADOS DE LAS MATRÍCULAS
        List<SgGradoPlan> listaParBuscarEqui = new ArrayList(gradosPlanCursados);

        Set<SgGradoPlan> elementosYaAnalizados = new HashSet<SgGradoPlan>(gradosPlanCursados);

        while (!listaParBuscarEqui.isEmpty()) {

            FiltroReglaEquivalenciaDetalle filtroEqDet = new FiltroReglaEquivalenciaDetalle();
            filtroEqDet.setHabilitado(Boolean.TRUE);
            filtroEqDet.setReglaHabilitada(Boolean.TRUE);
            filtroEqDet.setOperacion(EnumOperacionReglaEquivalencia.ORIGEN);
            filtroEqDet.setGradoPlan(listaParBuscarEqui);

            filtroEqDet.setIncluirCampos(new String[]{"redReglaEquivalencia.reqPk", "redReglaEquivalencia.reqVersion"});
            List<SgReglaEquivalenciaDetalle> lreglasDetalle = reglaEquivalenciaDetalleBean.obtenerPorFiltro(filtroEqDet);

            listaParBuscarEqui = new ArrayList();
            if (!lreglasDetalle.isEmpty()) {
                //Se consultan los destinos permitidos
                filtroEqDet = new FiltroReglaEquivalenciaDetalle();
                filtroEqDet.setHabilitado(Boolean.TRUE);
                filtroEqDet.setReglaHabilitada(Boolean.TRUE);
                filtroEqDet.setReglaPk(lreglasDetalle.stream().map(c -> c.getRedReglaEquivalencia().getReqPk()).collect(Collectors.toList()));
                filtroEqDet.setOperacion(EnumOperacionReglaEquivalencia.DESTINO);
                filtroEqDet.setIncluirCampos(new String[]{
                    "redPk", "redVersion",
                    "redGrado.graPk",
                    "redGrado.graVersion",
                    "redPlanEstudio.pesPk",
                    "redOpcion.opcPk",
                    "redOpcion.opcVersion",
                    "redProgramaEducativo.pedPk",
                    "redProgramaEducativo.pedVersion"});
                List<SgReglaEquivalenciaDetalle> lreglasDetalleDestino = reglaEquivalenciaDetalleBean.obtenerPorFiltro(filtroEqDet);

                if (!lreglasDetalleDestino.isEmpty()) {
                    Set<SgGradoPlan> gradosPlanCursadosEquivalentes = new HashSet<SgGradoPlan>();
                    Set<SgGrado> gradosCursadosEquivalentes = new HashSet<SgGrado>();

                    for (SgReglaEquivalenciaDetalle rgp : lreglasDetalleDestino) {
                        SgGradoPlan gp = new SgGradoPlan();
                        gp.setGrado(rgp.getRedGrado().getGraPk());
                        gp.setPlanEstudio(rgp.getRedPlanEstudio().getPesPk());
                        gp.setOpcion(rgp.getRedOpcion() != null ? rgp.getRedOpcion().getOpcPk() : null);
                        gp.setProgramaEducativo(rgp.getRedProgramaEducativo() != null ? rgp.getRedProgramaEducativo().getPedPk() : null);
                        gradosPlanCursadosEquivalentes.add(gp);
                        gradosCursadosEquivalentes.add(rgp.getRedGrado());
                    }
                    /*
                    LOGGER.log(Level.SEVERE, "--EQUIVALENCIA--- ");
                    for (SgGradoPlan rgp : gradosPlanCursadosEquivalentes) {
                          LOGGER.log(Level.SEVERE, "eq: ");
                          LOGGER.log(Level.SEVERE, "--GRADO PK: " + rgp.getGrado());
                          LOGGER.log(Level.SEVERE, "--PLAN PK: " + rgp.getPlanEstudio());
                          LOGGER.log(Level.SEVERE, "--OPCION PK: " + (rgp.getOpcion()));
                          LOGGER.log(Level.SEVERE, "--PROGED PK: " + (rgp.getProgramaEducativo()));
                      }*/

                    interseccionGrados = new HashSet<SgGrado>(gradosCursadosEquivalentes);
                    interseccionGrados.retainAll(gradosPlanSeccionNuevaPrecedencia);
                    if (!interseccionGrados.isEmpty()) {
                        return Boolean.TRUE;
                    }

                    Set<SgGradoPlan> gradosSinAnalizar = new HashSet<SgGradoPlan>(gradosPlanCursadosEquivalentes);
                    gradosSinAnalizar.removeAll(elementosYaAnalizados);

                    if (gradosSinAnalizar.isEmpty()) {
                        return Boolean.FALSE;
                    } else {
                        elementosYaAnalizados.addAll(gradosSinAnalizar);
                        listaParBuscarEqui = new ArrayList(gradosSinAnalizar);
                        /*   LOGGER.log(Level.SEVERE, "--------------------- ");
                        LOGGER.log(Level.SEVERE, "--proxima iteracion ORIGEN--- ");
                    for (SgGradoPlan rgp : listaParBuscarEqui) {
                          LOGGER.log(Level.SEVERE, "eq: ");
                          LOGGER.log(Level.SEVERE, "--GRADO PK: " + rgp.getGrado());
                          LOGGER.log(Level.SEVERE, "--PLAN PK: " + rgp.getPlanEstudio());
                          LOGGER.log(Level.SEVERE, "--OPCION PK: " + (rgp.getOpcion()));
                          LOGGER.log(Level.SEVERE, "--PROGED PK: " + (rgp.getProgramaEducativo()));
                      }*/
                    }

                }

            }
        }

        return Boolean.FALSE;
    }
}
