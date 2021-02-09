/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "repPk", scope = SgGradoReportaEn.class)
public class SgGradoReportaEn implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long repPk;

    private Long repGradoOrigen;

    private Long repGradoDestino;
    
    private SgExtraccion repExtraccion;
   
    private LocalDateTime repUltModFecha;

    private String repUltModUsuario;

    private Integer repVersion;

    public SgGradoReportaEn() {
    }

    public Long getRepPk() {
        return repPk;
    }

    public void setRepPk(Long repPk) {
        this.repPk = repPk;
    }

    public Long getRepGradoOrigen() {
        return repGradoOrigen;
    }

    public void setRepGradoOrigen(Long repGradoOrigen) {
        this.repGradoOrigen = repGradoOrigen;
    }

    public Long getRepGradoDestino() {
        return repGradoDestino;
    }

    public void setRepGradoDestino(Long repGradoDestino) {
        this.repGradoDestino = repGradoDestino;
    }

    public SgExtraccion getRepExtraccion() {
        return repExtraccion;
    }

    public void setRepExtraccion(SgExtraccion repExtraccion) {
        this.repExtraccion = repExtraccion;
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

    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.repPk);
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
        final SgGradoReportaEn other = (SgGradoReportaEn) obj;
        if (!Objects.equals(this.repPk, other.repPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgGradoReportaEn{" + "repPk=" + repPk + ", repGradoOrigen=" + repGradoOrigen + ", repGradoDestino=" + repGradoDestino + '}';
    }
    
    


}
