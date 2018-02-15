package br.com.Beans;

import br.com.Facade.FeriadoFacade;
import br.com.DAO.GenericDAO;
import br.com.Model.Departamento;
import br.com.Model.EspelhoPonto;
import br.com.Model.Feriado;
import br.com.Model.Usuario;
import br.com.utils.Message.Msg;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class FeriadoBean implements Serializable{
    
    Feriado feriado;
    FeriadoFacade feriadofacade;
    Usuario usuario;
    
    private long totalFeriados;
    
    public FeriadoBean() {
        
        if (feriado == null) {
            feriado = new Feriado();
        }
        
        if (feriadofacade == null) {
            feriadofacade = new FeriadoFacade();
        }
        
        if (usuario == null) {
            usuario = new Usuario();
        }
    }
    
    public List<Feriado> getFeriados() {
        return new GenericDAO<>(Feriado.class).listaTodos();
    }
    
    public List<Departamento> getDepartamentos() {
        return new GenericDAO<>(Departamento.class).listaTodos();
    }
    
    public String gravarFeriado() {
        try {
            
            Feriado verificaFeriado = feriadofacade.buscaFeriado(feriado.getNome());
            
            if (verificaFeriado != null) {
                Msg.addMsgWarn("Feriado já Cadastrado");
                
            } else {
                
                if (feriado.getId() == 0) {
                    feriadofacade.adiciona(feriado);
                    Msg.addMsgInfo("Cadastro realizado com sucesso! ");
                    
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);
                    
                    return "/View/feriado/lista-feriado/index?faces-redirect=true";
                    
                } else {
                    feriadofacade.atualiza(feriado);
                    Msg.addMsgInfo("Cadastro atualizado com sucesso! ");
                    
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getFlash().setKeepMessages(true);
                    
                    return "/View/feriado/lista-feriado/index?faces-redirect=true";
                    
                }
            }
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
        }
        return null;
    }
    
    public String excluir(Feriado feriado) {
        try {
            
            usuario = pegaUsuario();
            
            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                
                feriadofacade.remove(feriado);
                Msg.addMsgFatal("Excluido com Sucesso: " + feriado.getNome());
                
                return "/View/feriado/lista-feriado/index?faces-redirect=true";
                
            } else {
                
                System.err.println("Somente administrador");
                Msg.addMsgFatal("Somente administrador!");
            }
            
        } catch (Exception e) {
            Msg.addMsgError("Operação não realizada!");
            System.err.println("ERRO: " + e);
        }
        return null;
    }
    
    public void lancaFeriado(Feriado feriado) {
        
        try {
            List<Departamento> retornoEspelhodepartamento = feriado.getDepartamento();
            for (Departamento listdepartamento : retornoEspelhodepartamento) {
                
                List<EspelhoPonto> retornoEspelho = feriadofacade.buscaBatidasByDate(feriado.getDataferiado(), listdepartamento.getDescricao());
                
                for (EspelhoPonto espelho : retornoEspelho) {
                    
                    espelho.setEntrada01("Feriado");
                    espelho.setSaida01("Feriado");
                    espelho.setEntrada02("Feriado");
                    espelho.setSaida02("Feriado");
                    espelho.setAbonar(true);
                    espelho.setFeriado(true);
                    
                    new GenericDAO<>(EspelhoPonto.class).atualiza(espelho);
                }
            }
            Msg.addMsgInfo("Feriado Lançado: ");
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            Msg.addMsgWarn("ERRO: " + e);
        }
        
    }
    
    public String editar(Feriado feriado) {
        this.feriado = feriado;
        return "/View/feriado/cadastrar-feriado/index";
    }
    
    public String novoFeriado(Feriado feriado) {
        
        try {
            
            usuario = pegaUsuario();
            feriado = new Feriado();
            if (usuario.getUsuarioPerfil().getDescricao().equalsIgnoreCase("admin")) {
                
                return "/View/feriado/cadastrar-feriado/index?faces-redirect=true";
                
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
    
    public String listar() {
        return "/View/feriado/lista-feriado/index?faces-redirect=true";
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
    public Feriado getFeriado() {
        return feriado;
    }
    
    public void setFeriado(Feriado feriado) {
        this.feriado = feriado;
    }
    
    public long getTotalFeriados() {
        totalFeriados = feriadofacade.contaFeriadoTotal();
        return totalFeriados;
    }
    
    public void setTotalFeriados(long totalFeriados) {
        this.totalFeriados = totalFeriados;
    }
    
}
