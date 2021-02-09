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
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgGradoRiesgo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTiposRiesgo;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_factor_riesgo_sedes", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "friPk", scope = SgFactorRiesgoSede.class)
@Audited
public class SgFactorRiesgoSede implements Serializable , DataSecurity{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fri_pk", nullable = false)
    private Long friPk;
    
    @JoinColumn(name = "fri_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede friSede;
    
    @JoinColumn(name = "fri_tipo_riesgo_fk", referencedColumnName = "tri_pk")
    @ManyToOne
    private SgTiposRiesgo friTipoRiesgo;
    
    @JoinColumn(name = "fri_grado_riesgo_fk", referencedColumnName = "gre_pk")
    @ManyToOne
    private SgGradoRiesgo friGradoRiesgo;
    
    @Column(name = "fri_fecha_inicio")
    private LocalDate friFechaInicio;
    
    @Column(name = "fri_fecha_fin")
    private LocalDate friFechaFin;
    
    @Column(name = "fri_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime friUltModFecha;

    @Size(max = 45)
    @Column(name = "fri_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String friUltModUsuario;

    @Column(name = "fri_version")
    @Version
    private Integer friVersion;

    public SgFactorRiesgoSede() {
    }

    public Long getFriPk() {
        return friPk;
    }

    public void setFriPk(Long friPk) {
        this.friPk = friPk;
    }

    public SgSede getFriSede() {
        return friSede;
    }

    public void setFriSede(SgSede friSede) {
        this.friSede = friSede;
    }

    public SgTiposRiesgo getFriTipoRiesgo() {
        return friTipoRiesgo;
    }

    public void setFriTipoRiesgo(SgTiposRiesgo friTipoRiesgo) {
        this.friTipoRiesgo = friTipoRiesgo;
    }

    public SgGradoRiesgo getFriGradoRiesgo() {
        return friGradoRiesgo;
    }

    public void setFriGradoRiesgo(SgGradoRiesgo friGradoRiesgo) {
        this.friGradoRiesgo = friGradoRiesgo;
    }

    public LocalDate getFriFechaInicio() {
        return friFechaInicio;
    }

    public void setFriFechaInicio(LocalDate friFechaInicio) {
        this.friFechaInicio = friFechaInicio;
    }

    public LocalDate getFriFechaFin() {
        return friFechaFin;
    }

    public void setFriFechaFin(LocalDate friFechaFin) {
        this.friFechaFin = friFechaFin;
    }

    public LocalDateTime getFriUltModFecha() {
        return friUltModFecha;
    }

    public void setFriUltModFecha(LocalDateTime friUltModFecha) {
        this.friUltModFecha = friUltModFecha;
    }

    public String getFriUltModUsuario() {
        return friUltModUsuario;
    }

    public void setFriUltModUsuario(String friUltModUsuario) {
        this.friUltModUsuario = friUltModUsuario;
    }

    public Integer getFriVersion() {
        return friVersion;
    }

    public void setFriVersion(Integer friVersion) {
        this.friVersion = friVersion;
    }





    
    @Override
    public String securityAmbitCreate() {
       return "friSede";
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "friSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "friSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "friPk", -1L);
        } 
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.friPk);
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
        final SgFactorRiesgoSede other = (SgFactorRiesgoSede) obj;
        if (!Objects.equals(this.friPk, other.friPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgFactorRiesgoSede{" + "friPk=" + friPk +", friUltModFecha=" + friUltModFecha + ", friUltModUsuario=" + friUltModUsuario + ", friVersion=" + friVersion + '}';
    }
    
    

}
