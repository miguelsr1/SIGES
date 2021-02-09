package gob.mined.siap2.entities.data.impl;

import gob.mined.siges.entities.data.impl.SgClasificacion;
import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_rel_ges_pres_es_anio_fiscal")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
@Cacheable(false)
public class RelacionGesPresEsAnioFiscal implements Serializable {

    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_rel_gespreses_aniofiscal", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rel_gespreses_aniofiscal", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rel_gespreses_aniofiscal")
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ges_pres_es")
    private ComponentePresupuestoEscolar componentePresupuestoEscolar;

    @ManyToOne
    @JoinColumn(name = "id_anio_fiscal")
    private AnioFiscal anioFiscal;

    @ManyToOne
    @JoinColumn(name = "id_sub_cuenta")
    private Cuentas subCuenta;

    @Column(name = "objeto_especifico_gasto")
    private String objetoEspecificoGasto;

    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "descripcion_busqueda")
    private String descripcionBusqueda;
    
    @OneToMany
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_gespresanio_clasificacion",
            joinColumns = {
                @JoinColumn(name = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "cla_pk")}
    )
    private List<SgClasificacion> clasificaciones;
    
    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoPresupuestoAnio tipo;

    @ManyToOne
    @JoinColumn(name = "deducir_ges_pres_es")
    private ComponentePresupuestoEscolar deducirComponentePresupuestoEscolar;
    
    @OneToMany(mappedBy = "sgPresEsAnioFiscal", fetch = FetchType.LAZY)
    private List<Beneficiarios> beneficiarios;
    
    @OneToMany(mappedBy = "relAnioPresupuesto", fetch = FetchType.LAZY)
    private List<RelacionPresAnioFinanciamiento> relFinanciamiento;

    @Column(name = "monto_por_matricula")
    private BigDecimal montoPorMatricula;

    @Column(name = "monto_minimo")
    private BigDecimal montoMinimo;

    @Column(name = "monto_por_matricula_aprobacion")
    private BigDecimal montoPorMatriculaAprobacion;

    @Column(name = "monto_minimo_aprobacion")
    private BigDecimal montoMinimoAprobacion;
    
    @Column(name = "monto_total_formulado")
    private BigDecimal montoTotalFormulado;
    
    @Column(name = "monto_total_aprobado")
    private BigDecimal montoTotalAprobado;
    
    @Column(name = "cantidad_matriculas")
    private Integer cantidadMatriculas;

    @Column(name = "cantidad_matriculas_aprobadas")
    private Integer cantidadMatriculasAprobadas;
    
    @Column(name = "fecha_matricula")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMatricula;
    
    @Column(name = "nombre_complemento")
    private String nombreComplemento;

    @Column(name = "proceso_en_curso")
    private Boolean procesoEnCurso;
    
    @Column(name = "ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "version")
    @Version
    private Long version;

    @PrePersist
    @PreUpdate
    public void beforeSave() {
        this.descripcion = this.descripcion != null ? this.descripcion.trim() : "";
        this.descripcionBusqueda = this.descripcion != null ? this.descripcion.toLowerCase().trim() : "";
    }
    private transient String nombrePresupuesto;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ComponentePresupuestoEscolar getComponentePresupuestoEscolar() {
        return componentePresupuestoEscolar;
    }

    public void setComponentePresupuestoEscolar(ComponentePresupuestoEscolar componentePresupuestoEscolar) {
        this.componentePresupuestoEscolar = componentePresupuestoEscolar;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public Cuentas getSubCuenta() {
        return subCuenta;
    }

    public void setSubCuenta(Cuentas subCuenta) {
        this.subCuenta = subCuenta;
    }

    public String getObjetoEspecificoGasto() {
        return objetoEspecificoGasto;
    }

    public void setObjetoEspecificoGasto(String objetoEspecificoGasto) {
        this.objetoEspecificoGasto = objetoEspecificoGasto;
    }

    public List<SgClasificacion> getClasificaciones() {
        return clasificaciones;
    }

    public void setClasificaciones(List<SgClasificacion> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }

    public BigDecimal getMontoPorMatricula() {
        return montoPorMatricula;
    }

    public void setMontoPorMatricula(BigDecimal montoPorMatricula) {
        this.montoPorMatricula = montoPorMatricula;
    }

    public BigDecimal getMontoMinimo() {
        return montoMinimo;
    }

    public void setMontoMinimo(BigDecimal montoMinimo) {
        this.montoMinimo = montoMinimo;
    }

    public ComponentePresupuestoEscolar getDeducirComponentePresupuestoEscolar() {
        return deducirComponentePresupuestoEscolar;
    }

    public void setDeducirComponentePresupuestoEscolar(ComponentePresupuestoEscolar deducirComponentePresupuestoEscolar) {
        this.deducirComponentePresupuestoEscolar = deducirComponentePresupuestoEscolar;
    }

    public Date getFechaMatricula() {
        return fechaMatricula;
    }

    public void setFechaMatricula(Date fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public TipoPresupuestoAnio getTipo() {
        return tipo;
    }

    public void setTipo(TipoPresupuestoAnio tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getMontoPorMatriculaAprobacion() {
        return montoPorMatriculaAprobacion;
    }

    public void setMontoPorMatriculaAprobacion(BigDecimal montoPorMatriculaAprobacion) {
        this.montoPorMatriculaAprobacion = montoPorMatriculaAprobacion;
    }

    public BigDecimal getMontoMinimoAprobacion() {
        return montoMinimoAprobacion;
    }

    public void setMontoMinimoAprobacion(BigDecimal montoMinimoAprobacion) {
        this.montoMinimoAprobacion = montoMinimoAprobacion;
    }

    public BigDecimal getMontoTotalFormulado() {
        return montoTotalFormulado;
    }

    public void setMontoTotalFormulado(BigDecimal montoTotalFormulado) {
        this.montoTotalFormulado = montoTotalFormulado;
    }

    public BigDecimal getMontoTotalAprobado() {
        return montoTotalAprobado;
    }

    public void setMontoTotalAprobado(BigDecimal montoTotalAprobado) {
        this.montoTotalAprobado = montoTotalAprobado;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final RelacionGesPresEsAnioFiscal other = (RelacionGesPresEsAnioFiscal) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    public List<Beneficiarios> getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(List<Beneficiarios> beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    public List<RelacionPresAnioFinanciamiento> getRelFinanciamiento() {
        return relFinanciamiento;
    }

    public void setRelFinanciamiento(List<RelacionPresAnioFinanciamiento> relFinanciamiento) {
        this.relFinanciamiento = relFinanciamiento;
    }

    public Integer getCantidadMatriculas() {
        return cantidadMatriculas;
    }

    public void setCantidadMatriculas(Integer cantidadMatriculas) {
        this.cantidadMatriculas = cantidadMatriculas;
    }

    public String getNombreComplemento() {
        return nombreComplemento;
    }

    public void setNombreComplemento(String nombreComplemento) {
        this.nombreComplemento = nombreComplemento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidadMatriculasAprobadas() {
        return cantidadMatriculasAprobadas;
    }

    public void setCantidadMatriculasAprobadas(Integer cantidadMatriculasAprobadas) {
        this.cantidadMatriculasAprobadas = cantidadMatriculasAprobadas;
    }

    public String getNombrePresupuesto() {
        return nombrePresupuesto;
    }

    public void setNombrePresupuesto(String nombrePresupuesto) {
        this.nombrePresupuesto = nombrePresupuesto;
    }

    public String getDescripcionBusqueda() {
        return descripcionBusqueda;
    }

    public void setDescripcionBusqueda(String descripcionBusqueda) {
        this.descripcionBusqueda = descripcionBusqueda;
    }

    public Boolean getProcesoEnCurso() {
        return procesoEnCurso;
    }

    public void setProcesoEnCurso(Boolean procesoEnCurso) {
        this.procesoEnCurso = procesoEnCurso;
    }

    @Override
    public String toString() {
        return "RelacionGesPresEsAnioFiscal{" + "id=" + id + ", componentePresupuestoEscolar=" + componentePresupuestoEscolar + ", anioFiscal=" + anioFiscal + ", subCuenta=" + subCuenta + ", objetoEspecificoGasto=" + objetoEspecificoGasto + ", descripcion=" + descripcion + ", descripcionBusqueda=" + descripcionBusqueda + ", clasificaciones=" + clasificaciones + ", tipo=" + tipo + ", deducirComponentePresupuestoEscolar=" + deducirComponentePresupuestoEscolar + ", beneficiarios=" + beneficiarios + ", relFinanciamiento=" + relFinanciamiento + ", montoPorMatricula=" + montoPorMatricula + ", montoMinimo=" + montoMinimo + ", montoPorMatriculaAprobacion=" + montoPorMatriculaAprobacion + ", montoMinimoAprobacion=" + montoMinimoAprobacion + ", montoTotalFormulado=" + montoTotalFormulado + ", montoTotalAprobado=" + montoTotalAprobado + ", cantidadMatriculas=" + cantidadMatriculas + ", cantidadMatriculasAprobadas=" + cantidadMatriculasAprobadas + ", fechaMatricula=" + fechaMatricula + ", nombreComplemento=" + nombreComplemento + ", procesoEnCurso=" + procesoEnCurso + ", ultMod=" + ultMod + ", ultUsuario=" + ultUsuario + ", version=" + version + ", nombrePresupuesto=" + nombrePresupuesto + '}';
    }
    
}
