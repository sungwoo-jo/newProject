// scripts.js

// 회원가입 페이지 S
if (document.getElementById("signup-form")) {
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
}

// fetch API를 사용한 POST 요청 예시
// function sendData() {
//     // memberDto 객체 생성
//     const memberDto = {
//         memId: document.getElementById("memId").value,
//         memPw: document.getElementById("memPw").value,
//         memNm: document.getElementById("memNm").value,
//         nickNm: document.getElementById("nickNm").value,
//         address1: document.getElementById("address1").value,
//         address2: document.getElementById("address2").value,
//         zipCode: document.getElementById("zipCode").value,
//         phone: document.getElementById("phone").value,
//         email: document.getElementById("email").value
//     };
//
//     alert(memberDto);
//
//     // FormData 객체 생성
//     const formData = new FormData();
//
//     // memberDto 객체를 JSON 문자열로 변환하여 FormData에 추가
//     formData.append("memberDto", JSON.stringify(memberDto)); // 이거 되는 코드
//
//
//     // 파일 추가
//     const profileImage = document.getElementById("profileImage").files[0];
//     if (profileImage) {
//         formData.append("profileImage", profileImage);
//     }
//
//
//     // fetch API를 사용하여 서버에 데이터 전송
//     fetch('./doJoin', {
//         method: 'POST',  // 요청 방식
//         body: formData  // FormData를 요청 본문에 담기
//     })
//         .then(response => response.json())  // 응답을 JSON 형식으로 변환
//         .then(data => {
//             console.log("성공:", data);  // 성공적으로 응답 받은 데이터 출력
//         })
//         .catch(error => {
//             console.error("실패:", error);  // 오류 발생 시 에러 메시지 출력
//         });
// }
if (document.getElementById('joinForm')) {
    document.getElementById('joinForm').addEventListener('submit', function (event) {
        event.preventDefault();  // 폼 제출 기본 동작을 막습니다.

        const form = document.getElementById('joinForm');
        form.enctype = "multipart/form-data";
        form.method = 'POST';
        form.action = './doJoin';
        form.submit();
    })
}
// 회원가입 페이지 E

// 로그인 페이지 S
if (document.getElementById("loginForm")) {
    document.getElementById('loginForm').addEventListener('submit', function (event) {
        event.preventDefault();  // 폼 제출 기본 동작을 막습니다.

        const memId = document.getElementById('memId').value;
        const memPw = document.getElementById('memPw').value;

        const data = {
            memId: memId,
            memPw: memPw
        };

        // 서버에 요청을 보냄
        fetch("./doLogin", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"  // 서버에게 JSON 형식으로 데이터를 보낸다고 알림
            },
            body: JSON.stringify(data)  // 폼 데이터를 JSON 형태로 서버에 전송
        })
            .then(response => response.text())
            .then(data => {  // 서버에서 반환한 HTML 코드 받기
                if (data === "success") {  // 상태 코드가 200이면 성공
                    console.log('로그인 성공');
                    window.location.href="/";
                    // 성공 처리 로직
                } else {
                    alert('로그인에 실패했습니다. 정보를 다시 확인해주세요.');
                    // 실패 처리 로직
                }
            })
            .catch(error => console.error("Error:", error));
    });
}

// 로그인 페이지 E

// 비밀번호 찾기 페이지 S
if (document.getElementById("findPwForm")) {
    document.getElementById("findPwForm").onsubmit = function (event) {
        event.preventDefault(); // 기본 폼 제출 방지

        // 서버에 요청을 보냄
        // 폼 데이터 수집
        const memId = document.getElementById("memId").value;
        const memNm = document.getElementById("memNm").value;
        const email = document.getElementById("email").value;

        // 폼 데이터를 JSON 형식으로 변환
        const data = {
            memId: memId,
            memNm: memNm,
            email: email
        };

        // 서버에 요청을 보냄
        fetch("./doFindPw", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"  // 서버에게 JSON 형식으로 데이터를 보낸다고 알림
            },
            body: JSON.stringify(data)  // 폼 데이터를 JSON 형태로 서버에 전송
        })
            .then(response => response.text())
            .then(data => {  // 서버에서 반환한 HTML 코드 받기
                if (data === "sendMailSuccess") {
                    alert('입력하신 이메일로 임시 비밀번호가 전송되었습니다.\n새로운 비밀번호로 로그인 해주세요.');
                    window.location.href = "./login";
                } else {
                    alert('일치하는 정보가 없습니다.\n입력하신 정보를 다시 확인해주세요.');
                }
            })
            .catch(error => console.error("Error:", error));
    }
}

// 비밀번호 찾기 페이지 E

// 비밀번호 재설정 페이지 S
if (document.getElementById("resetPwForm")) {
    document.getElementById("resetPwForm").onsubmit = function (event) {
        event.preventDefault(); // 기본 폼 제출 방지

        const newPw = document.getElementById("newPw").value;
        const confirmPw = document.getElementById("confirmPw").value;

        // 비밀번호 일치 여부 확인
        if (newPw !== confirmPw) {
            document.getElementById("errorMessage").style.display = "block"; // 비밀번호 불일치 메시지 표시
            return;
        }

        // 비밀번호 일치하면 요청 보내기
        const memNo = document.getElementById("memNo").value;
        const data = {
            memNo: memNo,
            newPw: newPw
        };

        // 요청 보내기
        fetch("/doResetPw", {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert("비밀번호가 성공적으로 변경되었습니다.");
                    window.location.href = "/login"; // 로그인 페이지로 리디렉션
                } else {
                    alert("비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
                }
            })
            .catch(error => console.error("Error:", error));
    };
}
// 비밀번호 재설정 페이지 E

// 비밀번호 재설정 완료 페이지 S
function redirectLogin() {
    window.location.href = "./login"; // 로그인 페이지로 리디렉션
}
// 비밀번호 재설정 완료 페이지 E

// 회원 정보 수정 페이지 S
if (document.getElementById("updateForm")) {
    document.getElementById('updateForm').addEventListener('submit', function (event) {
        event.preventDefault();  // 폼 제출 기본 동작을 막습니다.

        const form = document.getElementById('updateForm');
        form.method = 'PATCH';
        form.action = './doUpdate';
        form.submit();
    });
}
// 회원 정보 수정 페이지 E

// 회원 탈퇴 페이지 S
if (document.getElementById('deleteForm')) {
    document.getElementById('deleteForm').addEventListener('submit', function (event) {
        event.preventDefault();  // 폼 제출 기본 동작을 막습니다.

        const form = document.getElementById('deleteForm');

        const memNo = document.getElementById('memNo').value;

        const memberDto = {
            memNo: memNo,
            // 추가적인 값들이 필요하다면 여기에 추가
        };

        fetch("./doDelete", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(memberDto)
        })
            .then(response => {
                if (response.status === 200) {  // 상태 코드가 200이면 성공
                    console.log('회원 탈퇴 성공');
                    window.location.href="/member/login";
                    // 성공 처리 로직
                } else {
                    console.error('실패: 상태 코드 ' + response.status);
                    // 실패 처리 로직
                }
            })
            .catch(error => console.error("Error:", error));
    });
}

// 회원 탈퇴 페이지 E