package br.com.DAO;

import static br.com.DAO.JpaUtil.getEntityManager;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public class GenericDAO<T> {

    public final Class<T> classe;
    EntityManager em = new JPAConect().getEntityManager();
    

    public GenericDAO(Class<T> classe) {
        this.classe = classe;
    }

    public void adiciona(T t) {

        em.getTransaction().begin();
        em.persist(t);
        em.getTransaction().commit();
        em.close();
    }

    public void remove(T t) {

        em.getTransaction().begin();
        em.remove(em.merge(t));
        em.getTransaction().commit();
        em.close();
    }

    public void atualiza(T t) {

        em.getTransaction().begin();
        em.merge(em.merge(t));
        em.getTransaction().commit();
        em.close();
    }

    public List<T> listaTodos() {

        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
        query.select(query.from(classe));
        List<T> lista = em.createQuery(query).getResultList();
        em.close();
        return lista;
    }

    public T buscaPorId(Integer id) {

        T instancia = em.find(classe, id);
        em.close();
        return instancia;
    }

    public int contaTodos() {

        long result = (Long) em.createQuery("select count(n) from ptoequioamento n")
                .getSingleResult();
        em.close();
        return (int) result;
    }

    public List<T> listaTodosPaginada(int firstResult, int maxResults) {

        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
        query.select(query.from(classe));
        List<T> lista = em.createQuery(query).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();

        em.close();
        return lista;
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        String querySelect = "SELECT empresa FROM " + classe.getSimpleName() + " obj";
        Query query = getEntityManager().createQuery(querySelect);
        return query.getResultList();
    }

//    public boolean Login(Usuario usuario) {
//
//        TypedQuery<Usuario> query = em.createQuery("select u from Usuario u where u.login=:pLogin "
//                + "and u.senha=:pSenha", Usuario.class);
//
//        query.setParameter("pLogin",usuario.getLogin());
//        query.setParameter("pSenha",usuario.getSenha());
//        
//        
//        Usuario resultado = query.getSingleResult();
//        em.close();
//
//        return resultado != null;
//
//    }
    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    
    
}
