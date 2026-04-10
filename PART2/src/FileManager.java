import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * FileManager – handles all file input and output for the application.
 * Responsibilities: save/load rider profiles (JSON), export/import journeys (CSV),
 * and export end-of-day summaries as both CSV and human-readable text.
 * All file operations are contained here to keep other classes free of I/O concerns.
 */
public class FileManager {

    private static final String CSV_HEADER =
            "ID,Date,PassengerName,FromZone,ToZone,TimeBand,PassengerType,"
            + "BaseFare,DiscountApplied,ChargedFare";

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Rider Profile (JSON) ──────────────────────────────────────

    /**
     * Saves a RiderProfile to a JSON file.
     *
     * @param profile  the profile to save
     * @param filePath the destination file path
     */
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

    /**
     * Loads a RiderProfile from a JSON file.
     * Returns null if the file is missing or cannot be parsed.
     *
     * @param filePath path to the JSON profile file
     * @return a RiderProfile, or null on failure
     */
    public RiderProfile loadProfile(String filePath) {
        File profileFile = new File(filePath);
        if (!profileFile.exists()) {
            System.out.println("Profile file not found: " + filePath);
            return null;
        }

        String name = "";
        String passengerType = "";
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

    // ── Journey CSV export ────────────────────────────────────────

    /**
     * Exports all journeys to a CSV file.
     *
     * @param journeys   list of journeys to export
     * @param riderName  rider name (used in filename)
     */
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

    // ── Journey CSV import ────────────────────────────────────────

    /**
     * Imports journeys from a CSV file.
     * Skips and counts any rows that fail validation.
     *
     * @param filePath path to the CSV file
     * @return list of Journey objects parsed from the file
     */
    public List<String[]> importJourneysCsv(String filePath) {
        List<String[]> rows = new ArrayList<>();
        File importFile = new File(filePath);

        if (!importFile.exists()) {
            System.out.println("Import file not found: " + filePath);
            return rows;
        }

        int imported = 0;
        int rejected = 0;
        final int EXPECTED_COLUMNS = 10;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != EXPECTED_COLUMNS) {
                    rejected++;
                    continue;
                }
                rows.add(parts);
                imported++;
            }
        } catch (IOException e) {
            System.out.println("Error reading import file: " + e.getMessage());
        }

        System.out.println("Import complete: " + imported
                + " imported, " + rejected + " rejected.");
        return rows;
    }

    // ── Summary exports ───────────────────────────────────────────

    /**
     * Exports the end-of-day summary as a human-readable text file.
     *
     * @param summaryText the formatted summary string
     * @param riderName   rider name (used in filename)
     */
    public void exportSummaryText(String summaryText, String riderName) {
        String fileName = buildFileName(riderName, "summary", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(summaryText);
            System.out.println("Summary saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving summary: " + e.getMessage());
        }
    }

    /**
     * Exports journey line items as a CSV summary report.
     *
     * @param journeys  list of journeys to include
     * @param riderName rider name (used in filename)
     */
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

    // ── Private helpers ───────────────────────────────────────────

    /**
     * Builds a timestamped filename for exports.
     * Format: riderName_type_dd-MM-yyyy.extension
     */
    private String buildFileName(String riderName, String type, String extension) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String safeName = riderName.replaceAll("\\s+", "_");
        return safeName + "_" + type + "_" + date + extension;
    }

    /**
     * Extracts the string value from a simple JSON line.
     * Handles format: "key": "value"
     */
    private String extractJsonValue(String line) {
        int start = line.lastIndexOf("\"", line.lastIndexOf(":") - 1);
        // Find value between the last pair of quotes
        int valueStart = line.indexOf("\"", line.indexOf(":")) + 1;
        int valueEnd = line.lastIndexOf("\"");
        if (valueStart > 0 && valueEnd > valueStart) {
            return line.substring(valueStart, valueEnd);
        }
        return "";
    }
}
