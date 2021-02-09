/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoParentesco;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_allegados", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "allPk", scope = SgAllegado.class)
public class SgAllegado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "all_pk")
    private Long allPk;

    @Column(name = "all_referente")
    private Boolean allReferente;

    @Column(name = "all_contacto_emergencia")
    private Boolean allContactoEmergencia;

    @Column(name = "all_dependiente")
    private Boolean allDependiente;

    @Column(name = "all_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime allUltModFecha;

    @Size(max = 45)
    @Column(name = "all_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String allUltModUsuario;

    @Column(name = "all_version")
    @Version
    private Integer allVersion;

    @JoinColumn(name = "all_persona_ref")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgPersona allPersonaReferenciada;

    @Column(name = "all_es_familiar")
    private Boolean allEsFamiliar;

    @JoinColumn(name = "all_persona", updatable = false)
    @OneToOne
    private SgPersona allPersona;

    @JoinColumn(name = "all_tipo_parentesco")
    @ManyToOne
    private SgTipoParentesco allTipoParentesco;
    
    @Column(name = "all_vive_con_allegado")
    private Boolean allViveConAllegado;

    public SgAllegado() {
    }

    public Long getAllPk() {
        return allPk;
    }

    public void setAllPk(Long allPk) {
        this.allPk = allPk;
    }

    public Boolean getAllReferente() {
        return allReferente;
    }

    public void setAllReferente(Boolean allReferente) {
        this.allReferente = allReferente;
    }

    public Boolean getAllContactoEmergencia() {
        return allContactoEmergencia;
    }

    public void setAllContactoEmergencia(Boolean allContactoEmergencia) {
        this.allContactoEmergencia = allContactoEmergencia;
    }

    public LocalDateTime getAllUltModFecha() {
        return allUltModFecha;
    }

    public void setAllUltModFecha(LocalDateTime allUltModFecha) {
        this.allUltModFecha = allUltModFecha;
    }

    public String getAllUltModUsuario() {
        return allUltModUsuario;
    }

    public void setAllUltModUsuario(String allUltModUsuario) {
        this.allUltModUsuario = allUltModUsuario;
    }

    public Integer getAllVersion() {
        return allVersion;
    }

    public void setAllVersion(Integer allVersion) {
        this.allVersion = allVersion;
    }

    public SgPersona getAllPersona() {
        return allPersona;
    }

    public void setAllPersona(SgPersona allPersona) {
        this.allPersona = allPersona;
    }

    public SgTipoParentesco getAllTipoParentesco() {
        return allTipoParentesco;
    }

    public void setAllTipoParentesco(SgTipoParentesco allTipoParentesco) {
        this.allTipoParentesco = allTipoParentesco;
    }

    public SgPersona getAllPersonaReferenciada() {
        return allPersonaReferenciada;
    }

    public void setAllPersonaReferenciada(SgPersona allPersonaReferenciada) {
        this.allPersonaReferenciada = allPersonaReferenciada;
    }

    public Boolean getAllEsFamiliar() {
        return allEsFamiliar;
    }

    public void setAllEsFamiliar(Boolean allEsFamiliar) {
        this.allEsFamiliar = allEsFamiliar;
    }

    public Boolean getAllDependiente() {
        return allDependiente;
    }

    public void setAllDependiente(Boolean allDependiente) {
        this.allDependiente = allDependiente;
    }

    public Boolean getAllViveConAllegado() {
        return allViveConAllegado;
    }

    public void setAllViveConAllegado(Boolean allViveConAllegado) {
        this.allViveConAllegado = allViveConAllegado;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (allPk != null ? allPk.hashCode() : 0);
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
        final SgAllegado other = (SgAllegado) obj;
        if (!Objects.equals(this.allPk, other.allPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAllegado[ allPk=" + allPk + " ]";
    }

}
