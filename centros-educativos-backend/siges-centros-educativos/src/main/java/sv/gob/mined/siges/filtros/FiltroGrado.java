/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.List;

public class FiltroGrado implements Serializable {
    
    private Long modPk;
    private Long modAtencionPk;
    private Long subModAtencionPk;
    private Long sedePk;
    private Long anioLectivoPk;
    private Boolean habilitado;
    private Long relModEdModAtenPk;
    private Boolean graDefinicionTitulo;
    private List<Long> listRelModEdModAtenPk;
    private Long graPk;
     
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;

    public FiltroGrado() {
        this.seNecesitaDistinct = Boolean.FALSE;
    }
    
    public Long getModPk() {
        return modPk;
    }

    public void setModPk(Long modPk) {
        this.modPk = modPk;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Long getRelModEdModAtenPk() {
        return relModEdModAtenPk;
    }

    public void setRelModEdModAtenPk(Long relModEdModAtenPk) {
        this.relModEdModAtenPk = relModEdModAtenPk;
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

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getModAtencionPk() {
        return modAtencionPk;
    }

    public void setModAtencionPk(Long modAtencionPk) {
        this.modAtencionPk = modAtencionPk;
    }

    public Long getSubModAtencionPk() {
        return subModAtencionPk;
    }

    public void setSubModAtencionPk(Long subModAtencionPk) {
        this.subModAtencionPk = subModAtencionPk;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Long getAnioLectivoPk() {
        return anioLectivoPk;
    }

    public void setAnioLectivoPk(Long anioLectivoPk) {
        this.anioLectivoPk = anioLectivoPk;
    }  

    public Boolean getGraDefinicionTitulo() {
        return graDefinicionTitulo;
    }

    public void setGraDefinicionTitulo(Boolean graDefinicionTitulo) {
        this.graDefinicionTitulo = graDefinicionTitulo;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public List<Long> getListRelModEdModAtenPk() {
        return listRelModEdModAtenPk;
    }

    public void setListRelModEdModAtenPk(List<Long> listRelModEdModAtenPk) {
        this.listRelModEdModAtenPk = listRelModEdModAtenPk;
    }

    public Long getGraPk() {
        return graPk;
    }

    public void setGraPk(Long graPk) {
        this.graPk = graPk;
    }
    
}
