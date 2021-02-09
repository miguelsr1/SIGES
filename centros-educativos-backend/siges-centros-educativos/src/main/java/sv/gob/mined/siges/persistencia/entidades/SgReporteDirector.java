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
@Table(name = "sg_reporte_director", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rdiPk", scope = SgReporteDirector.class)
@Audited
public class SgReporteDirector implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rdi_pk", nullable = false)
    private Long rdiPk;

    @Column(name = "rdi_observacion_sede")
    private String rdiObservacionSede;
    
    @Column(name = "rdi_datos_sede")
    private Boolean rdiDatosSede;
    
    @Column(name = "rdi_con_observaciones_sede")
    private Boolean rdiConObservacionesSede;
    
    @Column(name = "rdi_observacion_estudiante")
    private String rdiObservacionEstudiante;
    
    @Column(name = "rdi_datos_estudiante")
    private Boolean rdiDatosEstudiante;
    
    @Column(name = "rdi_con_observaciones_estudiante")
    private Boolean rdiConObservacionesEstudiante;
    
    @Column(name = "rdi_observacion_personal")
    private String rdiObservacionPersonal;
    
    @Column(name = "rdi_datos_personal")
    private Boolean rdiDatosPersonal;
    
    @Column(name = "rdi_con_observaciones_personal")
    private Boolean rdiConObservacionesPersonal;

    @Column(name = "rdi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rdiUltModFecha;

    @Size(max = 45)
    @Column(name = "rdi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rdiUltModUsuario;

    @Column(name = "rdi_version")
    @Version
    private Integer rdiVersion;
    
    @JoinColumn(name = "rdi_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede rdiSede;

    public SgReporteDirector() {
    }



    public Long getRdiPk() {
        return rdiPk;
    }

    public void setRdiPk(Long rdiPk) {
        this.rdiPk = rdiPk;
    }

    public LocalDateTime getRdiUltModFecha() {
        return rdiUltModFecha;
    }

    public void setRdiUltModFecha(LocalDateTime rdiUltModFecha) {
        this.rdiUltModFecha = rdiUltModFecha;
    }

    public String getRdiUltModUsuario() {
        return rdiUltModUsuario;
    }

    public void setRdiUltModUsuario(String rdiUltModUsuario) {
        this.rdiUltModUsuario = rdiUltModUsuario;
    }

    public Integer getRdiVersion() {
        return rdiVersion;
    }

    public void setRdiVersion(Integer rdiVersion) {
        this.rdiVersion = rdiVersion;
    }

    public String getRdiObservacionSede() {
        return rdiObservacionSede;
    }

    public void setRdiObservacionSede(String rdiObservacionSede) {
        this.rdiObservacionSede = rdiObservacionSede;
    }

    public Boolean getRdiDatosSede() {
        return rdiDatosSede;
    }

    public void setRdiDatosSede(Boolean rdiDatosSede) {
        this.rdiDatosSede = rdiDatosSede;
    }

    public Boolean getRdiConObservacionesSede() {
        return rdiConObservacionesSede;
    }

    public void setRdiConObservacionesSede(Boolean rdiConObservacionesSede) {
        this.rdiConObservacionesSede = rdiConObservacionesSede;
    }

    public SgSede getRdiSede() {
        return rdiSede;
    }

    public void setRdiSede(SgSede rdiSede) {
        this.rdiSede = rdiSede;
    }

    public String getRdiObservacionEstudiante() {
        return rdiObservacionEstudiante;
    }

    public void setRdiObservacionEstudiante(String rdiObservacionEstudiante) {
        this.rdiObservacionEstudiante = rdiObservacionEstudiante;
    }

    public Boolean getRdiDatosEstudiante() {
        return rdiDatosEstudiante;
    }

    public void setRdiDatosEstudiante(Boolean rdiDatosEstudiante) {
        this.rdiDatosEstudiante = rdiDatosEstudiante;
    }

    public Boolean getRdiConObservacionesEstudiante() {
        return rdiConObservacionesEstudiante;
    }

    public void setRdiConObservacionesEstudiante(Boolean rdiConObservacionesEstudiante) {
        this.rdiConObservacionesEstudiante = rdiConObservacionesEstudiante;
    }

    public String getRdiObservacionPersonal() {
        return rdiObservacionPersonal;
    }

    public void setRdiObservacionPersonal(String rdiObservacionPersonal) {
        this.rdiObservacionPersonal = rdiObservacionPersonal;
    }

    public Boolean getRdiDatosPersonal() {
        return rdiDatosPersonal;
    }

    public void setRdiDatosPersonal(Boolean rdiDatosPersonal) {
        this.rdiDatosPersonal = rdiDatosPersonal;
    }

    public Boolean getRdiConObservacionesPersonal() {
        return rdiConObservacionesPersonal;
    }

    public void setRdiConObservacionesPersonal(Boolean rdiConObservacionesPersonal) {
        this.rdiConObservacionesPersonal = rdiConObservacionesPersonal;
    }
    
    @Override
    public String securityAmbitCreate() {
       return "rdiSede";
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiPk", -1L);
        } 
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.rdiPk);
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
        final SgReporteDirector other = (SgReporteDirector) obj;
        if (!Objects.equals(this.rdiPk, other.rdiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgReporteDirector{" + "rdiPk=" + rdiPk +", rdiUltModFecha=" + rdiUltModFecha + ", rdiUltModUsuario=" + rdiUltModUsuario + ", rdiVersion=" + rdiVersion + '}';
    }
    
    

}
