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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_idiomas",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"idi_codigo"})})
@XmlRootElement
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
/**
 * Esta clase corresponde a los idiomas que maneja la aplicación.
 */
public class Idioma implements Serializable {

    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "seq_idiomas", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_idiomas", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_idiomas")
    @Basic(optional = false)
    @Column(name = "idi_id")
    /**
     * Id de la entidad.
     */
    private Integer idiId;
    
    @Size(max = 16)
    @Column(name = "idi_codigo", length = 16, unique = true)
    /**
     * Código del idioma
     */
    private String idiCodigo;
    
    @Size(max = 80)
   
    @Column(name = "idi_descripcion", length = 80)
    
    /**
     * Descripción o nombre del idioma.
     */
    private String idiDescripcion;
    @Column(name = "idi_habilitado")
    
    
    @NotNull
    @Basic(optional = false)
    /**
     * Indica si ese idioma está habilitado.
     */
    private Boolean idiHabilitado = Boolean.TRUE;
    
    
    @Column(name = "idi_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    /**
     * Fecha la última modificación de la entidad.
     */
    private Date idiUltMod;
    
    
    @Column(name = "idi_ult_origen")
    @AtributoUltimaOrigen
    /**
     * Origen de la última modificación realizada a la entidad.
     */
    private String idiUltOrigen;
    
    @Size(max = 255)
    @Column(name = "idi_ult_usuario", length = 255)
    @AtributoUltimoUsuario
    /**
     * Usuario que realizó la última modificación a la entidad.
     */
    private String idiUltUsuario;
    
    
    @Column(name = "idi_version")
    @Version
    /**
     * Versión de la entidad.
     */
    private Integer idiVersion;

    public Idioma() {
    }

    public Idioma(Integer idiId) {
        this.idiId = idiId;
    }

    public Integer getIdiId() {
        return idiId;
    }

    public void setIdiId(Integer idiId) {
        this.idiId = idiId;
    }

    public String getIdiCodigo() {
        return idiCodigo;
    }

    public void setIdiCodigo(String idiCodigo) {
        this.idiCodigo = idiCodigo;
    }

    public String getIdiDescripcion() {
        return idiDescripcion;
    }

    public void setIdiDescripcion(String idiDescripcion) {
        this.idiDescripcion = idiDescripcion;
    }

    public Boolean getIdiHabilitado() {
        return idiHabilitado;
    }

    public void setIdiHabilitado(Boolean idiHabilitado) {
        this.idiHabilitado = idiHabilitado;
    }

    public Date getIdiUltMod() {
        return idiUltMod;
    }

    public void setIdiUltMod(Date idiUltMod) {
        this.idiUltMod = idiUltMod;
    }

    public String getIdiUltOrigen() {
        return idiUltOrigen;
    }

    public void setIdiUltOrigen(String idiUltOrigen) {
        this.idiUltOrigen = idiUltOrigen;
    }

    public String getIdiUltUsuario() {
        return idiUltUsuario;
    }

    public void setIdiUltUsuario(String idiUltUsuario) {
        this.idiUltUsuario = idiUltUsuario;
    }

    public Integer getIdiVersion() {
        return idiVersion;
    }

    public void setIdiVersion(Integer idiVersion) {
        this.idiVersion = idiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idiId != null ? idiId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Idioma)) {
            return false;
        }
        Idioma other = (Idioma) object;
        if ((this.idiId == null && other.idiId != null) || (this.idiId != null && !this.idiId.equals(other.idiId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entities.Idioma[ idiId=" + idiId + " ]";
    }

 
}
