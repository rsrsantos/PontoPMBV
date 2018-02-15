package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Afastamento;
import br.com.Model.EspelhoPonto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class AfastamentoFacade extends GenericDAO<Afastamento> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public AfastamentoFacade() {
        super(Afastamento.class);
    }

    public Afastamento buscaAfastamento(long id) {
        Query query = em.createQuery("select u from Afastamento u where u.funcionario_id = :funcionario_id");
        query.setParameter("id", id);
        Afastamento afastamento = (Afastamento) query.getSingleResult();
        return afastamento;
    }

    public List<Afastamento> buscaFuncionariosAfastamento(Long afastamento) {
        Query q = em.createQuery("select p from Afastamento p JOIN p.justificativa j JOIN p.funcionario f Where p.id =:id");
        q.setParameter("id", afastamento);
        return (List<Afastamento>) q.getResultList();
    }

    public Afastamento buscaJustificativaAfastamento(Afastamento afastamento) {
        Query q = em.createQuery("select p from Afastamento p JOIN p.justificativa d WHERE d.id =:id");
        q.setParameter("id", afastamento.getJustificativa());
        return (Afastamento) q.getResultList();
    }

    public List<EspelhoPonto> buscaDataEspelho(String pis) {
        Query q = em.createQuery("select e from EspelhoPonto e where e.pis = :pis ORDER BY e.data ASC");
        q.setParameter("pis", pis);
        return (List<EspelhoPonto>) q.getResultList();
    }

//    public List<EspelhoPonto> getValorEntreDatas(Date data1, Date data2,long id) {
//        Query query = em.createQuery("select p from EspelhoPonto where  between :inicio and :fim");
//        query.setParameter("data1 ", data1);
//        query.setParameter("data2", data2);
//        query.setParameter("id",id);
//        return (List<EspelhoPonto>) query.getResultList();
    public List<EspelhoPonto> buscaByDate(String pis, Date data_batida) {
        Query q = em.createQuery("select p from EspelhoPonto p where p.pis = :pis and p.data_Batida =:data_batida");
        q.setParameter("pis", pis);
        q.setParameter("data_batida", data_batida);
        return (List<EspelhoPonto>) q.getResultList();
    }
    
     public long contaAfastamentoTotal() {
        Query q = em.createQuery("select count(u) from Afastamento u");
        long contador = (long) q.getSingleResult();
        return contador;
    }
}
