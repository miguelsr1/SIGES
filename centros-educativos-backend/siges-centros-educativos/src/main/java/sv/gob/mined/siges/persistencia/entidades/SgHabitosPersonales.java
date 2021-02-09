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
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_habitos_personales", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "hapPk", scope = SgHabitosPersonales.class)
@Audited
public class SgHabitosPersonales implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "hap_pk", nullable = false)
    private Long hapPk;

    @Column(name = "hap_tiempo_frente_pantalla")    
    private Double hapTiempoFrentePantalla;
    
    @Column(name = "hap_tiempo_compartido_con_padres")    
    private Double hapTiempoCompartidoConPadres;
    
    @Column(name = "hap_tiempo_tareas_escuela")    
    private Double hapTiempoTareasEscuela;
    
    @Column(name = "hap_tiempo_recreacion")    
    private Double hapTiempoRecreacion;
    
    @Column(name = "hap_tiempo_actividad_fisica")    
    private Double hapTiempoActividadFisica;
    
    @Column(name = "hap_tipo_actividad_fisica")    
    private String hapTipoActividadFisica;

    @Column(name = "hap_tiempo_dormir")    
    private Double hapTiempoDormir;
    
    @Column(name = "hap_observaciones")    
    private String hapObservaciones;
    
    @JoinColumn(name = "hap_anio_fk")
    @ManyToOne
    private SgAnioLectivo hapAnioLectivoFk;

    @JoinColumn(name = "hap_estudiante_fk")
    @ManyToOne
    private SgEstudiante hapEstudianteFk;

    @Column(name = "hap_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime hapUltModFecha;

    @Size(max = 45)
    @Column(name = "hap_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String hapUltModUsuario;

    @Column(name = "hap_version")
    @Version
    private Integer hapVersion;

    public SgHabitosPersonales() {
    }


    public Long getHapPk() {
        return hapPk;
    }

    public void setHapPk(Long hapPk) {
        this.hapPk = hapPk;
    }

    public LocalDateTime getHapUltModFecha() {
        return hapUltModFecha;
    }

    public void setHapUltModFecha(LocalDateTime hapUltModFecha) {
        this.hapUltModFecha = hapUltModFecha;
    }

    public String getHapUltModUsuario() {
        return hapUltModUsuario;
    }

    public void setHapUltModUsuario(String hapUltModUsuario) {
        this.hapUltModUsuario = hapUltModUsuario;
    }

    public Integer getHapVersion() {
        return hapVersion;
    }

    public void setHapVersion(Integer hapVersion) {
        this.hapVersion = hapVersion;
    }

    public Double getHapTiempoFrentePantalla() {
        return hapTiempoFrentePantalla;
    }

    public void setHapTiempoFrentePantalla(Double hapTiempoFrentePantalla) {
        this.hapTiempoFrentePantalla = hapTiempoFrentePantalla;
    }

    public Double getHapTiempoCompartidoConPadres() {
        return hapTiempoCompartidoConPadres;
    }

    public void setHapTiempoCompartidoConPadres(Double hapTiempoCompartidoConPadres) {
        this.hapTiempoCompartidoConPadres = hapTiempoCompartidoConPadres;
    }

    public Double getHapTiempoTareasEscuela() {
        return hapTiempoTareasEscuela;
    }

    public void setHapTiempoTareasEscuela(Double hapTiempoTareasEscuela) {
        this.hapTiempoTareasEscuela = hapTiempoTareasEscuela;
    }

    public Double getHapTiempoRecreacion() {
        return hapTiempoRecreacion;
    }

    public void setHapTiempoRecreacion(Double hapTiempoRecreacion) {
        this.hapTiempoRecreacion = hapTiempoRecreacion;
    }

    public Double getHapTiempoActividadFisica() {
        return hapTiempoActividadFisica;
    }

    public void setHapTiempoActividadFisica(Double hapTiempoActividadFisica) {
        this.hapTiempoActividadFisica = hapTiempoActividadFisica;
    }

    public String getHapTipoActividadFisica() {
        return hapTipoActividadFisica;
    }

    public void setHapTipoActividadFisica(String hapTipoActividadFisica) {
        this.hapTipoActividadFisica = hapTipoActividadFisica;
    }

    public Double getHapTiempoDormir() {
        return hapTiempoDormir;
    }

    public void setHapTiempoDormir(Double hapTiempoDormir) {
        this.hapTiempoDormir = hapTiempoDormir;
    }

    public String getHapObservaciones() {
        return hapObservaciones;
    }

    public void setHapObservaciones(String hapObservaciones) {
        this.hapObservaciones = hapObservaciones;
    }

    public SgAnioLectivo getHapAnioLectivoFk() {
        return hapAnioLectivoFk;
    }

    public void setHapAnioLectivoFk(SgAnioLectivo hapAnioLectivoFk) {
        this.hapAnioLectivoFk = hapAnioLectivoFk;
    }

    public SgEstudiante getHapEstudianteFk() {
        return hapEstudianteFk;
    }

    public void setHapEstudianteFk(SgEstudiante hapEstudianteFk) {
        this.hapEstudianteFk = hapEstudianteFk;
    }

    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.hapPk);
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
        final SgHabitosPersonales other = (SgHabitosPersonales) obj;
        if (!Objects.equals(this.hapPk, other.hapPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgHabitosPersonales{" + "hapPk=" + hapPk + ", hapUltModFecha=" + hapUltModFecha + ", hapUltModUsuario=" + hapUltModUsuario + ", hapVersion=" + hapVersion + '}';
    }
    
    

}
