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
@Table(name = "ss_configuraciones", schema = ConstantesConfiguracion.SCHEMA_PFEA)
public class Configuracion implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_configuraciones_gen")
    @SequenceGenerator(name = "seq_configuraciones_gen", schema = ConstantesConfiguracion.SCHEMA_PFEA, sequenceName = "seq_configuraciones", allocationSize = 1)
    @Column(name = "cnf_id")
    private Integer cnfId;
    
    @Column(name = "cnf_codigo")
    @Basic(optional = false)
    private String cnfCodigo;
    
    @Column(name = "cnf_descripcion")
    @Basic(optional = false)
    private String cnfDescripcion;

    @Column(name = "cnf_valor")
    private String cnfValor;
    
    //-- Datos de auditoría -------------------------
    @Column(name = "cnf_ultimo_usuario")
    @AtributoUltimoUsuario
    private String ultimoUsuario;
    @Column(name = "cnf_ultima_modificacion")
    @Temporal(javax.persistence.TemporalType.DATE)
    @AtributoUltimaModificacion
    private Date ultimaModificacion;
    @Column(name = "cnf_version")
    @Version
    private Integer version;
    //-- Fin datos de auditoría ---------------------
    
    // <editor-fold defaultstate="collapsed" desc="Setters && Getters">

    public Integer getCnfId() {
        return cnfId;
    }

    public void setCnfId(Integer cnfId) {
        this.cnfId = cnfId;
    }
    
    public Integer getId(){
        return this.getCnfId();
    }

    public String getCnfCodigo() {
        return cnfCodigo;
    }

    public void setCnfCodigo(String cnfCodigo) {
        this.cnfCodigo = cnfCodigo;
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
    
    public String getCnfDescripcion() {
        return cnfDescripcion;
    }

    public void setCnfDescripcion(String cnfDescripcion) {
        this.cnfDescripcion = cnfDescripcion;
    }

    public String getCnfValor() {
        return cnfValor;
    }

    public void setCnfValor(String cnfValor) {
        this.cnfValor = cnfValor;
    }
    // </editor-fold>
    
    
}
