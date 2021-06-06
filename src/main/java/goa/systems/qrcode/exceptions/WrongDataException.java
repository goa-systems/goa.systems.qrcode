package goa.systems.qrcode.exceptions;

/**
 * This exception is thrown in case data is not valid.
 * 
 * @author ago
 * @since 2021-06-06
 */
public class WrongDataException extends Exception {

	private static final long serialVersionUID = 1L;

	public WrongDataException() {
		super();
	}

	public WrongDataException(String message) {
		super(message);
	}
}
