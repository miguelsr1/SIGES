package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
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
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author sofis
 */


@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_inf_pagado")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class InfPagado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inf_pagado_gen")
    @SequenceGenerator(name = "seq_inf_pagado_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_inf_pagado", allocationSize = 1)
    @Column(name = "inf_id")
    private Integer id;
    
    @Column(name = "inf_ejercicio")
    private String ejercicio;
    
    @Column(name = "inf_nit")
    private String nit;
    
    @Column(name = "inf_nombre")
    private String nombre;
    
    @Column(name = "inf_nro_docu_orig")
    private String nroDocuOrig;
   
    @Column(name = "inf_item_pago")
    private String itemPago;
    
    @Column(name = "inf_proyecto")
    private String proyecto;
    
    @Column(name = "inf_up_lt")
    private String upLt;
    
    @Column(name = "inf_ao")
    private String ao;    
    
    @Column(name = "inf_fr")
    private String fr;
    
    @Column(name = "inf_ff")
    private String ff;
    
    @Column(name = "inf_mes_pagado")
    private String mesPagado;
    
    @Column(name = "inf_pagado")
    private BigDecimal pagado;
    
    @Column(name = "inf_concepto")
    private String concepto;
    
    @Column(name = "inf_otra_1")
    private String otra1;
    
    @Column(name = "inf_otra_2")
    private String otra2;
    
    @Column(name = "inf_clave_join")
    private String claveJoin;
    
    @JoinColumn(name = "inf_compromiso")
    @ManyToOne
    private InfCompromiso compromiso;  
    
    //Auditoria
    @Column(name = "inf_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "inf_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "inf_version")
    @Version
    private Integer version;
    

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClaveJoin() {
        return claveJoin;
    }

    public void setClaveJoin(String claveJoin) {
        this.claveJoin = claveJoin;
    }
    
    

    public InfCompromiso getCompromiso() {
        return compromiso;
    }

    public void setCompromiso(InfCompromiso compromiso) {
        this.compromiso = compromiso;
    }
    

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNroDocuOrig() {
        return nroDocuOrig;
    }

    public void setNroDocuOrig(String nroDocuOrig) {
        this.nroDocuOrig = nroDocuOrig;
    }

    public String getItemPago() {
        return itemPago;
    }

    public void setItemPago(String itemPago) {
        this.itemPago = itemPago;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getUpLt() {
        return upLt;
    }

    public void setUpLt(String upLt) {
        this.upLt = upLt;
    }

    public String getAo() {
        return ao;
    }

    public void setAo(String ao) {
        this.ao = ao;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getFf() {
        return ff;
    }

    public void setFf(String ff) {
        this.ff = ff;
    }

    public String getMesPagado() {
        return mesPagado;
    }

    public void setMesPagado(String mesPagado) {
        this.mesPagado = mesPagado;
    }

    public BigDecimal getPagado() {
        return pagado;
    }

    public void setPagado(BigDecimal pagado) {
        this.pagado = pagado;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getOtra1() {
        return otra1;
    }

    public void setOtra1(String otra1) {
        this.otra1 = otra1;
    }

    public String getOtra2() {
        return otra2;
    }

    public void setOtra2(String otra2) {
        this.otra2 = otra2;
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
        final InfPagado other = (InfPagado) obj;
        if (this.id!=null && other.id!=null) {
            return Objects.equals(this.id, other.id);
        }
        return (this== other);
    }    
    
}
