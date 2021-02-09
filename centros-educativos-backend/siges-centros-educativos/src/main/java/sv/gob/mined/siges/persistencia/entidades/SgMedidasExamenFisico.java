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
@Table(name = "sg_medidas_examen_fisico", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mefPk", scope = SgMedidasExamenFisico.class)
@Audited
public class SgMedidasExamenFisico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mef_pk", nullable = false)
    private Long mefPk;


    @Column(name = "mef_edad")    
    private Integer mefEdad;
    
    @Column(name = "mef_peso")    
    private Double mefPeso;
    
    @Column(name = "mef_talla")    
    private Double mefTalla;
    
    @Column(name = "mef_imc")    
    private Double mefImc;
    
    @Column(name = "mef_cbd")    
    private Double mefCbd;
    
    @JoinColumn(name = "mef_estudiante_fk")
    @ManyToOne
    private SgEstudiante mefEstudianteFk;
    
    
    @Column(name = "mef_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mefUltModFecha;

    @Size(max = 45)
    @Column(name = "mef_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mefUltModUsuario;

    @Column(name = "mef_version")
    @Version
    private Integer mefVersion;

    public SgMedidasExamenFisico() {
    }

 

    public Long getMefPk() {
        return mefPk;
    }

    public void setMefPk(Long mefPk) {
        this.mefPk = mefPk;
    }

    public Integer getMefEdad() {
        return mefEdad;
    }

    public void setMefEdad(Integer mefEdad) {
        this.mefEdad = mefEdad;
    }

    public Double getMefPeso() {
        return mefPeso;
    }

    public void setMefPeso(Double mefPeso) {
        this.mefPeso = mefPeso;
    }

    public Double getMefTalla() {
        return mefTalla;
    }

    public void setMefTalla(Double mefTalla) {
        this.mefTalla = mefTalla;
    }

    public Double getMefImc() {
        return mefImc;
    }

    public void setMefImc(Double mefImc) {
        this.mefImc = mefImc;
    }

    public Double getMefCbd() {
        return mefCbd;
    }

    public void setMefCbd(Double mefCbd) {
        this.mefCbd = mefCbd;
    }

    public SgEstudiante getMefEstudianteFk() {
        return mefEstudianteFk;
    }

    public void setMefEstudianteFk(SgEstudiante mefEstudianteFk) {
        this.mefEstudianteFk = mefEstudianteFk;
    }
 

    public LocalDateTime getMefUltModFecha() {
        return mefUltModFecha;
    }

    public void setMefUltModFecha(LocalDateTime mefUltModFecha) {
        this.mefUltModFecha = mefUltModFecha;
    }

    public String getMefUltModUsuario() {
        return mefUltModUsuario;
    }

    public void setMefUltModUsuario(String mefUltModUsuario) {
        this.mefUltModUsuario = mefUltModUsuario;
    }

    public Integer getMefVersion() {
        return mefVersion;
    }

    public void setMefVersion(Integer mefVersion) {
        this.mefVersion = mefVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mefPk);
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
        final SgMedidasExamenFisico other = (SgMedidasExamenFisico) obj;
        if (!Objects.equals(this.mefPk, other.mefPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMedidasExamenFisico{" + "mefPk=" + mefPk +  ", mefUltModFecha=" + mefUltModFecha + ", mefUltModUsuario=" + mefUltModUsuario + ", mefVersion=" + mefVersion + '}';
    }
    
    

}
