/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.EnumModeloContrato;
import sv.gob.mined.siges.enumerados.TipoPersonalSedeEducativa;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCargo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstadoDatoContratacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInstitucionPagaSalario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgLeySalario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgModoPago;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoAlfabetizador;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoContrato;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoInstitucionPaga;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoNombramiento;

@Entity
@Table(name = "sg_datos_contratacion", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dcoPk", scope = SgDatoContratacion.class)
@Audited
public class SgDatoContratacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dco_pk")
    private Long dcoPk;
    
    @Column(name = "dco_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPersonalSedeEducativa dcoTipo;
    
    @JoinColumn(name = "dco_dato_empleado_fk", referencedColumnName = "dem_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgDatoEmpleado dcoDatoEmpleado;
    
    @JoinColumn(name = "dco_ley_salario_fk", referencedColumnName = "lsa_pk")
    @ManyToOne
    private SgLeySalario dcoLeySalario;
    
    @JoinColumn(name = "dco_fuente_financiamiento_fk", referencedColumnName = "ffi_pk")
    @ManyToOne
    private SgFuenteFinanciamiento dcoFuenteFinanciamiento;
    
    @JoinColumn(name = "dco_institucion_paga_salario_fk", referencedColumnName = "ips_pk")
    @ManyToOne
    private SgInstitucionPagaSalario dcoInstitucionPagaSalario;
    
    @Column(name = "dco_institucion_paga_salario_otro",length = 255)
    private String dcoInstitucionPagaSalarioOtro;
    
    @JoinColumn(name = "dco_tipo_institucion_paga_fk", referencedColumnName = "tip_pk")
    @ManyToOne
    private SgTipoInstitucionPaga dcoTipoInstitucionPaga;
    
    @Column(name = "dco_hora_ingreso")
    private LocalTime dcoHoraIngreso;
    
    @Column(name = "dco_hora_salida")
    private LocalTime dcoHoraSalida;
    
    @Column(name = "dco_salario_mensual")
    private Double dcoSalarioMensual;
    
    @JoinColumn(name = "dco_tipo_nombramiento_fk", referencedColumnName = "tno_pk")
    @ManyToOne
    private SgTipoNombramiento dcoTipoNombramiento;
    
    @JoinColumn(name = "dco_modo_pago_fk", referencedColumnName = "mpa_pk")
    @ManyToOne
    private SgModoPago dcoModoPago;
    
    @JoinColumn(name = "dco_centro_educativo_fk")
    @ManyToOne(optional = false)
    private SgSedeLite dcoCentroEducativo;
    
    @Column(name = "dco_desde")
    private LocalDate dcoDesde;
    
    @Column(name = "dco_hasta")
    private LocalDate dcoHasta;
    
    @JoinColumn(name = "dco_cargo_fk", referencedColumnName = "car_pk")
    @ManyToOne
    private SgCargo dcoCargo;
    
    @JoinColumn(name = "dco_tipo_contrato_fk", referencedColumnName = "tco_pk")
    @ManyToOne
    private SgTipoContrato dcoTipoContrato;
    
    @Column(name = "dco_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dcoUltModFecha;
    
    @Size(max = 45)
    @Column(name = "dco_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String dcoUltModUsuario;
    
    @Column(name = "dco_version")
    @Version
    private Integer dcoVersion;
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_dato_contratacion_jornada_laboral",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "dco_pk"),
            inverseJoinColumns = @JoinColumn(name = "jla_pk"))
    private List<SgJornadaLaboral> dcoJornadasLaborales;
    
    @Column(name = "dco_razones_interinato")
    private String dcoRazonesInterinato;
    
    @Column(name = "dco_modelo_contrato")
    @Enumerated(value = EnumType.STRING)
    private EnumModeloContrato dcoModeloContrato;
    
    @Column(name = "dco_codigo_plaza")
    private String dcoCodigoPlaza;
    
    @Column(name = "dco_actividades_docentes")
    private Boolean dcoActividadesDocentes;
    

    @JoinColumn(name = "dco_estado_fk", referencedColumnName = "edc_pk")
    @ManyToOne
    private SgEstadoDatoContratacion dcoEstado;

    @JoinColumn(name = "dco_tipo_alfabetizador_fk", referencedColumnName = "tal_pk")
    @ManyToOne
    private SgTipoAlfabetizador dcoTipoAlfabetizador;


    public SgDatoContratacion() {
    }

    public SgDatoContratacion(Long dcoPk) {
        this.dcoPk = dcoPk;
    }

    
    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public Long getDcoPk() {
        return dcoPk;
    }

    public void setDcoPk(Long dcoPk) {
        this.dcoPk = dcoPk;
    }

    public LocalDate getDcoDesde() {
        return dcoDesde;
    }

    public void setDcoDesde(LocalDate dcoDesde) {
        this.dcoDesde = dcoDesde;
    }

    public LocalDate getDcoHasta() {
        return dcoHasta;
    }

    public void setDcoHasta(LocalDate dcoHasta) {
        this.dcoHasta = dcoHasta;
    }

    public SgSedeLite getDcoCentroEducativo() {
        return dcoCentroEducativo;
    }

    public void setDcoCentroEducativo(SgSedeLite dcoCentroEducativo) {
        this.dcoCentroEducativo = dcoCentroEducativo;
    }

    public SgCargo getDcoCargo() {
        return dcoCargo;
    }

    public void setDcoCargo(SgCargo dcoCargo) {
        this.dcoCargo = dcoCargo;
    }


    public SgTipoContrato getDcoTipoContrato() {
        return dcoTipoContrato;
    }

    public void setDcoTipoContrato(SgTipoContrato dcoTipoContrato) {
        this.dcoTipoContrato = dcoTipoContrato;
    }

    public LocalDateTime getDcoUltModFecha() {
        return dcoUltModFecha;
    }

    public void setDcoUltModFecha(LocalDateTime dcoUltModFecha) {
        this.dcoUltModFecha = dcoUltModFecha;
    }

    public String getDcoUltModUsuario() {
        return dcoUltModUsuario;
    }

    public void setDcoUltModUsuario(String dcoUltModUsuario) {
        this.dcoUltModUsuario = dcoUltModUsuario;
    }

    public Integer getDcoVersion() {
        return dcoVersion;
    }

    public void setDcoVersion(Integer dcoVersion) {
        this.dcoVersion = dcoVersion;
    }

    public SgDatoEmpleado getDcoDatoEmpleado() {
        return dcoDatoEmpleado;
    }

    public void setDcoDatoEmpleado(SgDatoEmpleado dcoDatoEmpleado) {
        this.dcoDatoEmpleado = dcoDatoEmpleado;
    }

    public SgLeySalario getDcoLeySalario() {
        return dcoLeySalario;
    }

    public void setDcoLeySalario(SgLeySalario dcoLeySalario) {
        this.dcoLeySalario = dcoLeySalario;
    }

    public SgFuenteFinanciamiento getDcoFuenteFinanciamiento() {
        return dcoFuenteFinanciamiento;
    }

    public void setDcoFuenteFinanciamiento(SgFuenteFinanciamiento dcoFuenteFinanciamiento) {
        this.dcoFuenteFinanciamiento = dcoFuenteFinanciamiento;
    }

    public SgInstitucionPagaSalario getDcoInstitucionPagaSalario() {
        return dcoInstitucionPagaSalario;
    }

    public void setDcoInstitucionPagaSalario(SgInstitucionPagaSalario dcoInstitucionPagaSalario) {
        this.dcoInstitucionPagaSalario = dcoInstitucionPagaSalario;
    }

    public String getDcoInstitucionPagaSalarioOtro() {
        return dcoInstitucionPagaSalarioOtro;
    }

    public void setDcoInstitucionPagaSalarioOtro(String dcoInstitucionPagaSalarioOtro) {
        this.dcoInstitucionPagaSalarioOtro = dcoInstitucionPagaSalarioOtro;
    }

    public SgTipoInstitucionPaga getDcoTipoInstitucionPaga() {
        return dcoTipoInstitucionPaga;
    }

    public void setDcoTipoInstitucionPaga(SgTipoInstitucionPaga dcoTipoInstitucionPaga) {
        this.dcoTipoInstitucionPaga = dcoTipoInstitucionPaga;
    }

    public LocalTime getDcoHoraIngreso() {
        return dcoHoraIngreso;
    }

    public void setDcoHoraIngreso(LocalTime dcoHoraIngreso) {
        this.dcoHoraIngreso = dcoHoraIngreso;
    }

    public LocalTime getDcoHoraSalida() {
        return dcoHoraSalida;
    }

    public void setDcoHoraSalida(LocalTime dcoHoraSalida) {
        this.dcoHoraSalida = dcoHoraSalida;
    }

    public Double getDcoSalarioMensual() {
        return dcoSalarioMensual;
    }

    public void setDcoSalarioMensual(Double dcoSalarioMensual) {
        this.dcoSalarioMensual = dcoSalarioMensual;
    }

    public SgTipoNombramiento getDcoTipoNombramiento() {
        return dcoTipoNombramiento;
    }

    public void setDcoTipoNombramiento(SgTipoNombramiento dcoTipoNombramiento) {
        this.dcoTipoNombramiento = dcoTipoNombramiento;
    }

    public SgModoPago getDcoModoPago() {
        return dcoModoPago;
    }

    public void setDcoModoPago(SgModoPago dcoModoPago) {
        this.dcoModoPago = dcoModoPago;
    }

    public List<SgJornadaLaboral> getDcoJornadasLaborales() {
        return dcoJornadasLaborales;
    }

    public void setDcoJornadasLaborales(List<SgJornadaLaboral> dcoJornadasLaborales) {
        this.dcoJornadasLaborales = dcoJornadasLaborales;
    }

    public EnumModeloContrato getDcoModeloContrato() {
        return dcoModeloContrato;
    }

    public void setDcoModeloContrato(EnumModeloContrato dcoModeloContrato) {
        this.dcoModeloContrato = dcoModeloContrato;
    }

    public String getDcoCodigoPlaza() {
        return dcoCodigoPlaza;
    }

    public void setDcoCodigoPlaza(String dcoCodigoPlaza) {
        this.dcoCodigoPlaza = dcoCodigoPlaza;
    }

    public Boolean getDcoActividadesDocentes() {
        return dcoActividadesDocentes;
    }

    public void setDcoActividadesDocentes(Boolean dcoActividadesDocentes) {
        this.dcoActividadesDocentes = dcoActividadesDocentes;
    }


    public SgEstadoDatoContratacion getDcoEstado() {
        return dcoEstado;
    }

    public void setDcoEstado(SgEstadoDatoContratacion dcoEstado) {
        this.dcoEstado = dcoEstado;
    }
    
    public SgTipoAlfabetizador getDcoTipoAlfabetizador() {
        return dcoTipoAlfabetizador;
    }

    public void setDcoTipoAlfabetizador(SgTipoAlfabetizador dcoTipoAlfabetizador) {
        this.dcoTipoAlfabetizador = dcoTipoAlfabetizador;
    }
    
    
    public String getDcoRazonesInterinato() {
        return dcoRazonesInterinato;
    }

    public void setDcoRazonesInterinato(String dcoRazonesInterinato) {
        this.dcoRazonesInterinato = dcoRazonesInterinato;
    }

    public TipoPersonalSedeEducativa getDcoTipo() {
        return dcoTipo;
    }

    public void setDcoTipo(TipoPersonalSedeEducativa dcoTipo) {
        this.dcoTipo = dcoTipo;
    }
    
    

    //</editor-fold>

 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dcoPk != null ? dcoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDatoContratacion)) {
            return false;
        }
        SgDatoContratacion other = (SgDatoContratacion) object;
        if ((this.dcoPk == null && other.dcoPk != null) || (this.dcoPk != null && !this.dcoPk.equals(other.dcoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDatoContratacion[ dcoPk=" + dcoPk + " ]";
    }

    
}
