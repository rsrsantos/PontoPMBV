package br.com.Beans;

import br.com.Facade.FolhaPeriodoFacade;
import br.com.Core.CoreUtils;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.EspelhoPonto;
import br.com.Model.FolhaPeriodo;
import br.com.Model.Funcionario;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class FolhaPeriodoBean implements Serializable {

    EntityManager manager = new JPAConect().getEntityManager();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat sdfh = new SimpleDateFormat("HH:mm");
    SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
    SimpleDateFormat periodo1 = new SimpleDateFormat("yyyyMM");

    StreamedContent stream;

    List<Funcionario> funcionarios;
    List<String> listaFoo;

    FolhaPeriodo periodoFolha;
    Usuario usuario;
    FolhaPeriodoFacade folhaPeriodoFacade;

    private long totalFolhas;

    public FolhaPeriodoBean() {

        if (periodoFolha == null) {
            periodoFolha = new FolhaPeriodo();
        }

        if (folhaPeriodoFacade == null) {
            folhaPeriodoFacade = new FolhaPeriodoFacade();
        }

        if (listaFoo == null) {
            listaFoo = new ArrayList<>();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

        if (funcionarios == null) {
            funcionarios = new ArrayList<>();
        }

    }

    public List<FolhaPeriodo> getFolhaPeriodos() {
        return new GenericDAO<>(FolhaPeriodo.class).listaTodos();
    }

    public String gravarPeriodoFolha() {
        String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");
        try {

            usuario = pegaUsuario();

            if (periodoFolha.getId() == 0) {
                periodoFolha.setUsuario(usuario);
                periodoFolha.setUltima_Alteracao(datahora);
                periodoFolha.setStatus("FECHADO");

                folhaPeriodoFacade.adiciona(periodoFolha);
                Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/folha/lista-periodo/index?faces-redirect=true";

            } else {

                periodoFolha.setUsuario(usuario);
                periodoFolha.setUltima_Alteracao(datahora);

                folhaPeriodoFacade.adiciona(periodoFolha);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/folha/lista-periodo/index?faces-redirect=true";
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
            System.err.println("ERRO: " + e);
        }
        return null;

    }

    public String excluir(FolhaPeriodo periodo) {

        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {

                folhaPeriodoFacade.remove(periodo);
                Msg.addMsgWarn("Periodo Excluido:  " + periodo.getPeriodo());

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/folha/lista-periodo/index?faces-redirect=true";

            } else {

                Msg.addMsgFatal("Somente administrador!");
            }
        } catch (Exception e) {

            Msg.addMsgError("Operação não realizada!");
            System.err.println("ERRO: " + e);
        }
        return null;
    }

    public String editar(FolhaPeriodo periodoFolha) {
        this.periodoFolha = periodoFolha;
        return "/View/folha/cadastrar-periodo/index";

    }

    public String novoPeriodo() {

        try {

            usuario = pegaUsuario();
            periodoFolha = new FolhaPeriodo();
            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                return "/View/folha/cadastrar-periodo/index";

            } else {
                Msg.addMsgFatal("Somente Administrador");
            }
        } catch (Exception e) {

            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

        return null;
    }

    public void abrirFolha(FolhaPeriodo periodoFolha) {

        try {

            usuario = pegaUsuario();

            if (periodoFolha.getStatus().equals("FECHADO")) {

                Date data_inicio = periodoFolha.getInicioFolha();
                Date data_fim = periodoFolha.getFimFolha();

                Locale pt = new Locale("pt", "BR");
                Calendar c = Calendar.getInstance(pt);
                Calendar c1 = Calendar.getInstance(pt);
                Date data = new Date();

                c.setTime(data_inicio);
                c1.setTime(data);
                // formata e exibe a data e hora
                Format format = new SimpleDateFormat("MM");

                //Gerando as Datas
                Date dt = null;

                DateFormat df = new SimpleDateFormat("dd/MM/yy", pt);
                DateFormat dff = new SimpleDateFormat("EEEEEE", pt);
                DateFormat dia = new SimpleDateFormat("dd", pt);

                Date dt1 = data_inicio;
                Date dt2 = data_fim;

                Calendar cal = Calendar.getInstance(pt);
                cal.setTime(dt1);

                Calendar cal1 = Calendar.getInstance(pt);
                cal1.setTime(dt1);

                for (dt = dt1; dt.compareTo(dt2) <= 0;) {
                    if (dt.compareTo(dt2) <= 0) {
                        listaFoo.add(df.format(dt) + "-" + dff.format(dt));
                    }
                    cal1.add(Calendar.DAY_OF_WEEK, +1);
                    dt = cal1.getTime();

                }

                funcionarios = folhaPeriodoFacade.buscadeFuncionarios();
                for (Funcionario funcionarioLista : funcionarios) {

                    for (String item : listaFoo) {
                        System.err.println(listaFoo.size());
                        EspelhoPonto espelho1 = new EspelhoPonto();

                        if (item.contains("Sáb") | item.contains("Dom") | item.contains("Sat") | item.contains("Sun")) {
                            espelho1.setEntrada01("Folga");
                            espelho1.setSaida01("Folga");
                            espelho1.setEntrada02("Folga");
                            espelho1.setSaida02("Folga");
                        }

                        espelho1.setData(item.substring(0, 12));
                        espelho1.setPeriodo(periodo1.format(periodoFolha.getInicioFolha()));
                        espelho1.setNome_Funcionario(funcionarioLista.getNome());
                        espelho1.setPis(funcionarioLista.getPis());
                        espelho1.setUsuario(usuario);
                        espelho1.setFolhaAberta(true);
                        espelho1.setFuncionario(funcionarioLista);
                        espelho1.setData_Batida(df.parse(item.substring(0, 8)));
                        espelho1.setPis(funcionarioLista.getPis());
                        espelho1.setDepartamentos(funcionarioLista.getDepartamento());
                        espelho1.setFuncao(funcionarioLista.getFuncao().getDescricao());
                        espelho1.setMatricula(funcionarioLista.getMatricula());
                        espelho1.setFolhaperiodo(periodoFolha);
                        

                        periodoFolha.setStatus("ABERTO");

                        if (funcionarioLista.getHorario().getCarga().equals("08:00")) {
                            espelho1.setEntrada_prevista_01(sdfh.format(funcionarioLista.getHorario().getHora_EntradaT1_util()));
                            espelho1.setSaida_prevista_01(sdfh.format(funcionarioLista.getHorario().getHora_saidaT1_util()));
                            espelho1.setEntrada_prevista_02(sdfh.format(funcionarioLista.getHorario().getHora_EntradaT2_util()));
                            espelho1.setSaida_prevista_02(sdfh.format(funcionarioLista.getHorario().getHora_saidaT2_util()));
                        } else {
                            espelho1.setEntrada_prevista_01(sdfh.format(funcionarioLista.getHorario().getHora_EntradaT1_util()));
                            espelho1.setSaida_prevista_01(sdfh.format(funcionarioLista.getHorario().getHora_saidaT1_util()));
                        }
                        
                        espelho1.setDia(Integer.parseInt(listaFoo.get(listaFoo.size() -1).substring(0,2)));
                        new GenericDAO<>(EspelhoPonto.class).adiciona(espelho1);
                        new GenericDAO<>(FolhaPeriodo.class).atualiza(periodoFolha);

                    }

                }

            } else {
                Msg.addMsgWarn("Folha já Aberta!");
                periodoFolha = new FolhaPeriodo();
            }

            Msg.addMsgInfo("Folha Mensal Aberta!");

        } catch (ParseException e) {

            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

    }

    public String retornaHoraBatida(List<String> batidas, int posicao, String data) {
        String hora;
        try {
            hora = batidas.get(posicao);
        } catch (IndexOutOfBoundsException e) {

            if (data.contains("Dom") | data.contains("Sáb")) {

                hora = "Folga";

            } else {

                hora = "";
            }

        }
        return hora;
    }

    public void fecharFolha(FolhaPeriodo folha) {

        try {
            folha.setStatus("FECHADO");
            folhaPeriodoFacade.atualiza(folha);
            Msg.addMsgInfo("Folha Fechada");
        } catch (Exception e) {
            System.err.println("ERRO AO FECHAR FOLHA " + e);
            Msg.addMsgWarn("Erro ao fechar folha " + e);
        }

    }
    
    public void reabrirFolha(FolhaPeriodo folha) {

        try {
            folha.setStatus("ABERTO");
            folhaPeriodoFacade.atualiza(folha);
            Msg.addMsgInfo("Folha Reaberta");
        } catch (Exception e) {
            System.err.println("ERRO AO FECHAR FOLHA " + e);
            Msg.addMsgWarn("Erro ao fechar folha " + e);
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

//Gets and setrs
    public FolhaPeriodo getPeriodoFolha() {
        return periodoFolha;
    }

    public void setPeriodoFolha(FolhaPeriodo periodoFolha) {
        this.periodoFolha = periodoFolha;
    }

    public String listar() {
        return "/View/folha/lista-periodo/index?faces-redirect=true";
    }

    public StreamedContent getStream() {
        return stream;
    }

    public void setStream(StreamedContent stream) {
        this.stream = stream;
    }

    public long getTotalFolhas() {
        totalFolhas = folhaPeriodoFacade.contaFolhaPeriodoTotal();
        return totalFolhas;
    }

    public void setTotalFolhas(long totalFolhas) {
        this.totalFolhas = totalFolhas;
    }

}
