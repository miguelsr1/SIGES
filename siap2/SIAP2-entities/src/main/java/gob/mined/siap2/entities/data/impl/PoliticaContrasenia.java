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
import javax.persistence.Id;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pol_contrasenia")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class PoliticaContrasenia implements Serializable {

    @Id
    @Column(name = "por_id")
    private Integer id;
    
    @Column(name = "pol_dias_caducidad")
    private Integer diasCaducidad;
    
    @Column(name = "pol_cantidad_intentos")
    private Integer cantidadIntentos;
    
    @Column(name = "pol_largo_minimo")
    private Integer largoMinimo;
    
    @Column(name = "pol_cant_min_especiales")
    private Integer cantidadMinimaCaracteresEspeciales;
    
    @Column(name = "pol_cant_min_mayusculas")
    private Integer cantidadMinimaCaracteresMayuscula;
    
    @Column(name = "pol_cant_min_minusculas")
    private Integer cantidadMinimaCaracteresMinusculas;
    
    @Column(name = "pol_cant_min_digitos")
    private Integer cantidadMinimaDigitos;
    
    @Column(name = "pol_permite_usu_cod_en_passwd")
    private Boolean permiteUsuCodEnPassword=Boolean.TRUE;
    
    @Column(name = "pol_cambia_passwd_prim_vez")
    private Boolean cambiaPasswordPrimeraVez=Boolean.TRUE;
    
    @Column(name = "pol_cambia_passwd_desp_olvido")
    private Boolean cambiaPasswordDespuesOlvido=Boolean.TRUE;
    
        
    //Auditoria
    @Column(name = "pol_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pol_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pol_version")
    @Version
    private Integer version;

    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidadMinimaCaracteresMinusculas() {
        return cantidadMinimaCaracteresMinusculas;
    }

    public void setCantidadMinimaCaracteresMinusculas(Integer cantidadMinimaCaracteresMinusculas) {
        this.cantidadMinimaCaracteresMinusculas = cantidadMinimaCaracteresMinusculas;
    }
    
    

    public Integer getDiasCaducidad() {
        return diasCaducidad;
    }

    public void setDiasCaducidad(Integer diasCaducidad) {
        this.diasCaducidad = diasCaducidad;
    }

    public Integer getCantidadIntentos() {
        return cantidadIntentos;
    }

    public void setCantidadIntentos(Integer cantidadIntentos) {
        this.cantidadIntentos = cantidadIntentos;
    }

    public Integer getLargoMinimo() {
        return largoMinimo;
    }

    public void setLargoMinimo(Integer largoMinimo) {
        this.largoMinimo = largoMinimo;
    }

    public Integer getCantidadMinimaCaracteresEspeciales() {
        return cantidadMinimaCaracteresEspeciales;
    }

    public void setCantidadMinimaCaracteresEspeciales(Integer cantidadMinimaCaracteresEspeciales) {
        this.cantidadMinimaCaracteresEspeciales = cantidadMinimaCaracteresEspeciales;
    }

    public Integer getCantidadMinimaCaracteresMayuscula() {
        return cantidadMinimaCaracteresMayuscula;
    }

    public void setCantidadMinimaCaracteresMayuscula(Integer cantidadMinimaCaracteresMayuscula) {
        this.cantidadMinimaCaracteresMayuscula = cantidadMinimaCaracteresMayuscula;
    }

    public Integer getCantidadMinimaDigitos() {
        return cantidadMinimaDigitos;
    }

    public void setCantidadMinimaDigitos(Integer cantidadMinimaDigitos) {
        this.cantidadMinimaDigitos = cantidadMinimaDigitos;
    }

    public Boolean getPermiteUsuCodEnPassword() {
        return permiteUsuCodEnPassword;
    }

    public void setPermiteUsuCodEnPassword(Boolean permiteUsuCodEnPassword) {
        this.permiteUsuCodEnPassword = permiteUsuCodEnPassword;
    }

    public Boolean getCambiaPasswordPrimeraVez() {
        return cambiaPasswordPrimeraVez;
    }

    public void setCambiaPasswordPrimeraVez(Boolean cambiaPasswordPrimeraVez) {
        this.cambiaPasswordPrimeraVez = cambiaPasswordPrimeraVez;
    }

    public Boolean getCambiaPasswordDespuesOlvido() {
        return cambiaPasswordDespuesOlvido;
    }

    public void setCambiaPasswordDespuesOlvido(Boolean cambiaPasswordDespuesOlvido) {
        this.cambiaPasswordDespuesOlvido = cambiaPasswordDespuesOlvido;
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
        final PoliticaContrasenia other = (PoliticaContrasenia) obj;
        if (this.id!= null && other.id!= null) {
            return Objects.equals(this.id, other.id);
        }
        return (this==other);
    }

}
