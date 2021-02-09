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
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumTipoPeriodoSeccion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSubModalidadAtencion;


/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_periodos_calificacion",  schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "pcaPk", scope = SgPeriodoCalificacion.class)
@Audited
public class SgPeriodoCalificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pca_pk", nullable = false)
    private Long pcaPk;
    
    @Size(max = 255)
    @Column(name = "pca_nombre", length = 255)
    private String pcaNombre;
    
    @Column(name = "pca_numero")
    private Integer pcaNumero;


    @Column(name = "pca_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pcaUltModFecha;

    @Size(max = 45)
    @Column(name = "pca_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String pcaUltModUsuario;
    
    @Column(name = "pca_permite_calificar_sin_nie")
    private Boolean pcaPermiteCalificarSinNie;

    @Column(name = "pca_version")
    @Version
    private Integer pcaVersion;
    
    @JoinColumn(name = "pca_modalidad_educativa_fk")
    @ManyToOne
    private SgModalidad pcaModalidad;
    
    @JoinColumn(name = "pca_sub_modalidad_educativa_fk")
    @ManyToOne
    private SgSubModalidadAtencion pcaSubModalidadAtencion;

    @JoinColumn(name = "pca_modalidad_atencion_fk")
    @ManyToOne
    private SgModalidadAtencion pcaModalidadAtencion;
    
    @JoinColumn(name = "pca_calendario_fk")
    @ManyToOne
    private SgCalendario pcaCalendario;

    
    @OneToMany(mappedBy = "rfePeriodoCalificacion",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SgRangoFecha> pcaRangoFecha;
    
    @Column(name = "pca_es_prueba")
    private Boolean pcaEsPrueba;
    
    //Las secciones pueden ser anuales o pertencer a un periodo especifico del año
    //Cuando no es anual se especifica a que periodo del año pertenece con pcaNumeroPeriodo
    @Column(name = "pca_tipo_periodo")
    @Enumerated(value = EnumType.STRING)
    private EnumTipoPeriodoSeccion pcaTipoPeriodo;
    
    @Column(name = "pca_numero_periodo")
    private Integer pcaNumeroPeriodo;

    public SgPeriodoCalificacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
   
    }

    public Long getPcaPk() {
        return pcaPk;
    }

    public void setPcaPk(Long pcaPk) {
        this.pcaPk = pcaPk;
    }

    public Integer getPcaNumero() {
        return pcaNumero;
    }

    public void setPcaNumero(Integer pcaNumero) {
        this.pcaNumero = pcaNumero;
    }

    public String getPcaNombre() {
        return pcaNombre;
    }

    public void setPcaNombre(String pcaNombre) {
        this.pcaNombre = pcaNombre;
    }
    
    
   
    public SgModalidad getPcaModalidad() {
        return pcaModalidad;
    }

    public void setPcaModalidad(SgModalidad pcaModalidad) {
        this.pcaModalidad = pcaModalidad;
    }

    

    public SgModalidadAtencion getPcaModalidadAtencion() {
        return pcaModalidadAtencion;
    }

    public void setPcaModalidadAtencion(SgModalidadAtencion pcaModalidadAtencion) {
        this.pcaModalidadAtencion = pcaModalidadAtencion;
    }

    public SgCalendario getPcaCalendario() {
        return pcaCalendario;
    }

    public void setPcaCalendario(SgCalendario pcaCalendario) {
        this.pcaCalendario = pcaCalendario;
    }
     

    public LocalDateTime getPcaUltModFecha() {
        return pcaUltModFecha;
    }

    public void setPcaUltModFecha(LocalDateTime pcaUltModFecha) {
        this.pcaUltModFecha = pcaUltModFecha;
    }

    public String getPcaUltModUsuario() {
        return pcaUltModUsuario;
    }

    public void setPcaUltModUsuario(String pcaUltModUsuario) {
        this.pcaUltModUsuario = pcaUltModUsuario;
    }

    public Integer getPcaVersion() {
        return pcaVersion;
    }

    public void setPcaVersion(Integer pcaVersion) {
        this.pcaVersion = pcaVersion;
    }

    public List<SgRangoFecha> getPcaRangoFecha() {
        return pcaRangoFecha;
    }

    public void setPcaRangoFecha(List<SgRangoFecha> pcaRangoFecha) {
        this.pcaRangoFecha = pcaRangoFecha;
    }

    public SgSubModalidadAtencion getPcaSubModalidadAtencion() {
        return pcaSubModalidadAtencion;
    }

    public void setPcaSubModalidadAtencion(SgSubModalidadAtencion pcaSubModalidadAtencion) {
        this.pcaSubModalidadAtencion = pcaSubModalidadAtencion;
    }

    public Boolean getPcaPermiteCalificarSinNie() {
        return pcaPermiteCalificarSinNie;
    }

    public void setPcaPermiteCalificarSinNie(Boolean pcaPermiteCalificarSinNie) {
        this.pcaPermiteCalificarSinNie = pcaPermiteCalificarSinNie;
    }

    public Boolean getPcaEsPrueba() {
        return pcaEsPrueba;
    }

    public void setPcaEsPrueba(Boolean pcaEsPrueba) {
        this.pcaEsPrueba = pcaEsPrueba;
    }

    public EnumTipoPeriodoSeccion getPcaTipoPeriodo() {
        return pcaTipoPeriodo;
    }

    public void setPcaTipoPeriodo(EnumTipoPeriodoSeccion pcaTipoPeriodo) {
        this.pcaTipoPeriodo = pcaTipoPeriodo;
    }

    public Integer getPcaNumeroPeriodo() {
        return pcaNumeroPeriodo;
    }

    public void setPcaNumeroPeriodo(Integer pcaNumeroPeriodo) {
        this.pcaNumeroPeriodo = pcaNumeroPeriodo;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.pcaPk);
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
        final SgPeriodoCalificacion other = (SgPeriodoCalificacion) obj;
        if (!Objects.equals(this.pcaPk, other.pcaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgPeriodoCalificacion{" + "pcaPk=" + pcaPk + ", pcaUltModFecha=" + pcaUltModFecha + ", pcaUltModUsuario=" + pcaUltModUsuario + ", pcaVersion=" + pcaVersion + '}';
    }
    
    

}
