package br.com.utils.Teste;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.faces.application.FacesMessage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.primefaces.model.DefaultStreamedContent;

public class Teste {

    public static void main(String[] args) throws ParseException, FileNotFoundException, JRException, SQLException {

        List<String> lista = new ArrayList<String>();

//        if (servidor != null) {
//            PessoaInclusaoRelogio pir = this.pessoaInclusaoRelogioService.porId(servidor.getPessoa().getId());
//            lista.add(pir.getArquivo());
//        } else {
//            if (listaServidoresPorEscala == null || listaServidoresPorEscala.size() == 0) {
//                facesUtil.addError("ERRO", "Clique primeiro em Carregar", FacesMessage.SEVERITY_ERROR);
//                desativarDownload = true;
//                return;
//            } else {
//                // 1 - pega todos os servidores em escala e inclui na lista
//                for (ServidorVinculo item : listaServidoresPorEscala) {
//                    PessoaInclusaoRelogio pir = this.pessoaInclusaoRelogioService.porId(item.getPessoa().getId());
//                    lista.add(pir.getArquivo());
//                }
//
//                // 2 - pega os servidores que não estao na escala mas que batem ponto neste relogio e inclui na lista
//                List<PtoPessoaAgrupamento> listaAgrupamento = this.agrupamentoService.porSetor(this.setorSelecionado);
//                for (PtoPessoaAgrupamento item : listaAgrupamento) {
//                    PessoaInclusaoRelogio pir = this.pessoaInclusaoRelogioService.porId(item.getPessoa().getId());
//                    lista.add(pir.getArquivo());
////                }
//            }
//        }
//		desativarDownload = false;
        lista.add("RICARDO SANTOS ROSA");
        FileWriter arquivo;
        String tempFolder = System.getProperty("java.io.tmpdir");
        tempFolder = tempFolder.substring(tempFolder.length() - 1, tempFolder.length() - 1).equals(File.separator) ? tempFolder
                : tempFolder + File.separator;
        File file = new File(tempFolder + "rep_colaborador.txt");

        try {
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
            System.err.println(arquivo);
            arquivo.close();

            InputStream inputStream = new FileInputStream(file);

//            stream = new DefaultStreamedContent(inputStream, "plain/txt", "rep_colaborador.txt");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//        

}
