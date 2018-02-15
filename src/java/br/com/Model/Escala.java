package br.com.Model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Escala implements Serializable{
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "horario_id")
    private Horario horario;
    
    private String descricao;
    
    @ManyToMany
    @JoinColumn(name = "funcionario_id")
    private List<Funcionario> funcionario;
    
    @ManyToOne
    @JoinColumn(name = "periodo_id")
    private FolhaPeriodo periodo;
    
    private long dias_trabalho;
    
    private long dias_folga;
    
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Funcionario> getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(List<Funcionario> funcionario) {
        this.funcionario = funcionario;
    }

    public FolhaPeriodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(FolhaPeriodo periodo) {
        this.periodo = periodo;
    }

    public long getDias_trabalho() {
        return dias_trabalho;
    }

    public void setDias_trabalho(long dias_trabalho) {
        this.dias_trabalho = dias_trabalho;
    }

    public long getDias_folga() {
        return dias_folga;
    }

    public void setDias_folga(long dias_folga) {
        this.dias_folga = dias_folga;
    }
    
    
    
    
}
