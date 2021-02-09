/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tmdPk", scope = SgInfTipoManejoDesechos.class)
public class SgInfTipoManejoDesechos implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tmdPk;

    private String tmdCodigo;

    private String tmdNombre;

    private Boolean tmdHabilitado;

    private LocalDateTime tmdUltModFecha;

    private String tmdUltModUsuario;

    private Integer tmdVersion;
    
    private Integer tmdOrden;
    
    private Boolean tmdAplicaOtros;  
    
    
    public SgInfTipoManejoDesechos() {
        this.tmdHabilitado = Boolean.TRUE;
    }

    public Long getTmdPk() {
        return tmdPk;
    }

    public void setTmdPk(Long tmdPk) {
        this.tmdPk = tmdPk;
    }

    public String getTmdCodigo() {
        return tmdCodigo;
    }

    public void setTmdCodigo(String tmdCodigo) {
        this.tmdCodigo = tmdCodigo;
    }

    public String getTmdNombre() {
        return tmdNombre;
    }

    public void setTmdNombre(String tmdNombre) {
        this.tmdNombre = tmdNombre;
    }

    public LocalDateTime getTmdUltModFecha() {
        return tmdUltModFecha;
    }

    public void setTmdUltModFecha(LocalDateTime tmdUltModFecha) {
        this.tmdUltModFecha = tmdUltModFecha;
    }

    public String getTmdUltModUsuario() {
        return tmdUltModUsuario;
    }

    public void setTmdUltModUsuario(String tmdUltModUsuario) {
        this.tmdUltModUsuario = tmdUltModUsuario;
    }

    public Integer getTmdVersion() {
        return tmdVersion;
    }

    public void setTmdVersion(Integer tmdVersion) {
        this.tmdVersion = tmdVersion;
    }

    
     public Boolean getTmdHabilitado() {
        return tmdHabilitado;
    }

    public void setTmdHabilitado(Boolean tmdHabilitado) {
        this.tmdHabilitado = tmdHabilitado;
    }

    public Integer getTmdOrden() {
        return tmdOrden;
    }

    public void setTmdOrden(Integer tmdOrden) {
        this.tmdOrden = tmdOrden;
    }

    public Boolean getTmdAplicaOtros() {
        return tmdAplicaOtros;
    }

    public void setTmdAplicaOtros(Boolean tmdAplicaOtros) {
        this.tmdAplicaOtros = tmdAplicaOtros;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tmdPk != null ? tmdPk.hashCode() : 0);
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
        final SgInfTipoManejoDesechos other = (SgInfTipoManejoDesechos) obj;
        if (!Objects.equals(this.tmdPk, other.tmdPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgInfTipoManejoDesechos[ tmdPk=" + tmdPk + " ]";
    }
    
}
