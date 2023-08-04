package co.com.bancolombia.declaraciones.libcommons.helpers;


import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.svg.converter.SvgConverter;
import java.io.IOException;
import java.util.Objects;

public class PdfHeaderEventHandler implements IEventHandler {
    Image bancolombiaIcon;
    Image trazoBancolombiaIcon;
    Table table;
    float tableHeight;
    Document document;

    public PdfHeaderEventHandler(Document document){
        try {
            this.document = document;
            PdfDocument pdfDocument = document.getPdfDocument();
            bancolombiaIcon = SvgConverter.convertToImage(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("Logo Bancolombia.svg"))
                    , pdfDocument);
            bancolombiaIcon.setHorizontalAlignment(HorizontalAlignment.LEFT);
            trazoBancolombiaIcon = SvgConverter.convertToImage(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("trazo.svg")),
                    pdfDocument);
            trazoBancolombiaIcon.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            initTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float getTableHeight() {
        return tableHeight;
    }

    public void initTable() {
        table = new Table(2);
        Cell cell;
        table.setWidth(document.getPdfDocument().getDefaultPageSize().getRight() - document.getPdfDocument().getDefaultPageSize().getLeft()
                - document.getLeftMargin());
        cell = new Cell().add(bancolombiaIcon).setBorder(Border.NO_BORDER);
        table.addCell(cell);
        cell = new Cell().add(trazoBancolombiaIcon).setBorder(Border.NO_BORDER);
        table.addCell(cell);
        TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
        tableHeight = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4))).getOccupiedArea().getBBox().getHeight();

    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = documentEvent.getDocument();
        PdfPage pdfPage = documentEvent.getPage();

        PdfCanvas canvas = new PdfCanvas(pdfPage.newContentStreamBefore(), pdfPage.getResources(), pdfDocument);
        Rectangle rectangle = new Rectangle(pdfDocument.getDefaultPageSize().getX() + document.getLeftMargin(),
                pdfDocument.getDefaultPageSize().getTop() - document.getTopMargin(), 100, getTableHeight());
        new Canvas(canvas, rectangle).add(table);
    }
}
