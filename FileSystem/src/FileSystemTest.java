import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.*;

public class FileSystemTest {

    @Test
    public void testFileSystemAddAndFind() {
        FileSystem fs = new FileSystem();
        assertEquals(0, fs.nameMap.size());
        
        //test adding file
        assertEquals(true, fs.add("mySample.txt", "/home", "02/01/2021"));
        assertEquals(false, fs.add("mySample.txt", "/home", "02/01/2021"));
        assertEquals(true, fs.add("hello.txt", "/home", "02/01/2021"));

        //test findAllFilesName method
        ArrayList<String> lst = new ArrayList<>();
        lst.add("mySample.txt");
        lst.add("hello.txt");
        Collections.sort(lst);
        Collections.sort(fs.findAllFilesName());
        assertEquals(lst, fs.findAllFilesName());

        //test find Files
        FileData f = new FileData("mySample.txt", "/home", "02/01/2021");
        assertEquals(f.toString(), fs.findFile("mySample.txt", "/home").toString());

        //test remove Files
        assertEquals(true, fs.removeByName("hello.txt"));
        assertEquals(1, fs.nameMap.size());
        assertEquals(1, fs.dateMap.size());
        //System.out.println(fs.nameMap.keys());
        //System.out.println(fs.nameMap.get("hello.txt"));
        assertEquals(null, fs.nameMap.get("hello.txt"));
        assertEquals(true, fs.removeByName("mySample.txt"));
        assertEquals(0, fs.nameMap.size());
        //System.out.println(fs.dateMap.get("02/01/2021"));
        assertEquals(0, fs.dateMap.size());
    }

    @Test
    public void testFileSystemWithInputFile() {
        FileSystem fs = new FileSystem("src/input.txt");
        assertEquals(3, fs.nameMap.get("mySample.txt").size());
    }
}
