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