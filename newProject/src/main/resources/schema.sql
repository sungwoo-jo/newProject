use newProjectDB;

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
    profileImageName VARCHAR(200) DEFAULT NULL comment '프로필이미지파일명',
    deleteYn BOOLEAN DEFAULT '0' comment '탈퇴여부',
    regDt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' comment '생성일',
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
    regDt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- travel(여행게시판 테이블)
DROP TABLE IF EXISTS `travel` CASCADE;
CREATE TABLE `travel` (
    boardNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '게시글번호',
    memNo INT NOT NULL DEFAULT 0 comment '회원번호',
    writerNm VARCHAR(100) NOT NULL DEFAULT '' comment '작성자',
    subject VARCHAR(100) NOT NULL comment '제목',
    contents MEDIUMTEXT NOT NULL comment '내용',
    hashTag VARCHAR(20) DEFAULT NULL comment '해시태그',
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
    visitDt DATETIME DEFAULT NULL comment '방문일',
    regDt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- reply(댓글 테이블)
DROP TABLE IF EXISTS `reply` CASCADE;
CREATE TABLE `reply` (
    replyNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '댓글번호',
    boardId VARCHAR(20) NOT NULL DEFAULT '' comment '게시판종류',
    boardNo INT NOT NULL comment '게시글번호',
    memNo INT DEFAULT NULL comment '회원번호',
    contents TEXT NOT NULL comment '내용',
    depth INT DEFAULT 0 comment '뎁스(대댓글 표현)',
    seqNo INT DEFAULT 0 comment '댓글순서',
    parentNo INT DEFAULT NULL comment '부모댓글번호',
    deleteYn BOOLEAN DEFAULT '0' comment '삭제여부',
    likeCnt INT DEFAULT 0 comment '좋아요수',
    regDt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- post(쪽지 테이블)
DROP TABLE IF EXISTS `post` CASCADE;
CREATE TABLE `post` (
    postNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '쪽지번호',
    senderMemId VARCHAR(50) NOT NULL comment '발신회원아이디',
    senderMemNo INT NOT NULL comment '발신회원번호',
    receiverMemId VARCHAR(50) NOT NULL comment '발신회원아이디',
    receiverMemNo INT NOT NULL comment '수신회원번호',
    subject VARCHAR(100) NOT NULL DEFAULT '' comment '제목',
    contents MEDIUMTEXT NOT NULL DEFAULT '' comment '내용',
    readYn BOOLEAN DEFAULT '0' NOT NULL comment '읽음여부',
    deleteBySender BOOLEAN NOT NULL DEFAULT '0' comment '발신회원 삭제여부',
    deleteByReceiver BOOLEAN NOT NULL DEFAULT '0' comment '수신회원 삭제여부',
    sendDt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP comment '전송일',
    readDt DATETIME DEFAULT NULL comment '확인일',
    regDt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- chatInfo(채팅방정보 테이블)
DROP TABLE IF EXISTS `chatInfo` CASCADE;
CREATE TABLE `chatInfo` (
    chatNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '채팅방번호',
    chatNm VARCHAR(50) DEFAULT NULL comment '채팅방이름',
    entrant TEXT DEFAULT NULL comment '참여자리스트',
    regDt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- message(채팅메시지 테이블)
DROP TABLE IF EXISTS `message` CASCADE;
CREATE TABLE `message` (
    messageNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '메시지번호',
    chatNo INT NOT NULL comment '채팅방번호',
    senderMemNo INT NOT NULL comment '발신회원번호',
    contents TEXT NOT NULL comment '내용',
    sendDt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' comment '전송일',
    readYn BOOLEAN DEFAULT '0' NOT NULL comment '읽음여부',
    regDt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- config(설정데이터 테이블)
DROP TABLE IF EXISTS `config` CASCADE;
CREATE TABLE `config` (
    type VARCHAR(50) NOT NULL comment '설정타입',
    data JSON DEFAULT NULL comment '설정데이터',
    regDt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- reservation(예약 정보 테이블)
DROP TABLE IF EXISTS `reservation` CASCADE;
CREATE TABLE `reservation` (
    rsvNo INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '예약번호',
    memNo INT NOT NULL DEFAULT 0 comment '회원번호',
    placeId INT NOT NULL DEFAULT 0 comment '장소번호',
    rsvDt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP comment '예약일',
    rsvStatus VARCHAR(10) NOT NULL DEFAULT 'PENDING' comment '예약상태',
    reqMsg VARCHAR(100) DEFAULT NULL comment '요청사항',
    deleteYn BOOLEAN DEFAULT '0' comment '삭제여부',
    regDt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- uploadFile(첨부파일 테이블)
DROP TABLE IF EXISTS `uploadFile` CASCADE;
CREATE TABLE `uploadFile` (
    sno INT AUTO_INCREMENT PRIMARY KEY NOT NULL comment '첨부파일번호',
    uploadFileName VARCHAR(200) DEFAULT NULL comment '업로드 당시 파일명',
    storedFileName VARCHAR(200) DEFAULT NULL comment '저장된 파일명',
    regDt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' comment '생성일',
    modDt DATETIME DEFAULT NULL comment '수정일'
);

-- 더미 데이터(회원)
INSERT INTO member(memId, memPw, memNm, nickNm, comm, address1, address2, zipCode, phone, email, profileImageName, regDt) 
VALUES 
('test1', '744ea9ec6fa0a83e9764b4e323d5be6b55a5accfc7fe4c08eab6a8de1fca4855', '테스트1', 'test1', '코멘트1', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '11111', '01011111111', 'sungwoo9671@naver.com', '1ccc9e61-01d4-446b-bb8e-e91f256d4359.png', NOW()),
('test2', '60303ae22b998861bce3b28f33eec1be758a213c86c93c076dbe9f558c11c752', '테스트2', 'test2', '코멘트2', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '22222', '01022222222', 'test2@naver.com', NULL, NOW()),
('test3', 'test3', '테스트3', 'test3', '코멘트3', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '33333', '01033333333', 'test3@naver.com', NULL, NOW()),
('test4', 'test4', '테스트4', 'test4', '코멘트4', '서울특별시 강서구', '가로공원로 88길 16-4 2층', '44444', '01044444444', 'test4@naver.com', NULL, NOW());

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

-- 더미 데이터(댓글)
INSERT INTO reply(boardId, boardNo, memNo, contents, depth, seqNo, parentNo, regDt)
VALUES
('travel', 1, 101, '정말 멋진 여행지네요! 꼭 가보고 싶어요.', 0, 1, NULL, NOW()), 
('travel', 1, 102, '저도 예전에 갔었는데, 정말 좋았어요.', 1, 2, 1, NOW()), 
('travel', 1, 103, '추천해주셔서 감사합니다. 여행 준비 잘 할게요.', 1, 3, 1, NOW()), 
('travel', 2, 104, '가보고 싶은 곳이지만, 너무 멀어서 고민이에요.', 0, 4, NULL, NOW()), 
('travel', 2, 105, '친구랑 가려고 계획 중이에요. 가격이 비쌀까요?', 1, 5, 4, NOW()), 
('travel', 2, 106, '가격은 좀 비쌀 수 있지만, 그만한 가치가 있어요.', 2, 6, 5, NOW()), 
('travel', 3, 107, '저는 여행 팁을 구체적으로 알고 싶어요.', 0, 7, NULL, NOW()), 
('travel', 3, 108, '여행 일정표를 추천해 주세요.', 1, 8, 7, NOW()), 
('travel', 3, 109, '여기 주변에는 어떤 음식점이 좋을까요?', 0, 9, NULL, NOW()), 
('travel', 4, 110, '저는 그 지역에서 유명한 음식이 궁금해요.', 1, 10, 9, NOW());

-- 더미 데이터(쪽지)
INSERT INTO post(senderMemId, senderMemNo, receiverMemId, receiverMemNo, subject, contents, readYn, deleteBySender, deleteByReceiver, sendDt, readDt, regDt, modDt) VALUES
('test15', 15, 'test1', 1, '제목 1', '내용 1', TRUE, FALSE, FALSE, '2025-01-22 15:34:00', '2025-01-22 16:00:00', '2025-01-01 10:00:00', '2025-01-01 10:00:00'),
('test20', 20, 'test1', 1, '제목 2', '내용 2', FALSE, FALSE, FALSE, '2025-01-21 14:10:00', NULL, '2025-01-02 11:00:00', '2025-01-02 11:00:00'),
('test5', 5, 'test1', 1, '제목 3', '내용 3', TRUE, FALSE, FALSE, '2025-01-20 13:25:00', '2025-01-20 13:45:00', '2025-01-03 12:00:00', '2025-01-03 12:00:00'),
('test30', 30, 'test1', 1, '제목 4', '내용 4', FALSE, FALSE, FALSE, '2025-01-19 12:20:00', NULL, '2025-01-04 09:30:00', '2025-01-04 09:30:00'),
('test10', 10, 'test1', 1, '제목 5', '내용 5', TRUE, FALSE, FALSE, '2025-01-18 11:50:00', '2025-01-18 12:30:00', '2025-01-05 14:00:00', '2025-01-05 14:00:00'),
('test12', 12, 'test2', 2, '제목 6', '내용 6', FALSE, FALSE, FALSE, '2025-01-17 10:40:00', NULL, '2025-01-06 15:30:00', '2025-01-06 15:30:00'),
('test2', 2, 'test2', 2, '제목 7', '내용 7', TRUE, FALSE, FALSE, '2025-01-16 09:50:00', '2025-01-16 10:40:00', '2025-01-07 16:10:00', '2025-01-07 16:10:00'),
('test25', 25, 'test2', 2, '제목 8', '내용 8', FALSE, FALSE, FALSE, '2025-01-15 08:30:00', NULL, '2025-01-08 17:20:00', '2025-01-08 17:20:00'),
('test18', 18, 'test2', 2, '제목 9', '내용 9', TRUE, FALSE, FALSE, '2025-01-14 07:40:00', '2025-01-14 08:10:00', '2025-01-09 18:15:00', '2025-01-09 18:15:00'),
('test9', 9, 'test2', 2, '제목 10', '내용 10', FALSE, FALSE, FALSE, '2025-01-13 06:50:00', NULL, '2025-01-10 19:40:00', '2025-01-10 19:40:00'),
('test14', 14, 'test3', 3, '제목 11', '내용 11', TRUE, FALSE, FALSE, '2025-01-12 05:45:00', '2025-01-12 06:30:00', '2025-01-11 20:00:00', '2025-01-11 20:00:00'),
('test7', 7, 'test3', 3, '제목 12', '내용 12', FALSE, FALSE, FALSE, '2025-01-11 04:30:00', NULL, '2025-01-12 08:20:00', '2025-01-12 08:20:00'),
('test17', 17, 'test3', 3, '제목 13', '내용 13', TRUE, FALSE, FALSE, '2025-01-10 03:15:00', '2025-01-10 04:05:00', '2025-01-13 09:10:00', '2025-01-13 09:10:00'),
('test22', 22, 'test3', 3, '제목 14', '내용 14', FALSE, FALSE, FALSE, '2025-01-09 02:00:00', NULL, '2025-01-14 10:30:00', '2025-01-14 10:30:00'),
('test16', 16, 'test3', 3, '제목 15', '내용 15', TRUE, FALSE, FALSE, '2025-01-08 01:20:00', '2025-01-08 02:00:00', '2025-01-15 11:40:00', '2025-01-15 11:40:00'),
('test13', 13, 'test4', 4, '제목 16', '내용 16', FALSE, FALSE, FALSE, '2025-01-07 00:45:00', NULL, '2025-01-16 12:00:00', '2025-01-16 12:00:00'),
('test6', 6, 'test4', 4, '제목 17', '내용 17', TRUE, FALSE, FALSE, '2025-01-06 23:30:00', '2025-01-06 23:50:00', '2025-01-17 13:20:00', '2025-01-17 13:20:00'),
('test3', 3, 'test4', 4, '제목 18', '내용 18', FALSE, FALSE, FALSE, '2025-01-05 22:10:00', NULL, '2025-01-18 14:50:00', '2025-01-18 14:50:00'),
('test11', 11, 'test4', 4, '제목 19', '내용 19', TRUE, FALSE, FALSE, '2025-01-04 21:00:00', '2025-01-04 21:45:00', '2025-01-19 16:10:00', '2025-01-19 16:10:00'),
('test8', 8, 'test4', 4, '제목 20', '내용 20', FALSE, FALSE, FALSE, '2025-01-03 19:55:00', NULL, '2025-01-20 17:30:00', '2025-01-20 17:30:00');

-- 더미 데이터(예약 정보)
INSERT INTO reservation (memNo, placeId, rsvDt, rsvStatus, reqMsg, deleteYn, regDt, modDt) VALUES
(1, 101, '2025-02-07 09:00:00', 'PENDING', '창가 자리로 예약 부탁드립니다.', 0, '2025-02-07 09:00:00', NULL),
(1, 102, '2025-02-07 14:30:00', 'CONFIRMED', '조용한 분위기의 자리 요청합니다.', 0, '2025-02-07 09:15:00', '2025-02-07 09:30:00'),
(1, 103, '2025-02-07 18:00:00', 'CANCELED', NULL, 0, '2025-02-07 09:45:00', '2025-02-07 10:00:00'),
(1, 104, '2025-02-07 20:00:00', 'PENDING', 'VIP 좌석으로 예약해 주세요.', 0, '2025-02-07 10:15:00', NULL),
(1, 105, '2025-02-08 12:00:00', 'CONFIRMED', '특별 메뉴 부탁드려요.', 0, '2025-02-07 10:45:00', '2025-02-07 11:00:00'),
(1, 106, '2025-02-08 10:30:00', 'PENDING', '창문 가까운 자리 부탁드려요.', 0, '2025-02-08 10:00:00', NULL),
(1, 107, '2025-02-08 15:00:00', 'CONFIRMED', '기념일이라 특별한 대접을 부탁드립니다.', 0, '2025-02-08 10:30:00', '2025-02-08 11:00:00'),
(1, 108, '2025-02-08 17:30:00', 'CANCELED', '예약 취소 부탁드립니다.', 0, '2025-02-08 11:15:00', '2025-02-08 11:30:00'),
(1, 109, '2025-02-08 19:00:00', 'PENDING', '혼자 예약인데 편안한 자리를 원합니다.', 0, '2025-02-08 12:00:00', NULL),
(1, 110, '2025-02-09 08:00:00', 'CONFIRMED', '어린이 메뉴 제공 부탁드립니다.', 0, '2025-02-08 13:00:00', '2025-02-08 13:15:00');