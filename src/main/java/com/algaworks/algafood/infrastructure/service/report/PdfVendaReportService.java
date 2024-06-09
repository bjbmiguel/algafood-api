package com.algaworks.algafood.infrastructure.service.report;

import com.algaworks.algafood.domain.exception.ReportException;
import com.algaworks.algafood.domain.service.VendaQueryService;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.service.VendaReportService;

import java.util.HashMap;
import java.util.Locale;

@Service
public class PdfVendaReportService implements VendaReportService {

    @Autowired
    private VendaQueryService vendaQueryService;


        @Override
        public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
            try {

                //Pegamos um inputStream (fluxo de Dados) do nosso report
                var inputStream = this.getClass().getResourceAsStream(
                        "/reports/vendas-diarias.jasper");

                //Definimos os parâmetros do report...//pt_AO - Angola
                var parametros = new HashMap<String, Object>();
                parametros.put("REPORT_LOCALE", new Locale("pt", "AO")); //Definimos o locale
                // Este constante REPORT_LOCALE precisa ser conforme está

                //Pegamos a nossa lista de vendas diárias
                var vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffset);

                //Geramos o nosso dataSource
                var dataSource = new JRBeanCollectionDataSource(vendasDiarias);

                //Responsável por preencher o report via fillReport
                var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);

                //Exportamos para pdf
                return JasperExportManager.exportReportToPdf(jasperPrint);
            } catch (Exception e) {
                throw new ReportException("Não foi possível emitir relatório de vendas diárias", e);
            }
        }

}