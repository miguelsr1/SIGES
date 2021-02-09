/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_confirmaciones_matriculas", uniqueConstraints = {
    @UniqueConstraint(name = "sg_confmat_sede_anio_uk", columnNames = {"cma_sede_fk", "cma_anio_lectivo_fk"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cmaPk", scope = SgConfirmacionMatriculas.class)
@Audited
public class SgConfirmacionMatriculas implements DataSecurity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cma_pk", nullable = false)
    private Long cmaPk;

    @Column(name = "cma_fecha_confirmacion")
    private LocalDateTime cmaFechaConfirmacion;
    
    @Size(max = 45)
    @Column(name = "cma_usuario_confirmacion",length = 45)
    private String cmaUsuarioConfirmacion;
    
    @JoinColumn(name = "cma_sede_fk")
    @ManyToOne
    private SgSede cmaSede;
    
    @JoinColumn(name = "cma_anio_lectivo_fk")
    @ManyToOne
    private SgAnioLectivo cmaAnioLectivo;

    @Column(name = "cma_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cmaUltModFecha;

    @Size(max = 45)
    @Column(name = "cma_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cmaUltModUsuario;

    @Column(name = "cma_version")
    @Version
    private Integer cmaVersion;
    
    @JoinColumn(name = "cma_archivo_firmado_fk")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgArchivo cmaArhivoFirmado;
    
    @Column(name = "cma_firmada")
    private Boolean cmaFirmada;
    
    @Column(name = "cma_firma_transaction_id")
    private String cmaFirmaTransactionId; // Transaction id
    
    @Transient
    private String cmaTransactionSignatureUrl; // Url a donde redirigir para firmar
    
    @Transient
    private String cmaTransactionReturnUrl; // Url a donde redirigir luego de firma
    

     

    

    public SgConfirmacionMatriculas() {
    }

    public Long getCmaPk() {
        return cmaPk;
    }

    public void setCmaPk(Long cmaPk) {
        this.cmaPk = cmaPk;
    }

    public LocalDateTime getCmaUltModFecha() {
        return cmaUltModFecha;
    }

    public void setCmaUltModFecha(LocalDateTime cmaUltModFecha) {
        this.cmaUltModFecha = cmaUltModFecha;
    }

    public String getCmaUltModUsuario() {
        return cmaUltModUsuario;
    }

    public void setCmaUltModUsuario(String cmaUltModUsuario) {
        this.cmaUltModUsuario = cmaUltModUsuario;
    }

    public LocalDateTime getCmaFechaConfirmacion() {
        return cmaFechaConfirmacion;
    }

    public void setCmaFechaConfirmacion(LocalDateTime cmaFechaConfirmacion) {
        this.cmaFechaConfirmacion = cmaFechaConfirmacion;
    }

    public String getCmaUsuarioConfirmacion() {
        return cmaUsuarioConfirmacion;
    }

    public void setCmaUsuarioConfirmacion(String cmaUsuarioConfirmacion) {
        this.cmaUsuarioConfirmacion = cmaUsuarioConfirmacion;
    }

    public SgSede getCmaSede() {
        return cmaSede;
    }

    public void setCmaSede(SgSede cmaSede) {
        this.cmaSede = cmaSede;
    }

    public SgAnioLectivo getCmaAnioLectivo() {
        return cmaAnioLectivo;
    }

    public void setCmaAnioLectivo(SgAnioLectivo cmaAnioLectivo) {
        this.cmaAnioLectivo = cmaAnioLectivo;
    }
    

    public Integer getCmaVersion() {
        return cmaVersion;
    }

    public void setCmaVersion(Integer cmaVersion) {
        this.cmaVersion = cmaVersion;
    }

    public SgArchivo getCmaArhivoFirmado() {
        return cmaArhivoFirmado;
    }

    public void setCmaArhivoFirmado(SgArchivo cmaArhivoFirmado) {
        this.cmaArhivoFirmado = cmaArhivoFirmado;
    }


    public String getCmaTransactionSignatureUrl() {
        return cmaTransactionSignatureUrl;
    }

    public void setCmaTransactionSignatureUrl(String cmaTransactionSignatureUrl) {
        this.cmaTransactionSignatureUrl = cmaTransactionSignatureUrl;
    }

    public String getCmaTransactionReturnUrl() {
        return cmaTransactionReturnUrl;
    }

    public void setCmaTransactionReturnUrl(String cmaTransactionReturnUrl) {
        this.cmaTransactionReturnUrl = cmaTransactionReturnUrl;
    }

    public Boolean getCmaFirmada() {
        return cmaFirmada;
    }

    public void setCmaFirmada(Boolean cmaFirmada) {
        this.cmaFirmada = cmaFirmada;
    }

    public String getCmaFirmaTransactionId() {
        return cmaFirmaTransactionId;
    }

    public void setCmaFirmaTransactionId(String cmaFirmaTransactionId) {
        this.cmaFirmaTransactionId = cmaFirmaTransactionId;
    }
  
    @Override
    public String securityAmbitCreate() {
       return "cmaSede";
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cmaSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cmaSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cmaSede.sedSedeAdscritaDe.sedPk", o.getContext()) );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cmaPk", -1L);
        } 
    }

    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cmaPk);
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
        final SgConfirmacionMatriculas other = (SgConfirmacionMatriculas) obj;
        if (!Objects.equals(this.cmaPk, other.cmaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgConfirmacionMatriculas{" + "cmaPk=" + cmaPk + '}';
    }

    
    
    

}
