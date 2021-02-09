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
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_notificaciones")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "not_tipo", discriminatorType = DiscriminatorType.STRING, length = 40)
@DiscriminatorValue(value = "GENERAL")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Notificacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notificaciones_gen")
    @SequenceGenerator(name = "seq_notificaciones_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_notificaciones", allocationSize = 1)
    @Column(name = "not_id")
    private Integer id;

    //este tipo es distinto al de la clase
    @Column(name = "not_tipo_n")
    @Enumerated(EnumType.STRING)
    private TipoNotificacion tipo;

    @ManyToOne
    @JoinColumn(name = "not_usuario")
    private SsUsuario usuario;

    @Column(name = "not_contenido")
    private String contenido;

    @Column(name = "not_fecha")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    
    @Column(name = "poa_objeto_id")
    private Integer objetoId;
    
        
    @Column(name = "not_objeto_2_id")
    private Integer objeto2Id;
    
    @ManyToOne
    @JoinColumn(name = "not_asignado_a", referencedColumnName = "usuId")
    private SsUsuario notAsignadoA;
    
    //Auditoria
    @Column(name = "not_ult_usuario")
    @AtributoUltimoUsuario
    private String notUsuario;

    @Column(name = "not_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date notMod;

    @Column(name = "not_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getNotUsuario() {
        return notUsuario;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public SsUsuario getUsuario() {
        return usuario;
    }

    public Integer getObjeto2Id() {
        return objeto2Id;
    }

    public void setObjeto2Id(Integer objeto2Id) {
        this.objeto2Id = objeto2Id;
    }
    

    public void setUsuario(SsUsuario usuario) {
        this.usuario = usuario;
    }

    public void setNotUsuario(String notUsuario) {
        this.notUsuario = notUsuario;
    }

    public Date getNotMod() {
        return notMod;
    }

    public void setNotMod(Date notMod) {
        this.notMod = notMod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    
    public Integer getObjetoId() {
        return objetoId;
    }

    public void setObjetoId(Integer objetoId) {
        this.objetoId = objetoId;
    }

    public SsUsuario getNotAsignadoA() {
        return notAsignadoA;
    }

    public void setNotAsignadoA(SsUsuario notAsignadoA) {
        this.notAsignadoA = notAsignadoA;
    }

     
    
    
    
    
    // </editor-fold>
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final Notificacion other = (Notificacion) obj;
        return true;
    }

}
