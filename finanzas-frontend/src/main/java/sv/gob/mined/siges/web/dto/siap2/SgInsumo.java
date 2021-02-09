/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.siap2;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "insPk", scope = SgInsumo.class)
public class SgInsumo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long insPk;

    private String insCodigo;

    private String insDescr;

    private String insNombre;

    private Boolean ins_Ce;

    private LocalDateTime insUltmodFecha;

    private String insUltmodUsuario;

    private Integer insVersion;

    private List<SsRelAreasInversionInsumo> insAreaRel;

    public SgInsumo() {

    }

    public String getInsCodigoNombre() {
        return this.insCodigo + " - " + this.insNombre;
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Long getInsPk() {
        return insPk;
    }

    public void setInsPk(Long insPk) {
        this.insPk = insPk;
    }

    public String getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(String insCodigo) {
        this.insCodigo = insCodigo;
    }

    public String getInsDescr() {
        return insDescr;
    }

    public void setInsDescr(String insDescr) {
        this.insDescr = insDescr;
    }

    public String getInsNombre() {
        return insNombre;
    }

    public void setInsNombre(String insNombre) {
        this.insNombre = insNombre;
    }

    public Boolean getIns_Ce() {
        return ins_Ce;
    }

    public void setIns_Ce(Boolean ins_Ce) {
        this.ins_Ce = ins_Ce;
    }

    public LocalDateTime getInsUltmodFecha() {
        return insUltmodFecha;
    }

    public void setInsUltmodFecha(LocalDateTime insUltmodFecha) {
        this.insUltmodFecha = insUltmodFecha;
    }

    public String getInsUltmodUsuario() {
        return insUltmodUsuario;
    }

    public void setInsUltmodUsuario(String insUltmodUsuario) {
        this.insUltmodUsuario = insUltmodUsuario;
    }

    public List<SsRelAreasInversionInsumo> getInsAreaRel() {
        return insAreaRel;
    }

    public void setInsAreaRel(List<SsRelAreasInversionInsumo> insAreaRel) {
        this.insAreaRel = insAreaRel;
    }

    public Integer getInsVersion() {
        return insVersion;
    }

    public void setInsVersion(Integer insVersion) {
        this.insVersion = insVersion;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hash-equals">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.insPk);
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
        final SgInsumo other = (SgInsumo) obj;
        if (!Objects.equals(this.insPk, other.insPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgInsumo[ insPk=" + insPk + " ]";
    }
// </editor-fold>
}
