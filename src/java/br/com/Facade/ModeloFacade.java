package br.com.Facade;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Modelo;
import java.io.Serializable;
import javax.persistence.EntityManager;

public class ModeloFacade extends GenericDAO<Modelo> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public ModeloFacade() {
        super(Modelo.class);
    }

}
