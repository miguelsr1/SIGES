/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class FiltroManifestacionViolencia implements Serializable {
    
    private Long mviPk;    
    private LocalDate mviFecha;    
    private String mviObservaciones;    
    private String mviTratamiento;    
    private LocalDateTime mviUltModFecha;    
    private String mviUltModUsuario;    
    private Integer mviVersion;    
    private Long mviEstudiante;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroManifestacionViolencia() {
    }

    public Long getMviPk() {
        return mviPk;
    }

    public void setMviPk(Long mviPk) {
        this.mviPk = mviPk;
    }

    public LocalDate getMviFecha() {
        return mviFecha;
    }

    public void setMviFecha(LocalDate mviFecha) {
        this.mviFecha = mviFecha;
    }

    public String getMviObservaciones() {
        return mviObservaciones;
    }

    public void setMviObservaciones(String mviObservaciones) {
        this.mviObservaciones = mviObservaciones;
    }

    public String getMviTratamiento() {
        return mviTratamiento;
    }

    public void setMviTratamiento(String mviTratamiento) {
        this.mviTratamiento = mviTratamiento;
    }

    public LocalDateTime getMviUltModFecha() {
        return mviUltModFecha;
    }

    public void setMviUltModFecha(LocalDateTime mviUltModFecha) {
        this.mviUltModFecha = mviUltModFecha;
    }

    public String getMviUltModUsuario() {
        return mviUltModUsuario;
    }

    public void setMviUltModUsuario(String mviUltModUsuario) {
        this.mviUltModUsuario = mviUltModUsuario;
    }

    public Integer getMviVersion() {
        return mviVersion;
    }

    public void setMviVersion(Integer mviVersion) {
        this.mviVersion = mviVersion;
    }

    public Long getMviEstudiante() {
        return mviEstudiante;
    }

    public void setMviEstudiante(Long mviEstudiante) {
        this.mviEstudiante = mviEstudiante;
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
    
    

    
}
