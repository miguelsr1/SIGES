/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgIdioma;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgNivelIdioma;

@Entity
@Table(name = "sg_idiomas_realizados",  schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "irePk", scope = SgIdiomaRealizado.class)
@Audited
public class SgIdiomaRealizado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ire_pk")
    private Long irePk;
    
    @JoinColumn(name = "ire_estudios_realizados_fk", referencedColumnName = "ere_pk")
    @ManyToOne
    private SgEstudioRealizado ireEstudiosRealizados;
    
    @JoinColumn(name = "ire_idioma_fk", referencedColumnName = "idi_pk")
    @ManyToOne
    private SgIdioma ireIdioma;
    
    @JoinColumn(name = "ire_nivel_idioma_fk", referencedColumnName = "nid_pk")
    @ManyToOne
    private SgNivelIdioma ireNivelIdioma;
    
    @Size(max = 5000)
    @Column(name = "ire_observaciones",length = 5000)
    private String ireObservaciones;
    
    @Column(name = "ire_validado")
    private Boolean ireValidado;
    
    @Column(name = "ire_ult_val_fecha")
    private LocalDateTime ireUltValidacionFecha;
    
    @Column(name = "ire_ult_val_usuario")
    private String ireUltValidacionUsuario;

    @Column(name = "ire_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ireUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ire_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String ireUltModUsuario;
    
    @Column(name = "ire_version")
    @Version
    private Integer ireVersion;

    public SgIdiomaRealizado() {
        this.ireValidado = Boolean.FALSE;
    }

    public SgIdiomaRealizado(Long irePk) {
        this.irePk = irePk;
    }

    public Long getIrePk() {
        return irePk;
    }

    public void setIrePk(Long irePk) {
        this.irePk = irePk;
    }

    public SgEstudioRealizado getIreEstudiosRealizados() {
        return ireEstudiosRealizados;
    }

    public void setIreEstudiosRealizados(SgEstudioRealizado ireEstudiosRealizados) {
        this.ireEstudiosRealizados = ireEstudiosRealizados;
    }

    public SgIdioma getIreIdioma() {
        return ireIdioma;
    }

    public void setIreIdioma(SgIdioma ireIdioma) {
        this.ireIdioma = ireIdioma;
    }

    public SgNivelIdioma getIreNivelIdioma() {
        return ireNivelIdioma;
    }

    public void setIreNivelIdioma(SgNivelIdioma ireNivelIdioma) {
        this.ireNivelIdioma = ireNivelIdioma;
    }

    public String getIreObservaciones() {
        return ireObservaciones;
    }

    public void setIreObservaciones(String ireObservaciones) {
        this.ireObservaciones = ireObservaciones;
    }

    public LocalDateTime getIreUltModFecha() {
        return ireUltModFecha;
    }

    public void setIreUltModFecha(LocalDateTime ireUltModFecha) {
        this.ireUltModFecha = ireUltModFecha;
    }

    public String getIreUltModUsuario() {
        return ireUltModUsuario;
    }

    public void setIreUltModUsuario(String ireUltModUsuario) {
        this.ireUltModUsuario = ireUltModUsuario;
    }

    public Integer getIreVersion() {
        return ireVersion;
    }

    public void setIreVersion(Integer ireVersion) {
        this.ireVersion = ireVersion;
    }

    public Boolean getIreValidado() {
        return ireValidado;
    }

    public void setIreValidado(Boolean ireValidado) {
        this.ireValidado = ireValidado;
    }

    public LocalDateTime getIreUltValidacionFecha() {
        return ireUltValidacionFecha;
    }

    public void setIreUltValidacionFecha(LocalDateTime ireUltValidacionFecha) {
        this.ireUltValidacionFecha = ireUltValidacionFecha;
    }

    public String getIreUltValidacionUsuario() {
        return ireUltValidacionUsuario;
    }

    public void setIreUltValidacionUsuario(String ireUltValidacionUsuario) {
        this.ireUltValidacionUsuario = ireUltValidacionUsuario;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (irePk != null ? irePk.hashCode() : 0);
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
        final SgIdiomaRealizado other = (SgIdiomaRealizado) obj;
        if (!Objects.equals(this.irePk, other.irePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgIdiomaRealizado{" + "ireIdioma=" + ireIdioma + ", ireNivelIdioma=" + ireNivelIdioma + ", ireObservaciones=" + ireObservaciones + ", ireValidado=" + ireValidado + ", ireVersion=" + ireVersion + '}';
    }

    

   
    
}
