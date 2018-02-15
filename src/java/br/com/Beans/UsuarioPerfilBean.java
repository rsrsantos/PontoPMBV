package br.com.Beans;

import br.com.Facade.UsuarioPerfilFacade;
import br.com.Model.Usuario;
import br.com.Core.CoreUtils;
import br.com.Model.UsuarioPerfil;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class UsuarioPerfilBean implements Serializable {

    String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");

    Usuario usuario;
    UsuarioPerfil usuarioperfil;
    UsuarioPerfilFacade usuarioperfilfacade;
    Usuario usuarioLogadoSistema;

    List<UsuarioPerfil> listaUsuarioPerfil;

    public UsuarioPerfilBean() {

        if (usuarioperfil == null) {
            usuarioperfil = new UsuarioPerfil();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

        if (usuarioperfilfacade == null) {
            usuarioperfilfacade = new UsuarioPerfilFacade();
        }

        if (usuarioLogadoSistema == null) {
            usuarioLogadoSistema = new Usuario();
        }
    }

    public String gravar() {
        try {
            if (usuarioperfil.getId() == 0) {
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");
                usuarioperfilfacade.adiciona(usuarioperfil);
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/usuarioperfil/lista-usuarioperfil/index?faces-redirect=true";

            } else {
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");
                usuarioperfilfacade.atualiza(usuarioperfil);
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/usuarioperfil/lista-usuarioperfil/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    //
    public String novoUsuarioPerfil() {

        try {
            usuarioLogadoSistema = pegaUsuario();
            usuarioperfil = new UsuarioPerfil();
            if (usuarioLogadoSistema.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                return "/View/usuarioperfil/cadastrar-usuarioperfil/index?faces-redirect=true";

            } else {
                Msg.addMsgFatal("Somente Administrador Sr(a) " + usuarioLogadoSistema.getFuncionario().getNome());
                System.err.println("Somente Administrador Sr(a) " + usuarioLogadoSistema.getFuncionario().getNome());
            }
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

        return null;
    }

    //
    public String excluir(UsuarioPerfil usuario) {

        try {

            usuarioLogadoSistema = pegaUsuario();

            if (usuarioLogadoSistema.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                usuarioperfilfacade.remove(usuario);
                Msg.addMsgFatal("Usuario Excluido com Sucesso");
                return "/View/usuario/lista-usuario/index";

            } else {

                Msg.addMsgFatal("Não Autorizado Sr(a) " + usuarioLogadoSistema.getFuncionario().getNome());
            }
        } catch (Exception e) {

            Msg.addMsgError("Operação não realizada!");
            System.err.println("ERRO: " + e);
        }
        return null;
    }

    //
    public String editar(UsuarioPerfil usuario) {
        this.usuarioperfil = usuario;
        return "/View/usuarioperfil/cadastrar-usuarioperfil/index";

    }
    //

    public Usuario pegaUsuario() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
            return usuarioLogado;
        } catch (Exception e) {
        }
        return null;

    }

    public List<UsuarioPerfil> getListaUsuarioPerfil() {
        return listaUsuarioPerfil = usuarioperfilfacade.listaTodosUsuariosPerfil();
    }

    public void setListaUsuarioPerfil(List<UsuarioPerfil> listaUsuarioPerfil) {
        this.listaUsuarioPerfil = listaUsuarioPerfil;
    }

    public UsuarioPerfil getUsuarioperfil() {
        return usuarioperfil;
    }

    public void setUsuarioperfil(UsuarioPerfil usuarioperfil) {
        this.usuarioperfil = usuarioperfil;
    }

}
