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

public class BancoHorasExtratoFacade extends GenericDAO<BancoHorasExtrato> implements Serializable {

    EntityManager em = new JPAConect().getEntityManager();

    public BancoHorasExtratoFacade() {
        super(BancoHorasExtrato.class);
    }

}
