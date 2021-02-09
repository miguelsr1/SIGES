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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoAccion;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_acciones_prevenir_embarazo", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "apePk", scope = SgAccionPrevenirEmbarazo.class)
@Audited
public class SgAccionPrevenirEmbarazo implements Serializable , DataSecurity{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ape_pk", nullable = false)
    private Long apePk;
    
    @JoinColumn(name = "ape_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede apeSede;
    
    @JoinColumn(name = "ape_anio_fk", referencedColumnName = "ale_pk")
    @ManyToOne
    private SgAnioLectivo apeAnio;
    
    @Size(max = 255)
    @Column(name = "ape_institucion", length = 255)
    private String apeInstitucion;
    
    @JoinColumn(name = "ape_tipo_accion_fk", referencedColumnName = "tac_pk")
    @ManyToOne
    private SgTipoAccion apeTipoAccion;
    
    @Size(max = 255)
    @Column(name = "ape_descripcion", length = 255)
    private String apeDescripcion;
    
    @Column(name = "ape_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime apeUltModFecha;

    @Size(max = 45)
    @Column(name = "ape_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String apeUltModUsuario;

    @Column(name = "ape_version")
    @Version
    private Integer apeVersion;

    public SgAccionPrevenirEmbarazo() {
    }

    public Long getApePk() {
        return apePk;
    }

    public void setApePk(Long apePk) {
        this.apePk = apePk;
    }

    public SgSede getApeSede() {
        return apeSede;
    }

    public void setApeSede(SgSede apeSede) {
        this.apeSede = apeSede;
    }

    public SgAnioLectivo getApeAnio() {
        return apeAnio;
    }

    public void setApeAnio(SgAnioLectivo apeAnio) {
        this.apeAnio = apeAnio;
    }

    public String getApeInstitucion() {
        return apeInstitucion;
    }

    public void setApeInstitucion(String apeInstitucion) {
        this.apeInstitucion = apeInstitucion;
    }

    public SgTipoAccion getApeTipoAccion() {
        return apeTipoAccion;
    }

    public void setApeTipoAccion(SgTipoAccion apeTipoAccion) {
        this.apeTipoAccion = apeTipoAccion;
    }

    public String getApeDescripcion() {
        return apeDescripcion;
    }

    public void setApeDescripcion(String apeDescripcion) {
        this.apeDescripcion = apeDescripcion;
    }

    public LocalDateTime getApeUltModFecha() {
        return apeUltModFecha;
    }

    public void setApeUltModFecha(LocalDateTime apeUltModFecha) {
        this.apeUltModFecha = apeUltModFecha;
    }

    public String getApeUltModUsuario() {
        return apeUltModUsuario;
    }

    public void setApeUltModUsuario(String apeUltModUsuario) {
        this.apeUltModUsuario = apeUltModUsuario;
    }

    public Integer getApeVersion() {
        return apeVersion;
    }

    public void setApeVersion(Integer apeVersion) {
        this.apeVersion = apeVersion;
    }



    @Override
    public String securityAmbitCreate() {
       return "apeSede";
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "apeSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "apeSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calPk", -1L);
        } 
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.apePk);
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
        final SgAccionPrevenirEmbarazo other = (SgAccionPrevenirEmbarazo) obj;
        if (!Objects.equals(this.apePk, other.apePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAccionPrevenirEmbarazo{" + "apePk=" + apePk +", apeUltModFecha=" + apeUltModFecha + ", apeUltModUsuario=" + apeUltModUsuario + ", apeVersion=" + apeVersion + '}';
    }
    
    

}
