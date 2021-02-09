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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_detalle_matricula", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "demPk", scope = SgDetalleMatricula.class)
@Audited
public class SgDetalleMatricula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dem_pk", nullable = false)
    private Long demPk;

    @Column(name = "dem_costo_matricula")
    private Integer demCostoMatricula;
    
    @Column(name = "dem_cantidad_mensualidad")
    private Integer demCantidadMensualidad;
    
    @Column(name = "dem_costo_mensualidad")
    private Integer demCostoMensualidad;

    @Column(name = "dem_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime demUltModFecha;

    @Size(max = 45)
    @Column(name = "dem_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String demUltModUsuario;

    @Column(name = "dem_version")
    @Version
    private Integer demVersion;
    
    @JoinColumn(name = "dem_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne(optional = false)
    private SgSede demSede;
    
    @JoinColumn(name = "dem_nivel_fk", referencedColumnName = "niv_pk")
    @ManyToOne(optional = false)
    private SgNivel demNivel;
    
    @JoinColumn(name = "dem_anio_fk", referencedColumnName = "ale_pk")
    @ManyToOne(optional = false)
    private SgAnioLectivo demAnioLectivo;

    public SgDetalleMatricula() {
    }

   

    public Long getDemPk() {
        return demPk;
    }

    public void setDemPk(Long demPk) {
        this.demPk = demPk;
    }

    public Integer getDemCostoMatricula() {
        return demCostoMatricula;
    }

    public void setDemCostoMatricula(Integer demCostoMatricula) {
        this.demCostoMatricula = demCostoMatricula;
    }

    public Integer getDemCantidadMensualidad() {
        return demCantidadMensualidad;
    }

    public void setDemCantidadMensualidad(Integer demCantidadMensualidad) {
        this.demCantidadMensualidad = demCantidadMensualidad;
    }

    public Integer getDemCostoMensualidad() {
        return demCostoMensualidad;
    }

    public void setDemCostoMensualidad(Integer demCostoMensualidad) {
        this.demCostoMensualidad = demCostoMensualidad;
    }

    public SgSede getDemSede() {
        return demSede;
    }

    public void setDemSede(SgSede demSede) {
        this.demSede = demSede;
    }

    public SgNivel getDemNivel() {
        return demNivel;
    }

    public void setDemNivel(SgNivel demNivel) {
        this.demNivel = demNivel;
    }


    public SgAnioLectivo getDemAnioLectivo() {
        return demAnioLectivo;
    }

    public void setDemAnioLectivo(SgAnioLectivo demAnioLectivo) {
        this.demAnioLectivo = demAnioLectivo;
    }   

    public LocalDateTime getDemUltModFecha() {
        return demUltModFecha;
    }

    public void setDemUltModFecha(LocalDateTime demUltModFecha) {
        this.demUltModFecha = demUltModFecha;
    }

    public String getDemUltModUsuario() {
        return demUltModUsuario;
    }

    public void setDemUltModUsuario(String demUltModUsuario) {
        this.demUltModUsuario = demUltModUsuario;
    }

    public Integer getDemVersion() {
        return demVersion;
    }

    public void setDemVersion(Integer demVersion) {
        this.demVersion = demVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.demPk);
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
        final SgDetalleMatricula other = (SgDetalleMatricula) obj;
        if (!Objects.equals(this.demPk, other.demPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDetalleMatricula{" + "demPk=" + demPk + '}';
    }

   
    
    
    

}
