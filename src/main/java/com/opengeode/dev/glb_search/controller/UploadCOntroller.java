package com.opengeode.dev.glb_search.controller;

import com.opencsv.bean.CsvToBean;
import com.opengeode.dev.glb_search.helper.jms.JmsHelper;
import com.opengeode.dev.glb_search.model.execution_flow.ExecutionFlow;
import com.opengeode.dev.glb_search.service.CsvService;
import com.opengeode.dev.glb_search.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Controller
public class UploadCOntroller {

    @Autowired
    private JmsHelper jmsHelper;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private CsvService csvService;
    @Value("${activemq.destination}")
    private String destination;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file")MultipartFile file, Model model){
        //Validate FIle
        if (!file.isEmpty()){
            model.addAttribute("message","Please select a CSV FILE to upload.");
            model.addAttribute("status",false);
            String fileName = String.format("%s",destination);
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                // create csv bean reader
                CsvToBean csvToBean = csvService.readCSV(reader,new ExecutionFlow());
                List<ExecutionFlow> executionFlows = csvToBean.parse();
                csvService.writeCSV(executionFlows,String.format("%s.csv",fileName));

                // Save csv bean in elasticsearch
                elasticsearchService.ingest_data(executionFlows,fileName);
                // send a csv object in activemq
                jmsHelper.sendTo(destination,executionFlows);
                model.addAttribute("messages_logs", executionFlows);
                model.addAttribute("status", true);

            } catch (Exception e){
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }
        return "file-upload-status";
    }
}
