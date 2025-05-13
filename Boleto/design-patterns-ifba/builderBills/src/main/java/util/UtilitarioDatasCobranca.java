package util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UtilitarioDatasCobranca {

    // A constante DATA_BASE_CALENDARIO não era usada no método original,
    // mas mantida aqui caso seja útil para futuras refatorações.
    private static final Calendar DATA_BASE_CALENDARIO = Calendar.getInstance();

    static {
        DATA_BASE_CALENDARIO.set(1997, Calendar.OCTOBER, 7, 0, 0, 0);
        DATA_BASE_CALENDARIO.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Calcula o fator de vencimento a partir da data base de 07/10/1997.
     * @param dataLimitePagamento A data de vencimento do documento.
     * @return String com 4 dígitos representando o fator de vencimento.
     */
    public static String obterFatorVencimento(Date dataLimitePagamento) {
        if (dataLimitePagamento == null) {
            // Alguns manuais especificam que um boleto sem vencimento deve usar fator 0000.
            // Ou pode-se optar por lançar uma exceção se a data for obrigatória.
            return "0000";
        }

        Calendar dataBaseCal = new GregorianCalendar(1997, Calendar.OCTOBER, 7, 0, 0, 0);
        dataBaseCal.set(Calendar.MILLISECOND, 0);
        long dataBaseMillis = dataBaseCal.getTimeInMillis();

        Calendar dataVencCal = Calendar.getInstance();
        dataVencCal.setTime(dataLimitePagamento);
        dataVencCal.set(Calendar.HOUR_OF_DAY, 0);
        dataVencCal.set(Calendar.MINUTE, 0);
        dataVencCal.set(Calendar.SECOND, 0);
        dataVencCal.set(Calendar.MILLISECOND, 0);
        long dataVencMillis = dataVencCal.getTimeInMillis();

        long diferencaEmMillis = dataVencMillis - dataBaseMillis;
        long diferencaEmDias = diferencaEmMillis / (24 * 60 * 60 * 1000);

        // Limita a 4 dígitos (0-9999) conforme especificação original do fator.
        // Para datas > 03/07/2022 (fator > 9999), a FEBRABAN introduziu nova regra de cálculo.
        // Esta implementação segue a lógica original de truncamento em 9999.
        // Para conformidade com regras mais recentes, este trecho precisaria ser atualizado.
        diferencaEmDias = Math.max(0, Math.min(diferencaEmDias, 9999));
        return String.format("%04d", diferencaEmDias);
    }
}