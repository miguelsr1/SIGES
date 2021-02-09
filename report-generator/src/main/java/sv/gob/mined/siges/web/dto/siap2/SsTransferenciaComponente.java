/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.siap2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.finanzas.SgTransferenciaDireccionDep;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tcId", scope = SsTransferenciaComponente.class)
public class SsTransferenciaComponente implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tcId;
    
    private SsCategoriaPresupuestoEscolar componente;
    
    private SsGesPresEs subComponente;
    
    private SsCuenta unidadPresupuestaria;
    
    private SsCuenta lineaPresupuestaria;
    
    private FuenteRecursos tcFuenteRecursosFk;
    
    private SsTransferencia tcTransferencia;
    
    private SgTransferenciaDireccionDep transferenciaDireccionDep;
    
    private Integer tcAnioFiscal;
    
    private Double tcPorcentaje;
    
    private Integer tcEstado;

    private LocalDateTime tcUltMod;

    private String tcUltUsuario;

    private Integer tcVersion;
    
    
    public SsTransferenciaComponente() {
    }

    public Long getTcId() {
        return tcId;
    }

    public void setTcId(Long tcId) {
        this.tcId = tcId;
    }

    public SsCategoriaPresupuestoEscolar getComponente() {
        return componente;
    }

    public void setComponente(SsCategoriaPresupuestoEscolar componente) {
        this.componente = componente;
    }

    public SsGesPresEs getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(SsGesPresEs subComponente) {
        this.subComponente = subComponente;
    }

    public Integer getTcAnioFiscal() {
        return tcAnioFiscal;
    }

    public void setTcAnioFiscal(Integer tcAnioFiscal) {
        this.tcAnioFiscal = tcAnioFiscal;
    }

    public Double getTcPorcentaje() {
        return tcPorcentaje;
    }

    public void setTcPorcentaje(Double tcPorcentaje) {
        this.tcPorcentaje = tcPorcentaje;
    }

    public Integer getTcEstado() {
        return tcEstado;
    }

    public void setTcEstado(Integer tcEstado) {
        this.tcEstado = tcEstado;
    }

    
    
    public LocalDateTime getTcUltMod() {
        return tcUltMod;
    }

    public void setTcUltMod(LocalDateTime tcUltMod) {
        this.tcUltMod = tcUltMod;
    }

    public String getTcUltUsuario() {
        return tcUltUsuario;
    }

    public void setTcUltUsuario(String tcUltUsuario) {
        this.tcUltUsuario = tcUltUsuario;
    }

    public Integer getTcVersion() {
        return tcVersion;
    }

    public void setTcVersion(Integer tcVersion) {
        this.tcVersion = tcVersion;
    }

    public SsCuenta getUnidadPresupuestaria() {
        return unidadPresupuestaria;
    }

    public void setUnidadPresupuestaria(SsCuenta unidadPresupuestaria) {
        this.unidadPresupuestaria = unidadPresupuestaria;
    }

    public SsCuenta getLineaPresupuestaria() {
        return lineaPresupuestaria;
    }

    public void setLineaPresupuestaria(SsCuenta lineaPresupuestaria) {
        this.lineaPresupuestaria = lineaPresupuestaria;
    }

    public FuenteRecursos getTcFuenteRecursosFk() {
        return tcFuenteRecursosFk;
    }

    public void setTcFuenteRecursosFk(FuenteRecursos tcFuenteRecursosFk) {
        this.tcFuenteRecursosFk = tcFuenteRecursosFk;
    }

    public SsTransferencia getTcTransferencia() {
        return tcTransferencia;
    }

    public void setTcTransferencia(SsTransferencia tcTransferencia) {
        this.tcTransferencia = tcTransferencia;
    }

    public SgTransferenciaDireccionDep getTransferenciaDireccionDep() {
        return transferenciaDireccionDep;
    }

    public void setTransferenciaDireccionDep(SgTransferenciaDireccionDep transferenciaDireccionDep) {
        this.transferenciaDireccionDep = transferenciaDireccionDep;
    }
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tcId != null ? tcId.hashCode() : 0);
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
        final SsTransferenciaComponente other = (SsTransferenciaComponente) obj;
        if (!Objects.equals(this.tcId, other.tcId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SsTransferenciaComponente{" + "tcId=" + tcId + ", componente=" + componente + ", subComponente=" + subComponente + ", unidadPresupuestaria=" + unidadPresupuestaria + ", lineaPresupuestaria=" + lineaPresupuestaria + ", tcFuenteRecursosFk=" + tcFuenteRecursosFk + ", tcTransferencia=" + tcTransferencia + ", transferenciaDireccionDep=" + transferenciaDireccionDep + ", tcAnioFiscal=" + tcAnioFiscal + ", tcPorcentaje=" + tcPorcentaje + ", tcEstado=" + tcEstado + ", tcUltMod=" + tcUltMod + ", tcUltUsuario=" + tcUltUsuario + ", tcVersion=" + tcVersion + '}';
    }


    
    
}
