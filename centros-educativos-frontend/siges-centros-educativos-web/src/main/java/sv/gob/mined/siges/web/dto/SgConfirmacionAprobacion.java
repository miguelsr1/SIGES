/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cprPk", scope = SgConfirmacionAprobacion.class)
public class SgConfirmacionAprobacion implements Serializable {

    private Long cprPk;

    private LocalDateTime cprFechaConfirmacion;
    
    private String cprUsuarioConfirmacion;
    
    private SgSeccion cprSeccion;
    
    private SgComponentePlanEstudio cprComponentePlanEstudio;
    
    private LocalDateTime cprUltModFecha;

    private String cprUltModUsuario;

    private Integer cprVersion;
    
    private SgArchivo cprArchivoFirmado;
    
    private Boolean cprFirmada;
    
    private String cprFirmaTransactionId; // Transaction id
    
    
    //Auxiliares firma
    
    private String cprTransactionSignatureUrl; // Url a donde redirigir para firmar
    
    private String cprTransactionReturnUrl; // Url a donde redirigir luego de firma
    

    public SgConfirmacionAprobacion() {
    }

    public Long getCprPk() {
        return cprPk;
    }

    public void setCprPk(Long cprPk) {
        this.cprPk = cprPk;
    }

    public LocalDateTime getCprFechaConfirmacion() {
        return cprFechaConfirmacion;
    }

    public void setCprFechaConfirmacion(LocalDateTime cprFechaConfirmacion) {
        this.cprFechaConfirmacion = cprFechaConfirmacion;
    }

    public String getCprUsuarioConfirmacion() {
        return cprUsuarioConfirmacion;
    }

    public void setCprUsuarioConfirmacion(String cprUsuarioConfirmacion) {
        this.cprUsuarioConfirmacion = cprUsuarioConfirmacion;
    }

    public SgSeccion getCprSeccion() {
        return cprSeccion;
    }

    public void setCprSeccion(SgSeccion cprSeccion) {
        this.cprSeccion = cprSeccion;
    }

    public LocalDateTime getCprUltModFecha() {
        return cprUltModFecha;
    }

    public void setCprUltModFecha(LocalDateTime cprUltModFecha) {
        this.cprUltModFecha = cprUltModFecha;
    }

    public String getCprUltModUsuario() {
        return cprUltModUsuario;
    }

    public void setCprUltModUsuario(String cprUltModUsuario) {
        this.cprUltModUsuario = cprUltModUsuario;
    }

    public Integer getCprVersion() {
        return cprVersion;
    }

    public void setCprVersion(Integer cprVersion) {
        this.cprVersion = cprVersion;
    }

    public SgArchivo getCprArchivoFirmado() {
        return cprArchivoFirmado;
    }

    public void setCprArchivoFirmado(SgArchivo cprArchivoFirmado) {
        this.cprArchivoFirmado = cprArchivoFirmado;
    }  

    public Boolean getCprFirmada() {
        return cprFirmada;
    }

    public void setCprFirmada(Boolean cprFirmada) {
        this.cprFirmada = cprFirmada;
    }

    public String getCprFirmaTransactionId() {
        return cprFirmaTransactionId;
    }

    public void setCprFirmaTransactionId(String cprFirmaTransactionId) {
        this.cprFirmaTransactionId = cprFirmaTransactionId;
    }

    public String getCprTransactionSignatureUrl() {
        return cprTransactionSignatureUrl;
    }

    public void setCprTransactionSignatureUrl(String cprTransactionSignatureUrl) {
        this.cprTransactionSignatureUrl = cprTransactionSignatureUrl;
    }

    public String getCprTransactionReturnUrl() {
        return cprTransactionReturnUrl;
    }

    public void setCprTransactionReturnUrl(String cprTransactionReturnUrl) {
        this.cprTransactionReturnUrl = cprTransactionReturnUrl;
    }

    public SgComponentePlanEstudio getCprComponentePlanEstudio() {
        return cprComponentePlanEstudio;
    }

    public void setCprComponentePlanEstudio(SgComponentePlanEstudio cprComponentePlanEstudio) {
        this.cprComponentePlanEstudio = cprComponentePlanEstudio;
    }
    
    

    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cprPk);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgConfirmacionAprobacion other = (SgConfirmacionAprobacion) obj;
        if (!Objects.equals(this.cprPk, other.cprPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgConfirmacionAprobacion{" + "cprPk=" + cprPk + '}';
    }

    
    
    

}
