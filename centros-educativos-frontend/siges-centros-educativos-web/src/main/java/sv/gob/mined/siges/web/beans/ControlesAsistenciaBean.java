/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgControlAsistenciaCabezal;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroControlAsistenciaCabezal;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyControlAsistenciaCabezalDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ControlAsistenciaCabezalRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgPlantilla;
import sv.gob.mined.siges.web.enumerados.EnumAmbito;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ControlesAsistenciaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ControlesAsistenciaBean.class.getName());

    @Inject
    private ControlAsistenciaCabezalRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SeccionRestClient restSeccion;

    @Inject
    private FiltrosSeccionBean filtrosSeccion;
    
    @Inject
    private PlantillaRestClient plantillaRestClient;

    private FiltroControlAsistenciaCabezal filtro = new FiltroControlAsistenciaCabezal();
    private LazyControlAsistenciaCabezalDataModel controlAsistenciaCabezalLazyModel;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private List<RevHistorico> historialControlAsistenciaCabezal = new ArrayList();
    private SgControlAsistenciaCabezal entidadEnEdicion = new SgControlAsistenciaCabezal();
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;

    private InputStream file;
    private String nombreFile;
    private Boolean uploading = Boolean.FALSE;
    private Integer filasCantMax = 500;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(xlsx)$/";//xlsx por defecto
    
    private SofisComboG<EnumAmbito> comboAmbito;
    private SgPlantilla plantillaImportacion;

    public ControlesAsistenciaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            inicializarFiltrosSeccion();
            cargarConfiguracionPropsArchivo();
            if (sessionBean.getAmbitosSeleccionablesBusqueda() != null && sessionBean.getAmbitosSeleccionablesBusqueda().size() > 1){
                comboAmbito = new SofisComboG(sessionBean.getAmbitosSeleccionablesBusqueda() , "text");
            }
            obtenerPlantilla();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void obtenerPlantilla(){
        try{
            plantillaImportacion = plantillaRestClient.obtenerPorCodigo(Constantes.PLANTILLA_IMPORTACION_ASISTENCIAS);
                
       } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }         
    }

    public void inicializarFiltrosSeccion() {
        this.filtrosSeccion.setFiltro(this.filtro);
        this.filtrosSeccion.cargarCombos();
        this.filtrosSeccion.seleccionarUltimoAnio();
    }

    public void forceInit() {
        //Utilizado para forzar init de ControlesAsistenciaBean antes que FiltrosSeccionBean
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ASISTENCIAS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarConfiguracionPropsArchivo() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigoExacto(Constantes.TAMANIO_ARCHIVO_IMPORT);
            List<SgConfiguracion> conf = restCatalogo.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                tamanioImportArchivo = conf.get(0).getConValor();
            }

            fc.setCodigoExacto(Constantes.TIPO_ARCHIVO_IMPORT);
            conf = restCatalogo.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                tipoImportArchivo = conf.get(0).getConValor();
            }

            fc.setCodigoExacto(Constantes.CANT_FILAS_ARCHIVO_IMPORT);
            conf = restCatalogo.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                filasCantMax = Integer.parseInt(conf.get(0).getConValor());
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public String buscar() {
        try {
            if (comboAmbito != null){
                filtro.setAmbito(comboAmbito.getSelectedT());
            }

            filtro.setDepartamento(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setMunicipio(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setIncluirCampos(new String[]{"cacPk", "cacEstudiantesPresentes", "cacEstudiantesEnLista",
                "cacEstudiantesAusentesJustificados", "cacEstudiantesAusentesSinJustificar",
                "cacSeccion.secServicioEducativo.sduSede.sedTipo",
                "cacSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                "cacSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                "cacSeccion.secServicioEducativo.sduSede.sedNombre",
                "cacSeccion.secServicioEducativo.sduSede.sedCodigo",
                "cacSeccion.secServicioEducativo.sduGrado.graNombre",
                "cacSeccion.secNombre",
                "cacSeccion.secPk",
                "cacFecha", "cacSeccion.secEstado",
                "cacUltModUsuario", "cacVersion"});

            totalResultados = restClient.buscarTotal(filtro);
            controlAsistenciaCabezalLazyModel = new LazyControlAsistenciaCabezalDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> deptos = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(deptos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgControlAsistenciaCabezal controlCabezal) {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = controlCabezal;
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCacPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            entidadEnEdicion = null;
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarDepartamento() {
        try {

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (comboDepartamento.getSelectedT() != null) {

                FiltroMunicipio fc = new FiltroMunicipio();
                fc.setOrderBy(new String[]{"munNombre"});
                fc.setAscending(new boolean[]{true});
                fc.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                fc.setMunDepartamentoFk(comboDepartamento.getSelectedT().getDepPk());

                List<SgMunicipio> municipios = restCatalogo.buscarMunicipio(fc);
                comboMunicipio = new SofisComboG(municipios, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroControlAsistenciaCabezal();
        //filtrosSeccion.limpiarCombos();
        inicializarFiltrosSeccion();
        comboDepartamento.setSelected(-1);
        comboMunicipio.setSelected(-1);
        buscar();
    }

    public String historial(Long id) {
        try {
            historialControlAsistenciaCabezal = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void nuevoImport() {
        nombreFile = null;
        file = null;
        uploading = Boolean.FALSE;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            nombreFile = event.getFile().getFileName();
            file = event.getFile().getInputstream();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void renderLoading() {
        if (file != null) {
            if (uploading) {
                uploading = Boolean.FALSE;
                procesarArchivo();
            } else {
                uploading = Boolean.TRUE;
            }
        }
    }

    public void procesarArchivo() {
        org.apache.poi.ss.usermodel.Workbook myWorkBook = null;
        try {
            if (file != null) {
                myWorkBook = WorkbookFactory.create(file);
                org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

                Integer cantRows = 0;

                // Get iterator to all the rows in current sheet
                Iterator<Row> rowIterator = mySheet.iterator();
                List<String[]> archivo = new ArrayList<>();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    if (row.getRowNum() > 0) {
                        Cell cell;
                        Long seccion = null;
                        LocalDate fecha;

                        //Se extraen los demas datos
                        String[] datos = new String[7];

                        //Extraemos el valor de la sección
                        cell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            seccion = Math.round(cell.getNumericCellValue());
                        } else {
                            if (row.getCell(2, Row.RETURN_BLANK_AS_NULL) == null
                                    && row.getCell(3, Row.RETURN_BLANK_AS_NULL) == null
                                    && row.getCell(4, Row.RETURN_BLANK_AS_NULL) == null) {
                                continue;
                            } else {
                                file = null;
                                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_SECCION_IMPORT_VACIO), "");
                                return;
                            }
                        }

                        datos[0] = String.valueOf(seccion);

                        //Extraemos el valor de la fecha
                        cell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            fecha = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        } else {
                            //Exit no se puede crear sin la fecha
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_FECHA_IMPORT_VACIO), "");
                            return;
                        }
                        datos[1] = String.valueOf(fecha);

                        //Codigo
                        cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        datos[2] = cell != null ? cell.getStringCellValue() : null;
                        //NIE
                        cell = row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_NIE_IMPORT_VACIO), "");
                            return;
                        }
                        datos[3] = cell != null ? String.valueOf(Math.round(cell.getNumericCellValue())) : null;
                        //Falto
                        cell = row.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        datos[4] = cell != null ? cell.getStringCellValue() : null;
                        //Justificacion
                        cell = row.getCell(5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        datos[5] = cell != null ? cell.getStringCellValue() : null;
                        //Observacion
                        cell = row.getCell(6, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        datos[6] = cell != null ? cell.getStringCellValue() : null;

                        archivo.add(datos);
                        cantRows++;

                    }
                }

                if (cantRows > filasCantMax) {
                    file = null;
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "La cantidad de filas del archivo no puede ser mayor a " + filasCantMax + ".", "");
                    return;
                }

                String[][] itemsArray = new String[archivo.size()][];
                itemsArray = archivo.toArray(new String[archivo.size()][]);

                restClient.importar(itemsArray);
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, "Se procesaron correctamente " + cantRows + " asistencias.", "");
            }
        } catch (BusinessException be) {
            file = null;
            JSFUtils.agregarMensajesDetalle(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            file = null;
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } finally {
            if (myWorkBook != null) {
                try {
                    myWorkBook.close();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
        }
    }

    public void eliminarFile() {
        file = null;
        nombreFile = null;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public ControlAsistenciaCabezalRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ControlAsistenciaCabezalRestClient restClient) {
        this.restClient = restClient;
    }

    public FiltroControlAsistenciaCabezal getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroControlAsistenciaCabezal filtro) {
        this.filtro = filtro;
    }

    public LazyControlAsistenciaCabezalDataModel getControlAsistenciaCabezalLazyModel() {
        return controlAsistenciaCabezalLazyModel;
    }

    public void setControlAsistenciaCabezalLazyModel(LazyControlAsistenciaCabezalDataModel controlAsistenciaCabezalLazyModel) {
        this.controlAsistenciaCabezalLazyModel = controlAsistenciaCabezalLazyModel;
    }

    public SgControlAsistenciaCabezal getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgControlAsistenciaCabezal entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialControlAsistenciaCabezal() {
        return historialControlAsistenciaCabezal;
    }

    public void setHistorialControlAsistenciaCabezal(List<RevHistorico> historialControlAsistenciaCabezal) {
        this.historialControlAsistenciaCabezal = historialControlAsistenciaCabezal;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SeccionRestClient getRestSeccion() {
        return restSeccion;
    }

    public void setRestSeccion(SeccionRestClient restSeccion) {
        this.restSeccion = restSeccion;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public String getNombreFile() {
        return nombreFile;
    }

    public void setNombreFile(String nombreFile) {
        this.nombreFile = nombreFile;
    }

    public Boolean getUploading() {
        return uploading;
    }

    public void setUploading(Boolean uploading) {
        this.uploading = uploading;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }

    public String getTipoImportArchivo() {
        return tipoImportArchivo;
    }

    public void setTipoImportArchivo(String tipoImportArchivo) {
        this.tipoImportArchivo = tipoImportArchivo;
    }

    public SofisComboG<EnumAmbito> getComboAmbito() {
        return comboAmbito;
    }

    public void setComboAmbito(SofisComboG<EnumAmbito> comboAmbito) {
        this.comboAmbito = comboAmbito;
    }

    public SgPlantilla getPlantillaImportacion() {
        return plantillaImportacion;
    }

    public void setPlantillaImportacion(SgPlantilla plantillaImportacion) {
        this.plantillaImportacion = plantillaImportacion;
    }
    
    

}
