import static org.junit.Assert.*;

import org.junit.*;

public class FileDataTest {
    @Test
    public void TestFileToStringTrue() {
        FileData f = new FileData("HashMap.java", "CSE12", "11/12/2023");
        assertEquals("{Name: HashMap.java, Directory: CSE12, Modified Date: 11/12/2023}", f.toString());
    }

    @Test
    public void TestFileToStringFalse() {
        FileData f = new FileData("HashMap.java", "CSE12", "11/12/2023");
        assertNotEquals("{Name: HashMap.java, Directory: CSE15l, Modified Date: 11/12/2023}", f.toString()); 
    }
}
