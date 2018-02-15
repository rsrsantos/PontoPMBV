package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Usuario;
import br.com.Model.UsuarioHistorico;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class Historico_usuarioFacade extends GenericDAO<UsuarioHistorico> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public Historico_usuarioFacade() {
        super(UsuarioHistorico.class);
    }

    public List<UsuarioHistorico> listaHistoricoUsuario(Usuario usuario) {
        Query q = em.createQuery("select uh from UsuarioHistorico uh where uh.usuario =:usuario");
        q.setParameter("usuario", usuario);
        return q.getResultList();
    }

}
