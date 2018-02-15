package br.com.Model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PtoEquipamentoElgin")
public class PtoEquipamentoElgin implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "descricaoel", length = 50)
    private String descricaoel;

    @Column(name = "Nfabricacaoel", length = 30)
    private String Nfabricacaoel;

    @Column(name = "ipel", length = 20)
    private String ipel;

    @Column(name = "portael")
    private int portael;

    @Column(name = "modeloel")
    private String modeloel;

    @Column(name = "Datainicio")
    private String datainicio;

    @Column(name = "Datafim")
    private String datafim;

    @Column(name = "localidadeel")
    private String localidadeel;
    
    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    
    
    
    
    // Gets and Seters =========================================
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricaoel() {
        return descricaoel;
    }

    public void setDescricaoel(String descricaoel) {
        this.descricaoel = descricaoel;
    }

    public String getNfabricacaoel() {
        return Nfabricacaoel;
    }

    public void setNfabricacaoel(String Nfabricacaoel) {
        this.Nfabricacaoel = Nfabricacaoel;
    }

    public String getIpel() {
        return ipel;
    }

    public void setIpel(String ipel) {
        this.ipel = ipel;
    }

    public int getPortael() {
        return portael;
    }

    public void setPortael(int portael) {
        this.portael = portael;
    }


    public String getModeloel() {
        return modeloel;
    }

    public void setModeloel(String modeloel) {
        this.modeloel = modeloel;
    }

    public String getDatainicio() {
        return datainicio;
    }

    public void setDatainicio(String datainicio) {
        this.datainicio = datainicio;
    }

    public String getDatafim() {
        return datafim;
    }

    public void setDatafim(String datafim) {
        this.datafim = datafim;
    }

  

    public String getLocalidadeel() {
        return localidadeel;
    }

    public void setLocalidadeel(String localidadeel) {
        this.localidadeel = localidadeel;
    }

}
