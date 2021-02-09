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
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumNivelSGC;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEscolaridad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMaximoNivelEducativoAlcanzado;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSistemaGestionContenido;

@Entity
@Table(name = "sg_estudios_realizados", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "erePk", scope = SgEstudioRealizado.class)
@Audited
public class SgEstudioRealizado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ere_pk")
    private Long erePk;
    
    @JoinColumn(name = "ere_personal_sede_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgPersonalSedeEducativa erePersonalSede;
    
    @Column(name = "ere_internet")
    private Boolean ereInternet;
    
    @JoinColumn(name = "ere_sistema_gestion_contenido_fk", referencedColumnName = "sgc_pk")
    @ManyToOne
    private SgSistemaGestionContenido ereSistemaGestionContenido;
    
    @Column(name = "ere_nivel_manejo_sgc")
    @Enumerated(EnumType.STRING)
    private EnumNivelSGC ereNivelManejoSgc;
    
    @Size(max = 5000)
    @Column(name = "ere_habilidades")
    private String ereHabilidades;
    
    @Size(max = 5000)
    @Column(name = "ere_destrezas")
    private String ereDestrezas;
    
    @JoinColumn(name = "ere_escolaridad_fk", referencedColumnName = "esc_pk")
    @ManyToOne
    private SgEscolaridad ereEscolaridad;
    
    @Column(name = "ere_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ereUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ere_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ereUltModUsuario;
    
    @Column(name = "ere_version")
    @Version
    private Integer ereVersion;
    
    @OneToMany(mappedBy = "ireEstudiosRealizados")
    @NotAudited
    private List<SgIdiomaRealizado> ereIdiomas;
    
    @OneToMany(mappedBy = "esuEstudios")
    @NotAudited
    private List<SgEstudioSuperior> ereEstudiosSuperiores;
    
    @OneToMany(mappedBy = "capEstudios")
    @NotAudited
    private List<SgCapacitacion> ereCapacitaciones;
    
    
    @JoinColumn(name = "ere_maximo_nivel_educativo_alcanzado_fk", referencedColumnName = "mne_pk")
    @ManyToOne
    private SgMaximoNivelEducativoAlcanzado ereMaximoNivelEducativoAlcanzado;        
            

    public SgEstudioRealizado() {
    }

    public SgEstudioRealizado(Long erePk) {
        this.erePk = erePk;
    }

    public Long getErePk() {
        return erePk;
    }

    public void setErePk(Long erePk) {
        this.erePk = erePk;
    }

    public Boolean getEreInternet() {
        return ereInternet;
    }

    public void setEreInternet(Boolean ereInternet) {
        this.ereInternet = ereInternet;
    }

    public SgSistemaGestionContenido getEreSistemaGestionContenido() {
        return ereSistemaGestionContenido;
    }

    public void setEreSistemaGestionContenido(SgSistemaGestionContenido ereSistemaGestionContenido) {
        this.ereSistemaGestionContenido = ereSistemaGestionContenido;
    }

    public EnumNivelSGC getEreNivelManejoSgc() {
        return ereNivelManejoSgc;
    }

    public void setEreNivelManejoSgc(EnumNivelSGC ereNivelManejoSgc) {
        this.ereNivelManejoSgc = ereNivelManejoSgc;
    }

    public String getEreHabilidades() {
        return ereHabilidades;
    }

    public void setEreHabilidades(String ereHabilidades) {
        this.ereHabilidades = ereHabilidades;
    }

    public String getEreDestrezas() {
        return ereDestrezas;
    }

    public void setEreDestrezas(String ereDestrezas) {
        this.ereDestrezas = ereDestrezas;
    }

    public SgEscolaridad getEreEscolaridad() {
        return ereEscolaridad;
    }

    public void setEreEscolaridad(SgEscolaridad ereEscolaridad) {
        this.ereEscolaridad = ereEscolaridad;
    }

    public LocalDateTime getEreUltModFecha() {
        return ereUltModFecha;
    }

    public void setEreUltModFecha(LocalDateTime ereUltModFecha) {
        this.ereUltModFecha = ereUltModFecha;
    }

    public String getEreUltModUsuario() {
        return ereUltModUsuario;
    }

    public void setEreUltModUsuario(String ereUltModUsuario) {
        this.ereUltModUsuario = ereUltModUsuario;
    }

    public Integer getEreVersion() {
        return ereVersion;
    }

    public void setEreVersion(Integer ereVersion) {
        this.ereVersion = ereVersion;
    }

    public List<SgCapacitacion> getEreCapacitaciones() {
        return ereCapacitaciones;
    }

    public void setEreCapacitaciones(List<SgCapacitacion> ereCapacitaciones) {
        this.ereCapacitaciones = ereCapacitaciones;
    }

    public SgPersonalSedeEducativa getErePersonalSede() {
        return erePersonalSede;
    }

    public void setErePersonalSede(SgPersonalSedeEducativa erePersonalSede) {
        this.erePersonalSede = erePersonalSede;
    }

    public SgMaximoNivelEducativoAlcanzado getEreMaximoNivelEducativoAlcanzado() {
        return ereMaximoNivelEducativoAlcanzado;
    }

    public void setEreMaximoNivelEducativoAlcanzado(SgMaximoNivelEducativoAlcanzado ereMaximoNivelEducativoAlcanzado) {
        this.ereMaximoNivelEducativoAlcanzado = ereMaximoNivelEducativoAlcanzado;
    }

    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (erePk != null ? erePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEstudioRealizado)) {
            return false;
        }
        SgEstudioRealizado other = (SgEstudioRealizado) object;
        if ((this.erePk == null && other.erePk != null) || (this.erePk != null && !this.erePk.equals(other.erePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstudioRealizado[ erePk=" + erePk + " ]";
    }

    public List<SgIdiomaRealizado> getEreIdiomas() {
        return ereIdiomas;
    }

    public void setEreIdiomas(List<SgIdiomaRealizado> ereIdiomas) {
        this.ereIdiomas = ereIdiomas;
    }

    public List<SgEstudioSuperior> getEreEstudiosSuperiores() {
        return ereEstudiosSuperiores;
    }

    public void setEreEstudiosSuperiores(List<SgEstudioSuperior> ereEstudiosSuperiores) {
        this.ereEstudiosSuperiores = ereEstudiosSuperiores;
    }
    
}
