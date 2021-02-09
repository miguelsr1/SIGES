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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosDescargo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfOpcionesDescargo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.persistencia.entidades.centroseducativos.SgSede;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_descargos", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "desPk", scope = SgAfDescargo.class)
public class SgAfDescargo implements Serializable, DataSecurity {

    private static final long serialVersionUID = 167347345734L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "des_pk")
    private Long desPk;
    
    @Size(max = 50)
    @Column(name = "des_codigo_descargo", length = 50)
    private String desCodigoDescargo;
    
    @Size(max = 150)
    @Column(name = "des_nombre_coord_adm", length = 150)
    private String desNombreCoordAdm;
    
    @Size(max = 150)
    @Column(name = "des_nombre_encargado_af", length = 150)
    private String desNombreEncargadoAf;
    
    @Size(max = 150)
    @Column(name = "des_nombre_director", length = 150)
    private String desNombreDirector;
    
    @Size(max = 150)
    @Column(name = "des_nombre_autoriza", length = 150)
    private String desNombreAutoriza;
    
    @Size(max = 150)
    @Column(name = "des_cargo_autoriza", length = 150)
    private String desCargoAutoriza;
    
    @Column(name = "des_activo")
    private Boolean desActivo;
    
    @Size(max = 250)
    @Column(name = "des_observacion_fallo", length = 250)
    private String desObservacionFallo;
    
    @Column(name = "des_fecha_creacion")
    private LocalDateTime desFechaCreacion;
    
    @Column(name = "des_fecha_solicitud")
    private LocalDate desFechaSolicitud;
    
    @Column(name = "des_fecha_envio_solicitud")
    private LocalDateTime desFechaEnvioSolicitud;
    
    @Column(name = "des_fecha_descargo")
    private LocalDate desFechaDescargo;
    
    @Column(name = "des_fecha_fallo")
    private LocalDate desFechaFallo;

    @JoinColumn(name = "des_tipo_descargo_fk", referencedColumnName = "ede_pk")
    @ManyToOne
    private SgAfEstadosDescargo desTipoDescargoFk;
 
    @JoinColumn(name = "des_opcion_descargo_fk", referencedColumnName = "ode_pk")
    @ManyToOne
    private SgAfOpcionesDescargo desOpcionDescargoFk;
    
    @JoinColumn(name = "des_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede desSedeFk;
    
    @JoinColumn(name = "des_unidad_administrativa_fk", referencedColumnName = "uad_pk")
    @ManyToOne
    private SgAfUnidadesAdministrativas desUnidadAdministrativaFk;
    
    @JoinColumn(name = "des_estado_fk",  referencedColumnName = "ebi_pk")
    @ManyToOne
    private SgAfEstadosBienes desEstadoFk;
    
    @Size(max = 45)
    @Column(name = "des_usuario_creacion", length = 45)
    private String desUsuarioCreacion;
    
    @Column(name = "des_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime desUltModFecha;
    
    @Size(max = 45)
    @Column(name = "des_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String desUltModUsuario;
    
    @Column(name = "des_version")
    @Version
    private Integer desVersion;
    
    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "ddeDescargoFk",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SgAfDescargoDetalle> sgAfDescargosDetalle;
    
    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "mrdDescargoFk", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SgAfMotivoRechazoDescargo> sgAfMotivosRechazoDescargo;
    
    @JoinColumn(name = "des_acta_descargo_fk", referencedColumnName = "ade_pk")
    @OneToOne(optional = true, cascade = CascadeType.ALL,orphanRemoval=true)
    private SgAfActaDescargo desActaDescargo;
    
    
    private transient Integer cantidad;
    private transient BigDecimal totalMonto;
    
    public SgAfDescargo() {
        this.sgAfDescargosDetalle = new ArrayList();
        this.sgAfMotivosRechazoDescargo = new ArrayList();
        this.desActaDescargo = new SgAfActaDescargo();
    }

    public Long getDesPk() {
        return desPk;
    }

    public void setDesPk(Long desPk) {
        this.desPk = desPk;
    }

    public String getDesCodigoDescargo() {
        return desCodigoDescargo;
    }

    public void setDesCodigoDescargo(String desCodigoDescargo) {
        this.desCodigoDescargo = desCodigoDescargo;
    }

    public String getDesNombreCoordAdm() {
        return desNombreCoordAdm;
    }

    public void setDesNombreCoordAdm(String desNombreCoordAdm) {
        this.desNombreCoordAdm = desNombreCoordAdm;
    }

    public String getDesNombreEncargadoAf() {
        return desNombreEncargadoAf;
    }

    public void setDesNombreEncargadoAf(String desNombreEncargadoAf) {
        this.desNombreEncargadoAf = desNombreEncargadoAf;
    }

    public String getDesNombreDirector() {
        return desNombreDirector;
    }

    public void setDesNombreDirector(String desNombreDirector) {
        this.desNombreDirector = desNombreDirector;
    }

    public String getDesNombreAutoriza() {
        return desNombreAutoriza;
    }

    public void setDesNombreAutoriza(String desNombreAutoriza) {
        this.desNombreAutoriza = desNombreAutoriza;
    }

    public String getDesCargoAutoriza() {
        return desCargoAutoriza;
    }

    public void setDesCargoAutoriza(String desCargoAutoriza) {
        this.desCargoAutoriza = desCargoAutoriza;
    }

    public String getDesObservacionFallo() {
        return desObservacionFallo;
    }

    public void setDesObservacionFallo(String desObservacionFallo) {
        this.desObservacionFallo = desObservacionFallo;
    }

    public LocalDateTime getDesFechaCreacion() {
        return desFechaCreacion;
    }

    public void setDesFechaCreacion(LocalDateTime desFechaCreacion) {
        this.desFechaCreacion = desFechaCreacion;
    }

    public LocalDate getDesFechaSolicitud() {
        return desFechaSolicitud;
    }

    public void setDesFechaSolicitud(LocalDate desFechaSolicitud) {
        this.desFechaSolicitud = desFechaSolicitud;
    }

    public LocalDate getDesFechaDescargo() {
        return desFechaDescargo;
    }

    public void setDesFechaDescargo(LocalDate desFechaDescargo) {
        this.desFechaDescargo = desFechaDescargo;
    }

    public LocalDate getDesFechaFallo() {
        return desFechaFallo;
    }

    public void setDesFechaFallo(LocalDate desFechaFallo) {
        this.desFechaFallo = desFechaFallo;
    }

    public SgAfEstadosDescargo getDesTipoDescargoFk() {
        return desTipoDescargoFk;
    }

    public void setDesTipoDescargoFk(SgAfEstadosDescargo desTipoDescargoFk) {
        this.desTipoDescargoFk = desTipoDescargoFk;
    }

    public SgSede getDesSedeFk() {
        return desSedeFk;
    }

    public void setDesSedeFk(SgSede desSedeFk) {
        this.desSedeFk = desSedeFk;
    }

    public SgAfUnidadesAdministrativas getDesUnidadAdministrativaFk() {
        return desUnidadAdministrativaFk;
    }

    public void setDesUnidadAdministrativaFk(SgAfUnidadesAdministrativas desUnidadAdministrativaFk) {
        this.desUnidadAdministrativaFk = desUnidadAdministrativaFk;
    }

    public SgAfEstadosBienes getDesEstadoFk() {
        return desEstadoFk;
    }

    public void setDesEstadoFk(SgAfEstadosBienes desEstadoFk) {
        this.desEstadoFk = desEstadoFk;
    }
    

    public String getDesUsuarioCreacion() {
        return desUsuarioCreacion;
    }

    public void setDesUsuarioCreacion(String desUsuarioCreacion) {
        this.desUsuarioCreacion = desUsuarioCreacion;
    }

    public LocalDateTime getDesUltModFecha() {
        return desUltModFecha;
    }

    public void setDesUltModFecha(LocalDateTime desUltModFecha) {
        this.desUltModFecha = desUltModFecha;
    }

    public String getDesUltModUsuario() {
        return desUltModUsuario;
    }

    public void setDesUltModUsuario(String desUltModUsuario) {
        this.desUltModUsuario = desUltModUsuario;
    }

    public Integer getDesVersion() {
        return desVersion;
    }

    public void setDesVersion(Integer desVersion) {
        this.desVersion = desVersion;
    }

    public List<SgAfDescargoDetalle> getSgAfDescargosDetalle() {
        return sgAfDescargosDetalle;
    }

    public void setSgAfDescargosDetalle(List<SgAfDescargoDetalle> sgAfDescargosDetalle) {
        if(this.sgAfDescargosDetalle != null) {
            this.sgAfDescargosDetalle.clear();
            this.sgAfDescargosDetalle.addAll(sgAfDescargosDetalle); 
        } 
    }

    public Boolean getDesActivo() {
        return desActivo;
    }

    public void setDesActivo(Boolean desActivo) {
        this.desActivo = desActivo;
    }

    public LocalDateTime getDesFechaEnvioSolicitud() {
        return desFechaEnvioSolicitud;
    }

    public void setDesFechaEnvioSolicitud(LocalDateTime desFechaEnvioSolicitud) {
        this.desFechaEnvioSolicitud = desFechaEnvioSolicitud;
    }

    public List<SgAfMotivoRechazoDescargo> getSgAfMotivosRechazoDescargo() {
        return sgAfMotivosRechazoDescargo;
    }

    public SgAfOpcionesDescargo getDesOpcionDescargoFk() {
        return desOpcionDescargoFk;
    }

    public void setDesOpcionDescargoFk(SgAfOpcionesDescargo desOpcionDescargoFk) {
        this.desOpcionDescargoFk = desOpcionDescargoFk;
    }

    public void setSgAfMotivosRechazoDescargo(List<SgAfMotivoRechazoDescargo> sgAfMotivosRechazoDescargo) {
        if(this.sgAfMotivosRechazoDescargo != null) {
            this.sgAfMotivosRechazoDescargo.clear();
            this.sgAfMotivosRechazoDescargo.addAll(sgAfMotivosRechazoDescargo); 
        } 
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotalMonto() {
        return totalMonto;
    }

    public void setTotalMonto(BigDecimal totalMonto) {
        this.totalMonto = totalMonto;
    }

    public SgAfActaDescargo getDesActaDescargo() {
        return desActaDescargo;
    }

    public void setDesActaDescargo(SgAfActaDescargo desActaDescargo) {
        this.desActaDescargo = desActaDescargo;
    }
    
    @Override
    public String securityAmbitCreate() {
        if(desSedeFk != null) {
            return "desSedeFk";
        } else if(desUnidadAdministrativaFk != null) {
            return "desUnidadAdministrativaFk";
        } else {
            return null;
        }
    } 
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "desSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "desUnidadAdministrativaFk.uadUnidadActivoFijoFk.uafDepartamento.depPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "desSedeFk.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.UNIDAD_ADMINISTRATIVA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "desUnidadAdministrativaFk.uadPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "desPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (desPk != null ? desPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfDescargo)) {
            return false;
        }
        SgAfDescargo other = (SgAfDescargo) object;
        if ((this.desPk == null && other.desPk != null) || (this.desPk != null && !this.desPk.equals(other.desPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfDescargos[ desPk=" + desPk + " ]";
    }
    
}
