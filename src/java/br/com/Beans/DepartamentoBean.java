package br.com.Beans;

import br.com.Facade.DepartamentoFacade;
import br.com.DAO.GenericDAO;
import br.com.Model.Departamento;
import br.com.Model.Empresa;
import br.com.Model.Secretaria;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@ManagedBean
@ViewScoped
public class DepartamentoBean implements Serializable {

    Departamento departamento;
    DepartamentoFacade departamentofacade;
    Usuario usuario;

    List<Empresa> Listempresas;

    private long totalDepartamentos;

    public DepartamentoBean() {

        if (departamento == null) {
            departamento = new Departamento();
        }

        if (departamentofacade == null) {
            departamentofacade = new DepartamentoFacade();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }
    }

    public List<Departamento> getDepartamentos() {
        return new GenericDAO<>(Departamento.class).listaTodos();

    }

    public List<Secretaria> getSecretarias() {
        return new GenericDAO<>(Secretaria.class).listaTodos();

    }

    public String gravarDepartamento() {
        try {

            Departamento departamentoValida = departamentofacade.buscaDepartamento(departamento.getDescricao());
            if (departamento.getId() == 0) {

                if (departamentoValida != null) {
                    Msg.addMsgWarn("Departamento já cadastrado ");

                } else {

                    departamentofacade.adiciona(departamento);
                    Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);

                    return "/View/departamento/lista-departamento/index?faces-redirect=true";
                }
            } else {

                departamentofacade.atualiza(departamento);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/departamento/lista-departamento/index?faces-redirect=true";

            }

        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
            System.err.println("ERRO: " + e);
        }
        return null;
    }

    public String novoDepartamento() {

        try {
            usuario = pegaUsuario();
            departamento = new Departamento();
            if (usuario.getUsuarioPerfil().getDescricao().equals("admin")) {
                return "/View/departamento/cadastrar-departamento/index?faces-redirect=true";
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

    public String listar() {
        return "/View/departamento/lista-departamento/index?faces-redirect=true";
    }

    public String excluir(Departamento departamento) {

        try {
            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                departamentofacade.remove(departamento);

                return "/View/departamento/lista-departamento/index";

            } else {

                Msg.addMsgFatal("Somente Administrador");
                System.err.println("Somente Administrador");
            }

        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada");
            System.err.println("ERRO: " + e);
        }

        return null;

    }

    public String editar(Departamento departamento) {
        try {

            this.departamento = departamento;
            return "/View/departamento/cadastrar-departamento/index";

        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada" + e);
            System.err.println("ERRO: " + e);
        }
        return null;

    }


//Gets and Setrs
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public long getTotalDepartamentos() {
        return totalDepartamentos = departamentofacade.contaDepartamentoTotal();
    }

    public void setTotalDepartamentos(long totalDepartamentos) {
        this.totalDepartamentos = totalDepartamentos;
    }

    public List<Empresa> getListempresas() {
        Listempresas = departamentofacade.buscaEmpresas();
        return Listempresas;
    }

    public void setListempresas(List<Empresa> Listempresas) {
        this.Listempresas = Listempresas;
    }

}
