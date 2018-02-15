package br.com.Beans;

import br.com.Facade.Historico_usuarioFacade;
import br.com.Model.Usuario;
import br.com.Model.UsuarioHistorico;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class UsuarioHistoricoBean implements Serializable {

    UsuarioHistorico usuarioHistorico;
    Usuario usuario;
    Historico_usuarioFacade historico_usuarioFacade;

    List<UsuarioHistorico> usuariosUsuarioHistoricos;

    public UsuarioHistoricoBean() {

        if (usuarioHistorico == null) {
            usuarioHistorico = new UsuarioHistorico();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

        if (historico_usuarioFacade == null) {
            historico_usuarioFacade = new Historico_usuarioFacade();
        }

    }

    public Usuario pegaUsuario() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
            return usuarioLogado;
        } catch (Exception e) {
        }
        return null;

    }

    //Gets and setrs
    public UsuarioHistorico getUsuarioHistorico() {
        return usuarioHistorico;
    }

    public void setUsuarioHistorico(UsuarioHistorico usuarioHistorico) {
        this.usuarioHistorico = usuarioHistorico;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Historico_usuarioFacade getHistorico_usuarioFacade() {
        return historico_usuarioFacade;
    }

    public void setHistorico_usuarioFacade(Historico_usuarioFacade historico_usuarioFacade) {
        this.historico_usuarioFacade = historico_usuarioFacade;
    }

    public List<UsuarioHistorico> getUsuariosUsuarioHistoricos() {

        usuario = pegaUsuario();
        usuariosUsuarioHistoricos = historico_usuarioFacade.listaHistoricoUsuario(usuario);

        return usuariosUsuarioHistoricos;
    }

    public void setUsuariosUsuarioHistoricos(List<UsuarioHistorico> usuariosUsuarioHistoricos) {
        this.usuariosUsuarioHistoricos = usuariosUsuarioHistoricos;
    }

}
