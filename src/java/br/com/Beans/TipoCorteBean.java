package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.Model.TipoCorte;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class TipoCorteBean implements Serializable {

    TipoCorte tipocorte;

    public TipoCorteBean() {

        if (tipocorte == null) {
            tipocorte = new TipoCorte();
        }

    }

    public List<TipoCorte> getTipoCortes() {
        return new GenericDAO<>(TipoCorte.class).listaTodos();

    }

    public String tipocorte() {
        try {
            if (tipocorte.getId() == 0) {
                new GenericDAO<>(TipoCorte.class).adiciona(this.tipocorte);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");
                return "/View/equipamento/lista-modelo/index?faces-redirect=true";

            } else {
                new GenericDAO<>(TipoCorte.class).atualiza(this.tipocorte);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");
                return "/View/equipamento/lista-modelo/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
            System.err.println("ERRO: " + e);
        }
        return null;
    }

    public String excluir(TipoCorte corte) {
        new GenericDAO<>(TipoCorte.class
        ).remove(corte);
        return "/View/equipamento/ModeloEquipamento";
    }

    public String editar(TipoCorte corte) {
        this.tipocorte = corte;

        return "/View/equipamento/cadastrar-tipoCorte/index";

    }

    //Gets and Setrs
    public TipoCorte getTipocorte() {
        return tipocorte;
    }

    public void setTipocorte(TipoCorte tipocorte) {
        this.tipocorte = tipocorte;
    }

}
