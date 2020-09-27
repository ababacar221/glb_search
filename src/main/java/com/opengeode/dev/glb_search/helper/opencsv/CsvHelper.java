package com.opengeode.dev.glb_search.helper.opencsv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@Slf4j
public class CsvHelper {

    public File createDirectoryAndFile(String dir, String fil) throws IOException {
        String fd = String.format("%s/%s",dir,fil);
        File file = new File(dir);
        if (file.exists()){
            String m = String.format("Directory : %s is already exist",fd);
            log.info(m);
            file = new File(fd);
            if (!file.exists()) file.createNewFile();

        }else{
            file.mkdir();
            file = new File(fd);
            if (!file.exists()){
                file.createNewFile();
            }else {
                String m = String.format("FILE : %s is already exist",fd);
                log.info(m);
            }
        }
        return file;
    }

//    @Bean
//    public void writeCSV(){
//        try {
//            Writer writer = Files.newBufferedWriter(createDirectoryAndFile("src/test/csv_files","message_log.csv").toPath());
//            CSVPrinter printer = CSVFormat.DEFAULT.withHeader(HeaderCsv.class)
//                    .withQuote(CSVWriter.NO_QUOTE_CHARACTER)
//                    .print(writer);
//
//            // create a list
//            List<ExecutionFlow> data = new ArrayList<>();
//            data.add(new ExecutionFlow(1, new Date().toString(), "project", "Entity","JobName","Component","Type","ErrorCode","ErrorMessage"));
//            data.add(new ExecutionFlow(2, new Date().toString(), "project1", "Entity1","JobName1","Component1","Type1","ErrorCode1","ErrorMessage1"));
//            data.add(new ExecutionFlow(3, new Date().toString(), "project2", "Entity2","JobName2","Component2","Type2","ErrorCode2","ErrorMessage2"));
//            data.add(new ExecutionFlow(4, new Date().toString(), "project3", "Entity3","JobName3","Component3","Type3","ErrorCode3","ErrorMessage3"));
//
//            // write list to file
//            printer.printRecords(data);
//
//            for (ExecutionFlow d: data){
//                System.out.println("Data: " + d.toString());
//            }
//
//            // flush the stream
//            printer.flush();
//
//            // close the writer
//            writer.close();
//        }catch (IOException ex){
//            ex.printStackTrace();
//        }
//    }

//    @Bean
//    public void readCSV() {
//        try {
//            Path path = createDirectoryAndFile("src/test/csv_files","message_log.csv").toPath();
//            Reader reader = Files.newBufferedReader(path);
//            CsvToBean csvToBean = new CsvToBeanBuilder(reader)
//                    .withType(ExecutionFlow.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .build();
//            List<ExecutionFlow> executionFlows = new ArrayList();
//
//            //for (CSVRecord record: records){
//            for (ExecutionFlow record: (Iterable<ExecutionFlow>) csvToBean){
//                executionFlows.add(record);
//                System.out.println("ID: " + record.getId());
//                System.out.println("TIMESTAMP: " + record.getTimestamp());
//                System.out.println("PROJECT: " + record.getProject());
//                System.out.println("ENTITY: " + record.getEntity());
//                System.out.println("JOB_NAME: " + record.getJobName());
//                System.out.println("COMPONENT: " + record.getComponent());
//                System.out.println("TYPE: " + record.getType());
//                System.out.println("ERROR_CODE: " + record.getError_code());
//                System.out.println("ERROR_MESSAGE: " + record.getError_message());
//            }
//            // CLoes the reader
//            elasticsearchService.ingest_data(executionFlows,"messageLog");
//            reader.close();
//
//        }catch (IOException | InterruptedException ex){
//            ex.printStackTrace();
//        }
//
//    }




}
