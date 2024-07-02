package com.bluenetworks.webapp.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
class CommonUtilTest {

    @Test
    @DisplayName("changePassword encryption test")
    void changePassword(){
        CommonUtil commonUtil = new CommonUtil();

        String s = commonUtil.encryption_sha256("exit@1169611");

        System.out.println("s = " + s);
    }

}