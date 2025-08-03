import com.opencsv.CSVWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        ArrayList<Double>[] oldArr = getValuesFromCsv(uploadCsvFile());
        List<String[]> newArr = new ArrayList<>();
        for(int i = 0; i<6; i++){
            newArr.add(new String[]{oldArr[0].get(i)+"", oldArr[1].get(i)+"", oldArr[2].get(i)+"", oldArr[3].get(i)+""});
        }
        for(int i = 6; i< oldArr[0].size()-6; i++){
            double total = 0;
            total += 0.5*oldArr[3].get(i-6);
            for(int j = -5; j<6; j++){
                total+= oldArr[3].get(i+j);
            }
            total += 0.5*oldArr[3].get(i+6);
            System.out.println(total);
            newArr.add(new String[]{oldArr[0].get(i)+"", oldArr[1].get(i)+"", oldArr[2].get(i)+"", ""+(total/12.0)});

        }
        for(int i = oldArr[0].size()-6; i<oldArr[0].size(); i++){
            newArr.add(new String[]{oldArr[0].get(i)+"", oldArr[1].get(i)+"", oldArr[2].get(i)+"", oldArr[3].get(i)+""});
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter("monthlySmoothedAvgs.csv"))) {
            writer.writeAll(newArr);
            System.out.println("CSV file created successfully with OpenCSV");
        } catch (IOException e) {
            System.err.println("Error writing CSV file with OpenCSV: " + e.getMessage());
        }
    }
    public static File uploadCsvFile(){
        //create JFileChooser
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open File");

        //Filters files shown to only csv files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("csv", "csv", "CSV");
        chooser.setFileFilter(filter);

        //Open chooser and get file
        int returnValue = chooser.showOpenDialog(new JFrame());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            System.out.println(file.getName());
            return file;
        }else{
            System.out.println("Something went wrong: Code "+returnValue);
            return null;
        }
    }

    public static ArrayList<Double>[] getValuesFromCsv(File file){
        try {
            ArrayList<Double>[] arr = new ArrayList[4];
            Scanner scan = new Scanner(file);
            ArrayList<Double> vals = new ArrayList<>();
            ArrayList<Double> year = new ArrayList<>();
            ArrayList<Double> month = new ArrayList<>();
            ArrayList<Double> day = new ArrayList<>();
//            scan.nextLine(); //So titles aren't taken as data
            while (scan.hasNextLine()){
                String line = scan.nextLine();
                String[] fields = line.split(",");
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].replace("\"", "").trim(); // strip quotes and whitespace
                }
                if(fields.length == 4) {
                    year.add(Double.parseDouble(fields[0]));
                    month.add(Double.parseDouble(fields[1]));
                    day.add(Double.parseDouble(fields[2]));
                    vals.add(Double.parseDouble(fields[3]));
                }
            }
            arr[0] = year;
            arr[1] = month;
            arr[2] = day;
            arr[3] = vals;

            return arr;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
