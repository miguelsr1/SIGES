/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_programas_educativos", uniqueConstraints = {
    @UniqueConstraint(name = "ped_codigo_uk", columnNames = {"ped_codigo"})
    ,
    @UniqueConstraint(name = "ped_nombre_uk", columnNames = {"ped_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pedPk", scope = SgProgramaEducativo.class)
public class SgProgramaEducativo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ped_pk")
    private Long pedPk;
    
    @Size(max = 10)
    @Column(name = "ped_codigo",length = 10)
    @AtributoCodigo
    private String pedCodigo;
    
    @Column(name = "ped_habilitado")
    @AtributoHabilitado
    private Boolean pedHabilitado;
    
    @Size(max = 255)
    @Column(name = "ped_nombre",length = 255)
    @AtributoNormalizable
    private String pedNombre;
    
    @Size(max = 255)
    @Column(name = "ped_nombre_busqueda",length = 255)
    @AtributoNombre
    private String pedNombreBusqueda;
    
    @Size(max = 500)
    @Column(name = "ped_descripcion",length = 500)
    @AtributoDescripcion
    private String pedDescripcion;
    
    @Column(name = "ped_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pedUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ped_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String pedUltModUsuario;
    
    @Column(name = "ped_version")
    @Version
    private Integer pedVersion;

    public SgProgramaEducativo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.pedNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.pedNombre);
    }

    public Long getPedPk() {
        return pedPk;
    }

    public void setPedPk(Long pedPk) {
        this.pedPk = pedPk;
    }

    public String getPedCodigo() {
        return pedCodigo;
    }

    public void setPedCodigo(String pedCodigo) {
        this.pedCodigo = pedCodigo;
    }

    public Boolean getPedHabilitado() {
        return pedHabilitado;
    }

    public void setPedHabilitado(Boolean pedHabilitado) {
        this.pedHabilitado = pedHabilitado;
    }

    public String getPedNombre() {
        return pedNombre;
    }

    public void setPedNombre(String pedNombre) {
        this.pedNombre = pedNombre;
    }

    public String getPedNombreBusqueda() {
        return pedNombreBusqueda;
    }

    public void setPedNombreBusqueda(String pedNombreBusqueda) {
        this.pedNombreBusqueda = pedNombreBusqueda;
    }

    public LocalDateTime getPedUltModFecha() {
        return pedUltModFecha;
    }

    public void setPedUltModFecha(LocalDateTime pedUltModFecha) {
        this.pedUltModFecha = pedUltModFecha;
    }

    public String getPedUltModUsuario() {
        return pedUltModUsuario;
    }

    public void setPedUltModUsuario(String pedUltModUsuario) {
        this.pedUltModUsuario = pedUltModUsuario;
    }

    public Integer getPedVersion() {
        return pedVersion;
    }

    public void setPedVersion(Integer pedVersion) {
        this.pedVersion = pedVersion;
    }

    public String getPedDescripcion() {
        return pedDescripcion;
    }

    public void setPedDescripcion(String pedDescripcion) {
        this.pedDescripcion = pedDescripcion;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedPk != null ? pedPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgProgramaEducativo)) {
            return false;
        }
        SgProgramaEducativo other = (SgProgramaEducativo) object;
        if ((this.pedPk == null && other.pedPk != null) || (this.pedPk != null && !this.pedPk.equals(other.pedPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgProgramasEducativos[ pedPk=" + pedPk + " ]";
    }
    
}
