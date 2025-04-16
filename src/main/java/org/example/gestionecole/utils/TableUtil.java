package org.example.gestionecole.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


public class TableUtil {
    public static TableView<ObservableList<String>>
    FillTable(ResultSet rs) throws Exception
    {
        TableView<ObservableList<String>> tableView = new TableView<>();

        // Récupérer les métadonnées du ResultSet
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Créer dynamiquement les colonnes
        for (int i = 1; i <= columnCount; i++) {
            final int colIndex = i - 1; // Index basé sur 0 pour ObservableList
            String columnName = metaData.getColumnName(i);

            TableColumn<ObservableList<String>, String>
                    column = new TableColumn<>(columnName);
            column.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().get(colIndex))
            );
            tableView.getColumns().add(column);
        }

        // Ajouter les lignes du ResultSet dans une ObservableList
        ObservableList<ObservableList<String>>
                data = FXCollections.observableArrayList();
        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getString(i)); // Ajouter chaque valeur de colonne
            }
            data.add(row);
        }
        tableView.setItems(data);

        return tableView;
    }
}