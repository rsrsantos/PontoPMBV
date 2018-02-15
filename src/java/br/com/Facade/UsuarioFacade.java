package br.com.Facade;

import br.com.Core.CoreUtils;
import br.com.DAO.GenericDAO;
import br.com.Model.Usuario;
import br.com.DAO.JPAConect;
import br.com.DAO.JPAConect;
import br.com.Model.Afastamento;
import br.com.Model.UsuarioPerfil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class UsuarioFacade extends GenericDAO implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public long contaUsuarioTotal() {
        Query q = em.createQuery("select count(u) from Usuario u");
        long contador = (long) q.getSingleResult();
        return contador;
    }

    public boolean existe(Usuario usuario) {

        TypedQuery<Usuario> query = em.createQuery("select u from Usuario u where u.login = :login", Usuario.class);
        query.setParameter("pLogin", usuario.getLogin());
        try {
            Usuario resultado = (Usuario) query.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
        em.close();
        return true;
    }

    public Usuario logarLogin(String login) {

        try {
            Query q = em.createQuery("SELECT u from Usuario u where u.login = :login");
            q.setParameter("login", login);
            return (Usuario) q.getSingleResult();
        } catch (NoResultException e) {
        }
        return null;
    }

    public Usuario logar(String senha) {
        try {
            Query q = em.createQuery("select u from Usuario u where u.senha =:senha ");
            q.setParameter("senha", senha);
            return (Usuario) q.getSingleResult();
        } catch (NoResultException e) {
        }
        return null;
    }

    public Usuario buscaEmail(String email) {
        try {
            Query query = em.createQuery("select u from Usuario u where u.email = :email");
            query.setParameter("email", email);
            Usuario usuario = (Usuario) query.getSingleResult();
            return usuario;
        } catch (NoResultException e) {
        }
        return null;

    }

    public Usuario buscaLogincadastro(String login) {
        try {
            Query query = em.createQuery("select u from Usuario u where u.login = :login");
            query.setParameter("login", login);
            Usuario usuario = (Usuario) query.getSingleResult();
            return usuario;
        } catch (NoResultException e) {
        }
        return null;

    }

    public boolean buscaLogin(String login) {
        try {
            Query q = em.createQuery("select u from Usuario u where u.login = :login");
            q.setParameter("login", login);
            Usuario resultado = (Usuario) q.getSingleResult();
        } catch (NoResultException e) {
        }
        em.close();
        return true;

    }

    public List<Usuario> buscaUsuarioPerfil(Usuario usuario) {
        Query q = em.createQuery("select u from Usuario u WHERE u.id= :id ");
        q.setParameter("id", usuario.getId());
        return (List<Usuario>) q.getResultList();
    }

    public List<Usuario> listaTodosUsuarios() {
        Query q = em.createQuery("select u from Usuario u");
        return (List<Usuario>) q.getResultList();
    }

    public List<UsuarioPerfil> listaTodosPerfisUsuario() {
        Query q = em.createQuery("select u from UsuarioPerfil u");
        return (List<UsuarioPerfil>) q.getResultList();
    }

    public Usuario buscaUsuarioRecuperarSenha(String login, long matricula, String email) {
        try {
            Query query = em.createQuery("select f from Usuario f JOIN f.funcionario fu WHERE fu.matricula=:matricula "
                    + "AND f.login=:login AND f.email=:email");
            query.setParameter("login", login);
            query.setParameter("matricula", matricula);
            query.setParameter("email", email);
            Usuario u = (Usuario) query.getSingleResult();
            return u;
        } catch (Exception e) {
        }
        return null;

    }

//    public Usuario logar(String login) {
//        try {
//            Query q = em.createQuery("SELECT u from Usuario u where u.login = :login");
//            q.setParameter("login", login);
//            return (Usuario) q.getSingleResult();
//        } catch (NoResultException e) {
//        }
//        return null;
//
//    }
}
