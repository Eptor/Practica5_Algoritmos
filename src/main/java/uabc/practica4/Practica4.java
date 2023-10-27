package uabc.practica4;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Practica4 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Practica4().display();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void display() throws Exception {
        String jsonString = "";

        try {
            jsonString = JsonFileReader.readJsonFromFile("D:\\Documentos Locales\\6to\\Algoritmos\\practica4\\src\\main\\java\\uabc\\practica4\\archivo.json");
        } catch (Exception e) {
            e.printStackTrace();  // Or handle the error in a way you see fit.
        }
        
        System.out.println("JSON STRING:");
        System.out.println(jsonString);

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray dataArray = jsonObject.getJSONArray("data");
        JSONObject dataObject = dataArray.getJSONObject(250);

        Card card = new Card(
            dataObject.getString("name"),
            dataObject.getString("type"),
            dataObject.getString("desc"),
            dataObject.getString("race"),
            dataObject.getString("archetype"),
            dataObject.getJSONArray("card_images").getJSONObject(0).getString("image_url_small")
        );

    JFrame frame = new JFrame("Card Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        String imageUrl = card.getImageUrlSmall();
        ImageIcon icon = new ImageIcon(URI.create(imageUrl).toURL());
        JLabel imageLabel = new JLabel(icon);
        frame.add(imageLabel, BorderLayout.WEST);
        
        // Display card information
        JTextArea infoArea = new JTextArea(10, 30);
        infoArea.setText(
            "Name: " + card.getName() + "\n"
            + "Type: " + card.getType() + "\n"
            + "Description: " + card.getDescription() + "\n"
            + "Race: " + card.getRace() + "\n"
            + "Archetype: " + card.getArchetype() + "\n"
        );
        infoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(infoArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
