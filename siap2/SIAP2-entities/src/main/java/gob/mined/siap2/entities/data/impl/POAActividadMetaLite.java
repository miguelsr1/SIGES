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
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_poa_actividades_metas")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POAActividadMetaLite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poa_actividades_metas_gen")
    @SequenceGenerator(name = "poa_actividades_metas_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".poa_actividades_metas_seq", allocationSize = 1)
    @Column(name = "pam_id")
    private Integer id;
    
    @Size(max = 500)
    @Column(name = "pam_nombre", length = 500)
    private String nombre;
    
    @Column(name = "pam_periodo_ejecucion_desde")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date periodoEjecusionDesde;
    
    @Column(name = "pam_periodo_ejecucion_hasta")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date periodoEjecusionHasta;
    
    @Size(max = 50)
    @Column(name = "pam_funcionario_responsable", length = 50)
    private String funcionarioResponsable;
    
    @ManyToOne
    @JoinColumn(name = "pam_unidad_tecnica_responsable")
    private UnidadTecnica unidadTecnicaResponsable;
    
    @ManyToOne
    @JoinColumn(name = "pam_meta_anual")
    private POAMetaAnual meta;
    
    @Column(name = "pam_porcentaje_avance_t1")
    private Integer porcentajeAvancePrimerTrimestre = 0;
    
    @Column(name = "pam_porcentaje_avance_t2")
    private Integer porcentajeAvanceSegundoTrimestre = 0;
    
    @Column(name = "pam_porcentaje_avance_t3")
    private Integer porcentajeAvanceTercerTrimestre = 0;
    
    @Column(name = "pam_porcentaje_avance_t4")
    private Integer porcentajeAvanceCuartoTrimestre = 0;
    
    
    @Column(name = "pam_ult_peri_modificado")
    private Integer ultimoPeriodoModificado = 0;
    
    
     //Auditoria
    @Column(name = "pam_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pam_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pam_version")
    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getPeriodoEjecusionDesde() {
        return periodoEjecusionDesde;
    }

    public void setPeriodoEjecusionDesde(Date periodoEjecusionDesde) {
        this.periodoEjecusionDesde = periodoEjecusionDesde;
    }

    public Date getPeriodoEjecusionHasta() {
        return periodoEjecusionHasta;
    }

    public void setPeriodoEjecusionHasta(Date periodoEjecusionHasta) {
        this.periodoEjecusionHasta = periodoEjecusionHasta;
    }

    public UnidadTecnica getUnidadTecnicaResponsable() {
        return unidadTecnicaResponsable;
    }

    public void setUnidadTecnicaResponsable(UnidadTecnica unidadTecnicaResponsable) {
        this.unidadTecnicaResponsable = unidadTecnicaResponsable;
    }

    public POAMetaAnual getMeta() {
        return meta;
    }

    public void setMeta(POAMetaAnual meta) {
        this.meta = meta;
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

    public String getFuncionarioResponsable() {
        return funcionarioResponsable;
    }

    public void setFuncionarioResponsable(String funcionarioResponsable) {
        this.funcionarioResponsable = funcionarioResponsable;
    }

    public Integer getPorcentajeAvancePrimerTrimestre() {
        return porcentajeAvancePrimerTrimestre;
    }

    public void setPorcentajeAvancePrimerTrimestre(Integer porcentajeAvancePrimerTrimestre) {
        this.porcentajeAvancePrimerTrimestre = porcentajeAvancePrimerTrimestre;
    }

    public Integer getPorcentajeAvanceSegundoTrimestre() {
        return porcentajeAvanceSegundoTrimestre;
    }

    public void setPorcentajeAvanceSegundoTrimestre(Integer porcentajeAvanceSegundoTrimestre) {
        this.porcentajeAvanceSegundoTrimestre = porcentajeAvanceSegundoTrimestre;
    }

    public Integer getPorcentajeAvanceTercerTrimestre() {
        return porcentajeAvanceTercerTrimestre;
    }

    public void setPorcentajeAvanceTercerTrimestre(Integer porcentajeAvanceTercerTrimestre) {
        this.porcentajeAvanceTercerTrimestre = porcentajeAvanceTercerTrimestre;
    }

    public Integer getPorcentajeAvanceCuartoTrimestre() {
        return porcentajeAvanceCuartoTrimestre;
    }

    public void setPorcentajeAvanceCuartoTrimestre(Integer porcentajeAvanceCuartoTrimestre) {
        this.porcentajeAvanceCuartoTrimestre = porcentajeAvanceCuartoTrimestre;
    }

    public Integer getUltimoPeriodoModificado() {
        return ultimoPeriodoModificado;
    }

    public void setUltimoPeriodoModificado(Integer ultimoPeriodoModificado) {
        this.ultimoPeriodoModificado = ultimoPeriodoModificado;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final POAActividadMetaLite other = (POAActividadMetaLite) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}
