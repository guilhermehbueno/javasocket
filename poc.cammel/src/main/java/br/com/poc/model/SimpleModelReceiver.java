package br.com.poc.model;

import org.w3c.dom.Document;

public class SimpleModelReceiver {
	
	public void receive(SimpleModel simpleModel){
		System.out.println("[SimpleModel] Recebido => "+ simpleModel);
	}
	
	public void receive(String simpleModel){
		System.out.println("[String] Recebido => "+ simpleModel);
	}
	
	public void receive(Document document){
		System.out.println("[Document] Recebido => "+ document);
	}

}
