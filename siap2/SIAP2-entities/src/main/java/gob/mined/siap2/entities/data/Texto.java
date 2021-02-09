/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimaOrigen;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_textos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tex_codigo", "tex_idi_id"})})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)

public class Texto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_textos_gen")
    @SequenceGenerator(name = "seq_textos_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_textos", allocationSize = 1)
    
    @Column(name = "tex_id")
    private Integer texId;

    @Column(name = "tex_codigo")
    @Basic(optional = false)
    private String texCodigo;

    @Column(name = "tex_valor")
    private String texValor;

    @Column(name = "tex_habilitado")
    @Basic(optional = false)
    private Boolean texHabilitado = Boolean.TRUE;

    @ManyToOne
    @JoinColumn(name = "tex_idi_id")
    private Idioma texIdiId;

    //Auditoria
    @Column(name = "tex_ult_usuario")
    @AtributoUltimoUsuario
    private String texUltUsuario;

    @Column(name = "tex_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    @NotNull
    @Basic(optional = false)
    private Date texUltMod;

    @Column(name = "tex_ult_origen")
    @AtributoUltimaOrigen
    private String texUltOrigen;

    @Column(name = "tex_version")
    @Version
    private Integer texVersion;

    public Texto() {
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Texto(Integer texId) {
        this.texId = texId;
    }

    public Integer getTexId() {
        return texId;
    }

    public void setTexId(Integer texId) {
        this.texId = texId;
    }

    public Boolean getTexHabilitado() {
        return texHabilitado;
    }

    public void setTexHabilitado(Boolean texHabilitado) {
        this.texHabilitado = texHabilitado;
    }

    public String getTexUltUsuario() {
        return texUltUsuario;
    }

    public void setTexUltUsuario(String texUltUsuario) {
        this.texUltUsuario = texUltUsuario;
    }

    public Date getTexUltMod() {
        return texUltMod;
    }

    public void setTexUltMod(Date texUltMod) {
        this.texUltMod = texUltMod;
    }

    public String getTexUltOrigen() {
        return texUltOrigen;
    }

    public void setTexUltOrigen(String texUltOrigen) {
        this.texUltOrigen = texUltOrigen;
    }

    public Integer getTexVersion() {
        return texVersion;
    }

    public void setTexVersion(Integer texVersion) {
        this.texVersion = texVersion;
    }

    public String getTexCodigo() {
        return texCodigo;
    }

    public void setTexCodigo(String texCodigo) {
        this.texCodigo = texCodigo;
    }

    public String getTexValor() {
        return texValor;
    }

    public void setTexValor(String texValor) {
        this.texValor = texValor;
    }

    public Idioma getTexIdiId() {
        return texIdiId;
    }

    public void setTexIdiId(Idioma texIdiId) {
        this.texIdiId = texIdiId;
    }

    // </editor-fold>
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (texId != null ? texId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Texto)) {
            return false;
        }
        Texto other = (Texto) object;
        if ((this.texId == null && other.texId != null) || (this.texId != null && !this.texId.equals(other.texId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entities.data.Texto[ texId=" + texId + " ]";
    }
}
