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

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_casos_violacion", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cviPk", scope = SgCasoViolacion.class)
@Audited
public class SgCasoViolacion implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cvi_pk", nullable = false)
    private Long cviPk;
    
    @JoinColumn(name = "cvi_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede cviSede;
    
    @JoinColumn(name = "cvi_anio_fk", referencedColumnName = "ale_pk")
    @ManyToOne
    private SgAnioLectivo cviAnio;
    
    @Column(name = "cvi_violaciones")
    private Boolean cviViolaciones;
    
    @Column(name = "cvi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cviUltModFecha;

    @Size(max = 45)
    @Column(name = "cvi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cviUltModUsuario;

    @Column(name = "cvi_version")
    @Version
    private Integer cviVersion;

    public SgCasoViolacion() {
    }

    public Long getCviPk() {
        return cviPk;
    }

    public void setCviPk(Long cviPk) {
        this.cviPk = cviPk;
    }

    public SgSede getCviSede() {
        return cviSede;
    }

    public void setCviSede(SgSede cviSede) {
        this.cviSede = cviSede;
    }

    public SgAnioLectivo getCviAnio() {
        return cviAnio;
    }

    public void setCviAnio(SgAnioLectivo cviAnio) {
        this.cviAnio = cviAnio;
    }

    public Boolean getCviViolaciones() {
        return cviViolaciones;
    }

    public void setCviViolaciones(Boolean cviViolaciones) {
        this.cviViolaciones = cviViolaciones;
    }

    public LocalDateTime getCviUltModFecha() {
        return cviUltModFecha;
    }

    public void setCviUltModFecha(LocalDateTime cviUltModFecha) {
        this.cviUltModFecha = cviUltModFecha;
    }

    public String getCviUltModUsuario() {
        return cviUltModUsuario;
    }

    public void setCviUltModUsuario(String cviUltModUsuario) {
        this.cviUltModUsuario = cviUltModUsuario;
    }

    public Integer getCviVersion() {
        return cviVersion;
    }

    public void setCviVersion(Integer cviVersion) {
        this.cviVersion = cviVersion;
    }



    
    @Override
    public String securityAmbitCreate() {
       return "cviSede";
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cviSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cviSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cviPk", -1L);
        } 
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(this.cviPk);
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
        final SgCasoViolacion other = (SgCasoViolacion) obj;
        if (!Objects.equals(this.cviPk, other.cviPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCasoViolacion[" + "cviPk=" + cviPk +", cviUltModFecha=" + cviUltModFecha + ", cviUltModUsuario=" + cviUltModUsuario + ", cviVersion=" + cviVersion + ']';
    }
    
    

}
