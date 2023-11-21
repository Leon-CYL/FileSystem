import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.*;

public class MyHashMapTest {
	
	private DefaultMap<String, String> testMap; // use this for basic tests
	private DefaultMap<String, String> mapWithCap; // use for testing proper rehashing
	public static final String TEST_KEY = "Test Key";
	public static final String TEST_VAL = "Test Value";
	
	@Before
	public void setUp() {
		testMap = new MyHashMap<>();
		mapWithCap = new MyHashMap<>(4, MyHashMap.DEFAULT_LOAD_FACTOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPut_nullKey() {
		testMap.put(null, TEST_VAL);
	}

	@Test
	public void testKeys_nonEmptyMap() {
		// You don't have to use array list 
		// This test will work with any object that implements List
		List<String> expectedKeys = new ArrayList<>(5);
		for(int i = 0; i < 5; i++) {
			// key + i is used to differentiate keys since they must be unique
			testMap.put(TEST_KEY + i, TEST_VAL + i);
			expectedKeys.add(TEST_KEY + i);
		}
		List<String> resultKeys = testMap.keys();
		// we need to sort because hash map doesn't guarantee ordering
		Collections.sort(resultKeys);
		assertEquals(expectedKeys, resultKeys);
	}
	
	/* Add more of your tests below */

	@Test
	public void testPutAndReplaceAndGet() {
		for(int i = 0; i < 5; i++) {
			// key + i is used to differentiate keys since they must be unique
			assertEquals(true, testMap.put(TEST_KEY + i, TEST_VAL + i));
			assertEquals(true, testMap.containsKey(TEST_KEY + i));
		}
		assertEquals(5, testMap.size());
		assertEquals(true, testMap.replace(TEST_KEY + 1, null));
		assertEquals(null, testMap.get(TEST_KEY + 1));
	}

	@Test
	public void testIsEmptyAndRemoveAndSize() {
		assertEquals(true, testMap.isEmpty());
		for(int i = 0; i < 5; i++) {
			// key + i is used to differentiate keys since they must be unique
			assertEquals(true, testMap.put(TEST_KEY + i, TEST_VAL + i));
		}
		assertEquals(true, testMap.remove(TEST_KEY + 0));
		assertEquals(4, testMap.size());
	}
	
}