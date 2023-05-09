package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import guru.qa.domain.Book;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipFile;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class FileParseTest {
    /*
    1.– Реализовать чтение и проверку содержимого каждого файла из архива - pdf, xlsx, csv
    2.– Реализовать разбор book.json файла библиотекой Jackson
    */
    ClassLoader classLoader = FileParseTest.class.getClassLoader();

    private static final String ZIP_FILE_NAME = "archiveFiles.zip";
    private static final String PDF_FILE_NAME = "TOP-3000.pdf";
    private static final String XLSX_FILE_NAME = "stmost.xlsx";
    private static final String CSV_FILE_NAME = "file.csv";

    private InputStream getFileFromArchive(String fileName) throws Exception {
        ZipFile zip = new ZipFile(new File("src/test/resources/" + ZIP_FILE_NAME));
        return zip.getInputStream(zip.getEntry(fileName));
    }

    @Test
    void pdfFileTest() throws Exception {
        try (InputStream is = getFileFromArchive(PDF_FILE_NAME)) {
            PDF pdf = new PDF(is);
            assertThat(pdf.numberOfPages).isEqualTo(172);
            assertThat(pdf.text).containsAnyOf("ТОП 3000 английских слов");
        }
    }

    @Test
    void xlsxFileTest() throws Exception {
        try (InputStream is = getFileFromArchive(XLSX_FILE_NAME)) {
            XLS xls = new XLS(is);
            assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(1).getNumericCellValue()).isEqualTo(5);
            assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(2).getNumericCellValue()).isEqualTo(6);
            assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(3).getNumericCellValue()).isEqualTo(7);
            assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(4).getNumericCellValue()).isEqualTo(8);
            assertThat(xls.excel.getSheetAt(0).getPhysicalNumberOfRows()).isEqualTo(246);
        }
    }

    @Test
    void csvFileTest() throws Exception {
        try (InputStream is = getFileFromArchive(CSV_FILE_NAME)) {
            CSVReader csvReader = new CSVReader(new InputStreamReader(is, UTF_8));
            List<String[]> csv = csvReader.readAll();
            assertThat(csv).contains(
                    new String[]{"firstname",
                            "lastname",
                            "email",
                            "phone",
                            "type",
                            "companyname",
                            "address",
                            "notes",
                            "city",
                            "countrycode",
                            "street",
                            "building"});
        }
    }

    @Test
    void jsonTest() {
        InputStream is = classLoader.getResourceAsStream("book.json");
        Gson gson = new Gson();
        Book jsonObject = gson.fromJson(new InputStreamReader(is), Book.class);
        assertThat(jsonObject.isTitle()).isEqualTo("Мастер и Маргарита");
        assertThat(jsonObject.isAuthor()).isEqualTo("Михаил Булгаков");
        assertThat(jsonObject.isYear()).isEqualTo(1966);
        assertThat(jsonObject.isCountOfPages()).isEqualTo(448);
        assertThat(jsonObject.isGenres()).isEqualTo(new String[]{"фантастика", "философия", "сатира"});
    }
}