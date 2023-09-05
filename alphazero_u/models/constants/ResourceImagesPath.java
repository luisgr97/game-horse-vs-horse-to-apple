package alphazero_u.models.constants;

public enum ResourceImagesPath {
    //Options
    LIGHT_SQUARE("LIGHT_SQUARE", "juego-Alpha-zero\\alphazero_u\\resources\\images\\cuadro_claro.png"),
    DARK_SQUARE("DARK_SQUARE", "juego-Alpha-zero\\alphazero_u\\resources\\images\\cuadro_oscuro.png"),
    DARK_HORSE("DARK_HORSE", "juego-Alpha-zero\\alphazero_u\\resources\\images\\Caballo_negro.gif"),
    LIGHT_HORSE("LIGHT_HORSE", "juego-Alpha-zero\\alphazero_u\\resources\\images\\caballo_blanco.gif");
    //Attributes
    private String reference;
    private String path;
    //Constructor
    ResourceImagesPath(String name, String path){
        this.reference= name;
        this.path= path;
    }
    //Methods
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    


}
