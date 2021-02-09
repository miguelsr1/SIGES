/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.EstGenerica;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCargaExterna;
import sv.gob.mined.siges.filtros.FiltroEstadisticas;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.CargaExternaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CargaExternaDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EstadisticasExternasDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCargaExterna;
import sv.gob.mined.siges.persistencia.entidades.SgTuplaCargaExterna;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class CargaExternaBean {

    @PersistenceContext(name = Constantes.MAIN_DATASOURCE)
    private EntityManager em;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCargaExterna
     * @throws GeneralException
     */
    public SgCargaExterna obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCargaExterna> codDAO = new CodigueraDAO<>(em, SgCargaExterna.class);
                SgCargaExterna ext = codDAO.obtenerPorId(id);
                return ext;
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
                CodigueraDAO<SgCargaExterna> codDAO = new CodigueraDAO<>(em, SgCargaExterna.class);
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
     * @param ese SgCargaExterna
     * @return SgCargaExterna
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCargaExterna guardar(SgCargaExterna ese) throws GeneralException {
        try {
            if (ese != null) {
                if (CargaExternaValidacionNegocio.validar(ese)) {
                    CodigueraDAO<SgCargaExterna> codDAO = new CodigueraDAO<>(em, SgCargaExterna.class);

                    String filePath = ese.getCarExcelTmpPath();

                    if (filePath != null) { //Nuevo archivo

                        if (ese.getCarPk() != null) {
                            //Eliminar tuplas viejas si las hay
                            em.createQuery("DELETE FROM SgTuplaCargaExterna where tupCargaExterna.carPk = :pk").setParameter("pk", ese.getCarPk()).executeUpdate();
                        }

                    }

                    SgCargaExterna c = codDAO.guardar(ese);

                    if (filePath != null) {
                        procesarExcelCarga(c, filePath);
                    }

                    return c;

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ese;
    }

    private void procesarExcelCarga(SgCargaExterna car, String filePath) throws GeneralException {

        org.apache.poi.ss.usermodel.Workbook myWorkBook = null;
        InputStream file = null;
        try {

            BusinessException be = new BusinessException();
            file = new BufferedInputStream(new FileInputStream(tmpBasePath + filePath));

            if (file != null) {

                myWorkBook = WorkbookFactory.create(file);
                org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

                Iterator<Row> rowIterator = mySheet.iterator();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    if (row.getRowNum() > 0) {
                        Cell cell;

                        SgTuplaCargaExterna tupla = new SgTuplaCargaExterna();

                        cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            tupla.setTupIdentificador(cell != null ? Long.toString(Math.round(cell.getNumericCellValue())) : null);
                        } else {
                            tupla.setTupIdentificador(cell != null ? cell.getStringCellValue() : null);
                        }

                        if (tupla.getTupIdentificador() == null) {
                            continue;
                        }

                        cell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            tupla.setTupValor(cell != null ? cell.getNumericCellValue() : null);
                        } else {
                            tupla.setTupValor(cell != null ? Double.parseDouble(cell.getStringCellValue()) : null);
                        }

                        if (tupla.getTupValor() == null) {
                            be.addErrorTextoPlano("La fila " + row.getRowNum() + " no tiene valor.");
                            throw be;
                        }

                        cell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        tupla.setTupDesagregacion(cell != null ? cell.getStringCellValue() : null);

                        if (car.getCarDesagregacion() != null && StringUtils.isBlank(tupla.getTupDesagregacion())) {
                            be.addErrorTextoPlano("La fila " + row.getRowNum() + " no tiene desagregación.");
                            throw be;
                        }

                        tupla.setTupCargaExterna(car);
                        em.persist(tupla);

                    }
                }

            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        } finally {
            if (myWorkBook != null) {
                try {
                    myWorkBook.close();
                } catch (Exception ex) {
                }
            }
            if (file != null) {
                try {
                    file.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCargaExterna filtro) throws GeneralException {
        try {
            CargaExternaDAO codDAO = new CargaExternaDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCargaExterna>
     * @throws GeneralException
     */
    public List<SgCargaExterna> obtenerPorFiltro(FiltroCargaExterna filtro) throws GeneralException {
        try {
            CargaExternaDAO codDAO = new CargaExternaDAO(em);
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
                CodigueraDAO<SgCargaExterna> codDAO = new CodigueraDAO<>(em, SgCargaExterna.class);
                em.createQuery("DELETE FROM SgTuplaCargaExterna where tupCargaExterna.carPk = :pk")
                        .setParameter("pk", id)
                        .executeUpdate();
   
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> obtenerEstadisticaDeCargaExterna(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasExternasDAO codDAO = new EstadisticasExternasDAO(em);

            if (filtro.getAnio() == null || filtro.getIndicadorPk() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DATO_VACIO);
                throw be;
            }

            FiltroCargaExterna filEx = new FiltroCargaExterna();
            filEx.setAnio(filtro.getAnio());
            filEx.setIndicadorPk(filtro.getIndicadorPk());
            filEx.setNombrePk(filtro.getNombrePk()); //No es obligatorio. Hay constraint unique por anio/indicador
            filEx.setDesagregacion(filtro.getDesagregacion());
            if (filtro.getDesagregacion() == null) {
                filEx.setSinDesagregacion(Boolean.TRUE);
            }
            filEx.setIncluirCampos(new String[]{"carPk", "carTipoNumerico"});

            List<SgCargaExterna> cargas = this.obtenerPorFiltro(filEx);

            if (cargas.isEmpty()) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_NO_EXISTE_CARGA_EXTERNA);
                throw be;
            } else {

                Long pkCarga = cargas.get(0).getCarPk();
                return codDAO.obtenerEstadisticaDeCargaExterna(pkCarga, filtro.getDesagregacion() != null, cargas.get(0).getCarTipoNumerico());

            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
}
