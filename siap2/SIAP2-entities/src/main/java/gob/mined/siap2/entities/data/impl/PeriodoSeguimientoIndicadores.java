/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
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
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_periodo_seg_ind")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class PeriodoSeguimientoIndicadores implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ss_periodo_seg_ind_gen")
    @SequenceGenerator(name = "seq_ss_periodo_seg_ind_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_ss_periodo_seg_ind", allocationSize = 1)
    @Column(name = "per_id")
    protected Integer id;
    
    @ManyToOne
    @JoinColumn(name = "per_anio_fiscal_id")
    protected AnioFiscal anioFiscal;
    
    @Column(name = "per_desde")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date desde;

    @Column(name = "per_hasta")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date hasta;

    @Column(name = "per_mensual")
    private Boolean habilitadoMensual;
    @Column(name = "per_posicion_mensual")
    private Integer posicionHabilitadaMensual;

    @Column(name = "per_trimestral")
    private Boolean habilitadoTrimestral;
    @Column(name = "per_posicion_trimestral")
    private Integer posicionHabilitadaTrimestral;

    @Column(name = "per_cuatrimestral")
    private Boolean habilitadoCuatrimestral;
    @Column(name = "per_posicion_cuatrimestral")
    private Integer posicionHabilitadaCuatrimestral;

    @Column(name = "per_semestral")
    private Boolean habilitadoSemestral;
    @Column(name = "per_posicion_semestral")
    private Integer posicionHabilitadaSemestral;

    @Column(name = "per_anual")
    private Boolean habilitadoAnual;
    
    @Column(name = "per_aplica_pro_inv")
    private Boolean aplicaProyectosDeInversion;
    
    @Column(name = "per_aplica_pro_admin")
    private Boolean aplicaProyectosAdministrarivos;
    
    //Auditoria
    @Column(name = "per_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "per_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "per_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getHabilitadoMensual() {
        return habilitadoMensual;
    }

    public Boolean getAplicaProyectosDeInversion() {
        return aplicaProyectosDeInversion;
    }

    public void setAplicaProyectosDeInversion(Boolean aplicaProyectosDeInversion) {
        this.aplicaProyectosDeInversion = aplicaProyectosDeInversion;
    }

    public Boolean getAplicaProyectosAdministrarivos() {
        return aplicaProyectosAdministrarivos;
    }

    public void setAplicaProyectosAdministrarivos(Boolean aplicaProyectosAdministrarivos) {
        this.aplicaProyectosAdministrarivos = aplicaProyectosAdministrarivos;
    }

    public void setHabilitadoMensual(Boolean habilitadoMensual) {
        this.habilitadoMensual = habilitadoMensual;
    }

    public Integer getPosicionHabilitadaMensual() {
        return posicionHabilitadaMensual;
    }

    public void setPosicionHabilitadaMensual(Integer posicionHabilitadaMensual) {
        this.posicionHabilitadaMensual = posicionHabilitadaMensual;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }
    

    public Boolean getHabilitadoTrimestral() {
        return habilitadoTrimestral;
    }

    public void setHabilitadoTrimestral(Boolean habilitadoTrimestral) {
        this.habilitadoTrimestral = habilitadoTrimestral;
    }

    public Integer getPosicionHabilitadaTrimestral() {
        return posicionHabilitadaTrimestral;
    }

    public void setPosicionHabilitadaTrimestral(Integer posicionHabilitadaTrimestral) {
        this.posicionHabilitadaTrimestral = posicionHabilitadaTrimestral;
    }

    public Boolean getHabilitadoCuatrimestral() {
        return habilitadoCuatrimestral;
    }

    public void setHabilitadoCuatrimestral(Boolean habilitadoCuatrimestral) {
        this.habilitadoCuatrimestral = habilitadoCuatrimestral;
    }

    public Integer getPosicionHabilitadaCuatrimestral() {
        return posicionHabilitadaCuatrimestral;
    }

    public void setPosicionHabilitadaCuatrimestral(Integer posicionHabilitadaCuatrimestral) {
        this.posicionHabilitadaCuatrimestral = posicionHabilitadaCuatrimestral;
    }

    public Boolean getHabilitadoSemestral() {
        return habilitadoSemestral;
    }

    public void setHabilitadoSemestral(Boolean habilitadoSemestral) {
        this.habilitadoSemestral = habilitadoSemestral;
    }

    public Integer getPosicionHabilitadaSemestral() {
        return posicionHabilitadaSemestral;
    }

    public void setPosicionHabilitadaSemestral(Integer posicionHabilitadaSemestral) {
        this.posicionHabilitadaSemestral = posicionHabilitadaSemestral;
    }

    public Boolean getHabilitadoAnual() {
        return habilitadoAnual;
    }

    public void setHabilitadoAnual(Boolean habilitadoAnual) {
        this.habilitadoAnual = habilitadoAnual;
    }


    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
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

   

    // </editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PeriodoSeguimientoIndicadores other = (PeriodoSeguimientoIndicadores) obj;
        if ((this.id != null) && (other.id!=null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }

}
