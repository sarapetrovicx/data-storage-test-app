package paket;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws IOException {


        Scanner sc = new Scanner(System.in);
        List<MyFile> lastSearchedList = null;

        try{
            Class.forName("paket.LocalDriveImplementation");
        } catch (Exception e){
            e.printStackTrace();
        }
        String path = args[0];
        Specification specification = DriveManager.getDrive(path);

        boolean init = false;

        try{
            init = specification.init(path);
        } catch (Exception e1){
            e1.printStackTrace();
        }

        String command;

        if(init){
            System.out.println("Storage found");
        } else {
            System.out.println("Storage not found. Create new one.\n");
            boolean storageMade = false;
            while (!storageMade) {
                System.out.println("Input command (example: createroot p-path n-name c-): ");
                command = sc.nextLine();
                command += " ";
                if (command.contains("createroot")) {
                    if (command.split(" ").length == 1) { // createroot
                        specification.createRootDirectory();
                        storageMade = true;
                        System.out.println("Default Path, Name and Configuration");
                    } else {
                        if (command.contains("c-")) {
                            System.out.println("Add configuration (example s-size max-maxNumberOfFiles e-.csv .pdf .doc): ");
                            String con = sc.nextLine();
                            con += " ";
                            int size = 700;
                            int getNumOfFiles = 20;
                            ArrayList<String> extension = new ArrayList<>();
                            if (con.contains("s-")) {
                                size = Integer.parseInt(substring(con, "s-"));
                            }
                            if (con.contains("max-")) {
                                getNumOfFiles = Integer.parseInt(substring(con, "max-"));
                            }
                            if (con.contains("e-")) {
                                String ex = con.substring(con.indexOf("e-") + 2);
                                for (String e : ex.split(" ")) {
                                    extension.add(e);
                                }
                            }
                            System.out.println("Added Configuration with size: " + size + ", limit for files: " + getNumOfFiles + " and restricted extensions: " + extension);
                            Configuration configuration = new Configuration(size, getNumOfFiles, extension);
                            //specification.makeConfiguration(size, getNumOfFiles, extension);
                            if (command.contains("p-") && command.contains("n-")) { //createroot p-path n-name c-
                                path = substring(command, "p-");
                                String name = substring(command, "n-");
                                System.out.println("Path: " + path + ", Name: " + name + " and added Cofiguration");
                                specification.createRootDirectory(path, name, configuration);
                                storageMade = true;
                            } else if (command.contains("p-")) { //createroot p-path c-
                                path = substring(command, "p-");
                                System.out.println("Path: " + path + ", added Configuration and dafult Name");
                                specification.createRootDirectory(path, configuration);
                                storageMade = true;
                            } else { //createroot c-
                                specification.createRootDirectory(configuration);
                                storageMade = true;
                            }
                            //specification.makeConfiguration(configuration.getSize(),configuration.getNumOfFiles(), configuration.getExtensions());
                        } else {
                            if (command.contains("p-") && command.contains("n-")) { //createroot p-path n-name
                                path = substring(command, "p-");
                                String name = substring(command, "n-");
                                System.out.println("Path: " + path + ", Name: " + name + ", default Configuration");
                                specification.createRootDirectory(path, name);
                                storageMade = true;
                            } else if (command.contains("p-")) { //createroot p-path
                                path = substring(command, "p-");
                                System.out.println("Path: " + path + ", default Name and Configuration");
                                specification.createRootDirectory(path);
                                storageMade = true;
                            }
                        }
                    }
                } else {
                    System.out.println("You need to make your storage first. Use command createroot p-path n-name c-");
                }
            }
        }
        do {
            System.out.println("Input command: ");
            /**
             * mkdir n-name
             */
            command = sc.nextLine();
            command += " ";
            if (command.startsWith("mkdirs")) {
                if(command.split(" ").length == 2) {
                    int numOfDir = Integer.parseInt(command.split(" ")[1]);
                    specification.makeDirectories(numOfDir);
                    System.out.println(numOfDir + "new directories are created");
                } else if (Integer.parseInt(command.split(" ")[1]) == command.split(" ").length-2){
                    ArrayList<String> arr = new ArrayList<>();
                    for(int i = 2; i < command.split(" ").length; i++){
                        arr.add(command.split(" ")[i]);
                    }
                    int numOfDir = Integer.parseInt(command.split(" ")[1]);
                    specification.makeDirectories(numOfDir, arr);
                    System.out.println(numOfDir + " new directories are created with names " + arr);
                }
            }
            if (command.startsWith("mkdir")) {
                if (command.split(" ").length == 1) { // mkdir
                    specification.makeDirectory();
                    System.out.println("New directory is created");
                } else if (command.contains("n-")) {
                    specification.makeDirectory(substring(command, "n-"));
                    System.out.println("Directory " + substring(command, "n-") + " is created");
                }
            }

            if (command.startsWith("copyf")) {
                if (command.split(" ").length == 3) {
                    String source = command.split(" ")[1];
                    String target = command.split(" ")[2];
                    specification.copyFile(source, target);
                    System.out.println(source + " has been copied to " + target);
                }
                if (command.split(" ").length > 3) { // copyf target src1 src2 src3
                    ArrayList<String> sources = new ArrayList<>();
                    for(int i = 2; i < command.split(" ").length;i++)
                        sources.add(command.split(" ")[i]);

                    String target = command.split(" ")[1];
                    specification.copyFiles(sources, target);
                    System.out.println(sources + " have been copied to " + target);
                }
            }
            if (command.startsWith("deletef")) {
                if (command.split(" ").length == 2) {
                    String file = command.split(" ")[1];
                    specification.deleteFile(file);
                    System.out.println("Files " + file + " has been deleted.");
                } else if (command.split(" ").length > 2) {
                    ArrayList<String> files = new ArrayList<>();
                    for(int i = 1; i < command.split(" ").length;i++)
                        files.add(command.split(" ")[i]);

                    specification.deleteFiles(files);
                    System.out.println("Files " + files + " have been deleted.");
                }
            }
            if (command.startsWith("deletedir")) {
                if (command.split(" ").length == 2) {
                    String dir = command.split(" ")[1];
                    specification.deleteDirectory(dir);
                    System.out.println("Directory " + dir + " has been deleted.");
                } else if (command.split(" ").length > 2) {
                    ArrayList<String> dirs = new ArrayList<>();
                    for(int i = 1; i < command.split(" ").length;i++)
                        dirs.add(command.split(" ")[i]);

                    specification.deleteDirectories(dirs);
                    System.out.println("Directories " + dirs + " have been deleted.");
                }
            }
            if (command.startsWith("movef")) {
                if (command.split(" ").length == 3) {
                    String source = command.split(" ")[1];
                    String target = command.split(" ")[2];
                    specification.moveFile(source, target);
                    System.out.println(source + " has been moved to " + target);
                }
                if (command.split(" ").length > 3) { // copyf target src1 src2 src3
                    ArrayList<String> sources = new ArrayList<>();
                    for(int i = 2; i < command.split(" ").length;i++)
                        sources.add(command.split(" ")[i]);

                    String target = command.split(" ")[1];
                    specification.moveFiles(sources, target);
                    System.out.println(sources + " have been moved to " + target);
                }
            }
            if (command.startsWith("downloadf")) {
                if (command.split(" ").length == 3) {
                    String source = command.split(" ")[1];
                    String target = command.split(" ")[2];
                    specification.downloadFile(source, target);
                    System.out.println(source + " has been downloaded to " + target);
                }
                if (command.split(" ").length > 3) { // copyf target src1 src2 src3
                    ArrayList<String> sources = new ArrayList<>();
                    for(int i = 2; i < command.split(" ").length;i++)
                        sources.add(command.split(" ")[i]);

                    String target = command.split(" ")[1];
                    specification.downloadFiles(sources, target);
                    System.out.println(sources + " have been downloaded to " + target);
                }
            }
            if (command.startsWith("renamef")) { // renamef path newName
                if (command.split(" ").length == 3) {
                    String source = command.split(" ")[1];
                    String newName = command.split(" ")[2];
                    specification.renameFile(source, newName);
                    System.out.println(source + " has been renamed to " + newName);
                }
            }
            if (command.startsWith("renamedir")) { // renamef path newName
                if (command.split(" ").length == 3) {
                    String source = command.split(" ")[1];
                    String newName = command.split(" ")[2];
                    specification.renameDirectory(source, newName);
                    System.out.println(source + " has been renamed to " + newName);
                }
            }
            if (command.startsWith("searchdir")) { // searchdir path
                if (command.split(" ").length == 2) {
                    try {
                        List<MyFile> files = specification.returnFilesInDirectory(command.split(" ")[1]);
                        lastSearchedList = files;
                        for(MyFile f: files)
                            System.out.println(f.toString());
                    } catch (ParseException e){
                        System.out.println("Directory could not be found");
                    }

                }
                //TODO: filesFromDirectories
                if (command.split(" ").length == 3 && command.split(" ")[2].equals("r")){ // searchdir path r
                    try {
                        List<MyFile> files = specification.filesFromDirectoriesAndSubdirectories(command.split(" ")[1]);
                        lastSearchedList = files;
                        for(MyFile f: files)
                            System.out.println(f.toString());
                    } catch (ParseException e){
                        System.out.println("Directory could not be found");
                    }
                }
            }
            if (command.startsWith("searchext")) { // searchext path ext
                String extensions = command.split(" ")[2];
                System.out.println(extensions);
                try {
                    List<MyFile> files = specification.returnFilesWithExt(command.split(" ")[1], extensions);
                    lastSearchedList = files;
                    for (MyFile f : files)
                        System.out.println(f.toString());
                }catch (ParseException e){
                    System.out.println("Directory could not be found");
                }
            }
            if (command.startsWith("searchsub")) { // searchsub path substring
                try {
                    List<MyFile> files = specification.returnFilesWithSubstring(command.split(" ")[1], command.split(" ")[2]);
                    lastSearchedList = files;
                    for (MyFile f : files)
                        System.out.println(f.toString());
                } catch (ParseException e){
                    System.out.println("Directory could not be found");
                }
            }
            if (command.startsWith("containsf")) { // containsf path file
                if(command.split(" ").length == 3) {
                    String dir = command.split(" ")[1];
                    String fileName = command.split(" ")[2];
                    if (specification.containsFile(dir, fileName))
                        System.out.println(dir + " contains " + fileName + ".");
                    else
                        System.out.println(dir + " does not contain " + fileName + ".");
                }
                if(command.split(" ").length > 3) { // containsf path file1 file2 ...
                    ArrayList<String> fileNames = new ArrayList<>();
                    for(int i = 2; i < command.split(" ").length;i++)
                        fileNames.add(command.split(" ")[i]);
                    String dir = command.split(" ")[1];
                    if (specification.containsFiles(dir, fileNames))
                        System.out.println(dir + " contains " + fileNames + ".");
                    else
                        System.out.println(dir + " does not contain " + fileNames + ".");
                }
            }
            if (command.startsWith("findf")) { //findf fileName
                if(command.split(" ").length == 2){
                    try {
                        String parent = specification.findFile(command.split(" ")[1]);
                        System.out.println("File found in " + parent);
                    } catch (IOException e){
                        System.out.println("File not found");
                    }
                }
            }
            if (command.startsWith("sortf")) { // sortf path asc name/date_m/date_c/size
                if (command.split(" ").length == 4) {
                    boolean asc;
                    if(command.split(" ")[2].equals("asc"))
                        asc = true;
                    else if (command.split(" ")[2].equals("desc"))
                        asc = false;
                    else {
                        System.out.println("False arguments for command sortf");
                        continue;
                    }
                    List<MyFile> files;
                    try {
                        if (command.split(" ")[3].equals("name")) {
                            files = specification.sortFiles(command.split(" ")[1], asc, SortBy.NAME);
                        } else if (command.split(" ")[3].equals("date_m")) {
                            files = specification.sortFiles(command.split(" ")[1], asc, SortBy.DATE_MODIFIED);
                        } else if (command.split(" ")[3].equals("date_c")) {
                            files = specification.sortFiles(command.split(" ")[1], asc, SortBy.DATE_CREATED);
                        } else if (command.split(" ")[3].equals("size")) {
                            files = specification.sortFiles(command.split(" ")[1], asc, SortBy.SIZE);
                        } else {
                            System.out.println("False arguments for command sortf");
                            continue;
                        }
                        lastSearchedList = files;
                        for (MyFile f : files) {
                            System.out.println(f.toString());
                        }
                    } catch (ParseException e){
                        System.out.println("Directory could not be found");
                    }
                }
            }
            if (command.startsWith("creation")) { // creation path from to mod/crt
                if (command.split(" ").length == 5) {
                    String type;
                    if(command.split(" ")[4].equals("mod"))
                        type = "modified";
                    else if (command.split(" ")[4].equals("crt"))
                        type = "created";
                    else {
                        System.out.println("False arguments for command creation");
                        continue;
                    }
                    try {
                        List<MyFile> files = specification.createdOrModifiedWithinTimePeriod(command.split(" ")[1],
                                command.split(" ")[2], command.split(" ")[3], type);
                        lastSearchedList = files;
                        for (MyFile f : files)
                            System.out.println(f.toString());
                    } catch (ParseException e){
                        System.out.println("Directory could not be found");
                    }
                }
            }
            if (command.startsWith("filter")) { // filter name/date_m/date_c/size
                if (lastSearchedList.isEmpty() || lastSearchedList != null) {
                    List<String> result = new ArrayList<>();
                    if (command.split(" ")[1].equals("name")) {
                        result = specification.filter(lastSearchedList, SortBy.NAME);
                    } else if (command.split(" ")[1].equals("date_m")) {
                        result = specification.filter(lastSearchedList, SortBy.DATE_MODIFIED);
                    } else if (command.split(" ")[1].equals("date_c")) {
                        result = specification.filter(lastSearchedList, SortBy.DATE_CREATED);
                    } else if (command.split(" ")[1].equals("size")) {
                        result = specification.filter(lastSearchedList, SortBy.SIZE);
                    } else {
                        System.out.println("False arguments for command filter");
                        continue;
                    }
                    for(String s : result){
                        System.out.println(s);
                    }
                }
            }
        } while (!command.startsWith("quit"));
    }

    private static String substring(String command, String atr){
        int ln = atr.length();
        return command.substring(command.indexOf(atr)+ln, command.indexOf(" ", command.indexOf(atr)));
    }
}
