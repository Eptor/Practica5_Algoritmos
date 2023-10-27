package uabc.practica4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GUI {

    private JFrame frame;
    private JTextArea cardDetailTextArea;
    private JSONArray dataArray;
    private int currentIndex = 0;

    private JButton nextButton;
    private JButton prevButton;
    private JButton showAllButton;
    private JButton sortByLevelButton;
    private JButton sortByAttributeButton;
    private JButton sortByAtkButton;
    private JButton sortByDefButton;
    private JCheckBox ascendingCheckBox;
    private JLabel cardImageLabel;

    public GUI(JSONArray dataArray) {
        this.dataArray = dataArray;
    }

    private void createAndShowGUI() {
        frame = new JFrame("Card Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 500);

        cardDetailTextArea = new JTextArea();
        cardDetailTextArea.setEditable(false);
        cardDetailTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(cardDetailTextArea);

        cardImageLabel = new JLabel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, cardImageLabel);
        splitPane.setDividerLocation(650); // puedes ajustar este valor como desees
        frame.add(splitPane);

        // Navigation Panel
        JPanel navigationPanel = new JPanel();

        prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                updateCardDetail();
                updateCardImage();
            }
        });

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            if (currentIndex < dataArray.length() - 1) {
                currentIndex++;
                updateCardDetail();
                updateCardImage();
            }
        });

        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);

        frame.add(navigationPanel, BorderLayout.SOUTH);

        // Toolbar Panel
        JPanel toolbarPanel = new JPanel();

        showAllButton = new JButton("Mostrar todas");
        showAllButton.addActionListener(e -> {
            updateUI();
        });

        sortByLevelButton = new JButton("Mostrar por nivel");
        sortByLevelButton.addActionListener(e -> {
            sortDataArrayBy("level");
            updateUI();
        });

        sortByAttributeButton = new JButton("Mostrar por atributo");
        sortByAttributeButton.addActionListener(e -> {
            sortDataArrayBy("attribute");
            updateUI();
        });

        sortByAtkButton = new JButton("Ordenar por ataque");
        sortByAtkButton.addActionListener(e -> {
            sortDataArrayBy("atk");
            updateUI();
        });

        sortByDefButton = new JButton("Ordenar por defensa");
        sortByDefButton.addActionListener(e -> {
            sortDataArrayBy("def");
            updateUI();
        });

        ascendingCheckBox = new JCheckBox("Ascendente");

        toolbarPanel.add(showAllButton);
        toolbarPanel.add(sortByLevelButton);
        toolbarPanel.add(sortByAttributeButton);
        toolbarPanel.add(sortByAtkButton);
        toolbarPanel.add(sortByDefButton);
        toolbarPanel.add(ascendingCheckBox);

        frame.add(toolbarPanel, BorderLayout.NORTH);

        frame.setVisible(true);
        updateCardDetail();
        updateCardImage();
    }

    private String formatCardDetail(JSONObject card) {
        try {
            String name = card.getString("name");
            int level = card.getInt("level");
            int atk = card.getInt("atk");
            int def = card.getInt("def");
            String attribute = card.getString("attribute");
            String description = card.getString("desc");

            return String.format("Nombre: %s\nNivel: %d\nAtaque: %d\nDefensa: %d\nAtributo: %s\nDescripción: %s",
                    name, level, atk, def, attribute, description);
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error al obtener detalles de la carta.";
        }
    }

    private void updateCardDetail() {
        try {
            JSONObject card = dataArray.getJSONObject(currentIndex);
            cardDetailTextArea.setText(formatCardDetail(card));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateCardImage() {
        try {
            JSONObject card = dataArray.getJSONObject(currentIndex);

            // Accediendo al array card_images y obteniendo el primer objeto
            JSONObject cardImagesObject = card.getJSONArray("card_images").getJSONObject(0);
            String imageUrl = cardImagesObject.getString("image_url_small");

            // Obtener y mostrar imagen desde la URL usando URI
            URI uri = new URI(imageUrl);
            ImageIcon imageIcon = new ImageIcon(uri.toURL());
            cardImageLabel.setIcon(imageIcon);
        } catch (JSONException | MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void sortDataArrayBy(String key) {
        List<JSONObject> jsonList = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            try {
                jsonList.add(dataArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        jsonList.sort((jsonObjectA, jsonObjectB) -> {
            int compare = 0;
            try {
                if (jsonObjectA.has(key) && jsonObjectB.has(key)) {
                    if (key.equals("attribute")) {
                        compare = jsonObjectA.getString(key).compareTo(jsonObjectB.getString(key));
                    } else {
                        compare = Integer.compare(jsonObjectA.getInt(key), jsonObjectB.getInt(key));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (ascendingCheckBox.isSelected()) {
                return compare;
            } else {
                return -compare;
            }
        });

        dataArray = new JSONArray(jsonList);
    }

    private void updateUI() {
        currentIndex = 0;
        updateCardDetail();
        updateCardImage();
    }

    public static void main(String[] args) {
        try {
            String jsonString = "";

            try {
                jsonString = JsonFileReader.readJsonFromFile(
                        "D:\\Documentos Locales\\6to\\Algoritmos\\practica4\\src\\main\\resources\\archivo.json");
            } catch (Exception e) {
                e.printStackTrace(); // Or handle the error in a way you see fit.
            }

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray dataArray = jsonObject.getJSONArray("data");

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new GUI(dataArray).createAndShowGUI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
