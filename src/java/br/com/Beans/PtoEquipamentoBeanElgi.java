package br.com.Beans;

import ZPM.ClienteTCP;
import ZPM.RepZPM;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.PtoEquipamentoElgin;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class PtoEquipamentoBeanElgi implements Serializable {

    RepZPM repzpm = new RepZPM();

    public PtoEquipamentoElgin getPtoequipamentoelgin() {
        return ptoequipamentoelgin;
    }
    ClienteTCP tcp = new ClienteTCP();

    private PtoEquipamentoElgin ptoequipamentoelgin = new PtoEquipamentoElgin();

    final private org.apache.log4j.Logger logger = Logger.getLogger(PtoEquipamentoBeanElgi.class.getName());

    final List<String> listaRegistros1 = new ArrayList<>();

    EntityManager manager1 = new JPAConect().getEntityManager();

    public PtoEquipamentoElgin getPtoEquipamentoElgin() {
        return ptoequipamentoelgin;
    }

    public List<PtoEquipamentoElgin> getPtoEquipamentosElgins() {
        return new GenericDAO<>(PtoEquipamentoElgin.class).listaTodos();

    }

    

    public EntityManager getManager() {
        return manager1;
    }

    public void setManager(EntityManager manager) {
        this.manager1 = manager;
    }

    public String gravarEquipamento() {
        try {

            if (ptoequipamentoelgin.getId() == 0) {
                new GenericDAO<>(PtoEquipamentoElgin.class
                ).adiciona(ptoequipamentoelgin);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                return "/View/equipamento/receAfd_Elgin?faces-redirect=true";

            } else {
                new GenericDAO<>(PtoEquipamentoElgin.class
                ).atualiza(ptoequipamentoelgin);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");
                return "/View/equipamento/receAfd_Elgin?faces-redirect=true";
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;

    }

    public String excluir(PtoEquipamentoElgin ptoequipamentoelgin) {
        new GenericDAO<>(PtoEquipamentoElgin.class
        ).remove(ptoequipamentoelgin);
        Msg.addMsgFatal("Exclusão Realizada");
        return "/View/equipamento/receAfd_Elgin";

    }

    public String editar(PtoEquipamentoElgin ptoequipamentoelgin) {
        this.ptoequipamentoelgin = ptoequipamentoelgin;

        return "/View/equipamento/cadEquipamentoElgin";

    }

    
    
    public String listar() {
        return "/View/equipamento/equipamento-Elgin/index?faces-redirect=true";
    }

}
