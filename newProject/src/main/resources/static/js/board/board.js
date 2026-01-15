document.body.addEventListener('click', function (e) {
    const likeBtn = e.target.closest('.btn-like');
    if (likeBtn) {
        const boardNo = likeBtn.dataset.boardNo;
        const boardId = likeBtn.dataset.boardId;
        handleLike(likeBtn, boardNo, boardId);
        return;
    }

    const deleteBtn = e.target.closest('.btn-delete');
    if (deleteBtn) {
        handleDelete(deleteBtn);
        return;
    }
});

// 좋아요 버튼
function handleLike(likeBtn, boardNo, boardId) {
        const data = {
            boardNo: boardNo,
            boardId: boardId
        };

        fetch('../doLike', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.text())
            .then(data => {
                if (data === "success") {
                    alert('좋아요!');
                } else {
                    alert('좋아요 실패!');
                }
            })
            .catch(error => console.error("Error:", error));
}

function handleDelete(btn) {
    console.log('삭제');
}

// const sessionMemNo = document.getElementById('sessionMemNo').value; // 로그인 회원
// 댓글 조회 함수
function getReply() {
    const boardId = document.body.dataset.boardId;
    const boardNo = document.body.dataset.boardNo;

    fetch(`/reply/${boardId}/getReply/${boardNo}`)
        .then(response => response.json())
        .then(replys => {
            const replyDiv = document.getElementById('replys');
            const replyCnt = document.getElementById('count');
            replyDiv.innerHTML = ''; // 기존 댓글 초기화
            replyCnt.innerHTML = ''; // 기존 갯수 초기화
            const replysCnt = replys.length;
            const count = document.createElement('count');
            count.innerHTML = `<h3 id="count">댓글 ${replysCnt}개</h3>`;
            replyCnt.appendChild(count);
            replys.forEach(reply => {
                const replyElement = document.createElement('div');
                replyElement.style.marginBottom = '20px';
                replyElement.style.marginLeft = (reply.depth * 50) + 'px';
                replyElement.innerHTML = `
                            <p id="reply-${reply.replyNo}">${reply.contents}</p>
                            `;
                if (reply.memNo == sessionMemNo) {
                    replyElement.innerHTML += `
                            <button id="deleteReplyBtn-${reply.replyNo}" name="deleteReplyBtn" style="display:inline-block" onclick="deleteReply(${reply.replyNo})">삭제</button>
                            <button id="updateReplyBtn-${reply.replyNo}" name="updateReplyBtn" style="display:inline-block" onclick="updateReply(${reply.replyNo})">수정</button>
                        `;
                }
                if (reply.depth == 0) {
                    replyElement.innerHTML += `
                            <button id="writeNestedReply-${reply.replyNo}" name="writeNestedReply" style="display:inline-block" onclick="writeNestedReply(${reply.replyNo})">답글</button>
                            `;
                }
                replyElement.innerHTML += `
                            <button id="saveReplyBtn-${reply.replyNo}" name="saveReplyBtn" style="display:none;" onclick="saveReply(${reply.replyNo})">저장</button>
                            <button id="cancelReplyBtn-${reply.replyNo}" name="cancelReplyBtn" style="display:none;" onclick="cancelReply(${reply.replyNo})">취소</button>
                            <textarea id="reply-textarea-${reply.replyNo}" style="display:none;">${reply.contents}</textarea>

                            <!-- 답글 요소 -->
                            <textarea id="nestedReplyTextArea-${reply.replyNo}" style="display:none;"></textarea>
                            <button id="saveNestedReply-${reply.replyNo}" name="saveNestedReplyBtn" style="display:none;" onclick="saveNestedReply(${reply.replyNo})">답글저장</button>
                            <button id="cancelNestedReply-${reply.replyNo}" name="cancelNestedReply" style="display:none;" onclick="cancelNestedReply(${reply.replyNo})">취소</button>
                        `;
                replyDiv.appendChild(replyElement);
            });
        })
        .catch(error => console.error('Error loading replys:', error));
}


// 대댓글 저장 함수 S
function saveNestedReply(replyNo) {
    const contents = document.getElementById('nestedReplyTextArea-' + replyNo).value;
    if (!contents) {
        alert("답글 내용을 입력하세요.");
        return;
    }

    const data = {
        contents: contents,
        parentNo: replyNo
    };

    fetch(`/reply/${boardId}/doWrite/${boardNo}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(() => {
            getReply();  // 대댓글 목록 새로고침
            document.getElementById('nestedReplyTextArea').value = '';  // 텍스트박스 비우기
        })
        .catch(error => console.error('Error adding reply:', error));
}

document.body.addEventListener('click', function (e) {
    const writeBtn = e.target.closest('.do-write');
    const replyArea = e.target.closest('.write-reply-area');
    const contents = replyArea.querySelector('.replyContents').value;

    if (writeBtn) {
        console.log(contents);

        doWrite(contents, boardNo, boardId);
    }
    // 댓글 추가 함수
    function doWrite(contents, boardNo, boardId) {
        if (!contents) {
            alert("댓글을 입력하세요.");
            return;
        }

        const data = {
            contents: contents
        };

        console.log('contents: ' + data.contents);

        fetch(`/reply/${boardId}/doWrite/${boardNo}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        })
            .then(() => {
                getReply();  // 댓글 목록 새로고침
                document.getElementById('replyContents').value = '';  // 텍스트박스 비우기
            })
            .catch(error => console.error('Error adding reply:', error));
}});

// 댓글 삭제 함수
function deleteReply(replyNo) {
    fetch(`/reply/${boardId}/doDelete/${replyNo}`, {
        method: 'DELETE',
    })
        .then(() => {
            getReply();  // 댓글 목록 새로고침
        })
        .catch(error => console.error('Error deleting reply:', error));
}

// 답글 버튼 클릭 시
function writeNestedReply(replyNo) {
    // const replyText = document.getElementById('reply-' + replyNo);
    const nestedReplyTextArea = document.getElementById('nestedReplyTextArea-' + replyNo);
    const saveNestedReply = document.getElementById('saveNestedReply-' + replyNo);
    const cancelNestedReply = document.getElementById('cancelNestedReply-' + replyNo);
    const writeNestedReply = document.getElementById('writeNestedReply-' + replyNo);

    // textarea를 보여줌
    nestedReplyTextArea.style.display = 'block';
    // 답글 버튼은 숨기고 답글저장, 취소 버튼을 보여줌
    writeNestedReply.style.display = 'none';
    saveNestedReply.style.display = 'block';
    cancelNestedReply.style.display = 'block';
}

// 댓글 수정 함수
function updateReply(replyNo) {
    const replyText = document.getElementById('reply-' + replyNo);
    const replyTextArea = document.getElementById('reply-textarea-' + replyNo);
    const updateReplyBtn = document.getElementById('updateReplyBtn-' + replyNo);
    const deleteReplyBtn = document.getElementById('deleteReplyBtn-' + replyNo);
    const saveReplyBtn = document.getElementById('saveReplyBtn-' + replyNo);
    const cancelReplyBtn = document.getElementById('cancelReplyBtn-' + replyNo);

    // 수정 버튼 클릭 시
    // 댓글 텍스트를 숨기고, textarea를 보여줌
    replyText.style.display = 'none';
    replyTextArea.style.display = 'block';
    // 삭제, 수정 버튼을 숨기고 저장, 취소 버튼을 보여줌
    updateReplyBtn.style.display = 'none';
    deleteReplyBtn.style.display = 'none';
    saveReplyBtn.style.display = 'inline-block';
    cancelReplyBtn.style.display = 'inline-block';
}

// 댓글 취소 버튼 클릭 시
function cancelReply(replyNo) {
    const replyText = document.getElementById('reply-' + replyNo);
    const replyTextArea = document.getElementById('reply-textarea-' + replyNo);
    const updateReplyBtn = document.getElementById('updateReplyBtn-' + replyNo);
    const deleteReplyBtn = document.getElementById('deleteReplyBtn-' + replyNo);
    const saveReplyBtn = document.getElementById('saveReplyBtn-' + replyNo);
    const cancelReplyBtn = document.getElementById('cancelReplyBtn-' + replyNo);

    // 취소 버튼 클릭 시
    // textarea를 숨기고 댓글 텍스트를 보여줌
    replyTextArea.style.display = 'none';
    replyText.style.display = 'block';
    // 저장, 취소 버튼을 숨기고 삭제, 수정 버튼을 보여줌
    updateReplyBtn.style.display = 'inline-block';
    deleteReplyBtn.style.display = 'inline-block';
    saveReplyBtn.style.display = 'none';
    cancelReplyBtn.style.display = 'none';
}

// 답글 취소 버튼 클릭 시
function cancelNestedReply(replyNo) {
    const nestedReplyTextArea = document.getElementById('nestedReplyTextArea-' + replyNo);
    const saveNestedReply = document.getElementById('saveNestedReply-' + replyNo);
    const cancelNestedReply = document.getElementById('cancelNestedReply-' + replyNo);
    const writeNestedReply = document.getElementById('writeNestedReply-' + replyNo);

    // textarea를 보여줌
    nestedReplyTextArea.style.display = 'none';
    // 답글 버튼은 숨기고 답글저장, 취소 버튼을 보여줌
    writeNestedReply.style.display = 'inline-block';
    saveNestedReply.style.display = 'none';
    cancelNestedReply.style.display = 'none';
}

// 댓글 저장 함수 S
function saveReply(replyNo) {

    const contents = document.getElementById('reply-textarea-' + replyNo).value;

    fetch(`/reply/${boardId}/doUpdate/${replyNo}`, {
        method: 'PATCH',
        body: contents
    })
        .then(() => {
            getReply();  // 댓글 목록 새로고침
        })
        .catch(error => console.error('Error deleting reply:', error));
}

// 페이지 로드 시 댓글 목록 불러오기
window.onload = getReply;

// 게시글 삭제 S
document.getElementById('doDelete').addEventListener('click', function () {
    const boardId = document.getElementById('boardId').value;
    const boardNo = document.getElementById('boardNo').value;
    const data = {
        boardId: boardId,
        boardNo: boardNo
    };
    fetch('../doDelete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // 요청 헤더에 JSON 타입 지정
        },
        body: JSON.stringify(data) // 자바스크립트 객체를 JSON 문자열로 변환하여 전송
    })
        .then(response => response.text())
        .then(data => {
            if (data === "success") {
                console.log('게시글 삭제 성공');
                window.location.href = "/board/" + boardId + "/list";
            } else {
                alert('게시글 삭제 실패');
            }
        })
        .catch(error => console.error("Error:", error));
});
// 게시글 삭제 E

// 팔로우하기 S
function follow() {
    if (confirm(document.getElementById('writerNm').value + '님을 팔로우하시겠습니까?')) {
        const followButton = document.getElementById('follow');
        const currentText = followButton.innerText;

        const boardId = document.getElementById('boardId').value;
        const boardNo = document.getElementById('boardNo').value;
        const memNo = document.getElementById('memNo').value;
        const data = {
            boardId: boardId,
            boardNo: boardNo,
            memNo: memNo
        };
        fetch('../../../member/follow', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // 요청 헤더에 JSON 타입 지정
            },
            body: JSON.stringify(data) // 자바스크립트 객체를 JSON 문자열로 변환하여 전송
        })
            .then(response => response.text())
            .then(data => {
                if (data === "success") {
                    alert('팔로우 하였습니다.');
                    if (currentText.includes('팔로우하기')) {
                        followButton.innerText = followButton.innerText.replace('팔로우하기', '팔로우 중');
                        followButton.id = 'cancelFollow';
                        followButton.onclick = cancelFollow;
                    }
                } else {
                    alert('팔로우 실패하였습니다.');
                }
            })
            .catch(error => console.error("Error:", error));
    } else {
        return false;
    }
}

// 팔로우하기 E

// 팔로우취소하기 S
function cancelFollow() {
    if (confirm(document.getElementById('writerNm').value + '님 팔로우를 취소하시겠습니까?')) {
        const followButton = document.getElementById('cancelFollow');
        const currentText = followButton.innerText;

        const boardId = document.getElementById('boardId').value;
        const boardNo = document.getElementById('boardNo').value;
        const memNo = document.getElementById('memNo').value;
        const data = {
            boardId: boardId,
            boardNo: boardNo,
            memNo: memNo
        };
        fetch('../../../member/cancelFollow', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json' // 요청 헤더에 JSON 타입 지정
            },
            body: JSON.stringify(data) // 자바스크립트 객체를 JSON 문자열로 변환하여 전송
        })
            .then(response => response.text())
            .then(data => {
                if (data === "success") {
                    alert('팔로우 취소 하였습니다.');
                    if (currentText.includes('팔로우 중')) {
                        followButton.innerText = followButton.innerText.replace('팔로우 중', '팔로우하기');
                        followButton.id = 'follow';
                        followButton.onclick = follow;
                    }
                } else {
                    alert('팔로우 취소에 실패하였습니다.');
                }
            })
            .catch(error => console.error("Error:", error));
    } else {
        return false;
    }
}

// 팔로우취소하기 E

// 친구 신청하기 S
function createRequest() {
    if (confirm(document.getElementById('writerNm').value + '님에게 친구 요청을 보내시겠습니까?')) {
        const createRequestButton = document.getElementById('createRequest');
        const currentText = createRequestButton.innerText;

        const fromMemNo = document.getElementById('memNo').value;
        const data = {
            fromMemNo: fromMemNo
        };
        fetch('../../../friend/createRequest', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // 요청 헤더에 JSON 타입 지정
            },
            body: JSON.stringify(data) // 자바스크립트 객체를 JSON 문자열로 변환하여 전송
        })
            .then(response => response.text())
            .then(data => {
                if (data === "success") {
                    alert('친구 요청을 전송했습니다.');
                    if (currentText.includes('친구신청')) {
                        createRequestButton.innerText = createRequestButton.innerText.replace('친구신청', '친구요청취소');
                        createRequestButton.id = 'cancelFollow';
                        createRequestButton.onclick = cancelFollow;
                    }
                } else {
                    alert('친구 요청에 실패하였습니다.');
                }
            })
            .catch(error => console.error("Error:", error));
    } else {
        return false;
    }
}

// 친구 신청하기 E
