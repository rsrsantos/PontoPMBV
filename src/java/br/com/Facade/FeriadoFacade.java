package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.EspelhoPonto;
import br.com.Model.Feriado;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class FeriadoFacade extends GenericDAO<Feriado> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public FeriadoFacade() {
        super(Feriado.class);
    }

    public long contaFeriadoTotal() {
        Query q = em.createQuery("select count(u) from Feriado u");
        long contador = (long) q.getSingleResult();
        return contador;
    }

    public List<EspelhoPonto> buscaBatidasByDate(Date data_batida, String departamento) {
        Query q = em.createQuery("select p from EspelhoPonto p where  p.data_Batida =:data_batida AND p.departamento =:departamento");
        q.setParameter("data_batida", data_batida);
        q.setParameter("departamento", departamento);
        return (List<EspelhoPonto>) q.getResultList();
    }

    public Feriado buscaFeriado(String nome) {
        try {
            Query query = em.createQuery("select u from Feriado u where u.nome = :nome");
            query.setParameter("nome", nome);
            Feriado feriado = (Feriado) query.getSingleResult();
            return feriado;
        } catch (NoResultException e) {
        }
        return null;

    }

}
