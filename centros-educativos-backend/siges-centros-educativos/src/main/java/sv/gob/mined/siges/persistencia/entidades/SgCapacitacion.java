/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAlcanceCapacitacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgModalidadEstudio;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCapacitacion;

@Entity
@Table(name = "sg_capacitaciones", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "capPk", scope = SgCapacitacion.class)
public class SgCapacitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cap_pk")
    private Long capPk;
    
    @JoinColumn(name = "cap_estudios_fk", referencedColumnName = "ere_pk")
    @ManyToOne
    private SgEstudioRealizado capEstudios;
    
    @Size(max = 255)
    @Column(name = "cap_nombre_capacitacion",length = 255)
    private String capNombreCapacitacion;
    
    @JoinColumn(name = "cap_tipo_capacitacion_fk", referencedColumnName = "tca_pk")
    @ManyToOne
    private SgTipoCapacitacion capTipoCapacitacion;
    
    @JoinColumn(name = "cap_alcance_capacitacion_fk", referencedColumnName = "aca_pk")
    @ManyToOne
    private SgAlcanceCapacitacion capAlcanceCapacitacion;
    
    @Column(name = "cap_institucion_estudio", length = 255)
    private String capInstitucionEstudio;
    
    @Column(name = "cap_desde")
    private LocalDate capDesde;
    
    @Column(name = "cap_hasta")
    private LocalDate capHasta;
    
    @Column(name = "cap_duracion_horas")
    private Integer capDuracionHoras;
    
    @JoinColumn(name = "cap_modalidad_fk", referencedColumnName = "mes_pk")
    @ManyToOne
    private SgModalidadEstudio capModalidad;
    
    @Column(name = "cap_curso_acreditado")
    private Boolean capCursoAcreditado;
    
    @Column(name = "cap_validado")
    private Boolean capValidado;
    
    @Column(name = "cap_ult_val_fecha")
    private LocalDateTime capUltValidacionFecha;

    @Column(name = "cap_ult_val_usuario")
    private String capUltValidacionUsuario;
    
    @Column(name = "cap_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime capUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cap_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String capUltModUsuario;
    
    @Column(name = "cap_version")
    private Integer capVersion;

    public SgCapacitacion() {
        this.capValidado = Boolean.FALSE;
    }

    public SgCapacitacion(Long capPk) {
        this.capPk = capPk;
    }

    public Long getCapPk() {
        return capPk;
    }

    public void setCapPk(Long capPk) {
        this.capPk = capPk;
    }

    public String getCapNombreCapacitacion() {
        return capNombreCapacitacion;
    }

    public void setCapNombreCapacitacion(String capNombreCapacitacion) {
        this.capNombreCapacitacion = capNombreCapacitacion;
    }

    public SgEstudioRealizado getCapEstudios() {
        return capEstudios;
    }

    public void setCapEstudios(SgEstudioRealizado capEstudios) {
        this.capEstudios = capEstudios;
    }

    public SgTipoCapacitacion getCapTipoCapacitacion() {
        return capTipoCapacitacion;
    }

    public void setCapTipoCapacitacion(SgTipoCapacitacion capTipoCapacitacion) {
        this.capTipoCapacitacion = capTipoCapacitacion;
    }

    public SgAlcanceCapacitacion getCapAlcanceCapacitacion() {
        return capAlcanceCapacitacion;
    }

    public void setCapAlcanceCapacitacion(SgAlcanceCapacitacion capAlcanceCapacitacion) {
        this.capAlcanceCapacitacion = capAlcanceCapacitacion;
    }

    public String getCapInstitucionEstudio() {
        return capInstitucionEstudio;
    }

    public void setCapInstitucionEstudio(String capInstitucionEstudio) {
        this.capInstitucionEstudio = capInstitucionEstudio;
    }

    public LocalDate getCapDesde() {
        return capDesde;
    }

    public void setCapDesde(LocalDate capDesde) {
        this.capDesde = capDesde;
    }

    public LocalDate getCapHasta() {
        return capHasta;
    }

    public void setCapHasta(LocalDate capHasta) {
        this.capHasta = capHasta;
    }

    public Integer getCapDuracionHoras() {
        return capDuracionHoras;
    }

    public void setCapDuracionHoras(Integer capDuracionHoras) {
        this.capDuracionHoras = capDuracionHoras;
    }

    public SgModalidadEstudio getCapModalidad() {
        return capModalidad;
    }

    public void setCapModalidad(SgModalidadEstudio capModalidad) {
        this.capModalidad = capModalidad;
    }

    public Boolean getCapCursoAcreditado() {
        return capCursoAcreditado;
    }

    public void setCapCursoAcreditado(Boolean capCursoAcreditado) {
        this.capCursoAcreditado = capCursoAcreditado;
    }

    public LocalDateTime getCapUltModFecha() {
        return capUltModFecha;
    }

    public void setCapUltModFecha(LocalDateTime capUltModFecha) {
        this.capUltModFecha = capUltModFecha;
    }

    public String getCapUltModUsuario() {
        return capUltModUsuario;
    }

    public void setCapUltModUsuario(String capUltModUsuario) {
        this.capUltModUsuario = capUltModUsuario;
    }

    public Integer getCapVersion() {
        return capVersion;
    }

    public void setCapVersion(Integer capVersion) {
        this.capVersion = capVersion;
    }

    public Boolean getCapValidado() {
        return capValidado;
    }

    public void setCapValidado(Boolean capValidado) {
        this.capValidado = capValidado;
    }

    public LocalDateTime getCapUltValidacionFecha() {
        return capUltValidacionFecha;
    }

    public void setCapUltValidacionFecha(LocalDateTime capUltValidacionFecha) {
        this.capUltValidacionFecha = capUltValidacionFecha;
    }

    public String getCapUltValidacionUsuario() {
        return capUltValidacionUsuario;
    }

    public void setCapUltValidacionUsuario(String capUltValidacionUsuario) {
        this.capUltValidacionUsuario = capUltValidacionUsuario;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (capPk != null ? capPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCapacitacion)) {
            return false;
        }
        SgCapacitacion other = (SgCapacitacion) object;
        if ((this.capPk == null && other.capPk != null) || (this.capPk != null && !this.capPk.equals(other.capPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCapacitacion[ capPk=" + capPk + " ]";
    }
    
}
