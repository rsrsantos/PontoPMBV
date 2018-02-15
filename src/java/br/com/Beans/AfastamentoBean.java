package br.com.Beans;

import br.com.Facade.AfastamentoFacade;
import br.com.Core.CoreUtils;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Afastamento;
import br.com.Model.EspelhoPonto;
import br.com.Model.Funcionario;
import br.com.Model.Justificativa;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

@ManagedBean
@SessionScoped
public class AfastamentoBean implements Serializable {

    Afastamento afastamento;
    AfastamentoFacade afastamentofacade;
    Usuario usuario;

    EntityManager manager = new JPAConect().getEntityManager();

    String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");
    DateFormat df = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat periodo = new SimpleDateFormat("yyyyMM");

    private List<Afastamento> afastamentoselecionado;
    List<String> listaFoo = new ArrayList<>();

    private boolean disable;
    String periodoString = "0";
    private long totalAfastamento;

    public AfastamentoBean() {
        if (afastamento == null) {
            afastamento = new Afastamento();
        }

        if (afastamentofacade == null) {
            afastamentofacade = new AfastamentoFacade();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }
    }

    ///Listas de Acesso
    public List<Afastamento> getAfastamentos() {
        return new GenericDAO<>(Afastamento.class).listaTodos();
    }

    public List<Funcionario> getFuncionarios() {
        return new GenericDAO<>(Funcionario.class).listaTodos();
    }

    public List<Justificativa> getJustificativas() {
        return new GenericDAO<>(Justificativa.class).listaTodos();
    }

    //Metodos
    public String gravarAfastamento() {

        try {
            usuario = pegaUsuario();
            boolean existe = true;

            if (afastamento.getId() == 0) {
                if (existe == true) {

                    afastamento.setData_criacao(new Date());
                    afastamento.setUsuario(usuario);
                    afastamento.setUltima_alteracao(datahora);
                    afastamentofacade.adiciona(afastamento);
                    Msg.addMsgInfo("Cadastro realizado com Secusso! ");

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);

                    return "/View/afastamento/lista-afastamento/index?faces-redirect=true";
                } else {
                    Msg.addMsgWarn("Periodo já cadastrado para o Funcionário");
                }
            } else {
                afastamento.setUsuario(usuario);
                afastamento.setUltima_alteracao(datahora);
                afastamentofacade.atualiza(afastamento);

                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/afastamento/lista-afastamento/index?faces-redirect=true";

            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!" + e);
        }
        return null;
    }

    public String excluir(Afastamento afastamento) {

        try {
            usuario = pegaUsuario();
            if (usuario.getUsuarioPerfil().getDescricao().equals("admin")) {
                afastamentofacade.remove(afastamento);
                Msg.addMsgWarn("Afastamento Excluido:  " + afastamento.getDescricao());

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/afastamento/lista-afastamento/index?faces-redirect=true";
            } else {
                Msg.addMsgWarn("Somente Administrador !");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
            System.err.println("ERRO: " + e);
        }
        return null;
    }

    public String editar(Afastamento afastamento) {
        this.afastamento = afastamento;
        return "/View/afastamento/cadastrar-afastamento/index";

    }

    public String novoAfastamento() {
        try {
            usuario = pegaUsuario();
            afastamento = new Afastamento();
            if (usuario.getUsuarioPerfil().getDescricao().equals("admin")) {
                return "/View/afastamento/cadastrar-afastamento/index";
            } else {
                Msg.addMsgWarn("Somente Administrador");
                System.err.println("Somente Administrador");
            }
        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada " + e);
            System.err.println("ERRO: " + e);

        }
        return null;
    }

    public void processaAfastamento(Afastamento afastado) {

        try {

            usuario = pegaUsuario();

            Date data_inicio = null;
            Date data_fim = null;

            List<Afastamento> afastamentos = afastamentofacade.buscaFuncionariosAfastamento(afastado.getId());
            for (Afastamento afasta : afastamentos) {

                List<Funcionario> funcionarios = afasta.getFuncionario();

                data_inicio = afasta.getData_inicio();

                data_fim = afasta.getData_fim();

                Calendar c = Calendar.getInstance();
                Calendar c1 = Calendar.getInstance();
                Date data = new Date();

                c.setTime(data_inicio);
                c1.setTime(data);
                // formata e exibe a data e hora
                Format format = new SimpleDateFormat("MM");
                String mes_informado = format.format(c.getTime());
                String mes_atual = format.format(c1.getTime());

                //Gerando as Datas
                Date dt = null;

                DateFormat df = new SimpleDateFormat("dd/MM/yy");
                DateFormat dff = new SimpleDateFormat("EEEEEE");

                Date dt1 = data_inicio;
                Date dt2 = data_fim;

                Calendar cal = Calendar.getInstance();
                cal.setTime(dt1);

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(dt1);

                for (dt = dt1; dt.compareTo(dt2) <= 0;) {
                    if (dt.compareTo(dt2) <= 0) {
                        listaFoo.add(df.format(dt));
                    }
                    cal1.add(Calendar.DAY_OF_WEEK, +1);
                    dt = cal1.getTime();

                }
                if (afastado.isProcessado() == false) {
                    manager.getTransaction().begin();
                    for (Funcionario funcionario : funcionarios) {
                        for (String item : listaFoo) {
                            Date dataBatida = df.parse(item);

                            List<EspelhoPonto> retornoEspelho = afastamentofacade.buscaByDate(funcionario.getPis(), dataBatida);
                            for (EspelhoPonto espelhoRecalcular : retornoEspelho) {
                                System.err.println(espelhoRecalcular.getData_Batida());
                                if (espelhoRecalcular.getEntrada01().equals("Folga")) {
                                    System.err.println("Final de Semana");
                                } else {

                                    espelhoRecalcular.setEntrada01(afasta.getJustificativa().getDescricao());
                                    espelhoRecalcular.setEntrada02(afasta.getJustificativa().getDescricao());
                                    espelhoRecalcular.setSaida01(afasta.getJustificativa().getDescricao());
                                    espelhoRecalcular.setSaida02(afasta.getJustificativa().getDescricao());
                                    espelhoRecalcular.setUsuario(usuario);
                                    espelhoRecalcular.setNome_Funcionario(funcionario.getNome());
                                    espelhoRecalcular.setPis(funcionario.getPis());
                                    espelhoRecalcular.setAbonar(true);
                                    afastado.setProcessado(true);
                                    manager.merge(espelhoRecalcular);
                                    manager.merge(afastado);
                                }

                            }

                        }
                    }
                } else {
                    Msg.addMsgError("Erro, Já Processado");
                }

            }

        } catch (ParseException e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("ERRO: " + e);
        }
        manager.getTransaction().commit();
        manager.close();
        Msg.addMsgInfo("Afastamento Processado");

    }

    public Date getTodayPlusThree() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -3);
        return c.getTime();
    }

    public String dataParaPeriodo(Date data) throws ParseException {
        if (data != null) {
            return periodo.format(data);
        }
        return null;
    }

    public String listar() {
        return "/View/afastamento/lista-afastamento/index?faces-redirect=true";
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

    //Gets and Seters
    public Afastamento getAfastamento() {
        return afastamento;
    }

    public void setAfastamento(Afastamento afastamento) {
        this.afastamento = afastamento;
    }

    public String getDatahora() {
        return datahora;
    }

    public void setDatahora(String datahora) {
        this.datahora = datahora;
    }

    public List<Afastamento> getAfastamentoselecionado() {
        return afastamentoselecionado;
    }

    public void setAfastamentoselecionado(List<Afastamento> afastamentoselecionado) {
        this.afastamentoselecionado = afastamentoselecionado;
    }

    public List<String> getListaFoo() {
        return listaFoo;
    }

    public void setListaFoo(List<String> listaFoo) {
        this.listaFoo = listaFoo;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public long getTotalAfastamento() {
        totalAfastamento = afastamentofacade.contaAfastamentoTotal();
        return totalAfastamento;
    }

    public void setTotalAfastamento(long totalAfastamento) {
        this.totalAfastamento = totalAfastamento;
    }

}
