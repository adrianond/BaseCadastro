package br.com.basecadastro.report.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;
@SuppressWarnings("deprecation")
@Component
public class ReportUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String UNDERLINE = "_";
	private static final String FOLDER_RELATORIOS = "/relatorios";
	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";
	private static final String EXTENSION_ODS = "ods";
	private static final String EXTENSION_XLS = "xls";
	private static final String EXTENSION_HTML = "html";
	private static final String EXTENSION_PDF = "pdf";
	private String SEPARATOR = File.separator;
	private static final int RELATORIO_PDF = 1;
	private static final int RELATORIO_EXCEL = 2;
	private static final int RELATORIO_HTML = 3;
	private static final int RELATORIO_PLANILHA_OPEN_OFFICE = 4;
	private static final String PONTO = ".";
	private StreamedContent arquivoRetorno = null;
	private String caminhoArquivoRelatorio = null;
	@SuppressWarnings("rawtypes")
	private JRExporter tipoArquivoExportado = null;
	private String extensaoArquivoExportado = "";
	private String caminhoSubreport_Dir = "";
	private File arquivoGerado = null;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public StreamedContent geraRelatorio(List<?> listDataBeanColletionReport, HashMap parametrosRelatorio, 
			String nomeRelatorioJasper, String nomeRelatorioSaida, int tipoRelatorio) throws JRException, FileNotFoundException {
		
		/*Cria a lista de collectionDataSource de beans que carregam os dados para o relatorio*/
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDataBeanColletionReport);

		/*
		 * Fornece o caminho fisico ate a pasta que contem os relatorios
		 * compilador .jasper
		 */
		FacesContext context = FacesContext.getCurrentInstance();
	    context.responseComplete();
		ServletContext scontext = (ServletContext) context.getExternalContext().getContext();

		String caminhoRelatorio = scontext.getRealPath(FOLDER_RELATORIOS);
		

		File file = new File(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper
				+ PONTO + "jasper");

		if (caminhoRelatorio == null
				|| (caminhoRelatorio != null && caminhoRelatorio.isEmpty())
				|| !file.exists()) {
			caminhoRelatorio = this.getClass().getResource(FOLDER_RELATORIOS).getPath();
			SEPARATOR = "";
		}	
		
		/*caminho para imgens*/
		parametrosRelatorio.put("REPORT_PARAMETERS_IMG", caminhoRelatorio); 

		/* caminho completo ate o relatorio compilado indicado */
		String caminhoArquivoJasper = caminhoRelatorio +  SEPARATOR 
				+ nomeRelatorioJasper + PONTO + "jasper";

		/* Faz o carregamento do relatorio indicado. */
		JasperReport relatorioJasper = (JasperReport) JRLoader
				.loadObjectFromFile(caminhoArquivoJasper);

		/* Seta parametro SUBREPORT_DIR com o caminho fisico para sub-reports. */
		caminhoSubreport_Dir = caminhoRelatorio + SEPARATOR ;
		parametrosRelatorio.put(SUBREPORT_DIR, caminhoSubreport_Dir);

		/* Carrega o arquivo compilado para a memoria. */
		JasperPrint impressoraJasper = JasperFillManager.fillReport(
				relatorioJasper, parametrosRelatorio, jrbcds);

		getTipoRelatorio(tipoRelatorio, tipoArquivoExportado, extensaoArquivoExportado);

		nomeRelatorioSaida += UNDERLINE + DateUtils.getDateAtualReportName();

		/* Caminho relatorio exportado */
		caminhoArquivoRelatorio = caminhoRelatorio + SEPARATOR
				+ nomeRelatorioSaida + PONTO + extensaoArquivoExportado;

		/* Cria novo File exportado */
		arquivoGerado = new File(caminhoArquivoRelatorio);

		/* Prepara a impressao */
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT,
				impressoraJasper);

		/* Nome do arquivo fisico a ser impresso/exportado */
		tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE,
				arquivoGerado);

		/* Executa a exportacao */
		tipoArquivoExportado.exportReport();

		/* Remove o arquivo do servidor apos ser feito o download pelo usuario */
		arquivoGerado.deleteOnExit();
		
		return retornarInputStreamGerado(nomeRelatorioSaida, arquivoGerado, extensaoArquivoExportado);
	}

	private StreamedContent retornarInputStreamGerado(String nomeRelatorioSaida, File arquivoGerado, 
			String extensaoArquivoExportado) throws FileNotFoundException {
		
		/* Cria o InputStream para ser usado para pelo PrimeFaces */
		InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);
		
		/* Faz o retorno para a aplicacao. */
		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio,
				"application/" + extensaoArquivoExportado, nomeRelatorioSaida
						+ PONTO + extensaoArquivoExportado);
		return arquivoRetorno;
	}

	private void getTipoRelatorio(int tipoRelatorio, JRExporter tipoArquivoExportado, String extensaoArquivoExportado) {
		switch (tipoRelatorio) {/* Realiza a exportacao para o tipo indicado. */
		case ReportUtil.RELATORIO_PDF:
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSION_PDF;
			break;
		case ReportUtil.RELATORIO_HTML:
			tipoArquivoExportado = new JRHtmlExporter();
			extensaoArquivoExportado = EXTENSION_HTML;
			break;
		case ReportUtil.RELATORIO_EXCEL:
			tipoArquivoExportado = new JRXlsExporter();
			extensaoArquivoExportado = EXTENSION_XLS;
			break;
		case ReportUtil.RELATORIO_PLANILHA_OPEN_OFFICE:
			tipoArquivoExportado = new JROdtExporter();
			extensaoArquivoExportado = EXTENSION_ODS;
			break;
		default:
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSION_PDF;
			break;
		}
	}

}
