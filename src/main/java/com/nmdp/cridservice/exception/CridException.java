/**
 * 
 */
package com.nmdp.cridservice.exception;

/**
 * @author mdudyala
 *
 */
public class CridException {
	
	private String errorMessage;
	 
    public CridException(String errorMessage){
        this.errorMessage = errorMessage;
    }
 
    public String getErrorMessage() {
        return errorMessage;
    }

}
