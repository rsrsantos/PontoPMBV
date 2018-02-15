package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.PtoArquivo;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class PtoArquivoFacade extends GenericDAO<PtoArquivo> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public PtoArquivoFacade() {
        super(PtoArquivo.class);
    }

    public boolean existe(int nsr) {
        Query query = em.createQuery("select p from PtoArquivo p where p.nsr =:nsr");
        query.setParameter("nsr", nsr);
        try {
            PtoArquivo resultado = (PtoArquivo) query.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
        em.close();
        return true;
    }

}
