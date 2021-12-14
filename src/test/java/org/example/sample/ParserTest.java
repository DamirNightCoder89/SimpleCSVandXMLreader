package org.example.sample;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import org.example.sample.entity.Building;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest  {

    @Test
    void parseXMLAssertions(@TempDir Path tempDir) throws IOException
    {
        Path XMLpath = tempDir.resolve("addressTest.xml");

        String sampleXML =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root>\n" +
                "<item city=\"Барнаул\" street=\"Дальняя улица\" house=\"56\" floor=\"2\" />\n" +
                "<item city=\"Братск\" street=\"Большая Октябрьская улица\" house=\"65\" floor=\"5\" />\n" +
                "<item city=\"Балаково\" street=\"Барыши, местечко\" house=\"67\" floor=\"2\" />\n" +
                "<item city=\"Азов\" street=\"Просека, улица\" house=\"156\" floor=\"3\" />\n" +
                "<item city=\"Видное\" street=\"Авиаторов, улица\" house=\"185\" floor=\"3\" />\n" +
                "<item city=\"Барнаул\" street=\"Дальняя улица\" house=\"56\" floor=\"2\" />\n" +
                "</root>\n";

        Building repitedBuilding = new Building();
        repitedBuilding.setCity("Барнаул");
        repitedBuilding.setStreet("Дальняя улица");
        repitedBuilding.setFloor(2);
        repitedBuilding.setHouse(56);

        Files.write(XMLpath, sampleXML.getBytes());
        HashMap<Building, Integer> buildingsXML = (HashMap<Building, Integer>) Parser.parseXMLstax(XMLpath);
        assertAll("Parsing for XML",
                () -> assertEquals(5, buildingsXML.size()),
                () -> assertTrue(buildingsXML.containsKey(repitedBuilding)),
                () -> assertEquals(2, buildingsXML.get(repitedBuilding))
        );
    }

    @Test
    void parseCSVAssertions(@TempDir Path tempDir) throws IOException
    {
        Path CSVpath = tempDir.resolve("addressTest.csv");

        String sampleCSV =
                "\"city\";\"street\";\"house\";\"floor\"\n" +
                "\"Барнаул\";\"Дальняя улица\";56;2\n" +
                "\"Братск\";\"Большая Октябрьская улица\";65;5\n" +
                "\"Балаково\";\"Барыши, местечко\";67;2\n" +
                "\"Азов\";\"Просека, улица\";156;3\n" +
                "\"Видное\";\"Авиаторов, улица\";185;3\n" +
                "\"Братск\";\"7-я Вишнёвая улица\";49;5\n" +
                "\"Батайск\";\"Мостотреста, улица\";133;4\n" +
                "\"Барнаул\";\"Дальняя улица\";56;2";

        Building repitedBuilding = new Building();
        repitedBuilding.setCity("Барнаул");
        repitedBuilding.setStreet("Дальняя улица");
        repitedBuilding.setFloor(2);
        repitedBuilding.setHouse(56);

        Files.write(CSVpath, sampleCSV.getBytes());
        HashMap<Building, Integer> buildingsCSV = (HashMap<Building, Integer>) Parser.parseCSV(CSVpath);

        assertAll("Parsing for CSV",
                () -> assertEquals(7, buildingsCSV.size()),
                () -> assertTrue(buildingsCSV.containsKey(repitedBuilding)),
                () -> assertEquals(2, buildingsCSV.get(repitedBuilding))
        );
    }
}
