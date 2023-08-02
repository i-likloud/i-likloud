package com.backend.external.nft.dto;

import com.backend.global.util.UnixTimestampDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Item {

    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date createdAt;

    private String owner;

    private String previousOwner;

    private String tokenId;

    private String tokenUri;

    private String transactionHash;

    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updatedAt;

}
