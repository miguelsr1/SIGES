/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProfesion;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_direcciones_departamentales", uniqueConstraints = {
    @UniqueConstraint(name = "departamento_unique", columnNames = {"ded_departamento_fk"})
    ,
    @UniqueConstraint(name = "nit_unique", columnNames = {"ded_nit"})
}, schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dedPk", scope = SgDireccionDepartamental.class)
@Audited
/**
 * Entidad correspondiente a las pagadurías de las Direcciones departamentales
 */
public class SgDireccionDepartamental implements Serializable,DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ded_pk", nullable = false)
    private Long dedPk;

    @Size(max = 255)
    @Column(name = "ded_nombre", length = 255)
    @AtributoNormalizable
    private String dedNombre;

    @Size(max = 50)
    @AtributoNombre
    @Column(name = "ded_nit", length = 50)
    private String dedNit;

    @Size(max = 20)
    @Column(name = "ded_telefono", length = 20)
    private String dedTelefono;

    @Size(max = 20)
    @Column(name = "ded_fax", length = 20)
    private String dedFax;

    @Size(max = 50)
    @Column(name = "ded_ip_autorizada", length = 50)
    private String dedIpAutorizada;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ded_nombre_busqueda", length = 255)
    private String dedNombreBusqueda;

    @Column(name = "ded_habilitado")
    @AtributoHabilitado
    private Boolean dedHabilitado;

    @Column(name = "ded_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dedUltModFecha;

    @Size(max = 45)
    @Column(name = "ded_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dedUltModUsuario;

    @Column(name = "ded_version")
    @Version
    private Integer dedVersion;

    @JoinColumn(name = "ded_departamento_fk", referencedColumnName = "dep_pk", unique = true)
    @ManyToOne
    private SgDepartamento dedDepartamentoFk;

    @Size(max = 255)
    @Column(name = "ded_director_cargo", length = 255)
    @AtributoNormalizable
    private String decDirectorCargo;

    @Size(max = 255)
    @Column(name = "ded_director_nombre", length = 255)
    @AtributoNormalizable
    private String decDirectorNombre;

    @Size(max = 255)
    @Column(name = "ded_director_domicilio", length = 255)
    @AtributoNormalizable
    private String decDirectorDomicilio;

    @JoinColumn(name = "ded_director_profesion_fk", referencedColumnName = "pro_pk", unique = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private SgProfesion dedDirectorProfesionFk;

    @Size(max = 255)
    @Column(name = "ded_pagador_cargo", length = 255)
    @AtributoNormalizable
    private String decPagadorCargo;

    @Size(max = 255)
    @Column(name = "ded_pagador_nombre", length = 255)
    @AtributoNormalizable
    private String decPagadorNombre;

    @Size(max = 255)
    @Column(name = "ded_refrendario_cargo", length = 255)
    @AtributoNormalizable
    private String decRefrendarioCargo;

    @Size(max = 255)
    @Column(name = "ded_refrendario_nombre", length = 255)
    @AtributoNormalizable
    private String decRefrendarioNombre;

    public SgDireccionDepartamental() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.dedNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.dedNombre);
    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getDedPk() {
        return dedPk;
    }

    public void setDedPk(Long dedPk) {
        this.dedPk = dedPk;
    }

    public String getDedNombre() {
        return dedNombre;
    }

    public void setDedNombre(String dedNombre) {
        this.dedNombre = dedNombre;
    }

    public String getDedNit() {
        return dedNit;
    }

    public void setDedNit(String dedNit) {
        this.dedNit = dedNit;
    }

    public String getDedTelefono() {
        return dedTelefono;
    }

    public void setDedTelefono(String dedTelefono) {
        this.dedTelefono = dedTelefono;
    }

    public String getDedFax() {
        return dedFax;
    }

    public void setDedFax(String dedFax) {
        this.dedFax = dedFax;
    }

    public String getDedIpAutorizada() {
        return dedIpAutorizada;
    }

    public void setDedIpAutorizada(String dedIpAutorizada) {
        this.dedIpAutorizada = dedIpAutorizada;
    }

    public String getDedNombreBusqueda() {
        return dedNombreBusqueda;
    }

    public void setDedNombreBusqueda(String dedNombreBusqueda) {
        this.dedNombreBusqueda = dedNombreBusqueda;
    }

    public Boolean getDedHabilitado() {
        return dedHabilitado;
    }

    public void setDedHabilitado(Boolean dedHabilitado) {
        this.dedHabilitado = dedHabilitado;
    }

    public LocalDateTime getDedUltModFecha() {
        return dedUltModFecha;
    }

    public void setDedUltModFecha(LocalDateTime dedUltModFecha) {
        this.dedUltModFecha = dedUltModFecha;
    }

    public String getDedUltModUsuario() {
        return dedUltModUsuario;
    }

    public void setDedUltModUsuario(String dedUltModUsuario) {
        this.dedUltModUsuario = dedUltModUsuario;
    }

    public Integer getDedVersion() {
        return dedVersion;
    }

    public void setDedVersion(Integer dedVersion) {
        this.dedVersion = dedVersion;
    }

    public SgDepartamento getDedDepartamentoFk() {
        return dedDepartamentoFk;
    }

    public void setDedDepartamentoFk(SgDepartamento dedDepartamentoFk) {
        this.dedDepartamentoFk = dedDepartamentoFk;
    }

    public String getDecDirectorCargo() {
        return decDirectorCargo;
    }

    public void setDecDirectorCargo(String decDirectorCargo) {
        this.decDirectorCargo = decDirectorCargo;
    }

    public String getDecDirectorNombre() {
        return decDirectorNombre;
    }

    public void setDecDirectorNombre(String decDirectorNombre) {
        this.decDirectorNombre = decDirectorNombre;
    }

    public String getDecPagadorCargo() {
        return decPagadorCargo;
    }

    public void setDecPagadorCargo(String decPagadorCargo) {
        this.decPagadorCargo = decPagadorCargo;
    }

    public String getDecPagadorNombre() {
        return decPagadorNombre;
    }

    public void setDecPagadorNombre(String decPagadorNombre) {
        this.decPagadorNombre = decPagadorNombre;
    }

    public String getDecRefrendarioCargo() {
        return decRefrendarioCargo;
    }

    public void setDecRefrendarioCargo(String decRefrendarioCargo) {
        this.decRefrendarioCargo = decRefrendarioCargo;
    }

    public String getDecRefrendarioNombre() {
        return decRefrendarioNombre;
    }

    public void setDecRefrendarioNombre(String decRefrendarioNombre) {
        this.decRefrendarioNombre = decRefrendarioNombre;
    }

    public String getDecDirectorDomicilio() {
        return decDirectorDomicilio;
    }

    public void setDecDirectorDomicilio(String decDirectorDomicilio) {
        this.decDirectorDomicilio = decDirectorDomicilio;
    }

    public SgProfesion getDedDirectorProfesionFk() {
        return dedDirectorProfesionFk;
    }

    public void setDedDirectorProfesionFk(SgProfesion dedDirectorProfesionFk) {
        this.dedDirectorProfesionFk = dedDirectorProfesionFk;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        return Objects.hashCode(this.dedPk);
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
        final SgDireccionDepartamental other = (SgDireccionDepartamental) obj;
        if (!Objects.equals(this.dedPk, other.dedPk)) {
            return false;
        }
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "SgDireccionDepartamental{" + "dedPk=" + dedPk + ", dedCodigo=" + ", dedNombre=" + dedNombre + ", dedNombreBusqueda=" + dedNombreBusqueda + ", dedHabilitado=" + dedHabilitado + ", dedUltModFecha=" + dedUltModFecha + ", dedUltModUsuario=" + dedUltModUsuario + ", dedVersion=" + dedVersion + '}';
    }
    // </editor-fold>

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "dedDepartamentoFk.depPk", o.getContext());
        } else {
            return null;
        }
    }

}
