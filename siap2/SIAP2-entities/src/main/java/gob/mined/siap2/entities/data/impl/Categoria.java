/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import com.mined.siap2.interfaces.BaseCodiguera;
import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_categoria")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Categoria implements BaseCodiguera ,Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_categoria_gen")
    @SequenceGenerator(name = "seq_categoria_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_categoria", allocationSize = 1)
    @Column(name = "cat_id")
    private Integer id;

    @Column(name = "cat_habilitado")
    private Boolean habilitado=Boolean.TRUE;
    
    @Column(name = "cat_orden")
    private Integer orden;
    
    @Column(name = "cat_codigo", nullable = false, unique=true)
    private String codigo;
    
    @Column(name = "cat_nombre", nullable = false)
    private String nombre;
        
    @Column(name = "cat_es_prod")
    private Boolean esProducto;
    
    
    @Enumerated(EnumType.STRING)
    @Column(name = "cat_tipo_seguimietno")
    private TipoSeguimiento tipoSeguimiento;

    //Auditoria
    @Column(name = "cat_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "cat_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "cat_version")
    @Version
    private Integer version;

    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
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

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public TipoSeguimiento getTipoSeguimiento() {
        return tipoSeguimiento;
    }

    public void setTipoSeguimiento(TipoSeguimiento tipoSeguimiento) {
        this.tipoSeguimiento = tipoSeguimiento;
    }

    
    
    public String getCodigo() {
        return codigo;
    }

    public Boolean getEsProducto() {
        return esProducto;
    }

    public void setEsProducto(Boolean esProducto) {
        this.esProducto = esProducto;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        final Categoria other = (Categoria) obj;
        if (this.id!= null && other.id!= null) {
            return Objects.equals(this.id, other.id);
        }
        return (this==other);
    }

}
