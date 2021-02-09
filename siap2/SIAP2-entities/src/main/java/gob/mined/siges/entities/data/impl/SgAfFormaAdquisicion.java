/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_fomas_adquisicion", schema = Constantes.SCHEMA_CATALOGO)
@Cacheable(false)
public class SgAfFormaAdquisicion  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "fad_pk")
    private Long fadPk;

    @Size(max = 4)
    @Column(name = "fad_codigo", length = 4)
    private String fadCodigo;

    @Column(name = "fad_version")
    @Version
    private Integer fadVersion;

    public SgAfFormaAdquisicion() {
    }

    public Long getFadPk() {
        return fadPk;
    }

    public void setFadPk(Long fadPk) {
        this.fadPk = fadPk;
    }

    public String getFadCodigo() {
        return fadCodigo;
    }

    public void setFadCodigo(String fadCodigo) {
        this.fadCodigo = fadCodigo;
    }

    public Integer getFadVersion() {
        return fadVersion;
    }

    public void setFadVersion(Integer fadVersion) {
        this.fadVersion = fadVersion;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgFormaAdquisicion[ fadPk=" + fadPk + " ]";
    }

}
