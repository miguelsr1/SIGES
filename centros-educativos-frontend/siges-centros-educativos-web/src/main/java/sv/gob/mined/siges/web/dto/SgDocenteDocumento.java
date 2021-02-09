/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoDocumentoDocente;

/**
 *
 * @author Sofis Solutions
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ddoPk", scope = SgDocenteDocumento.class)
public class SgDocenteDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long ddoPk;
    
    private SgPersonalSedeEducativa ddoPersonal;
    
    private SgTipoDocumentoDocente ddoTipoDocumento;
    
    private String ddoDescripcion;
    
    private SgArchivo ddoArchivo;
    
    private Boolean ddoValidado;
 
    private LocalDateTime ddoUltModFecha;
    
    private String ddoUltModUsuario;
    
    private Integer ddoVersion;

    public SgDocenteDocumento() {
        this.ddoValidado = Boolean.FALSE;
    }

    public Long getDdoPk() {
        return ddoPk;
    }

    public void setDdoPk(Long ddoPk) {
        this.ddoPk = ddoPk;
    }

    public SgPersonalSedeEducativa getDdoPersonal() {
        return ddoPersonal;
    }

    public void setDdoPersonal(SgPersonalSedeEducativa ddoPersonal) {
        this.ddoPersonal = ddoPersonal;
    }

    public SgTipoDocumentoDocente getDdoTipoDocumento() {
        return ddoTipoDocumento;
    }

    public void setDdoTipoDocumento(SgTipoDocumentoDocente ddoTipoDocumento) {
        this.ddoTipoDocumento = ddoTipoDocumento;
    }

    public String getDdoDescripcion() {
        return ddoDescripcion;
    }

    public void setDdoDescripcion(String ddoDescripcion) {
        this.ddoDescripcion = ddoDescripcion;
    }

    public SgArchivo getDdoArchivo() {
        return ddoArchivo;
    }

    public void setDdoArchivo(SgArchivo ddoArchivo) {
        this.ddoArchivo = ddoArchivo;
    }

    public LocalDateTime getDdoUltModFecha() {
        return ddoUltModFecha;
    }

    public void setDdoUltModFecha(LocalDateTime ddoUltModFecha) {
        this.ddoUltModFecha = ddoUltModFecha;
    }

    public String getDdoUltModUsuario() {
        return ddoUltModUsuario;
    }

    public void setDdoUltModUsuario(String ddoUltModUsuario) {
        this.ddoUltModUsuario = ddoUltModUsuario;
    }

    public Integer getDdoVersion() {
        return ddoVersion;
    }

    public void setDdoVersion(Integer ddoVersion) {
        this.ddoVersion = ddoVersion;
    }

    public Boolean getDdoValidado() {
        return ddoValidado;
    }

    public void setDdoValidado(Boolean ddoValidado) {
        this.ddoValidado = ddoValidado;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ddoPk != null ? ddoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDocenteDocumento)) {
            return false;
        }
        SgDocenteDocumento other = (SgDocenteDocumento) object;
        if ((this.ddoPk == null && other.ddoPk != null) || (this.ddoPk != null && !this.ddoPk.equals(other.ddoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDocenteDocumento[ ddoPk=" + ddoPk + " ]";
    }
    
}
