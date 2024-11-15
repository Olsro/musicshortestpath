package kernel;

public class Main {

    public static void main(String[] args) {
        System.out.println("Music Shortest Path v1.0.0 - Written by OlsroFR");
        try {
            Config.loadConfig();
        } catch (Exception e) {
            System.err.println("FATAL ERROR WHEN LOADING CONFIGURATION FILE");
            e.printStackTrace();
            return;
        }
        try {
            MusicFilesProcessor.process();
        } catch (Exception e) {
            System.err.println("FATAL ERROR WHILE PROCESSING MUSIC FILES");
            e.printStackTrace();
            return;
        }
    }
}