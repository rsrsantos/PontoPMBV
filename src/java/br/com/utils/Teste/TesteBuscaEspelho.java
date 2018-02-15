package br.com.utils.Teste;

import br.com.Facade.EspelhoPontoFacade;
import br.com.Facade.FolhaPeriodoFacade;
import br.com.DAO.GenericDAO;
import br.com.Model.Departamento;
import br.com.Model.EspelhoPonto;
import br.com.Model.FolhaPeriodo;
import br.com.Model.Funcionario;
import br.com.Model.PtoArquivo;
import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TesteBuscaEspelho {

    public static void main(String[] args) throws ParseException {
        EspelhoPontoFacade af = new EspelhoPontoFacade();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat sdfh = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
        SimpleDateFormat periodo1 = new SimpleDateFormat("yyyyMM");
        String PeriodoBatida = "0";
        final List<String> listaPis = new ArrayList<>();
        final List<String> listaFoo = new ArrayList<>();
        List<String> ListaPeriodo = new ArrayList<>();
        FolhaPeriodoFacade folhaPeriodoFacade = new FolhaPeriodoFacade();
        final List<String> lista4 = new ArrayList<>();
        FolhaPeriodo periodo = new FolhaPeriodo();
        Funcionario f1 = new Funcionario();
        String pis = null;
        f1.setPis("13744465275");
//
        Funcionario f2 = new Funcionario();
        f2.setPis("12754771036");
//
        Funcionario f3 = new Funcionario();
        f3.setPis("12659898660");
//
        List<Funcionario> funcionarios = new ArrayList<>();
//
        funcionarios.add(f1);
        funcionarios.add(f2);
        funcionarios.add(f3);
//
//        List<PtoArquivo> arquivos = new ArrayList();
//        EntityManager manager = new JPAConect().getEntityManager();
//        SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm");
//        String dataBatida = "0";
//        String PeriodoBatida = "0";
//        String pis = null;

// Inicia a busca das datas por periodo    
//        for (String func : funcionarios) {
//            arquivos1 = af.buscaBatidas(func, "201708");
//            System.out.println(func);
//        }
//        for (PtoArquivo arquivo : arquivos1) {
//            dataBatida = sdf.format(arquivo.getData_batida());
//
//            listaPis.add(dataBatida);
//        }
//
//        Map<String, Integer> contador = new HashMap<>();
//        for (String valor : listaPis) {
//            if (!contador.containsKey(valor)) {
//                contador.put(valor, 1);
//            }
//            contador.put(valor, contador.get(valor) + 1);
//        }
////Exibe os que tiverem mais de 1.
//        for (Map.Entry<String, Integer> item : contador.entrySet()) {
//            if (item.getValue() >= 2) {
//            }
//            listaDatas.add(item.getKey());
//            Collections.sort(listaDatas);
//
//        }
// Inicia Busca por dia    
//        manager.getTransaction().begin();
//        for (String data1 : listaDatas) {
//
//            for (Funcionario funcionarioLista : funcionarios) {
//                EspelhoPonto espelho = new EspelhoPonto();
//                arquivos = af.buscaBatidasByDate(funcionarioLista.getPis(), sdf.parse(data1));
//                for (PtoArquivo arquivo : arquivos) {
//
//                    dataBatida = sdf.format(arquivo.getData_batida());
//                    PeriodoBatida = periodo.format(arquivo.getData_batida());
//                    String horaBatida = sdfH.format(arquivo.getHora());
//                    pis = arquivo.getPis();
//
//                    lista4.add(horaBatida);
//                    Collections.sort(lista4);
//
//                    espelho.setData(data1);
//                    espelho.setPeriodo(PeriodoBatida);
//                    espelho.setPis(pis);
//                    manager.persist(espelho);
//                }
//
//                try {
//
//                    espelho.setEntrada01(lista4.get(0));
//                    espelho.setSaida01(lista4.get(1));
//                    espelho.setEntrada02(lista4.get(2));
//                    espelho.setSaida02(lista4.get(3));
//                    espelho.setData(data1);
//                    espelho.setPeriodo(PeriodoBatida);
//                    espelho.setPis(pis);
//                    System.out.println(lista4);
//                    manager.persist(espelho);
//                } catch (Exception e) {
////                    espelho.setData(dataBatida);
////                    espelho.setPeriodo(PeriodoBatida);
////                    espelho.setPis(pis);
//                }
//
//                lista4.clear();
//
//            }
//
//        }
//        manager.getTransaction().commit();
//        manager.close();
//        List<EspelhoPonto> espelhos = new ArrayList<>();
//        espelhos = af.buscaHorasEspelho("14721974279");
//        for (EspelhoPonto item : espelhos) {
//            EspelhoPonto espelho = new EspelhoPonto();
//            try {
////                Timestamp entrada = sdfh.parse(item.getEntrada01());
//
//                String[] td1 = item.getEntrada01().split(":");
//                String[] td2 = item.getSaida01().split(":");
//                String[] tds1 = item.getEntrada02().split(":");
//                String[] tds2 = item.getSaida02().split(":");
//
////==============================================================================  
//                String entrada01Hora = td1[0];
//                String entrada01Minuto = td1[1];
//
//                long entrada01HoraLong = Long.parseLong(entrada01Hora);
//                long entrada01MinutoLong = Long.parseLong(entrada01Minuto) / 60;
//
//                long result = entrada01HoraLong + entrada01MinutoLong;
//
////==============================================================================                
//                String saida01Hora = td2[0];
//                String saida01Minuto = td2[1];
//
//                long saida01HoraLong = Long.parseLong(saida01Hora);
//                long saida01MinutoLong = Long.parseLong(saida01Minuto) / 60;
//
//                long result1 = saida01HoraLong + saida01MinutoLong;
//
////==============================================================================  
//                String entrada02Hora = tds1[0];
//                String entrada02Minuto = tds1[1];
//
//                long entrada02HoraLong = Long.parseLong(entrada02Hora);
//                long entrada02MinutoLong = Long.parseLong(entrada02Minuto) / 60;
//
//                long result2 = entrada02HoraLong + entrada02MinutoLong;
//
////==============================================================================     
//                String saida02Hora = tds2[0];
//                String saida02Minuto = tds2[1];
//
//                long saida02HoraLong = Long.parseLong(saida02Hora);
//                long saida02MinutoLong = Long.parseLong(saida02Minuto) / 60;
//
//                long result3 = saida02HoraLong + saida02MinutoLong;
//                //==============================================================================                  
//                ////
//                //                long entradaHoras = Long.parseLong(entrada01.substring(0, 2));
//                //                long entradaMinutos = Long.parseLong(entrada01.substring(2, 3)) / 60;
//                //
//                //                long saida1Horas = Long.parseLong(saida01.substring(0, 2));
//                //                long saida1Minutos = Long.parseLong(saida01.substring(2, 4)) / 60;
//                ////
////
////                System.out.println(result);
//
////                long entrada2 = Long.parseLong(entrada02);
////                long saida2 = Long.parseLong(saida02);
////
////                long resultadoManha = saida2 - entrada1;
////
////                long resultadoTarde = saida1 - entrada2;
////
////                long somaTotal = resultadoTarde + resultadoManha ;
////
////                String convertFinal = String.format("%04d", somaTotal);
////
////                String resultSet = convertFinal.substring(0, 2) + ":" + convertFinal.substring(2, 4);
////
////                System.out.println(resultSet);
////                espelho.setTrabalhadas(resultSet);
////                manager.persist(espelho);
//            } catch (Exception e) {
//
//            }
//        }
//        manager.getTransaction().commit();
//        manager.close();
//        List<EspelhoPonto> espelhos = new ArrayList<>();
//        espelhos = af.buscaHorasEspelho("12659898660");
//
//        for (EspelhoPonto item : espelhos) {
//
//            EspelhoPonto espelho = new EspelhoPonto();
//
//            try {
//                Date hora1 = sdfh.parse(item.getEntrada01());
//                Date hora2 = sdfh.parse(item.getSaida01());
//
//                Date hora3 = sdfh.parse(item.getEntrada02());
//                Date hora4 = sdfh.parse(item.getSaida02());
//
//                long intervalo = hora2.getTime() - hora1.getTime();
//                long intervalo2 = hora4.getTime() - hora3.getTime();
//
//                String manha = String.format("%02d:%02d", intervalo / 3600000, (intervalo / 60000) % 60);
//                String tarde = String.format("%02d:%02d", intervalo2 / 3600000, (intervalo2 / 60000) % 60);
//
//                GregorianCalendar gc = new GregorianCalendar();
//                String v = manha;
//                String v2 = tarde;
//                int hora = Integer.parseInt(v.substring(0, 2));
//                int min = Integer.parseInt(v.substring(3, 5));
//                int seg = 0;
//                Time time = new Time(hora, min, seg);
//                gc.setTimeInMillis(time.getTime());
//                hora = Integer.parseInt(v2.substring(0, 2));
//                min = Integer.parseInt(v2.substring(3, 5));
//                gc.add(Calendar.HOUR, hora);
//                gc.add(Calendar.MINUTE, min);
//
//                System.out.println("HORA SOMADA: " + sdfh.format(gc.getTime()));
//                
//                
//            } catch (Exception e) {
//                System.out.println("Excessão" + e);
//            }
//        }
//        GregorianCalendar c = new GregorianCalendar();
//
//        c.add(Calendar.DAY_OF_MONTH, -3);
//
//
//        for (Funcionario funcionarioLista : funcionarios) {
//
//            for (String item : listaFoo) {
//
//                EspelhoPonto espelho1 = new EspelhoPonto();
//
//                espelho1.setData(item.substring(0,12));
//                espelho1.setPeriodo("201708");
//                espelho1.setNome_Funcionario(funcionarioLista.getNome());
//                espelho1.setPis(funcionarioLista.getPis());
//                new GenericDAO<>(EspelhoPonto.class).adiciona(espelho1);
//
//            }
//
//        }
//        
//        
//
//        EspelhoPontoFacade afBusca = new EspelhoPontoFacade();
//        List<EspelhoPonto> retornoEspelho = new ArrayList<>();
//        retornoEspelho = afBusca.buscaHorasEspelho("12738913034");
//
//        for (EspelhoPonto espelhoRecalcular : retornoEspelho) {
//
////            for (Funcionario funcionarioLista1 : funcionarios) {
//
//
//                List<PtoArquivo> arquivos = af.buscaBatidasByDate("12738913034", sdf.parse("01/08/17"));
//
//                for (PtoArquivo arquivo : arquivos) {
//
//                    String dataBatida = sdf.format(arquivo.getData_batida());
//                    PeriodoBatida = periodo1.format(arquivo.getData_batida());
//                    String horaBatida = sdfh.format(arquivo.getHora());
//                    pis = arquivo.getPis();
////                    processa = arquivo.getProcessado();
//                    lista4.add(horaBatida);
//                    Collections.sort(lista4);
//
//                    arquivo.setProcessado(true);
//                    espelhoRecalcular.setPeriodo(PeriodoBatida);
//                    espelhoRecalcular.setPis(pis);
////                    manager.persist(espelho1);
////                    manager.merge(arquivo);
//                    System.out.println(arquivo.getHora());
//                    new GenericDAO<>(EspelhoPonto.class).atualiza(espelhoRecalcular);
//
//                }
//                try {
//
//                    espelhoRecalcular.setPeriodo(periodo.getPeriodo());
////                    espelhoRecalcular.setFuncionario(funcionarioLista1);
////                    espelhoRecalcular.setNome_Funcionario(funcionarioLista1.getNome());
//                    espelhoRecalcular.setEntrada01(lista4.get(0));
//                    espelhoRecalcular.setSaida01(lista4.get(1));
//                    espelhoRecalcular.setEntrada02(lista4.get(2));
//                    espelhoRecalcular.setSaida02(lista4.get(3));
////                    espelho1.setData(data1);
//                    espelhoRecalcular.setPeriodo(PeriodoBatida);
//                    espelhoRecalcular.setPis(pis);
//
////                    espelhoRecalcular.setEntrada_prevista_01(sdfh.format(funcionarioLista1.getHorario().getHora_EntradaT1_util()));
////                    espelhoRecalcular.setSaida_prevista_01(sdfh.format(funcionarioLista1.getHorario().getHora_saidaT1_util()));
////                    espelhoRecalcular.setEntrada_prevista_02(sdfh.format(funcionarioLista1.getHorario().getHora_EntradaT2_util()));
////                    espelhoRecalcular.setSaida_prevista_02(sdfh.format(funcionarioLista1.getHorario().getHora_saidaT2_util()));
//
//// Calcula as Batidas 
//                    String[] td1 = lista4.get(0).split(":");
//                    String[] td2 = lista4.get(1).split(":");
//
//                    String[] tds1 = lista4.get(2).split(":");
//                    String[] tds2 = lista4.get(3).split(":");
//
//                    String entrada01 = td1[0] + td1[1];
//                    String saida01 = td2[0] + td2[1];
//
//                    String entrada02 = tds1[0] + tds1[1];
//                    String saida02 = tds2[0] + tds2[1];
//
//                    long entrada1 = Long.parseLong(entrada01);
//                    long saida1 = Long.parseLong(saida01);
//
//                    long entrada2 = Long.parseLong(entrada02);
//                    long saida2 = Long.parseLong(saida02);
//
////Calculo Hora Extra==========================================================================================================     
//                    long saidaextra = 1815;
//                    if (saida2 <= saidaextra) {
//                        System.out.println("Não Tem Hora Extra......");
//
//                    } else {
//
//                        long somaExtras = saida2 - saidaextra;
//                        String convertFinalExtra = String.format("%04d", somaExtras);
//                        String resultSetExtra = convertFinalExtra.substring(0, 2) + ":" + convertFinalExtra.substring(2, 4);
//                        espelhoRecalcular.setExtras(resultSetExtra);
//                    }
////Calculo Normais =============================================================================================================
//                    Date hora11 = sdfh.parse(lista4.get(0));
//                    Date hora22 = sdfh.parse(lista4.get(1));
//
//                    Date hora33 = sdfh.parse(lista4.get(2));
//                    Date hora44 = sdfh.parse(lista4.get(3));
//
//                    long intervalo = hora22.getTime() - hora11.getTime();
//                    long intervalo2 = hora44.getTime() - hora33.getTime();
//
//                    String manha = String.format("%02d:%02d", intervalo / 3600000, (intervalo / 60000) % 60);
//                    String tarde = String.format("%02d:%02d", intervalo2 / 3600000, (intervalo2 / 60000) % 60);
//
//                    GregorianCalendar gc = new GregorianCalendar();
//                    String v = manha;
//                    String v2 = tarde;
//                    int hora = Integer.parseInt(v.substring(0, 2));
//                    int min = Integer.parseInt(v.substring(3, 5));
//                    int seg = 0;
//                    Time time = new Time(hora, min, seg);
//                    gc.setTimeInMillis(time.getTime());
//                    hora = Integer.parseInt(v2.substring(0, 2));
//                    min = Integer.parseInt(v2.substring(3, 5));
//                    gc.add(Calendar.HOUR, hora);
//                    gc.add(Calendar.MINUTE, min);
//
//                    String resultSet = sdfh.format(gc.getTime());
//                    espelhoRecalcular.setTrabalhadas(resultSet);
////Fim Calculo Normais ===========================================================================================================
//
//                    new GenericDAO<>(EspelhoPonto.class).atualiza(espelhoRecalcular);
//
//                } catch (Exception e) {
////                    espelho1.setData(data1);
//                }
//
//                lista4.clear();
//
////            }
//
//        }
//        manager.getTransaction().commit();
//        manager.close();
    }
}
