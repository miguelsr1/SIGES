package uy.com.sofis.pfea.mb;

import com.sofis.utils.UtilVarios;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import uy.com.sofis.pfea.binarios.PfeaFileUtils;
import uy.com.sofis.pfea.constantes.ConstantesErrores;
import uy.com.sofis.pfea.entities.Documento;
import uy.com.sofis.pfea.exceptions.BusinessException;
import uy.com.sofis.pfea.exceptions.TechnicalException;
import uy.com.sofis.pfea.sb.ConfiguracionSB;
import uy.com.sofis.pfea.sb.DocumentosSB;
import uy.com.sofis.pfea.sb.EntityManagementSB;

/**
 *
 * @author Sofis Solutions
 */

@Named
@ViewScoped
public class FirmaPublicaMB implements Serializable {

    private static final Logger logger = Logger.getLogger(BandejaMB.class.getName());

    @Inject
    private TextMB textMB;

    @Inject
    private SessionBean sesionMB;

    @Inject
    DocumentosSB documentosSB;

    @Inject
    ConfiguracionSB configuracionSB;

    @Inject
    EntityManagementSB entityManagementSB;

    private Documento documentoEnEdicion;
    private boolean documentoCargado = false;
    private String idTransaccion;
    private String linkDescargaAplicativoPfea;
    private Boolean instalado = false;
    private List<Documento> documenosFirmados = new ArrayList();

    private String pasoWizard = null;
    private String metodoFirma = null;
    private boolean descargado = false;

    private String mensajeTitulo = null;
    private boolean soloMostrarFirma = true;

    @PostConstruct
    public void init() {
        String idTransaccionFirmaDirecta = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id_transaccion");
        if (!StringUtils.isBlank(idTransaccionFirmaDirecta)) {
            iniciarFirmaDirecta(idTransaccionFirmaDirecta);
        }
    }

    private void iniciarFirmaDirecta(String idTransaccionFirmaDirecta) {

        idTransaccion = idTransaccionFirmaDirecta;
        documentoEnEdicion = documentosSB.obtenerDocumentoPorIdentificador(idTransaccionFirmaDirecta, true);

        if (documentoEnEdicion != null) {
            documentoCargado = true;
            pasoWizard = "APLICATIVO";
        } else {
            BusinessException ge = new BusinessException();
            ge.addError(ConstantesErrores.ERR_GNRAL_FIND_BY_PROPERTIES);
            throw ge;
//            
//            String msj = textMB.obtenerTexto("messages.documentoNoEncontrado");
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msj, msj));
        }
    }

    
    public String getSoftwarePfeaDownlodLink() {
        try {
            if (linkDescargaAplicativoPfea == null) {
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String userAgent = request.getHeader("user-agent");
                if (StringUtils.isBlank(userAgent)) {
                    linkDescargaAplicativoPfea = "";
                }
                userAgent = userAgent.toUpperCase();
                if (userAgent.contains("WINDOWS")) {
                    linkDescargaAplicativoPfea = configuracionSB.obtenerConfiguracionPorCodigos("SOFTWARE_PFEA_WINDOWS");
                } else if (userAgent.contains("LINUX")) {
                    linkDescargaAplicativoPfea = configuracionSB.obtenerConfiguracionPorCodigos("SOFTWARE_PFEA_LINUX");
                } else if (userAgent.contains("OS X")) {
                    //linkDescargaAplicativoPfea = configuracionSB.obtenerConfiguracionPorCodigos("SOFTWARE_PFEA_MAC");
                    linkDescargaAplicativoPfea = ""; //Mac no soportado
                } else {
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

    public String describirMetodoFirma() {
        if (StringUtils.isBlank(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionGeneral");
        }
        if ("CEDULA".equals(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionCedula");
        }
        if ("TOKEN_ABITAB".equals(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionTokenAbitab");
        }
        if ("TOKEN_CORREO".equals(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionTokenCorreo");
        }
        if ("NUBE_ABITAB".equals(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionNubeAbitab");
        }
        if ("ARCHIVO".equals(metodoFirma)) {
            return textMB.obtenerTexto("messages.metodoFirmaDescripcionArchivo");
        }
        return textMB.obtenerTexto("messages.metodoFirmaDescripcionGeneral");
    }

    public String describirInstalacion() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String userAgent = request.getHeader("user-agent");
        if (userAgent == null) {
            return "";
        }
        userAgent = userAgent.toUpperCase();
        if (userAgent.contains("WINDOWS")) {
            return textMB.obtenerTexto("messages.instalacionWindows");
        } else if (userAgent.contains("LINUX")) {
            return textMB.obtenerTexto("messages.instalacionLinux");
        } else if (userAgent.contains("OS X")) {
            return textMB.obtenerTexto("messages.instalacionMac");
        } else {
            return "";
        }
    }

    public String siguientePaso() {
        if (StringUtils.isBlank(pasoWizard)) {
            return null;
        }
        if ("METODO".equals(pasoWizard)) {
            if (StringUtils.isBlank(metodoFirma)) {
                String msj = textMB.obtenerTexto("messages.debeSeleccionarUnMetodoDeFirma");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msj, msj));
                return null;
            }
            if ("NUBE_ABITAB".equals(metodoFirma)) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FIRMANUBEABITAB_DOCIDENTIFICADOR", documentoEnEdicion.getIdentificador());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FIRMANUBEABITAB_TARGET", "webb");
                HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String urlScheme = httpRequest.getScheme();
                String urlServer = httpRequest.getServerName();
                String urlPort = "" + httpRequest.getServerPort();
                String urlContext = httpRequest.getContextPath();
                if (!urlContext.startsWith("/")) {
                    urlContext = "/" + urlContext;
                }
                if (!urlContext.endsWith("/")) {
                    urlContext = urlContext + "/";
                }
                String urlPaso1 = urlScheme + "://" + urlServer + ":" + urlPort + urlContext + "NubeAbitab1Servlet";
                RequestContext.getCurrentInstance().execute("document.location='" + urlPaso1 + "';");
                return null;
            }
            //Cualquier otro método que requiera el aplicativo
            pasoWizard = "APLICATIVO";
            return null;
        }
        if ("APLICATIVO".equals(pasoWizard)) {
            if (!descargado) {
                String msj = textMB.obtenerTexto("messages.debeDescargarEInstalarElAplicativoDeFirma");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msj, msj));
                FacesContext.getCurrentInstance().validationFailed(); //Para que no invoque la descarga del archivo
                return null;
            }
            pasoWizard = "FIRMA";
            return null;
        }
        if ("FIRMA".equals(pasoWizard)) {
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
            String rutaArchivoPfeaGenerado = PfeaFileUtils.PfeaArchiveMaker(idTransaccion, urlWs, urlUpdate, metodoFirma, urlCRL, enabledCRL, documentoEnEdicion.getDebeFirmarUsuario(), trustoreCAName, trustoreValidationEnabled);
            File file = new File(rutaArchivoPfeaGenerado);
            InputStream stream = new FileInputStream(file);
            return new DefaultStreamedContent(stream, "application/octet-stream", file.getName());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al descargar el archivo", ex);
        }
        return null;
    }

    public boolean stopPollFirma() {
        boolean existeArchivoFirmado = existeArchivoFirmado();
        if (existeArchivoFirmado) {
            pasoWizard = "CONFIRMACION";
        }
        return existeArchivoFirmado;
    }

    public String firmarNuevoArchivo() {
        return "/publico/firmaPublica?faces-redirect=true;";
    }

    //Si el documento contiene un solo archivo se devuelve tal cual, sino se devuelve un archivo zip contenido los archivos
    public StreamedContent getDocumentoFirmado() {
        try {
            List<byte[]> bytesList = documentosSB.obtenerBytesDocumento(idTransaccion, true);
            byte[] bytes = null;

            if (bytesList.isEmpty()) {
                logger.log(Level.SEVERE, "Error al descargar el archivo");
                return null;
            }

            Documento documento = documentosSB.obtenerDocumentoPorIdentificador(idTransaccion, true);

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

    public String cerrarWizard() {
        Documento documento = documentosSB.obtenerDocumentoPorIdentificador(idTransaccion, true);
        if (documento != null && BooleanUtils.isTrue(documento.getFirmado()) && documento.getRetornoNavegacion() != null) {
            try {
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.redirect(UtilVarios.addIdTransaccion(documento.getRetornoNavegacion(), idTransaccion));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.redirect(documento.getRetornoNavegacion());
            } catch (IOException e) {
                RequestContext.getCurrentInstance().execute("window.close();");
            } 
        }

        return null;
    }

    public String getNombreOriginal() {
        return documentoEnEdicion != null ? documentoEnEdicion.getNombreOriginal() : "";
    }

    public boolean isDocumentoCargado() {
        return documentoCargado;
    }

    public void setDocumentoCargado(boolean documentoCargado) {
        this.documentoCargado = documentoCargado;
    }

    public TextMB getTextMB() {
        return textMB;
    }

    public void setTextMB(TextMB textMB) {
        this.textMB = textMB;
    }

    public SessionBean getSesionMB() {
        return sesionMB;
    }

    public void setSesionMB(SessionBean sesionMB) {
        this.sesionMB = sesionMB;
    }

    public String getLinkDescargaAplicativoPfea() {
        return linkDescargaAplicativoPfea;
    }

    public void setLinkDescargaAplicativoPfea(String linkDescargaAplicativoPfea) {
        this.linkDescargaAplicativoPfea = linkDescargaAplicativoPfea;
    }

    public Boolean getInstalado() {
        return instalado;
    }

    public void setInstalado(Boolean instalado) {
        this.instalado = instalado;
    }

    public List<Documento> getDocumenosFirmados() {
        return documenosFirmados;
    }

    public String getPasoWizard() {
        return pasoWizard;
    }

    public void setDocumenosFirmados(List<Documento> documenosFirmados) {
        this.documenosFirmados = documenosFirmados;
    }

    public String getMetodoFirma() {
        return metodoFirma;
    }

    public void setMetodoFirma(String metodoFirma) {
        this.metodoFirma = metodoFirma;
    }

    public boolean existeArchivoFirmado() {
        Documento documento = documentosSB.obtenerDocumentoPorIdentificador(idTransaccion, true);
        return documento != null && BooleanUtils.isTrue(documento.getFirmado());
    }

    public boolean isDescargado() {
        return descargado;
    }

    public void setDescargado(boolean descargado) {
        this.descargado = descargado;
    }

    public String getMensajeTitulo() {
        return mensajeTitulo;
    }

    public Boolean getSoloMuestraFirma() {
        return soloMostrarFirma;
    }
}
