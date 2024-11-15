package kernel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class MusicFilesProcessor {
    private static final List<String> MUSIC_FILES_EXTENSIONS = Arrays.asList(
            ".m4a",
            ".opus",
            ".aac",
            ".flac",
            ".mpc",
            ".wav",
            ".mp3"
    );

    private static final char[] PATH_AVAILABLE_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final int MAX_LETTERS_MUSIC_FOLDER = 2;
    private static final int MAX_LETTERS_MUSIC_FILE_IN_FOLDER = 2;

    private MusicFilesProcessor() {

    }

    public static void process() throws Exception {
        final List<File> processList = new ArrayList<>();
        final List<String> processListExtensions = new ArrayList<>();
        try (Stream<Path> walkStream = Files.walk(Path.of(Config.getValue(ConfigKeys.SRC_FOLDER_PATH)))) {
            walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
                String sFile = f.getFileName().toString();
                if (sFile.startsWith(".")) {
                    // Ignore hidden files
                    return;
                }
                for (String extension : MUSIC_FILES_EXTENSIONS) {
                    if (sFile.endsWith(extension)) {
                        processList.add(f.toFile());
                        processListExtensions.add(extension);
                        break;
                    }
                }
            });
        }
        if (processList.isEmpty()) {
            System.err.printf("No music to transfer detected from the folder: %s (and its related sub-directories)%n", Config.getValue(ConfigKeys.SRC_FOLDER_PATH));
            return;
        }
        System.out.printf("%d musics to transfer from the folder: %s (and its related sub-directories)%n", processList.size(), Config.getValue(ConfigKeys.SRC_FOLDER_PATH));
        int progressPercentage = 0;
        for (int i = 0; i < processList.size(); i++) {
            int currentProgressPercentage = 100 * i / processList.size();
            if (currentProgressPercentage > progressPercentage) {
                progressPercentage = currentProgressPercentage;
                System.out.printf("Progress: %d%% (please wait...)%n", progressPercentage);
            }
            Path destPath = getNextAvailableMusicFilePath(processListExtensions.get(i));
            File srcFile = processList.get(i);
            Files.createDirectories(destPath.getParent());
            if (Config.getValue(ConfigKeys.ACTION).equalsIgnoreCase("move")) {
                Files.move(srcFile.toPath(), destPath);
            } else if (Config.getValue(ConfigKeys.ACTION).equalsIgnoreCase("copy")) {
                // Action of copying rather than move is useful to debug or test the code
                Files.copy(srcFile.toPath(), destPath);
            }
        }
        System.out.println("Finished !");
    }

    private static int getAvailableCharIndex(char c) {
        for (int i = 0; i < PATH_AVAILABLE_CHARS.length; i++) {
            if (PATH_AVAILABLE_CHARS[i] == c) {
                return i;
            }
        }
        return -1;
    }

    private static boolean incrementPath(StringBuilder sb, int maxSize) {
        boolean incrementedSuccessfully = false;
        int incrementCurrentIndex = sb.length() - 1;
        while (!incrementedSuccessfully) {
            int currentCharIndex = getAvailableCharIndex(sb.charAt(incrementCurrentIndex));
            if (currentCharIndex + 1 < PATH_AVAILABLE_CHARS.length) {
                sb.setCharAt(incrementCurrentIndex, PATH_AVAILABLE_CHARS[currentCharIndex + 1]);
                for (int i = incrementCurrentIndex + 1; i < sb.length(); i++) {
                    sb.setCharAt(i, PATH_AVAILABLE_CHARS[0]);
                }
                incrementedSuccessfully = true;
            } else {
                incrementCurrentIndex--;
                if (incrementCurrentIndex < 0) {
                    // Adding a new character is a necessity at this point
                    if (sb.length() < maxSize) {
                        for (int i = 0; i < sb.length(); i++) {
                            sb.setCharAt(i, PATH_AVAILABLE_CHARS[0]);
                        }
                        sb.insert(0, PATH_AVAILABLE_CHARS[0]);
                        incrementedSuccessfully = true;
                    } else {
                        // We don't have the right to add a new character there
                        break;
                    }
                }
            }
        }
        return incrementedSuccessfully;
    }

    private static Path getNextAvailableMusicFilePath(String extension) throws IOException {
        final StringBuilder currentFolderPathSB = new StringBuilder().append(PATH_AVAILABLE_CHARS[0]);
        final StringBuilder currentFileInFolderPathSB = new StringBuilder().append(PATH_AVAILABLE_CHARS[0]);
        boolean validPathFound = false;
        while (!validPathFound) {
            Path candidatePath = Path.of(Config.getValue(ConfigKeys.DST_FOLDER_PATH), currentFolderPathSB.toString(), currentFileInFolderPathSB + extension);
            if (Files.notExists(candidatePath)) {
                validPathFound = true;
                continue;
            }
            // First, we try to increment the file path
            if (incrementPath(currentFileInFolderPathSB, MAX_LETTERS_MUSIC_FILE_IN_FOLDER)) {
                continue;
            }
            // If it exceeds the capacity, we need to increase the folder path
            if (incrementPath(currentFolderPathSB, MAX_LETTERS_MUSIC_FOLDER)) {
                currentFileInFolderPathSB.setLength(0);
                currentFileInFolderPathSB.append(PATH_AVAILABLE_CHARS[0]);
                continue;
            }
            throw new IOException("Program bug or insane amount of music detected");
        }
        return Path.of(Config.getValue(ConfigKeys.DST_FOLDER_PATH), currentFolderPathSB.toString(), currentFileInFolderPathSB + extension);
    }
}
