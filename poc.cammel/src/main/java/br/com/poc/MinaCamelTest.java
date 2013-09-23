package br.com.poc;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class MinaCamelTest {
	
	public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
            	//from("mina:tcp://localhost:6200?textline=true&sync=false").to("file://mina");
            	from("mina:tcp://localhost:6200?textline=true&sync=false").process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						String body = exchange.getIn().getBody(String.class);
						System.out.println(body);
						exchange.getOut().setBody("Documents was received " + body);
					}
				});
            }
        });
	
        context.start();
	
        ProducerTemplate template = context.createProducerTemplate();
        template.sendBody("mina:tcp://localhost:6200?textline=true&sync=false", "Hello World");
         
        Thread.sleep(1000);
        context.stop();
	}
}
