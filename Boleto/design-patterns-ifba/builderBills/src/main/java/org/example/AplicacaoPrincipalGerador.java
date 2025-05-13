package org.example;

// Imports atualizados para os nomes refatorados
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import builder.ConstrutorDocumentoBB;
import builder.ConstrutorDocumentoBradesco;
import builder.ConstrutorDocumentoCobranca;
import builder.ConstrutorDocumentoItau;
import model.DocumentoCobranca;
import pdf.EmissorDocumentoCobrancaPdf;

public class AplicacaoPrincipalGerador {

    public static void main(String[] args) {

        Scanner leitorEntrada = new Scanner(System.in, "UTF-8"); // Especificando UTF-8 para o Scanner

        System.out.println("************************************************************");
        System.out.println("*** Bem-vindo ao Sistema Gerador de Documentos de Cobrança ***");
        System.out.println("************************************************************");
        System.out.println("\nPor favor, escolha a Instituição Financeira para a qual o documento será gerado:");
        System.out.println("  1 - Banco do Brasil");
        System.out.println("  2 - Itaú Unibanco");
        System.out.println("  3 - Bradesco");
        System.out.print("Digite o número da opção e pressione Enter: ");

        int escolhaInstituicao = 0;
        try {
            if (leitorEntrada.hasNextInt()) {
                escolhaInstituicao = leitorEntrada.nextInt();
            } else {
                throw new InputMismatchException(); // Força o catch se não for int
            }
        } catch (InputMismatchException e) {
            System.out.println("\n[ERRO] Entrada inválida. Era esperado um número para a opção.");
            leitorEntrada.close();
            return;
        }
        leitorEntrada.nextLine(); // Limpar buffer do scanner

        ConstrutorDocumentoCobranca construtorDoc = escolherConstrutorDocumento(escolhaInstituicao);
        if (construtorDoc == null) {
            System.out.println("\n[ERRO] A opção de instituição financeira escolhida não é válida.");
            leitorEntrada.close();
            return;
        }

        System.out.println("\n--- Por favor, preencha os dados abaixo para o Documento de Cobrança ---");

        System.out.print("Informe o Nome ou Razão Social do Recebedor (Beneficiário) (texto livre, ex: Empresa XYZ Ltda): ");
        String nomeRec = leitorEntrada.nextLine();
        System.out.print("Digite o CPF (11 dígitos numéricos) ou CNPJ (14 dígitos numéricos) do Recebedor: ");
        String docRec = leitorEntrada.nextLine();
        System.out.print("Insira o Endereço Completo do Recebedor (texto livre, ex: Rua das Palmeiras, 123, Bairro, Cidade - UF, CEP): ");
        String endRec = leitorEntrada.nextLine();

        System.out.print("\nInforme o Nome ou Razão Social do Pagador (Sacado) (texto livre, ex: Cliente ABC): ");
        String nomePag = leitorEntrada.nextLine();
        System.out.print("Digite o CPF (11 dígitos numéricos) ou CNPJ (14 dígitos numéricos) do Pagador: ");
        String docPag = leitorEntrada.nextLine();
        System.out.print("Insira o Endereço Completo do Pagador (texto livre, ex: Av. Principal, 456, Bairro, Cidade - UF, CEP): ");
        String endPag = leitorEntrada.nextLine();

        System.out.print("\nDigite o Número de Identificação do Documento (atribuído pelo Recebedor, ex: NF-001, Contrato-X, max. aprox. 15 caracteres): ");
        String idDocOrig = leitorEntrada.nextLine();

        System.out.print("Insira a Data de Vencimento do Documento (formato dd/MM/yyyy, ex: 31/12/2025): ");
        Date dataLimite = converterTextoParaData(leitorEntrada.nextLine());

        BigDecimal valorMonetario = BigDecimal.ZERO;
        boolean valorValido = false;
        while (!valorValido) {
            System.out.print("Digite o Valor Nominal do Documento (use '.' como separador decimal, ex: 123.45 ou 1500.00): ");
            String valorStr = leitorEntrada.nextLine();
            try {
                valorMonetario = new BigDecimal(valorStr.replace(",", ".")); // Substitui vírgula por ponto para flexibilidade
                if (valorMonetario.compareTo(BigDecimal.ZERO) >= 0) { // Permite zero, mas não negativo
                    valorValido = true;
                } else {
                    System.out.println("[AVISO] O valor informado não pode ser negativo. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Formato monetário inválido. Utilize ponto (.) como separador decimal (ex: 150.75). Tente novamente.");
            }
        }

        System.out.print("\nInsira o Código da Agência do Recebedor (exatamente 4 dígitos numéricos, sem DV, ex: 1234): ");
        String codAgencia = leitorEntrada.nextLine();
        System.out.print("Insira o Número da Conta Corrente do Recebedor (apenas números, sem DV, ex: se 12345-X, informe 12345): ");
        String codConta = leitorEntrada.nextLine();

        // As dicas de tamanho para Carteira e Nosso Número podem ser genéricas
        // ou poderiam ser ajustadas dinamicamente se soubéssemos o banco aqui de forma mais estruturada.
        String dicaCarteira, dicaNossoNumero;
        switch (escolhaInstituicao) {
            case 1: // Banco do Brasil
                dicaCarteira = " (ex: 11, 17 - geralmente 2 dígitos numéricos)";
                dicaNossoNumero = " (ex: Convênio 7 pos: NN até 10 dígitos; Convênio 6 pos: NN até 5 ou 11 dígitos - numérico, sem DV)";
                break;
            case 2: // Itaú
                dicaCarteira = " (ex: 109, 175 - exatamente 3 dígitos numéricos)";
                dicaNossoNumero = " (ex: para este layout, 8 dígitos numéricos, sem DV)";
                break;
            case 3: // Bradesco
                dicaCarteira = " (ex: 09, 26 - geralmente 2 dígitos numéricos)";
                dicaNossoNumero = " (ex: 11 dígitos numéricos, sem DV)";
                break;
            default:
                dicaCarteira = " (verifique o padrão do seu banco)";
                dicaNossoNumero = " (verifique o padrão do seu banco)";
        }

        System.out.print("Digite o Código da Carteira de Cobrança" + dicaCarteira + ": ");
        String idCarteira = leitorEntrada.nextLine();
        System.out.print("Digite o Identificador Nosso Número" + dicaNossoNumero + ": ");
        String idNossoNum = leitorEntrada.nextLine();

        try {
            DocumentoCobranca documentoGerado = construtorDoc
                    .comRecebedor(nomeRec, docRec, endRec)
                    .comPagador(nomePag, docPag, endPag)
                    .comDadosDocumentoOriginador(idDocOrig, new Date()) // Data de emissão é a data atual
                    .comDataLimitePagamento(dataLimite)
                    .comValorNominal(valorMonetario)
                    .comCodigoAgencia(codAgencia)
                    .comCodigoConta(codConta)
                    .comIdCarteiraCobranca(idCarteira)
                    .comIdNossoNumero(idNossoNum)
                    .construir();

            System.out.println("\n****************************************************");
            System.out.println("*** Documento de Cobrança Processado com Sucesso! ***");
            System.out.println("****************************************************");
            System.out.println("Detalhes do Documento para a Instituição: " + obterDescricaoInstituicao(escolhaInstituicao));
            System.out.println("Linha Digitável Gerada: " + documentoGerado.getTextoLinhaDigitavel());
            System.out.println("Sequência Numérica do Código de Barras: " + documentoGerado.getNumeroCodigoBarras());

            System.out.print("\nEmitir o comprovante deste Documento de Cobrança em formato PDF? (Responda S para Sim ou N para Não): ");
            String opcaoGerarPdf = leitorEntrada.nextLine();
            if (opcaoGerarPdf.equalsIgnoreCase("S")) {
                System.out.print("Qual nome você deseja dar ao arquivo PDF (não inclua a extensão '.pdf', ex: cobranca_cliente_xyz): ");
                String nomeBaseArquivo = leitorEntrada.nextLine();
                String nomeCompletoArquivoPdf = nomeBaseArquivo.trim() + ".pdf";
                try {
                    EmissorDocumentoCobrancaPdf.emitirPdf(documentoGerado, nomeCompletoArquivoPdf);
                    System.out.println("\n>>> Arquivo PDF do comprovante foi salvo com sucesso em: " + nomeCompletoArquivoPdf);
                } catch (Exception e) {
                    System.err.println("\n[FALHA GRAVE] Não foi possível gerar o arquivo PDF: " + e.getMessage());
                    // e.printStackTrace(); // Descomente para ver o stack trace completo se necessário
                }
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("\n[ATENÇÃO] Ocorreu um erro durante a geração do documento de cobrança: " + e.getMessage());
        } finally {
            leitorEntrada.close();
            System.out.println("\n--- Geração Concluída ---");
        }
    }

    private static ConstrutorDocumentoCobranca escolherConstrutorDocumento(int opcaoInstituicao) {
        switch (opcaoInstituicao) {
            case 1: return new ConstrutorDocumentoBB();
            case 2: return new ConstrutorDocumentoItau();
            case 3: return new ConstrutorDocumentoBradesco();
            default: return null;
        }
    }

    private static String obterDescricaoInstituicao(int opcaoInstituicao) {
        switch (opcaoInstituicao) {
            case 1: return "Banco do Brasil (001)";
            case 2: return "Itaú Unibanco (341)";
            case 3: return "Bradesco (237)";
            default: return "Instituição Desconhecida";
        }
    }

    private static Date converterTextoParaData(String textoData) {
        try {
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
            formatador.setLenient(false);
            return formatador.parse(textoData);
        } catch (ParseException e) {
            System.out.println("[AVISO] A data '" + textoData + "' não está no formato dd/MM/yyyy. O vencimento será definido para a data de HOJE.");
            return new Date(); // Retorna a data atual como fallback
        }
    }
}