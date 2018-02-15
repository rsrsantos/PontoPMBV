package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Usuario;
import br.com.Model.UsuarioPerfil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UsuarioPerfilFacade extends GenericDAO implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public UsuarioPerfilFacade() {
        super(UsuarioPerfil.class);
    }

    public List<UsuarioPerfil> listaTodosUsuariosPerfil() {
        Query q = em.createQuery("select u from UsuarioPerfil u");
        return (List<UsuarioPerfil>) q.getResultList();
    }

}
