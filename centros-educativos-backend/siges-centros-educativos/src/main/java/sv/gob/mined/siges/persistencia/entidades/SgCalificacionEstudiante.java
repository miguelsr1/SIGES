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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.text.DecimalFormat;
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
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumProcesoCreacion;
import sv.gob.mined.siges.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.enumerados.EnumResolucionCalificacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCalificacion;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_calificaciones_estudiante", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "caePk", scope = SgCalificacionEstudiante.class)
@Audited
public class SgCalificacionEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cae_pk", nullable = false)
    private Long caePk;

    @JoinColumn(name = "cae_calificacion_fk", referencedColumnName = "cal_pk")
    @ManyToOne(optional = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgCalificacionCE caeCalificacion;

    @JoinColumn(name = "cae_estudiante_fk", referencedColumnName = "est_pk")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgEstudiante caeEstudiante;

    @Column(name = "cae_resolucion")
    @Enumerated(value = EnumType.STRING)
    private EnumResolucionCalificacion caeResolucion;

    @Column(name = "cae_promovido_calificacion")
    @Enumerated(value = EnumType.STRING)
    private EnumPromovidoCalificacion caePromovidoCalificacion;

    @Column(name = "cae_calificacion")
    private String caeCalificacionNumericaEstudiante;

    @JoinColumn(name = "cae_calificacion_conceptual_fk")
    @ManyToOne
    private SgCalificacion caeCalificacionConceptualEstudiante;

    @Column(name = "cae_observacion")
    private String caeObservacion;

    @Column(name = "cae_fecha_realizado")
    private LocalDate caeFechaRealizado;

    @Column(name = "cae_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime caeUltModFecha;

    @Size(max = 45)
    @Column(name = "cae_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String caeUltModUsuario;

    @Column(name = "cae_version")
    @Version
    private Integer caeVersion;

    @Column(name = "cae_proceso_de_creacion")
    @Enumerated(value = EnumType.STRING)
    private EnumProcesoCreacion caeProcesoDeCreacion;  

    @JoinColumn(name = "cae_info_procesamiento_calificacion_est_fk", referencedColumnName = "ipe_pk")
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval=true)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgInfoProcesamientoCalificacionEst caeInfoProcesamientoCalificacionEstFk;
    
    public SgCalificacionEstudiante() {
    }

    @JsonIgnore
    public String getCaeCalificacionValor() {
        if (caeCalificacionConceptualEstudiante != null) {
            return caeCalificacionConceptualEstudiante.getCalValor();
        }
        if (!StringUtils.isBlank(caeCalificacionNumericaEstudiante)) {
            Double d = Double.parseDouble(caeCalificacionNumericaEstudiante);
            DecimalFormat df = new DecimalFormat("#.0");
            return df.format(d);
        }
        return "";
    }

    public Long getCaePk() {
        return caePk;
    }

    public void setCaePk(Long caePk) {
        this.caePk = caePk;
    }

    public SgCalificacionCE getCaeCalificacion() {
        return caeCalificacion;
    }

    public void setCaeCalificacion(SgCalificacionCE caeCalificacion) {
        this.caeCalificacion = caeCalificacion;
    }

    public SgEstudiante getCaeEstudiante() {
        return caeEstudiante;
    }

    public void setCaeEstudiante(SgEstudiante caeEstudiante) {
        this.caeEstudiante = caeEstudiante;
    }

    public String getCaeCalificacionNumericaEstudiante() {
        return caeCalificacionNumericaEstudiante;
    }

    public void setCaeCalificacionNumericaEstudiante(String caeCalificacionNumericaEstudiante) {
        this.caeCalificacionNumericaEstudiante = caeCalificacionNumericaEstudiante;
    }

    public SgCalificacion getCaeCalificacionConceptualEstudiante() {
        return caeCalificacionConceptualEstudiante;
    }

    public void setCaeCalificacionConceptualEstudiante(SgCalificacion caeCalificacionConceptualEstudiante) {
        this.caeCalificacionConceptualEstudiante = caeCalificacionConceptualEstudiante;
    }

    public String getCaeObservacion() {
        return caeObservacion;
    }

    public void setCaeObservacion(String caeObservacion) {
        this.caeObservacion = caeObservacion;
    }

    public LocalDateTime getCaeUltModFecha() {
        return caeUltModFecha;
    }

    public void setCaeUltModFecha(LocalDateTime caeUltModFecha) {
        this.caeUltModFecha = caeUltModFecha;
    }

    public String getCaeUltModUsuario() {
        return caeUltModUsuario;
    }

    public void setCaeUltModUsuario(String caeUltModUsuario) {
        this.caeUltModUsuario = caeUltModUsuario;
    }

    public Integer getCaeVersion() {
        return caeVersion;
    }

    public void setCaeVersion(Integer caeVersion) {
        this.caeVersion = caeVersion;
    }

    public LocalDate getCaeFechaRealizado() {
        return caeFechaRealizado;
    }

    public void setCaeFechaRealizado(LocalDate caeFechaRealizado) {
        this.caeFechaRealizado = caeFechaRealizado;
    }

    public EnumResolucionCalificacion getCaeResolucion() {
        return caeResolucion;
    }

    public void setCaeResolucion(EnumResolucionCalificacion caeResolucion) {
        this.caeResolucion = caeResolucion;
    }

    public EnumPromovidoCalificacion getCaePromovidoCalificacion() {
        return caePromovidoCalificacion;
    }

    public void setCaePromovidoCalificacion(EnumPromovidoCalificacion caePromovidoCalificacion) {
        this.caePromovidoCalificacion = caePromovidoCalificacion;
    }

    public EnumProcesoCreacion getCaeProcesoDeCreacion() {
        return caeProcesoDeCreacion;
    }

    public void setCaeProcesoDeCreacion(EnumProcesoCreacion caeProcesoDeCreacion) {
        this.caeProcesoDeCreacion = caeProcesoDeCreacion;
    }

    public SgInfoProcesamientoCalificacionEst getCaeInfoProcesamientoCalificacionEstFk() {
        return caeInfoProcesamientoCalificacionEstFk;
    }

    public void setCaeInfoProcesamientoCalificacionEstFk(SgInfoProcesamientoCalificacionEst caeInfoProcesamientoCalificacionEstFk) {
        this.caeInfoProcesamientoCalificacionEstFk = caeInfoProcesamientoCalificacionEstFk;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.caePk);
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
        final SgCalificacionEstudiante other = (SgCalificacionEstudiante) obj;
        if (!Objects.equals(this.caePk, other.caePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCalificacionEstudiante{" + "caePk=" + caePk + ", caeCalificacion=" + caeCalificacion + ", caeResolucion=" + caeResolucion + ", caePromovidoCalificacion=" + caePromovidoCalificacion + ", caeCalificacionNumericaEstudiante=" + caeCalificacionNumericaEstudiante + ", caeCalificacionConceptualEstudiante=" + caeCalificacionConceptualEstudiante + ", caeObservacion=" + caeObservacion + ", caeFechaRealizado=" + caeFechaRealizado + ", caeUltModFecha=" + caeUltModFecha + ", caeUltModUsuario=" + caeUltModUsuario + ", caeVersion=" + caeVersion + ", caeProcesoDeCreacion=" + caeProcesoDeCreacion + '}';
    }

    
    

}
