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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsTransferenciaComponente;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 * Entidad correspondiente a las chequeras
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_chequeras", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@Audited
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "chePk", scope = SsTransferenciaComponente.class)
public class SgChequera implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "che_pk")
    private Long chePk;

    @JoinColumn(name = "che_cuenta_bancaria_fk", referencedColumnName = "cbc_pk")
    @ManyToOne
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgCuentasBancarias cheCuentaBancariaFk;

    @Column(name = "che_fecha_chequera")
    private LocalDate cheFechaChequera;

    @Column(name = "che_serie", nullable = true)
    private String cheSerie;

    @Column(name = "che_numero_inicial", nullable = true)
    private Integer cheNumeroInicial;

    @Column(name = "che_numero_final", nullable = true)
    private Integer cheNumeroFinal;

    @JoinColumn(name = "che_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede cheSedeFk;

    //Auditoria
    @Column(name = "che_ult_mod_fecha ")
    @AtributoUltimaModificacion
    private LocalDateTime cheUltMod;

    @Column(name = "che_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String cheUltUsuario;

    @Column(name = "che_version")
    @Version
    private Integer cheVersion;

    @NotAudited
    @OneToMany(mappedBy = "pgsChequeraFk", fetch = FetchType.LAZY)
    private List<SgPago> chePagos;

    @NotAudited
    @OneToMany(mappedBy = "mccChequeraFK", fetch = FetchType.LAZY)
    private List<SgMovimientoCajaChica> cheMovientosCajaChica;

    public Long getChePk() {
        return chePk;
    }

    public void setChePk(Long chePk) {
        this.chePk = chePk;
    }

    public SgCuentasBancarias getCheCuentaBancariaFk() {
        return cheCuentaBancariaFk;
    }

    public void setCheCuentaBancariaFk(SgCuentasBancarias cheCuentaBancariaFk) {
        this.cheCuentaBancariaFk = cheCuentaBancariaFk;
    }

    public LocalDate getCheFechaChequera() {
        return cheFechaChequera;
    }

    public void setCheFechaChequera(LocalDate cheFechaChequera) {
        this.cheFechaChequera = cheFechaChequera;
    }

    public String getCheSerie() {
        return cheSerie;
    }

    public void setCheSerie(String cheSerie) {
        this.cheSerie = cheSerie;
    }

    public LocalDateTime getCheUltMod() {
        return cheUltMod;
    }

    public void setCheUltMod(LocalDateTime cheUltMod) {
        this.cheUltMod = cheUltMod;
    }

    public String getCheUltUsuario() {
        return cheUltUsuario;
    }

    public void setCheUltUsuario(String cheUltUsuario) {
        this.cheUltUsuario = cheUltUsuario;
    }

    public Integer getCheVersion() {
        return cheVersion;
    }

    public void setCheVersion(Integer cheVersion) {
        this.cheVersion = cheVersion;
    }

    public Integer getCheNumeroInicial() {
        return cheNumeroInicial;
    }

    public void setCheNumeroInicial(Integer cheNumeroInicial) {
        this.cheNumeroInicial = cheNumeroInicial;
    }

    public Integer getCheNumeroFinal() {
        return cheNumeroFinal;
    }

    public void setCheNumeroFinal(Integer cheNumeroFinal) {
        this.cheNumeroFinal = cheNumeroFinal;
    }

    public List<SgPago> getChePagos() {
        return chePagos;
    }

    public void setChePagos(List<SgPago> chePagos) {
        this.chePagos = chePagos;
    }

    public List<SgMovimientoCajaChica> getCheMovientosCajaChica() {
        return cheMovientosCajaChica;
    }

    public void setCheMovientosCajaChica(List<SgMovimientoCajaChica> cheMovientosCajaChica) {
        this.cheMovientosCajaChica = cheMovientosCajaChica;
    }

    public SgSede getCheSedeFk() {
        return cheSedeFk;
    }

    public void setCheSedeFk(SgSede cheSedeFk) {
        this.cheSedeFk = cheSedeFk;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.chePk);
        return hash;
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
        final SgChequera other = (SgChequera) obj;
        if (!Objects.equals(this.chePk, other.chePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgChequera{" + "chePk=" + chePk + '}';
    }

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            //return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "cheCuentaBancariaFk.cbcSedeFk.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return null;
        }
    }
}
