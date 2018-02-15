package br.com.Model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Justificativa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "nome", nullable = false, length = 64)
    private String nome;

    @Column(name = "descricao", nullable = false, length = 64)
    private String descricao;

    private String valorDia;

    private boolean falta;

    private boolean ajuste;

    private boolean banco;

    private String ultima_alteracao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    //======= Get and Seters ==============================================
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUltima_alteracao() {
        return ultima_alteracao;
    }

    public void setUltima_alteracao(String ultima_alteracao) {
        this.ultima_alteracao = ultima_alteracao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isFalta() {
        return falta;
    }

    public void setFalta(boolean falta) {
        this.falta = falta;
    }

    public boolean isAjuste() {
        return ajuste;
    }

    public void setAjuste(boolean ajuste) {
        this.ajuste = ajuste;
    }

    public boolean isBanco() {
        return banco;
    }

    public void setBanco(boolean banco) {
        this.banco = banco;
    }

    public String getValorDia() {
        return valorDia;
    }

    public void setValorDia(String valorDia) {
        this.valorDia = valorDia;
    }
    
    
    
    

    //Hascode
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Justificativa other = (Justificativa) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
