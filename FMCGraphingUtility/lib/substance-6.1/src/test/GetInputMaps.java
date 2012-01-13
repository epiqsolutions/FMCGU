package test;

import java.util.*;

import javax.swing.*;

public class GetInputMaps {
	private static void trace(String key, InputMap im) {
		System.out.println(key);
		class Pair implements Comparable<Pair> {
			String key;
			String value;

			public Pair(String key, String value) {
				this.key = key;
				this.value = value;
			}

			public int compareTo(Pair o) {
				// by key
				// int res = this.key.compareTo(o.key);
				// if (res != 0)
				// return res;
				// return this.value.compareTo(o.value);

				// by value
				int res = this.value.compareTo(o.value);
				if (res != 0)
					return res;
				return this.key.compareTo(o.key);
			}
		}
		List<Pair> pairs = new ArrayList<Pair>();
		for (KeyStroke ks : im.allKeys()) {
			pairs.add(new Pair(ks.toString(), (String) im.get(ks)));
		}
		Collections.sort(pairs);
		for (Pair m : pairs) {
			System.out.println("\t" + m.key + " : " + m.value);
		}
	}

	private static void trace() {
		UIDefaults uid = UIManager.getLookAndFeelDefaults();
		Map<String, InputMap> mapping = new TreeMap<String, InputMap>();
		for (Object key : uid.keySet()) {
			if (key instanceof String) {
				String sKey = (String) key;
				if (sKey.endsWith("nputMap")) {
					Object value = uid.get(sKey);
					if (value instanceof InputMap) {
						InputMap im = (InputMap) value;
						mapping.put(sKey, im);
					}
				}
			}
		}
		for (Map.Entry<String, InputMap> m : mapping.entrySet()) {
			trace(m.getKey(), m.getValue());
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager
							.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (Exception exc) {
					exc.printStackTrace();
				}

				System.out.println(UIManager.getLookAndFeel().getName());

				trace();
			}
		});
	}

}
