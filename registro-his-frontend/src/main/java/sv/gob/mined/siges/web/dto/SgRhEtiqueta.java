/*  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "etiPk", scope = SgRhEtiqueta.class) 
public class SgRhEtiqueta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long etiPk;

    private String etiApellido;
    
    private String etiNombre;

    private String etiDui;    

    private String etiNie;       
    
    private SgRhPagina etiPagina;
    
    private String etiNombreSede;
    
    private String etiFolio;

    private Boolean etiHabilitado;

    private LocalDateTime etiUltModFecha;

    private String etiUltModUsuario;

    private Integer etiVersion;
     
    private Integer etiCorrelativo;
     
    private LocalDate etiFecha;    
    
    private String etiFolioMined;
    
    private String etiLinkArchivo;    
    
    private Boolean etiPaginaCreada;


    public SgRhEtiqueta() {
        //etiPagina=new SgRhPagina();
    }

    public Long getEtiPk() {
        return etiPk;
    }

    public void setEtiPk(Long etiPk) {
        this.etiPk = etiPk;
    }

    public String getEtiApellido() {
        return etiApellido;
    }

    public void setEtiApellido(String etiApellido) {
        this.etiApellido = etiApellido;
    }

    public String getEtiNombre() {
        return etiNombre;
    }

    public void setEtiNombre(String etiNombre) {
        this.etiNombre = etiNombre;
    }

    public String getEtiDui() {
        return etiDui;
    }

    public void setEtiDui(String etiDui) {
        this.etiDui = etiDui;
    }

    public String getEtiNie() {
        return etiNie;
    }

    public void setEtiNie(String etiNie) {
        this.etiNie = etiNie;
    }

    public String getEtiNombreSede() {
        return etiNombreSede;
    }

    public void setEtiNombreSede(String etiNombreSede) {
        this.etiNombreSede = etiNombreSede;
    }

    public String getEtiFolio() {
        return etiFolio;
    }

    public void setEtiFolio(String etiFolio) {
        this.etiFolio = etiFolio;
    }   

    public Boolean getEtiHabilitado() {
        return etiHabilitado;
    }

    public void setEtiHabilitado(Boolean etiHabilitado) {
        this.etiHabilitado = etiHabilitado;
    }

    public LocalDateTime getEtiUltModFecha() {
        return etiUltModFecha;
    }

    public void setEtiUltModFecha(LocalDateTime etiUltModFecha) {
        this.etiUltModFecha = etiUltModFecha;
    }

    public String getEtiUltModUsuario() {
        return etiUltModUsuario;
    }

    public void setEtiUltModUsuario(String etiUltModUsuario) {
        this.etiUltModUsuario = etiUltModUsuario;
    }

    public Integer getEtiVersion() {
        return etiVersion;
    }

    public void setEtiVersion(Integer etiVersion) {
        this.etiVersion = etiVersion;
    }

    public Integer getEtiCorrelativo() {
        return etiCorrelativo;
    }

    public void setEtiCorrelativo(Integer etiCorrelativo) {
        this.etiCorrelativo = etiCorrelativo;
    }

    public LocalDate getEtiFecha() {
        return etiFecha;
    }

    public void setEtiFecha(LocalDate etiFecha) {
        this.etiFecha = etiFecha;
    }

    public String getEtiFolioMined() {
        return etiFolioMined;
    }

    public void setEtiFolioMined(String etiFolioMined) {
        this.etiFolioMined = etiFolioMined;
    }

    public String getEtiLinkArchivo() {
        return etiLinkArchivo;
    }

    public void setEtiLinkArchivo(String etiLinkArchivo) {
        this.etiLinkArchivo = etiLinkArchivo;
    }

    public Boolean getEtiPaginaCreada() {
        return etiPaginaCreada;
    }

    public void setEtiPaginaCreada(Boolean etiPaginaCreada) {
        this.etiPaginaCreada = etiPaginaCreada;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (etiPk != null ? etiPk.hashCode() : 0);
        return hash;
    }

    public SgRhPagina getEtiPagina() {
        return etiPagina;
    }

    public void setEtiPagina(SgRhPagina etiPagina) {
        this.etiPagina = etiPagina;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRhEtiqueta)) {
            return false;
        }
        SgRhEtiqueta other = (SgRhEtiqueta) object;
        if ((this.etiPk == null && other.etiPk != null) || (this.etiPk != null && !this.etiPk.equals(other.etiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRhEtiqueta{" + "etiPk=" + etiPk + '}';
    }
     
}
