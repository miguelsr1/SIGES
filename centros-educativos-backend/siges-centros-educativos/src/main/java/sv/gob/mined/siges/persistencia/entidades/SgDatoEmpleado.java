/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
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
import java.util.List;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.enumerados.EnumEstadoDatoEmpleado;
import sv.gob.mined.siges.enumerados.EnumTipoDatoEmpleadoGuardar;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfp;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCategoriaEmpleado;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgNivelEmpleado;

@Entity
@Table(name = "sg_datos_empleado", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "demPk", scope = SgDatoEmpleado.class)
@Audited
public class SgDatoEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dem_pk")
    private Long demPk;
    
    @OneToOne(mappedBy = "pseDatoEmpleado")
    private SgPersonalSedeEducativa demPersonalSede;
    
    @Size(max = 45)
    @Column(name = "dem_codigo_empleado", length = 45)
    private String demCodigoEmpleado;
    
    @JoinColumn(name = "dem_afp_fk")
    @ManyToOne
    private SgAfp demAfp;
    
    @Column(name = "dem_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoDatoEmpleado demEstado;
    
    @Column(name = "dem_fecha_ingreso_gob")
    private LocalDate demFechaIngresoGob;
    
    @Column(name = "dem_fecha_ingreso_mined")
    private LocalDate demFechaIngresoMined;
    
    @JoinColumn(name = "dem_nivel_fk", referencedColumnName = "nem_pk")
    @ManyToOne
    private SgNivelEmpleado demNivelFk;
    
    @JoinColumn(name = "dem_categoria_fk", referencedColumnName = "cem_pk")
    @ManyToOne
    private SgCategoriaEmpleado demCategoriaFk;
    
    @Column(name = "dem_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime demUltModFecha;
    
    @Size(max = 45)
    @Column(name = "dem_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String demUltModUsuario;
    
    @Column(name = "dem_version")
    @Version
    private Integer demVersion;
    
    @OneToMany(mappedBy = "dcoDatoEmpleado")
    @NotAudited
    private List<SgDatoContratacion> demDatoContratacion;
    
    @OneToMany(mappedBy = "elaDatoEmpleado")
    @NotAudited
    private List<SgExperienciaLaboral> demExperienciaLaboral;
    
    @Transient
    private EnumTipoDatoEmpleadoGuardar demTipoDatoGuardar;
    
    @Column(name = "dem_empleado_mineducyt")
    private Boolean demEmpleadoMineducyt;    

    @Column(name = "dem_fecha_registro")
    private LocalDate demFechaRegistro;
    
    @Column(name = "dem_puede_aplicar_plaza")
    private Boolean demPuedeAplicarPlaza;
   
    public SgDatoEmpleado() {
    }

    public SgDatoEmpleado(Long demPk) {
        this.demPk = demPk;
    }

    public Long getDemPk() {
        return demPk;
    }

    public void setDemPk(Long demPk) {
        this.demPk = demPk;
    }

    public String getDemCodigoEmpleado() {
        return demCodigoEmpleado;
    }

    public void setDemCodigoEmpleado(String demCodigoEmpleado) {
        this.demCodigoEmpleado = demCodigoEmpleado;
    }

    public EnumEstadoDatoEmpleado getDemEstado() {
        return demEstado;
    }

    public void setDemEstado(EnumEstadoDatoEmpleado demEstado) {
        this.demEstado = demEstado;
    }


    public LocalDate getDemFechaIngresoGob() {
        return demFechaIngresoGob;
    }

    public void setDemFechaIngresoGob(LocalDate demFechaIngresoGob) {
        this.demFechaIngresoGob = demFechaIngresoGob;
    }

    public LocalDate getDemFechaIngresoMined() {
        return demFechaIngresoMined;
    }

    public void setDemFechaIngresoMined(LocalDate demFechaIngresoMined) {
        this.demFechaIngresoMined = demFechaIngresoMined;
    }

    public SgNivelEmpleado getDemNivelFk() {
        return demNivelFk;
    }

    public void setDemNivelFk(SgNivelEmpleado demNivelFk) {
        this.demNivelFk = demNivelFk;
    }

    public SgCategoriaEmpleado getDemCategoriaFk() {
        return demCategoriaFk;
    }

    public void setDemCategoriaFk(SgCategoriaEmpleado demCategoriaFk) {
        this.demCategoriaFk = demCategoriaFk;
    }

    public LocalDateTime getDemUltModFecha() {
        return demUltModFecha;
    }

    public void setDemUltModFecha(LocalDateTime demUltModFecha) {
        this.demUltModFecha = demUltModFecha;
    }

    public String getDemUltModUsuario() {
        return demUltModUsuario;
    }

    public void setDemUltModUsuario(String demUltModUsuario) {
        this.demUltModUsuario = demUltModUsuario;
    }

    public Integer getDemVersion() {
        return demVersion;
    }

    public void setDemVersion(Integer demVersion) {
        this.demVersion = demVersion;
    }

    public List<SgDatoContratacion> getDemDatoContratacion() {
        return demDatoContratacion;
    }

    public void setDemDatoContratacion(List<SgDatoContratacion> demDatoContratacion) {
        this.demDatoContratacion = demDatoContratacion;
    }

    public List<SgExperienciaLaboral> getDemExperienciaLaboral() {
        return demExperienciaLaboral;
    }

    public void setDemExperienciaLaboral(List<SgExperienciaLaboral> demExperienciaLaboral) {
        this.demExperienciaLaboral = demExperienciaLaboral;
    }

    public SgAfp getDemAfp() {
        return demAfp;
    }

    public void setDemAfp(SgAfp demAfp) {
        this.demAfp = demAfp;
    }

    public SgPersonalSedeEducativa getDemPersonalSede() {
        return demPersonalSede;
    }

    public void setDemPersonalSede(SgPersonalSedeEducativa demPersonalSede) {
        this.demPersonalSede = demPersonalSede;
    }

    public EnumTipoDatoEmpleadoGuardar getDemTipoDatoGuardar() {
        return demTipoDatoGuardar;
    }

    public void setDemTipoDatoGuardar(EnumTipoDatoEmpleadoGuardar demTipoDatoGuardar) {
        this.demTipoDatoGuardar = demTipoDatoGuardar;
    }

    public Boolean getDemEmpleadoMineducyt() {
        return demEmpleadoMineducyt;
    }

    public void setDemEmpleadoMineducyt(Boolean demEmpleadoMineducyt) {
        this.demEmpleadoMineducyt = demEmpleadoMineducyt;
    }

    public LocalDate getDemFechaRegistro() {
        return demFechaRegistro;
    }

    public void setDemFechaRegistro(LocalDate demFechaRegistro) {
        this.demFechaRegistro = demFechaRegistro;
    }

    public Boolean getDemPuedeAplicarPlaza() {
        return demPuedeAplicarPlaza;
    }

    public void setDemPuedeAplicarPlaza(Boolean demPuedeAplicarPlaza) {
        this.demPuedeAplicarPlaza = demPuedeAplicarPlaza;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (demPk != null ? demPk.hashCode() : 0);
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
        final SgDatoEmpleado other = (SgDatoEmpleado) obj;
        if (!Objects.equals(this.demPk, other.demPk)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDatoEmpleado[ demPk=" + demPk + " ]";
    }
    
}
