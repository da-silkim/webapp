package com.bluenetworks.webapp.app.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileUtil {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final FileInfoRegMapper fileInfoRegMapper;

    public Map<String, Object> commImageUpdate(@RequestParam("file") MultipartFile mf, String path
            , Map<String, Object> paramMap, HttpServletRequest req)  {

        Map<String, Object> rtnMap = new HashMap<>();

        log.info("new ■■■■■■■■■■■■■■■■■ 파일 업로드 시작 ■■■■■■■■■■■■■■■■■ ");


        String orgFileName = mf.getOriginalFilename();
        String ext = orgFileName.substring(orgFileName.lastIndexOf("."), orgFileName.length());

        log.info("orgFileName : "+orgFileName);
        log.info("extension : "+ext);

        //폴더 생성
        String folderPath = "/" + path + "/";
        File uploadPathFolder = new File(uploadPath, folderPath);
        if(uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }

        String saveName = RandomStringUtils.randomAlphabetic(5) + String.valueOf(System.currentTimeMillis()) + ext;
        log.info("saveName : "+saveName);
        String fileSavePath = uploadPath + folderPath + saveName;
        Path savePath = Paths.get(fileSavePath);

        try {
            mf.transferTo(savePath);
            rtnMap.put("atchFileId", fileInfoRegMapper.selectAtchFileId());
            rtnMap.put("atchFileCd", "01");
            rtnMap.put("filePath", folderPath);
            rtnMap.put("fileNm", orgFileName);
            rtnMap.put("chFileNm", saveName);
            rtnMap.put("sessionUserId", paramMap.get("userId"));

            fileInfoRegMapper.insertFileUpload(rtnMap);


        } catch (IOException e) {
            if(log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
        }

        return rtnMap;

    }
}
