/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dedPk", scope = SgDetalleDesembolso.class)
public class SgDetalleDesembolso implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dedPk;
    
    private SsFuenteRecursos dedFuenteRecursosFk;

    private Double dedPorcentaje;
    
    private BigDecimal dedMonto;
    
    private LocalDateTime dedUltModFecha;

    private String dedUltModUsuario;

    private Integer dedVersion;


    public SgDetalleDesembolso() {
    }

    public Long getDedPk() {
        return dedPk;
    }

    public void setDedPk(Long dedPk) {
        this.dedPk = dedPk;
    }


    public BigDecimal getDedMonto() {
        return dedMonto;
    }

    public void setDedMonto(BigDecimal dedMonto) {
        this.dedMonto = dedMonto;
    }

    public LocalDateTime getDedUltModFecha() {
        return dedUltModFecha;
    }

    public void setDedUltModFecha(LocalDateTime dedUltModFecha) {
        this.dedUltModFecha = dedUltModFecha;
    }

    public String getDedUltModUsuario() {
        return dedUltModUsuario;
    }

    public void setDedUltModUsuario(String dedUltModUsuario) {
        this.dedUltModUsuario = dedUltModUsuario;
    }

    public Integer getDedVersion() {
        return dedVersion;
    }

    public void setDedVersion(Integer dedVersion) {
        this.dedVersion = dedVersion;
    }

    public SsFuenteRecursos getDedFuenteRecursosFk() {
        return dedFuenteRecursosFk;
    }

    public void setDedFuenteRecursosFk(SsFuenteRecursos dedFuenteRecursosFk) {
        this.dedFuenteRecursosFk = dedFuenteRecursosFk;
    }

    public Double getDedPorcentaje() {
        return dedPorcentaje;
    }

    public void setDedPorcentaje(Double dedPorcentaje) {
        this.dedPorcentaje = dedPorcentaje;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.dedPk);
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
        final SgDetalleDesembolso other = (SgDetalleDesembolso) obj;
        if (!Objects.equals(this.dedPk, other.dedPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDetalleDesembolso{" + "dedPk=" + dedPk + '}';
    }
    

}
