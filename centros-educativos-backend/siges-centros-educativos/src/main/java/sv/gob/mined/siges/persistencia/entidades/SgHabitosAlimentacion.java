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
import java.util.List;
import java.util.Objects;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTiemposComidaDia;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_habitos_alimentacion", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "halPk", scope = SgHabitosAlimentacion.class)
@Audited
public class SgHabitosAlimentacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "hal_pk", nullable = false)
    private Long halPk;

    @Column(name = "hal_consume_frutas_verduras")    
    private Boolean halConsumeFrutasVerduras;
    
    @Column(name = "hal_frecuencia_consumo_frutas")    
    private Integer halFrecuenciaConsumoFrutas;
    
    @Column(name = "hal_consume_agua")    
    private Boolean halConsumeAgua;
    
    @Column(name = "hal_cantidad_vasos")    
    private Integer halCantidadVasos;
    
    @JoinColumn(name = "hal_anio_lectivo_fk")
    @ManyToOne
    private SgAnioLectivo halAnioLectivoFk;

    @JoinColumn(name = "hal_estudiante_fk")
    @ManyToOne
    private SgEstudiante halEstudianteFk;
    
    @Column(name = "hal_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime halUltModFecha;

    @Size(max = 45)
    @Column(name = "hal_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String halUltModUsuario;

    @Column(name = "hal_version")
    @Version
    private Integer halVersion;
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_habito_alimentacion_tiempo_comida_dia",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "hal_pk"),
            inverseJoinColumns = @JoinColumn(name = "tcd_pk"))
    @NotAudited
    private List<SgTiemposComidaDia> halTiempoComidaDia;

    public SgHabitosAlimentacion() {
    }



    public Long getHalPk() {
        return halPk;
    }

    public void setHalPk(Long halPk) {
        this.halPk = halPk;
    }


    public LocalDateTime getHalUltModFecha() {
        return halUltModFecha;
    }

    public void setHalUltModFecha(LocalDateTime halUltModFecha) {
        this.halUltModFecha = halUltModFecha;
    }

    public String getHalUltModUsuario() {
        return halUltModUsuario;
    }

    public void setHalUltModUsuario(String halUltModUsuario) {
        this.halUltModUsuario = halUltModUsuario;
    }

    public Integer getHalVersion() {
        return halVersion;
    }

    public void setHalVersion(Integer halVersion) {
        this.halVersion = halVersion;
    }

    public Boolean getHalConsumeFrutasVerduras() {
        return halConsumeFrutasVerduras;
    }

    public void setHalConsumeFrutasVerduras(Boolean halConsumeFrutasVerduras) {
        this.halConsumeFrutasVerduras = halConsumeFrutasVerduras;
    }

    public Integer getHalFrecuenciaConsumoFrutas() {
        return halFrecuenciaConsumoFrutas;
    }

    public void setHalFrecuenciaConsumoFrutas(Integer halFrecuenciaConsumoFrutas) {
        this.halFrecuenciaConsumoFrutas = halFrecuenciaConsumoFrutas;
    }

    public Boolean getHalConsumeAgua() {
        return halConsumeAgua;
    }

    public void setHalConsumeAgua(Boolean halConsumeAgua) {
        this.halConsumeAgua = halConsumeAgua;
    }

    public Integer getHalCantidadVasos() {
        return halCantidadVasos;
    }

    public void setHalCantidadVasos(Integer halCantidadVasos) {
        this.halCantidadVasos = halCantidadVasos;
    }

    public SgAnioLectivo getHalAnioLectivoFk() {
        return halAnioLectivoFk;
    }

    public void setHalAnioLectivoFk(SgAnioLectivo halAnioLectivoFk) {
        this.halAnioLectivoFk = halAnioLectivoFk;
    }

    public SgEstudiante getHalEstudianteFk() {
        return halEstudianteFk;
    }

    public void setHalEstudianteFk(SgEstudiante halEstudianteFk) {
        this.halEstudianteFk = halEstudianteFk;
    }

    

    public List<SgTiemposComidaDia> getHalTiempoComidaDia() {
        return halTiempoComidaDia;
    }

    public void setHalTiempoComidaDia(List<SgTiemposComidaDia> halTiempoComidaDia) {
        this.halTiempoComidaDia = halTiempoComidaDia;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.halPk);
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
        final SgHabitosAlimentacion other = (SgHabitosAlimentacion) obj;
        if (!Objects.equals(this.halPk, other.halPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgHabitosAlimentacion{" + "halPk=" + halPk +  ", halUltModFecha=" + halUltModFecha + ", halUltModUsuario=" + halUltModUsuario + ", halVersion=" + halVersion + '}';
    }
    
    

}
