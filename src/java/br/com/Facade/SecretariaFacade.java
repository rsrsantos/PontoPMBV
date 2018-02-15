package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Afastamento;
import br.com.Model.Funcionario;
import br.com.Model.Secretaria;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class SecretariaFacade extends GenericDAO<Secretaria> implements Serializable {

    public SecretariaFacade() {
        super(Secretaria.class);
    }

    EntityManager em = new JPAConect().getEntityManager();

    public Funcionario buscaMatricu(String matricula) {
        Query query = em.createQuery("select u from Funcionario u where u.matricula = :matricula");
        query.setParameter("matricula", matricula);
        Funcionario funcionario = (Funcionario) query.getSingleResult();
        return funcionario;
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

    public Funcionario buscapis(String pis) {
        Query query = em.createQuery("select u from Funcionario u where u.pis = :pis");
        query.setParameter("pis", pis);
        Funcionario funcionario = (Funcionario) query.getSingleResult();
        return funcionario;
    }

    public List<Funcionario> buscaSecretario() {
        Query q = em.createQuery("select f from Funcionario f where f.secretario = TRUE ");
        return (List<Funcionario>) q.getResultList();
    }

    public Secretaria buscaSecretaria(String descricao) {
        try {
            Query query = em.createQuery("select u from Secretaria u where u.nome = :descricao");
            query.setParameter("descricao", descricao);
            Secretaria secretaria = (Secretaria) query.getSingleResult();
            return secretaria;
        } catch (NoResultException e) {
        }
        return null;

    }

}
