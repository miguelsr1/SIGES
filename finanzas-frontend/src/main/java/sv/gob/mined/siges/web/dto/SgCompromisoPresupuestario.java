/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cprPk", scope = SgCompromisoPresupuestario.class)
public class SgCompromisoPresupuestario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cprPk;

    private SgRequerimientoFondo cprRequerimientoFondoFk;

    private String cprNumeroPresupuestario;

    private LocalDateTime cprUltMod;

    private Long cprFuenteRecursosFk;

    private String nombreRecurso;

    private String cprUltUsuario;

    private Integer cprVersion;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Long getCprPk() {
        return cprPk;
    }

    public void setCprPk(Long cprPk) {
        this.cprPk = cprPk;
    }

    public Long getCprFuenteRecursosFk() {
        return cprFuenteRecursosFk;
    }

    public void setCprFuenteRecursosFk(Long cprFuenteRecursosFk) {
        this.cprFuenteRecursosFk = cprFuenteRecursosFk;
    }

    public SgRequerimientoFondo getCprRequerimientoFondoFk() {
        return cprRequerimientoFondoFk;
    }

    public void setCprRequerimientoFondoFk(SgRequerimientoFondo cprRequerimientoFondoFk) {
        this.cprRequerimientoFondoFk = cprRequerimientoFondoFk;
    }

    public String getCprNumeroPresupuestario() {
        return cprNumeroPresupuestario;
    }

    public void setCprNumeroPresupuestario(String cprNumeroPresupuestario) {
        this.cprNumeroPresupuestario = cprNumeroPresupuestario;
    }

    public LocalDateTime getCprUltMod() {
        return cprUltMod;
    }

    public void setCprUltMod(LocalDateTime cprUltMod) {
        this.cprUltMod = cprUltMod;
    }

    public String getCprUltUsuario() {
        return cprUltUsuario;
    }

    public void setCprUltUsuario(String cprUltUsuario) {
        this.cprUltUsuario = cprUltUsuario;
    }

    public Integer getCprVersion() {
        return cprVersion;
    }

    public void setCprVersion(Integer cprVersion) {
        this.cprVersion = cprVersion;
    }

    public String getNombreRecurso() {
        return nombreRecurso;
    }

    public void setNombreRecurso(String nombreRecurso) {
        this.nombreRecurso = nombreRecurso;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hash-equals">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.cprPk);
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
        final SgCompromisoPresupuestario other = (SgCompromisoPresupuestario) obj;
        if (!Objects.equals(this.cprPk, other.cprPk)) {
            return false;
        }
        return true;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "SgCompromisoPresupuestario{" + "cprPk=" + cprPk + ", cprRequerimientoFondoFk=" + cprRequerimientoFondoFk + ", cprNumeroPresupuestario=" + cprNumeroPresupuestario + ", cprUltMod=" + cprUltMod + ", cprFuenteRecursosFk=" + cprFuenteRecursosFk + ", nombreRecurso=" + nombreRecurso + ", cprUltUsuario=" + cprUltUsuario + ", cprVersion=" + cprVersion + '}';
    }
// </editor-fold>

}
