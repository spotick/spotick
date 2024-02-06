const img = document.getElementById('detailProfileImg');
const nickname = document.getElementById('detailNickName');
const content = document.getElementById('detailContent');

const inquiryIdInput = document.getElementById('inquiryId');
const placeIdInput = document.getElementById('placeId');
const responseInput = document.getElementById('response');

const errorContent = document.querySelector('.error-content');

function uploadResponse() {
    let inquiryId = inquiryIdInput.value;
    let placeId = placeIdInput.value;
    let responseString = responseInput.value;


    showGlobalSelection("답변을 등록하시겠습니까?", () => requestUpload(inquiryId, placeId, responseString))
}

function requestUpload(inquiryId, placeId, responseString) {
    closeOnlyThisModal(globalSelection);

    const inquiryResponse = {
        inquiryId : inquiryId,
        placeId : placeId,
        response : responseString
    }

    fetch('/places/inquiry/response', {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(inquiryResponse)
    })
        .then(response => {
            if (response.ok) {
                alert("답변이 등록되었습니다.");
                window.location.reload();
            } else {
                throw response
            }
        })
        .catch(error => {
            console.error('Error:', error);

            error.text().then(data => {

                vibrateTarget(modalPlace);

                errorContent.innerHTML = data;
            })
        });
}

/////////////////////////////////////////////////////////////

document.querySelectorAll('.detailInquiryBtn').forEach(inquiryBtn => {
    inquiryBtn.addEventListener('click', function () {
        img.src = this.getAttribute('data-img');
        nickname.innerHTML = this.getAttribute('data-nickname');
        content.value = this.getAttribute('data-content');
        inquiryIdInput.value = this.getAttribute('data-id');
        placeIdInput.value = this.getAttribute('data-place-id');

        responseInput.value = '';
        errorContent.innerHTML = '';

        openModal(modalPlace);
    });
});

document.getElementById('requestBtn').addEventListener('click', function () {
    uploadResponse();
})


