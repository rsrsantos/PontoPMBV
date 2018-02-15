package br.com.Core;

import br.com.Beans.ColetaHenry1;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import br.com.Model.PtoEquipamento;

public class EquipamentoBuscarConexao implements Serializable {

    private static org.apache.log4j.Logger logger = Logger.getLogger(EquipamentoBuscarConexao.class.getName());

    public static void pegarEventosRelogio(EntityManager manager, Date hora) throws ParseException {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<PtoEquipamento> cq = cb.createQuery(PtoEquipamento.class);
        Root<PtoEquipamento> c = cq.from(PtoEquipamento.class);
        cq.select(c);

        cq.where(cb.and(cb.isNotNull(c.<String>get("ip")), cb.isNotNull(c.<String>get("porta"))));
        TypedQuery<PtoEquipamento> query = manager.createQuery(cq);

        List<PtoEquipamento> listaEquipamentos = new ArrayList<PtoEquipamento>();
        listaEquipamentos = query.getResultList();

        ColetaHenry1 ptoEquipamentoMB = new ColetaHenry1();
        ptoEquipamentoMB.setManager(manager);
        
        PtoEquipamento ptoequipamento = new PtoEquipamento();
        
        for (PtoEquipamento item : listaEquipamentos) {
            ptoequipamento = item;

            try {
//                ptoEquipamentoMB.conectarManual(ptoequipamento);
                logger.info("RESULT da primeira chamada: " +  " - relogio " + item.getIp());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
