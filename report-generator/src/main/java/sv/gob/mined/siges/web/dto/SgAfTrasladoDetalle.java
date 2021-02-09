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

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tdePk", scope = SgAfTrasladoDetalle.class)
public class SgAfTrasladoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tdePk;
    private LocalDateTime tdeUltModFecha;
    private String tdeUltModUsuario;
    private Integer tdeVersion;
    private SgAfBienDepreciable tdeBienesDepreciablesFk;
    private SgAfTraslado tdeTrasladoFk;
    private Boolean tdeRechazado;
    private String tdeObservacion;
    
    public SgAfTrasladoDetalle() {
    }

    public SgAfTrasladoDetalle(Long tdePk) {
        this.tdePk = tdePk;
    }

    public Long getTdePk() {
        return tdePk;
    }

    public void setTdePk(Long tdePk) {
        this.tdePk = tdePk;
    }

    public LocalDateTime getTdeUltModFecha() {
        return tdeUltModFecha;
    }

    public void setTdeUltModFecha(LocalDateTime tdeUltModFecha) {
        this.tdeUltModFecha = tdeUltModFecha;
    }

    public String getTdeUltModUsuario() {
        return tdeUltModUsuario;
    }

    public void setTdeUltModUsuario(String tdeUltModUsuario) {
        this.tdeUltModUsuario = tdeUltModUsuario;
    }

    public Integer getTdeVersion() {
        return tdeVersion;
    }

    public void setTdeVersion(Integer tdeVersion) {
        this.tdeVersion = tdeVersion;
    }

    public SgAfBienDepreciable getTdeBienesDepreciablesFk() {
        return tdeBienesDepreciablesFk;
    }

    public void setTdeBienesDepreciablesFk(SgAfBienDepreciable tdeBienesDepreciablesFk) {
        this.tdeBienesDepreciablesFk = tdeBienesDepreciablesFk;
    }

    public SgAfTraslado getTdeTrasladoFk() {
        return tdeTrasladoFk;
    }

    public void setTdeTrasladoFk(SgAfTraslado tdeTrasladoFk) {
        this.tdeTrasladoFk = tdeTrasladoFk;
    }

    public Boolean getTdeRechazado() {
        return tdeRechazado;
    }

    public void setTdeRechazado(Boolean tdeRechazado) {
        this.tdeRechazado = tdeRechazado;
    }

    public String getTdeObservacion() {
        return tdeObservacion;
    }

    public void setTdeObservacion(String tdeObservacion) {
        this.tdeObservacion = tdeObservacion;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tdePk != null ? tdePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfTrasladoDetalle)) {
            return false;
        }
        SgAfTrasladoDetalle other = (SgAfTrasladoDetalle) object;
        if ((this.tdePk == null && other.tdePk != null) || (this.tdePk != null && !this.tdePk.equals(other.tdePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfTrasladosDetalle[ tdePk=" + tdePk + " ]";
    }
    
}
