package br.com.cursomc.sbinc.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> errors = new ArrayList<>();
	
	public ValidationError(Integer status, String msg, Long timeStamp, String path) {
		super(status, msg, timeStamp, path);
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

	public void addErrors(String field, String message) {
		this.errors.add(new FieldMessage(field, message));
	}
}
