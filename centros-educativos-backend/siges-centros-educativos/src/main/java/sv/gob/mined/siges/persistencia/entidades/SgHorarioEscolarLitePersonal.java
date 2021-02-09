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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_horarios_escolares", uniqueConstraints = {
    @UniqueConstraint(name = "hes_seccion_uk", columnNames = {"hes_seccion_fk"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "hesPk", scope = SgHorarioEscolarLitePersonal.class)
@Audited
public class SgHorarioEscolarLitePersonal implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "hes_pk", nullable = false)
    private Long hesPk;

    @Column(name = "hes_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime hesUltModFecha;

    @Size(max = 45)
    @Column(name = "hes_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String hesUltModUsuario;

    @Column(name = "hes_version")
    @Version
    private Integer hesVersion;

    @JoinColumn(name = "hes_seccion_fk", referencedColumnName = "sec_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSeccion hesSeccion; // -> Se inicializa en obtenerPorId

    @Column(name = "hes_unico_docente")
    private Boolean hesUnicoDocente;

    @JoinColumn(name = "hes_docente_fk", referencedColumnName = "pse_pk")
    @ManyToOne
    private SgPersonalSedeEducativa hesDocente; //unico docente


    public SgHorarioEscolarLitePersonal() {
    }

    public Long getHesPk() {
        return hesPk;
    }

    public void setHesPk(Long hesPk) {
        this.hesPk = hesPk;
    }

    public SgSeccion getHesSeccion() {
        return hesSeccion;
    }

    public void setHesSeccion(SgSeccion hesSeccion) {
        this.hesSeccion = hesSeccion;
    }

    public LocalDateTime getHesUltModFecha() {
        return hesUltModFecha;
    }

    public void setHesUltModFecha(LocalDateTime hesUltModFecha) {
        this.hesUltModFecha = hesUltModFecha;
    }

    public String getHesUltModUsuario() {
        return hesUltModUsuario;
    }

    public void setHesUltModUsuario(String hesUltModUsuario) {
        this.hesUltModUsuario = hesUltModUsuario;
    }

    public Integer getHesVersion() {
        return hesVersion;
    }

    public void setHesVersion(Integer hesVersion) {
        this.hesVersion = hesVersion;
    }
    public Boolean getHesUnicoDocente() {
        return hesUnicoDocente;
    }

    public void setHesUnicoDocente(Boolean hesUnicoDocente) {
        this.hesUnicoDocente = hesUnicoDocente;
    }

    public SgPersonalSedeEducativa getHesDocente() {
        return hesDocente;
    }

    public void setHesDocente(SgPersonalSedeEducativa hesDocente) {
        this.hesDocente = hesDocente;
    }

   

    @Override
    public String securityAmbitCreate() {
        return "hesSeccion";
    }
    
     @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesSeccion.secServicioEducativo.sduSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesSeccion.secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else {
             return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesPk", -1L);
        } 
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.hesPk);
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
        final SgHorarioEscolarLitePersonal other = (SgHorarioEscolarLitePersonal) obj;
        if (!Objects.equals(this.hesPk, other.hesPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgHorarioEscolar{" + "hesPk=" + hesPk + ", hesUltModFecha=" + hesUltModFecha + ", hesUltModUsuario=" + hesUltModUsuario + ", hesVersion=" + hesVersion + '}';
    }

}
