/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.web.dto.catalogo.SgCompaniaTelecomunicacion;
import sv.gob.mined.siges.web.dto.catalogo.SgElementoHogar;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteAbastecimientoAgua;
import sv.gob.mined.siges.web.dto.catalogo.SgMaterialPiso;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoServicioSanitario;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "perPk", scope = SgDatosResidencialesPersona.class)
public class SgDatosResidencialesPersona implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long perPk;
    
    private SgPersona perPersona;

    private Integer perCantidadDormitoriosCasa;

    private Boolean perTieneServicioEnergiaElectricaResidencial;

    private Boolean perTieneConexionInternetResidencial;

    private SgCompaniaTelecomunicacion perCompaniaInternetResidencial;

    private SgTipoServicioSanitario perTipoServicioSanitarioResidencial;

    private String perTipoServicioSanitarioResidencialOtro;

    private SgFuenteAbastecimientoAgua perFuenteAbastecimientoAguaResidencial;

    private String perFuenteAbastecimientoAguaResidencialOtra;

    private SgMaterialPiso perMaterialPisoResidencial;

    private String perMaterialPisoResidencialOtro;

    private List<SgElementoHogar> perElementosHogar;

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
        
    }
    
    @JsonIgnore
    public String getElementosHogarString(){
        if (perElementosHogar != null){
            return perElementosHogar.stream().map(p -> p.getEhoNombre()).collect(Collectors.joining(", "));
        }
        return null;
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
