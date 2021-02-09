/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "conPk", scope = SgConfiguracionFirmaElectronica.class)
public class SgConfiguracionFirmaElectronica implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long conPk;

    private String conCodigo;

    private String conNombre;
    
    private Boolean conActivada;

    private LocalDateTime conUltModFecha;

    private String conUltModUsuario;

    private Integer conVersion;
    

    public SgConfiguracionFirmaElectronica() {
    }


    public Long getConPk() {
        return conPk;
    }

    public void setConPk(Long conPk) {
        this.conPk = conPk;
    }

    public String getConCodigo() {
        return conCodigo;
    }

    public void setConCodigo(String conCodigo) {
        this.conCodigo = conCodigo;
    }

    public String getConNombre() {
        return conNombre;
    }

    public void setConNombre(String conNombre) {
        this.conNombre = conNombre;
    }


    public LocalDateTime getConUltModFecha() {
        return conUltModFecha;
    }

    public void setConUltModFecha(LocalDateTime conUltModFecha) {
        this.conUltModFecha = conUltModFecha;
    }

    public String getConUltModUsuario() {
        return conUltModUsuario;
    }

    public void setConUltModUsuario(String conUltModUsuario) {
        this.conUltModUsuario = conUltModUsuario;
    }

    public Integer getConVersion() {
        return conVersion;
    }

    public void setConVersion(Integer conVersion) {
        this.conVersion = conVersion;
    }

    public Boolean getConActivada() {
        return conActivada;
    }

    public void setConActivada(Boolean conActivada) {
        this.conActivada = conActivada;
    }
    
    

     
    @Override
    public int hashCode() {
        return Objects.hashCode(this.conPk);
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
        final SgConfiguracionFirmaElectronica other = (SgConfiguracionFirmaElectronica) obj;
        if (!Objects.equals(this.conPk, other.conPk)) {
            return false;
        }
        return true;
    }

 

}
