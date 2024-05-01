function toggleDropdown(button) {
    let dropdown = button.querySelector('.mpc-dropdown');
    dropdown.classList.toggle('show');
}

const topNavigationButtons = document.querySelectorAll('.mpc-top-nav-button');


topNavigationButtons.forEach(button => {
    button.addEventListener('click', () => {
        topNavigationButtons.forEach(btn => {
            btn.classList.remove('active');
        });

        button.classList.add('active');
    });
});

function vibrateTarget(target) {
    target.classList.add('vibration');

    setTimeout(function() {
        target.classList.remove("vibration");
    }, 200);
}

function dateDifferenceInDays(date1, date2) {
    const oneDay = 24 * 60 * 60 * 1000;

    const timeDifference = Math.abs(date2.getTime() - date1.getTime());

    return Math.round(timeDifference / oneDay);
}

function formatDate(date) {

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}