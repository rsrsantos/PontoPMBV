package br.com.Beans;

import br.com.Facade.PtoArquivoFacade;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.ArquivoPonto;
import br.com.Model.EspelhoPonto;
import br.com.Model.Funcionario;
import br.com.Model.PtoArquivo;
import br.com.Model.PtoEquipamento;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class FileUploadBean implements Serializable {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    private static final SimpleDateFormat sdfH = new SimpleDateFormat("HHmm");
    private static final SimpleDateFormat periodo = new SimpleDateFormat("yyyyMM");
    EntityManager manager = new JPAConect().getEntityManager();
    final private org.apache.log4j.Logger logger = Logger.getLogger(PtoEquipamentoBeanHenry.class.getName());
    UploadedFile file;
    private final String lastNSR = "0";

    PtoArquivoFacade ptoarquivofacade;
    Usuario usuario;

    public FileUploadBean() {

        if (usuario == null) {
            usuario = new Usuario();
        }

        if (ptoarquivofacade == null) {
            ptoarquivofacade = new PtoArquivoFacade();
        }

    }

    public String dummyAction() {
        System.out.println("Arquivo :: " + file.getFileName() + " :: Tamanho :: " + file.getSize());
        return "";
    }

    public void fileUploadListener(FileUploadEvent e) {

        usuario = pegaUsuario();

        this.file = e.getFile();
        System.out.println("Uploaded File Name Is :: " + file.getFileName() + " :: Uploaded File Size :: " + file.getSize());

        if (file != null) {
            String prefix = FilenameUtils.getBaseName(file.getFileName());
            String suffix = FilenameUtils.getExtension(file.getFileName());
            String repCodigo = prefix.replace("rep_", "");
            File save;
            try {
                save = File.createTempFile(prefix + "-", ".");
                Files.write(save.toPath(), file.getContents());
                ArquivoPonto arquivoPonto = new ArquivoPonto();
                arquivoPonto.setNome(prefix);
                arquivoPonto.setTipo(suffix.toUpperCase());
//                arquivoPonto.setFile(Arrays.toString(file.getContents()));
                arquivoPonto.setUsuario(usuario);
                arquivoPonto.setDataRegistro(new Date());

                try {
                    new GenericDAO<>(ArquivoPonto.class).adiciona(arquivoPonto);
                    processarArquivoPonto(save, usuario, arquivoPonto.getId());

                } catch (ParseException e1) {
                    System.out.println(e1.getMessage());

                }

            } catch (IOException e1) {
                e1.printStackTrace();
                Msg.addMsgFatal("Erro ao Carregar Aquivo.");
                System.out.println("Erro ao Carregar");
            }
        }

    }

    public void processarArquivoPonto(File file, Usuario user, Integer idArquivo) throws ParseException {
        usuario = pegaUsuario();

        List<String> listaRegistros = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                listaRegistros.add(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        manager.getTransaction().begin();
        for (String item : listaRegistros) {
            int nsr = Integer.valueOf(item.substring(0, 9));
            int tipo = Integer.valueOf(item.substring(9, 10));
            Date dataEvento = sdf.parse(item.substring(10, 18));
            Date HrsString = sdfH.parse(item.substring(18, 22));
            String pisString = item.substring(23, 34);

            String periodoString = "";
            periodoString = dataParaPeriodo(dataEvento);
            PtoArquivo arquivo = new PtoArquivo();
            EspelhoPonto espelho = new EspelhoPonto();
            PtoEquipamento ptoequipamento = new PtoEquipamento();
            Funcionario funcinario = new Funcionario();

            Boolean existe = false;

            // verifica se nsr já existe
            // para
            // este equipamento
            existe = ptoarquivofacade.existe(nsr);
            if (existe == false) {

                if (tipo != 2 || tipo != 5) {

                    arquivo.setPeriodo(periodoString);
                    arquivo.setData_batida(dataEvento);
                    arquivo.setTipo(tipo);
                    arquivo.setNsr(nsr);
                    arquivo.setHora(HrsString);
                    arquivo.setPis(pisString);
                    arquivo.setPtooriginal(item);
                    arquivo.setArquivoFisico(idArquivo);
                    arquivo.setProcessado(false);
                    arquivo.setUsuario(usuario);

                    logger.info(" ###################### gravando registro de PONTO com NSR: " + nsr
                            + " para equipamento: " + ptoequipamento.getId() + (new Date()));
                    try {
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    manager.persist(arquivo);

                }

            } else {
                Msg.addMsgError("Arquivo com Nsr já importado");
                System.out.println("Nsr do Arquivo já importada");
                return;

            }
        }
        Msg.addMsgInfo("Importado com Sucesso: " + listaRegistros.size() + " Registros");
        System.out.println("Importado com Sucesso: " + listaRegistros.size() + " Registros");
        manager.getTransaction().commit();
        manager.close();

    }

    public String dataParaPeriodo(Date data) throws ParseException {
        if (data != null) {
            return periodo.format(data);
        }
        return null;
    }

    public String listar() {
        return "/View/equipamento/importar-arquivo/index?faces-redirect=true";
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
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
