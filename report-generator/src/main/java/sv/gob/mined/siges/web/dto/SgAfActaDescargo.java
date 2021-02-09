/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "adePk", scope = SgAfActaDescargo.class)
public class SgAfActaDescargo implements Serializable {

    private Long adePk;
    private String adeNumeroAcuerdo; 
    private String adeSeAutoriza;
    private LocalDate adeFechaAcuerdo;
    private LocalDateTime adeUltModFecha;
    private String adeUltModUsuario;
    private Integer adeVersion;

    public SgAfActaDescargo() {
    }
    
    
    public Long getAdePk() {
        return adePk;
    }

    public void setAdePk(Long adePk) {
        this.adePk = adePk;
    }

    public String getAdeNumeroAcuerdo() {
        return adeNumeroAcuerdo;
    }

    public void setAdeNumeroAcuerdo(String adeNumeroAcuerdo) {
        this.adeNumeroAcuerdo = adeNumeroAcuerdo;
    }

    public String getAdeSeAutoriza() {
        return adeSeAutoriza;
    }

    public void setAdeSeAutoriza(String adeSeAutoriza) {
        this.adeSeAutoriza = adeSeAutoriza;
    }

    public LocalDate getAdeFechaAcuerdo() {
        return adeFechaAcuerdo;
    }

    public void setAdeFechaAcuerdo(LocalDate adeFechaAcuerdo) {
        this.adeFechaAcuerdo = adeFechaAcuerdo;
    }

    public LocalDateTime getAdeUltModFecha() {
        return adeUltModFecha;
    }

    public void setAdeUltModFecha(LocalDateTime adeUltModFecha) {
        this.adeUltModFecha = adeUltModFecha;
    }

    public String getAdeUltModUsuario() {
        return adeUltModUsuario;
    }

    public void setAdeUltModUsuario(String adeUltModUsuario) {
        this.adeUltModUsuario = adeUltModUsuario;
    }

   

    public Integer getAdeVersion() {
        return adeVersion;
    }

    public void setAdeVersion(Integer adeVersion) {
        this.adeVersion = adeVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.adePk);
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
        final SgAfActaDescargo other = (SgAfActaDescargo) obj;
        if (!Objects.equals(this.adePk, other.adePk)) {
            return false;
        }
        return true;
    }

    
}