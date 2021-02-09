/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "ddePk", scope = SgAfDescargoDetalle.class)
public class SgAfDescargoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long ddePk;
    private LocalDateTime ddeUltModFecha;
    private String ddeUltModUsuario;
    private Integer ddeVersion;
    private SgAfBienDepreciable ddeBienesDepreciablesFk;
    private SgAfDescargo ddeDescargoFk;
    
    public SgAfDescargoDetalle() {
    }

    public Long getDdePk() {
        return ddePk;
    }

    public void setDdePk(Long ddePk) {
        this.ddePk = ddePk;
    }

    public LocalDateTime getDdeUltModFecha() {
        return ddeUltModFecha;
    }

    public void setDdeUltModFecha(LocalDateTime ddeUltModFecha) {
        this.ddeUltModFecha = ddeUltModFecha;
    }

    public String getDdeUltModUsuario() {
        return ddeUltModUsuario;
    }

    public void setDdeUltModUsuario(String ddeUltModUsuario) {
        this.ddeUltModUsuario = ddeUltModUsuario;
    }

    public Integer getDdeVersion() {
        return ddeVersion;
    }

    public void setDdeVersion(Integer ddeVersion) {
        this.ddeVersion = ddeVersion;
    }

    public SgAfBienDepreciable getDdeBienesDepreciablesFk() {
        return ddeBienesDepreciablesFk;
    }

    public void setDdeBienesDepreciablesFk(SgAfBienDepreciable ddeBienesDepreciablesFk) {
        this.ddeBienesDepreciablesFk = ddeBienesDepreciablesFk;
    }

    public SgAfDescargo getDdeDescargoFk() {
        return ddeDescargoFk;
    }

    public void setDdeDescargoFk(SgAfDescargo ddeDescargoFk) {
        this.ddeDescargoFk = ddeDescargoFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ddePk != null ? ddePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfDescargoDetalle)) {
            return false;
        }
        SgAfDescargoDetalle other = (SgAfDescargoDetalle) object;
        if ((this.ddePk == null && other.ddePk != null) || (this.ddePk != null && !this.ddePk.equals(other.ddePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfDescargosDetalle[ ddePk=" + ddePk + " ]";
    }
    
}
