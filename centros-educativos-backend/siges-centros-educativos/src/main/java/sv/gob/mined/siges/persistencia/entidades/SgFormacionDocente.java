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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCategoriaFormacionDocente;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEspecialidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgModuloFormacionDocente;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgNivelFormacionDocente;

@Entity
@Table(name = "sg_formaciones_docente", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "fdoPk", scope = SgFormacionDocente.class)
public class SgFormacionDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //IMPORTANTE: si se agrega un nuevo campo, verificar public void personalSedeEducativa(SgPersonalSedeEducativa var) de FormacionDocenteBean.
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fdo_pk")
    private Long fdoPk;
    
    @JoinColumn(name = "fdo_personal_sede_fk", referencedColumnName = "pse_pk", updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgPersonalSedeEducativa fdoPersonalSede;
    
    @JoinColumn(name = "fdo_nivel_fk", referencedColumnName = "nfd_pk")
    @ManyToOne
    private SgNivelFormacionDocente fdoNivel;
    
    @JoinColumn(name = "fdo_categoria_fk", referencedColumnName = "cfd_pk")
    @ManyToOne
    private SgCategoriaFormacionDocente fdoCategoria;
    
    @JoinColumn(name = "fdo_especialidad_fk", referencedColumnName = "esp_pk")
    @ManyToOne
    private SgEspecialidad fdoEspecialidad;
    
    @JoinColumn(name = "fdo_modulo_fk", referencedColumnName = "mfd_pk")
    @ManyToOne
    private SgModuloFormacionDocente fdoModulo;
    
    @Column(name = "fdo_fecha_desde")
    private LocalDate fdoFechaDesde;
    
    @Column(name = "fdo_fecha_hasta")
    private LocalDate fdoFechaHasta;
    
    @JoinColumn(name = "fdo_departamento_fk", referencedColumnName = "dep_pk")
    @ManyToOne
    private SgDepartamento fdoDepartamento;
    
    @JoinColumn(name = "fdo_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede fdoSede;
    
    @Column(name = "fdo_aprobado")
    private Boolean fdoAprobado;
    
    @Size(max = 20)
    @Column(name = "fdo_calificacion_final",length = 20)
    private String fdoCalificacionFinal;    
    
    
    
    @Column(name = "fdo_ult_mod_fecha")
    private LocalDateTime fdoUltModFecha;
    
    @Size(max = 45)
    @Column(name = "fdo_ult_mod_usuario",length = 45)
    private String fdoUltModUsuario;
    
    @Column(name = "fdo_version")
    private Integer fdoVersion;

    public SgFormacionDocente() {
    }

    public SgFormacionDocente(Long fdoPk) {
        this.fdoPk = fdoPk;
    }

    public Long getFdoPk() {
        return fdoPk;
    }

    public void setFdoPk(Long fdoPk) {
        this.fdoPk = fdoPk;
    }

    public SgNivelFormacionDocente getFdoNivel() {
        return fdoNivel;
    }

    public void setFdoNivel(SgNivelFormacionDocente fdoNivel) {
        this.fdoNivel = fdoNivel;
    }

    public SgCategoriaFormacionDocente getFdoCategoria() {
        return fdoCategoria;
    }

    public void setFdoCategoria(SgCategoriaFormacionDocente fdoCategoria) {
        this.fdoCategoria = fdoCategoria;
    }

    public SgEspecialidad getFdoEspecialidad() {
        return fdoEspecialidad;
    }

    public void setFdoEspecialidad(SgEspecialidad fdoEspecialidad) {
        this.fdoEspecialidad = fdoEspecialidad;
    }

    public SgModuloFormacionDocente getFdoModulo() {
        return fdoModulo;
    }

    public void setFdoModulo(SgModuloFormacionDocente fdoModulo) {
        this.fdoModulo = fdoModulo;
    }

    public LocalDate getFdoFechaDesde() {
        return fdoFechaDesde;
    }

    public void setFdoFechaDesde(LocalDate fdoFechaDesde) {
        this.fdoFechaDesde = fdoFechaDesde;
    }

    public LocalDate getFdoFechaHasta() {
        return fdoFechaHasta;
    }

    public void setFdoFechaHasta(LocalDate fdoFechaHasta) {
        this.fdoFechaHasta = fdoFechaHasta;
    }

    public SgDepartamento getFdoDepartamento() {
        return fdoDepartamento;
    }

    public void setFdoDepartamento(SgDepartamento fdoDepartamento) {
        this.fdoDepartamento = fdoDepartamento;
    }

    public SgSede getFdoSede() {
        return fdoSede;
    }

    public void setFdoSede(SgSede fdoSede) {
        this.fdoSede = fdoSede;
    }

    public Boolean getFdoAprobado() {
        return fdoAprobado;
    }

    public void setFdoAprobado(Boolean fdoAprobado) {
        this.fdoAprobado = fdoAprobado;
    }

    public String getFdoCalificacionFinal() {
        return fdoCalificacionFinal;
    }

    public void setFdoCalificacionFinal(String fdoCalificacionFinal) {
        this.fdoCalificacionFinal = fdoCalificacionFinal;
    }

    public LocalDateTime getFdoUltModFecha() {
        return fdoUltModFecha;
    }

    public void setFdoUltModFecha(LocalDateTime fdoUltModFecha) {
        this.fdoUltModFecha = fdoUltModFecha;
    }

    public String getFdoUltModUsuario() {
        return fdoUltModUsuario;
    }

    public void setFdoUltModUsuario(String fdoUltModUsuario) {
        this.fdoUltModUsuario = fdoUltModUsuario;
    }

    public Integer getFdoVersion() {
        return fdoVersion;
    }

    public void setFdoVersion(Integer fdoVersion) {
        this.fdoVersion = fdoVersion;
    }

    public SgPersonalSedeEducativa getFdoPersonalSede() {
        return fdoPersonalSede;
    }

    public void setFdoPersonalSede(SgPersonalSedeEducativa fdoPersonalSede) {
        this.fdoPersonalSede = fdoPersonalSede;
    }

    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fdoPk != null ? fdoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgFormacionDocente)) {
            return false;
        }
        SgFormacionDocente other = (SgFormacionDocente) object;
        if ((this.fdoPk == null && other.fdoPk != null) || (this.fdoPk != null && !this.fdoPk.equals(other.fdoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgFormacionDocente[ fdoPk=" + fdoPk + " ]";
    }
    
}
