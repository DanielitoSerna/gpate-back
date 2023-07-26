package com.gpate.services;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.DocumentException;

public interface IContratoService {
	
	public void generatePdf(HttpServletResponse response) throws DocumentException, IOException;

}
