/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.siap2;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rinPk", scope = SsRelAreasInversionInsumo.class)
public class SsRelAreasInversionInsumo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rinPk;

    private SgAreaInversion rinAreaInversionFk;

    private SgInsumo rinInsumoFk;

    public SsRelAreasInversionInsumo() {

    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Long getRinPk() {
        return rinPk;
    }

    public void setRinPk(Long rinPk) {
        this.rinPk = rinPk;
    }

    public SgAreaInversion getRinAreaInversionFk() {
        return rinAreaInversionFk;
    }

    public void setRinAreaInversionFk(SgAreaInversion rinAreaInversionFk) {
        this.rinAreaInversionFk = rinAreaInversionFk;
    }

    public SgInsumo getRinInsumoFk() {
        return rinInsumoFk;
    }

    public void setRinInsumoFk(SgInsumo rinInsumoFk) {
        this.rinInsumoFk = rinInsumoFk;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hash-equals">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.rinPk);
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
        final SsRelAreasInversionInsumo other = (SsRelAreasInversionInsumo) obj;
        if (!Objects.equals(this.rinPk, other.rinPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SsRelAreasInversionInsumo[ rinPk=" + rinPk + " ]";
    }
// </editor-fold>
}
