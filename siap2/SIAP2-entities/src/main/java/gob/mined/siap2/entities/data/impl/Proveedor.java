/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_proveedor")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Proveedor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proveedor")
    @SequenceGenerator(name = "seq_proveedor", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_proveedor", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;    
    
    @Column(name = "id_oferente")
    private Integer idOferente;
    
    @Column(name = "nit_oferente")
    private String nitOferente;
    
    @Column(name = "nombre_representante")
    private String nombreRepresentante;
     
    @Column(name = "apellidos_representante")
    private String apellidosRepresentante;

    @Column(name = "telefono_representante")
    private String telefonoRepresentante;

    @Column(name = "celular_representante")
    private String celularRepresentante;
    
    @Column(name = "fax")
    private String fax;
    
    @Column(name = "razon_social",length = 500)
    private String razonSocial;

    @Column(name = "nrc")
    private String nrc;
    
    @Column(name = "giro",length = 500)
    private String giro;
    
    @Column(name = "persona_juridica")
    private Boolean personaJuridica;
    
    @Column(name = "nombre_comercial",length = 500)
    private String nombreComercial;
    
    @Column(name = "direccion_factura",length = 500)
    private String direccionFactura;    
   
    @Column(name = "pais",length = 2)
    private String pais;
    
    @Column(name = "banco")
    private Long banco;
    
    @Column(name = "cuenta_bancaria",length = 10)
    private String cuentaBancaria;
    
    @Column(name = "identificadorPrimarioOferente")
    private Long identificadorPrimarioOferente;    
   
    @Column(name = "activo")
    private Boolean activo;
  
    @Column(name = "name")
    private String name;
    
    @Column(name = "fecha_actualizacion")
    private String fechaActualizacion;
    
    @Column(name = "pro_mail")
    private String mail;    
    
    @Column(name = "proveedor_mined", columnDefinition = "boolean default true")
    private Boolean proveedorMined;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdOferente() {
        return idOferente;
    }

    public void setIdOferente(Integer idOferente) {
        this.idOferente = idOferente;
    }

    public String getNitOferente() {
        return nitOferente;
    }

    public void setNitOferente(String nitOferente) {
        this.nitOferente = nitOferente;
    }

    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    public void setNombreRepresentante(String nombreRepresentante) {
        this.nombreRepresentante = nombreRepresentante;
    }

    public String getApellidosRepresentante() {
        return apellidosRepresentante;
    }

    public void setApellidosRepresentante(String apellidosRepresentante) {
        this.apellidosRepresentante = apellidosRepresentante;
    }

    public String getTelefonoRepresentante() {
        return telefonoRepresentante;
    }

    public void setTelefonoRepresentante(String telefonoRepresentante) {
        this.telefonoRepresentante = telefonoRepresentante;
    }

    public String getCelularRepresentante() {
        return celularRepresentante;
    }

    public void setCelularRepresentante(String celularRepresentante) {
        this.celularRepresentante = celularRepresentante;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getGiro() {
        return giro;
    }

    public void setGiro(String giro) {
        this.giro = giro;
    }

    public Boolean getPersonaJuridica() {
        return personaJuridica;
    }

    public void setPersonaJuridica(Boolean personaJuridica) {
        this.personaJuridica = personaJuridica;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getDireccionFactura() {
        return direccionFactura;
    }

    public void setDireccionFactura(String direccionFactura) {
        this.direccionFactura = direccionFactura;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Long getBanco() {
        return banco;
    }

    public void setBanco(Long banco) {
        this.banco = banco;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public Long getIdentificadorPrimarioOferente() {
        return identificadorPrimarioOferente;
    }

    public void setIdentificadorPrimarioOferente(Long identificadorPrimarioOferente) {
        this.identificadorPrimarioOferente = identificadorPrimarioOferente;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

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
        final Proveedor other = (Proveedor) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public Boolean getProveedorMined() {
        return proveedorMined;
    }

    public void setProveedorMined(Boolean proveedorMined) {
        this.proveedorMined = proveedorMined;
    }

}
