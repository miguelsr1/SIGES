/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoAcuerdo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_acuerdos_sedes", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "asePk", scope = SgAcuerdoSede.class)
@Audited
public class SgAcuerdoSede implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ase_pk")
    private Long asePk;

    @JoinColumn(name = "ase_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede aseSede;

    @JoinColumn(name = "ase_tipo_acuerdo_fk", referencedColumnName = "tao_pk")
    @ManyToOne
    private SgTipoAcuerdo aseTipoAcuerdo;

    @Column(name = "ase_numero_resolucion")
    private String aseNumeroResolucion;
    
    @Column(name = "ase_fecha_resolucion")
    private LocalDate aseFechaResolucion;

    @Size(max = 10)
    @Column(name = "ase_numero_acuerdo", length = 10)
    private String aseNumeroAcuerdo;

    @Column(name = "ase_fecha_registro")
    private LocalDate aseFechaRegistro;

    @Size(max = 100)
    @Column(name = "ase_nombre_responsable", length = 100)
    private String aseNombreResponsable;

    @Column(name = "ase_fecha_generacion")
    private LocalDate aseFechaGeneracion;

    @Size(max = 4000)
    @Column(name = "ase_observacion", length = 4000)
    private String aseObservacion;

    @Size(max = 10)
    @Column(name = "ase_numero_solicitud", length = 10)
    private String aseNumeroSolicitud;

    @Column(name = "ase_solo_lectura")
    private Boolean aseSoloLectura;

    @Size(max = 10)
    @Column(name = "ase_codigo_nominacion", length = 10)
    private String aseCodigoNominacion;

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

    @Transient
    private Boolean aseOrigenExterno;

    public SgAcuerdoSede() {
    }

    public SgAcuerdoSede(Long asePk) {
        this.asePk = asePk;
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

    public SgTipoAcuerdo getAseTipoAcuerdo() {
        return aseTipoAcuerdo;
    }

    public void setAseTipoAcuerdo(SgTipoAcuerdo aseTipoAcuerdo) {
        this.aseTipoAcuerdo = aseTipoAcuerdo;
    }

    public String getAseNumeroResolucion() {
        return aseNumeroResolucion;
    }

    public void setAseNumeroResolucion(String aseNumeroResolucion) {
        this.aseNumeroResolucion = aseNumeroResolucion;
    }

    public LocalDate getAseFechaResolucion() {
        return aseFechaResolucion;
    }

    public void setAseFechaResolucion(LocalDate aseFechaResolucion) {
        this.aseFechaResolucion = aseFechaResolucion;
    }

   

    public String getAseNumeroAcuerdo() {
        return aseNumeroAcuerdo;
    }

    public void setAseNumeroAcuerdo(String aseNumeroAcuerdo) {
        this.aseNumeroAcuerdo = aseNumeroAcuerdo;
    }

    public LocalDate getAseFechaRegistro() {
        return aseFechaRegistro;
    }

    public void setAseFechaRegistro(LocalDate aseFechaRegistro) {
        this.aseFechaRegistro = aseFechaRegistro;
    }

    public String getAseNombreResponsable() {
        return aseNombreResponsable;
    }

    public void setAseNombreResponsable(String aseNombreResponsable) {
        this.aseNombreResponsable = aseNombreResponsable;
    }

    public LocalDate getAseFechaGeneracion() {
        return aseFechaGeneracion;
    }

    public void setAseFechaGeneracion(LocalDate aseFechaGeneracion) {
        this.aseFechaGeneracion = aseFechaGeneracion;
    }

    public String getAseObservacion() {
        return aseObservacion;
    }

    public void setAseObservacion(String aseObservacion) {
        this.aseObservacion = aseObservacion;
    }

    public String getAseNumeroSolicitud() {
        return aseNumeroSolicitud;
    }

    public void setAseNumeroSolicitud(String aseNumeroSolicitud) {
        this.aseNumeroSolicitud = aseNumeroSolicitud;
    }

    public String getAseCodigoNominacion() {
        return aseCodigoNominacion;
    }

    public void setAseCodigoNominacion(String aseCodigoNominacion) {
        this.aseCodigoNominacion = aseCodigoNominacion;
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

    public Boolean getAseSoloLectura() {
        return aseSoloLectura;
    }

    public void setAseSoloLectura(Boolean aseSoloLectura) {
        this.aseSoloLectura = aseSoloLectura;
    }

    public Boolean getAseOrigenExterno() {
        return aseOrigenExterno;
    }

    public void setAseOrigenExterno(Boolean aseOrigenExterno) {
        this.aseOrigenExterno = aseOrigenExterno;
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
        int hash = 0;
        hash += (asePk != null ? asePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAcuerdoSede)) {
            return false;
        }
        SgAcuerdoSede other = (SgAcuerdoSede) object;
        if ((this.asePk == null && other.asePk != null) || (this.asePk != null && !this.asePk.equals(other.asePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAcuerdoSede[ asePk=" + asePk + " ]";
    }

}
