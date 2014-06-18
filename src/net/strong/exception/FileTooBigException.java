package net.strong.exception;

public class FileTooBigException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileTooBigException(String str){
		super(str);
	}
}
