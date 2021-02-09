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
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProgramaEducativo;

@Entity
@Table(name = "sg_rel_opcion_prog_ed",  schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "roePk", scope = SgRelOpcionProgEd.class)
@Audited
public class SgRelOpcionProgEd implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "roe_pk")
    private Long roePk;
    
    @JoinColumn(name = "roe_opcion_fk", referencedColumnName = "opc_pk")
    @ManyToOne
    private SgOpcion roeOpcion;
    
    @JoinColumn(name = "roe_programa_educativo_fk", referencedColumnName = "ped_pk")
    @ManyToOne(optional = false)
    private SgProgramaEducativo roeProgramaEducativo;
    
    @Column(name = "roe_habilitado")
    @AtributoHabilitado
    private Boolean roeHabilitado;
    
    @Column(name = "roe_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime roeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "roe_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String roeUltModUsuario;
    
    @Column(name = "roe_version")
    @Version
    private Integer roeVersion;

    public SgRelOpcionProgEd() {
    }

    public SgRelOpcionProgEd(Long roePk) {
        this.roePk = roePk;
    }

    public Long getRoePk() {
        return roePk;
    }

    public void setRoePk(Long roePk) {
        this.roePk = roePk;
    }

    public SgOpcion getRoeOpcion() {
        return roeOpcion;
    }

    public void setRoeOpcion(SgOpcion roeOpcion) {
        this.roeOpcion = roeOpcion;
    }

    public SgProgramaEducativo getRoeProgramaEducativo() {
        return roeProgramaEducativo;
    }

    public void setRoeProgramaEducativo(SgProgramaEducativo roeProgramaEducativo) {
        this.roeProgramaEducativo = roeProgramaEducativo;
    }



    public Boolean getRoeHabilitado() {
        return roeHabilitado;
    }

    public void setRoeHabilitado(Boolean roeHabilitado) {
        this.roeHabilitado = roeHabilitado;
    }

    public LocalDateTime getRoeUltModFecha() {
        return roeUltModFecha;
    }

    public void setRoeUltModFecha(LocalDateTime roeUltModFecha) {
        this.roeUltModFecha = roeUltModFecha;
    }

    public String getRoeUltModUsuario() {
        return roeUltModUsuario;
    }

    public void setRoeUltModUsuario(String roeUltModUsuario) {
        this.roeUltModUsuario = roeUltModUsuario;
    }

    public Integer getRoeVersion() {
        return roeVersion;
    }

    public void setRoeVersion(Integer roeVersion) {
        this.roeVersion = roeVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roePk != null ? roePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRelOpcionProgEd)) {
            return false;
        }
        SgRelOpcionProgEd other = (SgRelOpcionProgEd) object;
        if ((this.roePk == null && other.roePk != null) || (this.roePk != null && !this.roePk.equals(other.roePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelOpcionProgEd[ roePk=" + roePk + " ]";
    }
    
}
