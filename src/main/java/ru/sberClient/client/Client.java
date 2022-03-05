package ru.sberClient.client;

import groovyjarjarcommonscli.*;
import lombok.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import ru.sberClient.responseThread.SaveResponse;


import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class Client {
    public static void main(String[] args) throws InterruptedException, ParseException {

        Options options = new Options();
        Option thread = new Option("t","threads",true,"threads Count");
        thread.setRequired(true);
        options.addOption(thread);

        Option ip = new Option("i","ip",true,"ip address");
        ip.setRequired(true);
        options.addOption(ip);

        Option port = new Option("p","port",true,"ports");
        port.setRequired(true);
        options.addOption(port);

        Option countRequest = new Option("c","count",true,"counts");
        countRequest.setRequired(true);
        options.addOption(countRequest);


      CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cmdThread =  commandLineParser.parse(options,args);
        Integer threads = Integer.valueOf(cmdThread.getOptionValue("threads"));

        CommandLine cmdIP =  commandLineParser.parse(options,args);
        String ipAddress = cmdIP.getOptionValue("ip");

        CommandLine cmdPort = commandLineParser.parse(options,args);
        Integer ports = Integer.valueOf(cmdPort.getOptionValue("port"));

        CommandLine cmdCountRequest = commandLineParser.parse(options,args);
        Integer counts = Integer.valueOf(cmdCountRequest.getOptionValue("count"));


        List<Future> list = new ArrayList<>(); //collection 1

        ExecutorService service = Executors.newFixedThreadPool(threads);
        CompletionService<SaveResponse> completionService = new ExecutorCompletionService<>(service);

        for (int i = 0; i < threads; i++) {
          list.add(service.submit(new Process(ipAddress, ports, counts)));
        }

        int received = 0;
        boolean errors = false;

        while(received < threads && !errors) {
            Future<SaveResponse> resultFuture = completionService.take(); //blocks if none available
            try {
                SaveResponse saveResponse = resultFuture.get();
                received ++;
            }
            catch(Exception e) {
                errors = true;
            }
        }
    }
}


@ToString
class Process implements Callable {

   private final String ip;
   private final Integer port;
    private final Integer countRequest;

    Process(String  ip, Integer port, Integer countRequest) {
        this.ip = ip;
        this.port = port;
        this.countRequest = countRequest;
    }


    @Override
    public Object call() {
        final String URL_EMPLOYEES = "http://localhost:8080/test/client";
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://");
        urlBuilder.append(ip);
        urlBuilder.append(":");
        urlBuilder.append(port);
        urlBuilder.append("/test/client");

        ArrayList<SaveResponse> arrayList = new ArrayList<>();
        boolean errors = false;


        try {
            for (int i = 0; i < countRequest; i++) {

           RestTemplate restTemplate = new RestTemplate();
                restTemplate.getForObject(urlBuilder.toString(), String.class);

                SaveResponse result1 = SaveResponse.builder()
                        .tradeID(Thread.currentThread().getId())
                        .responseError(errors)
                        .responseTime(ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId()))
                        .responseStatus(String.valueOf(HttpStatus.OK))
                        .build();

                arrayList.add(result1);



            }
            System.out.println(arrayList);
        } catch (Exception e) {
            errors = true;
            e.printStackTrace();
        }
        return arrayList;
    }
}