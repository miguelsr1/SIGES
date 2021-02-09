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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumConsultaGuardada;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_consulta_guardada", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cgrPk", scope = SgConsultaGuardada.class)
@Audited
public class SgConsultaGuardada implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cgr_pk", nullable = false)
    private Long cgrPk;


    @Size(max = 255)
    @Column(name = "cgr_nombre", length = 255)
    @AtributoNormalizable
    private String cgrNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cgr_nombre_busqueda", length = 255)
    private String cgrNombreBusqueda;

    @Column(name = "cgr_habilitado")
    @AtributoHabilitado
    private Boolean cgrHabilitado;
    
    @Column(name = "cgr_consulta")
    private String cgrConsulta;    
    
    @Column(name = "cgr_usuario")
    private String cgrUsuario;
    
    @Column(name = "cgr_filtro")
    @Enumerated(value = EnumType.STRING)
    private EnumConsultaGuardada cgrFiltro;
 
    @Column(name = "cgr_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cgrUltModFecha;

    @Size(max = 45)
    @Column(name = "cgr_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cgrUltModUsuario;

    @Column(name = "cgr_version")
    @Version
    private Integer cgrVersion;

    public SgConsultaGuardada() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cgrNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cgrNombre);
    }

    public Long getCgrPk() {
        return cgrPk;
    }

    public void setCgrPk(Long cgrPk) {
        this.cgrPk = cgrPk;
    }


    public String getCgrNombre() {
        return cgrNombre;
    }

    public void setCgrNombre(String cgrNombre) {
        this.cgrNombre = cgrNombre;
    }

    public String getCgrNombreBusqueda() {
        return cgrNombreBusqueda;
    }

    public void setCgrNombreBusqueda(String cgrNombreBusqueda) {
        this.cgrNombreBusqueda = cgrNombreBusqueda;
    }

    public Boolean getCgrHabilitado() {
        return cgrHabilitado;
    }

    public void setCgrHabilitado(Boolean cgrHabilitado) {
        this.cgrHabilitado = cgrHabilitado;
    }

    public String getCgrConsulta() {
        return cgrConsulta;
    }

    public void setCgrConsulta(String cgrConsulta) {
        this.cgrConsulta = cgrConsulta;
    }

    public String getCgrUsuario() {
        return cgrUsuario;
    }

    public void setCgrUsuario(String cgrUsuario) {
        this.cgrUsuario = cgrUsuario;
    }
    
    public EnumConsultaGuardada getCgrFiltro() {
        return cgrFiltro;
    }

    public void setCgrFiltro(EnumConsultaGuardada cgrFiltro) {
        this.cgrFiltro = cgrFiltro;
    }  

   
    public LocalDateTime getCgrUltModFecha() {
        return cgrUltModFecha;
    }

    public void setCgrUltModFecha(LocalDateTime cgrUltModFecha) {
        this.cgrUltModFecha = cgrUltModFecha;
    }

    public String getCgrUltModUsuario() {
        return cgrUltModUsuario;
    }

    public void setCgrUltModUsuario(String cgrUltModUsuario) {
        this.cgrUltModUsuario = cgrUltModUsuario;
    }

    public Integer getCgrVersion() {
        return cgrVersion;
    }

    public void setCgrVersion(Integer cgrVersion) {
        this.cgrVersion = cgrVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cgrPk);
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
        final SgConsultaGuardada other = (SgConsultaGuardada) obj;
        if (!Objects.equals(this.cgrPk, other.cgrPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgConsultaGuardada{" + "cgrPk=" + cgrPk + ", cgrNombre=" + cgrNombre + ", cgrNombreBusqueda=" + cgrNombreBusqueda + ", cgrHabilitado=" + cgrHabilitado + ", cgrUltModFecha=" + cgrUltModFecha + ", cgrUltModUsuario=" + cgrUltModUsuario + ", cgrVersion=" + cgrVersion + '}';
    }
    
    

}
