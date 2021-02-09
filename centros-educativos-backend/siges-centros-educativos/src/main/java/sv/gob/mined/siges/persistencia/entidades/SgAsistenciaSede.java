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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoApoyo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoProveedor;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_asistencia_sedes", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "asePk", scope = SgAsistenciaSede.class)
@Audited
public class SgAsistenciaSede implements Serializable , DataSecurity{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ase_pk", nullable = false)
    private Long asePk;
    
    @JoinColumn(name = "ase_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede aseSede;
    
    @JoinColumn(name = "ase_anio_fk", referencedColumnName = "ale_pk")
    @ManyToOne
    private SgAnioLectivo aseAnio;
    
    @JoinColumn(name = "ase_tipo_apoyo_fk", referencedColumnName = "tap_pk")
    @ManyToOne
    private SgTipoApoyo aseTipoApoyo;
    
    @JoinColumn(name = "ase_tipo_proveedor_fk", referencedColumnName = "tpr_pk")
    @ManyToOne
    private SgTipoProveedor aseTipoProveedor;
    
    @Size(max = 255)
    @Column(name = "ase_nombre_proveedor", length = 255)
    private String aseNombreProveedor;
    
    @Column(name = "ase_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime aseUltModFecha;

    @Size(max = 45)
    @Column(name = "ase_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String aseUltModUsuario;

    @Column(name = "ase_version")
    @Version
    private Integer aseVersion;

    public SgAsistenciaSede() {
    }

    public Long getAsePk() {
        return asePk;
    }

    public void setAsePk(Long asePk) {
        this.asePk = asePk;
    }

    public SgSede getAseSede() {
        return aseSede;
    }

    public void setAseSede(SgSede aseSede) {
        this.aseSede = aseSede;
    }

    public SgAnioLectivo getAseAnio() {
        return aseAnio;
    }

    public void setAseAnio(SgAnioLectivo aseAnio) {
        this.aseAnio = aseAnio;
    }

    public SgTipoApoyo getAseTipoApoyo() {
        return aseTipoApoyo;
    }

    public void setAseTipoApoyo(SgTipoApoyo aseTipoApoyo) {
        this.aseTipoApoyo = aseTipoApoyo;
    }

    public SgTipoProveedor getAseTipoProveedor() {
        return aseTipoProveedor;
    }

    public void setAseTipoProveedor(SgTipoProveedor aseTipoProveedor) {
        this.aseTipoProveedor = aseTipoProveedor;
    }

    public String getAseNombreProveedor() {
        return aseNombreProveedor;
    }

    public void setAseNombreProveedor(String aseNombreProveedor) {
        this.aseNombreProveedor = aseNombreProveedor;
    }

    public LocalDateTime getAseUltModFecha() {
        return aseUltModFecha;
    }

    public void setAseUltModFecha(LocalDateTime aseUltModFecha) {
        this.aseUltModFecha = aseUltModFecha;
    }

    public String getAseUltModUsuario() {
        return aseUltModUsuario;
    }

    public void setAseUltModUsuario(String aseUltModUsuario) {
        this.aseUltModUsuario = aseUltModUsuario;
    }

    public Integer getAseVersion() {
        return aseVersion;
    }

    public void setAseVersion(Integer aseVersion) {
        this.aseVersion = aseVersion;
    }




    
    @Override
    public String securityAmbitCreate() {
       return "aseSede";
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aseSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aseSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asePk", -1L);
        }
    }

    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.asePk);
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
        final SgAsistenciaSede other = (SgAsistenciaSede) obj;
        if (!Objects.equals(this.asePk, other.asePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAsistenciaSede{" + "asePk=" + asePk +", aseUltModFecha=" + aseUltModFecha + ", aseUltModUsuario=" + aseUltModUsuario + ", aseVersion=" + aseVersion + '}';
    }
    
    

}
