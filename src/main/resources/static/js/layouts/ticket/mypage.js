export const mypageTicketLayout = (() => {

    const showGradeInfo = (data) => {
        let html = '';

        data.forEach(grade => {
            html += `
                <tr>
                    <td>${grade.gradeName}</td>
                    <td>${grade.price.toLocaleString()}</td>
                    <td>${grade.sold}</td>
                    <td>${grade.maxPeople}</td>
                </tr>`;
        });

        return html;
    }

    return {
        showGradeInfo: showGradeInfo,

    }
})();