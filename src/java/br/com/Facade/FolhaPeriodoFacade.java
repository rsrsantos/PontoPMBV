package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Departamento;
import br.com.Model.FolhaPeriodo;
import br.com.Model.Funcionario;
import br.com.utils.enumerado.StatusFolhaPeriodo;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class FolhaPeriodoFacade extends GenericDAO<FolhaPeriodo> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public FolhaPeriodoFacade() {
        super(FolhaPeriodo.class);
    }

    public List<Funcionario> buscadeFuncionarios() {
        Query q = em.createQuery("select p from Funcionario p ");
        return (List<Funcionario>) q.getResultList();
    }

    public FolhaPeriodo buscaStatusFolhaPeriodo(StatusFolhaPeriodo status) {
        Query query = em.createQuery("select fp from FolhaPeriodo fp where fp.status =:status ");
        query.setParameter("status", status);
        FolhaPeriodo folhaperiodo = (FolhaPeriodo) query.getSingleResult();
        return folhaperiodo;

    }
    
    public long contaFolhaPeriodoTotal() {
        Query q = em.createQuery("select count(u) from FolhaPeriodo u");
        long contador = (long) q.getSingleResult();
        return contador;
    }

}
