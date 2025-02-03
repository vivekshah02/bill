package com.example.exception;

public class ErrorResponse {

	 private String message;
	    private int status;
	    private String timestamp;

	    public ErrorResponse(String message, int status, String timestamp) {
	        this.message = message;
	        this.status = status;
	        this.timestamp = timestamp;
	    }

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
	    
}
