package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Justificativa;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class JustificativaFacade extends GenericDAO<Justificativa> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public JustificativaFacade() {
        super(Justificativa.class);
    }

    public Justificativa buscaJustificativa(String descricao) {
        try {
            Query query = em.createQuery("select u from Justificativa u where u.descricao = :descricao");
            query.setParameter("descricao", descricao);
            Justificativa justificativa = (Justificativa) query.getSingleResult();
            return justificativa;
        } catch (NoResultException e) {
        }
        return null;

    }

}
