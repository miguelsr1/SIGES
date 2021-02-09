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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFormula;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_grado_plan_estudio", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rgpPk", scope = SgRelGradoPlanEstudio.class)
@Audited
public class SgRelGradoPlanEstudio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rgp_pk", nullable = false)
    private Long rgpPk;
    
    @JoinColumn(name = "rgp_grado_fk", referencedColumnName = "gra_pk")
    @ManyToOne
    private SgGrado rgpGradoFk;
    
    @JoinColumn(name = "rgp_plan_estudio_fk", referencedColumnName = "pes_pk")
    @ManyToOne
    private SgPlanEstudio rgpPlanEstudioFk;
    
    @JoinColumn(name = "rgp_formula_fk", referencedColumnName = "fom_pk")
    @ManyToOne
    private SgFormula rgpFormulaFk;
    
    @Column(name = "rgp_permite_calificar_sin_mat_validada")
    private Boolean rgpPermiteCalificarSinMatValidada;
    
    @Column(name = "rgp_permite_calificar_con_mat_provisional")
    private Boolean rgpPermiteCalificarConMatProvisional;
    
    @Column(name = "rgp_habilitado")
    @AtributoHabilitado
    private Boolean rgpHabilitado;

    @Column(name = "rgp_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rgpUltModFecha;

    @Size(max = 45)
    @Column(name = "rgp_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rgpUltModUsuario;

    @Column(name = "rgp_version")
    @Version
    private Integer rgpVersion;
    
    
    @Column(name = "rgp_considerar_orden")
    private Boolean rgpConsiderarOrden;
    
    @Column(name = "rgp_permite_validar_matricula_sin_nie")
    private Boolean rgpPermiteValidarMatriculaSinNIE;
    
    @Column(name = "rgp_requiere_validacion_academica")
    private Boolean rgpRequiereValidacionAcademica;
    
    @JoinColumn(name = "rpg_formula_habilitacion_PP_fk", referencedColumnName = "fom_pk")
    @ManyToOne
    private SgFormula rgpFormulaHabilitacionPP;
    
    @JoinColumn(name = "rpg_formula_habilitacion_PS_fk", referencedColumnName = "fom_pk")
    @ManyToOne
    private SgFormula rgpFormulaHabilitacionSP;
    
    @JoinColumn(name = "rpg_formula_habilitacion_SP_fk", referencedColumnName = "fom_pk")
    @ManyToOne
    private SgFormula rgpFormulaHabilitacionPS;
    
    @JoinColumn(name = "rpg_formula_habilitacion_SS_fk", referencedColumnName = "fom_pk")
    @ManyToOne
    private SgFormula rgpFormulaHabilitacionSS;
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_rel_grado_plan_definicion_titulo",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "rgp_pk"),
            inverseJoinColumns = @JoinColumn(name = "dti_pk"))
    @NotAudited
    private List<SgDefinicionTitulo> rgpDefinicionTitulo;
    
    @Column(name = "rgp_anual")
    private Boolean rgpAnual;

    public SgRelGradoPlanEstudio() {
        this.rgpHabilitado = Boolean.TRUE;
        this.rgpConsiderarOrden = Boolean.TRUE;
        this.rgpPermiteCalificarSinMatValidada = Boolean.TRUE;
        this.rgpPermiteCalificarConMatProvisional = Boolean.TRUE;
        this.rgpPermiteValidarMatriculaSinNIE = Boolean.TRUE;
        this.rgpRequiereValidacionAcademica = Boolean.TRUE;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        
    }

    public Long getRgpPk() {
        return rgpPk;
    }

    public void setRgpPk(Long rgpPk) {
        this.rgpPk = rgpPk;
    }


    public Boolean getRgpHabilitado() {
        return rgpHabilitado;
    }

    public void setRgpHabilitado(Boolean rgpHabilitado) {
        this.rgpHabilitado = rgpHabilitado;
    }

    public LocalDateTime getRgpUltModFecha() {
        return rgpUltModFecha;
    }

    public void setRgpUltModFecha(LocalDateTime rgpUltModFecha) {
        this.rgpUltModFecha = rgpUltModFecha;
    }

    public String getRgpUltModUsuario() {
        return rgpUltModUsuario;
    }

    public void setRgpUltModUsuario(String rgpUltModUsuario) {
        this.rgpUltModUsuario = rgpUltModUsuario;
    }

    public Integer getRgpVersion() {
        return rgpVersion;
    }

    public void setRgpVersion(Integer rgpVersion) {
        this.rgpVersion = rgpVersion;
    }

    public SgGrado getRgpGradoFk() {
        return rgpGradoFk;
    }

    public void setRgpGradoFk(SgGrado rgpGradoFk) {
        this.rgpGradoFk = rgpGradoFk;
    }

    public SgPlanEstudio getRgpPlanEstudioFk() {
        return rgpPlanEstudioFk;
    }

    public void setRgpPlanEstudioFk(SgPlanEstudio rgpPlanEstudioFk) {
        this.rgpPlanEstudioFk = rgpPlanEstudioFk;
    }

    public SgFormula getRgpFormulaFk() {
        return rgpFormulaFk;
    }

    public void setRgpFormulaFk(SgFormula rgpFormulaFk) {
        this.rgpFormulaFk = rgpFormulaFk;
    }

    public Boolean getRgpConsiderarOrden() {
        return rgpConsiderarOrden;
    }

    public void setRgpConsiderarOrden(Boolean rgpConsiderarOrden) {
        this.rgpConsiderarOrden = rgpConsiderarOrden;
    }

    public Boolean getRgpPermiteCalificarSinMatValidada() {
        return rgpPermiteCalificarSinMatValidada;
    }

    public void setRgpPermiteCalificarSinMatValidada(Boolean rgpPermiteCalificarSinMatValidada) {
        this.rgpPermiteCalificarSinMatValidada = rgpPermiteCalificarSinMatValidada;
    }

    public Boolean getRgpPermiteCalificarConMatProvisional() {
        return rgpPermiteCalificarConMatProvisional;
    }

    public void setRgpPermiteCalificarConMatProvisional(Boolean rgpPermiteCalificarConMatProvisional) {
        this.rgpPermiteCalificarConMatProvisional = rgpPermiteCalificarConMatProvisional;
    }

    public Boolean getRgpPermiteValidarMatriculaSinNIE() {
        return rgpPermiteValidarMatriculaSinNIE;
    }

    public void setRgpPermiteValidarMatriculaSinNIE(Boolean rgpPermiteValidarMatriculaSinNIE) {
        this.rgpPermiteValidarMatriculaSinNIE = rgpPermiteValidarMatriculaSinNIE;
    }
    
    public Boolean getRgpRequiereValidacionAcademica() {
        return rgpRequiereValidacionAcademica;
    }

    public void setRgpRequiereValidacionAcademica(Boolean rgpRequiereValidacionAcademica) {
        this.rgpRequiereValidacionAcademica = rgpRequiereValidacionAcademica;
    }

    public SgFormula getRgpFormulaHabilitacionPP() {
        return rgpFormulaHabilitacionPP;
    }

    public void setRgpFormulaHabilitacionPP(SgFormula rgpFormulaHabilitacionPP) {
        this.rgpFormulaHabilitacionPP = rgpFormulaHabilitacionPP;
    }

    public SgFormula getRgpFormulaHabilitacionSP() {
        return rgpFormulaHabilitacionSP;
    }

    public void setRgpFormulaHabilitacionSP(SgFormula rgpFormulaHabilitacionSP) {
        this.rgpFormulaHabilitacionSP = rgpFormulaHabilitacionSP;
    }

    public SgFormula getRgpFormulaHabilitacionPS() {
        return rgpFormulaHabilitacionPS;
    }

    public void setRgpFormulaHabilitacionPS(SgFormula rgpFormulaHabilitacionPS) {
        this.rgpFormulaHabilitacionPS = rgpFormulaHabilitacionPS;
    }

    public SgFormula getRgpFormulaHabilitacionSS() {
        return rgpFormulaHabilitacionSS;
    }

    public void setRgpFormulaHabilitacionSS(SgFormula rgpFormulaHabilitacionSS) {
        this.rgpFormulaHabilitacionSS = rgpFormulaHabilitacionSS;
    }

    public List<SgDefinicionTitulo> getRgpDefinicionTitulo() {
        return rgpDefinicionTitulo;
    }

    public void setRgpDefinicionTitulo(List<SgDefinicionTitulo> rgpDefinicionTitulo) {
        this.rgpDefinicionTitulo = rgpDefinicionTitulo;
    }

    public Boolean getRgpAnual() {
        return rgpAnual;
    }

    public void setRgpAnual(Boolean rgpAnual) {
        this.rgpAnual = rgpAnual;
    }

    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.rgpPk);
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
        final SgRelGradoPlanEstudio other = (SgRelGradoPlanEstudio) obj;
        if (!Objects.equals(this.rgpPk, other.rgpPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelGradoPlanEstudio{" + "rgpPk=" + rgpPk + ", rgpHabilitado=" + rgpHabilitado + ", rgpUltModFecha=" + rgpUltModFecha + ", rgpUltModUsuario=" + rgpUltModUsuario + ", rgpVersion=" + rgpVersion + '}';
    }
    
    

}
