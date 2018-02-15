package br.com.Beans;

import br.com.Model.Usuario;
import br.com.DAO.GenericDAO;
import br.com.Facade.UsuarioFacade;
import br.com.Core.CoreUtils;
import br.com.Model.Departamento;
import br.com.Model.Funcionario;
import br.com.Model.UsuarioPerfil;
import br.com.utils.Autoriza.GeradorIdentificacao;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

@ManagedBean
@ViewScoped
public class UsuarioBean implements Serializable {

    private boolean exibeAdm = Boolean.TRUE; // get/set
    String datahora = CoreUtils.dateParaString(new Date(), " dd/MM/yyyy - HH:mm");
    private long totalUsuarios;

    Usuario usuarioUsuario;
    Usuario usuarioLogadoSistema;
    UsuarioFacade usuariFacade;

    List<Usuario> listaUsuarioPerfil;
    List<UsuarioPerfil> listaPerfilUsuario;

    public UsuarioBean() {

        if (usuarioUsuario == null) {
            usuarioUsuario = new Usuario();
        }

        if (usuariFacade == null) {
            usuariFacade = new UsuarioFacade();
        }

        if (listaUsuarioPerfil == null) {
            listaUsuarioPerfil = new ArrayList<>();
        }

        if (listaPerfilUsuario == null) {
            listaPerfilUsuario = new ArrayList<>();
        }

        if (usuarioLogadoSistema == null) {
            usuarioLogadoSistema = new Usuario();
        }

    }

    public List<Funcionario> getFuncionarios() {
        return new GenericDAO<>(Funcionario.class).listaTodos();
    }

    public List<Usuario> getUsuarios() {
        return new GenericDAO<>(Usuario.class).listaTodos();
    }

    public List<Departamento> getDepartamento() {
        return new GenericDAO<>(Departamento.class).listaTodos();
    }

    public boolean isExibeAdm() {
        return exibeAdm;
    }

    public void setExibeAdm(boolean exibeAdm) {
        this.exibeAdm = exibeAdm;
    }

    public String gravar() {
        try {

            GeradorIdentificacao geradorIdentificacao = new GeradorIdentificacao();
            String idendificacao = geradorIdentificacao.geraCriptografia(usuarioUsuario.getSenha());

            usuarioLogadoSistema = pegaUsuario();

            Usuario verificaLogin = usuariFacade.buscaLogincadastro(usuarioUsuario.getLogin());
            Usuario verificaEmail = usuariFacade.buscaEmail(usuarioUsuario.getEmail());
            if (usuarioUsuario.getId() == 0) {

                if (verificaLogin != null) {
                    Msg.addMsgWarn("Login já Cadastrado ");

                } else if (verificaEmail != null) {
                    Msg.addMsgWarn("Email já cadsatrado");
                } else {
                    usuarioUsuario.setUsuario(usuarioLogadoSistema);
                    usuarioUsuario.setUltima_alteracao(datahora);
                    usuarioUsuario.setSenha(idendificacao);
                    usuariFacade.adiciona(usuarioUsuario);
                    Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);

                    return "/View/usuario/lista-usuario/index?faces-redirect=true";
                }
            } else {
                usuarioUsuario.setUsuario(usuarioLogadoSistema);
                usuarioUsuario.setUltima_alteracao(datahora);
                usuariFacade.atualiza(usuarioUsuario);
                Msg.addMsgInfo("Cadastro atualizado com sucesso! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);

                return "/View/usuario/lista-usuario/index?faces-redirect=true";

            }

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            Msg.addMsgError("Operação não realizada!" + e);
            System.err.println("ERRO: " + e);
        }
        return null;
    }

    public String novoUsuario() {
//        usuarioUsuario = new Usuario();
//        usuarioLogadoSistema = new Usuario();
        try {

//            usuarioLogadoSistema = pegaUsuario();
//            if (usuarioLogadoSistema.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
            usuarioUsuario = new Usuario();
            usuarioUsuario.setId(0L);
            return "/View/usuario/cadastrar-usuario/index?faces-redirect=true";

//            } else {
//                Msg.addMsgFatal("Somente Administrador Sr(a) " + usuarioLogadoSistema.getFuncionario().getNome());
//                System.err.println("Somente Administrador Sr(a) " + usuarioLogadoSistema.getFuncionario().getNome());
//            }
        } catch (Exception e) {
            Msg.addMsgWarn("Operação não realizada " + e);
            System.err.println("ERRO: " + e);
        }

        return null;
    }

    public String excluir(Usuario usuario) {
        try {

            usuarioLogadoSistema = pegaUsuario();

            if (usuarioLogadoSistema.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                usuariFacade.remove(usuario);

                Msg.addMsgFatal("Usuario Excluido com Sucesso");
                return "/View/usuario/lista-usuario/index";

            } else {
                System.err.println("Somente administrador Sr(a) " + usuarioLogadoSistema.getFuncionario().getNome());
                Msg.addMsgFatal("Somente administrador Sr(a) " + usuarioLogadoSistema.getFuncionario().getNome());
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada " + e);
            System.err.println("ERRO: " + e);
        }
        return null;
    }

    public String editar(Usuario user) {

        try {
            this.usuarioUsuario = user;
            return "/View/usuario/cadastrar-usuario/index";

        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }
        return null;

    }

    public String minhaConta() {
        return "/View/usuario/lista-usuario/index?faces-redirect=true";
    }

    public void recuperarSenha(Usuario usuario) {

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
                message.setText("Conforme Solicitado sua atual senha é :" + usuario.getSenha());
                /**
                 * Método para enviar a mensagem criada
                 */
                Transport.send(message);
                System.out.println("Feito!!!");
                Msg.addMsgInfo("Senha Enviada com Sucesso !");

            } catch (MessagingException e) {
                Msg.addMsgFatal("Operação não Realizada");
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação Não Realizada !");
        }
    }

    public String getNomeUsuario(Usuario usuario) {
        String nome = null;

        LoginBean usuariologado = new LoginBean();

        if (usuario != null) {
            JOptionPane.showMessageDialog(null, nome);
        }

        return nome;
    }

    public void isValidaLogin() {
        usuarioUsuario = usuariFacade.buscaLogincadastro(this.usuarioUsuario.getLogin());
        if (usuarioUsuario.getLogin() != null) {
            Msg.addMsgFatal("Login já cadastrado!");
        }
    }

    public void isValidaEmailRecupera() {
        usuarioUsuario = usuariFacade.buscaEmail(this.usuarioUsuario.getEmail());
        if (usuarioUsuario.getEmail() == null) {
            Msg.addMsgFatal("Email Não Localizado!");
            System.out.println("Email Não Encontrado");
        }
    }

    public String home() {
        return "/home?faces-redirect=true";
    }

    private HttpSession getContext() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        return session;
    }

    public String deslogar() {

        Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.clear();

        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove("usuarioLogado");
        HttpSession session = getContext();
        session.setAttribute("usuarioLogado", null);

        context.renderResponse();

        String datahora1 = CoreUtils.dateParaString(new Date(), "dd/MM/yyyy - HH:mm");
        System.out.println("encerrando sessao as " + datahora1 + ".");

        return "/index?faces-redirect=true";
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

    public String getHome() {

        try {
            usuarioUsuario = pegaUsuario();

            if (usuarioUsuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {

                return "/View/HomeAdmin/index?faces-redirect=true";

            } else if (usuarioUsuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("gestor")) {

                return "/View/HomeGestor/index?faces-redirect=true";

            } else {
                return null;
            }

        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

        return null;

    }

//Gets and Setrs
    public Usuario getUsuario() {
        return usuarioUsuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioUsuario = usuario;
    }

    public List<Usuario> getListaUsuarioPerfil() {

        try {
//
//            usuarioLogadoSistema = pegaUsuario();
//
//            if (usuarioLogadoSistema.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
//
//                listaUsuarioPerfil.clear();
            listaUsuarioPerfil = usuariFacade.listaTodosUsuarios();

//            } else if (usuarioLogadoSistema.getUsuarioPerfil().getDescricao().equalsIgnoreCase("gestor")) {
//
//                listaUsuarioPerfil.clear();
//                listaUsuarioPerfil = usuariFacade.buscaUsuarioPerfil(usuarioLogadoSistema);
//            }
        } catch (Exception e) {

            System.err.println("ERRO ao Listar " + e);
            Msg.addMsgWarn("ERRO ao Listar " + e);
        }

        return listaUsuarioPerfil;
    }

    public void setListaUsuarioPerfil(List<Usuario> listaUsuarioPerfil) {
        this.listaUsuarioPerfil = listaUsuarioPerfil;
    }

    public long getTotalUsuarios() {
        return totalUsuarios = usuariFacade.contaUsuarioTotal();
    }

    public void setTotalUsuarios(long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public List<UsuarioPerfil> getListaPerfilUsuario() {
        return listaPerfilUsuario = usuariFacade.listaTodosPerfisUsuario();
    }

    public void setListaPerfilUsuario(List<UsuarioPerfil> listaPerfilUsuario) {
        this.listaPerfilUsuario = listaPerfilUsuario;
    }

}
