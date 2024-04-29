export const mypageService = (() => {

    const updateDefaultImage = async (image) => {
        const response = await fetch(`/mypage/api/updateDefaultImg?imgName=${image}`, {
            method: 'PATCH'
        });
        return await response.json();
    }

    const updatePersonalImage = async (file) => {
        const formData = new FormData();
        formData.append('uploadFile', file);

        const response = await fetch(`/mypage/api/updatePersonalImg`, {
            method: 'PATCH',
            body: formData
        });

        return await response.json();
    }

    const updateNickname = async (nickname) => {
        const response = await fetch(`/mypage/api/updateNickName?nickname=${nickname}`, {
            method: 'PATCH'
        })

        return await response.json();
    }

    const startTelVerification = async (tel) => {
        const response = await fetch(`/mypage/api/authenticateTelStart?tel=${tel}`);

        return response.ok;
    }

    const updateTel = async (tel, code) => {
        const response = await fetch(`/mypage/api/authenticateTel?tel=${tel}&code=${code}`, {
            method: 'PATCH'
        });

        return await response.json();
    }

    const updatePassword = async (password) => {
        const response = await fetch(`/mypage/api/changePassword?password=${password}`, {
            method: 'PATCH'
        });

        return await response.json();
    }

    return {
        updateDefaultImage: updateDefaultImage,
        updatePersonalImage: updatePersonalImage,
        updateNickname: updateNickname,
        startTelVerification: startTelVerification,
        updateTel: updateTel,
        updatePassword: updatePassword,
    }
})();