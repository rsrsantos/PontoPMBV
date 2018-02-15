package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Departamento;
import br.com.Model.EspelhoPonto;
import br.com.Model.FolhaPeriodo;
import br.com.Model.Funcionario;
import br.com.Model.Justificativa;
import br.com.Model.PtoArquivo;
import br.com.Model.Usuario;
import br.com.utils.enumerado.StatusFolhaPeriodo;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class EspelhoPontoFacade extends GenericDAO<EspelhoPonto> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public EspelhoPontoFacade() {
        super(EspelhoPonto.class);
    }

    public Funcionario buscaporDepartamento(Departamento departamento) {

        Query query = em.createQuery("select u from Usuario u where u.login = :login and u.senha = :senha");
        query.setParameter("departamento", departamento);
//        query.setParameter("funcionario", funcionario);
        Funcionario funcionario = (Funcionario) query.getSingleResult();
        return funcionario;

    }

    public List<Funcionario> buscadepartamento(Departamento departamento) {
        Query q = em.createQuery("select p from Funcionario p JOIN p.departamento d WHERE d.id =:id ORDER BY p.nome ASC");
        q.setParameter("id", departamento.getId());
        return (List<Funcionario>) q.getResultList();
    }

    public List<Funcionario> buscaDataEspelhoPonto(Departamento departamento) {
        Query q = em.createQuery("select p from Funcionario p JOIN p.departamento d WHERE d.id =:id ORDER BY p.nome ASC");
        q.setParameter("id", departamento.getId());
        return (List<Funcionario>) q.getResultList();
    }

    public List<Departamento> listaPorUsuario(String nome) {

        StringBuffer jpql = new StringBuffer();

        jpql.append(" SELECT l FROM Departamento l ");
        jpql.append(" WHERE l.usuario = :pUsuario ");

        TypedQuery<Departamento> query = em.createQuery(jpql.toString(), Departamento.class);
        query.setParameter("pUsuario", nome);

        return query.getResultList();
    }

//    public List<ServidorVinculoForaEscala> porSetor(Setor setorSelecionado, String periodo) {
//		return repository.porSetor(setorSelecionado, periodo);
//	}
    public List<Funcionario> porNomeSemelhante(String nome) {
        return em.createQuery("from Funcionario where nome like :nome", Funcionario.class)
                .setParameter("nome", "%" + nome + "%")
                .getResultList();
    }

    public Map<String, Funcionario> listaPorMesELocalTrabalhoEUsuario(Departamento departamento) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuffer jpql = new StringBuffer();

        jpql.append(" SELECT r FROM RegistroPonto r ");
        jpql.append(" WHERE r.localTrabalho.id = :pLocalTrabalho ");

        TypedQuery<Funcionario> query = em.createQuery(jpql.toString(), Funcionario.class);
        query.setParameter("pDepartamento", departamento.getId());

        Map<String, Funcionario> map = new HashMap<String, Funcionario>();
        List<Funcionario> lista = query.getResultList();
        for (Funcionario registroPonto : lista) {
        }

        return map;
    }

    public List<PtoArquivo> buscaBatidas(String pis, String periodo) {
        Query q = em.createQuery("select p from PtoArquivo p where p.pis = :pis and p.periodo =:periodo ORDER BY p.data_batida ASC");
        q.setParameter("pis", pis);
        q.setParameter("periodo", periodo);
        return (List<PtoArquivo>) q.getResultList();
    }

    public List<EspelhoPonto> buscaEspelhoPonto(String pis, String periodo) {
        Query q = em.createQuery("select p from EspelhoPonto p where p.pis = :pis and p.periodo =:periodo ORDER BY p.data");
        q.setParameter("pis", pis);
        q.setParameter("periodo", periodo);
        return (List<EspelhoPonto>) q.getResultList();
    }

    public List<PtoArquivo> buscaBatidasByDate(String pis, Date data_batida) {
        Query q = em.createQuery("select p from PtoArquivo p where p.pis = :pis and p.data_batida =:data_batida AND p.processado != TRUE");
        q.setParameter("pis", pis);
        q.setParameter("data_batida", data_batida);
        return (List<PtoArquivo>) q.getResultList();
    }

    public List<EspelhoPonto> buscaAlteracoes(String periodo, String pis) {
        Query q = em.createQuery("select e from EspelhoPonto e where e.pis = :pis and e.alterado =:alterado ");
        q.setParameter("pis", pis);
        q.setParameter("periodo", periodo);
        return (List<EspelhoPonto>) q.getResultList();
    }

    public List<EspelhoPonto> buscaHorasEspelho(String pis, String periodo) {
        Query q = em.createQuery("select e from EspelhoPonto e WHERE e.pis =:pis and e.periodo =:periodo ORDER BY e.data ASC");
        q.setParameter("pis", pis);
        q.setParameter("periodo", periodo);
        return (List<EspelhoPonto>) q.getResultList();
    }

    //select u from Usuario u where u.login = :login and u.senha = :senha
    public List<PtoArquivo> buscaBatidas1(String inicio, String fim) {
        Query q = em.createQuery("select p from PtoArquivo p where p.data_batida  between :inicio and :fim");
        q.setParameter("inicio", inicio);
        q.setParameter("fim", fim);
        return (List<PtoArquivo>) q.getResultList();
    }

    public List<Departamento> buscaDepartamentoPorUsuario(long usuario) {
        Query q = em.createQuery("select p from Usuario p JOIN p.departamento d WHERE d.id =:id ");
        q.setParameter("id", usuario);
        return (List<Departamento>) q.getResultList();
    }

    public List<Justificativa> buscaJustificativas() {
        Query q = em.createQuery("select j from Justificativa j ");
        return (List<Justificativa>) q.getResultList();
    }

    public FolhaPeriodo buscaStatusFolha(String status, Long id) {
        Query query = em.createQuery("select fp from FolhaPeriodo fp where fp.status =:status AND fp.id=:id");
        query.setParameter("status", status);
        query.setParameter("id", id);
        FolhaPeriodo folhaperiodo = (FolhaPeriodo) query.getSingleResult();
        return folhaperiodo;
    }

    public List<FolhaPeriodo> buscaFolhaPeriodo() {
        Query q = em.createQuery("select f from FolhaPeriodo f ");
        return (List<FolhaPeriodo>) q.getResultList();
    }

}
