package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Horario;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class HorarioFacade extends GenericDAO<Horario> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public HorarioFacade() {
        super(Horario.class);
    }

    public long contaHorarioTotal() {
        Query q = em.createQuery("select count(u) from Horario u");
        long contador = (long) q.getSingleResult();
        return contador;
    }

    public Horario buscaHorario(String nome) {
        try {
            Query query = em.createQuery("select u from Horario u where u.nome = :nome");
            query.setParameter("nome", nome);
            Horario horario = (Horario) query.getSingleResult();
            return horario;
        } catch (NoResultException e) {
        }
        return null;

    }

}
