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
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProgramaEducativo;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_escolaridad_estudiante", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "escPk", scope = SgEscolaridadEstudiante.class)
@Audited
public class SgEscolaridadEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "esc_pk", nullable = false)
    private Long escPk;
    
    @JoinColumn(name = "esc_estudiante", referencedColumnName = "est_pk")
    @ManyToOne
    private SgEstudiante escEstudiante;
    
    @JoinColumn(name = "esc_servicio_educativo", referencedColumnName = "sdu_pk")
    @ManyToOne
    private SgServicioEducativo escServicioEducativo; //No utilizado cuando es equivalencia
    
    @JoinColumn(name = "esc_anio")
    @ManyToOne
    private SgAnioLectivo escAnio; //No utilizado cuando es equivalencia
    
    @Column(name = "esc_resultado")
    @Enumerated(value = EnumType.STRING)
    private EnumPromovidoCalificacion escResultado;
    
    @Column(name = "esc_asistencias")
    private Integer escAsistencias;
    
    @Column(name = "esc_inasistencias")
    private Integer escInasistencias;

    @Column(name = "esc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime escUltModFecha;

    @Size(max = 45)
    @Column(name = "esc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String escUltModUsuario;

    @Column(name = "esc_version")
    @Version
    private Integer escVersion;
    
    
    @JoinColumn(name = "esc_matricula_fk", referencedColumnName = "mat_pk")
    @ManyToOne
    private SgMatricula escMatriculaFk; // No utilizado cuando es equivalencia
    
    @Column(name = "esc_creado_cierre")
    private Boolean escCreadoCierre;
    
    //Datos escolaridad por equivalencia
    
    @Column(name = "esc_generada_equivalencia")
    private Boolean escGeneradaPorEquivalencia;
    
    @Column(name = "esc_eq_numero_tramite")
    private String escEqNumeroTramite;
        
    @Column(name = "esc_eq_fecha_tramite")
    private LocalDate escEqFechaTramite;
    
    @Column(name = "esc_eq_anio")
    private Integer escEqAnio;
    
    @JoinColumn(name = "esc_eq_grado")
    @ManyToOne
    private SgGrado escEqGrado;
    
    @JoinColumn(name = "esc_eq_opcion")
    @ManyToOne
    private SgOpcion escEqOpcion;
    
    @JoinColumn(name = "esc_eq_programa")
    @ManyToOne
    private SgProgramaEducativo escEqProgramaEducativo;
    
    @Column(name = "esc_nombre_sede")
    private String escNombreSede;
    
    @JoinColumn(name = "esc_eq_plan_estudio")
    @ManyToOne
    private SgPlanEstudio escEqPlanEstudio; 
    
    public SgEscolaridadEstudiante() {
    }

    public Long getEscPk() {
        return escPk;
    }

    public void setEscPk(Long escPk) {
        this.escPk = escPk;
    }

    public SgEstudiante getEscEstudiante() {
        return escEstudiante;
    }

    public void setEscEstudiante(SgEstudiante escEstudiante) {
        this.escEstudiante = escEstudiante;
    }

    public SgServicioEducativo getEscServicioEducativo() {
        return escServicioEducativo;
    }

    public void setEscServicioEducativo(SgServicioEducativo escServicioEducativo) {
        this.escServicioEducativo = escServicioEducativo;
    }

    public SgAnioLectivo getEscAnio() {
        return escAnio;
    }

    public void setEscAnio(SgAnioLectivo escAnio) {
        this.escAnio = escAnio;
    }

    public EnumPromovidoCalificacion getEscResultado() {
        return escResultado;
    }

    public void setEscResultado(EnumPromovidoCalificacion escResultado) {
        this.escResultado = escResultado;
    }

    public Integer getEscAsistencias() {
        return escAsistencias;
    }

    public void setEscAsistencias(Integer escAsistencias) {
        this.escAsistencias = escAsistencias;
    }

    public Integer getEscInasistencias() {
        return escInasistencias;
    }

    public void setEscInasistencias(Integer escInasistencias) {
        this.escInasistencias = escInasistencias;
    }

   

    public LocalDateTime getEscUltModFecha() {
        return escUltModFecha;
    }

    public void setEscUltModFecha(LocalDateTime escUltModFecha) {
        this.escUltModFecha = escUltModFecha;
    }

    public String getEscUltModUsuario() {
        return escUltModUsuario;
    }

    public void setEscUltModUsuario(String escUltModUsuario) {
        this.escUltModUsuario = escUltModUsuario;
    }

    public Integer getEscVersion() {
        return escVersion;
    }

    public void setEscVersion(Integer escVersion) {
        this.escVersion = escVersion;
    }

    public SgMatricula getEscMatriculaFk() {
        return escMatriculaFk;
    }

    public void setEscMatriculaFk(SgMatricula escMatriculaFk) {
        this.escMatriculaFk = escMatriculaFk;
    }

    public Boolean getEscCreadoCierre() {
        return escCreadoCierre;
    }

    public void setEscCreadoCierre(Boolean escCreadoCierre) {
        this.escCreadoCierre = escCreadoCierre;
    }

    public Boolean getEscGeneradaPorEquivalencia() {
        return escGeneradaPorEquivalencia;
    }

    public void setEscGeneradaPorEquivalencia(Boolean escGeneradaPorEquivalencia) {
        this.escGeneradaPorEquivalencia = escGeneradaPorEquivalencia;
    }

    public String getEscEqNumeroTramite() {
        return escEqNumeroTramite;
    }

    public void setEscEqNumeroTramite(String escEqNumeroTramite) {
        this.escEqNumeroTramite = escEqNumeroTramite;
    }

    public Integer getEscEqAnio() {
        return escEqAnio;
    }

    public void setEscEqAnio(Integer escEqAnio) {
        this.escEqAnio = escEqAnio;
    }

    public SgGrado getEscEqGrado() {
        return escEqGrado;
    }

    public void setEscEqGrado(SgGrado escEqGrado) {
        this.escEqGrado = escEqGrado;
    }

    public SgOpcion getEscEqOpcion() {
        return escEqOpcion;
    }

    public void setEscEqOpcion(SgOpcion escEqOpcion) {
        this.escEqOpcion = escEqOpcion;
    }

    public SgProgramaEducativo getEscEqProgramaEducativo() {
        return escEqProgramaEducativo;
    }

    public void setEscEqProgramaEducativo(SgProgramaEducativo escEqProgramaEducativo) {
        this.escEqProgramaEducativo = escEqProgramaEducativo;
    }

    public String getEscNombreSede() {
        return escNombreSede;
    }

    public void setEscNombreSede(String escNombreSede) {
        this.escNombreSede = escNombreSede;
    }

    public SgPlanEstudio getEscEqPlanEstudio() {
        return escEqPlanEstudio;
    }

    public void setEscEqPlanEstudio(SgPlanEstudio escEqPlanEstudio) {
        this.escEqPlanEstudio = escEqPlanEstudio;
    }

    public LocalDate getEscEqFechaTramite() {
        return escEqFechaTramite;
    }

    public void setEscEqFechaTramite(LocalDate escEqFechaTramite) {
        this.escEqFechaTramite = escEqFechaTramite;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.escPk);
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
        final SgEscolaridadEstudiante other = (SgEscolaridadEstudiante) obj;
        if (!Objects.equals(this.escPk, other.escPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEscolaridadEstudiante{" + "escPk=" + escPk + ", escEstudiante=" + escEstudiante + ", escServicioEducativo=" + escServicioEducativo + ", escAnio=" + escAnio + ", escResultado=" + escResultado + ", escAsistencias=" + escAsistencias + ", escInasistencias=" + escInasistencias + ", escUltModFecha=" + escUltModFecha + ", escUltModUsuario=" + escUltModUsuario + ", escVersion=" + escVersion + '}';
    }

    

}
