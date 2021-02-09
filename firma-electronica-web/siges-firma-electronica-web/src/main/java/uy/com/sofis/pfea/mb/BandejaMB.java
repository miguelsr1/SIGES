/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.mb;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import com.sofis.utils.UtilVarios;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.agesic.firma.datatypes.InfoFirma;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import uy.com.sofis.pfea.binarios.PfeaFileUtils;
import uy.com.sofis.pfea.constantes.ConstantesErrores;
import uy.com.sofis.pfea.entities.Documento;
import uy.com.sofis.pfea.enums.DocPaisEmisor;
import uy.com.sofis.pfea.enums.EstadoDocumentoBandeja;
import uy.com.sofis.pfea.enums.TipoDocumento;
import uy.com.sofis.pfea.exceptions.TechnicalException;
import uy.com.sofis.pfea.sb.ConfiguracionSB;
import uy.com.sofis.pfea.sb.DocumentosSB;
import uy.com.sofis.pfea.sb.EntityManagementSB;
import uy.com.sofis.pfea.sb.FirmaSB;
import uy.com.sofis.pfea.utilidades.EntityReferenceDataProvider;
import uy.com.sofis.pfea.utilidades.GeneralLazyDataModel;
import uy.com.sofis.pfea.utilidades.IDataProvider;
import uy.com.sofis.pfea.utilidades.SelectItemComparator;

/**
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class BandejaMB implements Serializable {

    private static final Logger logger = Logger.getLogger(BandejaMB.class.getName());

    @Inject
    EntityManagementSB entityManagementSB;

    @Inject
    DocumentosSB documentosSB;


    @Inject
    ConfiguracionSB configuracionSB;

    @Inject
    FirmaSB firmaSB;

    @Inject
    private TextMB textMB;

    @Inject
    private SessionBean sesionMB;

    private LazyDataModel lazyModelVigentes;
    private LazyDataModel lazyModelVencidos;
    private String nombreOriginal;
    private Documento documentoEnEdicion;
    private boolean documentoCargado = false;

    private boolean permitirEditar = false;

    private String idTtransaccion;
    private String linkDescargaAplicativoPfea;


    private boolean descargado = false;
    private String metodoFirma = null;

    private String pasoWizard = null;

    private boolean firmaValida = true;

    private List<InfoFirma> infoFirmas;

    private String mensajeTitulo = null;
    private Boolean soloMostrarFirma = false;

    @PostConstruct
    public void init() {

        //Ver si se proviene de cliente externo para firmar documento ya subido
        String idTransaccionFirmar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id_transaccion");
        if(idTransaccionFirmar != null && !idTransaccionFirmar.trim().equalsIgnoreCase("")) {
            soloMostrarFirma = true;
            openWizardModal(idTransaccionFirmar);
        }
        else {
            buscarDocumentosVigentes();
            buscarDocumentosVencidos();
            permitirEditar = false;

        }
    }

    public void cancelarEliminarDocumento() {
        RequestContext.getCurrentInstance().execute("$('#confirmModalCancelar').modal('hide');");
    }

    public void openWizardModal(String idTransaction) {
        idTtransaccion = idTransaction;
        pasoWizard = "METODO";
        RequestContext.getCurrentInstance().execute("$('#modalFirmarArchivo').modal('show')");
    }

    private boolean documentosEstadoExpirados = false;

    public void cambiarTipoDocumento() {
        buscarDocumentosVigentes();
    }

    public void cambiarDocumentoEstado() {
        documentosEstadoExpirados = !documentosEstadoExpirados;
    }


    public boolean getDocumentosEstadoExpirados() {
        return documentosEstadoExpirados;
    }

    public void buscarDocumentosVigentes() {
        try {
            List<CriteriaTO> criteriosAnd = new ArrayList<>();
            criteriosAnd.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "eliminado", true));
            criteriosAnd.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "fechaExpiracion", new Date()));
            CriteriaTO condicion = CriteriaTOUtils.createANDTO(criteriosAnd.toArray(new CriteriaTO[0]));
            String[] propiedades = {"id", "nombreOriginal", "firmado", "fechaCreacion",
                    "fechaExpiracion", "estadoDocumento", "identificador", "origen", "codigopac", "ultimaAccion"};
            String className = Documento.class.getName();
            String[] orderBy = {"fechaCreacion", "id"};
            boolean[] asc = {false, false};
            IDataProvider dataProvider = new EntityReferenceDataProvider(entityManagementSB, propiedades, className, condicion, orderBy, asc);
            lazyModelVigentes = new GeneralLazyDataModel(dataProvider);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, textMB.obtenerTexto("errorBuscandoConvenios"), ex);
        }
    }

    public void buscarDocumentosVencidos() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            List<CriteriaTO> criteriosAnd = new ArrayList<>();
            List<CriteriaTO> criteriosOr = new ArrayList<>();
            criteriosOr.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "fechaExpiracion", new Date()));
            criteriosOr.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estadoDocumento", EstadoDocumentoBandeja.EX));
            criteriosOr.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estadoDocumento", EstadoDocumentoBandeja.EL));
            criteriosAnd.add(CriteriaTOUtils.createORTO(criteriosOr.toArray(new CriteriaTO[0])));
            CriteriaTO condicion = CriteriaTOUtils.createANDTO(criteriosAnd.toArray(new CriteriaTO[0]));
            String[] propiedades = {"id", "nombreOriginal", "fechaCreacion", "fechaExpiracion", "fechaEliminacion",
                    "firmado", "origen", "estadoDocumento", "motivo", "ultimaAccion"};
            String[] orderBy = {"fechaCreacion", "id"};
            boolean[] asc = {false, false};
            String className = Documento.class.getName();
            IDataProvider dataProvider = new EntityReferenceDataProvider(entityManagementSB, propiedades, className, condicion, orderBy, asc);
            lazyModelVencidos = new GeneralLazyDataModel(dataProvider);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, textMB.obtenerTexto("errorBuscandoConvenios"), ex);
        }
    }

    public String describirMetodoFirma() {
        if(StringUtils.isBlank(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionGeneral");
        }
        if("CEDULA".equals(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionCedula");
        }
        if("TOKEN_ABITAB".equals(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionTokenAbitab");
        }
        if("TOKEN_CORREO".equals(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionTokenCorreo");
        }
        if("NUBE_ABITAB".equals(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionNubeAbitab");
        }
        if("ARCHIVO".equals(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionArchivo");
        }
        return textMB.obtenerTexto("messages.metodoFirmaDescripcionGeneral");
    }

    

    List<SelectItem> tiposDocumento = null;

    public List<SelectItem> getTiposDocumento() {
        if (tiposDocumento == null) {
            tiposDocumento = new ArrayList<>();
            for (TipoDocumento tipoDocumento0 : TipoDocumento.values()) {
                tiposDocumento.add(new SelectItem(tipoDocumento0, tipoDocumento0.getNombre()));
            }
            Collections.sort(tiposDocumento, new SelectItemComparator());
        }
        return tiposDocumento;
    }

    List<SelectItem> paisesEmisores = null;

    public List<SelectItem> getPaisesEmisores() {
        if (paisesEmisores == null) {
            paisesEmisores = new ArrayList<>();
            for (DocPaisEmisor paisEmisor : DocPaisEmisor.values()) {
                paisesEmisores.add(new SelectItem(paisEmisor, paisEmisor.getNombre()));
            }
            Collections.sort(paisesEmisores, new SelectItemComparator());
        }
        return paisesEmisores;
    }


    /**
     * Devuelve el documento para descargar. Si está firmado, devuelve el archivo firmado, sino devuelve el archivo original.
     * Si el documento contiene un solo archivo se devuelve tal cual, sino se devuelve un archivo zip contenido los archivos
     * @param docId
     * @return
     */

    public StreamedContent getDocumentoDescarga(Integer docId) {
        try {
            
            Documento documento = documentosSB.obtenerDocumentoPorId(docId);
            List<byte[]> bytesList = documentosSB.obtenerBytesDocumento(documento.getIdentificador(), true);
            if(bytesList==null || bytesList.isEmpty()) {
                bytesList = documentosSB.obtenerBytesDocumento(documento.getIdentificador(), false);
            }
            
            if(bytesList==null || bytesList.isEmpty()) {
                String msj = textMB.obtenerTexto("messages.documentoNoEncontrado");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msj, msj));
                return null;
            }

            byte[] bytes;
            if (bytesList.size() == 1) {
                bytes = bytesList.get(0);
                if (bytes != null) {
                    InputStream stream = new ByteArrayInputStream(bytes);
                    return new DefaultStreamedContent(stream, "application/octet-stream", documento.getNombreOriginal());
                }
            } else {
                bytes = zip(documento.getNombreOriginal(), bytesList);
                if (bytes != null) {
                    InputStream stream = new ByteArrayInputStream(bytes);
                    return new DefaultStreamedContent(stream, "application/octet-stream", documento.getNombreOriginal()+".zip");
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al descargar el archivo", ex);
        }
        return null;
    }
    
    //Adaptado de: https://cirovladimir.wordpress.com/2014/07/03/java-comprimir-archivos-a-un-zip-en-memoria/
    private byte[] zip(String nombre, List<byte[]> documentos) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zout = null;
        try {
            zout = new ZipOutputStream(bos);
            int i = 0;
            for (byte[] doc : documentos) {
                ZipEntry ze = new ZipEntry(nombre + "_" + i + ".pdf");
                zout.putNextEntry(ze);
                zout.write(doc);
                zout.closeEntry();
                i++;
            }
        } finally {
            if (zout != null) {
                zout.close();
            }
        }
        return bos.toByteArray();
    }
    
    public void modalEliminarDocumento(String identificador) {
        idTtransaccion = identificador;
        RequestContext.getCurrentInstance().execute("$('#confirmModalCancelar').modal('show')");
    }

    public void eliminarDocumento() throws DAOGeneralException {
        try {
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
            Documento documentoABorrar = documentosSB.obtenerDocumentoPorIdentificador(idTtransaccion, false);
            documentoABorrar.setEliminado(Boolean.TRUE);
            documentoABorrar.setEstadoDocumento(EstadoDocumentoBandeja.EL);
            documentoABorrar.setMotivo("");
            documentosSB.eliminarDocumento(documentoABorrar);
            buscarDocumentosVigentes();
            buscarDocumentosVencidos();
            RequestContext.getCurrentInstance().execute("$('#confirmModalCancelar').modal('hide')");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(e.getMessage());
            throw ge;
        }
    }

    public void confirmarEliminarDocumento() {
        RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('show');");
    }

    public String abrirBandeja(boolean actualizar) {

        Documento documento = documentosSB.obtenerDocumentoPorIdentificador(idTtransaccion, false);
        if(documento != null && BooleanUtils.isTrue(documento.getFirmado()) && documento.getRetornoNavegacion() != null) {
            try {
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.redirect(UtilVarios.addIdTransaccion(documento.getRetornoNavegacion(), idTtransaccion));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            if(actualizar) {
                buscarDocumentosVigentes();
            }
            RequestContext.getCurrentInstance().execute("$('#modalFirmarArchivo').modal('hide')");
        }

        return null;
    }

    public String cerrar() {
        return "/privado/home?faces-redirect=true;";
    }

    public String siguientePaso() {
        if(StringUtils.isBlank(pasoWizard)) {
            return null;
        }
        if("METODO".equals(pasoWizard)) {
            if(StringUtils.isBlank(metodoFirma)) {
                String msj = textMB.obtenerTexto("messages.debeSeleccionarUnMetodoDeFirma");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msj, msj));
                return null;
            }
            if("NUBE_ABITAB".equals(metodoFirma)) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FIRMANUBEABITAB_DOCIDENTIFICADOR", idTtransaccion);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FIRMANUBEABITAB_TARGET", "webv");
                HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String urlScheme = httpRequest.getScheme();
                String urlServer = httpRequest.getServerName();
                String urlPort = ""+httpRequest.getServerPort();
                String urlContext = httpRequest.getContextPath();
                if(!urlContext.startsWith("/")) {
                    urlContext = "/"+urlContext;
                }
                if(!urlContext.endsWith("/")) {
                    urlContext = urlContext+"/";
                }
                String urlPaso1 = urlScheme+"://"+urlServer+":"+urlPort+urlContext+"NubeAbitab1Servlet";
                RequestContext.getCurrentInstance().execute("document.location='"+urlPaso1+"';");
                return null;
            }
            //Cualquier otro método que requiera el aplicativo
            pasoWizard = "APLICATIVO";
            return null;
        }
        if("APLICATIVO".equals(pasoWizard)) {
            if (!descargado) {
                String msj = textMB.obtenerTexto("messages.debeDescargarEInstalarElAplicativoDeFirma");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msj, msj));
                FacesContext.getCurrentInstance().validationFailed(); //Para que no invoque la descarga del archivo
                return null;
            }
            pasoWizard = "FIRMA";
            return null;
        }
        if("FIRMA".equals(pasoWizard)) {
            String msj = textMB.obtenerTexto("messages.debeFirmarElDocumentoConElAplicativoDeFirma");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msj, msj));
            return null;
        }
        return null;
    }

    @SuppressWarnings("UseSpecificCatch")
    public StreamedContent getArchivoPfea() {
        try {
            String urlWs = configuracionSB.obtenerConfiguracionPorCodigos("SIGES_FIRMA_WS");
            String urlUpdate = configuracionSB.obtenerConfiguracionPorCodigos("UPDATE_URL");
            String urlCRL = configuracionSB.obtenerConfiguracionPorCodigos("FIRMA_VALIDAR_CRL_URL");
            Boolean enabledCRL = Boolean.parseBoolean(configuracionSB.obtenerConfiguracionPorCodigos("FIRMA_CLIENTE_VALIDAR_CRL"));
            String trustoreCAName = configuracionSB.obtenerConfiguracionPorCodigos("TRUSTORE_CLIENTE_CA_NAME");
            Boolean trustoreValidationEnabled = Boolean.parseBoolean(configuracionSB.obtenerConfiguracionPorCodigos("TRUSTORE_CLIENTE_CA_VALIDATION_ENABLED"));
            Documento documento = documentosSB.obtenerDocumentoPorIdentificador(idTtransaccion, false);
            String rutaArchivoPfeaGenerado = PfeaFileUtils.PfeaArchiveMaker(idTtransaccion, urlWs, urlUpdate, metodoFirma, urlCRL, enabledCRL, documento.getDebeFirmarUsuario(), trustoreCAName, trustoreValidationEnabled);
            File file = new File(rutaArchivoPfeaGenerado);
            InputStream stream = new FileInputStream(file);
            return new DefaultStreamedContent(stream, "application/octet-stream", file.getName());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al descargar el archivo", ex);
        }
        return null;
    }

    public String getSoftwarePfeaDownlodLink() {
        try {
            if(linkDescargaAplicativoPfea == null) {
                HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String userAgent = request.getHeader("user-agent");
                if(StringUtils.isBlank(userAgent)) {
                    linkDescargaAplicativoPfea = "";
                }
                userAgent = userAgent.toUpperCase();
                if(userAgent.contains("WINDOWS")) {
                    linkDescargaAplicativoPfea = configuracionSB.obtenerConfiguracionPorCodigos("SOFTWARE_PFEA_WINDOWS");
                }else if(userAgent.contains("LINUX")) {
                    linkDescargaAplicativoPfea = configuracionSB.obtenerConfiguracionPorCodigos("SOFTWARE_PFEA_LINUX");
                }else if(userAgent.contains("OS X")) {
                    //linkDescargaAplicativoPfea = configuracionSB.obtenerConfiguracionPorCodigos("SOFTWARE_PFEA_MAC");
                    linkDescargaAplicativoPfea = ""; //Mac no soportado
                }else {
                    linkDescargaAplicativoPfea = "";
                }
            }
            return linkDescargaAplicativoPfea;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(e.getMessage());
            throw ge;
        }
    }

    public Boolean getSoloMuestraFirma() {
        return soloMostrarFirma;
    }

    public String describirInstalacion() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String userAgent = request.getHeader("user-agent");
        String descInstalacion;
        userAgent = userAgent.toUpperCase();
        if (userAgent.contains("WINDOWS")) {
            descInstalacion = textMB.obtenerTexto("messages.instalacionWindows");
        } else if (userAgent.contains("LINUX")) {
            descInstalacion = textMB.obtenerTexto("messages.instalacionLinux");
        } else if (userAgent.contains("OS X")) {
            descInstalacion = textMB.obtenerTexto("messages.instalacionMac");
        } else {
            descInstalacion = "";
        }
        return descInstalacion;
    }
    
    public void mostrarInfoFirmas(Integer docId) {
        infoFirmas = firmaSB.obtenerInfoFirmas(docId);
        firmaValida = true;
        if(infoFirmas!=null) {
            for(InfoFirma info : infoFirmas) {
                firmaValida = firmaValida && info.isValida();
            }
        }
        RequestContext.getCurrentInstance().execute("$('#modalInfoFirmas').modal('show')");
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    public boolean isDocumentoCargado() {
        return documentoCargado;
    }

    public void setDocumentoCargado(boolean documentoCargado) {
        this.documentoCargado = documentoCargado;
    }

    public boolean isPermitirEditar() {
        return permitirEditar;
    }

    public void setPermitirEditar(boolean permitirEditar) {
        this.permitirEditar = permitirEditar;
    }

    public LazyDataModel getLazyModelVencidos() {
        return lazyModelVencidos;
    }

    public LazyDataModel getLazyModelVigentes() {
        return lazyModelVigentes;
    }

    public SessionBean getSesionMB() {
        return sesionMB;
    }

    public void setSesionMB(SessionBean sesionMB) {
        this.sesionMB = sesionMB;
    }

    public String getIdTtransaccion() {
        return idTtransaccion;
    }

    public void setIdTtransaccion(String idTtransaccion) {
        this.idTtransaccion = idTtransaccion;
    }

    public String getLinkDescargaAplicativoPfea() {
        return linkDescargaAplicativoPfea;
    }

    public void setLinkDescargaAplicativoPfea(String linkDescargaAplicativoPfea) {
        this.linkDescargaAplicativoPfea = linkDescargaAplicativoPfea;
    }
    
    public String getPasoWizard() {
        return pasoWizard;
    }

    public boolean isDescargado() {
        return descargado;
    }

    public void setDescargado(boolean descargado) {
        this.descargado = descargado;
    }

    public String getMetodoFirma() {
        return metodoFirma;
    }

    public void setMetodoFirma(String metodoFirma) {
        this.metodoFirma = metodoFirma;
    }

    public boolean existeArchivoFirmado() {
        Documento documento = documentosSB.obtenerDocumentoPorIdentificador(idTtransaccion, false);
        return documento != null && BooleanUtils.isTrue(documento.getFirmado());
    }

    public boolean stopPollFirma() {
        boolean existeArchivoFirmado = existeArchivoFirmado();
        if(existeArchivoFirmado) {
            pasoWizard = "CONFIRMACION";
        }
        return existeArchivoFirmado;
    }


    public StreamedContent descargarCert(String nombre, byte[] bytes) {
        InputStream stream = new ByteArrayInputStream(bytes);
        StreamedContent cert = new DefaultStreamedContent(stream, "application/x-pem-file", nombre+".cert");
        return cert;
    }

    public boolean isFirmaValida() {
        return firmaValida;
    }

    public List<InfoFirma> getInfoFirmas() {
        return infoFirmas;
    }

  
    public String getMensajeTitulo() {
        return mensajeTitulo;
    }

}
