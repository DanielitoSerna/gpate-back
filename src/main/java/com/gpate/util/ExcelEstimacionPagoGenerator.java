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

import com.gpate.model.EstimacionPago;

public class ExcelEstimacionPagoGenerator {
	
	private List<EstimacionPago> estimacionPagos;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public ExcelEstimacionPagoGenerator(List<EstimacionPago> estimacionPagos) {
		this.estimacionPagos = estimacionPagos;
		this.workbook = new XSSFWorkbook();;
	}
	
	private void writeHeader() {
		sheet = workbook.createSheet("Student");
		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);
		createCell(row, 0, "No.", style);
		createCell(row, 1, "Concepto", style);
		createCell(row, 2, "Importe", style);
		createCell(row, 3, "Importe pagado", style);
		createCell(row, 4, "Fecha operación", style);
		createCell(row, 5, "Observaciones", style);
		createCell(row, 6, "Hipervinculo", style);
		createCell(row, 7, "Ident. contrato", style);
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
		for (EstimacionPago estimacionPago : estimacionPagos) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, estimacionPago.getNumeroAbono() != null ? estimacionPago.getNumeroAbono() : "", style);
			createCell(row, columnCount++, estimacionPago.getConcepto() != null ? estimacionPago.getConcepto() : "", style);
			createCell(row, columnCount++, estimacionPago.getImporte() != null ? "$ " + estimacionPago.getImporte().toString() : "$ 0.00", style);
			createCell(row, columnCount++, estimacionPago.getImporteAbono() != null ? "$ " + estimacionPago.getImporteAbono().toString() : "$ 0.00", style);
			createCell(row, columnCount++, estimacionPago.getFechaOperacion() != null ? DateUtils.convertDateToString(estimacionPago.getFechaOperacion()) : "", style);
			createCell(row, columnCount++, estimacionPago.getObservaciones() != null ? estimacionPago.getObservaciones() : "", style);
			createCell(row, columnCount++, estimacionPago.getHipervinculo() != null ? estimacionPago.getHipervinculo() : "", style);
			createCell(row, columnCount++, estimacionPago.getContrato() != null ? estimacionPago.getContrato() : "", style);
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
