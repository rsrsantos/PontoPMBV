package br.com.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

@Entity
public class EspelhoPonto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "bat_entrada_01")
    private String entrada01;

    private String entrada01_antiga;

    @Column(name = "bat_saida_01")
    private String saida01;

    private String saida01_antiga;

    @Column(name = "bat_entrada_02")
    private String entrada02;

    private String entrada02_antiga;

    @Column(name = "bat_saida_02")
    private String saida02;

    private String saida02_antiga;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "nome_usuario_id")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "folhaperiodo_id")
    private FolhaPeriodo folhaperiodo;

    @Column(name = "nome_funcionario")
    private String nome_Funcionario;
    
    @OneToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamentos;

    private String data;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data_Batida;
    
    private int dia;

    private String pis;

    private String funcao;

    private String periodo;

    private String trabalhadas;

    private long matricula;

    private String entrada_prevista_01;

    private String entrada_prevista_02;

    private String saida_prevista_01;

    private String saida_prevista_02;

    private String faltas;

    private String saldo_positivo;

    private String saldo_negativo;

    private String extras;

    private String observacao;

    private boolean folhaAberta;

    private boolean folhaFechada;

    private boolean alterado;

    private String TotalFaltas;

    private String TotalNormais;
    
    private String TotalBancoHoras;
    
    private boolean abonar;
    
    private boolean feriado;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEntrada01() {
        return entrada01;
    }

    public void setEntrada01(String entrada01) {
        this.entrada01 = entrada01;
    }

    public String getEntrada01_antiga() {
        return entrada01_antiga;
    }

    public void setEntrada01_antiga(String entrada01_antiga) {
        this.entrada01_antiga = entrada01_antiga;
    }

    public String getSaida01() {
        return saida01;
    }

    public void setSaida01(String saida01) {
        this.saida01 = saida01;
    }

    public String getSaida01_antiga() {
        return saida01_antiga;
    }

    public void setSaida01_antiga(String saida01_antiga) {
        this.saida01_antiga = saida01_antiga;
    }

    public String getEntrada02() {
        return entrada02;
    }

    public void setEntrada02(String entrada02) {
        this.entrada02 = entrada02;
    }

    public String getEntrada02_antiga() {
        return entrada02_antiga;
    }

    public void setEntrada02_antiga(String entrada02_antiga) {
        this.entrada02_antiga = entrada02_antiga;
    }

    public String getSaida02() {
        return saida02;
    }

    public void setSaida02(String saida02) {
        this.saida02 = saida02;
    }

    public String getSaida02_antiga() {
        return saida02_antiga;
    }

    public void setSaida02_antiga(String saida02_antiga) {
        this.saida02_antiga = saida02_antiga;
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

    public FolhaPeriodo getFolhaperiodo() {
        return folhaperiodo;
    }

    public void setFolhaperiodo(FolhaPeriodo folhaperiodo) {
        this.folhaperiodo = folhaperiodo;
    }

    public String getNome_Funcionario() {
        return nome_Funcionario;
    }

    public void setNome_Funcionario(String nome_Funcionario) {
        this.nome_Funcionario = nome_Funcionario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getData_Batida() {
        return data_Batida;
    }

    public void setData_Batida(Date data_Batida) {
        this.data_Batida = data_Batida;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getTrabalhadas() {
        return trabalhadas;
    }

    public void setTrabalhadas(String trabalhadas) {
        this.trabalhadas = trabalhadas;
    }

    public long getMatricula() {
        return matricula;
    }

    public void setMatricula(long matricula) {
        this.matricula = matricula;
    }

    public String getEntrada_prevista_01() {
        return entrada_prevista_01;
    }

    public void setEntrada_prevista_01(String entrada_prevista_01) {
        this.entrada_prevista_01 = entrada_prevista_01;
    }

    public String getEntrada_prevista_02() {
        return entrada_prevista_02;
    }

    public void setEntrada_prevista_02(String entrada_prevista_02) {
        this.entrada_prevista_02 = entrada_prevista_02;
    }

    public String getSaida_prevista_01() {
        return saida_prevista_01;
    }

    public void setSaida_prevista_01(String saida_prevista_01) {
        this.saida_prevista_01 = saida_prevista_01;
    }

    public String getSaida_prevista_02() {
        return saida_prevista_02;
    }

    public void setSaida_prevista_02(String saida_prevista_02) {
        this.saida_prevista_02 = saida_prevista_02;
    }

    public String getFaltas() {
        return faltas;
    }

    public void setFaltas(String faltas) {
        this.faltas = faltas;
    }

    public String getSaldo_positivo() {
        return saldo_positivo;
    }

    public void setSaldo_positivo(String saldo_positivo) {
        this.saldo_positivo = saldo_positivo;
    }

    public String getSaldo_negativo() {
        return saldo_negativo;
    }

    public void setSaldo_negativo(String saldo_negativo) {
        this.saldo_negativo = saldo_negativo;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public boolean isFolhaAberta() {
        return folhaAberta;
    }

    public void setFolhaAberta(boolean folhaAberta) {
        this.folhaAberta = folhaAberta;
    }

    public boolean isFolhaFechada() {
        return folhaFechada;
    }

    public void setFolhaFechada(boolean folhaFechada) {
        this.folhaFechada = folhaFechada;
    }

    public boolean isAlterado() {
        return alterado;
    }

    public void setAlterado(boolean alterado) {
        this.alterado = alterado;
    }

    public String getTotalFaltas() {
        return TotalFaltas;
    }

    public void setTotalFaltas(String TotalFaltas) {
        this.TotalFaltas = TotalFaltas;
    }

    public String getTotalNormais() {
        return TotalNormais;
    }

    public void setTotalNormais(String TotalNormais) {
        this.TotalNormais = TotalNormais;
    }

    public String getTotalBancoHoras() {
        return TotalBancoHoras;
    }

    public void setTotalBancoHoras(String TotalBancoHoras) {
        this.TotalBancoHoras = TotalBancoHoras;
    }

    public boolean isAbonar() {
        return abonar;
    }

    public void setAbonar(boolean abonar) {
        this.abonar = abonar;
    }

    public boolean isFeriado() {
        return feriado;
    }

    public void setFeriado(boolean feriado) {
        this.feriado = feriado;
    }

    public Departamento getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(Departamento departamentos) {
        this.departamentos = departamentos;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }
    
    
    
    
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 41 * hash + Objects.hashCode(this.entrada01);
        hash = 41 * hash + Objects.hashCode(this.entrada01_antiga);
        hash = 41 * hash + Objects.hashCode(this.saida01);
        hash = 41 * hash + Objects.hashCode(this.saida01_antiga);
        hash = 41 * hash + Objects.hashCode(this.entrada02);
        hash = 41 * hash + Objects.hashCode(this.entrada02_antiga);
        hash = 41 * hash + Objects.hashCode(this.saida02);
        hash = 41 * hash + Objects.hashCode(this.saida02_antiga);
        hash = 41 * hash + Objects.hashCode(this.funcionario);
        hash = 41 * hash + Objects.hashCode(this.usuario);
        hash = 41 * hash + Objects.hashCode(this.folhaperiodo);
        hash = 41 * hash + Objects.hashCode(this.nome_Funcionario);
        hash = 41 * hash + Objects.hashCode(this.data);
        hash = 41 * hash + Objects.hashCode(this.data_Batida);
        hash = 41 * hash + Objects.hashCode(this.pis);
        hash = 41 * hash + Objects.hashCode(this.funcao);
        hash = 41 * hash + Objects.hashCode(this.periodo);
        hash = 41 * hash + Objects.hashCode(this.trabalhadas);
        hash = 41 * hash + Objects.hashCode(this.matricula);
        hash = 41 * hash + Objects.hashCode(this.entrada_prevista_01);
        hash = 41 * hash + Objects.hashCode(this.entrada_prevista_02);
        hash = 41 * hash + Objects.hashCode(this.saida_prevista_01);
        hash = 41 * hash + Objects.hashCode(this.saida_prevista_02);
        hash = 41 * hash + Objects.hashCode(this.faltas);
        hash = 41 * hash + Objects.hashCode(this.saldo_positivo);
        hash = 41 * hash + Objects.hashCode(this.saldo_negativo);
        hash = 41 * hash + Objects.hashCode(this.extras);
        hash = 41 * hash + Objects.hashCode(this.observacao);
        hash = 41 * hash + (this.folhaAberta ? 1 : 0);
        hash = 41 * hash + (this.folhaFechada ? 1 : 0);
        hash = 41 * hash + (this.alterado ? 1 : 0);
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
        final EspelhoPonto other = (EspelhoPonto) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.folhaAberta != other.folhaAberta) {
            return false;
        }
        if (this.folhaFechada != other.folhaFechada) {
            return false;
        }
        if (this.alterado != other.alterado) {
            return false;
        }
        if (!Objects.equals(this.entrada01, other.entrada01)) {
            return false;
        }
        if (!Objects.equals(this.entrada01_antiga, other.entrada01_antiga)) {
            return false;
        }
        if (!Objects.equals(this.saida01, other.saida01)) {
            return false;
        }
        if (!Objects.equals(this.saida01_antiga, other.saida01_antiga)) {
            return false;
        }
        if (!Objects.equals(this.entrada02, other.entrada02)) {
            return false;
        }
        if (!Objects.equals(this.entrada02_antiga, other.entrada02_antiga)) {
            return false;
        }
        if (!Objects.equals(this.saida02, other.saida02)) {
            return false;
        }
        if (!Objects.equals(this.saida02_antiga, other.saida02_antiga)) {
            return false;
        }
        if (!Objects.equals(this.nome_Funcionario, other.nome_Funcionario)) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.pis, other.pis)) {
            return false;
        }
        if (!Objects.equals(this.funcao, other.funcao)) {
            return false;
        }
        if (!Objects.equals(this.periodo, other.periodo)) {
            return false;
        }
        if (!Objects.equals(this.trabalhadas, other.trabalhadas)) {
            return false;
        }
        if (!Objects.equals(this.matricula, other.matricula)) {
            return false;
        }
        if (!Objects.equals(this.entrada_prevista_01, other.entrada_prevista_01)) {
            return false;
        }
        if (!Objects.equals(this.entrada_prevista_02, other.entrada_prevista_02)) {
            return false;
        }
        if (!Objects.equals(this.saida_prevista_01, other.saida_prevista_01)) {
            return false;
        }
        if (!Objects.equals(this.saida_prevista_02, other.saida_prevista_02)) {
            return false;
        }
        if (!Objects.equals(this.faltas, other.faltas)) {
            return false;
        }
        if (!Objects.equals(this.saldo_positivo, other.saldo_positivo)) {
            return false;
        }
        if (!Objects.equals(this.saldo_negativo, other.saldo_negativo)) {
            return false;
        }
        if (!Objects.equals(this.extras, other.extras)) {
            return false;
        }
        if (!Objects.equals(this.observacao, other.observacao)) {
            return false;
        }
        if (!Objects.equals(this.funcionario, other.funcionario)) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        if (!Objects.equals(this.folhaperiodo, other.folhaperiodo)) {
            return false;
        }
        if (!Objects.equals(this.data_Batida, other.data_Batida)) {
            return false;
        }
        return true;
    }

}

    //Gets and Seters
