package uabc.practica4;

public class Card {
    private String name;
    private String type;
    private String description;
    private String race;
    private String archetype;
    private String imageUrlSmall;

    // Constructor
    public Card(String name, String type, String description, String race, String archetype, String imageUrlSmall) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.race = race;
        this.archetype = archetype;
        this.imageUrlSmall = imageUrlSmall;
    }

    // Getters and possibly setters if you need them
    public String getName() { return name; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getRace() { return race; }
    public String getArchetype() { return archetype; }
    public String getImageUrlSmall() { return imageUrlSmall; }
}
