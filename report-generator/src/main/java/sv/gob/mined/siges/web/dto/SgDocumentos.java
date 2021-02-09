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
import sv.gob.mined.siges.web.enumerados.EnumEstadoDocumento;
import sv.gob.mined.siges.web.enumerados.TipoDocumento;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "docPk", scope = SgDocumentos.class)
public class SgDocumentos implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long docPk;

    private SgPresupuestoEscolar docPresupuestoFk;
    
    private SgArchivo docArchivoFk;
    
    private TipoDocumento docTipoDocumento;
    
    private EnumEstadoDocumento docEstadoDocumento;

    private LocalDateTime docUltModFecha;

    private String docUltModUsuario;

    private Integer docVersion;
    
    
    public SgDocumentos() {
        
    }

    public Long getDocPk() {
        return docPk;
    }

    public void setDocPk(Long docPk) {
        this.docPk = docPk;
    }

    public SgPresupuestoEscolar getDocPresupuestoFk() {
        return docPresupuestoFk;
    }

    public void setDocPresupuestoFk(SgPresupuestoEscolar docPresupuestoFk) {
        this.docPresupuestoFk = docPresupuestoFk;
    }

    public SgArchivo getDocArchivoFk() {
        return docArchivoFk;
    }

    public void setDocArchivoFk(SgArchivo docArchivoFk) {
        this.docArchivoFk = docArchivoFk;
    }

    public TipoDocumento getDocTipoDocumento() {
        return docTipoDocumento;
    }

    public void setDocTipoDocumento(TipoDocumento docTipoDocumento) {
        this.docTipoDocumento = docTipoDocumento;
    }

    public EnumEstadoDocumento getDocEstadoDocumento() {
        return docEstadoDocumento;
    }

    public void setDocEstadoDocumento(EnumEstadoDocumento docEstadoDocumento) {
        this.docEstadoDocumento = docEstadoDocumento;
    }

    public LocalDateTime getDocUltModFecha() {
        return docUltModFecha;
    }

    public void setDocUltModFecha(LocalDateTime docUltModFecha) {
        this.docUltModFecha = docUltModFecha;
    }

    public String getDocUltModUsuario() {
        return docUltModUsuario;
    }

    public void setDocUltModUsuario(String docUltModUsuario) {
        this.docUltModUsuario = docUltModUsuario;
    }

    public Integer getDocVersion() {
        return docVersion;
    }

    public void setDocVersion(Integer docVersion) {
        this.docVersion = docVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (docPk != null ? docPk.hashCode() : 0);
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
        final SgDocumentos other = (SgDocumentos) obj;
        if (!Objects.equals(this.docPk, other.docPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgDocumentos[ docPk=" + docPk + " ]";
    }
    
}
