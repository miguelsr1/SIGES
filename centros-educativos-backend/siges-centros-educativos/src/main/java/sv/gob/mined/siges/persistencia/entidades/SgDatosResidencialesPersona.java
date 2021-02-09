/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoServicioSanitario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCompaniaTelecomunicacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgElementoHogar;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFuenteAbastecimientoAgua;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMaterialPiso;

@Entity
@Table(name = "sg_datos_residenciales_personas", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "perPk", scope = SgDatosResidencialesPersona.class)
@Audited
public class SgDatosResidencialesPersona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="per_pk", unique=true, nullable=false)
    private Long perPk;
    
    @MapsId("perPk")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "per_pk", nullable = false)
    private SgPersona perPersona;

    @Column(name = "per_cantidad_dormitorios_casa")
    private Integer perCantidadDormitoriosCasa;

    @Column(name = "per_tiene_servicio_energia_electrica_residencial")
    private Boolean perTieneServicioEnergiaElectricaResidencial;

    @Column(name = "per_tiene_conexion_internet_residencial")
    private Boolean perTieneConexionInternetResidencial;

    @JoinColumn(name = "per_compania_internet_residencial")
    @ManyToOne
    private SgCompaniaTelecomunicacion perCompaniaInternetResidencial;

    @JoinColumn(name = "per_tipo_servicio_sanitario_residencial")
    @ManyToOne
    private SgTipoServicioSanitario perTipoServicioSanitarioResidencial;

    @Column(name = "per_tipo_servicio_sanitario_residencial_otro")
    private String perTipoServicioSanitarioResidencialOtro;

    @JoinColumn(name = "per_fuente_abastecimiento_agua_residencial")
    @ManyToOne
    private SgFuenteAbastecimientoAgua perFuenteAbastecimientoAguaResidencial;

    @Column(name = "per_fuente_abastecimiento_agua_residencial_otra")
    private String perFuenteAbastecimientoAguaResidencialOtra;

    @JoinColumn(name = "per_material_piso_residencial")
    @ManyToOne
    private SgMaterialPiso perMaterialPisoResidencial;

    @Column(name = "per_material_piso_residencial_otro")
    private String perMaterialPisoResidencialOtro;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_personas_elementos_hogar",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "per_pk"),
            inverseJoinColumns = @JoinColumn(name = "eho_pk"))
    private List<SgElementoHogar> perElementosHogar;

    @Column(name = "per_version")
    @Version
    private Integer perVersion;

    public SgDatosResidencialesPersona() {
        
    }

    public SgDatosResidencialesPersona(Long perPk) {
        this.perPk = perPk;
    }

    public SgDatosResidencialesPersona(Long perPk, Integer perVersion) {
        this.perPk = perPk;
        this.perVersion = perVersion;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        if (BooleanUtils.isFalse(perTieneConexionInternetResidencial)){
            this.perCompaniaInternetResidencial = null;
        }
    }


    public Long getPerPk() {
        return perPk;
    }

    public void setPerPk(Long perPk) {
        this.perPk = perPk;
    }

    public Integer getPerVersion() {
        return perVersion;
    }

    public void setPerVersion(Integer perVersion) {
        this.perVersion = perVersion;
    }

    public Integer getPerCantidadDormitoriosCasa() {
        return perCantidadDormitoriosCasa;
    }

    public void setPerCantidadDormitoriosCasa(Integer perCantidadDormitoriosCasa) {
        this.perCantidadDormitoriosCasa = perCantidadDormitoriosCasa;
    }

    public Boolean getPerTieneServicioEnergiaElectricaResidencial() {
        return perTieneServicioEnergiaElectricaResidencial;
    }

    public void setPerTieneServicioEnergiaElectricaResidencial(Boolean perTieneServicioEnergiaElectricaResidencial) {
        this.perTieneServicioEnergiaElectricaResidencial = perTieneServicioEnergiaElectricaResidencial;
    }

    public Boolean getPerTieneConexionInternetResidencial() {
        return perTieneConexionInternetResidencial;
    }

    public void setPerTieneConexionInternetResidencial(Boolean perTieneConexionInternetResidencial) {
        this.perTieneConexionInternetResidencial = perTieneConexionInternetResidencial;
    }

    public SgCompaniaTelecomunicacion getPerCompaniaInternetResidencial() {
        return perCompaniaInternetResidencial;
    }

    public void setPerCompaniaInternetResidencial(SgCompaniaTelecomunicacion perCompaniaInternetResidencial) {
        this.perCompaniaInternetResidencial = perCompaniaInternetResidencial;
    }

    public SgTipoServicioSanitario getPerTipoServicioSanitarioResidencial() {
        return perTipoServicioSanitarioResidencial;
    }

    public void setPerTipoServicioSanitarioResidencial(SgTipoServicioSanitario perTipoServicioSanitarioResidencial) {
        this.perTipoServicioSanitarioResidencial = perTipoServicioSanitarioResidencial;
    }

    public String getPerTipoServicioSanitarioResidencialOtro() {
        return perTipoServicioSanitarioResidencialOtro;
    }

    public void setPerTipoServicioSanitarioResidencialOtro(String perTipoServicioSanitarioResidencialOtro) {
        this.perTipoServicioSanitarioResidencialOtro = perTipoServicioSanitarioResidencialOtro;
    }

    public SgFuenteAbastecimientoAgua getPerFuenteAbastecimientoAguaResidencial() {
        return perFuenteAbastecimientoAguaResidencial;
    }

    public void setPerFuenteAbastecimientoAguaResidencial(SgFuenteAbastecimientoAgua perFuenteAbastecimientoAguaResidencial) {
        this.perFuenteAbastecimientoAguaResidencial = perFuenteAbastecimientoAguaResidencial;
    }

    public String getPerFuenteAbastecimientoAguaResidencialOtra() {
        return perFuenteAbastecimientoAguaResidencialOtra;
    }

    public void setPerFuenteAbastecimientoAguaResidencialOtra(String perFuenteAbastecimientoAguaResidencialOtra) {
        this.perFuenteAbastecimientoAguaResidencialOtra = perFuenteAbastecimientoAguaResidencialOtra;
    }

    public SgMaterialPiso getPerMaterialPisoResidencial() {
        return perMaterialPisoResidencial;
    }

    public void setPerMaterialPisoResidencial(SgMaterialPiso perMaterialPisoResidencial) {
        this.perMaterialPisoResidencial = perMaterialPisoResidencial;
    }

    public String getPerMaterialPisoResidencialOtro() {
        return perMaterialPisoResidencialOtro;
    }

    public void setPerMaterialPisoResidencialOtro(String perMaterialPisoResidencialOtro) {
        this.perMaterialPisoResidencialOtro = perMaterialPisoResidencialOtro;
    }

    public List<SgElementoHogar> getPerElementosHogar() {
        return perElementosHogar;
    }

    public void setPerElementosHogar(List<SgElementoHogar> perElementosHogar) {
        this.perElementosHogar = perElementosHogar;
    }

    public SgPersona getPerPersona() {
        return perPersona;
    }

    public void setPerPersona(SgPersona perPersona) {
        this.perPersona = perPersona;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perPk != null ? perPk.hashCode() : 0);
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
        final SgDatosResidencialesPersona other = (SgDatosResidencialesPersona) obj;
        if (!Objects.equals(this.perPk, other.perPk)) {
            return false;
        }
        return true;
    }


}
