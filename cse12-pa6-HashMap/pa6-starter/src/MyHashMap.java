import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.LinkedList;

public class MyHashMap<K, V> implements DefaultMap<K, V> {
	public static final double DEFAULT_LOAD_FACTOR = 0.75;
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	public static final String ILLEGAL_ARG_CAPACITY = "Initial Capacity must be non-negative";
	public static final String ILLEGAL_ARG_LOAD_FACTOR = "Load Factor must be positive";
	public static final String ILLEGAL_ARG_NULL_KEY = "Keys must be non-null";
	
	private double loadFactor;
	private int capacity;
	private int size;

	// Use this instance variable for Separate Chaining conflict resolution
	private List<HashMapEntry<K, V>>[] buckets;  
	
	// Use this instance variable for Linear Probing
	private HashMapEntry<K, V>[] entries; 	

	public MyHashMap() {
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * 
	 * @param initialCapacity the initial capacity of this MyHashMap
	 * @param loadFactor the load factor for rehashing this MyHashMap
	 * @throws IllegalArgumentException if initialCapacity is negative or loadFactor not
	 * positive
	 */
	@SuppressWarnings("unchecked")
	public MyHashMap(int initialCapacity, double loadFactor) throws IllegalArgumentException {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException(ILLEGAL_ARG_CAPACITY);
		}

		if (loadFactor <= 0) {
			throw new IllegalArgumentException(ILLEGAL_ARG_LOAD_FACTOR);
		}

		capacity = initialCapacity;
		this.loadFactor = loadFactor;
		size = 0;

		// if you use Separate Chaining
		buckets = (List<HashMapEntry<K, V>>[]) new List<?>[capacity];

		// if you use Linear Probing
		entries = (HashMapEntry<K, V>[]) new HashMapEntry<?, ?>[initialCapacity];
	}

	@Override
	public boolean put(K key, V value) throws IllegalArgumentException {
		// can also use key.hashCode() assuming key is not null
		if (key == null) {
			throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
		}

		int keyHash = Objects.hashCode(key); 

		if (containsKey(key)) {
			return false;
		}

		//Add the valid key value to the HashMap.

		HashMapEntry<K, V> pair = new HashMapEntry<K, V>(key, value);
		int index = Math.abs(keyHash % buckets.length);
		if (buckets[index] != null) {
			buckets[index].add(pair);
		} else {
			buckets[index] = new LinkedList<MyHashMap.HashMapEntry<K, V>>();
			buckets[index].add(pair);
		}
		size++;
		return true;
	}

	@Override
	public boolean replace(K key, V newValue) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
		}

		if (!containsKey(key)) {
			return false;
		}


		int keyHash = Objects.hashCode(key);
		int index = Math.abs(keyHash % buckets.length);
		for (HashMapEntry<K, V> p : buckets[index]) {
			if (p.getKey().equals(key)) {
				p.setValue(newValue);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean remove(K key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
		}

		if (!containsKey(key)) {
			return false;
		}

		int hash = Objects.hashCode(key);
		int index = Math.abs(hash % buckets.length);
		buckets[index].remove(key);
		size--;
		return true;
	}

	@Override
	public void set(K key, V value) throws IllegalArgumentException {
		if (!replace(key, value)) {
			put(key, value);
		}
	}

	@Override
	public V get(K key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
		}

		if (!containsKey(key)) {
			return null;
		}

		if (this.size == 0) {
			return null;
		}

		int hash = Objects.hashCode(key);
		int index = Math.abs(hash % buckets.length);
		List<MyHashMap.HashMapEntry<K, V>> lst = buckets[index];
		for (int i = 0; i <lst.size(); i++) {
			if (lst.get(i).getKey().equals(key)) {
				return lst.get(i).getValue();
			}
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean containsKey(K key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);
		}

		int hash = Objects.hashCode(key);
		int index = Math.abs(hash % buckets.length);

		List<MyHashMap.HashMapEntry<K, V>> lst = buckets[index];
		if (lst == null) {
			return false;
		}
		
		for (int i = 0; i < lst.size(); i++) {
			if (lst.get(i).getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<K> keys() {
		List<K> result = new ArrayList<>();
		if (isEmpty()) {
			return result;
		}

		for (int i = 0; i < buckets.length; i++) {
			List<MyHashMap.HashMapEntry<K, V>> lst = buckets[i];
			if (lst != null) {
				for (HashMapEntry<K, V> pair: lst) {
					result.add(pair.getKey());
				}
			}
		}
		return result;
	}
	
	private static class HashMapEntry<K, V> implements DefaultMap.Entry<K, V> {
		
		K key;
		V value;
		
		private HashMapEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}
		
		@Override
		public void setValue(V value) {
			this.value = value;
		}
	}
}
