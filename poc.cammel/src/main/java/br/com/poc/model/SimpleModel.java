package br.com.poc.model;

import java.io.Serializable;

public class SimpleModel implements Serializable{
	
	private static final long serialVersionUID = -5251937721822539642L;
	private String id;
	private String name;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "SimpleModel [id=" + id + ", name=" + name + "]";
	}
}
