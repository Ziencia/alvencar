package es.mde.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import es.mde.entidades.FacturaConId;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PdfService {

        public byte[] generarPdf(FacturaConId factura) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                try {
                        PdfWriter writer = new PdfWriter(baos);
                        PdfDocument pdfDoc = new PdfDocument(writer);
                        Document document = new Document(pdfDoc);

                        document.setFont(PdfFontFactory.createFont("Helvetica"));

                        String encabezado = String.format("Factura 2025/%s", factura.getId());
                        addEncabezado(document, encabezado);
                        addSeccionEmpresa(document);
                        addSeccionCliente(document, factura);
                        if (factura.getTipoFactura() == 1) {
                                addSeccionDatosVenta(document, factura);
                        } else {
                                addSeccionDatosAlquiler(document, factura);
                        }
                        addSeccionEstadoPago(document, factura);
                        addInformacionPago(document, factura.getId().toString());
                        addAgradecimiento(document);

                        document.close();

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return baos.toByteArray();
        }

        private void addEncabezado(Document document, String headerText) {
                Paragraph header = new Paragraph(headerText)
                                .setTextAlignment(TextAlignment.CENTER)
                                .setFontSize(20)
                                .setBold()
                                .setMultipliedLeading(2);
                document.add(header);
        }

        private void addSeccionEmpresa(Document document) {

                Table datosEmpresa = new Table(new float[] { 1, 3 });
                datosEmpresa.setWidth(UnitValue.createPercentValue(100));

                Cell logoEmpresa = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
                Paragraph logo = new Paragraph("ALVENCAR S.L.U.")
                                .setFontSize(26)
                                .setBold()
                                .setFontColor(ColorConstants.BLUE)
                                .setTextAlignment(TextAlignment.CENTER)
                                .setBorderBottom(new SolidBorder(ColorConstants.BLUE, 2))
                                .setPaddingTop(10)
                                .setPaddingBottom(10)
                                .setCharacterSpacing(2);

                logoEmpresa.add(logo);
                datosEmpresa.addCell(logoEmpresa);

                Cell datosFiscalesEmpresa = new Cell().setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.RIGHT);

                datosFiscalesEmpresa.add(new Paragraph("CIF: B00000000"));
                datosFiscalesEmpresa.add(new Paragraph("Calle Libertad 34, Madrid"));
                datosFiscalesEmpresa.add(new Paragraph("Tel: +34 612 45 67 89"));
                datosFiscalesEmpresa.add(new Paragraph("contacta@alvencar.com"));

                datosEmpresa.addCell(datosFiscalesEmpresa);

                document.add(datosEmpresa);

                Paragraph espacio = new Paragraph("").setMarginBottom(5);
                document.add(espacio);
        }

        private void addSeccionCliente(Document document, FacturaConId factura) {
                Table tablaDatos = new Table(UnitValue.createPercentArray(new float[] { 30, 70 }))
                                .useAllAvailableWidth();

                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Factura Nº").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaDatos.addCell(new Cell().add(new Paragraph("2025/" + String.valueOf(factura.getId()))));

                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Concepto").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaDatos.addCell(new Cell().add(new Paragraph(factura.getConceptoFactura())));

                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Cliente").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaDatos.addCell(new Cell().add(new Paragraph(factura.getNombreApellidosDNI())));

                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Dirección").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaDatos.addCell(new Cell().add(new Paragraph(factura.getDatosDireccionLocalizacion())));

                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Vehículo").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaDatos.addCell(new Cell().add(new Paragraph(factura.getDatosVehiculo())));

                document.add(tablaDatos);

                Paragraph espacio = new Paragraph("").setMarginBottom(5);
                document.add(espacio);
        }

        private void addSeccionDatosVenta(Document document, FacturaConId factura) {
                Table tablaImportes = new Table(UnitValue.createPercentArray(new float[] { 70, 30 }))
                                .useAllAvailableWidth();

                tablaImportes.addCell(
                                new Cell().add(new Paragraph("Importe base").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaImportes.addCell(new Cell().add(new Paragraph(formatNumero(factura.getImporte()) + " €"))
                                .setTextAlignment(TextAlignment.RIGHT));

                float totalImpuestos = factura.getImporte() * factura.getImpuestos();
                String textoIVA = factura.getImpuestos() == 0 ? "Exento de I.V.A. Regimen Especial de bienes usados"
                                : "Impuestos (I.V.A. 21%)";
                tablaImportes.addCell(
                                new Cell().add(new Paragraph(textoIVA).setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaImportes.addCell(new Cell().add(new Paragraph(formatNumero(totalImpuestos) + " €"))
                                .setTextAlignment(TextAlignment.RIGHT));

                tablaImportes.addCell(
                                new Cell().add(new Paragraph("Total").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaImportes.addCell(new Cell().add(new Paragraph(formatNumero(factura.getImporteTotal()) + " €"))
                                .setTextAlignment(TextAlignment.RIGHT));

                document.add(tablaImportes);

                Paragraph espacio = new Paragraph("").setMarginBottom(5);
                document.add(espacio);
        }

        private void addSeccionDatosAlquiler(Document document, FacturaConId factura) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                Table tablaImportes = new Table(UnitValue.createPercentArray(new float[] { 70, 30 }))
                                .useAllAvailableWidth();

                tablaImportes.addCell(
                                new Cell().add(new Paragraph("Fecha de inicio del alquiler").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaImportes.addCell(new Cell().add(new Paragraph(factura.getFechaInicioAlquiler().format(formatter)))
                                .setTextAlignment(TextAlignment.RIGHT));

                tablaImportes.addCell(
                                new Cell().add(new Paragraph("Fecha de fin del alquiler").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaImportes.addCell(new Cell().add(new Paragraph(factura.getFechaFinAlquiler().format(formatter)))
                                .setTextAlignment(TextAlignment.RIGHT));

                tablaImportes.addCell(
                                new Cell().add(new Paragraph(
                                                "Total importe de días del alquiler (" + factura.getNumeroDiasAlquiler()
                                                                + ", " + factura.getImporte() + "€/dia)")
                                                .setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaImportes.addCell(new Cell().add(new Paragraph(formatNumero(factura.getImporteTotalDias()) + " €"))
                                .setTextAlignment(TextAlignment.RIGHT));

                tablaImportes.addCell(
                                new Cell().add(new Paragraph("Total importe por KMs extra").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaImportes.addCell(new Cell().add(new Paragraph(formatNumero(factura.getImporteKmExtra()) + " €"))
                                .setTextAlignment(TextAlignment.RIGHT));

                tablaImportes.addCell(
                                new Cell().add(new Paragraph("Total importe penalización depósito").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaImportes.addCell(new Cell()
                                .add(new Paragraph(formatNumero(factura.getImportePenalizacionDeposito()) + " €"))
                                .setTextAlignment(TextAlignment.RIGHT));

                float importeBase = factura.getImporteTotalDias() + factura.getImporteKmExtra()
                                + factura.getImportePenalizacionDeposito();
                tablaImportes.addCell(
                                new Cell().add(new Paragraph("Importe base").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaImportes.addCell(new Cell().add(new Paragraph(formatNumero(importeBase) + " €"))
                                .setTextAlignment(TextAlignment.RIGHT));

                float totalImpuestos = importeBase * 0.21f;
                tablaImportes.addCell(
                                new Cell().add(new Paragraph("Impuestos (I.V.A. 21%)").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaImportes.addCell(new Cell().add(new Paragraph(formatNumero(totalImpuestos) + " €"))
                                .setTextAlignment(TextAlignment.RIGHT));

                tablaImportes.addCell(
                                new Cell().add(new Paragraph("Total").setBold())
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaImportes.addCell(new Cell().add(new Paragraph(formatNumero(factura.getImporteTotal()) + " €"))
                                .setTextAlignment(TextAlignment.RIGHT));

                document.add(tablaImportes);

                Paragraph espacio = new Paragraph("").setMarginBottom(5);
                document.add(espacio);
        }

        private void addSeccionEstadoPago(Document document, FacturaConId factura) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                Table tablaExtras = new Table(UnitValue.createPercentArray(new float[] { 30, 70 }))
                                .useAllAvailableWidth();

                tablaExtras.addCell(new Cell().add(new Paragraph("Fecha de emisión").setBold())
                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaExtras.addCell(new Cell().add(new Paragraph(factura.getFechaFactura().format(formatter))));

                tablaExtras.addCell(new Cell().add(new Paragraph("Figura").setBold())
                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablaExtras.addCell(new Cell().add(new Paragraph(factura.isEstaPagada() ? "PAGADA" : "PENDIENTE")));

                document.add(tablaExtras);

                Paragraph espacio = new Paragraph("").setMarginBottom(5);
                document.add(espacio);
        }

        private void addInformacionPago(Document document, String id) {
                Paragraph titulo = new Paragraph("Detalles para realizar el pago")
                                .setFontSize(14)
                                .setBold()
                                .setFontColor(ColorConstants.DARK_GRAY)
                                .setTextAlignment(TextAlignment.LEFT)
                                .setMarginTop(5)
                                .setMarginBottom(5);
                document.add(titulo);

                Paragraph texto = new Paragraph(
                                "Por favor, realice el pago a través de los siguientes datos bancarios:")
                                .setFontSize(10)
                                .setTextAlignment(TextAlignment.LEFT)
                                .setMarginBottom(5);
                document.add(texto);

                Table tablaPago = new Table(UnitValue.createPercentArray(new float[] { 15, 55 }))
                                .setWidth(UnitValue.createPercentValue(70));

                tablaPago.addCell(new Cell().add(new Paragraph("Titular:").setBold()).setBorder(Border.NO_BORDER));
                tablaPago.addCell(new Cell().add(new Paragraph("Alvencar S.L.U.")).setBorder(Border.NO_BORDER));

                tablaPago.addCell(new Cell().add(new Paragraph("Banco:").setBold()).setBorder(Border.NO_BORDER));
                tablaPago.addCell(new Cell().add(new Paragraph("Banco Nacional Español")).setBorder(Border.NO_BORDER));

                tablaPago.addCell(new Cell().add(new Paragraph("IBAN:").setBold()).setBorder(Border.NO_BORDER));
                tablaPago.addCell(new Cell().add(new Paragraph("ES12 3456 7890 1234 5678 9012"))
                                .setBorder(Border.NO_BORDER));

                tablaPago.addCell(new Cell().add(new Paragraph("BIC/SWIFT:").setBold()).setBorder(Border.NO_BORDER));
                tablaPago.addCell(new Cell().add(new Paragraph("BNESESMMXXX")).setBorder(Border.NO_BORDER));

                tablaPago.addCell(new Cell().add(new Paragraph("Concepto:").setBold()).setBorder(Border.NO_BORDER));
                tablaPago.addCell(new Cell().add(new Paragraph("Pago factura 2025/" + id)).setBorder(Border.NO_BORDER));

                document.add(tablaPago);

                Paragraph espacio = new Paragraph("").setMarginBottom(5);
                document.add(espacio);
        }

        private void addAgradecimiento(Document document) {
                Style agradecimientoStyle = new Style()
                                .setTextAlignment(TextAlignment.CENTER)
                                .setFontSize(10)
                                .setItalic();

                document.add(new Paragraph(
                                "Gracias por confiar en Alvencar S.L.U. para la compra y alquiler de vehículos.")
                                .addStyle(agradecimientoStyle));
                document.add(new Paragraph(
                                "Para cualquier consulta, no dude en ponerse en contacto con nuestro equipo de atención al cliente.")
                                .addStyle(agradecimientoStyle));
                document.add(new Paragraph("¡Esperamos verle pronto!").addStyle(agradecimientoStyle));
        }

        private static String formatNumero(float numero) {
                NumberFormat formatoEspañol = NumberFormat.getInstance(new Locale("es", "ES"));
                return formatoEspañol.format(numero);
        }

        // https://api.itextpdf.com/iText/java/8.0.3/
}
