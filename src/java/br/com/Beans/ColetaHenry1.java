package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Facade.PtoArquivoFacade;
import br.com.Facade.PtoEquipamentoFacade;
import br.com.Model.Modelo;
import br.com.Model.PtoArquivo;
import br.com.Model.PtoEquipamento;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import tcpcom.TcpClient;

@ManagedBean
@ViewScoped
public final class ColetaHenry1 implements Serializable {

    private int nsrFora;
    private Boolean conectado;
    private Boolean stop = false;
    private TcpClient client;
    final private org.apache.log4j.Logger logger = Logger.getLogger(ColetaHenry1.class.getName());
    final List<String> listaRegistros = new ArrayList<>();
    private int lastNSR = 0;
    EntityManager manager = new JPAConect().getEntityManager();
    private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    private static final SimpleDateFormat sdfH = new SimpleDateFormat("HHmm");
    SimpleDateFormat periodoformat = new SimpleDateFormat("yyyyMM");

    PtoEquipamentoFacade ptoEquipamentoFacade;
    PtoArquivoFacade arquivoFacade;
    Usuario usuario;
    PtoEquipamento ptoEquipamento;
    PtoEquipamento equipamentoSelecionado;

    public ColetaHenry1() {

        if (ptoEquipamentoFacade == null) {
            ptoEquipamentoFacade = new PtoEquipamentoFacade();
        }

        if (arquivoFacade == null) {
            arquivoFacade = new PtoArquivoFacade();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

        if (ptoEquipamento == null) {
            ptoEquipamento = new PtoEquipamento();
        }

    }

    public void conectarManual(PtoEquipamento ptoequipamento) {

        final String EQUIPAMENTO_IP = ptoequipamento.getIp();
        final String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

        try {
            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
                procedimentoManual(ptoequipamento);

            } else {
            }
        } catch (Exception e) {
        }
    }

    public int registroManual(TcpClient client, PtoEquipamento ptoequipamento) {

        String codigo = "01+RR+00+N]";
        String qtdeRegistros = "20]";
        String ultimoNSR = String.valueOf(ptoequipamento.getUltimonsr());
        String str1 = codigo + qtdeRegistros + ultimoNSR;

        char[] data = str1.toCharArray();
        String str = textFormat(data);

        client.sendData(str.toCharArray());

        int dados = client.availableData();
        return dados;
    }

    public void procedimentoManual(final PtoEquipamento ptoequipamento) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (conectado) {
                        int temDados1 = registroManual(client, ptoequipamento);

                        if (temDados1 > 0) {

                            char[] temp = client.receiveData(temDados1); //RECEBENDO DADOS

                            String str = "";
                            for (char chr : temp) {
                                if (chr == ']') {
                                    str = "";
                                }
                                if (Character.isDigit(chr)) {
                                    str += chr;

                                    if (str.length() == 34) {
                                        listaRegistros.add(str);

                                        int var = Integer.valueOf(str.substring(0, 9));
                                        int lastNSRInt = lastNSR;
                                        if (var > lastNSRInt) {
                                            lastNSR = Integer.valueOf(str.substring(0, 9));
                                        }
                                        str = "";
                                    }
                                }
                            }

                            if (listaRegistros.size() > 0) {

                                try {

                                    manager.getTransaction().begin();

                                    for (String item : listaRegistros) {
                                        PtoArquivo arquivo = new PtoArquivo();

                                        int nsr = Integer.valueOf(item.substring(0, 9));
                                        nsrFora = Integer.valueOf(item.substring(0, 9));
                                        int tipo = Integer.valueOf(item.substring(9, 10));

                                        Date HrsString = sdfH.parse(item.substring(18, 22));
                                        String pisString = item.substring(23, 34);
                                        Date data = sdf.parse(item.substring(10, 18));
                                        String periodoString = dataParaPeriodo(data);
                                        String Equipam = ptoequipamento.getDescricao();
                                        Modelo modelo = ptoequipamento.getModelo();

                                        Boolean existe = false;

                                        // verifica se nsr já existe
                                        // para
                                        // este equipamento
//                                        existe = porEquipamentoNSR(ptoequipamento, nsr);
                                        if (existe == false) {

                                            if (tipo != 2) {

                                                arquivo.setEquipamento(Equipam);
                                                arquivo.setData_batida(data);
                                                arquivo.setTipo(tipo);
                                                arquivo.setNsr(nsr);
                                                arquivo.setHora(HrsString);
                                                arquivo.setPis(pisString);
                                                arquivo.setPtooriginal(item);
                                                arquivo.setPeriodo(periodoString);
                                                ptoequipamento.setUltimonsr(nsr);
                                                arquivo.setProcessado(false);

                                                logger.info(" Registro - NSR: " + nsr + " Equipamento: " + ptoequipamento.getDescricao() + " - " + (new Date()));
                                                manager.persist(arquivo);
                                                manager.merge(ptoequipamento);

                                            }
                                        }

                                    }
                                    manager.getTransaction().commit();
                                    manager.close();

                                } catch (Exception e) {
                                    logger.info("## Problema no Try Coleta ##" + e);
                                }

                            }
                            conectado = false;
                            client.disconnect();

                        } else {

                            logger.error("Equipamento IP: " + ptoequipamento.getIp() + " Não POSSUI dados." + (new Date()));

                        }

                        Thread.sleep(500);  //esperando resposta
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    conectado = false;
                    logger.info(" ### problema na conexão ###.");

                }
                logger.info("Conexão finalizada.");

            }

        }
        ).start();

    }

    private Boolean porEquipamentoNSR(PtoEquipamento ptoequipamento, int nsr) {

        Boolean retorno = false;

        int n2 = ptoequipamento.getUltimonsr();

        if (n2 == nsr) {

            retorno = false;
            client.disconnect();

        } else if (n2 != nsr) {
            retorno = true;
        }

        return retorno;
    }

    public String dataParaPeriodo(Date data) throws ParseException {
        if (data != null) {
            return periodoformat.format(data);
        }
        return null;
    }

    public String textFormat(char[] data) {
        String aux = "", aux2 = "", str = "";
        char BYTE_INIT, BYTE_END, BYTE_TAM[] = {0, 0}, BYTE_CKSUM;
        BYTE_INIT = (char) Integer.valueOf("2", 16).intValue();// conf. bit
        // inicial
        BYTE_END = (char) Integer.valueOf("3", 16).intValue();// conf. bit final
        BYTE_TAM[0] = (char) data.length;// conf. tamanho dos dados
        BYTE_TAM[1] = (char) Integer.valueOf("0", 16).intValue();
        aux2 += BYTE_INIT; // Inserindo byte inicial
        aux2 += BYTE_TAM[0]; // Inserindo byte do tamanho
        aux2 += BYTE_TAM[1];
        for (char chr : data) {
            str += chr;
        }
        aux = new String(aux2 + str); // concatenando com a informaÃ§Ã£o

        BYTE_CKSUM = aux.charAt(1);// Calculo do Checksum
        for (int a = 2; a < aux.length(); a++) {
            BYTE_CKSUM = (char) (BYTE_CKSUM ^ aux.charAt(a));
        }
        aux += BYTE_CKSUM; // Inserindo Checksum
        aux += BYTE_END; // Inserindo byte Final
        return aux;

    }

    public String stringHexFormat(String str) {
        String aux = "", temp = "";
        for (char ch : str.toCharArray()) {
            temp += Integer.toHexString(ch).toUpperCase();
            //Converte Hexa em String
            if (temp.length() == 1) {
                aux += "0" + temp + " ";//se tiver 1 digito complementa com 0
            } else {
                aux += temp + " ";
            }
            temp = new String();
        }
        return aux;
    }

    public void verificaColetaRelogio() {

        try {

            Date agora = new Date();

            List<PtoEquipamento> equipamentos = ptoEquipamentoFacade.listaTodosEquipamentos();

            for (PtoEquipamento pe : equipamentos) {

                if (sdf.format(pe.getUltima_coleta()).equals(sdf.format(agora))) {
                    System.err.println("RELOGIO: " + pe.getDescricao());
                    pe.setStatus("Coletado");
//                    ptoEquipamentoFacade.atualiza(pe);
                    System.err.println("COLETADO : SIM");

                } else {

                    System.err.println("RELOGIO: " + pe.getDescricao());
                    pe.setStatus("Não Coletado");
//                    ptoEquipamentoFacade.atualiza(pe);
                    System.err.println("COLETADO : NÃO");
                }

                new GenericDAO<>(PtoEquipamento.class).atualiza(pe);
            }

        } catch (Exception e) {
            System.err.println("Erro Status Relogio: " + e);
            Msg.addMsgWarn("Erro Status Relogio " + e);
        }

    }

    public Usuario pegaUsuario() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
            return usuarioLogado;
        } catch (Exception e) {
        }
        return null;

    }

    public EntityManager getManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public Boolean getStop() {
        return stop;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    public PtoEquipamento getEquipamentoSelecionado() {
        return equipamentoSelecionado;
    }

    public void setEquipamentoSelecionado(PtoEquipamento equipamentoSelecionado) {
        this.equipamentoSelecionado = equipamentoSelecionado;
    }

}
