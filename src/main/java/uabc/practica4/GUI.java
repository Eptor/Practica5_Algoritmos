package uabc.practica4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class GUI {

    private JFrame frame;
    private JTextArea cardDetailTextArea;
    private JSONArray dataArray;
    private ListaCircularDoble listaCircularDoble;

    private JButton nextButton;
    private JButton prevButton;
    private JButton showAllButton;
    private JButton sortByLevelButton;
    private JButton sortByAttributeButton;
    private JButton sortByAtkButton;
    private JButton sortByDefButton;
    private JCheckBox ascendingCheckBox;
    private JLabel cardImageLabel;
    private JSONArray originalDatArray;
    private Nodo currentNode = null;

    private JComboBox<String> attributeComboBox;

    public GUI(JSONArray dataArray) {
        this.dataArray = dataArray;
        this.originalDatArray = dataArray;
        fillDoublyCircularList();
    }

    private void fillDoublyCircularList() {
        listaCircularDoble = new ListaCircularDoble();
        for (int i = 0; i < dataArray.length(); i++) {
            try {
                listaCircularDoble.add(dataArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame("Card Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 500);

        cardDetailTextArea = new JTextArea();
        cardDetailTextArea.setEditable(false);
        cardDetailTextArea.setLineWrap(true);
        Insets margin = new Insets(10, 10, 10, 10);
        cardDetailTextArea.setMargin(margin);

        JScrollPane scrollPane = new JScrollPane(cardDetailTextArea);

        cardImageLabel = new JLabel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, cardImageLabel);
        splitPane.setDividerLocation(650); // puedes ajustar este valor como desees
        frame.add(splitPane);

        // Navigation Panel
        JPanel navigationPanel = new JPanel();

        prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> {
            if (currentNode != null) {
                currentNode = listaCircularDoble.getPrev(currentNode);
                updateCardDetail();
                updateCardImage();
            }
        });

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            if (currentNode != null) {
                currentNode = listaCircularDoble.getNext(currentNode);
                updateCardDetail();
                updateCardImage();
            }
        });

        MP3Player mp3Player = new MP3Player(
                "D:\\Documentos Locales\\6to\\Algoritmos\\practica4\\src\\main\\resources\\Yu-Gi-Oh GX.mp3");
        mp3Player.play();

        JButton muteButton = new JButton("Mute");
        muteButton.addActionListener(e -> {
            if (mp3Player.isPlaying()) {
                mp3Player.stop();
                muteButton.setText("Play");
            } else {
                mp3Player.play();
                muteButton.setText("Mute");
            }
        });

        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        navigationPanel.add(muteButton);

        frame.add(navigationPanel, BorderLayout.SOUTH);

        // Toolbar Panel
        JPanel toolbarPanel = new JPanel();

        showAllButton = new JButton("Mostrar todas");
        showAllButton.addActionListener(e -> {
            listaCircularDoble.clear();
            fillDoublyCircularList();
            updateUI();
        });

        sortByLevelButton = new JButton("Mostrar por nivel");
        sortByLevelButton.addActionListener(e -> {
            sortByLevel();
            updateUI();
        });

        sortByAttributeButton = new JButton("Mostrar por atributo");
        sortByAttributeButton.addActionListener(e -> {
            filterByAttribute();
            updateUI();
        });

        sortByAtkButton = new JButton("Ordenar por ataque");
        sortByAtkButton.addActionListener(e -> sortByAtk());

        sortByDefButton = new JButton("Ordenar por defensa");
        sortByDefButton.addActionListener(e -> sortByDef());

        ascendingCheckBox = new JCheckBox("Ascendente");

        toolbarPanel.add(showAllButton);
        toolbarPanel.add(sortByLevelButton);
        toolbarPanel.add(sortByAttributeButton);
        toolbarPanel.add(sortByAtkButton);
        toolbarPanel.add(sortByDefButton);
        toolbarPanel.add(ascendingCheckBox);
        // Dentro del método createAndShowGUI()

        String[] attributes = { "LIGHT", "DARK", "WATER", "FIRE", "EARTH", "WIND" };
        attributeComboBox = new JComboBox<>(attributes);

        navigationPanel.add(attributeComboBox);

        frame.add(toolbarPanel, BorderLayout.NORTH);

        if (listaCircularDoble.size() > 0) {
            currentNode = listaCircularDoble.getHead(); // Initialize currentNode to the head of the list
            updateCardDetail(); // Update the text area with the card details
            updateCardImage(); // Update the label with the card image
        }

        frame.setVisible(true);
        updateCardDetail();
        updateCardImage();
    }

    private void sortByAtk() {
        boolean ascending = ascendingCheckBox.isSelected();
        Nodo current = listaCircularDoble.getHead();
        boolean wasSwapped;
        do {
            wasSwapped = false;
            Nodo node = current;
            for (int i = 0; i < listaCircularDoble.size() - 1; i++) {
                Nodo nextNode = listaCircularDoble.getNext(node);
                try {
                    int currentAtk = node.info.getInt("atk");
                    int nextAtk = nextNode.info.getInt("atk");
                    if ((ascending && currentAtk > nextAtk) || (!ascending && currentAtk < nextAtk)) {
                        // Swap nodes
                        JSONObject tempInfo = node.info;
                        node.info = nextNode.info;
                        nextNode.info = tempInfo;
                        wasSwapped = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                node = nextNode;
            }
        } while (wasSwapped);

        // After sorting, reset the current node and update UI
        currentNode = listaCircularDoble.getHead();
        updateUI();
    }

    private void sortByDef() {
        boolean ascending = ascendingCheckBox.isSelected();
        Nodo current = listaCircularDoble.getHead();
        boolean wasSwapped;
        do {
            wasSwapped = false;
            Nodo node = current;
            for (int i = 0; i < listaCircularDoble.size() - 1; i++) {
                Nodo nextNode = listaCircularDoble.getNext(node);
                try {
                    int currentDef = node.info.getInt("def");
                    int nextDef = nextNode.info.getInt("def");
                    if ((ascending && currentDef > nextDef) || (!ascending && currentDef < nextDef)) {
                        // Swap nodes
                        JSONObject tempInfo = node.info;
                        node.info = nextNode.info;
                        nextNode.info = tempInfo;
                        wasSwapped = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                node = nextNode;
            }
        } while (wasSwapped);

        // After sorting, reset the current node and update UI
        currentNode = listaCircularDoble.getHead();
        updateUI();
    }

    private void sortByLevel() {
        boolean ascending = ascendingCheckBox.isSelected();
        boolean wasSwapped;
        do {
            wasSwapped = false;
            Nodo current = listaCircularDoble.getHead();
            Nodo next;
            for (int i = 0; i < listaCircularDoble.size() - 1; i++) {
                next = listaCircularDoble.getNext(current);
                try {
                    int comparisonResult = Integer.compare(current.info.getInt("level"), next.info.getInt("level"));
                    if (ascending ? comparisonResult > 0 : comparisonResult < 0) {
                        // Swap the info within the nodes
                        JSONObject temp = current.info;
                        current.info = next.info;
                        next.info = temp;
                        wasSwapped = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                current = next;
            }
        } while (wasSwapped);

        // After sorting, reset the current node and update UI
        currentNode = listaCircularDoble.getHead();
        updateUI();
    }

    private void filterByAttribute() {
        String selectedAttribute = (String) attributeComboBox.getSelectedItem();
        // Create a new ListaCircularDoble to store the filtered results
        ListaCircularDoble filteredList = new ListaCircularDoble();

        Nodo current = listaCircularDoble.getHead();
        if (current != null) {
            do {
                try {
                    JSONObject card = current.info;
                    if (card.getString("attribute").equals(selectedAttribute)) {
                        // Add to the filtered list if it matches the attribute
                        filteredList.add(card);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                current = current.next;
            } while (current != listaCircularDoble.getHead()); // Traverse the entire list once
        }

        // Update the main list with the filtered results
        listaCircularDoble = filteredList;
        currentNode = listaCircularDoble.getHead(); // Reset the current node to the head of the filtered list
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
        if (currentNode != null) {
            JSONObject card = currentNode.info;
            cardDetailTextArea.setText(formatCardDetail(card));
        }
    }

    private void updateCardImage() {
        if (currentNode != null) {
            JSONObject card = currentNode.info;

            try {
                // Accediendo al array card_images y obteniendo el primer objeto
                JSONObject cardImagesObject = card.getJSONArray("card_images").getJSONObject(0);
                String imageUrl = cardImagesObject.getString("image_url_small");

                // Obtener y mostrar imagen desde la URL usando URI
                URI uri = new URI(imageUrl);
                ImageIcon imageIcon = new ImageIcon(uri.toURL());
                cardImageLabel.setIcon(imageIcon);

                // Adjust the label size to the size of the image
                cardImageLabel.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
            } catch (JSONException | MalformedURLException | URISyntaxException e) {
                e.printStackTrace();
                // Handle the error scenario, possibly with a default image or error message
                cardImageLabel.setIcon(null);
                cardImageLabel.setText("Error displaying image.");
            }
        } else {
            // If currentNode is null, clear the image
            cardImageLabel.setIcon(null);
            cardImageLabel.setText("No image to display.");
        }
    }

    private void updateUI() {
        currentNode = listaCircularDoble.getHead(); // Reset to the start of the list
        updateCardDetail();
        updateCardImage();
    }

    public static void main(String[] args) {
        String jsonString = "";

        // Read JSON from file
        try {
            jsonString = JsonFileReader.readJsonFromFile(
                    "D:\\Documentos Locales\\6to\\Algoritmos\\practica4\\src\\main\\resources\\archivo.json");
        } catch (Exception e) {
            e.printStackTrace();
            return; // If file reading fails, exit the program
        }

        // Parse the JSON string into an object
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        // Schedule the GUI to be created in the Swing event dispatch thread
        SwingUtilities.invokeLater(() -> {
            try {
                new GUI(dataArray).createAndShowGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}