package com.zl.socket.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.zl.socket.Messages.UserMessageInfo;
import com.zl.socket.constant.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * 消息发送
 *
 * @author zhanglei
 * @ProjectName: netty-socketio
 * @create 2019-08-09 18:06
 * @Version: 1.0
 * <p>Copyright: Copyright (zl) 2019</p>
 **/
@Controller
@RequestMapping("/socketio")
public class SendMsgController {
    @Autowired
    private SocketIOServer server;

    @ResponseBody
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public void readMessage(@RequestBody String channel) {
        UUID uuid = UUID.fromString(channel);
        UserMessageInfo info = new UserMessageInfo();
        info.setNickName("zlAdmin");
        info.setMsg("hello 这里是你的第一条信息");
        server.getClient(uuid).sendEvent(Event.CHAT_EVENT, info);

    }
}
