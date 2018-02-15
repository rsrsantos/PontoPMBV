package br.com.Beans;

import br.com.Facade.HorarioFacade;
import br.com.DAO.GenericDAO;
import br.com.DAO.JPAConect;
import br.com.Model.Horario;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

@ManagedBean
@SessionScoped
public class HorarioBean implements Serializable {

    Horario horario;
    HorarioFacade horariofacade;
    Usuario usuario;

    EntityManager manager = new JPAConect().getEntityManager();
    SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm");

    private long totalHorario;

    public HorarioBean() {

        if (horario == null) {
            horario = new Horario();
        }

        if (horariofacade == null) {
            horariofacade = new HorarioFacade();
        }

        if (usuario == null) {
            usuario = new Usuario();
        }
    }

    public List<Horario> getHorarios() {
        return new GenericDAO<>(Horario.class).listaTodos();
    }

    public String gravarHorario() {

        try {

            Horario validaHorario = horariofacade.buscaHorario(horario.getNome());
            if (horario.getId() == 0) {

                if (validaHorario != null) {

                    Msg.addMsgWarn("Horário já Cadastrado ");

                } else {

                    Msg.addMsgInfo("Cadastro realizado com sucesso! ");

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);
                    CargaHoraria(horario);
                    horariofacade.adiciona(horario);

                    return "/View/horario/lista-horario/index?faces-redirect=true";
                }
            } else {

                CargaHoraria(horario);
                horariofacade.atualiza(horario);
                Msg.addMsgInfo("Horário Atualizado! ");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getFlash().setKeepMessages(true);
                CargaHoraria(horario);
                return "/View/horario/lista-horario/index?faces-redirect=true";

            }

        } catch (Exception e) {

            Msg.addMsgError("Operação não realizada!" + e);
            System.out.println(e);
        }
        return null;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public String excluir(Horario horario) {

        try {

            usuario = pegaUsuario();

            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {

                horariofacade.remove(horario);
                Msg.addMsgFatal("Horário Excluido: " + horario.getNome());
                return "/View/horario/lista-horario/index";

            } else {

                Msg.addMsgFatal("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());
                System.err.println("Somente administrador Sr(a) " + usuario.getFuncionario().getNome());
            }
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

        return null;

    }

    public String editar(Horario horario) {

        this.horario = horario;
        return "/View/horario/cadastrar-horario/index";

    }

    public String novoHorario() {

        try {

            usuario = pegaUsuario();
            horario = new Horario();
            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {

                return "/View/horario/cadastrar-horario/index?faces-redirect=true";

            } else {
                System.err.println("Somente administrador");
                Msg.addMsgFatal("Somente Administrador");
            }

        } catch (Exception e) {

            Msg.addMsgWarn("Operação não realizada! " + e);
            System.err.println("ERRO: " + e);
        }

        return null;
    }

    public void CargaHoraria(Horario horario) {

        try {

            if (horario.getHora_EntradaT2_util() == null) {

                String entrada01 = sdfH.format(horario.getHora_EntradaT1_util());
                String saida01 = sdfH.format(horario.getHora_saidaT1_util());

                Date hora111 = sdfH.parse(entrada01);
                Date hora23 = sdfH.parse(saida01);

                long intervalo1 = hora23.getTime() - hora111.getTime();

                String manha1 = String.format("%02d:%02d", intervalo1 / 3600000, (intervalo1 / 60000) % 60);

                horario.setCarga(manha1);

            } else {

                String entrada01 = sdfH.format(horario.getHora_EntradaT1_util());
                String saida01 = sdfH.format(horario.getHora_saidaT1_util());
                String entrada02 = sdfH.format(horario.getHora_EntradaT2_util());
                String saida02 = sdfH.format(horario.getHora_saidaT2_util());

                Date hora111 = sdfH.parse(retornaHoraCalculo(entrada01));
                Date hora23 = sdfH.parse(retornaHoraCalculo(saida01));

                Date hora33 = sdfH.parse(retornaHoraCalculo(entrada02));
                Date hora44 = sdfH.parse(retornaHoraCalculo(saida02));

                long intervalo1 = hora23.getTime() - hora111.getTime();
                long intervalo22 = hora44.getTime() - hora33.getTime();

                String manha1 = String.format("%02d:%02d", intervalo1 / 3600000, (intervalo1 / 60000) % 60);
                String tarde2 = String.format("%02d:%02d", intervalo22 / 3600000, (intervalo22 / 60000) % 60);

                GregorianCalendar gcc = new GregorianCalendar();
                String v11 = manha1;
                String v222 = tarde2;

                int hora11 = Integer.parseInt(v11.substring(0, 2));
                int min1 = Integer.parseInt(v11.substring(3, 5));
                int seg1 = 0;
                Time time1 = new Time(hora11, min1, seg1);
                gcc.setTimeInMillis(time1.getTime());
                hora11 = Integer.parseInt(v222.substring(0, 2));
                min1 = Integer.parseInt(v222.substring(3, 5));
                gcc.add(Calendar.HOUR, hora11);
                gcc.add(Calendar.MINUTE, min1);

                String resultSetFalta = sdfH.format(gcc.getTime());

                horario.setCarga(resultSetFalta);
            }

        } catch (NumberFormatException | ParseException e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("Operação não realizada " + e);
        }

    }

    public String retornaHoraCalculo(String data) {
        String hora;
        hora = data;
        if (hora == null) {
            hora = "00:00";
        } else {
            return hora;
        }
        return hora;
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
    public String listar() {
        return "/View/horario/lista-horario/index?faces-redirect=true";
    }

    public long getTotalHorario() {
        return totalHorario = horariofacade.contaHorarioTotal();
    }

    public void setTotalHorario(long totalHorario) {
        this.totalHorario = totalHorario;
    }

}
