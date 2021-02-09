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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "repPk", scope = SgReporte.class)
public class SgReporte implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long repPk;

    private String repCodigo;

    private String repNombre;

    private Boolean repHabilitado;

    private LocalDateTime repUltModFecha;

    private String repUltModUsuario;

    private Integer repVersion;
    
    private String repDescripcion;

    private String repOperacionAsociada;
    
    private SgPlantilla repPlantilla;
    
    
    public SgReporte() {
        this.repHabilitado = Boolean.TRUE;
    }

    public Long getRepPk() {
        return repPk;
    }

    public void setRepPk(Long repPk) {
        this.repPk = repPk;
    }

    public String getRepCodigo() {
        return repCodigo;
    }

    public void setRepCodigo(String repCodigo) {
        this.repCodigo = repCodigo;
    }

    public String getRepNombre() {
        return repNombre;
    }

    public void setRepNombre(String repNombre) {
        this.repNombre = repNombre;
    }

    public LocalDateTime getRepUltModFecha() {
        return repUltModFecha;
    }

    public void setRepUltModFecha(LocalDateTime repUltModFecha) {
        this.repUltModFecha = repUltModFecha;
    }

    public String getRepUltModUsuario() {
        return repUltModUsuario;
    }

    public void setRepUltModUsuario(String repUltModUsuario) {
        this.repUltModUsuario = repUltModUsuario;
    }

    public Integer getRepVersion() {
        return repVersion;
    }

    public void setRepVersion(Integer repVersion) {
        this.repVersion = repVersion;
    }

    
     public Boolean getRepHabilitado() {
        return repHabilitado;
    }

    public void setRepHabilitado(Boolean repHabilitado) {
        this.repHabilitado = repHabilitado;
    }

    public String getRepDescripcion() {
        return repDescripcion;
    }

    public void setRepDescripcion(String repDescripcion) {
        this.repDescripcion = repDescripcion;
    }

    public String getRepOperacionAsociada() {
        return repOperacionAsociada;
    }

    public void setRepOperacionAsociada(String repOperacionAsociada) {
        this.repOperacionAsociada = repOperacionAsociada;
    }

    public SgPlantilla getRepPlantilla() {
        return repPlantilla;
    }

    public void setRepPlantilla(SgPlantilla repPlantilla) {
        this.repPlantilla = repPlantilla;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (repPk != null ? repPk.hashCode() : 0);
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
        final SgReporte other = (SgReporte) obj;
        if (!Objects.equals(this.repPk, other.repPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgReporte[ repPk=" + repPk + " ]";
    }
    
}
