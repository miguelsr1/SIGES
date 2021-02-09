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

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "ivuPk", scope = SgInmueblesVulnerabilidades.class)
public class SgInmueblesVulnerabilidades implements Serializable {
 
    private static final long serialVersionUID = 1L;
    private Long ivuPk;
    private SgInmueblesSedes ivuInmueblesSedesFK;
    private SgVulnerabilidades ivuVulnerabilidadesFk;
    private String ivuUltModUsuario;
    private LocalDateTime ivuUltModFecha;
    private Integer ivuVersion;
    
    public Long getIvuPk() {
        return ivuPk;
    }

    public void setIvuPk(Long ivuPk) {
        this.ivuPk = ivuPk;
    }

    public SgInmueblesSedes getIvuInmueblesSedesFK() {
        return ivuInmueblesSedesFK;
    }

    public void setIvuInmueblesSedesFK(SgInmueblesSedes ivuInmueblesSedesFK) {
        this.ivuInmueblesSedesFK = ivuInmueblesSedesFK;
    }

    public SgVulnerabilidades getIvuVulnerabilidadesFk() {
        return ivuVulnerabilidadesFk;
    }

    public void setIvuVulnerabilidadesFk(SgVulnerabilidades ivuVulnerabilidadesFk) {
        this.ivuVulnerabilidadesFk = ivuVulnerabilidadesFk;
    }

    public String getIvuUltModUsuario() {
        return ivuUltModUsuario;
    }

    public void setIvuUltModUsuario(String ivuUltModUsuario) {
        this.ivuUltModUsuario = ivuUltModUsuario;
    }

    public LocalDateTime getIvuUltModFecha() {
        return ivuUltModFecha;
    }

    public void setIvuUltModFecha(LocalDateTime ivuUltModFecha) {
        this.ivuUltModFecha = ivuUltModFecha;
    }

    public Integer getIvuVersion() {
        return ivuVersion;
    }

    public void setIvuVersion(Integer ivuVersion) {
        this.ivuVersion = ivuVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.ivuPk);
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
        final SgInmueblesVulnerabilidades other = (SgInmueblesVulnerabilidades) obj;
        if (!Objects.equals(this.ivuPk, other.ivuPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgInmueblesVulnerabilidades[ ivuPk=" + ivuPk + " ]";
    }

  
    
}

