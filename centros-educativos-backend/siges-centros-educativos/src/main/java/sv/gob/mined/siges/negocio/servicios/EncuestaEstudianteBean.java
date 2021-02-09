/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAllegado;
import sv.gob.mined.siges.filtros.FiltroEncuestaEstudiante;
import sv.gob.mined.siges.negocio.validaciones.EncuestaEstudianteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EncuestaEstudianteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAllegado;
import sv.gob.mined.siges.persistencia.entidades.SgDatosResidencialesPersona;
import sv.gob.mined.siges.persistencia.entidades.SgDireccion;
import sv.gob.mined.siges.persistencia.entidades.SgEncuestaEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgMenorEncuestaEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;

@Stateless
@Traced
public class EncuestaEstudianteBean {

    private static final Logger LOGGER = Logger.getLogger(EncuestaEstudianteBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    @ConfigProperty(name = "email-valid-pattern")
    private String emailPattern;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private AllegadoBean allegadoBean;

    @Inject
    private ConfiguracionBean configuracionBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPersona
     * @throws GeneralException
     */
    public SgEncuestaEstudiante obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEncuestaEstudiante> dao = new CodigueraDAO<>(em, SgEncuestaEstudiante.class);
                SgEncuestaEstudiante ret = dao.obtenerPorId(id, null);
                if (ret != null) {
                    if (ret.getEncElementosHogar() != null) {
                        ret.getEncElementosHogar().size();
                    }
                    if (ret.getEncMenores() != null) {
                        ret.getEncMenores().size();
                    }
                }
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEncuestaEstudiante> codDAO = new CodigueraDAO<>(em, SgEncuestaEstudiante.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroEncuestaEstudiante filtro) throws GeneralException {
        try {
            EncuestaEstudianteDAO DAO = new EncuestaEstudianteDAO(em, seguridadBean);
            return DAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ENCUESTA_ESTUDIANTE);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgEstudiante
     * @throws GeneralException
     */
    public List<SgEncuestaEstudiante> obtenerPorFiltro(FiltroEncuestaEstudiante filtro) throws GeneralException {
        try {
            EncuestaEstudianteDAO DAO = new EncuestaEstudianteDAO(em, seguridadBean);
            List<SgEncuestaEstudiante> encuestas = DAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ENCUESTA_ESTUDIANTE);

            for (SgEncuestaEstudiante e : encuestas) {
                if (BooleanUtils.isTrue(filtro.getInicializarElementosHogar())) {
                    if (e.getEncElementosHogar() != null) {
                        e.getEncElementosHogar().size();
                    }
                }
                if (BooleanUtils.isTrue(filtro.getInicializarMenores())) {
                    if (e.getEncMenores() != null) {
                        e.getEncMenores().size();
                    }
                }
            }

            return encuestas;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public static CellStyle createTitleStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        //font.setFontHeightInPoints((short) 10);
        //font.setFontName("Arial");
        XSSFCellStyle cs = workbook.createCellStyle();
        cs.setFont(font);
        cs.setFillForegroundColor(new XSSFColor(Color.decode("#60ddec")));
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cs;
    }

    public static CellStyle createColumnTitleStyle(XSSFWorkbook workbook) {
        //XSSFFont font = workbook.createFont();
        //font.setFontHeightInPoints((short) 10);
        //font.setFontName("Arial");
        XSSFCellStyle cs = workbook.createCellStyle();
        //cs.setFont(font);
        cs.setFillForegroundColor(new XSSFColor(Color.decode("#33a5b3")));
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cs;
    }

    /* 
     * Devuelve excel de consulta de encuestas
     *
     * @param filtro FiltroEncuestaEstudiante
     * @return byte[]
     * @throws GeneralException
     */
    public byte[] exportarObtenerPorFiltroExcel(FiltroEncuestaEstudiante filtro) throws GeneralException {
        try {
            EncuestaEstudianteDAO DAO = new EncuestaEstudianteDAO(em, seguridadBean);

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Encuestas");
            XSSFSheet menoresSheet = workbook.createSheet("Menores");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            Integer rowCount = 0;
            Integer menoresRowCount = 0;

            Row row = sheet.createRow(rowCount);
            Cell cell;

            cell = row.createCell(0);
            cell.setCellValue("Nombre completo");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(1);
            cell.setCellValue("NIE");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(2);
            cell.setCellValue("Sexo");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(3);
            cell.setCellValue("Fecha nacimiento");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(4);
            cell.setCellValue("Zona de residencia");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(5);
            cell.setCellValue("¿Cuántos dormitorios tiene su casa?");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(6);
            cell.setCellValue("En su hogar, ¿cuenta con lo siguiente?");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(7);
            cell.setCellValue("¿Cuenta con servicio de energía eléctrica en su casa?");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(8);
            cell.setCellValue("¿Cuál es la fuente principal de abastecimiento de agua de su casa?");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(9);
            cell.setCellValue("¿Cuál es el material principal del piso de su casa?");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(10);
            cell.setCellValue("¿Qué tipo de servicio sanitario tiene su casa?");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(11);
            cell.setCellValue("¿Tiene algún tipo de conexión a Internet residencial?");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(12);
            cell.setCellValue("¿Con cuál compañía?");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(13);
            cell.setCellValue("Distancia al centro educativo (km)");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(14);
            cell.setCellValue("Cantidad de personas que viven con el estudiante");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(15);
            cell.setCellValue("¿Viven personas menores de 18 con el estudiante?");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            ///Menores
            row = menoresSheet.createRow(menoresRowCount);

            cell = row.createCell(0);
            cell.setCellValue("NIE estudiante encuesta");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(1);
            cell.setCellValue("Menor");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(2);
            cell.setCellValue("Fecha nacimiento");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(3);
            cell.setCellValue("Estudia");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(4);
            cell.setCellValue("NIE");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(5);
            cell.setCellValue("Centro Educativo");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            cell = row.createCell(6);
            cell.setCellValue("Medio de transporte");
            cell.setCellStyle(createColumnTitleStyle(workbook));

            Long cant = DAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ENCUESTA_ESTUDIANTE);

            if (cant > 1000) {
                throw new IllegalStateException();
            }

            Long first = 0L;
            Long maxResults = 50L;

            filtro.setFirst(first);
            filtro.setMaxResults(maxResults);
            filtro.setIncluirCampos(null);

            while (first < cant) {

                List<SgEncuestaEstudiante> encuestas = DAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ENCUESTA_ESTUDIANTE);

                for (SgEncuestaEstudiante e : encuestas) {
                    rowCount++;
                    row = sheet.createRow(rowCount);

                    SgPersona p = e.getEncEstudiante().getEstPersona();

                    cell = row.createCell(0);
                    cell.setCellValue(p.getPerNombreCompleto());

                    cell = row.createCell(1);
                    cell.setCellValue(p.getPerNie());

                    cell = row.createCell(2);
                    cell.setCellValue(p.getPerSexo().getSexNombre());

                    cell = row.createCell(3);
                    cell.setCellValue(formatter.format(p.getPerFechaNacimiento()));

                    cell = row.createCell(4);
                    cell.setCellValue(e.getEncZonaResidencia().getZonNombre());

                    cell = row.createCell(5);
                    cell.setCellValue(e.getEncCantidadDormitoriosCasa());

                    cell = row.createCell(6);
                    cell.setCellValue(e.getEncElementosHogarString());

                    cell = row.createCell(7);
                    cell.setCellValue(BooleanUtils.isTrue(e.getEncTieneServicioEnergiaElectricaResidencial()) ? "Sí" : "No");

                    cell = row.createCell(8);
                    String fuenteString = e.getEncFuenteAbastecimientoAguaResidencial().getFaaNombre();
                    if (StringUtils.isNotBlank(e.getEncFuenteAbastecimientoAguaResidencialOtra())) {
                        fuenteString += " - " + e.getEncFuenteAbastecimientoAguaResidencialOtra();
                    }
                    cell.setCellValue(fuenteString);

                    cell = row.createCell(9);
                    String materialPisoString = e.getEncMaterialPisoResidencial().getMapNombre();
                    if (StringUtils.isNotBlank(e.getEncMaterialPisoResidencialOtro())) {
                        materialPisoString += " - " + e.getEncMaterialPisoResidencialOtro();
                    }
                    cell.setCellValue(materialPisoString);

                    cell = row.createCell(10);
                    String tipoServicioSanitarioString = e.getEncTipoServicioSanitarioResidencial().getTssNombre();
                    if (StringUtils.isNotBlank(e.getEncTipoServicioSanitarioResidencialOtro())) {
                        tipoServicioSanitarioString += " - " + e.getEncTipoServicioSanitarioResidencialOtro();
                    }
                    cell.setCellValue(tipoServicioSanitarioString);

                    cell = row.createCell(11);
                    cell.setCellValue(BooleanUtils.isTrue(e.getEncTieneConexionInternetResidencial()) ? "Sí" : "No");

                    cell = row.createCell(12);
                    cell.setCellValue(e.getEncCompaniaInternetResidencial() != null ? e.getEncCompaniaInternetResidencial().getCteNombre() : "");

                    cell = row.createCell(13);
                    cell.setCellValue(e.getEncEstudianteDisKmCentro() != null ? e.getEncEstudianteDisKmCentro().toString() : "");

                    cell = row.createCell(14);
                    cell.setCellValue(e.getEncViveConCantidad());

                    cell = row.createCell(15);
                    cell.setCellValue(BooleanUtils.isTrue(e.getEncViveConPersonasMenores()) ? "Sí" : "No");

                    for (SgMenorEncuestaEstudiante menor : e.getEncMenores()) {

                        menoresRowCount++;

                        row = menoresSheet.createRow(menoresRowCount);

                        cell = row.createCell(0);
                        cell.setCellValue(p.getPerNie());

                        cell = row.createCell(1);
                        cell.setCellValue(menor.getMenNombreCompleto());

                        cell = row.createCell(2);
                        cell.setCellValue(formatter.format(menor.getMenFechaNacimiento()));

                        cell = row.createCell(3);
                        cell.setCellValue(BooleanUtils.isTrue(menor.getMenEstudia()) ? "Sí" : "No");

                        cell = row.createCell(4);
                        cell.setCellValue(menor.getMenNie() != null ? menor.getMenNie().toString() : "");

                        cell = row.createCell(5);
                        cell.setCellValue(menor.getMenSede() != null ? menor.getMenSede().getSedCodigoNombre() : "");

                        cell = row.createCell(6);
                        cell.setCellValue(menor.getMenMedioTransporte() != null ? menor.getMenMedioTransporte().getMtrNombre() : "");

                    }

                }

                em.clear();
                first += maxResults;
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            bos.close();
            byte[] bytes = bos.toByteArray();

            return bytes;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public SgEncuestaEstudiante guardar(SgEncuestaEstudiante entity) throws GeneralException {
        try {
            if (entity != null) {
                if (EncuestaEstudianteValidacionNegocio.validar(entity)) {

                    CodigueraDAO<SgEncuestaEstudiante> codDAO = new CodigueraDAO<>(em, SgEncuestaEstudiante.class);

                    if (entity.getEncPk() == null) {
                        entity.setEncFechaIngreso(LocalDateTime.now());
                    }

                    SgEstudiante est = em.find(SgEstudiante.class, entity.getEncEstudiante().getEstPk());
                    SgPersona per = est.getEstPersona();
                    SgDatosResidencialesPersona datosResidenciales = per.getPerDatosResidenciales();
                    SgDireccion dir = per.getPerDireccion();

                    est.setEstSintonizaCanal10(entity.getEncSintonizaCanal10());
                    est.setEstSintonizaFranjaEducativa(entity.getEncSintonizaFranjaEducativa());

                    datosResidenciales.setPerCantidadDormitoriosCasa(entity.getEncCantidadDormitoriosCasa());
                    datosResidenciales.setPerCompaniaInternetResidencial(entity.getEncCompaniaInternetResidencial());
                    datosResidenciales.setPerTieneConexionInternetResidencial(entity.getEncTieneConexionInternetResidencial());
                    if (entity.getEncElementosHogar() != null) {
                        datosResidenciales.setPerElementosHogar(new ArrayList<>(entity.getEncElementosHogar()));
                    }
                    datosResidenciales.setPerFuenteAbastecimientoAguaResidencial(entity.getEncFuenteAbastecimientoAguaResidencial());
                    datosResidenciales.setPerFuenteAbastecimientoAguaResidencialOtra(entity.getEncFuenteAbastecimientoAguaResidencialOtra());
                    datosResidenciales.setPerMaterialPisoResidencial(entity.getEncMaterialPisoResidencial());
                    datosResidenciales.setPerMaterialPisoResidencialOtro(entity.getEncMaterialPisoResidencialOtro());
                    datosResidenciales.setPerTieneServicioEnergiaElectricaResidencial(entity.getEncTieneServicioEnergiaElectricaResidencial());
                    datosResidenciales.setPerTipoServicioSanitarioResidencial(entity.getEncTipoServicioSanitarioResidencial());
                    datosResidenciales.setPerTipoServicioSanitarioResidencialOtro(entity.getEncTipoServicioSanitarioResidencialOtro());

                    est.setEstDisKmCentro(entity.getEncEstudianteDisKmCentro());
                    per.setPerViveConCantidad(entity.getEncViveConCantidad());
                    dir.setDirZona(entity.getEncZonaResidencia());

                    for (SgMenorEncuestaEstudiante menor : entity.getEncMenores()) {

                        SgAllegado all = new SgAllegado();
                        all.setAllReferente(Boolean.FALSE);
                        all.setAllViveConAllegado(Boolean.TRUE);
                        all.setAllContactoEmergencia(Boolean.FALSE);
                        all.setAllDependiente(Boolean.FALSE);
                        all.setAllPersonaReferenciada(per);
                        all.setAllPersona(new SgPersona());

                        //Verificar si allegado ya existe
                        if (menor.getMenPersonaFk() != null) {
                            FiltroAllegado filAll = new FiltroAllegado();
                            filAll.setAllPersonaPk(menor.getMenPersonaFk());
                            filAll.setAllPersonaReferenciadaPk(per.getPerPk());
                            filAll.setMaxResults(1L);
                            List<SgAllegado> alls = allegadoBean.obtenerPorFiltro(filAll);

                            if (!alls.isEmpty()) {
                                all = alls.get(0);
                            } else {
                                SgPersona perMenor = em.find(SgPersona.class, menor.getMenPersonaFk());
                                perMenor.setPerNoValidarDiagnostico(Boolean.TRUE);
                                all.setAllPersona(perMenor);
                                
                            }
                        } else {
                            //La dirección se crea solamente cuando es una nueva persona
                            SgPersona perMenor = all.getAllPersona();
                            perMenor.setPerNoValidarDiagnostico(Boolean.TRUE);
                            perMenor.setPerDireccion(dir.clonenew());

                        }

                        //Modificaciones entidad persona segun datos de la encuesta
                        SgPersona perMenor = all.getAllPersona();
                        perMenor.setPerPrimerNombre(menor.getMenPrimerNombre());
                        perMenor.setPerPrimerApellido(menor.getMenPrimerApellido());
                        perMenor.setPerSegundoNombre(menor.getMenSegundoNombre());
                        perMenor.setPerSegundoApellido(menor.getMenSegundoApellido());
                        perMenor.setPerNacionalidad(menor.getMenNacionalidad());
                        perMenor.setPerSexo(menor.getMenSexo());
                        perMenor.setPerEstadoCivil(menor.getMenEstadoCivil());
                        perMenor.setPerFechaNacimiento(menor.getMenFechaNacimiento());
                        perMenor.setPerNoValidarDiagnostico(Boolean.TRUE);

                        //Modificaciones entidad allegado
                        all.setAllEsFamiliar(menor.getMenEsFamiliar());
                        all.setAllTipoParentesco(menor.getMenTipoParentesco());
                        all.getAllPersona().setPerNoValidarDiagnostico(Boolean.TRUE);
                        //Modificaciones entidad estudiante allegado
                        if (menor.getMenEstudianteFk() != null && menor.getMenMedioTransporte() != null) {
                            SgEstudiante estMenor = em.find(SgEstudiante.class, menor.getMenEstudianteFk());
                            estMenor.setEstMedioTransporte(menor.getMenMedioTransporte());
                        }
                       

                        all = allegadoBean.guardar(all);

                        menor.setMenPersonaFk(all.getAllPersona().getPerPk());
                    }

                    entity = codDAO.guardar(entity);
                    est.setEstUltimaEncuesta(entity.getEncPk());
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
     * Elimina el objeto indicado
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    private void eliminar(SgEncuestaEstudiante enc) throws GeneralException {
        if (enc != null) {
            try {
                CodigueraDAO<SgEncuestaEstudiante> codDAO = new CodigueraDAO<>(em, SgEncuestaEstudiante.class);
                enc.getEncEstudiante().setEstUltimaEncuesta(null);
                codDAO.eliminar(enc, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
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
                SgEncuestaEstudiante enc = em.find(SgEncuestaEstudiante.class, id);
                this.eliminar(enc);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve SgEncuestaEstudiante en una determinada revision
     *
     * @param id Long
     * @param revision Long
     * @return T
     */
    public SgEncuestaEstudiante obtenerEnRevision(Long id, Long revision) {
        try {
            AuditReader reader = AuditReaderFactory.get(em);
            List<SgEncuestaEstudiante> respuesta = reader.createQuery().forEntitiesAtRevision(SgEncuestaEstudiante.class, revision)
                    .add(AuditEntity.id().eq(id))
                    .getResultList();
            if (respuesta != null && respuesta.size() > 0) {
                SgEncuestaEstudiante ret = respuesta.get(0);
                return ret;
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

}
