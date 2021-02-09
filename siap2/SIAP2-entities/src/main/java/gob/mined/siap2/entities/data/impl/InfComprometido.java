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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_inf_comprometido")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class InfComprometido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inf_comprometido_gen")
    @SequenceGenerator(name = "seq_inf_comprometido_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_inf_comprometido", allocationSize = 1)
    @Column(name = "inf_id")
    private Integer id;
    
    @Column(name = "inf_anio")
    private String anio;
    
    @Column(name = "inf_nro_comprobante")
    private String nroComprobante;
    
    @Column(name = "inf_item")
    private String item;
    
    @Column(name = "inf_fecha_elaborac")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaElaborac ;
    
    @Column(name = "inf_nro_d_resp")
    private String nroDResp;
    
    @Column(name = "inf_nit")
    private String nit;
    
    @Column(name = "inf_proveedor")
    private String proveedor;
    
    @Column(name = "inf_lt")
    private String lt;
    
    @Column(name = "inf_ff")
    private String ff;    
    
    @Column(name = "inf_proyecto")
    private String proyecto;    
    
    @Column(name = "inf_fr")
    private String fr;    
    
    @Column(name = "inf_ao")
    private String ao;    
    
    @Column(name = "inf_espec")
    private String espec;    
    
    @Column(name = "inf_mes")
    private String mes;    

    @Column(name = "inf_comprometido")
    private BigDecimal comprometido;
    
    @Column(name = "inf_descomp")
    private BigDecimal descomp;
    
    @Column(name = "inf_congelado")
    private BigDecimal congelado;
    
    @Column(name = "inf_disponible")
    private BigDecimal disponible;
    
    @Column(name = "inf_concepto")
    private String concepto;
    
    @JoinColumn(name = "inf_compromiso")
    @ManyToOne
    private InfCompromiso compromiso;    
    
    @Column(name = "inf_clave_join")
    private String claveJoin;
    
    
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
    

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getNroComprobante() {
        return nroComprobante;
    }

    public void setNroComprobante(String nroComprobante) {
        this.nroComprobante = nroComprobante;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Date getFechaElaborac() {
        return fechaElaborac;
    }

    public void setFechaElaborac(Date fechaElaborac) {
        this.fechaElaborac = fechaElaborac;
    }

    public String getNroDResp() {
        return nroDResp;
    }

    public void setNroDResp(String nroDResp) {
        this.nroDResp = nroDResp;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }
    
    public String getEscapeLt(){
        return lt;
    }

    public String getFf() {
        return ff;
    }

    public void setFf(String ff) {
        this.ff = ff;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getAo() {
        return ao;
    }

    public void setAo(String ao) {
        this.ao = ao;
    }

    public String getEspec() {
        return espec;
    }

    public void setEspec(String espec) {
        this.espec = espec;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public BigDecimal getDescomp() {
        return descomp;
    }

    public void setDescomp(BigDecimal descomp) {
        this.descomp = descomp;
    }

    public BigDecimal getCongelado() {
        return congelado;
    }

    public void setCongelado(BigDecimal congelado) {
        this.congelado = congelado;
    }

    public BigDecimal getDisponible() {
        return disponible;
    }

    public void setDisponible(BigDecimal disponible) {
        this.disponible = disponible;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
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
        final InfComprometido other = (InfComprometido) obj;
        if (this.id!=null && other.id!=null) {
            return Objects.equals(this.id, other.id);
        }
        return (this== other);
    }    
    
}
