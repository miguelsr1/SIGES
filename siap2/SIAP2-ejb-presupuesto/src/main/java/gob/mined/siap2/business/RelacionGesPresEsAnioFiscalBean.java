package gob.mined.siap2.business;

import gob.mined.siap2.business.ejbs.impl.TopePresupuestalBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.business.ejbs.impl.SgBeneficiariosBean;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.Beneficiarios;
import gob.mined.siap2.entities.enums.AccionTechos;
import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import gob.mined.siap2.entities.enums.TipoSubcomponente;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroRelPresAnioFiscal;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.RelacionGesPresEsAnioFiscalDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.jboss.ejb3.annotation.TransactionTimeout;

/**
 * Esta clase incluye los métodos para la gestión de SsGesPresEs
 *
 * @author Sofis Solutions
 */
@Stateless(name = "relacionGesPresEsAnioFiscalBean")
@LocalBean
public class RelacionGesPresEsAnioFiscalBean {

    private static final Logger logger = Logger.getLogger(RelacionGesPresEsAnioFiscalBean.class.getName());

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    @JPADAO
    private RelacionGesPresEsAnioFiscalDAO relacionGesPresEsAnioFiscalDAO;

    @Inject
    private TopePresupuestalBean topeBean;

  //  @Inject
   // private SedeBean sedeBean;

    @Inject
    private SgBeneficiariosBean beneficiariosBean;
    
    //@Inject
   // private RangoMatriculaBean rangoMatriculaBean;

    @Inject
    private ProcesamientoTechosAsincBean procesamientoTechosAsincBean;
    /**
     * Este método crea o actualiza una Relacion entre Gestion de presuspuesto
     * escolar y anio Fiscal.
     *
     * @param anioFiscal
     * @return 
     */
    public RelacionGesPresEsAnioFiscal crearActualizar(RelacionGesPresEsAnioFiscal anioFiscal) {
        try {
            BusinessException be = new BusinessException();
            if (anioFiscal.getMontoPorMatricula() == null) {
                be.addError(ConstantesErrores.ERR_MONTO_POR_MATRICULA_VACIO);
            }
            if (anioFiscal.getMontoPorMatriculaAprobacion() == null) {
                be.addError(ConstantesErrores.ERR_MONTO_POR_MATRICULA_VACIO);
            }
            if(anioFiscal.getComponentePresupuestoEscolar() != null && anioFiscal.getComponentePresupuestoEscolar().getTipo() != null 
                    && !anioFiscal.getComponentePresupuestoEscolar().getTipo().equals(TipoSubcomponente.CARGA_DESDE_ARCHIVO)) {
                if (anioFiscal.getFechaMatricula() == null) {
                    be.addError(ConstantesErrores.ERR_FECHA_MATRICULA_VACIA);
                }
            }
            if (StringUtils.isBlank(anioFiscal.getDescripcion())) {
                be.addError(ConstantesErrores.ERR_DESCRIPCION_VACIO);
            }

            if (!be.getErrores().isEmpty()) {
                throw be;
            }

            if (anioFiscal.getId() == null) {
               return (RelacionGesPresEsAnioFiscal) generalDAO.create(anioFiscal);
            } else {
               return (RelacionGesPresEsAnioFiscal) generalDAO.update(anioFiscal);
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Método que se encarga de eliminar una relacion entre Gestion de
     * presuspuesto escolar y anio Fiscal.
     *
     * @param id Identificador de registro a eliminar
     */
    public void eliminarRelacion(Integer id) {
        try {
            RelacionGesPresEsAnioFiscal reg = (RelacionGesPresEsAnioFiscal) generalDAO.find(RelacionGesPresEsAnioFiscal.class, id);
            if (reg != null) {
                generalDAO.delete(reg);
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public List<RelacionGesPresEsAnioFiscal> getComponentesPresupuestoEscolarByFiltro(FiltroRelPresAnioFiscal filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", filtro.getId());
                criterios.add(criterio);
            }

            if(filtro.getDescripcion() != null && StringUtils.isNotBlank(filtro.getDescripcion())) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "descripcionBusqueda", filtro.getDescripcion().toLowerCase());
                criterios.add(criterio);
            }
            if (filtro.getComponentePresupuestoEscolarId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "componentePresupuestoEscolar.id", filtro.getComponentePresupuestoEscolarId());
                criterios.add(criterio);
            }

            if (filtro.getCategoriaPresupuestoEscolarId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "componentePresupuestoEscolar.categoriaPresupuestoEscolar.id", filtro.getCategoriaPresupuestoEscolarId());
                criterios.add(criterio);
            }
            
            if (filtro.getAnioFiscalId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anioFiscal.id", filtro.getAnioFiscalId());
                criterios.add(criterio);
            }
            if (filtro.getSubCuentaId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "subCuenta.id", filtro.getSubCuentaId());
                criterios.add(criterio);
            }
            
            if (filtro.getTipo() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipo", filtro.getTipo());
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(filtro.getNombreComplemento())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "nombreComplemento", filtro.getNombreComplemento());
                criterios.add(criterio);
            }
            if (filtro.getIdFuenteFinanciamiento() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relFinanciamiento.fuenteFinanciamiento.id", filtro.getIdFuenteFinanciamiento());
                criterios.add(criterio);
            }
            if (filtro.getIdFuenteRecursos() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relFinanciamiento.fuenteRecursos.id", filtro.getIdFuenteRecursos());
                criterios.add(criterio);
            }
            

            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(RelacionGesPresEsAnioFiscal.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método devuelve una Relacion entre Gestion de presuspuesto escolar y
     * anio Fiscal.
     *
     * @param id
     * @return
     */
    public RelacionGesPresEsAnioFiscal getRelacionGesAnioById(Integer id) {
        return generalDAO.getEntityManager().find(RelacionGesPresEsAnioFiscal.class, id);
    }

    @TransactionTimeout(unit=TimeUnit.MINUTES, value=90)
    public void generarTechos(Integer relId, List<SsUsuario> usuarios) {
        try {
            RelacionGesPresEsAnioFiscal rel = generalDAO.getEntityManager().find(RelacionGesPresEsAnioFiscal.class, relId);
            
            BusinessException be = new BusinessException();
            if (rel.getFechaMatricula() == null) {
                be.addError(ConstantesErrores.ERR_FECHA_MATRICULA_VACIA);
            }
            if (rel.getComponentePresupuestoEscolar() == null) {
                be.addError(ConstantesErrores.ERR_COMPONENTE_VACIO);
            }
            if (rel.getSubCuenta() == null) {
                be.addError(ConstantesErrores.ERR_LINEA_PRESUPUESTARIA_VACIO);
            }
            if (rel.getAnioFiscal() == null) {
                be.addError(ConstantesErrores.ERR_ANIO_VACIO);
            }

            List<Beneficiarios> benef = beneficiariosBean.getBeneficiariosPorPresEsAnioFiscal(rel.getId());
            if (benef == null || benef.isEmpty()){
                be.addError(ConstantesErrores.ERR_BENEFICIARIOS_VACIO);
            }
            if (!be.getErrores().isEmpty()){
                throw be;
            }
            be = new BusinessException();
            if(rel.getProcesoEnCurso() == null || !rel.getProcesoEnCurso()) {
                procesamientoTechosAsincBean.modificarTechos(relId, AccionTechos.GENERAR,usuarios);
            } else {
                be.addError(ConstantesErrores.ERR_YA_EXISTE_PROCESO_PRESUPUESTO_EJECUTANDOSE, new String[]{rel.getDescripcion().trim()});
                throw be;
            }
        } catch (BusinessException be) {
            throw  be;
        } catch (DAOObjetoNoEditableException e) {
            throw  e;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public void actualizarEliminarTechos(Integer relId, Integer op) {
        if(op != null) {
            if(op.compareTo(0)==0) {
                topeBean.eliminarTopesPorRelGesPres(relId);
                //topeBean.actualizarRelGesPres(relId);
            } else if(op.compareTo(1)==0) {
                topeBean.actualizarTopesPorRelGesPres(relId);
                
            }
            topeBean.actualizarRelGesPres(relId);
        }
    }
    @TransactionTimeout(unit=TimeUnit.MINUTES, value=30)
    public void actualizarTechos(Integer relId, List<SsUsuario> usuarios) {
        try {
            RelacionGesPresEsAnioFiscal rel = generalDAO.getEntityManager().find(RelacionGesPresEsAnioFiscal.class, relId);

            BusinessException be = new BusinessException();
            if (rel.getFechaMatricula() == null) {
                be.addError(ConstantesErrores.ERR_FECHA_MATRICULA_VACIA);
            }
            if (rel.getComponentePresupuestoEscolar() == null) {
                be.addError(ConstantesErrores.ERR_COMPONENTE_VACIO);
            }
            if (rel.getSubCuenta() == null) {
                be.addError(ConstantesErrores.ERR_LINEA_PRESUPUESTARIA_VACIO);
            }
            if (rel.getAnioFiscal() == null) {
                be.addError(ConstantesErrores.ERR_ANIO_VACIO);
            }

            List<Beneficiarios> benef = beneficiariosBean.getBeneficiariosPorPresEsAnioFiscal(rel.getId());
            if (benef == null || benef.isEmpty()){
                be.addError(ConstantesErrores.ERR_BENEFICIARIOS_VACIO);
            }
            if (!be.getErrores().isEmpty()){
                throw be;
            }
            be = new BusinessException();
            if(rel.getProcesoEnCurso() == null || !rel.getProcesoEnCurso()) {
                procesamientoTechosAsincBean.modificarTechos(relId, AccionTechos.ACTUALIZAR, usuarios);
            } else {
                be.addError(ConstantesErrores.ERR_YA_EXISTE_PROCESO_PRESUPUESTO_EJECUTANDOSE, new String[]{rel.getDescripcion().trim()});
                throw be;
            }
        } catch (BusinessException be) {
            throw  be;
        } catch (DAOObjetoNoEditableException e) {
            throw  e;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
        
    }
    /**
    @Asynchronous
    @TransactionTimeout(unit=TimeUnit.MINUTES, value=15)
   // @Override
    private Future<Void> modificarTechos(Integer relId, AccionTechos accion) {

        try {

            RelacionGesPresEsAnioFiscal rel = generalDAO.getEntityManager().find(RelacionGesPresEsAnioFiscal.class, relId);
            
            BusinessException be = new BusinessException();
            if (rel.getFechaMatricula() == null) {
                be.addError(ConstantesErrores.ERR_FECHA_MATRICULA_VACIA);
                throw be;
            }
            if (rel.getComponentePresupuestoEscolar() == null) {
                be.addError(ConstantesErrores.ERROR_DATO_VACIO);
                throw be;
            }
            if (rel.getSubCuenta() == null) {
                be.addError(ConstantesErrores.ERROR_DATO_VACIO);
                throw be;
            }
            if (rel.getAnioFiscal() == null) {
                be.addError(ConstantesErrores.ERROR_DATO_VACIO);
                throw be;
            }

            rel.setMontoTotalAprobado(new BigDecimal(BigInteger.ZERO));
            rel.setCantidadMatriculasAprobadas(0);
            if (accion.equals(AccionTechos.ACTUALIZAR)) {
                topeBean.actualizarTopesPorRelGesPres(relId);
            } else {
                topeBean.eliminarTopesPorRelGesPres(relId);
                rel.setMontoTotalFormulado(new BigDecimal(BigInteger.ZERO));
                rel.setCantidadMatriculas(0);
            }
            
            FiltroSede fil = new FiltroSede();

            List<Long> clasificacionesId = new ArrayList<>();
            for (SgClasificacion c : rel.getClasificaciones()) {
                clasificacionesId.add(c.getClaPk());
            }

            List<TipoSede> tiposSede = new ArrayList<>();
            if (BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getCeduOficiales())) {
                tiposSede.add(TipoSede.CED_OFI);
            }
            if (BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getCeduPrivadosSub())
                    || BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getCeduPrivadosNoSub())) {
                tiposSede.add(TipoSede.CED_PRI);
            }
            if (BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getCirculosAlfabetizacion())) {
                tiposSede.add(TipoSede.CIR_ALF);
            }
            if (BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getCirculosFamilia())) {
                tiposSede.add(TipoSede.CIR_INF);
            }
            if (BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getSedesEducame())) {
                tiposSede.add(TipoSede.UBI_EDUC);
            }

            
                
            //TODO: ver como filtrar por subvencionada
            fil.setIncluirCampos(new String[]{"id", "version", "tipo","codigo"});
            fil.setClasificacionesId(clasificacionesId);
            fil.setOaeYMiembrosVigente(Boolean.TRUE);
            fil.setTipos(tiposSede);
            List<String> tiposOrganismos = new ArrayList();
            if(rel.getComponentePresupuestoEscolar() != null && rel.getComponentePresupuestoEscolar().getTiposOrganismoCurricular() != null) {
                for(SgTipoOrganismoAdministrativo tipo:rel.getComponentePresupuestoEscolar().getTiposOrganismoCurricular()) {
                    tiposOrganismos.add(tipo.getToaCodigo());
                }
            }
            fil.setTiposOrganismos(tiposOrganismos);
            
            List<SgSede> sedes = sedeBean.getSedesByFiltro(fil);

            if (sedes.isEmpty()) {
                return new AsyncResult<Void>(null);
            }

            List<Integer> sedesId = new ArrayList<>();
            for (SgSede sed : sedes) {
                sedesId.add(sed.getId());
            }

            //Consultas topes existentes
            FiltroTopePresupuestal filtro = new FiltroTopePresupuestal();
            filtro.setAnioFiscalId(rel.getAnioFiscal().getId());
            filtro.setComponentePresupuestoEscolarId(rel.getComponentePresupuestoEscolar().getId());
            filtro.setCuentaId(rel.getSubCuenta().getId());
            filtro.setSedesId(sedesId);
            List<TopePresupuestal> existentes = topeBean.getTopesPresupuestalesByFiltro(filtro);
            HashMap<Integer, TopePresupuestal> existentesPorSede = new HashMap<>();
            for (TopePresupuestal t : existentes) {
                existentesPorSede.put(t.getSede().getId(), t);
            }

            //Verificar montos a deducir
            HashMap<Integer, TopePresupuestal> existentesDeducirPorSede = new HashMap<>();
            if (rel.getDeducirComponentePresupuestoEscolar() != null) {
                filtro = new FiltroTopePresupuestal();
                filtro.setIncluirCampos(new String[]{"id", "monto", "montoAprobado", "sede.id","sede.codigo"});
                filtro.setAnioFiscalId(rel.getAnioFiscal().getId());
                filtro.setComponentePresupuestoEscolarId(rel.getDeducirComponentePresupuestoEscolar().getId());
                filtro.setCuentaId(rel.getSubCuenta().getId());
                filtro.setSedesId(sedesId);
                List<TopePresupuestal> existentesDeducir = topeBean.getTopesPresupuestalesByFiltro(filtro);
                for (TopePresupuestal t : existentesDeducir) {
                    existentesDeducirPorSede.put(t.getSede().getId(), t);
                }
            }

            List<Beneficiarios> benef = beneficiariosBean.getBeneficiariosPorPresEsAnioFiscal(rel.getId());

            //Obtener cantidad matriculas en todas las sedes del sistema
            HashMap<Integer, Long> cantidadMatriculasPorSede = new HashMap<>(); //Sedpk, cantidad
            List<Object[]> cantidades = sedeBean.getCantidadMatriculasPorSede(
                    rel.getFechaMatricula(),
                    sedesId,
                    benef,
                    rel.getComponentePresupuestoEscolar().getSedesAdscritas());
            for (Object[] ob : cantidades) {
                cantidadMatriculasPorSede.put(((Long) ob[0]).intValue(), (Long) ob[1]);
            }

            BigDecimal montoTotalAnio = BigDecimal.ZERO;

            Integer matriculasTotales = 0;
            for (SgSede sede : sedes) {

                BigDecimal monto = accion.equals(AccionTechos.ACTUALIZAR) ? rel.getMontoPorMatriculaAprobacion() : rel.getMontoPorMatricula();
                BigDecimal montoMinimo = accion.equals(AccionTechos.ACTUALIZAR) ? rel.getMontoMinimoAprobacion() : rel.getMontoMinimo();

                Long cantMatriculas = cantidadMatriculasPorSede.get(sede.getId());
                if (cantMatriculas == null) {
                    cantMatriculas = 0L;
                }
                
                matriculasTotales += cantMatriculas.intValue();
                if(cantMatriculas.compareTo(0L) > 0) {
                    //ASIGNACION DE MONTO POR "RANGO DE MATRICULA"
                    if (rel.getComponentePresupuestoEscolar().getParametro() != null && rel.getComponentePresupuestoEscolar().getParametro().equals(TipoParametro.RANGO_MATRICULA)) {
                        List<RangoMatricula> listaRangos = rangoMatriculaBean.getRangoMatriculaByRelacion(rel.getId());
                        if (listaRangos == null || listaRangos.isEmpty()) {
                            be.addError(ConstantesErrores.ERR_RANGOS_NO_ENCONTRADOS);
                            throw be;
                        }
                        BigDecimal rangoMayor;
                        BigDecimal rangoMenor = BigDecimal.ZERO;
                        for (RangoMatricula rango : listaRangos) {
                            rangoMayor = rango.getRango();
                            if (BigDecimal.valueOf(cantMatriculas).compareTo(rangoMenor) >= 0 && BigDecimal.valueOf(cantMatriculas).compareTo(rangoMayor) <= 0) {
                                monto = monto.multiply(new BigDecimal(rango.getValor()));
                                break;
                            }
                            rangoMenor = rango.getRango();
                        }
                    } else {
                        monto = monto.multiply(BigDecimal.valueOf(cantMatriculas));
                    }
                    
                    TopePresupuestal topeDeducir = existentesDeducirPorSede.get(sede.getId());
                    if (topeDeducir != null) {
                        BigDecimal montoDeducir = accion.equals(AccionTechos.ACTUALIZAR) ? topeDeducir.getMontoAprobado() : topeDeducir.getMonto();
                        if (montoDeducir != null) {
                            monto = monto.subtract(montoDeducir);
                        }
                    }

                    if (montoMinimo != null && monto.compareTo(montoMinimo) < 0) {
                        monto = montoMinimo;
                    }

                    montoTotalAnio = montoTotalAnio.add(monto);

                    if (!existentesPorSede.containsKey(sede.getId())) {
                        TopePresupuestal tope = new TopePresupuestal();
                        tope.setAnioFiscal(rel.getAnioFiscal());
                        tope.setGesPresEs(rel.getComponentePresupuestoEscolar());
                        tope.setSede(sede);
                        tope.setSubCuenta(rel.getSubCuenta());
                        
                        if (accion.equals(AccionTechos.ACTUALIZAR)) {
                            tope.setEstado(EstadoTopePresupuestal.APROBACION);
                            tope.setMontoAprobado(monto);
                            tope.setCantidadMatriculaAprobada(cantMatriculas.intValue());
                        } else {
                            tope.setEstado(EstadoTopePresupuestal.EN_PROCESO);
                            tope.setCantidadMatricula(cantMatriculas.intValue());
                            tope.setMonto(monto);
                        }
                        tope.setRelGesPres(rel);
                        topeBean.crearActualizarTopeMovimiento(tope);
                    } else {
                        TopePresupuestal existente = existentesPorSede.get(sede.getId());
                        if (accion.equals(AccionTechos.ACTUALIZAR)) {
                            existente.setMontoAprobado(monto);
                            existente.setEstado(EstadoTopePresupuestal.APROBACION);
                        } else {
                            existente.setMonto(monto);
                        }
                        existente.setRelGesPres(rel);
                        existente.setCantidadMatricula(cantMatriculas.intValue());
                        topeBean.crearActualizarTopeMovimiento(existente);
                    }
                } else {
                    logger.info("SEDE SIN MATRICULAS: Id: " + sede.getId());
                }
                

                
            }
            

            if (accion.equals(AccionTechos.ACTUALIZAR)) {
                rel.setMontoTotalAprobado(montoTotalAnio);
                rel.setCantidadMatriculasAprobadas(matriculasTotales);
            } else {
                rel.setMontoTotalFormulado(montoTotalAnio);
                rel.setCantidadMatriculas(matriculasTotales);
            }
            
            generalDAO.getEntityManager().merge(rel);

        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
        return new AsyncResult<Void>(null);
    }
    **/
    /**
     * Método utilizado para obtener los registros de Presupuesto con sus fuentes de financiamiento asociadas
     * 
     * @param idSubcomponente
     * @param idAnioFiscal
     * @param idLineaPresupuestaria
     * @return 
     */
    public List<RelacionGesPresEsAnioFiscal> getRelacionesAnioFiscalConFinanciamiento(Integer idSubcomponente, Integer idAnioFiscal, Integer idLineaPresupuestaria){
        try {
            return relacionGesPresEsAnioFiscalDAO.getRelacionesAnioFiscalConFinanciamiento(idSubcomponente, idAnioFiscal, idLineaPresupuestaria);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Método utilizado para obtener los registros de Presupuesto con sus fuentes de financiamiento asociadas
     * 
     * @param idSubcomponente
     * @param idAnioFiscal
     * @param idLineaPresupuestaria
     * @return 
     */
    public List<RelacionGesPresEsAnioFiscal> getRelacionesAnioFiscalPorSubcomponente(Integer idSubcomponente, TipoPresupuestoAnio tipo, Integer idAnioFiscal){
        try {
            return relacionGesPresEsAnioFiscalDAO.getRelacionesAnioFiscalPorSubcomponente(idSubcomponente, tipo, idAnioFiscal);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Método utilizado para obtener los registros de Presupuesto con sus fuentes de financiamiento asociadas
     * 
     * @param idSubcomponente
     * @param idAnioFiscal
     * @param idLineaPresupuestaria
     * @return 
     */
    public List<RelacionGesPresEsAnioFiscal> getRelacionesAnioFiscalPorSubcomponente(Integer idSubcomponente, Integer idAnioFiscal){
        try {
            return relacionGesPresEsAnioFiscalDAO.getRelacionesAnioFiscalPorSubcomponente(idSubcomponente, idAnioFiscal);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Método utilizado para obtener los registros de Presupuesto con sus fuentes de financiamiento asociadas
     * 
     * @param idSubcomponente
     * @param idAnioFiscal
     * @param idLineaPresupuestaria
     * @return 
     */
    public void actualizarProcesoPresupuesto(Integer idPresupuesto){
        try {
            generalDAO.getEntityManager().createQuery("update RelacionGesPresEsAnioFiscal rel set rel.procesoEnCurso=TRUE where rel.id = :id").setParameter("id", idPresupuesto).executeUpdate();
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
}
