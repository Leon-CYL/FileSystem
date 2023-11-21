import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileSystem {

    MyHashMap<String, ArrayList<FileData>> nameMap;
    MyHashMap<String, ArrayList<FileData>> dateMap;

    /**
     * Default Constructure
     */
    public FileSystem() {
        nameMap = new MyHashMap<>();
        dateMap = new MyHashMap<>();
    }


    /**
     * Constructure that load all the data from the input file to the FileSystem
     * @param inputFile a string path to the input file
     */
    public FileSystem(String inputFile) {
        nameMap = new MyHashMap<>();
        dateMap = new MyHashMap<>();
        try {
            File f = new File(inputFile);
            Scanner sc = new Scanner(f);
            System.out.println(f.getAbsolutePath());
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(", ");
                this.add(data[0], data[1], data[2]);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }


    /**
     * Add a FileData Object to the FileSystem
     * Note: File with the same name and diretory will not be added to the fileSystem
     * 
     * @param fileName The name of the file to be added
     * @param directory The diretory that the file is stored
     * @param modifiedDate The last time that this file is modified
     * @return true if the FileData is added to the FileSystem else false
     */
    public boolean add(String fileName, String directory, String modifiedDate) {
        FileData f = new FileData(fileName, directory, modifiedDate);
        if (nameMap.get(fileName) == null) {
            ArrayList<FileData> lst = new ArrayList<>();
            lst.add(f);
            nameMap.put(fileName, lst);
        } else {
            ArrayList<FileData> lst = nameMap.get(fileName);
            for (FileData d : lst) {
                if (d.dir.equals(directory)) {
                    return false;
                }
            }
            lst.add(f);
            nameMap.set(fileName, lst);
        }

        if (dateMap.get(modifiedDate) == null) {
            ArrayList<FileData> lst = new ArrayList<>();
            lst.add(f);
            dateMap.put(modifiedDate, lst);
        } else {
            ArrayList<FileData> lst = dateMap.get(modifiedDate);
            for (FileData d : lst) {
                if (d.dir.equals(directory) && d.name.equals(fileName)) {
                    nameMap.remove(fileName);
                    return false;
                }
            }
            lst.add(f);
            dateMap.set(modifiedDate, lst);
        }

        return true;
    }


    /**
     * Find the corresponding file with the given file name and directory
     * 
     * @param name The name of the file we are looking for
     * @param directory The diretory of the file we are looking for
     * @return a FileData object if it exist in the FileSystem else null
     */
    public FileData findFile(String name, String directory) {
        if (!name.contains(name)) {
            return null;
        }

        ArrayList<FileData> lst = nameMap.get(name);
        if (lst == null) {
            return null;
        }
        for (FileData d : lst) {
            if (d.dir.equals(directory) && d.name.equals(name)) {
                return d;
            }
        }
        return null;
    }


    /**
     * 
     * @return an ArrayList of all the file names in the FileSystem
     */
    public ArrayList<String> findAllFilesName() {
        ArrayList<String> lst = new ArrayList<>();
        for (String key : nameMap.keys()) {
            if (!lst.contains(key)) {
                lst.add(key);
            }
        }
        return lst;
    }


    /**
     * 
     * @param name the file name for searching FileData in the FileSystem
     * @return an ArrayList of FileData with the given file names from the FileSystem
     */
    public ArrayList<FileData> findFilesByName(String name) {
        if (nameMap.get(name) == null) {
            return new ArrayList<>();
        }
        return nameMap.get(name);
    }


    /**
     * 
     * @param modifiedDate the date for searching FilData in the FileSystem
     * @return an ArrayList of FileData with the given modifiedDate from the FileSystem
     */
    public ArrayList<FileData> findFilesByDate(String modifiedDate) {
        ArrayList<FileData> lst = new ArrayList<>();
        if (dateMap.get(modifiedDate) == null) {
            return lst;
        }
        return dateMap.get(modifiedDate);
    } 


    /**
     * 
     * @param modifiedDate the date for searching FilData in the FileSystem
     * @return a list of FileData with the given modifiedDate that has at least another file with 
     * the same file name in a different directory.
     */
    public ArrayList<FileData> findFilesInMultDir(String modifiedDate) {
        ArrayList<FileData> lst = new ArrayList<>();
        if (dateMap.get(modifiedDate) == null) {
            return lst;
        }
        for (FileData d : dateMap.get(modifiedDate)) {
            if (nameMap.get(d.name) != null) {
                for (FileData da : nameMap.get(d.name)) {
                    if (!da.dir.equals(d.dir)) {
                        lst.add(da);
                    }
                }
            }
        }

        return lst;
    }


    /**
     * 
     * @param name the file name for searching corresponding the FileData 
     * @return true if the FileData wa found and removed from the FileSystem else false
     */
    public boolean removeByName(String name) {
        if (nameMap.get(name) == null) {
            return false;
        }
        ArrayList<FileData> nf = new ArrayList<>(nameMap.get(name));
        for (FileData d : nf) {
            String date = d.lastModifiedDate;
            if (dateMap.get(date) != null){
                ArrayList<FileData> f = new ArrayList<>(dateMap.get(date));
                for (FileData df : f) {
                    if (df.name.equals(name)) {
                        dateMap.get(date).remove(df);
                    }
                }
                if (dateMap.get(date).size() == 0) {
                    dateMap.remove(date);
                    dateMap.set(date, null);
                }
            }
        }
        nameMap.remove(name);
        nameMap.set(name, null);
        return true;
    }


    /**
     * 
     * @param name the file name for searching corresponding the FileData
     * @param directory the directory for searching corresponding the FileData
     * @return true if the FileData was found and removed from the FileSystem else false
     */
    public boolean removeFile(String name, String directory) {
        if (nameMap.get(name) == null) {
            return false;
        }
        boolean dir = false;
        ArrayList<FileData> nf = new ArrayList<>(nameMap.get(name));
        for (FileData d : nf) {
            String date = d.lastModifiedDate;
            if (d.dir.equals(directory)) {
                nameMap.get(name).remove(d);
                dir = true;
            }
            if (dateMap.get(date)!= null) {
                ArrayList<FileData> f = new ArrayList<>(dateMap.get(date));
                for (FileData df : f) {
                    if (df.name.equals(name) && df.dir.equals(directory)) {
                        dateMap.get(date).remove(df);
                    }
                }
                if (dateMap.get(date).size() == 0) {
                    dateMap.set(date, null);
                    dateMap.remove(date);
                }
            }
        }
        if (nameMap.get(name).size() == 0) {
            nameMap.remove(name);
            nameMap.set(name, null);
        }
        return true && dir;
    }

}
