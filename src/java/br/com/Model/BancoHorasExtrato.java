package br.com.Model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author postgres
 */
@Entity
@Table(name = "bancohorasextrato")
public class BancoHorasExtrato implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    
    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    Funcionario funcionario;
////    
    @ManyToOne
    @JoinColumn(name = "nome_usuario_id")
    Usuario usuario;
    
    
    private String saldoMes;
    
    private String SaldoAtual;

    private String tipo;

    private String saldoPositivo;

    private String saldoNegativo;

    private String pis;

    private String periodo;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data_evento;

    private String observacao;

    private String descricao;
    
    private boolean alterado;
    
    private String acao;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSaldoPositivo() {
        return saldoPositivo;
    }

    public void setSaldoPositivo(String saldoPositivo) {
        this.saldoPositivo = saldoPositivo;
    }

    public String getSaldoNegativo() {
        return saldoNegativo;
    }

    public void setSaldoNegativo(String saldoNegativo) {
        this.saldoNegativo = saldoNegativo;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Date getData_evento() {
        return data_evento;
    }

    public void setData_evento(Date data_evento) {
        this.data_evento = data_evento;
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

    public boolean isAlterado() {
        return alterado;
    }

    public void setAlterado(boolean alterado) {
        this.alterado = alterado;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getSaldoMes() {
        return saldoMes;
    }

    public void setSaldoMes(String saldoMes) {
        this.saldoMes = saldoMes;
    }

    public String getSaldoAtual() {
        return SaldoAtual;
    }

    public void setSaldoAtual(String SaldoAtual) {
        this.SaldoAtual = SaldoAtual;
    }

    
    

    

}
