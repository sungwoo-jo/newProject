-- member(회원 테이블)
DROP TABLE IF EXISTS `member` CASCADE;
CREATE TABLE `member` (
    memNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '회원번호',
    memId VARCHAR(50) NOT NULL comment '아이디',
    memPw VARCHAR(500) NOT NULL comment '비밀번호',
    memNm VARCHAR(20) NOT NULL comment '이름',
    nickNm VARCHAR(20) NOT NULL comment '닉네임',
    address1 VARCHAR(150) DEFAULT NULL comment '주소',
    address2 VARCHAR(100) DEFAULT NULL comment '상세주소',
    zipCode CHAR(5) DEFAULT NULL comment '우편번호',
    phone VARCHAR(20) DEFAULT NULL comment '휴대폰번호',
    email VARCHAR(50) DEFAULT NULL comment '이메일',
    profilePath VARCHAR(200) DEFAULT NULL comment '프로필이미지경로',
    profileImage VARCHAR(200) DEFAULT NULL comment '프로필이미지파일명',
    deleteYn BOOLEAN DEFAULT '0' comment '탈퇴여부',
    regDt DATETIME NOT NULL DEFAULT NULL comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- memberSns(간편가입 테이블)
DROP TABLE IF EXISTS `memberSns` CASCADE;
CREATE TABLE `memberSns` (
    sno INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '번호',
    memNo INT NOT NULL comment '회원번호',
    snsType VARCHAR(10) NOT NULL comment 'SNS타입',
    accessToken VARCHAR(400) NOT NULL comment '액세스토큰',
    refreshToken VARCHAR(400) NOT NULL comment '갱신토큰',
    regDt DATETIME NOT NULL DEFAULT NULL comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- travelBoard(여행게시판 테이블)
DROP TABLE IF EXISTS `travelBoard` CASCADE;
CREATE TABLE `travelBoard` (
    boardNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '게시글번호',
    memNo INT DEFAULT NULL comment '회원번호',
    subject VARCHAR(100) NOT NULL comment '제목',
    contents MEDIUMTEXT NOT NULL comment '내용',
    deleteYn BOOLEAN DEFAULT '0' comment '삭제여부',
    likeCnt INT DEFAULT 0 comment '좋아요수',
    hitCnt INT DEFAULT 0 comment '조회수',
    district VARCHAR(100) DEFAULT NULL comment '여행지',
    regDt DATETIME NOT NULL comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- reply(댓글 테이블)
DROP TABLE IF EXISTS `reply` CASCADE;
CREATE TABLE `reply` (
    replyNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '댓글번호',
    boardNo INT NOT NULL comment '게시글번호',
    memNo INT DEFAULT NULL comment '회원번호',
    contents TEXT NOT NULL comment '내용',
    depth INT DEFAULT 0 comment '뎁스(대댓글 표현)',
    seqNo INT DEFAULT 0 comment '댓글순서',
    parentNo INT DEFAULT NULL comment '부모댓글번호',
    deleteYn BOOLEAN DEFAULT '0' comment '삭제여부',
    likeCnt INT DEFAULT 0 comment '좋아요수',
    regDt DATETIME NOT NULL comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일',
    FOREIGN KEY (boardNo) REFERENCES travelBoard(boardNo)
);

-- post(쪽지 테이블)
DROP TABLE IF EXISTS `post` CASCADE;
CREATE TABLE `post` (
    postNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '쪽지번호',
    senderMemNo INT NOT NULL comment '발신회원번호',
    receiverMemNo INT NOT NULL comment '수신회원번호',
    contents MEDIUMTEXT NOT NULL comment '내용',
    readYn BOOLEAN DEFAULT '0' NOT NULL comment '읽음여부',
    sendDt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP comment '전송일',
    readDt DATETIME DEFAULT NULL comment '확인일',
    regDt DATETIME NOT NULL comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일',
    FOREIGN KEY (senderMemNo) REFERENCES member(memNo),
    FOREIGN KEY (receiverMemNo) REFERENCES member(memNo)
);

-- chatInfo(채팅방정보 테이블)
DROP TABLE IF EXISTS `chatInfo` CASCADE;
CREATE TABLE `chatInfo` (
    chatNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '채팅방번호',
    chatNm VARCHAR(50) DEFAULT NULL comment '채팅방이름',
    entrant TEXT DEFAULT NULL comment '참여자리스트',
    regDt DATETIME NOT NULL comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- message(채팅메시지 테이블)
DROP TABLE IF EXISTS `message` CASCADE;
CREATE TABLE `message` (
    messageNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '메시지번호',
    chatNo INT NOT NULL comment '채팅방번호',
    senderMemNo INT NOT NULL comment '발신회원번호',
    contents TEXT NOT NULL comment '내용',
    sendDt DATETIME NOT NULL DEFAULT NULL comment '전송일',
    readYn BOOLEAN DEFAULT '0' NOT NULL comment '읽음여부',
    regDt DATETIME NOT NULL comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일',
    FOREIGN KEY (chatNo) REFERENCES chatInfo(chatNo),
    FOREIGN KEY (senderMemNo) REFERENCES member(memNo)
);

-- config(설정데이터 테이블)
DROP TABLE IF EXISTS `config` CASCADE;
CREATE TABLE `config` (
    type VARCHAR(50) NOT NULL comment '설정타입',
    data JSON DEFAULT NULL comment '설정데이터',
    regDt DATETIME NOT NULL comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- 테스트 데이터(회원)
INSERT INTO member(memId, memPw, memNm, nickNm, address1, address2, zipCode, phone, email, profileImage, regDt) VALUES ('test1', 'test1', '테스트1', 'test1', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '11111', '01011111111', 'sungwoo9671@naver.com', '/test/test1', now());
INSERT INTO member(memId, memPw, memNm, nickNm, address1, address2, zipCode, phone, email, profileImage, regDt) VALUES ('test2', 'test2', '테스트2', 'test2', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '22222', '01022222222', 'test2@naver.com', '/test/test2', now());
INSERT INTO member(memId, memPw, memNm, nickNm, address1, address2, zipCode, phone, email, profileImage, regDt) VALUES ('test3', 'test3', '테스트3', 'test3', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '33333', '01033333333', 'test3@naver.com', '/test/test3', now());
INSERT INTO member(memId, memPw, memNm, nickNm, address1, address2, zipCode, phone, email, profileImage, regDt) VALUES ('test4', 'test4', '테스트4', 'test4', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '44444', '01044444444', 'test4@naver.com', '/test/test4', now());