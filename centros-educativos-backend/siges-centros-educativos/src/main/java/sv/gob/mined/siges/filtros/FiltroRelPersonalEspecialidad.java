/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.util.List;

public class FiltroRelPersonalEspecialidad {

    private Long personal;
    private List<Long> personalPks;
    private Long especialidad;
    private List<Long> rplPersonalesPk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroRelPersonalEspecialidad() {
    }

    public Long getPersonal() {
        return personal;
    }

    public void setPersonal(Long personal) {
        this.personal = personal;
    }

    public Long getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Long especialidad) {
        this.especialidad = especialidad;
    }

    public List<Long> getRplPersonalesPk() {
        return rplPersonalesPk;
    }

    public void setRplPersonalesPk(List<Long> rplPersonalesPk) {
        this.rplPersonalesPk = rplPersonalesPk;
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

    public List<Long> getPersonalPks() {
        return personalPks;
    }

    public void setPersonalPks(List<Long> personalPks) {
        this.personalPks = personalPks;
    }

    
}
