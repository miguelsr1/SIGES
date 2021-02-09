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
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import gob.mined.siges.entities.data.impl.SgModalidad;
import gob.mined.siges.entities.data.impl.SgModalidadAtencion;
import gob.mined.siges.entities.data.impl.SgNivel;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_tope_detalle_matriculas")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class TopeDetalleMatriculas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tope_detalle_matriculas_gen")
    @SequenceGenerator(name = "tope_detalle_matriculas_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_tope_detalle_matriculas", allocationSize = 1)
    @Column(name = "tdm_id")
    private Integer id;
    
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "tdm_tope")
    private TopePresupuestal topePresupuestal;
     
    @ManyToOne
    @JoinColumn(name = "tdm_nivel")
    private SgNivel nivel;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "tdm_mod_educativa")
    private SgModalidad modalidadEducativa;
    
    @ManyToOne
    @JoinColumn(name = "tdm_mod_atencion")
    private SgModalidadAtencion modalidadAtencion;
    
    @Column(name = "tdm_cant_matriculas")
    private Integer cantidadMatriculas;
    
    @Column(name = "tdm_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "tdm_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "tdm_version")
    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TopePresupuestal getTopePresupuestal() {
        return topePresupuestal;
    }

    public void setTopePresupuestal(TopePresupuestal topePresupuestal) {
        this.topePresupuestal = topePresupuestal;
    }

    public SgNivel getNivel() {
        return nivel;
    }

    public void setNivel(SgNivel nivel) {
        this.nivel = nivel;
    }

    public SgModalidad getModalidadEducativa() {
        return modalidadEducativa;
    }

    public void setModalidadEducativa(SgModalidad modalidadEducativa) {
        this.modalidadEducativa = modalidadEducativa;
    }

    public SgModalidadAtencion getModalidadAtencion() {
        return modalidadAtencion;
    }

    public void setModalidadAtencion(SgModalidadAtencion modalidadAtencion) {
        this.modalidadAtencion = modalidadAtencion;
    }

    public Integer getCantidadMatriculas() {
        return cantidadMatriculas;
    }

    public void setCantidadMatriculas(Integer cantidadMatriculas) {
        this.cantidadMatriculas = cantidadMatriculas;
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
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
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
        final TopeDetalleMatriculas other = (TopeDetalleMatriculas) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}
