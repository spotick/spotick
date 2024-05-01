export const paginationLayout = (pagination) => {
    const {hasPrevBlock, hasNextBlock, startPage, endPage, lastPage, currentPage} = pagination;
    let html = "";

    if (hasPrevBlock) {
        html +=
            `<span class="pagination-previous">
                <a class="pagination-btns first" title="맨 처음" page="1">
                    <i class="fa-solid fa-chevron-left"></i>
                    <i class="fa-solid fa-chevron-left"></i>
                </a>
                <a class="pagination-btns" title="이전" page="${startPage - 1}">
                    <i class="fa-solid fa-chevron-left"></i>
                </a>
            </span>`;
    }

    html += "<span class=\"pagination-body\">";
    for (let i = startPage; i <= endPage; i++) {

        html +=
            `<span class="pagination-btns" page="${i}">
                <a class="${i === currentPage ? 'active' : ''}">
                   ${i}
                </a>
            </span>`;

    }
    html += "</span>";

    if (hasNextBlock) {
        html +=
            `<span class="pagination-next">
                <a class="pagination-btns" title="다음" page="${endPage + 1}">
                    <i class="fa-solid fa-chevron-right"></i>
                </a>
                <a class="pagination-btns end" title="맨 끝" page="${lastPage}">
                    <i class="fa-solid fa-chevron-right"></i>
                    <i class="fa-solid fa-chevron-right"></i>
                </a>
            </span>`;
    }

    return html;
}