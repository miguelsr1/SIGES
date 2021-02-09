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
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_diplomados", uniqueConstraints = {
    @UniqueConstraint(name = "dip_codigo_uk", columnNames = {"dip_codigo"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dipPk", scope = SgDiplomado.class)
@Audited
public class SgDiplomado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dip_pk")
    private Long dipPk;
    
    @Size(max = 10)
    @Column(name = "dip_codigo",length = 10)
    @AtributoCodigo
    private String dipCodigo;
    
    @Size(max = 255)
    @Column(name = "dip_nombre",length = 255)
    @AtributoNormalizable
    private String dipNombre;
    
    @Size(max = 255)
    @Column(name = "dip_nombre_busqueda", length = 255)
    @AtributoNombre
    private String dipNombreBusqueda;
    
    @Size(max = 500)
    @Column(name = "dip_descripcion",length = 500)
    private String dipDescripcion;
    
    @Column(name = "dip_habilitado")
    @AtributoHabilitado
    private Boolean dipHabilitado;
    
    @Column(name = "dip_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dipUltModFecha;
    
    @Size(max = 45)
    @Column(name = "dip_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String dipUltModUsuario;
    
    @Column(name = "dip_version")
    @Version
    private Integer dipVersion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mdiDiplomado")
    private List<SgModuloDiplomado> dipModulosDiplomado;
    
    @OneToMany( mappedBy = "depDiplomado")
    private List<SgDiplomadosEstudiante> dipDiplomadosEstudiante;
    
    public SgDiplomado() {
    }

    public SgDiplomado(Long dipPk) {
        this.dipPk = dipPk;
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.dipNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.dipNombre);
    }

    public Long getDipPk() {
        return dipPk;
    }

    public void setDipPk(Long dipPk) {
        this.dipPk = dipPk;
    }

    public String getDipCodigo() {
        return dipCodigo;
    }

    public void setDipCodigo(String dipCodigo) {
        this.dipCodigo = dipCodigo;
    }

    public String getDipNombre() {
        return dipNombre;
    }

    public void setDipNombre(String dipNombre) {
        this.dipNombre = dipNombre;
    }

    public String getDipNombreBusqueda() {
        return dipNombreBusqueda;
    }

    public void setDipNombreBusqueda(String dipNombreBusqueda) {
        this.dipNombreBusqueda = dipNombreBusqueda;
    }
    
    public String getDipDescripcion() {
        return dipDescripcion;
    }

    public void setDipDescripcion(String dipDescripcion) {
        this.dipDescripcion = dipDescripcion;
    }

    public Boolean getDipHabilitado() {
        return dipHabilitado;
    }

    public void setDipHabilitado(Boolean dipHabilitado) {
        this.dipHabilitado = dipHabilitado;
    }

    public LocalDateTime getDipUltModFecha() {
        return dipUltModFecha;
    }

    public void setDipUltModFecha(LocalDateTime dipUltModFecha) {
        this.dipUltModFecha = dipUltModFecha;
    }

    public String getDipUltModUsuario() {
        return dipUltModUsuario;
    }

    public void setDipUltModUsuario(String dipUltModUsuario) {
        this.dipUltModUsuario = dipUltModUsuario;
    }

    public Integer getDipVersion() {
        return dipVersion;
    }

    public void setDipVersion(Integer dipVersion) {
        this.dipVersion = dipVersion;
    }

    public List<SgModuloDiplomado> getDipModulosDiplomado() {
        return dipModulosDiplomado;
    }

    public void setDipModulosDiplomado(List<SgModuloDiplomado> dipModulosDiplomado) {
        this.dipModulosDiplomado = dipModulosDiplomado;
    }

    public List<SgDiplomadosEstudiante> getDipDiplomadosEstudiante() {
        return dipDiplomadosEstudiante;
    }

    public void setDipDiplomadosEstudiante(List<SgDiplomadosEstudiante> dipDiplomadosEstudiante) {
        this.dipDiplomadosEstudiante = dipDiplomadosEstudiante;
    }

 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dipPk != null ? dipPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDiplomado)) {
            return false;
        }
        SgDiplomado other = (SgDiplomado) object;
        if ((this.dipPk == null && other.dipPk != null) || (this.dipPk != null && !this.dipPk.equals(other.dipPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDiplomado[ dipPk=" + dipPk + " ]";
    }
    
}
