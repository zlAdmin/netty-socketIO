package com.zl.socket.Messages;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * 用户信息
 *
 * @author zhanglei
 * @ProjectName: netty-socketio
 * @create 2019-08-09 17:50
 * @Version: 1.0
 * <p>Copyright: Copyright (acmtc) 2019</p>
 **/
@Getter
@Setter
public class UserMessageInfo implements Serializable{
    private UUID uuid;
    private String roomId;
    private String msgType;
    private String msg;
    private String eventType;
    private String nickName;

}
