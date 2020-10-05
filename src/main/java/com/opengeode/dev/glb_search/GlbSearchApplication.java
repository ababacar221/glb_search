package com.opengeode.dev.glb_search;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opengeode.dev.glb_search.helper.jms.JmsConsumer;
import com.opengeode.dev.glb_search.helper.jms.JmsProducer;
import com.opengeode.dev.glb_search.helper.opencsv.FileHelper;
import com.opengeode.dev.glb_search.model.MessageStorage;
import com.opengeode.dev.glb_search.model.execution_flow.ExecutionFlow;
import com.opengeode.dev.glb_search.service.CsvService;
import com.opengeode.dev.glb_search.service.ElasticsearchService;
import com.opengeode.dev.glb_search.task.LoadDataTask;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;

import java.io.IOException;
import java.util.Timer;

@SpringBootApplication
@Slf4j
public class GlbSearchApplication extends SpringBootServletInitializer {

    @Value("${glb.activemq.queue}")
    private String destination;

    @Value("${path.directory.config}")
    private String directory_data_config;

    @Value("${path.directory.data}")
    private String directory_data;

    @Autowired
    private CsvService csvService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private FileHelper fileHelper;

    private JmsConsumer jmsConsumer;

    @Autowired
    private MessageStorage messageStorage;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GlbSearchApplication.class);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GlbSearchApplication.class, args);
        context.getBean(JmsTemplate.class);

    }

    @Bean
    public void readSchema() throws IOException {
        log.info("READING SCHEMA ...");
        elasticsearchService.readJsonSchemaConfig(fileHelper.readerFileConfig(directory_data_config));
        csvService.readConfig(destination, fileHelper.readerFileConfig(directory_data));
    }

    @Bean
    public void someFunction() {
        new Thread(new Runnable() {
            @SneakyThrows
            public void run() {
                while (true) {
                    csvService.readConfig(destination, fileHelper.readerFileConfig(directory_data));
                }
            }
        }).start();
    }


//	@Bean
//	public void task() throws IOException, InterruptedException {
//		new Timer().scheduleAtFixedRate(new LoadDataTask(), 0, 3000);
//		while (true) {
//			csvService.readConfig(destination, fileHelper.readerFileConfig(directory_data));
//			Thread.sleep(3000);
//		}
//	}


}
