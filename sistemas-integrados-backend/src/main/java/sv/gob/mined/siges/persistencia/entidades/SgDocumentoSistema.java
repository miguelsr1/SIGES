/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumCategoriaDocumento;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSiTipoDocumento;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author sofis2
 */
@Entity
@Table(name = "sg_documentos_sistemas", schema = Constantes.SCHEMA_SISTEMAS_INTEGRADOS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dsiPk", scope = SgDocumentoSistema.class)
public class SgDocumentoSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dsi_pk")
    private Long dsiPk;
    
    @JoinColumn(name = "dsi_sistema_integrado_fk")
    @ManyToOne
    private SgSistemaIntegrado dsiSistemaIntegrado;
    
    @Size(max = 4000)
    @Column(name = "dsi_descripcion", length = 4000)
    private String dsiDescripcion;
    
    @Size(max = 40)
    @Column(name = "dsi_nombre", length = 40)
    private String dsiNombre;
    
    @JoinColumn(name = "dsi_documento_fk", referencedColumnName = "ach_pk")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private SgArchivo dsiDocumento;
    
    @JoinColumn(name = "dsi_tipo_documento_fk", referencedColumnName = "tid_pk")
    @ManyToOne
    private SgSiTipoDocumento dsiTipoDocumento;
        
    @Column(name = "dsi_categoria_documento")
    @Enumerated(EnumType.STRING)
    private EnumCategoriaDocumento dsiCategoriaDocumento;
    
    @Column(name = "dsi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dsiUltModFecha;
    
    @Size(max = 45)
    @Column(name = "dsi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dsiUltModUsuario;
    
    @Column(name = "dsi_version")
    @Version
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
