package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Afastamento;
import br.com.Model.BancoHoras;
import br.com.Model.BancoHorasExtrato;
import br.com.Model.Departamento;
import br.com.Model.EspelhoPonto;
import br.com.Model.Funcionario;
import br.com.Model.PtoArquivo;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class BancoHorasFacade extends GenericDAO<BancoHoras> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public BancoHorasFacade() {
        super(BancoHoras.class);
    }

    public List<Departamento> listaDepartamento() {
        Query query = em.createQuery("select d from Departamento d");
        return query.getResultList();
    }

    public List<BancoHoras> listaBancoHoras() {
        Query query = em.createQuery("select b from BancoHoras b");
        return query.getResultList();
    }

    public List<BancoHorasExtrato> buscaBancoHoras(Funcionario funcionario, String periodo) {
        Query q = em.createQuery("select p from BancoHorasExtrato p JOIN p.funcionario d WHERE d.id =:id "
                + "AND p.periodo =:periodo  ");
        q.setParameter("id", funcionario.getId());
        q.setParameter("periodo", periodo);
        return (List<BancoHorasExtrato>) q.getResultList();
    }

    public List<BancoHorasExtrato> buscaBancoHorasExtrato(Funcionario funcionario, String periodo) {
        Query q = em.createQuery("select p from BancoHorasExtrato p JOIN p.funcionario d WHERE d.id =:id "
                + "AND p.periodo =:periodo AND p.alterado != FALSE ORDER BY p.data_evento ASC  ");
        q.setParameter("id", funcionario.getId());
        q.setParameter("periodo", periodo);
        return (List<BancoHorasExtrato>) q.getResultList();
    }

    public List<BancoHoras> buscadepartamentosBancoHoras(Long banco) {
        Query q = em.createQuery("select p from BancoHoras p JOIN p.departamento f Where p.id =:id");
        q.setParameter("id", banco);
        return (List<BancoHoras>) q.getResultList();
    }

    public List<Funcionario> buscafuncionariosbancohoras(Long departamento) {
        Query q = em.createQuery("select p from Funcionario p JOIN p.departamento d WHERE d.id =:id");
        q.setParameter("id", departamento);
        return (List<Funcionario>) q.getResultList();
    }

    public List<EspelhoPonto> buscaHorasEspelho(String pis, String periodo) {
        Query q = em.createQuery("select e from EspelhoPonto e WHERE e.pis =:pis AND e.periodo =:periodo");
        q.setParameter("pis", pis);
        q.setParameter("periodo", periodo);
        return (List<EspelhoPonto>) q.getResultList();
    }

    public List<EspelhoPonto> buscaHorasEspelho(String pis) {
        Query q = em.createQuery("select e from EspelhoPonto e WHERE e.pis =:pis ORDER BY e.data ASC");
        q.setParameter("pis", pis);
        return (List<EspelhoPonto>) q.getResultList();
    }

    public List<EspelhoPonto> buscaExtratoByDate(String pis, Date data_batida) {
        Query q = em.createQuery("select p from EspelhoPonto p where p.pis = :pis and p.data_Batida =:data_batida");
        q.setParameter("pis", pis);
        q.setParameter("data_batida", data_batida);
        return (List<EspelhoPonto>) q.getResultList();
    }

    public List<BancoHorasExtrato> buscaExtratoBanco(Funcionario funcionario, Date data_batida) {
        Query q = em.createQuery("select p from BancoHorasExtrato p where p.funcionario.id = :id and p.data_evento =:data_batida");
        q.setParameter("id", funcionario.getId());
        q.setParameter("data_batida", data_batida);
        return (List<BancoHorasExtrato>) q.getResultList();
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
