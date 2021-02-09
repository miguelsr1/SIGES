package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author fabricio
 */
@Entity
@Table(name = "sg_resumen_cierre_anio_seccion", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rcsPk", scope = SgResumenCierreSeccion.class)
@Audited
public class SgResumenCierreSeccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rcs_pk")
    private Long rcsPk;

    @JoinColumn(name = "rcs_cierre_anio_sede_fk")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgCierreAnioLectivoSede rcsCierreAnioLectivoSede;

    @JoinColumn(name = "rcs_seccion_fk")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSeccion rcsSeccion;

    @Column(name = "rcs_promociones_pendientes_masc")
    private Integer rcsPromocionesPendientesMasc;

    @Column(name = "rcs_promociones_pendientes_fem")
    private Integer rcsPromocionesPendientesFem;

    @Column(name = "rcs_promovidos_masc")
    private Integer rcsPromovidosMasc;

    @Column(name = "rcs_promovidos_fem")
    private Integer rcsPromovidosFem;

    @Column(name = "rcs_no_promovidos_masc")
    private Integer rcsNoPromovidosMasc;

    @Column(name = "rcs_no_promovidos_fem")
    private Integer rcsNoPromovidosFem;

    @Column(name = "rcs_asistencias")
    private Integer rcsAsistencias;

    @Column(name = "rcs_inasistencias")
    private Integer rcsInasistencias;

    @Column(name = "rcs_inasistencias_just")
    private Integer rcsInasistenciasJust;

    @Column(name = "rcs_solicitudes_titulos_masc")
    private Integer rcsSolicitudesTitulosMasc;

    @Column(name = "rcs_solicitudes_titulos_fem")
    private Integer rcsSolicitudesTitulosFem;
    
    @Column(name = "rcs_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rcsUltModFecha;
    
    @Size(max = 45)
    @Column(name = "rcs_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String rcsUltModUsuario;
    
    @Column(name = "rcs_version")
    @Version
    private Integer rcsVersion;

    public SgResumenCierreSeccion() {
        rcsPromocionesPendientesMasc = 0;
        rcsPromocionesPendientesFem = 0;
        rcsPromovidosMasc = 0;
        rcsPromovidosFem = 0;
        rcsNoPromovidosMasc = 0;
        rcsNoPromovidosFem = 0;
        rcsAsistencias = 0;
        rcsInasistencias = 0;
        rcsInasistenciasJust = 0;
        rcsSolicitudesTitulosMasc = 0;
        rcsSolicitudesTitulosFem = 0;
    }

    public Long getRcsPk() {
        return rcsPk;
    }

    public void setRcsPk(Long rcsPk) {
        this.rcsPk = rcsPk;
    }

    public SgCierreAnioLectivoSede getRcsCierreAnioLectivoSede() {
        return rcsCierreAnioLectivoSede;
    }

    public void setRcsCierreAnioLectivoSede(SgCierreAnioLectivoSede rcsCierreAnioLectivoSede) {
        this.rcsCierreAnioLectivoSede = rcsCierreAnioLectivoSede;
    }

    public SgSeccion getRcsSeccion() {
        return rcsSeccion;
    }

    public void setRcsSeccion(SgSeccion rcsSeccion) {
        this.rcsSeccion = rcsSeccion;
    }

    public Integer getRcsPromocionesPendientesMasc() {
        return rcsPromocionesPendientesMasc;
    }

    public void setRcsPromocionesPendientesMasc(Integer rcsPromocionesPendientesMasc) {
        this.rcsPromocionesPendientesMasc = rcsPromocionesPendientesMasc;
    }

    public Integer getRcsPromocionesPendientesFem() {
        return rcsPromocionesPendientesFem;
    }

    public void setRcsPromocionesPendientesFem(Integer rcsPromocionesPendientesFem) {
        this.rcsPromocionesPendientesFem = rcsPromocionesPendientesFem;
    }

    public Integer getRcsPromovidosMasc() {
        return rcsPromovidosMasc;
    }

    public void setRcsPromovidosMasc(Integer rcsPromovidosMasc) {
        this.rcsPromovidosMasc = rcsPromovidosMasc;
    }

    public Integer getRcsPromovidosFem() {
        return rcsPromovidosFem;
    }

    public void setRcsPromovidosFem(Integer rcsPromovidosFem) {
        this.rcsPromovidosFem = rcsPromovidosFem;
    }

    public Integer getRcsNoPromovidosMasc() {
        return rcsNoPromovidosMasc;
    }

    public void setRcsNoPromovidosMasc(Integer rcsNoPromovidosMasc) {
        this.rcsNoPromovidosMasc = rcsNoPromovidosMasc;
    }

    public Integer getRcsNoPromovidosFem() {
        return rcsNoPromovidosFem;
    }

    public void setRcsNoPromovidosFem(Integer rcsNoPromovidosFem) {
        this.rcsNoPromovidosFem = rcsNoPromovidosFem;
    }

    public Integer getRcsAsistencias() {
        return rcsAsistencias;
    }

    public void setRcsAsistencias(Integer rcsAsistencias) {
        this.rcsAsistencias = rcsAsistencias;
    }

    public Integer getRcsInasistencias() {
        return rcsInasistencias;
    }

    public void setRcsInasistencias(Integer rcsInasistencias) {
        this.rcsInasistencias = rcsInasistencias;
    }

    public Integer getRcsInasistenciasJust() {
        return rcsInasistenciasJust;
    }

    public void setRcsInasistenciasJust(Integer rcsInasistenciasJust) {
        this.rcsInasistenciasJust = rcsInasistenciasJust;
    }

    

    public Integer getRcsSolicitudesTitulosMasc() {
        return rcsSolicitudesTitulosMasc;
    }

    public void setRcsSolicitudesTitulosMasc(Integer rcsSolicitudesTitulosMasc) {
        this.rcsSolicitudesTitulosMasc = rcsSolicitudesTitulosMasc;
    }

    public Integer getRcsSolicitudesTitulosFem() {
        return rcsSolicitudesTitulosFem;
    }

    public void setRcsSolicitudesTitulosFem(Integer rcsSolicitudesTitulosFem) {
        this.rcsSolicitudesTitulosFem = rcsSolicitudesTitulosFem;
    }

    public Integer getRcsVersion() {
        return rcsVersion;
    }

    public void setRcsVersion(Integer rcsVersion) {
        this.rcsVersion = rcsVersion;
    }

    public LocalDateTime getRcsUltModFecha() {
        return rcsUltModFecha;
    }

    public void setRcsUltModFecha(LocalDateTime rcsUltModFecha) {
        this.rcsUltModFecha = rcsUltModFecha;
    }

    public String getRcsUltModUsuario() {
        return rcsUltModUsuario;
    }

    public void setRcsUltModUsuario(String rcsUltModUsuario) {
        this.rcsUltModUsuario = rcsUltModUsuario;
    }
      
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.rcsPk);
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
        final SgResumenCierreSeccion other = (SgResumenCierreSeccion) obj;
        if (!Objects.equals(this.rcsPk, other.rcsPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgResumenCierreSeccion{" + "rcsPk=" + rcsPk + ", rcsVersion=" + rcsVersion + '}';
    }

    

}
