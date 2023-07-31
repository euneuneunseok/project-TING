package com.ssafy.tingbackend.matching.dto;

import com.ssafy.tingbackend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketInfo {
    WebSocketSession session;
    User user;
}
