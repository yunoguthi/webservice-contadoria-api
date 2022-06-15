package br.jus.jfsp.nuit.contadoria.util;

import br.jus.jfsp.nuit.contadoria.exception.DataInvalidaException;
import br.jus.jfsp.nuit.contadoria.models.EMoeda;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class ManipulaMath {

    private static final int LENGTH = 15;

    private static final String NUMBER_FORMAT_14 = "#,##0.00000000000000";

    private static double getDivisor() {
        double number = 1.0;
        for (int i = 0; i < LENGTH-1; i++) {
            number = number * 10;
        }
        return number;
    }

    public static BigDecimal round(BigDecimal number) {
//        if (BigDecimal.isInfinite(number)) {
//            return new BigDecimal(0.0);
//        }
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern("#0.00000000000000");
        String valorFormatado = df.format(number);
        return new BigDecimal(valorFormatado);
    }

//    public static double round(double number) {
//
//        String numberStr = Double.toString(number);
//        String numberStrTemp = "";
//        boolean passouPonto = false;
//        String numberStrTempDepoisVirgula = "";
//        int contDepoisVirgula = 0;
//        int round = 0;
//        for (int i = 0; i < numberStr.length(); i++) {
//            //System.out.println("i " + i);
//            //System.out.println("numberStr.substring " + numberStr.substring(i, i+1));
//            //System.out.println("numberStrTemp " + numberStrTemp);
//            //System.out.println("numberStrTempDepoisVirgula " + numberStrTempDepoisVirgula);
//
//
//
//            if (numberStr.substring(i, i+1).equals(".")) {
//                //System.out.println("ponto");
//                passouPonto = true;
//                //numberStrTemp = numberStrTemp + ".";
//            }
//            if (passouPonto == false) {
//                numberStrTemp = numberStrTemp + numberStr.substring(i, i+1);
//                //System.out.println("numberStrTemp "+numberStrTemp);
//            } else {
//                //System.out.println("contDepoisVirgula "+contDepoisVirgula);
//                if (contDepoisVirgula<LENGTH) {
//                    numberStrTempDepoisVirgula = numberStrTempDepoisVirgula + numberStr.substring(i, i+1);
//                    contDepoisVirgula++;
//                } else if (contDepoisVirgula==LENGTH) {
//                    round = Integer.parseInt(numberStr.substring(i, i+1));
//                    break;
//                }
//            }
//        }
//        if (contDepoisVirgula<LENGTH) {
//            return number;
//        }
//        //System.out.println("numberStrTempDepoisVirgula " + numberStrTempDepoisVirgula);
//        numberStrTempDepoisVirgula = numberStrTempDepoisVirgula.replace(".", "");
//        Long longDepoisDaVirgula = Long.parseLong(numberStrTemp + numberStrTempDepoisVirgula);
//
//        //TODO round up
//        if (round>=5) {
//            longDepoisDaVirgula++;
//        }
//
////        Double retorno = new Double(longDepoisDaVirgula / 10000000000.0);
//        Double retorno = new Double(longDepoisDaVirgula / getDivisor());
//
//        //System.out.println("round " + round);
//
//        //System.out.println("numberStrTemp " + numberStrTemp);
//        //System.out.println("longDepoisDaVirgula " + longDepoisDaVirgula);
//
//        return retorno;
//    }




    public static void main(String[] args) {
//        Double teste = new Double(1.04328213564059);

        BigDecimal teste = new BigDecimal( 1.0116);
        System.out.println("divisor " + getDivisor());

        System.out.println("resultado " + round(teste));
    }


}
