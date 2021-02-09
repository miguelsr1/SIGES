package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siges.entities.data.impl.SgCiclo;
import gob.mined.siges.entities.data.impl.SgModalidad;
import gob.mined.siges.entities.data.impl.SgModalidadAtencion;
import gob.mined.siges.entities.data.impl.SgNivel;
import gob.mined.siges.entities.data.impl.SgOrganizacionCurricular;
import gob.mined.siges.entities.data.impl.SgSubModalidad;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Customizer;

@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_beneficiarios")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Beneficiarios implements Serializable{
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_beneficiarios", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_beneficiarios", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_beneficiarios")
    @Column(name = "b_id")
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_organizacion_curricular") 
    private SgOrganizacionCurricular organizacionesCurricular;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_nivel") 
    private SgNivel sgNiveles;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_ciclo") 
    private SgCiclo sgCiclos;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_modalidad") 
    private SgModalidad sgModalidades;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_modalidad_atencion") 
    private SgModalidadAtencion sgModalidadAtencion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_sub_modalidad") 
    private SgSubModalidad sgSubModalidad;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_ges_pres_es_anio_fiscal") 
    private RelacionGesPresEsAnioFiscal sgPresEsAnioFiscal;
    
    @Column(name = "v_valor")
    private Boolean valor;

    
    
    @Transient
    private String organizaciones;
    
    @Transient
    private String nivel;
    
    @Transient
    private String ciclo;
    
    @Transient
    private String modalidad;
    
    @Transient
    private String modalidadAtencion;
    
    @Transient
    private String subModalidad;

    public Beneficiarios() {
    }

    
    
    public Beneficiarios(String organizaciones, String nivel, String ciclo, String modalidad, String modalidadAtencion, String subModalidad) {
        this.organizaciones = organizaciones;
        this.nivel = nivel;
        this.ciclo = ciclo;
        this.modalidad = modalidad;
        this.modalidadAtencion = modalidadAtencion;
        this.subModalidad = subModalidad;
    }
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SgOrganizacionCurricular getOrganizacionesCurricular() {
        return organizacionesCurricular;
    }

    public void setOrganizacionesCurricular(SgOrganizacionCurricular organizacionesCurricular) {
        this.organizacionesCurricular = organizacionesCurricular;
    }

    public SgNivel getSgNiveles() {
        return sgNiveles;
    }

    public void setSgNiveles(SgNivel sgNiveles) {
        this.sgNiveles = sgNiveles;
    }

    public SgCiclo getSgCiclos() {
        return sgCiclos;
    }

    public void setSgCiclos(SgCiclo sgCiclos) {
        this.sgCiclos = sgCiclos;
    }

    public SgModalidad getSgModalidades() {
        return sgModalidades;
    }

    public void setSgModalidades(SgModalidad sgModalidades) {
        this.sgModalidades = sgModalidades;
    }

    public SgModalidadAtencion getSgModalidadAtencion() {
        return sgModalidadAtencion;
    }

    public void setSgModalidadAtencion(SgModalidadAtencion sgModalidadAtencion) {
        this.sgModalidadAtencion = sgModalidadAtencion;
    }

    public SgSubModalidad getSgSubModalidad() {
        return sgSubModalidad;
    }

    public void setSgSubModalidad(SgSubModalidad sgSubModalidad) {
        this.sgSubModalidad = sgSubModalidad;
    }

    public Boolean getValor() {
        return valor;
    }

    public void setValor(Boolean valor) {
        this.valor = valor;
    }

    public String getOrganizaciones() {
        return organizaciones;
    }

    public void setOrganizaciones(String organizaciones) {
        this.organizaciones = organizaciones;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getSubModalidad() {
        return subModalidad;
    }

    public void setSubModalidad(String subModalidad) {
        this.subModalidad = subModalidad;
    }


    public RelacionGesPresEsAnioFiscal getSgPresEsAnioFiscal() {
        return sgPresEsAnioFiscal;
    }

    public void setSgPresEsAnioFiscal(RelacionGesPresEsAnioFiscal sgPresEsAnioFiscal) {
        this.sgPresEsAnioFiscal = sgPresEsAnioFiscal;
    }
    

    public String getModalidadAtencion() {
        return modalidadAtencion;
    }

    public void setModalidadAtencion(String modalidadAtencion) {
        this.modalidadAtencion = modalidadAtencion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final Beneficiarios other = (Beneficiarios) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
