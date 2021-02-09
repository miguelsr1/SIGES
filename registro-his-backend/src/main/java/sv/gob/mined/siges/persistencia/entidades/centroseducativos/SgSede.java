/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.centroseducativos;

import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDireccion;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_sedes", uniqueConstraints = {
    @UniqueConstraint(name = "sed_codigo_uk", columnNames = {"sed_codigo"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "sedPk", scope = SgSede.class)
@Audited
public class SgSede implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "sed_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sedPk;

    @Column(name = "sed_codigo")
    @Size(max = 5)
    private String sedCodigo;

    @Column(name = "sed_nombre")
    @Size(max = 500)
    private String sedNombre;
    
    @Column(name = "sed_nombre_busqueda")
    @Size(max = 500)
    private String sedNombreBusqueda;
    
    @Column(name = "sed_habilitado")
    private Boolean sedHabilitado; 
   
    @JoinColumn(name = "sed_direccion_fk")
    @OneToOne(cascade = CascadeType.ALL)
    private SgDireccion sedDireccion;   

    @Column(name = "sed_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sedUltModFecha;

    @Size(max = 45)
    @Column(name = "sed_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String sedUltModUsuario;

    @Column(name = "sed_version")
    @Version
    private Integer sedVersion;

    public SgSede() {
    }

    public SgSede(Long sedPk) {
        this.sedPk = sedPk;
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.sedNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.sedNombre);
    }
         
    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
    }

    public String getSedCodigo() {
        return sedCodigo;
    }

    public void setSedCodigo(String sedCodigo) {
        this.sedCodigo = sedCodigo;
    }

    public String getSedNombre() {
        return sedNombre;
    }

    public void setSedNombre(String sedNombre) {
        this.sedNombre = sedNombre;
    }

    public Boolean getSedHabilitado() {
        return sedHabilitado;
    }

    public void setSedHabilitado(Boolean sedHabilitado) {
        this.sedHabilitado = sedHabilitado;
    }

    public String getSedNombreBusqueda() {
        return sedNombreBusqueda;
    }

    public void setSedNombreBusqueda(String sedNombreBusqueda) {
        this.sedNombreBusqueda = sedNombreBusqueda;
    }

    public SgDireccion getSedDireccion() {
        return sedDireccion;
    }

    public void setSedDireccion(SgDireccion sedDireccion) {
        this.sedDireccion = sedDireccion;
    }

    public LocalDateTime getSedUltModFecha() {
        return sedUltModFecha;
    }

    public void setSedUltModFecha(LocalDateTime sedUltModFecha) {
        this.sedUltModFecha = sedUltModFecha;
    }

    public String getSedUltModUsuario() {
        return sedUltModUsuario;
    }

    public void setSedUltModUsuario(String sedUltModUsuario) {
        this.sedUltModUsuario = sedUltModUsuario;
    }

    public Integer getSedVersion() {
        return sedVersion;
    }

    public void setSedVersion(Integer sedVersion) {
        this.sedVersion = sedVersion;
    }
    
    @Override
    public String securityAmbitCreate() {
       return "sedDireccion.dirDepartamento";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())){
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", -1L);
        } 
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.sedPk);
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
        final SgSede other = (SgSede) obj;
        if (!Objects.equals(this.sedPk, other.sedPk)) {
            return false;
        }
        return true;
    }


}
