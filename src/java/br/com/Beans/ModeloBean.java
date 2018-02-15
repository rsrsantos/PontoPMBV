package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.Facade.ModeloFacade;
import br.com.Model.Modelo;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class ModeloBean implements Serializable {

    Modelo modelo;
    ModeloFacade modeloFacade;

    public ModeloBean() {

        if (modelo == null) {
            modelo = new Modelo();
        }

        if (modeloFacade == null) {
            modeloFacade = new ModeloFacade();
        }

    }

    public List<Modelo> getModelos() {
        return new GenericDAO<>(Modelo.class).listaTodos();

    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public String gravarModelo() {
        try {
            if (modelo.getId() == 0) {
                modeloFacade.adiciona(modelo);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/equipamento/lista-modelo/index?faces-redirect=true";

            } else {
                modeloFacade.atualiza(modelo);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/equipamento/lista-modelo/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String excluir(Modelo modelo) {
        modeloFacade.remove(modelo);
        return "/View/equipamento/lista-modelo/index";
    }

    public String editar(Modelo modelo) {
        this.modelo = modelo;

        return "/View/equipamento/cadastrar-modelo/index";

    }

}
