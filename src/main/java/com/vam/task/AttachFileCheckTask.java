package com.vam.task;

import com.vam.domain.AttachImageVo;
import com.vam.mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Component
@Slf4j
public class AttachFileCheckTask {

    @Autowired
    private AdminMapper adminMapper;

    private String getFolderYesterDay() {

        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        String str = sdt.format(calendar.getTime());

        return str.replace("-", File.separator);
    }

    @Scheduled(cron = "0 0 1 * * *")    // 매일 새벽 1시
//    @Scheduled(cron= "0 * * * * *")     // 1분마다
    public void checkFile() throws Exception {

        log.warn("File Check Task Run...............");
        LocalTime now = LocalTime.now();
        log.warn("now" + now);

        /* DB에 저장된 파일 리스트 */
        List<AttachImageVo> fileList = adminMapper.checkFileList();

        /* 비교 기준 파일 리스트(Path객체) */
        List<Path> checkPathList = new ArrayList<Path>();

        // 원본 이미지
        fileList.forEach(vo ->{
            Path path = Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName());
            checkPathList.add(path);
        });

        // 썸네일 이미지
        fileList.forEach(vo ->{
            Path path = Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName());
            checkPathList.add(path);
        });

        /* 디렉토리 파일 리스트 */
        File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();
        File[] targetFile = targetDir.listFiles();

        /* 삭제 대상 파일 리스트(분류) */
        List<File> removeFileList = new ArrayList<File>(Arrays.asList(targetFile));
        for (File file : removeFileList) {
            checkPathList.forEach(checkFile ->{
                if (file.toPath().equals(checkFile)) {
                    removeFileList.add(file);
                }
            });
        }

        /* 삭제 대상 파일 제거 */
        log.warn("file Delete : ");
        for (File file : removeFileList) {
            log.warn(String.valueOf(file));
            file.delete();
        }
        log.warn("=====================");

    }
}
