package pdf;

// Imports iText (mantidos e adicionado PdfPCell)
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DocumentoCobranca;


public class EmissorDocumentoCobrancaPdf {

    private static final Font FONTE_TITULO_DOC = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static final Font FONTE_ROTULO_CAMPO = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
    private static final Font FONTE_VALOR_CAMPO = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
    private static final Font FONTE_LINHA_DIGITAVEL = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);
    private static final Font FONTE_CODIGO_BARRAS_TEXTO = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);


    public static void emitirPdf(DocumentoCobranca docCobranca, String nomeArquivoCompleto) throws DocumentException, IOException {
        Document documentoPdf = new Document(PageSize.A4, 30, 30, 30, 30);
        PdfWriter escritorPdf = PdfWriter.getInstance(documentoPdf, new FileOutputStream(nomeArquivoCompleto));

        documentoPdf.open();

        Paragraph paragrafoTituloDoc = new Paragraph("COMPROVANTE DE COBRANÇA", FONTE_TITULO_DOC);
        paragrafoTituloDoc.setAlignment(Element.ALIGN_CENTER);
        paragrafoTituloDoc.setSpacingAfter(20f);
        documentoPdf.add(paragrafoTituloDoc);

        PdfPTable tabelaPrincipal = new PdfPTable(2);
        tabelaPrincipal.setWidthPercentage(100);
        tabelaPrincipal.setWidths(new float[]{1.5f, 3.5f});
        tabelaPrincipal.setSpacingAfter(10f);

        SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        adicionarCelulaTabela(tabelaPrincipal, "Instituição Emissora:", obterNomeInstituicao(docCobranca.getCodigoBancoInstituicao()));
        adicionarCelulaTabela(tabelaPrincipal, "Data Limite Pagamento:", formatadorData.format(docCobranca.getDataLimitePagamento()));
        adicionarCelulaTabela(tabelaPrincipal, "Valor Nominal:", formatadorMoeda.format(docCobranca.getValorNominal()));
        adicionarCelulaTabela(tabelaPrincipal, "Nº Documento (Originador):", docCobranca.getIdDocumentoOriginador());
        adicionarCelulaTabela(tabelaPrincipal, "Nosso Número:", docCobranca.getIdNossoNumero());
        adicionarCelulaTabela(tabelaPrincipal, "Agência/Código Recebedor:",
                docCobranca.getCodigoAgencia() + " / " + docCobranca.getCodigoConta());

        adicionarCelulaTabelaComSpan(tabelaPrincipal, "RECEBEDOR (BENEFICIÁRIO)", FONTE_ROTULO_CAMPO);
        adicionarCelulaTabela(tabelaPrincipal, "Nome/Razão Social:", docCobranca.getDadosRecebedor().getNomeCompleto());
        adicionarCelulaTabela(tabelaPrincipal, "CPF/CNPJ:", docCobranca.getDadosRecebedor().getIdentificacaoOficial());
        adicionarCelulaTabela(tabelaPrincipal, "Endereço:", docCobranca.getDadosRecebedor().getLogradouroCompleto());

        adicionarCelulaTabelaComSpan(tabelaPrincipal, "PAGADOR (SACADO)", FONTE_ROTULO_CAMPO);
        adicionarCelulaTabela(tabelaPrincipal, "Nome/Razão Social:", docCobranca.getDadosPagador().getNomeCompleto());
        adicionarCelulaTabela(tabelaPrincipal, "CPF/CNPJ:", docCobranca.getDadosPagador().getIdentificacaoOficial());
        adicionarCelulaTabela(tabelaPrincipal, "Endereço:", docCobranca.getDadosPagador().getLogradouroCompleto());

        documentoPdf.add(tabelaPrincipal);

        Paragraph paragrafoLinhaDig = new Paragraph("Linha Digitável: " +
                formatarTextoLinhaDigitavel(docCobranca.getTextoLinhaDigitavel()), FONTE_LINHA_DIGITAVEL);
        paragrafoLinhaDig.setAlignment(Element.ALIGN_CENTER);
        paragrafoLinhaDig.setSpacingAfter(5f);
        documentoPdf.add(paragrafoLinhaDig);

        // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        // MODIFICAÇÃO: Usar o NÚMERO DO CÓDIGO DE BARRAS para gerar a imagem
        if (docCobranca.getNumeroCodigoBarras() != null && !docCobranca.getNumeroCodigoBarras().isEmpty()) {
            Image imagemCodigoBarras = criarImagemCodigoBarras128(docCobranca.getNumeroCodigoBarras(), escritorPdf); // <<< ALTERADO AQUI
            if (imagemCodigoBarras != null) {
                imagemCodigoBarras.scalePercent(80);
                imagemCodigoBarras.setAlignment(Element.ALIGN_CENTER);
                documentoPdf.add(imagemCodigoBarras);
            }
        }
        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        Paragraph paragrafoNumCodBarras = new Paragraph("Representação Numérica Cód. Barras: " + docCobranca.getNumeroCodigoBarras(), FONTE_CODIGO_BARRAS_TEXTO);
        paragrafoNumCodBarras.setAlignment(Element.ALIGN_CENTER);
        paragrafoNumCodBarras.setSpacingBefore(5f);
        documentoPdf.add(paragrafoNumCodBarras);

        documentoPdf.close();
    }

    private static String obterNomeInstituicao(String codigoBancoInst) {
        // (Implementação mantida)
        switch (codigoBancoInst) {
            case "001": return "Banco do Brasil S.A.";
            case "341": return "Itaú Unibanco S.A.";
            case "237": return "Banco Bradesco S.A.";
            default:    return "Instituição Desconhecida (" + codigoBancoInst + ")";
        }
    }

    private static String formatarTextoLinhaDigitavel(String linhaOriginal) {
        // (Implementação mantida)
        if (linhaOriginal == null || linhaOriginal.length() < 47 || linhaOriginal.startsWith("LINHA DIGITAVEL PENDENTE")) {
            return linhaOriginal;
        }
        return linhaOriginal.substring(0, 5) + "." + linhaOriginal.substring(5, 10) + "  " +
               linhaOriginal.substring(10, 15) + "." + linhaOriginal.substring(15, 21) + "  " +
               linhaOriginal.substring(21, 26) + "." + linhaOriginal.substring(26, 32) + "  " +
               linhaOriginal.substring(32, 33) + "  " +
               linhaOriginal.substring(33);
    }

    private static void adicionarCelulaTabela(PdfPTable tabelaAlvo, String textoRotulo, String textoValor) {
        // (Implementação mantida)
        Phrase rotulo = new Phrase(textoRotulo, FONTE_ROTULO_CAMPO);
        Phrase valor = new Phrase(textoValor, FONTE_VALOR_CAMPO);
        tabelaAlvo.addCell(rotulo);
        tabelaAlvo.addCell(valor);
    }

    private static void adicionarCelulaTabelaComSpan(PdfPTable tabelaAlvo, String texto, Font fonte) {
        // (Implementação mantida)
        PdfPCell celula = new PdfPCell(new Phrase(texto, fonte));
        celula.setColspan(2);
        celula.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula.setPaddingTop(8f);
        celula.setPaddingBottom(4f);
        celula.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabelaAlvo.addCell(celula);
    }

    public static Image criarImagemCodigoBarras128(String dadosParaCodigo, PdfWriter escritorPdf) {
        // (Implementação mantida - o 'dadosParaCodigo' agora será o número do código de barras)
        // O replaceAll("[^0-9]", "") no código original não é mais estritamente necessário se 'dadosParaCodigo'
        // for o código de barras numérico de 44 dígitos, mas não prejudica.
        if (dadosParaCodigo == null || dadosParaCodigo.trim().isEmpty()) return null;
        String dadosNumericos = dadosParaCodigo.replaceAll("[^0-9]", ""); // Garante que apenas dígitos sejam usados
        if(dadosNumericos.length() != 44 && dadosNumericos.length() != 0) { // Permite string vazia, mas se não for, deve ter 44
             System.err.println("Alerta: Dados para código de barras após limpeza não têm 44 dígitos: " + dadosNumericos);
             // Poderia retornar null ou tentar gerar mesmo assim, dependendo da robustez desejada.
             // Para Code128, não é estritamente necessário ter 44 dígitos, mas para um boleto é o esperado.
        }

        try {
            Barcode128 codigo128 = new Barcode128();
            codigo128.setCodeType(Barcode128.CODE128);
            codigo128.setCode(dadosNumericos); // Usa os dados numéricos limpos
            codigo128.setFont(null);
            codigo128.setBarHeight(40f);
            codigo128.setX(0.8f);

            return codigo128.createImageWithBarcode(escritorPdf.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);
        } catch (Exception e) {
            System.err.println("Erro ao criar imagem do código de barras: " + e.getMessage());
            return null;
        }
    }
}