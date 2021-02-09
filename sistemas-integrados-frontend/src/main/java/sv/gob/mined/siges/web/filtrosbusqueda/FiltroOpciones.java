package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author usuario
 */
public class FiltroOpciones implements Serializable {

    private Long opcModalidadPk;
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String descripcion;
    private Boolean habilitado;
    private Long opcSectorEconomicoPk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private Boolean inicializarProgramaEducativo;
    private String[] incluirCampos;

    public FiltroOpciones() {
        inicializarProgramaEducativo = Boolean.FALSE;
    }

    public Long getOpcModalidadPk() {
        return opcModalidadPk;
    }

    public void setOpcModalidadPk(Long opcModalidadPk) {
        this.opcModalidadPk = opcModalidadPk;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoExacto() {
        return codigoExacto;
    }

    public void setCodigoExacto(String codigoExacto) {
        this.codigoExacto = codigoExacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Long getOpcSectorEconomicoPk() {
        return opcSectorEconomicoPk;
    }

    public void setOpcSectorEconomicoPk(Long opcSectorEconomicoPk) {
        this.opcSectorEconomicoPk = opcSectorEconomicoPk;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    public Boolean getInicializarProgramaEducativo() {
        return inicializarProgramaEducativo;
    }

    public void setInicializarProgramaEducativo(Boolean inicializarProgramaEducativo) {
        this.inicializarProgramaEducativo = inicializarProgramaEducativo;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.opcModalidadPk);
        hash = 17 * hash + Objects.hashCode(this.codigo);
        hash = 17 * hash + Objects.hashCode(this.codigoExacto);
        hash = 17 * hash + Objects.hashCode(this.nombre);
        hash = 17 * hash + Objects.hashCode(this.descripcion);
        hash = 17 * hash + Objects.hashCode(this.habilitado);
        hash = 17 * hash + Objects.hashCode(this.opcSectorEconomicoPk);
        hash = 17 * hash + Objects.hashCode(this.first);
        hash = 17 * hash + Objects.hashCode(this.maxResults);
        hash = 17 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 17 * hash + Arrays.hashCode(this.ascending);
        hash = 17 * hash + Objects.hashCode(this.inicializarProgramaEducativo);
        hash = 17 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroOpciones other = (FiltroOpciones) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.codigoExacto, other.codigoExacto)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.opcModalidadPk, other.opcModalidadPk)) {
            return false;
        }
        if (!Objects.equals(this.habilitado, other.habilitado)) {
            return false;
        }
        if (!Objects.equals(this.opcSectorEconomicoPk, other.opcSectorEconomicoPk)) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        if (!Objects.equals(this.inicializarProgramaEducativo, other.inicializarProgramaEducativo)) {
            return false;
        }
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        return true;
    }

    
}
