import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String CSV_HEADER =
            "ID,Date,PassengerName,FromZone,ToZone,TimeBand,PassengerType,"
                    + "BaseFare,DiscountApplied,ChargedFare";

    private static final int CSV_COLUMN_COUNT = 10;

    public void saveProfile(RiderProfile profile, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("{");
            writer.newLine();
            writer.write("  \"name\": \"" + profile.getName() + "\",");
            writer.newLine();
            writer.write("  \"passengerType\": \"" + profile.getPassengerType() + "\",");
            writer.newLine();
            writer.write("  \"defaultPayment\": \"" + profile.getDefaultPayment() + "\"");
            writer.newLine();
            writer.write("}");
            System.out.println("Profile saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving profile: " + e.getMessage());
        }
    }

    public RiderProfile loadProfile(String filePath) {
        File profileFile = new File(filePath);
        if (!profileFile.exists()) {
            System.out.println("Profile file not found: " + filePath);
            return null;
        }
        return parseProfileFile(filePath);
    }

    private RiderProfile parseProfileFile(String filePath) {
        String name           = "";
        String passengerType  = "";
        String defaultPayment = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.contains("\"name\"")) {
                    name = extractJsonValue(line);
                } else if (line.contains("\"passengerType\"")) {
                    passengerType = extractJsonValue(line);
                } else if (line.contains("\"defaultPayment\"")) {
                    defaultPayment = extractJsonValue(line);
                }
            }
            System.out.println("Profile loaded for: " + name);
            return new RiderProfile(name, passengerType, defaultPayment);
        } catch (IOException e) {
            System.out.println("Error loading profile: " + e.getMessage());
            return null;
        }
    }

    public void exportJourneysCsv(List<Journey> journeys, String riderName) {
        String fileName = buildFileName(riderName, "journeys", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Journey journey : journeys) {
                writer.write(journey.toCsvRow());
                writer.newLine();
            }
            System.out.println("Journeys exported to " + fileName);
        } catch (IOException e) {
            System.out.println("Error exporting journeys: " + e.getMessage());
        }
    }

    public List<String[]> importJourneysCsv(String filePath) {
        List<String[]> rows = new ArrayList<>();
        File importFile = new File(filePath);
        if (!importFile.exists()) {
            System.out.println("Import file not found: " + filePath);
            return rows;
        }
        return readCsvRows(filePath, rows);
    }

    private List<String[]> readCsvRows(String filePath, List<String[]> rows) {
        int imported = 0;
        int rejected = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header row
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != CSV_COLUMN_COUNT) {
                    rejected++;
                } else {
                    rows.add(parts);
                    imported++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading import file: " + e.getMessage());
        }
        System.out.println("Import complete: " + imported
                + " imported, " + rejected + " rejected.");
        return rows;
    }

    public void exportSummaryText(String summaryText, String riderName) {
        String fileName = buildFileName(riderName, "summary", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(summaryText);
            System.out.println("Summary saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving summary: " + e.getMessage());
        }
    }

    public void exportSummaryCsv(List<Journey> journeys, String riderName) {
        String fileName = buildFileName(riderName, "summary", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Journey journey : journeys) {
                writer.write(journey.toCsvRow());
                writer.newLine();
            }
            System.out.println("Summary CSV saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving summary CSV: " + e.getMessage());
        }
    }

    private String buildFileName(String riderName, String type, String extension) {
        String date     = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String safeName = riderName.replaceAll("\\s+", "_");
        return safeName + "_" + type + "_" + date + extension;
    }

    private String extractJsonValue(String line) {
        int colonIndex  = line.indexOf(":");
        int valueStart  = line.indexOf("\"", colonIndex) + 1;
        int valueEnd    = line.lastIndexOf("\"");
        if (valueStart > 0 && valueEnd > valueStart) {
            return line.substring(valueStart, valueEnd);
        }
        return "";
    }
}