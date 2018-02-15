package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Empresa;
import br.com.Model.Funcionario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class EmpresaFacade extends GenericDAO<Empresa> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public EmpresaFacade() {
        super(Empresa.class);
    }

    @Override
    public List<Empresa> listaTodos() {
        Query query = em.createQuery("select e from Empresa e");
        return query.getResultList();
    }

    public long contaEmpresaTotal() {
        Query q = em.createQuery("select count(u) from Empresa u");
        long contador = (long) q.getSingleResult();
        return contador;
    }

    public Empresa buscaDepartamento(String descricao) {
        try {
            Query query = em.createQuery("select u from Empresa u where u.empresa = :descricao");
            query.setParameter("descricao", descricao);
            Empresa empresa = (Empresa) query.getSingleResult();
            return empresa;
        } catch (NoResultException e) {
        }
        return null;

    }

}
