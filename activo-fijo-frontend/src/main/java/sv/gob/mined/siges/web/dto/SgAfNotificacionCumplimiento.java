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
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "ncuId", scope = SgAfNotificacionCumplimiento.class)
public class SgAfNotificacionCumplimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ncuId;
    private Boolean ncuLeida;
    private short ncuNumeroNotificacion;
    private SgArchivo ncuDocumento;
    private Integer ncuAnio;
    private SgSede ncuSedeFk;
    private SgAfUnidadesAdministrativas ncuUnidadAdministrativaFk;
    private LocalDateTime ncuUltModFecha;
    private String ncuUltModUsuario;
    private Integer ncuVersion;

    public SgAfNotificacionCumplimiento() {
    }

    public Long getNcuId() {
        return ncuId;
    }

    public void setNcuId(Long ncuId) {
        this.ncuId = ncuId;
    }

    public Boolean getNcuLeida() {
        return ncuLeida;
    }

    public void setNcuLeida(Boolean ncuLeida) {
        this.ncuLeida = ncuLeida;
    }

    public short getNcuNumeroNotificacion() {
        return ncuNumeroNotificacion;
    }

    public void setNcuNumeroNotificacion(short ncuNumeroNotificacion) {
        this.ncuNumeroNotificacion = ncuNumeroNotificacion;
    }

    public SgArchivo getNcuDocumento() {
        return ncuDocumento;
    }

    public void setNcuDocumento(SgArchivo ncuDocumento) {
        this.ncuDocumento = ncuDocumento;
    }
    
    public Integer getNcuAnio() {
        return ncuAnio;
    }

    public void setNcuAnio(Integer ncuAnio) {
        this.ncuAnio = ncuAnio;
    }

    public SgSede getNcuSedeFk() {
        return ncuSedeFk;
    }

    public void setNcuSedeFk(SgSede ncuSedeFk) {
        this.ncuSedeFk = ncuSedeFk;
    }

    public SgAfUnidadesAdministrativas getNcuUnidadAdministrativaFk() {
        return ncuUnidadAdministrativaFk;
    }

    public void setNcuUnidadAdministrativaFk(SgAfUnidadesAdministrativas ncuUnidadAdministrativaFk) {
        this.ncuUnidadAdministrativaFk = ncuUnidadAdministrativaFk;
    }

    public LocalDateTime getNcuUltModFecha() {
        return ncuUltModFecha;
    }

    public void setNcuUltModFecha(LocalDateTime ncuUltModFecha) {
        this.ncuUltModFecha = ncuUltModFecha;
    }

    public String getNcuUltModUsuario() {
        return ncuUltModUsuario;
    }

    public void setNcuUltModUsuario(String ncuUltModUsuario) {
        this.ncuUltModUsuario = ncuUltModUsuario;
    }

    public Integer getNcuVersion() {
        return ncuVersion;
    }

    public void setNcuVersion(Integer ncuVersion) {
        this.ncuVersion = ncuVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ncuId != null ? ncuId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfNotificacionCumplimiento)) {
            return false;
        }
        SgAfNotificacionCumplimiento other = (SgAfNotificacionCumplimiento) object;
        if ((this.ncuId == null && other.ncuId != null) || (this.ncuId != null && !this.ncuId.equals(other.ncuId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfNotificacionesCumplimiento[ ncuId=" + ncuId + " ]";
    }
    
}
