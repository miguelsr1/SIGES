/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.filtros.FiltroEstudiante;
import sv.gob.mined.siges.filtros.FiltroSedes;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.CalificacionesHistoricasEstudianteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CalificacionesHistoricasEstudianteDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionesHistoricasEstudianteLite;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgSede;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class CalificacionesHistoricasEstudianteBean {

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SeguridadBean segBean;

    @Inject
    private ConfiguracionBean configuracionBean;

    @Inject
    private EstudianteBean estudianteBean;

    @Inject
    private SedeBean sedeBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCalificacionesHistoricasEstudiante
     * @throws GeneralException
     */
    public SgCalificacionesHistoricasEstudiante obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCalificacionesHistoricasEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionesHistoricasEstudiante.class);
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
                CodigueraDAO<SgCalificacionesHistoricasEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionesHistoricasEstudiante.class);
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
     * @param che SgCalificacionesHistoricasEstudiante
     * @return SgCalificacionesHistoricasEstudiante
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCalificacionesHistoricasEstudiante guardar(SgCalificacionesHistoricasEstudiante che) throws GeneralException {
        try {
            if (che != null) {
                if (CalificacionesHistoricasEstudianteValidacionNegocio.validar(che)) {
                    if (che.getChePk() != null) {
                        FiltroCalificacionesHistoricasEstudiante fche = new FiltroCalificacionesHistoricasEstudiante();
                        fche.setChePk(che.getChePk());
                        fche.setIncluirCampos(new String[]{"cheNF"});
                        List<SgCalificacionesHistoricasEstudiante> calList = this.obtenerPorFiltro(fche);

                        if (calList != null && !calList.isEmpty()) {
                            if (!che.getCheNF().equals(calList.get(0).getCheNF())) {
                                if (StringUtils.isBlank(che.getCheObservacionEdicion())) {
                                    BusinessException ge = new BusinessException();
                                    ge.addError("capNombreCapacitacion", Errores.ERROR_OBSERVACION_VACIO);
                                    throw ge;
                                }
                            }
                        }
                    }

                    CodigueraDAO<SgCalificacionesHistoricasEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionesHistoricasEstudiante.class);
                    return codDAO.guardar(che, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return che;
    }

    public void guardarCalificaciones(List<SgCalificacionesHistoricasEstudiante> calList) throws GeneralException {
        try {
            if (!calList.isEmpty()) {
                for (SgCalificacionesHistoricasEstudiante cal : calList) {
                    this.guardar(cal);
                }
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCalificacionesHistoricasEstudiante
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCalificacionesHistoricasEstudiante filtro) throws GeneralException {
        try {
            CalificacionesHistoricasEstudianteDAO dao = new CalificacionesHistoricasEstudianteDAO(em, segBean);
            return dao.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CALIFICACIONES_HISTORICAS_ESTUDIANTE);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCalificacionesHistoricasEstudiante
     * @return Lista de <SgCalificacionesHistoricasEstudiante>
     * @throws GeneralException
     */
    public List<SgCalificacionesHistoricasEstudiante> obtenerPorFiltro(FiltroCalificacionesHistoricasEstudiante filtro) throws GeneralException {
        try {
            CalificacionesHistoricasEstudianteDAO dao = new CalificacionesHistoricasEstudianteDAO(em, segBean);
            return dao.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CALIFICACIONES_HISTORICAS_ESTUDIANTE);
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
                CodigueraDAO<SgCalificacionesHistoricasEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionesHistoricasEstudiante.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Guarda el objeto indicado
     *
     * @param che SgCalificacionesHistoricasEstudiante
     * @return SgCalificacionesHistoricasEstudiante
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCalificacionesHistoricasEstudiante editarEstudianteCalificacion(SgCalificacionesHistoricasEstudiante che) throws GeneralException {
        try {
            if (che != null) {
                CodigueraDAO<SgCalificacionesHistoricasEstudianteLite> codDAO = new CodigueraDAO<>(em, SgCalificacionesHistoricasEstudianteLite.class);
                if (che.getNuevoEstudiante() != null) {
                    if (BooleanUtils.isTrue(che.getModificarCalificacionesEstudiante())) {
                        FiltroCalificacionesHistoricasEstudiante fce = new FiltroCalificacionesHistoricasEstudiante();
                        fce.setEstPk(che.getCheEstudianteFk().getEstPk());
                        fce.setIncluirCampos(new String[]{"chePk", "cheVersion"});
                        List<SgCalificacionesHistoricasEstudiante> calHistoricaEstList = this.obtenerPorFiltro(fce);

                        for (SgCalificacionesHistoricasEstudiante calEst : calHistoricaEstList) {
                            SgCalificacionesHistoricasEstudianteLite calLite = new SgCalificacionesHistoricasEstudianteLite();
                            calLite.setChePk(calEst.getChePk());
                            calLite.setCheVersion(calEst.getCheVersion());
                            calLite.setCheEstudianteFk(che.getNuevoEstudiante());
                            calLite.setCheEstudianteNie(che.getNuevoEstudiante().getEstPersona().getPerNie());
                            calLite.setChePersonaFk(che.getNuevoEstudiante().getEstPersona());
                            codDAO.guardar(calLite, null);
                        }
                    } else {
                        SgCalificacionesHistoricasEstudianteLite calLite = new SgCalificacionesHistoricasEstudianteLite();
                        calLite.setChePk(che.getChePk());
                        calLite.setCheVersion(che.getCheVersion());
                        calLite.setCheEstudianteFk(che.getNuevoEstudiante());
                        calLite.setCheEstudianteNie(che.getNuevoEstudiante().getEstPersona().getPerNie());
                        calLite.setChePersonaFk(che.getNuevoEstudiante().getEstPersona());
                        codDAO.guardar(calLite, null);
                    }
                } else {
                    BusinessException ge = new BusinessException();
                    ge.addError(Errores.ERROR_ESTUDIANTE_VACIO);
                    throw ge;
                }

                return che;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return che;
    }

    public void calificarConArchivo(SgArchivo arch) {
        InputStream stream = null;
        Workbook myWorkBook = null;
        try {
            BusinessException ge = new BusinessException();
            ge.addError("estNie", "Se encontraron los siguientes errores en el archivo:");

            String path = tmpBasePath;

            if (arch != null) {
                stream = new BufferedInputStream(new FileInputStream(path + arch.getAchTmpPath()));

                myWorkBook = WorkbookFactory.create(stream);
                Sheet mySheet = myWorkBook.getSheetAt(0);

                //VALIDAR CANT FILAS               
                Integer filasCantMax = 100;

                Iterator<Row> rowIterator1 = mySheet.iterator();
                Integer cantRows = 0;
                while (rowIterator1.hasNext()) {
                    Row row = rowIterator1.next();
                    if (row.getCell(0, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(1, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(2, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(3, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(4, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(5, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(6, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(7, Row.RETURN_BLANK_AS_NULL) == null) {
                        continue;
                    }
                    cantRows++;
                }
                if (cantRows <= 1) {
                    ge.addError("filas", "El archivo está vacío.");
                    throw ge;
                }

                if (cantRows > filasCantMax) {
                    ge.addError("filas", "La cantidad de filas del archivo no puede ser mayor a " + filasCantMax + ".");
                    throw ge;
                }

                Iterator<Row> rowIterator = mySheet.iterator();

                HashMap<Long, SgEstudiante> estudianteHash = new HashMap<>();
                HashMap<String, SgSede> sedeHash = new HashMap<>();
                Boolean existeAlMenosUnError = Boolean.FALSE;
                List<SgCalificacionesHistoricasEstudiante> calificacionList = new ArrayList();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Integer fila = row.getRowNum() + 1;
                    Cell cell;

                    Long nie = null;
                    Integer anio = null;
                    String codigoSede = null;
                    String nombreSede = null;
                    String planEstudio = null;
                    String servicioEducativo = null;
                    String componente = null;
                    String notaFinal = null;

                    Boolean existeError = Boolean.FALSE;
                    if (row.getRowNum() > 0) {
                        //Celda 0- NIE
                        cell = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            if (row.getCell(1, Row.RETURN_BLANK_AS_NULL) == null
                                    && row.getCell(2, Row.RETURN_BLANK_AS_NULL) == null
                                    && row.getCell(3, Row.RETURN_BLANK_AS_NULL) == null
                                    && row.getCell(4, Row.RETURN_BLANK_AS_NULL) == null
                                    && row.getCell(5, Row.RETURN_BLANK_AS_NULL) == null
                                    && row.getCell(6, Row.RETURN_BLANK_AS_NULL) == null
                                    && row.getCell(7, Row.RETURN_BLANK_AS_NULL) == null) {
                                continue;
                            }
                            ge.addError("estNie", "Fila " + fila + ": NIE es vacío.");
                            existeError = Boolean.TRUE;
                        } else {
                            if (!CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
                                ge.addError("estNie", "fila " + fila + ": NIE incorrecto.");
                                existeError = Boolean.TRUE;
                            } else {
                                nie = Math.round(cell.getNumericCellValue());
                            }
                        }
                        //Celda 1- Año
                        cell = row.getCell(1, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            ge.addError("estNie", "Fila " + fila + ": Año es vacío.");
                            existeError = Boolean.TRUE;
                        } else {
                            if (!CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
                                ge.addError("estNie", "Fila " + fila + ": Año es incorrecto.");
                                existeError = Boolean.TRUE;
                            } else {
                                String anioSt = String.valueOf(Math.round(cell.getNumericCellValue()));
                                anio = Integer.parseInt(anioSt);
                                if (anio < 2008 || anio > 2019) {
                                    ge.addError("estNie", "Fila " + fila + ": Año debe estar entre 2008 y 2019.");
                                    existeError = Boolean.TRUE;
                                }
                            }
                        }

                        //Celda 2-Codigo sede
                        cell = row.getCell(2, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            ge.addError("estNie", "Fila " + fila + ": Código de sede es vacío");
                            existeError = Boolean.TRUE;
                        } else {
                            if (!CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
                                codigoSede = cell.toString();
                            } else {
                                codigoSede = ((Long) Math.round(cell.getNumericCellValue())).toString();
                            }

                        }

                        //Celda 3-Nombre de la sede
                        cell = row.getCell(3, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            ge.addError("estNie", "Fila " + fila + ": Nombre de sede es vacío.");
                            existeError = Boolean.TRUE;
                        } else {
                            nombreSede = cell.toString();
                        }

                        //Celda 4- Plan de estudio
                        //No es obligatorio
                        cell = row.getCell(4, Row.RETURN_BLANK_AS_NULL);
                        if (cell != null) {
                            planEstudio = cell.toString();
                        }

                        //Celda 5-Servicio educativo
                        cell = row.getCell(5, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            ge.addError("estNie", "Fila " + fila + ": Servicio educativo vacío.");
                            existeError = Boolean.TRUE;
                        } else {
                            servicioEducativo = cell.toString();
                        }

                        //Celda 6-Componente
                        cell = row.getCell(6, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            ge.addError("estNie", "Fila " + fila + ": Componente vacío.");
                            existeError = Boolean.TRUE;
                        } else {
                            componente = cell.toString();
                        }

                        //Celda 7-Nota final
                        cell = row.getCell(7, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            ge.addError("estNie", "Fila " + fila + ":La nota final es vacío.");
                            existeError = Boolean.TRUE;
                        } else {
                            if (!CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
                                ge.addError("estNie", "Fila " + fila + ": Nota final tiene formato incorrecto.");
                                existeError = Boolean.TRUE;
                            } else {
                                notaFinal = String.valueOf(Math.round(cell.getNumericCellValue()));

                            }
                        }

                        if (BooleanUtils.isTrue(existeError)) {
                            existeAlMenosUnError = Boolean.TRUE;
                            continue;
                        } else {
                            SgCalificacionesHistoricasEstudiante calEst = new SgCalificacionesHistoricasEstudiante();
                            if (estudianteHash.containsKey(nie)) {
                                calEst.setCheEstudianteFk(estudianteHash.get(nie));
                            } else {
                                FiltroEstudiante fe = new FiltroEstudiante();
                                fe.setNie(nie);
                                fe.setIncluirCampos(new String[]{"estPk", "estVersion", "estPersona.perPk", "estPersona.perVersion", "estPersona.perNie"});
                                fe.setMaxResults(1L);
                                List<SgEstudiante> listEstudiante = estudianteBean.obtenerPorFiltro(fe);

                                if (listEstudiante.isEmpty()) {
                                    ge.addError("estNie", "Fila " + fila + ":Estudiante no existe.");
                                    existeError = Boolean.TRUE;
                                    existeAlMenosUnError = Boolean.TRUE;
                                    continue;
                                } else {
                                    SgEstudiante est = listEstudiante.get(0);
                                    estudianteHash.put(nie, est);
                                    calEst.setCheEstudianteFk(est);
                                }
                            }

                            if (sedeHash.containsKey(codigoSede)) {
                                calEst.setCheSedeFk(sedeHash.get(codigoSede));
                            } else {
                                FiltroSedes fs = new FiltroSedes();
                                fs.setSedCodigo(codigoSede);
                                fs.setMaxResults(1L);
                                fs.setIncluirCampos(new String[]{"sedPk", "sedVersion", "sedTipo"});
                                List<SgSede> listSede = sedeBean.obtenerPorFiltro(fs);

                                if (listSede.isEmpty()) {
                                    calEst.setCheSedeExternaCodigo(codigoSede);
                                    calEst.setCheSedeExternaNombre(nombreSede);
                                } else {
                                    SgSede sed = listSede.get(0);
                                    calEst.setCheSedeFk(sed);
                                    sedeHash.put(codigoSede, sed);
                                }
                            }

                            calEst.setCheAnioMatricula(anio);
                            calEst.setChePlanEstudioExternoDescripcion(planEstudio);
                            calEst.setCheServicioEducativoExternoEtiquetaImpresion(servicioEducativo);
                            calEst.setCheComponente(componente);
                            calEst.setCheNF(notaFinal);
                            calEst.setCheEsImportado(Boolean.TRUE);

                            CodigueraDAO<SgCalificacionesHistoricasEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionesHistoricasEstudiante.class);
                            codDAO.guardar(calEst, null);

                            repetidos:
                            for (SgCalificacionesHistoricasEstudiante calHist : calificacionList) {
                                if (calHist.getCheEstudianteFk().equals(calEst.getCheEstudianteFk())
                                        && calHist.getCheAnioMatricula().equals(calEst.getCheAnioMatricula())
                                        && calHist.getChePlanEstudioExternoDescripcion()!=null? 
                                        calHist.getChePlanEstudioExternoDescripcion().equals(calEst.getChePlanEstudioExternoDescripcion())
                                        && calHist.getCheServicioEducativoExternoEtiquetaImpresion().equals(calEst.getCheServicioEducativoExternoEtiquetaImpresion()):true
                                        && calHist.getCheComponente().equals(calEst.getCheComponente())
                                        && (calHist.getCheSedeFk() != null ? calHist.getCheSedeFk().equals(calEst.getCheSedeFk())
                                        : calHist.getCheSedeExternaCodigo().equals(calEst.getCheSedeExternaCodigo()))) {
                                    ge.addError("estNie", "Fila " + fila + ": Se repite registro.");
                                    existeError = Boolean.TRUE;
                                    existeAlMenosUnError = Boolean.TRUE;
                                    break repetidos;
                                }
                            }
                            calificacionList.add(calEst);

                        }
                    }

                }

                if (BooleanUtils.isTrue(existeAlMenosUnError)) {
                    throw ge;
                }
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
