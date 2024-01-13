package com.app.spotick.service.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class UserProfileFileServiceImplTest {

    @Autowired
    UserProfileFileServiceImpl profileFileService;

    @Test
    void fileDirTest(){

    }



}