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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCarrera;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEspecialidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgModalidadEstudio;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgPais;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoEstudio;

@Entity
@Table(name = "sg_estudios_superiores", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "esuPk", scope = SgEstudioSuperior.class)
public class SgEstudioSuperior implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "esu_pk")
    private Long esuPk;

    @JoinColumn(name = "esu_estudios_fk", referencedColumnName = "ere_pk")
    @ManyToOne
    private SgEstudioRealizado esuEstudios;

    @JoinColumn(name = "esu_tipo_fk", referencedColumnName = "tes_pk")
    @ManyToOne
    private SgTipoEstudio esuTipo;

    @JoinColumn(name = "esu_pais_fk", referencedColumnName = "pai_pk")
    @ManyToOne
    private SgPais esuPais;

    @JoinColumn(name = "esu_carrera_fk", referencedColumnName = "crr_pk")
    @ManyToOne
    private SgCarrera esuCarrera;

    @Column(name = "esu_carrera_txt")
    private String esuCarreraTxt;

    @JoinColumn(name = "esu_especialidad_fk", referencedColumnName = "esp_pk")
    @ManyToOne
    private SgEspecialidad esuEspecialidad;

    @JoinColumn(name = "esu_institucion_estudio_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede esuInstitucionEstudio;

    @Column(name = "esu_institucion_estudio_txt")
    private String esuInstitucionEstudioTxt;

    @Column(name = "esu_desde")
    private LocalDate esuDesde;

    @Column(name = "esu_hasta")
    private LocalDate esuHasta;

    @JoinColumn(name = "esu_modalidad_fk", referencedColumnName = "mes_pk")
    @ManyToOne
    private SgModalidadEstudio esuModalidad;

    @Column(name = "esu_anio_titulacion")
    private Integer esuAnioTitulacion;

    @Column(name = "esu_validado")
    private Boolean esuValidado;

    @Column(name = "esu_ult_val_fecha")
    private LocalDateTime esuUltValidacionFecha;

    @Column(name = "esu_ult_val_usuario")
    private String esuUltValidacionUsuario;

    @Column(name = "esu_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime esuUltModFecha;

    @Size(max = 45)
    @Column(name = "esu_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String esuUltModUsuario;

    @Column(name = "esu_version")
    private Integer esuVersion;

    public SgEstudioSuperior() {
        this.esuValidado = Boolean.FALSE;
    }

    public SgEstudioSuperior(Long esuPk) {
        this.esuPk = esuPk;
    }

    public Long getEsuPk() {
        return esuPk;
    }

    public void setEsuPk(Long esuPk) {
        this.esuPk = esuPk;
    }

    public SgEstudioRealizado getEsuEstudios() {
        return esuEstudios;
    }

    public void setEsuEstudios(SgEstudioRealizado esuEstudios) {
        this.esuEstudios = esuEstudios;
    }

    public SgTipoEstudio getEsuTipo() {
        return esuTipo;
    }

    public void setEsuTipo(SgTipoEstudio esuTipo) {
        this.esuTipo = esuTipo;
    }

    public SgPais getEsuPais() {
        return esuPais;
    }

    public void setEsuPais(SgPais esuPais) {
        this.esuPais = esuPais;
    }

    public SgCarrera getEsuCarrera() {
        return esuCarrera;
    }

    public void setEsuCarrera(SgCarrera esuCarrera) {
        this.esuCarrera = esuCarrera;
    }

    public SgEspecialidad getEsuEspecialidad() {
        return esuEspecialidad;
    }

    public void setEsuEspecialidad(SgEspecialidad esuEspecialidad) {
        this.esuEspecialidad = esuEspecialidad;
    }

    public SgSede getEsuInstitucionEstudio() {
        return esuInstitucionEstudio;
    }

    public void setEsuInstitucionEstudio(SgSede esuInstitucionEstudio) {
        this.esuInstitucionEstudio = esuInstitucionEstudio;
    }

    public LocalDate getEsuDesde() {
        return esuDesde;
    }

    public void setEsuDesde(LocalDate esuDesde) {
        this.esuDesde = esuDesde;
    }

    public LocalDate getEsuHasta() {
        return esuHasta;
    }

    public void setEsuHasta(LocalDate esuHasta) {
        this.esuHasta = esuHasta;
    }

    public SgModalidadEstudio getEsuModalidad() {
        return esuModalidad;
    }

    public void setEsuModalidad(SgModalidadEstudio esuModalidad) {
        this.esuModalidad = esuModalidad;
    }

    public Integer getEsuAnioTitulacion() {
        return esuAnioTitulacion;
    }

    public void setEsuAnioTitulacion(Integer esuAnioTitulacion) {
        this.esuAnioTitulacion = esuAnioTitulacion;
    }

    public LocalDateTime getEsuUltModFecha() {
        return esuUltModFecha;
    }

    public void setEsuUltModFecha(LocalDateTime esuUltModFecha) {
        this.esuUltModFecha = esuUltModFecha;
    }

    public String getEsuUltModUsuario() {
        return esuUltModUsuario;
    }

    public void setEsuUltModUsuario(String esuUltModUsuario) {
        this.esuUltModUsuario = esuUltModUsuario;
    }

    public Integer getEsuVersion() {
        return esuVersion;
    }

    public void setEsuVersion(Integer esuVersion) {
        this.esuVersion = esuVersion;
    }

    public String getEsuCarreraTxt() {
        return esuCarreraTxt;
    }

    public void setEsuCarreraTxt(String esuCarreraTxt) {
        this.esuCarreraTxt = esuCarreraTxt;
    }

    public String getEsuInstitucionEstudioTxt() {
        return esuInstitucionEstudioTxt;
    }

    public void setEsuInstitucionEstudioTxt(String esuInstitucionEstudioTxt) {
        this.esuInstitucionEstudioTxt = esuInstitucionEstudioTxt;
    }

    public Boolean getEsuValidado() {
        return esuValidado;
    }

    public void setEsuValidado(Boolean esuValidado) {
        this.esuValidado = esuValidado;
    }

    public LocalDateTime getEsuUltValidacionFecha() {
        return esuUltValidacionFecha;
    }

    public void setEsuUltValidacionFecha(LocalDateTime esuUltValidacionFecha) {
        this.esuUltValidacionFecha = esuUltValidacionFecha;
    }

    public String getEsuUltValidacionUsuario() {
        return esuUltValidacionUsuario;
    }

    public void setEsuUltValidacionUsuario(String esuUltValidacionUsuario) {
        this.esuUltValidacionUsuario = esuUltValidacionUsuario;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (esuPk != null ? esuPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEstudioSuperior)) {
            return false;
        }
        SgEstudioSuperior other = (SgEstudioSuperior) object;
        if ((this.esuPk == null && other.esuPk != null) || (this.esuPk != null && !this.esuPk.equals(other.esuPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstudioSuperior[ esuPk=" + esuPk + " ]";
    }

}
