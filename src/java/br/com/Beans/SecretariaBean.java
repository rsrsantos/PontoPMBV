package br.com.Beans;

import br.com.Facade.SecretariaFacade;
import br.com.DAO.GenericDAO;
import br.com.Model.Empresa;
import br.com.Model.Funcionario;
import br.com.Model.Secretaria;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class SecretariaBean implements Serializable {

    private List<Funcionario> secretarios;

    Secretaria secretaria;
    SecretariaFacade secretariaFacade;
    Usuario usuario;

    public SecretariaBean() {

        if (secretariaFacade == null) {
            secretariaFacade = new SecretariaFacade();
        }

        if (secretaria == null) {
            secretaria = new Secretaria();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }
    }

    public List<Secretaria> getSecretarias() {
        return new GenericDAO<>(Secretaria.class).listaTodos();

    }

    public List<Empresa> getEmpresas() {
        return new GenericDAO<>(Empresa.class).listaTodos();

    }

    public String gravarSecretaria() {
        try {

            Secretaria secretariaVerifica = secretariaFacade.buscaSecretaria(secretaria.getNome());

            if (secretaria.getId() == 0) {

                if (secretariaVerifica != null) {
                    Msg.addMsgWarn("Secretaria já cadastrada");

                } else {

                    secretariaFacade.adiciona(secretaria);
                    Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);

                    return "/View/secretaria/lista-secretaria/index?faces-redirect=true";
                }
            } else {
                secretariaFacade.atualiza(secretaria);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/secretaria/lista-secretaria/index.xhtml?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String novaSecretaria() {

        try {
            usuario = pegaUsuario();
            secretaria = new Secretaria();
            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {

                return "/View/secretaria/cadastrar-secretaria/index?faces-redirect=true";

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

    public String excluir(Secretaria secretaria) {

        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                secretariaFacade.remove(secretaria);
                return "/View/secretaria/lista-secretaria/index";

            } else {
                Msg.addMsgFatal("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());
                System.err.println("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());
            }
            return null;
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }
        return null;

    }

    public String editar(Secretaria secretaria) {
        try {
            this.secretaria = secretaria;
            return "/View/secretaria/cadastrar-secretaria/index";
        } catch (Exception e) {
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
        return "/View/secretaria/lista-secretaria/index?faces-redirect=true";
    }

//Gets and Setrs
    public Secretaria getSecretaria() {
        return secretaria;
    }

    public void setSecretaria(Secretaria secretaria) {
        this.secretaria = secretaria;
    }

    public List<Funcionario> getSecretarios() {
        secretarios = secretariaFacade.buscaSecretario();
        return secretarios;
    }

    public void setSecretarios(List<Funcionario> secretarios) {
        this.secretarios = secretarios;
    }

}
