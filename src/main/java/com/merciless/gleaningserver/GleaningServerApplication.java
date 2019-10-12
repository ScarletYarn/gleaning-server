package com.merciless.gleaningserver;

import com.merciless.gleaningserver.configuration.Config;
import com.merciless.gleaningserver.service.Provider;
import com.merciless.gleaningserver.service.Server;
import com.merciless.gleaningserver.service.thrift.ClientService;
import com.merciless.gleaningserver.service.thrift.ServerCommunication;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class GleaningServerApplication {

	public static ClientService.Processor cProcessor;
	  
	public static ServerCommunication.Processor sProcessor;
	
	public static void main(String[] args) {
		SpringApplication.run(GleaningServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(Server server, Provider provider, Config config) {
		return (args) -> {

			log.info("The application has started...");

			try {
				cProcessor = new ClientService.Processor(server);
				sProcessor = new ServerCommunication.Processor(provider);

				Runnable simple = new Runnable(){
				
					@Override
					public void run() {
						simple(cProcessor, sProcessor, config);
					}
				};

				new Thread(simple).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	public static void simple(ClientService.Processor cProcessor, ServerCommunication.Processor sProcessor, Config config) {
		
		try {
			TServerTransport serverTransport = new TServerSocket(config.SERVER_PORT);
			TServerTransport providerTransport = new TServerSocket(config.PROVIDER_PORT);
			TServer server = new TSimpleServer(new Args(serverTransport).processor(cProcessor));
			TServer provider = new TSimpleServer(new Args(providerTransport).processor(sProcessor));

			server.serve();
			log.info("the server has started...");

			provider.serve();
			log.info("the provider has started...");

		} catch (Exception e) {

			log.error("Server failed to start. ");
			e.printStackTrace();
		}
	}

}
