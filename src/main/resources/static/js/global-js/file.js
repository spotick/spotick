const fileService = (function () {

    /*
    * 파일의 용량을 체크하도록 하는 함수 : 용량 초과시 false반환 / 정상시 callback실행
    *
    * formData : files 프로퍼티를 formData로 전달.
    * sizeRestriction : 파일이 특정 용량을 초과하는지 체크하는 척도제공
    * callback : 용량검증을 마친 formData를 매개변수로 실행하게될 함수*/
    function checkFileSize(formData, sizeRestriction, callback) {
        if (formData.size > sizeRestriction) {
            return false;
        }

        if (callback) {
            callback(formData);
        }
    }

    /*
    * 파일등록을 요청하는 함수 : 오류 발생시 error반환 / 정상시 uuid 리스트 반환
    *
    * formData : files 프로퍼티를 formData로 전달*/
    function requestUpload(formData) {
        fetch('/file/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('파일 업로드 실패');
                }
                return response.json();
            })
            .then(uuids => {
                return uuids;
            })
            .catch(error => {
                console.error('파일 업로드 에러:', error);
                return error;
            });
    }

    return {
        checkFileSize: checkFileSize,
        requestUpload: requestUpload
    }
})();