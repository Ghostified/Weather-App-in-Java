package ProgramFiles;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;


public class WeatherAppGui extends JFrame{
    public WeatherAppGui(){
        //set up the gui with title
        super( "Weather App");

        //configure gui to end the program process once it has been closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //set the size of the gui in pixels
        setSize(450, 650);

        //load the gui at the center of the screen
        setLocationRelativeTo(null);

        //make the layout manager null to manually position our components within the gui
        setLayout(null);

        //prevent resize of the gui
        setResizable(false);

        //Call my gui component
        addGuiComponents();

    }

    private void addGuiComponents(){
        //search field
        JTextField searchTextField = new JTextField();

        //set the location and size of the component
        searchTextField.setBounds(15, 15, 351, 45);

        //change the font size and style 
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));

        add(searchTextField);

        //search button
        JButton searchButton = new JButton(loadImage("C:\\Users\\allan.branson\\Projects\\Weather App In Java\\Images\\search.png"));

        //Change the cursor to a hand cursor when hovering over this button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 47,45);
        add(searchButton);

        //Weather Image
        JLabel weatherConditionImage = new JLabel(loadImage("C:\\Users\\allan.branson\\Projects\\Weather App In Java\\Images\\cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450,217);
        add(weatherConditionImage);

        //Temprature Text
        JLabel tempratureText = new JLabel("10 c");
        tempratureText.setBounds(0,350,450,54);
        tempratureText.setFont(new Font ("Dialog", Font.BOLD, 48));

        //Center the Temprature Text
        tempratureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(tempratureText);

        //Description oif the weather Condition
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0,405,450 ,36);
        weatherConditionDesc.setFont( new Font ("Dialog",Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        //Humidity Images
        JLabel humidityImage = new JLabel (loadImage("C:\\Users\\allan.branson\\Projects\\Weather App In Java\\Images\\humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);

        //Humidity Text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90,500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN,16));
        add(humidityText);

        //windspeed Image
        JLabel windspeedImage = new JLabel (loadImage("C:\\Users\\allan.branson\\Projects\\Weather App In Java\\Images\\windspeed.png"));
        windspeedImage.setBounds(220,500,74,66);
        add(windspeedImage);

        //Windspeed Text
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds (310,500,85,55);
        windspeedText.setFont(new Font("Dialog",Font.PLAIN, 16));
        add(windspeedText);


    
   }
    //Used to create images in our gui components
    private ImageIcon loadImage(String resourcePath){
        try{
            //read the image file from the path given 
            BufferedImage image = ImageIO.read(new File(resourcePath));

            //returns an image icon so that our component can render it
            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
            }
            System.out.println("Could not find resource");
            
        return null; 
    }
    
}
