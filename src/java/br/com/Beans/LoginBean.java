package br.com.Beans;

import br.com.Model.Usuario;
import br.com.Facade.UsuarioFacade;
//import br.com.Core.BeanUtils;
import br.com.Core.CoreUtils;
import br.com.DAO.JPAConect;
import br.com.utils.Autoriza.GeradorIdentificacao;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import org.apache.log4j.Logger;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private String senha;
    private String login;
    private Date acesso;
    private Integer sessionTimeout;
    EntityManager manager = new JPAConect().getEntityManager();
    private Boolean isAdmin;
    private List<String> listaOnline = new ArrayList<>();

    final private org.apache.log4j.Logger logger = Logger.getLogger(LoginBean.class.getName());

    private String usuariologin;
    private String email;
    private long matricula;

    private Usuario usuario;
    private UsuarioFacade usuarioDao;
    private UsuarioFacade usuariofacade;

    public LoginBean() {
        if (usuario == null) {
            usuario = new Usuario();
        }

        if (usuarioDao == null) {
            usuarioDao = new UsuarioFacade();
        }

        if (usuariofacade == null) {
            usuariofacade = new UsuarioFacade();
        }

    }

    public String efetuaLogin() {

        try {
            usuario = usuarioDao.logarLogin(this.login);

            if (usuario != null) {

                GeradorIdentificacao geradorIdentificacao = new GeradorIdentificacao();
                String idendificacao = geradorIdentificacao.geraCriptografia(this.senha);

                if (usuario.getSenha().equals(idendificacao)) {

                    if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.getExternalContext().getSessionMap().put("usuarioLogado", usuario);
                        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//                        request.lo
                        retorna();
                        

                        return "/View/HomeAdmin/index?faces-redirect=true";

                    } else if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("Gestor")) {
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.getExternalContext().getSessionMap().put("usuarioLogado", usuario);
                        retorna();

                        return "/View/HomeGestor/index?faces-redirect=true";
                    }

                } else {
                    Msg.addMsgWarn("Senha inválida!");
                }

            } else {
                Msg.addMsgWarn("Login não encontrado");
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            Msg.addMsgWarn("login/senha não encontrado!");
        }
        return null;
    }

    public void retorna() {

        try {
            String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");
            usuario.setUltimologin(datahora);
            usuariofacade.atualiza(usuario);
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Erro Atualizar Usuario: " + e);
        }

    }

    public boolean verificaUsuarioNull() {
        usuario = usuarioDao.logarLogin(this.login);
        return usuario.getId() != null;
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        String ultimoAcesso = (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new Date(event.getSession().getLastAccessedTime()));
        System.out.println("Sessão expirada " + event.getSession().getId() + ". Ultimo Acesso = " + ultimoAcesso);
    }

    public String recuperarSenha() {

        usuario = usuariofacade.buscaUsuarioRecuperarSenha(usuariologin, matricula, email);
        if (usuario != null) {
            try {
                Properties props = new Properties();

                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                Session session = Session.getDefaultInstance(props,
                        new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("pontoeletronicopmbv@gmail.com", "@#monitor2016");
                    }
                });
                session.setDebug(true);
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("pontoeletronicopmbv@gmail.com")); //Remetente

                    Address[] toUser = InternetAddress //Destinatário(s)
                            .parse(usuario.getEmail());
                    message.setRecipients(Message.RecipientType.TO, toUser);
                    message.setSubject("Recuperação de Senha");//Assunto
                    message.setText(
                            "\n\n\n" + "SISTEMA DE PONTO PMBV" + "\n\n\n"
                            + "Senhor(a) , " + usuario.getFuncionario().getNome() + "\n"
                            + "Conforme Socilitado estamos encaminhando sua senha registrada no banco do sistema de Ponto PMBV" + "\n\n\n"
                            + "com o seguinte conteúdo Usuario : " + usuario.getLogin() + "\n"
                            + "com o seguinte conteúdo Senha : " + usuario.getSenha() + "\n\n\n"
                            + "Realize a análise das informações alimentadas no banco de dados e se necessário, proceda com o relatório para providências cabíveis." + "\n\n\n\n"
                            + "PREFEITUA MUNICIPAL DE BOA VISTA | SECRETÁRIA DE INCLUSÃO DIGITAL - CCTI"
                    );
                    /**
                     * Método para enviar a mensagem criada
                     */
                    Transport.send(message);
                    System.out.println("Feito!!!");
                    Msg.addMsgInfo("Senha Enviada com Sucesso: " + email);

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);

                    usuario = new Usuario();
                    limpacampos();

                    return "/index?faces-redirect=true";

                } catch (MessagingException e) {
                    Msg.addMsgFatal("Operação não Realizada");
                    logger.info("ERRO: " + e);
                }
            } catch (Exception e) {
                Msg.addMsgError("Operação Não Realizada !");
                logger.info("ERRO: " + e);

            }
        } else {
            Msg.addMsgError("Nenhum registro encontrado!");
            limpacampos();
        }
        return null;
    }

    public void limpacampos() {
        usuariologin = "";
        matricula = 0;
        email = "";
    }

    public String pesquisarLogin() {
        return "/View/usuario/MudarSenha.xhtml?faces-redirect=true";
    }

    public Date getAcesso() {
        return acesso;
    }

    public void setAcesso(Date acesso) {
        this.acesso = acesso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UsuarioFacade getUsuarioDao() {
        return usuarioDao;
    }

    public void setUsuarioDao(UsuarioFacade usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<String> getListaOnline() {
        return listaOnline;
    }

    public void setListaOnline(List<String> listaOnline) {
        this.listaOnline = listaOnline;
    }

    public String getUsuariologin() {
        return usuariologin;
    }

    public void setUsuariologin(String usuariologin) {
        this.usuariologin = usuariologin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getMatricula() {
        return matricula;
    }

    public void setMatricula(long matricula) {
        this.matricula = matricula;
    }

}
