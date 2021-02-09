package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.List;


public class SgDatoGeneralCentroEducativo implements Serializable {
    
    
    private String centroEducativoCodigo;
    private String centroEducativoNombre;
    private String departamentoNombre;
    private String municipioNombre;
    private String cantonNombre;
    private String zonaNombre;
    private String direccion;
    private String latitud;
    private String longitud;
    private String telefonos;
    private String nombreDirector;
    private List<String> nivelesEducativos;
    private List<String> jornadas;
    private Boolean opcionBTV;
    private String sector;
    private String sistemaIntegrado;

    public String getCentroEducativoCodigo() {
        return centroEducativoCodigo;
    }

    public void setCentroEducativoCodigo(String centroEducativoCodigo) {
        this.centroEducativoCodigo = centroEducativoCodigo;
    }

    public String getCentroEducativoNombre() {
        return centroEducativoNombre;
    }

    public void setCentroEducativoNombre(String centroEducativoNombre) {
        this.centroEducativoNombre = centroEducativoNombre;
    }

    public String getDepartamentoNombre() {
        return departamentoNombre;
    }

    public void setDepartamentoNombre(String departamentoNombre) {
        this.departamentoNombre = departamentoNombre;
    }

    public String getMunicipioNombre() {
        return municipioNombre;
    }

    public void setMunicipioNombre(String municipioNombre) {
        this.municipioNombre = municipioNombre;
    }

    public String getCantonNombre() {
        return cantonNombre;
    }

    public void setCantonNombre(String cantonNombre) {
        this.cantonNombre = cantonNombre;
    }

    public String getZonaNombre() {
        return zonaNombre;
    }

    public void setZonaNombre(String zonaNombre) {
        this.zonaNombre = zonaNombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getNombreDirector() {
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }

    public List<String> getNivelesEducativos() {
        return nivelesEducativos;
    }

    public void setNivelesEducativos(List<String> nivelesEducativos) {
        this.nivelesEducativos = nivelesEducativos;
    }

    public List<String> getJornadas() {
        return jornadas;
    }

    public void setJornadas(List<String> jornadas) {
        this.jornadas = jornadas;
    }

    public Boolean getOpcionBTV() {
        return opcionBTV;
    }

    public void setOpcionBTV(Boolean opcionBTV) {
        this.opcionBTV = opcionBTV;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSistemaIntegrado() {
        return sistemaIntegrado;
    }

    public void setSistemaIntegrado(String sistemaIntegrado) {
        this.sistemaIntegrado = sistemaIntegrado;
    }
    
    
    
    
}
