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
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.enumerados.EnumPromocionGradoMatricula;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoRetiro;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

//En caso de agregar un nuevo atributo, se debe agregar también en frontend en CambioMatriculaBean y CambioSeccionBean (filtroMatricula-> incluirCampos)
//Guardar en SolicitudTrasladoBean backend
@Entity
@Table(name = "sg_matriculas", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "matPk", scope = SgMatricula.class)
public class SgMatricula implements DataSecurity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mat_pk")
    private Long matPk;

    @JoinColumn(name = "mat_seccion_fk", referencedColumnName = "sec_pk")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSeccion matSeccion;

    @JoinColumn(name = "mat_estudiante_fk")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgEstudiante matEstudiante;

    @Column(name = "mat_estado")
    @Enumerated(EnumType.STRING)
    private EnumMatriculaEstado matEstado;

    @Column(name = "mat_fecha_hasta")
    private LocalDate matFechaHasta;

    @Column(name = "mat_fecha_registro")
    private LocalDateTime matFechaRegistro;

    @Column(name = "mat_fecha_ingreso")
    private LocalDate matFechaIngreso;

    @Size(max = 255)
    @Column(name = "mat_codigo_constancia", length = 255)
    private String matCodigoConstancia;

    @Size(max = 500)
    @Column(name = "mat_observaciones", length = 500)
    private String matObservaciones;

    @Size(max = 500)
    @Column(name = "mat_observacion_provisional", length = 500)
    private String matObservacioneProvisional;

    @Column(name = "mat_provisional")
    private Boolean matProvisional;

    @Column(name = "mat_anulada")
    private Boolean matAnulada;

    @JoinColumn(name = "mat_motivo_retiro_fk", referencedColumnName = "mre_pk")
    @ManyToOne
    private SgMotivoRetiro matMotivoRetiro;

    @Column(name = "mat_retirada")
    private Boolean matRetirada;

    @Column(name = "mat_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime matUltModFecha;

    @Size(max = 45)
    @Column(name = "mat_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String matUltModUsuario;

    @Size(max = 45)
    @Column(name = "mat_creacion_usuario", length = 45)
    private String matCreacionUsuario;

    @Column(name = "mat_version")
    @Version
    private Integer matVersion;

    @Size(max = 255)
    @Column(name = "mat_obs_anu_retiro", length = 255)
    private String matObsAnuRetiro;

    @Column(name = "mat_validacion_academica")
    private Boolean matValidacionAcademica;

    @Column(name = "mat_validacion_academica_fecha")
    private LocalDateTime matValidacionAcademicaFecha;

    @Size(max = 45)
    @Column(name = "mat_validacion_academica_usuario", length = 45)
    private String matValidacionAcademicaUsuario;

    @Column(name = "mat_promocion_grado")
    @Enumerated(EnumType.STRING)
    private EnumPromocionGradoMatricula matPromocionGrado;
    
    @Column(name = "mat_repetidor")
    private Boolean matRepetidor;
                 
    @Column(name = "mat_importado")
    private Boolean matImportado;
 
    @Column(name = "mat_cerrado_por_cierre_anio")
    private Boolean matCerradoPorCierreAnio;

    public SgMatricula() {
        this.matProvisional = Boolean.FALSE;
        this.matFechaIngreso = LocalDate.now();
        this.matRetirada = Boolean.FALSE;
        this.matValidacionAcademica = Boolean.FALSE;
        this.matAnulada = Boolean.FALSE;
        this.matEstado = EnumMatriculaEstado.ABIERTO;
        this.matImportado = Boolean.FALSE;
        this.matRepetidor = Boolean.FALSE;
    }

    @PrePersist
    public void prePersist() throws Exception {
        this.matFechaRegistro = LocalDateTime.now();
        this.matCreacionUsuario = Lookup.obtenerJWT().getSubject();
        this.matCodigoConstancia = RandomStringUtils.randomAlphanumeric(15);
    }

    public SgMatricula(Long matPk) {
        this.matPk = matPk;
    }

    public Long getMatPk() {
        return matPk;
    }

    public void setMatPk(Long matPk) {
        this.matPk = matPk;
    }

    public SgSeccion getMatSeccion() {
        return matSeccion;
    }

    public void setMatSeccion(SgSeccion matSeccion) {
        this.matSeccion = matSeccion;
    }

    public SgEstudiante getMatEstudiante() {
        return matEstudiante;
    }

    public void setMatEstudiante(SgEstudiante matEstudiante) {
        this.matEstudiante = matEstudiante;
    }

    public EnumMatriculaEstado getMatEstado() {
        return matEstado;
    }

    public void setMatEstado(EnumMatriculaEstado matEstado) {
        this.matEstado = matEstado;
    }

    public LocalDate getMatFechaHasta() {
        return matFechaHasta;
    }

    public void setMatFechaHasta(LocalDate matFechaHasta) {
        this.matFechaHasta = matFechaHasta;
    }

    public String getMatObservaciones() {
        return matObservaciones;
    }

    public void setMatObservaciones(String matObservaciones) {
        this.matObservaciones = matObservaciones;
    }

    public String getMatObservacioneProvisional() {
        return matObservacioneProvisional;
    }

    public void setMatObservacioneProvisional(String matObservacioneProvisional) {
        this.matObservacioneProvisional = matObservacioneProvisional;
    }

    public SgMotivoRetiro getMatMotivoRetiro() {
        return matMotivoRetiro;
    }

    public void setMatMotivoRetiro(SgMotivoRetiro matMotivoRetiro) {
        this.matMotivoRetiro = matMotivoRetiro;
    }

    public LocalDateTime getMatUltModFecha() {
        return matUltModFecha;
    }

    public void setMatUltModFecha(LocalDateTime matUltModFecha) {
        this.matUltModFecha = matUltModFecha;
    }

    public String getMatUltModUsuario() {
        return matUltModUsuario;
    }

    public void setMatUltModUsuario(String matUltModUsuario) {
        this.matUltModUsuario = matUltModUsuario;
    }

    public Integer getMatVersion() {
        return matVersion;
    }

    public void setMatVersion(Integer matVersion) {
        this.matVersion = matVersion;
    }

    public Boolean getMatProvisional() {
        return matProvisional;
    }

    public void setMatProvisional(Boolean matProvisional) {
        this.matProvisional = matProvisional;
    }

    public LocalDateTime getMatFechaRegistro() {
        return matFechaRegistro;
    }

    public void setMatFechaRegistro(LocalDateTime matFechaRegistro) {
        this.matFechaRegistro = matFechaRegistro;
    }

    public LocalDate getMatFechaIngreso() {
        return matFechaIngreso;
    }

    public void setMatFechaIngreso(LocalDate matFechaIngreso) {
        this.matFechaIngreso = matFechaIngreso;
    }

    public String getMatObsAnuRetiro() {
        return matObsAnuRetiro;
    }

    public void setMatObsAnuRetiro(String matObsAnuRetiro) {
        this.matObsAnuRetiro = matObsAnuRetiro;
    }

    public String getMatCodigoConstancia() {
        return matCodigoConstancia;
    }

    public void setMatCodigoConstancia(String matCodigoConstancia) {
        this.matCodigoConstancia = matCodigoConstancia;
    }

    public String getMatCreacionUsuario() {
        return matCreacionUsuario;
    }

    public void setMatCreacionUsuario(String matCreacionUsuario) {
        this.matCreacionUsuario = matCreacionUsuario;
    }

    public Boolean getMatValidacionAcademica() {
        return matValidacionAcademica;
    }

    public void setMatValidacionAcademica(Boolean matValidacionAcademica) {
        this.matValidacionAcademica = matValidacionAcademica;
    }

    public LocalDateTime getMatValidacionAcademicaFecha() {
        return matValidacionAcademicaFecha;
    }

    public void setMatValidacionAcademicaFecha(LocalDateTime matValidacionAcademicaFecha) {
        this.matValidacionAcademicaFecha = matValidacionAcademicaFecha;
    }

    public String getMatValidacionAcademicaUsuario() {
        return matValidacionAcademicaUsuario;
    }

    public void setMatValidacionAcademicaUsuario(String matValidacionAcademicaUsuario) {
        this.matValidacionAcademicaUsuario = matValidacionAcademicaUsuario;
    }

    public Boolean getMatRetirada() {
        return matRetirada;
    }

    public void setMatRetirada(Boolean matRetirada) {
        this.matRetirada = matRetirada;
    }

    public Boolean getMatAnulada() {
        return matAnulada;
    }

    public void setMatAnulada(Boolean matAnulada) {
        this.matAnulada = matAnulada;
    }

    public EnumPromocionGradoMatricula getMatPromocionGrado() {
        return matPromocionGrado;
    }

    public void setMatPromocionGrado(EnumPromocionGradoMatricula matPromocionGrado) {
        this.matPromocionGrado = matPromocionGrado;
    }

    public Boolean getMatRepetidor() {
        return matRepetidor;
    }

    public void setMatRepetidor(Boolean matRepetidor) {
        this.matRepetidor = matRepetidor;
    }
    

    public Boolean getMatImportado() {
        return matImportado;
    }

    public void setMatImportado(Boolean matImportado) {
        this.matImportado = matImportado;
    }

    public Boolean getMatCerradoPorCierreAnio() {
        return matCerradoPorCierreAnio;
    }

    public void setMatCerradoPorCierreAnio(Boolean matCerradoPorCierreAnio) {
        this.matCerradoPorCierreAnio = matCerradoPorCierreAnio;
    }
    
    @Override
    public String securityAmbitCreate() {
        return "matSeccion";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            //return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduSede.sedPk", o.getContext());
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matEstudiante.estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secAsociacion.asoPk", o.getContext());
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matPk != null ? matPk.hashCode() : 0);
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
        final SgMatricula other = (SgMatricula) obj;
        if (!Objects.equals(this.matPk, other.matPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMatricula[ matPk=" + matPk + " ]";
    }

}
