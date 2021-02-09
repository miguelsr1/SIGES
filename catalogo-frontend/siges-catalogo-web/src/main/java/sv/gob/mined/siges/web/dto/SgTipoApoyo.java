/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tapPk", scope = SgTipoApoyo.class)
public class SgTipoApoyo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tapPk;

    private String tapCodigo;

    private String tapNombre;

    private Boolean tapHabilitado;

    private LocalDateTime tapUltModFecha;

    private String tapUltModUsuario;

    private Integer tapVersion;
    
    private Boolean tapAplicaSede;
    
    private Boolean tapAplicaEstudiante;
    
    
    public SgTipoApoyo() {
        this.tapHabilitado = Boolean.TRUE;
        tapAplicaSede=Boolean.FALSE;
        tapAplicaEstudiante=Boolean.FALSE;
    }

    public Long getTapPk() {
        return tapPk;
    }

    public void setTapPk(Long tapPk) {
        this.tapPk = tapPk;
    }

    public String getTapCodigo() {
        return tapCodigo;
    }

    public void setTapCodigo(String tapCodigo) {
        this.tapCodigo = tapCodigo;
    }

    public String getTapNombre() {
        return tapNombre;
    }

    public void setTapNombre(String tapNombre) {
        this.tapNombre = tapNombre;
    }

    public LocalDateTime getTapUltModFecha() {
        return tapUltModFecha;
    }

    public void setTapUltModFecha(LocalDateTime tapUltModFecha) {
        this.tapUltModFecha = tapUltModFecha;
    }

    public String getTapUltModUsuario() {
        return tapUltModUsuario;
    }

    public void setTapUltModUsuario(String tapUltModUsuario) {
        this.tapUltModUsuario = tapUltModUsuario;
    }

    public Integer getTapVersion() {
        return tapVersion;
    }

    public void setTapVersion(Integer tapVersion) {
        this.tapVersion = tapVersion;
    }

    
     public Boolean getTapHabilitado() {
        return tapHabilitado;
    }

    public void setTapHabilitado(Boolean tapHabilitado) {
        this.tapHabilitado = tapHabilitado;
    }

    public Boolean getTapAplicaSede() {
        return tapAplicaSede;
    }

    public void setTapAplicaSede(Boolean tapAplicaSede) {
        this.tapAplicaSede = tapAplicaSede;
    }

    public Boolean getTapAplicaEstudiante() {
        return tapAplicaEstudiante;
    }

    public void setTapAplicaEstudiante(Boolean tapAplicaEstudiante) {
        this.tapAplicaEstudiante = tapAplicaEstudiante;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tapPk != null ? tapPk.hashCode() : 0);
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
        final SgTipoApoyo other = (SgTipoApoyo) obj;
        if (!Objects.equals(this.tapPk, other.tapPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoApoyo[ tapPk=" + tapPk + " ]";
    }
    
}
