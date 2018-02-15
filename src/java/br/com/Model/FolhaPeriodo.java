package br.com.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;




@Entity
public class FolhaPeriodo implements Serializable {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String periodo;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inicioFolha;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fimFolha;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    private String ultima_Alteracao;
    
    private String periodoAnterior;
    
    private String status;
    
    
    
    
    //Gets and Setrs

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getUltima_Alteracao() {
        return ultima_Alteracao;
    }

    public void setUltima_Alteracao(String ultima_Alteracao) {
        this.ultima_Alteracao = ultima_Alteracao;
    }

    public Date getInicioFolha() {
        return inicioFolha;
    }

    public void setInicioFolha(Date inicioFolha) {
        this.inicioFolha = inicioFolha;
    }

    public Date getFimFolha() {
        return fimFolha;
    }

    public void setFimFolha(Date fimFolha) {
        this.fimFolha = fimFolha;
    }

    public String getPeriodoAnterior() {
        return periodoAnterior;
    }

    public void setPeriodoAnterior(String periodoAnterior) {
        this.periodoAnterior = periodoAnterior;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    //=============================================================================

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 47 * hash + Objects.hashCode(this.periodo);
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
        final FolhaPeriodo other = (FolhaPeriodo) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.periodo, other.periodo)) {
            return false;
        }
        return true;
    }
    
    
    
}
