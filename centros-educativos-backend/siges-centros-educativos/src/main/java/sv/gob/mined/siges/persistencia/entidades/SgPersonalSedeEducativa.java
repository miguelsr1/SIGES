/**
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.SgUsuario;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCategoriaEscalafon;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgNivelEscalafon;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_personal_sede_educativa", uniqueConstraints = {
    @UniqueConstraint(name = "pse_codigo_uk", columnNames = {"pse_codigo"})},
        schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "psePk", scope = SgPersonalSedeEducativa.class)
public class SgPersonalSedeEducativa implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pse_pk")
    private Long psePk;

    @Size(max = 10)
    @Column(name = "pse_codigo", length = 10)
    @AtributoCodigo
    private String pseCodigo;

    @JoinColumn(name = "pse_persona_fk")
    @OneToOne(optional = false)
    private SgPersona psePersona;

    @Column(name = "pse_acceso_internet")
    private Boolean pseAccesoInternet;

    @Column(name = "pse_acceso_equipo_informatico")
    private Boolean pseAccesoEquipoInformatico;

    @Column(name = "pse_pensionado")
    private Boolean psePensionado;

    @JoinColumn(name = "pse_nivel_escalafon_fk", referencedColumnName = "nes_pk")
    @ManyToOne
    private SgNivelEscalafon pseNivelEscalafon;

    @JoinColumn(name = "pse_categoria_escalafon_fk", referencedColumnName = "ces_pk")
    @ManyToOne
    private SgCategoriaEscalafon pseCategoriaEscalafon;

    @Column(name = "pse_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pseUltModFecha;

    @Size(max = 45)
    @Column(name = "pse_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String pseUltModUsuario;

    @Column(name = "pse_version")
    @Version
    private Integer pseVersion;

//    @Column(name = "pse_tipo", nullable = false, insertable = false, updatable = false)
//    @Enumerated(EnumType.STRING)
//    private TipoPersonalSedeEducativa pseTipo;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "pse_dato_empleado_fk")
    private SgDatoEmpleado pseDatoEmpleado;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "erePersonalSede")
    private SgEstudioRealizado pseEstudioRealizado;

    @OneToMany(mappedBy = "fdoPersonalSede")
    @NotAudited
    private List<SgFormacionDocente> pseFormacionDocente;

    @OneToMany(mappedBy = "rpePersonal")
    @NotAudited
    private List<SgRelPersonalEspecialidad> pseRelEspecialidades;
    
    @Column(name = "pse_anio_servicio")
    private Integer pseAnioServicio;
       
    @Transient
    private SgDatoContratacion pseCrearConDatoContratacion;
    
    @Transient
    private Boolean psePuedeAplicarPlaza;
    
    //Esta relación se utiliza para poder filtrar personal en base a dato de secciones en la
    //que está asociada
    @OneToMany(mappedBy = "hesDocente",fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private List<SgHorarioEscolarLitePersonal> pseHorarioEscolarList;
    
    @OneToMany(mappedBy = "cdoDocente",fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private List<SgComponenteDocenteLitePersonal> pseComponenteDocenteList;

    public SgPersonalSedeEducativa() {
    }

    public SgPersonalSedeEducativa(Long psePk) {
        this.psePk = psePk;
    }

    public SgUsuario crearUsuario() {
        SgUsuario usu = new SgUsuario();
        usu.setUsuNombre(this.psePersona.getPerNombreCompleto());
        usu.setUsuCodigo(this.psePersona.getPerDui());
        usu.setUsuHabilitado(Boolean.TRUE);
        usu.setUsuPersonaPk(this.psePersona.getPerPk());
        return usu;
    }

    public Long getPsePk() {
        return psePk;
    }

    public void setPsePk(Long psePk) {
        this.psePk = psePk;
    }

    public String getPseCodigo() {
        return pseCodigo;
    }

    public void setPseCodigo(String pseCodigo) {
        this.pseCodigo = pseCodigo;
    }

    public Boolean getPseAccesoInternet() {
        return pseAccesoInternet;
    }

    public void setPseAccesoInternet(Boolean pseAccesoInternet) {
        this.pseAccesoInternet = pseAccesoInternet;
    }

    public Boolean getPseAccesoEquipoInformatico() {
        return pseAccesoEquipoInformatico;
    }

    public void setPseAccesoEquipoInformatico(Boolean pseAccesoEquipoInformatico) {
        this.pseAccesoEquipoInformatico = pseAccesoEquipoInformatico;
    }

    public Boolean getPsePensionado() {
        return psePensionado;
    }

    public void setPsePensionado(Boolean psePensionado) {
        this.psePensionado = psePensionado;
    }

    public LocalDateTime getPseUltModFecha() {
        return pseUltModFecha;
    }

    public void setPseUltModFecha(LocalDateTime pseUltModFecha) {
        this.pseUltModFecha = pseUltModFecha;
    }

    public String getPseUltModUsuario() {
        return pseUltModUsuario;
    }

    public void setPseUltModUsuario(String pseUltModUsuario) {
        this.pseUltModUsuario = pseUltModUsuario;
    }

    public Integer getPseVersion() {
        return pseVersion;
    }

    public void setPseVersion(Integer pseVersion) {
        this.pseVersion = pseVersion;
    }

    public SgNivelEscalafon getPseNivelEscalafon() {
        return pseNivelEscalafon;
    }

    public void setPseNivelEscalafon(SgNivelEscalafon pseNivelEscalafon) {
        this.pseNivelEscalafon = pseNivelEscalafon;
    }

    public SgCategoriaEscalafon getPseCategoriaEscalafon() {
        return pseCategoriaEscalafon;
    }

    public void setPseCategoriaEscalafon(SgCategoriaEscalafon pseCategoriaEscalafon) {
        this.pseCategoriaEscalafon = pseCategoriaEscalafon;
    }

    public SgPersona getPsePersona() {
        return psePersona;
    }

    public void setPsePersona(SgPersona psePersona) {
        this.psePersona = psePersona;
    }

    public SgDatoEmpleado getPseDatoEmpleado() {
        return pseDatoEmpleado;
    }

    public void setPseDatoEmpleado(SgDatoEmpleado pseDatoEmpleado) {
        this.pseDatoEmpleado = pseDatoEmpleado;
    }

    public SgEstudioRealizado getPseEstudioRealizado() {
        return pseEstudioRealizado;
    }

    public void setPseEstudioRealizado(SgEstudioRealizado pseEstudioRealizado) {
        this.pseEstudioRealizado = pseEstudioRealizado;
    }

    public List<SgFormacionDocente> getPseFormacionDocente() {
        return pseFormacionDocente;
    }

    public void setPseFormacionDocente(List<SgFormacionDocente> pseFormacionDocente) {
        this.pseFormacionDocente = pseFormacionDocente;
    }

    public List<SgRelPersonalEspecialidad> getPseRelEspecialidades() {
        return pseRelEspecialidades;
    }

    public void setPseRelEspecialidades(List<SgRelPersonalEspecialidad> pseRelEspecialidades) {
        this.pseRelEspecialidades = pseRelEspecialidades;
    }

    public Integer getPseAnioServicio() {
        return pseAnioServicio;
    }

    public void setPseAnioServicio(Integer pseAnioServicio) {
        this.pseAnioServicio = pseAnioServicio;
    }

    public SgDatoContratacion getPseCrearConDatoContratacion() {
        return pseCrearConDatoContratacion;
    }

    public void setPseCrearConDatoContratacion(SgDatoContratacion pseCrearConDatoContratacion) {
        this.pseCrearConDatoContratacion = pseCrearConDatoContratacion;
    }

    public Boolean getPsePuedeAplicarPlaza() {
        return psePuedeAplicarPlaza;
    }

    public void setPsePuedeAplicarPlaza(Boolean psePuedeAplicarPlaza) {
        this.psePuedeAplicarPlaza = psePuedeAplicarPlaza;
    }

    public List<SgHorarioEscolarLitePersonal> getPseHorarioEscolarList() {
        return pseHorarioEscolarList;
    }

    public void setPseHorarioEscolarList(List<SgHorarioEscolarLitePersonal> pseHorarioEscolarList) {
        this.pseHorarioEscolarList = pseHorarioEscolarList;
    }

    public List<SgComponenteDocenteLitePersonal> getPseComponenteDocenteList() {
        return pseComponenteDocenteList;
    }

    public void setPseComponenteDocenteList(List<SgComponenteDocenteLitePersonal> pseComponenteDocenteList) {
        this.pseComponenteDocenteList = pseComponenteDocenteList;
    }

    
    
   

    @Override
    public String securityAmbitCreate() {
        return null; //"pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedPk"; //TODO: el centro educativo se agrega en una pestaña luego de guardado. Ver.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "psePersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedPk", o.getRegla());
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "psePk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (psePk != null ? psePk.hashCode() : 0);
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
        final SgPersonalSedeEducativa other = (SgPersonalSedeEducativa) obj;
        if (!Objects.equals(this.psePk, other.psePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativa[ psePk=" + psePk + " ]";
    }

}
