package com.gpate.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gpate.model.Contrato;

public class ExcelGenerator {

	private List<Contrato> contratos;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public ExcelGenerator(List<Contrato> contratos) {
		this.contratos = contratos;
		workbook = new XSSFWorkbook();
	}

	private void writeHeader() {
		sheet = workbook.createSheet("Student");
		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);
		createCell(row, 0, "Proyecto", style);
		createCell(row, 1, "Folio", style);
		createCell(row, 2, "Especialidad", style);
		createCell(row, 3, "Proveedor", style);
		createCell(row, 4, "Importe contratado", style);
		createCell(row, 5, "Anticipo contratado", style);
		createCell(row, 6, "Estimaciones programadas", style);
		createCell(row, 7, "Estimaciones pagadas", style);
		createCell(row, 8, "Pagos aplicados", style);
		createCell(row, 9, "Saldo pendiente al contrato", style);
		createCell(row, 10, "Centro de costo", style);
		createCell(row, 11, "Fecha del fallo", style);
		createCell(row, 12, "Fecha de solicitud de contrato", style);
		createCell(row, 13, "Fecha programada de entrega", style);
		createCell(row, 14, "Días programados", style);
		createCell(row, 15, "Fecha que nos entregan jurídico el contrato", style);
		createCell(row, 16, "Fecha de entrega firmado por cliente - proveedor", style);
		createCell(row, 17, "Observaciones", style);
		createCell(row, 18, "Estatus general", style);
		createCell(row, 19, "Días de atención", style);
		createCell(row, 20, "Fecha de vencimiento del contrato", style);
		createCell(row, 21, "Días de vencimiento", style);
	}

	private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (valueOfCell instanceof Integer) {
			cell.setCellValue((Integer) valueOfCell);
		} else if (valueOfCell instanceof Long) {
			cell.setCellValue((Long) valueOfCell);
		} else if (valueOfCell instanceof String) {
			cell.setCellValue((String) valueOfCell);
		} else {
			cell.setCellValue((Boolean) valueOfCell);
		}
		cell.setCellStyle(style);
	}

	private void write() {
		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		for (Contrato contrato : contratos) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, contrato.getProyecto(), style);
			createCell(row, columnCount++, contrato.getFolio(), style);
			createCell(row, columnCount++, contrato.getEspecialidad(), style);
			createCell(row, columnCount++, contrato.getProveedor(), style);
			createCell(row, columnCount++, contrato.getImporteContratado().toString(), style);
			createCell(row, columnCount++, contrato.getAnticipoContratado().toString(), style);
			createCell(row, columnCount++, contrato.getEstimacionesProgramadas().toString(), style);
			createCell(row, columnCount++, contrato.getEstimacionesPagadas().toString(), style);
			createCell(row, columnCount++, contrato.getPagosAplicados().toString(), style);
			createCell(row, columnCount++, contrato.getSaldoPendienteContrato().toString(), style);
			createCell(row, columnCount++, contrato.getCentroCosto(), style);
			createCell(row, columnCount++, contrato.getFechaFallo().toString(), style);
			createCell(row, columnCount++, contrato.getFechaSolicitudContrato().toString(), style);
			createCell(row, columnCount++, contrato.getFechaProgramadaEntrega().toString(), style);
			createCell(row, columnCount++, contrato.getDiasProgramados(), style);
			createCell(row, columnCount++, contrato.getFechaJuridico().toString(), style);
			createCell(row, columnCount++, contrato.getFechaFirmadoCliente().toString(), style);
			createCell(row, columnCount++, contrato.getObservaciones(), style);
			createCell(row, columnCount++, contrato.getStatusGeneral(), style);
			createCell(row, columnCount++, contrato.getDiasAtencion(), style);
			createCell(row, columnCount++, contrato.getFechaVencimientoContrato().toString(), style);
			createCell(row, columnCount++, contrato.getDiasVencimiento(), style);
		}
	}

	public void generateExcelFile(HttpServletResponse response) throws IOException {
		writeHeader();
		write();
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

}
