/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
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
import gob.mined.siap2.entities.enums.TipoEjecucion;
import gob.mined.siap2.entities.enums.TipoMedida;
import gob.mined.siap2.entities.enums.TipoParametro;
import gob.mined.siap2.entities.enums.TipoSubcomponente;
import gob.mined.siges.entities.data.impl.SgTipoCuentaBancaria;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_ges_pres_es")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
//@Cacheable(false)
public class ComponentePresupuestoEscolarLite implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_ges_pres_es", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_ges_pres_es", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ges_pres_es")
    @Column(name = "ges_id")
    private Integer id;
    
    @Column(name = "ges_cod")
    private String cod;

    @Column(name = "ges_nombre")
    private String nombre;
    
    @Column(name = "ges_nombre_busqueda")
    private String nombreBusqueda;
    
    
    @Column(name = "ges_descripcion")
    private String descripcion;
    
    @Column(name = "ges_proyecto")
    private String proyecto;
    
    @Column(name = "ges_subactividad")
    private String subactividad;
    
    @Column(name = "ges_categoria")
    private String categoria;
    
    @Column(name = "ges_convenio")
    private String convenio;
    
    @Column(name = "ges_categoria_gasto_convenio")
    private Integer categoriaGastoConvenio;
    
    @ManyToOne
    @JoinColumn(name = "ges_tipo_transferencia")
    private TipoTransferencia tipoTransferencia;
    
    @Column(name = "ges_habilitado")
    private Boolean habilitado;
    
    @ManyToOne
    @JoinColumn(name = "ges_categoria_componente")
    private CategoriaPresupuestoEscolarLite categoriaPresupuestoEscolar;
    
    @Column(name = "ges_tipo")
    @Enumerated(EnumType.STRING)
    private TipoSubcomponente tipo;
    
    @ManyToOne
    @JoinColumn(name = "ges_tipo_cuenta_bancaria")
    private SgTipoCuentaBancaria tipoCuentaBancaria;
    
    @Column(name = "ges_parametro")
    @Enumerated(EnumType.STRING)
    private TipoParametro parametro;
    
    @Column(name = "ges_uni_med")
    @Enumerated(EnumType.STRING)
    private TipoMedida unidadMedida;
    
    @Column(name = "ges_mto_minimo")
    private Boolean montoMinimo;
    
    @Column(name = "ges_ctdad_anio")
    private Integer cantidadAnio = null;
    
    @Column(name = "ges_concepto")
    private String concepto;

    @ManyToOne
    @JoinColumn(name = "ges_tipo_credito")
    private TipoCredito tipoCredito;
    
    @ManyToOne
    @JoinColumn(name = "ges_unidad_tecnica")
    private UnidadTecnica unidadTecnica;
    
    @Column(name = "ges_tipo_ejecucion")
    @Enumerated(EnumType.STRING)
    private TipoEjecucion tipoEjecucion;
    
    @Column(name = "ges_centros_educativos")
    private Boolean ceduOficiales;
    
    @Column(name = "ges_centros_educativos_privados_sub")
    private Boolean ceduPrivadosSub;
    
    @Column(name = "ges_centros_educativos_privados_no_sub")
    private Boolean ceduPrivadosNoSub;
    
    @Column(name = "ges_circulos_familia")
    private Boolean circulosFamilia;
    
    @Column(name = "ges_sedes_educame")
    private Boolean sedesEducame;
    
    @Column(name = "ges_circulos_alfabetizacion")
    private Boolean circulosAlfabetizacion;
    
    @Column(name = "ges_incluir_sedes_adscritas")
    private Boolean sedesAdscritas;
    
    @ManyToOne
    @JoinColumn(name = "ges_usuario_responsable")
    private SsUsuario usuario;
    
    @Column(name = "ges_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "ges_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;
    
    @Column(name = "ges_version")
    @Version
    private Long version;


    public ComponentePresupuestoEscolarLite() {
        this.habilitado = Boolean.TRUE;
        if(this.id != null) {
            this.cod = StringUtils.leftPad(String.valueOf(this.id), 3, "0");
        }
    }
    
    @PrePersist
    @PreUpdate
    @PostLoad
    public void beforeSave() {
        if(this.id != null) {
            this.cod = StringUtils.leftPad(String.valueOf(this.id), 3, "0");
        }
        this.nombre = this.nombre != null ? this.nombre.trim() : "";
        this.nombreBusqueda = this.nombre != null ? this.nombre.toLowerCase().trim() : "";
    }
 
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }


    public Boolean getMontoMinimo() {
        return montoMinimo;
    }

    public void setMontoMinimo(Boolean montoMinimo) {
        this.montoMinimo = montoMinimo;
    }

    public Integer getCantidadAnio() {
        return cantidadAnio;
    }

    public void setCantidadAnio(Integer cantidadAnio) {
        this.cantidadAnio = cantidadAnio;
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

    public TipoEjecucion getTipoEjecucion() {
        return tipoEjecucion;
    }

    public void setTipoEjecucion(TipoEjecucion tipoEjecucion) {
        this.tipoEjecucion = tipoEjecucion;
    }

    public TipoCredito getTipoCredito() {
        return tipoCredito;
    }

    public void setTipoCredito(TipoCredito tipoCredito) {
        this.tipoCredito = tipoCredito;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public SsUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(SsUsuario usuario) {
        this.usuario = usuario;
    }

    public CategoriaPresupuestoEscolarLite getCategoriaPresupuestoEscolar() {
        return categoriaPresupuestoEscolar;
    }

    public void SubComponentePresupuesto(CategoriaPresupuestoEscolarLite categoriaPresupuestoEscolar) {
        this.categoriaPresupuestoEscolar = categoriaPresupuestoEscolar;
    }


    public Boolean getCeduOficiales() {
        return ceduOficiales;
    }

    public void setCeduOficiales(Boolean ceduOficiales) {
        this.ceduOficiales = ceduOficiales;
    }

    public Boolean getCeduPrivadosSub() {
        return ceduPrivadosSub;
    }

    public void setCeduPrivadosSub(Boolean ceduPrivadosSub) {
        this.ceduPrivadosSub = ceduPrivadosSub;
    }

    public Boolean getCeduPrivadosNoSub() {
        return ceduPrivadosNoSub;
    }

    public void setCeduPrivadosNoSub(Boolean ceduPrivadosNoSub) {
        this.ceduPrivadosNoSub = ceduPrivadosNoSub;
    }

    public Boolean getCirculosFamilia() {
        return circulosFamilia;
    }

    public void setCirculosFamilia(Boolean circulosFamilia) {
        this.circulosFamilia = circulosFamilia;
    }

    public Boolean getSedesEducame() {
        return sedesEducame;
    }

    public void setSedesEducame(Boolean sedesEducame) {
        this.sedesEducame = sedesEducame;
    }

    public Boolean getCirculosAlfabetizacion() {
        return circulosAlfabetizacion;
    }

    public void setCirculosAlfabetizacion(Boolean circulosAlfabetizacion) {
        this.circulosAlfabetizacion = circulosAlfabetizacion;
    }

    public Boolean getSedesAdscritas() {
        return sedesAdscritas;
    }

    public void setSedesAdscritas(Boolean sedesAdscritas) {
        this.sedesAdscritas = sedesAdscritas;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getSubactividad() {
        return subactividad;
    }

    public void setSubactividad(String subactividad) {
        this.subactividad = subactividad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public TipoTransferencia getTipoTransferencia() {
        return tipoTransferencia;
    }

    public void setTipoTransferencia(TipoTransferencia tipoTransferencia) {
        this.tipoTransferencia = tipoTransferencia;
    }

    public TipoSubcomponente getTipo() {
        return tipo;
    }

    public void setTipo(TipoSubcomponente tipo) {
        this.tipo = tipo;
    }

    public TipoParametro getParametro() {
        return parametro;
    }

    public void setParametro(TipoParametro parametro) {
        this.parametro = parametro;
    }

    public TipoMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(TipoMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComponentePresupuestoEscolarLite)) {
            return false;
        }
        ComponentePresupuestoEscolarLite other = (ComponentePresupuestoEscolarLite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[ usuId=" + id + " ]";
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public SgTipoCuentaBancaria getTipoCuentaBancaria() {
        return tipoCuentaBancaria;
    }

    public void setTipoCuentaBancaria(SgTipoCuentaBancaria tipoCuentaBancaria) {
        this.tipoCuentaBancaria = tipoCuentaBancaria;
    }

    public Integer getCategoriaGastoConvenio() {
        return categoriaGastoConvenio;
    }

    public void setCategoriaGastoConvenio(Integer categoriaGastoConvenio) {
        this.categoriaGastoConvenio = categoriaGastoConvenio;
    }

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }
    

   
}
 