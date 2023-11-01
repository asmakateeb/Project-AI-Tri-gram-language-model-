package sample;

import com.opencsv.CSVWriter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {
   static double probability =0;

    static int one = 0;
    public static ArrayList<Table> tablep = new ArrayList<>();
    public static ArrayList<Table> tableForWords = new ArrayList<>();

    TextArea inputtext;
    Label result;
    DecimalFormat df = new DecimalFormat("###.##");

    @Override
    public void start(Stage ps) throws IOException {
        Pane root = new Pane();
        GridPane container = new GridPane();

        VBox option = new VBox();
        option.setLayoutX(200);
        option.setLayoutY(120);
        Button makemodel = new Button("New languge model");

        inputtext = new TextArea();
        inputtext.setPrefHeight(250);
        inputtext.setPrefWidth(450);
        inputtext.setLayoutY(200);
        inputtext.setLayoutX(200);

        Button go = new Button("GO");
        go.setLayoutX(445);
        go.setLayoutY(460);
        go.setPrefWidth(200);
        go.setPrefHeight(50);
        go.setStyle("-fx-base: gray;");
        result = new Label();
        result.setLayoutX(200);
        result.setLayoutY(470);
        result.setPrefWidth(300);

        go.setOnAction(e -> {
            try {
                spell();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        makemodel.setOnAction(e -> {
            try {
                read("corpus.txt");
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            removeDouble();
        });
        makemodel.setDisable(true);

        if(tablep.size()==0){
            makemodel.setDisable(false);
        }
        makemodel.setPrefWidth(300);
        makemodel.setLayoutX(20);
        makemodel.setPrefHeight(50);
        makemodel.setStyle("-fx-base: coral;");

        option.getChildren().add(makemodel);
        container.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        root.getChildren().addAll(option, container, inputtext, go,result);

        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.BLACK);
        ps.setScene(scene);
        ps.setTitle("Project 1: Probability using language model");
        ps.show();
    }


    public static void main(String[] args) throws FileNotFoundException {

        launch(args);
    }

    public static void read(String file) throws IOException {
        File filename = new File(file);
        if (filename.exists()) {
            Scanner s = new Scanner(filename);
            while (s.hasNext()) {
                String st = s.nextLine();
                String tokens[] = st.split(" ");
                for (int i = 0; i < tokens.length; i++) {
                    tablep.add(new Table(tokens[i]));
                    one++;
                }
            }

        }
        int size = tablep.size();
        for (int i = 0; i < size - 1; i++) {
            String doub = tablep.get(i).getWord() + " " + tablep.get(i + 1).getWord();
            tablep.add(new Table(doub));
        }

        for (int i = 0; i < size - 2; i++) {
            String doub = tablep.get(i).getWord() + " " + tablep.get(i + 1).getWord() + " "
                    + tablep.get(i + 2).getWord();
            tablep.add(new Table(doub));
        }
        for (int i = 0; i < tablep.size(); i++) {
            tablep.get(i).setCount(countR(tablep.get(i).getWord()));
        }
        fillprob();

    }

    static int countR(String s) {
        int count = 0;
        for (int i = 0; i < tablep.size(); i++) {
            if (tablep.get(i).getWord().trim().equals(s)) {
                count++;
            }
        }
        return count;
    }

    public static void removeDouble() {
        int counter = 0;
        for (int i = 0; i < tablep.size(); i++) {
            for (int j = 1 + i; j < tablep.size(); j++) {
                if (tablep.get(j).getWord().equals(tablep.get(i).getWord())) {
                    tablep.remove(tablep.get(j));
                    counter++;

                }
            }
        }
    }

    static int search(String s) {
        for (int i = 0; i < tablep.size(); i++) {
            if (tablep.get(i).getWord().equals(s))
                return i;
        }
        return 0;
    }

    static void fillprob() throws IOException {
        for (int i = 0; i < tablep.size(); i++) {
            if (tablep.get(i).getLegth() == 1) {
                tablep.get(i).setProp(Double.valueOf( tablep.get(i).getCount() / one));
            } else if (tablep.get(i).getLegth() == 2) {
                String s[] = tablep.get(i).getWord().split(" ");
                int tow = search(s[0]);
                if ((tablep.get(i).getCount()) / (tablep.get(tow).getCount()) != 0)
                    tablep.get(i).setProp(Double.valueOf (tablep.get(i).getCount()) / (tablep.get(tow).getCount()));
            } else if (tablep.get(i).getLegth() == 3) {
                String s[] = tablep.get(i).getWord().split(" ");
                int tow = search(s[2] + " " + s[1]);
                if ((tablep.get(i).getCount()) / (tablep.get(tow).getCount()) != 0)
                    tablep.get(i).setProp(Double.valueOf (tablep.get(i).getCount()) / (tablep.get(tow).getCount()));
            }
        }
        FileWriter outputfile  = new FileWriter("model.csv");

        // create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outputfile );
        for (int i = 0; i < tablep.size(); i++) {
            writer.writeNext(new String[]{tablep.get(i).getWord() +","+tablep.get(i).getCount()+","+tablep.get(i).getLegth()+","+tablep.get(i).getProp()});
        }

        writer.close();


    }
    public void readCSV() throws IOException {
        {
//parsing a CSV file into Scanner class constructor
            BufferedReader br = new BufferedReader(new FileReader("model.csv"));
            String line = "";
            Table currentTable;
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] tabels = line.split(",");    // use comma as separator
                if(tabels[3].length()>9){
                    currentTable = new Table(tabels[0],Double.parseDouble(tabels[1]),Double.parseDouble(tabels[2]),Double.parseDouble(tabels[3].substring(0,tabels[3].length()-4)));
                    tableForWords.add(currentTable);
                }
                else {
                    currentTable = new Table(tabels[0],Double.parseDouble(tabels[1]),Double.parseDouble(tabels[2]),Double.parseDouble(tabels[3].substring(0,tabels[3].length()-1)));
                    tableForWords.add(currentTable);
                }
            }
        }
    }
    public void spell() throws IOException {
        readCSV();

        String str = inputtext.getText();
        String[] words = str.split(" ");
        String seq;
        if(words.length==1){
            find(words[0]);
        }
        if(words.length==2){
            find(words[0]+" "+words[1]);
        }
        if(words.length==3){
            find(words[0]+" "+words[1]+" "+words[2]);
        }
        if(words.length>3){
            findForScentnce(str);
        }
    }

    //words.add();
    public void findForScentnce(String str1) {
        String[] words = str1.split(" ");
        for(int j =0 ; j<words.length ; j++){
            for (int i = 0; i < tablep.size(); i++) {
                if (tablep.get(i).getWord().equalsIgnoreCase(words[j])){
                    probability += probability+tablep.get(i).getProp();
                }
            }
        }
        result.setText("Probability  "+String.valueOf(df.format(probability)));
    }
    public void find(String str1) {
        System.out.println(tableForWords.get(0));
        ArrayList<Table> top5 = new ArrayList<>();
        Table[] tab;
        ArrayList<String> y = new ArrayList<>();
        for (int i = 0; i < tablep.size(); i++) {
           if (tablep.get(i).getWord().equalsIgnoreCase(str1)){
               probability += probability+tablep.get(i).getProp();
           }
        }
        result.setText("Probability  "+String.valueOf(df.format(probability)));
    }


}






