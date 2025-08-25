package com.minacontrol.shared.service;

public interface GeneradorReporteService {
    String generarArchivoPDF(Object datos, String plantilla);
    String generarArchivoExcel(Object datos);
    String generarArchivoCSV(Object datos);
}
