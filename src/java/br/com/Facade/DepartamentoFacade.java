package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Departamento;
import br.com.Model.Empresa;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class DepartamentoFacade extends GenericDAO<Departamento> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public DepartamentoFacade() {
        super(Departamento.class);
    }

    public long contaDepartamentoTotal() {
        Query q = em.createQuery("select count(u) from Departamento u");
        long contador = (long) q.getSingleResult();
        return contador;
    }

    public List<Empresa> buscaEmpresas() {
        Query q = em.createQuery("select e from Empresa e ");
        return (List<Empresa>) q.getResultList();
    }

    public Departamento buscaDepartamento(String descricao) {
        try {
            Query query = em.createQuery("select u from Departamento u where u.descricao = :descricao");
            query.setParameter("descricao", descricao);
            Departamento departamento = (Departamento) query.getSingleResult();
            return departamento;
        } catch (NoResultException e) {
        }
        return null;

    }

}
