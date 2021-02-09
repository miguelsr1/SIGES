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
import sv.gob.mined.siges.web.enumerados.EnumTipoArchivoCarga;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cgaPk", scope = SgCargaArchivo.class)
public class SgCargaArchivo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long cgaPk;

    private String cgaCodigo;

    private String cgaNombre;
    
    private EnumTipoArchivoCarga cgaTipoArchivo;
    
    private String cgaFormatosPermitidos;
    
    private Integer cgaAncho;
    
    private Integer cgaAlto;

    private Boolean cgaHabilitado;

    private LocalDateTime cgaUltModFecha;

    private String cgaUltModUsuario;

    private Integer cgaVersion;
    
    
    public SgCargaArchivo() {
        this.cgaHabilitado = Boolean.TRUE;
    }

    
    //<editor-fold defaultstate="collapsed" desc="GET & SET">

    public Long getCgaPk() {
        return cgaPk;
    }

    public void setCgaPk(Long cgaPk) {
        this.cgaPk = cgaPk;
    }

    public String getCgaCodigo() {
        return cgaCodigo;
    }

    public void setCgaCodigo(String cgaCodigo) {
        this.cgaCodigo = cgaCodigo;
    }

    public String getCgaNombre() {
        return cgaNombre;
    }

    public void setCgaNombre(String cgaNombre) {
        this.cgaNombre = cgaNombre;
    }

    public LocalDateTime getCgaUltModFecha() {
        return cgaUltModFecha;
    }

    public void setCgaUltModFecha(LocalDateTime cgaUltModFecha) {
        this.cgaUltModFecha = cgaUltModFecha;
    }

    public String getCgaUltModUsuario() {
        return cgaUltModUsuario;
    }

    public void setCgaUltModUsuario(String cgaUltModUsuario) {
        this.cgaUltModUsuario = cgaUltModUsuario;
    }

    public Integer getCgaVersion() {
        return cgaVersion;
    }

    public void setCgaVersion(Integer cgaVersion) {
        this.cgaVersion = cgaVersion;
    }

    
     public Boolean getCgaHabilitado() {
        return cgaHabilitado;
    }

    public void setCgaHabilitado(Boolean cgaHabilitado) {
        this.cgaHabilitado = cgaHabilitado;
    }

    public EnumTipoArchivoCarga getCgaTipoArchivo() {
        return cgaTipoArchivo;
    }

    public void setCgaTipoArchivo(EnumTipoArchivoCarga cgaTipoArchivo) {
        this.cgaTipoArchivo = cgaTipoArchivo;
    }

    public String getCgaFormatosPermitidos() {
        return cgaFormatosPermitidos;
    }

    public void setCgaFormatosPermitidos(String cgaFormatosPermitidos) {
        this.cgaFormatosPermitidos = cgaFormatosPermitidos;
    }

    public Integer getCgaAncho() {
        return cgaAncho;
    }

    public void setCgaAncho(Integer cgaAncho) {
        this.cgaAncho = cgaAncho;
    }

    public Integer getCgaAlto() {
        return cgaAlto;
    }

    public void setCgaAlto(Integer cgaAlto) {
        this.cgaAlto = cgaAlto;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cgaPk != null ? cgaPk.hashCode() : 0);
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
        final SgCargaArchivo other = (SgCargaArchivo) obj;
        if (!Objects.equals(this.cgaPk, other.cgaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCargaArchivo[ cgaPk=" + cgaPk + " ]";
    }
    
}
