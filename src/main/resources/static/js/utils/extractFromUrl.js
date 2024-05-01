// http://localhost:10000/mypage/places/inquiries/100 를 예시로 현재 링크에서 맨 끝자리의 변수를 찾아내서 반납해주는 함수
// 링크에서 숫자를 찾아낼 시 String 반환 <> 없을시 null반환
export function extractVariableFromURL() {
    let variableValue = window.location.href.match(/\/(\d+)$/);

    return variableValue ? variableValue[1] : null;
}