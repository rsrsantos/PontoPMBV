/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ZPM;

/**
 *
 * @author postgres
 */
public class NewClass {

    public static void main(String[] args) {

        String Str = new String("012639466668[DIEGO DA SILVA ALMEIDA[1[1[43162]012713357030[FERNANDO CORREA MACHADO[1[1[847010]");
//Posição do caracter na string
        int pos = Str.indexOf("]0");
//Substring iniciando em 0 até posição do caracter especial
        System.out.println(Str.substring(0, pos));
    }
}
