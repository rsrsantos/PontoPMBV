package br.com.Beans;

import br.com.Facade.FuncaoFacade;
import br.com.DAO.GenericDAO;
import br.com.Model.Funcao;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class FuncaoBean implements Serializable {

    Funcao funcao;
    FuncaoFacade funcaofacade;
    Usuario usuario;

    private long totalFuncao;

    public FuncaoBean() {

        if (funcaofacade == null) {
            funcaofacade = new FuncaoFacade();
        }

        if (funcao == null) {
            funcao = new Funcao();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

    }

    public List<Funcao> getFuncaos() {
        return new GenericDAO<>(Funcao.class).listaTodos();

    }

    public String gravarFuncao() {
        try {

            Funcao funcaoValida = funcaofacade.buscaFuncao(funcao.getDescricao());
            if (funcao.getId() == 0) {

                if (funcaoValida != null) {
                    Msg.addMsgWarn("Funcão já cadastrada ");

                } else {

                    funcaofacade.adiciona(funcao);
                    Msg.addMsgInfo("Cadastro realizado com sucesso! ");
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);

                    return "/View/funcao/lista-funcao/index?faces-redirect=true";
                }
            } else {

                funcaofacade.atualiza(funcao);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);
                return "/View/funcao/lista-funcao/index?faces-redirect=true";

            }

        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
            System.err.println("ERRO: " + e);
        }
        return null;
    }

    public String excluir(Funcao funcao) {

        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {

                funcaofacade.remove(funcao);
                return "/View/lista-funcao/index";

            } else {

                Msg.addMsgFatal("Não Autorizado!");
                System.err.println("Somente administrador ");
            }
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);

        }

        return null;
    }

    public String novaFuncao() {

        try {

            usuario = pegaUsuario();
            funcao = new Funcao();
            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                return "/View/funcao/cadastrar-funcao/index?faces-redirect=true";

            } else {

                System.err.println("Somente administrador" + usuario.getFuncionario().getNome());
                Msg.addMsgFatal("Somente Administrador Sr(a) " + usuario.getFuncionario().getNome());
            }
        } catch (Exception e) {

            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

        return null;

    }

    public String listar() {
        return "/View/funcao/lista-funcao/index?faces-redirect=true";
    }

    public String editar(Funcao funcao) {

        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                this.funcao = funcao;

                return "/View/funcao/cadastrar-funcao/index";

            } else {
                Msg.addMsgFatal("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());
                System.err.println("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());

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

//Gets and Setrs
    public long getTotalFuncao() {
        return totalFuncao = funcaofacade.contaFuncaoTotal();
    }

    public void setTotalFuncao(long totalFuncao) {
        this.totalFuncao = totalFuncao;
    }

    public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

}
