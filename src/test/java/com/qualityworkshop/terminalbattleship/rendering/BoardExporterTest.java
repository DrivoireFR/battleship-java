package com.qualityworkshop.terminalbattleship.rendering;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoardExporterTest {

    @Test
    void shouldExportBothBoardsToFile(@TempDir Path tempDir) throws IOException {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(1, 1)));
        Path exportPath = tempDir.resolve("export.txt");

        BoardExporter.export(playerBoard, computerBoard, exportPath);

        assertThat(exportPath).exists();
        String content = Files.readString(exportPath);
        assertThat(content).contains("=== EXPORT PLATEAU");
        assertThat(content).contains("Votre grille:");
        assertThat(content).contains("Grille adverse:");
        assertThat(content).contains("A B C D E F");
    }

    @Test
    void shouldIncludeTimestampInExport(@TempDir Path tempDir) throws IOException {
        Board playerBoard = Board.withShips(6, List.of());
        Board computerBoard = Board.withShips(6, List.of());
        Path exportPath = tempDir.resolve("export.txt");

        BoardExporter.export(playerBoard, computerBoard, exportPath);

        String content = Files.readString(exportPath);
        // Vérifier format date: 2025-11-20 15:30:45
        assertThat(content).containsPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    }

    @Test
    void shouldExportPlayerBoardWithShipsRevealed(@TempDir Path tempDir) throws IOException {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of());
        Path exportPath = tempDir.resolve("export.txt");

        BoardExporter.export(playerBoard, computerBoard, exportPath);

        String content = Files.readString(exportPath);
        assertThat(content).contains("S"); // Ship visible on player board
    }

    @Test
    void shouldExportComputerBoardWithFog(@TempDir Path tempDir) throws IOException {
        Board playerBoard = Board.withShips(6, List.of());
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Path exportPath = tempDir.resolve("export.txt");

        BoardExporter.export(playerBoard, computerBoard, exportPath);

        String content = Files.readString(exportPath);
        // Vérifier que les navires adverses sont cachés
        int playerSectionStart = content.indexOf("Votre grille:");
        int computerSectionStart = content.indexOf("Grille adverse:");
        String computerSection = content.substring(computerSectionStart);
        // Dans la section adverse, on ne devrait voir que ~ X O (pas de S)
        assertThat(computerSection).doesNotContain("S ~"); // Pas de navire visible
    }

    @Test
    void shouldFailGracefullyOnInvalidPath() {
        Board playerBoard = Board.withShips(6, List.of());
        Board computerBoard = Board.withShips(6, List.of());
        Path invalidPath = Path.of("/invalid/path/that/does/not/exist/export.txt");

        assertThatThrownBy(() -> BoardExporter.export(playerBoard, computerBoard, invalidPath))
                .isInstanceOf(IOException.class);
    }

    @Test
    void shouldOverwriteExistingFile(@TempDir Path tempDir) throws IOException {
        Board playerBoard = Board.withShips(6, List.of());
        Board computerBoard = Board.withShips(6, List.of());
        Path exportPath = tempDir.resolve("export.txt");

        // Premier export
        Files.writeString(exportPath, "OLD CONTENT");

        // Deuxième export (écrase)
        BoardExporter.export(playerBoard, computerBoard, exportPath);

        String content = Files.readString(exportPath);
        assertThat(content).doesNotContain("OLD CONTENT");
        assertThat(content).contains("EXPORT PLATEAU");
    }

    @Test
    void shouldHandleMiniBoard(@TempDir Path tempDir) throws IOException {
        Board playerBoard = Board.withShips(4, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(4, List.of(new Coordinate(1, 1)));
        Path exportPath = tempDir.resolve("export.txt");

        BoardExporter.export(playerBoard, computerBoard, exportPath);

        String content = Files.readString(exportPath);
        // Vérifier que le header contient A B C D
        assertThat(content).contains("A B C D");
        // Vérifier que le header ne contient PAS "E F" (colonnes 6x6)
        assertThat(content).doesNotContain("A B C D E F");
        // Ou plus précis : vérifier qu'il n'y a pas de ligne avec E F après le header
        assertThat(content).containsPattern("  A B C D\\s*\\n");
    }
}