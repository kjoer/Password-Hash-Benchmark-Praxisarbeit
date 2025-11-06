package de.kon.praxisarbeit;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {
    public void exportToExcel(List<HashResult> results, SysInfo sysInfo, String filename)
            throws IOException {
        Workbook workbook = new XSSFWorkbook();

        createSystemInfoSheet(workbook, sysInfo);

        createResultsSheet(workbook, results);

        try (FileOutputStream fileOut = new FileOutputStream(filename)) {
            workbook.write(fileOut);
        }
        workbook.close();
        System.out.println("Excel-Datei erstellt: " + filename);
    }

    private void createSystemInfoSheet(Workbook workbook, SysInfo sysInfo) {
        Sheet sheet = workbook.createSheet("Systeminformationen");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        int rowNum = 0;
        createRow(sheet, rowNum++, headerStyle, "Komponente", "Wert");
        createRow(sheet, rowNum++, null, "Prozessor", sysInfo.getProcessorName());
        createRow(sheet, rowNum++, null, "CPU Kerne", String.valueOf(sysInfo.getProcessorCores()));
        createRow(sheet, rowNum++, null, "CPU Frequenz (MHz)",
                String.valueOf(sysInfo.getProcessorFrequencyMHz()));
        createRow(sheet, rowNum++, null, "RAM Gesamt (GB)",
                String.valueOf(sysInfo.getTotalMemoryGB()));
        createRow(sheet, rowNum++, null, "RAM Verfügbar (GB)",
                String.valueOf(sysInfo.getAvailableMemoryGB()));

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private void createResultsSheet(Workbook workbook, List<HashResult> results) {
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Hash-Ergebnisse");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Algorithmus", "Passwort", "Hash", "Salt",
                "Zeit (ms)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (HashResult result : results) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(result.getAlgorithm());
            row.createCell(1).setCellValue(result.getPassword());
            row.createCell(2).setCellValue(result.getHash());
            row.createCell(3).setCellValue(result.getSalt());
            row.createCell(4).setCellValue(result.getExecutionTimeMs());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        createChart(sheet, results.size());
    }

    private void createChart(XSSFSheet sheet, int dataSize) {
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFChart chart = drawing.createChart(drawing.createAnchor(0, 0, 0, 0, 8, 2, 15, 20));

        chart.setTitleText("Ausführungszeit Vergleich");
        chart.setTitleOverlay(false);

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Algorithmus");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("Zeit (ms)");

        XDDFDataSource<String> algorithms = XDDFDataSourcesFactory.fromStringCellRange(sheet,
                new CellRangeAddress(1, dataSize, 0, 0));
        XDDFNumericalDataSource<Double> times = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                new CellRangeAddress(1, dataSize, 4, 4));

        XDDFBarChartData data = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        XDDFBarChartData.Series series = (XDDFBarChartData.Series) data.addSeries(algorithms, times);
        series.setTitle("Ausführungszeit", null);

        chart.plot(data);
    }

    private void createRow(Sheet sheet, int rowNum, CellStyle style, String... values) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(values[i]);
            if (style != null) {
                cell.setCellStyle(style);
            }
        }
    }

    public static void convertCsvToExcel(String csvFile, String excelFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile));
             Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("JMH Results");
            String line;
            int rowNum = 0;
            while ((line = reader.readLine()) != null) {
                Row row = sheet.createRow(rowNum++);
                String[] cells = line.split(",");
                for (int i = 0; i < cells.length; i++) {
                    row.createCell(i).setCellValue(cells[i]);
                }
            }
            try (FileOutputStream out = new FileOutputStream(excelFile)) {
                workbook.write(out);
            }
        }
        System.out.println("Excel-Datei erstellt: " + excelFile);
    }
}

