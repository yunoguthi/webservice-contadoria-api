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
        try
        {
            File file=new File(FILE_NAME);
            FileInputStream fis=new FileInputStream(file);     //opens a connection to an actual file
            System.out.println("file content: ");
            int r=0;
            while((r=fis.read())!=-1)
            {
                retorno += (char)r;
                //System.out.print((char)r);      //prints the content of the file
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

//        retorno = retorno.replaceAll("\"(\\d+)\\.(\\d+,\\d+)\"", "$1$2");
//        retorno = retorno.replaceAll("\"(\\d+),(\\d+)\"", "$1.$2");
//        return retorno.replaceAll("(\\d+),(\\d+)", "$1.$2");
        return retorno;
    }

    private static String[] getLinhas() {
        String[] linhas = openFile().split("\\n");
        for (int i = 0; i < linhas.length; i++) {
            // System.out.println(linhas[i]);
        }
        return linhas;
    }

    public static String[] getColuna(int nroColuna) {
        String[] linhas = getLinhas();
        String[] coluna = new String[linhas.length];
        for (int i = 0; i < linhas.length; i++) {
            String[] col = linhas[i].split(";");
            coluna[i] = col[nroColuna].replaceAll("\\.", "").replaceAll(",", ".");
            System.out.println(coluna[i]);
        }
        return coluna;
    }

    public static String[] normalizar(String[] numeros) {
        String[] retorno = new String[numeros.length];
        for (int i = 0; i<numeros.length; i++) {
            Double numeroDouble = new Double(numeros[i]);
            String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(numeroDouble);
            retorno[i] = valorFormatado;
            System.out.println( valorFormatado );
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
