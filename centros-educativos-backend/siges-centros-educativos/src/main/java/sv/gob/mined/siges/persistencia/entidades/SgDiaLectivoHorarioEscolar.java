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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumCeldaDiaHora;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_dia_lectivo_horario_escolar", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dlhPk", scope = SgDiaLectivoHorarioEscolar.class)
@Audited
public class SgDiaLectivoHorarioEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dlh_pk", nullable = false)
    private Long dlhPk;
    
    @Column(name = "dlh_dia")
    @Enumerated(value = EnumType.STRING)
    private EnumCeldaDiaHora dlhDia;
    
    @Column(name = "dlh_es_lectivo")
    private Boolean dlhEsLectivo;
    
  
    @JoinColumn(name = "dlh_horario_escolar_fk", referencedColumnName = "hes_pk")
    @ManyToOne
    private SgHorarioEscolar dlhHorarioEscolarFk;   

    @Column(name = "dlh_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dlhUltModFecha;

    @Size(max = 45)
    @Column(name = "dlh_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dlhUltModUsuario;

    @Column(name = "dlh_version")
    @Version
    private Integer dlhVersion;

    public SgDiaLectivoHorarioEscolar() {
    }



    public Long getDlhPk() {
        return dlhPk;
    }

    public void setDlhPk(Long dlhPk) {
        this.dlhPk = dlhPk;
    }

    public EnumCeldaDiaHora getDlhDia() {
        return dlhDia;
    }

    public void setDlhDia(EnumCeldaDiaHora dlhDia) {
        this.dlhDia = dlhDia;
    }

    public Boolean getDlhEsLectivo() {
        return dlhEsLectivo;
    }

    public void setDlhEsLectivo(Boolean dlhEsLectivo) {
        this.dlhEsLectivo = dlhEsLectivo;
    }

    public SgHorarioEscolar getDlhHorarioEscolarFk() {
        return dlhHorarioEscolarFk;
    }

    public void setDlhHorarioEscolarFk(SgHorarioEscolar dlhHorarioEscolarFk) {
        this.dlhHorarioEscolarFk = dlhHorarioEscolarFk;
    }

  

    public LocalDateTime getDlhUltModFecha() {
        return dlhUltModFecha;
    }

    public void setDlhUltModFecha(LocalDateTime dlhUltModFecha) {
        this.dlhUltModFecha = dlhUltModFecha;
    }

    public String getDlhUltModUsuario() {
        return dlhUltModUsuario;
    }

    public void setDlhUltModUsuario(String dlhUltModUsuario) {
        this.dlhUltModUsuario = dlhUltModUsuario;
    }

    public Integer getDlhVersion() {
        return dlhVersion;
    }

    public void setDlhVersion(Integer dlhVersion) {
        this.dlhVersion = dlhVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.dlhPk);
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
        final SgDiaLectivoHorarioEscolar other = (SgDiaLectivoHorarioEscolar) obj;
        if (!Objects.equals(this.dlhPk, other.dlhPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDiaLectivoHorarioEscolar{" + "dlhPk=" + dlhPk+ ", dlhUltModFecha=" + dlhUltModFecha + ", dlhUltModUsuario=" + dlhUltModUsuario + ", dlhVersion=" + dlhVersion + '}';
    }
    
    

}
