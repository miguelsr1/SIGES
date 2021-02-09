/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumOperacionReglaEquivalencia;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_regla_equivalencia_detalle", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "redPk", scope = SgReglaEquivalenciaDetalle.class)
public class SgReglaEquivalenciaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "red_pk", nullable = false)
    private Long redPk;
    
    @JoinColumn(name = "red_grado_fk", referencedColumnName = "gra_pk")
    @ManyToOne(optional = false)
    private SgGrado redGrado;
    
    @JoinColumn(name = "red_plan_estudio_fk", referencedColumnName = "pes_pk")
    @ManyToOne(optional = false)
    private SgPlanEstudio redPlanEstudio;
    
    @JoinColumn(name = "red_regla_equivalencia_fk", referencedColumnName = "req_pk")
    @ManyToOne(optional = false)
    private SgReglaEquivalencia redReglaEquivalencia;
        
    @Column(name = "red_operacion")
    @Enumerated(value = EnumType.STRING)
    private EnumOperacionReglaEquivalencia redOperacion;
    
    @Column(name = "red_habilitado")
    @AtributoHabilitado
    private Boolean redHabilitado;

    @Column(name = "red_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime redUltModFecha;

    @Size(max = 45)
    @Column(name = "red_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String redUltModUsuario;

    @Column(name = "red_version")
    @Version
    private Integer redVersion;
    
    @JoinColumn(name = "red_opcion_fk", referencedColumnName = "opc_pk")
    @ManyToOne(optional = true)
    private SgOpcion redOpcion;
    
    @JoinColumn(name = "red_programa_educativo_fk", referencedColumnName = "ped_pk")
    @ManyToOne(optional = true)
    private SgProgramaEducativo redProgramaEducativo;

    public Long getRedPk() {
        return redPk;
    }

    public void setRedPk(Long redPk) {
        this.redPk = redPk;
    }

    public SgGrado getRedGrado() {
        return redGrado;
    }

    public void setRedGrado(SgGrado redGrado) {
        this.redGrado = redGrado;
    }

    public SgPlanEstudio getRedPlanEstudio() {
        return redPlanEstudio;
    }

    public void setRedPlanEstudio(SgPlanEstudio redPlanEstudio) {
        this.redPlanEstudio = redPlanEstudio;
    }

    public EnumOperacionReglaEquivalencia getRedOperacion() {
        return redOperacion;
    }

    public void setRedOperacion(EnumOperacionReglaEquivalencia redOperacion) {
        this.redOperacion = redOperacion;
    }

    public SgReglaEquivalencia getRedReglaEquivalencia() {
        return redReglaEquivalencia;
    }

    public void setRedReglaEquivalencia(SgReglaEquivalencia redReglaEquivalencia) {
        this.redReglaEquivalencia = redReglaEquivalencia;
    }

    public Boolean getRedHabilitado() {
        return redHabilitado;
    }

    public void setRedHabilitado(Boolean redHabilitado) {
        this.redHabilitado = redHabilitado;
    }

    public LocalDateTime getRedUltModFecha() {
        return redUltModFecha;
    }

    public void setRedUltModFecha(LocalDateTime redUltModFecha) {
        this.redUltModFecha = redUltModFecha;
    }

    public String getRedUltModUsuario() {
        return redUltModUsuario;
    }

    public void setRedUltModUsuario(String redUltModUsuario) {
        this.redUltModUsuario = redUltModUsuario;
    }

    public Integer getRedVersion() {
        return redVersion;
    }

    public void setRedVersion(Integer redVersion) {
        this.redVersion = redVersion;
    }

    public SgOpcion getRedOpcion() {
        return redOpcion;
    }

    public void setRedOpcion(SgOpcion redOpcion) {
        this.redOpcion = redOpcion;
    }

    public SgProgramaEducativo getRedProgramaEducativo() {
        return redProgramaEducativo;
    }

    public void setRedProgramaEducativo(SgProgramaEducativo redProgramaEducativo) {
        this.redProgramaEducativo = redProgramaEducativo;
    }
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.redPk);
        hash = 79 * hash + Objects.hashCode(this.redGrado);
        hash = 79 * hash + Objects.hashCode(this.redPlanEstudio);
        hash = 79 * hash + Objects.hashCode(this.redOperacion);
        hash = 79 * hash + Objects.hashCode(this.redReglaEquivalencia);
        hash = 79 * hash + Objects.hashCode(this.redHabilitado);
        hash = 79 * hash + Objects.hashCode(this.redUltModFecha);
        hash = 79 * hash + Objects.hashCode(this.redUltModUsuario);
        hash = 79 * hash + Objects.hashCode(this.redVersion);
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
        final SgReglaEquivalenciaDetalle other = (SgReglaEquivalenciaDetalle) obj;
        if (!Objects.equals(this.redPk, other.redPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgReglaEquivalenciaDetalle{" + "redPk=" + redPk + '}';
    }
}
