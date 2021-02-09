/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.mb;

import com.sofis.utils.UtilVarios;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.agesic.firma.datatypes.InfoFirma;
import org.apache.commons.io.IOUtils;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import uy.com.sofis.pfea.constantes.ConstantesErrores;
import uy.com.sofis.pfea.entities.Documento;
import uy.com.sofis.pfea.enums.EstadoDocumentoBandeja;
import uy.com.sofis.pfea.exceptions.TechnicalException;
import uy.com.sofis.pfea.sb.ConfiguracionSB;
import uy.com.sofis.pfea.sb.DocumentosSB;
import uy.com.sofis.pfea.sb.EntityManagementSB;
import uy.com.sofis.pfea.sb.FirmaSB;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ValidarFirmaPublicaMB implements Serializable {

    private static final Logger logger = Logger.getLogger(ValidarFirmaPublicaMB.class.getName());

    @Inject
    private TextMB textMB;

    @Inject
    private SessionBean sesionMB;

    @Inject
    DocumentosSB documentosSB;

    @Inject
    ConfiguracionSB configuracionSB;

    @Inject
    FirmaSB firmaSB;
    
    
    @Inject
    EntityManagementSB entityManagementSB;

    private Documento documentoEnEdicion;
    private boolean documentoCargado = false;
    private List<InfoFirma> infoFirmas;
    private boolean firmaValida = true;
    
    @PostConstruct
    public void init() {
    }

    public void cargarDocumento(FileUploadEvent event) {
        try {
            UploadedFile archivo = event.getFile();
            if (archivo.getSize() < 1) {
                String msj = textMB.obtenerTexto("messages.archivoNoSeleccionadoOVacio");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msj, msj));
                return;
            }
            if (!"application/pdf".equals(archivo.getContentType())) {
                String msj = textMB.obtenerTexto("messages.documentoNoEsPdf");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msj, msj));
                return;
            }
            documentoEnEdicion = new Documento();
            documentoEnEdicion.setNombreOriginal(archivo.getFileName());

            List<byte[]> docBytesList = new ArrayList<>();
            docBytesList.add(IOUtils.toByteArray(archivo.getInputstream()));
            documentoEnEdicion.setDocumentoBytes(docBytesList);

            documentoEnEdicion.setFechaCreacion(new Date());
            documentoEnEdicion.setFechaExpiracion(UtilVarios.sumarDiasFecha(new Date(), 10));
            //El identificador y el nombre se asignan solo si se confirma la subida del documento
            documentoEnEdicion.setIdentificador(null);
            documentoEnEdicion.setNombreArchivo(null);
            documentoEnEdicion.setEstadoDocumento(EstadoDocumentoBandeja.FI);
            documentoEnEdicion.setFirmado(true);
            documentoEnEdicion.setEliminado(Boolean.FALSE);
            documentoEnEdicion.setUltimoUsuario("PFEA-ANONIMO");
            documentoEnEdicion.setDescripcion(documentoEnEdicion.getNombreOriginal());
            documentoEnEdicion.setOrigen("WEB");
            documentoCargado = true;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void subirArchivo() {
        try {
            if (documentoEnEdicion == null) {
                String msj = "El documento no es válido, debe seleccionar un archivo";
                FacesContext.getCurrentInstance().addMessage("messageError", new FacesMessage(FacesMessage.SEVERITY_ERROR, msj, msj));
                return;
            }
            documentoEnEdicion.setIdentificador(UUID.randomUUID().toString());
            documentoEnEdicion.setNombreArchivo(documentoEnEdicion.getIdentificador() + ".pdf");
            Documento documentoCreado = documentosSB.crearDocumento(documentoEnEdicion);
            infoFirmas = firmaSB.obtenerInfoFirmas(documentoCreado.getId());
            firmaValida = true;
            if(infoFirmas!=null) {
                for(InfoFirma info : infoFirmas) {
                    firmaValida = firmaValida && info.isValida();
                }
            }
            RequestContext.getCurrentInstance().execute("$('#modalInfoFirmas').modal('show')");                
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(e.getMessage());
            throw ge;
        }
    }
 
    public String validarNuevoArchivo() {
        return "/publico/validarFirmaPublica?faces-redirect=true;";
    }

    public String cerrarWizard() {
        RequestContext.getCurrentInstance().execute("$('#modalInfoFirmas').modal('hide')");
        return null;
    }
    
    public StreamedContent descargarCert(String nombre, byte[] bytes) {
        InputStream stream = new ByteArrayInputStream(bytes);
        StreamedContent cert = new DefaultStreamedContent(stream, "application/x-pem-file", nombre+".cert");
        return cert;
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

    public List<InfoFirma> getInfoFirmas() {
        return infoFirmas;
    }

    public boolean isFirmaValida() {
        return firmaValida;
    }

}
