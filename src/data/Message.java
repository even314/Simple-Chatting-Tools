package data;

import java.io.Serializable;

public class Message implements Serializable{
private Integer type;
private Object object;
public Message() {
	
}
public Message(Integer type,Object object) {
	this.type=type;
	this.object=object;
}

public Object getobject() {
	return object;
}
public void setobject(Object object) {
	this.object=object;
}
public Integer gettype() {
	return type;
}
public void settype(Integer type) {
	this.type=type;
}
}
