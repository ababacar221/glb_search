package com.opengeode.dev.glb_search;

import com.opengeode.dev.glb_search.model.execution_flow.ExecutionFlow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import java.util.Date;

@SpringBootApplication
public class GlbSearchApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(GlbSearchApplication.class, args);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		System.out.println("SENDING an EXECUTION FLOW message.");
		jmsTemplate.convertAndSend("logbox",new ExecutionFlow(6,new Date().toString(),"GFGG","Entities","Job","Comp","Type","err","errMessage"));

	}

}
