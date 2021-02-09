/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.entities;

import com.sofis.persistence.anotaciones.AtributoUltimaModificacion;
import com.sofis.persistence.anotaciones.AtributoUltimoUsuario;
import com.sofis.persistence.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.hibernate.envers.Audited;
import uy.com.sofis.pfea.constantes.ConstantesConfiguracion;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@EntityListeners({DecoratorEntityListener.class})
@Audited
@Table(name = "ss_textos", schema = ConstantesConfiguracion.SCHEMA_PFEA)
public class Texto implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_textos_gen")
    @SequenceGenerator(name = "seq_textos_gen", schema = ConstantesConfiguracion.SCHEMA_PFEA, sequenceName = "seq_textos", allocationSize = 1)
    @Column(name = "tex_id")
    private Integer texId;
    
    @Column(name = "tex_codigo")
    @Basic(optional = false)
    private String texCodigo;

    @Column(name = "tex_valor")
    private String texValor;
    
    //-- Datos de auditoría -------------------------
    @Column(name = "tex_ultimo_usuario")
    @AtributoUltimoUsuario
    private String ultimoUsuario;
    @Column(name = "tex_ultima_modificacion")
    @Temporal(javax.persistence.TemporalType.DATE)
    @AtributoUltimaModificacion
    private Date ultimaModificacion;
    @Column(name = "tex_version")
    @Version
    private Integer version;
    //-- Fin datos de auditoría ---------------------
    
    // <editor-fold defaultstate="collapsed" desc="Setters && Getters">
    public Integer getTexId() {
        return texId;
    }

    public void setTexId(Integer id) {
        this.texId = id;
    }
    
    public Integer getId(){
        return this.getTexId();
    }

    public String getUltimoUsuario() {
        return ultimoUsuario;
    }

    public void setUltimoUsuario(String ultimoUsuario) {
        this.ultimoUsuario = ultimoUsuario;
    }

    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public String getTexCodigo() {
        return texCodigo;
    }

    public void setTexCodigo(String texCodigo) {
        this.texCodigo = texCodigo;
    }

    public String getTexValor() {
        return texValor;
    }

    public void setTexValor(String texValor) {
        this.texValor = texValor;
    }
 
    // </editor-fold>


    
    
}
