/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import sv.gob.mined.siges.web.dto.catalogo.SgAfProyectos;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "dmaPk", scope = SgAfDepreciacionMaestro.class)
public class SgAfDepreciacionMaestro implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long dmaPk;
    private Integer dmaAnioProceso;
    private Integer dmaMesProceso;
    private SgAfFuenteFinanciamiento dmaFuenteFinanciamientoFk;
    private SgAfProyectos dmaProyectoFk;
    private String dmaCodigoInventario;
    private LocalDateTime dmaFechaCreacion;
    private LocalDateTime dmaFechaInicioProcesamiento;
    private LocalDateTime dmaFechaFinalProcesamiento;
    private EnumEstadosProceso dmaEstado;
    private LocalDateTime dmaUltModFecha;
    private String dmaUltModUsuario;
    private Integer dmaVersion;
    private List<SgAfDepreciacion> sgAfDepreciacionList;

    public SgAfDepreciacionMaestro() {
         this.sgAfDepreciacionList = new ArrayList();
    }

    public Long getDmaPk() {
        return dmaPk;
    }

    public void setDmaPk(Long dmaPk) {
        this.dmaPk = dmaPk;
    }

    public Integer getDmaAnioProceso() {
        return dmaAnioProceso;
    }

    public void setDmaAnioProceso(Integer dmaAnioProceso) {
        this.dmaAnioProceso = dmaAnioProceso;
    }

    public Integer getDmaMesProceso() {
        return dmaMesProceso;
    }

    public void setDmaMesProceso(Integer dmaMesProceso) {
        this.dmaMesProceso = dmaMesProceso;
    }

    public SgAfFuenteFinanciamiento getDmaFuenteFinanciamientoFk() {
        return dmaFuenteFinanciamientoFk;
    }

    public void setDmaFuenteFinanciamientoFk(SgAfFuenteFinanciamiento dmaFuenteFinanciamientoFk) {
        this.dmaFuenteFinanciamientoFk = dmaFuenteFinanciamientoFk;
    }

    public SgAfProyectos getDmaProyectoFk() {
        return dmaProyectoFk;
    }

    public void setDmaProyectoFk(SgAfProyectos dmaProyectoFk) {
        this.dmaProyectoFk = dmaProyectoFk;
    }

    public String getDmaCodigoInventario() {
        return dmaCodigoInventario;
    }

    public void setDmaCodigoInventario(String dmaCodigoInventario) {
        this.dmaCodigoInventario = dmaCodigoInventario;
    }

    public LocalDateTime getDmaFechaCreacion() {
        return dmaFechaCreacion;
    }

    public void setDmaFechaCreacion(LocalDateTime dmaFechaCreacion) {
        this.dmaFechaCreacion = dmaFechaCreacion;
    }

    public LocalDateTime getDmaFechaInicioProcesamiento() {
        return dmaFechaInicioProcesamiento;
    }

    public void setDmaFechaInicioProcesamiento(LocalDateTime dmaFechaInicioProcesamiento) {
        this.dmaFechaInicioProcesamiento = dmaFechaInicioProcesamiento;
    }

    public LocalDateTime getDmaFechaFinalProcesamiento() {
        return dmaFechaFinalProcesamiento;
    }

    public void setDmaFechaFinalProcesamiento(LocalDateTime dmaFechaFinalProcesamiento) {
        this.dmaFechaFinalProcesamiento = dmaFechaFinalProcesamiento;
    }

    public EnumEstadosProceso getDmaEstado() {
        return dmaEstado;
    }

    public void setDmaEstado(EnumEstadosProceso dmaEstado) {
        this.dmaEstado = dmaEstado;
    }

    public LocalDateTime getDmaUltModFecha() {
        return dmaUltModFecha;
    }

    public void setDmaUltModFecha(LocalDateTime dmaUltModFecha) {
        this.dmaUltModFecha = dmaUltModFecha;
    }

    public String getDmaUltModUsuario() {
        return dmaUltModUsuario;
    }

    public void setDmaUltModUsuario(String dmaUltModUsuario) {
        this.dmaUltModUsuario = dmaUltModUsuario;
    }

    public Integer getDmaVersion() {
        return dmaVersion;
    }

    public void setDmaVersion(Integer dmaVersion) {
        this.dmaVersion = dmaVersion;
    }

    public List<SgAfDepreciacion> getSgAfDepreciacionList() {
        return sgAfDepreciacionList;
    }

    public void setSgAfDepreciacionList(List<SgAfDepreciacion> sgAfDepreciacionList) {
        this.sgAfDepreciacionList = sgAfDepreciacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dmaPk != null ? dmaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfDepreciacionMaestro)) {
            return false;
        }
        SgAfDepreciacionMaestro other = (SgAfDepreciacionMaestro) object;
        if ((this.dmaPk == null && other.dmaPk != null) || (this.dmaPk != null && !this.dmaPk.equals(other.dmaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfDepreciacionMaestro[ dmaPk=" + dmaPk + " ]";
    }
    
}
