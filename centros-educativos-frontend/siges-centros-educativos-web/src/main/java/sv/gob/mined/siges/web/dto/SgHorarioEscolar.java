/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "hesPk", scope = SgHorarioEscolar.class)
public class SgHorarioEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long hesPk;

    private Integer hesCantidad;

    private LocalDateTime hesUltModFecha;

    private String hesUltModUsuario;

    private Integer hesVersion;

    private SgSeccion hesSeccion;

    private List<SgCeldaDiaHora> hesCeldasDiaHora;
    
    private Boolean hesUnicoDocente;
    
    private SgPersonalSedeEducativaNoJsonIdentity hesDocente;
    
    private List<SgComponenteDocente> hesDocentes;
    
    private List<SgDiaLectivoHorarioEscolar> hesDiasLectivos;

    public SgHorarioEscolar() {
        this.hesCeldasDiaHora = new ArrayList();
        this.hesUnicoDocente = Boolean.FALSE;
        this.hesDocentes = new ArrayList();
    }

    public Long getHesPk() {
        return hesPk;
    }

    public void setHesPk(Long hesPk) {
        this.hesPk = hesPk;
    }

    public LocalDateTime getHesUltModFecha() {
        return hesUltModFecha;
    }

    public void setHesUltModFecha(LocalDateTime hesUltModFecha) {
        this.hesUltModFecha = hesUltModFecha;
    }

    public String getHesUltModUsuario() {
        return hesUltModUsuario;
    }

    public void setHesUltModUsuario(String hesUltModUsuario) {
        this.hesUltModUsuario = hesUltModUsuario;
    }

    public Integer getHesVersion() {
        return hesVersion;
    }

    public void setHesVersion(Integer hesVersion) {
        this.hesVersion = hesVersion;
    }

    public Integer getHesCantidad() {
        return hesCantidad;
    }

    public void setHesCantidad(Integer hesCantidad) {
        this.hesCantidad = hesCantidad;
    }

    public SgSeccion getHesSeccion() {
        return hesSeccion;
    }

    public void setHesSeccion(SgSeccion hesSeccion) {
        this.hesSeccion = hesSeccion;
    }

    public List<SgCeldaDiaHora> getHesCeldasDiaHora() {
        return hesCeldasDiaHora;
    }

    public void setHesCeldasDiaHora(List<SgCeldaDiaHora> hesCeldasDiaHora) {
        this.hesCeldasDiaHora = hesCeldasDiaHora;
    }

    public Boolean getHesUnicoDocente() {
        return hesUnicoDocente;
    }

    public void setHesUnicoDocente(Boolean hesUnicoDocente) {
        this.hesUnicoDocente = hesUnicoDocente;
    }

    public SgPersonalSedeEducativaNoJsonIdentity getHesDocente() {
        return hesDocente;
    }

    public void setHesDocente(SgPersonalSedeEducativaNoJsonIdentity hesDocente) {
        this.hesDocente = hesDocente;
    }

    public List<SgComponenteDocente> getHesDocentes() {
        return hesDocentes;
    }

    public void setHesDocentes(List<SgComponenteDocente> hesDocentes) {
        this.hesDocentes = hesDocentes;
    }

    public List<SgDiaLectivoHorarioEscolar> getHesDiasLectivos() {
        return hesDiasLectivos;
    }

    public void setHesDiasLectivos(List<SgDiaLectivoHorarioEscolar> hesDiasLectivos) {
        this.hesDiasLectivos = hesDiasLectivos;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hesPk != null ? hesPk.hashCode() : 0);
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
        final SgHorarioEscolar other = (SgHorarioEscolar) obj;
        if (!Objects.equals(this.hesPk, other.hesPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgHorarioEscolar[ hesPk=" + hesPk + " ]";
    }

}
