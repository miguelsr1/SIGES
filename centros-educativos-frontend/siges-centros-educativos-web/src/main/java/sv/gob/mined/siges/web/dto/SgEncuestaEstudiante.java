/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import sv.gob.mined.siges.web.dto.catalogo.SgCompaniaTelecomunicacion;
import sv.gob.mined.siges.web.dto.catalogo.SgElementoHogar;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteAbastecimientoAgua;
import sv.gob.mined.siges.web.dto.catalogo.SgMaterialPiso;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoServicioSanitario;
import sv.gob.mined.siges.web.dto.catalogo.SgZona;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "encPk", scope = SgEncuestaEstudiante.class)
public class SgEncuestaEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long encPk;
    
    private Integer encCantidadDormitoriosCasa;

    private Boolean encTieneServicioEnergiaElectricaResidencial;

    private Boolean encTieneConexionInternetResidencial;

    private SgCompaniaTelecomunicacion encCompaniaInternetResidencial;

    private SgTipoServicioSanitario encTipoServicioSanitarioResidencial;

    private String encTipoServicioSanitarioResidencialOtro;

    private SgFuenteAbastecimientoAgua encFuenteAbastecimientoAguaResidencial;

    private String encFuenteAbastecimientoAguaResidencialOtra;

    private SgMaterialPiso encMaterialPisoResidencial;

    private String encMaterialPisoResidencialOtro;

    private List<SgElementoHogar> encElementosHogar;
    
    private List<SgMenorEncuestaEstudiante> encMenores;
    
    private SgEstudiante encEstudiante;
    
    private Long encEstudianteFk;
    
    private LocalDateTime encFechaIngreso;
    
    private Integer encViveConCantidad;
    
    private Boolean encViveConPersonasMenores;
    
    private SgZona encZonaResidencia;
       
    private Double encEstudianteDisKmCentro;
    
    private LocalDateTime encUltModFecha;

    private String encUltModUsuario;

    private Integer encVersion;
    
    private Boolean encSintonizaCanal10;
     
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
