package br.com.Beans;

import br.com.Facade.JustificativaFacade;
import br.com.Core.CoreUtils;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Funcionario;
import br.com.Model.Justificativa;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

@ManagedBean
@SessionScoped
public class JustificativaBean implements Serializable {

    Justificativa justificativa;
    JustificativaFacade justificativafacade;
    Usuario usuario;

    EntityManager manager = new JPAConect().getEntityManager();
    String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");

    public JustificativaBean() {

        if (justificativa == null) {
            justificativa = new Justificativa();
        }

        if (justificativafacade == null) {
            justificativafacade = new JustificativaFacade();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

    }

    public List<Justificativa> getJustificativas() {
        return new GenericDAO<>(Justificativa.class).listaTodos();
    }

    public List<Funcionario> getFuncionarios() {
        return new GenericDAO<>(Funcionario.class).listaTodos();
    }

    public String gravarJustificativa() {

        try {

            usuario = pegaUsuario();
            Justificativa justificativaVerifica = justificativafacade.buscaJustificativa(justificativa.getDescricao());

            if (justificativa.getId() == 0) {

                if (justificativaVerifica != null) {
                    Msg.addMsgWarn("Justificativa já cadastrada");

                } else {

                    justificativa.setUsuario(usuario);
                    justificativa.setUltima_alteracao(datahora);

                    justificativafacade.adiciona(justificativa);
                    Msg.addMsgInfo("Cadastro realizado com sucesso! ");
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);

                    return "/View/justificativa/lista-justificativa/index?faces-redirect=true";
                }
            } else {
                justificativa.setUsuario(usuario);
                justificativa.setUltima_alteracao(datahora);

                justificativafacade.atualiza(justificativa);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/justificativa/lista-justificativa/index?faces-redirect=true";

            }
        } catch (Exception e) {

            Msg.addMsgError("Operação não realizada!" + e);
            System.err.println("ERRO: " + e);
        }
        return null;
    }

    public String excluir(Justificativa justificativa) {

        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {

                justificativafacade.remove(justificativa);
                Msg.addMsgFatal("Excluido com Sucesso: " + justificativa.getNome());
                return "/View/justificativa/lista-justificativa/index?faces-redirect=true";

            } else {

                Msg.addMsgFatal("Somente administrado Sr(a) " + usuario.getFuncionario().getNome());
                System.err.println("Somente administrado Sr(a) " + usuario.getFuncionario().getNome());
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada! " + e);
            System.err.println("ERRO: " + e);
        }
        return null;
    }

    public String editar(Justificativa justificativa) {

        try {

            this.justificativa = justificativa;
            return "/View/justificativa/cadastrar-justificativa/index";

        } catch (Exception e) {
            System.err.println("ERRO: " + e);
        }
        return null;

    }

    public String novaJustificativa() {

        try {

            usuario = pegaUsuario();
            justificativa = new Justificativa();
            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {

                return "/View/justificativa/cadastrar-justificativa/index?faces-redirect=true";

            } else {
                System.err.println("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());
                Msg.addMsgFatal("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());
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

    public String listar() {
        return "/View/justificativa/lista-justificativa/index?faces-redirect=true";
    }

    //Gets and setrs 
    public Justificativa getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(Justificativa justificativa) {
        this.justificativa = justificativa;
    }

}
