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
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "ss_configuraciones", schema = Constantes.SCHEMA_PFEA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "conPk", scope = SgConfiguracionPfea.class)
public class SgConfiguracionPfea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cnf_id", nullable = false)
    private Long conPk;

    @Column(name = "cnf_codigo")
    @AtributoCodigo
    private String conCodigo;

    @Column(name = "cnf_descripcion")
    @AtributoNormalizable
    private String conNombre;
    
    @Column(name = "cnf_descripcion_busqueda")
    @AtributoNombre
    private String conNombreBusqueda;
    
    @Column(name = "cnf_valor")
    private String conValor;


    @Column(name = "cnf_ultima_modificacion")
    @AtributoUltimaModificacion
    private LocalDateTime conUltModFecha;

    @Size(max = 45)
    @Column(name = "cnf_ultimo_usuario", length = 45)
    @AtributoUltimoUsuario
    private String conUltModUsuario;

    @Column(name = "cnf_version")
    @Version
    private Integer conVersion;
    
    @Column(name = "cnf_es_editor")
    private Boolean conEsEditor;

    public SgConfiguracionPfea() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.conNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.conNombre);
    }

    public Long getConPk() {
        return conPk;
    }

    public void setConPk(Long conPk) {
        this.conPk = conPk;
    }

    public String getConCodigo() {
        return conCodigo;
    }

    public void setConCodigo(String conCodigo) {
        this.conCodigo = conCodigo;
    }

    public String getConNombre() {
        return conNombre;
    }

    public void setConNombre(String conNombre) {
        this.conNombre = conNombre;
    }

    public String getConNombreBusqueda() {
        return conNombreBusqueda;
    }

    public void setConNombreBusqueda(String conNombreBusqueda) {
        this.conNombreBusqueda = conNombreBusqueda;
    }

    public LocalDateTime getConUltModFecha() {
        return conUltModFecha;
    }

    public void setConUltModFecha(LocalDateTime conUltModFecha) {
        this.conUltModFecha = conUltModFecha;
    }

    public String getConUltModUsuario() {
        return conUltModUsuario;
    }

    public void setConUltModUsuario(String conUltModUsuario) {
        this.conUltModUsuario = conUltModUsuario;
    }

    public Integer getConVersion() {
        return conVersion;
    }

    public void setConVersion(Integer conVersion) {
        this.conVersion = conVersion;
    }

    public String getConValor() {
        return conValor;
    }

    public void setConValor(String conValor) {
        this.conValor = conValor;
    }
    
    public Boolean getConEsEditor() {
        return conEsEditor;
    }

    public void setConEsEditor(Boolean conEsEditor) {
        this.conEsEditor = conEsEditor;
    }
     
    @Override
    public int hashCode() {
        return Objects.hashCode(this.conPk);
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
        final SgConfiguracionPfea other = (SgConfiguracionPfea) obj;
        if (!Objects.equals(this.conPk, other.conPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgConfiguracionPfea{" + "conPk=" + conPk + ", conCodigo=" + conCodigo + ", conNombre=" + conNombre + ", conNombreBusqueda=" + conNombreBusqueda + ", conUltModFecha=" + conUltModFecha + ", conUltModUsuario=" + conUltModUsuario + ", conVersion=" + conVersion + '}';
    }
  

}
