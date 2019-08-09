package com.zl.socket.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.zl.socket.Messages.UserMessageInfo;
import com.zl.socket.constant.WebSocketConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息事件监听
 *
 * @author zhanglei
 * @ProjectName: netty-socketio
 * @create 2019-08-09 17:45
 * @Version: 1.0
 * <p>Copyright: Copyright (acmtc) 2019</p>
 **/
@Component
@Slf4j
public class MessageEventHandler {

    private AtomicInteger onLine = new AtomicInteger(0);

    @Autowired
    private SocketIOServer server;

    @OnConnect
    public void onConnect(SocketIOClient client)
    {
        int i = onLine.addAndGet(1);
        log.info("client connect to the server ,sessionId={} ,onLine user={}",client.getSessionId().toString(), i);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client)
    {
        onLine.decrementAndGet();
        log.info("client disconnect to the server ,sessionId={}",client.getSessionId().toString());
    }

    @OnEvent(value = "userEvent")
    public void onUserEvent(SocketIOClient client, AckRequest request, UserMessageInfo data)
    {
        Objects.requireNonNull(data);
        server.getNamespace(WebSocketConstant.Namespace).getRoomOperations(data.getRoomId()).sendEvent(data.getEventType(),data);
    }

    @OnEvent(value = "userJoinRoom")
    public void studentConectEvent(SocketIOClient client, AckRequest request, UserMessageInfo data)
    {
        Objects.requireNonNull(data);
        client.joinRoom(data.getRoomId());
        log.info("user join the room,roomId={},studentNickName={}",data.getRoomId(),data.getNickName());
        server.getRoomOperations(data.getRoomId()).sendEvent(data.getEventType(),data);
    }

}
