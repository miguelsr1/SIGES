/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMedioTransporte;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgNacionalidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSexo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoParentesco;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_menores_encuesta_estudiante", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "menPk", scope = SgMenorEncuestaEstudiante.class)
@Audited
public class SgMenorEncuestaEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "men_pk", nullable = false)
    private Long menPk;

    @Column(name = "men_matriculado_siges")
    private Boolean menMatriculadoSiges;

    @Column(name = "men_tiene_nie")
    private Boolean menTieneNie;

    @Column(name = "men_nie")
    private Long menNie;

    @Column(name = "men_estudia")
    private Boolean menEstudia;

//    @JoinColumn(name = "men_allegado_fk")
//    @LazyToOne(LazyToOneOption.NO_PROXY)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private SgAllegado menAllegado;
//    
    @Column(name = "men_persona_fk")
    private Long menPersonaFk;

    @Column(name = "men_estudiante_fk")
    private Long menEstudianteFk; //En caso de ser estudiante almacenamos la fk

    @Column(name = "men_es_familiar")
    private Boolean menEsFamiliar;

    @JoinColumn(name = "men_tipo_parentesco")
    @ManyToOne
    private SgTipoParentesco menTipoParentesco;

    @Size(max = 100)
    @Column(name = "men_primer_nombre")
    @AtributoNormalizable
    private String menPrimerNombre;

    @Size(max = 100)
    @Column(name = "men_segundo_nombre")
    @AtributoNormalizable
    private String menSegundoNombre;

    @Size(max = 100)
    @Column(name = "men_primer_apellido")
    @AtributoNormalizable
    private String menPrimerApellido;

    @Size(max = 100)
    @Column(name = "men_segundo_apellido")
    @AtributoNormalizable
    private String menSegundoApellido;

    @JoinColumn(name = "men_sexo_fk")
    @ManyToOne
    private SgSexo menSexo;

    @JoinColumn(name = "men_estado_civil_fk")
    @ManyToOne
    private SgEstadoCivil menEstadoCivil;

    @JoinColumn(name = "men_nacionalidad_fk")
    @ManyToOne
    private SgNacionalidad menNacionalidad;

    @Column(name = "men_fecha_nacimiento")
    private LocalDate menFechaNacimiento;

    @JoinColumn(name = "men_sede_fk")
    @ManyToOne
    private SgSedeLite menSede;

    @Column(name = "men_validado_siges")
    private Boolean menValidadoSIGES;

    @JoinColumn(name = "men_medio_transporte_fk")
    @ManyToOne
    private SgMedioTransporte menMedioTransporte;

    @JoinColumn(name = "men_encuesta_estudiante_fk")
    @LazyToOne(LazyToOneOption.NO_PROXY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SgEncuestaEstudiante menEncuestaEstudiante;

    @Column(name = "men_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime menUltModFecha;

    @Size(max = 45)
    @Column(name = "men_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String menUltModUsuario;

    @Column(name = "men_version")
    @Version
    private Integer menVersion;

    public SgMenorEncuestaEstudiante() {

    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        if (BooleanUtils.isFalse(menTieneNie)) {
            menNie = null;
        }
    }

    @JsonIgnore
    public String getMenNombreCompleto() {
        StringBuilder s = new StringBuilder();
        if (this.menPrimerApellido != null) {
            s.append(this.menPrimerApellido).append(" ");
        }
        if (this.menSegundoApellido != null) {
            s.append(this.menSegundoApellido).append(" ");
        }
        if (s.toString().endsWith(" ")) {
            s.deleteCharAt(s.toString().length() - 1);
        }
        s.append(", ");
        if (this.menPrimerNombre != null) {
            s.append(this.menPrimerNombre).append(" ");
        }
        if (this.menSegundoNombre != null) {
            s.append(this.menSegundoNombre).append(" ");
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    public Long getMenPk() {
        return menPk;
    }

    public void setMenPk(Long menPk) {
        this.menPk = menPk;
    }

    public Boolean getMenMatriculadoSiges() {
        return menMatriculadoSiges;
    }

    public void setMenMatriculadoSiges(Boolean menMatriculadoSiges) {
        this.menMatriculadoSiges = menMatriculadoSiges;
    }

    public Boolean getMenTieneNie() {
        return menTieneNie;
    }

    public void setMenTieneNie(Boolean menTieneNie) {
        this.menTieneNie = menTieneNie;
    }

    public Boolean getMenEstudia() {
        return menEstudia;
    }

    public void setMenEstudia(Boolean menEstudia) {
        this.menEstudia = menEstudia;
    }

    public SgSedeLite getMenSede() {
        return menSede;
    }

    public void setMenSede(SgSedeLite menSede) {
        this.menSede = menSede;
    }

    public LocalDateTime getMenUltModFecha() {
        return menUltModFecha;
    }

    public void setMenUltModFecha(LocalDateTime menUltModFecha) {
        this.menUltModFecha = menUltModFecha;
    }

    public String getMenUltModUsuario() {
        return menUltModUsuario;
    }

    public void setMenUltModUsuario(String menUltModUsuario) {
        this.menUltModUsuario = menUltModUsuario;
    }

    public Integer getMenVersion() {
        return menVersion;
    }

    public void setMenVersion(Integer menVersion) {
        this.menVersion = menVersion;
    }

    public SgEncuestaEstudiante getMenEncuestaEstudiante() {
        return menEncuestaEstudiante;
    }

    public void setMenEncuestaEstudiante(SgEncuestaEstudiante menEncuestaEstudiante) {
        this.menEncuestaEstudiante = menEncuestaEstudiante;
    }

    public Boolean getMenValidadoSIGES() {
        return menValidadoSIGES;
    }

    public void setMenValidadoSIGES(Boolean menValidadoSIGES) {
        this.menValidadoSIGES = menValidadoSIGES;
    }

    public SgMedioTransporte getMenMedioTransporte() {
        return menMedioTransporte;
    }

    public void setMenMedioTransporte(SgMedioTransporte menMedioTransporte) {
        this.menMedioTransporte = menMedioTransporte;
    }

    public Long getMenNie() {
        return menNie;
    }

    public void setMenNie(Long menNie) {
        this.menNie = menNie;
    }

    public Long getMenPersonaFk() {
        return menPersonaFk;
    }

    public void setMenPersonaFk(Long menPersonaFk) {
        this.menPersonaFk = menPersonaFk;
    }

    public Long getMenEstudianteFk() {
        return menEstudianteFk;
    }

    public void setMenEstudianteFk(Long menEstudianteFk) {
        this.menEstudianteFk = menEstudianteFk;
    }

    public Boolean getMenEsFamiliar() {
        return menEsFamiliar;
    }

    public void setMenEsFamiliar(Boolean menEsFamiliar) {
        this.menEsFamiliar = menEsFamiliar;
    }

    public SgTipoParentesco getMenTipoParentesco() {
        return menTipoParentesco;
    }

    public void setMenTipoParentesco(SgTipoParentesco menTipoParentesco) {
        this.menTipoParentesco = menTipoParentesco;
    }

    public String getMenPrimerNombre() {
        return menPrimerNombre;
    }

    public void setMenPrimerNombre(String menPrimerNombre) {
        this.menPrimerNombre = menPrimerNombre;
    }

    public String getMenSegundoNombre() {
        return menSegundoNombre;
    }

    public void setMenSegundoNombre(String menSegundoNombre) {
        this.menSegundoNombre = menSegundoNombre;
    }

    public String getMenPrimerApellido() {
        return menPrimerApellido;
    }

    public void setMenPrimerApellido(String menPrimerApellido) {
        this.menPrimerApellido = menPrimerApellido;
    }

    public String getMenSegundoApellido() {
        return menSegundoApellido;
    }

    public void setMenSegundoApellido(String menSegundoApellido) {
        this.menSegundoApellido = menSegundoApellido;
    }

    public SgSexo getMenSexo() {
        return menSexo;
    }

    public void setMenSexo(SgSexo menSexo) {
        this.menSexo = menSexo;
    }

    public SgEstadoCivil getMenEstadoCivil() {
        return menEstadoCivil;
    }

    public void setMenEstadoCivil(SgEstadoCivil menEstadoCivil) {
        this.menEstadoCivil = menEstadoCivil;
    }

    public SgNacionalidad getMenNacionalidad() {
        return menNacionalidad;
    }

    public void setMenNacionalidad(SgNacionalidad menNacionalidad) {
        this.menNacionalidad = menNacionalidad;
    }

    public LocalDate getMenFechaNacimiento() {
        return menFechaNacimiento;
    }

    public void setMenFechaNacimiento(LocalDate menFechaNacimiento) {
        this.menFechaNacimiento = menFechaNacimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (menPk != null ? menPk.hashCode() : 0);
        return hash;
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
        final SgMenorEncuestaEstudiante other = (SgMenorEncuestaEstudiante) obj;
        if (!Objects.equals(this.menPk, other.menPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMenorEncuestaEstudiante{" + "menPk=" + menPk + ", menPersonaFk=" + menPersonaFk + ", menEstudianteFk=" + menEstudianteFk + '}';
    }

}
