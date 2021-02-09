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
import javax.persistence.OneToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_cmp_re_exp_pagos")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ComprobanteDeRecepcionDeExpedienteDePago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cmp_re_exp_pago_gen")
    @SequenceGenerator(name = "seq_cmp_re_exp_pago_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_cmp_re_exp_pago", allocationSize = 1)
    @Column(name = "cmp_id")
    protected Integer id;
    
        
    @OneToOne
    @JoinColumn(name = "cmp_archivo")
    private Archivo archivo;
    
        
    @OneToOne
    @JoinColumn(name = "cmp_acta")
    private ActaContrato acta;
    
    
    @Column(name = "cmp_fecha_generacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date fechaGeneracion;

    
    
    
    //Auditoria
    @Column(name = "cmp_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "cmp_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "cmp_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    public ActaContrato getActa() {
        return acta;
    }

    public void setActa(ActaContrato acta) {
        this.acta = acta;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
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
        final ComprobanteDeRecepcionDeExpedienteDePago other = (ComprobanteDeRecepcionDeExpedienteDePago) obj;
        if ((this.id != null) && (other.id!=null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }

}
