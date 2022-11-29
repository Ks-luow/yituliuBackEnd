package com.lhs.bot;

import com.alibaba.fastjson.JSONObject;
import com.lhs.bot.QqRobotService;
import com.lhs.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.acl.LastOwnerException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Component
public class WebSocketConfig {

    @Value("${bot.idAddress}")
    private String idAddress ;

    @Autowired
    private ApiService apiService;

    public Boolean flag = false;

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            WebSocketClient  webSocketClient = new WebSocketClient(new URI("ws://"+idAddress+":5701"),new Draft_6455()) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("[websocket] 连接成功");
                }

                @Resource
                private QqRobotService robotService;

                @Override
                public void onMessage(String message) {


                    JSONObject qqMessage = JSONObject.parseObject(message);
//                System.out.println(qqMessage);
//                    if(qqMessage.get("sub_type")!=null&&qqMessage.get("group_id")!=null){
//                        Object sub_type = qqMessage.get("sub_type");
//                            long group_id = Long.parseLong(qqMessage.get("group_id").toString());
//                            if (group_id == 572030857L ) {
//                                if ("approve".equals(sub_type)) {
//                                    String welcomeMessage = "欢迎来到方舟最大博朗台聚集地";
//                                    robotService.sendMessage(group_id, welcomeMessage);
//                                }
//                            }
//                    }


                    if(qqMessage.get("raw_message")!=null){
                        String raw_message = qqMessage.get("raw_message").toString().trim();

                        String roleName = "默认";
                        int group_id = Integer.parseInt( qqMessage.get("group_id").toString());

                         if(raw_message.endsWith("专精")||raw_message.endsWith("材料")||raw_message.endsWith("精二")){
                            roleName = raw_message.substring(0,raw_message.length()-2);
                            robotService.sendSkillImg(group_id,roleName);
                            apiService.addVisits("bot");
                            log.info("查询专精---"+roleName);
                        }if(raw_message.endsWith("模组")){
                            roleName = raw_message.substring(0,raw_message.length()-2);
                            robotService.sendModImg(group_id,roleName);
                            log.info("查询模组---"+roleName);
                            apiService.addVisits("bot");
                        }if(raw_message.endsWith("技能")){
                            roleName = raw_message.substring(0,raw_message.length()-2);
                            robotService.sendCharImg(group_id,roleName);
                            log.info("查询技能---"+roleName);
                            apiService.addVisits("bot");
                        }
                        if(raw_message.startsWith("材料一图流")||raw_message.startsWith("材料掉率")){
                            robotService.sendItemImg("",group_id);
                            log.info("查询材料---"+roleName);
                            apiService.addVisits("bot");
                        }
                        if(raw_message.startsWith("活动材料")||raw_message.startsWith("活动材料掉率")){
                            robotService.sendItemImg("act",group_id);
                            log.info("查询材料---"+roleName);
                            apiService.addVisits("bot");
                        }
                        if(raw_message.startsWith("商店性价比")){
                            robotService.sendItemImg("store",group_id);
                            log.info("查询材料---"+roleName);
                            apiService.addVisits("bot");
                        }

                        if(raw_message.startsWith("help")){
                            String helpMessage  =  "可用命令格式：\n干员名模组\n干员名技能\n干员名专精\n查看材料掉率：材料掉率、材料一图流、活动材料掉率" +
                                    "\n直接发送游戏截图查询公招组合";
                            robotService.sendMessage(group_id,helpMessage);
                            log.info("命令---"+roleName);
                        }

                        if (raw_message.startsWith("[CQ:image")) {
                            String substring = raw_message.substring(raw_message.indexOf("=")+1, raw_message.indexOf(",subType"));
                            boolean flag = robotService.imageOcr(substring, group_id);
                            log.info("查询公招---"+substring);
                            if(flag){
                                apiService.addVisits("bot");
                            }
                        }
                    }

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("[websocket] 退出连接");
                    flag = true;
                }

                @Override
                public void onError(Exception ex) {
                    log.info("[websocket] 连接错误={}",ex.getMessage());
                }
            };

            webSocketClient.connect();

            Timer t = new Timer();
            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (flag) {
                        try {
                            webSocketClient.reconnect();
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            },2000,5000);//5秒执行一次 然后休息2秒

            return webSocketClient;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}