package br.com.Model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "Usuario_historico")
public class UsuarioHistorico implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
//	@ManyToOne(cascade=CascadeType.REFRESH)
//	@JoinColumn(name="pessoa")
//	private Pessoa pessoa;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="tipo_acao")
	private TipoAcaoMotivo tipoAcao;
	
	private String descricao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_registro")
	private Date dataRegistro;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="usuario")
	private Usuario usuario;

//	public UsuarioHistorico() {
//		this.usuario = new Usuario();
//	}
//	
        
        //Gets and sers

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoAcaoMotivo getTipoAcao() {
        return tipoAcao;
    }

    public void setTipoAcao(TipoAcaoMotivo tipoAcao) {
        this.tipoAcao = tipoAcao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

        
}
