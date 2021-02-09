/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgEstCategoriaIndicador;
import sv.gob.mined.siges.web.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.web.enumerados.EnumTipoResultadoIndicador;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "indPk", scope = SgEstIndicador.class)
public class SgEstIndicador implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long indPk;

    private String indCodigo;

    private String indNombre;

    private String indDefinicion;

    private String indMetodoCalculo;

    private Boolean indHabilitado;

    private String indDescripcion;

    private String indFuente;

    private String indObservaciones;

    private Boolean indEsExterno;

    private Boolean indMostrarTotalSinDesagregacion;

    private Boolean indMostrarTotalDesagregacionPorFila;

    private Boolean indMostrarTotalDesagregacionPorColumna;

    private Boolean indEsPublico;
    
    private EnumTipoResultadoIndicador indTipoResultado;

    private SgEstCategoriaIndicador indCategoria;

    private List<EnumDesagregacion> indDesagregaciones;

    private LocalDateTime indUltModFecha;

    private String indUltModUsuario;

    private Integer indVersion;

    private SgEstArchivo indFormula;

    public SgEstIndicador() {
        this.indHabilitado = Boolean.TRUE;
    }

    public Long getIndPk() {
        return indPk;
    }

    public void setIndPk(Long indPk) {
        this.indPk = indPk;
    }

    public String getIndCodigo() {
        return indCodigo;
    }

    public void setIndCodigo(String indCodigo) {
        this.indCodigo = indCodigo;
    }

    public String getIndNombre() {
        return indNombre;
    }

    public void setIndNombre(String indNombre) {
        this.indNombre = indNombre;
    }

    public String getIndDefinicion() {
        return indDefinicion;
    }

    public void setIndDefinicion(String indDefinicion) {
        this.indDefinicion = indDefinicion;
    }

    public String getIndMetodoCalculo() {
        return indMetodoCalculo;
    }

    public void setIndMetodoCalculo(String indMetodoCalculo) {
        this.indMetodoCalculo = indMetodoCalculo;
    }

    public String getIndDescripcion() {
        return indDescripcion;
    }

    public void setIndDescripcion(String indDescripcion) {
        this.indDescripcion = indDescripcion;
    }

    public String getIndFuente() {
        return indFuente;
    }

    public void setIndFuente(String indFuente) {
        this.indFuente = indFuente;
    }

    public String getIndObservaciones() {
        return indObservaciones;
    }

    public void setIndObservaciones(String indObservaciones) {
        this.indObservaciones = indObservaciones;
    }

    public LocalDateTime getIndUltModFecha() {
        return indUltModFecha;
    }

    public void setIndUltModFecha(LocalDateTime indUltModFecha) {
        this.indUltModFecha = indUltModFecha;
    }

    public String getIndUltModUsuario() {
        return indUltModUsuario;
    }

    public void setIndUltModUsuario(String indUltModUsuario) {
        this.indUltModUsuario = indUltModUsuario;
    }

    public Integer getIndVersion() {
        return indVersion;
    }

    public void setIndVersion(Integer indVersion) {
        this.indVersion = indVersion;
    }

    public Boolean getIndHabilitado() {
        return indHabilitado;
    }

    public void setIndHabilitado(Boolean indHabilitado) {
        this.indHabilitado = indHabilitado;
    }

    public SgEstArchivo getIndFormula() {
        return indFormula;
    }

    public void setIndFormula(SgEstArchivo indFormula) {
        this.indFormula = indFormula;
    }

    public SgEstCategoriaIndicador getIndCategoria() {
        return indCategoria;
    }

    public void setIndCategoria(SgEstCategoriaIndicador indCategoria) {
        this.indCategoria = indCategoria;
    }

    public List<EnumDesagregacion> getIndDesagregaciones() {
        return indDesagregaciones;
    }

    public void setIndDesagregaciones(List<EnumDesagregacion> indDesagregaciones) {
        this.indDesagregaciones = indDesagregaciones;
    }

    public Boolean getIndEsPublico() {
        return indEsPublico;
    }

    public void setIndEsPublico(Boolean indEsPublico) {
        this.indEsPublico = indEsPublico;
    }

    public Boolean getIndEsExterno() {
        return indEsExterno;
    }

    public void setIndEsExterno(Boolean indEsExterno) {
        this.indEsExterno = indEsExterno;
    }

    public Boolean getIndMostrarTotalSinDesagregacion() {
        return indMostrarTotalSinDesagregacion;
    }

    public void setIndMostrarTotalSinDesagregacion(Boolean indMostrarTotalSinDesagregacion) {
        this.indMostrarTotalSinDesagregacion = indMostrarTotalSinDesagregacion;
    }

    public Boolean getIndMostrarTotalDesagregacionPorFila() {
        return indMostrarTotalDesagregacionPorFila;
    }

    public void setIndMostrarTotalDesagregacionPorFila(Boolean indMostrarTotalDesagregacionPorFila) {
        this.indMostrarTotalDesagregacionPorFila = indMostrarTotalDesagregacionPorFila;
    }

    public Boolean getIndMostrarTotalDesagregacionPorColumna() {
        return indMostrarTotalDesagregacionPorColumna;
    }

    public void setIndMostrarTotalDesagregacionPorColumna(Boolean indMostrarTotalDesagregacionPorColumna) {
        this.indMostrarTotalDesagregacionPorColumna = indMostrarTotalDesagregacionPorColumna;
    }

    public EnumTipoResultadoIndicador getIndTipoResultado() {
        return indTipoResultado;
    }

    public void setIndTipoResultado(EnumTipoResultadoIndicador indTipoResultado) {
        this.indTipoResultado = indTipoResultado;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (indPk != null ? indPk.hashCode() : 0);
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
        final SgEstIndicador other = (SgEstIndicador) obj;
        if (!Objects.equals(this.indPk, other.indPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgIndicador[ indPk=" + indPk + " ]";
    }

}
