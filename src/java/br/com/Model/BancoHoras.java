package br.com.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author postgres
 */
@Entity
@Table(name = "bancohoras")
public class BancoHoras implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    
    @ManyToMany
    @JoinColumn(name = "departamento_id")
    private List<Departamento> departamento;
////    
    @ManyToOne
    @JoinColumn(name = "nome_usuario_id")
    Usuario usuario;
    
    private boolean processado;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data_criacao;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data_registro;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data_expiracao;

    private String observacao;

    private String descricao;

    private String periodo;

    private long limite_credito_diario;

    private long limite_debito_diario;

//Gets and Setrs
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(Date data_criacao) {
        this.data_criacao = data_criacao;
    }

    public Date getData_registro() {
        return data_registro;
    }

    public void setData_registro(Date data_registro) {
        this.data_registro = data_registro;
    }

    public Date getData_expiracao() {
        return data_expiracao;
    }

    public void setData_expiracao(Date data_expiracao) {
        this.data_expiracao = data_expiracao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getLimite_credito_diario() {
        return limite_credito_diario;
    }

    public void setLimite_credito_diario(long limite_credito_diario) {
        this.limite_credito_diario = limite_credito_diario;
    }

    public long getLimite_debito_diario() {
        return limite_debito_diario;
    }

    public void setLimite_debito_diario(long limite_debito_diario) {
        this.limite_debito_diario = limite_debito_diario;
    }

    public List<Departamento> getDepartamento() {
        return departamento;
    }

    public void setDepartamento(List<Departamento> departamento) {
        this.departamento = departamento;
    }

    public boolean isProcessado() {
        return processado;
    }

    public void setProcessado(boolean processado) {
        this.processado = processado;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    
    


}
