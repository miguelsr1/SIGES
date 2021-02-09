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
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSubModalidadAtencion;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_periodos_calendario",schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cesPk", scope = SgPeriodoCalendario.class)
@Audited
public class SgPeriodoCalendario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ces_pk", nullable = false)
    private Long cesPk;
    
    @Size(max = 500)
    @Column(name = "ces_nombre", length = 500)
    @AtributoNombre
    private String cesNombre;

    @Column(name = "ces_fecha_desde")
    private LocalDate cesFechaDesde;
    
   
    @Column(name = "ces_fecha_hasta")
    private LocalDate cesFechaHasta;
    
    @Column(name = "ces_habilitado")
    private Boolean cesHabilitado; 
    
    @Column(name = "ces_tipo")
    @Enumerated(EnumType.STRING)
    private EnumCalendarioEscolar cesTipo;
    

    @Column(name = "ces_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cesUltModFecha;

    @Size(max = 45)
    @Column(name = "ces_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cesUltModUsuario;

    @Column(name = "ces_version")
    @Version
    private Integer cesVersion;
    
    @JoinColumn(name = "ces_nivel_fk")
    @ManyToOne
    private SgNivel cesNivel;

    @JoinColumn(name = "ces_modalidad_atencion_fk")
    @ManyToOne
    private SgModalidadAtencion cesModalidadAtencion;
    
    @JoinColumn(name = "ces_sub_modalidad_atencion_fk")
    @ManyToOne
    private SgSubModalidadAtencion cesSubModalidadAtencion;
    
    @JoinColumn(name = "ces_calendario_fk")
    @ManyToOne
    private SgCalendario cesCalendario;

    public SgPeriodoCalendario() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
       
    }

    public Long getCesPk() {
        return cesPk;
    }

    public void setCesPk(Long cesPk) {
        this.cesPk = cesPk;
    }

    public String getCesNombre() {
        return cesNombre;
    }

    public void setCesNombre(String cesNombre) {
        this.cesNombre = cesNombre;
    }
    
    
    public LocalDate getCesFechaDesde() {
        return cesFechaDesde;
    }

    public void setCesFechaDesde(LocalDate cesFechaDesde) {
        this.cesFechaDesde = cesFechaDesde;
    }

    public LocalDate getCesFechaHasta() {
        return cesFechaHasta;
    }

    public void setCesFechaHasta(LocalDate cesFechaHasta) {
        this.cesFechaHasta = cesFechaHasta;
    }

    public EnumCalendarioEscolar getCesTipo() {
        return cesTipo;
    }

    public void setCesTipo(EnumCalendarioEscolar cesTipo) {
        this.cesTipo = cesTipo;
    }
      

    public LocalDateTime getCesUltModFecha() {
        return cesUltModFecha;
    }

    public void setCesUltModFecha(LocalDateTime cesUltModFecha) {
        this.cesUltModFecha = cesUltModFecha;
    }

    public String getCesUltModUsuario() {
        return cesUltModUsuario;
    }

    public void setCesUltModUsuario(String cesUltModUsuario) {
        this.cesUltModUsuario = cesUltModUsuario;
    }

    public Integer getCesVersion() {
        return cesVersion;
    }

    public void setCesVersion(Integer cesVersion) {
        this.cesVersion = cesVersion;
    }

    public SgNivel getCesNivel() {
        return cesNivel;
    }

    public void setCesNivel(SgNivel cesNivel) {
        this.cesNivel = cesNivel;
    }

    public SgModalidadAtencion getCesModalidadAtencion() {
        return cesModalidadAtencion;
    }

    public void setCesModalidadAtencion(SgModalidadAtencion cesModalidadAtencion) {
        this.cesModalidadAtencion = cesModalidadAtencion;
    }

    public SgCalendario getCesCalendario() {
        return cesCalendario;
    }

    public void setCesCalendario(SgCalendario cesCalendario) {
        this.cesCalendario = cesCalendario;
    }

    public Boolean getCesHabilitado() {
        return cesHabilitado;
    }

    public void setCesHabilitado(Boolean cesHabilitado) {
        this.cesHabilitado = cesHabilitado;
    }

    public SgSubModalidadAtencion getCesSubModalidadAtencion() {
        return cesSubModalidadAtencion;
    }

    public void setCesSubModalidadAtencion(SgSubModalidadAtencion cesSubModalidadAtencion) {
        this.cesSubModalidadAtencion = cesSubModalidadAtencion;
    }
        
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cesPk);
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
        final SgPeriodoCalendario other = (SgPeriodoCalendario) obj;
        if (!Objects.equals(this.cesPk, other.cesPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCalendarioEscolar{" + "cesPk=" + cesPk + ", cesUltModFecha=" + cesUltModFecha + ", cesUltModUsuario=" + cesUltModUsuario + ", cesVersion=" + cesVersion + '}';
    }
    
    

}
