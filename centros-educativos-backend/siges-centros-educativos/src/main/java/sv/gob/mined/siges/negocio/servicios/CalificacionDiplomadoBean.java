/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgCalificacionDiplomadoAux;
import sv.gob.mined.siges.enumerados.EnumCalculoNotaInstitucional;
import sv.gob.mined.siges.enumerados.EnumTiposCalificacionDiplomado;
import sv.gob.mined.siges.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalificacionDiplomado;
import sv.gob.mined.siges.filtros.FiltroCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.filtros.FiltroDiplomadosEstudiante;
import sv.gob.mined.siges.filtros.FiltroModuloDiplomado;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.CalificacionDiplomadoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CalificacionDiplomadoDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAnioLectivo;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionDiplomado;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomadosEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgModuloDiplomado;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCalificacion;
import sv.gob.mined.siges.utils.MathFunctionsUtils;
import sv.gob.mined.siges.utils.NumberFormatUtils;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class CalificacionDiplomadoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SeguridadBean seguriadBean;

    @Inject
    private ModuloDiplomaBean moduloDiplomadoBean;
    
    @Inject
    private CalificacionDiplomadoEstudianteBean calificacionDiplomadoEstudianteBean;
    
    @Inject
    private DiplomadosEstudianteBean diplomadoEstudianteBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCalificacionDiplomado
     * @throws GeneralException
     */
    public SgCalificacionDiplomado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCalificacionDiplomado> codDAO = new CodigueraDAO<>(em, SgCalificacionDiplomado.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_CALIFICACION_DIPLOMADO);
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
                CodigueraDAO<SgCalificacionDiplomado> codDAO = new CodigueraDAO<>(em, SgCalificacionDiplomado.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CALIFICACION_DIPLOMADO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param cad SgCalificacionDiplomado
     * @return SgCalificacionDiplomado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCalificacionDiplomado guardar(SgCalificacionDiplomado cad) throws GeneralException {
        try {
            if (cad != null) {
                if (CalificacionDiplomadoValidacionNegocio.validar(cad)) {
                    CodigueraDAO<SgCalificacionDiplomado> codDAO = new CodigueraDAO<>(em, SgCalificacionDiplomado.class);

                    //Si pk == null y no tiene dato, no se guarda
                    //Si pk != null y no tiene dato, se borra por orphanRemoval
                    if (cad.getCadCalificacionDiplomadoEstudiantes() != null) {
                        if (cad.getCadModuloDiplomado().getMdiEscalaCalificacion().getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA)) {
                            cad.getCadCalificacionDiplomadoEstudiantes().forEach(c -> c.setCdeCalificacionConceptualFk(null));
                        } else if (cad.getCadModuloDiplomado().getMdiEscalaCalificacion().getEcaTipoEscala().equals(TipoEscalaCalificacion.CONCEPTUAL)) {
                            cad.getCadCalificacionDiplomadoEstudiantes().forEach(c -> c.setCdeCalificacionNumerica(null));
                        }
                        cad.getCadCalificacionDiplomadoEstudiantes()
                                .removeIf(c -> StringUtils.isBlank(c.getCdeCalificacionNumerica()) && c.getCdeCalificacionConceptualFk() == null);
                    }

                    return codDAO.guardar(cad, null);

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cad;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCalificacionDiplomado filtro) throws GeneralException {
        try {
            CalificacionDiplomadoDAO codDAO = new CalificacionDiplomadoDAO(em, seguriadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CALIFICACION_DIPLOMADO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCalificacionDiplomado>
     * @throws GeneralException
     */
    public List<SgCalificacionDiplomado> obtenerPorFiltro(FiltroCalificacionDiplomado filtro) throws GeneralException {
        try {
            CalificacionDiplomadoDAO codDAO = new CalificacionDiplomadoDAO(em, seguriadBean);
            List<SgCalificacionDiplomado> calDip = codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CALIFICACION_DIPLOMADO);
            if (BooleanUtils.isTrue(filtro.getIncluirCalificacionEstudiante())) {
                for (SgCalificacionDiplomado calDi : calDip) {
                    if (calDi.getCadCalificacionDiplomadoEstudiantes() != null) {
                        calDi.getCadCalificacionDiplomadoEstudiantes().size();
                    }
                }
            }
            return calDip;
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
                CodigueraDAO<SgCalificacionDiplomado> codDAO = new CodigueraDAO<>(em, SgCalificacionDiplomado.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    public Boolean calcularNotaIntitucionalDiplomados(SgCalificacionDiplomadoAux calAux) {
        try {
            BusinessException ge = new BusinessException();
            if (calAux.getAnioLectivoFk() != null && calAux.getModuloDiplomadoFk() != null && calAux.getSedeFk() != null && calAux.getDiplomadoFk()!=null) {
                FiltroModuloDiplomado fmd = new FiltroModuloDiplomado();
                fmd.setMdiPk(calAux.getModuloDiplomadoFk());
                fmd.setIncluirCampos(new String[]{
                    "mdiVersion",
                    "mdiEscalaCalificacion.ecaTipoEscala",
                    "mdiEscalaCalificacion.ecaMinimo",
                    "mdiEscalaCalificacion.ecaMaximo",
                    "mdiEscalaCalificacion.ecaMinimoAprobacion",
                    "mdiEscalaCalificacion.ecaPrecision",
                    "mdiCalculoNotaInstitucional",
                    "mdiFuncionRedondeo",
                    "mdiPrecision",
                    "mdiPeriodosCalificacion"
                });
                List<SgModuloDiplomado> moduloDiplomadoList= moduloDiplomadoBean.obtenerPorFiltro(fmd);
                
                if(!moduloDiplomadoList.isEmpty()){
                    SgModuloDiplomado moduloDiplomado=moduloDiplomadoList.get(0);
                    
                   if(moduloDiplomado.getMdiEscalaCalificacion()==null){
                        ge.addError("mdiCodigo", Errores.ERROR_MD_ESCALA_CALIFICACION_VACIO);
                    }
                    if(moduloDiplomado.getMdiCalculoNotaInstitucional()==null){
                        ge.addError("mdiCodigo", Errores.ERROR_MD_CALCULO_NOTA_INSTITUCIONAL_VACIO);
                    }
                    else{
                        if(EnumCalculoNotaInstitucional.PROM.equals(moduloDiplomado.getMdiCalculoNotaInstitucional())){
                            if(moduloDiplomado.getMdiFuncionRedondeo()==null){
                                ge.addError("mdiCodigo", Errores.ERROR_MD_FUNCION_REDONDEO_VACIO);
                            }
                        }
                    }
                    if (ge.getErrores().size() > 0) {
                        throw ge;
                    }
                    
                    //Busco calificaciones de estudiantes en periodo ordinario y notin
                    FiltroCalificacionDiplomadoEstudiante fcde=new FiltroCalificacionDiplomadoEstudiante();
                    fcde.setAnioLectivoFk(calAux.getAnioLectivoFk());
                    fcde.setModuloDiplomadoFk(moduloDiplomado.getMdiPk());
                    fcde.setSedeFk(calAux.getSedeFk());
                    List<EnumTiposCalificacionDiplomado> tipoCalificacion=new ArrayList();
                    tipoCalificacion.add(EnumTiposCalificacionDiplomado.ORD);
                    tipoCalificacion.add(EnumTiposCalificacionDiplomado.NOTIN);
                    fcde.setTipoCalificacionDiplomadoList(tipoCalificacion);
                    List<SgCalificacionDiplomadoEstudiante> calificacionEstudianteList=calificacionDiplomadoEstudianteBean.obtenerPorFiltro(fcde);
                    
                    
                    if(!calificacionEstudianteList.isEmpty()){
                        FiltroDiplomadosEstudiante fde = new FiltroDiplomadosEstudiante();
                        fde.setAnioLectivoId(calAux.getAnioLectivoFk());
                        fde.setSedePk(calAux.getSedeFk());
                        fde.setDiplomadoId(calAux.getDiplomadoFk());
                        fde.setIncluirCampos(new String[]{"depEstudiante.estVersion",
                            "depEstudiante.estPk",
                            "depEstudiante.estPersona.perVersion",
                            "depEstudiante.estPersona.perPrimerNombre",
                            "depEstudiante.estPersona.perSegundoNombre",
                            "depEstudiante.estPersona.perPrimerApellido",
                            "depEstudiante.estPersona.perSegundoApellido",
                            "depEstudiante.estPersona.perNie",
                            "depEstudiante.estPersona.perPk",});
                        List<SgDiplomadosEstudiante> dipEst = diplomadoEstudianteBean.obtenerPorFiltro(fde);
                        
                        if(!dipEst.isEmpty()){
                           List<SgEstudiante> listEstudiante=  dipEst.stream().map(c -> c.getDepEstudiante()).collect(Collectors.toList());
                           
                           List<SgCalificacionDiplomadoEstudiante> calificacionesORD=calificacionEstudianteList.stream().filter(c->EnumTiposCalificacionDiplomado.ORD.equals(c.getCdeCalificacionDiplomadoFk().getCadTipoCalificacion())).collect(Collectors.toList());
                           List<SgCalificacionDiplomadoEstudiante> calificacionesNOTIN=calificacionEstudianteList.stream().filter(c->EnumTiposCalificacionDiplomado.NOTIN.equals(c.getCdeCalificacionDiplomadoFk().getCadTipoCalificacion())).collect(Collectors.toList());
                           
                           for(SgEstudiante est:listEstudiante){
                               List<SgCalificacionDiplomadoEstudiante> calificacionesORDdeEst=calificacionesORD.stream().filter(c->c.getCdeEstudianteFk().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());
                               
                               SgCalificacionDiplomadoEstudiante calificacionEst = new SgCalificacionDiplomadoEstudiante();
                                List<SgCalificacionDiplomadoEstudiante> calEstList = calificacionesNOTIN.stream().filter(c ->c.getCdeEstudianteFk().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());
                                if (!calEstList.isEmpty()) {
                                    calificacionEst = calEstList.get(0);
                                }
                                Object resultadoObjeto = calcularNotaInstitucionalPorEstudiante(moduloDiplomado, calificacionesORDdeEst);
                                if (resultadoObjeto != null) {
                                    if (resultadoObjeto instanceof String) {
                                        calificacionEst.setCdeCalificacionNumerica((String) resultadoObjeto);
                                    }
                                    if (resultadoObjeto instanceof SgCalificacion) {
                                        calificacionEst.setCdeCalificacionConceptualFk((SgCalificacion) resultadoObjeto);
                                    }
                                    calificacionEst.setCdeFechaRealizado(LocalDate.now());
                                    calificacionEst.setCdeEstudianteFk(est);

                                    if (calificacionEst.getCdePk()== null) {
                                        calificacionesNOTIN.add(calificacionEst);
                                    }
                                }
                           }
                           
                           
                           if(!calificacionesNOTIN.isEmpty()){
                               FiltroCalificacionDiplomado fcd=new FiltroCalificacionDiplomado();
                               fcd.setCalSedeFk(calAux.getSedeFk());
                               fcd.setCalAnioLectivoFk(calAux.getAnioLectivoFk());
                               fcd.setCalModuloDiplomadoFk(moduloDiplomado.getMdiPk());
                               fcd.setCadTipoCalificacion(EnumTiposCalificacionDiplomado.NOTIN);
                               fcd.setIncluirCampos(new String[]{
                                   "cadModuloDiplomado.mdiPk",
                                    "cadModuloDiplomado.mdiVersion",
                                    "cadModuloDiplomado.mdiEscalaCalificacion.ecaTipoEscala",
                                    "cadSedeFk.sedPk",
                                    "cadSedeFk.sedTipo",
                                    "cadSedeFk.sedVersion",
                                    "cadAnioLectivoFk.alePk",
                                    "cadAnioLectivoFk.aleVersion",
                                    "cadNumeroPeriodo",
                                    "cadTipoCalificacion",
                                    "cadVersion"
                               });
                               List<SgCalificacionDiplomado> listCalDipNotin=this.obtenerPorFiltro(fcd);
                  
                               SgCalificacionDiplomado calDiplomadoNotin=new SgCalificacionDiplomado();
                               if(!listCalDipNotin.isEmpty()){
                                   calDiplomadoNotin=listCalDipNotin.get(0);
                               }else{
                                   calDiplomadoNotin.setCadAnioLectivoFk(new SgAnioLectivo(calAux.getAnioLectivoFk(),0));
                                   calDiplomadoNotin.setCadSedeFk(new SgSede(calAux.getSedeFk(),0));
                                   calDiplomadoNotin.setCadTipoCalificacion(EnumTiposCalificacionDiplomado.NOTIN);
                                   calDiplomadoNotin.setCadModuloDiplomado(moduloDiplomado);
                               }
                               
                               for(SgCalificacionDiplomadoEstudiante calEs:calificacionesNOTIN){
                                   calEs.setCdeCalificacionDiplomadoFk(calDiplomadoNotin);
                               }
                               calDiplomadoNotin.setCadCalificacionDiplomadoEstudiantes(calificacionesNOTIN);
                               
                               this.guardar(calDiplomadoNotin);
                              return Boolean.TRUE;  
                            }
                        }
                        
                        
                    }
                }
            }else{
                if(calAux.getAnioLectivoFk() == null ){
                    ge.addError("calSeccion", Errores.ERROR_ANIO_LECTIVO_VACIO);
                }
                if(calAux.getModuloDiplomadoFk() == null ){
                    ge.addError("calSeccion", Errores.ERROR_MODULO_DIPLOMADO_VACIO);
                }
                if(calAux.getSedeFk() == null ){
                    ge.addError("calSeccion", Errores.ERROR_SEDE_VACIO);
                }
                if(calAux.getDiplomadoFk() == null ){
                    ge.addError("calSeccion", Errores.ERROR_DIPLOMADO_VACIO);
                }
            }
            if (ge.getErrores().size() > 0) {
                throw ge;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return Boolean.FALSE; 
    }
    
     public Object calcularNotaInstitucionalPorEstudiante(SgModuloDiplomado moduloDiplomado, List<SgCalificacionDiplomadoEstudiante> calificaciones) {
        List<BigDecimal> calificacionesNumericas = new ArrayList();
        List<Double> calificacionesNumericasDouble = new ArrayList();
        List<SgCalificacion> calificacionesConceptuales = new ArrayList();
        if (moduloDiplomado != null) {
            if (moduloDiplomado.getMdiEscalaCalificacion()!= null) {

                Integer ultimaPeriodo = null;
                String ultimaNotaNumerica = null;
                SgCalificacion ultimaNotaConceptual = null;

                for (SgCalificacionDiplomadoEstudiante calEst : calificaciones) {
                    if (calEst.getCdeCalificacionDiplomadoFk().getCadNumeroPeriodo() != null ) {

                        if (calEst.getCdeCalificacionNumerica()!= null) {
                            calificacionesNumericas.add(new BigDecimal(calEst.getCdeCalificacionNumerica()));
                            calificacionesNumericasDouble.add(Double.valueOf(calEst.getCdeCalificacionNumerica()));
                        } else {
                            if (calEst.getCdeCalificacionConceptualFk()!= null) {
                                calificacionesConceptuales.add(calEst.getCdeCalificacionConceptualFk());
                            }
                        }
                        if (ultimaPeriodo == null) {
                            ultimaPeriodo = calEst.getCdeCalificacionDiplomadoFk().getCadNumeroPeriodo();
                            ultimaNotaNumerica = calEst.getCdeCalificacionNumerica();
                            ultimaNotaConceptual = calEst.getCdeCalificacionConceptualFk();
                        } else {

                            if (ultimaPeriodo< calEst.getCdeCalificacionDiplomadoFk().getCadNumeroPeriodo()) {
                                ultimaPeriodo = calEst.getCdeCalificacionDiplomadoFk().getCadNumeroPeriodo();
                                ultimaNotaNumerica = calEst.getCdeCalificacionNumerica();
                                ultimaNotaConceptual = calEst.getCdeCalificacionConceptualFk();
                            }
                        }
                    }
                }
                if (calificacionesNumericas.size() > 0) {
                    if (TipoEscalaCalificacion.NUMERICA.equals(moduloDiplomado.getMdiEscalaCalificacion().getEcaTipoEscala())) {
                        Integer precision = null;
                        if (moduloDiplomado.getMdiPrecision()!= null) {
                            precision = moduloDiplomado.getMdiPrecision();
                        } else {
                            precision = moduloDiplomado.getMdiEscalaCalificacion().getEcaPrecision();
                        }
                        Double resultado = null;
                        switch (moduloDiplomado.getMdiCalculoNotaInstitucional()) {
                            case PROM:
                                //  resultado = MathFunctionsUtils.promedio(calificacionesNumericas);
                                switch (moduloDiplomado.getMdiFuncionRedondeo()) {
                                    case RED:
                                        resultado = MathFunctionsUtils.promedio(calificacionesNumericas, precision != null ? precision : 0, RoundingMode.HALF_UP);
                                        break;
                                    case PIS:
                                        resultado = MathFunctionsUtils.promedio(calificacionesNumericas, precision != null ? precision : 0, RoundingMode.FLOOR);
                                        break;
                                    case TEC:
                                        resultado = MathFunctionsUtils.promedio(calificacionesNumericas, precision != null ? precision : 0, RoundingMode.CEILING);
                                        break;
                                    default:// nada
                                }
                                break;
                            case MAY:
                                resultado = MathFunctionsUtils.mayor(calificacionesNumericasDouble);
                                break;
                            case ULT:
                                resultado = Double.parseDouble(ultimaNotaNumerica);
                                break;
                            case MED:
                                resultado = MathFunctionsUtils.mediana(calificacionesNumericasDouble);
                                break;
                            default:

                        }
                        if (resultado != null) {
                            return NumberFormatUtils.formatDouble(resultado, precision);
                        }

                        return resultado != null ? resultado.toString() : null;
                    }
                }
                if (TipoEscalaCalificacion.CONCEPTUAL.equals(moduloDiplomado.getMdiEscalaCalificacion().getEcaTipoEscala())) {
                    SgCalificacion resultadoConceptual = null;
                    switch (moduloDiplomado.getMdiCalculoNotaInstitucional()) {
                        case MAY:
                            for (SgCalificacion calConceptual : calificacionesConceptuales) {
                                if (resultadoConceptual == null) {
                                    resultadoConceptual = calConceptual;
                                } else {
                                    if (calConceptual.getCalOrden() != null) {
                                        if (resultadoConceptual.getCalOrden() < calConceptual.getCalOrden()) {
                                            resultadoConceptual = calConceptual;
                                        }
                                    }
                                }
                            }
                            break;
                        case ULT:
                            resultadoConceptual = ultimaNotaConceptual;
                            break;
                        default:

                    }

                    return resultadoConceptual != null ? resultadoConceptual : null;
                }

            }
        }
        return null;
    }

}
