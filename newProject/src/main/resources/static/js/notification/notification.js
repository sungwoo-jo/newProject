window.addEventListener("DOMContentLoaded", () => {
    const eventSource = new EventSource("/notifications/subscribe");

    eventSource.addEventListener("message", function (event) {
        console.log("서버로부터 알림: " + event.data);
    });

    eventSource.onerror = function (err) {
        console.error("SSE 연결 오류:", err);
        eventSource.close();
    };

    eventSource.addEventListener("sse", function (event) {
        console.log("더미 데이터 수신 완료");
    });

    eventSource.addEventListener("notify", function (event) {
        console.log("🔔 알림: " + event.data);
        document.querySelector(".badge-dot").style.display = "block";
    });
    eventSource.addEventListener("FRIEND_REQ", function (event) {
        console.log("🔔 친구 요청 알림: " + event.data);
        document.querySelector(".badge-dot").style.display = "block";
    });
    eventSource.addEventListener("FRIEND_ACC", function (event) {
        console.log("🔔 친구 수락 알림: " + event.data);
        document.querySelector(".badge-dot").style.display = "block";
    });
    eventSource.addEventListener("FRIEND_REJ", function (event) {
        console.log("🔔 친구 거절 알림: " + event.data);
        document.querySelector(".badge-dot").style.display = "block";
    });
    eventSource.addEventListener("POST_SEND", function (event) {
        console.log("🔔 쪽지 수신 알림: " + event.data);
        document.querySelector(".badge-dot").style.display = "block";
    });
    eventSource.addEventListener("POST_READ", function (event) {
        console.log("🔔 쪽지 읽음 알림: " + event.data);
        document.querySelector(".badge-dot").style.display = "block";
    });
    eventSource.addEventListener("FOLLOW_ADD", function (event) {
        console.log("🔔 팔로우 알림: " + event.data);
        document.querySelector(".badge-dot").style.display = "block";
    });
    eventSource.addEventListener("REPLY", function (event) {
        console.log("🔔 댓글 알림: " + event.data);
        document.querySelector(".badge-dot").style.display = "block";
    });
    eventSource.addEventListener("BOARD_LIKE", function (event) {
        console.log("🔔 좋아요 알림: " + event.data);
        document.querySelector(".badge-dot").style.display = "block";
    });
    eventSource.addEventListener("LOGIN", function (event) {
        console.log("🔔 로그인 알림: " + event.data);
        document.querySelector(".badge-dot").style.display = "block";
    });
});