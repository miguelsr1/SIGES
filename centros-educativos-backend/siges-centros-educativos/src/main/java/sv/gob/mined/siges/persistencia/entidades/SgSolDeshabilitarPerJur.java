/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
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
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudOAE;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_sol_deshabilitar_perjur", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dpjPk", scope = SgSolDeshabilitarPerJur.class)
@Audited
public class SgSolDeshabilitarPerJur implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dpj_pk", nullable = false)
    private Long dpjPk;
    
    @Column(name = "dpj_motivo")
    private String dpjMotivo;
    
    @Column(name = "dpj_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoSolicitudOAE dpjEstado;
    
    @JoinColumn(name = "dpj_acta_fk", referencedColumnName = "ach_pk")
    @ManyToOne(cascade = CascadeType.ALL)
    private SgArchivo dpjActa;
    
    @Column(name = "dpj_numero_acuerdo")
    private String dpjNumeroAcuerdo;
    
    @Column(name = "dpj_fecha_acuerdo")
    private LocalDate dpjFechaAcuerdo;
    
    @JoinColumn(name = "dpj_acuerdo_fk", referencedColumnName = "ach_pk")
    @ManyToOne(cascade = CascadeType.ALL)
    private SgArchivo dpjAcuerdo;
    
    @JoinColumn(name = "dpj_org_admin_esc", referencedColumnName = "oae_pk")
    @ManyToOne
    private SgOrganismoAdministracionEscolar dpjOaeFk;
    
    @Column(name = "dpj_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dpjUltModFecha;

    @Size(max = 45)
    @Column(name = "dpj_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dpjUltModUsuario;

    @Column(name = "dpj_version")
    @Version
    private Integer dpjVersion;
    
    @Transient
    private Boolean dpjAprobar;

    public SgSolDeshabilitarPerJur() {
    }

    public Long getDpjPk() {
        return dpjPk;
    }

    public void setDpjPk(Long dpjPk) {
        this.dpjPk = dpjPk;
    }

    public String getDpjMotivo() {
        return dpjMotivo;
    }

    public void setDpjMotivo(String dpjMotivo) {
        this.dpjMotivo = dpjMotivo;
    }

    public EnumEstadoSolicitudOAE getDpjEstado() {
        return dpjEstado;
    }

    public void setDpjEstado(EnumEstadoSolicitudOAE dpjEstado) {
        this.dpjEstado = dpjEstado;
    }

    public SgArchivo getDpjActa() {
        return dpjActa;
    }

    public void setDpjActa(SgArchivo dpjActa) {
        this.dpjActa = dpjActa;
    }

    public String getDpjNumeroAcuerdo() {
        return dpjNumeroAcuerdo;
    }

    public void setDpjNumeroAcuerdo(String dpjNumeroAcuerdo) {
        this.dpjNumeroAcuerdo = dpjNumeroAcuerdo;
    }

    public LocalDate getDpjFechaAcuerdo() {
        return dpjFechaAcuerdo;
    }

    public void setDpjFechaAcuerdo(LocalDate dpjFechaAcuerdo) {
        this.dpjFechaAcuerdo = dpjFechaAcuerdo;
    }

    public SgArchivo getDpjAcuerdo() {
        return dpjAcuerdo;
    }

    public void setDpjAcuerdo(SgArchivo dpjAcuerdo) {
        this.dpjAcuerdo = dpjAcuerdo;
    }

    public SgOrganismoAdministracionEscolar getDpjOaeFk() {
        return dpjOaeFk;
    }

    public void setDpjOaeFk(SgOrganismoAdministracionEscolar dpjOaeFk) {
        this.dpjOaeFk = dpjOaeFk;
    }

    public LocalDateTime getDpjUltModFecha() {
        return dpjUltModFecha;
    }

    public void setDpjUltModFecha(LocalDateTime dpjUltModFecha) {
        this.dpjUltModFecha = dpjUltModFecha;
    }

    public String getDpjUltModUsuario() {
        return dpjUltModUsuario;
    }

    public void setDpjUltModUsuario(String dpjUltModUsuario) {
        this.dpjUltModUsuario = dpjUltModUsuario;
    }

    public Integer getDpjVersion() {
        return dpjVersion;
    }

    public void setDpjVersion(Integer dpjVersion) {
        this.dpjVersion = dpjVersion;
    }

    public Boolean getDpjAprobar() {
        return dpjAprobar;
    }

    public void setDpjAprobar(Boolean dpjAprobar) {
        this.dpjAprobar = dpjAprobar;
    }
    
    @Override
    public String securityAmbitCreate() {     
        return null;
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedServicioEducativo.sduSeccion.secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedServicioEducativo.sduSeccion.secMatricula.matEstudiante.estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "dpjOaeFk.oaeSede.sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedServicioEducativo.sduSeccion.secAsociacion.asoPk", o.getContext());
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.dpjPk);
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
        final SgSolDeshabilitarPerJur other = (SgSolDeshabilitarPerJur) obj;
        if (!Objects.equals(this.dpjPk, other.dpjPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSolDeshabilitarPerJur{" + "dpjPk=" + dpjPk + ", dpjMotivo=" + dpjMotivo + ", dpjArchivo=" + dpjActa + ", dpjUltModFecha=" + dpjUltModFecha + ", dpjUltModUsuario=" + dpjUltModUsuario + ", dpjVersion=" + dpjVersion + '}';
    }
    
    
}
