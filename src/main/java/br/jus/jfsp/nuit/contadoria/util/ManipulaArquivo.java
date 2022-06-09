package br.jus.jfsp.nuit.contadoria.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            //System.out.println(i + " " + numeros[i]);
            Double numeroDouble = new Double(0.0);
            try {
                numeroDouble = new Double(numeros[i]);
            } catch (Exception e) {
                //System.out.println(numeros[i]);
                retorno[i] = numeros[i];
                continue;
            }
            String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(numeroDouble);
            retorno[i] = valorFormatado.replace(".", "");
        }
        return retorno;
    }

    public static void geraArquivo(String nomeArquivo, String[] conteudo) throws IOException {
        OutputStream os = new FileOutputStream(nomeArquivo);
        Writer wr = new OutputStreamWriter(os);
        BufferedWriter br = new BufferedWriter(wr);

        for (int i=0; i < conteudo.length; i++) {
           try {
               br.write(conteudo[i]);
               if (i != conteudo.length-1) {
                   br.newLine();
               }
           } catch (Exception e) {}
        }
        br.close();
    }

    public static Resource download(String filePath, String filename) {
        try {
            Path file = Paths.get(filePath).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        //openFile();
        //getLinhas();
        normalizar(getColuna(11));
//        System.out.println(getColuna(11));
    }

}
