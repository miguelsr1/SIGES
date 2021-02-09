/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_configuraciones", schema = Constantes.SCHEMA_CATALOGO, uniqueConstraints = {
    @UniqueConstraint(name = "con_codigo_uk", columnNames = {"con_codigo"})
    ,
    @UniqueConstraint(name = "con_nombre_uk", columnNames = {"con_nombre"})})
@XmlRootElement
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "conPk", scope = SgConfiguracion.class)
public class SgConfiguracion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "con_pk", nullable = false)
    private Long conPk;

    @Size(max = 45)
    @Column(name = "con_codigo", length = 45)
    private String conCodigo;

    @Size(max = 255)
    @Column(name = "con_nombre", length = 255)
    private String conNombre;
    
    @Column(name = "con_valor")
    private String conValor;

    @Size(max = 255)
    @Column(name = "con_nombre_busqueda", length = 255)
    private String conNombreBusqueda;

    @Column(name = "con_ult_mod_fecha")
    private LocalDateTime conUltModFecha;

    @Size(max = 45)
    @Column(name = "con_ult_mod_usuario", length = 45)
    private String conUltModUsuario;

    @Column(name = "con_version")
    @Version
    private Integer conVersion;
    
    @Column(name = "con_es_editor")
    private Boolean conEsEditor;

    public SgConfiguracion() {
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
        final SgConfiguracion other = (SgConfiguracion) obj;
        if (!Objects.equals(this.conPk, other.conPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgConfiguracion{" + "conPk=" + conPk + ", conCodigo=" + conCodigo + ", conNombre=" + conNombre + ", conNombreBusqueda=" + conNombreBusqueda + ", conUltModFecha=" + conUltModFecha + ", conUltModUsuario=" + conUltModUsuario + ", conVersion=" + conVersion + '}';
    }

    public Boolean getConEsEditor() {
        return conEsEditor;
    }

    public void setConEsEditor(Boolean conEsEditor) {
        this.conEsEditor = conEsEditor;
    }
    
    

}
