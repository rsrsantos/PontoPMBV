package br.com.Beans;

import br.com.Facade.BancoHorasExtratoFacade;
import br.com.Facade.BancoHorasFacade;
import br.com.Facade.EspelhoPontoFacade;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.BancoHoras;
import br.com.Model.BancoHorasExtrato;
import br.com.Model.Departamento;
import br.com.Model.EspelhoPonto;
import br.com.Model.FolhaPeriodo;
import br.com.Model.Funcionario;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;

@ManagedBean
@ViewScoped
public class BancoHorasBean implements Serializable {

    SimpleDateFormat sdfh = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat periodoFormat = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm");
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    EntityManager manager = new JPAConect().getEntityManager();

    private boolean carregado;
    private boolean carregadoBt;
    long resultSetCredito = 0;
    long resultSetDebito = 0;
    String StringSaldoFinal;
    String StringSaldoAnterior;
    String StringSaldoAtual;
    long TotalCredito = 0;
    long TotalDebito = 0;
    String TotalCreditoString;
    String TotalDebitoString;
    long resultFinal;
    long resultFinalSaldo;
    String horaBanco;
    String item;
    Date data_Lancamento;
    String observacao;

    private EspelhoPontoFacade espelhofacade;
    private Departamento departamento;
    private Funcionario funcionarioSelecionado;
    private FolhaPeriodo periodo;
    private BancoHoras bancohoras;
    private BancoHorasFacade bancohorasfacade;
    private EspelhoPonto espelho;
    private BancoHorasExtrato extratoHorasBanco;
    private BancoHorasExtratoFacade extratofacade;
    private EspelhoPontoFacade espelhoPontoFacade;
    private Usuario usuario;

    private final List<String> listaExtrato = new ArrayList<>();
    private List<BancoHorasExtrato> listaBancoHorasporPessoa;
    private List<BancoHorasExtrato> listaBancoHorasporPessoaFinal;
    private List<BancoHorasExtrato> listaBancoHorasporPessoaAnteriorSaldo;
    private List<Funcionario> listapordepartamento;
    private List<Funcionario> funcionarioPorDepartamento;
    private List<String> retornoExtrato;
    private List<Departamento> listadepartamento;
    private List<BancoHoras> listaBanco;
    private List<String> listaStringBanco;
    private final List<String> listaMinutosDebito = new ArrayList();
    private final List<String> listaMinutosCredito = new ArrayList();

    public BancoHorasBean() {

        if (funcionarioSelecionado == null) {
            funcionarioSelecionado = new Funcionario();
        }

        if (bancohorasfacade == null) {
            bancohorasfacade = new BancoHorasFacade();
        }

        if (espelhofacade == null) {
            espelhofacade = new EspelhoPontoFacade();
        }

        if (bancohoras == null) {
            bancohoras = new BancoHoras();
        }

        if (listadepartamento == null) {
            listadepartamento = new ArrayList<>();
        }

        if (periodo == null) {
            periodo = new FolhaPeriodo();
        }

        if (departamento == null) {
            departamento = new Departamento();
        }

        if (listaBancoHorasporPessoa == null) {
            listaBancoHorasporPessoa = new ArrayList<>();
        }

        if (listaStringBanco == null) {
            listaStringBanco = new ArrayList<>();
        }

        if (extratoHorasBanco == null) {
            extratoHorasBanco = new BancoHorasExtrato();
        }

        if (extratofacade == null) {
            extratofacade = new BancoHorasExtratoFacade();
        }

        if (espelhoPontoFacade == null) {
            espelhoPontoFacade = new EspelhoPontoFacade();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

        if (funcionarioPorDepartamento == null) {
            funcionarioPorDepartamento = new ArrayList<>();
        }

    }

    public List<Departamento> getDepartamentos() {
        return new GenericDAO<>(Departamento.class).listaTodos();
    }

    public List<Funcionario> getFuncionarios() {
        return new GenericDAO<>(Funcionario.class).listaTodos();
    }

    public List<FolhaPeriodo> getFolhaPeriodos() {
        return new GenericDAO<>(FolhaPeriodo.class).listaTodos();
    }

    public void carregarFuncionarios() {

        if (this.departamento == null) {
            Msg.addMsgError("Selecione o Departamento");
            return;
        }
        try {

            listapordepartamento = espelhofacade.buscadepartamento(departamento);
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada: " + e);

        }

    }

    public void extratoBancoFuncionario() throws ParseException {
        boolean temDados = false;
        listaBancoHorasporPessoa = bancohorasfacade.buscaBancoHorasExtrato(funcionarioSelecionado, periodo.getPeriodo());
        for (BancoHorasExtrato extratos : listaBancoHorasporPessoa) {

            if (extratos.getPeriodo().isEmpty()) {
                Msg.addMsgWarn("Sem banco para esse periodo!");
            } else {
                temDados = true;
                String itemExtratoNegativo = this.retornaHoraCalculo(extratos.getSaldoNegativo().replace("-:-", "00:00"));
                listaMinutosDebito.add(itemExtratoNegativo);

                String itemCredito = this.retornaHoraCalculo(extratos.getSaldoPositivo().replace("-:-", "00:00"));

                listaMinutosCredito.add(itemCredito);
            }

        }
        if (temDados == true) {
            carregadoBt = true;
            try {
                for (String item1 : listaMinutosCredito) {

                    long hora = Long.valueOf(item1.substring(0, 2));
                    long minuto = Long.valueOf(item1.substring(3, 5));
                    long segundo = 0;
                    long minutosTrabalhado = (hora * 60) + minuto + (segundo / 60);

                    long soma = TotalCredito;
                    long soma01 = minutosTrabalhado;
                    TotalCredito = soma01 + soma;

                    long minutos = TotalCredito;
                    long inteira = minutos / 60;
                    long resto = minutos % 60;

                    TotalCreditoString = String.format("%02d:%02d", inteira, resto);

                }

                for (String item2 : listaMinutosDebito) {

                    long hora = Long.valueOf(item2.substring(0, 2));
                    long minuto = Long.valueOf(item2.substring(3, 5));
                    long segundo = 0;
                    long minutosTrabalhado = (hora * 60) + minuto + (segundo / 60);

                    long soma = TotalDebito;
                    long soma01 = minutosTrabalhado;
                    TotalDebito = soma01 + soma;

                    long minutos = TotalDebito;
                    long inteira = minutos / 60;
                    long resto = minutos % 60;

                    TotalDebitoString = String.format("%02d:%02d", inteira, resto);

                }
                if (TotalCreditoString.equals("00:00")) {
                    StringSaldoFinal = "-" + TotalDebitoString;

                } else if (TotalDebitoString.equals("00:00")) {
                    StringSaldoFinal = "+" + TotalCreditoString;
                } else {

                    if (TotalDebitoString.length() == 6) {
                        long hora = Long.valueOf(TotalCreditoString.substring(0, 2));
                        long minuto = Long.valueOf(TotalCreditoString.substring(3, 5));
                        long segundo = 0;
                        long minutosTrabalhado = (hora * 60) + minuto + (segundo / 60);

                        long hora1 = Long.valueOf(TotalDebitoString.substring(0, 3));
                        long minuto1 = Long.valueOf(TotalDebitoString.substring(4, 6));
                        long segundo1 = 0;
                        long minutosTrabalhado1 = (hora1 * 60) + minuto1 + (segundo1 / 60);

                        resultFinal = minutosTrabalhado - minutosTrabalhado1;

                        long minutos = resultFinal;
                        long inteira = minutos / 60;
                        long resto = minutos % 60;
                        StringSaldoFinal = String.format("%02d:%02d", inteira, resto);

                    } else {
                        long hora = Long.valueOf(TotalCreditoString.substring(0, 2));
                        long minuto = Long.valueOf(TotalCreditoString.substring(3, 5));
                        long segundo = 0;
                        long minutosTrabalhado = (hora * 60) + minuto + (segundo / 60);

                        long hora1 = Long.valueOf(TotalDebitoString.substring(0, 2));
                        long minuto1 = Long.valueOf(TotalDebitoString.substring(3, 5));
                        long segundo1 = 0;
                        long minutosTrabalhado1 = (hora1 * 60) + minuto1 + (segundo1 / 60);

                        resultFinal = minutosTrabalhado - minutosTrabalhado1;

                        long minutos = resultFinal;
                        long inteira = minutos / 60;
                        long resto = minutos % 60;
                        String restoString = String.valueOf(resto).replace("-", "");
                        String inteiraString = String.valueOf(inteira).replace("-", "");
                        int restoInt = Integer.parseInt(restoString);
                        int inteiroInt = Integer.parseInt(inteiraString);
                        if (inteira < 0) {
                            String s = String.format("%02d:%02d", inteiroInt, restoInt);
                            StringSaldoFinal = "-" + s;
                        } else {
                            StringSaldoFinal = String.format("%02d:%02d", inteiroInt, restoInt);
                            System.err.println("SALDO FINAL FORMATADO: " + StringSaldoFinal);
                        }
                    }

                }

                listaBancoHorasporPessoaFinal = bancohorasfacade.buscaBancoHorasExtrato(funcionarioSelecionado, periodo.getPeriodo());
                for (BancoHorasExtrato extratos : listaBancoHorasporPessoaFinal) {
                    extratos.setSaldoMes(StringSaldoFinal);
                    new GenericDAO<>(BancoHorasExtrato.class).atualiza(extratos);
                }
                saldoAnterior();
            } catch (NumberFormatException e) {
                System.err.println(e);
            }

        } else {
            Msg.addMsgWarn("Sem banco para esse periodo");
        }
    }

    public void saldoAnterior() {

        listaBancoHorasporPessoaAnteriorSaldo = bancohorasfacade.buscaBancoHorasExtrato(funcionarioSelecionado, periodo.getPeriodoAnterior());
        for (BancoHorasExtrato extratos : listaBancoHorasporPessoaAnteriorSaldo) {
            StringSaldoAnterior = this.retornaHoraCalculo(extratos.getSaldoAtual());
            System.err.println(extratos.getSaldoMes());
        }
        if (StringSaldoAnterior == null) {
            StringSaldoAnterior = "00:00";
        }

        if (StringSaldoAnterior.length() == 7) {

            String itemString = StringSaldoAnterior.replace("-", "");
            String itemString1 = StringSaldoFinal.replace("-", "");

            long hora = Long.valueOf(itemString.substring(0, 3));
            long minuto = Long.valueOf(itemString.substring(4, 6));
            long segundo = 0;
            long minutosTrabalhado = (hora * 60) + minuto + (segundo / 60);

            long hora1 = Long.valueOf(itemString1.substring(0, 2));
            long minuto1 = Long.valueOf(itemString1.substring(3, 5));
            long segundo1 = 0;
            long minutosTrabalhado1 = (hora1 * 60) + minuto1 + (segundo1 / 60);

            resultFinalSaldo = minutosTrabalhado + minutosTrabalhado1;

            if (StringSaldoAnterior.contains("-") || StringSaldoFinal.contains("-")) {
                long minutos = resultFinalSaldo;
                long inteira = minutos / 60;
                long resto = minutos % 60;
                StringSaldoAtual = "-" + String.format("%02d:%02d", inteira, resto);
            } else {
                long minutos = resultFinalSaldo;
                long inteira = minutos / 60;
                long resto = minutos % 60;
                StringSaldoAtual = String.format("%02d:%02d", inteira, resto);
            }

        } else if (StringSaldoFinal.length() == 8) {

            String itemString = StringSaldoAnterior.replace("-", "");
            String itemString1 = StringSaldoFinal.replace("-", "");

            long hora = Long.valueOf(itemString.substring(0, 2));
            long minuto = Long.valueOf(itemString.substring(3, 5));
            long segundo = 0;
            long minutosTrabalhado = (hora * 60) + minuto + (segundo / 60);

            long hora1 = Long.valueOf(itemString1.substring(0, 3));
            long minuto1 = Long.valueOf(itemString1.substring(4, 6));
            long segundo1 = 0;
            long minutosTrabalhado1 = (hora1 * 60) + minuto1 + (segundo1 / 60);

            resultFinalSaldo = minutosTrabalhado + minutosTrabalhado1;

            if (StringSaldoAnterior.contains("-") || StringSaldoFinal.contains("-")) {
                long minutos = resultFinalSaldo;
                long inteira = minutos / 60;
                long resto = minutos % 60;
                StringSaldoAtual = "-" + String.format("%02d:%02d", inteira, resto);
            } else {
                long minutos = resultFinalSaldo;
                long inteira = minutos / 60;
                long resto = minutos % 60;
                StringSaldoAtual = String.format("%02d:%02d", inteira, resto);
            }

        } else if (StringSaldoFinal.length() == 6) {
            String itemString = StringSaldoAnterior.replace("-", "");
            String itemString1 = StringSaldoFinal.replace("-", "");

            long hora = Long.valueOf(itemString.substring(0, 2));
            long minuto = Long.valueOf(itemString.substring(3, 5));
            long segundo = 0;
            long minutosTrabalhado = (hora * 60) + minuto + (segundo / 60);

            long hora1 = Long.valueOf(itemString1.substring(0, 2));
            long minuto1 = Long.valueOf(itemString1.substring(3, 5));
            long segundo1 = 0;
            long minutosTrabalhado1 = (hora1 * 60) + minuto1 + (segundo1 / 60);

            resultFinalSaldo = minutosTrabalhado + minutosTrabalhado1;

            if (StringSaldoAnterior.contains("-") || StringSaldoFinal.contains("-")) {
                long minutos = resultFinalSaldo;
                long inteira = minutos / 60;
                long resto = minutos % 60;
                StringSaldoAtual = "-" + String.format("%02d:%02d", inteira, resto);
            } else {
                long minutos = resultFinalSaldo;
                long inteira = minutos / 60;
                long resto = minutos % 60;
                StringSaldoAtual = String.format("%02d:%02d", inteira, resto);
            }

        } else {
            String itemString = StringSaldoAnterior.replaceAll("-", "");
            String itemString1 = StringSaldoFinal.replace("-", "");

            long hora = Long.valueOf(itemString.substring(0, 2));
            long minuto = Long.valueOf(itemString.substring(3, 5));
            long segundo = 0;
            long minutosTrabalhado = (hora * 60) + minuto + (segundo / 60);

            long hora1 = Long.valueOf(itemString1.substring(0, 2));
            long minuto1 = Long.valueOf(itemString1.substring(3, 5));
            long segundo1 = 0;
            long minutosTrabalhado1 = (hora1 * 60) + minuto1 + (segundo1 / 60);

            resultFinalSaldo = minutosTrabalhado + minutosTrabalhado1;

            if (StringSaldoAnterior.contains("-") || StringSaldoFinal.contains("-")) {
                long minutos = resultFinalSaldo;
                long inteira = minutos / 60;
                long resto = minutos % 60;
                StringSaldoAtual = "-" + String.format("%02d:%02d", inteira, resto);
            } else {
                long minutos = resultFinalSaldo;
                long inteira = minutos / 60;
                long resto = minutos % 60;
                StringSaldoAtual = String.format("%02d:%02d", inteira, resto);
            }
        }

        List<BancoHorasExtrato> retornoBancoSet = bancohorasfacade.buscaBancoHorasExtrato(funcionarioSelecionado, periodo.getPeriodo());
        for (BancoHorasExtrato extratos : retornoBancoSet) {
            extratos.setSaldoAtual(StringSaldoAtual);
            new GenericDAO<>(BancoHorasExtrato.class).atualiza(extratos);
        }

        List<EspelhoPonto> retornoEspelhoSet = espelhoPontoFacade.buscaHorasEspelho(funcionarioSelecionado.getPis(), periodo.getPeriodo());
        for (EspelhoPonto espelhoFaltasSet : retornoEspelhoSet) {
            espelhoFaltasSet.setTotalBancoHoras(StringSaldoAtual);
            new GenericDAO<>(EspelhoPonto.class).atualiza(espelhoFaltasSet);
        }

    }

    public String retornaHoraCalculo(String hora) {
        if (hora == null) {
            hora = "00:00";
        } else if ("".equals(hora)) {
            hora = "00:00";
            return hora;
        }
        return hora;

    }

    public void bancoExtrato() {

        try {

            List<BancoHorasExtrato> retornoExtratoBanco = bancohorasfacade.buscaBancoHoras(funcionarioSelecionado, periodo.getPeriodo());

            for (BancoHorasExtrato extrato : retornoExtratoBanco) {

                List<EspelhoPonto> retornoEspelho = bancohorasfacade.buscaExtratoByDate(funcionarioSelecionado.getPis(), extrato.getData_evento());
                for (EspelhoPonto objectEspelho : retornoEspelho) {

                    if (objectEspelho.getSaldo_negativo() != null) {

                        extrato.setSaldoNegativo(objectEspelho.getSaldo_negativo());
                        extrato.setTipo("Falta");
                        extrato.setAlterado(true);
                    } else {
                        extrato.setSaldoNegativo("-:-");
                        extrato.setAlterado(false);

                    }

                    if (objectEspelho.getSaldo_positivo() != null) {

                        extrato.setSaldoPositivo(objectEspelho.getSaldo_positivo());
                        extrato.setTipo("Extra");
                        extrato.setAlterado(true);
                    } else {
                        extrato.setSaldoPositivo("-:-");
                        extrato.setAlterado(false);

                    }
                    new GenericDAO<>(BancoHorasExtrato.class).atualiza(extrato);
                }

            }

        } catch (Exception e) {
            System.err.println("Erro: " + e);

        }
        try {
            extratoBancoFuncionario();
        } catch (ParseException ex) {
            Logger.getLogger(BancoHorasBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        carregado = true;
    }

    public String gravarBanco() {

        try {

            usuario = pegaUsuario();

            if (bancohoras.getId() == 0) {
                bancohoras.setUsuario(usuario);
                bancohorasfacade.adiciona(bancohoras);
                Msg.addMsgInfo("Cadastro realizado com sucesso! " + bancohoras.getDescricao());
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);
                return "/View/bancoHoras/gerencia-banco/lista-banco/index?faces-redirect=true";

            } else {

                bancohoras.setUsuario(usuario);
                bancohorasfacade.atualiza(bancohoras);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! " + bancohoras.getDescricao());
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);
                return "/View/bancoHoras/gerencia-banco/lista-banco/index?faces-redirect=true";
            }

        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada!" + e);
        }
        return null;

    }

    public void zerarBanco() {

        listaBancoHorasporPessoa = bancohorasfacade.buscaBancoHorasExtrato(funcionarioSelecionado, periodo.getPeriodo());
        for (BancoHorasExtrato extratos : listaBancoHorasporPessoa) {
            extratos.setSaldoAtual("00:00");
            extratos.setSaldoMes("00:00");
            extratos.setSaldoNegativo("-:-");
            extratos.setSaldoPositivo("-:-");
            new GenericDAO<>(BancoHorasExtrato.class).atualiza(extratos);
        }

    }

    public String excluir(BancoHoras bancoHoras) {
        try {
            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                bancohorasfacade.remove(bancoHoras);
                Msg.addMsgWarn("Afastamento Excluido:  " + bancoHoras.getDescricao());

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/bancoHoras/gerencia-banco/lista-banco/index?faces-redirect=true";

            } else {

                Msg.addMsgWarn("Somente administrador!");
                System.err.println("Somente administrador");
            }
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }

    public String editar(BancoHoras bancohoras) {
        this.bancohoras = bancohoras;
        return "/View/bancoHoras/gerencia-banco/cadastrar-banco/index";

    }

    public String novoBanco() {
        try {
            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                return "/View/bancoHoras/gerencia-banco/cadastrar-banco/index?faces-redirect=true";

            } else {

                Msg.addMsgFatal("Somente Administrador");
                System.err.println("Somente Administrador");
                return null;
            }
        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada " + e);
            System.err.println("ERRO: " + e);

        }

        return null;
    }

    public void abrirBancoHoras(BancoHoras banco) {

        try {

            usuario = pegaUsuario();

            Date data_inicio = banco.getData_criacao();
            Date data_fim = banco.getData_expiracao();

            Calendar c = Calendar.getInstance();
            Calendar c1 = Calendar.getInstance();
            Date data = new Date();

            c.setTime(data_inicio);
            c1.setTime(data);

            //Gerando as Datas
            Date dt = null;

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            Date dt1 = data_inicio;
            Date dt2 = data_fim;

            Calendar cal = Calendar.getInstance();
            cal.setTime(dt1);

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(dt1);
            for (dt = dt1; dt.compareTo(dt2) <= 0;) {
                if (dt.compareTo(dt2) <= 0) {
                    listaStringBanco.add(df.format(dt));
                }
                cal1.add(Calendar.DAY_OF_WEEK, +1);
                dt = cal1.getTime();

            }

            listaBanco = bancohorasfacade.buscadepartamentosBancoHoras(banco.getId());

            for (BancoHoras bancolista : listaBanco) {

                System.out.println(bancolista.getDescricao());

                for (Departamento itemDepartamento : bancolista.getDepartamento()) {

                    listapordepartamento = bancohorasfacade.buscafuncionariosbancohoras(itemDepartamento.getId());

                    for (Funcionario funcionariosretorno : listapordepartamento) {

                        for (String itens : listaStringBanco) {

                            BancoHorasExtrato horasBanco = new BancoHorasExtrato();
                            String[] dataArray = itens.split("/");
                            Date data1 = sdf.parse(dataArray[0] + dataArray[1] + dataArray[2]);

                            horasBanco.setData_evento(sdfh.parse(itens));
                            horasBanco.setFuncionario(funcionariosretorno);
                            horasBanco.setUsuario(usuario);
                            horasBanco.setPeriodo(periodoFormat.format(data1));
                            horasBanco.setSaldoNegativo("-:-");
                            horasBanco.setSaldoPositivo("-:-");
                            horasBanco.setSaldoMes("00:00");
                            horasBanco.setSaldoAtual("00:00");
                            banco.setProcessado(true);

                            new GenericDAO<>(BancoHorasExtrato.class
                            ).atualiza(horasBanco);
                        }

                    }

                }

            }
            bancohorasfacade.atualiza(banco);
        } catch (ParseException e) {
            System.err.println("Exceção" + e);
        }
        Msg.addMsgInfo("Banco Horas Aberto!");
    }

    public void lancaBancoHoras() {

        try {

            usuario = pegaUsuario();

            List<BancoHorasExtrato> retornoextrato = bancohorasfacade.buscaExtratoBanco(funcionarioSelecionado, data_Lancamento);
            for (BancoHorasExtrato itemExtrato : retornoextrato) {

                List<EspelhoPonto> retornoEspelho = bancohorasfacade.buscaExtratoByDate(funcionarioSelecionado.getPis(), data_Lancamento);
                for (EspelhoPonto objectEspelho : retornoEspelho) {

                    if (item.equals("credito")) {
                        itemExtrato.setSaldoPositivo(horaBanco);
                        itemExtrato.setData_evento(data_Lancamento);
                        itemExtrato.setAlterado(true);
                        itemExtrato.setTipo("Extra Manual");
                        itemExtrato.setObservacao(observacao);
                        itemExtrato.setUsuario(usuario);
                        itemExtrato.setUsuario(usuario);
                        objectEspelho.setSaldo_positivo(horaBanco);

                        extratofacade.atualiza(itemExtrato);
                        espelhoPontoFacade.atualiza(objectEspelho);

                    } else {
                        itemExtrato.setSaldoNegativo(horaBanco);
                        itemExtrato.setData_evento(data_Lancamento);
                        itemExtrato.setTipo("Débito Manual");
                        itemExtrato.setAlterado(true);
                        itemExtrato.setUsuario(usuario);
                        itemExtrato.setObservacao(observacao);
                        itemExtrato.setUsuario(usuario);
                        objectEspelho.setSaldo_negativo(horaBanco);

                        extratofacade.atualiza(itemExtrato);
                        espelhoPontoFacade.atualiza(objectEspelho);
                    }

                }
            }
        } catch (Exception e) {
            System.err.println("Erro..." + e);
        }
    }

    public String dataParaPeriodo(Date data) throws ParseException {
        if (data != null) {
            return periodoFormat.format(data);
        }
        return null;
    }

    public String listar() {
        return "/View/bancoHoras/extrato-banco/index?faces-redirect=true";
    }

    public Usuario pegaUsuario() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
            return usuarioLogado;
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
        }
        return null;

    }

//Gets and Seters    
    public List<Funcionario> getListapordepartamento() {

        return listapordepartamento;
    }

    public void setListapordepartamento(List<Funcionario> listapordepartamento) {
        this.listapordepartamento = listapordepartamento;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public FolhaPeriodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(FolhaPeriodo periodo) {
        this.periodo = periodo;
    }

    public boolean isCarregado() {
        return carregado;
    }

    public void setCarregado(boolean carregado) {
        this.carregado = carregado;
    }

    public Funcionario getFuncionarioSelecionado() {
        return funcionarioSelecionado;
    }

    public void setFuncionarioSelecionado(Funcionario funcionarioSelecionado) {
        this.funcionarioSelecionado = funcionarioSelecionado;
    }

    public BancoHoras getBancohoras() {
        return bancohoras;
    }

    public void setBancohoras(BancoHoras bancohoras) {
        this.bancohoras = bancohoras;
    }

    public List<Departamento> getListadepartamento() {
        listadepartamento = bancohorasfacade.listaDepartamento();
        return listadepartamento;
    }

    public void setListadepartamento(List<Departamento> listadepartamento) {
        this.listadepartamento = listadepartamento;
    }

    public List<BancoHoras> getListaBanco() {
        listaBanco = bancohorasfacade.listaBancoHoras();
        return listaBanco;
    }

    public void setListaBanco(List<BancoHoras> listaBanco) {
        this.listaBanco = listaBanco;
    }

    public List<BancoHorasExtrato> getListaBancoHorasporPessoa() {
        return listaBancoHorasporPessoa;
    }

    public void setListaBancoHorasporPessoa(List<BancoHorasExtrato> listaBancoHorasporPessoa) {
        this.listaBancoHorasporPessoa = listaBancoHorasporPessoa;
    }

    public String getStringSaldoFinal() {
        return StringSaldoFinal;
    }

    public void setStringSaldoFinal(String StringSaldoFinal) {
        this.StringSaldoFinal = StringSaldoFinal;
    }

    public BancoHorasExtrato getExtratoHorasBanco() {
        return extratoHorasBanco;
    }

    public void setExtratoHorasBanco(BancoHorasExtrato extratoHorasBanco) {
        this.extratoHorasBanco = extratoHorasBanco;
    }

    public String getHora() {
        return horaBanco;
    }

    public void setHora(String hora) {
        this.horaBanco = hora;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Date getData_Lancamento() {
        return data_Lancamento;
    }

    public void setData_Lancamento(Date data_Lancamento) {
        this.data_Lancamento = data_Lancamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getStringSaldoAnterior() {
        return StringSaldoAnterior;
    }

    public void setStringSaldoAnterior(String StringSaldoAnterior) {
        this.StringSaldoAnterior = StringSaldoAnterior;
    }

    public String getStringSaldoAtual() {
        return StringSaldoAtual;
    }

    public void setStringSaldoAtual(String StringSaldoAtual) {
        this.StringSaldoAtual = StringSaldoAtual;
    }

    public boolean isCarregadoBt() {
        return carregadoBt;
    }

    public void setCarregadoBt(boolean carregadoBt) {
        this.carregadoBt = carregadoBt;
    }

    public List<Funcionario> getFuncionarioPorDepartamento() {
        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                funcionarioPorDepartamento = bancohorasfacade.listaFuncionarioGeral();

            } else if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("gestor")) {

                List<Departamento> departamentoUsuario = usuario.getDepartamento();
                for (Departamento itemDepartamento : departamentoUsuario) {
                    List<Funcionario> retorno = bancohorasfacade.listaFuncionarioDepartamento(itemDepartamento.getId());
                    for (Funcionario retornoFuncionario : retorno) {
                        funcionarioPorDepartamento.add(retornoFuncionario);
                        System.err.println("Funcionários " + retornoFuncionario);
                    }
                }

            }
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

        return funcionarioPorDepartamento;
    }

    public void setFuncionarioPorDepartamento(List<Funcionario> funcionarioPorDepartamento) {
        this.funcionarioPorDepartamento = funcionarioPorDepartamento;
    }

}
