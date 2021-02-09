/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.enumerados.TipoSede;

public class SgRegistroSedeRequest implements Serializable {

    private TipoSede sedTipo;
    private String numTramite;
    private String sedNombre;
    private String sedCodigo;
    private String sedPropietario;
    private Long sedTipoCalendario;
    private Long sedTipoOrganismoAdministrativo;
    private List<Long> sedJornadasLaborales;
    private Integer sedAnioInicioActividades;
    private Boolean sedFinesDeLucro;
    private String sedCorreoElectronico;
    private List<List<String>> listadoServicios;

    private Long dirZona;
    private Long dirDepartamento;
    private Long dirMunicipio;
    private Long dirCanton;
    private String dirCaserio;
    private String dirDireccion;
    
    private String numAcuerdo;
    private String observacion; 
    private String numResolucion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaResolucion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaGeneracion;

//    listadoServicios = [["4","6","7","1","-1","-1","-1","1"],  ["4","6","8","1","-1","-1","-1","20"]]
//    0 - nivel
//    1 - ciclo
//    2 - modalidad educativa
//    3 - modalidad de atención
//    4 - submodalidad
//    5 - opción
//    6 - programa educativo
//    7 - grado

    public TipoSede getSedTipo() {
        return sedTipo;
    }

    public void setSedTipo(TipoSede sedTipo) {
        this.sedTipo = sedTipo;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getSedNombre() {
        return sedNombre;
    }

    public void setSedNombre(String sedNombre) {
        this.sedNombre = sedNombre;
    }

    public String getSedCodigo() {
        return sedCodigo;
    }

    public void setSedCodigo(String sedCodigo) {
        this.sedCodigo = sedCodigo;
    }

    public String getSedPropietario() {
        return sedPropietario;
    }

    public void setSedPropietario(String sedPropietario) {
        this.sedPropietario = sedPropietario;
    }

    public Long getSedTipoCalendario() {
        return sedTipoCalendario;
    }

    public void setSedTipoCalendario(Long sedTipoCalendario) {
        this.sedTipoCalendario = sedTipoCalendario;
    }

    public Long getSedTipoOrganismoAdministrativo() {
        return sedTipoOrganismoAdministrativo;
    }

    public void setSedTipoOrganismoAdministrativo(Long sedTipoOrganismoAdministrativo) {
        this.sedTipoOrganismoAdministrativo = sedTipoOrganismoAdministrativo;
    }

    public List<Long> getSedJornadasLaborales() {
        return sedJornadasLaborales;
    }

    public void setSedJornadasLaborales(List<Long> sedJornadasLaborales) {
        this.sedJornadasLaborales = sedJornadasLaborales;
    }

    public Integer getSedAnioInicioActividades() {
        return sedAnioInicioActividades;
    }

    public void setSedAnioInicioActividades(Integer sedAnioInicioActividades) {
        this.sedAnioInicioActividades = sedAnioInicioActividades;
    }

    public Boolean getSedFinesDeLucro() {
        return sedFinesDeLucro;
    }

    public void setSedFinesDeLucro(Boolean sedFinesDeLucro) {
        this.sedFinesDeLucro = sedFinesDeLucro;
    }

    public String getSedCorreoElectronico() {
        return sedCorreoElectronico;
    }

    public void setSedCorreoElectronico(String sedCorreoElectronico) {
        this.sedCorreoElectronico = sedCorreoElectronico;
    }

    public List<List<String>> getListadoServicios() {
        return listadoServicios;
    }

    public void setListadoServicios(List<List<String>> listadoServicios) {
        this.listadoServicios = listadoServicios;
    }

    public Long getDirZona() {
        return dirZona;
    }

    public void setDirZona(Long dirZona) {
        this.dirZona = dirZona;
    }

    public Long getDirDepartamento() {
        return dirDepartamento;
    }

    public void setDirDepartamento(Long dirDepartamento) {
        this.dirDepartamento = dirDepartamento;
    }

    public Long getDirMunicipio() {
        return dirMunicipio;
    }

    public void setDirMunicipio(Long dirMunicipio) {
        this.dirMunicipio = dirMunicipio;
    }

    public Long getDirCanton() {
        return dirCanton;
    }

    public void setDirCanton(Long dirCanton) {
        this.dirCanton = dirCanton;
    }

    public String getDirCaserio() {
        return dirCaserio;
    }

    public void setDirCaserio(String dirCaserio) {
        this.dirCaserio = dirCaserio;
    }

    public String getDirDireccion() {
        return dirDireccion;
    }

    public void setDirDireccion(String dirDireccion) {
        this.dirDireccion = dirDireccion;
    }

    public String getNumAcuerdo() {
        return numAcuerdo;
    }

    public void setNumAcuerdo(String numAcuerdo) {
        this.numAcuerdo = numAcuerdo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNumResolucion() {
        return numResolucion;
    }

    public void setNumResolucion(String numResolucion) {
        this.numResolucion = numResolucion;
    }

    public LocalDate getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDate fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    
    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
    
    
    
    

}
