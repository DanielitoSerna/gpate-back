package com.gpate.rest;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.gpate.dtos.EstimacionPagoDto;
import com.gpate.model.Contrato;
import com.gpate.model.EstimacionPago;
import com.gpate.repository.EstimacionPagoRepository;
import com.gpate.services.impl.ContratoService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;

@Controller
@RequestMapping("api")
@CrossOrigin("*")
public class ReportController {

	@Autowired
	private ContratoService contratoService;

	@Autowired
	private EstimacionPagoRepository estimacionPagoRepository;

	@Autowired
	ServletContext servletContext;

	private final TemplateEngine templateEngine;

	public ReportController(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	@GetMapping("/pdf/estadoCuenta")
	public ResponseEntity<?> getPdf(@RequestParam(name = "idContrato") Long idContrato, HttpServletRequest request,
			HttpServletResponse response) throws ParseException {

		Contrato contrato = contratoService.getInfoContrato(idContrato);

		List<EstimacionPago> estimacionPagos = estimacionPagoRepository.findByContrato(contrato.getId());

		List<EstimacionPagoDto> estimacionPagosEstimaciones = new ArrayList<>();
		List<EstimacionPagoDto> estimacionPagosPagos = new ArrayList<>();

		DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Locale locale = new Locale("en", "US");
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);

		Integer contadorEstimaciones = 0, contadorPagos = 0;

		for (EstimacionPago estimacionPago : estimacionPagos) {
			EstimacionPagoDto estimacionPagoDto = new EstimacionPagoDto();
			if ("ESTIMACIÓN".equals(estimacionPago.getConcepto())) {
				contadorEstimaciones++;

				String fechaOPeracion = dateFormatter.format(estimacionPago.getFechaOperacion());

				estimacionPagoDto.setId(estimacionPago.getId());
				estimacionPagoDto.setConcepto(estimacionPago.getConcepto());
				estimacionPagoDto.setContrato(estimacionPago.getContrato());
				estimacionPagoDto.setFechaOperacion(fechaOPeracion);
				estimacionPagoDto.setHipervinculo(estimacionPago.getHipervinculo());
				estimacionPagoDto.setImporte(numberFormat.format(estimacionPago.getImporte().doubleValue()));
				estimacionPagoDto.setImporteAbono(numberFormat.format(estimacionPago.getImporte().doubleValue()));
				estimacionPagoDto.setNumeroAbono(estimacionPago.getNumeroAbono());
				estimacionPagoDto.setObservaciones(estimacionPago.getObservaciones());

				String importeAbono = estimacionPago.getImporteAbono() != null
						? numberFormat.format(estimacionPago.getImporteAbono().doubleValue())
						: numberFormat.format(0.00);

				if (estimacionPago.getImporte().equals(estimacionPago.getImporteAbono())) {
					if (estimacionPago.getHipervinculo() != null && !estimacionPago.getHipervinculo().isEmpty()) {
						estimacionPagoDto.setStatus("PAGADO *");
					} else {
						estimacionPagoDto.setStatus("PAGADO");
					}
				} else {
					estimacionPagoDto.setStatus("PAG " + importeAbono + " / PTE "
							+ numberFormat.format(estimacionPago.getImporte().doubleValue()));
				}

				estimacionPagosEstimaciones.add(estimacionPagoDto);
			} else {
				contadorPagos++;

				String fechaOPeracion = dateFormatter.format(estimacionPago.getFechaOperacion());

				estimacionPagoDto.setId(estimacionPago.getId());
				estimacionPagoDto.setConcepto(estimacionPago.getConcepto() + " No. " + estimacionPago.getNumeroAbono());

				estimacionPagoDto.setContrato(estimacionPago.getContrato());
				estimacionPagoDto.setFechaOperacion(fechaOPeracion);
				estimacionPagoDto.setHipervinculo(estimacionPago.getHipervinculo());
				estimacionPagoDto.setImporte(numberFormat.format(estimacionPago.getImporte().doubleValue()));
				estimacionPagoDto.setImporteAbono(numberFormat.format(estimacionPago.getImporte().doubleValue()));
				if (contadorPagos < 10) {
					estimacionPagoDto.setNumeroAbono("0" + contadorPagos + "");
				} else {
					estimacionPagoDto.setNumeroAbono(contadorPagos + "");
				}

				if (estimacionPago.getHipervinculo() != null && !estimacionPago.getHipervinculo().isEmpty()) {
					estimacionPagoDto.setObservaciones(estimacionPago.getObservaciones() + "*");
				} else {
					estimacionPagoDto.setObservaciones(estimacionPago.getObservaciones());
				}
				estimacionPagosPagos.add(estimacionPagoDto);
			}
		}

		String currentDateTime = dateFormatter.format(new Date());

		String montoContratado = contrato.getImporteContratado() != null
				? numberFormat.format(contrato.getImporteContratado())
				: "-";

		String anticipoContratado = contrato.getAnticipoContratado() != null
				? numberFormat.format(contrato.getAnticipoContratado())
				: "-";

		String anticipoPagado = contrato.getAnticipoPagado() != null ? numberFormat.format(contrato.getAnticipoPagado())
				: "-";

		String estimacionProgramada = contrato.getEstimacionesProgramadas() != null
				? numberFormat.format(contrato.getEstimacionesProgramadas())
				: "-";

		String estimacionPagada = contrato.getEstimacionesPagadas() != null
				? numberFormat.format(contrato.getEstimacionesPagadas())
				: "-";

		String pagoAplicado = contrato.getPagosAplicados() != null ? numberFormat.format(contrato.getPagosAplicados())
				: "-";

		String saldoContrato = contrato.getSaldoPendienteContrato() != null
				? numberFormat.format(contrato.getSaldoPendienteContrato())
				: "-";

		float porcentajeEstimacionPagado = contrato.getEstimacionesPagadas() != null
				? contrato.getEstimacionesPagadas().floatValue()
				: 0;
		float porcentajeEstimacionProgramado = contrato.getEstimacionesProgramadas() != null
				? contrato.getEstimacionesProgramadas().floatValue()
				: 0;

		float porcentajeMontoContrato = contrato.getImporteContratado() != null
				? contrato.getImporteContratado().floatValue()
				: 0;

		float porcentajePagoContrato = contrato.getPagosAplicados() != null ? contrato.getPagosAplicados().floatValue()
				: 0;

		String porcentajeEstimacionPago = df
				.format(((porcentajeEstimacionPagado * 100) / porcentajeEstimacionProgramado));

		String porcentajeEstimacionSinPagar = df
				.format(((100 - (porcentajeEstimacionPagado * 100) / porcentajeEstimacionProgramado)));

		String porcentajeContratoPago = df.format(((porcentajePagoContrato * 100) / porcentajeMontoContrato));

		String porcentajeContratoSinPagar = df
				.format(((100 - (porcentajePagoContrato * 100) / porcentajeMontoContrato)));

		float estimacionPagadaFloat = contrato.getEstimacionesPagadas() != null
				? contrato.getEstimacionesPagadas().floatValue()
				: 0;
		float estimacionProgramadaFloat = contrato.getEstimacionesProgramadas() != null
				? contrato.getEstimacionesProgramadas().floatValue()
				: 0;

		float pendEstimacion = estimacionProgramadaFloat - estimacionPagadaFloat;

		String pendEstimacionString = numberFormat.format(pendEstimacion);

		String retencionViciosOcultos = contrato.getRetencionViciosOcultos() != null
				? numberFormat.format(contrato.getRetencionViciosOcultos())
				: "-";

		String amortizacionAnticipo = contrato.getAmortizacionAnticipo() != null
				? numberFormat.format(contrato.getAmortizacionAnticipo())
				: "-";

		String retencionIva = contrato.getRetencionIva() != null ? numberFormat.format(contrato.getRetencionIva())
				: "-";

		String deducciones = contrato.getRetencionIva() != null ? numberFormat.format(contrato.getRetencionIva()) : "-";

		String totalFacturado = contrato.getImporteBruto() != null ? numberFormat.format(contrato.getImporteBruto()) : "-";

		WebContext context = new WebContext(request, response, servletContext);
		context.setVariable("contrato", contrato);
		context.setVariable("fechaActual", currentDateTime);
		context.setVariable("estimacionProgramada", estimacionProgramada);
		context.setVariable("montoContratado", montoContratado);
		context.setVariable("anticipoContratado", anticipoContratado);
		context.setVariable("anticipoPagado", anticipoPagado);
		context.setVariable("estimacionPagada", estimacionPagada);
		context.setVariable("pagoAplicado", pagoAplicado);
		context.setVariable("saldoContrato", saldoContrato);
		context.setVariable("porcentajeEstimacionPago", porcentajeEstimacionPago + "%");
		context.setVariable("porcentajeEstimacionSinPagar", porcentajeEstimacionSinPagar + "%");
		context.setVariable("porcentajeContratoPago", porcentajeContratoPago + "%");
		context.setVariable("porcentajeContratoSinPagar", porcentajeContratoSinPagar + "%");
		context.setVariable("estimacionPagosEstimaciones", estimacionPagosEstimaciones);
		context.setVariable("contadorEstimaciones", "(" + contadorEstimaciones + ") ESTIMACIONES");
		context.setVariable("contadorPagos", "DESGLOSE DE (" + contadorPagos + ") PAGOS");
		context.setVariable("estimacionPagosPagos", estimacionPagosPagos);
		context.setVariable("pendEstimacion", pendEstimacionString);
		context.setVariable("retencionViciosOcultos", retencionViciosOcultos);
		context.setVariable("amortizacionAnticipo", amortizacionAnticipo);
		context.setVariable("retencionIva", retencionIva);
		context.setVariable("deducciones", deducciones);
		context.setVariable("totalFacturado", totalFacturado);

		String orderHtml = templateEngine.process("contrato", context);

		ByteArrayOutputStream target = new ByteArrayOutputStream();
		ConverterProperties converterProperties = new ConverterProperties();
//		converterProperties.setBaseUri("http://localhost:8080");
		converterProperties.setBaseUri("https://gpate-service.onrender.com");

		HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

		/* extract output as bytes */
		byte[] bytes = target.toByteArray();

		/* Send the response as downloadable PDF */

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estado_cuenta.pdf")
				.contentType(MediaType.APPLICATION_PDF).body(bytes);

	}

}
