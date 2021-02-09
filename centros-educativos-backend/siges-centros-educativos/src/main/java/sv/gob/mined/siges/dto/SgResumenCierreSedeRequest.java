
package sv.gob.mined.siges.dto;

import java.io.Serializable;

/**
 *
 * @author fabricio
 */
public class SgResumenCierreSedeRequest implements Serializable {
    
    private Long cierreAnioPk;
    
    private Long sedePk;
    private Long anioLectivoPk;

    public Long getCierreAnioPk() {
        return cierreAnioPk;
    }

    public void setCierreAnioPk(Long cierreAnioPk) {
        this.cierreAnioPk = cierreAnioPk;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Long getAnioLectivoPk() {
        return anioLectivoPk;
    }

    public void setAnioLectivoPk(Long anioLectivoPk) {
        this.anioLectivoPk = anioLectivoPk;
    }
    
    
    
}
