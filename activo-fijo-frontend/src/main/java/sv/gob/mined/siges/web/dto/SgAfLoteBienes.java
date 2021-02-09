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
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "lbiId", scope = SgAfLoteBienes.class)
public class SgAfLoteBienes implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long lbiId;
    private String lbiCodigoInventarioPadre;
    private String lbiPrimerCodInventario;
    private String lbiUltimoCodInventario;
    private EnumEstadosProceso lbiEstado;
    private Integer lbiCantidadBienesReplicar;
    private LocalDateTime lbiFechaInicioProcesamiento;
    private LocalDateTime lbiFechaFinalProcesamiento;
    private SgSede lbiSede;
    private SgAfUnidadesAdministrativas lbiUnidadesAdministrativas;
    private LocalDateTime lbiUltModFecha;
    private String lbiUltModUsuario;
    private Integer lbiVersion;
    

    public SgAfLoteBienes() {
    }

    public Long getLbiId() {
        return lbiId;
    }

    public void setLbiId(Long lbiId) {
        this.lbiId = lbiId;
    }

    public String getLbiUltimoCodInventario() {
        return lbiUltimoCodInventario;
    }

    public void setLbiUltimoCodInventario(String lbiUltimoCodInventario) {
        this.lbiUltimoCodInventario = lbiUltimoCodInventario;
    }

    public String getLbiPrimerCodInventario() {
        return lbiPrimerCodInventario;
    }

    public void setLbiPrimerCodInventario(String lbiPrimerCodInventario) {
        this.lbiPrimerCodInventario = lbiPrimerCodInventario;
    }

    public LocalDateTime getLbiUltModFecha() {
        return lbiUltModFecha;
    }

    public void setLbiUltModFecha(LocalDateTime lbiUltModFecha) {
        this.lbiUltModFecha = lbiUltModFecha;
    }

    public String getLbiUltModUsuario() {
        return lbiUltModUsuario;
    }

    public void setLbiUltModUsuario(String lbiUltModUsuario) {
        this.lbiUltModUsuario = lbiUltModUsuario;
    }

    public Integer getLbiVersion() {
        return lbiVersion;
    }

    public void setLbiVersion(Integer lbiVersion) {
        this.lbiVersion = lbiVersion;
    }

    public String getLbiCodigoInventarioPadre() {
        return lbiCodigoInventarioPadre;
    }

    public void setLbiCodigoInventarioPadre(String lbiCodigoInventarioPadre) {
        this.lbiCodigoInventarioPadre = lbiCodigoInventarioPadre;
    }

    public EnumEstadosProceso getLbiEstado() {
        return lbiEstado;
    }

    public void setLbiEstado(EnumEstadosProceso lbiEstado) {
        this.lbiEstado = lbiEstado;
    }

    public LocalDateTime getLbiFechaInicioProcesamiento() {
        return lbiFechaInicioProcesamiento;
    }

    public void setLbiFechaInicioProcesamiento(LocalDateTime lbiFechaInicioProcesamiento) {
        this.lbiFechaInicioProcesamiento = lbiFechaInicioProcesamiento;
    }

    public LocalDateTime getLbiFechaFinalProcesamiento() {
        return lbiFechaFinalProcesamiento;
    }

    public void setLbiFechaFinalProcesamiento(LocalDateTime lbiFechaFinalProcesamiento) {
        this.lbiFechaFinalProcesamiento = lbiFechaFinalProcesamiento;
    }

    public Integer getLbiCantidadBienesReplicar() {
        return lbiCantidadBienesReplicar;
    }

    public void setLbiCantidadBienesReplicar(Integer lbiCantidadBienesReplicar) {
        this.lbiCantidadBienesReplicar = lbiCantidadBienesReplicar;
    }

    public SgSede getLbiSede() {
        return lbiSede;
    }

    public void setLbiSede(SgSede lbiSede) {
        this.lbiSede = lbiSede;
    }

    public SgAfUnidadesAdministrativas getLbiUnidadesAdministrativas() {
        return lbiUnidadesAdministrativas;
    }

    public void setLbiUnidadesAdministrativas(SgAfUnidadesAdministrativas lbiUnidadesAdministrativas) {
        this.lbiUnidadesAdministrativas = lbiUnidadesAdministrativas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lbiId != null ? lbiId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfLoteBienes)) {
            return false;
        }
        SgAfLoteBienes other = (SgAfLoteBienes) object;
        if ((this.lbiId == null && other.lbiId != null) || (this.lbiId != null && !this.lbiId.equals(other.lbiId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfLoteBienes[ lbiId=" + lbiId + " ]";
    }
    
}
