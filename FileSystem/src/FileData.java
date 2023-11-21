public class FileData {

    public String name;
    public String dir;
    public String lastModifiedDate;

    public FileData(String name, String directory, String modifiedDate) {
        this.name = name;
        this.dir = directory;
        this.lastModifiedDate = modifiedDate;
    }

    public String toString() {
        return String.format("{Name: %s, Directory: %s, Modified Date: %s}", name, dir, lastModifiedDate);
    }
}
