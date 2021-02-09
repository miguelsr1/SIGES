/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.utils.SofisStringUtils;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mesPk", scope = SgModalidadEstudio.class)
public class SgModalidadEstudio implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long mesPk;

    private String mesCodigo;

    private Boolean mesHabilitado;

    private String mesNombre;

    private String mesNombreBusqueda;

    private LocalDateTime mesUltModFecha;

    private String mesUltModUsuario;

    private Integer mesVersion;

    public SgModalidadEstudio() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mesNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mesNombre);
    }

    public SgModalidadEstudio(Long mesPk) {
        this.mesPk = mesPk;
    }

    public Long getMesPk() {
        return mesPk;
    }

    public void setMesPk(Long mesPk) {
        this.mesPk = mesPk;
    }

    public String getMesCodigo() {
        return mesCodigo;
    }

    public void setMesCodigo(String mesCodigo) {
        this.mesCodigo = mesCodigo;
    }

    public Boolean getMesHabilitado() {
        return mesHabilitado;
    }

    public void setMesHabilitado(Boolean mesHabilitado) {
        this.mesHabilitado = mesHabilitado;
    }

    public String getMesNombre() {
        return mesNombre;
    }

    public void setMesNombre(String mesNombre) {
        this.mesNombre = mesNombre;
    }

    public String getMesNombreBusqueda() {
        return mesNombreBusqueda;
    }

    public void setMesNombreBusqueda(String mesNombreBusqueda) {
        this.mesNombreBusqueda = mesNombreBusqueda;
    }

    public LocalDateTime getMesUltModFecha() {
        return mesUltModFecha;
    }

    public void setMesUltModFecha(LocalDateTime mesUltModFecha) {
        this.mesUltModFecha = mesUltModFecha;
    }

    public String getMesUltModUsuario() {
        return mesUltModUsuario;
    }

    public void setMesUltModUsuario(String mesUltModUsuario) {
        this.mesUltModUsuario = mesUltModUsuario;
    }

    public Integer getMesVersion() {
        return mesVersion;
    }

    public void setMesVersion(Integer mesVersion) {
        this.mesVersion = mesVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mesPk != null ? mesPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgModalidadEstudio)) {
            return false;
        }
        SgModalidadEstudio other = (SgModalidadEstudio) object;
        if ((this.mesPk == null && other.mesPk != null) || (this.mesPk != null && !this.mesPk.equals(other.mesPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgModalidadEstudio[ mesPk=" + mesPk + " ]";
    }

}
