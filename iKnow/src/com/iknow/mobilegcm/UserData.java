package com.iknow.mobilegcm;

public  class UserData {

	//private variables
    int _id;
    String _imei;
    String _name;
    String _message;
    
    // Empty constructor
    public UserData(){
 
    }
    // constructor
    public UserData(int id, String imei, String name, String message){
        this._id      = id;
        this._imei    = imei;
        this._name    = name;
        this._message = message;
        
    }
 
    // getting ID
    public int getID(){
        return this._id;
    }
 
    // setting id
    public void setID(int id){
        this._id = id;
    }
 
    // getting imei
    public String getIMEI(){
        return this._imei;
    }
 
    // setting imei
    public void setIMEI(String imei){
        this._imei = imei;
    }
    
    // getting name
    public String getName(){
        return this._name;
    }
 
    // setting name
    public void setName(String name){
        this._name = name;
    }
    
    // getting Message
    public String getMessage(){
        return this._message;
    }
 
    // setting Message
    public void setMessage(String message){
        this._message = message;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserInfo [name=" + _name + "]";
	}
	
}
