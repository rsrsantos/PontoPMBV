package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import javax.faces.bean.ManagedBean;

import br.com.Model.Agendamento;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

@ManagedBean
public class TarefaAgendadaBean implements Serializable {

    EntityManager manager = new JPAConect().getEntityManager();
    private Agendamento agendamento = new Agendamento();

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public List<Agendamento> getAgendamentos() {
        return new GenericDAO<>(Agendamento.class).listaTodos();

    }

    public String gravarAgendamento() {

        if (agendamento.getId() == 0) {
            new GenericDAO<>(Agendamento.class).adiciona(agendamento);
            Msg.addMsgInfo("Agendamento realizado com sucesso! ");
           return "/View/agendamento/agendamento?faces-redirect=true";

        } else {
            new GenericDAO<>(Agendamento.class).atualiza(agendamento);
            Msg.addMsgInfo("Agendamento atualizado com sucesso! ");
            return "/View/agendamento/agendamento?faces-redirect=true";
        }

    }

    public String excluirAgendamento(Agendamento agendamento) {
        new GenericDAO<>(Agendamento.class
        ).remove(agendamento);
        return "/View/agendamento/agendamento";
    }

    public String editar(Agendamento agendamento) {
        this.agendamento = agendamento;
        return "/View/agendamento/cadAgendamento";

    }

}
