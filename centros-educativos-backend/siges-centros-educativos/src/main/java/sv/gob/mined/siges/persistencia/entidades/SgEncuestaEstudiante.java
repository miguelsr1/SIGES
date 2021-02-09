/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoServicioSanitario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCompaniaTelecomunicacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgElementoHogar;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFuenteAbastecimientoAgua;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMaterialPiso;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgZona;

@Entity
@Table(name = "sg_encuestas_estudiantes", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "encPk", scope = SgEncuestaEstudiante.class)
@Audited
public class SgEncuestaEstudiante implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "enc_pk", nullable = false)
    private Long encPk;

    @Column(name = "enc_cantidad_dormitorios_casa")
    private Integer encCantidadDormitoriosCasa;

    @Column(name = "enc_tiene_servicio_energia_electrica_residencial")
    private Boolean encTieneServicioEnergiaElectricaResidencial;

    @Column(name = "enc_tiene_conexion_internet_residencial")
    private Boolean encTieneConexionInternetResidencial;

    @JoinColumn(name = "enc_compania_internet_residencial")
    @ManyToOne
    private SgCompaniaTelecomunicacion encCompaniaInternetResidencial;

    @JoinColumn(name = "enc_tipo_servicio_sanitario_residencial")
    @ManyToOne
    private SgTipoServicioSanitario encTipoServicioSanitarioResidencial;

    @Column(name = "enc_tipo_servicio_sanitario_residencial_otro")
    private String encTipoServicioSanitarioResidencialOtro;

    @JoinColumn(name = "enc_fuente_abastecimiento_agua_residencial")
    @ManyToOne
    private SgFuenteAbastecimientoAgua encFuenteAbastecimientoAguaResidencial;

    @Column(name = "enc_fuente_abastecimiento_agua_residencial_otra")
    private String encFuenteAbastecimientoAguaResidencialOtra;

    @JoinColumn(name = "enc_material_piso_residencial")
    @ManyToOne
    private SgMaterialPiso encMaterialPisoResidencial;

    @Column(name = "enc_material_piso_residencial_otro")
    private String encMaterialPisoResidencialOtro;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_encuestas_elementos_hogar",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "enc_pk"),
            inverseJoinColumns = @JoinColumn(name = "eho_pk"))
    private List<SgElementoHogar> encElementosHogar;
    
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "menEncuestaEstudiante", orphanRemoval = true)
    private List<SgMenorEncuestaEstudiante> encMenores;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enc_estudiante_fk", nullable = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgEstudiante encEstudiante;
    
    @Column(name = "enc_estudiante_fk", nullable = false, insertable = false, updatable = false)
    private Long encEstudianteFk;

    @Column(name = "enc_fecha_ingreso")
    private LocalDateTime encFechaIngreso;

    @Column(name = "enc_vive_con_cantidad")
    private Integer encViveConCantidad;
    
    @Column(name = "enc_vive_con_personas_menores")
    private Boolean encViveConPersonasMenores;

    @JoinColumn(name = "enc_zona_residencia")
    @ManyToOne
    private SgZona encZonaResidencia;

    @Column(name = "enc_estudiante_dis_km_centro")
    private Double encEstudianteDisKmCentro;

    @Column(name = "enc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime encUltModFecha;

    @Size(max = 45)
    @Column(name = "enc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String encUltModUsuario;

    @Column(name = "enc_version")
    @Version
    private Integer encVersion;
    
    @Column(name = "enc_sintoniza_canal_10")
    private Boolean encSintonizaCanal10;
    
    @Column(name = "enc_sintoniza_franja_educativa")
    private Boolean encSintonizaFranjaEducativa;

    public SgEncuestaEstudiante() {

    }

    public SgEncuestaEstudiante(Long encPk) {
        this.encPk = encPk;
    }

    public SgEncuestaEstudiante(Long encPk, Integer encVersion) {
        this.encPk = encPk;
        this.encVersion = encVersion;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        if (BooleanUtils.isFalse(encTieneConexionInternetResidencial)) {
            this.encCompaniaInternetResidencial = null;
        }
    }
    
        
    @JsonIgnore
    public String getEncElementosHogarString(){
        if (encElementosHogar != null){
            return encElementosHogar.stream().map(p -> p.getEhoNombre()).collect(Collectors.joining(", "));
        }
        return null;
    }

    public Long getEncPk() {
        return encPk;
    }

    public void setEncPk(Long encPk) {
        this.encPk = encPk;
    }

    public Integer getEncCantidadDormitoriosCasa() {
        return encCantidadDormitoriosCasa;
    }

    public void setEncCantidadDormitoriosCasa(Integer encCantidadDormitoriosCasa) {
        this.encCantidadDormitoriosCasa = encCantidadDormitoriosCasa;
    }

    public Boolean getEncTieneServicioEnergiaElectricaResidencial() {
        return encTieneServicioEnergiaElectricaResidencial;
    }

    public void setEncTieneServicioEnergiaElectricaResidencial(Boolean encTieneServicioEnergiaElectricaResidencial) {
        this.encTieneServicioEnergiaElectricaResidencial = encTieneServicioEnergiaElectricaResidencial;
    }

    public Boolean getEncTieneConexionInternetResidencial() {
        return encTieneConexionInternetResidencial;
    }

    public void setEncTieneConexionInternetResidencial(Boolean encTieneConexionInternetResidencial) {
        this.encTieneConexionInternetResidencial = encTieneConexionInternetResidencial;
    }

    public SgCompaniaTelecomunicacion getEncCompaniaInternetResidencial() {
        return encCompaniaInternetResidencial;
    }

    public void setEncCompaniaInternetResidencial(SgCompaniaTelecomunicacion encCompaniaInternetResidencial) {
        this.encCompaniaInternetResidencial = encCompaniaInternetResidencial;
    }

    public SgTipoServicioSanitario getEncTipoServicioSanitarioResidencial() {
        return encTipoServicioSanitarioResidencial;
    }

    public void setEncTipoServicioSanitarioResidencial(SgTipoServicioSanitario encTipoServicioSanitarioResidencial) {
        this.encTipoServicioSanitarioResidencial = encTipoServicioSanitarioResidencial;
    }

    public String getEncTipoServicioSanitarioResidencialOtro() {
        return encTipoServicioSanitarioResidencialOtro;
    }

    public void setEncTipoServicioSanitarioResidencialOtro(String encTipoServicioSanitarioResidencialOtro) {
        this.encTipoServicioSanitarioResidencialOtro = encTipoServicioSanitarioResidencialOtro;
    }

    public SgFuenteAbastecimientoAgua getEncFuenteAbastecimientoAguaResidencial() {
        return encFuenteAbastecimientoAguaResidencial;
    }

    public void setEncFuenteAbastecimientoAguaResidencial(SgFuenteAbastecimientoAgua encFuenteAbastecimientoAguaResidencial) {
        this.encFuenteAbastecimientoAguaResidencial = encFuenteAbastecimientoAguaResidencial;
    }

    public String getEncFuenteAbastecimientoAguaResidencialOtra() {
        return encFuenteAbastecimientoAguaResidencialOtra;
    }

    public void setEncFuenteAbastecimientoAguaResidencialOtra(String encFuenteAbastecimientoAguaResidencialOtra) {
        this.encFuenteAbastecimientoAguaResidencialOtra = encFuenteAbastecimientoAguaResidencialOtra;
    }

    public SgMaterialPiso getEncMaterialPisoResidencial() {
        return encMaterialPisoResidencial;
    }

    public void setEncMaterialPisoResidencial(SgMaterialPiso encMaterialPisoResidencial) {
        this.encMaterialPisoResidencial = encMaterialPisoResidencial;
    }

    public String getEncMaterialPisoResidencialOtro() {
        return encMaterialPisoResidencialOtro;
    }

    public void setEncMaterialPisoResidencialOtro(String encMaterialPisoResidencialOtro) {
        this.encMaterialPisoResidencialOtro = encMaterialPisoResidencialOtro;
    }

    public List<SgElementoHogar> getEncElementosHogar() {
        return encElementosHogar;
    }

    public void setEncElementosHogar(List<SgElementoHogar> encElementosHogar) {
        this.encElementosHogar = encElementosHogar;
    }

    public SgEstudiante getEncEstudiante() {
        return encEstudiante;
    }

    public void setEncEstudiante(SgEstudiante encEstudiante) {
        this.encEstudiante = encEstudiante;
    }

    public LocalDateTime getEncFechaIngreso() {
        return encFechaIngreso;
    }

    public void setEncFechaIngreso(LocalDateTime encFechaIngreso) {
        this.encFechaIngreso = encFechaIngreso;
    }

    public Integer getEncViveConCantidad() {
        return encViveConCantidad;
    }

    public void setEncViveConCantidad(Integer encViveConCantidad) {
        this.encViveConCantidad = encViveConCantidad;
    }

    public SgZona getEncZonaResidencia() {
        return encZonaResidencia;
    }

    public void setEncZonaResidencia(SgZona encZonaResidencia) {
        this.encZonaResidencia = encZonaResidencia;
    }


    public Double getEncEstudianteDisKmCentro() {
        return encEstudianteDisKmCentro;
    }

    public void setEncEstudianteDisKmCentro(Double encEstudianteDisKmCentro) {
        this.encEstudianteDisKmCentro = encEstudianteDisKmCentro;
    }

    public Integer getEncVersion() {
        return encVersion;
    }

    public void setEncVersion(Integer encVersion) {
        this.encVersion = encVersion;
    }

    public LocalDateTime getEncUltModFecha() {
        return encUltModFecha;
    }

    public void setEncUltModFecha(LocalDateTime encUltModFecha) {
        this.encUltModFecha = encUltModFecha;
    }

    public String getEncUltModUsuario() {
        return encUltModUsuario;
    }

    public void setEncUltModUsuario(String encUltModUsuario) {
        this.encUltModUsuario = encUltModUsuario;
    }

    public Long getEncEstudianteFk() {
        return encEstudianteFk;
    }

    public void setEncEstudianteFk(Long encEstudianteFk) {
        this.encEstudianteFk = encEstudianteFk;
    }

    public List<SgMenorEncuestaEstudiante> getEncMenores() {
        return encMenores;
    }

    public void setEncMenores(List<SgMenorEncuestaEstudiante> encMenores) {
        this.encMenores = encMenores;
    }

    public Boolean getEncViveConPersonasMenores() {
        return encViveConPersonasMenores;
    }

    public void setEncViveConPersonasMenores(Boolean encViveConPersonasMenores) {
        this.encViveConPersonasMenores = encViveConPersonasMenores;
    }

    public Boolean getEncSintonizaCanal10() {
        return encSintonizaCanal10;
    }

    public void setEncSintonizaCanal10(Boolean encSintonizaCanal10) {
        this.encSintonizaCanal10 = encSintonizaCanal10;
    }

    public Boolean getEncSintonizaFranjaEducativa() {
        return encSintonizaFranjaEducativa;
    }

    public void setEncSintonizaFranjaEducativa(Boolean encSintonizaFranjaEducativa) {
        this.encSintonizaFranjaEducativa = encSintonizaFranjaEducativa;
    }
    
    
    
    @Override
    public String securityAmbitCreate() {
        return "encEstudiante";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "encEstudiante.estUltimaMatricula.matSeccion.secAsociacion.asoPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "encEstudiante.estUltimaSeccionPk", o.getContext());
            //No hacer join con otra tabla. Al hacer joins si la persona tiene muchos ambitos, se forma query con ORs degradando la performance
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "encEstudiante.estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "encPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (encPk != null ? encPk.hashCode() : 0);
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
        final SgEncuestaEstudiante other = (SgEncuestaEstudiante) obj;
        if (!Objects.equals(this.encPk, other.encPk)) {
            return false;
        }
        return true;
    }

}
