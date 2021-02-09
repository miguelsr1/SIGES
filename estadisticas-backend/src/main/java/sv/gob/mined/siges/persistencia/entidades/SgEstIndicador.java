/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.enumerados.EnumTipoResultadoIndicador;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstCategoriaIndicador;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_indicadores", schema = Constantes.SCHEMA_ESTADISTICAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "indPk", scope = SgEstIndicador.class)
@Audited
public class SgEstIndicador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ind_pk", nullable = false)
    private Long indPk;

    @Size(max = 45)
    @Column(name = "ind_codigo", length = 45)
    @AtributoCodigo
    private String indCodigo;

    @Size(max = 1000)
    @Column(name = "ind_nombre", length = 1000)
    private String indNombre;

    @Column(name = "ind_definicion")
    private String indDefinicion;

    @Column(name = "ind_metodo_calculo")
    private String indMetodoCalculo;

    @Column(name = "ind_habilitado")
    @AtributoHabilitado
    private Boolean indHabilitado;

    @Column(name = "ind_descripcion")
    private String indDescripcion;

    @Column(name = "ind_fuente")
    private String indFuente;


    @Column(name = "ind_observaciones")
    private String indObservaciones;
    
    @Column(name = "ind_es_publico")
    private Boolean indEsPublico;
    
    @Column(name = "ind_es_externo")
    private Boolean indEsExterno;
    
    @Column(name = "ind_aplica_paridad_genero")
    private Boolean indAplicaParidadGenero;
    
    @Column(name = "ind_mostrar_total_sin_desagregacion")
    private Boolean indMostrarTotalSinDesagregacion;
    
    @Column(name = "ind_mostrar_total_desagregacion_por_fila")
    private Boolean indMostrarTotalDesagregacionPorFila;
    
    
    @Column(name = "ind_mostrar_total_desagregacion_por_columna")
    private Boolean indMostrarTotalDesagregacionPorColumna;

    @ManyToOne
    @JoinColumn(name = "ind_categoria_fk")
    private SgEstCategoriaIndicador indCategoria;
    
    @Column(name = "ind_precision")
    private Integer indPrecision;
    
    @Column(name = "ind_tipo_resultado")
    @Enumerated(EnumType.STRING)
    private EnumTipoResultadoIndicador indTipoResultado;

    @ElementCollection(targetClass = EnumDesagregacion.class, fetch = FetchType.EAGER)
    @CollectionTable(schema = Constantes.SCHEMA_ESTADISTICAS, name = "sg_indicadores_desagregaciones", joinColumns = @JoinColumn(name = "ind_pk"))
    @Column(name = "ind_desagregacion")
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.SUBSELECT)
    private List<EnumDesagregacion> indDesagregaciones;

    @Column(name = "ind_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime indUltModFecha;

    @Size(max = 45)
    @Column(name = "ind_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String indUltModUsuario;

    @Column(name = "ind_version")
    @Version
    private Integer indVersion;

    @JoinColumn(name = "ind_formula", referencedColumnName = "ach_pk")
    @ManyToOne(cascade = CascadeType.ALL)
    private SgEstArchivo indFormula;

    public SgEstIndicador() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave(){
        if (this.indPrecision == null){
            this.indPrecision = 0;
        }
    }

    public SgEstIndicador(Long indPk, Integer indVersion) {
        this.indPk = indPk;
        this.indVersion = indVersion;
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

    public Boolean getIndHabilitado() {
        return indHabilitado;
    }

    public void setIndHabilitado(Boolean indHabilitado) {
        this.indHabilitado = indHabilitado;
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

    public Boolean getIndEsExterno() {
        return indEsExterno;
    }

    public void setIndEsExterno(Boolean indEsExterno) {
        this.indEsExterno = indEsExterno;
    }

    public EnumTipoResultadoIndicador getIndTipoResultado() {
        return indTipoResultado;
    }

    public void setIndTipoResultado(EnumTipoResultadoIndicador indTipoResultado) {
        this.indTipoResultado = indTipoResultado;
    }

    public Integer getIndPrecision() {
        return indPrecision;
    }

    public void setIndPrecision(Integer indPrecision) {
        this.indPrecision = indPrecision;
    }

    public Boolean getIndAplicaParidadGenero() {
        return indAplicaParidadGenero;
    }

    public void setIndAplicaParidadGenero(Boolean indAplicaParidadGenero) {
        this.indAplicaParidadGenero = indAplicaParidadGenero;
    }
    
    
    
     
    @Override
    public int hashCode() {
        return Objects.hashCode(this.indPk);
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
        return "SgIndicador{" + "indPk=" + indPk + ", indCodigo=" + indCodigo + ", indDefinicion=" + indDefinicion + ", indMetodoCalculo=" + indMetodoCalculo + ", indHabilitado=" + indHabilitado + ", indDescripcion=" + indDescripcion + ", indFuente=" + indFuente + ", indObservaciones=" + indObservaciones + ", indUltModFecha=" + indUltModFecha + ", indUltModUsuario=" + indUltModUsuario + ", indVersion=" + indVersion + '}';
    }

}
