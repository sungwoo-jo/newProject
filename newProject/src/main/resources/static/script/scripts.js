// scripts.js

// 회원가입 페이지 S
document.getElementById("signup-form").addEventListener("submit", function (event) {
    // 간단한 클라이언트 측 유효성 검사
    let memId = document.getElementById("memId").value;
    let memPw = document.getElementById("memPw").value;
    let nickNm = document.getElementById("nickNm").value;
    let phone = document.getElementById("phone").value;
    let email = document.getElementById("email").value;

    // 비밀번호와 전화번호, 이메일의 기본 유효성 검사
    if (memPw.length < 6) {
        alert("비밀번호는 6자 이상이어야 합니다.");
        event.preventDefault();
        return;
    }

    const phonePattern = /^[0-9]{10,11}$/;
    if (!phonePattern.test(phone)) {
        alert("전화번호는 10자 또는 11자 숫자만 입력해주세요.");
        event.preventDefault();
        return;
    }

    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if (!emailPattern.test(email)) {
        alert("유효한 이메일 주소를 입력해주세요.");
        event.preventDefault();
        return;
    }

    // AJAX 요청을 보내는 함수 호출
    sendData();
});

// fetch API를 사용한 POST 요청 예시
function sendData() {
    const data = {
        // memId : document.getElementById("memId").value,
        // memPw : document.getElementById("memPw").value,
        // nickNm : document.getElementById("nickNm").value,
        // address1 : document.getElementById("address1").value,
        // address2 : document.getElementById("address2").value,
        // zipCode : document.getElementById("zipCode").value,
        // phone : document.getElementById("phone").value,
        // email : document.getElementById("email").value,
        // profilePath : document.getElementById("profilePath").value,
        memId : "memId",
        memPw : "memPw",
        nickNm : "nickNm",
        address1 : "address1",
        address2 : "address2",
        zipCode : "zipCode",
        phone : "phone",
        email : "email",
        profilePath : "profilePath"
    };

    fetch('/doJoin', {
        method: 'POST',  // 요청 방식
        headers: {
            'Content-Type': 'application/json'  // JSON 형식으로 요청 본문 설정
        },
        body: JSON.stringify(data)  // 데이터를 JSON 문자열로 변환하여 요청 본문에 담기
    })
        .then(response => response.json())  // 응답을 JSON 형식으로 변환
        .then(data => {
            console.log("성공:", data);  // 성공적으로 응답 받은 데이터 출력
        })
        .catch(error => {
            console.error("실패:", error);  // 오류 발생 시 에러 메시지 출력
        });
}

// 회원가입 페이지 E

// 로그인 페이지 S
document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();  // 폼 제출 기본 동작을 막습니다.

    const memId = document.getElementById('memId').value;
    const memPw = document.getElementById('memPw').value;

    const data = {
        memId: memId,
        memPw: memPw
    };

    fetch('/doLogin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("로그인 성공!");
                window.location.href = "/home";  // 로그인 성공 후 이동할 페이지
            } else {
                alert("아이디 또는 비밀번호가 잘못되었습니다.");
            }
        })
        .catch(error => {
            console.error("로그인 실패:", error);
            alert("로그인 중 오류가 발생했습니다.");
        });
});

document.getElementById('findId').addEventListener('click', function() {
    alert("아이디 찾기 기능이 아직 구현되지 않았습니다.");
});

document.getElementById('findPw').addEventListener('click', function() {
    alert("비밀번호 찾기 기능이 아직 구현되지 않았습니다.");
});
// 로그인 페이지 E