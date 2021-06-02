/*
* Enes Güler
* 18050111005
*
* CENG202 - Lab 9
* */


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

class City{
    // To keep it simple, I define variables as public
    public String name;
    public int plate;
    public boolean isVisited;
    public HashMap<Integer, City> neighbourhood;

    public City(String name, int plate) {
        this.name = name;
        this.plate = plate;
        this.isVisited = false;
        this.neighbourhood = new HashMap<>();
    }

    public void addNeighbour(City neighbour){
        neighbourhood.put(neighbour.plate, neighbour);
    }
}

public class Main {

    public static int plateOfaCityName(String cityName, HashMap<Integer, City> cities) throws Exception {
        // The function returns the plate of given city
        for(Map.Entry<Integer, City> city: cities.entrySet())
            if (city.getValue().name.equalsIgnoreCase(cityName)){
                city.getValue().isVisited = true;
                makeNeighboursVisited(city.getValue().neighbourhood);
                return city.getKey();
            }
        throw new Exception("There is no city named " + cityName + " in Turkey!");
    }

    public static void makeNeighboursVisited(HashMap<Integer, City> cities){
        // The function makes visited given all cities
        for(Map.Entry<Integer, City> city: cities.entrySet())
            city.getValue().isVisited = true;
    }

    public static boolean isFinish(HashMap<Integer, City> cities){
        // The function checks is all cities visited or not
        for(Map.Entry<Integer, City> city: cities.entrySet())
            if (!city.getValue().isVisited)
                return false;
        return true;
    }

    public static void printLevels(ArrayList<HashMap<Integer, City>> levels){
        // The function prints all cities with their relative level
        int i = 1;
        for(HashMap<Integer, City> level: levels){
            System.out.println("---------------------------------");
            System.out.println("Neighbourhood Level " + i);
            System.out.println("---------------------------------");
            for(Map.Entry<Integer, City> city: level.entrySet()){
                System.out.println(city.getValue().name + " " + city.getValue().plate);
            }
            i++;
        }
    }

    public static void main(String[] args) throws Exception {
        HashMap<Integer ,City> cities = new HashMap<>();

        // Reading cities from txt to hashmap with their names and plates
        BufferedReader br = new BufferedReader(new FileReader("cities.txt"));
        String cityName;
        int cityPlate = 1;
        while ((cityName = br.readLine()) != null)
            cities.put(cityPlate, new City(cityName, cityPlate++));

        // Adding all cities neighbours to their neighbour hashmap
        br = new BufferedReader(new FileReader("graph.txt"));
        String line;
        int currentPlate = 1;
        while ((line = br.readLine()) != null) {
            String[] s = line.split(" ");
            for(int i = 0; i < 81; i++)
                if (s[i].equals("1"))
                    cities.get(currentPlate).addNeighbour(cities.get(i+1));
            currentPlate++;
        }

        // Getting input
        Scanner scanner = new Scanner(System.in);

        System.out.print("What is your target city: ");
        String theCity = scanner.nextLine();

        int thePlate = plateOfaCityName(theCity, cities);

        // Starting the level processes
        ArrayList<HashMap<Integer, City>> levels = new ArrayList<>();

        // Level 1
        levels.add(cities.get(thePlate).neighbourhood);

        //Other levels
        int i = 0;
        while(!isFinish(cities)){
            HashMap<Integer, City> level_i = new HashMap<>();

            for(Map.Entry<Integer, City> city: levels.get(i).entrySet())
                for(Map.Entry<Integer, City> _city: city.getValue().neighbourhood.entrySet())
                    if (! _city.getValue().isVisited){
                        level_i.put(_city.getKey(), _city.getValue());
                        _city.getValue().isVisited = true;
                    }

            levels.add(level_i);
            i++;
        }

        printLevels(levels);
    }
}
