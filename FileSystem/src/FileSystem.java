import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileSystem {

    MyHashMap<String, ArrayList<FileData>> nameMap;
    MyHashMap<String, ArrayList<FileData>> dateMap;

    public FileSystem() {
        nameMap = new MyHashMap<>();
        dateMap = new MyHashMap<>();
    }

    public FileSystem(String inputFile) {
        nameMap = new MyHashMap<>();
        dateMap = new MyHashMap<>();
        try {
            File f = new File(inputFile);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(", ");
                this.add(data[0], data[1], data[2]);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

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

    public ArrayList<String> findAllFilesName() {
        ArrayList<String> lst = new ArrayList<>();
        for (String key : nameMap.keys()) {
            if (!lst.contains(key)) {
                lst.add(key);
            }
        }
        return lst;
    }

    public ArrayList<FileData> findFilesByName(String name) {
        if (nameMap.get(name) == null) {
            return new ArrayList<>();
        }
        return nameMap.get(name);
    }

    public ArrayList<FileData> findFilesByDate(String modifiedDate) {
        ArrayList<FileData> lst = new ArrayList<>();
        if (dateMap.get(modifiedDate) == null) {
            return lst;
        }
        return dateMap.get(modifiedDate);
    } 

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
