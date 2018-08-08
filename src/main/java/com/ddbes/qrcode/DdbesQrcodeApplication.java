package com.ddbes.qrcode;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.ddbes.qrcode.dao.PassCodeRecordMapper;
import com.didispace.swagger.EnableSwagger2Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableSwagger2Doc
//@EnableEurekaClient
//@EnableFeignClients
public class DdbesQrcodeApplication {

	@Value("${conf.nio.server.host}")
	private String host;
	@Value("${conf.nio.server.port}")
	private Integer port;

	@Autowired
	private PassCodeRecordMapper passCodeRecordMapper;

	public static void main(String[] args) {
		SpringApplication.run(DdbesQrcodeApplication.class, args);
	}

	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
		configuration.setHostname(host);
		configuration.setPort(port);
		final SocketIOServer socketIOServer = new SocketIOServer(configuration);
		return socketIOServer;
	}

	@Bean
	public SpringAnnotationScanner createServer(SocketIOServer socketIOServer) {
		//连接服务器做记录
		socketIOServer.addConnectListener((i) -> {
			if (i != null) {
				String clientId = i.getHandshakeData().getSingleUrlParam("clientId");
				if(clientId==null){
					return;
				}
				passCodeRecordMapper.updateUuidById(Long.parseLong(clientId),i.getSessionId().toString());
				System.out.println("二维码"+clientId+",uuid:"+i.getSessionId() + "链接成功");
			} else {
				System.out.println("无人链接...");
			}
		});
		socketIOServer.addEventListener("msg2", String.class, (i, j, k) -> {
			System.out.println(i);
			System.out.println("接收到的数据为:" + j);
		});
		socketIOServer.addEventListener("msg", String.class, (i, j, k) -> {
			System.out.println(i);
			System.out.println("接收到的数据为:" + j);
		});
		socketIOServer.addDisconnectListener((i) -> {
			System.out.println(i.getSessionId() + "关闭链接");
		});
		socketIOServer.start();
		return new SpringAnnotationScanner(socketIOServer);
	}
}
