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
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumCeldaDiaHora;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dlhPk", scope = SgDiaLectivoHorarioEscolar.class)
public class SgDiaLectivoHorarioEscolar implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long dlhPk;

    private EnumCeldaDiaHora dlhDia;

    private Boolean dlhEsLectivo;

    private SgHorarioEscolar dlhHorarioEscolarFk;   

    private LocalDateTime dlhUltModFecha;

    private String dlhUltModUsuario;

    private Integer dlhVersion;
    
    
    public SgDiaLectivoHorarioEscolar() {
        this.dlhEsLectivo = Boolean.TRUE;
    }

    public Long getDlhPk() {
        return dlhPk;
    }

    public void setDlhPk(Long dlhPk) {
        this.dlhPk = dlhPk;
    }

    public EnumCeldaDiaHora getDlhDia() {
        return dlhDia;
    }

    public void setDlhDia(EnumCeldaDiaHora dlhDia) {
        this.dlhDia = dlhDia;
    }

    public Boolean getDlhEsLectivo() {
        return dlhEsLectivo;
    }

    public void setDlhEsLectivo(Boolean dlhEsLectivo) {
        this.dlhEsLectivo = dlhEsLectivo;
    }

    public SgHorarioEscolar getDlhHorarioEscolarFk() {
        return dlhHorarioEscolarFk;
    }

    public void setDlhHorarioEscolarFk(SgHorarioEscolar dlhHorarioEscolarFk) {
        this.dlhHorarioEscolarFk = dlhHorarioEscolarFk;
    }

 
    public LocalDateTime getDlhUltModFecha() {
        return dlhUltModFecha;
    }

    public void setDlhUltModFecha(LocalDateTime dlhUltModFecha) {
        this.dlhUltModFecha = dlhUltModFecha;
    }

    public String getDlhUltModUsuario() {
        return dlhUltModUsuario;
    }

    public void setDlhUltModUsuario(String dlhUltModUsuario) {
        this.dlhUltModUsuario = dlhUltModUsuario;
    }

    public Integer getDlhVersion() {
        return dlhVersion;
    }

    public void setDlhVersion(Integer dlhVersion) {
        this.dlhVersion = dlhVersion;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dlhPk != null ? dlhPk.hashCode() : 0);
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
        final SgDiaLectivoHorarioEscolar other = (SgDiaLectivoHorarioEscolar) obj;
        if (!Objects.equals(this.dlhPk, other.dlhPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgDiaLectivoHorarioEscolar[ dlhPk=" + dlhPk + " ]";
    }
    
}
