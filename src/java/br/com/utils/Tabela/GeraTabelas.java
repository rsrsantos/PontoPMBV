package br.com.utils.Tabela;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GeraTabelas {

    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("APIPrismaPU");
        EntityManager manager = factory.createEntityManager();
        System.out.println("Tabelas Criadas");

    }

}
