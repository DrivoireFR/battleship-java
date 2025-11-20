package com.qualityworkshop.terminalbattleship.rendering;

import com.qualityworkshop.terminalbattleship.board.Board;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class BoardExporter {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private BoardExporter() {
    }

    /**
     * Exporte l'état actuel des deux plateaux dans un fichier texte.
     *
     * @param playerBoard   Plateau du joueur (navires révélés)
     * @param computerBoard Plateau de l'ordinateur (brouillard de guerre)
     * @param exportPath    Chemin du fichier de destination
     * @throws IOException Si l'écriture échoue
     */
    public static void export(Board playerBoard, Board computerBoard, Path exportPath) throws IOException {
        String content = buildExportContent(playerBoard, computerBoard);
        Files.writeString(exportPath, content);
    }

    /**
     * Génère une version exportable avec timestamp.
     *
     * @param playerBoard   Plateau du joueur
     * @param computerBoard Plateau de l'ordinateur
     * @param exportDir     Dossier de destination
     * @return Chemin du fichier créé
     * @throws IOException Si l'écriture échoue
     */
    public static Path exportWithTimestamp(Board playerBoard, Board computerBoard, Path exportDir) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "board_export_" + timestamp + ".txt";
        Path exportPath = exportDir.resolve(filename);

        export(playerBoard, computerBoard, exportPath);
        return exportPath;
    }

    private static String buildExportContent(Board playerBoard, Board computerBoard) {
        StringBuilder content = new StringBuilder();

        // En-tête avec timestamp
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        content.append("=== EXPORT PLATEAU - ").append(timestamp).append(" ===\n\n");

        // Grille du joueur (navires révélés)
        content.append("Votre grille:\n");
        content.append(BoardRenderer.render(playerBoard, true));
        content.append("\n");

        // Grille adverse (brouillard de guerre)
        content.append("Grille adverse:\n");
        content.append(BoardRenderer.render(computerBoard, false));

        return content.toString();
    }
}