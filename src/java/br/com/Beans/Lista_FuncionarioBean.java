package br.com.Beans;

import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Funcionario;
import br.com.Model.Funcionario_resposta;
import br.com.Model.PtoEquipamento;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import tcpcom.TcpClient;

@ManagedBean
public class Lista_FuncionarioBean implements Serializable {

    Funcionario funcionario;
    public Funcionario_resposta getResposta() {
        return resposta;
    }

    public void setResposta(Funcionario_resposta resposta) {
        this.resposta = resposta;
    }
    Funcionario_resposta resposta = new Funcionario_resposta();

    public List<String> getListaFuncionarios() {
        return listaFuncionarios;
    }

    public List<Funcionario_resposta> getFuncionario_respostas() {
        return new GenericDAO<>(Funcionario_resposta.class).listaTodos();

    }

    private TcpClient client;
    private org.apache.log4j.Logger logger = Logger.getLogger(PtoEquipamentoBeanHenry.class.getName());
    private Boolean conectado;
    EntityManager manager = new JPAConect().getEntityManager();

    private List<String> listaFuncionarios = new ArrayList<>();

    public void setListaFuncionarios(List<String> listaFuncionarios) {
        this.listaFuncionarios = listaFuncionarios;
    }

    public boolean procedimentobiometria(final TcpClient client, final Boolean solicitarRegistros, Funcionario funcionario) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (conectado) {

//                        if (solicitarRegistros == true) {
                        int temDados = RecebeFuncionarios(funcionario);

                        if (temDados > 0) {

                            char[] temp = client.receiveData(temDados); //RECEBENDO DADOS

                            String str = "";
                            for (char chr : temp) {
                                str += chr;
                            }

                            String str1 = str.replaceAll("[ãâàáä]", "a")
                                    .replaceAll("[êèéë]", "e")
                                    .replaceAll("[îìíï]", "i")
                                    .replaceAll("[õôòóö]", "o")
                                    .replaceAll("[ûúùü]", "u")
                                    .replaceAll("[ÃÂÀÁÄ]", "A")
                                    .replaceAll("[ÊÈÉË]", "E")
                                    .replaceAll("[ÎÌÍÏ]", "I")
                                    .replaceAll("[ÕÔÒÓÖ]", "O")
                                    .replaceAll("[ÛÙÚÜ]", "U")
                                    .replace('ç', 'c')
                                    .replace('Ç', 'C')
                                    .replace('ñ', 'n')
                                    .replace('Ñ', 'N')
                                    .replaceAll("!", " ")
                                    .replaceAll("ø", "")
                                    .replaceAll("\\+", "")
                                    .replaceAll("01RU0003", "")
                                    .replaceAll("\\[\\´\\`\\@\\#\\$\\%\\¨\\*\\?", "")
                                    .replaceAll("\\(\\)\\=\\{\\}\\[\\]\\~\\^\\]", "")
                                    .replaceAll("[\\.\\;\\-\\_\\'\\ª\\º\\:\\;\\/]", "");

                            StringBuilder s = new StringBuilder(str1);
                            s.delete(0, 3);
                            String u = s.toString();
                            String[] linha = u.split("]");

                            if (linha.length > 0) {

                                try {

                                    manager.getTransaction().begin();

                                    for (String item : linha) {
                                        System.out.println(item);
                                        Funcionario_resposta resposta1 = new Funcionario_resposta();

                                        resposta1.setColaboradores(item);
                                        manager.persist(resposta1);
                                    }

                                    manager.getTransaction().commit();
                                    manager.close();

                                } catch (Exception e) {
                                }
                            }
                            conectado = false;
                            client.disconnect();

                        } else {
//                            logger.error("Equipamento IP: " + ptoequipamento.getIp() + " Não POSSUI dados de Biometria." + (new Date()));
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

        return true;
    }

    public int RecebeFuncionarios(Funcionario funcionario) {

        String codigo = "01+RU+00+-2]";
        String pis = funcionario.getPis();
        String str1 = codigo + pis;
        char[] data = str1.toCharArray();
        String str = textFormat(data);
        client.sendData(str.toCharArray());
        int dados = client.availableData();

        return dados;

    }
//=================================================================================================================================================

    public void conectar(PtoEquipamento ptoequipamento, Funcionario funcionario) {

        final String EQUIPAMENTO_IP = ptoequipamento.getIp();
        final String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();
        try {
            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));

            conectado = client.connect();
            if (client.isConnected()) {
                Msg.addMsgInfo("Funcionário Verificado");
                procedimentobiometria(client, conectado, funcionario);
            } else {

                Msg.addMsgFatal("Problema na Conexão");
            }
        } catch (Exception e) {
            Msg.addMsgFatal("Operação Não Realizada : " + e);

        }
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

    public String excluir(Funcionario_resposta resposta) {
        new GenericDAO<>(Funcionario_resposta.class).remove(resposta);
        Msg.addMsgFatal("Excluido");
        return "/View/equipamento/enviarRecebeFuncionario";

    }
}
