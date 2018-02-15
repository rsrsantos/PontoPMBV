package ZPM;

import br.com.Beans.PtoEquipamentoBeanHenry;
import br.com.DAO.JPAConect;
import br.com.Model.PtoArquivo;
import br.com.Model.PtoEquipamentoElgin;
import br.com.utils.Message.Msg;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;

@ManagedBean
public class RepZPM {

    final List<String> listaRegistros = new ArrayList<>();

    int lasdata;
    private org.apache.log4j.Logger logger = Logger.getLogger(PtoEquipamentoBeanHenry.class.getName());
    private static final SimpleDateFormat sdfH = new SimpleDateFormat("HHmm");
    EntityManager manager = new JPAConect().getEntityManager();
    int porta_comunicacao_rep = 5000;
    ClienteTCP rep;
    byte[] fim = new byte[]{(byte) 0x0d, (byte) 0x0a};
    private boolean termina;
    private boolean debug = true;
    private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

    public RepZPM() {
        this.rep = new ClienteTCP();
        this.rep.setDebug(debug);
    }

//    public RepZPM(Ponto ponto) {
//        this.rep = new ClienteTCP();
//        this.ponto = ponto;
//        this.rep.setDebug(debug);
//        System.out.println("Coletar ponto: " + ponto.getDescricao());
//    }
    public String lerStatusREP() {
        String resposta = "";
        byte[] dados = new byte[16];

//        rep.conecta(getPonto().getIp(), this.porta_comunicacao_rep);
        dados[0] = (byte) 0x05;
        dados[1] = (byte) 0x00;
        dados[2] = (byte) 0x00;
        dados[3] = (byte) 0x00;
        dados[4] = (byte) 0x08;
        dados[5] = (byte) 0x00;
        dados[6] = (byte) 0x00;
        dados[7] = (byte) 0x00;
        dados[8] = (byte) 0x35;
        dados[9] = (byte) 0x3b;
        dados[10] = (byte) 0x31;
        dados[11] = (byte) 0x0d;
        dados[12] = (byte) 0x0a;
        dados[13] = (byte) 0x31;
        dados[14] = (byte) 0x0d;
        dados[15] = (byte) 0x0a;

        rep.escreve(dados);

        resposta = resposta();
        rep.desconecta();

        return resposta;
    }

    public boolean ajustarDataHora(PtoEquipamentoElgin elgin1) {

        DateFormat dt = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        String ajuste = dt.format(Calendar.getInstance().getTime());

        String resposta = "";
        byte[] dados = new byte[34];

        try {

            String ips = elgin1.getIpel();
            int portas = elgin1.getPortael();
            rep.conecta(ips, portas);

            dados[0] = (byte) 0x05;
            dados[1] = (byte) 0x00;
            dados[2] = (byte) 0x00;
            dados[3] = (byte) 0x00;
            dados[4] = (byte) 0x1a;
            dados[5] = (byte) 0x00;
            dados[6] = (byte) 0x00;
            dados[7] = (byte) 0x00;
            dados[8] = (byte) 0x34; //  4
            dados[9] = (byte) 0x3b; //  ;
            dados[10] = (byte) 0x31; // 3
            dados[11] = (byte) 0x0d;
            dados[12] = (byte) 0x0a;

            byte[] ajusteBytes = ajuste.getBytes();

            for (int i = 0; i < ajusteBytes.length; i++) {
                dados[i + 13] = ajusteBytes[i];
            }

//        //fim do pacote
            dados[32] = (byte) 0x0d; // :
            dados[33] = (byte) 0x0a; // :

            rep.escreve(dados);

            resposta = resposta();

            rep.desconecta();
        } catch (Exception e) {
            Msg.addMsgError("Operação não Realizada: " + e);

        }

        return true;

    }

//    public Empregador lerEmpregador() {
//        String resposta = "";
//        byte[] dados = new byte[16];
////        rep.conecta(getPonto().getIp(), this.porta_comunicacao_rep);
//
//        dados[0] = (byte) 0x05; //  .
//        dados[1] = (byte) 0x00; //  .
//        dados[2] = (byte) 0x00; //  .
//        dados[3] = (byte) 0x00; //  .
//        dados[4] = (byte) 0x08; //  .
//        dados[5] = (byte) 0x00; //  .
//        dados[6] = (byte) 0x00; //  .
//        dados[7] = (byte) 0x00; //  .
//        dados[8] = (byte) 0x31; //  1
//        dados[9] = (byte) 0x3b; //  ;
//        dados[10] = (byte) 0x31; // 1
//        dados[11] = (byte) 0x0d; // .
//        dados[12] = (byte) 0x0a; // .
//        dados[13] = (byte) 0x33; // 3
//        dados[14] = (byte) 0x0d; // .
//        dados[15] = (byte) 0x0a; // .
//
//        rep.escreve(dados);
//
//        resposta = resposta();
//
//        rep.desconecta();
//        Empregador emp = new Empregador();
//
//        return emp;
//    }
//    public List<Empregados> lerEmpregados() {
//
//        List<Empregados> resposta = new ArrayList<>();
//
//        byte[] dados = new byte[16];
//
////        rep.conecta(getPonto().getIp(), this.porta_comunicacao_rep);
//        dados[0] = (byte) 0x05; //  .
//        dados[1] = (byte) 0x00; //  .
//        dados[2] = (byte) 0x00; //  .
//        dados[3] = (byte) 0x00; //  .
//        dados[4] = (byte) 0x08; //  .
//        dados[5] = (byte) 0x00; //  .
//        dados[6] = (byte) 0x00; //  .
//        dados[7] = (byte) 0x00; //  .
//        dados[8] = (byte) 0x32; //  2
//        dados[9] = (byte) 0x3b; //  ;
//        dados[10] = (byte) 0x31; // 1
//        dados[11] = (byte) 0x0d; // .
//        dados[12] = (byte) 0x0a; // .
//        dados[13] = (byte) 0x33; // 3
//        dados[14] = (byte) 0x0d; // .
//        dados[15] = (byte) 0x0a; // .
//
//        rep.escreve(dados);
//
//        resposta();
//
//        rep.desconecta();
//
//        return resposta;
//    }
    public void ColetarManual(final PtoEquipamentoElgin elgin) {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

//        DateFormat dt = new SimpleDateFormat("dd/MM/YYYY");
//        elgin.setDatainicio(dt.format(Calendar.getInstance().getTime()));
//        elgin.setDatafim(dt.format(Calendar.getInstance().getTime()));
//        boolean retorno = false;
        String resposta;
        byte[] dados = new byte[36];

        String ip = elgin.getIpel();
        int porta = elgin.getPortael();

        rep.conecta(ip, porta);

        String DataInicio = elgin.getDatainicio();
        String DataFim = elgin.getDatafim();

        String[] dataIni = DataInicio.split("/");
        String[] dataFim = DataFim.split("/");

        Calendar CalIni = Calendar.getInstance();
        CalIni.set(Integer.parseInt(dataIni[2]), Integer.parseInt(dataIni[1]) - 1, Integer.parseInt(dataIni[0]));

        Calendar CalFim = Calendar.getInstance();
        CalFim.set(Integer.parseInt(dataFim[2]), Integer.parseInt(dataFim[1]) - 1, Integer.parseInt(dataFim[0]));

        Calendar ca = Calendar.getInstance();
        ca.setTime(CalIni.getTime());

        Calendar caFim = Calendar.getInstance();
        caFim.setTime(CalFim.getTime());

        String inidia = "" + ca.get(Calendar.DAY_OF_MONTH);
        String inimes = "" + (ca.get(Calendar.MONTH) + 1);
        String iniano = "" + ca.get(Calendar.YEAR);

        String fimdia = "" + caFim.get(Calendar.DAY_OF_MONTH);
        String fimmes = "" + (caFim.get(Calendar.MONTH) + 1);
        String fimano = "" + caFim.get(Calendar.YEAR);

        while (inidia.length() < 2) {
            inidia = "0" + inidia;
        }
        while (inimes.length() < 2) {
            inimes = "0" + inimes;
        }

        while (fimdia.length() < 2) {
            fimdia = "0" + fimdia;
        }
        while (fimmes.length() < 2) {
            fimmes = "0" + fimmes;
        }

        if (isDebug()) {
            System.out.println(inidia + " - " + inimes + " - " + iniano);
            System.out.println(fimdia + " - " + fimmes + " - " + fimano);
        }

        byte[] inidiabytes = inidia.getBytes();
        byte[] inimesbytes = inimes.getBytes();
        byte[] inianobytes = iniano.getBytes();

        byte[] fimdiabytes = fimdia.getBytes();
        byte[] fimmesbytes = fimmes.getBytes();
        byte[] fimanobytes = fimano.getBytes();

        dados[0] = (byte) 0x05; //  .
        dados[1] = (byte) 0x00; //  .
        dados[2] = (byte) 0x00; //  .
        dados[3] = (byte) 0x00; //  .
        dados[4] = (byte) 0x1c; //  .
        dados[5] = (byte) 0x00; //  .
        dados[6] = (byte) 0x00; //  .
        dados[7] = (byte) 0x00; //  .
        dados[8] = (byte) 0x36; //  6
        dados[9] = (byte) 0x3b; //  ;
        dados[10] = (byte) 0x31; // 1
        dados[11] = (byte) 0x0d; // .
        dados[12] = (byte) 0x0a; // .

//        data ini
        dados[13] = inidiabytes[0]; // 1
        dados[14] = inidiabytes[1]; // 8
        dados[15] = (byte) 0x2f; // /
        dados[16] = inimesbytes[0]; // 0
        dados[17] = inimesbytes[1]; // 2
        dados[18] = (byte) 0x2f; // /
        dados[19] = inianobytes[0]; // 2
        dados[20] = inianobytes[1]; // 0
        dados[21] = inianobytes[2]; // 1
        dados[22] = inianobytes[3]; // 5

        dados[23] = (byte) 0x3b; // ;

//        data fim
        dados[24] = fimdiabytes[0]; // 1
        dados[25] = fimdiabytes[1]; // 8
        dados[26] = (byte) 0x2f; // /
        dados[27] = fimmesbytes[0]; // 0
        dados[28] = fimmesbytes[1]; // 2
        dados[29] = (byte) 0x2f; // /
        dados[30] = fimanobytes[0]; // 2
        dados[31] = fimanobytes[1]; // 0   
        dados[32] = fimanobytes[2]; // 1
        dados[33] = fimanobytes[3]; // 5

        dados[34] = (byte) 0x0d; // .
        dados[35] = (byte) 0x0a; // .

        if (rep.escreve(dados)) {

            resposta = resposta(true);
            String[] resposta_linhas = resposta.split("\n");
            String[] linha;

            for (String resposta_linha : resposta_linhas) {

                linha = resposta_linha.split(";");

                if (linha.length == 5) {

                    String s = (linha[2] + ";" + linha[3] + ";" + linha[4]);
                    char[] temp = s.toCharArray();

                    String str = "";
                    for (char chr : temp) {
                        if (chr == ']') {
                            str = "";
                        }
                        if (Character.isDigit(chr)) {
                            str += chr;
                            if (str.length() == 34) {

                                listaRegistros.add(str);

                                str = "";
                            }
                        }
                    }

                }
            }

            if (listaRegistros.size() > 0) {

                try {

                    manager.getTransaction().begin();

                    for (String item : listaRegistros) {
                        int nsr = Integer.valueOf(item.substring(26, 34));
                        Date data = sdf.parse(item.substring(11, 19));

                        PtoArquivo arquivo = new PtoArquivo();

                        Boolean existe = false;

//                        existe = porEquipamentoNSR(elgin, nsr);
                        Date HrsString = sdfH.parse(item.substring(19, 25));
                        String pisString = item.substring(0, 11);
                        String Equipam = elgin.getDescricaoel();
                        String modelo = elgin.getModeloel();
                        if (existe == false) {

                            arquivo.setModelo(modelo);
                            arquivo.setEquipamento(Equipam);
                            arquivo.setData_batida(data);
                            arquivo.setNsr(nsr);
                            arquivo.setHora(HrsString);
                            arquivo.setPis(pisString);
                            arquivo.setPtooriginal(item);

                            logger.info(" ###################### gravando registro de PONTO com NSR: " + nsr
                                    + " para equipamento: " + elgin.getId() + (new Date()));
                            manager.persist(arquivo);
                            manager.merge(elgin);

//                            }
                        }
                    }
                    manager.getTransaction().commit();
                    manager.close();
                    Msg.addMsgInfo("Coletado com Sucesso: " + listaRegistros.size() + " Registros");
                } catch (Exception e) {
                    logger.info("Não Possui Dados");
                    rep.desconecta();
                }

            }

        }
    }
//===================================================================

    public boolean lerBatidas(final PtoEquipamentoElgin elgin) {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

//        DateFormat dt = new SimpleDateFormat("dd/MM/YYYY");
//        elgin.setDatainicio(dt.format(Calendar.getInstance().getTime()));
//        elgin.setDatafim(dt.format(Calendar.getInstance().getTime()));
//        boolean retorno = false;
        String resposta;
        byte[] dados = new byte[36];

        String ip = elgin.getIpel();
        int porta = elgin.getPortael();

        rep.conecta(ip, porta);

        String DataInicio = elgin.getDatainicio();
        String DataFim = elgin.getDatafim();

        String[] dataIni = DataInicio.split("/");
        String[] dataFim = DataFim.split("/");

        Calendar CalIni = Calendar.getInstance();
        CalIni.set(Integer.parseInt(dataIni[2]), Integer.parseInt(dataIni[1]) - 1, Integer.parseInt(dataIni[0]));

        Calendar CalFim = Calendar.getInstance();
        CalFim.set(Integer.parseInt(dataFim[2]), Integer.parseInt(dataFim[1]) - 1, Integer.parseInt(dataFim[0]));

        Calendar ca = Calendar.getInstance();
        ca.setTime(CalIni.getTime());

        Calendar caFim = Calendar.getInstance();
        caFim.setTime(CalFim.getTime());

        String inidia = "" + ca.get(Calendar.DAY_OF_MONTH);
        String inimes = "" + (ca.get(Calendar.MONTH) + 1);
        String iniano = "" + ca.get(Calendar.YEAR);

        String fimdia = "" + caFim.get(Calendar.DAY_OF_MONTH);
        String fimmes = "" + (caFim.get(Calendar.MONTH) + 1);
        String fimano = "" + caFim.get(Calendar.YEAR);

        while (inidia.length() < 2) {
            inidia = "0" + inidia;
        }
        while (inimes.length() < 2) {
            inimes = "0" + inimes;
        }

        while (fimdia.length() < 2) {
            fimdia = "0" + fimdia;
        }
        while (fimmes.length() < 2) {
            fimmes = "0" + fimmes;
        }

        if (isDebug()) {
            System.out.println(inidia + " - " + inimes + " - " + iniano);
            System.out.println(fimdia + " - " + fimmes + " - " + fimano);
        }

        byte[] inidiabytes = inidia.getBytes();
        byte[] inimesbytes = inimes.getBytes();
        byte[] inianobytes = iniano.getBytes();

        byte[] fimdiabytes = fimdia.getBytes();
        byte[] fimmesbytes = fimmes.getBytes();
        byte[] fimanobytes = fimano.getBytes();

        dados[0] = (byte) 0x05; //  .
        dados[1] = (byte) 0x00; //  .
        dados[2] = (byte) 0x00; //  .
        dados[3] = (byte) 0x00; //  .
        dados[4] = (byte) 0x1c; //  .
        dados[5] = (byte) 0x00; //  .
        dados[6] = (byte) 0x00; //  .
        dados[7] = (byte) 0x00; //  .
        dados[8] = (byte) 0x36; //  6
        dados[9] = (byte) 0x3b; //  ;
        dados[10] = (byte) 0x31; // 1
        dados[11] = (byte) 0x0d; // .
        dados[12] = (byte) 0x0a; // .

//        data ini
        dados[13] = inidiabytes[0]; // 1
        dados[14] = inidiabytes[1]; // 8
        dados[15] = (byte) 0x2f; // /
        dados[16] = inimesbytes[0]; // 0
        dados[17] = inimesbytes[1]; // 2
        dados[18] = (byte) 0x2f; // /
        dados[19] = inianobytes[0]; // 2
        dados[20] = inianobytes[1]; // 0
        dados[21] = inianobytes[2]; // 1
        dados[22] = inianobytes[3]; // 5

        dados[23] = (byte) 0x3b; // ;

//        data fim
        dados[24] = fimdiabytes[0]; // 1
        dados[25] = fimdiabytes[1]; // 8
        dados[26] = (byte) 0x2f; // /
        dados[27] = fimmesbytes[0]; // 0
        dados[28] = fimmesbytes[1]; // 2
        dados[29] = (byte) 0x2f; // /
        dados[30] = fimanobytes[0]; // 2
        dados[31] = fimanobytes[1]; // 0   
        dados[32] = fimanobytes[2]; // 1
        dados[33] = fimanobytes[3]; // 5

        dados[34] = (byte) 0x0d; // .
        dados[35] = (byte) 0x0a; // .

        if (rep.escreve(dados)) {

            resposta = resposta(true);
            String[] resposta_linhas = resposta.split("\n");
            String[] linha;

            for (String resposta_linha : resposta_linhas) {

                linha = resposta_linha.split(";");

                if (linha.length == 5) {

                    String s = (linha[2] + ";" + linha[3] + ";" + linha[4]);
                    char[] temp = s.toCharArray();

                    String str = "";
                    for (char chr : temp) {
                        if (chr == ']') {
                            str = "";
                        }
                        if (Character.isDigit(chr)) {
                            str += chr;
                            if (str.length() == 34) {

                                listaRegistros.add(str);

                                str = "";
                            }
                        }
                    }

                }
            }

            if (listaRegistros.size() > 0) {

                try {

                    manager.getTransaction().begin();

                    for (String item : listaRegistros) {
                        int nsr = Integer.valueOf(item.substring(26, 34));
                        Date data = sdf.parse(item.substring(11, 19));

                        PtoArquivo arquivo = new PtoArquivo();

                        Boolean existe = false;

//                        existe = porEquipamentoNSR(elgin, nsr);
                        Date HrsString = sdfH.parse(item.substring(19, 25));
                        String pisString = item.substring(0, 11);
                        String Equipam = elgin.getDescricaoel();
                        String modelo = elgin.getModeloel();
                        if (existe == false) {

                            arquivo.setModelo(modelo);
                            arquivo.setEquipamento(Equipam);
                            arquivo.setData_batida(data);
                            arquivo.setNsr(nsr);
                            arquivo.setHora(HrsString);
                            arquivo.setPis(pisString);
                            arquivo.setPtooriginal(item);

                            logger.info(" ###################### gravando registro de PONTO com NSR: " + nsr
                                    + " para equipamento: " + elgin.getId() + (new Date()));
                            manager.persist(arquivo);
                            manager.merge(elgin);

//                            }
                        }
                    }
                    manager.getTransaction().commit();
                    manager.close();

                } catch (Exception e) {
                    logger.info("Não Possui Dados");
                    rep.desconecta();
                }

            }

        }

        return true;
    }

    private Boolean porEquipamentoNSR(PtoEquipamentoElgin elgin, int nsr) {

        Boolean retorno = false;

        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<PtoArquivo> cq = cb.createQuery(PtoArquivo.class);
        Root<PtoArquivo> c = cq.from(PtoArquivo.class);
        cq.where(cb.and(cb.equal(c.<PtoEquipamentoElgin>get("ptoequipamentoelgin"), elgin), cb.equal(c.<Integer>get("nsr"), nsr)));

        TypedQuery<PtoArquivo> typedQuery = manager.createQuery(cq);

        if (!typedQuery.getResultList().isEmpty()) {
            retorno = true;
        }

        return retorno;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    private void mais() {
        rep.escreve(new byte[]{(byte) 0x06});
    }

    private String resposta() {

        String ret = "";

        int i = 0;
        int respostavazia = 10;

        while (!termina) {

            if (isDebug()) {
                System.out.println("i = " + i++);
            }
            String leitura = this.rep.leitura2();

            if (leitura != null) {
                ret += leitura + "\n";
                if (leitura.length() > 2) {
                    if (!leitura.substring(leitura.length() - 2).equals(new String(fim)));
                    {
                        mais();
                    }
                }

            } else {
                break;
            }
        }
        return ret;
    }

    private String respostaThread() {
        resposta res = new resposta(rep);

        res.start();

        return "";
    }

    private String resposta(boolean aguardafim) {

        String ret = "";

        int i = 0;

        int recebeu = 0;
        termina = false;

        while (!termina) {
            String leitura = this.rep.leitura2();

            if (leitura != null) {
                recebeu++;

                if (isDebug()) {
                }

                if (leitura.length() > 8) {
                    ret += leitura.substring(8);

                } else {
                    ret += leitura;
                }
                if (leitura.length() > 10) {
                    mais();
                }
            } else {
                if (leitura != null) {
                    if (leitura.endsWith("\n\n")) {
                        break;
                    }
                }
                if (!aguardafim) {
                    break;
                } else {
//                    mais();
                }
                if (recebeu > 2) {
                    break;
                }
            }
        }
        return ret;
    }

    public boolean coleta(final PtoEquipamentoElgin elgin) {

        boolean retorno = false;

        this.setDebug(true);

        retorno = lerBatidas(elgin);
        return retorno;
    }

    public String ajustahora(final PtoEquipamentoElgin elgin) {

        try {
            this.setDebug(true);
            ajustarDataHora(elgin);
            Msg.addMsgInfo("Data e Hora Atualizadas!");

        } catch (Exception e) {
            Msg.addMsgFatal("Operação Não Realizada" + e);
            System.out.println("Operação Não Realizada" + e);
        }

        return null;

    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
        rep.setDebug(debug);

    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

}
