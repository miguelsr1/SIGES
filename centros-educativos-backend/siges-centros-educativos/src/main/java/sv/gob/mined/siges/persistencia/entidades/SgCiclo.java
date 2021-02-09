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
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "sg_ciclos", uniqueConstraints = {
    @UniqueConstraint(name = "cic_codigo_uk", columnNames = {"cic_codigo"}),
    @UniqueConstraint(name = "cic_nombre_nivel_uk", columnNames = {"cic_nombre","cic_nivel"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cicPk", scope = SgCiclo.class)
@Audited
public class SgCiclo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cic_pk")
    private Long cicPk;
    
    @Size(max = 4)
    @Column(name = "cic_codigo", length = 4)
    @AtributoCodigo
    private String cicCodigo;
    
    @Size(max = 255)
    @Column(name = "cic_nombre",length = 255)
    @AtributoNombre
    private String cicNombre;
    
    @Column(name = "cic_orden")
    private Integer cicOrden;
        
    @Column(name = "cic_obligatorio")
    private Boolean cicObligatorio;
    
    @Column(name = "cic_habilitado")
    @AtributoHabilitado
    private Boolean cicHabilitado;
    
    @Column(name = "cic_ult_mod_fecha")
    @AtributoUltimaModificacion 
    private LocalDateTime cicUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cic_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String cicUltModUsuario;
    
    @Column(name = "cic_version")
    @Version
    private Integer cicVersion;
    
    @JoinColumn(name = "cic_nivel", referencedColumnName = "niv_pk")
    @ManyToOne(optional = false)
    private SgNivel cicNivel;
    
    @OneToMany(mappedBy = "modCiclo")
    private List<SgModalidad> cicModalidades;

    
    public SgCiclo() {
    }

    public Long getCicPk() {
        return cicPk;
    }

    public void setCicPk(Long cicPk) {
        this.cicPk = cicPk;
    }

    public String getCicCodigo() {
        return cicCodigo;
    }

    public void setCicCodigo(String cicCodigo) {
        this.cicCodigo = cicCodigo;
    }

    public String getCicNombre() {
        return cicNombre;
    }

    public void setCicNombre(String cicNombre) {
        this.cicNombre = cicNombre;
    }

    public Integer getCicOrden() {
        return cicOrden;
    }

    public void setCicOrden(Integer cicOrden) {
        this.cicOrden = cicOrden;
    }


    public Boolean getCicObligatorio() {
        return cicObligatorio;
    }

    public void setCicObligatorio(Boolean cicObligatorio) {
        this.cicObligatorio = cicObligatorio;
    }

    public Boolean getCicHabilitado() {
        return cicHabilitado;
    }

    public void setCicHabilitado(Boolean cicHabilitado) {
        this.cicHabilitado = cicHabilitado;
    }

    public LocalDateTime getCicUltModFecha() {
        return cicUltModFecha;
    }

    public void setCicUltModFecha(LocalDateTime cicUltModFecha) {
        this.cicUltModFecha = cicUltModFecha;
    }

    public String getCicUltModUsuario() {
        return cicUltModUsuario;
    }

    public void setCicUltModUsuario(String cicUltModUsuario) {
        this.cicUltModUsuario = cicUltModUsuario;
    }

    public Integer getCicVersion() {
        return cicVersion;
    }

    public void setCicVersion(Integer cicVersion) {
        this.cicVersion = cicVersion;
    }

    public SgNivel getCicNivel() {
        return cicNivel;
    }

    public void setCicNivel(SgNivel cicNivel) {
        this.cicNivel = cicNivel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cicPk != null ? cicPk.hashCode() : 0);
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
        final SgCiclo other = (SgCiclo) obj;
        if (!Objects.equals(this.cicPk, other.cicPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesCiclo[ cicPk=" + cicPk + " ]";
    }

    public List<SgModalidad> getCicModalidades() {
        return cicModalidades;
    }

    public void setCicModalidades(List<SgModalidad> cicModalidades) {
        this.cicModalidades = cicModalidades;
    }

}
