package net.strong.dao.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DTable {

	private String name;

	private List<DField> pks;

	private List<DField> ais;

	private List<DField> fields;

	private Map<String, DField> maps;

	public DTable() {
		fields = new LinkedList<DField>();
		pks = new LinkedList<DField>();
		ais = new LinkedList<DField>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DTable addField(DField field) {
		fields.add(field);
		if (field.isAutoIncreament())
			ais.add(field);
		if (field.isPrimaryKey())
			pks.add(field);
		return this;
	}

	public List<DField> getPks() {
		return pks;
	}

	public String getPkNames() {
		if (pks.isEmpty())
			return null;
		Iterator<DField> it = pks.iterator();
		StringBuilder sb = new StringBuilder(it.next().getName());
		while (it.hasNext())
			sb.append(",").append(it.next().getName());
		return sb.toString();
	}

	public String getPkNames(char wrapper) {
		if (pks.isEmpty())
			return null;
		if (wrapper < 32)
			return getPkNames();

		Iterator<DField> it = pks.iterator();
		StringBuilder sb = new StringBuilder(it.next().getName());
		while (it.hasNext())
			sb.append(",").append(wrapper).append(it.next().getName()).append(wrapper);
		return sb.toString();
	}

	public List<DField> getAutoIncreaments() {
		return ais;
	}

	public List<DField> getFields() {
		return fields;
	}

	public DField getField(String name) {
		if (null == maps) {
			synchronized (this) {
				if (null == maps) {
					maps = new HashMap<String, DField>();
					for (DField df : fields)
						maps.put(df.getName(), df);
				}
			}
		}
		return maps.get(name);
	}

	public boolean hasField(String name) {
		return null != getField(name);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(name);
		sb.append(" {");
		/*
		 * PK
		 */
		List<DField> list = new ArrayList<DField>(fields.size());
		for (DField df : fields) {
			if (df.isPrimaryKey())
				list.add(df);
		}
		sortFields(list);
		appendFieldToSb(sb, list);
		/*
		 * Uniques
		 */
		list = new ArrayList<DField>(fields.size());
		for (DField df : fields) {
			if (df.isUnique() && !df.isPrimaryKey())
				list.add(df);
		}
		sortFields(list);
		appendFieldToSb(sb, list);
		/*
		 * Others
		 */
		list = new ArrayList<DField>(fields.size());
		for (DField df : fields) {
			if (!df.isUnique() && !df.isPrimaryKey())
				list.add(df);
		}
		sortFields(list);
		appendFieldToSb(sb, list);

		sb.deleteCharAt(sb.length() - 1);
		sb.append("\n}");
		return sb.toString();
	}

	private static void appendFieldToSb(StringBuilder sb, List<DField> list) {
		for (DField df : list) {
			sb.append("\n\t").append(df.toString()).append(",");
		}
	}

	private static void sortFields(List<DField> list) {
		Collections.sort(list, new Comparator<DField>() {
			public int compare(DField o1, DField o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

}
