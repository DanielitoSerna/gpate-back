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

import com.gpate.dtos.EstimacionPagoDto;

public class ExcelEstimacionPagoGenerator {
	
	private List<EstimacionPagoDto> estimacionPagos;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public ExcelEstimacionPagoGenerator(List<EstimacionPagoDto> estimacionPagos) {
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
		createCell(row, 3, "Fecha operación", style);
		createCell(row, 4, "Observaciones", style);
		createCell(row, 5, "Hipervinculo", style);
		createCell(row, 6, "Contrato / Folio", style);
		createCell(row, 7, "Importe bruto", style);
		createCell(row, 8, "Retención vicios oscuros", style);
		createCell(row, 9, "Amortización anticipo", style);
		createCell(row, 10, "Iva", style);
		createCell(row, 11, "Retención iva", style);
		createCell(row, 12, "Isr", style);
		createCell(row, 13, "Deducciones", style);
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
		for (EstimacionPagoDto estimacionPago : estimacionPagos) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, estimacionPago.getNumeroAbono() != null ? estimacionPago.getNumeroAbono() : "", style);
			createCell(row, columnCount++, estimacionPago.getConcepto() != null ? estimacionPago.getConcepto() : "", style);
			createCell(row, columnCount++, estimacionPago.getImporte() != null ? estimacionPago.getImporte().toString() : "$0.00", style);
			createCell(row, columnCount++, estimacionPago.getFechaOperacion() != null ? estimacionPago.getFechaOperacion() : "", style);
			createCell(row, columnCount++, estimacionPago.getObservaciones() != null ? estimacionPago.getObservaciones() : "", style);
			createCell(row, columnCount++, estimacionPago.getHipervinculo() != null ? estimacionPago.getHipervinculo() : "", style);
			createCell(row, columnCount++, estimacionPago.getFolio() != null ? estimacionPago.getFolio() : "", style);
			createCell(row, columnCount++, estimacionPago.getImporteBruto() != null ? estimacionPago.getImporteBruto().toString() : "$0.00", style);
			createCell(row, columnCount++, estimacionPago.getRetencionViciosOcultos() != null ? estimacionPago.getRetencionViciosOcultos().toString() : "$0.00", style);
			createCell(row, columnCount++, estimacionPago.getAmortizacionAnticipo() != null ? estimacionPago.getAmortizacionAnticipo().toString() : "$0.00", style);
			createCell(row, columnCount++, estimacionPago.getIva() != null ? estimacionPago.getIva().toString() : "$0.00", style);
			createCell(row, columnCount++, estimacionPago.getRetencionIva() != null ? estimacionPago.getRetencionIva().toString() : "$0.00", style);
			createCell(row, columnCount++, estimacionPago.getIsr() != null ? estimacionPago.getIsr().toString() : "$0.00", style);
			createCell(row, columnCount++, estimacionPago.getDeducciones() != null ? estimacionPago.getDeducciones().toString() : "$0.00", style);
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
