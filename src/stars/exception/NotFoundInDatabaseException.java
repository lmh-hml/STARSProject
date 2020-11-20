package stars.exception;

import java.util.Objects;

public class NotFoundInDatabaseException extends Exception {

	public NotFoundInDatabaseException() {
		// TODO Auto-generated constructor stub
	}

	public NotFoundInDatabaseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public NotFoundInDatabaseException(String message, Object... args) {
		super(String.format(message, args));
		// TODO Auto-generated constructor stub
	}

	public NotFoundInDatabaseException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NotFoundInDatabaseException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotFoundInDatabaseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
