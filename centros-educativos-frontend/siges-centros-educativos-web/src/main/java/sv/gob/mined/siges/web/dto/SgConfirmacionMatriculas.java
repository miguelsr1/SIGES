/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cmaPk", scope = SgConfirmacionMatriculas.class)
public class SgConfirmacionMatriculas implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long cmaPk;

    private LocalDateTime cmaFechaConfirmacion;
    
    private String cmaUsuarioConfirmacion;
    
    private SgSede cmaSede;
    
    private SgAnioLectivo cmaAnioLectivo;

    private LocalDateTime cmaUltModFecha;

    private String cmaUltModUsuario;

    private Integer cmaVersion;
    
    private SgArchivo cmaArhivoFirmado;
    
    private Boolean cmaFirmada;
    
    private String cmaFirmaTransactionId; // Transaction id
    
    
    //Auxiliares firma
    
    private String cmaTransactionSignatureUrl;
    
    private String cmaTransactionReturnUrl;
    
    
    
    public SgConfirmacionMatriculas() {
    }

    public Long getCmaPk() {
        return cmaPk;
    }

    public void setCmaPk(Long cmaPk) {
        this.cmaPk = cmaPk;
    }


    public LocalDateTime getCmaUltModFecha() {
        return cmaUltModFecha;
    }

    public void setCmaUltModFecha(LocalDateTime cmaUltModFecha) {
        this.cmaUltModFecha = cmaUltModFecha;
    }

    public String getCmaUltModUsuario() {
        return cmaUltModUsuario;
    }

    public void setCmaUltModUsuario(String cmaUltModUsuario) {
        this.cmaUltModUsuario = cmaUltModUsuario;
    }

    public Integer getCmaVersion() {
        return cmaVersion;
    }

    public void setCmaVersion(Integer cmaVersion) {
        this.cmaVersion = cmaVersion;
    }

    public String getCmaUsuarioConfirmacion() {
        return cmaUsuarioConfirmacion;
    }

    public void setCmaUsuarioConfirmacion(String cmaUsuarioConfirmacion) {
        this.cmaUsuarioConfirmacion = cmaUsuarioConfirmacion;
    }

    public SgSede getCmaSede() {
        return cmaSede;
    }

    public void setCmaSede(SgSede cmaSede) {
        this.cmaSede = cmaSede;
    }

    public SgAnioLectivo getCmaAnioLectivo() {
        return cmaAnioLectivo;
    }

    public void setCmaAnioLectivo(SgAnioLectivo cmaAnioLectivo) {
        this.cmaAnioLectivo = cmaAnioLectivo;
    }

    public LocalDateTime getCmaFechaConfirmacion() {
        return cmaFechaConfirmacion;
    }

    public void setCmaFechaConfirmacion(LocalDateTime cmaFechaConfirmacion) {
        this.cmaFechaConfirmacion = cmaFechaConfirmacion;
    }

    public String getCmaTransactionSignatureUrl() {
        return cmaTransactionSignatureUrl;
    }

    public void setCmaTransactionSignatureUrl(String cmaTransactionSignatureUrl) {
        this.cmaTransactionSignatureUrl = cmaTransactionSignatureUrl;
    }

    public String getCmaTransactionReturnUrl() {
        return cmaTransactionReturnUrl;
    }

    public void setCmaTransactionReturnUrl(String cmaTransactionReturnUrl) {
        this.cmaTransactionReturnUrl = cmaTransactionReturnUrl;
    }

    public Boolean getCmaFirmada() {
        return cmaFirmada;
    }

    public void setCmaFirmada(Boolean cmaFirmada) {
        this.cmaFirmada = cmaFirmada;
    }

    public String getCmaFirmaTransactionId() {
        return cmaFirmaTransactionId;
    }

    public void setCmaFirmaTransactionId(String cmaFirmaTransactionId) {
        this.cmaFirmaTransactionId = cmaFirmaTransactionId;
    }

 

    public SgArchivo getCmaArhivoFirmado() {
        return cmaArhivoFirmado;
    }

    public void setCmaArhivoFirmado(SgArchivo cmaArhivoFirmado) {
        this.cmaArhivoFirmado = cmaArhivoFirmado;
    }

     
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cmaPk != null ? cmaPk.hashCode() : 0);
        return hash;
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
        final SgConfirmacionMatriculas other = (SgConfirmacionMatriculas) obj;
        if (!Objects.equals(this.cmaPk, other.cmaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgConfirmacionMatriculas[ cmaPk=" + cmaPk + " ]";
    }
    
}
