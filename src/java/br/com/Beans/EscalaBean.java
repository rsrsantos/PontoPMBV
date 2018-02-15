package br.com.Beans;

import br.com.Facade.EscalaFacade;
import br.com.Model.Escala;
import br.com.Model.Horario;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class EscalaBean implements Serializable {

    Escala escala;
    EscalaFacade escalaFacade;
    Usuario usuario;

    List<Escala> ListEscala;
    List<Horario> ListHorario;
    

    public EscalaBean() {

        if (escala == null) {
            escala = new Escala();
        }

        if (escalaFacade == null) {
            escalaFacade = new EscalaFacade();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

    }

    public String gravarEscala() {
        try {
            if (escala.getId() == 0) {
                escalaFacade.adiciona(escala);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/equipamento/lista-modelo/index?faces-redirect=true";

            } else {
                escalaFacade.atualiza(escala);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/equipamento/lista-modelo/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!" + e);
            System.err.println("ERRO: " + e);
        }
        return null;
    }

    public String excluir(Escala escala) {
        escalaFacade.remove(escala);
        return "/View/equipamento/lista-modelo/index";
    }

    public String editar(Escala escala) {
        this.escala = escala;

        return "/View/equipamento/cadastrar-modelo/index";

    }

    public String novaEscala() {
        try {
            usuario = pegaUsuario();
            escala = new Escala();
            if (usuario.getUsuarioPerfil().getDescricao().equals("admin")) {
                return "/View/escala-trabalho/cadastro-escala/index";
            } else {
                Msg.addMsgWarn("Somente Administrador");
                System.err.println("Somente Administrador");
            }
        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada " + e);
            System.err.println("ERRO: " + e);

        }
        return null;
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
    public Escala getEscala() {
        return escala;
    }

    public void setEscala(Escala escala) {
        this.escala = escala;
    }

    public EscalaFacade getEscalaFacade() {
        return escalaFacade;
    }

    public void setEscalaFacade(EscalaFacade escalaFacade) {
        this.escalaFacade = escalaFacade;
    }

    public List<Escala> getListEscala() {

        ListEscala = escalaFacade.listaEscala();
        return ListEscala;
    }

    public void setListEscala(List<Escala> ListEscala) {
        this.ListEscala = ListEscala;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Horario> getListHorario() {
        return ListHorario;
    }

    public void setListHorario(List<Horario> ListHorario) {
        this.ListHorario = ListHorario;
    }
    
    

}
