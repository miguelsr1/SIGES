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
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.enumerados.EnumServicioEducativoEstado;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProgramaEducativo;

/**
 *
 * NOTA: Si se agrega un nuevo campo a esta entidad, verificar el método
 * copiarServicios en la clase ServicioEducativoBean.java de ser necesario
 * agregar dicho campo al filtro IncluirCampos
 *
 */
@Entity
@Table(name = "sg_servicio_educativo", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sduPk", scope = SgServicioEducativo.class)
@Audited
public class SgServicioEducativo implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sdu_pk")
    private Long sduPk;

    @JoinColumn(name = "sdu_grado_fk", referencedColumnName = "gra_pk")
    @ManyToOne(optional = false)
    private SgGrado sduGrado;

    @Column(name = "sdu_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumServicioEducativoEstado sduEstado;

    @Column(name = "sdu_fecha_habilitado")
    private LocalDate sduFechaHabilitado;

    @Column(name = "sdu_fecha_solicitada")
    private LocalDate sduFechaSolicitada;

    @Size(max = 20)
    @Column(name = "sdu_numero_tramite", length = 20)
    private String sduNumeroTramite;

    @JoinColumn(name = "sdu_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne(optional = false)
    private SgSede sduSede;

    @Column(name = "sdu_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sduUltModFecha;

    @Size(max = 45)
    @Column(name = "sdu_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String sduUltModUsuario;

    @Column(name = "sdu_version")
    @Version
    private Integer sduVersion;

    @JoinColumn(name = "sdu_opcion_fk", referencedColumnName = "opc_pk")
    @ManyToOne
    private SgOpcion sduOpcion;

    @JoinColumn(name = "sdu_programa_educativo_fk", referencedColumnName = "ped_pk")
    @ManyToOne
    private SgProgramaEducativo sduProgramaEducativo;

    @OneToMany(mappedBy = "secServicioEducativo")
    @NotAudited
    private List<SgSeccion> sduSeccion;

    @Transient
    private Boolean sduOrigenExterno;

    public SgServicioEducativo() {
    }

    public SgServicioEducativo(Long sduPk) {
        this.sduPk = sduPk;
    }

    public Long getSduPk() {
        return sduPk;
    }

    public void setSduPk(Long sduPk) {
        this.sduPk = sduPk;
    }

    public EnumServicioEducativoEstado getSduEstado() {
        return sduEstado;
    }

    public void setSduEstado(EnumServicioEducativoEstado sduEstado) {
        this.sduEstado = sduEstado;
    }

    public LocalDate getSduFechaHabilitado() {
        return sduFechaHabilitado;
    }

    public void setSduFechaHabilitado(LocalDate sduFechaHabilitado) {
        this.sduFechaHabilitado = sduFechaHabilitado;
    }

    public LocalDate getSduFechaSolicitada() {
        return sduFechaSolicitada;
    }

    public void setSduFechaSolicitada(LocalDate sduFechaSolicitada) {
        this.sduFechaSolicitada = sduFechaSolicitada;
    }

    public String getSduNumeroTramite() {
        return sduNumeroTramite;
    }

    public void setSduNumeroTramite(String sduNumeroTramite) {
        this.sduNumeroTramite = sduNumeroTramite;
    }

    public LocalDateTime getSduUltModFecha() {
        return sduUltModFecha;
    }

    public void setSduUltModFecha(LocalDateTime sduUltModFecha) {
        this.sduUltModFecha = sduUltModFecha;
    }

    public String getSduUltModUsuario() {
        return sduUltModUsuario;
    }

    public void setSduUltModUsuario(String sduUltModUsuario) {
        this.sduUltModUsuario = sduUltModUsuario;
    }

    public Integer getSduVersion() {
        return sduVersion;
    }

    public void setSduVersion(Integer sduVersion) {
        this.sduVersion = sduVersion;
    }

    public SgGrado getSduGrado() {
        return sduGrado;
    }

    public void setSduGrado(SgGrado sduGrado) {
        this.sduGrado = sduGrado;
    }

    public SgSede getSduSede() {
        return sduSede;
    }

    public void setSduSede(SgSede sduSede) {
        this.sduSede = sduSede;
    }

    public SgOpcion getSduOpcion() {
        return sduOpcion;
    }

    public void setSduOpcion(SgOpcion sduOpcion) {
        this.sduOpcion = sduOpcion;
    }

    public SgProgramaEducativo getSduProgramaEducativo() {
        return sduProgramaEducativo;
    }

    public void setSduProgramaEducativo(SgProgramaEducativo sduProgramaEducativo) {
        this.sduProgramaEducativo = sduProgramaEducativo;
    }

    public List<SgSeccion> getSduSeccion() {
        return sduSeccion;
    }

    public void setSduSeccion(List<SgSeccion> sduSeccion) {
        this.sduSeccion = sduSeccion;
    }

    public Boolean getSduOrigenExterno() {
        return sduOrigenExterno;
    }

    public void setSduOrigenExterno(Boolean sduOrigenExterno) {
        this.sduOrigenExterno = sduOrigenExterno;
    }

    @Override
    public String securityAmbitCreate() {
        return "sduSede";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSede.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "sduSede.sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSeccion.secAsociacion.asoPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSeccion.secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSeccion.secMatricula.matEstudiante.estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.sduPk);
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
        final SgServicioEducativo other = (SgServicioEducativo) obj;
        if (!Objects.equals(this.sduPk, other.sduPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgServicioEducativo[ sduPk=" + sduPk + " ]";
    }

}
