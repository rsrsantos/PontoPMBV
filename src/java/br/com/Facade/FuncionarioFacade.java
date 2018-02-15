package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Afastamento;
import br.com.Model.Funcionario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class FuncionarioFacade extends GenericDAO implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public FuncionarioFacade() {
        super(Funcionario.class);
    }

    public long contaFuncionarioTotal() {
        Query q = em.createQuery("select count(u) from Funcionario u");
        long contador = (long) q.getSingleResult();
        return contador;
    }

    public Funcionario buscaMatricu(Long matricula) {
        try {
            Query query = em.createQuery("select u from Funcionario u where u.matricula = :matricula");
            query.setParameter("matricula", matricula);
            Funcionario funcionario = (Funcionario) query.getSingleResult();
            return funcionario;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Funcionario buscaEquipamentoId(Long id) {
        Query query = em.createQuery("select u from Funcionario u JOIN u.ptoequipamento e where e.id = :id");
        query.setParameter("id", id);
        Funcionario funcionario = (Funcionario) query.getSingleResult();
        return funcionario;
    }

    public Funcionario buscaFuncionarioId(Long id) {
        Query query = em.createQuery("select u from Funcionario u where u.id = :id");
        query.setParameter("id", id);
        Funcionario funcionario = (Funcionario) query.getSingleResult();
        return funcionario;
    }

    public List<Afastamento> buscaJustificativa(Afastamento afastamento) {
        Query q = em.createQuery("select p from Afastamento p JOIN p.justificativa d WHERE d.id =:id");
        q.setParameter("id", afastamento.getId());
        return (List<Afastamento>) q.getResultList();
    }

    public List<Funcionario> listaFuncionarioDepartamento(Long departamento) {
        Query query = em.createQuery("select p from Funcionario p JOIN p.departamento d WHERE d.id =:id");
        query.setParameter("id", departamento);
        return (List<Funcionario>) query.getResultList();
    }

    public List<Funcionario> listaFuncionarioGeral() {
        Query q = em.createQuery("select f from Funcionario f");
        return q.getResultList();
    }

}
