/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mipPk", scope = SgMotivoInasistenciaPersonal.class)
public class SgMotivoInasistenciaPersonal implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mipPk;

    private String mipCodigo;

    private String mipNombre;

    private Boolean mipHabilitado;
    
    private Boolean mipMotivoJustificado;

    private LocalDateTime mipUltModFecha;

    private String mipUltModUsuario;

    private Integer mipVersion;
    
    private List<SgCargo> mipCargos;
    
    
    public SgMotivoInasistenciaPersonal() {
        this.mipHabilitado = Boolean.TRUE;
        this.mipMotivoJustificado = Boolean.FALSE;
    }

    public Long getMipPk() {
        return mipPk;
    }

    public void setMipPk(Long mipPk) {
        this.mipPk = mipPk;
    }

    public String getMipCodigo() {
        return mipCodigo;
    }

    public void setMipCodigo(String mipCodigo) {
        this.mipCodigo = mipCodigo;
    }

    public String getMipNombre() {
        return mipNombre;
    }

    public void setMipNombre(String mipNombre) {
        this.mipNombre = mipNombre;
    }

    public LocalDateTime getMipUltModFecha() {
        return mipUltModFecha;
    }

    public void setMipUltModFecha(LocalDateTime mipUltModFecha) {
        this.mipUltModFecha = mipUltModFecha;
    }

    public String getMipUltModUsuario() {
        return mipUltModUsuario;
    }

    public void setMipUltModUsuario(String mipUltModUsuario) {
        this.mipUltModUsuario = mipUltModUsuario;
    }

    public Integer getMipVersion() {
        return mipVersion;
    }

    public void setMipVersion(Integer mipVersion) {
        this.mipVersion = mipVersion;
    }

    
     public Boolean getMipHabilitado() {
        return mipHabilitado;
    }

    public void setMipHabilitado(Boolean mipHabilitado) {
        this.mipHabilitado = mipHabilitado;
    }

    public Boolean getMipMotivoJustificado() {
        return mipMotivoJustificado;
    }

    public void setMipMotivoJustificado(Boolean mipMotivoJustificado) {
        this.mipMotivoJustificado = mipMotivoJustificado;
    }

    public List<SgCargo> getMipCargos() {
        return mipCargos;
    }

    public void setMipCargos(List<SgCargo> mipCargos) {
        this.mipCargos = mipCargos;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mipPk != null ? mipPk.hashCode() : 0);
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
        final SgMotivoInasistenciaPersonal other = (SgMotivoInasistenciaPersonal) obj;
        if (!Objects.equals(this.mipPk, other.mipPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMotivoInasistenciaPersonal[ mipPk=" + mipPk + " ]";
    }
    
}
