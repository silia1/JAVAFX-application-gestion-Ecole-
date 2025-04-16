package org.example.gestionecole.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.entities.Inscription;
import org.example.gestionecole.entities.Professeur;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PDFExporter {

    public static void exportEtudiantListToPdf(String filePath, List<Etudiant> etudiantList) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();

        // Add Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Liste des Étudiants", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // Add spacing

        // Create table
        PdfPTable table = new PdfPTable(6); // 6 columns
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // Add table headers
        String[] headers = {"Matricule", "Nom", "Prénom", "Date de Naissance", "Email", "Promotion"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Add data rows
        for (Etudiant etudiant : etudiantList) {
            table.addCell(etudiant.getMatricule());
            table.addCell(etudiant.getNom());
            table.addCell(etudiant.getPrenom());
            table.addCell(etudiant.getDateNaissance().toString());
            table.addCell(etudiant.getEmail());
            table.addCell(etudiant.getPromotion());
        }

        document.add(table);
        document.close();
    }

    public static void exportProfesseurListToPdf(String filePath, List<Professeur> professeurList) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();

        // Add Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Liste des Professeurs", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // Add spacing

        // Create table
        PdfPTable table = new PdfPTable(3); // 3 columns: Nom, Prénom, Spécialité
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // Add table headers
        String[] headers = {"Nom", "Prénom", "Spécialité"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Add data rows
        for (Professeur professeur : professeurList) {
            table.addCell(professeur.getNom());
            table.addCell(professeur.getPrenom());
            table.addCell(professeur.getSpecialite());
        }

        document.add(table);
        document.close();
    }

    public static void exportInscriptionListToPdf(String filePath, List<Inscription> inscriptionList) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();

        // Add Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Liste des Inscriptions", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // Add spacing

        // Create table
        PdfPTable table = new PdfPTable(3); // 3 columns: Étudiant, Module, Date d'Inscription
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // Add table headers
        String[] headers = {"Étudiant", "Module", "Date d'Inscription"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Add data rows
        for (Inscription inscription : inscriptionList) {
            table.addCell(inscription.getEtudiantName());
            table.addCell(inscription.getModuleName());
            table.addCell(inscription.getDateInscription().toString());
        }

        document.add(table);
        document.close();
    }


}
