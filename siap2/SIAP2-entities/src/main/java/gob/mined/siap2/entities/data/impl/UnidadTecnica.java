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
import java.math.BigDecimal;
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
import javax.persistence.Cacheable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_unidad_tecnica")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
@Cacheable(false)
public class UnidadTecnica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_unidad_tec_gen")
    @SequenceGenerator(name = "seq_unidad_tec_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_unidad_tec", allocationSize = 1)
    @Column(name = "uni_id")
    private Integer id;

    @Column(name = "uni_nombre")
    private String nombre;

    @Column(name = "uni_nombre_busqueda")
    private String nombreBusqueda;
    
    @Column(name = "uni_direccion")
    private Boolean direccion;
    
    @Column(name = "uni_operativa")
    private Boolean unidadOperativa;
    
    @ManyToOne
    @JoinColumn(name = "uni_padre")
    private UnidadTecnica padre;
    
    @Column(name = "uni_codigo", unique = true, length = 20)
    private String codigo;
    
    @JoinColumn(name = "uni_usuario", referencedColumnName = "usu_id", nullable = false)
    @ManyToOne(optional = false)
    private SsUsuario uniUsuario;
    
    
    @Column(name = "uni_lista_padres")
    private String listaUTinenAcceso;
    

    //Auditoria
    @Column(name = "uni_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "uni_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "uni_version")
    @Version
    private Integer version;

    @Transient
    private Object any;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDireccion() {
        return direccion;
    }

    public void setDireccion(Boolean direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public UnidadTecnica getPadre() {
        return padre;
    }

    public void setPadre(UnidadTecnica padre) {
        this.padre = padre;
    }

    public Object getAny() {
        return any;
    }

    public void setAny(Object any) {
        this.any = any;
    }

    public String getListaUTinenAcceso() {
        return listaUTinenAcceso;
    }

    public void setListaUTinenAcceso(String listaUTinenAcceso) {
        this.listaUTinenAcceso = listaUTinenAcceso;
    }
    

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public SsUsuario getUniUsuario() {
        return uniUsuario;
    }

    public void setUniUsuario(SsUsuario uniUsuario) {
        this.uniUsuario = uniUsuario;
    }

    public Boolean getUnidadOperativa() {
        return unidadOperativa;
    }

    public void setUnidadOperativa(Boolean unidadOperativa) {
        this.unidadOperativa = unidadOperativa;
    } 

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }

    // </editor-fold>
    
    @PrePersist
    @PreUpdate
    public void beforeSave() {
        this.nombreBusqueda = this.nombre != null ? this.nombre.toLowerCase() : "";
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final UnidadTecnica other = (UnidadTecnica) obj;
        if ((this.id!= null)&&( other.id!=null)) {
            return Objects.equals(this.id, other.id);
        }
        return (this== other);
    }

    public String toString() {
        String respuesta = "";
        if (nombre != null) {
            respuesta = nombre;
        }
        if (any != null) {
            if (any instanceof BigDecimal) {
                respuesta = respuesta + " $" + any;
            } else {
                respuesta = respuesta + " " + any;
            }
        }
        if (respuesta.length() == 0 && id != null) {
            respuesta = id + "";
        }
        return respuesta;
    }

}
