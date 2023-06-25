import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TSPSolverApp extends JFrame {
    private List<City> cities;
    private JComboBox<String> locationComboBox;
    private JTextArea outputTextArea;

    public TSPSolverApp() {
        setTitle("Traveling Salesman Problem Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 300);
        setLayout(new BorderLayout());

        initializeCities();

        createLocationComboBox();
        createButton();
        createOutputTextArea();

        setVisible(true);
    }

    private void initializeCities() {
        cities = new ArrayList<>();
        cities.add(new City("Chashara", 23.62378111033078, 90.4998858107671));
        cities.add(new City("Signboard", 23.69391541124286, 90.48129952812872));
        cities.add(new City("Kachpur", 23.705615427072086, 90.52143606398302));
        cities.add(new City("Rupsi", 23.740150837124784, 90.52488992277914));
        cities.add(new City("Gausia", 23.78396120550584, 90.56304178255297));
        cities.add(new City("Green University", 23.830856212703736, 90.566784362937));
        cities.add(new City("Kuril", 23.821257180117065, 90.4228955621415));
        cities.add(new City("Mirpur", 23.80813946407457, 90.3669981466151));
        cities.add(new City("Gulistan", 23.725273760373703, 90.41187846732028));
    }

    private void createLocationComboBox() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Current Location:");
        locationComboBox = new JComboBox<>();
        for (City city : cities) {
            locationComboBox.addItem(city.getName());
        }

        panel.add(label);
        panel.add(locationComboBox);
        add(panel, BorderLayout.NORTH);
    }

    private void createButton() {
        JPanel panel = new JPanel();
        JButton solveButton = new JButton("Find Shortest Path");

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCityName = locationComboBox.getSelectedItem().toString();
                City selectedCity = null;
                for (City city : cities) {
                    if (city.getName().equals(selectedCityName)) {
                        selectedCity = city;
                        break;
                    }
                }
                if (selectedCity != null) {
                    List<City> shortestPath = solveTSP(selectedCity);
                    displayShortestPath(shortestPath);
                }
            }
        });

        panel.add(solveButton);
        add(panel, BorderLayout.CENTER);
    }

    private List<City> solveTSP(City startCity) {
        List<City> path = new ArrayList<>();
        path.add(startCity);

        List<City> unvisitedCities = new ArrayList<>(cities);
        unvisitedCities.remove(startCity);

        while (!unvisitedCities.isEmpty()) {
            City currentCity = path.get(path.size() - 1);
            City nearestCity = null;
            double shortestDistance = Double.MAX_VALUE;

            for (City city : unvisitedCities) {
                double distance = calculateDistance(currentCity, city);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    nearestCity = city;
                }
            }

            path.add(nearestCity);
            unvisitedCities.remove(nearestCity);
        }

        // Return to the start city
        path.add(startCity);

        return path;
    }

    private double calculateDistance(City city1, City city2) {
        double lat1 = Math.toRadians(city1.getLatitude());
        double lon1 = Math.toRadians(city1.getLongitude());
        double lat2 = Math.toRadians(city2.getLatitude());
        double lon2 = Math.toRadians(city2.getLongitude());

        double earthRadius = 6371.0; // Earth's radius in kilometers

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadius * c;
        return distance;
    }

    private void displayShortestPath(List<City> path) {
        StringBuilder sb = new StringBuilder();
        for (City city : path) {
            sb.append(city.getName()).append(" -> ");
        }
        sb.setLength(sb.length() - 4);

        outputTextArea.setText(sb.toString());
    }

    private void createOutputTextArea() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Shortest Path:");
        outputTextArea = new JTextArea(10, 100);
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        panel.add(label);
        panel.add(scrollPane);
        add(panel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TSPSolverApp();
            }
        });
    }
}
