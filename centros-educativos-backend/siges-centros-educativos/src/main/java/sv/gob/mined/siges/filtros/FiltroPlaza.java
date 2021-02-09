/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumEstadoPlaza;
import sv.gob.mined.siges.enumerados.EnumSituacionPlaza;


public class FiltroPlaza implements Serializable {
    
    private Long idPuesto;
    private Integer partida;
    private Integer subpartida;
    private Integer codigo;
    private EnumEstadoPlaza estado;
    private EnumSituacionPlaza situacion;
    private Integer anioPartida;
    private Long sedeFk;
    private Long departamento;
    private Long municipio;
    private String codigoOnombre;

    
    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    public FiltroPlaza() {
        
    }
    
    //<editor-fold defaultstate="collapsed" desc="GET & SET">

    public Long getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Long idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Integer getPartida() {
        return partida;
    }

    public void setPartida(Integer partida) {
        this.partida = partida;
    }

    public Integer getSubpartida() {
        return subpartida;
    }

    public void setSubpartida(Integer subpartida) {
        this.subpartida = subpartida;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public EnumEstadoPlaza getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadoPlaza estado) {
        this.estado = estado;
    }

    public EnumSituacionPlaza getSituacion() {
        return situacion;
    }

    public void setSituacion(EnumSituacionPlaza situacion) {
        this.situacion = situacion;
    }


    public Integer getAnioPartida() {
        return anioPartida;
    }

    public void setAnioPartida(Integer anioPartida) {
        this.anioPartida = anioPartida;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }
    
    
    //</editor-fold>

    public Long getSedeFk() {
        return sedeFk;
    }

    public void setSedeFk(Long sedeFk) {
        this.sedeFk = sedeFk;
    }

    public Long getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Long departamento) {
        this.departamento = departamento;
    }

    public Long getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Long municipio) {
        this.municipio = municipio;
    }

    public String getCodigoOnombre() {
        return codigoOnombre;
    }

    public void setCodigoOnombre(String codigoOnombre) {
        this.codigoOnombre = codigoOnombre;
    }

   
    
}
