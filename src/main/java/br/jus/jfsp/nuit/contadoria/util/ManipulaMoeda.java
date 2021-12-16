package br.jus.jfsp.nuit.contadoria.util;

import br.jus.jfsp.nuit.contadoria.exception.DataInvalidaException;
import br.jus.jfsp.nuit.contadoria.models.EMoeda;

import java.util.Calendar;

public class ManipulaMoeda {

    private static Calendar cruzeiro = Calendar.getInstance();
    private static Calendar cruzeiroCorteCentavos = Calendar.getInstance();
    private static Calendar cruzeiroNovo = Calendar.getInstance();
    private static Calendar cruzeiro2 = Calendar.getInstance();
    private static Calendar cruzeiro2CorteCentavos = Calendar.getInstance();
    private static Calendar cruzado = Calendar.getInstance();
    private static Calendar cruzadoNovo = Calendar.getInstance();
    private static Calendar cruzeiro3 = Calendar.getInstance();
    private static Calendar cruzeiroReal = Calendar.getInstance();
    private static Calendar urv = Calendar.getInstance();
    private static Calendar real = Calendar.getInstance();

    private static void iniciaMoedas() {
        cruzeiro.set(Calendar.YEAR, 1964);
        cruzeiro.set(Calendar.MONTH, 10+1);
        cruzeiro.set(Calendar.DAY_OF_MONTH, 1);

        cruzeiroCorteCentavos.set(Calendar.YEAR, 1964);
        cruzeiroCorteCentavos.set(Calendar.MONTH, 12+1);
        cruzeiroCorteCentavos.set(Calendar.DAY_OF_MONTH, 1);

        cruzeiroNovo.set(Calendar.YEAR, 1967);
        cruzeiroNovo.set(Calendar.MONTH, 2+1);
        cruzeiroNovo.set(Calendar.DAY_OF_MONTH, 1);

        cruzeiro2.set(Calendar.YEAR, 1970);
        cruzeiro2.set(Calendar.MONTH, 5+1);
        cruzeiro2.set(Calendar.DAY_OF_MONTH, 1);

        cruzeiro2CorteCentavos.set(Calendar.YEAR, 1984);
        cruzeiro2CorteCentavos.set(Calendar.MONTH, 8+1);
        cruzeiro2CorteCentavos.set(Calendar.DAY_OF_MONTH, 1);

        cruzado.set(Calendar.YEAR, 1986);
        cruzado.set(Calendar.MONTH, 3+1);
        cruzado.set(Calendar.DAY_OF_MONTH, 1);

        cruzadoNovo.set(Calendar.YEAR, 1989);
        cruzadoNovo.set(Calendar.MONTH, 2+1);
        cruzadoNovo.set(Calendar.DAY_OF_MONTH, 1);

        cruzeiro3.set(Calendar.YEAR, 1990);
        cruzeiro3.set(Calendar.MONTH, 4+1);
        cruzeiro3.set(Calendar.DAY_OF_MONTH, 1);

        cruzeiroReal.set(Calendar.YEAR, 1993);
        cruzeiroReal.set(Calendar.MONTH, 8+1);
        cruzeiroReal.set(Calendar.DAY_OF_MONTH, 1);

        urv.set(Calendar.YEAR, 1994);
        urv.set(Calendar.MONTH, 3+1);
        urv.set(Calendar.DAY_OF_MONTH, 1);

        real.set(Calendar.YEAR, 1994);
        real.set(Calendar.MONTH, 7+1);
        real.set(Calendar.DAY_OF_MONTH, 1);

    }

    public static EMoeda getMoedaCorrente(Calendar calendar) throws DataInvalidaException {
        iniciaMoedas();
        if ((calendar.after(cruzeiro) && calendar.before(cruzeiroCorteCentavos))) {
            return EMoeda.CRUZEIRO;
        } else if ((calendar.after(cruzeiroCorteCentavos) && calendar.before(cruzeiroNovo))) {
            return EMoeda.CRUZEIRO_CORTE_CENTAVOS;
        } else if ((calendar.after(cruzeiroNovo) && calendar.before(cruzeiro2))) {
            return EMoeda.CRUZEIRO_NOVO;
        } else if ((calendar.after(cruzeiro2) && calendar.before(cruzeiro2CorteCentavos))) {
            return EMoeda.CRUZEIRO2;
        } else if ((calendar.after(cruzeiro2CorteCentavos) && calendar.before(cruzado))) {
            return EMoeda.CRUZEIRO2_CORTE_CENTAVOS;
        } else if ((calendar.after(cruzado) && calendar.before(cruzadoNovo))) {
            return EMoeda.CRUZADO;
        } else if ((calendar.after(cruzadoNovo) && calendar.before(cruzeiro3))) {
            return EMoeda.CRUZADO_NOVO;
        } else if ((calendar.after(cruzeiro3) && calendar.before(cruzeiroReal))) {
            return EMoeda.CRUZEIRO3;
        } else if ((calendar.after(cruzeiroReal) && calendar.before(urv))) {
            return EMoeda.CRUZEIRO_REAL;
        }  else if (calendar.compareTo(urv)==0 || (calendar.after(urv) && calendar.before(real))) {
            return EMoeda.URV;
        } else if (calendar.after(real)) {
            return EMoeda.REAL;
        }
        throw new DataInvalidaException();

    }



}
