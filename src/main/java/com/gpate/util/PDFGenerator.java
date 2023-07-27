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

		Document document = new Document(PageSize.A1.rotate());

		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		fontTitle.setSize(14);

		Paragraph paragraph = new Paragraph("Listado de contratos", fontTitle);

		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);

		PdfPTable table = new PdfPTable(22);

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

		for (Contrato contrato : contratos) {
			table.addCell(contrato.getProyecto());
			table.addCell(contrato.getFolio());
			table.addCell(contrato.getEspecialidad());
			table.addCell(contrato.getProveedor());
			table.addCell("$ " + contrato.getImporteContratado().toString());
			table.addCell("$ " + contrato.getAnticipoContratado().toString());
			table.addCell("$ " + contrato.getEstimacionesPagadas().toString());
			table.addCell("$ " + contrato.getEstimacionesPagadas().toString());
			table.addCell("$ " + contrato.getPagosAplicados().toString());
			table.addCell("$ " + contrato.getSaldoPendienteContrato().toString());
			table.addCell(contrato.getCentroCosto());
			table.addCell(DateUtils.convertDateToString(contrato.getFechaFallo()));
			table.addCell(DateUtils.convertDateToString(contrato.getFechaSolicitudContrato()));
			table.addCell(DateUtils.convertDateToString(contrato.getFechaProgramadaEntrega()));
			table.addCell(String.valueOf(contrato.getDiasProgramados()));
			table.addCell(DateUtils.convertDateToString(contrato.getFechaJuridico()));
			table.addCell(DateUtils.convertDateToString(contrato.getFechaFirmadoCliente()));
			table.addCell(contrato.getObservaciones());
			table.addCell(contrato.getStatusGeneral());
			table.addCell(String.valueOf(contrato.getDiasAtencion()));
			table.addCell(DateUtils.convertDateToString(contrato.getFechaVencimientoContrato()));
			table.addCell(contrato.getDiasVencimiento());
		}
		document.add(table);

		document.close();
	}

}
