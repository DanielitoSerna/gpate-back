package com.gpate.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.gpate.model.Contrato;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PDFGenerator {

	private List<Contrato> contratos;

	public List<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(List<Contrato> contratos) {
		this.contratos = contratos;
	}

	public void generate(HttpServletResponse response) throws DocumentException, IOException {

		Document document = new Document(PageSize.A0.rotate());

		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		fontTitle.setSize(14);

		Paragraph paragraph = new Paragraph("Listado de contratos", fontTitle);

		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);

		PdfPTable table = new PdfPTable(23);

		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);

		// Create Table Cells for table header
		PdfPCell cell = new PdfPCell();
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(CMYKColor.BLACK);

		cell.setPhrase(new Phrase("Proyecto", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Folio", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Especialidad", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Proveedor", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Importe contratado", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Anticipo contratado", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Estimaciones programadas", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Estimaciones pagadas", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Pagos aplicados", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Saldo pendiente al contrato", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Centro de costos", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha del fallo", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha de solicitud de contrato", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha programada de entrega", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Días programados", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha que nos entregan jurídico el contrato", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha de entrega firmado por cliente - proveedor", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Observaciones", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Estatus general", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Días de atención", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha de vencimiento del contrato", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Días de vencimiento", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Estado", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Hipervinculo", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Tipo contrato", font));
		table.addCell(cell);

		for (Contrato contrato : contratos) {
			table.addCell(contrato.getProyecto());
			table.addCell(contrato.getFolio());
			table.addCell(contrato.getEspecialidad());
			table.addCell(contrato.getProveedor());
			table.addCell(contrato.getImporteContratado() != null ? "$ " + contrato.getImporteContratado().toString() : "$ 0.00");
			table.addCell(contrato.getAnticipoContratado() != null ? "$ " + contrato.getAnticipoContratado().toString() : "$ 0.00");
			table.addCell(contrato.getEstimacionesProgramadas() != null ? "$ " + contrato.getEstimacionesProgramadas().toString() : "$ 0.00");
			table.addCell(contrato.getEstimacionesPagadas() != null ? "$ " + contrato.getEstimacionesPagadas().toString() : "$ 0.00");
			table.addCell(contrato.getPagosAplicados() != null ? "$ " + contrato.getPagosAplicados().toString() : "$ 0.00");
			table.addCell(contrato.getSaldoPendienteContrato() != null ? "$ " + contrato.getSaldoPendienteContrato().toString() : "$ 0.00");
			table.addCell(contrato.getCentroCosto());
			table.addCell(contrato.getFechaFallo() != null ? DateUtils.convertDateToString(contrato.getFechaFallo()) : "");
			table.addCell(contrato.getFechaSolicitudContrato() != null ? DateUtils.convertDateToString(contrato.getFechaSolicitudContrato()) : "");
			table.addCell(contrato.getFechaProgramadaEntrega() != null ? DateUtils.convertDateToString(contrato.getFechaProgramadaEntrega()) : "");
			table.addCell(contrato.getDiasProgramados() != null ? String.valueOf(contrato.getDiasProgramados()) : "");
			table.addCell(contrato.getFechaJuridico() != null ? DateUtils.convertDateToString(contrato.getFechaJuridico()) : "");
			table.addCell(contrato.getFechaFirmadoCliente() != null ? DateUtils.convertDateToString(contrato.getFechaFirmadoCliente()) : "");
			table.addCell(contrato.getObservaciones());
			table.addCell(contrato.getStatusGeneral());
			table.addCell(contrato.getDiasAtencion() != null ? String.valueOf(contrato.getDiasAtencion()) : "");
			table.addCell(contrato.getFechaVencimientoContrato() != null ? DateUtils.convertDateToString(contrato.getFechaVencimientoContrato()) : "");
			table.addCell(contrato.getDiasVencimiento());
			table.addCell(contrato.getEstado());
			table.addCell(contrato.getHipervinculo() != null ? contrato.getHipervinculo() : "");
			table.addCell(contrato.getTieneImporte() != null ? (contrato.getTieneImporte() ? "I" : "D") : "D");
		}
		document.add(table);

		document.close();
	}

}
