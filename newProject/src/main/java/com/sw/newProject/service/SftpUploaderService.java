package com.sw.newProject.service;

import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.xfer.InMemorySourceFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class SftpUploaderService {

    final String PI_HOSTNAME = "121.171.123.7";
    final int PI_PORT = 16724;
    final String PI_USERNAME = "sungwoo-jo";
    final String PI_PASSWORD = "1234";

    // 외부 저장소에 FTP 업로드
    public void uploadToRemotePath(MultipartFile file, String remotePath, String fileName) throws IOException {
        final SSHClient ssh = new SSHClient();

        try {
            // ssh 연결
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.setConnectTimeout(5000);
            ssh.setTimeout(5000);

            // 2. 라즈베리파이 접속 (IP주소 확인 필수)
            ssh.connect(PI_HOSTNAME, PI_PORT);
            ssh.authPassword(PI_USERNAME, PI_PASSWORD); // 사용자명, 비밀번호

            // 3. SFTP 세션 시작
            try (SFTPClient sftp = ssh.newSFTPClient()) {


                // 1. 원격 서버에 폴더가 없으면 생성 (계층적 생성 mkdirs)
                if (sftp.statExistence(remotePath) == null) {
                    sftp.mkdirs(remotePath);
                }

                // 2. 파일명까지 포함된 최종 경로로 업로드
                String finalDestination = remotePath + "/" + fileName;

                try (InputStream is = file.getInputStream()) {
                    sftp.put(new InMemorySourceFile() {

                        @Override
                        public String getName() {
                            return fileName;
                        }

                        @Override
                        public long getLength() {
                            return file.getSize();
                        }

                        @Override
                        public InputStream getInputStream() throws IOException {
                            return is;
                        }
                    }, finalDestination);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  finally {
            ssh.disconnect();
        }
    }
}
