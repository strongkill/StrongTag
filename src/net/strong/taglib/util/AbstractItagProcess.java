package net.strong.taglib.util;

import net.strong.hibernate.BaseDAO;



public abstract class AbstractItagProcess extends BaseDAO implements ItagProcess {
	private long maxPage=0;
	private long pageMax=0;
	private long MaxReord=0;
	
	public long getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(long maxPage) {
		this.maxPage = maxPage;
	}

	public long getPageMax() {
		return pageMax;
	}

	public void setPageMax(long pageMax) {
		this.pageMax = pageMax;
	}

	public long getMaxReord() {
		return MaxReord;
	}

	public void setMaxReord(long maxReord) {
		MaxReord = maxReord;
	}

	public final void init(long maxPage,long pageMax ,long MaxRecord){
		this.maxPage = maxPage;
		this.pageMax = pageMax;
		this.MaxReord = MaxRecord;
	}

	
	
}
