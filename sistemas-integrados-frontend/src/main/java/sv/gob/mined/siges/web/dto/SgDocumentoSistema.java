/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import sv.gob.mined.siges.web.dto.centros_educativos.SgArchivo;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgSiTipoDocumento;
import sv.gob.mined.siges.web.enumerados.EnumCategoriaDocumento;

/**
 *
 * @author sofis2
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dsiPk", scope = SgDocumentoSistema.class)
public class SgDocumentoSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long dsiPk;
    
    private SgSistemaIntegrado dsiSistemaIntegrado;
    
    private String dsiDescripcion;
    
    private String dsiNombre;
    
    private SgArchivo dsiDocumento;
    
    private SgSiTipoDocumento dsiTipoDocumento;
        
    private EnumCategoriaDocumento dsiCategoriaDocumento;
    
    private LocalDateTime dsiUltModFecha;
    
    private String dsiUltModUsuario;
    
    private Integer dsiVersion;

    public SgDocumentoSistema() {
    }

    public SgDocumentoSistema(Long dsiPk) {
        this.dsiPk = dsiPk;
    }

    public Long getDsiPk() {
        return dsiPk;
    }

    public void setDsiPk(Long dsiPk) {
        this.dsiPk = dsiPk;
    }

    public SgSistemaIntegrado getDsiSistemaIntegrado() {
        return dsiSistemaIntegrado;
    }

    public void setDsiSistemaIntegrado(SgSistemaIntegrado dsiSistemaIntegrado) {
        this.dsiSistemaIntegrado = dsiSistemaIntegrado;
    }

    public String getDsiDescripcion() {
        return dsiDescripcion;
    }

    public void setDsiDescripcion(String dsiDescripcion) {
        this.dsiDescripcion = dsiDescripcion;
    }

    public String getDsiNombre() {
        return dsiNombre;
    }

    public void setDsiNombre(String dsiNombre) {
        this.dsiNombre = dsiNombre;
    }

    public SgArchivo getDsiDocumento() {
        return dsiDocumento;
    }

    public void setDsiDocumento(SgArchivo dsiDocumento) {
        this.dsiDocumento = dsiDocumento;
    }

    public SgSiTipoDocumento getDsiTipoDocumento() {
        return dsiTipoDocumento;
    }

    public void setDsiTipoDocumento(SgSiTipoDocumento dsiTipoDocumento) {
        this.dsiTipoDocumento = dsiTipoDocumento;
    }

    public EnumCategoriaDocumento getDsiCategoriaDocumento() {
        return dsiCategoriaDocumento;
    }

    public void setDsiCategoriaDocumento(EnumCategoriaDocumento dsiCategoriaDocumento) {
        this.dsiCategoriaDocumento = dsiCategoriaDocumento;
    }

    public LocalDateTime getDsiUltModFecha() {
        return dsiUltModFecha;
    }

    public void setDsiUltModFecha(LocalDateTime dsiUltModFecha) {
        this.dsiUltModFecha = dsiUltModFecha;
    }


    public String getDsiUltModUsuario() {
        return dsiUltModUsuario;
    }

    public void setDsiUltModUsuario(String dsiUltModUsuario) {
        this.dsiUltModUsuario = dsiUltModUsuario;
    }

    public Integer getDsiVersion() {
        return dsiVersion;
    }

    public void setDsiVersion(Integer dsiVersion) {
        this.dsiVersion = dsiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dsiPk != null ? dsiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDocumentoSistema)) {
            return false;
        }
        SgDocumentoSistema other = (SgDocumentoSistema) object;
        if ((this.dsiPk == null && other.dsiPk != null) || (this.dsiPk != null && !this.dsiPk.equals(other.dsiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDocumentoSistema[ dsiPk=" + dsiPk + " ]";
    }
    
}
