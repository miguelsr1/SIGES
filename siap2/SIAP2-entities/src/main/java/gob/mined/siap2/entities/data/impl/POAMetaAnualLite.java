/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.enums.TipoMedicion;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_poa_metas_anuales")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
@Cacheable(false)
public class POAMetaAnualLite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poa_metas_anuales_gen")
    @SequenceGenerator(name = "poa_metas_anuales_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".poa_metas_anuales_seq", allocationSize = 1)
    @Column(name = "pma_id")
    private Integer id;
    
    @Size(max = 500)
    @Column(name = "pma_meta_anual", length = 500)
    private String metaAnual;
    
    
    @ManyToOne
    @JoinColumn(name = "pma_indicador")
    private Indicador indicador;
     
    @ManyToOne
    @JoinColumn(name = "pma_poa_fk")
    private POA poa;

    @Column(name = "pma_vinculado_up")
    private Boolean vinculadoUnidadOperativa;
    
    @Size(max = 500)
    @Column(name = "pma_medio_verificacion_indicador", length = 500)
    private String medioVerificacionIndicador;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "pma_tipo_medicion")
    private TipoMedicion tipoMedicion;
            
    
    @Column(name = "pma_programa_t1")
    private Integer programaPrimerTrimestre=0;
    
    @Column(name = "pma_programa_t2")
    private Integer programaSegundoTrimestre=0;
            
    @Column(name = "pma_programa_t3")
    private Integer programaTercerTrimestre=0;
            
    @Column(name = "pma_programa_t4")
    private Integer programaCuartoTrimestre=0;
    
    @Column(name = "pma_programa_real_t1")
    private Integer programaPrimerTrimestreReal=0;
    
    @Column(name = "pma_programa_real_t2")
    private Integer programaSegundoTrimestreReal=0;
            
    @Column(name = "pma_programa_real_t3")
    private Integer programaTercerTrimestreReal=0;
            
    @Column(name = "pma_programa_real_t4")
    private Integer programaCuartoTrimestreReal=0;
    
    @Size(max = 500)
    @Column(name = "pma_alcance_limitaciones", length = 500)
    private String alcanceLimitaciones;
    
    @Size(max = 500)
    @Column(name = "pma_medio_verificacion", length = 500)
    private String medioVerificacion;
      
    @Column(name = "pma_fecha_ult_mod_seguimiento")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultFechaModSeguimiento;
    
    @Column(name = "pma_fecha_ult_mod_seguimiento_t1")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultFechaModSeguimientoPrimerTrimestre;
    
    @Column(name = "pma_fecha_ult_mod_seguimiento_t2")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultFechaModSeguimientoSegundoTrimestre;
    
    @Column(name = "pma_fecha_ult_mod_seguimiento_t3")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultFechaModSeguimientoTercerTrimestre;
    
    @Column(name = "pma_fecha_ult_mod_seguimiento_t4")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultFechaModSeguimientoCuartoTrimestre;
    
    //Auditoria
    @Column(name = "pma_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pma_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pma_version")
    @Version
    private Integer version;
    
    @Transient
    private transient Integer total;

    @Transient
    private transient Integer totalReal;
    
    public POAMetaAnualLite() {
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMetaAnual() {
        return metaAnual;
    }

    public void setMetaAnual(String metaAnual) {
        this.metaAnual = metaAnual;
    }

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }

    public POA getPoa() {
        return poa;
    }

    public void setPoa(POA poa) {
        this.poa = poa;
    }

    public Boolean getVinculadoUnidadOperativa() {
        return vinculadoUnidadOperativa;
    }

    public void setVinculadoUnidadOperativa(Boolean vinculadoUnidadOperativa) {
        this.vinculadoUnidadOperativa = vinculadoUnidadOperativa;
    }

    public String getMedioVerificacionIndicador() {
        return medioVerificacionIndicador;
    }

    public void setMedioVerificacionIndicador(String medioVerificacionIndicador) {
        this.medioVerificacionIndicador = medioVerificacionIndicador;
    }

    public TipoMedicion getTipoMedicion() {
        return tipoMedicion;
    }

    public void setTipoMedicion(TipoMedicion tipoMedicion) {
        this.tipoMedicion = tipoMedicion;
    }

    public Integer getProgramaPrimerTrimestre() {
        return programaPrimerTrimestre;
    }

    public void setProgramaPrimerTrimestre(Integer programaPrimerTrimestre) {
        this.programaPrimerTrimestre = programaPrimerTrimestre;
    }

    public Integer getProgramaSegundoTrimestre() {
        return programaSegundoTrimestre;
    }

    public void setProgramaSegundoTrimestre(Integer programaSegundoTrimestre) {
        this.programaSegundoTrimestre = programaSegundoTrimestre;
    }

    public Integer getProgramaTercerTrimestre() {
        return programaTercerTrimestre;
    }

    public void setProgramaTercerTrimestre(Integer programaTercerTrimestre) {
        this.programaTercerTrimestre = programaTercerTrimestre;
    }

    public Integer getProgramaCuartoTrimestre() {
        return programaCuartoTrimestre;
    }

    public void setProgramaCuartoTrimestre(Integer programaCuartoTrimestre) {
        this.programaCuartoTrimestre = programaCuartoTrimestre;
    }

    public Integer getProgramaPrimerTrimestreReal() {
        return programaPrimerTrimestreReal;
    }

    public void setProgramaPrimerTrimestreReal(Integer programaPrimerTrimestreReal) {
        this.programaPrimerTrimestreReal = programaPrimerTrimestreReal;
    }

    public Integer getProgramaSegundoTrimestreReal() {
        return programaSegundoTrimestreReal;
    }

    public void setProgramaSegundoTrimestreReal(Integer programaSegundoTrimestreReal) {
        this.programaSegundoTrimestreReal = programaSegundoTrimestreReal;
    }

    public Integer getProgramaTercerTrimestreReal() {
        return programaTercerTrimestreReal;
    }

    public void setProgramaTercerTrimestreReal(Integer programaTercerTrimestreReal) {
        this.programaTercerTrimestreReal = programaTercerTrimestreReal;
    }

    public Integer getProgramaCuartoTrimestreReal() {
        return programaCuartoTrimestreReal;
    }

    public void setProgramaCuartoTrimestreReal(Integer programaCuartoTrimestreReal) {
        this.programaCuartoTrimestreReal = programaCuartoTrimestreReal;
    }

    public String getAlcanceLimitaciones() {
        return alcanceLimitaciones;
    }

    public void setAlcanceLimitaciones(String alcanceLimitaciones) {
        this.alcanceLimitaciones = alcanceLimitaciones;
    }

    public String getMedioVerificacion() {
        return medioVerificacion;
    }

    public void setMedioVerificacion(String medioVerificacion) {
        this.medioVerificacion = medioVerificacion;
    }

    public Date getUltFechaModSeguimiento() {
        return ultFechaModSeguimiento;
    }

    public void setUltFechaModSeguimiento(Date ultFechaModSeguimiento) {
        this.ultFechaModSeguimiento = ultFechaModSeguimiento;
    }

    public Date getUltFechaModSeguimientoPrimerTrimestre() {
        return ultFechaModSeguimientoPrimerTrimestre;
    }

    public void setUltFechaModSeguimientoPrimerTrimestre(Date ultFechaModSeguimientoPrimerTrimestre) {
        this.ultFechaModSeguimientoPrimerTrimestre = ultFechaModSeguimientoPrimerTrimestre;
    }

    public Date getUltFechaModSeguimientoSegundoTrimestre() {
        return ultFechaModSeguimientoSegundoTrimestre;
    }

    public void setUltFechaModSeguimientoSegundoTrimestre(Date ultFechaModSeguimientoSegundoTrimestre) {
        this.ultFechaModSeguimientoSegundoTrimestre = ultFechaModSeguimientoSegundoTrimestre;
    }

    public Date getUltFechaModSeguimientoTercerTrimestre() {
        return ultFechaModSeguimientoTercerTrimestre;
    }

    public void setUltFechaModSeguimientoTercerTrimestre(Date ultFechaModSeguimientoTercerTrimestre) {
        this.ultFechaModSeguimientoTercerTrimestre = ultFechaModSeguimientoTercerTrimestre;
    }

    public Date getUltFechaModSeguimientoCuartoTrimestre() {
        return ultFechaModSeguimientoCuartoTrimestre;
    }

    public void setUltFechaModSeguimientoCuartoTrimestre(Date ultFechaModSeguimientoCuartoTrimestre) {
        this.ultFechaModSeguimientoCuartoTrimestre = ultFechaModSeguimientoCuartoTrimestre;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalReal() {
        return totalReal;
    }

    public void setTotalReal(Integer totalReal) {
        this.totalReal = totalReal;
    }

    
    
    @PostLoad
    private void cargarDatos() {
        total = 0;
        total= programaPrimerTrimestre != null ? programaPrimerTrimestre : 0;
        total= programaSegundoTrimestre != null ? (total + programaSegundoTrimestre) : total;
        total= programaTercerTrimestre != null ? (total + programaTercerTrimestre) : total;
        total= programaCuartoTrimestre != null ? (total + programaCuartoTrimestre) : total;
        
        totalReal = 0;
        totalReal= programaPrimerTrimestreReal != null ? programaPrimerTrimestreReal : 0;
        totalReal= programaSegundoTrimestreReal != null ? (totalReal +programaSegundoTrimestreReal) : totalReal;
        totalReal= programaTercerTrimestreReal != null ? (totalReal +programaTercerTrimestreReal) : totalReal;
        totalReal= programaCuartoTrimestreReal != null ? (totalReal +programaCuartoTrimestreReal) : totalReal;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final POAMetaAnualLite other = (POAMetaAnualLite) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
}
