package org.example.sample;

import org.example.sample.entity.Building;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("Введите путь к файлу или для выхода введите EXIT(e): ");

        while( in.hasNextLine() )
        {
            String path = in.nextLine().trim();
            if (path.equals("EXIT") || path.equals("e"))
                break;
            if (path.length() < 4)
            {
                System.out.println("Не корректный путь к файлу(короткий)");
                continue;
            }
            try {
                Path filePath = Paths.get(path);
                if (Files.exists(filePath)) {
                    if (filePath.getFileName().toString().endsWith(".xml")) {
                        filterBuildings(parseXMLstax(filePath));
                    } else if (filePath.getFileName().toString().endsWith(".csv")) {
                        filterBuildings(parseCSV(filePath));
                    } else {
                        System.out.println("Не корректный формат файла");
                    }
                } else {
                    System.out.println("Файла по данному пути не существует");
                }
            } catch (InvalidPathException e) {
                System.out.println("Не корректный путь к файлу");
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Не корректный файл");
                System.out.println(e.getMessage());
            } finally {
                in = new Scanner(System.in);
            }
            System.out.println("Введите путь к файлу или для выхода введите EXIT(e): ");
        }
        in.close();

    }

    public static Map<Building, Integer> parseXMLstax(Path fileName) {
        HashMap<Building, Integer> buildingsMap = new HashMap<>();
        Building building = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName.toFile())))
        {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(br);

            while (xmlEventReader.hasNext()){
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()){
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals("item")){

                        building = new Building();

                        Attribute cityAttr = startElement.getAttributeByName(new QName("city"));
                        if(cityAttr != null){
                            building.setCity(cityAttr.getValue());
                        }

                        Attribute streetAttr = startElement.getAttributeByName(new QName("street"));
                        if(streetAttr != null){
                            building.setStreet(streetAttr.getValue());
                        }

                        Attribute houseAttr = startElement.getAttributeByName(new QName("house"));
                        if (houseAttr != null){
                            building.setHouse(Integer.parseInt(houseAttr.getValue()));
                        }

                        Attribute floorAttr = startElement.getAttributeByName(new QName("floor"));
                        if (floorAttr != null){
                            building.setFloor(Integer.parseInt(floorAttr.getValue()));
                        }
                    }
                }
                if (xmlEvent.isEndElement()){
                    EndElement endElement = xmlEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals("item")){
                        if (buildingsMap.containsKey(building)) {
                            buildingsMap.put(building, buildingsMap.get(building) + 1);
                        } else {
                            buildingsMap.put(building, 1);
                        }
                    }
                }
            }
            xmlEventReader.close();

        } catch (XMLStreamException e) {
            System.out.println("Проблема c парсингом файла: ");
            System.out.println(e.getCause());
        } catch (IOException e) {
            System.out.println("Проблема доступа к файлу: ");
            System.out.println(e.getCause());
        }

        return buildingsMap;
    }

    public static Map<Building, Integer> parseCSV(Path fileName)
    {
        String line = "";
        String cvsSplitBy = ";";
        HashMap<Building, Integer> buildingsMap = null;
        Building building = null;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName.toFile()))) {

            line = br.readLine();
            if (line != null)
            {
                String[] haeders = line.split(cvsSplitBy);
                if (haeders.length == 4)
                {
                    if (haeders[0].equals("\"city\"")
                        && haeders[1].equals("\"street\"")
                        && haeders[2].equals("\"house\"")
                        && haeders[3].equals("\"floor\""))
                    {
                        buildingsMap = new HashMap<>();

                        while ((line = br.readLine()) != null) {

                            String[] country = line.split(cvsSplitBy);

                            building = new Building();
                            building.setCity(country[0].replace("\"", ""));
                            building.setStreet(country[1].replace("\"", ""));
                            building.setHouse(Integer.valueOf(country[2].replace("\"", "")));
                            building.setFloor(Integer.valueOf(country[3].replace("\"", "")));
                            if (buildingsMap.containsKey(building)) {
                                buildingsMap.put(building, buildingsMap.get(building) + 1);
                            } else {
                                buildingsMap.put(building, 1);
                            }
                        }
                    } else {
                        System.out.println("Файл не соотвествует");
                        return null;
                    }
                } else {
                    System.out.println("Файл не соотвествует");
                    return null;
                }
            } else {
                System.out.println("Файл пустой");
                return null;
            }
        }  catch (IOException e) {
            System.out.println("Проблема с доступом к файлу, повторитер еще раз");
            System.out.println(e.getCause());
        }

        return buildingsMap;
    }

    public static void filterBuildings(Map<Building, Integer> buildings) {
        buildings
                .entrySet()
                .stream()
                .sorted((mp1, mp2) -> mp1.getKey().getCity().compareTo(mp2.getKey().getCity()))
                .peek(mp -> {
                    if (mp.getValue() > 1)
                        System.out.println(mp.getKey().getCity() + " "
                                + mp.getKey().getStreet() + " "
                                + mp.getKey().getHouse() + " "
                                + mp.getKey().getFloor() + " "
                                + " повторяется " + mp.getValue()
                                + " раз"
                        );
                })
                .map(mp -> mp.getKey())
                .collect(Collectors.groupingBy(mp -> mp.getCity(), Collectors.toList()))
                .entrySet()
                .stream()
                .sorted((mp1, mp2) -> mp1.getKey().compareTo(mp2.getKey()))
                                .peek(mp -> {
                    System.out.println(mp.getKey());
                    mp.getValue().stream().collect(
                            Collectors.groupingBy(item -> item.getFloor(), Collectors.counting())
                    )
                    .entrySet()
                    .stream()
                    .peek(item -> {
                        System.out.println(item.getKey() + " этажных домов " + item.getValue() + " шт");
                    })
                    .collect(Collectors.toSet());
                })
                .collect(Collectors.toSet());
    }

}

