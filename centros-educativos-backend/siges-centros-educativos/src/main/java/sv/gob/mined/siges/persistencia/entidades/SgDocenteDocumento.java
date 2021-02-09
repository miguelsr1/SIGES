/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoDocumentoDocente;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_docentes_documentos",   schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ddoPk", scope = SgDocenteDocumento.class)
@Audited
public class SgDocenteDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ddo_pk")
    private Long ddoPk;
    
    @JoinColumn(name = "ddo_personal_fk", referencedColumnName = "pse_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgPersonalSedeEducativa ddoPersonal;
    
    @JoinColumn(name = "ddo_tipo_documento_fk", referencedColumnName = "tdd_pk")
    @ManyToOne
    private SgTipoDocumentoDocente ddoTipoDocumento;
    
    @Size(max = 255)
    @Column(name = "ddo_descripcion", length = 255)
    private String ddoDescripcion;
    
    @JoinColumn(name = "ddo_archivo_fk", referencedColumnName = "ach_pk")
    @ManyToOne(cascade = CascadeType.ALL)
    private SgArchivo ddoArchivo;
    
    @Column(name = "ddo_validado")
    private Boolean ddoValidado;
    
    @Column(name = "ddo_ult_val_fecha")
    private LocalDateTime ddoUltValidacionFecha;
    
    @Column(name = "ddo_ult_val_usuario")
    private String ddoUltValidacionUsuario;
    
    @Column(name = "ddo_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ddoUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ddo_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ddoUltModUsuario;
    
    @Column(name = "ddo_version")
    @Version
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

    public LocalDateTime getDdoUltValidacionFecha() {
        return ddoUltValidacionFecha;
    }

    public void setDdoUltValidacionFecha(LocalDateTime ddoUltValidacionFecha) {
        this.ddoUltValidacionFecha = ddoUltValidacionFecha;
    }

    public String getDdoUltValidacionUsuario() {
        return ddoUltValidacionUsuario;
    }

    public void setDdoUltValidacionUsuario(String ddoUltValidacionUsuario) {
        this.ddoUltValidacionUsuario = ddoUltValidacionUsuario;
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
