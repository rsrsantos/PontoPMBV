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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PtoArquivo")
public class PtoArquivo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OrderBy(value = "nsr")
    @Column(name = "nsr", length = 9)
    private int nsr;

    @Column(name = "tipo", length = 1)
    private int tipo;

    @Column(name = "pis", length = 12)
    private String pis;

    @Column(name = "hora", length = 6)
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date hora;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data_batida;

    @Column(name = "Ptooriginal")
    private String Ptooriginal;

    @Column(name = "equipamento", length = 50)
    private String equipamento;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "data_importacao")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataImportacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String periodo;

    @Column(name = "arquivo_fisico")
    private Integer arquivoFisico;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_ultimonsr")
    private Date dataUltimonsr;

    private String hash;

    private Boolean processado;


    public PtoArquivo() {
    }

    //Gets and Setrs
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public String getPtooriginal() {
        return Ptooriginal;
    }

    public void setPtooriginal(String Ptooriginal) {
        this.Ptooriginal = Ptooriginal;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public int getNsr() {
        return nsr;
    }

    public void setNsr(int nsr) {
        this.nsr = nsr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Date getDataImportacao() {
        return dataImportacao;
    }

    public void setDataImportacao(Date dataImportacao) {
        this.dataImportacao = dataImportacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Date getData_batida() {
        return data_batida;
    }

    public void setData_batida(Date data_batida) {
        this.data_batida = data_batida;
    }

    public Integer getArquivoFisico() {
        return arquivoFisico;
    }

    public void setArquivoFisico(Integer arquivoFisico) {
        this.arquivoFisico = arquivoFisico;
    }


    public Date getDataUltimonsr() {
        return dataUltimonsr;
    }

    public void setDataUltimonsr(Date dataUltimonsr) {
        this.dataUltimonsr = dataUltimonsr;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Boolean getProcessado() {
        return processado;
    }

    public void setProcessado(Boolean processado) {
        this.processado = processado;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }
    
    
    
    

}
