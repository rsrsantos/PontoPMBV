package br.com.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Horario implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;

    private String toleranciaGeral;
   
    private boolean compensado;

    //Dias Uteis
    @Temporal(TemporalType.TIME)
    private Date hora_EntradaT1_util;

    @Temporal(TemporalType.TIME)
    private Date hora_saidaT1_util;

    @Temporal(TemporalType.TIME)
    private Date hora_EntradaT2_util;

    @Temporal(TemporalType.TIME)
    private Date hora_saidaT2_util;

    //Finais de Semana
    @Temporal(TemporalType.TIME)
    private Date hora_EntradaT1_final;

    @Temporal(TemporalType.TIME)
    private Date hora_saidaT1_final;

    @Temporal(TemporalType.TIME)
    private Date hora_EntradaT2_final;

    @Temporal(TemporalType.TIME)
    private Date hora_saidaT2_final;
    
    private String carga;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getToleranciaGeral() {
        return toleranciaGeral;
    }

    public void setToleranciaGeral(String toleranciaGeral) {
        this.toleranciaGeral = toleranciaGeral;
    }

    public boolean isCompensado() {
        return compensado;
    }

    public void setCompensado(boolean compensado) {
        this.compensado = compensado;
    }

    public Date getHora_EntradaT1_util() {
        return hora_EntradaT1_util;
    }

    public void setHora_EntradaT1_util(Date hora_EntradaT1_util) {
        this.hora_EntradaT1_util = hora_EntradaT1_util;
    }

    public Date getHora_saidaT1_util() {
        return hora_saidaT1_util;
    }

    public void setHora_saidaT1_util(Date hora_saidaT1_util) {
        this.hora_saidaT1_util = hora_saidaT1_util;
    }

    public Date getHora_EntradaT2_util() {
        return hora_EntradaT2_util;
    }

    public void setHora_EntradaT2_util(Date hora_EntradaT2_util) {
        this.hora_EntradaT2_util = hora_EntradaT2_util;
    }

    public Date getHora_saidaT2_util() {
        return hora_saidaT2_util;
    }

    public void setHora_saidaT2_util(Date hora_saidaT2_util) {
        this.hora_saidaT2_util = hora_saidaT2_util;
    }

    public Date getHora_EntradaT1_final() {
        return hora_EntradaT1_final;
    }

    public void setHora_EntradaT1_final(Date hora_EntradaT1_final) {
        this.hora_EntradaT1_final = hora_EntradaT1_final;
    }

    public Date getHora_saidaT1_final() {
        return hora_saidaT1_final;
    }

    public void setHora_saidaT1_final(Date hora_saidaT1_final) {
        this.hora_saidaT1_final = hora_saidaT1_final;
    }

    public Date getHora_EntradaT2_final() {
        return hora_EntradaT2_final;
    }

    public void setHora_EntradaT2_final(Date hora_EntradaT2_final) {
        this.hora_EntradaT2_final = hora_EntradaT2_final;
    }

    public Date getHora_saidaT2_final() {
        return hora_saidaT2_final;
    }

    public void setHora_saidaT2_final(Date hora_saidaT2_final) {
        this.hora_saidaT2_final = hora_saidaT2_final;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 89 * hash + Objects.hashCode(this.nome);
        hash = 89 * hash + Objects.hashCode(this.toleranciaGeral);
        hash = 89 * hash + (this.compensado ? 1 : 0);
        hash = 89 * hash + Objects.hashCode(this.hora_EntradaT1_util);
        hash = 89 * hash + Objects.hashCode(this.hora_saidaT1_util);
        hash = 89 * hash + Objects.hashCode(this.hora_EntradaT2_util);
        hash = 89 * hash + Objects.hashCode(this.hora_saidaT2_util);
        hash = 89 * hash + Objects.hashCode(this.hora_EntradaT1_final);
        hash = 89 * hash + Objects.hashCode(this.hora_saidaT1_final);
        hash = 89 * hash + Objects.hashCode(this.hora_EntradaT2_final);
        hash = 89 * hash + Objects.hashCode(this.hora_saidaT2_final);
        hash = 89 * hash + Objects.hashCode(this.carga);
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
        final Horario other = (Horario) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.compensado != other.compensado) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.toleranciaGeral, other.toleranciaGeral)) {
            return false;
        }
        if (!Objects.equals(this.carga, other.carga)) {
            return false;
        }
        if (!Objects.equals(this.hora_EntradaT1_util, other.hora_EntradaT1_util)) {
            return false;
        }
        if (!Objects.equals(this.hora_saidaT1_util, other.hora_saidaT1_util)) {
            return false;
        }
        if (!Objects.equals(this.hora_EntradaT2_util, other.hora_EntradaT2_util)) {
            return false;
        }
        if (!Objects.equals(this.hora_saidaT2_util, other.hora_saidaT2_util)) {
            return false;
        }
        if (!Objects.equals(this.hora_EntradaT1_final, other.hora_EntradaT1_final)) {
            return false;
        }
        if (!Objects.equals(this.hora_saidaT1_final, other.hora_saidaT1_final)) {
            return false;
        }
        if (!Objects.equals(this.hora_EntradaT2_final, other.hora_EntradaT2_final)) {
            return false;
        }
        if (!Objects.equals(this.hora_saidaT2_final, other.hora_saidaT2_final)) {
            return false;
        }
        return true;
    }

    
    
    
    
    

    
}
