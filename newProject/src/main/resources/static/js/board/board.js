document.body?.addEventListener('click', function (e) {
    const likeBtn = e.target.closest('.btn-like');
    if (likeBtn) {
        const boardNo = likeBtn.dataset.boardNo;
        const boardId = likeBtn.dataset.boardId;
        handleLike(likeBtn, boardNo, boardId);
        return;
    }

    const deleteBtn = e.target.closest('.btn-delete');
    if (deleteBtn) {
        const boardNo = deleteBtn.dataset.boardNo;
        const boardId = deleteBtn.dataset.boardId;
        handleDelete(deleteBtn, boardNo, boardId);
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
            .then(response => {
                if (response.ok) {
                    return response.text(); // 좋아요 갯수를 텍스트로 반환
                }

                // 2. 실패(400, 500 등)일 경우 에러 메시지를 읽어서 throw
                return response.json().then(err => {
                    throw new Error(err.message);
                });
            })

            .then(likeCount => {
                likeBtn.getElementsByTagName('span')[1].innerText = likeCount;
            })

            .catch(error => {
                alert(error.message);
            });
}

function handleDelete(btn, boardNo, boardId) {
    if (confirm("게시글을 삭제하시겠습니까?")) {
        const data = {
            boardNo: boardNo,
            boardId: boardId
        };

        fetch('../doDelete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (response.ok) {
                    return response.text(); // 좋아요 갯수를 텍스트로 반환
                }

                // 2. 실패(400, 500 등)일 경우 에러 메시지를 읽어서 throw
                return response.json().then(err => {
                    throw new Error(err.message);
                });
            })

            .then()

        alert("게시글 삭제가 완료되었습니다.");
        if (document.referrer) {
            window.location.href = document.referrer;
        } else {
            // 이전 페이지 정보가 없는 경우 메인으로 이동
            window.location.href = '/';
        }
    }
}

// 댓글 조회 함수
function getReply() {
    const boardId = document.body.dataset.boardId;
    const boardNo = document.body.dataset.boardNo;
    const url = `/reply/${boardId}/getReply/${boardNo}`;

    fetch(url)
        .then(response => response.json())
        .then(replys => {
            console.log(replys);
            const replysDiv = document.querySelector('.replys');
            const replyCountView = document.querySelector('.reply-count-view');
            replysDiv.innerHTML = ''; // 기존 댓글 초기화
            replyCountView.innerHTML = ''; // 기존 갯수 초기화
            const replysCnt = replys.length;
            const countDiv = document.createElement('div');
            countDiv.className = 'reply-count';
            countDiv.textContent = '댓글 ' + replysCnt + '개';
            replyCountView.appendChild(countDiv);
            if (replysCnt > 0) {
                replys.forEach(reply => {

                    const replyBody = document.createElement('div');
                    const depth = `${reply.depth}`;
                    replyBody.className = 'reply-body';
                    replyBody.dataset.replyNo = `${reply.replyNo}`;
                    replyBody.style.paddingLeft = 2 * depth + 'rem';

                    const authorProfileArea = document.createElement('div');
                    authorProfileArea.className = 'author-profile-area';

                    // 댓글 작성자 프로필 이미지 영역
                    const authorProfileImage = document.createElement('img');
                    authorProfileImage.className = 'profile-image';
                    authorProfileImage.src = `http://121.171.123.7:62334/${reply.profileImageName}`;

                    // 댓글 작성자 닉네임 영역
                    const authorNickName = document.createElement('div');
                    authorNickName.className = 'author-nickname';
                    authorNickName.textContent = `${reply.memNm}`;

                    // 댓글 내용 영역
                    const replyContents = document.createElement('p');
                    replyContents.className = 'reply-contents';
                    // replyContents.dataset.replyNo = `${reply.replyNo}`;
                    replyContents.textContent = `${reply.contents}`;

                    authorProfileArea.appendChild(authorProfileImage);
                    authorProfileArea.appendChild(authorNickName);

                    replyBody.appendChild(authorProfileArea);
                    replyBody.appendChild(replyContents);

                    if (reply.memNo == sessionMemNo) {
                        replyBody.innerHTML += `
                                <button id="deleteReplyBtn-${reply.replyNo}" name="deleteReplyBtn" style="display:inline-block" onclick="deleteReply(${reply.replyNo})">삭제</button>
                                <button id="updateReplyBtn-${reply.replyNo}" name="updateReplyBtn" style="display:inline-block" onclick="updateReply(${reply.replyNo})">수정</button>
                            `;
                    }
                    // if (reply.depth == 0) {
                        replyBody.innerHTML += `
                                <div class="btn-area">
                                    <textarea class="reply-contents" style="display:none;"></textarea>
                                    <button class="btn-write-answer-open" id="writeNestedReply-${reply.replyNo}" name="writeNestedReply" style="display:block; margin-left: 1.5rem;">답글쓰기</button>
                                    <button class="do-write" style="display:none; margin-left: 1.5rem;">저장</button>
                                </div>
                                `;
                    // }
                    replyBody.innerHTML += `
                                <button id="saveReplyBtn-${reply.replyNo}" name="saveReplyBtn" style="display:none;" onclick="saveReply(${reply.replyNo})">저장</button>
                                <button id="cancelReplyBtn-${reply.replyNo}" name="cancelReplyBtn" style="display:none;" onclick="cancelReply(${reply.replyNo})">취소</button>
    
                                <!-- 답글 요소 -->
                                <a href="#" class="btn-write-answer" style="display:none;">답글저장</a>
                                <button id="cancelNestedReply-${reply.replyNo}" name="cancelNestedReply" style="display:none;" onclick="cancelNestedReply(${reply.replyNo})">취소</button>
                            `;
                    replysDiv.appendChild(replyBody);
                });
            } else {
                const replyText = document.createElement('div');
                replyText.className = 'no-reply';
                replyText.innerHTML = '작성된 댓글이 없습니다.';
                replysDiv.appendChild(replyText);
            }
        })
        .catch(error => console.error('Error loading replys:', error));
}

// 작성자를 팔로우 하고 있는 상태인지 확인하는 함수
function validateExistingFollow() {

}

// 댓글 작성 함수
document.body?.addEventListener('click', function (e) {
    const writeBtn = e.target.closest('.do-write');

    if (writeBtn) {
        const board = e.target.closest('.board-view');
        const boardNo = board.dataset.boardNo;
        const boardId = board.dataset.boardId;
        const replyBody = writeBtn.closest('.reply-body');
        const parentNo = replyBody?.dataset?.replyNo || null;
        const btnArea = writeBtn.closest('.btn-area');
        const contents = btnArea.querySelector('.reply-contents').value;
        console.log('textArea: ' + contents);
        console.log('parentNo: ' + parentNo);
        console.log('boardId: ' + boardId);
        console.log('boardNo: ' + boardNo);

        writeReply(writeBtn, boardNo, boardId, parentNo, contents);
        return;
    }
});

function writeReply(writeBtn, boardNo, boardId, parentNo, contents) {
    const data = {
        boardNo: boardNo,
        boardId: boardId,
        parentNo: parentNo,
        contents: contents
    };

    fetch(`/reply/${boardId}/doWrite/${boardNo}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            }

            // 2. 실패(400, 500 등)일 경우 에러 메시지를 읽어서 throw
            return response.json().then(err => {
                throw new Error(err.message);
            });
        })

        .catch(error => {
            alert(error.message);
        });
}

// 대댓글 작성/취소 버튼 클릭 시 동작하는 함수 S
document.body?.addEventListener('click', function (e) {
    const btn = e.target.closest('.btn-write-answer-open') || e.target.closest('.btn-write-answer-close');
    if (!btn) return;

    e.preventDefault();

    const container = btn.closest('.reply-body');
    const textarea = container.querySelector('textarea.reply-contents');
    const saveBtn = container.querySelector('button.do-write');

    if (textarea) {
        if (btn.classList.contains('btn-write-answer-open')) {
            // 열기 동작
            textarea.style.display = 'block';
            btn.classList.replace('btn-write-answer-open', 'btn-write-answer-close');
            btn.textContent = '취소';
            saveBtn.style.display = 'block'; // 저장 버튼

        } else {
            // 닫기 동작
            textarea.style.display = 'none';
            btn.classList.replace('btn-write-answer-close', 'btn-write-answer-open');
            btn.textContent = '답글쓰기';
            saveBtn.style.display = 'none'; // 저장 버튼
        }
    }
});

document.body?.addEventListener('click', function (e) {
    const writeAnswerBtn = e.target.closest('.btn-write-answer');

    if (writeAnswerBtn) {
        const board = e.target.closest('.board-view');
        const parentNo = writeAnswerBtn.closest('.reply-body').dataset.replyNo;
        const boardNo = board.dataset.boardNo;
        const boardId = board.dataset.boardId;
        const container = writeAnswerBtn.closest('.reply-body');
        const textarea = container.querySelector('textarea.reply-contents');
        const contents = textarea.value;

        console.log('boardId: ' + boardId);
        console.log('boardNo: ' + boardNo);

        console.log('replyContents: ' + contents);

        console.log(boardNo, boardId, parentNo, contents);

        handleWriteAnswer(writeAnswerBtn, boardNo, boardId, parentNo, contents);
        return;
    }
});

function handleWriteAnswer(writeAnswerBtn, boardNo, boardId, parentNo, contents) {
    const data = {
        boardNo: boardNo,
        boardId: boardId,
        parentNo: parentNo,
        contents: contents
    };

    fetch(`/reply/${boardId}/doWrite/${boardNo}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            }

            // 2. 실패(400, 500 등)일 경우 에러 메시지를 읽어서 throw
            return response.json().then(err => {
                throw new Error(err.message);
            });
        })

        // .then(likeCount => {
        //     likeBtn.getElementsByTagName('span')[1].innerText = likeCount;
        // })

        .catch(error => {
            alert(error.message);
        });
}
// 신규 작성 대댓글 저장 함수 E

// 기존 대댓글 저장 함수 내용
// function saveNestedReply(replyNo) {
//     const contents = document.getElementById('nestedReplyTextArea-' + replyNo).value;
//     if (!contents) {
//         alert("답글 내용을 입력하세요.");
//         return;
//     }
//
//     const boardId = dataset.boardId
//
//     const data = {
//         contents: contents,
//         parentNo: replyNo
//     };
//
//     fetch(`/reply/${boardId}/doWrite/${boardNo}`, {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(data),
//     })
//         .then(() => {
//             getReply();  // 대댓글 목록 새로고침
//             document.getElementById('nestedReplyTextArea').value = '';  // 텍스트박스 비우기
//         })
//         .catch(error => console.error('Error adding reply:', error));
// }

// document.body.addEventListener('click', function (e) {
//     const writeBtn = e.target.closest('.do-write');
//     const replyArea = e.target.closest('.write-reply-area');
//     const contents = replyArea.querySelector('.replyContents').value;
//
//     if (writeBtn) {
//         console.log(contents);
//
//         doWrite(contents, boardNo, boardId);
//     }
//     // 댓글 추가 함수
//     function doWrite(contents, boardNo, boardId) {
//         if (!contents) {
//             alert("댓글을 입력하세요.");
//             return;
//         }
//
//         const data = {
//             contents: contents
//         };
//
//         console.log('contents: ' + data.contents);
//
//         fetch(`/reply/${boardId}/doWrite/${boardNo}`, {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json',
//             },
//             body: JSON.stringify(data),
//         })
//             .then(() => {
//                 getReply();  // 댓글 목록 새로고침
//                 document.getElementById('replyContents').value = '';  // 텍스트박스 비우기
//             })
//             .catch(error => console.error('Error adding reply:', error));
// }});

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

// 답글 버튼 클릭 시(신)
// document.body?.addEventListener('click', function (e) {
//     const writeAnswerReplyBtn = e.target.closest('.btn-write-answer-reply');
//     const replyTextarea = writeAnswerReplyBtn.closest('.reply-textarea');
//
//     if (writeAnswerReplyBtn) {
//         console.log('답글 작성 버튼 클릭');
//         replyTextarea.style.display = 'block';
//     }
// })


// 답글 버튼 클릭 시(구)
// function writeNestedReply(replyNo) {
//     // const replyText = document.getElementById('reply-' + replyNo);
//
//     const nestedReplyTextArea = document.getElementById('nestedReplyTextArea-' + replyNo);
//     const saveNestedReply = document.getElementById('saveNestedReply-' + replyNo);
//     const cancelNestedReply = document.getElementById('cancelNestedReply-' + replyNo);
//     const writeNestedReply = document.getElementById('writeNestedReply-' + replyNo);
//
//     // textarea를 보여줌
//     nestedReplyTextArea.style.display = 'block';
//     // 답글 버튼은 숨기고 답글저장, 취소 버튼을 보여줌
//     writeNestedReply.style.display = 'none';
//     // saveNestedReply.style.display = 'block';
//     cancelNestedReply.style.display = 'block';
// }

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

// 페이지 로드 시 실행할 함수
// window.onload = function () {
//     getReply(); // 댓글 목록 불러오기
//     validateExistingFollow(); // 작성자를 팔로우하고 있는 상태인지 확인
// }


// 게시글 삭제 S
document.getElementById('doDelete')?.addEventListener('click', function () {
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

// 새로운 팔로우 하기
document.body.addEventListener('click', function(event) {
    if (event.target.classList.contains('follow-btn')) {
        if (confirm(document.getElementById('writerNm').value + '님을 팔로우하시겠습니까?')) {
            const followButton = event.target.closest('.follow-btn');
            const boardId = followButton.dataset['boardId'];
            const boardNo = followButton.dataset['boardNo'];
            const data = {
                boardId: boardId,
                boardNo: boardNo,
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
                        followButton.innerText = followButton.innerText.replace('팔로우하기', '팔로우 중');
                        followButton.classList.remove('follow-btn');
                        followButton.classList.add('follow-cancel-btn');
                    } else {
                        alert('팔로우 실패하였습니다.');
                    }
                })
                .catch(error => console.error("Error:", error));
        } else {
            return false;
        }
    }
});

// 새로운 팔로우 취소하기
document.body.addEventListener('click', function(event) {
    if (event.target.classList.contains('follow-cancel-btn')) {
        if (confirm(document.getElementById('writerNm').value + '님을 팔로우 취소 하시겠습니까?')) {
            const followButton = event.target.closest('.follow-cancel-btn');
            const boardId = followButton.dataset['boardId'];
            const boardNo = followButton.dataset['boardNo'];
            const data = {
                boardId: boardId,
                boardNo: boardNo,
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
                        followButton.innerText = followButton.innerText.replace('팔로우 중', '팔로우하기');
                        followButton.classList.remove('follow-cancel-btn');
                        followButton.classList.add('follow-btn');
                    } else {
                        alert('팔로우 취소에 실패하였습니다.');
                    }
                })
                .catch(error => console.error("Error:", error));
        } else {
            return false;
        }
    }
});

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
