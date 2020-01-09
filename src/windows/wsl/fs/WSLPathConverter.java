package windows.wsl.fs;


public class WSLPathConverter {
    private final static float APP_VERSION = 2.00f;
    private final static String usageString = "convpath [<option>] <file_path_1> [<file_path_2> [<file_path_3>...]]";

    public static void main(final String[] args) {
        if(args.length == 0) {
            System.err.println("Usage: " + usageString);
            System.exit(StandardExitCodes.ERROR);
        }
        
        boolean optionProvided = false; /* default value set */
        boolean convertForWSL = true; /* default value set */
        
        switch(args[0]) {
            case "-h":
            case "--help":
                showHelp();
                System.exit(StandardExitCodes.NORMAL);
            
            case "-l": 
            case "--wsl":
                optionProvided = true;
                convertForWSL = true;
                break;
                
            case "-w":
            case "--win":
                optionProvided = true;
                convertForWSL = false;
                break;
        }

        if(optionProvided && args.length==1) {
            System.err.println("Usage: " + usageString);
            System.exit(StandardExitCodes.ERROR);
        }
            
        for(int i = optionProvided ? 1 : 0, len=args.length; i<len; i++)
            System.out.println(convertForWSL ? convertForWSL(args[i]) : convertForWindows(args[i]));
    }

    private static String convertForWindows(String path) {
        StringBuilder tmpPath = new StringBuilder(path.replace('/', '\\'));
        
        if(path.startsWith("/mnt/") && path.length() >= 6) {
            char driveLetter = Character.toUpperCase(path.charAt(5));
            tmpPath.delete(0, 6).insert(0, driveLetter+":");
        }

        return tmpPath.toString();
    }

    
    private static String convertForWSL(String path) {
        StringBuilder tmpPath = new StringBuilder(path.length()+4);
        tmpPath.append(path.replace('\\', '/'));
        
        if(tmpPath.length() >= 2 && tmpPath.charAt(1) == ':') {
            char driveLetter = Character.toLowerCase(tmpPath.charAt(0));
            tmpPath.delete(0, 2).insert(0, "/mnt/" + driveLetter);
        }

        return tmpPath.toString();
    }

    private static void showHelp() {
        System.out.println("WSL Path Converter");
        System.out.println("Purpose: For interchanging file path formats to and from WSL");
        System.out.printf ("Version: %.2f\n", APP_VERSION);
        System.out.println("Usage:   " + usageString);
        System.out.println();
        System.out.println("Options:");
        System.out.println("    --help, -h    Shows this help message and exit");
        System.out.println("    --wsl,  -l    Convert to WSL path (default)");
        System.out.println("    --win,  -w    Convert to Windows path");
        System.out.println();
        StandardExitCodes.showMessage();
    }
}