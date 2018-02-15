package br.com.DAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory factory;

    static {
        factory = Persistence.createEntityManagerFactory("APIPrismaPU");
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static EntityManagerFactory getFactory() {
        return factory;
    }

}
