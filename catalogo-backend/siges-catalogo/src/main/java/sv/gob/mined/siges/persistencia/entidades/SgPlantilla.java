/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
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
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_plantilla", uniqueConstraints = {
    @UniqueConstraint(name = "pla_codigo_uk", columnNames = {"pla_codigo"})
    ,
    @UniqueConstraint(name = "pla_nombre_uk", columnNames = {"pla_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "plaPk", scope = SgPlantilla.class)
@Audited
public class SgPlantilla implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pla_pk", nullable = false)
    private Long plaPk;

    @Size(max = 45)
    @Column(name = "pla_codigo", length = 45)
    @AtributoCodigo
    private String plaCodigo;

    @Size(max = 255)
    @Column(name = "pla_nombre", length = 255)
    @AtributoNormalizable
    private String plaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "pla_nombre_busqueda", length = 255)
    private String plaNombreBusqueda;

    @Column(name = "pla_habilitado")
    @AtributoHabilitado
    private Boolean plaHabilitado;
    
    @Size(max = 500)
    @Column(name = "pla_descripcion", length = 500)
    private String plaDescripcion;
    
    @Column(name = "pla_fecha_habilitada")
    private LocalDate plaFechaHabilitada;
    
    @Column(name = "pla_fecha_deshabilitada")
    private LocalDate plaFechaDeshabilitada;

    @Column(name = "pla_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime plaUltModFecha;

    @Size(max = 45)
    @Column(name = "pla_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String plaUltModUsuario;

    @Column(name = "pla_version")
    @Version
    private Integer plaVersion;
    
    @JoinColumn(name = "pla_archivo_fk")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgArchivo plaArchivo;
    
    @JoinColumn(name = "pla_organizacion_curricular_fk")
    @ManyToOne
    private SgOrganizacionCurricular plaOrganizacionCurricular;
    
    @Column(name = "pla_habilitada_reemplazo_org")
    private Boolean plaHabilitadaReemplazoOrg;

    public SgPlantilla() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.plaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.plaNombre);
        if (plaHabilitadaReemplazoOrg == null){
            plaHabilitadaReemplazoOrg = false;
        }
    }

    public Long getPlaPk() {
        return plaPk;
    }

    public void setPlaPk(Long plaPk) {
        this.plaPk = plaPk;
    }

    public String getPlaCodigo() {
        return plaCodigo;
    }

    public void setPlaCodigo(String plaCodigo) {
        this.plaCodigo = plaCodigo;
    }

    public String getPlaNombre() {
        return plaNombre;
    }

    public void setPlaNombre(String plaNombre) {
        this.plaNombre = plaNombre;
    }

    public String getPlaNombreBusqueda() {
        return plaNombreBusqueda;
    }

    public void setPlaNombreBusqueda(String plaNombreBusqueda) {
        this.plaNombreBusqueda = plaNombreBusqueda;
    }

    public String getPlaDescripcion() {
        return plaDescripcion;
    }

    public void setPlaDescripcion(String plaDescripcion) {
        this.plaDescripcion = plaDescripcion;
    }

    public Boolean getPlaHabilitado() {
        return plaHabilitado;
    }

    public void setPlaHabilitado(Boolean plaHabilitado) {
        this.plaHabilitado = plaHabilitado;
    }

    public LocalDateTime getPlaUltModFecha() {
        return plaUltModFecha;
    }

    public void setPlaUltModFecha(LocalDateTime plaUltModFecha) {
        this.plaUltModFecha = plaUltModFecha;
    }

    public String getPlaUltModUsuario() {
        return plaUltModUsuario;
    }

    public void setPlaUltModUsuario(String plaUltModUsuario) {
        this.plaUltModUsuario = plaUltModUsuario;
    }

    public Integer getPlaVersion() {
        return plaVersion;
    }

    public void setPlaVersion(Integer plaVersion) {
        this.plaVersion = plaVersion;
    }

    public SgArchivo getPlaArchivo() {
        return plaArchivo;
    }

    public void setPlaArchivo(SgArchivo plaArchivo) {
        this.plaArchivo = plaArchivo;
    }

    public LocalDate getPlaFechaHabilitada() {
        return plaFechaHabilitada;
    }

    public void setPlaFechaHabilitada(LocalDate plaFechaHabilitada) {
        this.plaFechaHabilitada = plaFechaHabilitada;
    }

    public LocalDate getPlaFechaDeshabilitada() {
        return plaFechaDeshabilitada;
    }

    public void setPlaFechaDeshabilitada(LocalDate plaFechaDeshabilitada) {
        this.plaFechaDeshabilitada = plaFechaDeshabilitada;
    }

    public SgOrganizacionCurricular getPlaOrganizacionCurricular() {
        return plaOrganizacionCurricular;
    }

    public void setPlaOrganizacionCurricular(SgOrganizacionCurricular plaOrganizacionCurricular) {
        this.plaOrganizacionCurricular = plaOrganizacionCurricular;
    }

    public Boolean getPlaHabilitadaReemplazoOrg() {
        return plaHabilitadaReemplazoOrg;
    }

    public void setPlaHabilitadaReemplazoOrg(Boolean plaHabilitadaReemplazoOrg) {
        this.plaHabilitadaReemplazoOrg = plaHabilitadaReemplazoOrg;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.plaPk);
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
        final SgPlantilla other = (SgPlantilla) obj;
        if (!Objects.equals(this.plaPk, other.plaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgPlantilla{" + "plaPk=" + plaPk + ", plaCodigo=" + plaCodigo + ", plaNombre=" + plaNombre + ", plaNombreBusqueda=" + plaNombreBusqueda + ", plaHabilitado=" + plaHabilitado + ", plaUltModFecha=" + plaUltModFecha + ", plaUltModUsuario=" + plaUltModUsuario + ", plaVersion=" + plaVersion + '}';
    }
    
    

}
