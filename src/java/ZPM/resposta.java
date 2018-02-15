package ZPM;

import java.awt.event.ActionListener;
import javax.swing.Timer;

public class resposta extends Thread {

    boolean termina;
    ClienteTCP rep;
    Timer timer;

    public resposta(ClienteTCP rep) {
        this.rep = rep;
    }

    @Override
    public void run() {
        String ret = "";

        int i = 0;
        termina = false;

        ActionListener action = new ActionListener() {
            public void actionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent e) {
                System.out.println("timer resposta");
                termina = true;
                timer.stop();
            }
        };
        timer = new Timer(10000, action);

        timer.start();

        while (!termina) {
            System.out.println("i = " + i++);
            String leitura = this.rep.leitura2();

            if (leitura != null) {
                timer.restart();
                ret += leitura + "\n";
                rep.escreve(new byte[]{(byte) 0x06});
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    break;
                }
            }
        }
    }

}
