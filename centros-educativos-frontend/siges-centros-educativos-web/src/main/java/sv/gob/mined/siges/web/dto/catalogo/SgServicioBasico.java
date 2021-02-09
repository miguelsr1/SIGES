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
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sbaPk", scope = SgServicioBasico.class)
public class SgServicioBasico implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long sbaPk;

    private String sbaCodigo;

    private String sbaNombre;

    private Boolean sbaHabilitado;

    private Boolean sbaAplicaTerreno;   

    private Boolean sbaAplicaEdificio;    

    private Boolean sbaAplicaAula;    
 
    private String sbaDescripcion;
    
    private LocalDateTime sbaUltModFecha;

    private String sbaUltModUsuario;

    private Integer sbaVersion;
    
    
    public SgServicioBasico() {
        this.sbaHabilitado = Boolean.TRUE;
    }

    public Long getSbaPk() {
        return sbaPk;
    }

    public void setSbaPk(Long sbaPk) {
        this.sbaPk = sbaPk;
    }

    public String getSbaCodigo() {
        return sbaCodigo;
    }

    public void setSbaCodigo(String sbaCodigo) {
        this.sbaCodigo = sbaCodigo;
    }

    public String getSbaNombre() {
        return sbaNombre;
    }

    public void setSbaNombre(String sbaNombre) {
        this.sbaNombre = sbaNombre;
    }

    public LocalDateTime getSbaUltModFecha() {
        return sbaUltModFecha;
    }

    public void setSbaUltModFecha(LocalDateTime sbaUltModFecha) {
        this.sbaUltModFecha = sbaUltModFecha;
    }

    public String getSbaUltModUsuario() {
        return sbaUltModUsuario;
    }

    public void setSbaUltModUsuario(String sbaUltModUsuario) {
        this.sbaUltModUsuario = sbaUltModUsuario;
    }

    public Integer getSbaVersion() {
        return sbaVersion;
    }

    public void setSbaVersion(Integer sbaVersion) {
        this.sbaVersion = sbaVersion;
    }

    
     public Boolean getSbaHabilitado() {
        return sbaHabilitado;
    }

    public void setSbaHabilitado(Boolean sbaHabilitado) {
        this.sbaHabilitado = sbaHabilitado;
    }

    public Boolean getSbaAplicaTerreno() {
        return sbaAplicaTerreno;
    }

    public void setSbaAplicaTerreno(Boolean sbaAplicaTerreno) {
        this.sbaAplicaTerreno = sbaAplicaTerreno;
    }

    public Boolean getSbaAplicaEdificio() {
        return sbaAplicaEdificio;
    }

    public void setSbaAplicaEdificio(Boolean sbaAplicaEdificio) {
        this.sbaAplicaEdificio = sbaAplicaEdificio;
    }

    public Boolean getSbaAplicaAula() {
        return sbaAplicaAula;
    }

    public void setSbaAplicaAula(Boolean sbaAplicaAula) {
        this.sbaAplicaAula = sbaAplicaAula;
    }

    public String getSbaDescripcion() {
        return sbaDescripcion;
    }

    public void setSbaDescripcion(String sbaDescripcion) {
        this.sbaDescripcion = sbaDescripcion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sbaPk != null ? sbaPk.hashCode() : 0);
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
        final SgServicioBasico other = (SgServicioBasico) obj;
        if (!Objects.equals(this.sbaPk, other.sbaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgServicioBasico[ sbaPk=" + sbaPk + " ]";
    }
    
}
