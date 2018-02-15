package br.com.Beans;

import br.com.Facade.PtoEquipamentoFacade;
import br.com.Model.PtoEquipamento;
import br.com.Model.PtoArquivo;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Facade.PtoArquivoFacade;
import br.com.Model.Departamento;
import br.com.Model.Funcionario;
import br.com.Model.Modelo;
import br.com.Model.TipoCorte;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import tcpcom.TcpClient;

@ManagedBean
@SessionScoped
public class PtoEquipamentoBeanHenry implements Serializable {

    TipoCorte tipo;
    Usuario usuario;
    PtoEquipamentoFacade equipamentoFacade;
    List<Departamento> listDepartamento;
    PtoEquipamentoFacade ptoEquipamentoFacade;
    PtoArquivoFacade arquivoFacade;
    PtoEquipamento ptoEquipamento;
    PtoEquipamento equipamentoSelecionado;
    List<PtoEquipamento> ListEquipamento;
    Departamento DepartamentoSelecionado;

    private TcpClient client;
    String u;
    private Boolean conectado;
    private int lastNSR = 0;
    final private org.apache.log4j.Logger logger = Logger.getLogger(PtoEquipamentoBeanHenry.class.getName());
    final List<String> listaRegistros = new ArrayList<>();
    private int nsrFora;
    EntityManager manager = new JPAConect().getEntityManager();
    private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    private static final SimpleDateFormat sdfH = new SimpleDateFormat("HHmm");
    SimpleDateFormat periodoformat = new SimpleDateFormat("yyyyMM");

    public PtoEquipamentoBeanHenry() {

        if (tipo == null) {
            tipo = new TipoCorte();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

        if (equipamentoFacade == null) {
            equipamentoFacade = new PtoEquipamentoFacade();
        }

        if (listDepartamento == null) {
            listDepartamento = new ArrayList<>();
        }

        if (ptoEquipamentoFacade == null) {
            ptoEquipamentoFacade = new PtoEquipamentoFacade();
        }

        if (ListEquipamento == null) {
            ListEquipamento = new ArrayList();
        }

        if (listDepartamento == null) {
            listDepartamento = new ArrayList();
        }

        if (DepartamentoSelecionado == null) {
            DepartamentoSelecionado = new Departamento();
        }

    }

    public List<PtoEquipamento> getPtoEquipamentos() {
        return new GenericDAO<>(PtoEquipamento.class).listaTodos();

    }

    public List<TipoCorte> getTipoCortes() {
        return new GenericDAO<>(TipoCorte.class).listaTodos();

    }

    public void listaEquipamento() {

        ListEquipamento.clear();
        ListEquipamento = ptoEquipamentoFacade.ListEquipamentoDepartamento(DepartamentoSelecionado);
//        verificaColetaRelogio();
    }

//    public void verificaColetaRelogio() {
//
//        try {
//
//            Date agora = new Date();
//
//            for (PtoEquipamento pe : ListEquipamento) {
//                System.err.println("EQUIPAMENTO " + pe.getDescricao());
//
//                if (sdf.format(pe.getUltima_coleta()).equals(sdf.format(agora))) {
//
//                    System.err.println("RELOGIO: " + pe.getDescricao());
//                    pe.setStatus("Coletado");
//                    ptoEquipamentoFacade.atualiza(pe);
//                    System.err.println("COLETADO : SIM");
//
//                } else {
//
//                    System.err.println("RELOGIO: " + pe.getDescricao());
//                    pe.setStatus("Não Coletado");
//                    ptoEquipamentoFacade.atualiza(pe);
//                    System.err.println("COLETADO : NÃO");
//                }
//
//            }
//
//        } catch (Exception e) {
//            System.err.println("Erro Status Relogio: " + e);
//            Msg.addMsgWarn("Erro Status Relogio " + e);
//        }
//        DepartamentoSelecionado = null;
//
//    }

    public void conectarManual(PtoEquipamento ptoequipamento) {

        final String EQUIPAMENTO_IP = ptoequipamento.getIp();
        final String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

        try {
            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
                procedimentoManual(ptoequipamento);

            } else {
                Msg.addMsgWarn("Problema na conexão");
                ptoequipamento.setStatus("Problema na Conexão");
                ptoEquipamentoFacade.atualiza(ptoequipamento);

            }
        } catch (NumberFormatException e) {
            System.err.println("ERRO: " + e);
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
                                        existe = porEquipamentoNSR(ptoequipamento, nsr);
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
                                                ptoequipamento.setStatus("COLETANDO....");

                                                logger.info(" Registro - NSR: " + nsr + " Equipamento: " + ptoequipamento.getDescricao() + " - " + (new Date()));
                                                manager.persist(arquivo);
                                                manager.merge(ptoequipamento);

                                            }
                                        } else {
                                            System.err.println("Todos os registros coletados");
                                            ptoequipamento.setStatus("Todos os registros Coletados");
                                            ptoequipamento.setUltima_coleta(new Date());
                                            manager.merge(ptoequipamento);
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

    public String dataParaPeriodo(Date data) throws ParseException {
        if (data != null) {
            return periodoformat.format(data);
        }
        return null;
    }

    private Boolean porEquipamentoNSR(PtoEquipamento ptoequipamento, int nsr) {

        Boolean retorno = false;

        int n2 = ptoequipamento.getUltimonsr();

        if (n2 == nsr) {

            retorno = true;
            client.disconnect();

        } else {
            retorno = false;
        }

        return retorno;

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

    public void hora(PtoEquipamento ptoequipamento) {

        final String EQUIPAMENTO_IP = ptoequipamento.getIp();
        final String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();
        try {
            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));

            conectado = client.connect();

            if (client.isConnected()) {

                String str;     //Recebendo data do sistema e formatando string
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                str = new String("01+EH+00+" + dateFormat.format(new Date()) + "]00/00/00]00/00/00");
                str = textFormat(str.toCharArray());//formatando texto (cabeçalho, checksum e Byte final)
                client.sendData(str.toCharArray());
                Msg.addMsgInfo("Data e Hora Atualizados com Sucesso!");
                logger.info("####-Hora Enviada com Sucesso-### :" + EQUIPAMENTO_IP);
            } else {
                Msg.addMsgFatal("Problema na Conexão");
            }
        } catch (NumberFormatException e) {
            Msg.addMsgError("Operação não Realizada!" + e);
        }

    }

    public String senhaMenu(PtoEquipamento ptoequipamento) {

        final String EQUIPAMENTO_IPp = ptoequipamento.getIp();
        final String EQUIPAMENTO_PORTAa = ptoequipamento.getPorta();
        try {
            client = new TcpClient(EQUIPAMENTO_IPp, Integer.valueOf(EQUIPAMENTO_PORTAa));

            conectado = client.connect();

            if (client.isConnected()) {

                String str1 = "01+EC+00+SENHA_MENU[";
                String password = ptoequipamento.getSenhaMenu();
//                String corte = ptoequipamento.getTipoCorte();
                String cd2 = "]";
                String codigo = str1 + password + cd2;
                char[] data = codigo.toCharArray();
                String str = textFormat(data);
                client.sendData(str.toCharArray());
                Msg.addMsgInfo("Configurações Enviadas");

            } else {
                Msg.addMsgFatal("Problema na Conexão");
            }
        } catch (NumberFormatException e) {
            Msg.addMsgError("Operação não Realizada!" + e);
            return "/View/receAfd?faces-redirect=true";
        }
        return null;

    }

    public String conectarFuncionarios(PtoEquipamento ptoequipamento, final Funcionario funcionario) {

        String ip = ptoequipamento.getIp();
        String porta = ptoequipamento.getPorta();

        client = new TcpClient(ip, Integer.valueOf(porta));

        client.connect();

        if (client.isConnected()) {

            String str1 = "01+RD+00+D]847010";
            char[] data = str1.toCharArray();
            String str = textFormat(data);
            client.sendData(str.toCharArray());
            int dados = client.availableData();
            System.out.println(dados);
        }

        return "/View/equipamento/receAfd";
    }

    //=================================Receber Manual===============================================
    public String gravarEquipamento() {

        try {

            PtoEquipamento equipamentoVerifica = equipamentoFacade.buscaEquipamentoHenry(ptoEquipamento.getDescricao());

            if (ptoEquipamento.getId() == 0) {

                if (equipamentoVerifica != null) {
                    Msg.addMsgWarn("Equipamento já cadastrado");
                } else {

                    ptoEquipamento.setStatus("Não Coletado");
                    ptoEquipamento.setUltima_coleta(new Date());
                    equipamentoFacade.adiciona(ptoEquipamento);
                    Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);

                    return "/View/equipamento/equipamento-Henry/index?faces-redirect=true";
                }
            } else {
                equipamentoFacade.atualiza(ptoEquipamento);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/equipamento/equipamento-Henry/index?faces-redirect=true";
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!" + e);
            System.err.println("ERRO: " + e);
        }
        return null;

    }

    public String excluir(PtoEquipamento ptoequipamento) {

        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                equipamentoFacade.remove(ptoequipamento);

                Msg.addMsgWarn("Exclusão Realizada");
                return "/View/equipamento/receAfd";
            } else {
                Msg.addMsgFatal("Não Autorizado!");
            }
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

        return null;
    }

    public String editar(PtoEquipamento ptoequipamento) {
        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equals("admin")) {

                this.ptoEquipamento = ptoequipamento;
                return "/View/equipamento/cadastrar-equipamentoHenry/index";

            } else {

                Msg.addMsgFatal("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());
                System.err.println("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());
            }
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

        return null;

    }

    public String novoEquipamento() {

        try {
            ptoEquipamento = new PtoEquipamento();
            return "/View/equipamento/cadastrar-equipamentoHenry/index?faces-redirect=true";

        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

        return null;

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

    public String listar() {
        listDepartamento.clear();
        DepartamentoSelecionado = null;
        ListEquipamento.clear();
        return "/View/equipamento/equipamento-Henry/index?faces-redirect=true";
    }

//gets and Setrs
    public TipoCorte getTipo() {
        return tipo;
    }

    public void setTipo(TipoCorte tipo) {
        this.tipo = tipo;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public PtoEquipamento getPtoequipamento() {
        return ptoEquipamento;
    }

    public void setPtoequipamento(PtoEquipamento ptoequipamento) {
        this.ptoEquipamento = ptoequipamento;
    }

    public List<Departamento> getListDepartamento() {
        listDepartamento = equipamentoFacade.listaTodosDepartamentos();
        return listDepartamento;
    }

    public void setListDepartamento(List<Departamento> listDepartamento) {
        this.listDepartamento = listDepartamento;
    }

    public List<PtoEquipamento> getListEquipamento() {
        return ListEquipamento;
    }

    public void setListEquipamento(List<PtoEquipamento> ListEquipamento) {
        this.ListEquipamento = ListEquipamento;
    }

    public Departamento getDepartamentoSelecionado() {
        return DepartamentoSelecionado;
    }

    public void setDepartamentoSelecionado(Departamento DepartamentoSelecionado) {
        this.DepartamentoSelecionado = DepartamentoSelecionado;
    }

}
