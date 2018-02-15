package br.com.Model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class PtoEvento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "arquivo")
    private PtoArquivo arquivo;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_evento")
    private Date dataEvento;

    @ManyToOne
    @JoinColumn(name = "equipamento")
    private PtoEquipamento ptoEquipamento;

    @Temporal(TemporalType.TIME)
    @Column(name = "hora_evento")
    private Date horaEvento;

    private Integer nsr;

    @ManyToOne
    @JoinColumn(name = "pessoa")
    private Funcionario funcionario;

    private String pis;

    private Boolean processado;

    private Integer tipo;

    public PtoEvento() {
    }

    @Override
    public String toString() {
        return "PtoEvento{" + "id=" + id + ", arquivo=" + arquivo + ", dataEvento=" + dataEvento + ", ptoEquipamento=" + ptoEquipamento + ", horaEvento=" + horaEvento + ", nsr=" + nsr + ", funcionario=" + funcionario + ", pis=" + pis + ", processado=" + processado + ", tipo=" + tipo + '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PtoArquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(PtoArquivo arquivo) {
        this.arquivo = arquivo;
    }

    public Date getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(Date dataEvento) {
        this.dataEvento = dataEvento;
    }

    public PtoEquipamento getPtoEquipamento() {
        return ptoEquipamento;
    }

    public void setPtoEquipamento(PtoEquipamento ptoEquipamento) {
        this.ptoEquipamento = ptoEquipamento;
    }

    public Date getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(Date horaEvento) {
        this.horaEvento = horaEvento;
    }

    public Integer getNsr() {
        return nsr;
    }

    public void setNsr(Integer nsr) {
        this.nsr = nsr;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public Boolean getProcessado() {
        return processado;
    }

    public void setProcessado(Boolean processado) {
        this.processado = processado;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    
    
    

}
