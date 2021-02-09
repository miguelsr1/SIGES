/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import sv.gob.mined.siges.web.enumerados.EnumPlanCompraEstado;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "plaPk", scope = SgEncabezadoPlanCompras.class)
public class SgEncabezadoPlanCompras implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long plaPk;

    private SgPresupuestoEscolar plaPresupuestoFk;

    private EnumPlanCompraEstado plaEstado;

    private String plaComentario;

    private LocalDateTime plaUltModFecha;

    private String plaUltModUsuario;

    private Integer plaVersion;

    public SgEncabezadoPlanCompras() {

    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getPlaPk() {
        return plaPk;
    }

    public void setPlaPk(Long plaPk) {
        this.plaPk = plaPk;
    }

    public SgPresupuestoEscolar getPlaPresupuestoFk() {
        return plaPresupuestoFk;
    }

    public void setPlaPresupuestoFk(SgPresupuestoEscolar plaPresupuestoFk) {
        this.plaPresupuestoFk = plaPresupuestoFk;
    }

    public EnumPlanCompraEstado getPlaEstado() {
        return plaEstado;
    }

    public void setPlaEstado(EnumPlanCompraEstado plaEstado) {
        this.plaEstado = plaEstado;
    }

    public String getPlaComentario() {
        
        return StringUtils.isNotBlank(plaComentario) ? Jsoup.parse(plaComentario).text() : "";
    }

    public void setPlaComentario(String plaComentario) {
        this.plaComentario = plaComentario;
    }

    public LocalDateTime getPlaUltModFecha() {
        return plaUltModFecha;
    }

    public void setPlaUltModFecha(LocalDateTime plaUltModFecha) {
        this.plaUltModFecha = plaUltModFecha;
    }

    public String getPlaUltModUsuario() {
        return plaUltModUsuario;
    }

    public void setPlaUltModUsuario(String plaUltModUsuario) {
        this.plaUltModUsuario = plaUltModUsuario;
    }

    public Integer getPlaVersion() {
        return plaVersion;
    }

    public void setPlaVersion(Integer plaVersion) {
        this.plaVersion = plaVersion;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plaPk != null ? plaPk.hashCode() : 0);
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
        final SgEncabezadoPlanCompras other = (SgEncabezadoPlanCompras) obj;
        if (!Objects.equals(this.plaPk, other.plaPk)) {
            return false;
        }
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "com.sofis.entidades.SgEncabezadoPlanCompras[ plaPk=" + plaPk + " ]";
    }
    // </editor-fold>

}
