
package sv.gob.mined.siges.dto;

import java.util.Objects;


public class SgIndicador {
    
    private String sexo;
    private String indicador;
    private Long valor;

    public SgIndicador(String sexo, String indicador, Long valor) {
        this.sexo = sexo;
        this.indicador = indicador;
        if (valor != null){
            this.valor = valor;
        } else {
            this.valor = 0L;
        }
    }
    

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.sexo);
        hash = 79 * hash + Objects.hashCode(this.indicador);
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
        final SgIndicador other = (SgIndicador) obj;
        if (!Objects.equals(this.sexo, other.sexo)) {
            return false;
        }
        if (!Objects.equals(this.indicador, other.indicador)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
