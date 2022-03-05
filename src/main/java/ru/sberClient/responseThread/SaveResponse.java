package ru.sberClient.responseThread;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SaveResponse {

     long tradeID;
     long responseTime;
     String responseStatus;
     Boolean responseError;


}
