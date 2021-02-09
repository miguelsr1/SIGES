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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumActividadCalendario;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_actividad_calendario", uniqueConstraints = {
    @UniqueConstraint(name = "aca_nombre_uk", columnNames = {"aca_nombre"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "acaPk", scope = SgActividadCalendario.class)
@Audited
public class SgActividadCalendario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aca_pk", nullable = false)
    private Long acaPk;

    @Size(max = 255)
    @Column(name = "aca_nombre", length = 255)
    @AtributoNormalizable
    private String acaNombre;
    
    @JoinColumn(name = "aca_sede_fk")
    @ManyToOne
    private SgSede acaSede;
    
    @JoinColumn(name = "aca_departamento_fk")
    @ManyToOne
    private SgDepartamento acaDepartamento;
    
    @Column(name = "aca_ambito")
    @Enumerated(value = EnumType.STRING)
    private EnumAmbito acaAmbito;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "aca_nombre_busqueda", length = 255)
    private String acaNombreBusqueda;
    
    @Size(max = 255)
    @Column(name = "aca_descripcion",length = 255)
    private String acaDescripcion;
    
    
    @Column(name = "aca_fecha_desde")
    private LocalDate acaFechaDesde;
    
   
    @Column(name = "aca_fecha_hasta")
    private LocalDate acaFechaHasta;

    
    @Column(name = "aca_tipo")
    @Enumerated(value = EnumType.STRING)
    private EnumActividadCalendario acaTipo;

    @Column(name = "aca_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime acaUltModFecha;

    @Size(max = 45)
    @Column(name = "aca_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String acaUltModUsuario;

    @Column(name = "aca_version")
    @Version
    private Integer acaVersion;
            
    @JoinColumn(name = "aca_calendario_fk", referencedColumnName = "cal_pk")
    @ManyToOne
    private SgCalendario acaCalendario;
    
    @Column(name = "aca_dia_lectivo")
    private Boolean acaDiaLectivo;
    

    public SgActividadCalendario() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.acaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.acaNombre);
    }

    public Long getAcaPk() {
        return acaPk;
    }

    public void setAcaPk(Long acaPk) {
        this.acaPk = acaPk;
    }


    public String getAcaNombre() {
        return acaNombre;
    }

    public void setAcaNombre(String acaNombre) {
        this.acaNombre = acaNombre;
    }

    public String getAcaNombreBusqueda() {
        return acaNombreBusqueda;
    }

    public void setAcaNombreBusqueda(String acaNombreBusqueda) {
        this.acaNombreBusqueda = acaNombreBusqueda;
    }

    public EnumActividadCalendario getAcaTipo() {
        return acaTipo;
    }

    public void setAcaTipo(EnumActividadCalendario acaTipo) {
        this.acaTipo = acaTipo;
    }

    public LocalDate getAcaFechaDesde() {
        return acaFechaDesde;
    }

    public void setAcaFechaDesde(LocalDate acaFechaDesde) {
        this.acaFechaDesde = acaFechaDesde;
    }

    public LocalDate getAcaFechaHasta() {
        return acaFechaHasta;
    }

    public void setAcaFechaHasta(LocalDate acaFechaHasta) {
        this.acaFechaHasta = acaFechaHasta;
    }

    
    public String getAcaDescripcion() {
        return acaDescripcion;
    }

    public void setAcaDescripcion(String acaDescripcion) {
        this.acaDescripcion = acaDescripcion;
    }
        

    public LocalDateTime getAcaUltModFecha() {
        return acaUltModFecha;
    }

    public void setAcaUltModFecha(LocalDateTime acaUltModFecha) {
        this.acaUltModFecha = acaUltModFecha;
    }

    public String getAcaUltModUsuario() {
        return acaUltModUsuario;
    }

    public void setAcaUltModUsuario(String acaUltModUsuario) {
        this.acaUltModUsuario = acaUltModUsuario;
    }

    public Integer getAcaVersion() {
        return acaVersion;
    }

    public void setAcaVersion(Integer acaVersion) {
        this.acaVersion = acaVersion;
    }

    public SgCalendario getAcaCalendario() {
        return acaCalendario;
    }

    public void setAcaCalendario(SgCalendario acaCalendario) {
        this.acaCalendario = acaCalendario;
    }

    public SgSede getAcaSede() {
        return acaSede;
    }

    public void setAcaSede(SgSede acaSede) {
        this.acaSede = acaSede;
    }

    public SgDepartamento getAcaDepartamento() {
        return acaDepartamento;
    }

    public void setAcaDepartamento(SgDepartamento acaDepartamento) {
        this.acaDepartamento = acaDepartamento;
    }

    public EnumAmbito getAcaAmbito() {
        return acaAmbito;
    }

    public void setAcaAmbito(EnumAmbito acaAmbito) {
        this.acaAmbito = acaAmbito;
    }
    
    public Boolean getAcaDiaLectivo() {
        return acaDiaLectivo;
    }

    public void setAcaDiaLectivo(Boolean acaDiaLectivo) {
        this.acaDiaLectivo = acaDiaLectivo;
    }

           
    @Override
    public int hashCode() {
        return Objects.hashCode(this.acaPk);
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
        final SgActividadCalendario other = (SgActividadCalendario) obj;
        if (!Objects.equals(this.acaPk, other.acaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgActividadCalendario{" + "acaPk=" + acaPk + ", acaNombre=" + acaNombre + ", acaNombreBusqueda=" + acaNombreBusqueda + ", acaUltModFecha=" + acaUltModFecha + ", acaUltModUsuario=" + acaUltModUsuario + ", acaVersion=" + acaVersion + '}';
    }

}
