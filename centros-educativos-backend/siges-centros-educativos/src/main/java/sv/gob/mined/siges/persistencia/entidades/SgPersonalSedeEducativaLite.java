/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;



@Entity
@Table(name = "sg_personal_sede_educativa", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@Audited
public class SgPersonalSedeEducativaLite implements Serializable, DataSecurity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pse_pk")
    private Long psePk;

    @JoinColumn(name = "pse_dato_empleado_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgDatoEmpleadoLite pseDatoEmpleado;
    
    @JoinColumn(name = "pse_persona_fk")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgPersonaLite psePersona;

//    @Column(name = "pse_tipo", nullable = false, insertable = false, updatable = false)
//    @Enumerated(EnumType.STRING)
//    private TipoPersonalSedeEducativa pseTipo;
    
    @Column(name = "pse_version")
    @Version
    private Integer pseVersion;
    
    public SgPersonalSedeEducativaLite(){
    }    

    public SgPersonalSedeEducativaLite(Long psePk, Integer pseVersion) {
        this.psePk = psePk;
        this.pseVersion = pseVersion;
    }    
    
    
    @PrePersist
    @PreUpdate
    public void preSave() throws Exception {
        throw new Exception("Esta clase no debe ser guardada por si sola. Debe utilizarse para asociaciones EntidadPadre - SgPersonal");
    }
    
    public Long getPsePk() {
        return psePk;
    }

    public void setPsePk(Long psePk) {
        this.psePk = psePk;
    }

    public Integer getPseVersion() {
        return pseVersion;
    }

    public void setPseVersion(Integer pseVersion) {
        this.pseVersion = pseVersion;
    }

    public SgPersonaLite getPsePersona() {
        return psePersona;
    }

    public void setPsePersona(SgPersonaLite psePersona) {
        this.psePersona = psePersona;
    }

    public SgDatoEmpleadoLite getPseDatoEmpleado() {
        return pseDatoEmpleado;
    }

    public void setPseDatoEmpleado(SgDatoEmpleadoLite pseDatoEmpleado) {
        this.pseDatoEmpleado = pseDatoEmpleado;
    }
    
    @Override
    public String securityAmbitCreate() {
       return null; //"pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedPk"; //TODO: el centro educativo se agrega en una pestaña luego de guardado. Ver.
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SISTEMA_INTEGRADO.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedSistemas.sinPk.sinPk", o.getContext()); 
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "psePersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedPk", o.getRegla());
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "psePk", -1L);
        } 
    }
    
    //Debe ser igual al hashCode de SgPersonalSedeEducativa
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (psePk != null ? psePk.hashCode() : 0);
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
        final SgPersonalSedeEducativaLite other = (SgPersonalSedeEducativaLite) obj;
        if (!Objects.equals(this.psePk, other.psePk)) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
