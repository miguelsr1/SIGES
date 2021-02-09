/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "perPk", scope = SgPersonaLite.class)

public class SgPersonaLite implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long perPk;

    private String perPrimerNombre;

    private String perSegundoNombre;

    private String perTercerNombre;

    private String perPrimerApellido;

    private String perSegundoApellido;

    private String perTercerApellido;

    private String perPrimerNombreBusqueda;
  
    private SgSexo perSexo;
    
    private String perSegundoNombreBusqueda;

    private String perTercerNombreBusqueda;

    private String perPrimerApellidoBusqueda;

    private String perSegundoApellidoBusqueda;

    private String perTercerApellidoBusqueda;

    private String perNombreBusqueda;

    private LocalDate perFechaNacimiento;

    private String perEmail;

    private Boolean perHabilitado;

    private LocalDateTime perUltModFecha;

    private String perUltModUsuario;

    private Integer perVersion;

    private String perDui;

    private Long perCun;

    private Long perNie;

    private String perNip;

    private String perNit;

    private String perInpep;

    private String perIsss;

    private String perNup;

    private Boolean perNaturalizada;

    private Long perPartidaNacimiento;

    private Integer perPartidaNacimientoAnio;

    private String perPartidaNacimientoFolio;

    private String perPartidaNacimientoLibro;

    private String perPartidaNacimientoComplemento;

    private Boolean perPartidaNacimientoPresenta;

    private Boolean perTieneIdentificacion;
    
    private Boolean perLuceneIndexUpdated;

    private Long perUsuarioPk;

    public SgPersonaLite() {
    }

    public SgPersonaLite(Long perPk) {
        this.perPk = perPk;
    }

    public SgPersonaLite(Long perPk, Integer perVersion) {
        this.perPk = perPk;
        this.perVersion = perVersion;
    }
    
    @JsonIgnore
    public String getPerDuiNombreCompleto(){
        StringBuilder s = new StringBuilder();
        if (this.perDui != null) {
            s.append(this.perDui).append(" - ");
        }
        s.append(this.getPerNombreCompleto());
        return s.toString();
    }


    @JsonIgnore
    public String getPerNombreCompleto() {
        StringBuilder s = new StringBuilder();
        if (this.perPrimerNombre != null) {
            s.append(this.perPrimerNombre).append(" ");
        }
        if (this.perSegundoNombre != null) {
            s.append(this.perSegundoNombre).append(" ");
        }
        if (this.perTercerNombre != null) {
            s.append(this.perTercerNombre).append(" ");
        }
        if (this.perPrimerApellido != null) {
            s.append(this.perPrimerApellido).append(" ");
        }
        if (this.perSegundoApellido != null) {
            s.append(this.perSegundoApellido).append(" ");
        }
        if (this.perTercerApellido != null) {
            s.append(this.perTercerApellido).append(" ");
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    public Long getPerPk() {
        return perPk;
    }

    public void setPerPk(Long perPk) {
        this.perPk = perPk;
    }

    public String getPerPrimerNombre() {
        return perPrimerNombre;
    }

    public void setPerPrimerNombre(String perPrimerNombre) {
        this.perPrimerNombre = perPrimerNombre;
    }

    public String getPerSegundoNombre() {
        return perSegundoNombre;
    }

    public void setPerSegundoNombre(String perSegundoNombre) {
        this.perSegundoNombre = perSegundoNombre;
    }

    public String getPerTercerNombre() {
        return perTercerNombre;
    }

    public void setPerTercerNombre(String perTercerNombre) {
        this.perTercerNombre = perTercerNombre;
    }

    public String getPerPrimerApellido() {
        return perPrimerApellido;
    }

    public void setPerPrimerApellido(String perPrimerApellido) {
        this.perPrimerApellido = perPrimerApellido;
    }

    public String getPerSegundoApellido() {
        return perSegundoApellido;
    }

    public void setPerSegundoApellido(String perSegundoApellido) {
        this.perSegundoApellido = perSegundoApellido;
    }

    public String getPerTercerApellido() {
        return perTercerApellido;
    }

    public void setPerTercerApellido(String perTercerApellido) {
        this.perTercerApellido = perTercerApellido;
    }

    public LocalDate getPerFechaNacimiento() {
        return perFechaNacimiento;
    }

    public void setPerFechaNacimiento(LocalDate perFechaNacimiento) {
        this.perFechaNacimiento = perFechaNacimiento;
    }

    public String getPerEmail() {
        return perEmail;
    }

    public void setPerEmail(String perEmail) {
        this.perEmail = perEmail;
    }

    public Boolean getPerHabilitado() {
        return perHabilitado;
    }

    public void setPerHabilitado(Boolean perHabilitado) {
        this.perHabilitado = perHabilitado;
    }

    public LocalDateTime getPerUltModFecha() {
        return perUltModFecha;
    }

    public void setPerUltModFecha(LocalDateTime perUltModFecha) {
        this.perUltModFecha = perUltModFecha;
    }

    public String getPerUltModUsuario() {
        return perUltModUsuario;
    }

    public void setPerUltModUsuario(String perUltModUsuario) {
        this.perUltModUsuario = perUltModUsuario;
    }

    public Integer getPerVersion() {
        return perVersion;
    }

    public void setPerVersion(Integer perVersion) {
        this.perVersion = perVersion;
    }
    
    public String getPerPrimerNombreBusqueda() {
        return perPrimerNombreBusqueda;
    }

    public void setPerPrimerNombreBusqueda(String perPrimerNombreBusqueda) {
        this.perPrimerNombreBusqueda = perPrimerNombreBusqueda;
    }

    public String getPerSegundoNombreBusqueda() {
        return perSegundoNombreBusqueda;
    }

    public void setPerSegundoNombreBusqueda(String perSegundoNombreBusqueda) {
        this.perSegundoNombreBusqueda = perSegundoNombreBusqueda;
    }

    public String getPerTercerNombreBusqueda() {
        return perTercerNombreBusqueda;
    }

    public void setPerTercerNombreBusqueda(String perTercerNombreBusqueda) {
        this.perTercerNombreBusqueda = perTercerNombreBusqueda;
    }

    public String getPerPrimerApellidoBusqueda() {
        return perPrimerApellidoBusqueda;
    }

    public void setPerPrimerApellidoBusqueda(String perPrimerApellidoBusqueda) {
        this.perPrimerApellidoBusqueda = perPrimerApellidoBusqueda;
    }

    public String getPerSegundoApellidoBusqueda() {
        return perSegundoApellidoBusqueda;
    }

    public void setPerSegundoApellidoBusqueda(String perSegundoApellidoBusqueda) {
        this.perSegundoApellidoBusqueda = perSegundoApellidoBusqueda;
    }

    public String getPerTercerApellidoBusqueda() {
        return perTercerApellidoBusqueda;
    }

    public void setPerTercerApellidoBusqueda(String perTercerApellidoBusqueda) {
        this.perTercerApellidoBusqueda = perTercerApellidoBusqueda;
    }

    public String getPerNombreBusqueda() {
        return perNombreBusqueda;
    }

    public void setPerNombreBusqueda(String perNombreBusqueda) {
        this.perNombreBusqueda = perNombreBusqueda;
    }

    public String getPerDui() {
        return perDui;
    }

    public void setPerDui(String perDui) {
        this.perDui = perDui;
    }

    public Long getPerCun() {
        return perCun;
    }

    public void setPerCun(Long perCun) {
        this.perCun = perCun;
    }

    public Long getPerNie() {
        return perNie;
    }

    public void setPerNie(Long perNie) {
        this.perNie = perNie;
    }

    public String getPerNip() {
        return perNip;
    }

    public void setPerNip(String perNip) {
        this.perNip = perNip;
    }

    public Long getPerPartidaNacimiento() {
        return perPartidaNacimiento;
    }

    public void setPerPartidaNacimiento(Long perPartidaNacimiento) {
        this.perPartidaNacimiento = perPartidaNacimiento;
    }

    public String getPerPartidaNacimientoFolio() {
        return perPartidaNacimientoFolio;
    }

    public void setPerPartidaNacimientoFolio(String perPartidaNacimientoFolio) {
        this.perPartidaNacimientoFolio = perPartidaNacimientoFolio;
    }

    public String getPerPartidaNacimientoLibro() {
        return perPartidaNacimientoLibro;
    }

    public void setPerPartidaNacimientoLibro(String perPartidaNacimientoLibro) {
        this.perPartidaNacimientoLibro = perPartidaNacimientoLibro;
    }


    public Boolean getPerPartidaNacimientoPresenta() {
        return perPartidaNacimientoPresenta;
    }

    public void setPerPartidaNacimientoPresenta(Boolean perPartidaNacimientoPresenta) {
        this.perPartidaNacimientoPresenta = perPartidaNacimientoPresenta;
    }

    public String getPerNit() {
        return perNit;
    }

    public void setPerNit(String perNit) {
        this.perNit = perNit;
    }

    public Boolean getPerNaturalizada() {
        return perNaturalizada;
    }

    public void setPerNaturalizada(Boolean perNaturalizada) {
        this.perNaturalizada = perNaturalizada;
    }

    public String getPerInpep() {
        return perInpep;
    }

    public void setPerInpep(String perInpep) {
        this.perInpep = perInpep;
    }

    public String getPerIsss() {
        return perIsss;
    }

    public void setPerIsss(String perIsss) {
        this.perIsss = perIsss;
    }

    public String getPerNup() {
        return perNup;
    }

    public void setPerNup(String perNup) {
        this.perNup = perNup;
    }

   
    public String getPerPartidaNacimientoComplemento() {
        return perPartidaNacimientoComplemento;
    }

    public void setPerPartidaNacimientoComplemento(String perPartidaNacimientoComplemento) {
        this.perPartidaNacimientoComplemento = perPartidaNacimientoComplemento;
    }

    public Integer getPerPartidaNacimientoAnio() {
        return perPartidaNacimientoAnio;
    }

    public void setPerPartidaNacimientoAnio(Integer perPartidaNacimientoAnio) {
        this.perPartidaNacimientoAnio = perPartidaNacimientoAnio;
    }

    public Boolean getPerLuceneIndexUpdated() {
        return perLuceneIndexUpdated;
    }

    public void setPerLuceneIndexUpdated(Boolean perLuceneIndexUpdated) {
        this.perLuceneIndexUpdated = perLuceneIndexUpdated;
    }

    public Boolean getPerTieneIdentificacion() {
        return perTieneIdentificacion;
    }

    public void setPerTieneIdentificacion(Boolean perTieneIdentificacion) {
        this.perTieneIdentificacion = perTieneIdentificacion;
    }

    public Long getPerUsuarioPk() {
        return perUsuarioPk;
    }

    public void setPerUsuarioPk(Long perUsuarioPk) {
        this.perUsuarioPk = perUsuarioPk;
    }

    public SgSexo getPerSexo() {
        return perSexo;
    }

    public void setPerSexo(SgSexo perSexo) {
        this.perSexo = perSexo;
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
        final SgPersonaLite other = (SgPersonaLite) obj;
        if (!Objects.equals(this.perPk, other.perPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgPersonaLite{" + "perPk=" + perPk + '}';
    }
    
    
    

}
