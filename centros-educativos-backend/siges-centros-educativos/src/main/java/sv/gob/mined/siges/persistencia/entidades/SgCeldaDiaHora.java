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
import java.util.Objects;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumCeldaDiaHora;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_celda_dia_hora", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cdhPk", scope = SgCeldaDiaHora.class)
@Audited
public class SgCeldaDiaHora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cdh_pk", nullable = false)
    private Long cdhPk;

    @Column(name = "cdh_dia")
    private EnumCeldaDiaHora cdhDia;
            
    @Column(name = "cdh_hora")
    private Integer hesHora;
 
    @Column(name = "cdh_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cdhUltModFecha;

    @Size(max = 45)
    @Column(name = "cdh_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cdhUltModUsuario;

    @Column(name = "cdh_version")
    @Version
    private Integer cdhVersion;
    
    @JoinColumn(name = "cdh_componente_plan_grado_fk", referencedColumnName = "cpg_pk")
    @ManyToOne
    private SgComponentePlanGrado cdhComponentePlanGrado;
    
    @JoinColumn(name = "cdh_horario_escolar_fk", referencedColumnName = "hes_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgHorarioEscolar cdhHorarioEscolar;

    public SgCeldaDiaHora() {
    }

     public Long getCdhPk() {
        return cdhPk;
    }

    public void setCdhPk(Long cdhPk) {
        this.cdhPk = cdhPk;
    }

    public EnumCeldaDiaHora getCdhDia() {
        return cdhDia;
    }

    public void setCdhDia(EnumCeldaDiaHora cdhDia) {
        this.cdhDia = cdhDia;
    }

    public Integer getHesHora() {
        return hesHora;
    }

    public void setHesHora(Integer hesHora) {
        this.hesHora = hesHora;
    }
     
    public LocalDateTime getCdhUltModFecha() {
        return cdhUltModFecha;
    }

    public void setCdhUltModFecha(LocalDateTime cdhUltModFecha) {
        this.cdhUltModFecha = cdhUltModFecha;
    }

    public String getCdhUltModUsuario() {
        return cdhUltModUsuario;
    }

    public void setCdhUltModUsuario(String cdhUltModUsuario) {
        this.cdhUltModUsuario = cdhUltModUsuario;
    }

    public Integer getCdhVersion() {
        return cdhVersion;
    }

    public void setCdhVersion(Integer cdhVersion) {
        this.cdhVersion = cdhVersion;
    }

    public SgComponentePlanGrado getCdhComponentePlanGrado() {
        return cdhComponentePlanGrado;
    }

    public void setCdhComponentePlanGrado(SgComponentePlanGrado cdhComponentePlanGrado) {
        this.cdhComponentePlanGrado = cdhComponentePlanGrado;
    }

    public SgHorarioEscolar getCdhHorarioEscolar() {
        return cdhHorarioEscolar;
    }

    public void setCdhHorarioEscolar(SgHorarioEscolar cdhHorarioEscolar) {
        this.cdhHorarioEscolar = cdhHorarioEscolar;
    }
        
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cdhPk);
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
        final SgCeldaDiaHora other = (SgCeldaDiaHora) obj;
        if (!Objects.equals(this.cdhPk, other.cdhPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCeldaDiaHora{" + "cdhPk=" + cdhPk +  ", cdhUltModFecha=" + cdhUltModFecha + ", cdhUltModUsuario=" + cdhUltModUsuario + ", cdhVersion=" + cdhVersion + '}';
    }
    
    

}
