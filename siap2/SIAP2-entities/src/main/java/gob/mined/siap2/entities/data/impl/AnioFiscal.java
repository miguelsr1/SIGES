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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_anio_fiscal")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class AnioFiscal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_anio_fiscal_gen")
    @SequenceGenerator(name = "seq_anio_fiscal_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_anio_fiscal", allocationSize = 1)
    @Column(name = "ani_id")
    protected Integer id;
    
    @Column(name = "ani_nombre")
    private String nombre;
    
    @Column(name = "ani_anio", unique=true)
    private Integer anio;

    @Column(name = "ani_desde")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date desde;

    @Column(name = "ani_hasta")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date hasta;

    @Column(name = "ani_planificacion")
    private Boolean habilitadoPlanificacion;
        
    @Column(name = "ani_ejecucion")
    private Boolean habilitadoEjecucion;
    
    @Column(name = "ani_cerrado")
    private Boolean cerrado;    
    
    @Column(name = "ani_formulacion_ce")
    private Boolean formulacionCe;    
    
    @Column(name = "ani_ajuste_ce")
    private Boolean ajusteCe;
    
    //Auditoria
    @Column(name = "ani_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "ani_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "ani_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public Boolean getHabilitadoPlanificacion() {
        return habilitadoPlanificacion;
    }

    public void setHabilitadoPlanificacion(Boolean habilitadoPlanificacion) {
        this.habilitadoPlanificacion = habilitadoPlanificacion;
    }

    public Boolean getHabilitadoEjecucion() {
        return habilitadoEjecucion;
    }

    public void setHabilitadoEjecucion(Boolean habilitadoEjecucion) {
        this.habilitadoEjecucion = habilitadoEjecucion;
    }

  

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    public void setAnio(Integer anio) {
        this.anio = anio;
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

    public Boolean getCerrado() {
        return cerrado;
    }

    public void setCerrado(Boolean cerrado) {
        this.cerrado = cerrado;
    }

    public Boolean getFormulacionCe() {
        return formulacionCe;
    }

    public void setFormulacionCe(Boolean formulacionCe) {
        this.formulacionCe = formulacionCe;
    }

    public Boolean getAjusteCe() {
        return ajusteCe;
    }

    public void setAjusteCe(Boolean ajusteCe) {
        this.ajusteCe = ajusteCe;
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
        final AnioFiscal other = (AnioFiscal) obj;
        if ((this.id != null) && (other.id!=null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }

    @Override
    public String toString() {
        return "AnioFiscal{" + "id=" + id + ", version=" + version + '}';
    }

}
