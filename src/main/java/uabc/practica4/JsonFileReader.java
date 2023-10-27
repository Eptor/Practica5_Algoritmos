/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uabc.practica4;

/**
 *
 * @author Hector
 */
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonFileReader {

    public static String readJsonFromFile(String filePath) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
