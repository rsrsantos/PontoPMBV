package br.com.Core;

import ZPM.RepZPM;
import br.com.Beans.PtoEquipamentoBeanHenry;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import br.com.DAO.JpaUtil;
import br.com.Model.PtoEquipamento;
import br.com.Model.PtoEquipamentoElgin;

public class EquipamentoBuscarConexaoElgin implements Serializable {

    private static final long serialVersionUID = 1L;

    private static org.apache.log4j.Logger logger = Logger.getLogger(EquipamentoBuscarConexaoElgin.class.getName());

    public static void main(String[] args) throws ParseException {
        EntityManager manager = JpaUtil.getEntityManager();
        pegarEventosRelogio(manager, new Date());
    }

    public static void pegarEventosRelogio(EntityManager manager, Date hora) throws ParseException {

        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<PtoEquipamento> cq = cb.createQuery(PtoEquipamento.class);
        Root<PtoEquipamento> c = cq.from(PtoEquipamento.class);
        cq.select(c);

        cq.where(cb.and(cb.isNotNull(c.<String>get("ip")), cb.isNotNull(c.<String>get("porta"))));
        TypedQuery<PtoEquipamento> query = manager.createQuery(cq);

        List<PtoEquipamento> listaEquipamentos = new ArrayList<PtoEquipamento>();
        listaEquipamentos = query.getResultList();

        PtoEquipamentoBeanHenry ptoEquipamentoMB = new PtoEquipamentoBeanHenry();
//        ptoEquipamentoMB.setManager(manager);

        PtoEquipamento equipamento = new PtoEquipamento();

        for (PtoEquipamento item : listaEquipamentos) {
            equipamento = item;

            try {
//                ptoEquipamentoMB.conectar(equipamento, Boolean.TRUE);
                logger.info("RESULT da primeira chamada:  - relogio " + item.getIp());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
