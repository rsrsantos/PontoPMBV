package br.com.Beans;

import br.com.Facade.BancoHorasExtratoFacade;
import br.com.Model.BancoHorasExtrato;
import br.com.Model.Usuario;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class BancoHorasExtratoBean implements Serializable{
    
    FacesContext context = FacesContext.getCurrentInstance();
    Usuario usuarioLogado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
    BancoHorasExtratoFacade extratofacade;
    String hora;
    String item;
    
    BancoHorasExtrato extrato;
    
    public BancoHorasExtratoBean() {
        
        if (extrato == null) {
            extrato = new BancoHorasExtrato();
        }
        
        if (extratofacade == null) {
            extratofacade = new BancoHorasExtratoFacade();
        }
    }
    
    

    //Gets and Setrs
    public BancoHorasExtrato getExtrato() {
        return extrato;
    }
    
    public void setExtrato(BancoHorasExtrato extrato) {
        this.extrato = extrato;
    }
    
    public String getHora() {
        return hora;
    }
    
    public void setHora(String hora) {
        this.hora = hora;
    }
    
    public String getItem() {
        return item;
    }
    
    public void setItem(String item) {
        this.item = item;
    }
    
}
