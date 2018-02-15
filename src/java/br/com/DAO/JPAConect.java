package br.com.DAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class JPAConect {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("APIPrismaPU");

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

}
