-- member(회원 테이블)
DROP TABLE IF EXISTS `member` CASCADE;
CREATE TABLE `member` (
    memNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '회원번호',
    memId VARCHAR(50) NOT NULL comment '아이디',
    memPw VARCHAR(500) NOT NULL comment '비밀번호',
    memNm VARCHAR(20) NOT NULL comment '이름',
    nickNm VARCHAR(20) NOT NULL comment '닉네임',
    comm VARCHAR(100) DEFAULT NULL comment '코멘트',
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

-- travel(여행게시판 테이블)
DROP TABLE IF EXISTS `travel` CASCADE;
CREATE TABLE `travel` (
    boardNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '게시글번호',
    memNo INT DEFAULT NULL comment '회원번호',
    writerNm VARCHAR(100) NOT NULL DEFAULT NULL comment '작성자',
    subject VARCHAR(100) NOT NULL comment '제목',
    contents MEDIUMTEXT NOT NULL comment '내용',
    deleteYn BOOLEAN DEFAULT '0' comment '삭제여부',
    likeCnt INT DEFAULT 0 comment '좋아요수',
    hitCnt INT DEFAULT 0 comment '조회수',
    hiddenFl BOOLEAN DEFAULT '0' comment '숨김여부',
    complaintFl BOOLEAN DEFAULT '0' comment '신고여부',
    foodCharge INT DEFAULT 0 comment '식비',
    transportCharge INT DEFAULT 0 comment '차비',
    etcCharge INT DEFAULT 0 comment '기타비용',
    budget INT DEFAULT 0 comment '예산',
    district VARCHAR(100) DEFAULT NULL comment '여행지',
    visitDt DATETIME NOT NULL comment '방문일',
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
    FOREIGN KEY (boardNo) REFERENCES travel(boardNo)
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

-- 더미 데이터(회원)
INSERT INTO member(memId, memPw, memNm, nickNm, comm, address1, address2, zipCode, phone, email, profileImage, regDt) VALUES ('test1', 'test1', '테스트1', 'test1', '코멘트1', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '11111', '01011111111', 'sungwoo9671@naver.com', '/test/test1', now());
INSERT INTO member(memId, memPw, memNm, nickNm, comm, address1, address2, zipCode, phone, email, profileImage, regDt) VALUES ('test2', 'test2', '테스트2', 'test2', '코멘트2', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '22222', '01022222222', 'test2@naver.com', '/test/test2', now());
INSERT INTO member(memId, memPw, memNm, nickNm, comm, address1, address2, zipCode, phone, email, profileImage, regDt) VALUES ('test3', 'test3', '테스트3', 'test3', '코멘트3', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '33333', '01033333333', 'test3@naver.com', '/test/test3', now());
INSERT INTO member(memId, memPw, memNm, nickNm, comm, address1, address2, zipCode, phone, email, profileImage, regDt) VALUES ('test4', 'test4', '테스트4', 'test4', '코멘트4', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '44444', '01044444444', 'test4@naver.com', '/test/test4', now());

-- 더미 데이터(여행게시판)
INSERT INTO travel(memNo, writerNm, subject, contents, deleteYn, likeCnt, hitCnt, hiddenFl, complaintFl, foodCharge, transportCharge, etcCharge, budget, district, visitDt, regDt, modDt)
VALUES
(1, '테스트23', '부산의 특별한 여행', '부산은 해운대의 바다와 맛있는 음식이 최고였습니다. 여행 중에 특별한 기억을 남기게 되었습니다. 맛있는 음식을 경험하며, 친구들과 함께 즐거운 시간을 보냈습니다.', FALSE, 5, 35, FALSE, FALSE, 30, 50, 20, 3000, '부산 해운대', '2025-01-18 10:00:00', NOW(), NULL),
(2, '테스트45', '서울의 잊을 수 없는 경험', '서울은 전통과 현대가 잘 어우러진 도시입니다. 여행 중에 많은 멋진 풍경을 만났고, 역사적인 장소를 돌아보며 많은 것을 배웠습니다. 혼자만의 여유를 즐겼습니다.', FALSE, 8, 42, FALSE, FALSE, 40, 60, 25, 4500, '서울 강남', '2025-01-10 14:30:00', NOW(), NULL),
(3, '테스트67', '제주도의 특별한 여행', '제주도는 아름다운 자연경관과 함께한 여정이었습니다. 멋진 바다와 산을 느끼며, 기념품을 사는 즐거움도 있었습니다. 친구들과 함께 많은 추억을 남겼습니다.', FALSE, 12, 78, FALSE, FALSE, 35, 70, 15, 3800, '제주도 서귀포', '2025-01-12 16:45:00', NOW(), NULL),
(4, '테스트89', '강릉의 특별한 여행', '강릉은 바다와 산을 동시에 느낄 수 있어 좋았습니다. 날씨도 좋고, 다양한 음식들을 경험하면서 기분 좋은 시간을 보냈습니다. 가족과 함께 즐거운 여행을 했습니다.', FALSE, 20, 90, FALSE, FALSE, 50, 80, 30, 5200, '강릉 경포대', '2025-01-15 09:15:00', NOW(), NULL),
(5, '테스트32', '서울의 매력적인 밤거리', '서울은 특히 밤거리가 매우 아름답습니다. 다양한 음식들과 함께, 번화한 거리를 걸으며 느낄 수 있는 활기찬 분위기가 인상 깊었습니다. 혼자서 조용히 여행을 즐겼습니다.', FALSE, 15, 60, FALSE, FALSE, 60, 90, 40, 5000, '서울 명동', '2025-01-20 18:00:00', NOW(), NULL),
(6, '테스트50', '부산의 낭만적인 여행', '부산에서의 여행은 정말 낭만적이었습니다. 해운대에서 바다를 바라보며 커피 한 잔을 마시는 여유로움을 즐겼습니다. 혼자만의 여유로운 시간을 가졌습니다.', FALSE, 25, 120, FALSE, FALSE, 80, 100, 50, 5500, '부산 해운대', '2025-01-14 17:30:00', NOW(), NULL),
(7, '테스트99', '강릉에서의 힐링 여행', '강릉은 바다와 산의 경치가 너무나 아름다웠습니다. 바닷가를 걸으며, 맑은 공기를 마시며 힐링의 시간을 가졌습니다. 친구들과 함께한 즐거운 여행이었습니다.', FALSE, 10, 55, FALSE, FALSE, 25, 45, 20, 3300, '강릉 경포대', '2025-01-17 11:00:00', NOW(), NULL),
(8, '테스트12', '서울에서의 역사적인 여행', '서울의 전통적인 고궁들을 방문하며 많은 것을 배웠습니다. 고즈넉한 분위기 속에서 전통의 아름다움을 느낄 수 있었습니다. 역사적인 장소에서 많은 시간을 보냈습니다.', FALSE, 18, 75, FALSE, FALSE, 60, 80, 30, 4800, '서울 종로', '2025-01-13 13:20:00', NOW(), NULL),
(9, '테스트61', '제주도에서의 여유로운 여행', '제주도의 자연경관을 만끽하며, 바다와 산을 즐기며 평화로운 시간을 보냈습니다. 특히 제주도는 여유롭게 여행하기에 좋은 곳입니다. 가족과 함께 다녀왔습니다.', FALSE, 22, 95, FALSE, FALSE, 45, 70, 35, 4500, '제주도 제주시', '2025-01-11 08:00:00', NOW(), NULL),
(10, '테스트34', '부산의 문화적 여행', '부산은 다양한 문화와 맛있는 음식들이 가득한 도시입니다. 여행 중에 부산의 문화와 예술을 체험하며, 다양한 음식들을 맛볼 수 있었습니다. 친구들과 함께 다녀왔습니다.', FALSE, 30, 110, FALSE, FALSE, 55, 85, 45, 5100, '부산 자갈치', '2025-01-19 20:30:00', NOW(), NULL);


