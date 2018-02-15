package br.com.Beans;

import br.com.Facade.EmpresaFacade;
import br.com.Model.Empresa;
import br.com.Model.Usuario;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class EmpresaBean implements Serializable {

    Empresa empresa;
    EmpresaFacade empresafacade;
    Usuario usuario;

    List<Empresa> listaEmpresa;

    private long totalEmpresa;

    public EmpresaBean() {

        if (empresa == null) {
            empresa = new Empresa();
        }

        if (empresafacade == null) {
            empresafacade = new EmpresaFacade();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

    }

    public String gravarEmpresa() {

        try {

            Empresa empresaVerifica = empresafacade.buscaDepartamento(empresa.getEmpresa());
            if (empresa.getId() == 0) {

                if (empresaVerifica != null) {
                    Msg.addMsgWarn("Empresa já cadastrada");
                } else {

                    empresafacade.adiciona(empresa);
                    Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);

                    return "/View/empresa/lista-empresa/index?faces-redirect=true";
                }
            } else {
                empresafacade.atualiza(empresa);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/empresa/lista-empresa/index?faces-redirect=true";
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;

    }

    public String novaEmpresa() {
        try {
            usuario = pegaUsuario();
            empresa = new Empresa();
            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                return "/View/empresa/cadastrar-empresa/index?faces-redirect=true";

            } else {
                System.err.println("Somente Administrador");
                Msg.addMsgFatal("Somente Administrador");
            }
        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada " + e);
            System.err.println("ERRO: " + e);
        }

        return null;
    }

    public String excluir(Empresa empresa) {

        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                empresafacade.remove(empresa);

                Msg.addMsgFatal("Empresa Excluida");
                return "/View/empresa/Empresa";

            } else {
                Msg.addMsgFatal("Somente administrador!");
                System.err.println("Somente Administrador");
            }
        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada " + e);
            System.err.println("ERRO: " + e);
        }

        return null;

    }

    public String editar(Empresa empresa) {

        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                this.empresa = empresa;

                return "/View/empresa/cadastrar-empresa/index";

            } else {
                System.err.println("Somente administrador");
                Msg.addMsgFatal("Somente Administrador");
            }
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

        return null;
    }

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            results.add(query + i);
        }

        return results;
    }

    public void isValidCNPJ(String cnpj) {
        CNPJValidator valida = new CNPJValidator();

        try {
            valida.assertValid(cnpj);
            Msg.addMsgWarn("CNPJ Válido");

        } catch (InvalidStateException e) {
            Msg.addMsgFatal("CNPJ Inválido");
        }

    }

    public String listar() {
        return "/View/empresa/lista-empresa/index?faces-redirect=true";
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
    public List<Empresa> getListaEmpresa() {
        listaEmpresa = empresafacade.listaTodos();
        return listaEmpresa;
    }

    public void setListaEmpresa(List<Empresa> listaEmpresa) {
        this.listaEmpresa = listaEmpresa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public long getTotalEmpresa() {
        return totalEmpresa = empresafacade.contaEmpresaTotal();
    }

    public void setTotalEmpresa(long totalEmpresa) {
        this.totalEmpresa = totalEmpresa;
    }

}
