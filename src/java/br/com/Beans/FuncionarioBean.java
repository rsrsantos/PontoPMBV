package br.com.Beans;

import br.com.Facade.FuncionarioFacade;
import br.com.Core.CoreUtils;
import br.com.DAO.GenericDAO;
import br.com.Facade.Historico_usuarioFacade;
import br.com.Model.Departamento;
import br.com.Model.Funcao;
import br.com.Model.Funcionario;
import br.com.Model.Horario;
import br.com.Model.PtoEquipamento;
import br.com.Model.TipoAcaoMotivo;
import br.com.Model.Usuario;
import br.com.Model.UsuarioHistorico;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import tcpcom.TcpClient;

@ManagedBean
//@SessionScoped
public class FuncionarioBean implements Serializable {

    PtoEquipamento ptoequipamento;
    final private org.apache.log4j.Logger logger = Logger.getLogger(PtoEquipamentoBeanHenry.class.getName());
    String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");
    private TcpClient client;
    private Boolean conectado;
    private long totalFuncionarios;

    Funcionario funcionario;
    FuncionarioFacade funcionarioFacade;
    Usuario usuario;
    Historico_usuarioFacade historico_usuarioFacade;

    List<Funcionario> listaFuncionario;
    List<String> listaRegistros = new ArrayList<>();

    public FuncionarioBean() {
        if (funcionarioFacade == null) {
            funcionarioFacade = new FuncionarioFacade();
        }

        if (funcionario == null) {
            funcionario = new Funcionario();
        }

        if (ptoequipamento == null) {
            ptoequipamento = new PtoEquipamento();
        }

        if (listaFuncionario == null) {
            listaFuncionario = new ArrayList<>();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

        if (historico_usuarioFacade == null) {
            historico_usuarioFacade = new Historico_usuarioFacade();
        }

    }

    public List<Funcionario> getFuncionarios() {
        return new GenericDAO<>(Funcionario.class).listaTodos();
    }

    public List<Funcao> getFuncaos() {
        return new GenericDAO<>(Funcao.class).listaTodos();
    }

    public List<Departamento> getDepartamentos() {
        return new GenericDAO<>(Departamento.class).listaTodos();
    }

    public List<Horario> getHorarios() {
        return new GenericDAO<>(Horario.class).listaTodos();
    }

    public String gravarFuncionario() {

        try {

            usuario = pegaUsuario();

            Funcionario funcionarioValid = funcionarioFacade.buscaMatricu(funcionario.getMatricula());

            if (funcionario.getId() == 0) {

                if (funcionarioValid != null) {
                    Msg.addMsgWarn("Matricula já Cadastrada");

                } else {
                    funcionario.setUsuario(usuario);
                    funcionario.setUltima_alteracao(datahora);
                    funcionarioFacade.adiciona(funcionario);
                    Msg.addMsgInfo("Cadastro realizado com sucesso! " + funcionario.getNome());

                    UsuarioHistorico historico = new UsuarioHistorico();
                    TipoAcaoMotivo acaoMotivo = new TipoAcaoMotivo();

                    historico.setDataRegistro(new Date());
                    historico.setDescricao("Cadastro de Funcionário - " + funcionario.getNome());
                    acaoMotivo.setId(1);
                    historico.setTipoAcao(acaoMotivo);
                    historico.setUsuario(usuario);
                    historico_usuarioFacade.adiciona(historico);

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);
                    limpaCampos();
                    
                    return "/View/funcionario/lista-funcionario/index?faces-redirect=true";
                }
            } else {

                funcionario.setUsuario(usuario);
                funcionario.setUltima_alteracao(datahora);
                funcionarioFacade.atualiza(funcionario);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! " + funcionario.getNome());

                UsuarioHistorico historico = new UsuarioHistorico();
                TipoAcaoMotivo acaoMotivo = new TipoAcaoMotivo();

                historico.setDataRegistro(new Date());
                historico.setDescricao("Atualização Cadastro de Funcionário - " + funcionario.getNome());
                acaoMotivo.setId(3);
                historico.setTipoAcao(acaoMotivo);
                historico.setUsuario(usuario);
                historico_usuarioFacade.atualiza(historico);
                    
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);
                limpaCampos();
                
                return "/View/funcionario/lista-funcionario/index?faces-redirect=true";
            }
        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada!" + e);
        }

        return null;

    }

    public String novoFuncionario() {
        try {
            funcionario = new Funcionario();
            return "/View/funcionario/cadastrar-funcionario/index?faces-redirect=true";
        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada " + e);
            System.err.println("ERRO: " + e);
        }
        return null;

    }

    public String excluir(Funcionario funcionario) {
        try {
            funcionarioFacade.remove(funcionario);
            this.listaFuncionario.remove(funcionario);
            getListaFuncionario();
            Msg.addMsgWarn("Funcionário Excluido: " + funcionario.getNome());
            return "/View/funcionario/lista-funcionario/index";
        } catch (Exception e) {
            Msg.addMsgWarn("Operação Não Realizada !" + e);
        }
        return null;
    }

    public String editar(Funcionario funcionario) {

        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                this.funcionario = funcionario;
                return "/View/funcionario/cadastrar-funcionario/index";

            } else {

                Msg.addMsgWarn("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());
                System.err.println("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());
            }

        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada" + e);
            System.err.println("ERRO: " + e);
        }

        return null;
    }

    private void limpaCampos() {
        funcionario = new Funcionario();
    }

//=========Envio de Funcionarios=======================================================================================================================
    public void conectarFuncionario(PtoEquipamento ptoequipamento, Funcionario funcionario) {

        try {
            String EQUIPAMENTO_IP = ptoequipamento.getIp();
            String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
                enviarFuncionario(client, ptoequipamento, funcionario);
                Msg.addMsgInfo("Enviado : " + funcionario.getNome());
            } else {
                Msg.addMsgFatal("Problema na Conexão");
                client.disconnect();
            }
        } catch (NumberFormatException e) {
            Msg.addMsgError("Operação não Realizada!" + e);

        }

    }

    public void enviarFuncionario(TcpClient client, PtoEquipamento ptoequipamento, Funcionario funcionario) {

        String codigo = "01+EU+00+1+I[";
        String nome = funcionario.getNome();
        String cod01 = "[";
        long matricula = funcionario.getMatricula();
        String cod02 = "[0[1[";
        String pis = funcionario.getPis();
        String str1 = codigo + pis + cod01 + nome + cod02 + matricula;

        char[] data = str1.toCharArray();
        String str = textFormat(data);
        client.sendData(str.toCharArray());
        int dados = client.availableData();

    }

    //========================Excluir Funcionário ===================================================================================
    public void conectarFuncionarioExcluir(PtoEquipamento ptoequipamento, Funcionario funcionario) {
        try {
            String EQUIPAMENTO_IP = ptoequipamento.getIp();
            String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
                enviarFuncionarioExcluir(client, ptoequipamento, funcionario);
                Msg.addMsgFatal("Funcionário Excluido! " + funcionario.getNome());
            } else {
                Msg.addMsgFatal("Problema na Conexão");
                client.disconnect();
            }
        } catch (NumberFormatException e) {
            Msg.addMsgError("Operação não Realizada!" + e);
        }
    }

    public void enviarFuncionarioExcluir(TcpClient client, PtoEquipamento ptoequipamento, Funcionario funcionario) {

        String codigo = "01+EU+00+1+E[";
        String nome = funcionario.getNome();
        String cod01 = "[";
        long matricula = funcionario.getMatricula();
        String cod02 = "[0[1[";
        String pis = funcionario.getPis();
        String str1 = codigo + pis + cod01 + nome + cod02 + matricula;
        System.out.println("Funcionário : " + str1);

        char[] data = str1.toCharArray();
        String str = textFormat(data);
        client.sendData(str.toCharArray());

    }
    //======================Excluir Biometria ==============================================================================

    public void ExcluirBiometria(TcpClient client, PtoEquipamento ptoequipamento, Funcionario funcionario) {

        try {
            String EQUIPAMENTO_IP = ptoequipamento.getIp();
            String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {

                String codigo = "01+ED+00+E]";
                long matricula = funcionario.getMatricula();
                String str1 = codigo + matricula;
                char[] data = str1.toCharArray();
                String str = textFormat(data);
                client.sendData(str.toCharArray());
                Msg.addMsgFatal("Biometria Excluida: " + funcionario.getNome());
            } else {
                Msg.addMsgFatal("Problema na Conexão");
                client.disconnect();
            }
        } catch (NumberFormatException e) {
            Msg.addMsgError("Operação não Realizada!" + e);
        }

    }

    //======================Pesquisa Funcionário ===========================================================================
    public void conectarFuncionarioPesquisa(PtoEquipamento ptoequipamento, Funcionario funcionario) {
        try {
            String EQUIPAMENTO_IP = ptoequipamento.getIp();
            String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
//                enviarFuncionarioPesquisa(client, ptoequipamento, funcionario);
                procedimentoPesquisa(ptoequipamento, client, conectado, funcionario);
            } else {
                Msg.addMsgFatal("Problema na Conexão");
                client.disconnect();
            }
        } catch (NumberFormatException e) {
            Msg.addMsgError("Operação não Realizada!" + e);
        }
    }

    public int enviarFuncionarioPesquisa(TcpClient client, PtoEquipamento ptoequipamento, Funcionario funcionario) {

        String codigo = "01+RU+00+-2]";
        String pis = funcionario.getPis();
        String str1 = codigo + pis;

        char[] data = str1.toCharArray();
        String str = textFormat(data);
        client.sendData(str.toCharArray());
        int dados = client.availableData();
        return dados;
    }

    public String procedimentoPesquisa(final PtoEquipamento ptoequipamento, final TcpClient client, final Boolean solicitarRegistros, Funcionario funcionario) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (conectado) {

//                        if (solicitarRegistros == true) {
                        int temDados = enviarFuncionarioPesquisa(client, ptoequipamento, funcionario);

                        if (temDados > 0) {

                            char[] temp = client.receiveData(temDados); //RECEBENDO DADOS
//                            Msg.addMsgInfo("Funcionário Encontrado");
                            String str8 = "", aux = "";
                            for (char chr : temp) {
                                str8 += chr;
                                System.out.println(str8);

                            }

                            conectado = false;
                            client.disconnect();

                        } else {

                            logger.error("Funcionário não encontrado!" + funcionario.getNome());

                        }

                        Thread.sleep(5000);  //esperando resposta

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    conectado = false;
                    logger.info(" ### problema na conexão ###.");

                }
                logger.info("Conexão finalizada.");

            }

        }
        ).start();
        return null;

    }

    //=========================RecebeBiometria=======================================
    public void conectarBiometria(PtoEquipamento ptoequipamento, Funcionario funcionario) {
        try {
            String EQUIPAMENTO_IP = ptoequipamento.getIp();
            String EQUIPAMENTO_PORTA = ptoequipamento.getPorta();

            client = new TcpClient(EQUIPAMENTO_IP, Integer.valueOf(EQUIPAMENTO_PORTA));
            conectado = client.connect();
            if (client.isConnected()) {
//                enviarFuncionarioPesquisa(client, ptoequipamento, funcionario);
                procedimentoBiometria(ptoequipamento, client, conectado, funcionario);
            } else {
                Msg.addMsgFatal("Problema na Conexão");
                client.disconnect();
            }
        } catch (NumberFormatException e) {
            Msg.addMsgError("Operação não Realizada!" + e);
        }
    }

    public int recebeBiometria(TcpClient client, PtoEquipamento ptoequipamento, Funcionario funcionario) {

        String codigo = "01+RD+00+D]";
        long pis = funcionario.getMatricula();
        String str1 = codigo + pis;

        char[] data = str1.toCharArray();
        String str = textFormat(data);
        client.sendData(str.toCharArray());
        int dados = client.availableData();
        return dados;
    }

    public String procedimentoBiometria(final PtoEquipamento ptoequipamento, final TcpClient client, final Boolean solicitarRegistros, Funcionario funcionario) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (conectado) {
//                        if (solicitarRegistros == true) {
                        int temDados = recebeBiometria(client, ptoequipamento, funcionario);

                        if (temDados > 0) {

                            char[] temp = client.receiveData(temDados); //RECEBENDO DADOS
                            String str8 = "";
                            for (char chr : temp) {
                                str8 += chr;
                                System.out.println(str8);
                            }
                            conectado = false;
                            client.disconnect();

                        } else {

                            logger.error("Funcionário não encontrado!" + funcionario.getNome());

                        }

                        Thread.sleep(500);  //esperando resposta

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    conectado = false;
                    logger.info(" ### problema na conexão ###.");

                }
                logger.info("Conexão finalizada.");

            }

        }
        ).start();
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

    private int currentTab = 0;

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }

    public void onTabChange(org.primefaces.event.TabChangeEvent event) {
        String id = event.getTab().getId();
        if (id.equals("tab01")) {
            this.setCurrentTab(01);
        } else if (id.equals("tab02")) {
            this.setCurrentTab(02);
        } else if (id.equals("tab03")) {
            this.setCurrentTab(03);
        }
    }

    public String listar() {
        return "/View/funcionario/lista-funcionario/index?faces-redirect=true";
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

//Gets and Setrs    
    public List<Funcionario> getListaFuncionario() {
        try {
            usuario = pegaUsuario();

            List<Departamento> departamentoUsuario = usuario.getDepartamento();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                listaFuncionario = funcionarioFacade.listaFuncionarioGeral();

            } else if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("gestor")) {

                for (Departamento item : departamentoUsuario) {
                    List<Funcionario> retorno = funcionarioFacade.listaFuncionarioDepartamento(item.getId());
                    for (Funcionario retornoFuncionario : retorno) {
                        listaFuncionario.add(retornoFuncionario);
                        System.err.println("Funcionarios : " + retornoFuncionario);
                    }
                }

            }

        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

        return listaFuncionario;
    }

    public void setListaFuncionario(List<Funcionario> listaFuncionario) {
        this.listaFuncionario = listaFuncionario;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public long getTotalFuncionarios() {
        return totalFuncionarios = funcionarioFacade.contaFuncionarioTotal();
    }

    public void setTotalFuncionarios(long totalFuncionarios) {
        this.totalFuncionarios = totalFuncionarios;
    }

}
