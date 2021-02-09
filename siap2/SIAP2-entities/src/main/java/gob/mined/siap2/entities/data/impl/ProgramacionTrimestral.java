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
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_poa_programacion_trimestral")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProgramacionTrimestral implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "programacion_trimestral_seq_gen")
    @SequenceGenerator(name = "programacion_trimestral_seq_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_poa_programacion_trimestral", allocationSize = 1)
    @Column(name = "ppt_id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "ppt_anio")
    private AnioFiscal anio;
    
    @Column(name = "ppt_fecha_desde_t1")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaDesdePrimerTrimestre;
    
    @Column(name = "ppt_fecha_hasta_t1")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaHastaPrimerTrimestre;
    
    
    @Column(name = "ppt_fecha_desde_t2")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaDesdeSegundoTrimestre;
    
    @Column(name = "ppt_fecha_hasta_t2")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaHastaSegundoTrimestre;
    
    @Column(name = "ppt_fecha_desde_t3")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaDesdeTercerTrimestre;
    
    @Column(name = "ppt_fecha_hasta_t3")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaHastaTercerTrimestre;
    
    @Column(name = "ppt_fecha_desde_t4")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaDesdeCuartoTrimestre;
    
    @Column(name = "ppt_fecha_hasta_t4")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaHastaCuartoTrimestre;
    
    //Auditoria
    @Column(name = "ppt_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "ppt_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "ppt_version")
    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AnioFiscal getAnio() {
        return anio;
    }

    public void setAnio(AnioFiscal anio) {
        this.anio = anio;
    }

    public Date getFechaDesdePrimerTrimestre() {
        return fechaDesdePrimerTrimestre;
    }

    public void setFechaDesdePrimerTrimestre(Date fechaDesdePrimerTrimestre) {
        this.fechaDesdePrimerTrimestre = fechaDesdePrimerTrimestre;
    }

    public Date getFechaHastaPrimerTrimestre() {
        return fechaHastaPrimerTrimestre;
    }

    public void setFechaHastaPrimerTrimestre(Date fechaHastaPrimerTrimestre) {
        this.fechaHastaPrimerTrimestre = fechaHastaPrimerTrimestre;
    }

    public Date getFechaDesdeSegundoTrimestre() {
        return fechaDesdeSegundoTrimestre;
    }

    public void setFechaDesdeSegundoTrimestre(Date fechaDesdeSegundoTrimestre) {
        this.fechaDesdeSegundoTrimestre = fechaDesdeSegundoTrimestre;
    }

    public Date getFechaHastaSegundoTrimestre() {
        return fechaHastaSegundoTrimestre;
    }

    public void setFechaHastaSegundoTrimestre(Date fechaHastaSegundoTrimestre) {
        this.fechaHastaSegundoTrimestre = fechaHastaSegundoTrimestre;
    }

    public Date getFechaDesdeTercerTrimestre() {
        return fechaDesdeTercerTrimestre;
    }

    public void setFechaDesdeTercerTrimestre(Date fechaDesdeTercerTrimestre) {
        this.fechaDesdeTercerTrimestre = fechaDesdeTercerTrimestre;
    }

    public Date getFechaHastaTercerTrimestre() {
        return fechaHastaTercerTrimestre;
    }

    public void setFechaHastaTercerTrimestre(Date fechaHastaTercerTrimestre) {
        this.fechaHastaTercerTrimestre = fechaHastaTercerTrimestre;
    }

    public Date getFechaDesdeCuartoTrimestre() {
        return fechaDesdeCuartoTrimestre;
    }

    public void setFechaDesdeCuartoTrimestre(Date fechaDesdeCuartoTrimestre) {
        this.fechaDesdeCuartoTrimestre = fechaDesdeCuartoTrimestre;
    }

    public Date getFechaHastaCuartoTrimestre() {
        return fechaHastaCuartoTrimestre;
    }

    public void setFechaHastaCuartoTrimestre(Date fechaHastaCuartoTrimestre) {
        this.fechaHastaCuartoTrimestre = fechaHastaCuartoTrimestre;
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
        final ProgramacionTrimestral other = (ProgramacionTrimestral) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
