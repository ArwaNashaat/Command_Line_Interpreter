import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.System.getProperty;

public class Main {

    public static BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
    public static StringTokenizer command;
    public static String home = pwd();

    public static void Switch(String exit) throws IOException {
        switch (exit) {
            case ">":
                reDirectReplace(command.nextToken(),command.nextToken());
                break;
            case ">>":
                reDirectAppend(command.nextToken(),command.nextToken());
                break;
            case "pwd":
                System.out.println(pwd());
                break;

            case "cd":

                if (command.hasMoreTokens()) {
                    String parameter = command.nextToken();
                    if (parameter != "|")
                        cd(parameter);

                } else
                    cd(home);
                if (command.hasMoreTokens())
                    Switch(command.nextToken());
                break;

            case "ls":
                try {
                    String hidden = command.nextToken();
                    if (hidden.equals("-a")) {
                        ls(command.nextToken(), true);
                    } else {
                        ls(hidden, false);
                    }

                } catch (NoSuchElementException ex) {
                    System.out.println("ls takes one parameter");
                }
                break;

            case "clear":
                clear();
                break;

            case "cp":
                try {
                    cp(command.nextToken(), command.nextToken());
                } catch (NoSuchElementException ex) {
                    System.out.println("cp takes two arguments");
                }
                break;
            case "mkdir":
                mkdir(command.nextToken());
                break;
            case "rmdir":
                rmdir(command.nextToken());
                break;

            case "cat":

                String f1 = command.nextToken();
                String f2;
                try {
                    f2 = command.nextToken();
                } catch (NoSuchElementException ex) {
                    f2 = "";
                }
                cat(f1, f2);

                break;
            case "help":
                help("");
                break;

            case "args":
                args();
                break;

            case "?":
                help(command.nextToken());
                break;
            case "rm":
                String s = command.nextToken();
                String r = "";
                try{
                    r = command.nextToken();
                }
                catch (NoSuchElementException ex){

                }
                boolean rBOOl= false;
                if(r.equals("-r")){
                    rBOOl = true;
                }
                else if ((s.equals("-force") || s.equals("-f")) && rBOOl) {
                    rmR(command.nextToken(), true);
                }

                 else if ((s.equals("-force") || s.equals("-f")) && !rBOOl) {
                    rm(r, true);
                }

                else if ((!s.equals("-force") && !s.equals("-f"))&& s.equals("-r") && !rBOOl) {
                    rmR(r, false);
                }

                else {
                    rm(s, false);
                }

                break;

            case "mv":
                mv(command.nextToken(), command.nextToken());
                break;

            case "date":
                date();
                break;
            case "|":
                try {
                    Switch(command.nextToken());
                } catch (NoSuchElementException ex) {
                    return;
                }

                break;

            case "exit":
                System.exit(0);
                break;

            default:
                System.out.println("No Command Named " + exit + " Exist");

        }

    }

    public static String pwd(){

        String workingDir = getProperty("user.dir");
        return workingDir;
    }

    public static boolean cd(String dir) {

        boolean found = false;

        File directory;
        if (dir.equals(".."))
            directory = new File(System.getProperty("user.dir")).getParentFile();

        else
            directory = new File(dir).getAbsoluteFile();


            if (directory.exists()) {
                System.setProperty("user.dir", directory.getAbsolutePath());
                found = true;
            }
            else {
                System.out.println("Directory Not Found");

            }
        return found;
    }

    //zawedet -a (to print hidden and visible files) condition
    public static void ls(String dir, boolean hidden) {

        File directory = new File(dir).getAbsoluteFile();

        File[] files = directory.listFiles();

        if (!hidden) {
            for (File s : files) {
                if (!s.isHidden()) {
                    System.out.println(s.getName());
                }
            }
        }
        else {
            for (File s : files) {
                System.out.println(s.getName());
            }
        }


    }

    public static void cp(String curDir, String tarDir)throws IOException {

        File f = new File(tarDir).getAbsoluteFile();
        Files.copy(new File(curDir).toPath(), f.toPath());

    }

    public static void mkdir(String dir) {

        try {
            boolean newFile = new File(dir).mkdirs();
            //    System.out.println("file created");
        } catch (NoSuchElementException ex) {
            System.out.println("file exist");
        }

    }

    public static void rmdir(String dir) {
        File directory = new File(dir);
        directory.delete();
    }

    public static void rm(String dir, boolean force) {

        File directory = new File(dir);
        String[] files = directory.list();

        if (!force) {
            for (String f : files) {
                File curFile = new File(directory.getPath(), f);
                if (!curFile.isDirectory()) {
                    System.out.println("Are you sure you want to delete " + curFile.getName());
                    Scanner sc = new Scanner(System.in);
                    char y = sc.next().charAt(0);

                    if (y == 'y')
                        curFile.delete();
                } else {
                    //if (r)
                    rm(dir + "/" + f, force);
                    System.out.println("Are you sure you want to delete " + curFile.getName());
                    Scanner sc = new Scanner(System.in);
                    char y = sc.next().charAt(0);

                    if(y=='y' && y=='Y')
                        curFile.delete();

                }
            }
            System.out.println("Are you sure you want to delete " + directory.getName());
            Scanner sc = new Scanner(System.in);
            char y = sc.next().charAt(0);

            if(y=='y' || y=='Y')
                directory.delete();

        } else {
            for (String f : files) {

                File curFile = new File(directory.getPath(), f);
                if (!curFile.isDirectory())
                    curFile.delete();
     //           else{
   //                 rm(dir + "/" + f, force);

 //               }

            }

            directory.delete();
        }
    }

    public static void rmR(String dir, boolean force) {

        File directory = new File(dir);
        String[] files = directory.list();

        if (!force) {
            for (String f : files) {
                File curFile = new File(directory.getPath(), f);
                if (!curFile.isDirectory()) {
                    System.out.println("Are you sure you want to delete " + curFile.getName());
                    Scanner sc = new Scanner(System.in);
                    char y = sc.next().charAt(0);

                    if (y == 'y' || y == 'Y')
                        curFile.delete();
                }
                else {
                    //if (r)
                    rmR(dir + "/" + f, force);

                    //System.out.println("Are you sure you want to delete " + curFile.getName());
                    //Scanner sc = new Scanner(System.in);
                    //char y = sc.next().charAt(0);

                    //if (y == 'y' || y == 'Y')
                  //      curFile.delete();

                }

            }
            System.out.println("Are you sure you want to delete " + directory.getName());
            Scanner sc = new Scanner(System.in);
            char y = sc.next().charAt(0);

            if (y == 'y' || y == 'Y')
                directory.delete();

        }
        else {
            for (String f : files) {

                File curFile = new File(directory.getPath(), f);
                if (!curFile.isDirectory())
                    curFile.delete();
                else
                {
                    rmR(dir + "/" + f, force);
                    curFile.delete();
                }

            }
            directory.delete();

        }
 }

    //print f1 then f2
    public static void cat(String file1, String file2) throws IOException {

        BufferedReader buf1 = new BufferedReader(new FileReader(file1));
        BufferedReader buf2 = null;
        String s2;

        if (file2.equals(""))
            s2 = null;

        else {
            buf2 = new BufferedReader(new FileReader(file2));

            s2 = buf2.readLine();
        }
        String s1 = buf1.readLine();


        /*while (s1 != null && s2 != null) {
            System.out.println(s1 + s2);
            //System.out.println(s2);

            s1 = buf1.readLine();
            s2 = buf2.readLine();
        }*/
       while (s1 != null) {
            System.out.println(s1);
            s1 = buf1.readLine();

        }
        while (s2 != null) {
            System.out.println(s2);
            s2 = buf2.readLine();
        }
    }

    public static void mv(String s1, String s2){
        File f1=new File (s1);
        f1.renameTo(new File(s2));
    }

    public static void reDirectReplace(String s1,String s2) throws IOException {

        FileReader f1 = null;
        FileWriter f2 = null;
        try {
            f1 = new FileReader(s1);
            f2 = new FileWriter(s2);
            int s = f1.read();

            while(s!=-1) {
                f2.write(s);
                s = f1.read();
            }
        } catch(IOException e) {
            System.out.println("file not found");
        }
        f1.close();
        f2.close();


    }

    public static void reDirectAppend(String s1,String s2) throws IOException {

        BufferedReader f1 = null;
        BufferedReader f2 = null;
        ArrayList<String> list = new ArrayList<>();

        try {
            f1 = new BufferedReader(new FileReader(s1));
            f2 = new BufferedReader(new FileReader(s2));

            String line1 = f1.readLine();
            String line2 = f2.readLine();

            while(line2!=null){
                list.add(line2);
                line2 = f2.readLine();
            }
            while(line1!=null){
                list.add(line1);
                line1 = f1.readLine();
            }

        } catch(IOException e) {
            System.out.println("file not found");
        }
        f1.close();
        f2.close();

        BufferedWriter fWrite = new BufferedWriter(new FileWriter(s2));

        for(int i=0; i<list.size(); i++){
            fWrite.write(list.get(i));
            fWrite.newLine();
        }

        fWrite.close();

    }
    public static void clear() {
        for(int i = 0; i < 80*300; i++) // Default Height of cmd is 300 and Default width is 80
            System.out.print("\n");
    }

    public static void date(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }

    public static void help(String command) {

        System.out.println("Command Name                    Arguments                           Definition ");

        if(command.equals("") || command.equals("date"))
            System.out.println("   date                          (none)                           \"shows the current date and time \" ");

        if(command.equals("") || command.equals("clear"))
            System.out.println("   clear                          (none)                               \" Clear the Terminal\" ");

        if(command.equals("") || command.equals("cd"))
            System.out.println("    cd                        (directory name)                         \"Change the directory\" ");

        if(command.equals("") || command.equals("ls"))
            System.out.println("    ls                            (none)                               \"list files and folders in the directory\" ");

        if(command.equals("") || command.equals(">>"))
            System.out.println("    >>                       (file name)                            \"Redirect the output to be written to a file using the redirect >> create/append to file operator \" ");

        if(command.equals("") || command.equals(">"))
            System.out.println("    >                        (file name)                            \"Redirect the output to be written to a file using the redirect > create/replace file operator. \" ");

        if(command.equals("") || command.equals("cp"))
            System.out.println("    cp                      (file name,The directory)                  \"copy files and folders\" ");

        if(command.equals("") || command.equals("mv"))
            System.out.println("    mv                      (file name,The directory)                  \"move files and folders\" ");

        if(command.equals("") || command.equals("rm"))
            System.out.println("    rm                           (file name)                           \"remove files\" ");

        if(command.equals("") || command.equals("mkdir"))
            System.out.println("   mkdir                         (Folder name)                         \"create a new folder\" ");

        if(command.equals("") || command.equals("rmdir"))
            System.out.println("   rmdir                         (folder name)                         \"remove folder\" ");

        if(command.equals("") || command.equals("more"))
            System.out.println("   more                          (file name)                           \"list 5 row at most and can scroll downwords only using 's'\" ");

        if(command.equals("") || command.equals("cat"))
            System.out.println("   cat                    (file name)|(file name) (file name)          \"Concatenate files and print on the standard output \" ");

        if(command.equals("") || command.equals("pwd"))
            System.out.println("   pwd                            (none)                               show the current directroy ");

    }

    public static void args() {
        System.out.println("Command Name                    Arguments                           Definition ");
        System.out.println("   date                          (none)                           \"shows the current date and time \" ");
        System.out.println("   clear                          (none)                               \" Clear the Terminal\" ");
        System.out.println("    cd                        (directory name)                         \"Change the directory\" ");
        System.out.println("    ls                            (none)                               \"list files and folders in the directory\" ");
        System.out.println("    >>                       (file name)                            \"Redirect the output to be written to a file using the redirect >> create/append to file operator \" ");
        System.out.println("    >                        (file name)                            \"Redirect the output to be written to a file using the redirect > create/replace file operator. \" ");
        System.out.println("    cp                      (file name,The directory)                  \"copy files and folders\" ");
        System.out.println("    mv                      (file name,The directory)                  \"move files and folders\" ");
        System.out.println("    rm                           (file name)                           \"remove files\" ");
        System.out.println("   mkdir                         (Folder name)                         \"create a new folder\" ");
        System.out.println("   rmdir                         (folder name)                         \"remove folder\" ");
        System.out.println("   more                          (file name)                           \"list 5 row at most and can scroll downwords only using 's'\" ");
        System.out.println("   cat                          (file name)                           \"prints the whole content of a file\" ");
        System.out.println("   pwd                            (none)                               show the current directroy ");

    }

    public static void main(String[] args) throws IOException{

        while(true)       {
            System.out.println("Enter command: ");
             command = new StringTokenizer(buffer.readLine());

             String exit;
             try
             {
                exit = command.nextToken();
             }
             catch (NoSuchElementException ex){
                exit = command.nextToken();
             }

             Switch(exit);

             while(command.hasMoreTokens() && command.nextToken().equals("|")) {
                try{
                    exit = command.nextToken();
                }
                catch (NoSuchElementException ex){
                    System.out.println("Enter a command after \'|\' operator");
                }
                Switch(exit);
                }
        }
    }
}
