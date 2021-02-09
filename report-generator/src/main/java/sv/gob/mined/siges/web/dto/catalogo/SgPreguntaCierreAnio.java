/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pcaPk", scope = SgPreguntaCierreAnio.class)
public class SgPreguntaCierreAnio implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long pcaPk;

    private String pcaCodigo;

    private String pcaNombre;

    private Boolean pcaHabilitado;
    
    private String pcaPregunta;

    private LocalDateTime pcaUltModFecha;

    private String pcaUltModUsuario;

    private Integer pcaVersion;
    
    
    public SgPreguntaCierreAnio() {
        this.pcaHabilitado = Boolean.TRUE;
    }

    public Long getPcaPk() {
        return pcaPk;
    }

    public void setPcaPk(Long pcaPk) {
        this.pcaPk = pcaPk;
    }

    public String getPcaCodigo() {
        return pcaCodigo;
    }

    public void setPcaCodigo(String pcaCodigo) {
        this.pcaCodigo = pcaCodigo;
    }

    public String getPcaNombre() {
        return pcaNombre;
    }

    public void setPcaNombre(String pcaNombre) {
        this.pcaNombre = pcaNombre;
    }

    public LocalDateTime getPcaUltModFecha() {
        return pcaUltModFecha;
    }

    public void setPcaUltModFecha(LocalDateTime pcaUltModFecha) {
        this.pcaUltModFecha = pcaUltModFecha;
    }

    public String getPcaUltModUsuario() {
        return pcaUltModUsuario;
    }

    public void setPcaUltModUsuario(String pcaUltModUsuario) {
        this.pcaUltModUsuario = pcaUltModUsuario;
    }

    public Integer getPcaVersion() {
        return pcaVersion;
    }

    public void setPcaVersion(Integer pcaVersion) {
        this.pcaVersion = pcaVersion;
    }

    
     public Boolean getPcaHabilitado() {
        return pcaHabilitado;
    }

    public void setPcaHabilitado(Boolean pcaHabilitado) {
        this.pcaHabilitado = pcaHabilitado;
    }

    public String getPcaPregunta() {
        return pcaPregunta;
    }

    public void setPcaPregunta(String pcaPregunta) {
        this.pcaPregunta = pcaPregunta;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pcaPk != null ? pcaPk.hashCode() : 0);
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
        final SgPreguntaCierreAnio other = (SgPreguntaCierreAnio) obj;
        if (!Objects.equals(this.pcaPk, other.pcaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgPreguntaCierreAnio[ pcaPk=" + pcaPk + " ]";
    }
    
}
