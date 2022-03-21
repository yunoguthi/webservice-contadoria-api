package br.jus.jfsp.nuit.contadoria.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;

public class ManipulaArquivo {

    private static final String FILE_NAME = "/home/rodrigo/dsv/workspace/webservice-contadoria-api/src/main/java/br/jus/jfsp/nuit/contadoria/testes/indices2.csv";

    private static String openFile() {
        String retorno = "";
        try {
            File file=new File(FILE_NAME);
            FileInputStream fis=new FileInputStream(file);
            int r=0;
            while((r=fis.read())!=-1) {
                retorno += (char)r;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    private static String[] getLinhas() {
        String[] linhas = openFile().split("\\n");
        return linhas;
    }

    public static String[] getColuna(int nroColuna) {
        String[] linhas = getLinhas();
        String[] coluna = new String[linhas.length];
        for (int i = 0; i < linhas.length; i++) {
            String[] col = linhas[i].split(";");
            coluna[i] = col[nroColuna].replaceAll("\\.", "").replaceAll(",", ".");
        }
        return coluna;
    }

    public static String[] normalizar(String[] numeros) {
        String[] retorno = new String[numeros.length];
        for (int i = 0; i<numeros.length; i++) {
            Double numeroDouble = new Double(0.0);
            try {
                numeroDouble = new Double(numeros[i]);
            } catch (Exception e) {}
            String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(numeroDouble);
            retorno[i] = valorFormatado;
        }
        return retorno;
    }

    public static void geraArquivo(String nomeArquivo, String[] conteudo) throws IOException {
        OutputStream os = new FileOutputStream(nomeArquivo);
        Writer wr = new OutputStreamWriter(os);
        BufferedWriter br = new BufferedWriter(wr);

        for (int i=0; i < conteudo.length; i++) {
            br.write(conteudo[i]);
            br.newLine();
        }
        br.close();
    }

    public static void main(String[] args) {
        //openFile();
        //getLinhas();
        normalizar(getColuna(11));
//        System.out.println(getColuna(11));
    }

}
