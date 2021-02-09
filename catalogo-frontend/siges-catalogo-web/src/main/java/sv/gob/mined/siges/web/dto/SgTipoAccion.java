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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tacPk", scope = SgTipoAccion.class)
public class SgTipoAccion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tacPk;

    private String tacCodigo;

    private String tacNombre;

    private Boolean tacHabilitado;
    
    private Boolean tacNecesitaDescripcion;

    private LocalDateTime tacUltModFecha;

    private String tacUltModUsuario;

    private Integer tacVersion;
    
    
    public SgTipoAccion() {
        this.tacHabilitado = Boolean.TRUE;
        this.tacNecesitaDescripcion = Boolean.FALSE;
    }

    public Long getTacPk() {
        return tacPk;
    }

    public void setTacPk(Long tacPk) {
        this.tacPk = tacPk;
    }

    public String getTacCodigo() {
        return tacCodigo;
    }

    public void setTacCodigo(String tacCodigo) {
        this.tacCodigo = tacCodigo;
    }

    public String getTacNombre() {
        return tacNombre;
    }

    public void setTacNombre(String tacNombre) {
        this.tacNombre = tacNombre;
    }

    public LocalDateTime getTacUltModFecha() {
        return tacUltModFecha;
    }

    public void setTacUltModFecha(LocalDateTime tacUltModFecha) {
        this.tacUltModFecha = tacUltModFecha;
    }

    public String getTacUltModUsuario() {
        return tacUltModUsuario;
    }

    public void setTacUltModUsuario(String tacUltModUsuario) {
        this.tacUltModUsuario = tacUltModUsuario;
    }

    public Integer getTacVersion() {
        return tacVersion;
    }

    public void setTacVersion(Integer tacVersion) {
        this.tacVersion = tacVersion;
    }

    
     public Boolean getTacHabilitado() {
        return tacHabilitado;
    }

    public void setTacHabilitado(Boolean tacHabilitado) {
        this.tacHabilitado = tacHabilitado;
    }

    public Boolean getTacNecesitaDescripcion() {
        return tacNecesitaDescripcion;
    }

    public void setTacNecesitaDescripcion(Boolean tacNecesitaDescripcion) {
        this.tacNecesitaDescripcion = tacNecesitaDescripcion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tacPk != null ? tacPk.hashCode() : 0);
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
        final SgTipoAccion other = (SgTipoAccion) obj;
        if (!Objects.equals(this.tacPk, other.tacPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoAccion[ tacPk=" + tacPk + " ]";
    }
    
}
