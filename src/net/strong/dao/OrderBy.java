package net.strong.dao;

public interface OrderBy extends Condition {

	OrderBy asc(String name);

	OrderBy desc(String name);
}
