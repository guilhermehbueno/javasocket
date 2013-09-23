package br.com.poc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mina2.Mina2Endpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultMessage;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import br.com.poc.model.SimpleModel;

public class MinaCamelFlowTest {
	
	static CamelContext clientContext;
	static CamelContext serverContext;
	static Mina2Endpoint endpoint;
	
	@BeforeClass
	public void init() throws Exception{
		serverContext = new DefaultCamelContext();
		serverContext.addRoutes(new RouteBuilder() {
            public void configure() {
            	//from("mina:tcp://localhost:6200?textline=true&sync=false").to("file://mina");
            	from("mina2:tcp://localhost:6200?textline=true&sync=false").process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						try{
							Object body = exchange.getIn().getMandatoryBody();
							System.out.println("Received: " +body.getClass().getName());
							System.out.println("Received: " +body);
							exchange.getOut().setBody("Documents was received " + body, String.class);
						}catch(Exception exc){
							exc.printStackTrace();
						}
					}
				});
            	//from("mina2:tcp://localhost:6200?textline=true&sync=false")
            	//.bean(new SimpleModelReceiver());
            }
        });
		serverContext.start();
	}
	
	@AfterClass
	public void end() throws Exception{
		 Thread.sleep(3000);
		 serverContext.stop();
	}
	
	@BeforeTest
	public void before() throws Exception{
		clientContext = new DefaultCamelContext();
		//clientContext.start();
		endpoint=clientContext.getEndpoint("mina2:tcp://localhost:6200?textline=true&sync=false",Mina2Endpoint.class);
	}
	
	@AfterTest
	public void after() throws Exception{
		Thread.sleep(3000);
		//clientContext.stop();
	}
	
	//@Test
	public void test() throws Exception{
		ProducerTemplate template = clientContext.createProducerTemplate();
		template.sendBody(endpoint, "GUILHERME H BUENO 2");
	}
	
	@Test
	public void testSimpleModelBody() throws Exception{
		SimpleModel model = new SimpleModel();
		model.setId(UUID.randomUUID().toString());
		model.setName("SIMPLE MODEL");

		//DefaultMessage defaultMessage = new DefaultMessage();
		//defaultMessage.setBody(model, SimpleModel.class);
		//Producer prod = endpoint.createProducer();
		//Exchange exchange = prod.createExchange();
		//exchange.setIn(defaultMessage);
		
		ProducerTemplate template = clientContext.createProducerTemplate();
		//template.sendBody(endpoint, exchange);
		String result = template.requestBody(endpoint, model, String.class);
		Assert.assertNotNull(result);
		System.out.println("Result: "+ result);
	}
	
	public ByteArrayOutputStream serialize(Object instance){
		ByteArrayOutputStream arq = null;
		ObjectOutputStream out = null;
		try {
			//arquivo no qual os dados vao ser gravados
			arq = new ByteArrayOutputStream();
			//objeto que vai escrever os dados
			out = new ObjectOutputStream(arq);
			//escreve todos os dados
			out.writeObject(instance);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				arq.close();
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return arq;
	}
}
