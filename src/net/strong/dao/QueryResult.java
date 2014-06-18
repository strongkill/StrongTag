package net.strong.dao;

import java.util.ArrayList;
import java.util.List;

import net.strong.castor.Castors;
import net.strong.dao.pager.Pager;

public class QueryResult {

	private List<?> list;
	private Pager pager;

	public QueryResult() {}

	public QueryResult(List<?> list, Pager pager) {
		this.list = list;
		this.pager = pager;
	}

	public List<?> getList() {
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(Class<T> eleType) {
		return (List<T>) list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> convertList(Class<T> eleType) {
		if (null == list || list.isEmpty())
			return (List<T>) list;

		List<T> re = new ArrayList<T>(list.size());
		Castors castors = Castors.me();
		for (Object obj : list)
			re.add(castors.castTo(obj, eleType));

		return re;
	}

	public QueryResult setList(List<?> list) {
		this.list = list;
		return this;
	}

	public Pager getPager() {
		return pager;
	}

	public QueryResult setPager(Pager pager) {
		this.pager = pager;
		return this;
	}

}
