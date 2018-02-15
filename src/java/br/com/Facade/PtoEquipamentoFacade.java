package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Departamento;
import br.com.Model.PtoArquivo;
import br.com.Model.PtoEquipamento;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

public class PtoEquipamentoFacade extends GenericDAO<PtoEquipamento> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public PtoEquipamentoFacade() {
        super(PtoEquipamento.class);
    }

    public PtoEquipamento buscaEquipamentoHenry(String descricao) {
        try {
            Query query = em.createQuery("select u from PtoEquipamento u where u.descricao = :descricao");
            query.setParameter("descricao", descricao);
            PtoEquipamento ptoEquipamento = (PtoEquipamento) query.getSingleResult();
            return ptoEquipamento;
        } catch (NoResultException e) {
        }
        return null;

    }

    public boolean existe(int nsr) {
        Query query = em.createQuery("select p from PtoArquivo p where p.nsr =:nsr");
        query.setParameter("nsr", nsr);
        try {
            PtoArquivo resultado = (PtoArquivo) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            return false;
        }
        em.close();
        return true;
    }

    public List<Departamento> listaTodosDepartamentos() {
        Query query = em.createQuery("select d from Departamento d");
        return query.getResultList();
    }

    public List<PtoEquipamento> listaTodosEquipamentos() {
        Query query = em.createQuery("select p from PtoEquipamento p");
        return query.getResultList();
    }

    public List<PtoEquipamento> ListEquipamentoDepartamento(Departamento departamento) {
        Query q = em.createQuery("select e from PtoEquipamento e JOIN e.departamento d WHERE d.id =:departamento");
        q.setParameter("departamento", departamento.getId());
        return q.getResultList();
    }

}
