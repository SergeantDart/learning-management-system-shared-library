package csie.bdsa.lms.shared.util;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import csie.bdsa.lms.shared.dto.BaseDto;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public abstract class PdfExporter<DTO extends BaseDto<ID>, ID> {

    private final String title;
    private final String[] columnNames;
    private final float[] columnWidths;
    protected final List<CellContentGetter<DTO>> cellContentGetters;

    public PdfExporter(String title, String[] columnNames,
                       float[] columnWidths, List<CellContentGetter<DTO>> cellContentGetters) {
        this.title = title;
        this.columnNames = columnNames;
        this.columnWidths = columnWidths;
        this.cellContentGetters = cellContentGetters;
    }

    protected void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        for (String column : columnNames) {
            cell.setPhrase(new Phrase(column, font));
            table.addCell(cell);
        }
    }

    protected void writeTableData(List<DTO> list, PdfPTable table) {
        list.forEach(e -> {
            for (CellContentGetter<DTO> cellContentGetter : cellContentGetters) {
                table.addCell(cellContentGetter.getCellContent(e));
            }
        });
    }

    public void export(List<DTO> list, HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph = new Paragraph(title, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        PdfPTable table = new PdfPTable(columnNames.length);
        table.setWidthPercentage(100f);
        table.setWidths(columnWidths);
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(list, table);

        document.add(paragraph);
        document.add(table);

        document.close();
    }

    @FunctionalInterface
    protected interface CellContentGetter<DTO> {
        String getCellContent(DTO dto);
    }

}
