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
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_especialidades", uniqueConstraints = {
    @UniqueConstraint(name = "esp_codigo_uk", columnNames = {"esp_codigo"})
    ,
    @UniqueConstraint(name = "esp_nombre_uk", columnNames = {"esp_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "espPk", scope = SgEspecialidad.class)
public class SgEspecialidad implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "esp_pk")
    private Long espPk;
    
    @Size(max = 45)
    @Column(name = "esp_codigo",length = 45)
    @AtributoCodigo
    private String espCodigo;
    
    @Column(name = "esp_habilitado")
    @AtributoHabilitado
    private Boolean espHabilitado;
    
    @Size(max = 255)
    @Column(name = "esp_nombre",length = 255)
    @AtributoNormalizable
    private String espNombre;
    
    @Size(max = 255)
    @Column(name = "esp_nombre_busqueda",length = 255)
    @AtributoNombre
    private String espNombreBusqueda;
    
    @Column(name = "esp_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime espUltModFecha;
    
    @Size(max = 45)
    @Column(name = "esp_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String espUltModUsuario;
    
    @Column(name = "esp_version")
    @Version
    private Integer espVersion;

    public SgEspecialidad() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.espNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.espNombre);
    }

    public SgEspecialidad(Long espPk) {
        this.espPk = espPk;
    }

    public Long getEspPk() {
        return espPk;
    }

    public void setEspPk(Long espPk) {
        this.espPk = espPk;
    }

    public String getEspCodigo() {
        return espCodigo;
    }

    public void setEspCodigo(String espCodigo) {
        this.espCodigo = espCodigo;
    }

    public Boolean getEspHabilitado() {
        return espHabilitado;
    }

    public void setEspHabilitado(Boolean espHabilitado) {
        this.espHabilitado = espHabilitado;
    }

    public String getEspNombre() {
        return espNombre;
    }

    public void setEspNombre(String espNombre) {
        this.espNombre = espNombre;
    }

    public String getEspNombreBusqueda() {
        return espNombreBusqueda;
    }

    public void setEspNombreBusqueda(String espNombreBusqueda) {
        this.espNombreBusqueda = espNombreBusqueda;
    }

    public LocalDateTime getEspUltModFecha() {
        return espUltModFecha;
    }

    public void setEspUltModFecha(LocalDateTime espUltModFecha) {
        this.espUltModFecha = espUltModFecha;
    }

    public String getEspUltModUsuario() {
        return espUltModUsuario;
    }

    public void setEspUltModUsuario(String espUltModUsuario) {
        this.espUltModUsuario = espUltModUsuario;
    }

    public Integer getEspVersion() {
        return espVersion;
    }

    public void setEspVersion(Integer espVersion) {
        this.espVersion = espVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (espPk != null ? espPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEspecialidad)) {
            return false;
        }
        SgEspecialidad other = (SgEspecialidad) object;
        if ((this.espPk == null && other.espPk != null) || (this.espPk != null && !this.espPk.equals(other.espPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEspecialidad[ espPk=" + espPk + " ]";
    }
    
}
