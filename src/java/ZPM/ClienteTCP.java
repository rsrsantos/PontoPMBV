package ZPM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.poi.hssf.record.PageBreakRecord;
import org.apache.poi.hssf.record.PageBreakRecord.Break;

public class ClienteTCP {

  

    Socket socket = null;
    OutputStream os = null;
    InputStream is = null;
    byte[] fim = new byte[]{(byte) 0x0d, (byte) 0x0a};
    private boolean debug = false;
    String ips;


    public boolean conecta(String ip,int porta) {

        boolean resultado = false;

        try {

            socket = new Socket(ip, porta);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            socket.setSoTimeout(2000);
            resultado = true;
        } catch (UnknownHostException e) {
        } catch (IOException e) {
        }

        return resultado;
    }

    public boolean escreve(byte[] dados) {

        boolean resultado = false;

        try {
            if (isDebug()) {
            }
            os.write(dados);
            os.flush();
            resultado = true;
        } catch (IOException e) {
        } catch (NullPointerException ex) {
            System.out.println("Erro de conexão ao enviar dados!\n" );
        }

        return resultado;
    }

    public String leitura2() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            char[] inputChars = new char[1500];
            int charsRead = 0;

            if ((charsRead = isr.read(inputChars)) != -1) {
                if (debug) {
//                    System.out.println("Chars read from stream 01: " + charsRead);
                }

                if (isDebug()) {
//                    System.out.println(new String(inputChars));
                }
//                System.out.flush();

            }

            if (inputChars != null) {
                String ret = "";
                String temp = "";
                for (char inputChar : inputChars) {
                    if (inputChar == ' ' || inputChar == ' ') {
                        temp += inputChar;
                    } else {
                        ret += temp + inputChar;
                        temp = "";
                    }
                }
                return ret;
            }
        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
        }

        return null;
    }

    public byte[] leituraByte() {
        String codigoBarras = null;
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        try {
            char[] inputChars = new char[1500];
            int charsRead = 0;

            if ((charsRead = isr.read(inputChars)) != -1) {
                if (debug) {
//                    System.out.println("Chars read from stream - : " + charsRead);
                }

                if (isDebug()) {
//                    System.out.println(new String(inputChars));
                }
//                System.out.flush();
            }

            byte[] b = new byte[inputChars.length];
            for (int i = 0; i < b.length; i++) {
                b[i] = (byte) inputChars[i];
            }
            return b;

//
//            codigoBarras = br.readLine();
        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
        }

        return null;
    }

    public void desconecta() {
        try {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
