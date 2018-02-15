package br.com.Beans;

import br.com.Facade.BancoHorasFacade;
import br.com.Facade.EspelhoPontoFacade;
import br.com.Facade.FolhaPeriodoFacade;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.BancoHoras;
import br.com.Model.BancoHorasExtrato;
import br.com.Model.Departamento;
import br.com.Model.EspelhoPonto;
import br.com.Model.FolhaPeriodo;
import br.com.Model.Funcionario;
import br.com.Model.Justificativa;
import br.com.Model.PtoArquivo;
import br.com.Model.Usuario;
import br.com.utils.Autoriza.Autorizador;
import br.com.utils.Message.Msg;
import com.itextpdf.text.Image;
import static com.sun.faces.facelets.util.Path.context;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import org.primefaces.event.SelectEvent;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.hibernate.internal.SessionFactoryImpl;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ViewScoped
@ManagedBean
public class EspelhoPontoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    EntityManager manager = new JPAConect().getEntityManager();
    private final org.apache.log4j.Logger logger = Logger.getLogger(EspelhoPonto.class.getName());

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm");
    SimpleDateFormat sdfH1 = new SimpleDateFormat("HHmm");
    SimpleDateFormat periodoformat = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat periodoExportarFolha = new SimpleDateFormat("MMyyyy");

    String resultSetFaltas = "00:00";
    String StringSaldoFinal;
    long TotalCredito = 0;
    long TotalFaltasSoma = 0;
    StreamedContent stream;
    StreamedContent streamcolaborador;

    private boolean carregado;
    private boolean carregadoProcessar;
    private boolean carregaEditar = true;

    private DataModel<FolhaPeriodo> dataModel;
    List<EspelhoPonto> tabelaEspelho = new ArrayList<>();
    List<String> listaFoo = new ArrayList<>();
    List<String> ListaPeriodo = new ArrayList<>();
    List<String> ListaMoverBatidas = new ArrayList<>();
    List<Funcionario> listapordepartamento;
    DataModel<Funcionario> dataModeloFuncionarioSlecionado;
    List<Departamento> listaDepartamento;
    List<EspelhoPonto> espelhoPontos = new ArrayList<>();
    List<String> ListaFaltasSomar;
    List<Justificativa> listaJustificativas;
    List<FolhaPeriodo> listFolhaPerido;

    BancoHorasExtrato bancohorasExtrato;
    FolhaPeriodoFacade folhaPeriodoFacade;
    EspelhoPonto espelhoPonto;
    FolhaPeriodo folhaPeriodo;
    Usuario usuario;
    Departamento departamento;
    Funcionario funcionarioSelecionado;
    EspelhoPontoFacade espelhoPontoFacade;
    BancoHorasFacade bancohorasfacade;
    BancoHorasBean extratobean;
    Autorizador autorizador;

    public EspelhoPontoBean() {

        if (funcionarioSelecionado == null) {
            funcionarioSelecionado = new Funcionario();
        }

        if (folhaPeriodoFacade == null) {
            folhaPeriodoFacade = new FolhaPeriodoFacade();
        }

        if (espelhoPontoFacade == null) {
            espelhoPontoFacade = new EspelhoPontoFacade();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }

        if (bancohorasfacade == null) {
            bancohorasfacade = new BancoHorasFacade();
        }

        if (bancohorasExtrato == null) {
            bancohorasExtrato = new BancoHorasExtrato();
        }

        if (espelhoPonto == null) {
            espelhoPonto = new EspelhoPonto();
        }

        if (departamento == null) {
            departamento = new Departamento();
        }

        if (folhaPeriodo == null) {
            folhaPeriodo = new FolhaPeriodo();
        }

        if (ListaFaltasSomar == null) {
            ListaFaltasSomar = new ArrayList<>();
        }

        if (listaJustificativas == null) {
            listaJustificativas = new ArrayList<>();
        }

        if (extratobean == null) {
            extratobean = new BancoHorasBean();
        }

    }

    public List<Departamento> getDepartamentos() {
        return new GenericDAO<>(Departamento.class).listaTodos();
    }

    public List<FolhaPeriodo> getFolhaPeriodos() {
        return new GenericDAO<>(FolhaPeriodo.class).listaTodos();
    }

    public void carregaBatidas() {
        long contador = 0;
        tabelaEspelho = espelhoPontoFacade.buscaEspelhoPonto(funcionarioSelecionado.getPis(), folhaPeriodo.getPeriodo());

    }

    public void carregarFuncionarios() {

        if (this.departamento == null) {
            Msg.addMsgError("Selecione o Departamento");
            return;
        }
        try {
//            departamento = new Departamento();
            listapordepartamento = espelhoPontoFacade.buscadepartamento(departamento);
            carregado = true;
            carregadoProcessar = true;
            processarBatidas();
            atualizaStatusFolha();

        } catch (ParseException e) {
            Msg.addMsgError("Operação não realizada: " + e);
            System.err.println("ERRO: " + e);

        }
    }

    public void limparLista() {
        tabelaEspelho.clear();
        limparFaltas();
    }

    public void onRowSelect(SelectEvent event) {
        funcionarioSelecionado = (Funcionario) event.getObject();

    }

    public Funcionario onRowSelect1(SelectEvent event) {
        return funcionarioSelecionado = (Funcionario) event.getObject();

    }

    public void processarBatidas() throws ParseException {
        List<String> listaBatidas = new ArrayList<>();

        for (Funcionario funcionarioLista : listapordepartamento) {
            List<EspelhoPonto> retornoEspelho = espelhoPontoFacade.buscaHorasEspelho(funcionarioLista.getPis(), folhaPeriodo.getPeriodo());

            for (EspelhoPonto espelho1 : retornoEspelho) {
                List<PtoArquivo> arquivos = espelhoPontoFacade.buscaBatidasByDate(funcionarioLista.getPis(), sdf2.parse(espelho1.getData()));

                for (PtoArquivo arquivo : arquivos) {

                    listaBatidas.add(sdfH.format(arquivo.getHora()));
                    System.out.println(arquivo.getPis() + arquivo.getHora());
                }
                try {
                    Collections.sort(listaBatidas);

                    String entrada01 = this.retornaHoraBatida(listaBatidas, 0, espelho1.getData(), funcionarioLista, espelho1.getObservacao());
                    String saida01 = this.retornaHoraBatida(listaBatidas, 1, espelho1.getData(), funcionarioLista, espelho1.getObservacao());
                    String entrada02 = this.retornaHoraBatida(listaBatidas, 2, espelho1.getData(), funcionarioLista, espelho1.getObservacao());
                    String saida02 = this.retornaHoraBatida(listaBatidas, 3, espelho1.getData(), funcionarioLista, espelho1.getObservacao());

                    espelho1.setFuncionario(funcionarioLista);
                    espelho1.setNome_Funcionario(funcionarioLista.getNome());
                    espelho1.setEntrada01(entrada01);
                    espelho1.setEntrada01_antiga(entrada01);

                    espelho1.setSaida01(saida01);
                    espelho1.setSaida01_antiga(saida01);

                    espelho1.setEntrada02(entrada02);
                    espelho1.setEntrada02_antiga(entrada02);

                    espelho1.setSaida02(saida02);
                    espelho1.setSaida02_antiga(saida02);

                    espelho1.setPis(funcionarioLista.getPis());

                    new GenericDAO<>(EspelhoPonto.class).atualiza(espelho1);

                } catch (Exception e) {
//                    System.err.println("Erro: " + e);
//                    Msg.addMsgWarn("#### ERRO AO PROCESSAR BATIDAS ###");
                }

                listaBatidas.clear();

            }
        }

    }

    public String retornaHoraBatida(List<String> batidas, int posicao, String data, Funcionario funcionario, String obs) {
        String hora = null;
        try {
            hora = batidas.get(posicao);
        } catch (IndexOutOfBoundsException e) {

            if (funcionario.getHorario().getHora_EntradaT2_util() == null) {
                hora = "";

                if (data.contains("Dom") | data.contains("Sáb")) {
                    hora = "Folga";

                }

            } else if (data.contains("Dom") | data.contains("Sáb")) {

                hora = "Folga";

            } else if (obs != null) {

                hora = batidas.get(0);
                hora = batidas.get(1);
                hora = batidas.get(2);
                hora = batidas.get(3);

            } else {

                hora = "Falta";
            }

        }
        return hora;
    }

    public void onRowEdit(RowEditEvent event) {

        if (!verificaStatusFolhaEspelho()) {
            try {
                EspelhoPonto espelhoss = (EspelhoPonto) event.getObject();

                espelhoss.setAlterado(true);
                espelhoss.setAbonar(true);

                if (espelhoss.getEntrada02() == null ? espelhoss.getEntrada02_antiga() != null : !espelhoss.getEntrada02().equals(espelhoss.getEntrada02_antiga())) {
                    espelhoss.setEntrada02(espelhoss.getEntrada02() + "***");
                    new GenericDAO<>(EspelhoPonto.class).atualiza(espelhoss);

                } else {
                    espelhoss.setEntrada02(espelhoss.getEntrada02());
                }

                if (espelhoss.getEntrada01() == null ? espelhoss.getEntrada01_antiga() != null : !espelhoss.getEntrada01().equals(espelhoss.getEntrada01_antiga())) {
                    espelhoss.setEntrada01(espelhoss.getEntrada01() + "***");
                    new GenericDAO<>(EspelhoPonto.class).atualiza(espelhoss);
                } else {
                    espelhoss.setEntrada01(espelhoss.getEntrada01());
                }

                if (espelhoss.getSaida01() == null ? espelhoss.getSaida01_antiga() != null : !espelhoss.getSaida01().equals(espelhoss.getSaida01_antiga())) {
                    espelhoss.setSaida01(espelhoss.getSaida01() + "***");
                    new GenericDAO<>(EspelhoPonto.class).atualiza(espelhoss);
                } else {
                    espelhoss.setSaida01(espelhoss.getSaida01());
                }

                if (espelhoss.getSaida02() == null ? espelhoss.getSaida02_antiga() != null : !espelhoss.getSaida02().equals(espelhoss.getSaida02_antiga())) {
                    espelhoss.setSaida02(espelhoss.getSaida02() + "***");
                    new GenericDAO<>(EspelhoPonto.class).atualiza(espelhoss);
                } else {
                    espelhoss.setSaida02(espelhoss.getSaida02());
                }

                Msg.addMsgInfo("Alterado com sucesso");
            } catch (Exception e) {
                System.out.println("Erro..." + e);
            }
        } else {
            Msg.addMsgWarn("Folha Mensal Fechada!");
        }

    }

    public void onRowCancel(RowEditEvent event) {
        try {
            Msg.addMsgInfo("Cancelado...");
            event = null;
        } catch (Exception e) {
            System.err.println("Erro: " + e);
            Msg.addMsgWarn("Erro: " + e);
        }
    }

    public void MoverBatidas(EspelhoPonto espelho) {

        this.espelhoPonto = espelho;

        try {
            ListaMoverBatidas.clear();
            if (ListaMoverBatidas.size() <= 0) {
                ListaMoverBatidas.add(espelho.getEntrada01());
                ListaMoverBatidas.add(espelho.getSaida01());
                ListaMoverBatidas.add(espelho.getEntrada02());
                ListaMoverBatidas.add(espelho.getSaida02());
            } else {

            }
        } catch (Exception e) {
            System.out.println("Excessão" + e);
        }
    }

    public void onRowReorder(ReorderEvent event) {

        if (!verificaStatusFolhaEspelho()) {
            int fromIndex = 0;
            int toIndex = 0;

            try {
                fromIndex = event.getFromIndex();
                toIndex = event.getToIndex();

                if (toIndex != fromIndex) {

                    Collections.rotate(ListaMoverBatidas.subList(fromIndex, toIndex + 1), -1);

                    espelhoPonto.setEntrada01(ListaMoverBatidas.get(0));
                    espelhoPonto.setSaida01(ListaMoverBatidas.get(1));
                    espelhoPonto.setEntrada02(ListaMoverBatidas.get(2));
                    espelhoPonto.setSaida02(ListaMoverBatidas.get(3));

                    new GenericDAO<>(EspelhoPonto.class).atualiza(espelhoPonto);
                }
                Msg.addMsgInfo("Alterado com sucesso");

            } catch (Exception e) {
                System.out.println("Erro..." + e);
            }
        } else {
            Msg.addMsgWarn("Folha Mensal Fechada!");
        }

    }

    public String recalcular() {
        tabelaEspelho.clear();

        List<EspelhoPonto> retornoEspelho = espelhoPontoFacade.buscaHorasEspelho(funcionarioSelecionado.getPis(), folhaPeriodo.getPeriodo());

        for (EspelhoPonto espelhoRecalcular : retornoEspelho) {
            try {

                String batidaEntrada01 = espelhoRecalcular.getEntrada01().replace("*", "");
                String batidaSaida01 = espelhoRecalcular.getSaida01().replace("*", "");
                String batidaEntrada02 = espelhoRecalcular.getEntrada02().replace("*", "");
                String batidaSaida02 = espelhoRecalcular.getSaida02().replace("*", "");

                Date td1 = sdfH.parse(this.retornaHoraCalculo(batidaEntrada01).substring(0, 5));
                Date td2 = sdfH.parse(this.retornaHoraCalculo(batidaSaida01).substring(0, 5));
                Date td3 = sdfH.parse(this.retornaHoraCalculo(batidaEntrada02).substring(0, 5));
                Date td4 = sdfH.parse(this.retornaHoraCalculo(batidaSaida02).substring(0, 5));

                String td1S = sdfH.format(td1);
                String td2S = sdfH.format(td2);
                String td3S = sdfH.format(td3);
                String td4S = sdfH.format(td4);

                String[] cargaString = funcionarioSelecionado.getHorario().getCarga().split(":");
                long carga = Long.parseLong(cargaString[0]);

                long CalculoLongManha = td2.getTime() - td1.getTime();
                long CalculoTardeLong = td4.getTime() - td3.getTime();

                String ManhaStringCalculo = String.format("%02d:%02d", CalculoLongManha / 3600000, (CalculoLongManha / 60000) % 60);
                String TardeStringCalculo = String.format("%02d:%02d", CalculoTardeLong / 3600000, (CalculoTardeLong / 60000) % 60);

                Date dataManha = sdfH.parse(ManhaStringCalculo.replace("-", ""));
                Date dataTarde = sdfH.parse(TardeStringCalculo.replace("-", ""));

                String[] batidaSaida0222 = td4S.split(":");
                long saidaLongComercial = Long.parseLong(batidaSaida0222[0] + batidaSaida0222[1]);

                GregorianCalendar gc = new GregorianCalendar();

// Inicio calculo FALTAS =========================================================================================================
                if (carga == 8) {
                    sdfH.setLenient(false);
                    String CargaNormal = "08:00";
                    Date hora444 = sdfH.parse(CargaNormal);

                    if (td2S.contains("18")) {
                        espelhoRecalcular.setFaltas(null);
                    } else if (td4S.equals("00:00")) {
                        long FaltaFinal = hora444.getTime() - dataManha.getTime();
                        String StringFaltaFinal = String.format("%02d:%02d", FaltaFinal / 3600000, (FaltaFinal / 60000) % 60);
                        espelhoRecalcular.setFaltas(StringFaltaFinal);
                        espelhoRecalcular.setSaldo_negativo(StringFaltaFinal);
                    } else {

                        String v11 = sdfH.format(dataTarde);
                        String v222 = sdfH.format(dataManha);

                        int hora11 = Integer.parseInt(v11.substring(0, 2));
                        int min1 = Integer.parseInt(v11.substring(3, 5));
                        int seg1 = 0;

                        Time time1 = new Time(hora11, min1, seg1);
                        gc.setTimeInMillis(time1.getTime());

                        hora11 = Integer.parseInt(v222.substring(0, 2));
                        min1 = Integer.parseInt(v222.substring(3, 5));

                        gc.add(Calendar.HOUR, hora11);
                        gc.add(Calendar.MINUTE, min1);
                        String resultSetFalta = sdfH.format(gc.getTime());

                        Date hora333 = sdfH.parse(resultSetFalta);

//
                        long FaltaFinal = (hora444.getTime() - hora333.getTime());

                        if (FaltaFinal <= 0 || espelhoRecalcular.isAbonar() == true) {
                            espelhoRecalcular.setFaltas(null);
                            espelhoRecalcular.setSaldo_negativo(null);

                            espelhoRecalcular.setFaltas(null);
                            espelhoRecalcular.setSaldo_negativo(null);
                            espelhoRecalcular.setTotalNormais(funcionarioSelecionado.getHorario().getCarga());

                        } else {
                            String StringFaltaFinal = String.format("%02d:%02d", FaltaFinal / 3600000, (FaltaFinal / 60000) % 60);

                            espelhoRecalcular.setFaltas(StringFaltaFinal);
                            espelhoRecalcular.setSaldo_negativo(StringFaltaFinal);

                        }

                    }
//Inicio Calculo Normais

                    String v = sdfH.format(dataTarde);
                    String v1 = sdfH.format(dataManha);

                    int hora = Integer.parseInt(v.substring(0, 2));
                    int min = Integer.parseInt(v.substring(3, 5));

                    int seg = 0;
                    Time time = new Time(hora, min, seg);
                    gc.setTimeInMillis(time.getTime());

                    hora = Integer.parseInt(v1.substring(0, 2));
                    min = Integer.parseInt(v1.substring(3, 5));

                    gc.add(Calendar.HOUR, hora);
                    gc.add(Calendar.MINUTE, min);
                    String resultSetRecalcular = sdfH.format(gc.getTime());

                    if (CalculoLongManha < 0) {
                        espelhoRecalcular.setTrabalhadas(null);
                    } else {
                        espelhoRecalcular.setTrabalhadas(resultSetRecalcular);
                    }

// Fim Calculo Normais 
// nicio Calculo Extras
                    long saidaextraComercial = Long.parseLong(funcionarioSelecionado.getHorario().getToleranciaGeral());
                    String toleranciaExtra = td4S + "15";

                    if (saidaLongComercial <= saidaextraComercial) {
                        espelhoRecalcular.setExtras("");
                        espelhoRecalcular.setSaldo_positivo("");
                        System.out.println("Não Tem Hora Extra......");

                    } else {

                        long h = Long.valueOf(td4S.substring(0, 2));
                        long m = Long.valueOf(td4S.substring(3, 5));
                        long s = 0;
                        long minutosTrabalhado = (h * 60) + m + (s / 60);
//
                        System.err.println("HORA: " + funcionarioSelecionado.getHorario().getToleranciaGeral().substring(0, 2));
                        System.err.println("MINUTO: " + funcionarioSelecionado.getHorario().getToleranciaGeral().substring(2, 4));

                        long hora1 = Long.valueOf(funcionarioSelecionado.getHorario().getToleranciaGeral().substring(0, 2));
                        long minuto1 = Long.valueOf(funcionarioSelecionado.getHorario().getToleranciaGeral().substring(2, 4));
                        long segundo1 = 0;
                        long minutosTrabalhado1 = (hora1 * 60) + minuto1 + (segundo1 / 60);

                        Long resultFinalExtra = minutosTrabalhado - minutosTrabalhado1;
////
//
                        long minutos = resultFinalExtra;
                        long inteira = minutos / 60;
                        long resto = minutos % 60;
                        String extra = String.format("%02d:%02d", inteira, resto);

                        espelhoRecalcular.setExtras(extra);
                        espelhoRecalcular.setSaldo_positivo(extra);
                    }
//Fim Calculos Extras
                } else {

                    String CargaNormal = funcionarioSelecionado.getHorario().getCarga();

                    String manha1 = String.format("%02d:%02d", CalculoLongManha / 3600000, (CalculoLongManha / 60000) % 60);

                    Date hora333 = sdfH.parse(manha1);
                    Date hora444 = sdfH.parse(CargaNormal);

                    long FaltaFinal = hora444.getTime() - hora333.getTime();

                    if (FaltaFinal <= 0) {
                        espelhoRecalcular.setFaltas(null);
                        espelhoRecalcular.setSaldo_negativo(null);
                    } else {
                        String StringFaltaFinal = String.format("%02d:%02d", FaltaFinal / 3600000, (FaltaFinal / 60000) % 60);
                        espelhoRecalcular.setFaltas(StringFaltaFinal);
                        espelhoRecalcular.setSaldo_negativo(StringFaltaFinal);

//                               
                    }

//Inicio Calculo Normais Horario Corrido
                    long intervalo = td2.getTime() - td1.getTime();

                    if (intervalo < 0) {
                        espelhoRecalcular.setTrabalhadas(null);

                    } else {
                        String manha = String.format("%02d:%02d", intervalo / 3600000, (intervalo / 60000) % 60);
                        espelhoRecalcular.setTrabalhadas(manha);
                    }
//Fim Calculo Normais
//Inicio Calculo Extras
//
//                    String[] ArrayStringBatida02Corrido = td2S.split(":");
                    long saidaLongCorrido = Long.valueOf(td2S.replaceAll(":", "")
                            .replace("*", ""));

                    long saidaextraCorrido = Long.valueOf(funcionarioSelecionado.getHorario().getToleranciaGeral());
                    if (saidaLongCorrido <= saidaextraCorrido) {
                        System.out.println("Não Tem Hora Extra......");
                        espelhoRecalcular.setExtras("");
                        espelhoRecalcular.setSaldo_positivo("");

                    } else {

                        long somaExtrasCorrido = saidaLongCorrido - saidaextraCorrido;

                        String convertFinalExtra = String.format("%04d", somaExtrasCorrido);
                        String resultSetExtra = convertFinalExtra.substring(0, 2) + ":" + convertFinalExtra.substring(2, 4);
                        espelhoRecalcular.setExtras(resultSetExtra);
                        espelhoRecalcular.setSaldo_positivo(resultSetExtra);
                    }
//Fim Calculo Extras
                }

//                    
                new GenericDAO<>(EspelhoPonto.class).atualiza(espelhoRecalcular);

            } catch (NumberFormatException | ParseException e) {
                System.out.println("ERRO" + e);

            }
        }

//        }
        somarFaltas();
        carregaBatidas();
        return "/View/espelhoponto/espelhoPonto?faces-redirect=true";

    }

    public void somarFaltas() {
        limparFaltas();
        try {
            List<EspelhoPonto> retornoEspelho = espelhoPontoFacade.buscaHorasEspelho(funcionarioSelecionado.getPis(), folhaPeriodo.getPeriodo());
            for (EspelhoPonto espelhoFaltas : retornoEspelho) {
                if (espelhoFaltas.getFaltas() == null) {
                    ListaFaltasSomar.add("00:00");
                } else {
                    ListaFaltasSomar.add(espelhoFaltas.getFaltas());
                }
            }

            //Somar Todas as Faltas    
            for (String faltas : ListaFaltasSomar) {

                System.err.println("FALTAS: " + faltas);
                long hora = Long.valueOf(faltas.substring(0, 2));
                long minuto = Long.valueOf(faltas.substring(3, 5));
                long segundo = 0;
                long minutosTrabalhado = (hora * 60) + minuto + (segundo / 60);

                long soma = TotalFaltasSoma;
                long soma01 = minutosTrabalhado;
                TotalFaltasSoma = soma01 + soma;
//

                long minutos = TotalFaltasSoma;
                long inteira = minutos / 60;
                long resto = minutos % 60;
//            resultSetDebito = inteira + resto;
                resultSetFaltas = String.format("%02d:%02d", inteira, resto);

            }

            List<EspelhoPonto> retornoEspelhoSet = espelhoPontoFacade.buscaHorasEspelho(funcionarioSelecionado.getPis(), folhaPeriodo.getPeriodo());
            for (EspelhoPonto espelhoFaltasSet : retornoEspelhoSet) {
                espelhoFaltasSet.setTotalFaltas(resultSetFaltas);
                new GenericDAO<>(EspelhoPonto.class).atualiza(espelhoFaltasSet);
            }

            retornoEspelhoSet.clear();
            ListaFaltasSomar.clear();

        } catch (NumberFormatException e) {
            Msg.addMsgWarn("Erro Soma Faltas: " + e);
        }
    }

    public void limparFaltas() {
        resultSetFaltas = "00:00";
        TotalFaltasSoma = 0;
        ListaFaltasSomar.clear();
    }

    public String retornaHoraCalculo(String data) {

        List<Justificativa> listaJustificativa = espelhoPontoFacade.buscaJustificativas();
        for (Justificativa justificativa : listaJustificativa) {

            if (data.equals(justificativa.getDescricao())) {
                data = "08:00:00";
            } else if (data.equals("")) {
                data = "00:00:00";
            } else if (data.equals("-")) {
                data = "00:00:00";
            }
        }

        return data;
    }

    public String listar() {
        return "/View/espelhoPonto/espelhoPonto?faces-redirect=true";
    }

    public void relatorioFrequencia() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Crude", "postgres", "root");

            Map parametros = new HashedMap();
            FacesContext context1 = FacesContext.getCurrentInstance();

            ServletContext servletContext = (ServletContext) context1.getExternalContext().getContext();
            String caminhoRelatorio = servletContext.getRealPath("/resources/reports/frequenciaPessoa.jasper");
            String caminhoimagem = servletContext.getRealPath("/resources/reports");

            parametros.put("reportPath", caminhoimagem);
            parametros.put("gPeriodoId", folhaPeriodo.getId());
            parametros.put("gId", funcionarioSelecionado.getId());

            HttpServletResponse response = (HttpServletResponse) context1.getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.addHeader("Content-disposition", "inline; filename=FolhaPonto.pdf");
            JasperPrint impressao = JasperFillManager.fillReport(caminhoRelatorio, parametros, conn);

            JasperExportManager.exportReportToPdfStream(impressao, response.getOutputStream());
            context1.getApplication().getStateManager().saveView(context1);
            context1.renderResponse();
            context1.responseComplete();

        } catch (IOException | SQLException | JRException e) {
            System.err.println("Erro: " + e);
            logger.info("ERRO RELATÓRIO: " + e);
        }

    }

    public void relatorioFrequenciaGeral() throws JRException {

        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Crude", "postgres", "root");

            Map parametros = new HashedMap();
            FacesContext context1 = FacesContext.getCurrentInstance();

            ServletContext servletContext = (ServletContext) context1.getExternalContext().getContext();
            String caminhoRelatorio = servletContext.getRealPath("/resources/reports/frequenciaPessoaDepartamento.jasper");
            String caminhoimagem = servletContext.getRealPath("/resources/reports/");

            parametros.put("reportPath", caminhoimagem);
            parametros.put("gPeriodoId", folhaPeriodo.getId());
            parametros.put("gIdDepartamento", departamento.getId());

            HttpServletResponse response = (HttpServletResponse) context1.getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.addHeader("Content-disposition", "attachment; filename=FolhaPonto - "+departamento.getDescricao()+".pdf");

//            
//            JasperPrint impressao = JasperFillManager.fillReport(caminhoRelatorio, parametros, conn);
            byte[] bPDF = JasperRunManager.runReportToPdf(caminhoRelatorio, parametros, conn);
//
//            JasperExportManager.exportReportToPdfStream(impressao, response.getOutputStream());
//            context1.getApplication().getStateManager().saveView(context1);
//            context1.renderResponse();
//            context1.responseComplete();
            try {

                response.setContentLength(bPDF.length);
                ServletOutputStream output = response.getOutputStream();
                output.write(bPDF, 0, bPDF.length);
                response.flushBuffer();
                response.getOutputStream().flush();
                response.getOutputStream().close();
                context1.responseComplete();

            } catch (IOException e) {

            }

        } catch (SQLException e) {
            System.err.println("Erro: " + e);
            logger.info("ERRO RELATÓRIO: " + e);
        }

    }

    public void exportarFaltas() throws ParseException {

        Date data = periodoExportarFolha.parse(folhaPeriodo.getPeriodo());
        Date dataAtual = new Date();
        List<String> lista = new ArrayList<>();
        String funcionarioRetorno = "";
        int matricula = 0;
        int faltas = 0;

        try {
            List<Funcionario> espelhoFaltas = espelhoPontoFacade.buscadepartamento(departamento);
            for (Funcionario funcionario : espelhoFaltas) {
                funcionarioRetorno = funcionario.getPis();

                List<EspelhoPonto> horasFaltasespelho = espelhoPontoFacade.buscaHorasEspelho(funcionarioRetorno, folhaPeriodo.getPeriodo());

                for (EspelhoPonto espelho : horasFaltasespelho) {
                    matricula = (int) espelho.getMatricula();

                    if (espelho.getTotalFaltas() != null) {
                        faltas = Integer.parseInt(espelho.getTotalFaltas().replace(":", ""));
                    } else {
                        System.err.println("Sem Falta: " + matricula);
                    }

                }

                //Empresa
                lista.add("001");
                //Matricula
                String matriculaFormat = String.format("%010d", matricula);
                lista.add(matriculaFormat);
                //Periodo
                lista.add(periodoExportarFolha.format(data).substring(0, 4));
                //Evento Falta
                lista.add("592");
                //Falta Horas e Minutos
                String faltasformat = String.format("%08d", faltas);
                lista.add(faltasformat);
                //Data
                lista.add(sdf2.format(dataAtual));
                //Evento Horas
                lista.add("H" + "\r\n");
            }

            FileWriter arquivo;
            String tempFolder = System.getProperty("java.io.tmpdir");
            tempFolder = tempFolder.substring(tempFolder.length() - 1, tempFolder.length() - 1).equals(File.separator) ? tempFolder
                    : tempFolder + File.separator;
            File file = new File(tempFolder + "faltas_folha.txt");

            arquivo = new FileWriter(file);

            int size = lista.size() - 1;
            for (String item : lista) {
                int indexOf = lista.indexOf(item);
                if (indexOf == size) {
                    arquivo.write(item);
                } else {
                    arquivo.write(item + "\n");
                }

            }

            arquivo.close();

            InputStream inputStream = new FileInputStream(file);
            stream = new DefaultStreamedContent(inputStream, "plain/txt", departamento.getDescricao() + ".txt");
            lista.clear();

        } catch (IOException e) {
            e.printStackTrace();
            Msg.addMsgWarn("Operação não realizada!");
        }
    }

    public boolean verificaStatusFolhaEspelho() {

        try {
            folhaPeriodo = espelhoPontoFacade.buscaStatusFolha("ABERTO", folhaPeriodo.getId());
            System.err.println("FOLHA RETORNA: " + folhaPeriodo.getId());

            if (folhaPeriodo == null) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.err.println("ERRO: " + e);
        }

        return true;
    }

    private void atualizaStatusFolha() {
        try {
            if (!verificaStatusFolhaEspelho()) {
                carregaEditar = true;
            } else {
                carregaEditar = false;
            }
        } catch (Exception e) {
            System.out.println("ERRO " + e);
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

    public void exportarColaboradores() throws ParseException {

        List<String> lista = new ArrayList<>();

        try {
            List<Funcionario> espelhoFaltas = espelhoPontoFacade.buscadepartamento(departamento);
            for (Funcionario funcionario : espelhoFaltas) {
                //Cabeçalho
                lista.add("1+1+I[");
                // pis
                lista.add(funcionario.getPis());
                //caracter
                lista.add("[");
                //nome
                lista.add(funcionario.getNome());
                //caracter
                lista.add("[0[0[");
                //Matricula
                String matriculaFormat = String.format("%d", funcionario.getMatricula());
                lista.add(matriculaFormat + "\r\n");

            }

            FileWriter arquivo;
            String tempFolder = System.getProperty("java.io.tmpdir");
            tempFolder = tempFolder.substring(tempFolder.length() - 1, tempFolder.length() - 1).equals(File.separator) ? tempFolder
                    : tempFolder + File.separator;
            File file = new File(tempFolder + "faltas_folha.txt");

            arquivo = new FileWriter(file);

            int size = lista.size() - 1;
            for (String item : lista) {
                int indexOf = lista.indexOf(item);
                if (indexOf == size) {
                    arquivo.write(item);
                } else {
                    arquivo.write(item + "\n");
                }

            }

            arquivo.close();

            InputStream inputStream = new FileInputStream(file);
            streamcolaborador = new DefaultStreamedContent(inputStream, "plain/txt", "rep_colaborador" + ".txt");
            lista.clear();

        } catch (IOException e) {
            e.printStackTrace();
            Msg.addMsgWarn("Operação não realizada!");
        }
    }

//Gets and Setrs
    public boolean isCarregadoProcessar() {
        return carregadoProcessar;
    }

    public void setCarregadoProcessar(boolean carregadoProcessar) {
        this.carregadoProcessar = carregadoProcessar;
    }

    public List<Funcionario> getListapordepartamento() {
        return listapordepartamento;
    }

    public void setListapordepartamento(List<Funcionario> listapordepartamento) {
        this.listapordepartamento = listapordepartamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Funcionario getFuncionarioSelecionado() {
        return funcionarioSelecionado;
    }

    public void setFuncionarioSelecionado(Funcionario funcionarioSelecionado) {
        this.funcionarioSelecionado = funcionarioSelecionado;
    }

    public EspelhoPonto getEspelho() {
        return espelhoPonto;
    }

    public void setEspelho(EspelhoPonto espelho) {
        this.espelhoPonto = espelho;
    }

    public List<String> getListaFoo() {
        return listaFoo;
    }

    public void setListaFoo(List<String> listaFoo) {
        this.listaFoo = listaFoo;
    }

    public EntityManager getManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    public boolean isCarregado() {
        return carregado;
    }

    public void setCarregado(boolean carregado) {
        this.carregado = carregado;
    }

    public List<String> getListaPeriodo() {
        return ListaPeriodo;
    }

    public void setListaPeriodo(List<String> ListaPeriodo) {
        this.ListaPeriodo = ListaPeriodo;
    }

    public void setEspelhoPontos(List<EspelhoPonto> espelhoPontos) {
        this.espelhoPontos = espelhoPontos;
    }

    public void setDataModel(DataModel<FolhaPeriodo> dataModel) {
        this.dataModel = dataModel;
    }

    public EspelhoPontoFacade getEspelhofacade() {
        return espelhoPontoFacade;
    }

    public void setEspelhofacade(EspelhoPontoFacade espelhofacade) {
        this.espelhoPontoFacade = espelhofacade;
    }

    public FolhaPeriodoFacade getFolhaPeriodoFacade() {
        return folhaPeriodoFacade;
    }

    public DataModel<Funcionario> getDataModeloFuncionarioSlecionado() {
        getListapordepartamento();
        dataModeloFuncionarioSlecionado = new ListDataModel<>(listapordepartamento);
        return dataModeloFuncionarioSlecionado;
    }

    public void setDataModeloFuncionarioSlecionado(DataModel<Funcionario> dataModeloFuncionarioSlecionado) {
        this.dataModeloFuncionarioSlecionado = dataModeloFuncionarioSlecionado;
    }

    public List<EspelhoPonto> getTabelaEspelho() {
        return tabelaEspelho;
    }

    public void setTabelaEspelho(List<EspelhoPonto> tabelaEspelho) {
        this.tabelaEspelho = tabelaEspelho;
    }

    public FolhaPeriodo getPeriodo() {
        return folhaPeriodo;
    }

    public void setPeriodo(FolhaPeriodo periodo) {
        this.folhaPeriodo = periodo;
    }

    public List<String> getListaMoverBatidas() {
        return ListaMoverBatidas;
    }

    public void setListaMoverBatidas(List<String> ListaMoverBatidas) {
        this.ListaMoverBatidas = ListaMoverBatidas;
    }

    public boolean isCarregaEditar() {
        return carregaEditar;
    }

    public void setCarregaEditar(boolean carregaEditar) {
        this.carregaEditar = carregaEditar;
    }

    public String getResultSetFaltas() {
        return resultSetFaltas;
    }

    public void setResultSetFaltas(String resultSetFaltas) {
        this.resultSetFaltas = resultSetFaltas;
    }

    public String getStringSaldoFinal() {
        return StringSaldoFinal;
    }

    public void setStringSaldoFinal(String StringSaldoFinal) {
        this.StringSaldoFinal = StringSaldoFinal;
    }

    public List<Departamento> getListaDepartamento() {
        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                listaDepartamento = getDepartamentos();
            } else if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("gestor")) {
                listaDepartamento = usuario.getDepartamento();
            } else {
                listaDepartamento = null;
            }
        } catch (Exception e) {
        }

        return listaDepartamento;
    }

    public void setListaDepartamento(List<Departamento> listaDepartamento) {
        this.listaDepartamento = listaDepartamento;
    }

    public List<Justificativa> getListaJustificativas() {
        listaJustificativas = espelhoPontoFacade.buscaJustificativas();
        return listaJustificativas;
    }

    public void setListaJustificativas(List<Justificativa> listaJustificativas) {
        this.listaJustificativas = listaJustificativas;
    }

    public StreamedContent getStream() {
        return stream;
    }

    public void setStream(StreamedContent stream) {
        this.stream = stream;
    }

    public List<FolhaPeriodo> getListFolhaPerido() {
        return listFolhaPerido;
    }

    public void setListFolhaPerido(List<FolhaPeriodo> listFolhaPerido) {
        this.listFolhaPerido = listFolhaPerido;
    }

    public StreamedContent getStreamcolaborador() {
        return streamcolaborador;
    }

    public void setStreamcolaborador(StreamedContent streamcolaborador) {
        this.streamcolaborador = streamcolaborador;
    }

}
