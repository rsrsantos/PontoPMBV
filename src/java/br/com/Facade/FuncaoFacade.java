package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Funcao;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class FuncaoFacade extends GenericDAO<Funcao> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public FuncaoFacade() {
        super(Funcao.class);
    }

    public Funcao buscaFuncao(String descricao) {
        try {
            Query query = em.createQuery("select u from Funcao u where u.descricao = :descricao");
            query.setParameter("descricao", descricao);
            Funcao funcao = (Funcao) query.getSingleResult();
            return funcao;
        } catch (NoResultException e) {
        }
        return null;

    }

    public long contaFuncaoTotal() {
        Query q = em.createQuery("select count(u) from Funcao u");
        long contador = (long) q.getSingleResult();
        return contador;
    }

}
