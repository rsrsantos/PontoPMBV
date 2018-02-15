package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Escala;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class EscalaFacade extends GenericDAO<Escala> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public EscalaFacade() {
        super(Escala.class);
    }
    
    
       public List<Escala> listaEscala() {
        Query q = em.createQuery("select e from Escala e");
        return q.getResultList();
    }

}
