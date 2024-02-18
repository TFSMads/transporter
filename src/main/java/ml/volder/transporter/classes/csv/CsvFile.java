package ml.volder.transporter.classes.csv;

import ml.volder.unikapi.UnikAPI;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class CsvFile {

    public class CsvEntry {
        public String entryValue;
        public String columnName;
        public int row;

        public CsvEntry(String entryValue, String columnName, int row) {
            this.entryValue = entryValue;
            this.columnName = columnName;
            this.row = row;
        }
    }

    private class ColumnInfo {
        public String columnName;
        public int column;

        public ColumnInfo(String columnName, int column) {
            this.columnName = columnName;
            this.column = column;
        }
    }

    private final Map<ColumnInfo, List<CsvEntry>> columnes = new HashMap<>();
    private final char seperator;

    private File file;

    public static CsvFile fromFile(File file) {
        return fromFile(file, ',');
    }

    public static CsvFile fromFile(File file, char seperator) {
        try {
            CsvFile csvFile = new CsvFile(Files.newInputStream(file.toPath()), seperator);
            csvFile.file = file;
            return csvFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CsvFile(InputStream csvInputStream, char seperator) {
        this(new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8)), seperator);
    }

    public CsvFile(BufferedReader br, char seperator) {
        this.seperator = seperator;
        try {
            String line = br.readLine();
            //Create columnes from first line
            String[] columnNames = line.split(seperator + "");
            ColumnInfo[] columnInfos = new ColumnInfo[columnNames.length];
            int column = 0;
            for (String columnName : columnNames) {
                columnInfos[column] = new ColumnInfo(columnName, column);
                columnes.put(columnInfos[column], new ArrayList<>());
                column++;
            }
            //Read all lines and add to columnes
            while (line != null) {
                String[] attributes = line.split(seperator + "");
                for (int i = 0; i < attributes.length; i++) {
                    columnes.get(columnInfos[i]).add(new CsvEntry(attributes[i], columnNames[i], columnes.get(columnInfos[i]).size()));
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return List of all column names
     */
    public List<String> getColumnNames() {
        return columnes.keySet().stream().map(csvEntry -> csvEntry.columnName).collect(Collectors.toList());
    }
    /**
     * @param columnName Name of the column to get values from
     * @return List of all values in the column
     */
    public List<String> getColumnValues(String columnName) {
        for (Map.Entry<ColumnInfo, List<CsvEntry>> entry : columnes.entrySet()) {
            if(entry.getKey().columnName.equals(columnName))
                return entry.getValue().stream().map(csvEntry -> csvEntry.entryValue).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * @param columnName Name of the column to get entries from
     * @return List of all entries in the column
     */
    public List<CsvEntry> getColumnEntries(String columnName) {
        for (Map.Entry<ColumnInfo, List<CsvEntry>> entry : columnes.entrySet()) {
            if(entry.getKey().columnName.equals(columnName))
                return entry.getValue();
        }
        return new ArrayList<>();
    }

    /**
     * @param row Row to get values from
     * @return Map of all values in the row
     */
    public Map<String, String> getRow(int row) {
        Map<String, String> rowMap = new HashMap<>();
        for (List<CsvEntry> entries : columnes.values()) {
            entries.stream().filter(csvEntry -> csvEntry.row == row).forEach(csvEntry -> rowMap.put(csvEntry.columnName, csvEntry.entryValue));
        }
        return rowMap;
    }

    /**
     * @param row Row to get values from
     * @return Map of all entries in the row
     */
    public Map<String, CsvEntry> getRowEntriesMap(int row) {
        Map<String, CsvEntry> rowMap = new HashMap<>();
        for (List<CsvEntry> entries : columnes.values()) {
            entries.stream().filter(csvEntry -> csvEntry.row == row).forEach(csvEntry -> rowMap.put(csvEntry.columnName, csvEntry));
        }
        return rowMap;
    }

    /**
     * @param row Row to get values from
     * @return Map of all entries in the row
     */
    public List<CsvEntry> getRowEntries(int row) {
        CsvEntry[] rowArray = new CsvEntry[getColumnCount()];
        for (Map.Entry<ColumnInfo, List<CsvEntry>> entry : columnes.entrySet()) {
            entry.getValue().stream().filter(csvEntry -> csvEntry.row == row).forEach(csvEntry -> rowArray[entry.getKey().column] = csvEntry);
        }
        return rowArray.length > 0 ? Arrays.stream(rowArray).collect(Collectors.toList()) : new ArrayList<>();
    }

    /**
     * @return Number of rows in the csv file
     */
    public int getRowCount() {
        return columnes.values().stream().findFirst().isPresent() ? columnes.values().stream().findFirst().get().size() : 0;
    }

    public int getColumnCount() {
        return columnes.size();
    }


    public void addRow() {
        columnes.forEach((key, value) -> value.add(new CsvEntry("", key.columnName, value.size())));
    }

    public void removeLastRow() {
        columnes.forEach((key, value) -> value.remove(value.size() - 1));
    }

    /**
     * A method to save the csv file if it was loaded from a file
     */
    public void save() {
        if (file == null)
            return;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            //Write all rows
            for (int row = 0; row < getRowCount(); row++) {
                writer.write(getRowEntries(row).stream().map(csvEntry -> csvEntry.entryValue).collect(Collectors.joining(seperator + "")));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            UnikAPI.LOGGER.warning("Failed to save csv file: " + file.getName());
        }
    }

}
